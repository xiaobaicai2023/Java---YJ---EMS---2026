<!--
 * 功能：极坐标
 * 作者：罗甜甜
 * 日期：2024-07-12
-->
<template>
  <a-card class="chart-container" :title="chartInfo.cardName">
    <!-- 额外操作栏，只有当configType为1时显示 -->
    <template #extra v-if="configType === 1">
      <time-bar @change="timeBarChange" isCurrentDateDisabled />
    </template>
    <!-- 加载动画，加载完成后显示图表 -->
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <chart :options="chartInfo.polarInfo" autoResize></chart>
    </a-spin>
  </a-card>
</template>
<script setup lang="ts">
import {onBeforeUnmount, ref} from 'vue';
import useLoading from '@/hooks/loading';
import { getChartInfo } from '@/api/dashboard/api';
import { useIntervalFn } from '@vueuse/core';
import { getTimeObject, handleConfigChart } from '@/utils/charts';

// 定义图表信息的接口
interface IChartInfo {
  cardName: string;
  echartsOption: any;
  singleInfo: null | any;
  polarInfo:null
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
  deviceSn:{
    type: String,
    default: ''
  }
});

// 状态管理
const { loading, setLoading } = useLoading(false);
const chartInfo = ref<IChartInfo>({
  cardName: '',
  echartsOption: {},
  singleInfo: null,
  polarInfo:null
});
const timeOptions = ref<any>({});

// 时间变化处理函数
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
};

// 数据获取函数
const fetchData = async () => {
  try {
    setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      deviceSn: props.deviceSn,
      ...getTimeObject(timeOptions.value),
    };

    const res: any = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = {
        ...res.data,
        polarInfo: chartData(res.data),
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

const chartData = (data:any)=>{
  return {
    polar: {
      radius: [30, '80%']
    },
    angleAxis: {
    },
    radiusAxis: {
      type: 'category',
      data: data.polarInfo.radiusAxisData
    },
    tooltip: {
      trigger: 'item',
    },
    series: {
      name: data.cardName,
      type: 'bar',
      data: data.polarInfo.seriesData,
      coordinateSystem: 'polar',
      label: {
        show: true,
        position: 'middle',
      }
    }
  };
}

// 每5分钟轮询一次数据
useIntervalFn(fetchData, 300000);

// 暴露timeBarChange方法
defineExpose({
  timeBarChange,
});
</script>

<style scoped lang="less">
.chart-container {
  height: 100%;
  width: 100%;

  :deep(.arco-card-body) {
    width: 100%;
    height: calc(100% - 46px);
  }

  .chart-view {
    width: 100%;
    height: 100%;
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

