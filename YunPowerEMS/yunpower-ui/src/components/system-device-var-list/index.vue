<!--
 * 功能：变量列表
 * 作者：曹晓桐
 * 日期：2023-11-7
-->
<template>
  <div>
    <a-card class="content">
      <a-row>
        <a-col>
          <!-- tab 切换 -->
          <a-tabs v-model:active-key="tabsActiveKey" @change="handleTabsChange">
            <a-tab-pane v-for="(item, index) in tabsList" :key="item.key" :title="item.title">
            </a-tab-pane>
          </a-tabs>
        </a-col>
      </a-row>

      <!-- 搜索条件 -->
      <a-row>
        <a-col :flex="1">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left" auto-label-width>
            <a-row :gutter="16">
              <a-col :span="6">
                <a-form-item field="catalogId" :label="$t('power.energy.device.name')">
                  <a-tree-select allow-clear allow-search v-model="searchModel.deviceId"
                                 :fieldNames="catalogIdFieldNames" :data="deviceData"
                                 :placeholder="$t('global.pleaseSelect')"></a-tree-select>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item field="deviceName" :label="$t('power.energy.device.varName')">
                  <a-input v-model="searchModel.varName" :placeholder="$t('global.please')" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-space direction="vertical" :size="18">
                  <a-button type="primary" @click="search">
                    <template #icon>
                      <icon-search/>
                    </template>
                    {{ $t('global.search') }}
                  </a-button>
                </a-space>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
      </a-row>

      <!-- 分割线 -->
      <a-divider style="margin-top: 0"/>

      <!-- 按钮 -->
      <a-row style="margin-bottom: 20px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleDeviceVarAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('power.energy.device.addVariable') }}
            </a-button>
            <a-button type="primary" style="background-color:rgb(var(--arcoblue-4))" @click="handleExport">
              <template #icon>
                <icon-download/>
              </template>
              {{ $t('power.energy.device.varExport') }}
            </a-button>
            <a-upload :show-file-list="false" :action="(`${baseUrl}/system/device-var/importData`)"
                      @success="handleUploadSuccess" :on-before-upload="handleBeforeUpload" :headers="headers"
                      accept=".xlsx, .xls">
              <template #upload-button>
                <a-button type="primary" style="background-color: rgb(var(--red-5));">
                  <template #icon>
                    <icon-upload/>
                  </template>
                  {{ $t('power.energy.device.varImport') }}
                </a-button>
              </template>
            </a-upload>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格 start -->
      <a-table size="medium" row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }"
               :pagination="pagination" :columns="tabColumns" :scroll="{ x: 2450 }" :data="renderData"
               @page-change="onPageChange" @page-size-change="onPageSizeChange">

        <!-- 是否监控 -->
        <!--        <template #isMonitor="{ record }">-->
        <!--          <a-tag v-if="record.isMonitor == 0" color="rgb(var(&#45;&#45;primary-6))">否</a-tag>-->
        <!--          <a-tag v-else color="rgb(var(&#45;&#45;red-6))">是</a-tag>-->
        <!--        </template>-->

        <!--变量类型-->
        <template #varType="{ record }">
          <template v-for="item in sys_config_variable_type" :key="item.value">
            <span v-if="item.value == record.variableType">{{ item.label }}</span>
          </template>
        </template>

        <!-- 是否修正 -->
        <template #dataFix="{ record }">
          <a-tag v-if="record.dataFix" color="rgb(var(--red-6))">是</a-tag>
          <a-tag v-else color="rgb(var(--primary-6))">否</a-tag>
        </template>

        <!-- 是否累积 -->
        <template #isAccumulation="{ record }">
          <a-tag v-if="record.isAccumulation == 0" color="rgb(var(--primary-6))">否</a-tag>
          <a-tag v-else color="rgb(var(--red-6))">是</a-tag>
        </template>

        <!-- 数据来源-->
        <template #origin="{ record }">
          <dict-tag :options="sys_data_origin" :value="record.origin"/>
        </template>

        <!-- 数据类型-->
        <template #dataType="{ record }">
          <dict-tag :options="sys_data_type" :value="record.dataType"/>
        </template>

        <!-- 存盘周期-->
        <template #saveCycle="{ record }">
          <dict-tag :options="sys_save_cycle" :value="record.saveCycle"/>
        </template>
        <!-- 状态 -->
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"/>
<!--          <span v-if="record.stopFlag === 1" class="circle-device"></span>-->
<!--          <span v-else class="circle-device pass"></span>-->
        </template>
        <template #operate="{ record }">
          <!--          <a-button size="small" type="text" @click="handleMonitor(record)"-->
          <!--            :status="record.isMonitor == 0 ? 'normal' : 'warning'">-->
          <!--            {{ record.isMonitor == 0 ? $t('power.energy.device.openMonitor') : $t('power.energy.device.closeMonitor') }}-->
          <!--          </a-button>-->
          <a-button size="small" type="text" @click="handleStopFlag(record)"
                    :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" @click="handleDeviceVarUpdate(record.id)">{{
              $t('global.edit')
            }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{
              $t('global.delete')
            }}
          </a-button>
          <a-button size="small" type="text" @click="handleDeviceDetail(record)">{{ $t('global.detail') }}</a-button>
        </template>
      </a-table>
      <!-- 表格 end -->

      <!-- 操作弹框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                             style="color: rgb(var(--red-6)); margin-right: 5px"/>
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
          确认提示
        </template>
        <div v-if="operateModalModel.type < 2" style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{
            operateModalModel.name
          }}】的数据项？
        </div>
        <div v-if="operateModalModel.type == 2" style="text-align: center;">是否确认为【{{ operateModalModel.name }}】数据{{
            operateModalModel.title
          }}？
        </div>
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

      <!-- 详情右弹层 start  -->
      <a-drawer :width="1000" :title="formDrawer.title" :visible="formDrawer.visible" hide-cancel
                @cancel="handleDrawerCancel" @ok="handleDrawerCancel" unmountOnClose :mask-closable="false">
        <a-row :gutter="24" style="padding: 0 20px;">
          <a-col :span="12">
            <a-divider orientation="left">变量信息</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
            color: 'rgb(var(--color-neutral-10))',
          }" :data="formDrawer.varInfo" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
            <a-divider orientation="left">数据来源</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                            :data="formDrawer.dataSource" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
          </a-col>
          <a-col :span="12">
            <a-divider orientation="left">数据处理</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                            :data="formDrawer.dataHandle" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
            <a-divider orientation="left">数据过滤</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                            :data="formDrawer.dataFilter" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
            <a-divider orientation="left">系统信息</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                            :data="formDrawer.systemInfo" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
          </a-col>
        </a-row>
      </a-drawer>
      <!-- 详情右弹层 end -->
      <a-modal width="450px" v-model:visible="uploadResultVisible" :hide-cancel="true">
        <template #title>
          <icon-check-circle-fill size="18" style="color: rgb(var(--primary-6)); margin-right: 5px" v-if="uploadResultType === 'success'" />
          <icon-close-circle-fill size="18" style="color: rgb(var(--red-5)); margin-right: 5px" v-else />
          {{uploadResultType === 'success' ? '上传成功' : '上传失败'}}
        </template>
        <div style="max-height: 200px;" v-html="uploadResult"></div>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" @click="closeUploadResult">确认</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import {useRouter, useRoute} from 'vue-router'
import {computed, getCurrentInstance, reactive, ref} from 'vue';
import {StationTypeEnum, listFusionGroup} from '@/api/system/device';
import {processSelectable} from '@/utils/ruoyi';
import {onMounted} from 'vue';
import useLoading from '@/hooks/loading';
import {BasePagination} from '@/api/common';
import {FileItem, Message} from "@arco-design/web-vue";
import {notification} from "@/hooks/my-design";
import {
  changeDeviceVarStatus,
  changeMonitorStatus,
  delDeviceVar,
  getDeviceVar,
  listDeviceVar
} from '@/api/system/device-var';
import {getDictLabel} from '@/utils/dict';
import {getChannelDevice} from '@/api/system/channel-device';
import {download} from '@/api/download';
import {getToken} from '@/utils/auth';
import dayjs from 'dayjs';

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_data_type,
  sys_data_origin,
  sys_save_cycle,
  sys_config_variable_type
} = proxy?.useDict("sys_data_type", "sys_data_origin", "sys_save_cycle", "sys_config_variable_type");
//******* 这里编写动态获取下拉列表 end ******

/*************************** 变量区域 begin ***************************/
//路由
const router = useRouter();
const route = useRoute();
//tab切换选中值
const tabsActiveKey = ref<number>(1);
//组件参数
const props = defineProps({
  stationType: {
    type: Number,
    default: StationTypeEnum.power,
  }
})
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
const catalogIdFieldNames = ref<any>({
  key: 'key', title: 'deviceName', children: 'children'
});
const deviceData = ref<any>([]);
//生成查询条件对象
const generateSearchModel = () => {
  return {
    varName: '',
    deviceId: undefined
  };
};

//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    title: '',
    varInfo: [],
    dataSource: [],
    dataHandle: [],
    dataFilter: [],
    systemInfo: []
  };
};
//表单模型
const formDrawer = ref(generateFormDrawerModel());

//查询表单对象
const searchModel = ref(generateSearchModel());
//表格数据
const renderData = ref<any>([]);
//加载中
const {loading, setLoading} = useLoading(true);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
//设置表格列
const tabColumns = computed<any[]>(() => [
  {
    title: "变量名称",
    dataIndex: "varName",
    fixed: "left",
    width: 250,
    align: "left",
  },
  {
    title: "变量编码",
    fixed: "left",
    width: 200,
    dataIndex: "varSn",
    align: "left",
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "变量类型",
    width: 180,
    dataIndex: "varType",
    slotName: 'varType',
    align: "left",
  },
  // {
  //   title: "变量属性",
  //   width: 180,
  //   dataIndex: "varMapName",
  //   align: "left",
  // },
  // {
  //   title: "索引变量编码",
  //   width: 180,
  //   dataIndex: "varMapSn",
  //   align: "left",
  //   ellipsis: true,
  //   tooltip: { position: 'top' },
  // },
  // {
  //   title: "是否监控",
  //   width: 120,
  //   dataIndex: "isMonitor",
  //   slotName: "isMonitor",
  //   align: "center",
  // },
  {
    title: "是否修正",
    width: 120,
    dataIndex: "dataFix",
    slotName: "dataFix",
    align: "center",
  },
  {
    title: "是否累积",
    width: 120,
    dataIndex: "isAccumulation",
    slotName: "isAccumulation",
    align: "center",
  },
  {
    title: "监控设备",
    width: 180,
    dataIndex: "deviceName",
    align: "left",
  },
  {
    title: "所属通道",
    width: 180,
    dataIndex: "channelName",
    align: "center",
  },
  {
    title: "数据来源",
    width: 120,
    dataIndex: "origin",
    slotName: "origin",
    align: "center",
  },
  {
    title: "数据地址",
    width: 120,
    dataIndex: "messageAddress",
    align: "center",
  },
  {
    title: "数据类型",
    width: 180,
    dataIndex: "dataType",
    slotName: "dataType",
    align: "center",
  },
  {
    title: "系数",
    width: 100,
    dataIndex: "coefficient",
    align: "center",
  },
  {
    title: "存盘周期",
    width: 120,
    dataIndex: "saveCycle",
    slotName: "saveCycle",
    align: "center",
  },
  {
    title: "启用状态",
    width: 120,
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: "center",
    fixed: "right",
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 200,
    slotName: "operate",
    align: "center",
    fixed: "right",
  },
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

//路径
const baseUrl = import.meta.env.VITE_API_BASE_URL;
//请求头
const headers: Record<string, string> = {
  'Authorization': `Bearer ${getToken()}`
}

//上传结果显示状态
const uploadResultVisible = ref<boolean>(false);
const uploadResultType = ref<string>('success');
const uploadResult = ref<String>('');
/*************************** 变量区域 end ***************************/



/*************************** 方法区域 begin ***************************/

/**
 * 添加设备变量
 */
const handleDeviceVarAdd = () => {
  const resultRoute = (route.path).replace('/listVar', '/varCreate');
  router.push({
    path: resultRoute,
    query: {
      ...props,
      varType: tabsActiveKey.value,
      redirect: router.currentRoute.value.fullPath,
    }
  });
}

/** 导出按钮操作 */
const handleExport = () => {
  download("/system/device-var/export?stationType=" + props.stationType, {}, `${props.stationType == StationTypeEnum.power ? '用电' : props.stationType == StationTypeEnum.pv ? '光伏' : ""}设备-变量列表-${dayjs().format("YYYY-MM-DD HH:mm:ss")}.xlsx`);
}


const handleBeforeUpload = (file: File) => {
  Message.loading({
    content: "正在上传文件，请稍后",
    duration: 6000 * 1000
  });
  return true;
}

/**
 * 上传成功
 * @param fileItem
 */
const handleUploadSuccess = (fileItem: FileItem) => {
  Message.clear();
  const { code, msg } = fileItem.response;

  uploadResultType.value = code === 200 ? 'success' : 'error';

  if (code !== 200 && !msg) {
    Message.error("上传失败,请联系管理员");
    return;
  }

  uploadResultVisible.value = true;
  uploadResult.value = msg;
}


/**
 * 关闭上传成功提示谈框
 */
const closeUploadResult = async () => {
  uploadResult.value = '';
  uploadResultVisible.value = false;
  await fetchData();
}

/**
 * 修改设备变量
 */
const handleDeviceVarUpdate = (id: any) => {
  const resultRoute = (route.path).replace('/listVar', '/varUpdate');
  router.push({
    path: resultRoute,
    query: {
      ...props,
      bizId: id,
      redirect: router.currentRoute.value.fullPath
    }
  });
}

/**
 * 详情
 * @param val
 */
const handleDeviceDetail = async (record: any) => {
  formDrawer.value.visible = true;
  formDrawer.value.loading = true;
  try {
    formDrawer.value.title = record.varName;
    let res = await getDeviceVar(record.id);
    if (res.code == 200) {
      let channelDeviceRes = await getChannelDevice(res.data.comDeviceId);
      let comDeviceInfo: any = {}
      if (channelDeviceRes.code == 200) {
        comDeviceInfo = channelDeviceRes.data;
        ;
      }
      let varInfo: any = [
        {
          label: '所属站点:',
          value: res.data.stationName || '-',
        },
        {
          label: '电站类型:',
          value: getDictLabel('sys_station_type', res.data.stationType) || '-',
        },
        {
          label: '所属通道:',
          value: res.data.channelName || '-',
        },
        {
          label: '通讯设备:',
          value: comDeviceInfo.deviceName || '-',
        },
        {
          label: '用电设备:',
          value: res.data.deviceName || '-',
        },
        {
          label: '变量名称:',
          value: res.data.varName || '-',
        },
        {
          label: '变量编码:',
          value: res.data.varSn || '-',
        },
        {
          label: '变量属性:',
          value: res.data.varMapSn || '-',
        },
        {
          label: '变量单位:',
          value: res.data.unit || '-',
        },
        {
          label: '是否监控:',
          value: getDictLabel('sys_yes_no', res.data.isMonitor) || '-',
        },
        {
          label: '启用状态:',
          value: res.data.stopFlag == 0 ? '停用' : '启用',
        }
      ];

      let dataSource: any = [{
        label: '来源类型:',
        value: getDictLabel('sys_data_origin', res.data.origin) || '-',
      }, {
        label: '通讯设备:',
        value: comDeviceInfo.deviceName || '-',
      },
        // {
        //   label: '数据区域:',
        //   value: res.data.registerName || '-',
        // },
        {
          label: '数据地址:',
          value: res.data.messageAddress || '-',
        }, {
          label: '读写类型:',
          value: getDictLabel('sys_rw', res.data.rw) || '-',
        }, {
          label: '计算公式:',
          value: res.data.computeFormula || '-',
        }, {
          label: '缺失值处理:',
          value: getDictLabel('sys_deletion_type', res.data.deletionHandle) || '-',
        }];

      let dataHandle: any = [{
        label: '数据类型:',
        value: getDictLabel('sys_data_type', res.data.dataType) || '-',
      }, {
        label: '初始赋值:',
        value: res.data.initValue || '-',
      }, {
        label: '数据基值:',
        value: res.data.baseValue || '-',
      }, {
        label: '数据系数:',
        value: res.data.coefficient || '-',
      }, {
        label: '存盘周期:',
        value: getDictLabel('sys_save_cycle', res.data.saveCycle) || '-',
      }, {
        label: '是否累积量:',
        value: getDictLabel('sys_yes_no', res.data.isAccumulation) || '-',
      }, {
        label: '累积类型:',
        value:  res.data.isAccumulation==1 ? getDictLabel('sys_accumulate_type', res.data.accumulationType) : '-'
      }];

      let dataFilter: any = [{
        label: '数据过滤:',
        value: getDictLabel('sys_yes_no', res.data.isDataFilter) || '-',
      }, {
        label: '绝对值小于:',
        value: `${res.data.lessAbs?res.data.lessAbs:'-'}     替换为    ${res.data.replaceValueUpper?res.data.replaceValueUpper:'-'}`,
      }, {
        label: '绝对值大于:',
        value: `${res.data.moreAbs?res.data.moreAbs:'-'}     替换为    ${res.data.replaceValueLower?res.data.replaceValueLower:'-'}`,
      }]
      let systemInfo: any = [{
        label: '创建人员:',
        value: res.data.createBy || '-',
      }, {
        label: '创建时间:',
        value: res.data.createTime || '-',
      }, {
        label: '更新人员:',
        value: res.data.updateBy || '-',
      }, {
        label: '更新时间:',
        value: res.data.updateTime || '-',
      }];
      formDrawer.value.varInfo = varInfo;
      formDrawer.value.dataSource = dataSource;
      formDrawer.value.dataHandle = dataHandle;
      formDrawer.value.dataFilter = dataFilter;
      formDrawer.value.systemInfo = systemInfo;

    }
  } catch (err) {

  } finally {
    formDrawer.value.loading = false;
  }
}

/**
 * table切换
 */
const handleTabsChange = async () => {
  pagination.current = 1;
  await fetchData();
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
 * 详情关闭
 */
const handleDrawerCancel = () => {
  formDrawer.value = generateFormDrawerModel();
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
 * 监控
 * @param record
 */
const handleMonitor = (record: any) => {
  operateModalModel.value = {
    visible: true,
    type: 2,
    title: record.isMonitor == 0 ? '开启监控' : '关闭监控',
    id: record.id,
    value: record.isMonitor == 0 ? 1 : 0,
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
  let result: any = {code: 201};
  operateModalModel.value.visible = false;
  setLoading(true);
  if (operateModalModel.value.type == 0) {
    result = await changeDeviceVarStatus(operateModalModel.value.id, operateModalModel.value.value);
  } else if (operateModalModel.value.type == 1) {
    result = await delDeviceVar(operateModalModel.value.id);
  } else if (operateModalModel.value.type == 2) {
    result = await changeMonitorStatus(operateModalModel.value.id, operateModalModel.value.value);
  }
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}

/**
 * 获取设备列表
 */
const fetchDeviceData = async () => {
  try {
    const res = await listFusionGroup({
      stationType: props.stationType,
    });
    processSelectable(res.data);
    deviceData.value = res.data;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
}

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const data: any = await listDeviceVar({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchModel.value,
      ...props,
      varType: tabsActiveKey.value
    });
    renderData.value = data.rows;
    pagination.total = data.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

/*************************** 方法区域 end ***************************/

/**
 * 首次加载
 */
onMounted(async () => {
  let deviceId = router.currentRoute.value.query.deviceId
  if (deviceId) {
    searchModel.value.deviceId = Number(router.currentRoute.value.query.deviceId || 0);
  }
  await fetchDeviceData();
  await fetchData();
})
</script>
