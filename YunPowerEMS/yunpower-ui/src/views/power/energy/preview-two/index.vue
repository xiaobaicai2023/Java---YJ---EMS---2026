<!--
 * 功能：能耗概括(新)
 * 作者：罗甜甜
 * 日期：2024-04-27
-->

<template>
  <div class="container">
    <!-- 第一排 -->
    <div class="electricity-box">
      <!-- 能耗概括 -->
      <Energyconsumption />
    </div>

    <!-- 第二排 -->
    <a-row class="grid-box" :gutter="15" type="flex" justify="center" align="top">
      <a-col :span="18">
        <!-- 峰谷平用电能耗统计 -->
        <ChartOne />
      </a-col>
      <a-col :span="6">
        <!-- 事件警告 -->
        <EventWarnings></EventWarnings>
      </a-col>
    </a-row>

    <!-- 第三排 -->
    <a-row class="grid-box" :gutter="15">
      <a-col :span="12">
        <!-- 用电能耗统计 -->
        <chartTwo />
      </a-col>
      <a-col :span="12">
        <!-- 平均功率因素监控  -->
        <chartThree />
      </a-col>
    </a-row>

    <!-- 第四排 -->
    <a-row class="grid-box" :gutter="15">
      <a-col :span="12">
        <!-- 平均功率因素监控  new -->
        <CardSeven />
      </a-col>
      <a-col :span="12">
        <!-- 用户容量负荷需量监控  new -->
        <CardSeven />
      </a-col>
    </a-row>

    <!-- 第五排 -->
    <a-row class="grid-box" :gutter="15">
      <a-col :span="12">
        <!-- 有功功率监控 -->
        <ChartSix />
      </a-col>
      <a-col :span="12">
        <!-- 平均功率因素监控 -->
        <ChartSeven />
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { watchEffect } from 'vue'
import { useCompanyStore } from '@/store'
import { storeToRefs } from "pinia";
//今日用电
// import CardOne from './components/card-one.vue';
// 用电统计
// import CardTwo from './components/card-two.vue';
// 能耗概括
import Energyconsumption from './components/energy-consumption.vue';
//历史用电量详细
import CardThree from './components/card-three.vue';
//用电容量负荷需量监控
import CardSeven from './components/card-seven.vue';
//分组能耗占比
import CardNine from './components/card-nine.vue';

//峰谷平用电能耗统计
import ChartOne from './components/chart-one.vue';
// 事件警告
import EventWarnings from './components/event-warnings.vue';
//用电能耗统计
import chartTwo from './components/chart-two.vue';
// 负荷和负载率
import chartThree from './components/chart-three.vue';


//有功功率监控
import ChartSix from './components/chart-six.vue';
//平均功率因素监控
import ChartSeven from './components/chart-seven.vue';

/*************************** 变量区域 begin ***************************/
    // 全局下拉框
const store = useCompanyStore()
const { companyValue } = storeToRefs(store)
watchEffect(() => {
  console.log(companyValue.value)
})
/*************************** 变量区域 end ***************************/

</script>

<style lang="less" scoped>
.container {
  width: 100%;
  height: 100%;
}

.electricity-box {
  /* display: flex;
align-items: center;
justify-content: space-between; */
  color: #FFF;
  /* height: 220px; */
}


/* //历史用电量详细 */
.history-battery-box {
  display: flex;
  margin: 15px 0;
  width: 100%;
  height: 460px;

  .calendar-box {
    margin-right: 15px;
    width: 52%;
    min-width: 550px;
  }
}

.grid-box {
  margin-top: 15px;
}

.arco-card {
  border-radius: 5px;
}

/* 设置card 高度（防止echarts 塌陷） */
:deep(.history-battery-box) {
  .arco-card-body {
    width: 100%;
    height: calc(100% - 46px);
  }
}
</style>
