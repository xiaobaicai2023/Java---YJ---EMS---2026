<!--
 * 功能：基础图
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <a-spin class="card-base-wrapper" :loading="loading">
    <a-card :bordered="false" :style="{ background: chartInfo.background }">
      <div class="chart-view" v-if="chartInfo.cardName">
        <div class="echart-wapper">
          <a-typography-title :heading="5" :ellipsis="{ rows: 1, showTooltip: true }" style="margin-top: 0">
            {{ chartInfo.cardName }}
          </a-typography-title>
          <div class="content-wrap">
            <!-- 标题、数值start -->
            <div class="content">
              <a-statistic :value="chartInfo.singleValue" :precision="2" :value-style="{ color: chartInfo.fontColor }"
                           show-group-separator>
                <template #suffix>
                  <span style="padding-left: 6px">{{ chartInfo.unit }}</span>
                </template>
              </a-statistic>
              <div class="desc" v-if="chartInfo.chain">
                <a-typography-text type="secondary" class="content-label">{{ chartInfo.chainTitle }}</a-typography-text>
                <a-typography-text :type="chartInfo.chainRatio < 0 ? 'danger' : 'success'">
                  {{ Math.abs(chartInfo.chainRatio) }}%
                  <icon-arrow-fall v-if="chartInfo.chainRatio < 0" />
                  <icon-arrow-rise v-else />
                </a-typography-text>
              </div>
              <div class="desc" v-if="chartInfo.yoy">
                <a-typography-text type="secondary" class="content-label">{{ chartInfo.yoyTitle }}</a-typography-text>
                <a-typography-text :type="chartInfo.yoyRatio < 0 ? 'danger' : 'success'">
                  {{ Math.abs(chartInfo.yoyRatio) }}%
                  <icon-arrow-fall v-if="chartInfo.yoyRatio < 0" />
                  <icon-arrow-rise v-else />
                </a-typography-text>
              </div>
            </div>
            <!-- 标题、数值end -->
            <!-- 右侧图标start -->
            <div class="icon-view" v-if="!chartInfo.coalConvert">
              <result-icon :name="chartInfo.icon"
                           :iconStyle="{ fontSize: '38px', color: chartInfo.iconColor?chartInfo.iconColor:'' }"></result-icon>
            </div>
            <div class="corn-icon-view" v-else>
              <p>
                <custom-icon type="icon-ykite-jiantanpaifang" class="corn-custom-icon"></custom-icon>
                <a-statistic class="corn-statistic" :value="888">
                  <template #suffix>吨</template>
                </a-statistic>
              </p>
              <p>
                <custom-icon type="icon-ykite-shu" class="corn-custom-icon"></custom-icon>
                <a-statistic class="corn-statistic" :value="888">
                  <template #suffix>棵</template>
                </a-statistic>
              </p>
            </div>
          </div>
        </div>
      </div>
      <a-empty v-else />
    </a-card>
  </a-spin>
</template>

<script lang="ts" setup>
import {inject, onBeforeUnmount, onMounted, ref, watch} from 'vue';
import useLoading from '@/hooks/loading';
import {getChartInfo} from '@/api/dashboard/api';
import {SocketData} from '@/api/websocketService';
import {useIntervalFn} from '@vueuse/core';
import {getTimeObject} from '@/utils/charts';
import {generateSimilarColor} from '@/utils/ruoyi';

const props = defineProps({
    public: {
      type: Object,
      default: () => ({}),
    },
    // 设备编号
    deviceSn: {
      type: String,
      default: '',
    },
    totalType: {
      type: Number,
      default: 2,
    },
  });

  const { loading, setLoading } = useLoading(false);

  const chartInfo = ref<any>({
    cardName: '',
    singleInfo: {},
    singleRightList: [],
  });

  // WebSocket 数据
  const { data, sendData }: SocketData = inject('webSocketData', {
    data: { value: 0 },
    sendData: () => {},
  });

  const timeOptions = ref<any>({});
  /**
   * 时间改变
   */
  const timeBarChange = async (timer: any) => {
    timeOptions.value = timer;
    await fetchData();
  };

  // 监听 WebSocket 数据
  watch(data, async (newVal) => {
    if (newVal.key && newVal.key === chartInfo.value.varSn) {
      await fetchData();
    }
  });

  // 5 分钟轮询
  const { resume: startInterval, pause: stopInterval } = useIntervalFn(async () => {
    await fetchData();
  }, 1000 * 60 * 5);

  // 获取数据
  const fetchData = async () => {
    try {
      // setLoading(true);
      const params = {
        configId: props.public.dashboardConfigId,
        cardKey: props.public.cardKey,
        deviceSn: props.deviceSn,
        ...getTimeObject(timeOptions.value),
      };
      const res: any = await getChartInfo(params);
      if (res.code === 200 && res.data) {
        chartInfo.value = {
          cardName: res.data.cardName,
          ...res.data.singleInfo,
          ...res.data.singleRightList
        };
        if (chartInfo.value.backgroundColor && chartInfo.value.backgroundColor.length > 0) {
          let color = generateSimilarColor(chartInfo.value.backgroundColor);
          chartInfo.value.background = `linear-gradient(180deg, ${chartInfo.value.backgroundColor} 0%, ${color} 100%)`;
        }
      } else {
        chartInfo.value = {
          cardName: '',
        };
      }
    } catch (e) {
      console.error('单值组件出错', e);
    } finally {
      // setLoading(false);
    }
  };

  onMounted(async () => {
    await fetchData();
    startInterval();
    sendData({
      action: 'subscribe',
      bizSn: chartInfo.value.varSn,
    });
  });

  onBeforeUnmount(() => {
    stopInterval();
    sendData({
      action: 'unsubscribe',
      bizSn: chartInfo.value.varSn,
    });
  });

  defineExpose({
    fetchData,
    handleTime: (val: any) => {
      timeBarChange(val);
    },
  });
</script>

<style scoped lang="less">
  .card-base-wrapper {
    width: 100%;
    height: 100%;

    :deep(.arco-card) {
      border-radius: 4px;
      height: 100%;

      .arco-card-body {
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
      }
    }

    .content-wrap {
      width: 100%;
      display: flex;
      justify-content: space-between;

      .content {
        flex: 1;
        display: flex;
        flex-direction: column;

        .desc {
          white-space: nowrap;
        }
      }

      .icon-view {
        flex-shrink: 0;
        align-items: center;
        font-size: var(--font-size-display-1);
        margin: 0 auto;
      }

      .content-label {
        padding-right: 8px;
        font-size: 12px;
        color: var(--color-text-1);
      }

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

      :deep(.arco-statistic-title) {
        width: 140%;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      :deep(.arco-card-header) {
        border: none;
      }

      //碳右侧icon
      .corn-icon-view {
        flex-shrink: 0;
        margin-top: -16px;

        p {
          margin: 0;
          padding: 0;
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-top: 6px;

          .corn-custom-icon {
            font-size: 20px !important;
            color: var(--color-neutral-9);
            margin-top: 3px;
          }

          .corn-statistic {
            line-height: 0 !important;
            margin-left: 4px;

            :deep(.arco-statistic-value-integer) {
              font-size: 12px;
              font-weight: bold;
              white-space: nowrap;
            }
          }
        }
      }
    }
  }

  .chart-view {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-wrap: nowrap;

    .echart-wapper {
      flex: 1;
      height: 100%;
      display: flex;
      flex-direction: column;
      //justify-content: center;
    }

    .total-wapper {
      width: 30%;
      height: 100%;
    }
  }

  :deep(.arco-typography-success){
    color: rgb(var(--success-5));
  }
</style>
