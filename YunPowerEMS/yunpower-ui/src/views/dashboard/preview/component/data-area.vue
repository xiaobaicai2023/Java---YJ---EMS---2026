<template>
  <!--模块名称-->
  <a-row :gutter="[0, 12]">
    <a-col v-for="(item,index) in form.varList" :key="index">
      <a-card :title="`数据区${index+1}`">
        <template #extra>
          <a-button v-if="index!=0" type="primary" status="danger" @click="deleteData(index)">
            移除
          </a-button>
        </template>
        <a-form ref="formRef" :model="item" auto-label-width>
          <!--指定变量展示-->
          <template v-if="defaultVal.indicator==1">
            <!--状态量-->
            <template v-if="[13].includes(defaultVal.chartType)">
              <a-form-item field="equipType" label="设备类型">
                <a-radio-group v-model="item.equipType">
                  <a-radio :value="Number(item.value)" v-for="(item,index) in datav_equip_type" :key="index">
                    {{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!--状态类型-->
              <a-form-item v-if="item.equipType==1" field="stateType" label="状态类型" class="input-width">
                <a-select v-model="item.stateType" placeholder="请选择" allow-clear>
                  <a-option v-for="(item,index) in datav_state_type" :value="item.value" :key="index">{{ item.label }}
                  </a-option>
                </a-select>
              </a-form-item>

              <template v-if="item.equipType==2">
                <!--选择变量-->
                <a-form-item field="varName" label="选择变量" class="input-width">
                  <a-input-search search-button v-model="item.varName" button-text="选择变量" @click="chooseVar(index)"
                                  readonly>
                  </a-input-search>
                </a-form-item>

                <!--变量编码-->
                <a-form-item field="varSn" label="变量编码">
                  <a-input v-model="item.varSn" disabled/>
                </a-form-item>

                <!--图例名称-->
                <a-form-item field="legend" label="图例名称">
                  <a-input v-model="item.legend"/>
                </a-form-item>
              </template>
            </template>

            <template v-else>
              <!--选择变量-->
              <a-form-item v-if="defaultVal.chartType!=21" field="varName" label="选择变量" class="input-width">
                <a-input-search search-button v-model="item.varName" readonly @click="chooseVar(index)">
                  <template #button-default>
                    <div>选择变量</div>
                  </template>
                </a-input-search>
              </a-form-item>

              <!--变量编码-->
              <a-form-item v-if="defaultVal.chartType!=21" field="varSn" label="变量编码">
                <a-input v-model="item.varSn" disabled/>
              </a-form-item>

              <!--图例名称-->
              <a-form-item field="legend" label="图例名称">
                <a-input v-model="item.legend"/>
              </a-form-item>

              <!--指标单位-->
              <a-form-item field="unit" label="指标单位">
                <a-input v-model="item.unit" :max-length="6"/>
              </a-form-item>

            </template>
          </template>

          <!--通用变量展示-->
          <a-form-item v-if="defaultVal.indicator==2" field="stateType" label="通用变量" class="input-width">
            <a-select v-model="item.comVariable" placeholder="请选择" allow-clear>
              <a-option :value="item.value" v-for="(item,index) in sys_config_variable_type" :key="index">
                {{ item.label }}
              </a-option>
            </a-select>
          </a-form-item>

          <!--显示电费-->
          <a-form-item field="showCharge" v-if="defaultVal.chartType==4" label="显示电费">
            <a-checkbox v-model="item.showCharge">是</a-checkbox>
          </a-form-item>

          <!-- 状态量-->
          <template v-if="![13,4, 7, 21].includes(defaultVal.chartType)">
            <a-form-item field="storageType" label="存储类型">
              <a-radio-group v-model="item.storageType" @change="handleStorage(index)">
                <a-radio v-for="(item,index) in datav_storage_type" :value="Number(item.value)" :key="index">
                  {{ item.label }}
                </a-radio>
              </a-radio-group>
            </a-form-item>
            <!--数值类型-->
            <a-form-item field="changeType" label="数值类型" v-if="item.storageType==1 && defaultVal.dateDim!=70">
              <a-radio-group v-model="item.changeType">
                <!--                  <template v-if="defaultVal.dateDim==70">-->
                <!--                    <span v-for="(item,index) in datav_change_type" :key="index" ></span>-->
                <!--                    <a-radio v-for="(item,index) in datav_change_type" :key="index" v-show="item.value=='1'" :value="Number(item.value)">-->
                <!--                    </a-radio>-->
                <!--                  </template>-->
                <a-radio v-for="(item,index) in datav_change_type" :key="index" :value="Number(item.value)"
                         v-show="item.value!=1">{{ item.label }}
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </template>

          <!--数据转换-->
          <a-form-item v-if="![13, 11].includes(defaultVal.chartType)" field="conversion" label="数据转换">
            <a-select v-model="item.conversion" placeholder="请选择" allow-clear>
              <a-option :value="item.label" v-for="(item,index) in datav_data_conversion" :key="index">
                {{ item.label }}
              </a-option>
            </a-select>
          </a-form-item>

          <!--单位换算-->
          <a-form-item v-if="[14].includes(defaultVal.chartType)" field="unitConvert" label="单位换算">
            <a-checkbox v-model="item.unitConvert">自动换算</a-checkbox>
          </a-form-item>

          <!--饼图、状态量不展示-->
          <template v-if="![6,13,4, 7, 19, 21].includes(defaultVal.chartType)">

            <!--同比环比 排行榜、水滴图、仪表盘不显示-->
            <a-form-item label="辅助指标" :content-flex="false" :merge-props="false"
                         v-if="![12,8,9].includes(defaultVal.chartType)">
              <a-row style="margin-top: 8px;">
                <a-col flex="80px">
                  <a-form-item field="chain" no-style>
                    <a-checkbox v-model="item.chain" :value="1">环比</a-checkbox>
                  </a-form-item>
                </a-col>
                <a-col flex="80px">
                  <a-form-item field="yoy" no-style>
                    <a-checkbox v-model="item.yoy" :value="1">同比</a-checkbox>
                  </a-form-item>
                </a-col>
                <template v-if="defaultVal.chartType==14">
                  <a-col flex="80px">
                    <a-form-item field="showCoal" no-style>
                      <a-checkbox v-model="item.showCoal" :value="1">标准煤</a-checkbox>
                    </a-form-item>
                  </a-col>
                  <a-col flex="80px">
                    <a-form-item field="showTree" no-style>
                      <a-checkbox v-model="item.showTree" :value="1">植树量</a-checkbox>
                    </a-form-item>
                  </a-col>
                </template>
              </a-row>
            </a-form-item>

            <!--显示类型 水滴图不展示-->
            <a-form-item v-if="![8].includes(defaultVal.chartType)" field="percentage" label="显示类型">
              <a-radio-group v-model="item.percentage" @change="handlePercentage(index)">
                <template v-if="defaultVal.chartType==14" v-for="(item,index) in datav_percentage" :key="index">
                  <a-radio :value="Number(item.value)" v-if="item.value!=3">{{ item.label }}</a-radio>
                </template>
                <template v-else>
                  <a-radio v-for="(item,index) in datav_percentage" :key="index" :value="Number(item.value)">
                    {{ item.label }}
                  </a-radio>
                </template>
              </a-radio-group>
            </a-form-item>

            <!--基准值-->
            <a-form-item field="baseValue" label="基准值"
                         v-if="item.percentage == 2 || item.percentage == 3 || defaultVal.chartType==8">
              <a-input-number v-model="item.baseValue" hideButton placeholder="请输入"/>
            </a-form-item>

            <template v-if="item.percentage==1 && defaultVal.chartType==9">
              <!-- 最小值-->
              <a-form-item label="最小值">
                <a-input-number v-model="item.minValue" placeholder="请输入" hide-button/>
              </a-form-item>

              <!-- 最大值-->
              <a-form-item label="最大值">
                <a-input-number v-model="item.maxValue" placeholder="请输入" hide-button/>
              </a-form-item>
            </template>
          </template>

          <!--排行榜、饼图、水滴图、仪表图、状态量不显示-->
          <template v-if="![12,6,8,9,4, 7, 19, 21].includes(defaultVal.chartType)">

            <!--图例颜色-->
            <a-form-item field="color" :class="item.color==''?'color-wrapper':''" label="图例颜色"
                         v-if="[1, 2, 3, 5].includes(defaultVal.chartType)">
              <a-color-picker
                  v-model="item.color"
                  :historyColors="history"
                  showHistory
                  showPreset
                  @popup-visible-change="addHistory"
              />
              <icon-close-circle class="delete-icon" @click="handleDeleteColor(item, index, 'color')"/>
            </a-form-item>

            <!--字体颜色-->
            <a-form-item :field="`valList[${index}].fontColor`"
                         :class="item.fontColor==''?'color-wrapper':''"
                         v-if="[14].includes(defaultVal.chartType)"
                         label="字体颜色">
              <a-color-picker
                  v-model="item.fontColor"
                  :historyColors="history"
                  showHistory
                  showPreset
                  format="rgb"
                  @popup-visible-change="addHistory"
              />
              <icon-close-circle class="delete-icon" @click="handleDeleteColor(item,index, 'fontColor')"/>
            </a-form-item>

            <!--背景颜色-->
            <a-form-item :field="`valList[${index}].backgroundColor`"
                         :class="item.backgroundColor==''?'color-wrapper':''"
                         v-if="[14].includes(defaultVal.chartType)"
                         label="背景颜色"
                         :rules="[{required:true,message:'请选择背景颜色'}]">
              <a-color-picker
                  v-model="item.backgroundColor"
                  :historyColors="history"
                  showHistory
                  showPreset
                  format="rgb"
                  @popup-visible-change="addHistory"
              />
              <icon-close-circle class="delete-icon" @click="handleDeleteColor(item,index, 'backgroundColor')"/>
            </a-form-item>

            <!--碳量换算-->
            <a-form-item v-if="[14].includes(defaultVal.chartType)" field="coalConvert" label="碳量换算">
              <a-radio-group v-model="item.coalConvert">
                <a-radio :value="1">是</a-radio>
                <a-radio :value="0">否</a-radio>
              </a-radio-group>
            </a-form-item>

            <template v-if="!item.coalConvert">
              <!--ICON-->
              <a-form-item field="icon" label="ICON" class="input-width" v-if="[14, 13].includes(defaultVal.chartType)">
                <select-icon @change="selectIconChange">
                  <a-input v-model="item.icon" :placeholder="$t('manage.account.menu.chooseIcon')"
                           allow-clear/>
                </select-icon>
              </a-form-item>

              <!--ICON颜色-->
              <a-form-item v-if="[14].includes(defaultVal.chartType)" field="color" label="ICON颜色"
                           :class="item.iconColor==''?'color-wrapper':''">
                <a-color-picker
                    v-model="item.iconColor"
                    :historyColors="history"
                    showHistory
                    showPreset
                    @popup-visible-change="addHistory"
                />
                <icon-close-circle class="delete-icon" @click="handleDeleteColor(item, index,'iconColor')"/>
              </a-form-item>
            </template>
          </template>

          <a-form-item field="baseValue" label="固定值" v-if="[21].includes(defaultVal.chartType)">
            <a-input-number v-model="item.baseValue" hideButton placeholder="请输入"/>
          </a-form-item>
        </a-form>
      </a-card>
    </a-col>
    <a-col v-if="![14, 4, 7, 8, 9, 13,21].includes(defaultVal.chartType)">
      <a-button type="primary" @click="handleAddDataArea">添加数据区</a-button>
    </a-col>

    <!--弹窗-->
    <select-var-modal :visible="selectVarVisible"
                      :varType="defaultVal.chartType==13?2:1"
                      :is-accumulation="defaultVal.chartType==4?1:0"
                      :dept-id="cardForm.deptId"
                      @cancel="handleModalClose"
                      @add="handleDeviceVarSelect"/>
  </a-row>
</template>

<script setup lang="ts">
import {getCurrentInstance, onMounted, ref, nextTick, watchEffect} from "vue";
import SelectVarModal from "@/views/manage/station/components/select-var-modal/index.vue";
import selectIcon from "@/components/select-icon/index.vue";
import {FormInstance} from "@arco-design/web-vue/es/form";

const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_config_variable_type,
  datav_equip_type,
  datav_state_type,
  datav_storage_type,
  datav_change_type,
  datav_percentage,
  datav_data_conversion,
} = proxy?.useDict("sys_config_variable_type", "datav_equip_type", "datav_state_type", "datav_storage_type", "datav_change_type", "datav_percentage", "datav_data_conversion");

const props = defineProps({
  defaultVal: {
    type: Object,
    default: {}
  },
  cardForm: {
    type: Object,
    default: {}
  }
});

// 弹窗显示隐藏
const selectVarVisible = ref<boolean>(false);
// 当前选中
const nowCheckIndex = ref<any>(null);
// 初始化数据配置
const initDataConfig = () => {
  return {
    comVariable: "", //通用变量（用电量 发电量 电流 电压 有功功率
    varSn: "",
    varName: "", //名称
    legend: "",
    color: "",
    iconColor: '',
    unit: "",
    percentage: 1,
    baseValue: 0,
    minValue: 0,
    maxValue: 0,
    storageType: 1,
    changeType: 1,
    chain: false,
    yoy: false,
    showCoal: false,
    showTree: false,
    backgroundColor: "",
    fontColor: '', //字体颜色
    equipType: 1,
    stateType: undefined,
    unitConvert: false, //单位换算
    showCharge: false, //显示电费
    conversion: '原汁原味', //数据转换
    coalConvert: 0, //碳量换算
  }
};

const formRef = ref<FormInstance>();

const form = ref<any>({
  varList: [{...initDataConfig()}], //数据区域
});

const history = ref(['#165DFF']);
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

// 选择变量
const chooseVar = (index: number) => {
  selectVarVisible.value = true;
  nowCheckIndex.value = index;
};

// 移除数据区
const deleteData = (index: number) => {
  form.value.varList.splice(index, 1);
};

// 添加数据区
const handleAddDataArea = () => {
  const newConfig = initDataConfig();
  form.value.varList.push(newConfig);
};

// 关闭变量弹窗
const handleModalClose = () => {
  selectVarVisible.value = false;
};

// 定义对象类型
type MyObjectType = {
  [key: string]: any;
};

// 合并对象
function mergeObjects(...objects: MyObjectType[]): MyObjectType {
  return Object.assign({}, ...objects);
};

// 选择变量
const handleDeviceVarSelect = (record: any) => {
  let obj = {
    varSn: record.varSn,
    varName: record.deviceName + '/' + record.varName,
    legend: record.varName,
    unit: record.unit
  };
  let form_obj = form.value.varList[nowCheckIndex.value];
  form.value.varList[nowCheckIndex.value] = mergeObjects(form_obj, obj);
  selectVarVisible.value = false;
};

// 选择icon
const selectIconChange = (iconName: string) => {
  form.value.varList[0].icon = iconName;
};

const handleDeleteColor = async (item: any, index: number, type: string) => {
  form.value.varList[index][type] = "";
  await nextTick();
  if (item[type] == "#FF0000" || item[type] == "rgb(255, 0, 0)") {
    form.value.varList[index][type] = "";
  }
};

// 存储类型切换
const handleStorage = (index: number) => {
  if (props.defaultVal.dateDim == 70) {
    form.value.varList[index].changeType = 1;
  } else {
    form.value.varList[index].changeType = 2;
  }
};

// 显示类型切换
const handlePercentage = (index: number) => {
  form.value.varList[index].baseValue = 0;
  form.value.varList[index].minValue = 0;
  form.value.varList[index].maxValue = 0;
};

const formInit = () => {
  if (props.defaultVal.dateDim == 70) {
    form.value.varList.map(val => {
      val.changeType = 1;
    })
  } else if (!props.defaultVal.isDetail) {
    form.value.varList.map(val => {
      val.changeType = 2;
    })
  }
}

watchEffect(() => {
  formInit();
})


defineExpose({
  form,
  formRef,
  setForm: async (data: any) => {
    if (data.varList.length == 0) {
      form.value.varList = [{...initDataConfig()}];
    } else {
      form.value = data;
      await nextTick();
    }
  }
});

</script>

<style lang="less" scoped>
.input-width {
  width: 500px;
}

.delete-icon {
  font-size: 16px;
  margin-left: 6px;
}

.color-wrapper {
  :deep(.arco-color-picker-size-medium .arco-color-picker-preview) {
    background: none !important;
  }
}

</style>
