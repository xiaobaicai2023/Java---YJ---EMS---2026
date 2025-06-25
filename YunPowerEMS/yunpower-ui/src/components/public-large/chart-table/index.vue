<!--
 * 功能：表格
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <div class="table-list-view">
    <template v-if="public.chartType == 15">
      <transition-table v-show="tableInfo.tableData.length"
                        :data="tableInfo.tableData"
                        :preview="public.tablePre"
                        :columns="tableInfo.header"
                        :table-css="tableCss">
      </transition-table>
      <a-empty v-show="!tableInfo.tableData.length"/>
    </template>

    <template v-if="public.chartType == 16">
      <transition-table v-show="tableInfoOrder.tableData.length"
                        :data="tableInfoOrder.tableData"
                        :preview="public.tablePre"
                        :columns="tableInfoOrder.header"
                        :table-css="tableCss">
      </transition-table>
      <a-empty v-show="!tableInfoOrder.tableData.length"/>
    </template>
  </div>
</template>
<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router';
import {inject, onBeforeUnmount, onMounted, onUnmounted, reactive, ref, watch} from 'vue';
import useLoading from '@/hooks/loading';
import {getChartInfo} from '@/api/dashboard/api';
import {getTimeObject} from "@/utils/charts";
import TransitionTable from "@/components/transition-table/index.vue";
import {DeviceVarAlarmMessage, SocketData} from "@/api/websocketService";
import {useCompanyStore} from "@/store";
import {findById} from "@/utils/ruoyi";
import {storeToRefs} from "pinia";
import dayjs from "dayjs";
//加载中
const router = useRouter();
const route = useRoute();
const companyStore = useCompanyStore();
const {companyList} = companyStore; // 选择的机构, 机构列表
const props = defineProps({
  public: {
    type: Object,
    default: () => {
      return {}
    }
  },
  configType: {
    type: Number,
    default: 1,
  },
  // 设备编号, -------------------------------------------没有timeBar的组件一定要传
  deviceSn: {
    type: String,
    default: ''
  }
})
const {loading, setLoading} = useLoading(false);
const tableCss = {
  columns: {
    color: props.public.tablePre == 1 ? 'var(--color-text-1)' : '#FFFFFF',
    background: 'rgba(52, 144, 227, 0.05)'
  },
  data: {
    color: props.public.tablePre == 1 ? 'var(--color-text-1)' : '#FFFFFF',
    background: 'rgba(52, 144, 227, 0.1)'
  }
}
const tableInfo = ref<any>({
  cardName: '',
  header: [],
  tableData: []
})

const tableInfoOrder = ref<any>({
  cardName: '',
  header: [],
  tableData: []
})
const timeOptions = ref<any>({
  timer: dayjs().format("YYYY"),
  timeUnit: 2,
});
const selectCompany = ref<any>(route.query.stationId ? findById(companyList, Number(route.query.stationId)) : storeToRefs(companyStore).selectCompany.value);
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
}

const fetchData = async () => {
  if (!props.public.cardKey || !props.public.dashboardConfigId) {
    return;
  }
  if (props.public.chartType == 15) onSubscribe();
  try {
    setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      deviceSn: props.public.deviceSn,
      ...getTimeObject(timeOptions.value)
    };
    const res: any = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      if (props.public.chartType == 15) {
        tableInfo.value.cardName = res.data.cardName;
        tableInfo.value.header = res.data.tableInfo?.header;
        tableInfo.value.tableData = res.data.tableInfo ? res.data.tableInfo.tableData : [];
        subscribe();
      } else if (props.public.chartType == 16) {
        tableInfoOrder.value.cardName = res.data.cardName;
        tableInfoOrder.value.header = res.data.tableInfo?.header;
        tableInfoOrder.value.tableData = res.data.tableInfo ? res.data.tableInfo.tableData : [];
      }
    } else {
      if (props.public.chartType == 15) {
        tableInfo.value.cardName = "";
        tableInfo.value.header = [];
        tableInfo.value.tableData = [];
        subscribe();
      } else if (props.public.chartType == 16) {
        tableInfoOrder.value.cardName = "";
        tableInfoOrder.value.header = [];
        tableInfoOrder.value.tableData = [];
      }
    }
  } catch (error) {
    console.error('获取图表数据时出错:', error);
  } finally {
    setLoading(false);
  }
}

// WebSocket 数据
const {data, sendData}: SocketData = inject('webSocketData', {
  data: {value: 0},
  sendData: () => {
  }
});

// 订阅
const subscribe = () => {
  sendData({
    action: 'subscribe',
    bizSn: selectCompany.value.deptSn
  });
}

// 取消订阅
const onSubscribe = () => {
  sendData({
    action: 'unsubscribe',
    bizSn: selectCompany.value.deptSn
  });
}

watch(data, async (newVal: DeviceVarAlarmMessage) => {
  if (newVal.stationSn == selectCompany.value.deptSn && props.public.chartType == 15) {
    let item: any = {
      id: tableInfo.value.tableData.length + 1,
      '时间': newVal.happenTime,
      '事件': newVal.content,
      '站点': newVal.stationName,
      '设备': `${newVal.deviceName} ${newVal.deviceVarName}`,
      '类别': newVal.categoryName,
      '等级': newVal.triggerLevelName,
      '状态': newVal.triggerStatusName,
      '操作': '处理',
    }
    tableInfo.value.tableData.unshift(item);
  }
});


onMounted(() => {
  // console.log(props.public, '公共')
  if (route.query.stationId) {
    selectCompany.value = findById(companyList, Number(route.query.stationId));
  } else {
    selectCompany.value = storeToRefs(companyStore).selectCompany.value;
  }
  fetchData();
})

onUnmounted(() => {
  onSubscribe();
})

defineExpose({
  handleTime: (val: any) => {
    timeBarChange(val);
  },
})
</script>
<style lang="less" scoped>
.table-list-view {
  width: 100%;
  height: 100%;
  color: #FFFFFF;
  margin-top: 6px;
}

.arco-empty {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
</style>
