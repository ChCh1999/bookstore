import Vue from 'vue'
import './plugins/bootstrap-vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import store from './store'
import axios from "axios"
import qs from "qs"
import "../vue.config"
import VueCookies from 'vue-cookies'

// axios.defaults.baseURL = "http://localhost:8080"
axios.defaults.headers.post['Content-Type'] = 'application/json'
axios.defaults.withCredentials = true
Vue.use(VueCookies)
Vue.config.productionTip = false
Vue.prototype.axios = axios
Vue.prototype.qs = qs

new Vue({
  router,
  store,
  axios,
  render: h => h(App)
}).$mount('#app')
