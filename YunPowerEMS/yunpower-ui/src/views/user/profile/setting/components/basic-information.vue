<template>
  <a-spin style="width: 100%;height: 100%" :loading="loading">
    <a-form
        ref="formRef"
        :model="formData"
        class="form"
        :label-col-props="{ span: 8 }"
        :wrapper-col-props="{ span: 16 }"
    >
      <!--用户昵称-->
      <a-form-item
          field="nickName"
          label="用户昵称"
          :rules="[
        {
          required: true,
          message: '请输入用户昵称',
        },
      ]"
      >
        <a-input
            v-model="formData.nickName"
            placeholder="请输入用户昵称"
        />
      </a-form-item>
      <!--手机号码-->
      <a-form-item
          field="mobile"
          label="手机号码"
          :rules="[
        {
          required: true,
          message: '请输入手机号码',
        },
      ]"
      >
        <a-input
            :max-length="11"
            v-model="formData.mobile"
            placeholder="请输入手机号码"
        />
      </a-form-item>
      <!--邮箱-->
      <a-form-item
          field="email"
          label="邮箱"
          :max-length="32"
          :rules="[
        {
          required: true,
          message: '请输入邮箱',
        },
      ]"
      >
        <a-input
            v-model="formData.email"
            placeholder="请输入邮箱"
        />
      </a-form-item>

      <a-form-item field="sex" label="性别">
        <a-select v-model="formData.sex" :max-length="20" allow-clear
                  :placeholder="$t('manage.account.user.genderSelect')">
          <a-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label"
                    :value="Number(dict.value)"></a-option>
        </a-select>
      </a-form-item>

      <a-form-item>
        <a-space size="large">
          <a-button type="primary" @click="validate" :loading="loading">
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
import {ref, getCurrentInstance, onMounted} from 'vue';
import {FormInstance} from '@arco-design/web-vue/es/form';
import {updateUserProfile} from '@/api/user/user-center';
import {useUserStore} from "@/store";
import {notification} from "@/hooks/my-design";
import useLoading from "@/hooks/loading";

const userStore: any = useUserStore();
//加载中
const {loading, setLoading} = useLoading(false);
//******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {sys_user_sex} = proxy?.useDict("sys_user_sex");
const emit = defineEmits(['handleSave']);
const formRef = ref<FormInstance>();
const formDataConfig = () => {
  return {
    nickName: '',
    mobile: '',
    sex: '',
    email: ''
  }
}
const formData = ref<any>(formDataConfig());

const validate = async () => {
  const res = await formRef.value?.validate();
  if (!res) {
    try {
      setLoading(true);
      let params = {
        nickName: formData.value.nickName,
        mobile: formData.value.mobile,
        sex: formData.value.sex,
        email: formData.value.email
      };
      const res = await updateUserProfile(params);
      if(res.code == 200){
        emit('handleSave');
      }
      notification(res);
    }catch (e) {
      console.error(e);
    }finally {
      setLoading(false);
    }
  }
};
const reset = async () => {
  formData.value = {...userStore.user};
};

onMounted(()=>{
  formData.value = {...userStore.user};
})
</script>

<style scoped lang="less">
.form {
  width: 70%;
}
</style>
