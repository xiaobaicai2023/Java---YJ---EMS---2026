<!--
 * 功能：水滴图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <div class="chart-container" ref="spinRef">
    <!-- 加载动画，加载完成后显示水滴图或空状态 -->
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <div :class="'chart-water-ref'+randomNumber" :style="[{'height': chartHeight+'px'},{'width':chartWidth+'px'}]"
           v-show="chartInfo.dashboardInfo.varSn"></div>
      <a-empty v-show="!chartInfo.dashboardInfo.varSn"/>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import useLoading from '@/hooks/loading';
import {useIntervalFn, useWindowSize} from '@vueuse/core';
import {inject, onBeforeUnmount, onMounted, ref, watch, watchEffect} from "vue";
import * as echarts from "echarts";
import 'echarts-liquidfill';
import {getChartInfo} from "@/api/dashboard/api";
import {SocketData} from "@/api/websocketService";
import {getTimeObject} from "@/utils/charts";

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
console.log(props.public, 'shui')
// 加载状态
const {loading, setLoading} = useLoading(false);
const randomNumber = ref<number>(getRandomInt());

// 窗口大小
const {width} = useWindowSize();

const chartWidth = ref<number>();
const chartHeight = ref<number>();

const spinRef = ref<HTMLDivElement | null>(null);
// echarts实例
const chartInstance = ref<any>(null);

// 图表详情
const chartInfo = ref<any>({
  cardName: '',
  dashboardInfo: {},
  dashboardRightList: []
});

// socket 数据
const {data, sendData}: SocketData = inject('webSocketData', {
  data: {
    value: 0
  },
  sendData: () => {
  }
});

const timeOptions = ref<any>({});
/**
 * 时间改变
 */
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
}

// 监听webSocket数据
watch(data, async (newVal, oldVal) => {
  if (newVal.key && newVal.key === chartInfo.value.dashboardInfo.varSn) {
    await fetchData();
  }
});

// 初始化并获取数据
const fetchData = async () => {
  if(!props.public.cardKey || !props.public.dashboardConfigId){
    return;
  }
  try {
    setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      ...getTimeObject(timeOptions.value),
    };
    const res = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = {
        cardName: res.data.cardName,
        dashboardInfo: res.data.dashboardInfo,
        dashboardRightList: res.data.dashboardRightList,
        deviceSn: props.deviceSn
      };
      chartData(res.data.dashboardRightList);
    } else {
      chartInfo.value = {
        cardName: '',
        dashboardInfo: {},
        dashboardRightList: []
      }
    }
  } catch (e) {
    console.error('获取图表数据时出错:', e);
  } finally {
    setLoading(false);
  }
};

// 设置水滴图数据
const chartData = (data: any) => {
  // 获取对应的dom元素
  const el = document.querySelector('.chart-water-ref' + randomNumber.value) as HTMLDivElement;
  // 初始化
  if (chartInstance.value) {
    chartInstance.value.dispose();
  }
  chartInstance.value = echarts.init(el);

  const option: any = {
    series: []
  };

  option.series = data.map((item: any, index: number) => {
    const centerResult = 0;

    return {
      type: 'liquidFill', // 水位图
      radius: '110px', // 显示比例
      center: [(index + 1) * 25 + '%', '45%'], // 中心点
      amplitude: 10, // 水波振幅
      data: [item.value], // data个数代表波浪数
      color: [
        {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            {
              offset: 0,
              color: '#446bf5',
            },
            {
              offset: 1,
              color: '#2ca3e2',
            },
          ],
          globalCoord: false,
        },
      ], // 波浪颜色
      backgroundStyle: {
        borderWidth: 1, // 外边框
        color: 'RGBA(51, 66, 127, 0.7)', // 边框内部填充部分颜色
      },
      label: {
        color: '#fff',
        insideColor: 'transparent',
        fontSize: 26,
        fontWeight: 'bold'
      },
      outline: {
        show: true,
        borderDistance: 5,
        itemStyle: {
          borderColor: 'rgba(67,209,100,1)',
          borderWidth: 0
        }
      },
    }
  });

  chartInstance.value.setOption(option);
};

// 监听窗口大小变化
watch(width, () => {
  if (chartInstance.value) {
    chartInstance.value.resize();
  }
});

watchEffect(() => {
  // 确保 DOM 渲染完成后再获取宽高
  if (spinRef.value) {
    const {width, height} = spinRef.value.getBoundingClientRect();
    chartWidth.value = props.public.waterPre ? width * 2 : width;
    chartHeight.value = props.public.waterPre ? height * 2 : height;
  } else {
    console.warn('Div元素未找到');
  }
})

// 5分钟轮询
const {resume: startInterval, pause: stopInterval} = useIntervalFn(async () => {
  await fetchData();
}, 1000 * 60 * 5);

function getRandomInt() {
  let min = Math.ceil(0);
  let max = Math.floor(100);
  return Math.floor(Math.random() * (100 - 0 + 1)) + min;
}

// 组件挂载时获取数据并订阅
onMounted(async () => {
  await fetchData();
  startInterval();
  sendData({
    action: 'subscribe',
    bizSn: chartInfo.value.dashboardInfo.varSn
  });

});

// 组件卸载时取消订阅
onBeforeUnmount(() => {
  stopInterval();
  sendData({
    action: 'unsubscribe',
    bizSn: chartInfo.value.dashboardInfo.varSn
  });
});

// 对外暴露的接口
defineExpose({
  fetchData,
  handleTime: (val:any) => {
    timeBarChange(val);
  },
});
</script>

<style scoped lang="less">
.chart-container {
  width: 100%;
  height: 100%;

  :deep(.arco-card-body) {
    width: 100%;
    height: calc(100% - 46px);
  }

  .chart-view {
    width: 100%;
    height: 100%;

    .chart-water-ref {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
    }

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

