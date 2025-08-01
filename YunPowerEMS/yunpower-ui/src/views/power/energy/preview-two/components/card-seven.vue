<template>
  <a-card :title="title">
    <template #extra>
      <time-bar @change="timeChange" />
    </template>
    <a-spin class="chart-box" :loading="loading" style="width: 100%;" :tip="$t('global.loading')">
      <a-row style="height: 100%;" justify="start" align="center">
        <a-col :span="18" style="height: 100%;">
          <CutomChart v-if="!loading" :options="chartData" autoresize></CutomChart>
        </a-col>
        <a-col :span="6">
          <a-space direction="vertical" fill>
            <div v-for="(item, index) in chartCardData" :key="index" class="card-statistic">
              <a-statistic :title="item.name" :value="item.value" :precision="2">
                <template #suffix>{{ item.unit }}</template>
              </a-statistic>
            </div>
          </a-space>
        </a-col>
      </a-row>
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
import BigNumber from 'bignumber.js';
const props = defineProps({
  title: {
    type: String,
    default: ''
  }
})

//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>({});
const chartCardData = ref<any>({});
/**
 * 时间改变
 */
const timeChange = async (options: any) => {
  console.log("options", options.timeUnit)
  if (options.timeUnit == 0) {
    await fetchData({ pageValue: PowerModuleEunm.capacity_demand_day, "dayTime": options.timer });
  } else if (options.timeUnit == 1) {
    await fetchData({ pageValue: PowerModuleEunm.capacity_demand_month, "monthTime": options.timer });
  } else {
    await fetchData({ pageValue: PowerModuleEunm.capacity_demand_year, "yearTime": options.timer });
  }
  await fetchCapacityDemandData({ pageValue: PowerModuleEunm.capacity_demand, "yearTime": options.timer });
}

/**
 *
 */
const fetchCapacityDemandData = async (param: any) => {
  try {
    chartCardData.value = [];
    let res = await getChart(param);
    if (res.code == 200) {
      if (res.data.yAxis && res.data.yAxis.length > 0) {
        let cardList: any = [];
        res.data.yAxis.forEach((item: any) => {
          let info = {
            name: item.name,
            value: item.dataList[0],
            unit: item.yAxisUnit
          };
          cardList.push(info)
        })
        chartCardData.value = cardList;
      }
    }
  } catch (ex) {
  } finally {
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
      console.log("用电容量负荷需量监控", param.pageValue, chartData.value)
    }
  } catch (ex) {
    console.error("用电容量负荷需量监控出错", ex)
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

.card-statistic {
  background-color: var(--color-fill-2);
  padding: 5px;

  :deep(.arco-statistic-value) {
    font-size: 18px;
  }
}
</style>
