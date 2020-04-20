<template>
  <div class="user-info">
    <el-tabs v-model="activeName"
             tab-position="left">
      <el-tab-pane label="个人详细信息"
                   name="first">
        <el-form :model="form"
                 label-position="top">
          <el-form-item label="用户姓名:">
            <el-col :span="6">
              <el-input v-model="form.name"
                        maxlength="11"
                        autocomplete="off"
                        style="width:200px" />
            </el-col>
          </el-form-item>
          <el-form-item label="备注:">
            <el-col :span="6">
              <el-input v-model="form.info"
                        maxlength="11"
                        autocomplete="off"
                        style="width:200px" />
            </el-col>
          </el-form-item>
          <!-- <el-form-item label="登录账号:">
            <el-col :span="5">
              <el-input v-model="form.userAccount"
                        maxlength="11"
                        autocomplete="off"
                        style="width:200px" />
            </el-col>
          </el-form-item> -->
          <!-- <el-form-item label="省:">
            <el-col :span="5"
                    style="margin-left:40px">
              <el-input v-model="form.province"
                        maxlength="11"
                        autocomplete="off"
                        style="width:200px" />
            </el-col>
          </el-form-item>
          <el-form-item label="市:">
            <el-col :span="5"
                    style="margin-left:40px">
              <el-input v-model="form.city"
                        maxlength="11"
                        autocomplete="off"
                        style="width:200px" />
            </el-col>
          </el-form-item>
          <el-form-item label="详细地址:">
            <el-col :span="5">
              <el-input v-model="form.address"
                        type="textarea"
                        maxlength="11"
                        autocomplete="off"
                        style="width:200px" />
            </el-col>
          </el-form-item> -->
          <el-form-item label="收货地址：">
            <div>
              <v-distpicker hide-area
                            @province="onChangeProvince"
                            @city="onChangeCity"
                            :province="form.province"
                            :city="form.city"></v-distpicker>
            </div>
            <div style="margin-top: 20px;">
              <el-input v-model="form.address"
                        placeholder="详细地址"
                        type="textarea"></el-input>
            </div>
          </el-form-item>
          <el-button type="success"
                     icon="el-icon-edit"
                     @click="changeUserInfo">确认修改</el-button>
          <el-button class="updateCode-item"
                     type="danger"
                     icon="el-icon-edit"
                     @click="quit">退出登录</el-button>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="修改密码"
                   name="second">
        <el-form ref="dataForm"
                 :model="code"
                 :rules="rules"
                 label-position="top">
          <el-form-item label="新密码:"
                        prop="userPassword"
                        class="dialogShow">
            <el-col :span="6">
              <el-input v-model="code.userPassword"
                        type="password"
                        placeholder="请输入新密码"
                        style="width:200px"
                        autocomplete="off" />
            </el-col>
          </el-form-item>
          <el-form-item label="确认密码:"
                        prop="aginUserPassword"
                        class="dialogShow">
            <el-col :span="6">
              <el-input v-model="code.aginUserPassword"
                        type="password"
                        placeholder="请输入确认新密码"
                        style="width:200px"
                        autocomplete="off" />
            </el-col>
          </el-form-item>
        </el-form>
        <el-button class="updateCode-item"
                   type="success"
                   icon="el-icon-edit"
                   @click="changeUserPwd">确 定</el-button>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import VDistpicker from "v-distpicker-en";
// import base64 from "../plugins/base64"
export default {
  data () {
    var validateAginPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('确认密码不能为空'));
      } else if (value !== this.code.userPassword) {
        callback(new Error('两次输入的密码不一致！'));
      } else {
        callback();
      }
    };
    return {
      activeName: "first",
      form: {},
      code: {},
      imgUrl: "",
      qiniuData: {
        token: "",
        key: ""
      },
      // 校验规则
      rules: {
        userPassword: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
        aginUserPassword: [
          { required: true, validator: validateAginPassword, trigger: "blur" }
        ]
      }
    }
  },
  components: { VDistpicker },
  created () {
    this.form.userPassword = "";
    this.imgUrl = this.form.userHeadImg;

  },
  methods: {
    changeUserInfo () {
      console.log(this.form);
      this.axios.post("/server/user/update", this.form).then(response => {
        this.$message({
          message: '修改成功！',
          type: 'success',
          showClose: true
        })
      }).catch(error => {
        console.log(error);
      })
    },
    changeUserPwd () {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          this.axios.post("/server/user/update", {
            password: this.code.userPassword
          })
            .then(response => {
              this.$message({
                message: '修改成功！',
                type: 'success',
                showClose: true
              })
            })
            .catch(error => {
              console.log(error)
            })
        }
        else {
          console.log('error submit!!')
        }
      })
    },
    onChangeProvince (data) {
      if (data.value === "Province") {
        delete this.form.province;
        return;
      }
      this.form.province = data.value;
    },
    onChangeCity (data) {
      if (data.value === "City") {
        delete this.form.city;
        return;
      }
      this.form.city = data.value
    },
    quit () {
      this.axios
        .get("/server/user/logout")
        .then(response => {
          console.log(response);
        })
        .catch(error => {
          console.log(error);
        })
        .then(response => {
          console.log("logout response", response);
          this.$store.commit("quit");
          this.$router.push({ path: "/store" });
        })
        .catch(err => {
          console.log(err);
        });
    }
  },
  mounted () {
    var session = this.$cookies.get("jsessionid");
    console.log("getcookie", session);
    this.axios
      .get(
        "/server/user/info",
        {},
        {
          headers: {
            "X-CSRFToken": this.$cookies.get("jsessionid"),
            "Content-type": "application/json",
            Cookie: "jsessionid=" + this.$cookies.get("jsessionid")
          }
        }
      )
      .then(response => {
        console.log(response.data);
        this.form = response.data;
        // if (form.province !== undefined && form.province !== '') {
        //   this.playceHolders.province = form.province;
        // }
        // if (form.city !== undefined && form.city !== '') {
        //   this.playceHolders.city = form.city;
        // }
      });
  }
};
</script>
<style>
.user-info {
  margin-left: 60px;
  margin-right: 60px;
  margin-top: 30px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.el-button--success {
  margin-left: 60px;
}
.updateCode-item {
  margin-left: 150px;
}
</style>
