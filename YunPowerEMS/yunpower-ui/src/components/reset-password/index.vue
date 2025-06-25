<template>
  <a-modal v-model:visible="visible"
           title="重置密码"
           :mask-closable="false"
           @cancel="handleCancel"
           width="40%"
           :footer="false">
    <a-spin style="width: 100%;height: 100%" :loading="loading">
      <a-form
          ref="formRef"
          :model="formData"
          class="form"
      >
        <!--登录名称-->
        <a-form-item
            field="logonName"
            label="登录名称"
            disabled
        >
          <a-input
              v-model="userStore.user.logonName"
              :style="{'text-fill-color': isDark?'#fff':''}"
          />
        </a-form-item>
        <!--用户昵称-->
        <a-form-item
            field="oldPassword"
            label="旧密码"
            :rules="[
        {
          required: true,
          message: '请输入旧密码',
        },
      ]"
        >
          <a-input-password
              v-model="formData.oldPassword"
              :style="{'text-fill-color': isDark?'#fff':''}"
              placeholder="请输入旧密码"
              autocomplete="off"
          />
        </a-form-item>
        <!--手机号码-->
        <a-form-item
            field="newPassword"
            label="新密码"
            :rules="[
        {
          required: true,
          message: '请输入新密码',
        },
      ]"
        >
          <a-input-password
              v-model="formData.newPassword"
              :style="{'text-fill-color': isDark?'#fff':''}"
              placeholder="请输入新密码"
              autocomplete="off"
          />
        </a-form-item>
        <!--邮箱-->
        <a-form-item
            field="rawPassword"
            label="确认密码"
            :rules="[
        {
          required: true,
          message: '请确认新密码',
        },
      ]"
        >
          <a-input-password
              v-model="formData.rawPassword"
              :style="{'text-fill-color': isDark?'#fff':''}"
              placeholder="请确认新密码"
              autocomplete="off"
          />
        </a-form-item>
      </a-form>
      <a-row style="width: 100%;height:100%" justify="center">
        <a-col :span="4">
          <a-space size="large">
            <a-button type="primary" @click="validate">
              保存
            </a-button>
            <a-button type="secondary" @click="reset">
              重置
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-spin>
  </a-modal>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, watchEffect} from "vue";
import useLoading from "@/hooks/loading";
import {useUserStore} from '@/store'
import {updatePwd} from "@/api/user";
import {notification} from "@/hooks/my-design";
import useThemes from '@/hooks/themes';

const {isDark} = useThemes();
const userStore = useUserStore();

const formRef = ref<any>(null);
//加载中
const {loading, setLoading} = useLoading(false);
const formConfig = () => {
  return {
    oldPassword: '',
    newPassword: '',
    rawPassword: '',
    logonName: userStore?.user?.logonName
  }
}

const formData = ref<any>(formConfig());

const visible = ref<boolean>(userStore.initPassword && localStorage.getItem("initPassword") == "1")

const validate = async () => {
  try {
    const validate = await formRef.value?.validate();
    if (!validate) {
      if (formData.value.newPassword !== formData.value.rawPassword) {
        notification({
          code: 500,
          msg: '输入密码不一致'
        })
        return;
      }
      setLoading(true);
      try {
        let params: any = {
          newPassword: formData.value.newPassword,
          oldPassword: formData.value.oldPassword
        }
        const res = await updatePwd(params);
        notification(res)
        if (res.code == 200) {
          // 关闭弹窗
          handleCancel();
        }
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    }
    // 这里可以添加提交表单的逻辑
  } catch (error) {
    console.error(error);
  }
}
const reset = () => {
  formData.value = formConfig();
}

const handleCancel = () => {
  localStorage.removeItem("initPassword");
  visible.value = false;
  formData.value = formConfig();
}

watchEffect(() => {
  if (userStore?.user.logonName) {
    formData.value.logonName = userStore?.user.logonName;
  }
})
</script>

<style scoped lang="less">
:deep(.arco-input-wrapper .arco-input.arco-input-size-medium) {
  height: 0;
  padding: 1.2em .5em;
  background-clip: content-box;
}
</style>