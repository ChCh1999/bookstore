import Vue from 'vue'
import VueRouter from 'vue-router'
import Store from '../views/Store.vue'
import Cart from '../views/Cart.vue'
import CommitOrders from '../views/CommitOrders.vue'
import UserInfo from '../views/UserInfo.vue'
import Login from "../views/Login.vue"
import Manager from "../views/Manager.vue"

Vue.use(VueRouter)

const routes = [
  {
    path: '/store',
    name: 'Store',
    component: Store
  },
  {
    // 初始页重定向到商城
    path: '/',
    redirect: '/store',
    component: Store
  },
  {
    path: '/cart',
    component: Cart
  },
  {
    path: '/commit-orders',
    component: CommitOrders
  },
  {
    path: '/user-info',
    component: UserInfo
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/manager',
    component: Manager
  }
]

const router = new VueRouter({
  routes
})

export default router
