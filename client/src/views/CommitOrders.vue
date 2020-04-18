<template>
  <div>
    <div style="text-align: start; margin: 10px;">商品列表：</div>
    <div style="margin: 20px">
      <el-table :data="tableData"
                stripe
                size="small">
        <el-table-column label="书名"
                         prop="name">
        </el-table-column>
        <el-table-column prop="count"
                         label="数量">
        </el-table-column>
        <el-table-column label="总价">
          <template slot-scope="scope">
            <div>
              {{scope.row.price * scope.row.count}}
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="margin-top: 10px">总价：{{totalPrice}}</div>
    <div style="margin-top: 10px">
      <el-row align="middle"
              type="flex">
        <el-col :span="3"
                :offset="5">收货地址：</el-col>
        <el-col :span="10">
          <el-input v-model="diliverAddress"
                    placeholder="请输入收货地址"
                    type="textarea"
                    autosize></el-input>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 10px;">
      <el-button type="danger"
                 @click="dialogVisible = true">
        确认提交
      </el-button>
    </div>
    <el-dialog :visible.sync="dialogVisible"
               width="30%"
               title="确认提交订单？">
      总价：{{totalPrice}}<br>
      收货地址：{{diliverAddress}}
      <span slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="danger"
                   @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: 'CommitOrders',

  data () {
    return {
      diliverAddress: '',
      dialogVisible: false
    }
  },

  computed: {
    ...mapState({
      tableData: state => state.Orders.bookOrders
    }),

    totalPrice () {
      return this.$store.getters.totalPrice
    }
  }
}
</script>

<style scoped>
.el-talbe-column {
  width: fit-content;
}
</style>
