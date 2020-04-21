 <template>
  <div>
    <el-input v-model="filterQuery.orderId"
              placeholder="订单号..."
              style="width: 150px;" />
    <el-input v-model="filterQuery.userId"
              placeholder="用户id..."
              style="width: 150px;"
              class="filter-item" />
    <el-button class="filter-item"
               type="primary">搜索</el-button>
    <el-table :data="tableData.slice(
      (currentPage - 1) * filterQuery.pageSize,
      currentPage * filterQuery.pageSize
    )"
              border
              stripe
              style="width: 100%">
      <el-table-column type="index"
                       label="序号"
                       width="50"></el-table-column>
      <el-table-column prop="id"
                       label="订单id"></el-table-column>
      <el-table-column prop="userAccount"
                       label="用户id"></el-table-column>
      <el-table-column prop="bookInfo.name"
                       label="书籍名称"></el-table-column>
      <el-table-column prop="count"
                       label="购买数量"></el-table-column>
      <el-table-column prop="bookInfo.price"
                       label="单价"></el-table-column>
      <el-table-column label="总金额">
        <template slot-scope="scope">
          {{(scope.row.bookInfo.price * scope.row.count).toFixed(2)}}
        </template>
      </el-table-column>
      <el-table-column align="center"
                       prop="created_at"
                       label="操作">
        <template slot-scope="scope">
          <el-button type="danger"
                     size="small"
                     @click="deleteOrder(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="float:right;margin-top:30px;">
      <el-pagination background
                     :current-page="currentPage"
                     :page-sizes="[10, 20, 30, 40]"
                     :page-size="filterQuery.pageSize"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="tableData.length"
                     @size-change="handleSizeChange"
                     @current-change="handleCurrentChange" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'orders-manage',
  data () {
    return {
      tableData: [],
      currentPage: 1,
      filterQuery: {
        pageNum: 1,
        pageSize: 10
      },
      form: {},
      imageUrl: "",
      dialogFormVisible: false,
      dialogStatus: "",
      formLabelWidth: "120px",
      imgLabelWidth: "120px"
    }
  },
  methods: {
    handleSizeChange (val) {
      this.filterQuery.pageSize = val;
      this.getList();
    },
    handleCurrentChange (val) {
      this.filterQuery.pageNum = val;
      this.getList();
    },
    deleteOrder (orderId) {
      console.log("Deleting orderid: " + orderId)
      this.axios.post("server/order/delete", {
        orderid: orderId
      })
        .then(response => {
          if (response.status === 200) {
            this.$options.methods.refresh(this)
            this.$message({
              message: "删除成功",
              showClose: true,
              type: "success"
            })
          } else {
            this.$message.error({
              message: "删除失败",
              showClose: true
            })
          }
        })
        .catch(error => {
          console.log(error)
        })
    },
    refresh (vueComponent) {
      console.log("refreshing")
      console.log(vueComponent)
      vueComponent.axios.get("server/order/all").then(response => {
        vueComponent.tableData = response.data
      })
        .catch(error => {
          console.log(error)
        })
    }
  },
  mounted () {
    console.log("ordermanag mounted")
    console.log(this)
    this.axios.get("server/order/all").then(response => {
      this.tableData = response.data
    })
      .catch(error => {
        console.log(error)
      })
  }
}
</script>
