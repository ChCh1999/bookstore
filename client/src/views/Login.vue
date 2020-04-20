<template>
  <el-row class="loginRow">
    <img id="imgLogin" src="../assets/book.png" />
    <transition mode="out-in">
      <component
        class="card"
        v-bind:is="comName"
        :componentName="comName"
        v-on:listen="changeCard"
      ></component>
    </transition>
  </el-row>
</template>

<script>
import loginCard from "../components/LoginCard";
import setUpCard from "../components/SetUpCard";
export default {
  name: "Login",
  components: {
    loginCard,
    setUpCard
  },
  data: function() {
    return {
      comName: "loginCard"
    };
  },
  methods: {
    changeCard: function(componentName) {
      this.comName = componentName;
    }
  }
};
</script>

<style scoped>
#UserLogin {
  text-align: center;
}
.loginRow {
  /* background-attachment: fixed; */
  /* position: fixed; */
  width: 100%;
  height: 100%;
  bottom: 0;
  margin-top: 0%;
  margin-bottom: 0%;
}
.card {
  /* background-color: rgb(190, 227, 241); */
}
#imgLogin {
  position: absolute;
  left: 10%;
  width: 40%;
  top: 10%;
}
</style>
