<template>
  <a-card class="energy-box" :title="$t('power.energy.preview.peek')">
    <template #extra>
      <time-bar @change="timeChange" />
    </template>
    <a-spin :loading="loading" style="width: 100%;height: 390px;" :tip="$t('global.loading')">
      <CutomChart v-if="!loading" :options="chartData" autoresize height="390px"></CutomChart>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { PowerModuleEunm, getChart } from '@/api/system/home-power';
import { onMounted, onUnmounted } from 'vue';
import { ref } from 'vue';
import useLoading from '@/hooks/loading';
import { handleChartData } from '@/utils/charts';
import { useIntervalFn } from '@vueuse/core';


//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>({});
const options = ref<any>({});
/**
 * 时间改变
 */
const timeChange = async (val: any) => {
  console.log("options", val)
  options.value = val;
  await fetchData();
}

/**
 * 获取日期数据
 * @param param 时间参数
 */
const fetchData = async () => {
  setLoading(true);
  let param: any = { pageValue: '', dayTime: '', monthTime: '', yearTime: '' };
  if (options.value.timeUnit == 0) {
    param = { pageValue: PowerModuleEunm.electric_use_stat_day, dayTime: options.value.timer };
  } else if (options.value.timeUnit == 1) {
    param = { pageValue: PowerModuleEunm.electric_use_stat_month, monthTime: options.value.timer };
  } else {
    param = { pageValue: PowerModuleEunm.electric_use_stat_year, yearTime: options.value.timer };
  }
  try {
    let res = await getChart(param);
    if (res.code == 200) {
      chartData.value = handleChartData(res.data);
      console.log("用电能耗统计", param.pageValue, chartData.value)
    }
  } catch (ex) {
    console.error("用电能耗统计出错", ex)
  } finally {
    setLoading(false);
  }
}


/**
 * 5分钟定时-用电能耗统计
 *
 */
 const { resume: startInterval, pause: stopInterval } = useIntervalFn(async () => {
  console.log("5分钟定时-用电能耗统计")
    await fetchData();
}, 1000 * 60 * 5)

/**
 * 组件加载完成
 */
 onMounted(async () => {
  startInterval();
})

/**
 * 组件卸载
 */
onUnmounted(() => {
  stopInterval()
})
</script>

<style scoped lang="less"></style>
