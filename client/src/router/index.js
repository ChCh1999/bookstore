import Vue from 'vue'
import VueRouter from 'vue-router'
import Store from '../views/Store.vue'
import Cart from '../views/Cart.vue'
import CommitOrders from '../views/CommitOrders.vue'
import UserInfo from '../views/UserInfo.vue'
import Login from "../views/Login.vue"

import store from "../store/index.js"

Vue.use(VueRouter);

const routes = [
  {
    path: "/store",
    name: "Store",
    component: Store
  },
  {
    // 初始页重定向到商城
    path: "/",
    redirect: "/store",
    component: Store
  },
  {
    path: "/cart",
    component: Cart,
    meta: {
      requireAuth: true
    }
  },
  {
    path: "/commit-orders",
    component: CommitOrders,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/user-info',
    component: UserInfo
  },
  {
    path: '/login',
    component: Login
  }
];

const router = new VueRouter({
  routes
});
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    // 判断该路由是否需要登录权限
    if (store.getters.ifLogin) {
      // 通过vuex state获取是否登录
      next();
    } else {
      alert("请先登录")
      next({
        path: "/login",
        // 将跳转的路由path作为参数，登录成功后跳转到该路由
        query: { redirect: to.fullPath }
      });
    }
  } else {
    next();
  }
});
export default router;
