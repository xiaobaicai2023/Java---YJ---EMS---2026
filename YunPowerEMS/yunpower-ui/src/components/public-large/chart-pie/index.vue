<!--
 * 功能：饼图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <div class="chart-container">
    <!-- 加载动画，加载完成后显示图表或空状态 -->
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <chart v-if="chartInfo?.echartsOptionList.length" :options="chartInfo.echartsOption"></chart>
      <a-empty v-else/>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, watchEffect} from 'vue';
import useLoading from '@/hooks/loading';
import {getChartInfo} from '@/api/dashboard/api';
import {getTimeObject, handleConfigChart, handlePreConfigChart} from '@/utils/charts';


// 定义组件接收的属性
const props = defineProps({
  public: {
    type: Object,
    default: () => ({}),
  }
});

// 状态管理
const {loading, setLoading} = useLoading(false);
const timeOptions = ref<any>({});
const chartInfo = ref({
  cardName: '',
  echartsOption: {
    series: [
      {
        data: [],
      },
    ],
  },
  echartsOptionList: []
});
// 查询图表数据
const fetchData = async () => {
  if(!props.public.cardKey || !props.public.dashboardConfigId){
    return;
  }
  try {
    setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      ...getTimeObject(timeOptions.value),
    };
    const res = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = {
        ...res.data,
        // echartsOption: chartData(res.data.echartsOption),
      };

      if (res.data.echartsOptionList && res.data.echartsOptionList.length) {
        chartInfo.value.echartsOption = chartData(res.data.echartsOptionList);
      }
    } else {
      chartInfo.value = {
        cardName: '',
        echartsOption: {
          series: [
            {
              data: [],
            },
          ],
        },
        echartsOptionList: []
      }
    }
  } catch (error) {
    console.error('获取图表数据时出错:', error);
  } finally {
    setLoading(false);
  }
};

// 生成图表配置
const chartData = (option: any) => {
  const seriesList = option.map((item: any, index: number) => {
    // 1：小图 2：编辑预览 3：大屏预览
    const radiusResult = props.public.preview == 1 ? ['20%', '35%'] : props.public.preview == 2 ? ['30%', '60%'] : ['40%', '70%'];
    return {
      name: item.series[0].name,
      type: 'pie',
      radius: radiusResult,
      center: [(index + 1) * 25 + '%', '45%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderWidth: 2,
        show: false,
        borderRadius: 5,
      },
      label: {
        show: false,
        position: 'center',
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 20,
          fontWeight: 'bold',
          formatter: '{d}%',
        },
      },
      labelLine: {
        show: false,
      },
      data: item.series[0].objData,
    }
  })

  console.log(seriesList, 'seriesList')

  return {
    tooltip: {
      trigger: 'item',
    },
    series: seriesList,
  };
};

const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
};

onMounted(async () => {
  await fetchData();
})

watchEffect(() => {
  console.log(props.public, 'props.public.preview')
})

// 暴露 timeBarChange 方法
defineExpose({
  handleTime: (val: any) => {
    timeBarChange(val);
  },
});
</script>

<style scoped lang="less">
.chart-container {
  height: 100%;
  width: 100%;

  .chart-view {
    width: 100%;
    height: 100%;
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
