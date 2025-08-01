<!--
 * 功能：分段图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <div class="chart-container" :title="chartInfo.cardName">
    <a-spin :loading="loading" class="chart-view" :tip="$t('global.loading')">
      <CutomChart v-if="!loading" :options="chartInfo.schemeMap"></CutomChart>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import useLoading from '@/hooks/loading';
import {onMounted, ref} from "vue";
import {getTimeObject, handleSeasonalRangeChart} from "@/utils/charts";
import {getChartInfo} from "@/api/dashboard/api";
import {useIntervalFn} from "@vueuse/core";

const props = defineProps({
  public: {
    type: Object,
    default: () => {
      return {}
    }
  },
  configType: {
    type: Number,
    default: 1
  },
  // 设备编号
  deviceSn:{
    type: String,
    default: ''
  }
});
//加载中
const {loading, setLoading} = useLoading(false);
const chartInfo = ref<any>({
  cardName: '',
  schemeMap: null
});

// 时间选项
const timeOptions = ref<any>({});

// 时间变化处理函数
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
};


const fetchData = async () => {
  if(!props.public.cardKey || !props.public.dashboardConfigId){
    return;
  }
  try {
    setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      deviceSn: props.deviceSn,
      ...getTimeObject(timeOptions.value),
    };
    const res = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = {
        cardName: res.data.cardName,
        schemeMap: handleSeasonalRangeChart(res.data.schemeMap)
      };
    } else{
      chartInfo.value = {
        cardName: '',
        schemeMap: null
      }
    }
  } catch (e) {
    console.log(e)
  } finally {
    setLoading(false);
  }
};
// 每5分钟轮询一次数据
useIntervalFn(fetchData, 300000);

onMounted(() => {
  fetchData()
})
defineExpose({
  fetchData,
  handleTime: (val)=>{
    timeBarChange(val);
  },
})
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
