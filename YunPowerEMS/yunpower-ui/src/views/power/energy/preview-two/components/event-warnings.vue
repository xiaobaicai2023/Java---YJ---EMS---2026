<template>
  <a-card class="event-warnings" :title="$t('power.energy.preview.warning')">
    <!-- 右上角插槽start -->
    <template #extra>
      <span class="warnings-extra" @click="lookMore" style="color:rgb(var(--arcoblue-6))">{{
        $t('power.energy.preview.more') }}</span>
    </template>
    <div class="warnings-wrapper" ref="warningsDiv">
      <!-- 右上角插槽end -->
      <a-spin :loading="loading" :tip="$t('global.loading')" class="warnings-spin">
        <!-- 列表start -->
        <div class="warnings-content" :style="{ 'max-height': warningsDivHeight + 'px' }">
          <EventWarningsList />
        </div>
        <!-- 列表end -->
      </a-spin>
    </div>
  </a-card>
</template>
<script setup>
  import { useRouter } from 'vue-router'
  import { PowerModuleEunm, getChart } from '@/api/system/home-power';
  import { onMounted, onUnmounted } from 'vue';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { useIntervalFn } from '@vueuse/core';
  // list
  import EventWarningsList from "./event-warnings-list.vue";

  //加载中
  const { loading, setLoading } = useLoading(false);
  const router = useRouter();
  const warningsDiv = ref(null);
  const warningsDivHeight = ref(0);

  // 查看更多跳转
  const lookMore = () => {
    router.push({
      path: "/power/alarm/list",
      // query: {
      //   ...props,
      //   bizId: id,
      //   redirect: router.currentRoute.value.fullPath
      // }
    });
  };

  onMounted(() => {
    let timer = null;

    // 获取最大高度
    timer = setInterval(function () {
      if (document.readyState === "complete") {
        warningsDivHeight.value = warningsDiv.value.clientHeight - 10;
        window.clearInterval(timer);
      }
    }, 1000);
  })

</script>
<style lang="less" scoped>
  .event-warnings {
    flex: 1;
    height: 100%;

    .warnings-spin,
    .warnings-wrapper {
      height: inherit;
      width: 100%;
    }

    .warnings-row {
      height: 100%;
    }
  }

  .warnings-extra {
    cursor: pointer;
  }

  .warnings-content {
    width: 100%;
    height: 100%;
    overflow: hidden;
  }

  /* 调整卡片组件高度 */
  :deep(.arco-card-body) {
    height: calc(100% - 24px);
  }
</style>