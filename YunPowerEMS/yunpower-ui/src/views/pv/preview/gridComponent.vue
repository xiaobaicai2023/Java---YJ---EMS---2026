<template>
  <div>
    <a-row :gutter="[12, 12]" v-if="!isConfig">
      <!-- 第一排 -->
      <a-col>
        <a-row :gutter="12" style="height: 400px;">
          <a-col :span="6">
            <!-- 电站概览【电站】基础数据 -->
            <CardOne/>
          </a-col>
          <a-col :span="12">
            <GridLayout :style="{height: '100%'}" v-model:layout="layout.pageConfig"
                        :row-height="30"
                        is-draggable
                        is-resizable
                        is-bounded>
              <template #item="{item}">
                <component :is="EnumChartType[item.chartType]" :public="{
                  cardKey:item.cardKey,
                  dashboardConfigId: 7
                }"></component>
                <!-- 编辑or删除按钮 -->
                <div class="handle-row" v-if="!item.static">
                  <a-button type="primary" size="mini" @click="handleEditItem(item)">编辑</a-button>
                </div>
              </template>
            </GridLayout>
          </a-col>
          <a-col class="weather" :span="6">
            <!-- 天气 -->
            <CardFour/>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
    <!-- 编辑 -->
    <edit-card v-else :cardEditParams="cardEditParams" :deviceSn="searchForm.deviceSn"
               @handleEditCancel="handleEditCancel"/>
  </div>
</template>

<script setup lang="ts">
import {GridLayout} from 'grid-layout-plus'
import {onMounted, ref} from 'vue';
import CardOne from './components/card-one.vue'
import CardFour from './components/card-four.vue'
import {EnumChartType, getPageKey, getPageName} from "@/views/dashboard/preview/index";
import {useRoute, useRouter} from "vue-router";
import {getCardConfig, updateDashboardConfig} from "@/api/dashboard/api";
import EditCard from "@/views/dashboard/preview/edit-card.vue";
import {isArray} from "lodash";

const props = defineProps({
  // 1 用电，2 光伏
  stationType: {
    type: Number,
    default: 1
  },
  // configType 1: 指定 2: 通用
  configType: {
    type: Number,
    default: 1
  }
})


// 是否开启 配置模式
const isConfig = ref(false);
//路由
const router = useRouter();
const route = useRoute();
// 页面key
const pageKey = getPageKey(router) + '-pv';
const pageName = getPageName(route);
const searchForm = ref({

})
// grid布局
const layout = ref<any>({
  id: 0,
  pageName: pageName,
  pageKey: pageKey,
  pageType: 2,
  pageConfig: [
    {x: 0, y: 0, w: 8, h: 7, i: 0, static: false, chartType: 8, cardKey: 1719195486780},
    {x: 8, y: 3.5, w: 4, h: 3.5, i: 1, static: false, chartType: 14, cardKey: 1719384449255},
    {x: 8, y: 0, w: 4, h: 3.5, i: 2, static: false, chartType: 14, cardKey: 1719384449255},
    {x: 0, y: 5, w: 3, h: 3, i: 3, static: false, chartType: 14, cardKey: 1719384449255},
    {x: 3, y: 5, w: 3, h: 3, i: 4, static: false, chartType: 14, cardKey: 1719384449255},
    {x: 6, y: 5, w: 3, h: 3, i: 5, static: false, chartType: 14, cardKey: 1719384449255},
    {x: 9, y: 5, w: 3, h: 3, i: 6, static: false, chartType: 14, cardKey: 1719384449255},
  ]
});
// 传给编辑组件的数据
const cardEditParams = ref<any>();

/**
 * 获取布局
 */
const getLayout = async () => {
  try {
    const res = await getCardConfig(pageKey);
    // if (res.code === 200 && res.data) {
    //   layout.value = {
    //     ...res.data,
    //     pageConfig: JSON.parse(res.data.pageConfig)
    //   }
    // }
    // 如果是第一次进入页面 调用一下保存接口（生成id）
    if (!res.data) {
      layout.value = {
        pageName,
        pageKey,
        pageType: 2,
        pageConfig: '[]'
      }
      await updateDashboardConfig(layout.value);
      await getLayout();
    }
  } catch (e) {
    console.log('err', e);
  }
};
/**
 * 编辑
 */
const handleEditItem = (item: any) => {
  cardEditParams.value = {
    configType: props.configType,
    chartType: item.chartType,
    cardKey: item.i,
    headType: item.headType,
    staticType: item.staticType,
    public: item.public,
    dashboardConfigId: layout.value.id,
  };
  isConfig.value = true;
};
/**
 * 子组件 是否可编辑
 * @param status
 */
const changeLayoutDisable = (status: boolean) => {
  if (!isArray(layout.value.pageConfig) && !layout.value.pageConfig.length) return
  layout.value.pageConfig.forEach((item: any) => {
    item.static = status;
  });
}

/**
 * 关闭编辑页组件
 */``
const handleEditCancel = () => {
  isConfig.value = false;
  if (props.configType === 2) {
    searchForm.value.deviceSn = null;
  }
}
onMounted(async () => {
  await getLayout();
  // changeLayoutDisable(true);
});
</script>

<style lang="less" scoped>
.handle-row {
  position: absolute;
  bottom: 5px;
  right: 10px;

  .arco-btn-status-danger {
    margin-left: 10px;
  }
}
</style>
