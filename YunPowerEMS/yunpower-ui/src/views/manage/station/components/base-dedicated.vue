<template>
  <a-card title="基础设置">
    <a-form ref="formRef" :rules="rules" :model="form" auto-label-width>
      <!--模块名称-->
      <a-row :gutter="[12,0]">
        <a-col :span="18">
          <a-form-item field="cardName" label="卡片名称" :validate-trigger="['change','input']">
            <a-input v-model="form.cardName" :maxLength="20" placeholder="请输入字母或数字，最多20个字符"/>
          </a-form-item>
        </a-col>

        <!--表格宽度-->
        <a-form-item field="headType" label="表格宽度" v-if="defaultVar.chartType==15||defaultVar.chartType==16">
          <a-radio-group v-model="form.headType">
            <a-radio v-for="(item,index) in datav_head_type" :key="index" :value="Number(item.value)">{{ item.label }}</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-row>

    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {getCurrentInstance, ref} from "vue";
import {FormInstance} from "@arco-design/web-vue/es/form";

const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {datav_head_type} = proxy?.useDict("datav_head_type");
const props = defineProps({
  defaultVar: {
    type: Object,
    default: {}
  }
});

const formRef = ref<any>({});
const rules: any = {
  cardName: [
    {
      required: true,
      message: '请输入卡片名称'
    }
  ]
};
// 初始化数据配置
const initDataConfig = () => {
  return {
    cardName: props.defaultVar.cardName,
    headType: 1, //表格宽度-长表头
  }
};

// 初始化form
const form = ref<any>(initDataConfig());

// 校验
const validateForm = async() => {
  try {
    const valid = await formRef.value.validate();
    return {
      valid
    }
  } catch (error) {
    console.error('验证时发生错误', error);
  }
};

defineExpose({
  form,
  formRef,
  validateForm,
  setForm: (val: any) => {
    if (Object.keys(val).length === 0) {
      form.value = initDataConfig();
    } else {
      form.value = val;
    }
  }
});
</script>

<style scoped lang="less">

</style>