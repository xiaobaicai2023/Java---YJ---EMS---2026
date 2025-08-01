<template>
  <a-card class="general-card" style="padding: 5px;overflow-y: scroll;">
    <a-grid :cols='{ xs: 1, sm: 1, md: 1, lg: 1, xl: 2, xxl: 2 }' :colGap="10" :row-gap="10">
      <a-grid-item>
        <PowerGeneration pageValue="today-pv-power" :pageTitle="$t('pv.preview.today')" suffix="kWh" :card-style="{
          background: isDark
            ? 'linear-gradient(180deg, #284991 0%, #122B62 100%)'
            : 'linear-gradient(180deg, #f2f9fe 0%, #e6f4fe 100%)',
        }" />
      </a-grid-item>
      <a-grid-item>
        <PowerGeneration pageValue="month-pv-power" :pageTitle="$t('pv.preview.month')" suffix="MWh" :card-style="{
          background: isDark
            ? ' linear-gradient(180deg, #3D492E 0%, #263827 100%)'
            : 'linear-gradient(180deg, #F5FEF2 0%, #E6FEEE 100%)',
        }" />
      </a-grid-item>
      <a-grid-item>
        <PowerGeneration pageValue="year-pv-power" :pageTitle="$t('pv.preview.year')" suffix="MWh" :card-style="{
          background: isDark
            ? 'linear-gradient(180deg, #294B94 0%, #0F275C 100%)'
            : 'linear-gradient(180deg, #f2f9fe 0%, #e6f4fe 100%)',
        }" />
      </a-grid-item>
      <a-grid-item>
        <PowerGeneration pageValue="total-pv-power" :pageTitle="$t('pv.preview.total')" suffix="MWh" :card-style="{
          background: isDark
            ? 'linear-gradient(180deg, #312565 0%, #201936 100%)'
            : 'linear-gradient(180deg, #F7F7FF 0%, #ECECFF 100%)',
        }" />
      </a-grid-item>
    </a-grid>
  </a-card>
</template>
<script lang="ts" setup>
import { computed, reactive, ref } from 'vue';
import useThemes from '@/hooks/themes';
import PowerGeneration from './power-generation.vue'

const { isDark } = useThemes();
</script>

<style lang="less" scoped>
:deep(.arco-card) {
  border-radius: 4px;
}

:deep(.arco-card-body) {
  width: 100%;
  // height: 134px;
  padding: 5px;
}

.content-wrap {
  width: 100%;
  padding: 16px;
  white-space: nowrap;
}

:deep(.content) {
  float: left;
  width: 108px;
  // height: 102px;
}

:deep(.arco-statistic) {
  .arco-statistic-title {
    margin-bottom: 0px;
    font-size: 12px;
    font-weight: bold;
    white-space: nowrap;
  }

  .arco-statistic-content {
    margin-top: 0px;
  }
}
</style>
