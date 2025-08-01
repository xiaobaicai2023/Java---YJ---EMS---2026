<template>
  <a-card class="general-card" :title="$t('power.energy.preview')" :header-style="{ paddingBottom: '12px' }">
    <a-grid :cols="20" :col-gap="12" :row-gap="12">
      <!-- 有功功率 -->
      <a-grid-item :span="{ xs: 5, sm: 5, md: 5, lg: 5, xl: 5, xxl: 3}">
        <EnergyconsumptionItem :title="$t('power.energy.preview.active')" quota="visitors" chart-type="line"
          icon-url="icon-ykite-gongshuai" icon-color="rgb(var(--arcoblue-4))" :card-style="{
          background: isDark
            ? 'linear-gradient(180deg, #284991 0%, #122B62 100%)'
            : 'linear-gradient(180deg, #f2f9fe 0%, #e6f4fe 100%)',
        }" />
      </a-grid-item>
      <!-- 功率因素 -->
      <a-grid-item :span="{ xs: 5, sm: 5, md: 5, lg: 5, xl: 5, xxl: 3 }">
        <EnergyconsumptionItem :title="$t('power.energy.preview.factor')" quota="published" chart-type="bar"
          icon-url="icon-ykite-gongshuaiyinsu" icon-color="rgb(var(--green-3))" :card-style="{
          background: isDark
            ? ' linear-gradient(180deg, #3D492E 0%, #263827 100%)'
            : 'linear-gradient(180deg, #F5FEF2 0%, #E6FEEE 100%)',
        }" />
      </a-grid-item>
      <!-- 负载率 -->
      <a-grid-item :span="{ xs: 5, sm: 5, md: 5, lg: 5, xl: 5, xxl: 3 }">
        <EnergyconsumptionItem :title="$t('power.energy.preview.load')" quota="comment" chart-type="line"
          icon-url="icon-ykite-fuzaishuai" icon-color="rgb(var(--arcoblue-4))" :card-style="{
          background: isDark
            ? 'linear-gradient(180deg, #294B94 0%, #0F275C 100%)'
            : 'linear-gradient(180deg, #f2f9fe 0%, #e6f4fe 100%)',
        }" />
      </a-grid-item>
      <!-- 今日用电量 -->
      <a-grid-item :span="{ xs: 5, sm: 5, md: 5, lg: 5, xl: 5, xxl: 3 }">
        <EnergyconsumptionItem :title="$t('power.energy.preview.today')" quota="share" chart-type="pie"
          icon-url="icon-ykite-jinriyongdianliang" icon-color="rgb(var(--red-4))" :card-style="{
          background: isDark
            ? 'linear-gradient(180deg, #312565 0%, #201936 100%)'
            : 'linear-gradient(180deg, #F7F7FF 0%, #ECECFF 100%)',
        }" />
      </a-grid-item>
      <!-- 本月用电量 -->
      <a-grid-item :span="{ xs: 5, sm: 5, md: 5, lg: 5, xl: 5, xxl: 3 }">
        <EnergyconsumptionItem :title="$t('power.energy.preview.thisMonth')" quota="visitors" chart-type="line"
          icon-url="icon-ykite-yueyongdianliangchaxun" icon-color="rgb(var(--green-3))" :card-style="{
          background: isDark
          ? ' linear-gradient(180deg, #3D492E 0%, #263827 100%)'
          : 'linear-gradient(180deg, #F5FEF2 0%, #E6FEEE 100%)'
        }" />
      </a-grid-item>
      <!-- 今年用电量 -->
      <a-grid-item :span="{ xs: 5, sm: 5, md: 5, lg: 5, xl: 5, xxl: 3 }">
        <EnergyconsumptionItem :title="$t('power.energy.preview.thisYear')" quota="published" chart-type="bar"
          icon-url="icon-ykite-nianyongdianliang" icon-color="rgb(var(--arcoblue-4))" :card-style="{
          background: isDark
          ? 'linear-gradient(180deg, #284991 0%, #122B62 100%)'
          : 'linear-gradient(180deg, #f2f9fe 0%, #e6f4fe 100%)',
        }" />
      </a-grid-item>
      <!-- 站点通讯 -->
      <a-grid-item :span="{ xs: 2, sm: 2, md: 2, lg: 2, xl: 5, xxl: 2 }" style="margin: auto 0;">
        <EnergyconsumptionSite />
      </a-grid-item>
    </a-grid>
  </a-card>
</template>

<script lang="ts" setup>
  import { ref, PropType, CSSProperties, onMounted, onUnmounted } from 'vue';
  import { PowerModuleEunm, getChart } from '@/api/system/home-power';
  import { useIntervalFn } from '@vueuse/core';
  import useThemes from '@/hooks/themes';
  import EnergyconsumptionItem from './energy-consumption-item.vue';
  import EnergyconsumptionSite from './energy-consumption-site.vue';
  import dayjs from "dayjs";

  const { isDark } = useThemes();

  const loading = ref < boolean > (false);
  const todayEleUseInfo: any = ref < any > ({
    value: "0",
    time: dayjs().format("YYYY-MM-DD HH:mm")
  });

  onMounted(() => {
  })
</script>
<style lang="less" scoped>
  .general-card {
    height: 100%;
  }
</style>
