<template>
  <div class="login-form-wrapper">
    <slot name="title">
      <div class="login-form-title">欢迎使用{{ title }}</div>
      <div class="login-form-sub-title">{{ title }}管理系统</div>
    </slot>
    <slot name="error-msg">
      <div class="login-form-error-msg">{{ errorMessage }}</div>
    </slot>
    <a-form ref="loginForm" :model="userInfo" class="login-form" layout="vertical" @submit="handleSubmit">
      <a-form-item field="username" :rules="[{ required: true, message: $t('login.form.userName.errMsg') }]"
                   :validate-trigger="['change', 'blur']" hide-label>
        <a-input v-model="userInfo.username" :placeholder="$t('login.form.userName.placeholder')">
          <template #prefix>
            <icon-user/>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item field="password" :rules="[{ required: true, message: $t('login.form.password.errMsg') }]"
                   :validate-trigger="['change', 'blur']" hide-label>
        <a-input-password v-model="userInfo.password" :placeholder="$t('login.form.password.placeholder')" allow-clear>
          <template #prefix>
            <icon-lock/>
          </template>
        </a-input-password>
      </a-form-item>
      <a-row :gutter="5">
        <a-col :span="12">
          <a-form-item field="code" :rules="[
            { required: true, message: $t('login.form.verificationCode.errMsg') },
          ]" :validate-trigger="['change', 'blur']" hide-label>
            <a-input v-model="userInfo.code" :placeholder="$t('login.form.password.verificationCode.placeholder')"
                     :max-length="4" style="width: 200px" class="code">
              <template #prefix>
                <icon-safe/>
              </template>
            </a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <div class="login-code" @click="captcha">
            <img :src=codeData alt="" class="login-img"/>
          </div>
        </a-col>
        <a-col :span="4">
          <span class="login-img-text" @click="captcha">换一张</span>
        </a-col>
      </a-row>

      <a-space :size="16" direction="vertical">
        <div class="login-form-password-actions">
          <a-checkbox checked="rememberPassword" :model-value="loginConfig.rememberPassword"
                      @change="setRememberPassword as any">
            {{ $t('login.form.rememberPassword') }}
          </a-checkbox>
        </div>
        <a-button type="primary" html-type="submit" long :loading="loading">
          {{ $t('login.form.login') }}
        </a-button>
        <!-- <a-button type="text" long class="login-form-register-btn">
          {{ $t('login.form.register') }}
          <a-link>{{ $t('login.form.forgetPassword') }}</a-link>
        </a-button> -->
      </a-space>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import type {LoginData} from '@/api/user'
import {getCaptcha, getMenuList} from '@/api/user'
import useLoading from '@/hooks/loading'
import {useUserStore} from '@/store'
import {Message} from '@arco-design/web-vue'
import {ValidatedError} from '@arco-design/web-vue/es/form/interface'
import {useStorage} from '@vueuse/core'
import {onMounted, reactive, ref} from 'vue'
import {useI18n} from 'vue-i18n'
import {useRouter} from 'vue-router'
import CryptoJS from "crypto-js";

const router = useRouter()

const env = import.meta.env.MODE;
if (env == "demo") {
  const currentRoute = router.currentRoute.value
  router.replace({
    name: "loginDemo",
    query: {
      ...router.currentRoute.value.query,
    },
  })
}
const {t} = useI18n()
const errorMessage = ref('')
const {loading, setLoading} = useLoading()
const userStore = useUserStore()
const title = import.meta.env.VITE_APP_TITLE;
const loginConfig = useStorage('login-config', {
  rememberPassword: true,
  username: '', // 演示默认值
  password: '', // demo default value
})

const decryptText = (password: string) => {
  const decrypted = CryptoJS.AES.decrypt(password, code);
  return decrypted.toString(CryptoJS.enc.Utf8);
}

const encryptText = (password: string) => {
  return CryptoJS.AES.encrypt(password, code).toString();
}

const code = 'ylt-cloud';
const userInfo = reactive({
  username: loginConfig.value.username,
  password: loginConfig.value.password ? decryptText(loginConfig.value.password) : '',
  code: "",
  uuid: ""
})
const codeData: any = ref();

const captcha = () => {
  getCaptcha({randomStr: Date.now()}).then((res: any) => {
    codeData.value = 'data:image/png;base64,' + res.img;
    userInfo.uuid = res.uuid;
  })
};

const handleSubmit = async ({
                              errors,
                              values,
                            }: {
  errors: Record<string, ValidatedError> | undefined
  values: Record<string, any>
}) => {
  if (loading.value) return
  if (!errors) {
    setLoading(true)
    try {
      values.browserFlag = Math.random().toString(32).substring(4).toUpperCase();
      await userStore.login(values as LoginData)

      //读取权限菜单
      const {data} = await getMenuList();
      if (!data || data.length <= 0) {
        captcha();
        errorMessage.value = "登录失败";
        Message.error("暂无菜单权限，请联系管理员");
        return;
      }
      localStorage.setItem('initPassword', "1");
      Message.success(t('login.form.login.success'))
      const {rememberPassword} = loginConfig.value
      const {username, password} = values
      // 实际生产环境需要进行加密存储。

      // The actual production environment requires encrypted storage.
      loginConfig.value.username = rememberPassword ? username : ''
      loginConfig.value.password = rememberPassword ? encryptText(password) : ''
      const {redirect, ...othersQuery} = router.currentRoute.value.query
      router.push({
        path: redirect || "/",
        query: {
          ...othersQuery,
        }
      })

    } catch (err) {
      captcha();
      errorMessage.value = (err as Error).message;
      userInfo.code = '';
    } finally {
      setLoading(false);
    }
  }
}
const setRememberPassword = (value: boolean) => {
  loginConfig.value.rememberPassword = value
}


onMounted(() => {
  captcha();
})
</script>

<style lang="less" scoped>
.login-form {
  &-wrapper {
    width: 320px;
  }

  &-title {
    color: var(--color-text-1);
    font-weight: 500;
    font-size: 24px;
    line-height: 32px;
  }

  &-sub-title {
    color: var(--color-text-3);
    font-size: 16px;
    line-height: 24px;
  }

  &-error-msg {
    height: 32px;
    color: rgb(var(--red-6));
    line-height: 32px;
  }

  &-password-actions {
    display: flex;
    justify-content: space-between;
  }

  &-register-btn {
    color: var(--color-text-3) !important;
  }

  .login-code {
    width: 100%;
    height: 32px;

    img {
      width: 100%;
      height: 100%;
    }
  }

  .login-img-text {
    line-height: 32px;
    cursor: pointer;
    color: var(--color-text-1);
  }

  .login-img-text:hover {
    text-decoration-line: underline;
  }
}
</style>
