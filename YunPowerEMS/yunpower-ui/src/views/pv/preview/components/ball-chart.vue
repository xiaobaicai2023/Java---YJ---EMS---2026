<template>
  <div class="container">
    <div class="chartRef"></div>
    <div class="my-tooltip">
      <a-tooltip content="This is tooltip content" >
        <icon-question-circle-fill size="18"/>
      </a-tooltip>
    </div>
  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts' //引入echarts
import 'echarts-liquidfill'
import {onMounted, ref, watch} from "vue";
import {useWindowSize} from "@vueuse/core";
// echarts实例
const chartInstance = ref<any>(null)
const initEchart = () => {
  //获取对应的dom元素
  const el = document.querySelector('.chartRef') as HTMLDivElement
  // 初始化
  chartInstance.value = echarts.init(el)
  const option:any = {
    series: [{
      type: 'liquidFill',
      radius: '100px',
      data: [0.6],
      label: {
        normal: {
          color: '#fff',
          insideColor: 'transparent',
          textStyle: {
            fontSize: 26,
            fontWeight: 'bold',
          }
        }
      },
      color: [{
        type: 'linear',
        x: 0,
        y: 1,
        x2: 0,
        y2: 0,
        colorStops: [{
          offset: 1,
          color: ['#7595F7'], // 0% 处的颜色
        }, {
          offset: 0,
          color: ['#D0DCFD'], // 100% 处的颜色
        }],
        global: false // 缺省为 false
      }],
      outline: {
        show: true,
        borderDistance: 5,
        itemStyle: {
          borderColor: 'rgba(67,209,100,1)',
          borderWidth: 0
        }
      }
    }]
  }
  chartInstance.value.setOption(option)
}

const { width,height } = useWindowSize()
watch(width,() => {
  chartInstance.value.resize()
})

onMounted(() => {
  initEchart()
})
</script>

<style scoped>
.container{
  width: 100%;
  height: 100%;
  position: relative;
}
.chartRef {
  width: 100%;
  height: 100%;
}
.my-tooltip{
  position: absolute;
  right: calc(50% - 60px);
  top: 10px;
}
</style>
