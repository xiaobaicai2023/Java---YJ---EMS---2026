<template>
  <a-modal width="70%" :modal-style="{height:'85%'}" :body-style="{height:'100%'}" v-model:visible="props.visible"
           :closable="false" :footer="false">
    <div class="edit-wrapper" v-if="props.visible">
      <!--      左侧-->
      <div class="edit-left">
        <edit-left :var-data="varData"
                   :data="data"
                   :companyData="companyData"
                   @handleVarChange="handleVarChange"
                   @handleVarDelete="handleVarDelete"></edit-left>
      </div>

      <div class="edit-right">
        <div style="height:350px">
          <!--  修改的组件样式-->
          <component
              :is="data.staticType == 1 ? 'card-chart-pie-statistics' : data.staticType == 2 || data.staticType == 3 ? 'large-combine' : EnumChartType[data.chartType]"
              :public="{...data.public, chartType: data.chartType, preview: 1, tablePre: 1, waterPre: 0}"
              style="margin-top: 0">
          </component>
        </div>

        <!-- 基础设置-->
        <!-- 专用模板-->
        <base-dedicated :defaultVar="defaultVar" v-if="[15,16,17,18].includes(data.chartType)" ref="baseDedicatedRef"/>
        <!-- 基础模板-->
        <baseSetting :defaultVar="defaultVar" @handleDateDim="handleDateDim" v-else ref="baseSettingRef"/>

        <!--详细设置 专用模板不展示-->
        <template v-if="![15,16,17,18].includes(data.chartType)">
          <detailed-panel :varData="varData" :defaultVar="defaultVar" v-if="data.chartType==9" ref="detailedPanelRef"/>
          <detailed-status :chart-type="data.chartType" v-else-if="data.chartType==13" ref="detailedStatusRef"/>
          <detailed :defaultVar="defaultVar" :varData="varData" v-else ref="detailedRef" :addVarList="addVarList"
                    @addVarListInit="addVarListInit"/>
          <!--          <detailed :chart-type="data.chartType" :unit="defaultVar.varChildren[0]?.unit" v-else ref="detailedRef" />-->
        </template>

        <!--辅助设置 饼图、水滴图、仪表盘、专用模板不展示-->
        <template v-if="![6,8,9,13,12,14,15,16,17,18].includes(data.chartType)">
          <auxiliary :chart-type="data.chartType" ref="auxiliaryRef"/>
        </template>

        <!--数据区-->
        <a-card class="children-card" v-if="![6,15,16,17,18].includes(data.chartType)" v-for="(item,index) in children">
          <template #title>
            {{ data.chartType == 12 ? '数据区' : '右数据区' }}{{ index + 1 }}
            <a-tooltip v-if="childLength"
                       :content="`最多能添加${childLength}个${data.chartType == 12 ?'数据区':'右数据区'}`"
                       position="top" mini>
              <icon-exclamation-circle style="color:var(--color-neutral-8);cursor: pointer"/>
            </a-tooltip>
          </template>
          <template #extra>
            <a-button type="primary" status="danger" @click="deleteData(index)">
              移除
            </a-button>
          </template>
          <a-form :model="item" auto-label-width>
            <!--水滴图-->
            <template v-if="data.chartType == 8">
              <a-form-item label="选择变量">
                <a-button type="outline" size="small" @click="chooseVar(index)">选择变量</a-button>
                <a-space>
                  <a-tag closable v-if="item.varSn" @close="handleRemove(item,index)"> {{ item.varName }}</a-tag>
                </a-space>
              </a-form-item>

              <!--指标单位-->
              <a-form-item label="指标单位">
                <a-input v-model="item.unit" placeholder="请输入"/>
              </a-form-item>

              <a-form-item label="存储类型">
                <a-radio-group v-model="item.storageType" @change="handleStorage(index)">
                  <a-radio v-for="(item,index) in datav_storage_type" :value="Number(item.value)" :key="index">
                    {{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!--数值类型 存储类型为变化值展示-->
              <a-form-item label="数值类型" v-if="item.storageType==1 && defaultVar.dateDim!=70">
                <a-radio-group v-model="item.changeType">
                  <a-radio v-for="(item,index) in datav_change_type" :key="index" :value="Number(item.value)"
                           v-show="item.value!=1">{{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!-- 基准值-->
              <a-form-item label="基准值">
                <a-input-number hideButton v-model="item.baseValue" placeholder="请输入"/>
              </a-form-item>

              <!--数据转换-->
              <a-form-item field="conversion" label="数据转换">
                <a-select v-model="item.conversion" placeholder="请选择" allow-clear>
                  <a-option :value="item.label" v-for="(item,index) in datav_data_conversion" :key="index">
                    {{ item.label }}
                  </a-option>
                </a-select>
              </a-form-item>
            </template>
            <!--仪表盘-->
            <template v-else-if="data.chartType == 9">
              <!--选择变量-->
              <a-form-item label="选择变量">
                <a-button type="outline" size="small" @click="chooseVar(index)">选择变量</a-button>
                <a-space>
                  <!--                  <a-tag-->
                  <!--                      v-for="(tag, childindex) of item.varList"-->
                  <!--                      :key="index"-->
                  <!--                      closable-->
                  <!--                      @close="handleRemove(item,index,childindex)"-->
                  <!--                  >-->
                  <!--                    {{ tag.varName }}-->
                  <!--                  </a-tag>-->
                  <a-tag closable v-if="item.varSn" @close="handleRemove(item,index)"> {{ item.varName }}</a-tag>
                </a-space>
              </a-form-item>
              <!--指标单位-->
              <a-form-item label="指标单位">
                <a-input v-model="item.unit" placeholder="请输入"/>
              </a-form-item>

              <!--存储类型-->
              <a-form-item label="存储类型">
                <a-radio-group v-model="item.storageType" @change="handleStorage(index)">
                  <a-radio v-for="(item,index) in datav_storage_type" :value="Number(item.value)" :key="index">
                    {{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!--数值类型 存储类型为变化值展示-->
              <a-form-item label="数值类型" v-if="item.storageType==1 && defaultVar.dateDim!=70">
                <a-radio-group v-model="item.changeType">
                  <a-radio v-for="(item,index) in datav_change_type" :key="index" :value="Number(item.value)"
                           v-show="item.value!=1">{{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!--显示类型-->
              <a-form-item label="显示类型">
                <a-radio-group v-model="item.percentage" @change="handlePercentage(index)">
                  <a-radio v-for="(item,index) in datav_percentage" :value="Number(item.value)" :key="index">
                    {{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!-- 基准值-->
              <a-form-item label="基准值" v-if="item.percentage==2||item.percentage==3">
                <a-input-number v-model="item.baseValue" hideButton placeholder="请输入"/>
              </a-form-item>

              <!--数据转换-->
              <a-form-item field="conversion" label="数据转换">
                <a-select v-model="item.conversion" placeholder="请选择" allow-clear>
                  <a-option :value="item.label" v-for="(item,index) in datav_data_conversion" :key="index">
                    {{ item.label }}
                  </a-option>
                </a-select>
              </a-form-item>

              <template v-if="item.percentage==1">
                <!-- 最小值-->
                <a-form-item label="最小值">
                  <a-input-number hideButton v-model="item.minValue" placeholder="请输入"/>
                </a-form-item>

                <!-- 最大值-->
                <a-form-item label="最大值">
                  <a-input-number hideButton v-model="item.maxValue" placeholder="请输入"/>
                </a-form-item>
              </template>
            </template>
            <!--状态图-->
            <template v-else-if="data.chartType == 13">
              <!--状态类型-->
              <a-form-item label="状态类型">
                <a-select v-model="item.stateType" placeholder="请选择" allow-clear>
                  <a-option v-for="(item, index) in datav_state_type" :value='Number(item.value)' :key="index">
                    {{ item.label }}
                  </a-option>
                </a-select>
              </a-form-item>
            </template>
            <!--排行榜-->
            <template v-else-if="data.chartType == 12">
              <!--选择变量-->
              <a-form-item label="选择变量">
                <a-button type="outline" size="small" @click="chooseVar(index)">选择变量</a-button>
                <a-space>
                  <a-tag closable v-if="item.varSn" @close="handleRemove(item,index)"> {{ item.varName }}</a-tag>
                </a-space>
              </a-form-item>
              <!--指标单位-->
              <a-form-item label="指标单位">
                <a-input v-model="item.unit" placeholder="请输入"/>
              </a-form-item>
              <!--图例颜色-->
              <a-form-item label="图例颜色" :class="item.color==''?'color-wrapper':''">
                <a-color-picker
                    v-model="item.color"
                    :historyColors="history"
                    showHistory
                    showPreset
                    @popup-visible-change="addHistory"
                />
                <icon-close-circle class="delete-icon" @click="handleColor(item, index)"/>
              </a-form-item>

              <!--数据转换-->
              <a-form-item field="conversion" label="数据转换">
                <a-select v-model="item.conversion" placeholder="请选择" allow-clear>
                  <a-option :value="item.label" v-for="(item,index) in datav_data_conversion" :key="index">
                    {{ item.label }}
                  </a-option>
                </a-select>
              </a-form-item>
            </template>
            <!--单值-->
            <template v-else-if="data.chartType == 14">
              <!--选择变量-->
              <a-form-item label="选择变量">
                <a-button type="outline" size="small" @click="chooseVar(index)">选择变量</a-button>
                <a-space>
                  <!--                  <a-tag-->
                  <!--                      v-for="(tag, childindex) of item.varList"-->
                  <!--                      :key="childindex"-->
                  <!--                      closable-->
                  <!--                      @close="handleRemove(item, index, childindex)"-->
                  <!--                  >-->
                  <!--                    {{ tag.varName }}-->
                  <!--                  </a-tag>-->
                  <a-tag closable v-if="item.varSn" @close="handleRemove(item,index)"> {{ item.varName }}</a-tag>
                </a-space>
              </a-form-item>

              <a-form-item label="指标单位">
                <a-input v-model="item.unit" placeholder="请输入"/>
              </a-form-item>

              <a-form-item label="存储类型">
                <a-radio-group v-model="item.storageType" @change="handleStorage(index)">
                  <a-radio v-for="(item,index) in datav_storage_type" :value="Number(item.value)" :key="index">
                    {{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!--数值类型 存储类型为变化值展示-->
              <a-form-item label="数值类型" v-if="item.storageType==1 && defaultVar.dateDim!=70">
                <a-radio-group v-model="item.changeType">
                  <a-radio v-for="(item,index) in datav_change_type" :key="index" :value="Number(item.value)"
                           v-show="item.value!=1">{{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!--数据转换-->
              <a-form-item field="conversion" label="数据转换">
                <a-select v-model="item.conversion" placeholder="请选择" allow-clear>
                  <a-option :value="item.label" v-for="(item,index) in datav_data_conversion" :key="index">
                    {{ item.label }}
                  </a-option>
                </a-select>
              </a-form-item>

              <!--单位换算-->
              <a-form-item field="unitConvert" label="单位换算">
                <a-checkbox v-model="item.unitConvert">自动换算</a-checkbox>
              </a-form-item>
            </template>
            <!--其他-->
            <template v-else>
              <!--图例名称-->
              <a-form-item label="图例名称">
                <a-input v-model="item.legend" placeholder=""/>
              </a-form-item>
              <!--选择变量-->
              <a-form-item label="选择变量">
                <a-button type="outline" size="small" @click="chooseVar(index)">选择变量</a-button>
                <a-space>
                  <a-tag closable v-if="item.varSn" @close="handleRemove(item,index)"> {{ item.varName }}</a-tag>
                </a-space>
              </a-form-item>
              <!--指标单位-->
              <a-form-item label="指标单位">
                <a-input v-model="item.unit" placeholder=""/>
              </a-form-item>

              <!--存储类型-->
              <a-form-item label="存储类型">
                <a-radio-group v-model="item.storageType">
                  <a-radio v-for="(item,index) in datav_storage_type" :value="Number(item.value)" :key="index">
                    {{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!--数值类型 存储类型为变化值展示-->
              <a-form-item label="数值类型" v-if="item.storageType==1 && defaultVar.dateDim!=70">
                <a-radio-group v-model="item.changeType">
                  <a-radio v-for="(item,index) in datav_change_type" :key="index" :value="Number(item.value)"
                           v-show="item.value!=1">{{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!--显示类型-->
              <a-form-item label="显示类型">
                <a-radio-group v-model="item.percentage" @change="handlePercentage(index)">
                  <a-radio v-for="(item,index) in datav_percentage" :value="Number(item.value)" :key="index">
                    {{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <!-- 基准值-->
              <a-form-item label="基准值" v-if="item.percentage==2||item.percentage==3">
                <a-input-number v-model="item.baseValue" hideButton placeholder="请输入"/>
              </a-form-item>

              <!--数据转换-->
              <a-form-item v-if="![13, 11].includes(defaultVar.chartType)" field="conversion" label="数据转换">
                <a-select v-model="item.conversion" placeholder="请选择" allow-clear>
                  <a-option :value="item.label" v-for="(item,index) in datav_data_conversion" :key="index">
                    {{ item.label }}
                  </a-option>
                </a-select>
              </a-form-item>
            </template>
          </a-form>
        </a-card>

        <a-space class="form-footer">
          <template v-if="![4,6,15,16,17,18].includes(data.chartType)">
            <a-button type="primary" class="preview-button" @click="addData"
                      :disabled="children?.length >= childLength">
              {{ data.chartType == 12 ? '添加数据区' : '添加右数据区' }}
            </a-button>
          </template>
          <a-button @click="submit" type="primary">保存</a-button>
          <a-button @click="closeModal">取消</a-button>
        </a-space>
      </div>

      <!--弹窗-->
      <select-var-modal :visible="selectVarVisible"
                        :varType="defaultVar.chartType==13?2:1"
                        :is-accumulation="defaultVar.chartType==4?1:0"
                        :station-type="Number(companyData?.stationType)"
                        :deptId="companyData?.deptId"
                        @cancel="handleModalClose"
                        @add="handleDeviceVarSelect"/>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import {getCurrentInstance, nextTick, onMounted, reactive, ref, watchEffect} from "vue";
import EditLeft from "@/views/manage/station/components/edit-left/index.vue";
import Auxiliary from "@/views/manage/station/components/auxiliary.vue";
import baseSetting from "@/views/manage/station/components/baseSetting.vue";
import Detailed from "@/views/manage/station/components/detailed.vue";
import DetailedPanel from "@/views/manage/station/components/detailed-panel.vue";
import DetailedStatus from "@/views/manage/station/components/detailed-status.vue";
import BaseDedicated from "@/views/manage/station/components/base-dedicated.vue";
import SelectVarModal from "@/views/manage/station/components/select-var-modal/index.vue";
import {Notification} from "@arco-design/web-vue";
import {DefaultValueData, EnumChartType} from "@/views/manage/station/components/index";
import {updateConfigCard, getConfigCard} from "@/api/manage/station/edit";
import {useRoute, useRouter} from "vue-router";
import useLoading from '@/hooks/loading';
import {getConfigInfo} from "@/api/dashboard/api";


const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  datav_storage_type,
  datav_change_type,
  datav_percentage,
  datav_state_type,
  datav_data_conversion
} = proxy?.useDict("datav_storage_type",
    "datav_change_type", "datav_percentage", "datav_state_type", "datav_data_conversion");
const props = defineProps({
  // 弹窗数据
  data: {
    type: Object,
    default: () => {
      return {
        chartType: 2
      }
    }
  },
  //弹窗
  visible: {
    type: Boolean,
    default: true
  },
});

const emit = defineEmits(['onConfirm', 'input', 'closeModal']);

// 获取form
const baseSettingRef = ref<any>(null);
const baseDedicatedRef = ref<any>(null);
const detailedRef = ref<any>(null);
const detailedStatusRef = ref<any>(null);
const detailedPanelRef = ref<any>(null);
const auxiliaryRef = ref<any>(null);
const editLeftRef = ref<any>(null);

// 子数据
const children = ref<any>([]);
// 子数据区域长度限制
const childLength = ref<any>(0);
// 弹窗显示隐藏
const selectVarVisible = ref(false);
// 当前选择子数据
const nowChildrenIndex = ref<number>(0);
// 默认数据
const defaultVar = ref<any>([]);
// 中转数据
const varData = ref<any>([]);
//路由
const router = useRouter();
const route = useRoute();

const {loading, setLoading} = useLoading(true);
// 左侧公司基本信息
const companyData = ref<any>({});
// 当前信息id
const id = ref<any>(undefined);

// 右数据区初始化配置
const defaultChildConfig = () => {
  return {
    legend: '', //图例名称
    comVariable: '', //通用变量
    percentage: 1, //是否百分比
    baseValue: 0, //基准值
    storageType: 1, //存储类型
    changeType: 1, //数值类型
    minValue: 0, //最小值
    maxValue: 0, //最大值
    stateType: undefined,//状态类型
    unit: '', //指标单位
    color: "", //图例颜色,
    varSn: "",
    varName: "",
    conversion: '原汁原味', //数据转换
    unitConvert: false, //单位换算
  }
  // return {
  //
  // }
};

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

// 删除图例颜色
const handleColor = async (item: any, index: number) => {
  children.value[index].color = "";
  await nextTick();
  if (item.color == "#FF0000") {
    children.value[index].color = "";
  }
};

const addVarList = ref<boolean>(false);

// 子数据
const addData = () => {
  if (defaultVar.value.chartType == 12) {
    addVarList.value = true;
  } else {
    const data = defaultChildConfig();
    if (defaultVar.value.dateDim == 70) {
      data.changeType = 1;
    } else {
      data.changeType = 2;
    }
    if (!children.value) {
      children.value = [];
    }
    children.value.push(data);
  }
};

const addVarListInit = () => {
  addVarList.value = false;
}

// 长度限制
const maxLength = () => {
  if ([2, 3, 1, 5].includes(props.data.chartType)) {
    return 4;
  } else if ([8, 13].includes(props.data.chartType)) {
    return 2;
  } else if ([9].includes(props.data.chartType)) {
    return 1;
  }
};

// 移除子数据
const deleteData = (index: number) => {
  children.value.splice(index, 1);
};

//提交
const submit = async () => {
  if (baseSettingRef.value) {
    const validBase = await baseSettingRef.value?.validateForm();
    if (validBase.valid) {
      return;
    }
  }

  if (baseDedicatedRef.value) {
    const validDedicated = await baseDedicatedRef.value?.validateForm();
    if (validDedicated.valid) {
      return;
    }
  }

  setLoading(true);
  const base = baseSettingRef.value?.form;
  const dedicatedData = baseDedicatedRef.value?.form;
  const detailData = detailedRef.value?.form;
  const statusData = detailedStatusRef.value?.form;
  const panelData = detailedPanelRef.value?.form;
  const auxiliary = auxiliaryRef.value?.form;
  const form = {
    dashboardConfigId: props.data.dashboardConfigId,
    indicator: 1,
    ...defaultVar.value,
    ...base,
    ...dedicatedData,
    cardConfig: {
      // 默认值
      ...defaultVar.value,
      // 详细设置
      ...detailData,
      ...statusData,
      ...panelData,
      // 辅助设置
      ...auxiliary,
      // 子数据
      children: children.value
    },
  };

  if (defaultVar.value.chartType == 6) {
    form.cardConfig.varList = varData.value;
  }

  if (detailData) {
    form.cardConfig.varList[0] = detailData?.varList[0];
  }

  if (panelData) {
    form.cardConfig.varList[0] = panelData?.varList[0];
  }

  // 处理varList
  if(form.cardConfig.varList) synchronizeObjects(form.cardConfig.varList);

  if ([15, 16].includes(defaultVar.value.chartType)) {
    if (!form.cardConfig.hasOwnProperty('headType')) {
      form.cardConfig.headType = form.headType;
    }
  }

  if ([17, 18].includes(defaultVar.value.chartType)) {
    form.cardConfig.staticType = props.data.staticType ? props.data.staticType : form.staticType;
  }

  if (!props.data.cardKey) {
    form.cardKey = `${Date.now()}`;
    form.dashboardConfigId = route.query.id;
  } else {
    form.cardKey = props.data.cardKey;
  }

  form.id = id.value ? id.value : undefined;
  form.cardConfig = JSON.stringify(form.cardConfig);
  try {
    const res: any = await updateConfigCard(form);
    if (res.code == 200) {
      Notification.success({
        title: '提示',
        content: '添加成功',
      });
      emit('closeModal', 1);
    } else {
      Notification.error({
        title: '提示',
        content: res.msg,
      });
    }
  } catch (e) {
    console.log(e, '错误');
  } finally {
    setLoading(false);
  }
};

// 数组所有项同步第一项
const synchronizeObjects = (arr: any): void => {
  if (arr.length === 0) return;
  const referenceObj = arr[0];
  arr.forEach((obj: any, index: number) => {
    if (index === 0) return;
    Object.keys(referenceObj).forEach(key => {
      if (key !== 'unit' && key !== 'varSn' && key !== 'varName') {
        obj[key] = referenceObj[key];
      }
    });
  });
}

// 选择变量
const chooseVar = (index: any) => {
  selectVarVisible.value = true;
  nowChildrenIndex.value = index;
};

// 删除选中变量
const handleRemove = (item: any, index: number) => {
  children.value[index].unit = '';
  children.value[index].varSn = '';
  children.value[index].varName = '';
};

// 关闭编辑弹窗
const closeModal = () => {
  // inputInit()
  varData.value = [];
  emit('closeModal');
};

// 确认添加
const handleDeviceVarSelect = (record: any) => {
  // console.log('record', record);
  let obj = {
    varName: record.varName,
    varSn: record.varSn,
    unit: record.unit
  };

  children.value[nowChildrenIndex.value] = {...children.value[nowChildrenIndex.value], ...obj}
  handleModalClose();
};

// 关闭变量弹窗
const handleModalClose = () => {
  selectVarVisible.value = false;
};

// 左侧添加变量
const handleVarChange = (data: any) => {
  varData.value = data;
};

// 左侧删除变量
const handleVarDelete = (data: any) => {
  varData.value = data;
}


// 获取详情数据
const getDetailData = async () => {
  setLoading(true);
  try {
    const res: any = await getConfigCard(props.data);
    if (res?.code == 200) {
      id.value = res.data.id;
      if (defaultVar.value.chartType == 6) {
        varData.value = JSON.parse(res.data.cardConfig).varList;
      } else {
        varData.value = JSON.parse(res.data.cardConfig)?.varList?.splice(0, 1);
      }

      res.data.cardConfig = JSON.parse(res.data.cardConfig);
      // 默认数据
      defaultVar.value.dateDim = res.data.dateDim;
      defaultVar.value.secondDateDim = res.data.secondDateDim;
      defaultVar.value.isFullDate = res.data.isFullDate;
      defaultVar.value.isDetail = true;

      baseSettingRef.value?.setForm({
        cardName: res.data.cardName,
        dateDim: res.data.dateDim,
        secondDateDim: res.data.secondDateDim,
        id: res.data.id,
      });
      baseDedicatedRef.value?.setForm({
        cardName: res.data.cardName,
        dateDim: res.data.dateDim,
        secondDateDim: res.data.secondDateDim,
        id: res.data.id,
        headType: res.data.cardConfig.headType,
        tableType: res.data.cardConfig.tableType
      });

      // 数据区只显示一个
      detailedRef.value?.setForm({varList: res.data.cardConfig.varList});
      detailedStatusRef.value?.setForm({varList: res.data.cardConfig.varList});
      detailedPanelRef.value?.setForm({varList: res.data.cardConfig.varList});
      // 辅助信息
      const {legendPosition, lineChildren, isShowLine, baseValue, percentage} = res.data.cardConfig;
      auxiliaryRef.value?.setForm({
        legendPosition,
        lineChildren,
        isShowLine,
        percentage,
        baseValue
      });
      children.value = res.data.cardConfig.children;
      await nextTick();
    }
  } catch (e) {
    console.error(e)
  } finally {
    setLoading(false);
  }
};

// 基础区域时间维度change
const handleDateDim = (val: number) => {
  defaultVar.value['dateDim'] = val;
  defaultVar.value['isDetail'] = false;
  if (val == 70) {
    children.value?.map((val: any) => {
      val.changeType = 1;
    })
  } else if (!defaultVar.value.isDetail) {
    children.value?.map((val: any) => {
      val.changeType = 2;
    })
  }
};

// 存储类型切换
const handleStorage = (index: number) => {
  if (defaultVar.value.dateDim == 70) {
    children.value[index].changeType = 1;
  } else {
    children.value[index].changeType = 2;
  }
};

// 显示类型切换
const handlePercentage = (index: number) => {
  children.value[index].baseValue = 0;
  children.value[index].minValue = 0;
  children.value[index].maxValue = 0;
};

/**
 * 编辑详情数据
 */
const getConfigDetail = async () => {
  try {
    setLoading(true);
    const res: any = await getConfigInfo(route.query.id);
    if (res.code === 200) {
      companyData.value = res.data;
    }
  } catch (e) {

  } finally {
    setLoading(false)
  }
};

watchEffect(() => {
  defaultVar.value = {
    chartType: props.data.chartType,
    idDetail: false,
    dateDim: 70,
    ...DefaultValueData[props.data.chartType]
  };
  childLength.value = maxLength();
  // 是否调用编辑接口
  if (props.data.cardKey) {
    getDetailData();
  } else {
    varData.value = [];
    children.value = [];
    id.value = undefined;
  }

  if (route.query.id) getConfigDetail();
});

</script>

<style lang="less" scoped>
.edit-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  position: relative;

  .edit-left {
    width: 22%;
    height: 100%;
    margin-right: 16px;
  }

  .edit-right {
    flex: 1;
    height: 100%;
    overflow-y: auto;
    padding-bottom: 80px;
    box-sizing: border-box;

    .arco-card {
      margin-bottom: 16px;
      margin-top: 16px;

      &:nth-child(1) {
        margin-top: 0;
      }
    }

    .children-card {
      :deep(.arco-card-header) {
        background: var(--color-neutral-3);
      }
    }

    .form-footer {
      position: absolute;
      bottom: 0;
      right: 0;
      width: 78%;
      height: 60px;
      padding: 14px 20px 0 0;
      background: var(--color-bg-2);
      justify-content: flex-end;
      box-sizing: border-box;
    }
  }

  .edit-right::-webkit-scrollbar {
    display: none;
  }
}

.color-wrapper {
  :deep(.arco-color-picker-size-medium .arco-color-picker-preview) {
    background: none !important;
  }
}
</style>
