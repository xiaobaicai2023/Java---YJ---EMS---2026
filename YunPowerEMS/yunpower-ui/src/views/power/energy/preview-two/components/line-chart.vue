<template>
  <div class="chart-box">
    <CutomChart :options="options"/>
  </div>
</template>

<script setup lang="ts">
//数据
const props = defineProps(['data','average'])
// 公共设置
const setting = {
  symbol: 'circle',
  type: 'line',
  showSymbol: false,
  smooth: true,
}
// 处理后的数据
const seriesData = []
for (const dataKey in props.data) {
  seriesData.push({name: dataKey, data: props.data[dataKey], ...setting})
}
const options = {
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    icon: 'roundRect',
    itemWidth: 8,
    itemHeight: 8,
    bottom: 0
  },
  grid: {
    top: 20,
    left: 40,
    bottom: 70,
    right: 30
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'],
    axisTick: {
      // 轴刻度
      show: false,
    }
  },
  yAxis: {
    type: 'value'
  },
  dataZoom: [
    {
      type: "inside",
    },
    {
      backgroundColor: '#7BCFA3',
      height: 10,
      opacity: 0,
      moveHandleSize: 0
    }
  ],
  series: seriesData
}
</script>

<style scoped lang="less">
.chart-box {
  width: 100%;
  height: 350px;
}
</style>
