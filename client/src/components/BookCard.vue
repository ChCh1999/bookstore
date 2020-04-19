<template>
<el-col :xs="24" :sm="12" :md="8" >
  <el-card class="box-card">
    <img src="../assets/bookcover.webp"
         :alt="name"
         class="book-img" />
    <div class="book-title">{{name}}</div>
    <div class="book-author">作者：{{author}}</div>
    <hr />
    <div class="book-desc">简介：{{desc}}</div>
    <div class="cart">
      <div class="book-price">{{price}}￥</div>
      <el-input-number v-model="count"
                       :min="1"
                       size="mini"
                       style="margin-bottom: 5px" />
      <br>
      <el-button type="danger">添加购物车</el-button>
    </div>
  </el-card>
</el-col>
</template>

<script>
export default {
  name: 'BookCard',
  props: {
    name: String,
    author: String,
    desc: String,
    price: Number
  },
  data () {
    return {
      count: 1
    }
  }
}
</script>

<style scoped>
img {
  width: 120px;
  height: auto;
  margin: 5px;
}

hr {
  margin-left: 130px;
}

.book-img {
  float: left;
}

.book-title {
  font-size: 30px;
  font-weight: bold;
  text-align: start;
}

.book-author {
  font-size: 15px;
  text-align: start;
}

.book-desc {
  padding: 5px;
  font-size: 18px;
  text-align: start;
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}

.box-card {
  margin: 30px;
}

.book-price {
  color: #F56C6C;
  font-style: bold;
  text-shadow: 1px 1px 1px #606266;
  font-size: 40px;
  float: left;
}

.cart {
  width: fit-content;
  margin: auto;
}
</style>
