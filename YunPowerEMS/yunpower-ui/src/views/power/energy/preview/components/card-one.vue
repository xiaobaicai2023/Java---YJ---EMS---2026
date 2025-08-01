<template>
  <a-spin class="today-box" :loading="loading" :tip="$t('global.loading')">
    <!-- <span class="today-title">用电量统计</span> -->
    <div class="today-center-box">
      <span class="today-title">今日用电量 kWh</span>
      <div class="today-num-box">
        <div v-for="(item, index) in todayEleUseInfo.value" :key="index">
          {{ item }}
        </div>
      </div>
    </div>
    <div class="today-timer">
      <a-space size="medium">
        <span>截止至：{{ todayEleUseInfo.time }}</span>
        <span @click.stop="getTodayEleUse" style="cursor: pointer"><icon-refresh /> 刷新</span>
      </a-space>
    </div>
  </a-spin>
</template>
<script lang="ts" setup>
import { onMounted, onUnmounted, ref } from 'vue'
import dayjs from "dayjs";
import { PowerModuleEunm, getChart } from '@/api/system/home-power';
import { useIntervalFn } from '@vueuse/core';

const loading = ref<boolean>(false);
const todayEleUseInfo: any = ref<any>({
  value: "0",
  time: dayjs().format("YYYY-MM-DD HH:mm")
});

/**
 * 今日用电量
 */
const getTodayEleUse = async () => {
  try {
    loading.value = true;
    var dateNow = dayjs().format("YYYY-MM-DD HH:mm");
    let res = await getChart({ pageValue: PowerModuleEunm.today_electric_use, "dayTime": dateNow });
    if (res.code == 200) {
      todayEleUseInfo.value = {
        value: res.data.yAxis.length > 0 && res.data.yAxis[0].dataList.length > 0 ? res.data.yAxis[0].dataList[0].toString() : '0',
        time: dateNow
      }
      console.log("用电量统计", PowerModuleEunm.today_electric_use, todayEleUseInfo.value)
    }
  } catch (e) {
    console.error("用电量统计出错", e)
  } finally {
    loading.value = false;
  }

}

/**
 * 5分钟定时-今日用电量统计
 *
 */
const { resume: startInterval, pause: stopInterval } = useIntervalFn(async () => {
  console.log('5分钟定时-今日用电量统计')
  await getTodayEleUse()
}, 1000 * 60 * 5)

/**
 * 组件加载完成
 */
onMounted(async () => {
  await getTodayEleUse();
  startInterval();
})

/**
 * 组件卸载
 */
onUnmounted(() => {
  stopInterval()
})
</script>
<style  lang="less" scoped>
.today-box {
  height: 100%;
  padding: 0 16px;
  min-width: 400px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  background: linear-gradient(to bottom right, rgb(var(--primary-6)), rgb(var(--primary-4)));
  border-radius: 5px;

  .today-center-box {
    margin-top: 5px;

    .today-num-box {
      padding: 10px 0 15px 0;

      div {
        min-width: 34px;
        padding: 10px 2px;
        font-size: 40px;
        font-weight: 600;
        margin-right: 10px;
        text-align: center;
        display: inline-block;
        background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFQAAACQCAYAAACWEkxjAAAAAXNSR0IArs4c6QAAAERlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAA6ABAAMAAAABAAEAAKACAAQAAAABAAAAVKADAAQAAAABAAAAkAAAAACr/YNbAAADpklEQVR4Ae3cvU4UURjG8ffM8BFlF6MJMRo/Gk2Ihb2lnVfALXhNegWGwtrOwsLewpBggxiNwcToLhrAYeQlWbKwQljyr5z/NDsf5zzJ/nhm50xDiTO21batH8Wnud1YmJuLuq6jVGcM/28vNdHu70bTzMX27tu4tbtSSnPaly3/urDartYP4/HlxZif/9f1rp/7GTs77+L1r5WyMgE7Abrers9fj3u9YQwmrnUdcvz796Lffo0Pw/vl/s74+WNom217qY7BwvgA988WaKK/fbuU36NRR7+J2UwxRyzn/0yztBvNOATN38y8zUcn/ZxOIO3SMGcd3vJr7VbfB9B0iCdH54NquSwNqlwaiXmSZ/rjNEzLstluHjyIrvggmt5wYkYTP7arXLRPXPHEhQTSsso3oAvNdtKEQFpWXX2dnNAATqTl0ToUyDPiQEBQuAaCCgoLwHE2VFBYAI6zoYLCAnCcDRUUFoDjbKigsAAcZ0MFhQXgOBsqKCwAx9lQQWEBOM6GCgoLwHE2VFBYAI6zoYLCAnCcDRUUFoDjbKigsAAcZ0MFhQXgOBsqKCwAx9lQQWEBOM6GCgoLwHE2VFBYAI6zoYLCAnCcDRUUFoDjbKigsAAcZ0MFhQXgOBsqKCwAx9lQQWEBOM6GCgoLwHE2VFBYAI6zoYLCAnCcDRUUFoDjbKigsAAcN7MfcQ3O7HSctzz85xdUUFgAjrOhgsICcJwNFRQWgONsqKCwABxnQwWFBeA4GyooLADH2VBBYQE4zoYKCgvAcTZUUFgAjrOhgsICcJwNFRQWgONsqKCwABxnQwWFBeA4GyooLADH2VBBYQE4zoYKCgvAcTZUUFgAjrOhgsICcJwNFRQWgONsqKCwABxnQwWFBeA4GyooLADH2VBBYQE4zoYKCgvAcTZUUFgAjrOhgsICcJwNFRQWgONsqKCwABxnQwWFBeA4G0qDNrH/B87sbFxaVnvR7HVWAP7iaVnNxnAI53Y2Li2rV3FHUKgCaVky6327dWMx5vtQbidjfsbO4EFZ+nL4lH8TL7/1o3/wz27dLiKQdmmYcw8bmjtr7Vq/Fzdv5L7bdALD+PxluSwPctYRaB5stO3VOgZLue92PoEm+lt3S/k+Gn1sYZ8XUtvbf8Rz+mcapdU4Zo4+Bponsrov4vlG/sjmsdukQNqk0eg2Hx9x7JYfv5D7z9p29kl87O1Frzcb9Wwd1czJMV04zjegXLTnOjOXRk9LOfVl6C8/OaP7fYtUMwAAAABJRU5ErkJggg==");
        background-repeat: no-repeat;
        background-size: 100% 100%;
      }

      div:last-child {
        margin-right: 0;
      }
    }
  }

}
</style>
