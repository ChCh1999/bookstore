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
    <el-table :data="tableData"
              border
              stripe
              style="width: 100%">
      <el-table-column type="index"
                       label="序号"
                       width="50"></el-table-column>
      <el-table-column prop="orderId"
                       label="订单id"></el-table-column>
      <el-table-column prop="userId"
                       label="用户id"></el-table-column>
      <el-table-column prop="bookName"
                       label="书籍名称"></el-table-column>
      <el-table-column prop="bookSellNum"
                       label="购买数量"></el-table-column>
      <el-table-column prop="bookPri"
                       label="单价"></el-table-column>
      <el-table-column prop="totalPri"
                       label="总金额"></el-table-column>
      <el-table-column prop="orderDate"
                       label="下单时间"></el-table-column>
      <el-table-column align="center"
                       prop="created_at"
                       label="操作">
        <template>
          <el-button type="danger"
                     size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="float:right;margin-top:30px;">
      <el-pagination background
                     :current-page="currentPage"
                     :page-sizes="[10, 20, 30, 40]"
                     :page-size="10"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="total"
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
      tableData: [
        {
          orderId: 'xxx184452222',
          userId: "001",
          bookName: "Java开发",
          orderDate: "2020-04-18",
          bookSellNum: "20",
          bookPri: "￥50",
          totalPri: "￥100",
          bookPublish: "清华大学出版社",
          bookPagination: "1000页"
        },
        {
          orderId: 'xxx184452223',
          userId: "002",
          bookName: "Web开发",
          orderDate: "2020-04-18",
          bookSellNum: "20",
          bookPri: "￥50",
          totalPri: "￥100",
          bookPublish: "清华大学出版社",
          bookPagination: "1000页"
        },
        {
          orderId: 'xxx184452224',
          userId: "003",
          bookName: "C#开发",
          orderDate: "2020-04-18",
          bookSellNum: "20",
          bookPri: "￥50",
          totalPri: "￥100",
          bookPublish: "清华大学出版社",
          bookPagination: "1000页"
        },
        {
          orderId: 'xxx184452225',
          userId: "004",
          bookName: "NodeJs开发",
          orderDate: "2020-04-18",
          bookSellNum: "20",
          bookPri: "￥50",
          totalPri: "￥100",
          bookPublish: "清华大学出版社",
          bookPagination: "1000页"
        }
      ],
      total: 4,
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
    }

  }
}
</script>
