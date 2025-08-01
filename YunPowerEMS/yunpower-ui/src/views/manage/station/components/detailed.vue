<template>
  <a-card v-for="(item,index) in form.varList"
          :key="index"
          :title="defaultVar.chartType==12&&index!=0?'数据区'+index:'详细设置'"
          :class="[index!=0?'children-card':'']"
          style="margin-bottom: 16px">
    <a-form ref="formRef" :rules="rules" :model="item" auto-label-width>
      <!--水滴图不展示-->
<!--      <template>-->
        <!--选择变量-->
        <a-form-item label="选择变量" v-if="defaultVar.chartType==12&&index!=0">
          <a-button type="outline" size="small" @click="chooseVar(index)">选择变量</a-button>
          <a-space v-show="item.varSn">
            <a-tag
                closable
                @close="handleRemove(index)"
            >
              {{ item.varName }}
            </a-tag>
          </a-space>
        </a-form-item>

        <!--指标单位:饼图不显示-->
        <a-form-item field="unit" label="指标单位" v-if="defaultVar.chartType!=6">
          <a-input v-model="item.unit" placeholder="请输入"/>
        </a-form-item>

        <!--单值不显示-->
        <template v-if="![14,8].includes(defaultVar.chartType)">
          <!--辅助指标 排行榜不展示-->
          <a-form-item label="辅助指标" v-if="defaultVar.chartType!=12" :content-flex="false" :merge-props="false">
            <a-row style="margin-top: 5px;">
              <a-col flex="80px">
                <a-form-item field="chain" no-style>
                  <a-checkbox v-model="item.chain">环比</a-checkbox>
                </a-form-item>
              </a-col>
              <a-col flex="80px">
                <a-form-item field="yoy" no-style>
                  <a-checkbox v-model="item.yoy">同比</a-checkbox>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form-item>

          <!--图例颜色-->
          <a-form-item field="color" :class="item.color==''?'color-wrapper':''" label="图例颜色">
            <a-color-picker
                v-model="item.color"
                :historyColors="history"
                showHistory
                showPreset
                @popup-visible-change="addHistory"
            />
            <icon-close-circle class="delete-icon" @click="handleColor(item, index)"/>
          </a-form-item>
        </template>
<!--      </template>-->

      <!--排行榜不展示-->
      <template v-if="defaultVar.chartType!=12">
        <!--存储类型：堆叠图不显示-->
        <a-form-item field="storageType" label="存储类型" v-if="defaultVar.chartType!=4">
          <a-radio-group v-model="item.storageType" @change="handleStorage(index)">
            <a-radio v-for="(item,index) in datav_storage_type" :value="Number(item.value)" :key="index">
              {{ item.label }}
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <!--数值类型 只有饼图||水滴图||仪表盘||柱状图||条形图||折线图||面积图 &&存储类型为变化值展示-->
        <a-form-item field="changeType" label="数值类型"
                     v-if="(![4,13,12].includes(defaultVar.chartType)) && item.storageType==1 && defaultVar.dateDim!=70">
          <a-radio-group v-model="item.changeType">
            <a-radio v-for="(item,index) in datav_change_type" :key="index" :value="Number(item.value)"
                     v-show="item.value!=1">{{ item.label }}
            </a-radio>
          </a-radio-group>
        </a-form-item>

        <!-- 基准值 只有水滴图展示-->
        <a-form-item field="baseValue" label="基准值" v-if="defaultVar.chartType==8">
          <a-input-number hideButton v-model="item.baseValue" placeholder="请输入"/>
        </a-form-item>
      </template>

      <!--数据转换-->
      <a-form-item v-if="![13, 11].includes(defaultVar.chartType)" field="conversion" label="数据转换">
        <a-select v-model="item.conversion" placeholder="请选择" allow-clear>
          <a-option :value="item.label" v-for="(item,index) in datav_data_conversion" :key="index">
            {{ item.label }}
          </a-option>
        </a-select>
      </a-form-item>

      <!--单位换算-->
      <a-form-item v-if="[14].includes(defaultVar.chartType)" field="unitConvert" label="单位换算">
        <a-checkbox v-model="item.unitConvert">自动换算</a-checkbox>
      </a-form-item>

      <!--背景颜色-->
      <a-form-item :field="`valList[${index}].backgroundColor`"
                   :class="item.backgroundColor==''?'color-wrapper':''"
                   v-if="[14].includes(defaultVar.chartType)"
                   label="背景颜色"
                   :rules="[{required:true,message:'请选择背景颜色'}]">
        <a-color-picker
            v-model="item.backgroundColor"
            :historyColors="history"
            showHistory
            showPreset
            @popup-visible-change="addHistory"
        />
        <icon-close-circle class="delete-icon" @click="handleBgColor(item,index)"/>
      </a-form-item>
    </a-form>
  </a-card>

  <!--弹窗-->
  <select-var-modal :visible="selectVarVisible" :varType="defaultVar.chartType==13?2:1"
                    :is-accumulation="defaultVar.chartType==4?1:0" @cancel="handleModalClose"
                    @add="handleDeviceVarSelect"/>
</template>

<script setup lang="ts">
import {getCurrentInstance, nextTick, onMounted, ref, watchEffect} from "vue";
import {FormInstance} from "@arco-design/web-vue/es/form";
import SelectVarModal from "@/views/manage/station/components/select-var-modal/index.vue";

const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {datav_storage_type, datav_change_type,datav_data_conversion} = proxy?.useDict("datav_storage_type", "datav_change_type", "datav_data_conversion");
// 弹窗显示隐藏
const selectVarVisible = ref(false);
const props = defineProps({
  varData: {
    type: Array,
    default: []
  },
  defaultVar: {
    type: Object,
    default: {}
  },
  addVarList: {
    type: Boolean,
    default: false,
  }
});
const emit = defineEmits(["addVarListInit"])
const formRef = ref<FormInstance>();

// 初始化数据配置
const initDataConfig = () => {
  return {
    chain: false,
    yoy: false,
    storageType: 1, //存储类型
    changeType: 1, //数值类型
    unit: '', //指标单位
    color: "", //图例颜色
    baseValue: 0,
    backgroundColor:"", //背景颜色
    conversion: '原汁原味', //数据转换
    unitConvert: false, //单位换算
  }
};

const rules: any = [{}];
const nowChildrenIndex = ref<number>(0);
const history = ref(['#165DFF']);
const form = ref<any>({
  varList: [initDataConfig()], //数据区域
});

// 存储类型切换
const handleStorage = (index: number) => {
  if (props.defaultVar.dateDim == 70) {
    form.value.varList[index].changeType = 1;
  } else {
    form.value.varList[index].changeType = 2;
  }
};

// 颜色选择器历史颜色
const addHistory = (visible: boolean, color: string) => {
  if (!visible) {
    const index = history.value.indexOf(color);
    if (index !== -1) {
      history.value.splice(index, 1);
    }
    history.value.unshift(color);
  }
};

// 删除图例颜色
const handleColor = async (item: any, index: number) => {
  form.value.varList[index].color = "";
  await nextTick();
  if (item.color == "#FF0000") {
    form.value.varList[index].color = "";
  }
};

const addVarListMethod = () => {
  const newConfig = initDataConfig();
  form.value.varList.push(newConfig);
  emit("addVarListInit")
}

// 选择变量
const chooseVar = (index: any) => {
  selectVarVisible.value = true;
  nowChildrenIndex.value = index;
};

// 关闭变量弹窗
const handleModalClose = () => {
  selectVarVisible.value = false;
};

// 确认添加
const handleDeviceVarSelect = (record: any) => {
  console.log('record', record);
  form.value.varList[nowChildrenIndex.value].unit = record.unit;
  form.value.varList[nowChildrenIndex.value].varSn = record.varSn;
  form.value.varList[nowChildrenIndex.value].varName = record.varName;
  handleModalClose();
};

// 删除变量
const handleRemove = (index: number) => {
  form.value.varList[index].unit = "";
  form.value.varList[index].varSn = "";
  form.value.varList[index].varName = "";
}

// 删除背景颜色
const handleBgColor = async (item: any, index: number) => {
  form.value.varList[index].backgroundColor = "";
  await nextTick();
  if (item.backgroundColor == "#FF0000") {
    form.value.varList[index].backgroundColor = "";
  }
};

watchEffect(() => {
  if (props.addVarList) {
    addVarListMethod();
  }
  if (props.varData.length > 0) {
    form.value.varList[0].unit = props.varData[0].unit;
    form.value.varList[0].varName = props.varData[0].varName;
    form.value.varList[0].varSn = props.varData[0].varSn;
  } else {
    form.value.varList[0].unit = '';
    form.value.varList[0].varName = '';
    form.value.varList[0].varSn = '';
  }

  // 变化值初始化
  if (props.defaultVar.dateDim == 70) {
    form.value.varList.map((val: any) => {
      val.changeType = 1;
    })
  } else if (!props.defaultVar.isDetail) {
    form.value.varList.map((val: any) => {
      val.changeType = 2;
    })
  }
  // console.log(form.value.varList);
});

defineExpose({
  form,
  formRef,
  setForm: async (data: any) => {
    if (data.varList.length == 0) {
      form.value.varList = [initDataConfig()];
    } else {
      if (props.defaultVar.chartType == 6) {
        // console.log(data.varList.slice(0, 1));
        form.value.varList = data.varList.slice(0, 1);
      } else {
        form.value = data;
      }
      await nextTick();
    }
  },
});

</script>

<style scoped lang="less">
.color-wrapper {
  :deep(.arco-color-picker-size-medium .arco-color-picker-preview) {
    background: none !important;
  }
}

.children-card {
  :deep(.arco-card-header) {
    background: var(--color-neutral-3);
  }
}
</style>