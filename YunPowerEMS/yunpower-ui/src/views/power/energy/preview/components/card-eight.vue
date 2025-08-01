<template>
  <a-card title="有功功率监控">
    <template #extra>
      <time-bar @change="timeChange" />
    </template>
    <a-spin class="chart-box" :loading="loading" style="width: 100%" :tip="$t('global.loading')">
      <CutomChart v-if="!loading" :options="chartData" autoresize></CutomChart>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { PowerModuleEunm, getChart } from '@/api/system/home-power';
import dayjs from "dayjs";
import { onMounted } from 'vue';
import { ref } from 'vue';
import useLoading from '@/hooks/loading';
import { handleChartData } from '@/utils/charts';


//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>({});

/**
 * 时间改变
 */
const timeChange = async (options: any) => {
  console.log("options", options.timeUnit)
  if (options.timeUnit == 0) {
    await fetchData({ pageValue: PowerModuleEunm.active_power_day, "dayTime": options.timer });
  } else if (options.timeUnit == 1) {
    await fetchData({ pageValue: PowerModuleEunm.active_power_month, "monthTime": options.timer });
  } else {
    await fetchData({ pageValue: PowerModuleEunm.active_power_year, "yearTime": options.timer });
  }
}

/**
 * 获取日期数据
 * @param param 时间参数
 */
const fetchData = async (param: any) => {

  setLoading(true);
  try {
    let res = await getChart(param);
    if (res.code == 200) {
      chartData.value = handleChartData(res.data);
      console.log("有功功率监控", param.pageValue, chartData.value)
    }
  } catch (ex) {
    console.error("有功功率监控出错", ex)
  } finally {
    setLoading(false);
  }
}
</script>

<style scoped lang="less">
.chart-box {
  width: 100%;
  height: 350px;
}
</style>

