<!--
 * 功能：折线图
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

    <!-- 额外操作栏，只有当configType为1时显示 -->
    <div class="time-search" v-if="configType === 1">
      <time-bar @change="timeBarChange" isCurrentDateDisabled/>
    </div>
    <!-- 加载动画，加载完成后显示图表 -->
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <template v-if="chartInfo.echartsOption?.series?.length>0">
        <div class="echart-wapper">
          <CutomChart ref="chartRef" v-if="!loading" :options="chartInfo.echartsOption"/>
        </div>
      </template>
      <a-empty v-else/>

    </a-spin>

  </div>
</template>


<script setup lang="ts">
import {onMounted, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {getChartInfo} from '@/api/dashboard/api';
import {useIntervalFn} from '@vueuse/core';
import {getTimeObject, handlePreConfigChart} from '@/utils/charts';

// 定义图表信息的接口
interface IChartInfo {
  cardName: string;
  echartsOption: any;
  singleInfo: null | any;
  singleRightList: Array<any>;
}

// 定义组件接收的属性
const props = defineProps({
  public: {
    type: Object,
    default: () => ({}),
  },
  configType: {
    type: Number,
    default: 1,
  },
  // 设备编号
  deviceSn: {
    type: String,
    default: ''
  },
  totalType: {
    type: Number,
    default: 2,
  },
  pageTemplate: {
    type: [String,Number],
    default: '1',
  },
  isDownload: {
    type: Boolean,
    default: false,
  }
});
// 状态管理
const {loading, setLoading} = useLoading(false);
const chartInfo = ref<IChartInfo>({
  cardName: '',
  echartsOption: {
    series: []
  },
  singleInfo: null,
  singleRightList: [],
});

const timeOptions = ref<any>({});

// 时间变化处理函数
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
};

// 数据获取函数
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
        ...res.data,
        echartsOption: handlePreConfigChart(res.data.echartsOption, '', res.data.chartType),
      };
    } else {
      chartInfo.value = {
        cardName: '',
        echartsOption: {},
        singleInfo: null,
      };
    }
  } catch (error) {
    console.error('获取图表数据时出错:', error);
  } finally {
    setLoading(false);
  }
};

const chartRef = ref<any>(null)

const download = () => {
  chartRef.value.downloadEchartsImg(chartInfo.value.cardName)
}
// 每5分钟轮询一次数据
useIntervalFn(fetchData, 300000);

onMounted(() => {
  if(props.configType !== 1){
    fetchData();
  }
});

defineExpose({
  handleTime: (val: any) => {
    timeBarChange(val);
  },
});
</script>

<style scoped lang="less">
.chart-container {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;

  .download-icon {
    position: absolute;
    color: #FFFFFF;
    cursor: pointer;
    z-index: 999999;
  }

  .time-search {
    width: 100%;
    display: flex;
    justify-content: flex-end;
    z-index: 11;
  }

  .chart-view {
    width: 100%;
    flex: 1;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-wrap: nowrap;
    padding: 0 10px;
    box-sizing: border-box;

    .echart-wapper {
      flex: 1;
      height: 100%;
    }

    .total-wapper {
      width: 30%;
      height: 100%;
    }
  }

  .arco-empty {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
  }
}
</style>

