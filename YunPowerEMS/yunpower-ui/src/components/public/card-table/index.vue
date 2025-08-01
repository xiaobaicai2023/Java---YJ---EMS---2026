<!--
 * 功能：表格
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <a-card :title="public.chartType == 15?tableInfo.cardName:tableInfoOrder.cardName">
    <!-- 额外操作栏，只有当configType为1时显示 -->
    <template #extra v-if="configType === 1">
      <time-bar @change="timeBarChange" :chart-type="public.chartType" isCurrentDateDisabled/>
    </template>
    <template v-if="public.chartType == 15">
      <transition-table v-show="tableInfo.tableData.length"
                        :data="tableInfo.tableData"
                        :columns="tableInfo.header">
      </transition-table>
      <a-empty v-show="!tableInfo.tableData.length"/>
    </template>

    <template v-if="public.chartType == 16">
      <transition-table v-show="tableInfoOrder.tableData?.length"
                        :data="tableInfoOrder.tableData"
                        :columns="tableInfoOrder.header">
      </transition-table>
      <a-empty v-show="!tableInfoOrder.tableData?.length"/>
    </template>
  </a-card>
</template>
<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router'
import {inject, nextTick, onBeforeUnmount, onMounted, reactive, ref, watch} from 'vue';
import TransitionTable from '@/components/transition-table/index.vue'
import useLoading from '@/hooks/loading';
import {getChartInfo} from '@/api/dashboard/api';
import {getTimeObject} from "@/utils/charts";
import {SocketData} from "@/api/websocketService";
import {findById} from "@/utils/ruoyi";
import {useCompanyStore} from "@/store";
import {storeToRefs} from "pinia";
//加载中
const router = useRouter();

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
const varSn = ref<any>('');
const tableInfo = reactive<any>({
  cardName: '',
  header: [],
  tableData: []
});
const tableInfoOrder = reactive<any>({
  cardName: '',
  header: [],
  tableData: []
});
const timeOptions = ref<any>({});
const route = useRoute();
const companyStore = useCompanyStore();
const {companyValue, companyList, selectCompany} = storeToRefs(companyStore); // 选择的机构, 机构列表
// WebSocket 数据
const {data, sendData}: SocketData = inject('webSocketData', {
  data: {value: 0},
  sendData: () => {
  }
});

const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
}

const fetchData = async () => {
  try {
    setLoading(true);
    if (props.public.chartType == 15) onSubscribe();
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      deviceSn: props.public.deviceSn,
      ...getTimeObject(timeOptions.value)
    };
    const res: any = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      if (props.public.chartType == 15) {
        tableInfo.cardName = res.data.cardName;
        tableInfo.header = res.data.tableInfo.header;
        tableInfo.tableData = res.data.tableInfo.tableData;
        subscribe();
      } else if (props.public.chartType == 16) {
        tableInfoOrder.cardName = res.data.cardName;
        tableInfoOrder.header = res.data.tableInfo.header;
        tableInfoOrder.tableData = res.data.tableInfo.tableData;
      }
    } else {
      if (props.public.chartType == 15) {
        tableInfo.cardName = '';
        tableInfo.header = [];
        tableInfo.tableData = [];
        subscribe();
      } else if (props.public.chartType == 16) {
        tableInfoOrder.cardName = '';
        tableInfoOrder.header = [];
        tableInfoOrder.tableData = [];
      }
    }
  } catch (error) {
    console.error('获取图表数据时出错:', error);
  } finally {
    setLoading(false);
  }
}

// 监听 WebSocket 数据
watch(data, async (newVal: any) => {
  if (newVal.stationSn == selectCompany.value?.deptSn && props.public.chartType == 15) {
    let item: any = {
      id: tableInfo.tableData.length + 1,
      '时间': newVal.happenTime,
      '事件': newVal.content,
      '站点': newVal.stationName,
      '设备': `${newVal.deviceName} ${newVal.deviceVarName}`,
      '类别': newVal.categoryName,
      '等级': newVal.triggerLevelName,
      '状态': newVal.triggerStatusName,
      '操作': '处理',
    }
    tableInfo.tableData.unshift(item);
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


onMounted(() => {
  nextTick(() => {
    fetchData();
  })
})

onBeforeUnmount(() => {
  onSubscribe();
})

defineExpose({
  timeBarChange
})
</script>
<style lang="less" scoped>
@border-color: var(--color-border-2);
.arco-card {
  width: 100%;
  height: 100%;
  padding-bottom: 12px;
  box-sizing: border-box;
  /* 调整卡片组件高度 */

  :deep(.arco-card-body) {
    height: calc(100% - 46px);
    padding: 0;
    overflow: hidden;
  }
}

.arco-empty {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
</style>
