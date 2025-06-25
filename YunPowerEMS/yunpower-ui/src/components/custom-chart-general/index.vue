<template>
  <Chart  :options="chartOption" autoresize></Chart>
</template>
<script setup lang="ts">
import useChartOption from '@/hooks/chart-option';
import { nextTick, onMounted, ref } from 'vue';

const props = defineProps({
  chartData: {
    type: Object,
    default: {
      yAxis: [],
      xAxis: [],
      legend: [],
      xAxisUnit: []
    }
  }
})
const renderChart = ref(false)
const xAxis =ref<string[]>([]);
//Y轴单位
const yAxis: any = ref<any>([]);
//Y轴数据
const series: any = ref<any>([]);

const { chartOption } = useChartOption(() => {
  return {
    tooltip: { trigger: "axis" },
    legend: {
      data: props.chartData.legend,
      icon: "roundRect",
      itemWidth: 8,
      itemHeight: 8,
      left: 40,
      bottom: 0,
    },
    grid: {
      top: 10,
    },
    xAxis: [
      {
        type: "category",
        data: xAxis.value,
        // 阴影指示器
        axisPointer: {
          type: "shadow",
        },
        axisTick: {
          // 轴刻度
          show: false,
        },
      },
    ],
    yAxis: yAxis.value,
    dataZoom: [
      {
        type: "inside",
      },
      {
        backgroundColor: "#7BCFA3",
        height: 10,
        moveHandleSize: 0,
      },
    ],
    series: series.value,
  };
})



const _getMaxValue = (arr: any) => {
  const max = Math.max(...arr);
  return Math.ceil(max / 9.5) * 10;
}

const _getMinValue = (arr: any) => {
  const min = Math.min(...arr);
  return Math.floor(min / 12) * 10;
}

onMounted(() => {
  
  xAxis.value = props.chartData.xAxis;

  if (props.chartData.yAxisUnit && props.chartData.yAxisUnit.length > 0) {

    const values: any = [];
    if (props.chartData.yAxis && props.chartData.yAxis.length > 0) {
      props.chartData.yAxis.forEach((item: any, index: number) => {
        values.push({
          minValue: _getMinValue(item.dataList),
          maxValue: _getMaxValue(item.dataList),
        });
      });
    }

    props.chartData.yAxisUnit.forEach((item: any, index: number) => {
      yAxis.value.push({
        type: "value",
        name: item,
        show: true,
        // min:  values[index].minValue,    
        // max:  values[index].maxValue,  
        // interval: (values[index].maxValue - values[index].minValue) / 5,  
        axisLabel: {
          formatter: "{value}",
        },
        boundaryGap: [0.2, 0.2]
      });
    });
  }
  if (props.chartData.yAxis && props.chartData.yAxis.length > 0) {
    props.chartData.yAxis.forEach((item: any, index: number) => {
      series.value.push({
        name: item.name,
        type: props.chartData.chartType[index],
        yAxisIndex: index,
        symbol: "circle",
        smooth: true,
        showSymbol: false,
        color: index % 2 == 0 ? "#7BCFA3" : "#F3B07E",
        tooltip: {
          valueFormatter: function (value: any) {
            return value + props.chartData.yAxisUnit[index];
          },
        },
        data: item.dataList,
      });
    });
  }

  console.log("series",series.value);


})
// wait container expand
nextTick(() => {
  renderChart.value = true
})

</script>