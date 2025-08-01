<template>
  <a-card title="详细设置" v-for="(item,index) in form.varList" :key="index">
    <a-form ref="formRef" :rules="rules" :model="item" auto-label-width>

      <!--水滴图不展示-->
        <!--指标单位-->
        <a-form-item field="unit" label="指标单位" >
          <a-input v-model="item.unit" placeholder="请输入" />
        </a-form-item>

      <!--存储类型-->
      <a-form-item field="storageType" label="存储类型">
        <a-radio-group v-model="item.storageType" @change="handleStorage(index)">
          <a-radio v-for="(item,index) in datav_storage_type" :value="Number(item.value)" :key="index">
            {{ item.label }}
          </a-radio>
        </a-radio-group>
      </a-form-item>

      <!--数值类型 存储类型为变化值展示-->
      <a-form-item field="changeType" label="数值类型" v-if="item.storageType==1 && defaultVar.dateDim!=70">
        <a-radio-group v-model="item.changeType">
          <a-radio v-for="(item,index) in datav_change_type" :key="index" :value="Number(item.value)"
                   v-show="item.value!=1">{{ item.label }}
          </a-radio>
        </a-radio-group>
      </a-form-item>

      <!--显示类型-->
      <a-form-item field="percentage" label="显示类型">
        <a-radio-group v-model="item.percentage" @change="handlePercentage(index)">
          <a-radio v-for="(item,index) in datav_percentage" :value="Number(item.value)" :key="index">{{ item.label }}
          </a-radio>
        </a-radio-group>
      </a-form-item>

      <!-- 基准值-->
      <a-form-item field="baseValue" label="基准值" v-if="item.percentage==2||item.percentage==3">
        <a-input-number hideButton v-model="item.baseValue" placeholder="请输入" />
      </a-form-item>

      <!--数据转换-->
      <a-form-item v-if="![13, 11].includes(defaultVar.chartType)" field="conversion" label="数据转换">
        <a-select v-model="item.conversion" placeholder="请选择" allow-clear>
          <a-option :value="item.label" v-for="(item,index) in datav_data_conversion" :key="index">
            {{ item.label }}
          </a-option>
        </a-select>
      </a-form-item>

      <template v-if="item.percentage==1">
        <!-- 最小值-->
        <a-form-item field="minValue" label="最小值">
          <a-input-number hideButton v-model="item.minValue" placeholder="请输入" />
        </a-form-item>

        <!-- 最大值-->
        <a-form-item field="maxValue" label="最大值">
          <a-input-number hideButton v-model="item.maxValue" placeholder="请输入" />
        </a-form-item>
      </template>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {getCurrentInstance, ref, watchEffect} from "vue";
import {FormInstance} from "@arco-design/web-vue/es/form";
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {datav_storage_type, datav_change_type, datav_percentage, datav_data_conversion} = proxy?.useDict("datav_storage_type", "datav_change_type", "datav_percentage", "datav_data_conversion");

const props = defineProps({
  defaultVar: {
    type: Object,
    default: {}
  },
  varData: {
    type: Object,
    default: {}
  }
});

const formRef = ref<FormInstance>();
const initDataConfig = () => {
  return {
    unit: '',
    chain: false,
    yoy: false,
    storageType: 1, //存储类型
    changeType: 1, //数值类型
    percentage: 1, //是否百分比
    baseValue: 0, //基准值
    minValue: 0, //最小值
    maxValue: 0, //最大值
    conversion: '原汁原味', //数据转换
  }
};
const rules:any = [{}];
const form = ref<any>({
  varList: [initDataConfig()], //数据区域
});

// 存储类型切换
const handleStorage = (index: number) => {
  if(props.defaultVar.dateDim == 70){
    form.value.varList[index].changeType = 1;
  }else{
    form.value.varList[index].changeType = 2;
  }
};

// 显示类型切换
const handlePercentage = (index: number) => {
  form.value.varList[index].baseValue = 0;
  form.value.varList[index].minValue = 0;
  form.value.varList[index].maxValue = 0;
};

watchEffect(() => {
  if (props.varData.length > 0) {
    form.value.varList[0].unit = props.varData[0].unit;
    form.value.varList[0].varName = props.varData[0].varName;
    form.value.varList[0].varSn = props.varData[0].varSn;
  } else {
    form.value.varList[0].unit = '';
    form.value.varList[0].varName = '';
    form.value.varList[0].varSn = '';
  }

  if (props.defaultVar.dateDim == 70) {
    form.value.varList.map(val => {
      val.changeType = 1;
    })
  }else if(!props.defaultVar.isDetail){
    form.value.varList.map(val => {
      val.changeType = 2;
    })
  }
});

defineExpose({
  form,
  formRef,
  setForm:(data:any)=>{
    if(data.varList.length==0){
      form.value.varList = [initDataConfig()];
    }else{
      form.value.varList[0] = data.varList[0];
    }
  }
});
</script>

<style scoped lang="less">

</style>