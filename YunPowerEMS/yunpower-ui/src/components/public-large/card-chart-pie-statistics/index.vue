<!--
 * 功能：饼图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <a-card class="chart-container" :title="chartInfo.cardName">
    <!-- 额外操作栏，只有当 configType 为 1 时显示 -->
    <template #extra v-if="configType === 1">
      <time-bar @change="timeBarChange" isCurrentDateDisabled/>
    </template>
    <!-- 加载动画，加载完成后显示图表或空状态 -->
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <chart v-if="chartInfo.echartsOption?.series[0]?.data.length" :options="chartInfo.echartsOption"></chart>
      <a-empty v-else></a-empty>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {getTimeObject} from '@/utils/charts';
import {getChartInfo} from '@/api/dashboard/api';

// 定义组件接收的属性
const props = defineProps({
  public: {
    type: Object,
    default: () => ({}),
  },
  configType: {
    type: Number,
    default: 1,
  },
});

// 状态管理
const {loading, setLoading} = useLoading(false);
const chartInfo = ref({
  cardName: '',
  echartsOption: {
    series: [
      {
        data: [],
      },
    ],
  },
});
const timeOptions = ref<any>({});

// 时间变化处理函数
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
};

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
    const res: any = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = {
        ...res.data,
        echartsOption: chartData(res.data.echartsOption),
      };
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
  return {
    tooltip: {
      trigger: 'item',
    },
    legend: {
      bottom: 0,
      left: 'center',
      type: 'scroll',
    },
    series: [
      {
        name: option.series[0].name,
        type: 'pie',
        radius: '50%',
        avoidLabelOverlap: false,
        itemStyle: {
          borderWidth: 2,
          show: false,
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
        data: option.series[0].objData,
      },
    ],
  };
};

onMounted(() => {
  fetchData();
});


// 暴露 timeBarChange 方法
defineExpose({
  handleTime: (val: any)=>{
    timeBarChange(val);
  },
});
</script>

<style scoped lang="less">
.chart-container {
  height: 100%;
  width: 100%;
  //background: transparent;
  display: flex;
  flex-direction: column;

  :deep(.arco-card-body) {
    width: 100%;
    height: calc(100% - 46px);
  }

  .time-search {
    width: 100%;
    display: flex;
    justify-content: flex-end;
    z-index: 11;
  }

  .chart-view {
    width: 100%;
    flex: 1;
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
