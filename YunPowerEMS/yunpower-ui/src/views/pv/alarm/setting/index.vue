<!--
* 报警配置
* 作者：曹晓桐
* 日期：2023-11-18
-->
<template>
  <div>
    <a-row :gutter="10">

      <!-- 左侧树 start -->
      <a-col :span="4">
        <!--  :listStyle="1" -->
        <search-tree :title="$t('global.device')" @onChange="searchTreeChange" :stationType="props.stationType"
                     :needCheck="false"/>
      </a-col>
      <!-- 左侧树 end -->

      <!-- 右侧内容 start -->
      <a-col :span="20">
        <a-card class="content">
          <!-- 查询条件 start-->
          <a-row>
            <a-col :flex="1" style="margin-top: 4px;">
              <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                      label-align="left" auto-label-width>
                <a-row :gutter="16">
                  <a-col :span="8">
                    <!--能源类型-->
                    <a-form-item field="stationType" :label="$t('global.energy')">
                      <a-select v-model="searchModel.stationType" :placeholder="$t('global.pleaseSelect')"
                                allow-clear @change="handleStationType">
                        <a-option :key="0" :label="'全部类型'" :value="Number(-100)"/>
                        <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                                  :value="Number(dict.value)"/>
                      </a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <!--报警类型-->
                    <a-form-item :label="$t('pv.alarm.setting.alarmType')">
                      <a-select v-model="searchModel.categoryId" :options="renderAlarmCategoryDataAll"
                                :fieldNames="alarmCategoryfieldNames" allowSearch allow-clear>
                      </a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <a-button type="primary" @click="search">
                      <template #icon>
                        <icon-search/>
                      </template>
                      {{ $t('global.search') }}
                    </a-button>
                  </a-col>
                </a-row>
              </a-form>
            </a-col>
          </a-row>
          <!-- 查询条件 end-->

          <!-- 分割线 -->
          <a-divider style="margin-top: 0"/>

          <!-- 操作按钮 start-->
          <a-row style="margin-bottom: 20px">
            <a-col :span="12">
              <a-space size="large">
                <!-- 新建配置 -->
                <a-button type="primary" @click="handleAdd">
                  <template #icon>
                    <icon-plus/>
                  </template>
                  {{ $t('pv.alarm.setting.newConfig') }}
                </a-button>
                <!-- 批量删除 -->
                <a-button type="primary" status="danger" @click="handleDeleteClick">
                  {{ $t('pv.alarm.setting.batchDelete') }}
                </a-button>
                <div>
                  {{ $t('pv.alarm.setting.alarmSwitch') }}：
                  <a-switch v-model="alarmStatus" :checked-value="1" :unchecked-value="0"
                            @change="handleAlarmStatusChange">
                    <template #checked>{{ $t('pv.alarm.setting.open') }}</template>
                    <template #unchecked>{{ $t('pv.alarm.setting.close') }}</template>
                  </a-switch>
                </div>
              </a-space>
            </a-col>
          </a-row>
          <!-- 操作按钮 end-->

          <!-- table start-->
          <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
                   :columns="tabColumns" :data="renderData" @page-change="onPageChange"
                   @page-size-change="onPageSizeChange"
                   v-model:selectedKeys="tableSelectedKeys" :row-selection="rowSelection" :scroll="{ x: 910 }">
            <template #stopFlag="{ record }">
              <stop-flag :value="record.stopFlag"/>
            </template>
            <template #operate="{ record }">
              <a-button size="small" type="text" @click="handleStopFlag(record)"
                        :status="record.stopFlag == 1 ? 'normal' : 'warning'">
                {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
              </a-button>
              <a-button size="small" type="text" @click="handleUpdate(record.id)">{{ $t('global.edit') }}</a-button>
              <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{
                  $t('global.delete')
                }}
              </a-button>
            </template>
          </a-table>
          <!-- table end-->

          <!-- 操作弹框 start -->
          <a-modal width="400px" v-model:visible="operateModalModel.visible" class="modal-box">
            <template #title>
              <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                                 style="color: rgb(var(--red-6)); margin-right: 5px"/>
              <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
              确认提示
            </template>
            <div v-if="operateModalModel.type < 2" style="text-align: center;">是否确认{{
                operateModalModel.title
              }}编号为【{{
                operateModalModel.name
              }}】的数据项？
            </div>
            <div v-if="operateModalModel.type == 2" style="text-align: center;">你确定要【删除所选记录】吗？</div>
            <template #footer>
              <div style="text-align: center">
                <a-space>
                  <a-button type="primary" status="danger" @click="handleOperateModelCancle">取消</a-button>
                  <a-button type="primary" @click="handleOperateModelOk">确认</a-button>
                </a-space>
              </div>
            </template>
          </a-modal>
          <!-- 操作弹框 end -->

          <!-- 选择设备弹框 -->
          <a-modal width="800px" :visible="modalModel.visible" :footer="false" title-align="start" title="选择设备"
                   @cancel="handleModalClose">
            <a-form :model="modalModel.serarchModel" auto-label-width>
              <a-row :gutter="16">
                <a-col :span="10">
                  <a-form-item field="deviceId" label="用电设备">
                    <a-tree-select v-model="modalModel.serarchModel.deviceId" :data="renderDeviceData"
                                   placeholder="请选择用电设备" :fieldNames="{
                  key: 'id', title: 'deviceName', children: 'children'
                }" :allow-search="true" :allow-clear="true" :filter-tree-node="filterTreeNode"></a-tree-select>
                  </a-form-item>
                </a-col>
                <a-col :span="10">
                  <a-form-item field="varName" label="变量名称">
                    <a-input v-model="modalModel.serarchModel.varName" allow-clear
                             placeholder="变量名称，支持模糊搜索"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="4">
                  <!-- 模块类型 -->
                  <a-button type="primary" @click="searchDeviceVar">
                    <template #icon>
                      <icon-search/>
                    </template>
                    查询
                  </a-button>
                </a-col>
              </a-row>
            </a-form>
            <a-table :scroll="{ y: 400 }" row-key="id" :bordered="{ wrapper: true, cell: true }"
                     :columns="(deviceVarTabColumns as TableColumnData[])" :data="modalModel.renderData"
                     @page-change="onDeviceVarPageChange" :pagination="deviceVarPagination"
                     @page-size-change="onDeviceVarPageSizeChange">
              <template #operate="{ record }">

                <a-button v-if="record.isAdd != 1" size="small" type="primary"
                          @click="handleDeviceVarSelect(record)">添加
                </a-button>
                <a-button v-else size="small" type="primary" status="danger"
                          @click="handleDeviceVarDelete(record)">移除
                </a-button>
              </template>
            </a-table>
<!--            <a-form :model="modalModel.serarchModel" auto-label-width>-->
<!--              <a-row>-->
<!--                <a-col>-->
<!--                  <a-form-item field="catalogId" label="设备名称">-->
<!--                    <a-tree-select allow-search v-model="modalModel.serarchModel.deviceId" :data="renderDeviceData"-->
<!--                                   placeholder="可通过设备名称筛选" @change="fetchDeviceVarData"-->
<!--                                   @clear="fetchDeviceVarData" :fieldNames="{-->
<!--                        key: 'key', title: 'deviceName', children: 'children'-->
<!--                      }" allow-clear></a-tree-select>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->


<!--                <a-col>-->
<!--                  -->
<!--                </a-col>-->
<!--              </a-row>-->
<!--            </a-form>-->
          </a-modal>

          <!-- 添加右弹层 start -->
          <a-drawer :width="750" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm" @cancel="handleFormCancel"
                    :mask-closable="false">
            <template #title>
              {{
                formDrawer.formModel.id ? $t('pv.alarm.setting.editConfiguration') : $t('pv.alarm.setting.addConfiguration')
              }}
            </template>
            <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
              <a-row :gutter="24" class="row-mp-30">
                <a-col :span="24">
                  <!-- tab 切换 -->
                  <a-tabs v-if="formDrawer.formModel.id == 0" v-model:active-key="tabsActiveKey"
                          @change="handleTabsChange">
                    <a-tab-pane v-for="(item, index) in tabsList" :key="item.key" :title="item.title">
                    </a-tab-pane>
                  </a-tabs>
                  <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                          :rules="formDrawer.rules">

                    <!-- 添加变量 -->
                    <div v-if="formDrawer.formModel.id == 0">
                      <a-form-item hide-label>
                        <a-button type="primary" @click="handleDeviceVarAdd">{{
                            $t('pv.alarm.setting.selectVar')
                          }}
                        </a-button>
                      </a-form-item>
                      <a-form-item hide-label>
                        <a-table row-key="id" :scroll="{ y: 300 }" :bordered="{ wrapper: true, cell: true }"
                                 :columns="(deviceVarTabColumns as TableColumnData[])" :data="renderDeviceVarSelectData"
                                 :pagination="false">
                          <template #operate="{ record }">
                            <a-button size="small" type="primary" status="danger"
                                      @click="handleDeviceVarDelete(record)">{{ $t('global.remove') }}
                            </a-button>
                          </template>
                        </a-table>
                      </a-form-item>
                    </div>
                    <div v-else>
                      <!-- 所属设备 -->
                      <a-form-item :label="$t('pv.alarm.setting.belongDevice')">
                        {{ formDrawer.formModel.deviceName }}
                      </a-form-item>
                      <!-- 变量名称 -->
                      <a-form-item :label="$t('power.energy.device.varName')">
                        {{ formDrawer.formModel.varName }}
                      </a-form-item>
                      <!-- 变量编码 -->
                      <a-form-item :label="$t('pv.alarm.setting.varCode')">
                        {{ formDrawer.formModel.varSn }}
                      </a-form-item>
                    </div>
                    <!-- 报警类型 -->
                    <a-form-item field="categoryId" :label="$t('pv.alarm.setting.alarmType')">
                      <a-select :placeholder="$t('global.pleaseSelect')"
                                v-model="formDrawer.formModel.categoryId"
                                :options="renderAlarmCategoryData" :fieldNames="alarmCategoryfieldNames" allowSearch
                                allow-clear
                                @change="handleCategoryChange">
                      </a-select>
                    </a-form-item>

                    <!-- 触发条件 -->
                    <a-form-item :label="$t('pv.alarm.setting.condition')">
                      <dict-tag v-if="formDrawer.formModel.triggerType != ''" :options="sys_trigger_type"
                                :value="formDrawer.formModel.triggerType"/>
                    </a-form-item>

                    <!-- 阈值设定 -->
                    <a-form-item v-if="formDrawer.formModel.varType == 1" :label="$t('pv.alarm.setting.threshold')"
                                 field="threshold">
                      <a-input-number v-model="formDrawer.formModel.threshold"/>
                    </a-form-item>

                    <!-- 短信发送 -->
                    <a-form-item field="isSendSms" :label="$t('pv.alarm.setting.smsSend')">
                      <a-switch :checked-value="1" :unchecked-value="0" v-model="formDrawer.formModel.isSendSms">
                        <template
                            #checked>{{ $t('global.yes') }}
                        </template>
                        <template #unchecked>{{ $t('global.no') }}</template>
                      </a-switch>
                    </a-form-item>

                    <!-- 接收人员 -->
                    <a-form-item field="receiveType" :label="$t('pv.alarm.setting.receivePerson')">
                      <a-radio-group v-model="formDrawer.formModel.receiveType" @change="handleReceiveTypeChange">
                        <a-radio value="1">{{ $t('pv.alarm.setting.sendByPost') }}</a-radio>
                        <a-radio value="2">{{ $t('pv.alarm.setting.sendPerson') }}</a-radio>
                      </a-radio-group>
                    </a-form-item>

                    <!-- 接收人员-岗位 -->
                    <a-form-item v-if="formDrawer.formModel.receiveType == 1" field="postIds" label=""
                                 :validate-trigger="['change', 'input']">
                      <a-select multiple v-model="formDrawer.formModel.postIds" allow-clear
                                :placeholder="$t('pv.alarm.setting.electPost')">
                        <a-option v-for="item in renderPostData" :key="item.id" :label="item.postName"
                                  :value="item.id"></a-option>
                      </a-select>
                    </a-form-item>

                    <!-- 接收人员-人员 -->
                    <a-form-item v-if="formDrawer.formModel.receiveType == 2" field="userIds" label=""
                                 :validate-trigger="['change', 'input']">
                      <a-select multiple v-model="formDrawer.formModel.userIds" allow-clear
                                :placeholder="$t('pv.alarm.setting.electPerson')">
                        <a-option v-for="item in renderUserData" :key="item.id" :label="item.nickName"
                                  :value="item.id"></a-option>
                      </a-select>
                    </a-form-item>

                    <!-- 报警时间 -->
                    <a-form-item field="time" label="报警时间">
                      <a-time-picker
                          type="time-range"
                          format="HH:mm"
                          v-model="formDrawer.formModel.time"
                          style="width: 252px;"/>
                    </a-form-item>

                    <!-- 启用状态 -->
                    <a-form-item field="stopFlag" :label="$t('global.status')">
                      <a-switch :checked-value="0" :unchecked-value="1" v-model="formDrawer.formModel.stopFlag">
                        <template
                            #checked>{{ $t('global.enable') }}
                        </template>
                        <template #unchecked>{{ $t('global.forbidden') }}</template>
                      </a-switch>
                    </a-form-item>
                  </a-form>
                </a-col>
              </a-row>
            </a-spin>
          </a-drawer>

        </a-card>
      </a-col>
      <!-- 右侧内容 end -->
    </a-row>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, reactive, ref} from "vue";
import useLoading from "@/hooks/loading";
import {FormInstance} from '@arco-design/web-vue';
import {listTriggerCategoryAll} from "@/api/system/trigger-category";
import {TableColumnData, TableRowSelection} from "@arco-design/web-vue/es/table";
import {BasePagination} from "@/api/common";
import {StationTypeEnum, listFusionGroup} from "@/api/system/device";
import {
  addTriggerConfig,
  changeTriggerConfigStatus,
  delTriggerConfig,
  getTriggerConfig,
  listTriggerConfig,
  updateTriggerConfig
} from "@/api/system/trigger-config";
import {processSelectable} from "@/utils/ruoyi";
import {listDeviceVar} from "@/api/system/device-var";
import {listPostAll} from "@/api/manage/account/post";
import {listUserAll} from "@/api/manage/account/user";
import {changeAlarmStatus, getAlarmStatus} from "@/api/system/station";
import {notification} from "@/hooks/my-design";

//接受组件参数
const props = defineProps({
  stationType: {
    type: Number,
    default: StationTypeEnum.power,
  },
})

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_trigger_type,
  sys_station_type
} = proxy?.useDict(
    "sys_trigger_type", "sys_station_type"
);
//******* 这里编写动态获取下拉列表 end ******

/*************************** 变量区域 begin ***************************/

//电站报警开关
const alarmStatus = ref<any>(1);
//tab切换选中值
const tabsActiveKey = ref<number>(1);
//tab切换
const tabsList = ref<any>([
  {
    key: 1,
    title: "模拟量",
  },
  {
    key: 2,
    title: "状态量",
  }
])

//生成查询条件对象
const generateSearchModel = () => {
  return {
    // stationType: -100,
    stationType: props.stationType,
    categoryId: -1,
    deviceSn: ""
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());
//表格数据
const renderData = ref<any>([]);
//报警类型数据-下拉框
const renderDefaultAlarmCategoryData = ref<any>([{
  id: -1,
  triggerName: "全部报警",
  triggerSn: "all",
}]);
const renderAlarmCategoryDataAll = ref<any>([]);
const renderAlarmCategoryData = ref<any>([]);
//报警类型数据-下拉框-自定义字段
const alarmCategoryfieldNames = {value: 'id', label: 'triggerName'}
//设备变量数据
const renderDeviceVarSelectData = ref<any>([]);
//设备数据
const renderDeviceData = ref<any>([]);
//岗位数据
const renderPostData = ref<any[]>([]);
//人员数据
const renderUserData = ref<any[]>([]);
//选中的key
const tableSelectedKeys = ref();
//行选择器
const rowSelection: TableRowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});
//加载中
const {loading, setLoading} = useLoading(true);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
const deviceVarPagination: any = reactive({...BasePagination()});
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "变量类型",
    dataIndex: "varTypeName",
    width: 120,
  },
  {
    title: "变量名称",
    dataIndex: "varName",
    width: 250,
  },
  {
    title: "所属设备",
    dataIndex: "deviceName",
    width: 180,
  },
  {
    title: "报警类型",
    dataIndex: "categoryName",
    width: 180,
  },
  {
    title: "阈值",
    dataIndex: "threshold",
    width: 100,
  },
  {
    title: "启用状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100,
    fixed: "right"
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 180,
    slotName: 'operate',
    align: 'center',
    fixed: "right"
  },
]);
const deviceVarTabColumns = computed<TableColumnData[]>(() => [
  {
    title: "变量名称",
    dataIndex: "varName",
    align: 'center',
    width: 200
  }, {
    title: "变量编码",
    dataIndex: "varSn",
    align: 'center',
    width: 150
  }, {
    title: "所属设备",
    dataIndex: "deviceName",
    align: 'center',
    width: 150
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    width: 100,
    align: 'center'
  }
]);
//操作弹框
const generateOperateModalModel = () => {
  return {
    //0 状态 1删除
    type: 0,
    visible: false,
    title: "",
    id: 0,
    value: 0,
    name: ""
  };
};
//操作弹框模型
const operateModalModel = ref(generateOperateModalModel());
//表单示例
const formRef = ref<FormInstance>();
//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {
      categoryId: [{required: true, message: "请选择报警类型"}],
      threshold: [{required: true, message: "请输入阈值"}],
    },
    formModel: {
      stationType: props.stationType,
      categoryId: undefined,
      categoryName: "",
      triggerType: '',
      createBy: "",
      createTime: "",
      deleteFlag: 0,
      deptId: 0,
      deviceId: 0,
      deviceName: "",
      deviceSn: "",
      entId: 0,
      id: 0,
      isSendSms: 1,
      params: {},
      receiveConcrete: [
        {
          name: "",
          value: ""
        }
      ],
      receiveType: 0,
      remark: "",
      stopFlag: 0,
      threshold: undefined,
      updateBy: "",
      updateTime: "",
      varId: 0,
      varName: "",
      varSn: "",
      varType: 0,
      varTypeName: "",
      varIds: [] as number[],
      postIds: [] as number[],
      userIds: [] as number[],
      time: ["00:00", "23:59"],
    }
  };
};
//表单模型
const formDrawer = ref(generateFormDrawerModel());

const selectedKeys = new Set<number>();
//选择变量弹框模型
const generateModalModel = () => {
  return {
    visible: false,
    //搜索条件
    serarchModel: {
      stationType: props.stationType,
      varType: 1,
      varName: '',
      deviceId: undefined
    },
    //表格数据
    renderData: []
  }
}
//弹框模型
const modalModel = ref(generateModalModel());
/**
 * 设备搜索
 * @param searchValue
 * @param nodeData
 */
const filterTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.deviceName.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
}
/*************************** 变量区域 end ***************************/



/*************************** 方法区域 begin ***************************/


const searchDeviceVar = async () => {
  pagination.current = 1;
  await fetchDeviceVarData();
}

/**
 * 选择变量-打开
 */
const handleDeviceVarAdd = async () => {
  modalModel.value = generateModalModel();
  deviceVarPagination.current = 1;
  await fetchDeviceVarData();
  await fetchDeviceData();
  modalModel.value.visible = true;
}

/**
 * 选择变量-关闭
 */
const handleModalClose = () => {
  modalModel.value = generateModalModel();
  // selectedKeys.clear();
}

/**
 * 设备变量选中
 */
const handleDeviceVarSelect = (record: any) => {
  try {
    if (!selectedKeys.has(record.id)) {
      selectedKeys.add(record.id);
      renderDeviceVarSelectData.value.push(record);
      formDrawer.value.formModel.varIds = Array.from(selectedKeys);
      record.isAdd = true;
    }
  } catch (error) {

  }
}

/**
 * 设备变量移除
 * @param record
 */
const handleDeviceVarDelete = (record: any) => {
  selectedKeys.delete(record.id);
  record.isAdd = undefined;
  renderDeviceVarSelectData.value = renderDeviceVarSelectData.value.filter((item: any) => item.id !== record.id);
  formDrawer.value.formModel.varIds = Array.from(selectedKeys);
}

/**
 * 选中报警类型
 */
const handleCategoryChange = () => {
  if (formDrawer.value.formModel.categoryId) {
    let info = renderAlarmCategoryData.value.find((item: any) => item.id === formDrawer.value.formModel.categoryId);
    if (info) {
      formDrawer.value.formModel.triggerType = info.triggerType;
    }
  } else {
    formDrawer.value.formModel.triggerType = ''
  }
}


/**
 * 接收类型发生改变
 */
const handleReceiveTypeChange = () => {
  formDrawer.value.formModel.userIds = [];
  formDrawer.value.formModel.postIds = [];
}

/**
 * 搜索树Change
 */
const searchTreeChange = async (val: any) => {
  searchModel.value.deviceSn = val.deviceSn;
  await search();
}


/**
 * 添加报警配置
 */
const handleAdd = async () => {
  await fetchPostData();
  await fetchUserData();
  formDrawer.value.visible = true;
  formDrawer.value.formModel.varType = tabsActiveKey.value;
}

/**
 * 删除弹框
 */
const handleDeleteClick = () => {
  if (!tableSelectedKeys.value || tableSelectedKeys.value.length <= 0) {
    notification({code: 201, msg: "请选择要删除的数据"})
    return;
  }
  operateModalModel.value = {
    visible: true,
    type: 2,
    title: '',
    id: -1,
    name: '删除所选记录',
    value: tableSelectedKeys.value
  }
};

/**
 * 修改报警配置
 */
const handleUpdate = async (id: any) => {
  await fetchPostData();
  await fetchUserData();
  let res = await getTriggerConfig(id);
  if (res.code == 200) {
    res.data.receiveType += "";
    //反现岗位或者人员
    let ids: any = [];
    res.data.receiveConcrete.forEach((item: any) => {
      ids.push(Number(item.value));
    })
    if (res.data.receiveType == '1') {
      res.data.postIds = ids;
    }
    if (res.data.receiveType == '2') {
      res.data.userIds = ids;
    }
    formDrawer.value.formModel = res.data;
    if(res.data.startTime || res.data.endTime){
      formDrawer.value.formModel['time'] = [res.data.startTime, res.data.endTime];
    }else{
      formDrawer.value.formModel['time'] = [];
    }
    handleCategoryChange();
    formDrawer.value.visible = true;
  }
}

/**
 * table切换
 */
const handleTabsChange = async () => {
  selectedKeys.clear();
  renderDeviceVarSelectData.value = [];
  formDrawer.value.formModel.varType = tabsActiveKey.value;
  formDrawer.value.formModel.varIds = Array.from(selectedKeys);
}

/**
 * 查询
 */
const search = async () => {
  pagination.current = 1;
  await fetchData();
}


/**
 * 表格页码发生变化
 * @param val
 */
const onPageChange = async (val: number) => {
  pagination.current = val;
  await fetchData();
}

/**
 * 表格每页数量发生变化
 * @param val 值
 */
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchData();
}

/**
 * 表格页码发生变化-变量列表
 * @param val
 */
const onDeviceVarPageChange = async (val: number) => {
  deviceVarPagination.current = val;
  await fetchDeviceVarData();
}

/**
 * 表格每页数量发生变化-变量列表
 * @param val 值
 */
const onDeviceVarPageSizeChange = async (val: number) => {
  deviceVarPagination.pageSize = val;
  await fetchDeviceVarData();
}

/**
 * 修改电站报警状态
 */
const handleAlarmStatusChange = async () => {
  let res = await changeAlarmStatus(alarmStatus.value);
  notification(res);
}

/**
 * 停用
 * @param record
 */
const handleStopFlag = (record: any) => {
  operateModalModel.value = {
    visible: true,
    type: 0,
    title: record.stopFlag == 0 ? "停用" : "启用",
    id: record.id,
    value: record.stopFlag == 0 ? 1 : 0,
    name: record.varName
  }
}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  operateModalModel.value = {
    visible: true,
    type: 1,
    title: "删除",
    id: record.id,
    name: record.varName,
    value: record.id
  }
}

/**
 * 删除提示弹框取消
 */
const handleOperateModelCancle = () => {
  operateModalModel.value = generateOperateModalModel();
}


/**
 * 删除提示弹框确认
 */
const handleOperateModelOk = async () => {
  let result: any = {};
  operateModalModel.value.visible = false;
  setLoading(true);
  if (operateModalModel.value.type == 0) {
    result = await changeTriggerConfigStatus(operateModalModel.value.id, operateModalModel.value.value);
  } else if (operateModalModel.value.type == 1) {
    result = await delTriggerConfig(operateModalModel.value.id);
  } else if (operateModalModel.value.type == 2) {
    result = await delTriggerConfig(operateModalModel.value.value);
  }
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}


/**
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
  formRef.value?.resetFields();
  renderDeviceVarSelectData.value = [];
  tabsActiveKey.value = 1;
  selectedKeys.clear();
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    formDrawer.value.loading = true;
    let receiveConcrete: any = [];
    let postIds = formDrawer.value.formModel.postIds;
    let userIds = formDrawer.value.formModel.userIds;
    if (postIds && postIds.length > 0) {
      renderPostData.value.forEach((item: any) => {
        if (postIds.includes(item.id)) {
          receiveConcrete.push({
            name: item.postName,
            value: item.id
          })
        }
      })
    }
    if (userIds && userIds.length > 0) {
      renderUserData.value.forEach((item: any) => {
        if (userIds.includes(item.id)) {
          receiveConcrete.push({
            name: item.nickName,
            value: item.id
          })
        }
      })
    }
    formDrawer.value.formModel.stationType = props.stationType;
    formDrawer.value.formModel.receiveConcrete = receiveConcrete;
    if (formDrawer.value.formModel.time?.length > 0) {
      formDrawer.value.formModel['startTime'] = formDrawer.value.formModel.time[0];
      formDrawer.value.formModel['endTime'] = formDrawer.value.formModel.time[1];
    }else{
      formDrawer.value.formModel.time = [];
      formDrawer.value.formModel['startTime'] = "00:00";
      formDrawer.value.formModel['endTime'] = "23:59";
    }

    try {
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addTriggerConfig(formDrawer.value.formModel);
      } else {
        result = await updateTriggerConfig(formDrawer.value.formModel);
      }
      notification(result);
      if (result.code == 200) {
        handleFormCancel();
        pagination.current = 1;
        await fetchData();
      }
    }catch (e) {
      console.error(e);
    }finally {
      formDrawer.value.loading = false;
    }
  }
}

/**
 * 查询岗位数据
 */
const fetchPostData = async () => {
  try {
    const res = await listPostAll({});
    if (res.code = 200) {
      renderPostData.value = res.data;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 查询用户数据
 */
const fetchUserData = async () => {
  try {
    const res = await listUserAll({});
    if (res.code = 200) {
      renderUserData.value = res.data;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 获取设备变量列表
 */
const fetchDeviceVarData = async () => {
  try {
    const res = await listDeviceVar({
      ...modalModel.value.serarchModel,
      varType: tabsActiveKey.value,
      pageSize: deviceVarPagination.pageSize,
      pageNum: deviceVarPagination.current,
      stationType: props.stationType,
    });
    if (res.code == 200) {
      res.rows.forEach((item: any) => {
        if (selectedKeys.has(item.id)) {
          item.isAdd = 1;
        }
      });
      modalModel.value.renderData = res.rows;
      deviceVarPagination.total = res.total;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
}

/**
 * 获取设备列表
 */
const fetchDeviceData = async () => {
  try {
    let param = {
      stationType: props.stationType,
    }
    const res = await listFusionGroup(param);
    processSelectable(res.data);
    renderDeviceData.value = res.data;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
}

/**
 * 查询报警类型
 */
const fetchAlarmCategoryData = async () => {
  try {
    const res = await listTriggerCategoryAll({});
    if (res.code == 200) {
      renderAlarmCategoryData.value = res.data;
      renderAlarmCategoryDataAll.value = renderDefaultAlarmCategoryData.value.concat(res.data);
    }
  } catch (err) {
  } finally {
  }
}

/**
 * 获取电站报警开关
 */
const fetchAlarmStatus = async () => {
  try {
    const res = await getAlarmStatus();
    if (res.code == 200) {
      alarmStatus.value = res.data;
    }
  } catch (err) {
  } finally {
  }
}

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const data = await listTriggerConfig({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      stationType: searchModel.value.stationType == -100 ? undefined : searchModel.value.stationType,
      deviceSn: searchModel.value.deviceSn,
      categoryId: searchModel.value.categoryId == -1 ? '' : searchModel.value.categoryId
    });
    renderData.value = data.rows;
    pagination.total = data.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

// 切换能源类型，置空报警
const handleStationType = () => {
  searchModel.value.categoryId = -1;
};
/*************************** 方法区域 end ***************************/

/**
 * 首次加载
 */
onMounted(async () => {
  await fetchAlarmCategoryData();
  await search();
  await fetchAlarmStatus();
})
</script>

