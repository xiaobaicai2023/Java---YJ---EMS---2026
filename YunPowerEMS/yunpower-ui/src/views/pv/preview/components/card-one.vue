<template>
  <a-spin :loading="loading" style="width: 100%">
    <a-card class="general-card" hoverable style="height: 400px;">
      <template #cover>
        <div>
          <img v-if="renderData.picUrl && renderData.picUrl.length >0" :style="{ width: '100%',height:'200px' }" alt="dessert"
            :src="renderData.picUrl" />
            <img v-else :style="{ width: '100%',height:'200px' }" alt="dessert"
            src="@/assets/icons/pv.jpg" />
        </div>
      </template>
      <a-descriptions style="margin-top: 40px;" :column="1" :label-style="{
        textAlign: 'right',
        paddingRight: '10px',
        color: 'rgb(var(--gray-8))',
      }">
        <a-descriptions-item :label="`${$t('pv.preview.stationName')}：`">
          {{ renderData.stationName }}
        </a-descriptions-item>
        <a-descriptions-item :label="`${$t('pv.preview.stationAddress')}：`">
          {{ renderData.address || '--' }}
        </a-descriptions-item>
        <a-descriptions-item :label="`${$t('pv.preview.stationType')}：`">
          {{ renderData.pvType }}
        </a-descriptions-item>
        <a-descriptions-item :label="`${$t('pv.preview.buildTime')}：`">
          {{ renderData.buildDate }}
        </a-descriptions-item>
      </a-descriptions>
    </a-card>
  </a-spin>
</template>
<script lang="ts" setup>
import { getStationInfo } from '@/api/system/home-pv';
import useLoading from '@/hooks/loading';
import { onMounted } from 'vue';
import { ref } from 'vue';

//加载中
const { loading, setLoading } = useLoading(false)
//页面数据
const renderData = ref<any>({});

/**
 * 电站概览【电站】基础数据
 */
const fetchData = async () => {
  setLoading(true)
  try {
    let res = await getStationInfo({});
    if (res.code == 200) {
      renderData.value = res.data;
    }
  } catch (e) {
  } finally {
    setLoading(false)
  }
}

/**
 * 首次加载
 */
onMounted(async () => {
  await fetchData();
})
</script>

<style lang="less" scoped></style>
