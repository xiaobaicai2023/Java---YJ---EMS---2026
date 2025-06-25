<template>
  <a-spin :loading="loading" style="width: 100%">
    <a-card :bordered="false" :style="cardStyle">
      <a-statistic style="margin-left: 10px;" :title="pageTitle" :value="renderData" :value-from="0" animation :precision="2">
        <template #suffix>{{ suffix }}</template></a-statistic>
    </a-card>
  </a-spin>
</template>

<script lang="ts" setup>
import { ref, PropType, CSSProperties } from 'vue';
import useLoading from '@/hooks/loading';
import { onMounted } from 'vue';
import { getChart } from '@/api/system/home-power';

const renderData = ref<any>(0);
const { loading, setLoading } = useLoading(false);
const props = defineProps({
  pageTitle: {
    type: String,
    default: '',
  },
  pageValue: {
    type: String,
    default: '',
  },
  suffix: {
    type: String,
    default: '',
  },
  cardStyle: {
    type: Object as PropType<CSSProperties>,
    default: () => {
      return {};
    },
  },
});


/**
 * 获取日期数据
 * @param param 时间参数
 */
const fetchData = async () => {
  if (!props.pageValue) {
    return
  }
  setLoading(true);
  try {
    let res = await getChart({ pageValue: props.pageValue });
    if (res.code == 200 && res.data.yAxis && res.data.yAxis.length == 1 && res.data.yAxis[0].dataList &&  res.data.yAxis[0].dataList.length) {
      renderData.value = res.data.yAxis[0].dataList[0];
    }
  } catch (ex) {
    console.error("有功功率监控出错", ex)
  } finally {
    setLoading(false);
  }
}

onMounted(async () => {
  await fetchData();
})
</script>

<style scoped lang="less">
</style>
