<template>
  <a-card :title="$t('pv.preview.inverter')" class="home-statistics-box">
    <template #extra>
      <time-bar  @change="timeChange" />
    </template>
    <a-spin :loading="loading" style="width: 100%" :tip="$t('global.loading')">
      <div class="chart-box">
        <CutomChart :options="chartData" />
      </div>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { PowerModuleEunm, getChart } from "@/api/system/home-power";
import useLoading from "@/hooks/loading";
import { handleChartData } from "@/utils/charts";
import { onMounted, ref } from "vue";

//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>({});

/**
 * 时间改变
 */
const timeChange = async (options: any) => {
  console.log("options", options)
  if (options.timeUnit == 0) {
    await fetchData({ pageValue: PowerModuleEunm.pv_power_inverter_day, "dayTime": options.timer });
  } else if (options.timeUnit == 1) {
    await fetchData({ pageValue: PowerModuleEunm.pv_power_inverter_month, "monthTime": options.timer });
  } else {
    await fetchData({ pageValue: PowerModuleEunm.pv_power_inverter_year, "yearTime": options.timer });
  }
}

/**
 * 获取逆变器数据
 * @param param 时间参数
 */
const fetchData = async (param: any) => {
  setLoading(true);
  try {
    let res = await getChart(param);
    if (res.code == 200) {
      chartData.value = handleChartData(res.data);
      console.log("逆变器统计", param.pageValue, chartData.value)
    }
  } catch (ex) {
    console.error("逆变器统计出错", ex)
  } finally {
    setLoading(false);
  }
}
</script>
<style scoped lang="less">
:deep(.arco-statistic) {
  .arco-statistic-title {
    margin-bottom: 0px;
    font-size: 14px;
    font-weight: bold;
    white-space: nowrap;
  }

  .arco-statistic-content {
    margin-top: 0px;
  }
}

.chart-box {
  margin-top: 15px;
  height: 400px;
}
</style>
