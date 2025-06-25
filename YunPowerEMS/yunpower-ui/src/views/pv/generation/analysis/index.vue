<!--
* 功能：历史数据
* 作者：曹晓桐
* 日期：2023-11-18
-->
<template>
  <div>
    <a-row :gutter="10">

      <!-- 左侧树 start -->
      <a-col :span="4">
        <!--  :listStyle="1" -->
        <search-tree :title="$t('global.device')" :time="true" @onChange="searchTreeChange" :stationType="StationTypeEnum.pv" />
      </a-col>
      <!-- 左侧树 end -->

      <!-- 右侧内容 start -->
      <a-col :span="20">
        <a-card class="general-card" :title="$t('power.energy.search.queryData')">
          <!-- tab切换 -->
          <a-tabs type="rounded" v-model:active-key="tabsActiveKey" @change="handleTabsChange">
            <a-tab-pane key="0" :title="$t('pv.preview.generation.analysis')"></a-tab-pane>
            <a-tab-pane key="1" :title="$t('pv.preview.generation.analysisDc')"></a-tab-pane>
            <a-tab-pane key="2" :title="$t('pv.preview.generation.analysisDcVoltage')"></a-tab-pane>
<!--            <a-tab-pane v-for="(item, index) in pageValues" :key="index" :title="item.title">-->
<!--            </a-tab-pane>-->
          </a-tabs>
          <a-spin :loading="loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
            <CutomChart :options="chartData" autoresize height="460px"></CutomChart>
          </a-spin>
        </a-card>
      </a-col>
      <!-- 右侧内容 end -->
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import useLoading from "@/hooks/loading";
import { getChartBySingleDate } from "@/api/system/home-power";
import { handleChartData } from "@/utils/charts";
import { StationTypeEnum } from "@/api/system/device";

//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>([]);
//tab切换选中值
const tabsActiveKey = ref<number>(0);
//模块
const pageValues = ref<any>([{
  title: "交流分析",
  value: "photovoltaic-exchange"
}, {
  title: "直流电流分析",
  value: "photovoltaic-direct-current"
}, {
  title: "直流电压分析",
  value: "photovoltaic-direct-voltage"
}]);


const searchParams = ref<any>({});
/**
 * 搜索树Change
 */
const searchTreeChange = async (val: any) => {
  searchParams.value = val;
  await fetchChartData();
}

/**
 * tab切换
 */
const handleTabsChange = async () => {
  await fetchChartData();
}
/**
 * 获取图表数据
 */
const fetchChartData = async () => {
  setLoading(true);
  try {
    let param = {
      pageValue: pageValues.value[tabsActiveKey.value].value,
      ...searchParams.value
    }
    let chartsRes = await getChartBySingleDate(param);
    if (chartsRes.code == 200) {
      chartData.value = handleChartData(chartsRes.data);
    } else {
      chartData.value = [];
    }
  } catch (error) {

  } finally {
    setLoading(false)
  }
}

</script>

