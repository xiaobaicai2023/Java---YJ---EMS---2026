<template>
  <a-spin :loading="loading" style="width: 100%;height: 230px;">
    <a-card class="runtime-card" :title="$t('pv.preview.realTime')" style="height: 220px;">
      <a-grid :cols='{ xs: 1, sm: 1, md: 1, lg: 1, xl: 2, xxl: 2 }' :colGap="10" :row-gap="10">
        <a-grid-item>
          <div class="chartRef" style="height: 140px;"></div>
        </a-grid-item>
        <a-grid-item>
          <a-space direction="vertical" fill>
            <a-statistic :title="$t('pv.preview.power')" :value="renderData['发电功率']" :precision="2" :value-from="0" animation>
              <template #suffix>kW</template>
            </a-statistic>
            <a-statistic :title="$t('pv.preview.capacity')" :value="renderData['装机容量']" :precision="2" :value-from="0" animation>
              <template #suffix>kWp</template>
            </a-statistic>
          </a-space>
        </a-grid-item>
      </a-grid>
    </a-card>
  </a-spin>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import useLoading from '@/hooks/loading';
import { useWindowSize } from '@vueuse/core';
import * as echarts from 'echarts' //引入echarts
import 'echarts-liquidfill'
import { getRuntimeInfo } from '@/api/system/home-pv';

const { width, height } = useWindowSize();
const renderData = ref<any>(0);
// echarts实例
const chartInstance = ref<any>(null)

//初始化
const initEchart = (data:any) => {
  //获取对应的dom元素
  const el = document.querySelector('.chartRef') as HTMLDivElement
  // 初始化
  chartInstance.value = echarts.init(el)
  const option: any = {
    series: [
      {
        type: 'liquidFill', //水位图
        radius: '110px', //显示比例
        center: ['50%', '50%'], //中心点
        amplitude: 10, //水波振幅
        data: [data], // data个数代表波浪数
        color: [
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#446bf5',
              },
              {
                offset: 1,
                color: '#2ca3e2',
              },
            ],
            globalCoord: false,
          },
        ], //波浪颜色
        backgroundStyle: {
          borderWidth: 1, //外边框
          // borderColor: '#23cc72', //边框颜色
          color: 'RGBA(51, 66, 127, 0.7)', //边框内部填充部分颜色
        },
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
        outline: {
          show: true,
          borderDistance: 5,
          itemStyle: {
            borderColor: 'rgba(67,209,100,1)',
            borderWidth: 0
          }
        },
      }
    ]
  }
  chartInstance.value.setOption(option)
}

/**
 * 监听
 */
watch(width, () => {
  chartInstance.value.resize()
})

/**
 * 获取实时数据
 */
const fetchData = async () => {
  setLoading(true)
  try {
    let res = await getRuntimeInfo({});
    if (res.code == 200) {
      renderData.value = res.data;
      initEchart(renderData.value["功率归一化"]);
    }
  } catch (e) {
  } finally {
    setLoading(false)
  }
}
/**
 * 首次加载
 */
onMounted(async () => {
  await fetchData();
})
const { loading, setLoading } = useLoading(false);
</script>
<style lang="less" scoped>
:deep(.arco-statistic) {
  .arco-statistic-title {
    margin-bottom: 0px;
    font-size: 12px;
    font-weight: bold;
    white-space: nowrap;
  }

  .arco-statistic-content {
    margin-top: 0px;
  }
}
</style>
