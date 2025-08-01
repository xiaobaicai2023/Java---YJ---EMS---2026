<!--
* 功能：峰谷分析
* 作者：曹晓桐
* 日期：2023-11-18
-->

<template>
  <div>
    <a-row :gutter="15">

      <!-- 左侧树 start -->
      <a-col :span="5">
        <search-tree :title="$t('global.device')" :time="true" :pickType="1" @onChange="searchTreeChange" />
      </a-col>
      <!-- 左侧树 end -->

      <!-- 右侧内容 start -->
      <a-col :span="19">
        <a-row :gutter="[15,15]">
          <a-col>
            <a-card class="general-card" :title="$t('power.energy.analysis.standard')">
              <Chart :options="chartData" autoresize height="260px"></Chart>
            </a-card>
          </a-col>

          <a-col>
            <div v-if="chartDataTwoLength > 0">
              <a-row :gutter="[15,15]">
                <a-col v-for="(item, index) in chartDataTwo" :key="index">
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
          <!-- <a-col>
            <a-card class="general-card" title="用电量分时段环比">
              <a-spin :loading="loading" style="width: 100%;height: 460px;" :tip="$t('global.loading')">
                <CutomChart v-if="!loading" :options="chartDataTwo" height="460px" />
              </a-spin>
            </a-card>
            <a-card class="general-card">
              <div v-if="chartDataTwoLength > 0" v-for="(item, index) in chartDataTwo" :key="index">
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
      </a-col>
      <!-- 右侧内容 end -->
    </a-row>
  </div>
</template>

<script setup lang="ts">
  import { computed, onMounted, reactive, ref } from "vue";
  import useLoading from "@/hooks/loading";
  import { getSeasonalRangeList, getPageValueList, getChartByDoubleDate } from "@/api/system/home-power";
  import { handleSeasonalRangeChart, handleStackChart } from "@/utils/charts";

  //加载中
  const { loading, setLoading } = useLoading(false);
  //图表数据
  const chartData = ref < any > ([]);
  //图表数据
  const chartDataTwo = ref < any > ([]);
  //模块
  const pageValues = ref < any > ([]);
  //树-搜索条件
  const searchParams = ref < any > ({});
  const chartDataTwoLength = computed(() => {
    return chartDataTwo.value.filter((item: any) => item.value !== null).length;
  });
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
  const fetchOneData = async () => {
    try {
      let res = await getSeasonalRangeList({});
      if (res.code == 200) {
        if (res.data.length) {

        }
        let options = handleSeasonalRangeChart(res.data);
        chartData.value = options;
      }
    } catch (ex) {
      console.error("分时段标准出错", ex)
    } finally {
    }
  }

  /**
   * 获取图表类型
   * @param param 时间参数
   */
  const fetchData = async () => {
    try {
      let res = await getPageValueList('ppfv-analysis');
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
        chartDataTwo.value = chartTempData;
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
          chartDataTwo.value[index].loading = true;
          let param = {
            pageValue: item,
            ...searchParams.value
          };
          let chartsRes = await getChartByDoubleDate(param);
          if (chartsRes.code == 200 && chartsRes.data.xAxis && chartsRes.data.xAxis.length > 0) {
            chartDataTwo.value[index] = {
              title: chartsRes.data.title,
              value: handleStackChart(chartsRes.data),
              loading: false
            };
          } else {
            chartDataTwo.value[index].loading = false;
            chartDataTwo.value[index].value = null;
          }
        });

      }
    } catch (error) {
    } finally {
      setLoading(false)
    }
  }

  onMounted(async () => {
    await fetchOneData();
  })

</script>

<style scoped>
  .title {
    font-size: 16px;
  }
</style>
