<!--
 * 功能：桑基图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <a-card class="chart-container" :title="chartInfo.cardName">
    <!-- 额外操作栏，只有当 configType 为 1 时显示 -->
    <template #extra v-if="configType === 1">
      <time-bar @change="timeBarChange" isCurrentDateDisabled/>
    </template>
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <chart :options="chartInfo.sankeyInfo" v-if="chartInfo.sankeyInfo?.series?.data.length"></chart>
      <a-empty v-else/>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import {ref} from "vue";
import useLoading from "@/hooks/loading";
import {getTimeObject} from "@/utils/charts";
import {getChartInfo} from "@/api/dashboard/api";

// 定义 props
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

// 加载状态
const {loading, setLoading} = useLoading(false);

// 图表信息
const chartInfo = ref({
  cardName: '',
  sankeyInfo: {
    series: {
      data: []
    }
  }
});

// 时间选项
const timeOptions = ref<any>({});

// 时间变化处理函数
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
};

// 获取数据
const fetchData = async () => {
  setLoading(true);
  try {
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      ...getTimeObject(timeOptions.value),
    };
    const res: any = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = {
        cardName: res.data.cardName,
        sankeyInfo: chartData(res.data.sankeyInfo),
      };
    } else {
      chartInfo.value = {
        cardName: '',
        sankeyInfo: {
          series: {
            data: []
          }
        }
      }
    }
  } catch (error) {
    console.error('获取图表数据时出错:', error);
  } finally {
    setLoading(false);
  }
};

// 处理图表数据
const chartData = (option: any): any => {
  return {
    tooltip: {
      show: true,
      formatter: (params: any) => {
        let data = params.data;
        if (data.name) {
          return `${params.name} <b style="color: #1d2129">${params.value}</b>`;
        } else {
          return `<div>
              ${data.source}--${data.target} <b style="margin-left:10px;color: #1d2129">${data.value}</span> ${data.unit?data.unit:''}</b>
          </div>`;
        }
      }
    },
    grid: {
      top: "12%",
      left: 20,
      bottom: 0,
      right: 20,
      containLabel: true,
    },
    series: {
      type: 'sankey',
      layout: 'none',
      emphasis: {
        focus: 'adjacency',
      },
      data: option.data,
      links: option.links,
    },
  };
};

// 暴露 fetchData 函数
defineExpose({
  timeBarChange,
});
</script>

<style scoped lang="less">
.chart-container {
  height: 100%;

  :deep(.arco-card-body) {
    width: 100%;
    height: calc(100% - 46px);
  }

  .chart-view {
    width: 100%;
    height: 100%;

    .arco-empty {
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
    }
  }
}
</style>
