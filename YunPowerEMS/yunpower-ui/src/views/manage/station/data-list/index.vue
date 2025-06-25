<!--
 * 功能：数据列表
 * 作者：罗甜甜
 * 日期：2024-05-29
-->
<template>
  <div>
    <a-card class="content">
      <a-row>
        <a-col>
          <a-alert>当前大屏：{{ routeParams.entName }}-{{ routeParams.stationName }}</a-alert>
        </a-col>
      </a-row>
      <!-- 按钮 -->
      <a-row class="add-button">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus/>
              </template>
              新建卡片
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 start-->
      <a-table row-key="id" ref="tableRef" :loading="loading"
               :bordered="{ wrapper: true, cell: true }" :scroll="{ x: 1270 }" :columns="columns"
               :data="renderData"
               :pagination="pagination"
               @page-change="handlePageChange"
               @page-size-change="handlePageSizeChange">
        <!--单位-->
        <!--模板类型-->
        <template #chartType="{ record }">
          {{ ChartLabel[record.chartType] }}
        </template>
        <!-- 状态 -->
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"></stop-flag>
        </template>
        <!-- 操作 -->
        <template #operate="{ record }">
          <a-button size="small" type="text" style="color:#0E88F6" status="success" @click="handlePreview(record)">预览
          </a-button>
          <a-button size="small" type="text" @click="handleStop(record)"
                    :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdate(record)">{{ $t('global.edit') }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
        </template>
      </a-table>
      <!-- 表格内容 end-->
    </a-card>
    <a-modal :width="800" v-model:visible="modelVisible" @ok="handleClose" hide-cancel>
      <template #title>
        预览
      </template>
      <div style="height: 500px" v-if="modelVisible">
        <!--        <card-chart-combine/>-->
        <component
            :is="previewData.staticType == 1 ? 'card-chart-pie-statistics' : previewData.staticType == 2|| previewData.staticType == 3 ? 'large-combine' : EnumChartType[previewData.chartType]"
            :public="{...previewData, preview: 1, tablePre: 1, waterPre:1,}"
            style="margin-top: 0">
        </component>
      </div>
    </a-modal>
    <!--选择卡片模板弹窗-->
    <card-template :visible="cardVisible" @chooseTemplate="handleCardTemplate"></card-template>

    <!--卡片编辑弹窗-->
    <card-edit :visible="cardEditVisible"
               :data="data"
               @closeModal="closeModal"></card-edit>
  </div>
</template>

<script setup lang="ts">
import {computed, inject, onMounted, reactive, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {Message, Modal, Notification, TableInstance} from "@arco-design/web-vue";
import {BasePagination} from '@/api/common';
import {listGroupAll} from "@/api/system/group";
import {useRoute, useRouter} from 'vue-router';
import CardTemplate from "@/views/manage/station/components/card-template/index.vue";
import CardEdit from '@/views/manage/station/components/card-edit/index.vue'
import {getCardList, deleteConfigList, updateConfigStatus} from "@/api/manage/station/edit";
import {ChartLabel, EnumChartType} from '@/views/manage/station/components/index';

/*************************** 变量区域 begin ***************************/
//参数
const props = reactive<any>({
  mapId: 5
})

//加载中
const {loading, setLoading} = useLoading(false);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
//表格实例
const tableRef = ref<TableInstance>();
// 预览窗
const modelVisible = ref(false);
//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "卡片名称",
    dataIndex: "cardName",
    slotName: "cardName",
  },
  {
    title: "图表类型",
    dataIndex: "chartType",
    slotName: "chartType",
  },
  {
    title: "启用状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    align: 'center',
    width: 240
  },
]);
//路由
const router = useRouter();
const route = useRoute();

//选择卡片模板弹窗
const cardVisible = ref(false);
// 卡片编辑弹窗
const cardEditVisible = ref(false);
const data = ref<any>({
  layout: 'card-chart-combine',
  public: {
    title: '',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
})
const routeParams = ref<any>(route.query);

const input = (val) => {
  data.value.public.title = val;
}

// 关闭编辑组件
const closeModal = async (val) => {
  // 如果是新增按钮 重新调接口
  if (val == 1) {
    await fetchData()
  }
  cardEditVisible.value = false;
}
//表格数据
const renderData = ref<any[]>([]);

// 关闭选择卡片模板弹窗
const handleCardTemplate = (val: any) => {
  if (val) {
    cardVisible.value = false;
    cardEditVisible.value = true;
    data.value = {dashboardConfigId: routeParams.value.dashboardConfigId, ...val};
    console.log(data.value, '我是父组件的data')
  } else {
    cardVisible.value = false;
    cardEditVisible.value = false;
  }
}


/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

/**
 * 表格分页发生改变时触发
 * @param val
 */
const handlePageChange = (val: number) => {
  console.log('表格分页发生改变时触发', val)
  pagination.current = val;
  fetchData();
}
/**
 * 表格每页数据数量发生改变时触发
 * @param val
 */
const handlePageSizeChange = (val: number) => {
  console.log('表格每页数据数量发生改变时触发', val)
  pagination.pageSize = val;
  fetchData();
}


/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  renderData.value = [];
  try {
    const res = await getCardList({
      dashboardConfigId: routeParams.value.id,
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
    });
    if (res.code == 200) {
      renderData.value = res.rows;
      pagination.total = res.total;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
}

// 数据
const handleDataConfig = () => {
  router.push({
    path: "/manage/station/largeScreenDetail",
    query: {
      // channelId: id,
    }
  });
}

const editParams = ref<any>(null)

// 新建
const handleAdd = () => {
  cardVisible.value = true;
}

// 编辑
const handleUpdate = (record: any) => {
  const {dashboardConfigId, cardKey, chartType, cardName} = record
  const cardConfig = JSON.parse(record.cardConfig);
  console.log(record)
  const detailParams = {
    dashboardConfigId,
    cardKey,
    chartType,
    staticType: cardConfig.staticType ? cardConfig.staticType : undefined,
    public: {
      dashboardConfigId,
      cardName,
      cardKey,
    },
  }
  data.value = {...detailParams}
  // console.log(data.value)
  cardEditVisible.value = true;
}
// 删除
const handleDelete = (record: any) => {
  Modal.error({
    title: '提示',
    content: `你确定要删除${record.cardName}吗？`,
    hideCancel: false,
    simple: false,
    onOk: async () => {
      try {
        setLoading(true)
        const res = await deleteConfigList(record.id)
        if (res.code == 200) {
          Notification.success({
            title: '提示',
            content: '删除成功',
          });
          await fetchData();
        }
      } catch {

      } finally {
        setLoading(false)
      }
    },
  });
}

// 停用
const handleStop = (record: any) => {
  const text = record.stopFlag == 1 ? '启用' : '停用';
  const state = record.stopFlag == 1 ? 0 : 1;
  Modal.warning({
    title: '提示',
    // ${record.entName}
    content: `你确定要${text}${record.cardName}吗？`,
    hideCancel: false,
    simple: false,
    onOk: async () => {
      const data = {
        id: record.id,
        state: state
      }
      try {
        setLoading(true)
        const res = await updateConfigStatus(data)
        if (res.code == 200) {
          Notification.success({
            title: '提示',
            content: text + '成功',
          });
          await fetchData();
        }
      } catch {

      } finally {
        setLoading(false)
      }
    },
  });
}
const previewData = ref<any>({});

// 打开预览窗
const handlePreview = (record: any) => {
  const {dashboardConfigId, cardKey, chartType, cardName} = record;
  const cardConfig = JSON.parse(record.cardConfig);
  const detailParams = {
    dashboardConfigId,
    chartType,
    cardKey,
    staticType: cardConfig.staticType ? cardConfig.staticType : undefined,
    public: {
      cardName,
      cardKey
    },
    preview: 1
  }
  previewData.value = {...detailParams}
  modelVisible.value = true;
}
// 关闭预览窗
const handleClose = () => {
  modelVisible.value = false;
}

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
  tableRef.value?.expandAll(false);
})
</script>

<style lang="less" scoped>
.add-button {
  margin: 16px 0;
}
</style>
