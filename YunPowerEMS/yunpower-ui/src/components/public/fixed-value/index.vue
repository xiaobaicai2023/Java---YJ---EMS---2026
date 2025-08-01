<!--
 * 功能：固定图
 * 作者：罗甜甜
 * 日期：2024-07-12
-->
<template>
  <!-- 加载动画，加载完成后显示状态图或空状态 -->
  <a-card :title="chartInfo.cardName"
  >
    <a-spin :loading="loading" class="fixed-wapper">
      <a-statistic v-if="chartInfo.cardName&&chartInfo.singleInfo.singleValue!=null"
                   :value="chartInfo.singleInfo.singleValue" :precision="2" :ellipsis="{ rows: 1, showTooltip: true }"
                   show-group-separator>
        <template #suffix>
          <span class="fixed-unit">{{ chartInfo.singleInfo.unit }}</span>
        </template>
      </a-statistic>
      <a-empty v-else/>
    </a-spin>
  </a-card>
</template>

<script lang="ts" setup>
import {computed, inject, onBeforeUnmount, onMounted, ref, watch} from "vue";
import useLoading from "@/hooks/loading";
import {SocketData} from "@/api/websocketService";
import {useIntervalFn} from "@vueuse/core";
import {getChartInfo} from "@/api/dashboard/api";

// 加载状态
const {loading, setLoading} = useLoading(false);

// 定义组件属性
const props = defineProps({
  public: {
    type: Object,
    default: () => ({})
  },
  // 设备编号
  deviceSn: {
    type: String,
    default: ''
  }
});

// 图表信息
const chartInfo = ref({
  cardName: ' ',
  singleInfo: {
    singleValue: null,
    unit: ''
  }
});

// 获取数据
const fetchData = async () => {
  try {
    setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      deviceSn: props.deviceSn
    };
    const res: any = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      chartInfo.value = res.data;
    } else {
      chartInfo.value = {
        cardName: ' ',
        singleInfo: {
          singleValue: null,
          unit: ''
        }
      }
    }
  } catch (e) {
    console.error('获取数据失败:', e);
  } finally {
    setLoading(false);
  }
};

// 组件挂载时获取数据并订阅
onMounted(async () => {
  await fetchData();
});

// 对外暴露的接口
defineExpose({
  fetchData
});
</script>

<style lang="less" scoped>
.arco-spin {
  width: 100%;
  height: 100%;
  border-radius: 4px;
  border: 1px solid var(--color-border-2);
  background: var(--color-bg-2);
}

.arco-card {
  height: 100%;

  :deep(.arco-card-body) {
    height: calc(100% - 46px);
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.arco-empty {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.fixed-wapper {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 20px;
  border: none;

  .fixed-unit {
    margin-left: 2px;
  }
}

:deep(.arco-statistic-value-integer),:deep(.arco-statistic-value-decimal) {
  display: inline-block;
  color: rgb(var(--primary-6));
  font-weight: bold;
}

:deep(.arco-card-body) {
  height: calc(100% - 66px);
}

</style>

