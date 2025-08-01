<template>
  <div class="container">

    <!-- 第一排 -->
    <div class="electricity-box">
      <!-- 今日用电 -->
      <CardOne />
      <!-- 用电总计 -->
      <CardTwo />
    </div>

    <!-- 第二排 -->
    <div class="history-battery-box">
      <!-- 历史用电量详细 -->
      <CardThree />
      <!-- 用电能耗统计 -->
      <CardFour />
    </div>

    <!-- 第三排 -->
    <div class="home-statistics-box">
      <!-- 峰谷平用电能耗统计 -->
      <CardFive />
    </div>

    <!-- 第四排 -->
    <a-row class="grid-box" :gutter="15">
      <a-col :span="12">
        <!-- 平均功率因素监控 -->
        <CardSix />
      </a-col>
      <a-col :span="12">
        <!-- 用户容量负荷需量监控 -->
        <CardSeven />
      </a-col>
    </a-row>

    <!-- 第四排 -->
    <a-row class="grid-box" :gutter="15">
      <a-col :span="12">
        <!-- 有功功率监控 -->
        <CardEight />
      </a-col>
      <a-col :span="12">
        <!-- 分组能耗占比 -->
        <CardNine />
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { watchEffect } from 'vue'
import { useCompanyStore } from '@/store'
import { storeToRefs } from "pinia";
//今日用电
import CardOne from './components/card-one.vue';
//用电统计
import CardTwo from './components/card-two.vue';
//历史用电量详细
import CardThree from './components/card-three.vue';
//用电能耗统计
import CardFour from './components/card-four.vue';
//峰谷平用电能耗统计
import CardFive from './components/card-five.vue';
//平均功率因素监控
import CardSix from './components/card-six.vue';
//用电容量负荷需量监控
import CardSeven from './components/card-seven.vue';
//有功功率监控
import CardEight from './components/card-eight.vue';
//分组能耗占比
import CardNine from './components/card-nine.vue';
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
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #FFF;
  height: 160px;
}


//历史用电量详细
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

  .energy-box {
    flex: 1;
    //width: 48%;
    //min-width: 450px;
  }
}

.grid-box {
  margin-top: 15px;
}

.arco-card {
  border-radius: 5px;
}

//设置card 高度（防止echarts 塌陷）
:deep(.history-battery-box) {
  .arco-card-body {
    width: 100%;
    height: calc(100% - 46px);
  }
}
</style>
