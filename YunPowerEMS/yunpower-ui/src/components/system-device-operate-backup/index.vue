<template>
  <a-card class="container">
    <!-- 设备表单 -->
    <a-spin :loading="loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
      <a-form ref="formRef" :model="formData" :rules="formRule" auto-label-width>
        <a-space direction="vertical" :size="16">
          <a-row :gutter="24">
            <a-col :span="12">
              <a-divider orientation="left">设备信息</a-divider>
              <!-- 设备名称 -->
              <a-form-item field="deviceName" label="设备名称" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formData.deviceName" allow-clear />
              </a-form-item>

              <!-- 设备编号 -->
              <a-form-item field="deviceSn" label="设备编号" :validate-trigger="['change', 'blur', 'input']">
                <a-input disabled v-model="formData.deviceSn" allow-clear />
              </a-form-item>

              <!-- 电站类型 -->
              <!-- <a-form-item field="stationType" label="电站类型">
              <a-select v-model="formData.stationType" placeholder="请选择电站类型" allow-clear>
                <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></a-option>
              </a-select>
            </a-form-item> -->

              <!-- 电能类型 -->
              <a-form-item field="electricType" label="电能类型">
                <a-select v-model="formData.electricType" placeholder="请选择电能类型" allow-clear>
                  <a-option v-for="dict in sys_electric_type" :key="dict.value" :label="dict.label"
                    :value="Number(dict.value)"></a-option>
                </a-select>
              </a-form-item>

              <!-- 通讯设备 -->
              <a-form-item field="comDeviceId" label="通讯设备">
                <!-- <a-tree-select allow-search v-model="formData.comDeviceId" :data="channelDeviceTreeData"
                placeholder="请选择通讯设备" allow-clear @change="handleChannelDeviceChange"
                @clear="handleChannelDeviceClear"></a-tree-select>
                </a-select> -->
                <a-select v-model="formData.comDeviceId" :options="channelDeviceData" @clear="handleChannelDeviceClear"
                  @change="handleChannelDeviceChange" placeholder="请选择通讯设备" allow-clear
                  :fieldNames="{ value: 'id', label: 'deviceName' }">
                </a-select>
              </a-form-item>

              <!-- 设备类型 -->
              <a-form-item field="proType" label="设备类型">
                <a-tree-select allow-search v-model="formData.proType" :data="deviceTypeData" placeholder="请选择设备类型"
                  :fieldNames="{
                    key: 'id', title: 'groupName', children: 'children'
                  }" allow-clear></a-tree-select>
              </a-form-item>

              <!-- 设备分组 -->
              <a-form-item field="groupId" label="设备分组">
                <a-tree-select allow-search v-model="formData.groupId" :data="deviceGroupData" placeholder="请选择设备分组"
                  :fieldNames="{
                    key: 'id', title: 'groupName', children: 'children'
                  }" allow-clear></a-tree-select>
              </a-form-item>

              <!-- 生产厂家 -->
              <a-form-item field="proFactory" label="生产厂家">
                <a-tree-select allow-search v-model="formData.proFactory" :data="companyData" placeholder="请选择生产厂家"
                  allow-clear :field-names="{ key: 'id', title: 'companyName' }"></a-tree-select>
              </a-form-item>

              <!-- 产品型号 -->
              <a-form-item field="proModel" label="产品型号" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formData.proModel" allow-clear />
              </a-form-item>

              <!-- 产品版本 -->
              <a-form-item field="proVer" label="产品版本" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formData.proVer" allow-clear />
              </a-form-item>

              <!-- 购买价格 -->
              <a-form-item field="buyPrice" label="购买价格" :validate-trigger="['change', 'blur', 'input']">
                <a-input-number v-model="formData.buyPrice" allow-clear>
                  <template #append>
                    元
                  </template>
                </a-input-number>
              </a-form-item>

              <!-- 产品sn号 -->
              <a-form-item field="proSn" label="产品sn号" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formData.proSn" allow-clear />
              </a-form-item>

              <!-- 安规国家 -->
              <a-form-item field="agStandard" label="安规国家" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formData.agStandard" allow-clear />
              </a-form-item>

              <!-- 额定电压 -->
              <a-form-item field="ratedVol" label="额定电压">
                <a-select v-model="formData.ratedVol" placeholder="请选择额定电压" allow-clear>
                  <a-option v-for="dict in sys_rated_vol" :key="dict.value" :label="dict.label"
                    :value="dict.value"></a-option>
                </a-select>
              </a-form-item>

              <!-- 额定电流 -->
              <a-form-item field="ratedEle" label="额定电流">
                <a-input-number v-model="formData.ratedEle" allow-clear placeholder="请输入数字">
                  <template #append>
                    A
                  </template>
                </a-input-number>
              </a-form-item>

              <!-- 额定功率 -->
              <a-form-item field="ratedPower" label="额定功率">
                <a-input-number v-model="formData.ratedPower" allow-clear placeholder="请输入数字">
                  <template #append>
                    KW
                  </template>
                </a-input-number>
              </a-form-item>

              <!-- 电流负载率 -->
              <a-form-item field="eleLoadRate" label="电流负载率">
                <a-input-number v-model="formData.eleLoadRate" allow-clear placeholder="请输入数字">
                  <template #append>
                    %
                  </template>
                </a-input-number>
              </a-form-item>

              <!-- 是否关口表 -->
              <a-form-item field="isPass" label="是否关口表">
                <a-switch :checked-value="1" :unchecked-value="0" v-model="formData.isPass" />
              </a-form-item>

              <!-- 电能质量分 -->
              <a-form-item field="isAnalysisEnergy" label="电能质量分">
                <a-switch :checked-value="1" :unchecked-value="0" v-model="formData.isAnalysisEnergy" />
              </a-form-item>

              <!-- 启用状态 -->
              <a-form-item field="stopFlag" label="启用状态">
                <a-switch :checked-value="0" :unchecked-value="1" v-model="formData.stopFlag"></a-switch>
              </a-form-item>

              <a-form-item field="remark" label="备注说明">
                <a-textarea v-model="formData.remark" allow-clear :max-length="200" :auto-size="true"
                  :show-word-limit="true" placeholder="请输入备注说明，最多不能超过200字" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <!-- 光伏设备 -->
              <div v-if="formData.stationType == 2">
                <a-divider orientation="left">光伏组件</a-divider>
                <a-form-item hide-label>
                  <a-alert>逆变器设备请认真填写以下数据，用于发电性能分析</a-alert>
                </a-form-item>
                <!-- 组件容量 -->
                <a-form-item field="ratedVolume" label="组件容量">
                  <a-input-number v-model="formData.ratedVolume" allow-clear placeholder="请输入数字">
                    <template #append>
                      KWP
                    </template></a-input-number>
                </a-form-item>

                <!-- 方位角度 -->
                <a-form-item field="azimuth" label="方位角度">
                  <a-input-number v-model="formData.azimuth" allow-clear placeholder="请输入数字">
                    <template #append>
                      °
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 组件倾角 -->
                <a-form-item field="dipAngle" label="组件倾角">
                  <a-input-number v-model="formData.dipAngle" allow-clear placeholder="请输入数字">
                    <template #append>
                      °
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 生产日期 -->
                <a-form-item field="manufactureDate" label="生产日期">
                  <a-date-picker v-model="formData.manufactureDate" placeholder="请选择生产日期" />
                </a-form-item>

                <!-- 使用年限  -->
                <a-form-item field="useLife" label="使用年限">
                  <a-input-number v-model="formData.useLife">
                    <template #append>
                      年
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 性能衰减 -->
                <a-form-item field="attenuationRate" label="性能衰减">
                  <a-input-number v-model="formData.attenuationRate" allow-clear placeholder="请输入数字">
                    <template #append>
                      %
                    </template>
                  </a-input-number>
                </a-form-item>
              </div>
              <!-- 用电设备 -->
              <div v-if="formData.stationType == 1">
                <a-divider orientation="left">计费信息</a-divider>
                <a-form-item hide-label>
                  <a-alert>远程抄表请填写以下数据，用于计算并扣费</a-alert>
                </a-form-item>
                <!-- 抄表变量  -->
                <a-form-item field="readingVar" label="抄表变量">
                  <a-input-number v-model="formData.readingVar" />
                </a-form-item>

                <!-- DTU端口  -->
                <a-form-item field="dtuCmd" label="DTU端口">
                  <a-input-number v-model="formData.dtuCmd" />
                </a-form-item>

                <!-- <a-form-item field="bindUserId" label="绑定用户">
                <a-input-number v-model="formData.bindUserId" />
              </a-form-item>

              <a-form-item field="chargingType" label="付费模式">
                <a-select v-model="formData.chargingType" placeholder="请选择付费模式" allow-clear>
                  <a-option value="1">此处动态加载数据</a-option>
                </a-select>
              </a-form-item>

              <a-form-item field="ruleId" label="付费规则">
                <a-select v-model="formData.ruleId" placeholder="请选择付费规则" allow-clear>
                  <a-option value="1">此处动态加载数据</a-option>
                </a-select>
              </a-form-item>

              <a-form-item field="checkoutType" label="付费周期">
                <a-select v-model="formData.checkoutType" placeholder="请选择付费周期" allow-clear>
                  <a-option value="1">此处动态加载数据</a-option>
                </a-select>
              </a-form-item> -->
              </div>

              <div v-if="formData.stationType == 2">
                <a-divider orientation="left">关联设备</a-divider>
                <a-form-item hide-label>
                  <a-alert>汇流箱、并网柜请选择关联设备，用于展示连接关系</a-alert>
                </a-form-item>
                <a-form-item hide-label>
                  <a-button type="primary" @click="handleDeviceAdd">添加设备</a-button>
                </a-form-item>
                <a-table row-key="id" :bordered="{ wrapper: true, cell: true }"
                  :columns="(chooseTabColumns as TableColumnData[])" :data="chooseRenderData" :pagination="false">
                  <template #operate="{ record }">
                    <a-button size="small" type="primary" status="danger"
                      @click="handleDeviceDelete(record)">移除</a-button>
                  </template>
                </a-table>
              </div>
            </a-col>
          </a-row>
        </a-space>
        <div class="actions">
          <a-space>
            <a-button type="primary" status="danger" @click="onCancleClick">取消</a-button>
            <a-button type="primary" :loading="submitLoading" @click="onSubmitClick">保存</a-button>
          </a-space>
        </div>
      </a-form>
    </a-spin>

    <!-- 选择设备弹框 -->
    <a-modal width="600px" :visible="modalModel.visible" :footer="false" title-align="start" title="选择设备"
      @cancel="handleModalClose">
      <a-form :model="modalModel.serarchModel" auto-label-width>
        <a-row>
          <a-col>
            <a-form-item field="groupId" label="设备分组">
              <a-tree-select allow-search v-model="modalModel.serarchModel.groupId" :data="deviceGroupData"
                @change="handleCatalogIdChange" placeholder="请选择设备分组"
                :fieldNames="{ key: 'id', title: 'groupName', children: 'children' }" allow-clear></a-tree-select>
            </a-form-item>
          </a-col>
          <a-col>

            <a-table height="300px" row-key="id" :bordered="{ wrapper: true, cell: true }"
              :columns="(tabColumns as TableColumnData[])" :data="modalModel.renderData" @page-change="onPageChange"
              :pagination="pagination" @page-size-change="onPageSizeChange">
              <template #operate="{ record }">
                <a-button size="small" type="primary" @click="handleDeviceChoose(record)">添加</a-button>
              </template>
            </a-table>
            <!-- <div style="text-align: center">
              <a-button @click="handleDeviceChoose" :disabled="modalModel.renderData.length == 0" type="primary"
                style="margin-top: 20px;">添加选中设备</a-button>
            </div> -->
          </a-col>
        </a-row>
      </a-form>
    </a-modal>

  </a-card>
</template>
<script setup lang="ts">

import { FormInstance, TreeNodeData, TableColumnData, TableRowSelection } from '@arco-design/web-vue';
import { computed, getCurrentInstance, onMounted, reactive, ref } from 'vue';
import useLoading from '@/hooks/loading';
import { listGroupAll } from '@/api/system/group';
import { listCompanyAll } from '@/api/system/company';
import { handleTreeNodeData } from '@/utils/ruoyi';
import { listChannelDeviceAll } from '@/api/pv/channel/device';
import { StationTypeEnum, addDevice, getDevice, listDevice, updateDevice } from '@/api/system/device';
import { BasePagination } from '@/api/common';
import { useRouter, useRoute } from 'vue-router'
import { debounce } from 'lodash';
import { notification } from "@/hooks/my-design";

const router = useRouter()
//事件
const emits = defineEmits(['success', 'cancle'])
//组件参数
const props = ref<any>({
  stationType: StationTypeEnum.power,
  bizId: 0,
  groupName: '',
})
//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_electric_type,
  sys_rated_vol,
} = proxy?.useDict(
  "sys_electric_type",
  "sys_rated_vol",
);
//******* 这里编写动态获取下拉列表 end ******
//通讯设备
const channelDeviceData = ref<any>([]);
const channelDeviceTreeData = ref<TreeNodeData[]>([]);
//设备类型
const deviceTypeData = ref<any>([]);
//设备分组
const deviceGroupData = ref<any>([]);
//生产厂家
const companyData = ref<any>([]);
//加载中
const { loading, setLoading } = useLoading();
//表单实例
const formRef = ref<FormInstance>();
const submitLoading = ref<boolean>(false);
//生成表单模型
const generateFormModel = () => {
  return {
    id: 0,
    entId: undefined,
    deptId: undefined,
    stationType: 0,
    electricType: 1,
    channelId: undefined,
    channelSn: undefined,
    channelName: undefined,
    comDeviceId: undefined,
    comDeviceSn: undefined,
    deviceName: undefined,
    deviceSn: undefined,
    groupId: undefined,
    groupName:"",
    isPass: 0,
    proFactory: undefined,
    proType: undefined,
    proVer: undefined,
    buyPrice: undefined,
    proSn: undefined,
    proModel: undefined,
    agStandard: undefined,
    ratedVol: undefined,
    ratedEle: undefined,
    ratedPower: undefined,
    eleLoadRate: undefined,
    isAnalysisEnergy: 0,
    monitorId: undefined,
    electricAttribute: undefined,
    temperatureAttribute: undefined,
    ortherAttribute: undefined,
    isReading: undefined,
    ratedVolume: undefined,
    azimuth: undefined,
    dipAngle: undefined,
    manufactureDate: undefined,
    useLife: undefined,
    attenuationRate: undefined,
    readingVar: undefined,
    dtuCmd: undefined,
    bindUserId: 0,
    chargingType: 0,
    ruleId: 0,
    checkoutType: 0,
    stopFlag: 0,
    remark: "",
    varIds: [] as number[]
  };
};

//表单模型
const formData = ref(generateFormModel());
//选中的设备列表
const chooseRenderData = ref<any>([]);
//表单规则
const formRule = {
  deviceName: [{ required: true, message: "请输入设备名称" }],
  comDeviceId: [{ required: true, message: "请选择通讯设备" }],
  proType: [{ required: true, message: "请选择设备类型" }],
  groupId: [{ required: true, message: "请选择设备分组" }],
  proFactory: [{ required: true, message: "请选择生产厂家" }],
}
//添加设备-多选
const selectedKeys = new Set<number>();
const rowSelection: TableRowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true
});
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "设备名称",
    dataIndex: "deviceName",
    align: 'center',
    width: 200
  }, {
    title: "设备编号",
    dataIndex: "deviceSn",
    align: 'center',
    width: 200
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    width: 100,
    align: 'center'
  }
]);
//设置表格列
const chooseTabColumns = computed<TableColumnData[]>(() => [
  {
    title: "设备名称",
    dataIndex: "deviceName",
    align: 'center',
    width: 200
  }, {
    title: "设备编号",
    dataIndex: "deviceSn",
    align: 'center',
    width: 200
  }, {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    width: 100,
    align: 'center'
  }
]);
//生成弹框模型
const generateModalModel = () => {
  return {
    visible: false,
    //搜索条件
    serarchModel: {
      groupId: "",
      stationType: props.value.stationType,
    },
    //表格数据
    renderData: []
  }
}
//弹框模型
const modalModel = ref(generateModalModel());
//表格分页参数
const pagination: any = reactive({ ...BasePagination() });
// pagination.pageSize = 5;
// pagination.pageSizeOptions.unshift(5);


/**
 * 选择设备分组改变
 */
const handleCatalogIdChange = async () => {
  await fetchDeviceData();
}

/**
 * 添加设备
 */
const handleDeviceAdd = async () => {
  modalModel.value = generateModalModel();
  modalModel.value.visible = true;
  pagination.current = 1;
  await fetchDeviceData();
}

/**
 * 添加设备
 */
const handleModalClose = () => {
  console.log("handleModalClose");
  modalModel.value = generateModalModel();
  // selectedKeys.value.clear();
}

/**
 * 设备选中
 */
const handleDeviceChoose = (record: any) => {
  try {
    // console.log("selectedKeys", selectedKeys.value);
    // if (!selectedKeys.value || selectedKeys.value.length <= 0) {
    //   notification({ code: 201, msg: "请选择要添加的数据" })
    //   return;
    // }
    // formData.value.varIds = selectedKeys;
    if (!selectedKeys.has(record.id)) {
      selectedKeys.add(record.id);
      chooseRenderData.value.push(record);
      formData.value.varIds = Array.from(selectedKeys);
      record.isAdd = true;
    }
  } catch (error) {

  }
}
const handleDeviceDelete = (record: any) => {
  selectedKeys.delete(record.id);
  chooseRenderData.value = chooseRenderData.value.filter((item: any) => item.id !== record.id);
  formData.value.varIds = Array.from(selectedKeys);
}

/**
 * 保存
 */
const onSubmitClick = debounce(
  async () => {
    if (submitLoading.value) {
      return;
    }
    submitLoading.value = true;
    try {
      const validate = await formRef.value?.validate();
      if (!validate) {
        let result;
        if (formData.value.id == 0) {
          formData.value.groupName = props.value.groupName;
          result = await addDevice(formData.value);
        } else {
          result = await updateDevice(formData.value);
        }
        notification(result);
        if (result.code == 200) {
          back()
        }
      }
    } catch (e) {

    } finally {
      submitLoading.value = false;
    }
  }, 1000
)

/**
 * 取消
 */
const onCancleClick = () => {
  back()
}

/**
 * 通讯设备选择的时候
 */
const handleChannelDeviceChange = (val: any) => {
  const findData = channelDeviceData.value.find((item: any) => item.id === val)
  if (findData) {
    console.log(findData)
    formData.value.comDeviceId = findData.id;
    formData.value.comDeviceSn = findData.deviceSn;
    formData.value.channelId = findData.channelId;
    formData.value.channelSn = findData.channelSn;
    formData.value.channelName = findData.channelName;

  }
}

/**
 * 通讯设备选择的时候
 */
const handleChannelDeviceClear = () => {
  formData.value.comDeviceId = undefined;
  formData.value.comDeviceSn = undefined;
}

/**
 * 获取设备类型
 */
const fetchDeviceTypeData = async () => {
  try {
    const res = await listGroupAll({ mapId: 4 });
    if (res.code == 200) {
      deviceTypeData.value = res.data;
    }
  } catch (ex) {
    console.error("添加设备-获取设备类型失败", ex)
  }
}

/**
 * 获取设备分组
 */
const fetchDeviceGroupData = async () => {
  try {
    const res = await listGroupAll({ mapId: props.value.stationType == 1 ? 2 : 3 });
    if (res.code == 200) {
      deviceGroupData.value = res.data;
    }
  } catch (ex) {
    console.error("添加设备-获取设备分组失败", ex)
  }
}

/**
 * 获取公司
 */
const fetchCompanyData = async () => {
  try {
    const res = await listCompanyAll({ companyGroup: 1 });
    if (res.code == 200) {
      companyData.value = res.data;
      // companyData.value = handleTreeNodeData(res.data, "id", "companyName");
    }
  } catch (ex) {
    console.error("添加设备-获取生产厂家失败", ex)
  }
}


/**
 * 获取设备明细
 */
const fetchDeviceInfoData = async () => {
  if (props.value.bizId && props.value.bizId > 0) {
    setLoading(true);
    try {
      const res = await getDevice(props.value.bizId);
      if (res.code == 200) {
        formData.value = res.data;
      }
    } catch (ex) {
      console.error("添加设备-获取设备信息失败", ex)
    } finally {
      setLoading(false);
    }
  }
}

/**
 * 获取设备列表
 */
const fetchDeviceData = async () => {
  try {
    const res = await listDevice({
      pageSize: pagination.pageSize,
      pageNum: pagination.current, ...modalModel.value.serarchModel
    });
    if (res.code == 200) {
      removeEmptyChildren(res.rows);
      modalModel.value.renderData = res.rows;
      pagination.total = res.total;
    }
  } catch (ex) {
    console.error("添加设备-获取设备信息失败", ex)
  } finally {
  }
}

//表格页码发生变化
const onPageChange = async (val: number) => {
  pagination.current = val;
  await fetchDeviceData();
}

//表格每页数量发生变化
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchDeviceData();
}
/**
 * 获取通讯设备
 */
const fetchChannelDevice = async () => {
  try {
    const res = await listChannelDeviceAll({});
    if (res.code == 200) {
      channelDeviceData.value = res.data;
      channelDeviceTreeData.value = handleTreeNodeData(res.data, "id", "deviceName");
    }
  } catch (error) {

  }
}

/**
 * 删除空数据
 * @param data
 */
const removeEmptyChildren = (data: any) => {
  data.forEach((item: any, index: number) => {
    if (item.children && item.children.length === 0) {
      delete data[index].children;
    } else if (item.children) {
      removeEmptyChildren(item.children);
    }
  });
}


/**
 * 返回上一级页面
 */
const back = () => {
  router.push({
    path: router.currentRoute.value.query.redirect as string,
  });
}


/**
 * 获取所有数据
 */
const fetchAllData = async () => {
  await Promise.all([
    //获取通讯设备
    fetchChannelDevice(),
    //获取设备类型
    fetchDeviceTypeData(),
    //获取设备分组
    fetchDeviceGroupData(),
    //获取生产厂家
    fetchCompanyData(),
    //获取设备详情
    fetchDeviceInfoData()
  ]);
}
/**
 * 首次渲染
 */
onMounted(async () => {
  props.value = {
    stationType: router.currentRoute.value.query.stationType,
    groupName: router.currentRoute.value.query.groupName,
    bizId: router.currentRoute.value.query.bizId
  }
  formData.value = generateFormModel();
  formData.value.stationType = props.value.stationType;
  setLoading(true);
  try {
    await fetchAllData();
  } catch (error) {

  } finally {
    setLoading(false);
  }
})
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 40px 20px;
  overflow: hidden;
}

.actions {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: 60px;
  padding: 14px 20px 14px 0;
  background: var(--color-bg-2);
  text-align: right;
  box-shadow: 0 10px 30px 0 rgb(0 0 0 / 40%);
}
</style>
