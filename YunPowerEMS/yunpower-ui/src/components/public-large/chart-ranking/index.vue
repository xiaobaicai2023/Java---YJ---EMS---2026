<!--
 * 功能：排行榜
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
    <!-- 加载动画，加载完成后显示排名列表或空状态 -->
  <a-spin class="ranking-box" :loading="loading" :tip="$t('global.loading')">
    <template v-if="chartInfo.rankingList.length > 0">
      <div v-for="(item, index) in chartInfo.rankingList" :key="index" class="ranking-list">
        <div class="header">
          <span class="tag" :style="{ backgroundColor: colorList[index] }">{{ index + 1 }}</span>
          <span>{{ item.name }}</span>
          <span>{{ item.value + '/' + item.unit }}</span>
        </div>
        <a-progress :percent="item.ratio" :show-text="false" :stroke-width="8" />
      </div>
    </template>
    <a-empty v-else />
  </a-spin>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import useLoading from '@/hooks/loading';
import { getChartInfo } from "@/api/dashboard/api";
import { getTimeObject } from "@/utils/charts";

// 定义组件属性
const props = defineProps({
  public: {
    type: Object,
    default: () => ({})
  },
  configType: {
    type: Number,
    default: 1
  }
});

// 颜色列表
const colorList = [
  "rgb(var(--red-7))",
  "rgb(var(--orange-7))",
  "rgb(var(--orange-6))",
  "rgb(var(--orange-5))",
  "rgb(var(--orange-4))",
  "rgb(var(--orange-3))"
];

// 加载状态
const { loading, setLoading } = useLoading(false);

// 图表数据
const chartInfo = ref({
  cardName: '',
  rankingList: []
});

const timeOptions = ref<any>({});

// 时间改变处理函数
const timeBarChange = async (timer: any) => {
  timeOptions.value = timer;
  await fetchData();
}

// 获取图表数据
const fetchData = async () => {
  if(!props.public.cardKey || !props.public.dashboardConfigId){
    return;
  }
  try {
    setLoading(true);
    const params = {
      configId: props.public.dashboardConfigId,
      cardKey: props.public.cardKey,
      ...getTimeObject(timeOptions.value)
    }
    const res = await getChartInfo(params);
    if (res.code === 200 && res.data) {
      console.log(chartInfo.value)
      chartInfo.value = res.data;
    } else {
      chartInfo.value = {
        cardName: '',
        rankingList: []
      }
    }
  } catch (error) {
    console.error('获取图表数据时出错:', error);
  } finally {
    setLoading(false);
  }
}

// 初次挂载时获取数据
onMounted(() => {
  fetchData();
});

defineExpose({
  handleTime: (val)=>{
    timeBarChange(val);
  },
})

</script>

<style scoped lang="less">
.ranking-box {
  padding: 5px;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  scrollbar-width: none; /* 适用于 Firefox */
  -ms-overflow-style: none; /* 适用于 Internet Explorer 和 Edge */

  .ranking-list {
    padding: 10px 0;

    .header {
      display: grid;
      grid-template-columns: 5% 65% 30%;
      padding-bottom: 5px;
      color: #FFFFFF;
      span:last-child {
        text-align: right;
      }
    }

    .tag {
      display: inline-block;
      color: #FFF;
      font-size: 12px;
      width: 17px;
      height: 17px;
      text-align: center;
      line-height: 17px;
      border-radius: 2px;
    }
  }

  &::-webkit-scrollbar {
    display: none; /* 适用于 Chrome, Safari 和 Opera */
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
