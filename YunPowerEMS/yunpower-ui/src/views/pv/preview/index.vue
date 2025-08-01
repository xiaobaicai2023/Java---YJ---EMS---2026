<!--
* 功能：电站概览
* 作者：曹晓桐
* 日期：2023-12-7
-->
<template>
  <div class="container">
    <grid-component/>
    <Dashboard />
  </div>
</template>
<script lang="ts" setup>

import Dashboard from '@/views/dashboard/preview/index.vue'
import { onMounted } from 'vue';
import GridComponent from "@/views/pv/preview/gridComponent.vue";



onMounted(() => {

})
</script>

<style lang="less" scoped>
.container {
  width: 100%;
  height: 100%;
}
</style>
