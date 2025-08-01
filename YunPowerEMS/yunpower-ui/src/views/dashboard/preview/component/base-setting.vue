<template>
  <a-card title="基础设置">
    <a-form ref="formRef" :rules="rules" :model="form" auto-label-width>
      <!--模块名称-->
      <a-form-item field="cardName" label="卡片名称" :validate-trigger="['change','input']">
        <a-input v-model="form.cardName" :maxLength="20" placeholder="请输入字母或数字，最多20个字符"/>
      </a-form-item>
      <!-- 分段图显示-->
      <template v-if="[11].includes(defaultVal.chartType)">
        <!--电价标准-->
        <a-form-item field="electricityPriceType" label="电价标准">
          <!--          {{form.electricityPriceType}}-->
          <a-radio-group v-model="form.electricityPriceType" @change="handlePriceType">
            <a-radio v-for="(item,index) in datav_electricity_price_type" :key="index" :value='Number(item.value)'>
              {{ item.label }}
            </a-radio>
          </a-radio-group>
        </a-form-item>

        <!--选择电价-->
        <a-form-item field="schemeId" label="选择电价" v-if="form.electricityPriceType==2">
          <a-select v-model="form.schemeId" placeholder="请选择" allow-clear>
            <a-option v-for="(item, index) in renderData" :key="index" :value="item.id">{{ item.electricName }}
            </a-option>
          </a-select>
        </a-form-item>
      </template>

      <!--数据类型-->
      <a-form-item field="staticType" label="数据类型" v-if="[15,16].includes(defaultVal.chartType)">
        <a-select v-model="form.staticType" placeholder="请选择" allow-clear>
          <template v-if="defaultVal.chartType==16">
            <a-option v-for="(item,index) in sys_order_status" :key="index" :value="Number(item.value)">{{ item.label }}
            </a-option>
          </template>
          <template v-if="defaultVal.chartType==15">
            <a-option v-for="(item,index) in sys_trigger_status" :key="index" :value="Number(item.value)">{{
                item.label
              }}
            </a-option>
          </template>
        </a-select>
      </a-form-item>

      <!--时间维度 1, 2, 3, 5, 10-->
      <template v-if="![13,11,4,12,15,16,17,18,21].includes(defaultVal.chartType)">
        <a-form-item field="dateDim" label="时间维度">
          <a-row :gutter="[0,16]">
            <!--时间维度-->
            <a-col :span="24">
              <a-select v-model="form.dateDim" placeholder="请选择" allow-clear style="width: 400px"
                        @change="handleDateDim">
                <!--单值-->
                <template v-if="defaultVal.chartType==14">
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
            <a-col v-show="[70].includes(form.dateDim) && ![19,14,6,8,9].includes(defaultVal.chartType)" :span="24">
              <a-radio-group v-model="form.secondDateDim">
                <a-radio v-for="(item,index) in datav_date_second_dim" :key="index" :value='Number(item.value)'>
                  {{ item.label }}
                </a-radio>
              </a-radio-group>
            </a-col>
          </a-row>
        </a-form-item>

        <!--全日期-->
        <a-form-item field="isFullDate" label="全日期" v-if="![14, 7,10, 19, 6,8,9,21].includes(defaultVal.chartType)">
          <a-checkbox v-model="form.isFullDate" :value="true">是</a-checkbox>
        </a-form-item>
      </template>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, nextTick, onMounted, ref, watchEffect} from "vue";
import {FormInstance} from "@arco-design/web-vue/es/form";
import {listPriceScheme} from "@/api/system/price-scheme";

const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  datav_date_dim,
  datav_date_second_dim,
  datav_electricity_price_type,
  datav_value_type,
  sys_order_status,
  sys_trigger_status
} = proxy?.useDict(
    "sys_config_page_x", "datav_date_dim", "datav_date_second_dim", "datav_electricity_price_type", "datav_value_type", "sys_order_status", "sys_trigger_status");
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
  defaultVal: {
    type: Object,
    default: {}
  }
});
const emit = defineEmits(["handleDateDim"]);

// 初始化数据配置
const initDataConfig = () => {
  if (props.defaultVal.chartType == 11) {
    return {
      cardName: '',
      dateDim: 70,
      secondDateDim: 60,
      isFullDate: false,
      electricityPriceType: 1, //电价标准
      schemeId: undefined, //选择电价
    }
  } else {
    return {
      cardName: '',
      dateDim: 70,
      secondDateDim: 60,
      isFullDate: false,
      staticType: [15, 16].includes(props.defaultVal.chartType) ? 2 : undefined,
    }
  }
};

const form = ref<any>(initDataConfig());

const renderData = ref<any>();

//生成查询条件对象
const generateSearchModel = () => {
  return {
    electricName: "",
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());

/**
 * 查询表格数据
 */
const fetchData = async () => {
  try {
    const res = await listPriceScheme({
      ...searchModel.value,
    });

    if (res.code == 200 && res.rows && res.rows.length > 0) {
      // console.log(res.rows)
      renderData.value = res.rows;
    }

  } catch (err) {
    console.error(err)
    // you can report use errorHandler or other
  } finally {
  }
};

// 日期
const handleDateDim = (val: any) => {
  // props.defaultVal.dateDim = val;
  emit("handleDateDim", val);
}

// 电价标准选择
const handlePriceType = (val: any) => {
  if (val == 2) {
    form.value.schemeId = '';
  }
}

// 校验
const validateForm = async () => {
  try {
    const valid = await formRef.value.validate();
    return {
      valid
    }
  } catch (error) {
    console.error('验证时发生错误', error);
  }
};

watchEffect(async () => {
  if (props.defaultVal.chartType == 11) {
    form.value = initDataConfig();
    fetchData();
    await nextTick();
  }
})

defineExpose({
  form,
  formRef,
  validateForm,
  setForm: (val: any) => {
    if (Object.keys(val).length === 0) {
      form.value = initDataConfig();
    } else {
      val.isFullDate = !!val.isFullDate;
      form.value = val;

      // console.log(form.value, '222222222')
    }
  }
});
</script>

<style scoped lang="less">

</style>