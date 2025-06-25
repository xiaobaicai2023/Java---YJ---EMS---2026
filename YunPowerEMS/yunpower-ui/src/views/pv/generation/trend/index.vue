<!--
* 功能：发电趋势
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
        <a-card class="general-card" v-if="chartData.length > 0">
          <a-spin :loading="loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
            <a-card v-for="(item, index) in chartData" :key="index" :title="item.title" class="general-card" style="margin-bottom: 16px;">
              <CutomChart :options="item.value" autoresize height="600px"></CutomChart>
            </a-card>
          </a-spin>
        </a-card>
        <custom-empty v-else />
      </a-col>
      <!-- 右侧内容 end -->
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import useLoading from "@/hooks/loading";
import { getChartBySingleDate, getPageValueList } from "@/api/system/home-power";
import { handleChartData } from "@/utils/charts";
import { StationTypeEnum } from "@/api/system/device";


//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>([]);

//模块
const pageValues = ref<any>([]);


const searchParams = ref<any>({});
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
    let res = await getPageValueList('electric-power-trend');
    console.log("图表种类", res.data)
    if (res.code == 200 && res.data) {
      let data = [];
      for (const [key, value] of Object.entries(res.data)) {
        console.log(`${key}: ${value}`);
        data.push(key);
      }
      if (data.length > 0) {
        pageValues.value = data;
        await fetchChartData();
      }
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
  setLoading(true);
  try {
    if (pageValues.value && pageValues.value.length > 0) {
      let data: any = [];
      const promises = pageValues.value.map(async (item: any, index: number) => {
        let param = {
          pageValue: item,
          ...searchParams.value
        }
        let chartsRes = await getChartBySingleDate(param);
        console.log(`图表种类${index}的数据：`, chartsRes.data)
        if (chartsRes.code == 200 && chartsRes.data.xAxis && chartsRes.data.xAxis.length > 0) {
          if (param.pageValue.indexOf("ppfv") != -1) {
            chartsRes.data.type = 'stack';
          }
          data.push({
            title: chartsRes.data.title,
            value: handleChartData(chartsRes.data)
          });
        }
      });
      await Promise.all(promises);
      if (data.length > 0) {
        chartData.value = data;
      } else {
        chartData.value = [];
      }
    }
  } catch (error) {

  } finally {
    setLoading(false);
  }
}

</script>

