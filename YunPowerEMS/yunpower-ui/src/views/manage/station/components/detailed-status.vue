<template>
  <a-card v-for="(item,index) in form.varList"
          :key="index"
          :title="chartType==12&&index!=0?'数据区'+index:'详细设置'"
          :class="[index!=0?'children-card':'']"
          style="margin-bottom: 16px">
    <a-form ref="formRef" :rules="rules" :model="item" auto-label-width>
      <!--状态类型-->
      <a-form-item field="stateType" label="状态类型">
        <a-select v-model="item.stateType" placeholder="请选择" allow-clear>
          <a-option v-for="(item, index) in datav_state_type" :value='Number(item.value)' :key="index">{{item.label}}</a-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {getCurrentInstance, ref} from "vue";
import {FormInstance} from "@arco-design/web-vue/es/form";
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {datav_state_type} = proxy?.useDict("datav_state_type");

const porps = defineProps({
  chartType:{
    type: Number,
    default: 8,
  }
})
const formRef = ref<FormInstance>();
const rules:any = [{}];

// 初始化数据配置
const initDataConfig = () => {
  return {
    stateType: undefined, //状态类型
  }
};

// 初始化form
const form = ref<any>({
  varList: [initDataConfig()], //数据区域
});

defineExpose({
  form,
  formRef,
  setForm:(val:any)=>{
    if (Object.keys(val).length === 0) {
      form.value.varList = initDataConfig();
    } else {
      form.value = val;
      // console.log(form.value);
    }
  }
})
</script>

<style scoped lang="less">

</style>