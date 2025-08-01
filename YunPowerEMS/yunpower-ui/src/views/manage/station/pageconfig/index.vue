<!--
 * 功能：页面配置 mapId = 5
 * 作者：曹晓桐
 * 日期：2023-12-23
-->
<template>
  <div>
    <a-card class="content">
      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left"
            auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <!-- 站点类型 -->
                <a-form-item field="ext1" :label="$t('manage.system.siteType')">
                  <a-select v-model="searchModel.ext1" :placeholder="`${$t('manage.system.pleaseSelect')}${$t('manage.system.siteType')}`" allow-clear>
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                      :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <!-- 模块类型 -->
                <a-form-item field="id" :label="$t('manage.station.pageConfig.moduleType')">
                  <a-select v-model="searchModel.id" :options="renderParentData"
                    :field-names="{ value: 'id', label: 'label' }" :placeholder="$t('manage.station.pageConfig.moduleTypeSelect')" allow-clear>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <!-- 模块类型 -->
                <a-button type="primary" @click="search">
                  <template #icon>
                    <icon-search />
                  </template>
                  {{$t('global.search')}}
                </a-button>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

      <!-- 分割线 -->
      <a-divider style="margin-top: 0" />

      <!-- 按钮 -->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus />
              </template>
              {{$t('manage.station.pageConfig.configuration')}}
            </a-button>
            <a-button @click="handleExpand"> {{$t('global.expand')}}/{{$t('global.fold')}} </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 start-->
      <a-table row-key="id" ref="tableRef" :loading="loading" :pagination="false"
        :bordered="{ wrapper: true, cell: true }" :scroll="{ x: 1270 }" :columns="columns"
        :data="renderData">

        <template #parentName="{ record }">
          {{ record.parentName || '--' }}
        </template>
        <!-- 站点类型 -->
        <template #ext1="{ record }">
          <dict-tag :options="sys_station_type" :value="String(record.ext1)" />
        </template>
        <!-- 是否动态 -->
        <template #isShow="{ record }">
          <a-tag v-if="record.isShow == 0" color="rgb(var(--primary-6))">{{$t('global.yes')}}</a-tag>
          <a-tag v-else color="rgb(var(--red-6))">{{$t('global.no')}}</a-tag>
        </template>
        <!-- 状态 -->
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"></stop-flag>
        </template>
        <!-- 操作 -->
        <template #operate="{ record }">
          <a-button v-if="record.parentId > 0" size="small" type="text" status="success"
            @click="handleConfig(record.id)">{{$t('manage.station.pageConfig.disposition')}}</a-button>
          <a-button size="small" type="text" @click="handleStopFlag(record)"
            :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button v-if="record.isSystem == 0" size="small" type="text" status="success" @click="handleUpdate(record.id)">{{$t('global.edit')}}</a-button>
          <a-button v-if="record.isSystem == 0" size="small" type="text" status="danger" @click="handleDelete(record)">{{$t('global.delete')}}
          </a-button>
        </template>
      </a-table>
      <!-- 表格内容 end-->


      <!-- 操作弹框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible" class="modal-box">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
            style="color: rgb(var(--red-6)); margin-right: 5px" />
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px" />
          {{$t('global.confirmTip')}}
        </template>
        <div style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{ operateModalModel.name }}】的数据项？</div>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">{{$t('global.cancel')}}</a-button>
              <a-button type="primary" @click="handleOperateModelOk">{{$t('global.confirm')}}</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>
      <!-- 操作弹框 end -->

      <!-- 添加右弹层 start -->
      <a-drawer :width="750" :visible="formDrawer.visible" @ok="handleSubmitForm" @cancel="handleFormCancel"
        :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id == 0 ? $t('manage.station.pageConfig.addconfig') : $t('manage.station.pageConfig.editconfig') }}
        </template>
        <a-row :gutter="24" class="row-mp-30">
          <a-col :span="24">
            <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
              :rules="formDrawer.rules">

              <!-- 站点类型 -->
              <a-form-item field="ext1" :label="$t('manage.station.pageConfig.siteType')">
                <a-select :disabled="formDrawer.formModel.id == 0 ? false : true" v-model="formDrawer.formModel.ext1"
                  :placeholder="$t('manage.station.pageConfig.siteTypeSelect')" allow-clear>
                  <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                    :value="dict.value"></a-option>
                </a-select>
              </a-form-item>

              <!-- 上级目录 -->
              <a-form-item field="parentId" :label="$t('manage.station.pageConfig.parentDirectory')">
                <a-select v-model="formDrawer.formModel.parentId" :options="parentList"
                  :field-names="{ value: 'id', label: 'label' }" @change="handleParentChange" :placeholder="$t('manage.station.pageConfig.directorySelect')"
                  allow-clear>
                </a-select>
              </a-form-item>

              <!-- 模块名称 -->
              <a-form-item field="groupName" :label="$t('manage.station.pageConfig.moduleName')">
                <a-input placeholder="" v-model="formDrawer.formModel.groupName" />
              </a-form-item>

              <!-- 页面标识 -->
              <a-form-item field="groupSn" :label="$t('manage.station.pageConfig.identity')">
                <a-input placeholder="" v-model="formDrawer.formModel.groupSn" />
              </a-form-item>

              <!-- 页面排序  -->
              <a-form-item field="orderNum" :label="$t('manage.station.pageConfig.sorting')">
                <a-input-number v-model="formDrawer.formModel.orderNum" />
              </a-form-item>

              <!-- 是否动态 -->
              <a-form-item field="isShow" :label="$t('manage.station.pageConfig.dynamicOrNot')">
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                  v-model="formDrawer.formModel.isShow"><template #checked>{{$t('global.yes')}}</template><template
                    #unchecked>{{$t('global.no')}}</template></a-switch>
              </a-form-item>

              <!-- 启用状态 -->
              <a-form-item field="stopFlag" :label="$t('global.status')">
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                  v-model="formDrawer.formModel.stopFlag"><template #checked>{{$t('global.enable')}}</template><template
                    #unchecked>{{$t('global.forbidden')}}</template></a-switch>
              </a-form-item>

              <a-form-item field="paramInfo" :label="$t('manage.station.pageConfig.remark')">
                <a-textarea :placeholder="$t('manage.station.pageConfig.remarksMmaximum')" :max-length="200" v-model="formDrawer.formModel.remark"
                  show-word-limit allow-clear />
              </a-form-item>

            </a-form>
          </a-col>
        </a-row>
      </a-drawer>
      <!-- 添加右弹层 end -->

      <!-- 配置-右弹层 start -->
      <a-drawer :width="1500" :visible="formDrawerConfig.visible" @ok="handleConfigSubmitForm"
        @cancel="handleConfigFormCancel" :mask-closable="false">
        <template #title>
          <!-- 页面配置 -->
          {{$t('manage.station.pageConfig.pageConfiguration')}}
        </template>
        <a-spin :loading="formDrawerConfig.loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
          <a-row :gutter="24" class="row-mp-30">
            <a-col :span="24">
              <a-form ref="formConfigRef" auto-label-width :model="formDrawerConfig.formModel" label-align="right"
                :rules="formDrawerConfig.rules">

                <!-- 站点类型 -->
                <a-form-item field="stationTypeName" :label="$t('manage.station.pageConfig.siteType')">
                  <a-input readonly v-model="formDrawerConfig.formModel.stationTypeName"></a-input>
                </a-form-item>

                <!-- 模块类型 -->
                <a-form-item field="groupParentName" :label="$t('manage.station.pageConfig.moduleType')">
                  <a-input readonly v-model="formDrawerConfig.formModel.groupParentName"></a-input>
                </a-form-item>

                <!-- 模块名称 -->
                <a-form-item field="groupName" :label="$t('manage.station.pageConfig.moduleName')">
                  <a-input v-model="formDrawerConfig.formModel.groupName" readonly />
                </a-form-item>

                <!-- 页面标识 -->
                <a-form-item field="pageValue" :label="$t('manage.station.pageConfig.identity')">
                  <a-input v-model="formDrawerConfig.formModel.pageValue" readonly />
                </a-form-item>

                <!-- X轴类型 -->
                <a-form-item field="xAxis" :label="$t('manage.station.pageConfig.x-axis')">
                  <a-select v-model="formDrawerConfig.formModel.xAxis" :placeholder="$t('manage.station.pageConfig.x-axisSelect')" allow-clear>
                    <a-option v-for="dict in sys_config_page_x" :key="dict.value" :label="dict.label"
                      :value="Number(dict.value)"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 选择变量 -->
                <a-form-item hide-label>
                  <a-space>
                    <a-button type="primary" @click="handleDeviceVarAdd(null)">{{$t('manage.station.pageConfig.variableSelect')}}</a-button>
                    <a-button @click="handleConfigExpand"> {{$t('global.expand')}}/{{$t('global.fold')}} </a-button>
                  </a-space>
                </a-form-item>

                <!-- 变量列表 -->
                <a-form-item hide-label>
                  <a-table ref="tableConfigRef" row-key="varSn" :scroll="{ y: 300 }"
                    :bordered="{ wrapper: true, cell: true }" :columns="deviceVarSelectColumns"
                    :data="formDrawerConfig.formModel.variableConfig" :pagination="false"
                    v-bind:hide-expand-button-on-empty="true">

                    <!-- 变量名称 -->
                    <template #varName="{ record }">
                      <a-input v-model="record.varName" />
                    </template>

                    <!-- 单位 -->
                    <template #unit="{ record }">
                      <a-input v-model="record.unit" />
                    </template>

                    <!-- 存储类型 -->
                    <template #storageType="{ record }">
                      <a-select v-model="record.storageType" :placeholder="$t('global.pleaseSelect')" allow-clear>
                        <a-option v-for="dict in sys_config_page_storage" :key="dict.value" :label="dict.label"
                          :value="Number(dict.value)"></a-option>
                      </a-select>
                    </template>

                    <!-- 取值类型 -->
                    <template #dataType="{ record }">
                      <a-select v-model="record.dataType" :placeholder="$t('global.pleaseSelect')" allow-clear>
                        <a-option v-for="dict in sys_config_page_y" :key="dict.value" :label="dict.label"
                          :value="dict.value"></a-option>
                      </a-select>
                    </template>

                    <!-- 是否堆叠 -->
                    <template #isStack="{ record }">
                      <a-select v-model="record.isStack" :placeholder="$t('global.pleaseSelect')" allow-clear>
                        <a-option v-for="dict in sys_yes_no" :key="dict.value" :label="dict.label"
                          :value="Number(dict.value)"></a-option>
                      </a-select>
                    </template>

                    <!-- 是否环比 -->
                    <template #isChain="{ record }">
                      <a-select v-model="record.isChain" :placeholder="$t('global.pleaseSelect')" allow-clear>
                        <a-option v-for="dict in sys_yes_no" :key="dict.value" :label="dict.label"
                          :value="Number(dict.value)"></a-option>
                      </a-select>
                    </template>

                    <!-- 图表类型 -->
                    <template #chartType="{ record }">
                      <a-select v-model="record.chartType" :placeholder="$t('global.pleaseSelect')" allow-clear>
                        <a-option v-for="dict in sys_chart_type" :key="dict.value" :label="dict.label"
                          :value="dict.value"></a-option>
                      </a-select>
                    </template>

                    <template #operate="{ record }">
                      <a-button size="small" type="text" @click="handleDeviceVarAdd(record.key)">{{$t('manage.station.pageConfig.subvariable')}}</a-button>
                      <a-button size="small" type="text" status="danger"
                        @click="handleDeviceVarDelete(record)">{{$t('manage.station.pageConfig.Remove')}}</a-button>
                    </template>
                  </a-table>
                </a-form-item>

                <!-- 备注 -->
                <a-form-item field="remark" :label="$t('manage.station.pageConfig.remark')">
                  <a-textarea :placeholder="$t('manage.station.pageConfig.remarksMmaximum')" :max-length="200" v-model="formDrawerConfig.formModel.remark"
                    show-word-limit allow-clear />
                </a-form-item>
              </a-form>
            </a-col>
          </a-row>
        </a-spin>
      </a-drawer>
      <!-- 配置-右弹层 end -->


      <!-- 选择变量弹框 -->
      <a-modal width="800px" :visible="modalModel.visible" :footer="false" title-align="start" title="请选择变量"
        @cancel="handleModalClose">
        <a-row>
          <a-col :flex="1" style="margin-top: 4px;">
            <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
              label-align="left" auto-label-width>
              <a-row :gutter="16">
                <a-col :span="10">
                  <a-form-item field="deviceId" :label="$t('manage.station.pageConfig.deviceSelect')">
                    <a-tree-select v-model="formDrawerConfig.formModel.deviceId" :data="renderDeviceData"
                      :placeholder="$t('manage.station.pageConfig.devicePlaceholder')" :fieldNames="{
                        key: 'id', title: 'deviceName', children: 'children'
                      }" :allow-search="true" allow-clear :filter-tree-node="filterTreeNode"></a-tree-select>
                  </a-form-item>
                </a-col>
                <a-col :span="10">
                  <a-form-item field="varName" :label="$t('manage.station.pageConfig.variableName')">
                    <a-input v-model="formDrawerConfig.formModel.varName" allow-clear :placeholder="$t('manage.station.pageConfig.variablePlaceholder')"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="4">
                  <!-- 模块类型 -->
                  <a-button type="primary" @click="searchDeviceVar">
                    <template #icon>
                      <icon-search />
                    </template>
                    {{$t('global.search')}}
                  </a-button>
                </a-col>
              </a-row>
            </a-form>
          </a-col>
        </a-row>
        <a-table :scroll="{ y: 400 }" row-key="varSn" :bordered="{ wrapper: true, cell: true }"
          :columns="deviceVarTabColumns" :data="modalModel.renderData" @page-change="onDeviceVarPageChange"
          :pagination="deviceVarPagination" @page-size-change="onDeviceVarPageSizeChange">
          <template #operate="{ record }">
            <a-button size="small" type="primary" @click="handleDeviceVarSelect(record)">添加</a-button>
          </template>
        </a-table>
      </a-modal>

<!--      <select-variables :visible="modalModel.visible" :formDrawerConfig="formDrawerConfig"></select-variables>-->
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { computed, getCurrentInstance, onMounted, reactive, ref } from 'vue';
import useLoading from '@/hooks/loading';
import { Message, TableInstance } from "@arco-design/web-vue";
import { notification } from "@/hooks/my-design";
import { BasePagination } from '@/api/common';
import { FormInstance } from '@arco-design/web-vue/es/form';
import { getGroup, addGroup, updateGroup, delGroup, listGroupAll, listSelect } from "@/api/system/group";
import { listFusionGroup } from '@/api/system/device';
import { processSelectable } from '@/utils/ruoyi';
import { listDeviceVar } from '@/api/system/device-var';
import { VariableConfigEntity, getPageConfigByGroupId, updatePageConfig, addPageConfig } from '@/api/system/page-config';
import { getDictLabel } from '@/utils/dict';
import { useTimeoutFn } from '@vueuse/core';
import SelectVariables from "@/components/select-variables/index.vue";

/*************************** 变量区域 begin ***************************/

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_station_type
  , sys_config_page_x
  , sys_config_page_storage
  , sys_config_page_y
  , sys_yes_no
  , sys_chart_type
} = proxy?.useDict("sys_station_type"
  , "sys_config_page_x"
  , "sys_config_page_storage"
  , "sys_config_page_y"
  , "sys_yes_no"
  , "sys_chart_type");
//******* 这里编写动态获取下拉列表 end ******


//参数
const props = reactive<any>({
  mapId: 5
})


//生成查询条件对象
const generateSearchModel = () => {
  return {
    ext1: undefined,
    id: undefined,
    mapId: props.mapId
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const { loading, setLoading } = useLoading(true);
//表格分页参数
const pagination: any = reactive({ ...BasePagination() });
//表格实例
const tableRef = ref<TableInstance>();
const tableConfigRef = ref<TableInstance>();
//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    align: 'center',
    width: 100,
    fixed: "left"
  },
  {
    title: "站点类型",
    dataIndex: "ext1",
    slotName: "ext1",
    align: "center",
    width: 100
  },
  {
    title: "模块类型",
    dataIndex: "parentName",
    slotName: "parentName",
    align: "center",
    width: 180
  },
  {
    title: "模块名称",
    dataIndex: "groupName",
    align: "left",
    width: 180,
  },
  {
    title: "页面标识",
    dataIndex: "groupSn",
    align: "left",
    width: 180,
    ellipsis: true,
    tooltip: { position: 'top' }
  },
  {
    title: "是否动态",
    dataIndex: "isShow",
    slotName: "isShow",
    align: "center",
    width: 100
  },
  {
    title: "排序",
    dataIndex: "orderNum",
    align: "center",
    width: 100
  },
  {
    title: "状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100,
    fixed: "right"
  },
  {
    title: "操作",
    dataIndex: "operate",
    width: 240,
    slotName: 'operate',
    align: 'center',
    fixed: "right"
  },
]);

//表格数据
const renderData = ref<any[]>([]);
//父级模块
const renderParentData = ref<any[]>([]);
//展开/折叠控制值
const expandValue = ref<boolean>(false);
//展开/折叠控制值
const expandConfigValue = ref<boolean>(false);
//设备数据
const renderDeviceData = ref<any>([]);
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
//表格示例
const formRef = ref<FormInstance>();
const formConfigRef = ref<FormInstance>();
//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    configVisible: false,
    rules: {
      ext1: [{ required: true, message: "请选择站点类型" }],
      parentId: [{ required: true, message: "请选择上级目录" }],
      groupName: [{ required: true, message: "请输入模块名称" }],
      groupSn: [{ required: true, message: "请输入页面标识" }],
      orderNum: [{ required: true, message: "请输入页面排序" }],
    },
    formModel: {
      id: 0,
      groupName: '',
      groupSn: '',
      ext1: undefined,
      parentId: 0,
      parentName: '',
      orderNum: 0,
      stopFlag: 0,
      isShow: 1,
      mapId: props.mapId,
      remark: "",
      xAxis: '',
      deviceId: undefined,
    },
  };
};
//表单模型
const formDrawer = ref(generateFormDrawerModel());

//生成页面模型
const generateFormDrawerConfigModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {
      stationTypeName: [{ required: true, message: "请重新选择配置项" }],
      groupParentName: [{ required: true, message: "请重新选择配置项" }],
      groupName: [{ required: true, message: "请重新选择配置项" }],
      pageValue: [{ required: true, message: "请重新选择配置项" }],
      xAxis: [{ required: true, message: "请选择X轴类型" }],
      deviceId: [{ required: true, message: "请选择设备" }],
    },
    formModel: {
      id: 0,
      groupId: 0,
      deviceId: undefined,
      varName: undefined,
      groupParentName: undefined,
      groupName: undefined,
      methodValue: undefined,
      pageValue: undefined,
      remark: undefined,
      stationType: 0,
      stationTypeName: '',
      stopFlag: 0,
      updateBy: undefined,
      updateTime: undefined,
      variableConfig: [] as VariableConfigEntity[],
      xAxis: 0
    }
  }
};
//页面配置-模型
const formDrawerConfig = ref(generateFormDrawerConfigModel());

//switch选中值
const checkedValue = ref<number>(0);
//switch未选中值
const unCheckedValue = ref<number>(1);
//默认上级目录
const defaultParentList = ref<any>(
  [
    {
      id: 0,
      label: "根目录"
    }
  ]
);
//上级目录
const parentList = ref<any>();

//选择变量弹框模型
const generateModalModel = () => {
  return {
    visible: false,
    parentId: '',
    //表格数据
    renderData: []
  }
}
//选中的key
const selectedKeys = new Set<String>();
//弹框模型
const modalModel = ref(generateModalModel());
//变量分页
const deviceVarPagination: any = reactive({ ...BasePagination() });
// deviceVarPagination.defaultPageSize = 5;
// deviceVarPagination.pageSize = 5;
// deviceVarPagination.pageSizeOptions = [5];
//变量列
const deviceVarTabColumns = computed<any[]>(() => [
  {
    title: "设备名称",
    dataIndex: "deviceName",
    align: 'left',
    width: 150
  }, {
    title: "变量名称",
    dataIndex: "varName",
    align: 'left',
    width: 150
  }, {
    title: "变量编码",
    dataIndex: "varSn",
    align: 'left',
    width: 150
  }, {
    title: "单位",
    dataIndex: "unit",
    align: 'left',
    width: 100
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    width: 100,
    align: 'center'
  }
]);

//变量列
const deviceVarSelectColumns = computed<any[]>(() => [
  {
    title: "变量名称",
    dataIndex: "varName",
    slotName: "varName",
    align: 'center',
    width: 150,
    ellipsis: true,
    tooltip: { position: 'top' },
  }, {
    title: "变量编码",
    dataIndex: "varSn",
    align: 'center',
    width: 130,
    ellipsis: true,
    tooltip: { position: 'top' },
  }, {
    title: "索引编码",
    dataIndex: "varMapSn",
    align: 'center',
    width: 80
  }, {
    title: "单位",
    dataIndex: "unit",
    slotName: "unit",
    align: 'center',
    width: 80
  }, {
    title: "存储类型",
    dataIndex: "storageType",
    slotName: "storageType",
    align: 'center',
    width: 115
  }, {
    title: "取值类型",
    dataIndex: "dataType",
    slotName: "dataType",
    align: 'center',
    width: 180
  }, {
    title: "是否堆叠",
    dataIndex: "isStack",
    slotName: "isStack",
    align: 'center',
    width: 115
  }, {
    title: "是否环比",
    dataIndex: "isChain",
    slotName: "isChain",
    align: 'center',
    width: 115
  }, {
    title: "图表类型",
    dataIndex: "chartType",
    slotName: "chartType",
    align: 'center',
    width: 115
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    width: 150,
    align: 'center',
  }
]);
/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

//重置查询条件
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

const searchDeviceVar = async () => {
  deviceVarPagination.current = 1;
  await fetchDeviceVarData();
}

/**
 * 新增数据
 * @param row 数据行
 */
const handleAdd = async () => {
  await fetchParentData();
  formDrawer.value.visible = true;
}

/**
 * 展开/折叠
 */
const handleExpand = () => {
  expandValue.value = !expandValue.value;
  tableRef.value?.expandAll(expandValue.value);
}

const handleConfigExpand = () => {
  expandConfigValue.value = !expandConfigValue.value;
  tableConfigRef.value?.expandAll(expandConfigValue.value);
}

/**
 * 选择上级目录
 */
const handleParentChange = () => {
  if (formDrawer.value.formModel.parentId > 0) {
    let info: any = parentList.value.find((item: any) => item.id == formDrawer.value.formModel.parentId)
    if (info) {
      formDrawer.value.formModel.parentName = info.label;
    }
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
 * 配置
 * @param record
 */
const handleConfig = async (id: any) => {
  formDrawerConfig.value.visible = true;
  formDrawerConfig.value.loading = true;
  try {
    let result = await getPageConfigByGroupId(id);
    if (result.code == 200 && result.data) {
      result.data.variableConfig = result.data.variableConfig || [];
      result.data.stationTypeName = getDictLabel("sys_station_type", result.data.stationType);
      processVariableConfig(result.data.variableConfig, '');
      console.log(result.data.variableConfig);
      formDrawerConfig.value.formModel = result.data;
      await fetchDeviceData();
    }
  } catch (e) {
    console.error(e);
  } finally {
    formDrawerConfig.value.loading = false;
    useTimeoutFn(async () => {
      handleConfigExpand();
    }, 500)
  }
}

/**
 * 处理变量
 */
const processVariableConfig = (data: any, parent: string) => {
  data.forEach((item: any, index: number) => {
    if (parent == '') {
      item.parentId = '0';
      item.key = `${index}@${item.varSn}`;
    } else {
      item.parentId = parent;
      item.key = `${item.parentId}@${item.varSn}`;
    }
    if (item.children && item.children.length > 0) {
      processVariableConfig(item.children, item.key);
    }
  })
}

/**
 * 设备搜索
 * @param searchValue
 * @param nodeData
 */
const filterTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.deviceName.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
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
 * 选择变量
 */
const handleDeviceVarAdd = async (parentId: any) => {
  modalModel.value = generateModalModel();
  if (parentId) {
    modalModel.value.parentId = parentId;
  }
  deviceVarPagination.current = 1;
  await fetchDeviceVarData();
  modalModel.value.visible = true;
}

/**
 * 设备变量选中
 */
const handleDeviceVarSelect = (record: any) => {
  try {
    let param: VariableConfigEntity = {
      unit: record.unit,
      varMapSn: record.varMapSn,
      varName: record.varName,
      varSn: record.varSn,
      children: [] as VariableConfigEntity[],
      key: '',
      parentId: '0'
    }
    let variableConfig = formDrawerConfig.value.formModel.variableConfig;
    if (!modalModel.value.parentId || modalModel.value.parentId == '') {
      param.key = `${variableConfig.length}@${param.varSn}`;
      variableConfig.push(param as VariableConfigEntity)
      formDrawerConfig.value.formModel.variableConfig = variableConfig;
    } else {
      let info: VariableConfigEntity | undefined = getItem(variableConfig, modalModel.value.parentId);
      if (info) {
        param.parentId = modalModel.value.parentId;
        param.key = `${param.parentId}@${param.varSn}`;
        info.children.push(param as VariableConfigEntity)
      }
    }
  } catch (error) {
    console.error(error)
  } finally {
    tableConfigRef.value?.expandAll(true);
  }
}

/**
 * 获取数据
 * @param data
 * @param parentKey
 */
const getItem = (data: VariableConfigEntity[], parentKey: string): VariableConfigEntity | undefined => {
  for (const item of data) {
    if (item.key === parentKey) {
      return item;
    }
    if (item.children && item.children.length > 0) {
      const result = getItem(item.children, parentKey);
      if (result) {
        return result;
      }
    }
  }
  return undefined;
};

/**
 * 设备变量移除
 * @param record
 */
const handleDeviceVarDelete = (record: any) => {
  let info: VariableConfigEntity | undefined = getItem(formDrawerConfig.value.formModel.variableConfig, record.key);
  if (info) {
    if (info.children && info.children.length > 0) {
      Message.error({
        content: `请先移除子变量`,
        duration: 1.5 * 1000,
      });
      return false;
    }
    let data = formDrawerConfig.value.formModel.variableConfig;
    // formDrawerConfig.value.formModel.variableConfig = formDrawerConfig.value.formModel.variableConfig.filter((item: any) => item.varSn !== record.varSn);
    data = removeData(data, record.key)
    formDrawerConfig.value.formModel.variableConfig = data;
  }
}

function removeData(data: VariableConfigEntity[], key: string): VariableConfigEntity[] {
  return data.filter(item => {
    if (item.key === key) {
      return false;
    }
    if (item.children) {
      item.children = removeData(item.children, key);
    }
    return true;
  });
}

/**
 * 选择变量-关闭
 */
const handleModalClose = () => {
  modalModel.value = generateModalModel();
}

/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (id: any) => {
  await fetchParentData();
  let result = await getGroup(id);
  if (result.code == 200) {
    result.data.ext1 += '';
    formDrawer.value.formModel = result.data;
    formDrawer.value.visible = true;
  }
}

const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.groupName;
  operateModalModel.value.value = record.stopFlag == 0 ? 1 : 0;
  operateModalModel.value.type = 0;
}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = '删除';
  operateModalModel.value.name = record.groupName;
  operateModalModel.value.type = 1;
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
  let result;
  if (operateModalModel.value.type == 0) {
    let info = await getGroup(operateModalModel.value.id);
    if (info.code == 200) {
      result = await updateGroup({ ...info.data, stopFlag: operateModalModel.value.value });
    } else {
      result = info;
    }
  } else {
    result = await delGroup(operateModalModel.value.id);
  }
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    let result;
    if (formDrawer.value.formModel.id == 0) {
      result = await addGroup(formDrawer.value.formModel);
    } else {
      result = await updateGroup(formDrawer.value.formModel);
    }
    notification(result);
    if (result.code == 200) {
      handleFormCancel();
      await fetchData();
      await fetchParentData();
    }
  }
}

/**
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
}

/**
 * 配置报错
 */
const handleConfigSubmitForm = async () => {
  const validate = await formConfigRef.value?.validate();
  if (!validate) {
    let result
    if (formDrawerConfig.value.formModel.id > 0) {
      result = await updatePageConfig(formDrawerConfig.value.formModel);
    } else {
      result = await addPageConfig(formDrawerConfig.value.formModel);
    }
    notification(result);
    if (result.code == 200) {
      handleConfigFormCancel();
      // await fetchData();
    }
  }
}

/**
 * 配置-表单取消
 */
const handleConfigFormCancel = () => {
  formDrawerConfig.value = generateFormDrawerConfigModel();
  expandConfigValue.value = false;
}

/**
 * 查询表格数据
 */
const fetchParentData = async () => {
  try {
    const res = await listSelect({
      parentId: 0,
      mapId: props.mapId
    });
    renderParentData.value = res.data;
    parentList.value = defaultParentList.value.concat(res.data);
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 获取设备列表
 */
const fetchDeviceData = async () => {
  try {
    const res = await listFusionGroup({ stationType: formDrawerConfig.value.formModel.stationType });
    console.log(formDrawerConfig.value.formModel.stationType)
    processSelectable(res.data);
    renderDeviceData.value = res.data;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
}


/**
 * 获取设备变量列表
 */
const fetchDeviceVarData = async () => {
  try {
    const res = await listDeviceVar({
      deviceId: formDrawerConfig.value.formModel.deviceId,
      varName: formDrawerConfig.value.formModel.varName,
      pageSize: deviceVarPagination.pageSize,
      pageNum: deviceVarPagination.current,
    });
    if (res.code == 200) {
      modalModel.value.renderData = res.rows;
      deviceVarPagination.total = res.total;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listGroupAll({
      ...searchModel.value,
    });
    removeEmptyChildren(res.data);
    renderData.value = res.data;
    tableRef.value?.expandAll(false);
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
  tableRef.value?.expandAll(false);
  await fetchParentData();
})
</script>

<style scoped></style>
