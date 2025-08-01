<!--
 * 功能：设备列表
 * 作者：曹晓桐
 * 日期：2023-11-29
-->

<template>
  <div>
    <a-card class="content">
      <!-- 表单搜索 -->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left" auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="deviceName" label="设备名称">
                  <a-input v-model="searchModel.deviceName" placeholder="设备名称，支持模糊查询" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="groupId" label="设备分组">
                  <a-tree-select
                      allow-clear
                      allow-search
                      v-model="searchModel.groupId"
                      :fieldNames="{
                      key: 'id',
                      title: 'groupName',
                      children: 'children',
                    }"
                      :data="catalogData"
                      placeholder="请选择设备分组"
                  ></a-tree-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-space direction="vertical" :size="18">
                  <a-button type="primary" @click="search">
                    <template #icon>
                      <icon-search/>
                    </template>
                    搜索
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
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('power.energy.device.addDevice') }}
            </a-button>
            <a-button @click="handleExpand"> {{ $t('global.expand') }}/{{ $t('global.fold') }}</a-button>

            <a-button type="primary" style="background-color: rgb(var(--arcoblue-4))" @click="handleExport">
              <template #icon>
                <icon-download/>
              </template>
              {{ $t('power.energy.device.export') }}
            </a-button>
            <a-upload
                :show-file-list="false"
                :action="`${baseUrl}/system/device/importData`"
                @success="handleUploadSuccess"
                :on-before-upload="handleBeforeUpload"
                :headers="headers"
                accept=".xlsx, .xls"
            >
              <template #upload-button>
                <a-button type="primary" style="background-color: rgb(var(--red-5))">
                  <template #icon>
                    <icon-upload/>
                  </template>
                  {{ $t('power.energy.device.import') }}
                </a-button>
              </template>
            </a-upload>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table
          ref="tableRef"
          row-key="id"
          :loading="loading"
          v-bind:hide-expand-button-on-empty="true"
          :pagination="false"
          :bordered="{ wrapper: true, cell: true }"
          :columns="columns"
          :data="renderData"
          :scroll="{ x: 1370 }"
      >
        <!-- 设备名称 -->
        <template #deviceName="{ record }">
          <a-tag style="color: rgb(var(--primary-6))" checkable>
            <template #icon>
              <icon-storage v-if="record.isGroup == 1" style="color: rgb(var(--primary-6))"/>
              <icon-thunderbolt v-else style="color: rgb(var(--primary-6))"/>
            </template>
            {{ record.deviceName }}
          </a-tag
          >
        </template>
        <!-- 设备编码 -->
        <template #deviceSn="{ record }">
          {{ record.deviceSn }}
        </template>

        <!-- 所属通道 -->
        <template #channelName="{ record }">
          {{ record.channelName }}
        </template>

        <!-- 设备类型 -->
        <template #proTypeName="{ record }">
          {{ record.proTypeName }}
        </template>

        <!-- 电能类型 -->
        <template #electricType="{ record }">
          <dict-tag :options="sys_electric_type" :value="record.electricType"/>
        </template>

        <!-- 额定电压 V -->
        <template #ratedVol="{ record }">
          <dict-tag v-if="record.isGroup == 0" :options="sys_rated_vol" :value="Number(record.ratedVol)"/>
        </template>
        <!-- 额定电流 A -->

        <template #ratedEle="{ record }">
          <span v-if="record.isGroup == 0">{{ record.ratedEle }}A</span>
        </template>
        <!-- 额定功率 KW -->

        <template #ratedPower="{ record }">
          <span v-if="record.isGroup == 0">{{ record.ratedPower }}KW</span>
        </template>
        <!-- 状态 -->

        <template #isActive="{ record }">
          <!--          <stop-flag v-if="record.isGroup == 0" :value="record.stopFlag"></stop-flag>-->
          <div v-if="record.isGroup == 0">
            <span v-if="record.isActive === 0" class="circle-device"></span>
            <span v-else class="circle-device pass"></span>
          </div>
        </template>

        <template #operate="{ record }">
          <!-- <a-space> -->
          <a-button v-if="record.isGroup == 0" size="small" type="text" @click="handleStopFlag(record)"
                    :status="record.status == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button v-if="record.isGroup == 0" size="small" type="text" status="success" @click="handleUpdate(record)">
            {{ $t('global.edit') }}
          </a-button>
          <a-button v-if="record.isGroup == 0" size="small" type="text" status="danger" @click="handleDelete(record)">
            {{ $t('global.delete') }}
          </a-button>
          <a-button v-if="record.isGroup == 0" size="small" type="text" @click="handleToVariable(record.id)">
            {{ $t('power.energy.device.variable') }}
          </a-button>
          <!-- </a-space> -->
        </template>
      </a-table>

      <!-- 操作弹框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible" class="modal-box">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                             style="color: rgb(var(--red-6)); margin-right: 5px"/>
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
          确认提示
        </template>
        <div style="text-align: center">是否确认{{ operateModalModel.title }}名称为【{{
            operateModalModel.name
          }}】的数据项？
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
      <a-modal width="450px" v-model:visible="uploadResultVisible" :hide-cancel="true">
        <template #title>
          <icon-check-circle-fill size="18" style="color: rgb(var(--primary-6)); margin-right: 5px" v-if="uploadResultType === 'success'" />
          <icon-close-circle-fill size="18" style="color: rgb(var(--red-5)); margin-right: 5px" v-else />
          {{uploadResultType === 'success' ? '上传成功' : '上传失败'}}
        </template>
        <div style="max-height: 200px" v-html="uploadResult"></div>
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
import {computed, getCurrentInstance, onMounted, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {FileItem, Message, TableInstance} from '@arco-design/web-vue';
import {listGroupAll} from '@/api/system/group';
import {useRouter, useRoute} from 'vue-router';
import {StationTypeEnum, changeDeviceStatus, delDevice, listFusionGroup} from '@/api/system/device';
import {download} from '@/api/download';
import {getToken} from '@/utils/auth';
import dayjs from 'dayjs';
import {notification} from '@/hooks/my-design';

/*************************** 变量区域 begin ***************************/

    //******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {sys_electric_type, sys_rated_vol} = proxy?.useDict('sys_electric_type', "sys_rated_vol");

//路径
const baseUrl = import.meta.env.VITE_API_BASE_URL;
//请求头
const headers: Record<string, string> = {
  Authorization: `Bearer ${getToken()}`,
};

//组件参数
const props = defineProps({
  stationType: {
    type: Number,
    default: StationTypeEnum.power,
  },
});
const router = useRouter();
const route = useRoute();
//生成查询条件对象
const generateSearchModel = () => {
  return {
    deviceName: '',
    groupId: '',
    stationType: props.stationType,
  };
};
//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const {loading, setLoading} = useLoading(true);
//设置表格列
const columns = computed<any[]>(() => [
  {
    title: '设备名称',
    dataIndex: 'deviceName',
    slotName: 'deviceName',
    align: 'left',
    fixed: 'left',
    width: 250,
  },
  {
    title: '设备编码',
    dataIndex: 'deviceSn',
    slotName: 'deviceSn',
    align: 'left',
    fixed: 'left',
    width: 200,
  },
  {
    title: '所属通道',
    dataIndex: 'channelName',
    slotName: 'channelName',
    align: 'left',
    width: 150,
  },
  {
    title: '设备类型',
    dataIndex: 'proTypeName',
    slotName: 'proTypeName',
    align: 'left',
    width: 150,
  },
  {
    title: '用能类型',
    dataIndex: 'electricType',
    slotName: 'electricType',
    align: 'center',
    width: 100,
  },
  {
    title: '额定电压',
    dataIndex: 'ratedVol',
    slotName: 'ratedVol',
    align: 'center',
    width: 100,
  },
  {
    title: '额定电流',
    dataIndex: 'ratedEle',
    slotName: 'ratedEle',
    align: 'center',
    width: 100,
  },
  {
    title: '额定功率',
    dataIndex: 'ratedPower',
    slotName: 'ratedPower',
    align: 'center',
    width: 100,
  },
  {
    title: '设备状态',
    dataIndex: 'isActive',
    slotName: 'isActive',
    align: 'center',
    fixed: 'right',
    width: 100,
  },
  {
    title: '操作',
    dataIndex: 'operations',
    width: 220,
    slotName: 'operate',
    align: 'center',
    fixed: 'right',
  },
]);

//展开/折叠控制值
const expandValue = ref<boolean>(true);
//表格实例
const tableRef = ref<TableInstance>();
//表格数据
const renderData = ref<any[]>([]);
const catalogData = ref<any[]>([]);
//操作弹框
const generateOperateModalModel = () => {
  return {
    //0 状态 1删除
    type: 0,
    visible: false,
    title: '',
    id: 0,
    value: 0,
    name: '',
  };
};

//操作弹框模型
const operateModalModel = ref(generateOperateModalModel());

//上传结果显示状态
const uploadResultVisible = ref<boolean>(false);
const uploadResultType = ref<string>('success');
const uploadResult = ref<String>('');
/*************************** 变量区域 end ***************************/

/*************************** 方法区域 begin ***************************/

const handleBeforeUpload = (file: File) => {
  Message.loading({
    content: '正在上传文件，请稍后',
    duration: 6000 * 1000,
  });
  return true;
};

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
};

/**
 * 展开/折叠
 */
const handleExpand = () => {
  expandValue.value = !expandValue.value;
  tableRef.value?.expandAll(expandValue.value);
};

//重置查询条件
const search = async () => {
  await fetchData();
};

/**
 * 跳转到变量页面
 */
const handleToVariable = (val: number) => {
  const resultRoute = (route.path).replace('/list', '/listVar');
  router.push({
    path: resultRoute,
    query: {
      deviceId: val,
      redirect: router.currentRoute.value.fullPath,
    },
  });
};

/**
 * 新增数据
 * @param row 数据行
 */
const handleAdd = () => {
  const resultRoute = (route.path).replace('/list', '/create');
  router.push({
    path: resultRoute,
    query: {
      stationType: props.stationType,
      redirect: router.currentRoute.value.fullPath,
    },
  });
};

/** 导出按钮操作 */
const handleExport = () => {
  download('/system/device/export?stationType=' + props.stationType, {}, `用电设备-${dayjs().format('YYYY-MM-DD HH:mm:ss')}.xlsx`);
};
/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  const resultRoute = (route.path).replace('/list', '/update');
  router.push({
    path: resultRoute,
    query: {
      stationType: props.stationType,
      bizId: record.id,
      redirect: router.currentRoute.value.fullPath,
    },
  });
};

/**
 * 停用
 * @param record
 */
const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? '停用' : '启用';
  operateModalModel.value.name = record.deviceName;
  operateModalModel.value.value = record.stopFlag == 0 ? 1 : 0;
  operateModalModel.value.type = 0;
};

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
 * 删除提示弹框取消
 */
const handleOperateModelCancle = () => {
  operateModalModel.value = generateOperateModalModel();
};

/**
 * 删除提示弹框确认
 */
const handleOperateModelOk = async () => {
  let result;
  operateModalModel.value.visible = false;
  try {
    setLoading(true);
    if (operateModalModel.value.type == 0) {
      result = await changeDeviceStatus(operateModalModel.value.id, operateModalModel.value.value);
    } else {
      result = await delDevice(operateModalModel.value.id);
    }
    notification(result);
    if (result.code == 200) {
      handleOperateModelCancle();
      await fetchData();
    }
  } catch (e) {
    console.error(e);
  } finally {
    setLoading(false);
  }
};

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
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

/**
 * 查询设备分组
 */
const fetchCatalogData = async () => {
  try {
    const res = await listGroupAll({mapId: 2});
    catalogData.value = res.data;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
  await fetchCatalogData();
  tableRef.value?.expandAll(true);
});
</script>

<style scoped></style>
