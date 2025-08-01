<template>
  <a-row class="login-container">
    <a-col :span="14" class="login-logo">
      <img alt="logo" src="@/assets/login-logo.png"/>
    </a-col>
    <a-col :span="10" class="login-form">
      <LoginForm/>
      <Footer/>
    </a-col>
  </a-row>
</template>

<script lang="ts" setup>
import Footer from '@/components/footer/index.vue'
import LoginForm from './components/login-form.vue'
</script>

<style lang="less" scoped>
.login-container {
  height: 100vh;

  .login-logo {
    height: 100%;
    background: url("@/assets/login-background.png") no-repeat;
    background-size: 100% 100%;
    img {
      position: fixed;
      left: 20px;
      top: 20px;
    }
  }

  .login-form {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

    .footer {
      position: fixed;
      bottom: 30px;
    }
  }
}

</style>
