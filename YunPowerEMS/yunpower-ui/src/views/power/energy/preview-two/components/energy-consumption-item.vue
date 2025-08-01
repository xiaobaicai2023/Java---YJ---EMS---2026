<template>
  <a-spin :loading="loading" style="width: 100%;height: 100%;">
    <a-card :bordered="false" :style="cardStyle">
      <div class="content-wrap">
        <!-- 标题、数值start -->
        <div class="content">
          <a-statistic :title="title" :value="1562" animation show-group-separator>
            <template #suffix>/kWh</template>
          </a-statistic>
          <a-statistic :value="1562" animation show-group-separator>
            <template #suffix>/kWh</template>
          </a-statistic>
        </div>
        <!-- 标题、数值end -->
        <!-- 右侧图标start -->
        <div class="icon">
          <custom-icon :type="iconUrl" :size="38" :style="{color: iconColor}"></custom-icon>
        </div>
        <!-- 右侧图标end -->
      </div>
    </a-card>
  </a-spin>
</template>

<script lang="ts" setup>
  import { ref, PropType, CSSProperties } from 'vue';
  import useLoading from '@/hooks/loading';

  const props = defineProps({
    title: {
      type: String,
      default: '',
    },
    quota: {
      type: String,
      default: '',
    },
    iconUrl: {
      type: String,
      default: '',
    },
    iconColor: {
      type: String,
      default: '',
    },
    cardStyle: {
      type: Object as PropType<CSSProperties>,
      default: () => {
        return {};
      },
    },
  });

  const { loading, setLoading } = useLoading(false);


</script>

<style scoped lang="less">
  /* 弹性盒 */
  .content-wrap {
    width: 100%;
    padding: 16px;
    white-space: nowrap;
    display: flex;
    align-items: center;
    justify-content: space-between;

    /* 撑起左侧内容 */
    .content {
      flex: 1;
      display: flex;
      flex-direction: column;
    }
  }

  .icon {
    width: 25%;
    align-items: center;
    font-size: var(font-size-display-1);
    margin: 0 auto;
  }

  .label {
    padding-right: 8px;
    font-size: 12px;
  }

  :deep(.content) {
    float: left;
    width: 108px;
    min-height: 90px;
  }

  /* 调整数值组件样式 */
  :deep(.arco-statistic) {
    .arco-statistic-title {
      font-size: 16px;
      font-weight: bold;
      white-space: nowrap;
    }

    .arco-statistic-content {
      margin-top: 0;
    }
  }

  /* 调整卡片样式 */
  :deep(.arco-card) {
    border-radius: 4px;
  }

  :deep(.arco-card-body) {
    width: 100%;
    padding: 0;
  }

  /* 调整英文下title溢出问题 */
  :deep(.arco-statistic-title) {
    width: 140%;
    overflow: hidden;
    text-overflow: ellipsis;
  }
</style>