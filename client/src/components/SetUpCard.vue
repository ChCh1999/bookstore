<template>
  <el-card class="loginCard">
    <p id="#UserSignUp">用户注册</p>
    <el-form
      style="position: relative;top: 20px;"
      :model="SignupForm"
      ref="SignupForm"
      label-width="70px"
      label-position="left"
      :rules="rules"
    >
      <el-form-item label="账号" prop="account">
        <el-input v-model="SignupForm.account" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="pass">
        <el-input v-model="SignupForm.pass"  type="password" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="pass2">
        <el-input v-model="SignupForm.pass2"  type="password" placeholder="确认密码"></el-input>
      </el-form-item>
      <el-button type="danger" class="loginButton" @click="register"
        >注册</el-button
      >
      <p></p>
      <el-button id="ReturnButton" type="warning" long @click="toLogin"
        >返回登录</el-button
      >
    </el-form>
  </el-card>
</template>

<script>
export default {
  data() {
    // 检查账号
    var checkAccount = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("账号不能为空"));
      } else {
        if (this.SignupForm.account !== "") {
          this.$refs.SignupForm.validateField("checkPass");
        }
        callback();
      }
    };
    //检查密码
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.SignupForm.checkPass !== "") {
          this.$refs.SignupForm.validateField("checkPass");
        }
        callback();
      }
    };
    //检查密码
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次密码"));
      } else if (value !== this.SignupForm.pass) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        if (this.SignupForm.checkPass !== "") {
          this.$refs.SignupForm.validateField("checkPass");
        }
        callback();
      }
    };
    return {
      SignupForm: {
        pass: "",
        pass2: "",
        account: ""
      },
      rules: {
        account: [{ validator: checkAccount, trigger: "blur" }],
        pass: [{ validator: validatePass, trigger: "blur" }],
        pass2: [{ validator: validatePass2, trigger: "blur" }]
      }
    };
  },
  methods: {
    toLogin: function() {
      this.$emit("listen", "loginCard");
    },
    register: function() {
      var cur = this;
      console.log("try register");
      this.$refs.SignupForm.validate(valid => {
        if (valid) {
          console.log(cur.SignupForm.account, cur.SignupForm.pass);
          this.axios
            .post("/server/user/register", {
              account: cur.SignupForm.account,
              password: cur.SignupForm.pass
            })
            .then(response => {
              console.log('login response', response);
              if (response.status === 200) {
                // console.log(cur.SignupForm.account)
                cur.$store.commit("successLogin", cur.SignupForm.account);
                cur.$router.push({ path: "/store" });
              } else {
                alert("注册失败，账号已被占用");
              }
            })
            .catch(error => {
              console.log(error);
            });
        } else {
          console.log("error submit!!");
        }
      });
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
