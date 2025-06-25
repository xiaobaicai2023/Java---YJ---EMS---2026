<!--
 * 功能：堆叠图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <a-card class="chart-container" :title="chartInfo.cardName">
    <template #extra v-if="configType === 1">
      <time-bar @change="timeBarChange" isCurrentDateDisabled />
    </template>
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <CutomChart v-if="!loading" :options="chartInfo.echartsOption"></CutomChart>
    </a-spin>
    <slot/>
  </a-card>
</template>

<script setup lang="ts">
import useLoading from '@/hooks/loading';
import {ref} from "vue";
import {getTimeObject, handleConfigChart, handleZhiChart} from "@/utils/charts";
import {getChartInfo} from "@/api/dashboard/api";

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
const chartInfo = ref({
  cardName: '',
  echartsOption: {}
});
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
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      ...getTimeObject(timeOptions.value),
    };
    const res: any = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = {
        cardName: res.data.cardName,
        echartsOption: timeOptions.value.timeUnit === 0 ? handleZhiChart(res.data) : handleConfigChart(res.data.echartsOption)
      }
    } else {
      chartInfo.value = {
        cardName: '',
        echartsOption: {}
      }
    }
  } catch (error) {
    console.error('堆叠图表数据时出错:', error);
  } finally {
    setLoading(false);
  }
};


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
