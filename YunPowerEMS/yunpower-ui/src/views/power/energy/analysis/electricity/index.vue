<!--
* 用电分析
* 作者：曹晓桐
* 日期：2023-11-21
-->

<template>
  <div>
    <a-row :gutter="15">
      <!-- 左侧树 start -->
      <a-col :span="4">
        <search-tree :title="$t('global.device')" :time="true" :pickType="1" @onChange="searchTreeChange" />
      </a-col>
      <!-- 左侧树 end -->

      <!-- 右侧内容 start -->
      <a-col :span="20">
        <div v-if="chartDataLength > 0">
          <a-row :gutter="[15,15]">
            <a-col v-for="(item, index) in chartData" :key="index">
              <a-card v-if="item.value" :title="item.title" class="general-card">
                <a-spin :loading="item.loading" style="width: 100%;height: 460px;" :tip="$t('global.loading')">
                  <CutomChart :options="item.value" autoresize height="460px"></CutomChart>
                </a-spin>
              </a-card>
            </a-col>
          </a-row>
        </div>
        <div v-else>
          <a-row :gutter="[15,15]">
            <a-col>
              <custom-empty />
            </a-col>
          </a-row>
        </div>
      </a-col>
      <!-- 右侧内容 end -->

      <!-- <a-col :span="20">
        <a-card class="general-card">
          <div v-if="chartDataLength > 0" v-for="(item, index) in chartData" :key="index">
            <a-card v-if="item.value" :title="item.title" class="general-card">
              <a-spin :loading="item.loading" style="width: 100%;height: 460px;" :tip="$t('global.loading')">
                <CutomChart :options="item.value" autoresize height="460px"></CutomChart>
              </a-spin>
            </a-card>
          </div>
          <custom-empty v-else />
        </a-card>
      </a-col> -->
    </a-row>
  </div>
</template>

<script setup lang="ts">
  import { computed, ref } from "vue";
  import useLoading from "@/hooks/loading";
  import { getChartByDoubleDate, getChartBySingleDate, getPageValueList } from "@/api/system/home-power";
  import { handleChartData } from "@/utils/charts";

  //加载中
  const { loading, setLoading } = useLoading(false);
  //图表数据
  const chartData = ref < any > ([]);

  //模块
  const pageValues = ref < any > ([]);

  const chartDataLength = computed(() => {
    return chartData.value.filter((item: any) => item.value !== null).length;
  });
  const searchParams = ref < any > ({});
  /**
   * 搜索树Change
   */
  const searchTreeChange = async (val: any) => {
    searchParams.value = val;
    if (pageValues.value && pageValues.value.length > 0) {
      await fetchChartData();
    } else {
      await fetchData();
    }
  }


  /**
   * 获取图表类型
   * @param param 时间参数
   */
  const fetchData = async () => {
    try {
      let res = await getPageValueList('electricity-analysis');
      if (res.code == 200 && res.data) {
        let data = [];
        let chartTempData: any = [];

        for (const [key, value] of Object.entries(res.data)) {
          data.push(key);
          chartTempData.push({
            title: '',
            value: '',
            loading: false
          });
        }
        pageValues.value = data;
        chartData.value = chartTempData;
        await fetchChartData();
      }
    } catch (ex) {
      console.error("历史数据出错", ex)
    } finally {
    }
  }


  /**
   * 获取图表数据
   */
  const fetchChartData = async () => {
    setLoading(true)
    try {
      if (pageValues.value && pageValues.value.length > 0) {
        pageValues.value.map(async (item: any, index: number) => {
          chartData.value[index].loading = true;
          let param = {
            pageValue: item,
            ...searchParams.value
          }
          let chartsRes = await getChartByDoubleDate(param);
          if (chartsRes.code == 200 && chartsRes.data.xAxis && chartsRes.data.xAxis.length > 0) {
            if (param.pageValue.indexOf("ppfv") != -1) {
              chartsRes.data.type = 'stack';
            }
            chartData.value[index] = {
              title: chartsRes.data.title,
              value: handleChartData(chartsRes.data),
              loading: false
            };
          } else {
            chartData.value[index].loading = false;
            chartData.value[index].value = null;
          }
        });
      }
    } catch (error) {

    } finally {
      setLoading(false)
    }
  }
</script>
<style lang="less" scoped>
  .chart-content {
    width: 100%;
    display: flex;
    padding: 0 8px;
    flex-direction: column;

    .chart-content-list {
      width: 100%;
      margin-top: 20px;
    }
  }
</style>
