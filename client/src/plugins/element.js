import Vue from 'vue'
import {Upload, Row, Col, Message, Button, Card, Pagination, InputNumber, Container, Table, TableColumn, Dialog, Badge, Input, Form, FormItem, Tabs, TabPane } from 'element-ui'

Vue.use(Button)
Vue.use(Card)
Vue.use(Pagination)
Vue.use(InputNumber)
Vue.use(Container)
Vue.use(Row)
Vue.use(Col)
Vue.use(Table)
Vue.use(TableColumn)
Vue.use(Dialog)
Vue.use(Badge)
Vue.use(Input)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Tabs)
Vue.use(TabPane)
Vue.use(Upload)
Message.install = function (Vue, options) {
  Vue.prototype.$message = Message
}
Vue.use(Message)
