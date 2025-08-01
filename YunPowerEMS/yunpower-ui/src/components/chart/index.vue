<template>
  <VCharts ref="myChartRef" :option="{ ...options, backgroundColor: 'rgba(128, 128, 128, 0)' }" :autoresize="autoResize"
    :style="{ width, height }" :theme="theme" />
</template>

<script lang="ts" setup>
import { computed, nextTick, provide, ref } from 'vue'
import VCharts, { THEME_KEY } from 'vue-echarts'
import { useAppStore } from '@/store'
import {useEventListener} from "@vueuse/core";

defineProps({
  options: {
    type: Object,
    default() {
      return {}
    },
  },
  autoResize: {
    type: Boolean,
    default: true,
  },
  width: {
    type: String,
    default: '100%',
  },
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
provide(THEME_KEY, theme)


const renderChart = ref(false)
const myChartRef = ref<any>(null);
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
// wait container expand
nextTick(() => {
  renderChart.value = true
})
defineExpose({
  downloadEchartsImg
})
</script>

<style scoped lang="less">
</style>
