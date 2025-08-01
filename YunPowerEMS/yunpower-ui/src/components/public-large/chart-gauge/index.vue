<!--
 * 功能：仪表盘图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <div class="chart-container">
    <a-tooltip content="下载" position="bottom" v-if="isDownload">
      <img class="download-icon"
           :style="{ right: '50px', top: '4px' }"
           src="@/assets/bi/technology/icon-download.png" alt="" @click="download">
    </a-tooltip>
    <!-- 加载动画，加载完成后显示图表或空状态 -->
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <chart ref="chartRef" :options="chartInfo.dashboardInfo" v-if="chartInfo.dashboardInfo"></chart>
      <a-empty v-else/>
    </a-spin>
  </div>
</template>


<script setup lang="ts">
import {ref, inject, watch, onMounted, onBeforeUnmount, computed} from 'vue';
import useLoading from '@/hooks/loading';
import {getChartInfo} from '@/api/dashboard/api';
import {useIntervalFn} from '@vueuse/core';
import {SocketData} from '@/api/websocketService';
import {getTimeObject} from "@/utils/charts";

// 接收属性
const props = defineProps({
  public: {
    type: Object,
    default: () => ({}),
  },
  // 设备编号
  deviceSn: {
    type: String,
    default: ''
  },
  pageTemplate:{
    type: [String,Number],
    default: 1,
  },
  isDownload: {
    type: Boolean,
    default: false,
  }
});
const downLoadStyle = computed(()=>{
  return {
    right: '50px',
    top: '4px'
  }
})
const chartRef = ref<any>(null);

// 加载状态管理
const {loading, setLoading} = useLoading(false);

// 图表信息
const chartInfo = ref({
  cardName: '',
  dashboardInfo: {},
});

// WebSocket 数据
const {data, sendData}: SocketData = inject('webSocketData', {
  data: {
    value: 0,
  },
  sendData: () => {
  },
});

// 监听 WebSocket 数据变化
watch(data, async (newVal) => {
  if (newVal.key && newVal.key === chartInfo.value.dashboardInfo.varSn) {
    await fetchData();
  }
});

const timeOptions = ref<any>({});

// 时间变化处理函数
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
};

// 查询图表数据
const fetchData = async () => {
  if(!props.public.cardKey || !props.public.dashboardConfigId){
    return;
  }
  try {
    setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      deviceSn: props.deviceSn,
      ...getTimeObject(timeOptions.value),
    };

    const res = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = {
        cardName: res.data.cardName,
        // dashboardInfo: chartData(res.data.dashboardInfo),
      };

      if (res.data.dashboardRightList && res.data.dashboardRightList.length) {
        chartInfo.value.dashboardInfo = chartData(res.data.dashboardRightList);
      }
    } else {
      chartInfo.value = {
        cardName: '',
        dashboardInfo: {},
        dashboardRightList: []
      }
    }
  } catch (error) {
    console.error('获取图表数据时出错:', error);
  } finally {
    setLoading(false);
  }
};

// 生成图表配置
const chartData = (option: any) => {
  console.log(props.public, 1111111)
  const seriesList = option.map((item: any, index: number) => {
    return {
      type: 'gauge',
      min: item.minValue,
      max: item.maxValue,
      radius: props.public.preview ? '40%' : '90%',
      grid: {
        top: 0,
        left: 20,
        right: 5,
        bottom: 0,
      },
      center: [(index + 1) * 35 + '%', "60%"],
      progress: {
        show: true,
      },
      itemStyle: {
        borderWidth: 0
      },
      axisLabel: {
        color: props.public.preview ? '#000' : '#fff'
      },
      data: [
        {
          value: item.value,
          name: item.name,
          detail: {
            valueAnimation: true,
            formatter: `{value} ${item.unit}`,
            fontSize: 14,
            color: props.public.preview ? '#000' : '#fff'
          },
          title: {
            fontSize: 14,
            offsetCenter: [0, '60%'],
            color: props.public.preview ? '#000' : '#fff'
          },
        },
      ],
    }
  })

  return {
    tooltip: {
      formatter(params: any) {
        return `<div class="content-panel">
							<p>
								<span style="background-color: ${params.color}" class="tooltip-item-icon"></span>
								<span>${params.name}</span>
							</p>
							<span class="tooltip-value">
								${params.value + ' ' + option[params.seriesIndex].unit}
							</span>
					</div>`
      },
      appendToBody: true,
      className: 'echarts-tooltip-diy'
    },
    toolbox: {
      show: false,
    },
    series: seriesList,
  };
};

// 每5分钟轮询一次数据
const {resume: startInterval, pause: stopInterval} = useIntervalFn(fetchData, 1000 * 60 * 5);

const download = () => {
  chartRef.value.downloadEchartsImg(chartInfo.value.cardName)
}
// 组件挂载和卸载生命周期
onMounted(async () => {
  await fetchData();
  startInterval();
  sendData({
    action: 'subscribe',
    bizSn: chartInfo.value.dashboardInfo.varSn,
  });
});

onBeforeUnmount(() => {
  stopInterval();
  sendData({
    action: 'unsubscribe',
    bizSn: chartInfo.value.dashboardInfo?.varSn,
  });
});


onMounted(() => {
  fetchData();
})

defineExpose({
  handleTime: (val) => {
    timeBarChange(val);
  },
})
</script>

<style scoped lang="less">
.chart-container {
  height: 100%;

  .download-icon {
    position: absolute;
    color: #FFFFFF;
    cursor: pointer;
    z-index: 999999;
  }

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

