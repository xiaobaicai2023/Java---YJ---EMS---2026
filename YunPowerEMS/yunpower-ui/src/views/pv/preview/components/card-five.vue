<template>
  <a-card title="发电历史" class="home-statistics-box">
    <template #extra>
      <time-bar @change="timeChange" />
    </template>
    <a-spin :loading="loading" style="width: 100%" :tip="$t('global.loading')">
      <div style="text-align: center;">
        <a-space>
          <template #split>
            <a-divider direction="vertical" style="margin: 0 100px" />
          </template>
          <a-statistic :title="optionsData.timeUnit == 0 ? $t('pv.preview.day') : optionsData.timeUnit == 1 ? $t('pv.preview.month') : $t('pv.preview.year')"
            :value="renderData['当日发电量']" :precision="2" :value-from="0">
            <template #suffix>kWh</template>
          </a-statistic>
          <a-statistic v-if="optionsData.timeUnit == 0" :title="$t('pv.preview.exposure')" :value="renderData['当日幅照量']" :precision="2"
            :value-from="0">
            <template #suffix>kWh/m²</template>
          </a-statistic>
          <a-statistic :value="renderData['满发电小时']" :precision="2" :value-from="0">
            <template #title>
              {{$t('pv.preview.full')}}<icon-question-circle @click="handleClickInfo" />
            </template>
            <template #suffix>h</template>
          </a-statistic>
        </a-space>
      </div>
      <div class="chart-box">
        <a-row style="height: 100%;" justify="start" align="center">
          <a-col :span="20" style="height: 100%;">
            <CutomChart :options="chartData" />
          </a-col>
          <a-col :span="4" style="text-align: left;">
            <a-space direction="vertical" fill>
              <div class="card-statistic">
                <a-space>
                  <img alt="节约标准煤" src="@/assets/icons/history/1.png" :width="32" />
                  <a-statistic :title="$t('pv.preview.standard')" :value="chartCardData['节约标准煤']" :precision="2">
                    <template #suffix> 吨</template>
                  </a-statistic>
                </a-space>
              </div>
              <div class="card-statistic">
                <a-space>
                  <img alt="等效植树量" src="@/assets/icons/history/2.png" :width="32" />
                  <a-statistic :title="$t('pv.preview.equivalent')" :value="chartCardData['等效植树量']" :precision="2">
                    <template #suffix> {{ $t('pv.preview.unit') }}</template>
                  </a-statistic>
                </a-space>
              </div>
              <div class="card-statistic">
                <a-space>
                  <img alt="CO₂减排量" src="@/assets/icons/history/3.png" :width="32" />
                  <a-statistic :title="`CO₂${$t('pv.preview.reduce')}`" :value="chartCardData['CO₂减排量']" :precision="2">
                    <template #suffix> 吨</template>
                  </a-statistic>
                </a-space>
              </div>
            </a-space>
          </a-col>
        </a-row>
      </div>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { PowerModuleEunm, getChart } from "@/api/system/home-power";
import { getHistoryHeadInfo, getHistoryRightInfo } from "@/api/system/home-pv";
import useLoading from "@/hooks/loading";
import { handleChartData } from "@/utils/charts";
import Modal from "@arco-design/web-vue/es/modal";
import { onMounted, ref } from "vue";

//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>({});
const totalElectric = ref<any>(0);
//右侧内容
const chartCardData = ref<any>({});
const optionsData = ref<any>({ timeUnit: 0 });
// 占电总能
const renderData = ref<any>({
  "满发电小时": 0,
  "当日幅照量": 0,
  "当日发电量": 0
});

/**
 * 时间改变
 */
const timeChange = async (options: any) => {
  console.log("options", options)
  optionsData.value = options;
  if (options.timeUnit == 0) {
    await fetchData({ pageValue: PowerModuleEunm.pv_power_history_day, "dayTime": options.timer });
  } else if (options.timeUnit == 1) {
    await fetchData({ pageValue: PowerModuleEunm.pv_power_history_month, "monthTime": options.timer });
  } else {
    await fetchData({ pageValue: PowerModuleEunm.pv_power_history_year, "yearTime": options.timer });
  }
  await fetchHistoryHeadData();
  await fetchHistoryRightData();
}

/**
 * 获取发电历史-头部
 * @param param 时间参数
 */
const fetchHistoryHeadData = async () => {
  try {
    let res = await getHistoryHeadInfo(totalElectric.value);
    if (res.code == 200) {
      renderData.value = res.data;
    }
  } catch (ex) {
    console.error("发电历史耗统计出错", ex)
  } finally {

  }
}
/**
 * 获取发电历史-右侧
 * @param param 时间参数
 */
const fetchHistoryRightData = async () => {
  try {
    let res = await getHistoryRightInfo(totalElectric.value);
    if (res.code == 200) {
      chartCardData.value = res.data;
    }
  } catch (ex) {
    console.error("发电历史耗统计-右侧出错", ex)
  } finally {

  }
}

/**
 * 获取日期数据
 * @param param 时间参数
 */
const fetchData = async (param: any) => {
  setLoading(true);
  try {
    let res = await getChart(param);
    if (res.code == 200) {
      totalElectric.value = res.data.totalElectric || 0;
      chartData.value = handleChartData(res.data);
      console.log("发电历史耗统计", param.pageValue, chartData.value)
    }
  } catch (ex) {
    console.error("发电历史耗统计出错", ex)
  } finally {
    setLoading(false);
  }
}

/**
 *
 */
const handleClickInfo = () => {
  Modal.info({
    title: '满发电小时',
    content: '满发小时是衡量一座光伏电站发电能力的重要指标之一，日满发小时则表示日发电能力。计算公式：满发小时=发电量/装机容量。例如日满发小时为4.5h，表示此电站以额定功率工作了4.5个小时'
  });
}
</script>
<style scoped lang="less">
:deep(.arco-statistic) {
  .arco-statistic-title {
    margin-bottom: 0px;
    font-size: 14px;
    font-weight: bold;
    white-space: nowrap;
  }

  .arco-statistic-content {
    margin-top: 0px;
  }
}

.chart-box {
  margin-top: 15px;
  height: 400px;
}


.card-statistic {
  // background-color: var(--color-fill-2);
  padding: 5px;

  :deep(.arco-statistic-value) {
    font-size: 18px;
  }
}
</style>
