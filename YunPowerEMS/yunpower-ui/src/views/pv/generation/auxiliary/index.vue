<!--
* 功能：辅助分析
* 作者：曹晓桐
* 日期：2023-11-18
-->
<template>
  <div>
    <a-row :gutter="10">

      <!-- 左侧树 start -->
      <a-col :span="4">
        <!--  :listStyle="2" -->
        <search-tree :title="$t('global.device')" :time="true" @onChange="searchTreeChange"  :stationType="StationTypeEnum.pv"  />
      </a-col>
      <!-- 左侧树 end -->

      <!-- 右侧内容 start -->
      <a-col :span="20">{{ chartDataTitle.title }}
        <a-card class="general-card" :title="chartDataTitle">
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
import { getAuxiliaryAnalysis } from "@/api/system/home-power";
import { handleChartData } from "@/utils/charts";
import { StationTypeEnum } from "@/api/system/device";

//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>([]);
const chartDataTitle = ref<any>('');
//tab切换选中值
const tabsActiveKey = ref<number>(0);
//搜索参数
const searchParams = ref<any>({});
/**
 * 搜索树Change
 */
const searchTreeChange = async (val: any) => {
  searchParams.value = val;
  await fetchChartData();
}

/**
 * 获取图表数据
 */
const fetchChartData = async () => {
  setLoading(true);
  try {
    let param = { ...searchParams.value }
    let chartsRes = await getAuxiliaryAnalysis(param);
    if (chartsRes.code == 200) {
      chartDataTitle.value = chartsRes.data.title || '';
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

