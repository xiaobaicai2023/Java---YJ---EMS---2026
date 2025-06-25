<!--
 * 功能：光伏设备
 * 作者：曹晓桐
 * 日期：2023-11-7
-->
<template>
  <div>
    <a-card class="content">
      <!-- 按钮 -->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleDeviceAdd">
              <template #icon>
                <icon-plus />
              </template>
              {{ $t('pv.device.list.newDevice') }}
            </a-button>
            <a-button type="primary" style="background-color:rgb(22, 93, 255)" @click="handleExport">
              <template #icon>
                <icon-download />
              </template>
              {{ $t('pv.device.list.export') }}
            </a-button>
            <a-upload :show-file-list="false" :action="(`${baseUrl}/system/device/importData`)"
              @success="handleUploadSuccess" :on-before-upload="handleBeforeUpload" :headers="headers"
              accept=".xlsx, .xls">
              <template #upload-button>
                <a-button type="primary" status="danger">
                  <template #icon>
                    <icon-upload />
                  </template>
                  {{ $t('pv.device.list.import') }}
                </a-button>
              </template>
            </a-upload>
          </a-space>
        </a-col>
      </a-row>
      <a-row style="margin-bottom: 16px">
        <a-col>
          <a-tabs v-model:active-key="tabsActiveKey" @change="handleTabsChange">
            <a-tab-pane v-for="(item, index) in tabsList" :key="item.value" :title="item.key">
            </a-tab-pane>
          </a-tabs>
          <!-- 表格内容  :span-method="dataSpanMethod"  :scroll="{ x: 1410 }" -->
          <a-table ref="tableRef" row-key="id" :loading="loading" v-bind:hide-expand-button-on-empty="true"
            :pagination="false" :scroll="{ x: 1560 }" :bordered="{ wrapper: true, cell: true }"
            :columns="(columns as TableColumnData[])" :data="renderData">
            <!-- 设备名称 -->
            <template #deviceName="{ record }">
              <a-tag style="color: rgb(var(--primary-6));" checkable>
                <template #icon>
                  <icon-storage v-if="record.isGroup == 1" style="color: rgb(var(--primary-6));" />
                  <icon-thunderbolt v-else style="color: rgb(var(--primary-6));" />
                </template>
                {{ record.deviceName }}</a-tag>
            </template>
            <!-- 状态 -->
            <template #isActive="{ record }">
              <div v-if="record.isGroup == 0">
                <span v-if="record.isActive === 0" class="circle-device"></span>
                <span v-else class="circle-device pass"></span>
              </div>
            </template>
            <!-- 组件容量 kWp -->
            <template #ratedVolume="{ record }">
              <div v-if="record.isGroup == 0">
                {{ record.ratedVolume }}kWp
              </div>
            </template>
            <!-- 发电功率 kW -->
            <template #electricPower="{ record }">
              <div v-if="record.isGroup == 0">
                {{ record.deviceDayRuntime.electricPower }}kW
              </div>
            </template>
            <!-- 当日发电量 kWh -->
            <template #electricQuantity="{ record }">
              <div v-if="record.isGroup == 0">
                {{ record.deviceDayRuntime.electricQuantity }}kWh
              </div>
            </template>
            <!-- 当日满发电小时 -->
            <template #electricHours="{ record }">
              <div v-if="record.isGroup == 0">
                {{ record.deviceDayRuntime.electricHours || '0' }}
              </div>
            </template>
            <!-- 逆变器性能 -->
            <template #inverterPerformanceName="{ record }">
              <div v-if="record.isGroup == 0">
                {{ record.deviceDayRuntime.inverterPerformanceName || '-' }}
              </div>
            </template>
            <!-- 直流离散率 -->
            <template #dcDispersionRate="{ record }">
              <div v-if="record.isGroup == 0">
                {{ record.deviceDayRuntime.dcDispersionRate || '0' }}%
              </div>
            </template>
            <!-- 功率归一化 -->
            <template #normalizedRate="{ record }">
              <div v-if="record.isGroup == 0">
                {{ record.deviceDayRuntime.normalizedRate || '0' }}%
              </div>
            </template>
            <!-- 数据更新时间 -->
            <template #staticTime="{ record }">
              <div v-if="record.isGroup == 0">
                {{ record.deviceDayRuntime.staticTime || '-' }}
              </div>
            </template>
            <template #operate="{ record }">
              <!-- <a-space> -->
              <a-button v-if="record.isGroup == 0" size="small" type="text" @click="handleStopFlag(record)"
                :status="record.stopFlag == 1 ? 'normal' : 'warning'">
                {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
              </a-button>
              <a-button v-if="record.isGroup == 0" size="small" type="text" status="success"
                @click="handleUpdate(record)">{{ $t('global.edit') }}</a-button>
<!--              <a-button v-if="record.isGroup == 0" size="small" type="text" status="danger"-->
<!--                @click="handleDetail(record.id)">{{ $t('global.detail') }}</a-button>-->
              <a-button v-if="record.isGroup == 0" size="small" type="text" status="danger" @click="handleDelete(record)">
                {{ $t('global.delete') }}
              </a-button>
              <a-button v-if="record.isGroup == 0" size="small" type="text"
                @click="handleToVariable(record.id)">{{ $t('pv.preview.var') }}</a-button>
              <!-- </a-space> -->
            </template>
          </a-table>

          <!-- 操作弹框 start -->
          <a-modal width="450px" v-model:visible="operateModalModel.visible" class="modal-box">
            <template #title>
              <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                style="color: rgb(var(--red-6)); margin-right: 5px" />
              <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px" />
              {{ $t('global.confirmTip') }}
            </template>
            <div style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{ operateModalModel.name }}】的数据项？
            </div>
            <template #footer>
              <div style="text-align: center">
                <a-space>
                  <a-button type="primary" status="danger" @click="handleOperateModelCancle">{{ $t('global.cancel') }}</a-button>
                  <a-button type="primary" @click="handleOperateModelOk">{{ $t('global.confirm') }}</a-button>
                </a-space>
              </div>
            </template>
          </a-modal>

          <a-modal width="450px" v-model:visible="uploadResultVisible" :hide-cancel="true">
            <template #title>
              <icon-check-circle-fill size="18" style="color: rgb(var(--primary-6)); margin-right: 5px" />
              上传成功
            </template>
            <div style="height: 200px;" v-html="uploadResult"></div>
            <template #footer>
              <div style="text-align: center">
                <a-space>
                  <a-button type="primary" @click="closeUploadResult">确认</a-button>
                </a-space>
              </div>
            </template>
          </a-modal>
          <!-- 操作弹框 end -->
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { useRouter } from 'vue-router'
import { computed, ref, onMounted, h, compile } from 'vue';
import {StationTypeEnum, changeDeviceStatus, getPVTab, listFusionGroup, delDevice} from '@/api/system/device';
import { TableColumnData, TableInstance } from '@arco-design/web-vue/es/table';
import { FileItem, Message } from "@arco-design/web-vue";
import useLoading from '@/hooks/loading';
import { notification } from "@/hooks/my-design";
import { download } from '@/api/download';
import dayjs from 'dayjs';
import { getToken } from '@/utils/auth';

/*************************** 变量区域 begin ***************************/
//路径
const baseUrl = import.meta.env.VITE_API_BASE_URL;
//请求头
const headers: Record<string, string> = {
  'Authorization': `Bearer ${getToken()}`
}
//路由
const router = useRouter();
const stationType = StationTypeEnum.pv;
//生成查询条件对象
const generateSearchModel = () => {
  return {
    // deviceName: '',
    groupName: "",
    stationType: stationType,
  };
};
//查询表单对象
const searchModel = ref(generateSearchModel());
//选中的tab
const tabsActiveKey = ref<any>(0);
//tab集合
const tabsList = ref<any>([])
//加载中
const { loading, setLoading } = useLoading(true);
//设置表格列
const columns = computed<TableColumnData[]>(() => [
  {
    title: "名称",
    dataIndex: "deviceName",
    slotName: "deviceName",
    align: "left",
    fixed: "left",
    width: 200,
  },
  {
    title: "状态",
    dataIndex: "isActive",
    slotName: "isActive",
    align: "center",
    width: 100,
  },
  {
    title: "组件容量(kWp)",
    dataIndex: "ratedVolume",
    slotName: "ratedVolume",
    align: "center",
    width: 150,
  },
  {
    title: "发电功率(kW)",
    dataIndex: "deviceDayRuntime.electricPower",
    slotName: "electricPower",
    align: "center",
    width: 150,
  },
  {
    title: "当日发电量(kWp)",
    dataIndex: "deviceDayRuntime.electricQuantity",
    slotName: "electricQuantity",
    align: "center",
    width: 150,
  },
  {
    title: "当日满发电小时",
    dataIndex: "deviceDayRuntime.electricHours",
    slotName: "electricHours",
    align: "center",
    width: 140,
  },
  {
    title: "逆变器性能",
    dataIndex: "deviceDayRuntime.inverterPerformanceName",
    slotName: "inverterPerformanceName",
    align: "center",
    width: 120,
  },
  {
    title: "直流离散率",
    dataIndex: "deviceDayRuntime.dcDispersionRate",
    slotName: "dcDispersionRate",
    align: "center",
    width: 120,
  },
  {
    title: "功率归一化",
    dataIndex: "deviceDayRuntime.normalizedRate",
    slotName: "normalizedRate",
    align: "center",
    width: 120,
  },
  {
    title: "数据更新时间",
    dataIndex: "deviceDayRuntime.staticTime",
    slotName: "staticTime",
    align: "center",
    width: 130,
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 220,
    slotName: "operate",
    align: "center",
    fixed: "right",
  },
]);


//展开/折叠控制值
const expandValue = ref<boolean>(true);
//表格实例
const tableRef = ref<TableInstance>();
//表格数据
const renderData = ref<any[]>([]);
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

//上传结果显示状态
const uploadResultVisible = ref<boolean>(false);
const uploadResult = ref<String>('');
/*************************** 变量区域 end ***************************/



/*************************** 方法区域 begin ***************************/

//添加设备
const handleDeviceAdd = () => {
  router.push({
    path: "/pv/device/create",
    query: {
      stationType: stationType,
      groupName: searchModel.value.groupName,
      redirect: router.currentRoute.value.fullPath
    }
  });
}

/** 导出按钮操作 */
const handleExport = () => {
  download("/system/device/export?stationType=" + StationTypeEnum.pv, {
  }, `光伏设备-${dayjs().format("YYYY-MM-DD HH:mm:ss")}.xlsx`);
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
  if (fileItem.response.code == 200) {
    uploadResultVisible.value = true;
    uploadResult.value = fileItem.response.msg;
  } else {
    Message.error(fileItem.response.msg || "上传失败,请联系管理员");
  }
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
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  router.push({
    path: "/pv/device/update",
    query: {
      stationType: StationTypeEnum.pv,
      bizId: record.id,
      redirect: router.currentRoute.value.fullPath
    }
  });
}

/**
 * 停用
 * @param record
 */
const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.deviceName;
  operateModalModel.value.value = record.stopFlag == 0 ? 1 : 0;
  operateModalModel.value.type = 0;
}

/**
 * 查看详情
 * @param val
 */
const handleDetail = (val: number) => {

}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = '删除';
  operateModalModel.value.name = record.deviceName;
  operateModalModel.value.type = 1;
};

/**
 * 跳转到变量页面
 */
const handleToVariable = (val: number) => {
  router.push({
    path: "/pv/device/listVar",
    query: {
      deviceId: val,
      redirect: router.currentRoute.value.fullPath
    }
  });
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
  let result: any;
  if(operateModalModel.value.type ==  0){
    result = await changeDeviceStatus(operateModalModel.value.id, operateModalModel.value.value);
  }else{
    result = await delDevice(operateModalModel.value.id);
  }
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}

/**
 * table切换
 */
const handleTabsChange = async () => {
  searchModel.value.groupName = tabsList.value.find((item: any) => item.value == tabsActiveKey.value).key;
  await fetchData();
  tableRef.value?.expandAll(true);
}

/**
 * 获取tab
 */
const fetchTabData = async () => {
  try {
    let res = await getPVTab();
    if (res.code == 200 && res.data && res.data.length > 0) {
      tabsList.value = res.data;
      tabsActiveKey.value = res.data[0].value;
      await handleTabsChange();
    }
  } catch (error) {

  }
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listFusionGroup({
      ...searchModel.value,
    });
    renderData.value = res.data;
    tableRef.value?.expandAll(true);
  } catch (err) {
    console.error("fetchData", err);
  } finally {
    setLoading(false);
  }
}
/**
 * 首次加载
 */
onMounted(async () => {
  await fetchTabData();
})
/*************************** 方法区域 end ***************************/
</script>
