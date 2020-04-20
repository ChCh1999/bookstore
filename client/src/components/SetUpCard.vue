<template>
  <el-card class="loginCard">
    <p id="#UserSignUp" >用户注册</p>
    <el-form
      label-position="left"
      label-width= "70px"
      style="position: relative;top: 20px;"

    >
      <el-form-item label="用户名" prop="name">
        <el-input
          v-model="this.name"
          placeholder="用户名"
        ></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="this.password"
          placeholder="密码"
        ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmpwd">
        <el-input
          v-model="this.password"
          placeholder="确认密码"
        ></el-input>
      </el-form-item>
      <el-button type="danger" class="loginButton">注册</el-button>
      <p></p>
      <el-button id="ReturnButton" type="warning" long @click="toLogin"
        >返回登录</el-button
      >
    </el-form>
  </el-card>
</template>

<script>
export default {
  methods: {
    toLogin: function() {
      this.$emit("listen", "loginCard");
    }
  }
};
</script>

<style>
#UserSignUp {
  margin-top: 30px;
  text-align: center;
  font-size: 20px;
}
#ReturnButton {
   position: relative;
  top: 60px;
  width: 80%;
}
</style>
