<template>
  <a-card title="基础设置">
    <a-form ref="formRef" :rules="rules" :model="form" auto-label-width>
      <!--模块名称-->
      <a-row :gutter="[12,0]">
<!--        <a-col :span="18">-->
          <a-form-item field="cardName" label="卡片名称" :validate-trigger="['change','input']">
            <a-input v-model="form.cardName" :maxLength="20" placeholder="请输入字母或数字，最多20个字符" />
          </a-form-item>
<!--        </a-col>-->
<!--        <a-col :span="6">-->
<!--          &lt;!&ndash;智能拼接&ndash;&gt;-->
<!--          <a-checkbox v-model="form.isSplit">智能拼接</a-checkbox>-->
<!--        </a-col>-->
      </a-row>

      <!--时间维度-->
<!--      <a-form-item field="dateDim" label="时间维度">-->
<!--        <a-select v-model="form.dateDim" placeholder="请选择" allow-clear>-->
<!--          <a-option :value="item.value" v-for="(item,index) in sys_config_page_x" :key="item.id">{{item.label}}</a-option>-->
<!--        </a-select>-->
<!--      </a-form-item>-->

      <a-form-item field="dateDim" label="时间维度">
        <a-row :gutter="[0,16]">
          <!--时间维度-->
          <a-col :span="24">
            <a-select v-model="form.dateDim" placeholder="请选择" allow-clear style="width: 400px" @change="handleDateDim">
              <!--单值-->
              <template v-if="defaultVar.chartType==14">
                <a-option v-for="(item,index) in datav_date_dim"
                          :value="Number(item.value)" :key="index">
                  {{ item.label }}
                </a-option>
              </template>
              <!--其他-->
              <template v-else>
                <a-option v-for="(item,index) in datav_date_dim" v-show="item.value!=100"
                          :value="Number(item.value)" :key="index">
                  {{ item.label }}
                </a-option>
              </template>
            </a-select>
          </a-col>
          <!--具体时间-->
          <a-col v-show="[70].includes(form.dateDim)" :span="24">
            <a-radio-group v-model="form.secondDateDim">
              <a-radio v-for="(item,index) in datav_date_second_dim" :key="index" :value='Number(item.value)'>
                {{ item.label }}
              </a-radio>
            </a-radio-group>
          </a-col>
        </a-row>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {getCurrentInstance, ref} from "vue";
import {FormInstance} from "@arco-design/web-vue/es/form";
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {datav_date_dim, datav_date_second_dim} = proxy?.useDict("datav_date_dim", "datav_date_second_dim");
const formRef = ref<any>({});
const rules: any = {
  cardName: [
    {
      required: true,
      message: '请输入卡片名称'
    }
  ]
};
const props = defineProps({
  defaultVar: {
    type: Object,
    default: {}
  }
});
const emit = defineEmits(["handleDateDim"]);

// 初始化数据配置
const initDataConfig = () => {
  return {
    cardName: '',
    dateDim: 70,
    secondDateDim: 60,
  }
};

// 初始化form
const form = ref<any>(initDataConfig());


// 日期
const handleDateDim = (val:any) => {
  emit("handleDateDim", val)
}

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
  setForm:(val:any)=>{
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