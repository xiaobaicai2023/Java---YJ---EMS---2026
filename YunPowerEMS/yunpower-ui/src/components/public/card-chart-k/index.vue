<!--
 * 功能：k图
 * 作者：闫李壮
 * 日期：2024-06-06
-->
<template>
  <a-card class="chart-container" :title="public.cardName">
    <template #extra v-if="configType === 1">
      <time-bar @change="timeBarChange" isCurrentDateDisabled />
    </template>
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <CutomChart v-if="!loading" :options="chartOptions"></CutomChart>
    </a-spin>
    <slot/>
  </a-card>
</template>

<script setup lang="ts">
import useLoading from '@/hooks/loading';
import {ref} from "vue";

const props = defineProps({
  public: {
    type: Object,
    default: () => {
      return {}
    }
  },
  configType: {
    type: Number,
    default: 1
  }
});
//加载中
const {loading, setLoading} = useLoading(false);
const chartOptions = ref({});
const timeOptions = ref<any>({});
/**
 * 时间改变
 */
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
}
const fetchData = async () => {
  try {
    setLoading(true);
    let res = chartData(props.public.chartType)
    // 处理图标数据
    chartOptions.value = res;
  } catch (e) {
    console.log(e)
  } finally {
    setLoading(false);
  }
};
/**
 * 条形
 * @param type
 */
const chartData = (type: string) => {
  const res = {
    xAxis: [
      {
        data: ['2017-10-24', '2017-10-25', '2017-10-26', '2017-10-27']
      },
    ],
    grid: {
      top: "12%",
      left: 20,
      bottom: 0,
      right: 20,
      containLabel: true,
    },
    tooltip: {
      show: true
    },
    yAxis: {},
    series: [
      {
        type: 'candlestick',
        data: [
          [20, 34, 10, 38],
          [40, 35, 30, 50],
          [31, 38, 33, 44],
          [38, 15, 5, 42]
        ]
      }
    ]
  }
  return res
}
defineExpose({
  timeBarChange
})
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
}
</style>
