<!--
* 报警事件
* 作者：曹晓桐
* 日期：2023-11-18
-->
<template>
  <div>
    <a-card class="content">
      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left"
                  auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="catalogId" :label="$t('power.energy.device.name')">
                  <a-tree-select v-model="searchModel.deviceId" :data="renderDeviceData" placeholder="全部设备"
                                 :fieldNames="{
                    key: 'id', title: 'deviceName', children: 'children'
                  }" :allow-search="true" :allow-clear="true" :filter-tree-node="filterTreeNode"
                                 @clear="handleDeviceClear"></a-tree-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="state" :label="$t('pv.alarm.list.eventStatus')">
                  <a-select v-model="searchModel.triggerStatus" @clear="handleTriggerStatusClear" placeholder="全部状态"
                            allow-clear>
                    <a-option key="-10" label="全部状态" value="-10"></a-option>
                    <a-option v-for="dict in sys_trigger_status" :key="dict.value" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="time" :label="$t('pv.alarm.list.occurTime')">
                  <a-range-picker v-model="searchModel.time"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="16">
              <a-col :span="8">
                <!--能源类型-->
                <a-form-item field="stationType" :label="$t('global.energy')">
                  <a-select v-model="searchModel.stationType" :placeholder="$t('global.pleaseSelect')"
                            allow-clear>
                    <a-option :key="0" :label="'全部类型'" :value="Number(-100)"/>
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"/>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="categoryId" :label="$t('pv.alarm.setting.alarmType')">
                  <a-select v-model="searchModel.categoryId" placeholder="全部类型" @clear="handleCategoryIdClear"
                            :options="renderAlarmCategoryData" :fieldNames="alarmCategoryfieldNames" allowSearch
                            allow-clear>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="triggerLevel" :label="$t('pv.alarm.list.alarmLevel')">
                  <a-select v-model="searchModel.triggerLevel" placeholder="全部等级" @clear="handleTriggerLevelClear"
                            allow-clear>
                    <a-option key="-1" label="全部等级" value="-10"></a-option>
                    <a-option v-for="dict in sys_alaram_level" :key="dict.value" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 84px" direction="vertical"/>
        <a-col :flex="'86px'" style="text-align: right">
          <a-space direction="vertical" :size="18">
            <a-button type="primary" @click="search">
              <template #icon>
                <icon-search/>
              </template>
              {{ $t('global.search') }}
            </a-button>
            <a-button @click="reset">
              <template #icon>
                <icon-refresh/>
              </template>
              {{ $t('global.reset') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

      <!-- 分割线 -->
      <a-divider style="margin-top: 0"/>

      <!-- 表格内容 -->
      <a-table row-key="id" ref="tableRef" :loading="loading" :pagination="pagination" :scroll="{ x: 1650 }"
               @page-change="onPageChange" @page-size-change="onPageSizeChange"
               :bordered="{ wrapper: true, cell: true }" :columns="columns" :data="renderData">
        <!-- 报警级别 -->
        <template #triggerLevel="{ record }">
          <dict-tag :options="sys_alaram_level" :value="record.triggerLevel"/>
        </template>
        <!-- 状态 -->
        <template #triggerStatus="{ record }">
          <dict-tag :options="sys_trigger_status" :value="record.triggerStatus"/>
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" v-if="record.triggerStatus!=10" status="warning"
                    @click="handleConfirm(record)">{{
              $t('global.confirm')
            }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleDetail(record)">{{
              $t('global.detail')
            }}
          </a-button>
          <a-button size="small" v-if="record.triggerStatus!=10 && record.triggerStatus!=-1" type="text" @click="handleCreateOrder(record)">
            <template #icon>
              <icon-plus/>
            </template>
            {{ $t('pv.alarm.list.createTicket') }}
          </a-button>
        </template>
      </a-table>

      <!-- 操作弹框 start -->
      <a-modal width="450px" title="事件确认" v-model:visible="operateModalModel.visible" @ok="handleOperateModelOk"
               @cancel="handleOperateModelCancle">
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form ref="formRef" auto-label-width :model="operateModalModel.formModel" label-align="right"
                    :rules="operateModalModel.rules">

              <!-- 事件名称 -->
              <a-form-item field="triggerName" label="事件名称">
                {{ operateModalModel.formModel.triggerName }}
              </a-form-item>

              <!-- 报警状态 -->
              <a-form-item field="triggerStatus" label="事件状态">
                <a-select v-model="operateModalModel.formModel.triggerStatus" placeholder="请选择事件状态" allow-clear>
                  <a-option v-for="dict in sys_trigger_status" :key="dict.value" :label="dict.label"
                            :value="Number(dict.value)"></a-option>
                </a-select>
              </a-form-item>

              <!-- 解决人员 -->
              <a-form-item field="confirmBy" label="解决人员" :validate-trigger="['change', 'input']">
                <a-select v-model="operateModalModel.formModel.confirmBy" allow-clear placeholder="请选择解决人员">
                  <a-option v-for="item in renderUserData" :key="item.id" :label="item.nickName"
                            :value="item.nickName"></a-option>
                </a-select>
              </a-form-item>

              <!-- 解决过程 -->
              <a-form-item field="confirmContent" label="解决过程">
                <a-textarea placeholder="请输入解决过程" v-model="operateModalModel.formModel.confirmContent"
                            allow-clear/>
              </a-form-item>

            </a-form>
          </a-col>
        </a-row>
      </a-modal>

      <!-- 添加右弹层 start  -->
      <a-drawer :width="600" title="报警详情" :visible="formDrawer.visible" hide-cancel @cancel="handleFormCancel"
                @ok="handleFormCancel" unmountOnClose :mask-closable="false">
        <a-descriptions :value-style="{ width: '600px' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList" :column="1" size="large">
          <template #value="{ value }">
            <a-skeleton v-if="formDrawer.loading" :animation="true">
              <a-skeleton-line :rows="1"/>
            </a-skeleton>
            <span v-else>{{ value }}</span>
          </template>
        </a-descriptions>
      </a-drawer>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, reactive, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {DescData, TableInstance} from "@arco-design/web-vue";
import {BasePagination} from '@/api/common';
import {FormInstance} from '@arco-design/web-vue/es/form';
import dayjs from 'dayjs';
import {confirmTrigger, getTrigger, listTrigger} from '@/api/system/trigger';
import {StationTypeEnum, listFusionGroup} from '@/api/system/device';
import {processSelectable} from '@/utils/ruoyi';
import {listTriggerCategoryAll} from '@/api/system/trigger-category';
import {listUserAll} from '@/api/manage/account/user';
import {getDictLabel} from '@/utils/dict';
import {notification} from "@/hooks/my-design";
import {useRoute, useRouter} from "vue-router";
import {useAppStore} from "@/store";

/*************************** 变量区域 begin ***************************/
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
  sys_alaram_level, sys_trigger_status, sys_station_type
} = proxy?.useDict(
    "sys_alaram_level", "sys_trigger_status", "sys_station_type"
);
//******* 这里编写动态获取下拉列表 end ******

// 获取当前时间
var dateNow = dayjs().format("YYYY-MM-DD");
var dateBefore = dayjs().subtract(30, 'day').format("YYYY-MM-DD");
const router = useRouter();
const route = useRoute();

//生成查询条件对象
const generateSearchModel:any = () => {
  return {
    id: 0,
    //设备名称
    deviceId: 0,
    //报警类型
    categoryId: 0,
    //事件状态
    triggerStatus: '-10',
    // 报警等级
    triggerLevel: '-10',
    // 选择日期
    time: [dateBefore, dateNow],
    // 能源类型
    stationType: props.stationType,
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const {loading, setLoading} = useLoading(true);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
//表格实例
const tableRef = ref<TableInstance>();
//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "事件名称",
    dataIndex: "triggerName",
    slotName: "triggerName",
    fixed: "left",
    width: 250,
  },
  {
    title: "所属站点",
    dataIndex: "stationName",
    slotName: "stationName",
    width: 250,
  },
  {
    title: "所属设备",
    dataIndex: "deviceName",
    width: 200,
  },
  {
    title: "报警类型",
    dataIndex: "categoryName",
    width: 200,
  },
  {
    title: "报警等级",
    dataIndex: "triggerLevel",
    slotName: "triggerLevel",
    align: 'left',
    width: 120,
  },
  {
    title: "发生时间",
    dataIndex: "happenTime",
    align: 'center',
    width: 180,
  },
  {
    title: "恢复时间",
    dataIndex: "recoverTime",
    align: 'center',
    width: 180,
  },
  {
    title: "状态",
    dataIndex: "triggerStatus",
    slotName: "triggerStatus",
    fixed: "right",
    align: 'center',
    width: 100,
  },
  {
    title: "操作",
    width: 250,
    dataIndex: "operations",
    slotName: 'operate',
    fixed: "right",
    align: 'center'
  }
]);

//表格数据
const renderData = ref<any[]>([]);
//设备数据
const renderDeviceData = ref<any>([{
  id: 0,
  deviceSn: "0",
  selectable: true,
  isGroup: 0,
  deviceName: "全部设备",
  children: []
}]);
//报警类型数据-下拉框
const renderAlarmCategoryData = ref<any>([{
  id: 0,
  triggerName: "全部类型",
  triggerSn: "all",
}]);
//报警类型数据-下拉框-自定义字段
const alarmCategoryfieldNames = {value: 'id', label: 'triggerName'}
//人员数据
const renderUserData = ref<any[]>([]);
//操作弹框
const generateOperateModalModel = () => {
  return {
    visible: false,
    rules: {
      confirmBy: [{required: true, message: "请选择解决人员"}],
      triggerStatus: [{required: true, message: "请选择事件状态"}],
      confirmContent: [{required: true, message: "请输入解决过程"}],
    },
    formModel: {
      id: 0,
      triggerName: '',
      confirmBy: '',
      triggerStatus: undefined,
      confirmContent: ""
    }
  };
};
//操作弹框模型
const operateModalModel = ref(generateOperateModalModel());
//表格示例
const formRef = ref<FormInstance>();
//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {},
    formModel: {
      id: 0,
      //事件名称
      triggerName: '',
      //所属站点
      stationName: '',
      // 所属设备
      deviceName: '',
      //变量名称
      varName: '',
      //变量代码
      varSn: '',
      //报警类型
      categoryName: '',
      //报警等级
      triggerLevel: '',
      //发生时间
      happenTime: '',
      //恢复时间
      recoverTime: '',
      triggerStatusName: '',
      //解决人员
      confirmBy: '',
      //解决过程
      confirmContent: '',
      //报警内容
      triggerContent: '',
      //报警状态
      triggerStatus: undefined,
    }
  };
};
//表单模型
const formDrawer = ref(generateFormDrawerModel());
const appStore = useAppStore();
const renderFatherMenu = computed(() => appStore.appAsyncFatherMenu);
//报警详情
const detailDataList = computed<DescData[]>(() => {
  const result: DescData[] = [
    {
      label: '事件名称',
      value: formDrawer.value.formModel.triggerName || '-',
    },
    {
      label: '所属站点',
      value: formDrawer.value.formModel.stationName || '-',
    },
    {
      label: '所属设备',
      value: formDrawer.value.formModel.deviceName || '-',
    },
    {
      label: '变量名称',
      value: formDrawer.value.formModel.varName || '-',
    },
    {
      label: '变量代码',
      value: formDrawer.value.formModel.varSn || '-',
    },
    {
      label: '报警类型',
      value: formDrawer.value.formModel.categoryName || '-',
    },
    {
      label: '报警等级',
      value: getDictLabel("sys_alaram_level", formDrawer.value.formModel.triggerLevel) || '-',
    },
    {
      label: '报警内容',
      value: formDrawer.value.formModel.triggerContent || '-',
    },
    {
      label: '发生时间',
      value: formDrawer.value.formModel.happenTime || '-',
    },
    {
      label: '恢复时间',
      value: formDrawer.value.formModel.recoverTime || '-',
    },
    {
      label: '报警状态',
      value: getDictLabel("sys_trigger_status", formDrawer.value.formModel.triggerStatus) || '-',
    },
    {
      label: '解决人员',
      value: formDrawer.value.formModel.confirmBy || '-',
    },
    {
      label: '解决过程',
      value: formDrawer.value.formModel.confirmContent || '-',
    }
  ]
  return result;
});
/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

//重置查询条件
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

//重置查询
const reset = async () => {
  searchModel.value = generateSearchModel();
  pagination.current = 1;
  await fetchData();
};

/**
 * 设备清空
 */
const handleDeviceClear = () => {
  searchModel.value.deviceId = 0;
}

/**
 * 报警类型清空
 */
const handleCategoryIdClear = () => {
  searchModel.value.categoryId = 0;
}
/**
 * 事件状态清空
 */
const handleTriggerStatusClear = () => {
  searchModel.value.triggerStatus = '-10';
}

/**
 * 报警等级清空
 */
const handleTriggerLevelClear = () => {
  searchModel.value.triggerLevel = '-10';
}


/**
 * 确认按钮
 */
const handleConfirm = async (val: any) => {
  await fetchUserData();
  operateModalModel.value.visible = true;
  operateModalModel.value.formModel.id = val.id;
  operateModalModel.value.formModel.triggerName = val.triggerName;
}

/**
 * 查看详情
 */
const handleDetail = async (val: any) => {
  formDrawer.value.visible = true;
  formDrawer.value.loading = true;
  formDrawer.value.formModel.id = val.id;
  try {
    let res = await getTrigger(formDrawer.value.formModel.id);
    if (res.code == 200) {
      formDrawer.value.formModel = res.data;
    }
  } catch (error) {
    console.error(error)
  } finally {
    formDrawer.value.loading = false;
  }
}

/**
 * 创建工单
 */
const handleCreateOrder = (val:any) => {
  const aaa: any = renderFatherMenu.value.find(item => item.props?.stationType == searchModel.value.stationType);
  router.push({
    path: `${aaa.path}/maintenance/list`,
    query: {
      deviceId: val.deviceId,
      id: val.id,
      stationType: val.stationType,
      deptId: val.deptId
      // redirect: router.currentRoute.value.fullPath,
    }
  });
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
 * 删除提示弹框取消
 */
const handleOperateModelCancle = () => {
  operateModalModel.value = generateOperateModalModel();
}

/**
 * 删除提示弹框确认
 */
const handleOperateModelOk = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    //确认接口
    let result = await confirmTrigger({
      ...operateModalModel.value.formModel,
      confirmTime: dayjs().format("YYYY-MM-DD HH:mm:ss")
    });
    notification(result)
    if (result.code == 200) {
      handleOperateModelCancle();
      await fetchData();
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
 * 查询用户数据
 */
const fetchUserData = async () => {
  try {
    const res: any = await listUserAll({});
    if (res.code == 200) {
      renderUserData.value = res.data;
    }
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
    let param = {
      stationType: props.stationType,
    }
    const res = await listFusionGroup(param);
    processSelectable(res.data);
    renderDeviceData.value[0].children = res.data;
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
      renderAlarmCategoryData.value = renderAlarmCategoryData.value.concat(res.data);
    }
  } catch (err) {
  } finally {
  }
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res: any = await listTrigger({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      //时间
      params: {
        beginTime: searchModel.value.time[0],
        endTime: searchModel.value.time[1]
      },
      //设备
      deviceId: searchModel.value.deviceId > 0 ? searchModel.value.deviceId : '',
      //报警类型
      categoryId: searchModel.value.categoryId > 0 ? searchModel.value.categoryId : '',
      //事件状态
      triggerStatus: searchModel.value.triggerStatus != '-10' ? searchModel.value.triggerStatus : '',
      // 报警等级
      triggerLevel: searchModel.value.triggerLevel != '-10' ? searchModel.value.triggerLevel : '',
      // 能源类型
      stationType: searchModel.value.stationType != '-100' ? searchModel.value.stationType : '',
    });
    if (res.code == 200) {
      renderData.value = res.rows;
      pagination.total = res.total;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

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


/*************************** 方法区域 end ***************************/
onMounted(async () => {
  await fetchData();
  await fetchDeviceData();
  await fetchAlarmCategoryData();
})
</script>

<style scoped></style>
