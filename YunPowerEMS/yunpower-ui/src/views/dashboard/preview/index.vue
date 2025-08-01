<!--
 * 功能：通用配置页面
 * 作者：闫李壮
 * 日期：2024-06-05
-->
<template>
  <div :style="{marginTop : configType !== 2 ? '-16px' : '0'}">
    <a-spin :loading="loading" style="width: 100%">
      <!-- 布局 -->
      <div v-if="!isConfig" class="container-mask">
        <!-- 搜索框 -->
        <search-form v-if="configType === 2" :stationType="stationType" @search="searchConfig" />
        <!-- 拖拽组件 -->
        <GridLayout :style="{ margin: '0 -10px' }" v-model:layout="layout.pageConfig" :row-height="30" is-draggable is-resizable is-bounded>
          <template #item="{ item }">
            <component
              v-if="configType === 1 || (configType === 2 && searchForm.deviceSn)"
              :is="item.staticType === 1 ? 'card-chart-pie-statistics' : item.staticType === 2 || item.staticType === 3? 'card-chart-combine' : EnumChartType[item.chartType]"
              :ref="(el:any) => setComponentRef(el, item.i)"
              :configType="configType"
              :deviceSn="searchForm.deviceSn"
              :public="{
                ...item.public,
                chartType: item.chartType,
                dashboardConfigId: layout.id,
              }"
            >
            </component>
          </template>
        </GridLayout>
      </div>
    </a-spin>
  </div>
</template>
<script setup lang="ts">
  import { GridLayout } from 'grid-layout-plus';
  import { nextTick, onMounted, ref } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { calculateNewPosition, EnumChartType, getPageKey, getPageName } from '@/views/dashboard/preview/index';
  import { getCardConfig } from '@/api/dashboard/api';
  import { notification } from '@/hooks/my-design';
  import useLoading from '@/hooks/loading';
  import { isArray } from '@/utils/is';
  import SearchForm from '@/views/dashboard/preview/component/searchForm.vue';

  const { loading, setLoading } = useLoading(false);

  const props = defineProps({
    // 1 用电，2 光伏
    stationType: {
      type: Number,
      default: 1,
    },
    // configType 1: 指定 2: 通用
    configType: {
      type: Number,
      default: 1,
    },
  });
  const searchForm = ref<any>({
    deviceSn: null,
  });
  // 是否显示模板弹窗
  const templateModalShow = ref(false);
  // 是否开启 配置模式
  const isConfig = ref(false);
  //路由
  const router = useRouter();
  const route = useRoute();
  // 页面key
  const pageKey = getPageKey(router);
  const pageName = getPageName(route);

  // 是否开启配置
  const isSetting = ref(false);
  // grid布局
  const layout = ref<any>({
    id: 0,
    pageName: pageName,
    pageKey: pageKey,
    pageType: 2,
    pageConfig: [],
  });
  // 传给编辑组件的数据
  const cardEditParams = ref<any>();
  const componentRef = ref<any>({});

  const setComponentRef = (el: any, i: number) => {
    componentRef.value[i] = el;
  };
  /**
   * 获取布局
   */
  const getLayout = async () => {
    try {
      const res: any = await getCardConfig(pageKey);
      if (res.code != 200) return notification(res);
      if (res.data) {
        layout.value = {
          ...res.data,
          pageConfig: JSON.parse(res.data.pageConfig),
        };
      } else {
        // 如果是第一次进入页面 调用一下保存接口（生成id）
        layout.value = {
          pageName,
          pageKey,
          pageType: 2,
          pageConfig: '[]',
        };
        await getLayout();
      }
    } catch (e) {
      console.log('err', e);
    }
  };
  /**
   * 查询
   */
  const searchConfig = (params: any) => {
    searchForm.value.deviceSn = null;

    nextTick(() => {
      searchForm.value.deviceSn = params.deviceSn;
      setTimeout(() => {
        for (const key in componentRef.value) {
          const currentInstance = componentRef.value[key];
          if (currentInstance && currentInstance.timeBarChange) {
            currentInstance.timeBarChange(params);
          }
        }
      });
    });
  };

  /**
   * 新建
   */
  const handleAddItem = () => {
    templateModalShow.value = true;
  };
  
  /**
   * 子组件 是否可编辑
   * @param status
   */
  const changeLayoutDisable = (status: boolean) => {
    const pageConfig = layout.value.pageConfig;

    if (!Array.isArray(pageConfig) || pageConfig.length === 0) return;

    for (let i = 0; i < pageConfig.length; i++) {
      const item = pageConfig[i];
      item.static = item.fixed || status;
    }
  };

  onMounted(async () => {
    await getLayout();
    changeLayoutDisable(true);
  });
</script>
<style lang="less" scoped>
  .time-bar-card {
    width: 100%;
  }

  .handle-row {
    position: absolute;
    bottom: 5px;
    right: 10px;
    z-index: 11;

    .arco-btn-status-danger {
      margin-left: 10px;
    }
  }

  .container-mask{
    :deep(.arco-spin-mask){
      background: transparent;
    }
  }

</style>
