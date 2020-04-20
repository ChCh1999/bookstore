<template>
  <el-card class="loginCard">
    <p id="UserLogin" style="text-align: center">用户登录</p>
    <el-form
      style="position: relative;top: 20px;"
      :model="loginForm"
      ref="loginForm"
      label-width="40px"
      :rules="rules"
    >
      <el-form-item label="账号" prop="account">
        <el-input
          v-model="loginForm.account"
          placeholder="请输入账号"
          class="inputBox"
        ></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="pass">
        <el-input
          v-model="loginForm.pass"
          type="password"
          placeholder="请输入密码"
          class="inputBox pwdBox"
        ></el-input>
      </el-form-item>
      <el-button type="warning" class="loginButton" @click="login"
        >登录</el-button
      >
      <p></p>
      <el-button type="danger" @click="toSetUp" class="signupButton"
        >注册</el-button
      >
    </el-form>
  </el-card>
</template>

<script>
export default {
  props: {
    componentName: {
      type: String,
      default: () => {
        return "";
      }
    }
  },
  data() {
    // 检查账号
    var checkAccount = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("账号不能为空"));
      } else {
        if (this.loginForm.account !== "") {
          this.$refs.loginForm.validateField("checkPass");
        }
        callback();
      }
    };
    //检查密码
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.loginForm.checkPass !== "") {
          this.$refs.loginForm.validateField("checkPass");
        }
        callback();
      }
    };
    return {
      loginForm: {
        pass: "",
        account: ""
      },
      rules: {
        account: [{ validator: checkAccount, trigger: "blur" }],
        pass: [{ validator: validatePass, trigger: "blur" }]
      }
    };
  },
  methods: {
    toSetUp: function() {
      this.$emit("listen", "setUpCard");
    },
    login: function() {
      var cur = this;
      console.log("try login");
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          // console.log(cur.loginForm.account, cur.loginForm.pass);
          this.axios
            .post("/server/user/login", {
              account: cur.loginForm.account,
              password: cur.loginForm.pass
            })
            .then(response => {
              console.log('login response', response);
              if (response.status === 200) {
                // console.log(cur.loginForm.account)
                cur.$store.commit("successLogin", cur.loginForm.account);
                cur.$router.push({ path: "/store" });
              } else {
                alert("账号或密码错误");
              }
            })
            .catch(error => {
              console.log(error);
            });
        } else {
          console.log("error submit!!");
        }
      });
    },
    toGetBackPassword: function() {
      this.$emit("listen", "getBackPassword");
    }
  }
};
</script>

<style>
#UserLogin {
  margin-top: 30px;
  text-align: center;
  font-size: 20px;
}
.loginCard {
  /*background-color: rgba(113, 202, 247, 0.877);*/
  width: 400px;
  height: 500px;
  position: relative;
  left: 65%;
  margin-top: 60px;
  opacity: 0.9;
}
.loginButton {
  text-align: center;
  position: relative;
  top: 40px;
  /* margin-top: 30px; */
  width: 80%;
}
.signupButton {
  text-align: center;
  position: relative;
  top: 60px;
  width: 80%;
  margin-left: 0;
}
/* .inputBox {
  margin-top: 20px;
}
.pwdBox {
  margin-top: 40px;
} */
</style>
