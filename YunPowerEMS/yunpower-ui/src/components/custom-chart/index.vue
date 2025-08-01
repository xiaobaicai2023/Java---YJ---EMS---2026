<template>
  <VCharts
      v-if="shouldRenderChart"
      ref="myChartRef"
      :option="chartOptions"
      :autoresize="autoResize"
      :style="{ width, height }"
      :theme="theme"/>
  <a-empty :style="{ height }" v-else/>
</template>

<script lang="ts" setup>
import {computed, nextTick, provide, ref} from 'vue'
import VCharts, {THEME_KEY,UPDATE_OPTIONS_KEY} from 'vue-echarts'
import * as echarts from 'echarts'
import {useAppStore} from '@/store'

const props = defineProps({
  // 图表的配置选项
  options: {
    type: Object,
    default() {
      return {}
    },
  },
  // 是否自动调整大小
  autoResize: {
    type: Boolean,
    default: true,
  },
  // 图表的宽度
  width: {
    type: String,
    default: '100%',
  },
  // 图表的高度
  height: {
    type: String,
    default: '100%',
  },
})
// 支持深色模式
const appStore = useAppStore();
const theme = computed(() => {
  if (appStore.theme === 'dark') return 'dark';
  return '';
});
/**
 * 配置 VCharts 的主题
 */
provide(THEME_KEY, theme)
// 是否渲染图表
const renderChart = ref(false);

const myChartRef = ref<any>(null);
nextTick(() => {
  renderChart.value = true
})

/**
 * 计算属性：检查是否应该渲染图表
 */
const shouldRenderChart = computed(() => {
  // renderChart 为 true 且 options 对象中存在 series 并且长度大于0
  return renderChart.value && props.options.series && props.options.series.length > 0;
});

/**
 * 计算属性：获取带有修改后背景色的图表配置
 */
const chartOptions = computed(() => {
  return {
    ...props.options, // 保留原有的 options 属性
    backgroundColor: 'rgba(128, 128, 128, 0)' // 修改背景色为透明
  };
});


/**
 * echarts图表-保存图片
 */
const downloadEchartsImg = (name:string) => {
  const $a = document.createElement('a');
  $a.download = name + '.png';
  $a.target = '_blank';
  $a.href = myChartRef.value.getConnectedDataURL(myChartRef.value.getDom());
  // Chrome and Firefox
  if (typeof MouseEvent === 'function') {
    const evt = new MouseEvent('click', {
      view: window,
      bubbles: true,
      cancelable: false
    });
    $a.dispatchEvent(evt);
  }
}

defineExpose({
  downloadEchartsImg
})
</script>

<style scoped lang="less">
.arco-empty {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
</style>
