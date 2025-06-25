<!--
 * 功能：仪表盘图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <a-card class="chart-container" :title="chartInfo.cardName">
    <!-- 加载动画，加载完成后显示图表或空状态 -->
    <a-spin class="chart-view">
      <chart :options="chartInfo.dashboardInfo" v-if="chartInfo.dashboardInfo"></chart>
      <a-empty v-else/>
    </a-spin>
  </a-card>
</template>


<script setup lang="ts">
import { ref, inject, watch, onMounted, onBeforeUnmount } from 'vue';
import useLoading from '@/hooks/loading';
import { getChartInfo } from '@/api/dashboard/api';
import { useIntervalFn } from '@vueuse/core';
import { SocketData } from '@/api/websocketService';

// 接收属性
const props = defineProps({
  public: {
    type: Object,
    default: () => ({}),
  },
  // 设备编号
  deviceSn:{
    type: String,
    default: ''
  }
});

// 加载状态管理
const { loading, setLoading } = useLoading(false);

// 图表信息
const chartInfo = ref({
  cardName: '',
  dashboardInfo: {
    varSn: undefined
  }
});

// WebSocket 数据
const { data, sendData }: SocketData = inject('webSocketData', {
  data: {
    value: 0,
  },
  sendData: () => {},
});

// 监听 WebSocket 数据变化
watch(data, async (newVal: any) => {
  if (newVal.key && newVal.key === chartInfo.value.dashboardInfo?.varSn) {
    await fetchData();
  }
});

// 查询图表数据
const fetchData = async () => {
  try {
    // setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      deviceSn: props.deviceSn
    };
    const res: any = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = {
        cardName: res.data.cardName,
        dashboardInfo: chartData(res.data.dashboardInfo)
      };
    } else {
      chartInfo.value = {
        cardName: '',
        dashboardInfo: {
          varSn: undefined
        }
      }
    }
  } catch (error) {
    console.error('获取图表数据时出错:', error);
  } finally {
    // setLoading(false);
  }
};

// 生成图表配置
const chartData = (option: any) => {
  return {
    varSn: option.varSn,
    tooltip: {
      formatter(params: any) {
        return `<div class="content-panel">
							<p>
								<span style="background-color: ${params.color}" class="tooltip-item-icon"></span>
								<span>${params.name}</span>
							</p>
							<span class="tooltip-value">
								${params.value + ' ' + option.unit}
							</span>
					</div>`
      },
      appendToBody: true,
      className: 'echarts-tooltip-diy'
    },
    series: [
      {
        type: 'gauge',
        min: option.minValue,
        max: option.maxValue,
        radius: '100%',
        progress: {
          show: true,
        },
        data: [
          {
            value: option.value,
            name: option.name,
            detail: {
              valueAnimation: true,
              formatter: `{value} ${option.unit}`,
              fontSize: 24,
            },
            title: {
              fontSize: 14,
              offsetCenter: [0, '60%'],
            },
          },
        ],
      },
    ],
  };
};

// 每5分钟轮询一次数据
const { resume: startInterval, pause: stopInterval } = useIntervalFn(fetchData, 1000 * 60 * 5);

// 组件挂载和卸载生命周期
onMounted(async () => {
  await fetchData();
  startInterval();
  sendData({
    action: 'subscribe',
    bizSn: chartInfo.value.dashboardInfo?.varSn,
  });
});

onBeforeUnmount(() => {
  stopInterval();
  sendData({
    action: 'unsubscribe',
    bizSn: chartInfo.value.dashboardInfo?.varSn,
  });
});

// 暴露 fetchData 方法
defineExpose({
  fetchData,
});
</script>

<style scoped lang="less">
.chart-container {
  height: 100%;

  :deep(.arco-card-body) {
    width: 100%;
    height: calc(100% - 46px);
  }

  .chart-view {
    width: 100%;
    height: 100%;

    .arco-empty {
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
    }
  }
}
</style>

