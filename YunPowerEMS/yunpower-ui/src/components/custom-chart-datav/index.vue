<template>
  <VCharts :option="options" :autoresize="autoResize" :style="{ width, height }" :theme="theme"/>
</template>

<script lang="ts" setup>
import {computed, nextTick, provide, ref} from 'vue'
import VCharts, {THEME_KEY} from 'vue-echarts'
import {useAppStore} from '@/store'

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
// wait container expand
nextTick(() => {
  renderChart.value = true
})
</script>

<style scoped lang="less">
</style>
