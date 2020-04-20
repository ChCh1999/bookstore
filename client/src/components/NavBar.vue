<template>
  <div>
    <nav class="navbar navbar-light border-bottom mt-1">
      <a class="navbar-brand"
         href="#/store">BookStore</a>
      <ul class="nav nav-fill nav-pills">
        <li class="nav-item">
          <a class="nav-link"
             :class="($route.path==='/store')? 'active' : 'text-muted'"
             href="#/store">商城</a>
        </li>
        <li class="nav-item">
          <el-badge :value="totalCount">
            <a class="nav-link"
               :class="($route.path==='/cart')? 'active': 'text-muted'"
               href="#/cart">购物车</a>
          </el-badge>
        </li>
      </ul>
      <a href="#/login" v-if='!ifLogin'>
        <!-- <span></span> -->
        <i class="iconfont icon-denglu" herf="#/login"></i>
        登录/注册
      </a>
      <a href="#/store" v-else>
        <!-- <span></span> -->
        <!-- <i class="iconfont icon-denglu" herf="#/login"></i> -->
        已登录用户：
        {{userAccount}}
      </a>
    </nav>
  </div>
</template>

<script>
export default {
  name: 'NavBar',
  computed: {
    totalCount () {
      return this.$store.getters.totalOrderCount
    },
    ifLogin () {
      console.log('ifLogin', this.$store.getters.ifLogin)
      return this.$store.getters.ifLogin
    },
    userAccount () {
      return this.$store.getters.userAccount
    }
  }
}
</script>

<style scoped>
</style>
