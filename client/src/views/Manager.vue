<template>
  <div class="manager-view">
    <el-tabs v-model="activeTab"
             type="card">
      <el-tab-pane label="书籍管理"
                   name="books">
        <book-manage></book-manage>
      </el-tab-pane>
      <el-tab-pane label="订单管理"
                   name="orders">
        <order-manage></order-manage>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import BookManage from "../components/BookManage.vue"
import OrderManage from "../components/OrderManage.vue"
export default {
  name: 'manager',
  data () {
    return {
      activeTab: 'books'
    }
  },
  components: {
    BookManage,
    OrderManage
  }
}
</script>

<style scoped>
.manager-view {
  margin: 30px;
}
</style>
