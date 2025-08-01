<template>
  <a-spin style="width: 100%;height: 100%" :loading="loading">
    <a-form
        ref="formRef"
        :model="formData"
        class="form"
        :label-col-props="{ span: 8 }"
        :wrapper-col-props="{ span: 16 }"
    >
      <!--旧密码-->
      <a-form-item
          field="oldPassword"
          label="旧密码"
          :rules="[
        {
          required: true,
          message: '请输入旧密码',
        }]"
      >
        <a-input-password
            v-model="formData.oldPassword"
            placeholder="请输入旧密码"
            autocomplete="off"
        />
      </a-form-item>
      <!--新密码-->
      <a-form-item
          field="newPassword"
          label="新密码"
          :rules="[
        {
          required: true,
          message: '请输入新密码',
        },{
        minLength: 5,
        maxLength: 20,
        message: '密码长度必须介于 5 和 20 之间'
      }
      ]"
      >
        <a-input-password
            v-model="formData.newPassword"
            placeholder="请输入新密码"
            autocomplete="off"
        />
      </a-form-item>
      <!--确认密码-->
      <a-form-item
          field="rawPassword"
          label="确认密码"
          :rules="[
        {
          required: true,
          message: '请确认新密码',
        },{
        minLength: 5,
        maxLength: 20,
        message: '密码长度必须介于 5 和 20 之间'
      }]"
      >
        <a-input-password
            v-model="formData.rawPassword"
            placeholder="请确认新密码"
            autocomplete="off"
        />
      </a-form-item>

      <a-form-item>
        <a-space size="large">
          <a-button type="primary" @click="validate">
            保存
          </a-button>
          <a-button type="secondary" @click="reset">
            重置
          </a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </a-spin>
</template>

<script lang="ts" setup>
import {ref} from 'vue';
import {FormInstance} from '@arco-design/web-vue/es/form';
import {updateUserPwd} from '@/api/user/user-center';
import {useUserStore} from "@/store";
import {Notification} from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import {notification} from "@/hooks/my-design";

const userStore: any = useUserStore();
//加载中
const {loading, setLoading} = useLoading(false);
const formRef = ref<FormInstance>();
const formConfig = () => {
  return {
    oldPassword: '',
    newPassword: '',
    rawPassword: ''
  }
}
const formData = ref<any>(formConfig());

const validate = async () => {
  const res = await formRef.value?.validate();
  if (!res) {
    if (formData.value.newPassword !== formData.value.rawPassword) {
      Notification.info({
        title: '提示',
        content: '登录密码不一致',
        duration: 2000,
      });
      return false;
    }

    try {
      setLoading(true);
      const res = await updateUserPwd(formData.value);
      if (res.code == 200) {
        formData.value = formConfig();
        await userStore.info();
      }
      notification(res);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
    // do some thing
    // you also can use html-type to submit
  }
};
const reset = async () => {
  await formRef.value?.resetFields();
};


</script>

<style scoped lang="less">
.form {
  width: 70%;
}
</style>
