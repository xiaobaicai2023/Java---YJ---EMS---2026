<template>
  <a-card title="峰谷平用电能耗统计" class="home-statistics-box">
    <template #extra>
      <time-bar :pick-type="1" @change="timeChange" />
    </template>
    <a-spin :loading="loading" style="width: 100%" :tip="$t('global.loading')">
      <a-card class="general-card-grid">
        <a-grid :cols='{ xs: 1, sm: 1, md: 1, lg: 1, xl: 5, xxl: 5 }' :colGap="10" :row-gap="10">
          <a-grid-item v-for="(item, index) in electricityTotal" :key="index"
            style="backgroundColor: var(--color-fill-2);padding: 5px;">
            <a-row align="center">
              <a-col :span="17" style="overflow: auto">
                <a-statistic :title="item.name" :value="Number(item.numStr)" :precision="2">
                  <template #suffix>kWh</template>
                </a-statistic></a-col>
              <a-col :span="7">
                <a-progress type="circle" :percent="item.percentValue">
                  <template v-slot:text="scope">
                    {{ (scope.percent * 100).toFixed(0) }}%占电总能
                  </template></a-progress></a-col>
            </a-row>
          </a-grid-item>
        </a-grid>
      </a-card>
      <div class="chart-box">
        <CutomChart :options="chartData" />
      </div>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { PowerModuleEunm, getStackChart } from "@/api/system/home-power";
import useLoading from "@/hooks/loading";
import { handleStackChart } from "@/utils/charts";
import dayjs from "dayjs";
import { onMounted, onUnmounted, ref } from "vue";
import { BigNumber } from 'bignumber.js';
import { useIntervalFn } from "@vueuse/core";
//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>({});

interface electricity {
  name: string,
  num: BigNumber,
  numStr: number,
  percent: BigNumber,
  percentValue: number
}

// 占电总能
const electricityTotal = ref<electricity[]>([])

//时间参数
const timeOptions = ref<any>();

/**
 * 时间改变
 */
const timeChange = async (options: any) => {
  console.log("options", options)
  timeOptions.value = options;
  await fetchData();
}

/**
 * 获取日期数据
 * @param param 时间参数
 */
const fetchData = async () => {
  setLoading(true);
  try {
    let param;
    if (timeOptions.value.timeUnit == 0) {
      param = { pageValue: PowerModuleEunm.ppfv_electric_day, "beginTime": timeOptions.value.timer[0], "endTime": timeOptions.value.timer[1] };
    } else if (timeOptions.value.timeUnit == 1) {
      param = { pageValue: PowerModuleEunm.ppfv_electric_month, "beginTime": timeOptions.value.timer[0], "endTime": timeOptions.value.timer[1] };
    } else {
      param = { pageValue: PowerModuleEunm.ppfv_electric_year, "beginTime": timeOptions.value.timer[0], "endTime": timeOptions.value.timer[1] };
    }
    let res = await getStackChart(param);
    if (res.code == 200) {
      chartData.value = handleStackChart(res.data);
      console.log("峰谷平用电能耗统计", param.pageValue, chartData.value)
      //计算总数
      let electricityTotalList: electricity[] = [];
      if (res.data.yAxis && res.data.yAxis.length > 0) {
        res.data.yAxis.forEach((item: any) => {
          let total = item.dataList.reduce((accumulator: BigNumber, currentValue: BigNumber) => {
            return accumulator.plus(currentValue);
          }, new BigNumber(0));
          electricityTotalList.push({
            name: item.name,
            num: total,
            percent: new BigNumber(0),
            percentValue: 0,
            numStr: total.decimalPlaces(2).toNumber()
          });
        });
        electricityTotalList = calculatePercentage(electricityTotalList);
        electricityTotal.value = electricityTotalList;
      }
    }
  } catch (ex) {
    console.error("峰谷平用电能耗统计出错", ex)
  } finally {
    setLoading(false);
  }
}


/**
 * 计算百分比
 * @param data
 */
const calculatePercentage = (data: electricity[]): electricity[] => {

  // 优化总和计算
  const totalNum = data.reduce((accumulator: BigNumber, item: electricity) => accumulator.plus(item.num), new BigNumber(0));

  //总数为0 则直接返回
  if (totalNum.toNumber() == 0) {
    return data;
  }

  // 计算每个数据项的百分比
  const percentageData = data.map((item: electricity) => ({
    ...item,
    percent: item.num.dividedBy(totalNum)
  }));


  // // 计算所有数据百分比和
  // const totalPercentage = percentageData.reduce((accumulator: BigNumber, item: electricity) => accumulator.plus(item.percent), new BigNumber(0));
  // console.log("计算所有数据百分比和", totalPercentage.toNumber())
  // //调整余数
  // let defaultNumber = new BigNumber(1);
  // if (totalPercentage.isGreaterThan(defaultNumber)) {
  //   const percentageDifference = totalPercentage.minus(defaultNumber);
  //   for (let i = 0; i < percentageData.length; i++) {
  //     if (percentageData[i].percent.isGreaterThan(new BigNumber(0))) {
  //       percentageData[i].percent = percentageData[i].percent.minus(percentageDifference);
  //       break;
  //     }
  //   }
  // }

  let total: number = 0;
  let max = 0;
  percentageData.forEach(item => {
    item.percentValue = Number(item.percent.multipliedBy(100).decimalPlaces(0))
    // if (item.percentValue > 0 && item.percentValue < 10) {
    //   item.percentValue = 10;
    // }
    total += item.percentValue;
    if (item.percentValue > max) {
      max = item.percentValue
    }
    console.log(item.numStr);
  })
  console.log("max", max);
  const diff = total - 100;
  for (let i = 0; i < percentageData.length; i++) {
    let info = percentageData[i];
    if (max == info.percentValue) {
      info.percentValue -= diff
      break;
    }
  }
  for (let i = 0; i < percentageData.length; i++) {
    let info = percentageData[i];
    info.percentValue = (info.percentValue / 100)
  }
  return percentageData;
}


/**
 * 5分钟定时-峰谷平用电能耗统计
 *
 */
const { resume: startInterval, pause: stopInterval, isActive } = useIntervalFn(async () => {
  console.log('5分钟定时-峰谷平用电能耗统计')
  await fetchData()
}, 1000 * 60 * 5)

/**
 * 组件加载完成
 */
onMounted(async () => {
  console.log("onMounted-5分钟定时-峰谷平用电能耗统计")
  startInterval();
})

/**
 * 组件卸载
 */
onUnmounted(() => {
  console.log("onUnmounted-5分钟定时-峰谷平用电能耗统计")
  stopInterval()
})
</script>
<style scoped lang="less">
.grid-box {
  display: grid;
  grid-column-gap: 15px;

  .columns-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: var(--color-neutral-2);
    padding: 15px;
  }
}

.electricity-box {
  padding: 15px 0;
  justify-content: space-around !important;
  background-color: var(--color-neutral-2);
}

.chart-box {
  margin-top: 15px;
  height: 400px;
}

:deep(.arco-statistic) {
  color: var(--color-text-2);
  line-height: 1.5715;

  .arco-statistic-value {
    font-size: 24px;
  }
}
</style>
