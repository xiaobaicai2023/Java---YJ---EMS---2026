<!--
 * 功能：状态图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <!-- 加载动画，加载完成后显示状态图或空状态 -->
  <a-spin :loading="loading">
    <a-card :title="chartInfo.cardName" v-if="chartInfo.statusInfo.icon">
      <component :is="chartInfo.statusInfo.icon" :style="iconStyle"/>
    </a-card>
    <a-empty v-else/>
  </a-spin>
</template>

<script lang="ts" setup>
import {computed, inject, onBeforeUnmount, onMounted, ref, watch} from "vue";
import useLoading from "@/hooks/loading";
import {SocketData} from "@/api/websocketService";
import {useIntervalFn} from "@vueuse/core";
import {getChartInfo} from "@/api/dashboard/api";

// 加载状态
const {loading, setLoading} = useLoading(false);

// 定义组件属性
const props = defineProps({
  public: {
    type: Object,
    default: () => ({})
  },
  // 设备编号
  deviceSn: {
    type: String,
    default: ''
  }
});

// 图表信息
const chartInfo = ref({
  cardName: '',
  statusInfo: {
    icon: '',
    status: 1,
    varSn: ''
  }
});

// 图标样式
const iconStyle = computed(() => {
  return {
    fontSize: '50px',
    color: chartInfo.value.statusInfo.status ? '#52c41a' : '#f5222d'
  }
});

// webSocket 数据
const {data, sendData}: SocketData = inject('webSocketData', {
  data: {
    value: 0
  },
  sendData: () => {
  }
});

// 监听 webSocket 数据
watch(data, (newVal: any) => {
  if (newVal && newVal.key && newVal.key === chartInfo.value.statusInfo?.varSn) {
    chartInfo.value.statusInfo.status = newVal.value.status === 1 ? 0 : 1;
  }
});

// 5分钟轮询
const {resume: startInterval, pause: stopInterval} = useIntervalFn(async () => {
  await fetchData();
}, 1000 * 60 * 5);

// 获取数据
const fetchData = async () => {
  try {
    setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      deviceSn: props.deviceSn
    };
    const res: any = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = res.data;
    } else {
      chartInfo.value = {
        cardName: '',
        statusInfo: {
          icon: '',
          status: 0,
          varSn: ''
        }
      }
    }
  } catch (e) {
    console.error('获取数据失败:', e);
  } finally {
    setLoading(false);
  }
};

// 组件挂载时获取数据并订阅
onMounted(async () => {
  await fetchData();
  startInterval();
  if (chartInfo.value.statusInfo?.varSn) {
    sendData({
      action: 'subscribe',
      bizSn: chartInfo.value.statusInfo.varSn
    });
  }
});

// 组件卸载时取消订阅
onBeforeUnmount(() => {
  stopInterval();
  if (chartInfo.value.statusInfo?.varSn) {
    sendData({
      action: 'unsubscribe',
      bizSn: chartInfo.value.statusInfo.varSn
    });
  }
});

// 对外暴露的接口
defineExpose({
  fetchData
});
</script>

<style lang="less" scoped>
.arco-spin {
  width: 100%;
  height: 100%;
  border-radius: 4px;
  border: 1px solid var(--color-border-2);
  background: var(--color-bg-2);
}

.arco-card {
  height: 100%;

  :deep(.arco-card-body) {
    height: calc(100% - 46px);
    display: flex;
    align-items: center;
    justify-content: center;
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

