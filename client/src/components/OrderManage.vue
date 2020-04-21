 <template>
  <div>
    <el-table :data="tableData.slice(
      (currentPage - 1) * 10,
      currentPage * 10
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
                     :page-size="10"
                     :current-page="currentPage"
                     @current-change="changePage"
                     layout="total, prev, pager, next, jumper"
                     :total="tableData.length" />
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
      form: {},
      dialogFormVisible: false,
      formLabelWidth: "120px",
      imgLabelWidth: "120px"
    }
  },
  methods: {
    changePage (page) {
      this.currentPage = page
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
