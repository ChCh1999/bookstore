<template>
  <div class="user-info">
    <el-tabs v-model="activeName"
             tab-position="left">
      <div></div>
      <el-tab-pane label="个人详细信息"
                   name="first">
        <el-form :model="form"
                 label-position="top">
          <el-form-item label="用户姓名:">
            <el-col :span="6">
              <el-input v-model="form.userName"
                        maxlength="11"
                        autocomplete="off"
                        style="width:200px" />
            </el-col>
          </el-form-item>
          <el-form-item label="用户手机号:">
            <el-col :span="6">
              <el-input v-model="form.userPhone"
                        maxlength="11"
                        autocomplete="off"
                        style="width:200px" />
            </el-col>
          </el-form-item>
          <el-form-item label="登录账号:">
            <el-col :span="5">
              <el-input v-model="form.userAccount"
                        maxlength="11"
                        autocomplete="off"
                        style="width:200px" />
            </el-col>
          </el-form-item>
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
                            @city="onChangeCity"></v-distpicker>
            </div>
            <div>
              <el-input v-model="form.address"
                        placeholder="详细地址"
                        type="textarea"></el-input>

            </div>
          </el-form-item>
          <el-button type="success"
                     icon="el-icon-edit"
                     @click=";">确认修改</el-button>
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
                   @click=";">确 定</el-button>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import VDistpicker from 'v-distpicker'
export default {
  data () {
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
          { required: true, message: "确认密码不能为空", trigger: "blur" }
        ]
      }
    };
  },
  components: { VDistpicker },
  created () {
    this.form.userPassword = "";
    this.imgUrl = this.form.userHeadImg;
  },
  methods: {
    onChangeProvince (data) {
      if (data.value === '省') {
        delete this.form.province
        return
      }
      this.form.province = data.value
    },
    onChangeCity (data) {
      if (data.value === '市') {
        delete this.form.city
        return
      }
      this.form.city = data.value
    }
  }
};
</script>
<style>
.user-info{
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
