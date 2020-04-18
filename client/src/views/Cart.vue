<template>
  <div style="margin: 20px">
    <el-table :data="tableData"
              border
              stripe>
      <el-table-column label="商品信息">
        <template slot-scope="scope">
          <img src="../assets/bookcover.webp"
               alt="bookcover"
               width="120px">
          <span class="book-name">{{scope.row.name}}</span>
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
            {{scope.row.count*scope.row.price}}
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
      <span>总价：{{totalPrice}}</span>
      <el-button type="warning"
                 @click="dialog2Visible = true"
                 style="margin-left: 10px">
        清空购物车
      </el-button>
      <el-button type="danger"
                 @click="jumpCommit"
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
    jumpCommit () {
      window.location.href = "#/commit-orders"
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
