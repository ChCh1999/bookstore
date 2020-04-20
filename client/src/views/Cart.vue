<template>
  <div style="margin: 20px">
    <el-table :data="tableData"
              border
              stripe>
      <el-table-column label="商品信息">
        <template slot-scope="scope">
          <img :src="scope.row.imgData"
               alt="bookcover"
               width="120px">
          <div class="book-name">{{scope.row.name}}</div>
        </template>
      </el-table-column>
      <el-table-column label="数量">
        <template slot-scope="scope">
          <el-input-number v-model="scope.row.count"
                           :min="1"
                           size="mini" />
        </template>
      </el-table-column>
      <el-table-column label="单价"
                       prop="price">
      </el-table-column>
      <el-table-column label="总价">
        <template slot-scope="scope">
          <div>
            {{(scope.row.count*scope.row.price).toFixed(2)}}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="danger"
                     @click="showDialog(scope.$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialog1Visible"
               width="30%">
      <span>确定删除吗？</span>
      <span slot="footer">
        <el-button @click="dialog1Visible = false">取 消</el-button>
        <el-button type="danger"
                   @click="deleteOrder(indexToDelete)">确 定</el-button>
      </span>
    </el-dialog>
    <div class="commit-orders">
      <span>总价：{{totalPrice.toFixed(2)}}</span>
      <el-button type="warning"
                 @click="dialog2Visible = true"
                 style="margin-left: 10px">
        清空购物车
      </el-button>
      <el-button type="danger"
                 @click="commit"
                 style="margin-left: 10px;">提交订单
      </el-button>
    </div>
    <el-dialog :visible.sync="dialog2Visible"
               width="30%">
      确定清空购物车吗？
      <span slot="footer">
        <el-button @click="dialog2Visible = false">取 消</el-button>
        <el-button type="danger"
                   @click="clearCart">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: 'Cart',
  data () {
    return {
      dialog1Visible: false,
      dialog2Visible: false,
      indexToDelete: 0
    }
  },
  computed: {
    ...mapState({
      tableData: state => state.Orders.bookOrders
    }),
    totalPrice () {
      return this.$store.getters.totalPrice
    }
  },
  methods: {
    commit () {
      this.tableData.forEach(order => {
        console.log("committing order, bookid: " + order.id + ", count: " + order.count)
        this.axios.post("/server/order/addbook", {
          bookid: order.id,
          count: order.count
        }).then(response => {
          console.log(response)
        }).catch(error => {
          console.log(error)
        })
      });
    },
    showDialog (index) {
      this.dialog1Visible = true
      this.indexToDelete = index
    },
    deleteOrder (index) {
      this.$store.commit('deleteOrder', index)
      this.dialog1Visible = false
    },
    clearCart () {
      this.$store.commit('clearCart')
      this.dialog2Visible = false
    }
  }
}
</script>

<style scoped>
.book-name {
  word-break: normal;
}

.commit-orders {
  margin-top: 10px;
  text-align: end;
}
</style>
