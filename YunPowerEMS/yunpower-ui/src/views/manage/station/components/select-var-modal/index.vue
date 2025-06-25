<template>
  <a-modal width="800px" :visible="visible" :footer="false" @cancel="handleCancel" title-align="start"
           title="请选择变量">
    <a-form :model="searchForm" auto-label-width>
      <a-row :gutter="24">
        <!--能源类型-->
        <a-col :span="10">
          <a-form-item label="能源类型">
            <a-select v-model="searchForm.stationType" allow-clear
                      :placeholder="$t('manage.station.list.stationTypeSelect')" @change="handleStationType">

              <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                        :value="Number(dict.value)"></a-option>
            </a-select>
          </a-form-item>
        </a-col>
        <!--设备-->
        <a-col :span="10">
          <a-form-item :label="$t('manage.station.pageConfig.deviceSelect')">
            <a-tree-select v-model="searchForm.deviceId" :data="renderDeviceData"
                           :placeholder="$t('manage.station.pageConfig.devicePlaceholder')"
                           :fieldNames="{key: 'id', title: 'deviceName', children: 'children'}"
                           :allow-search="true" allow-clear
                           :filter-tree-node="filterTreeNode"></a-tree-select>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-button type="primary" @click="searchDeviceVar">
            <template #icon>
              <icon-search/>
            </template>
            {{ $t('global.search') }}
          </a-button>
        </a-col>
      </a-row>
    </a-form>
    <a-table :scroll="{ y: 400 }" row-key="varSn" :bordered="{ wrapper: true, cell: true }"
             :loading="loading"
             :columns="deviceVarTabColumns" :data="renderData" @page-change="onDeviceVarPageChange"
             :pagination="deviceVarPagination" @page-size-change="onDeviceVarPageSizeChange">
      <template #operate="{ record }">
        <a-button size="small" :disabled="record.status" type="primary" @click="handleDeviceVarSelect(record)">添加</a-button>
      </template>
    </a-table>
  </a-modal>
</template>

<script setup lang="ts">
import {getCurrentInstance, reactive, ref, watch, watchEffect} from "vue";
import {listFusionGroup, StationTypeEnum} from "@/api/system/device";
import {processSelectable} from '@/utils/ruoyi';
import {listDeviceVar} from "@/api/system/device-var";
import {BasePagination} from "@/api/common";
import useLoading from '@/hooks/loading';
import {storeToRefs} from "pinia";
import {useCompanyStore} from "@/store";

const companyStore = useCompanyStore();
const {companyValue} = storeToRefs(companyStore); // 选择的机构, 机构列表

const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {sys_station_type} = proxy?.useDict("sys_station_type");

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  // 变量类型 1模拟量 2状态量
  varType: {
    type: Number,
    default: 1,
  },
  // 是否累积量  1是 0否
  isAccumulation: {
    type: Number,
    default: 0,
  },
  // 站点
  deptId: {
    type: Number,
    default: -100,
  },
  // 能源类型 1电力 2光伏
  stationType: {
    type: Number,
    default: StationTypeEnum.power
  },
  // 桑基图单独处理
  filterVarList: {
    type: Array,
    default: []
  }
});
const emit = defineEmits(['cancel', 'add']);
//查询表单
const searchForm = reactive({
  stationType: props.stationType ? props.stationType : undefined,
  deviceId: '',
  isAccumulation: props.isAccumulation == 0 ? undefined : props.isAccumulation,
  deptId: props.deptId == -100 ? companyValue.value : props.deptId,
});
// 设备数据
const renderDeviceData = ref<any>([]);
//加载中
const {loading, setLoading} = useLoading(false);
//变量列
const deviceVarTabColumns = reactive<any[]>([
  {
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
// 分页
const deviceVarPagination: any = reactive({...BasePagination()});
// 表格数据
const renderData = ref<any>([]);

interface TreeElement {
  key: string;
  children?: TreeElement[];
}

interface ArrayElement {
  key: string;
  status?: boolean;
}

/**
 * 获取设备列表
 */
const fetchDeviceData = async () => {
  try {
    const params = {
      stationType: searchForm.stationType,
      deptId: props.deptId == -100 ? companyValue.value : props.deptId
    };
    const res = await listFusionGroup(params);
    processSelectable(res.data);
    renderDeviceData.value = res.data;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};
/**
 * 设备搜索
 * @param searchValue
 * @param nodeData
 */
const filterTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.deviceName.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
};
/**
 * 获取设备变量列表
 */
const fetchDeviceVarData = async () => {
  setLoading(true);
  try {
    const params = {
      ...searchForm,
      pageSize: deviceVarPagination.pageSize,
      pageNum: deviceVarPagination.current,
      varType: props.varType,
      // deptId: props.deptId == -100 ?  companyValue.value : props.deptId
    };
    const res: any = await listDeviceVar(params);
    if (res.code == 200) {
      checkStatusAgainstTree(res.rows, props.filterVarList);
      renderData.value = res.rows;
      deviceVarPagination.total = res.total;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

// 当前变量与整个数组
const checkStatusRecursively = (element: ArrayElement, tree: TreeElement[]): boolean => {
  for (const treeElement of tree) {
    if (treeElement.varSn === element.varSn) {
      element.status = true;
      return true;
    }
    if (treeElement.children) {
      if (checkStatusRecursively(element, treeElement.children)) {
        return true;
      }
    }
  }
  element.status = false;
  return false;
}

const checkStatusAgainstTree = (elements: ArrayElement[], tree: TreeElement[]): void => {
  elements.forEach(element => {
    checkStatusRecursively(element, tree);
  });
}

// 当前变量的父元素去重
const compareArrays = (arr1: any, arr2: any) =>  {
  // 创建一个Map来存储arr2中的所有id，便于快速查找
  const idMap = new Map();
  arr2.forEach(item => idMap.set(item.varSn, true));

  // 遍历arr1，根据id是否存在于idMap中来决定status的值
  return arr1.map(item => ({
    ...item,
    status: idMap.has(item.varSn) ? false : true,
  }));
}

/**
 * 表格页码发生变化-变量列表
 * @param val
 */
const onDeviceVarPageChange = async (val: number) => {
  deviceVarPagination.current = val;
  await fetchDeviceVarData();
};

const searchDeviceVar = async () => {
  deviceVarPagination.current = 1;
  await fetchDeviceVarData();
};

/**
 * 表格每页数量发生变化-变量列表
 * @param val 值
 */
const onDeviceVarPageSizeChange = async (val: number) => {
  deviceVarPagination.pageSize = val;
  await fetchDeviceVarData();
};
/**
 * 设备变量选中
 */
const handleDeviceVarSelect = (record: any) => {
  emit('add', record);
};
// 关闭弹窗
const handleCancel = () => {
  emit('cancel');
};

const handleStationType = (val: any) => {
  searchForm.deviceId = '';
  fetchDeviceData();
};

/**
 * @desc 监听id变化
 */
watch(
    () => props.visible,
    async (visible) => {
      if (visible) {
        searchForm.stationType = props.stationType ? props.stationType : undefined;
        searchForm.deptId = props.deptId == -100 ? companyValue.value : props.deptId;
        searchForm.deviceId = '';
        await fetchDeviceData();
        await fetchDeviceVarData();
      }
    },
)

</script>

<style lang="less" scoped>

</style>
