<template>
  <el-col :xs="24"
          :sm="12"
          :md="12"
          :lg="8">
    <el-card class="box-card">
      <img :src="imgData"
           :alt="name"
           class="book-img" />
      <div class="book-title">{{ name }}</div>
      <div class="book-author">出版社：{{ author }}</div>
      <hr />
      <div class="book-desc">简介：{{ desc }}</div>
      <div class="cart">
        <div class="book-price">{{ price }}￥</div>
        <el-input-number v-model="count"
                         :min="1"
                         size="mini"
                         style="margin-bottom: 5px" />
        <br />
        <el-button type="danger"
                   @click="handleAddOrder">添加购物车</el-button>
      </div>
    </el-card>
  </el-col>
</template>

<script>
export default {
  props: {
    id: Number,
    name: String,
    author: String,
    desc: String,
    price: Number,
    imgPath: String
  },
  data () {
    return {
      count: 1,
      imgData: String
    };
  },
  mounted () {
    this.axios
      .get("/server/file/get", {
        params: {
          fileName: this.imgPath
        }
      })
      .then(response => {
        var imgString = response.data.imgData;
        this.imgData = "data:image/jpg;base64," + imgString;
      })
      .catch(error => {
        console.log(error);
      });
  },
  methods: {
    handleAddOrder () {
      console.log("name" + this.name)
      var order = {
        id: this.id,
        imgData: this.imgData,
        name: this.name,
        count: this.count,
        price: this.price
      }
      this.$store.commit('addOrder', order)
      this.$message({
        message: '添加成功！',
        type: 'success',
        showClose: true
      })
    }
  }
};
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
  color: #f56c6c;
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
