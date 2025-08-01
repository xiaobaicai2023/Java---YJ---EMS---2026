<template>
  <a-card title="峰谷平用电能耗统计" class="home-statistics-box">
    <template #extra>
      <time-bar :pick-type="1" @change="timerChange"/>
    </template>
    <div class="grid-box" :style="{ gridTemplateColumns: electricityTotal.length > 4 ?  '1fr 1fr 1fr 1fr 1fr' : '1fr 1fr 1fr 1fr' }">
      <div v-for="(item,index) in electricityTotal" :key="index" class="columns-box">
        <a-statistic :title="item.name" :value="item.num" :precision="2">-->
          <template #suffix>/kWh</template>
        </a-statistic>
        <a-progress type="circle" :percent="item.percent"/>
      </div>
    </div>
    <div class="chart-box">
      <CutomChart :options="chartOptions"/>
    </div>
  </a-card>
</template>

<script setup lang="ts">

import TimeBar from "@/components/time-bar/index.vue";
import {reactive, ref} from "vue";

interface electricityTotalType {
  name: string,
  num: number,
  percent: number
}

const timerChange = (date: any) => {

}
// 占电总能
const electricityTotal = ref<electricityTotalType[]>([
  {
    name: '高峰时段',
    num: 9999,
    percent: 0.5
  },
  {
    name: '低谷时段',
    num: 0.0,
    percent: 0
  },
  {
    name: '深谷时段',
    num: 7.77,
    percent: 0.36
  },
  {
    name: '平均时段',
    num: 62689.5,
    percent: 1
  },
  {
    name: '尖峰时段',
    num: 7.77,
    percent: 0.36
  }
])
const chartData = {
  // 高峰时段
  peakData: [75,50,53,10,7,78,52,19,39,5,70,54,2,25,64,69,71,3,73,20,61,80,31],
  // 低谷时段
  lowEbbData: [61,69,24,51,43,71,49,74,23,78,66,80,22,12,60,7,20,73,63,65,76,62,2],
  // 深谷时段
  highEbbData: [80,77,64,61,35,59,14,49,54,67,56,58,62,71,21,2,75,16,66,65,29,53,28],
  // 平均时段
  averageData: [24,67,5,35,66,52,22,77,66,39,74,22,2,6,64,48,47,60,38,36,30,19,5],
  // 尖峰时段
  aiguilleData: [64,67,36,18,49,67,76,24,16,19,6,58,71,58,17,4,80,22,71,13,4,13,29]
}
// charts 数据
const chartOptions = reactive({
  color: ['#A05B08','#CEF3DC','#7BCFA3','#3C7161','#F3B07E'],
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['高峰时段','低谷时段','深谷时段','平均时段','尖峰时段'],
    icon: 'rect',
    left: 30,
    bottom: 0,
    itemWidth: 8,
    itemHeight: 8
  },
  grid: {
    left: 50,
    right: 0
  },
  xAxis: [
    {
      type: 'category',
      data: ['0时', '1时', '2时', '3时', '4时', '5时', '6时', '7时', '8时', '9时', '10时', '11时', '12时', '13时', '14时', '15时', '16时', '17时', '18时', '19时', '20时', '21时', '22时', '23时'],
      // 阴影指示器
      axisPointer: {
        type: 'shadow'
      },
      axisTick: {
        // 轴刻度
        show: false,
      }
    }
  ],
  yAxis: [
    {
      type: 'value',
      name: '总用电量(kW-h)'
    }
  ],
  dataZoom: [
    {
      type: "inside",
    },
    {
      type: "slider",
      backgroundColor: '#7BCFA3',
      height: 10,
      opacity: 0,
      moveHandleSize: 0
    }
  ],
  series: [
    {
      name: '高峰时段',
      type: 'bar',
      stack: 'x',
      data: chartData.peakData
    },
    {
      name: '低谷时段',
      type: 'bar',
      stack: 'x',
      data: chartData.lowEbbData
    },
    {
      name: '深谷时段',
      type: 'bar',
      stack: 'x',
      data: chartData.highEbbData
    },
    {
      name: '平均时段',
      type: 'bar',
      stack: 'x',
      data: chartData.averageData
    },
    {
      name: '尖峰时段',
      type: 'bar',
      stack: 'x',
      data: chartData.aiguilleData
    },
  ]
})
</script>
<style scoped lang="less">
.grid-box {
  display: grid;
  grid-column-gap: 15px;
  .columns-box{
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
</style>
