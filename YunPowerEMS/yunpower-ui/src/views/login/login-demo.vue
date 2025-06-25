<template>
  <a-row class="login-container">
    <a-col :span="14" class="login-logo">
      <img alt="logo" src="@/assets/login-logo.png"/>
    </a-col>
    <a-col :span="10" class="login-form">
      <LoginForm>
        <template #title>
          <div class="login-form-title">
            <a-typography-title>
              <span>YunPower</span>EMS
            </a-typography-title>
          </div>
        </template>
      </LoginForm>
      <div class="login-message">
        <p>
          <a-link @click="openGitee">加入Gitee开源社区</a-link>
        </p>
<!--        <img src="@/assets/qiyeweixin.jpg" alt="">-->
<!--        <p>申请测试账号</p>-->
      </div>
      <Footer/>
    </a-col>
  </a-row>
</template>

<script lang="ts" setup>
import Footer from '@/components/footer/index.vue'
import LoginForm from './components/login-form.vue'

const openGitee = () => {
  window.open('https://gitee.com/')
}
</script>

<style lang="less" scoped>
.login-container {
  height: 100vh;

  .login-logo {
    height: 100%;
    background: url("../../assets/login-background.png") no-repeat;
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
    flex-direction: column;
    align-items: center;
    justify-content: center;

    .login-form-title {
      .arco-typography {
        letter-spacing: 1.2px;
        font-size: 80px;
        margin-left: -80px;
        margin-top: -40px;
        white-space: nowrap;

        span {
          font-weight: 1000;
        }
      }
    }

    .login-message {
      width: 200px;
      text-align: center;

      img {
        width: 100px;
        height: 100px;
      }
    }

    .footer {
      position: fixed;
      bottom: 30px;
    }
  }
}

</style>
