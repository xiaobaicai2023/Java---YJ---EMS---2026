<template>
  <a-card :loading="loading" :tip="$t('global.loading')" class="contrast-box" :bordered="false" :body-style="{ padding: '40px 16px 16px 16px' }">
      <template class="content-box">
        <div class="my-statistic" v-for="(item, index) in eleUseTotalList" :key="index">
          <div class="title">
            <span>{{ item.title }}</span>/kWh
          </div>
          <div class="value">
            {{ item.value }}
          </div>
          <!-- <div class="extra">
        {{ !item.count ? `开始于 ${item.time}` : `环比 ${item.count}%` }}
        <icon-arrow-fall v-if="item.count as number < 0" class="icon-fall" />
        <icon-arrow-rise v-if="item.count as number > 0" class="icon-rise" />
      </div> -->
        </div>
      </template>
    </a-card>
  </template>
<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { PowerModuleEunm, getChart } from '@/api/system/home-power';
import useLoading from '@/hooks/loading';
//加载中
const { loading, setLoading } = useLoading(false);
const eleUseTotalList = ref<any>([
  {
    title: '昨日用电量',
    value: 0,
    count: 0,
    enum: PowerModuleEunm.yesterday_electric_use
  }, {
    title: '本月用电量',
    value: 0,
    count: 0,
    enum: PowerModuleEunm.month_electric_use
  }, {
    title: '今年用电量',
    value: 0,
    count: 0,
    enum: PowerModuleEunm.year_electric_use
  }, {
    title: '总电量',
    value: 0,
    time: 0,
    enum: PowerModuleEunm.total_electric_use
  }
])

/**
 * 用电统计
 */
const getTotalEleUse = async () => {
  setLoading(true)
  try {

    eleUseTotalList.value.forEach(async (item: any) => {
      let res = await getChart({ pageValue: item.enum });
      if (res.code == 200) {
        if (res.data.yAxis && res.data.yAxis.length == 1 && res.data.yAxis[0].dataList && res.data.yAxis[0].dataList.length == 1) {
          item.value = res.data.yAxis[0].dataList[0].toString()
          console.log("用电量统计", item.enum, item.value);
        }
      }
    });
  } catch (ex) {
    console.error("用电统计出错", ex)
  } finally {
    setLoading(false)
  }
}

onMounted(async () => {
  //今日用电量统计
  await getTotalEleUse();
})

</script>
<style  lang="less" scoped>
.contrast-box {
  margin-left: 15px;
  flex: 1;
  height: 100%;

  .content-box {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .my-statistic {
      .title {
        span {
          color: var(--color-text-1);
          font-weight: 600;
          margin-right: 3px;
        }
      }

      .value {
        padding: 15px 0;
        font-size: 36px;
        font-weight: 600;
        color: var(--color-text-1);
      }

      .arco-icon-arrow-rise {
        color: rgb(var(--green-6));
      }

      .arco-icon-arrow-fall {
        color: rgb(var(--red-6));
      }
    }
  }
}
</style>
