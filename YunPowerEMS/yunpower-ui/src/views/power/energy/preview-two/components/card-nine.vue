
<template>
  <a-card title="分组能耗占比">
    <template #extra>
      <time-bar @change="timeChange" />
    </template>
    <a-spin class="ranking-box" :loading="loading" style="width: 100%" :tip="$t('global.loading')">
      <div v-if="chartData && chartData.length > 0" v-for="(item, index) in chartData" :key="index" class="ranking-list">
        <div class="header">
          <span class="tag"
            :style="{ backgroundColor: colorList[index] }">{{
              index + 1
            }}</span>
          <span> {{ item.name }}</span>
          <span> {{ (item.dataList[0] | 0) + '/kW-h' }}</span>
        </div>
        <a-progress v-if="total > 0" :percent="((item.dataList[0] | 0) / total)" :show-text="false" :stroke-width="8" />
        <a-progress v-else :percent="0" :show-text="false" :stroke-width="8" />
      </div>
      <a-empty v-else />
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { PowerModuleEunm, getChart } from '@/api/system/home-power';
import { ref } from 'vue';
import useLoading from '@/hooks/loading';

const colorList= ref<any>([
  "rgb(var(--red-7))",
  "rgb(var(--orange-7))",
  "rgb(var(--orange-6))",
  "rgb(var(--orange-5))",
  "rgb(var(--orange-4))",
  "rgb(var(--orange-3))"
]);
//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>({});
const total = ref<any>(0);
/**
 * 时间改变
 */
const timeChange = async (options: any) => {
  console.log("options", options.timeUnit)
  if (options.timeUnit == 0) {
    await fetchData({ pageValue: PowerModuleEunm.group_energy_day, "dayTime": options.timer });
  } else if (options.timeUnit == 1) {
    await fetchData({ pageValue: PowerModuleEunm.group_energy_month, "monthTime": options.timer });
  } else {
    await fetchData({ pageValue: PowerModuleEunm.group_energy_year, "yearTime": options.timer });
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
    total.value = 0;
    chartData.value = {};
    if (res.code == 200 && res.data.yAxis && res.data.yAxis.length > 0) {
      total.value = res.data.yAxis.reduce((accumulator: number, item: any) => {
        return accumulator + (item.dataList[0] | 0);
      }, 0); // 初始值设置为 0
      chartData.value = res.data.yAxis;
      console.log("total", total.value);
      console.log("分组能耗占比", param.pageValue, chartData.value)
    }
  } catch (ex) {
    console.error("分组能耗占比出错", ex)
  } finally {
    setLoading(false);
  }
}
</script>

<style scoped lang="less">
.ranking-box {
  height: 350px;
  overflow-y: auto;

  .ranking-list {
    .header {
      display: grid;
      grid-template-columns: 5% 65% 30%;
      padding-bottom: 5px;

      span:last-child {
        text-align: right;
      }
    }

    padding: 10px 0;
  }

  .tag {
    display: inline-block;
    color: #FFF;
    font-size: 12px;
    width: 17px;
    height: 17px;
    text-align: center;
    line-height: 17px;
    border-radius: 2px;
  }
}

.arco-empty {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
</style>
