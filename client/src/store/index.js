import Vue from 'vue'
import Vuex from 'vuex'
import Books from './modules/Books.js'
import Orders from './modules/Orders.js'
import Login from './modules/login.js'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    Books,
    Orders,
    Login
  }
})
