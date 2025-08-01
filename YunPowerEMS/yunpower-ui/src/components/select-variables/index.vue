<template>
  <!-- 选择变量弹框 -->
  <a-modal width="800px" :visible="visible" :footer="false" title-align="start" title="请选择变量"
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
</template>
<script lang="ts" setup>
import {computed, ref, onMounted} from "vue";
import {listFusionGroup} from "@/api/system/device";
import {processSelectable} from "@/utils/ruoyi";
import { listDeviceVar } from '@/api/system/device-var';

const props = defineProps({
  formDrawerConfig: {
    type: Object,
    default: {}
  },
  visible: {
    type: Boolean,
    default: false
  }
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
//设备数据
const renderDeviceData = ref<any>([]);


//选择变量弹框模型
const generateModalModel = () => {
  return {
    visible: false,
    parentId: '',
    //表格数据
    renderData: []
  }
}

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

//弹框模型
const modalModel = ref(generateModalModel());

const handleModalClose = () => {
  modalModel.value = generateModalModel();
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
 * 获取设备列表
 */
const fetchDeviceData = async () => {
  try {
    const res = await listFusionGroup({ stationType: props.formDrawerConfig.formModel.stationType });
    processSelectable(res.data);
    renderDeviceData.value = res.data;
  } catch (err) {
    console.log('qq')
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


//重置查询条件
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

const searchDeviceVar = async () => {
  deviceVarPagination.current = 1;
  await fetchDeviceVarData();
}

onMounted(async()=>{
  await fetchDeviceData();
})

</script>
<style lang="less" scoped></style>