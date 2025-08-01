<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :flex="1" style="margin-top: 4px;">
        <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                label-align="left"
                auto-label-width>
          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-item field="title" :label="$t('manage.logs.execute.taskTitle')">
                <a-input v-model="searchParams.title" :placeholder="$t('manage.logs.execute.taskTitlePlaceholder')"
                         allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item field="operateBy" :label="$t('manage.logs.execute.executionPersonnel')">
                <a-input v-model="searchParams.operateBy" :placeholder="$t('manage.logs.execute.personnelPlaceholder')"
                         allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item field="time" :label="$t('manage.logs.execute.executionTime')">
                <a-range-picker v-model="searchParams.executeTime"/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-divider style="height: 35px" direction="vertical"/>
      <a-col :flex="'86px'" style="margin-top: 4px;">
        <a-space :size="18">
          <a-button type="primary" @click="onQueryClick">
            <template #icon>
              <icon-search/>
            </template>
            {{ $t('global.search') }}
          </a-button>
          <a-button @click="reset">
            <template #icon>
              <icon-refresh/>
            </template>
            {{ $t('global.reset') }}
          </a-button>
        </a-space>
      </a-col>
    </a-row>
    <!-- 查询条件 end-->
    <a-divider style="margin: 5px 0 15px;"/>

    <!-- 操作条 start-->
    <a-row style="margin-bottom: 16px;">
      <a-col :span="24">
        <a-space>
          <a-button type="primary" @click="handleDeleteClick">
            {{ $t('global.batchDeletion') }}
          </a-button>
          <a-button type="primary" status="danger" @click="handleDeleteAllClick">
            {{ $t('global.empty') }}
          </a-button>
        </a-space>
      </a-col>
    </a-row>
    <!-- 操作条 end-->

    <!-- 表格 start-->
    <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
             :columns="(tabColumns as TableColumnData[])" :data="renderData" @page-change="onPageChange"
             @page-size-change="onPageSizeChange" v-model:selectedKeys="selectedKeys" :row-selection="rowSelection"
             :scroll="scroll">
      <!-- 操作类别 -->
      <template #operateType="{ record }">
        <dict-tag :options="sys_oper_cate" :value="record.operateType"/>
      </template>
      <!-- 执行状态 -->
      <template #status="{ record }">
        <dict-tag :options="sys_execute_status" :value="record.status"/>
      </template>
      <template #operate="{ record }">
        <a-button size="small" type="text" status="success" @click="handleDetail(record)">{{ $t('global.detail') }}
        </a-button>
      </template>
    </a-table>
    <!-- 详情 -->
    <a-drawer :width="750" :visible="detailVisible" @ok="handleSubmitForm" @cancel="handleFormCancel"
              :mask-closable="false" hide-cancel>
      <template #title>
        {{ $t('global.detail') }}
      </template>
      <a-row :gutter="24" style="padding: 10px;">
        <a-col>
          <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                          :column="1"
                          size="large">
            <!-- 任务标题 -->
            <a-descriptions-item :label="$t('manage.logs.execute.taskTitle')">
              <span>{{ detailModel.title }}</span>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.execute.executionPersonnel')">
              <div>{{ detailModel.operateBy }}</div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.execute.executionMethod')">
              <div>{{ detailModel.method }}</div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.execute.executionResult')">
              <div style="width: 600px;">
                <hljs v-if="detailModel.operateResult" :code="detailModel.operateResult || ''" :format="true"/>
              </div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.execute.executionTime')">
              <div>{{ detailModel.executeTime }}</div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.execute.executionState')">
              <div>{{ detailModel.statusStr }}</div>
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>
    </a-drawer>
    <!-- 表格 end-->
    <a-modal width="400px" v-model:visible="modalVisible" class="modal-box">
      <template #title>
        <icon-close-circle v-if="modalTitle == '删除'" size="18" style="color:rgb(var(--red-6)); margin-right: 5px;"/>
        <icon-exclamation-circle v-else size="18" style="color:rgb(var(--orange-6)); margin-right: 5px;"/>
        {{ $t('global.confirmTip') }}
      </template>
      <div style="text-align: center;">你确定要{{ modalTitle }}吗？</div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleModalCancel">{{ $t('global.cancel') }}</a-button>
            <a-button type="primary" @click="handleModalOk">{{ $t('global.confirm') }}</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
  </a-card>
</template>

<script lang="ts" setup>
import {computed, onMounted, reactive, ref, getCurrentInstance} from "vue";
import {Pagination} from "@/api/common";
import {PaginationProps, TableColumnData, TableRowSelection} from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import {notification} from "@/hooks/my-design";
import dayjs from "dayjs";
import {cleanExecuteLog, delExecuteLog, listExecuteLog} from "@/api/system/execute-log";
import {getDictLabel} from "@/utils/dict";

//******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_oper_type,
  sys_oper_cate,
  sys_execute_status
} = proxy?.useDict("sys_oper_type", "sys_oper_cate", "sys_execute_status");
//******* 这里写动态获取下拉列表 end ******

//加载中
const {loading, setLoading} = useLoading(true);
//表格数据
const renderData = ref<any[]>([]);
//表格分页配置
const pagination: PaginationProps = Pagination;
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "任务标题",
    dataIndex: "title",
    align: 'left',
    fixed: 'left',
    ellipsis: true,
    tooltip: {position: 'top'}
  },
  {
    title: "操作类别",
    dataIndex: "operateType",
    slotName: "operateType",
    align: 'center',
    width: 100,
  },
  {
    title: "执行人员",
    width: 100,
    ellipsis: true,
    align: 'center',
    dataIndex: "operateBy",
  },
  {
    title: "执行状态",
    dataIndex: "status",
    slotName: 'status',
    align: 'center',
    width: 100,
  },
  {
    title: "执行时间",
    dataIndex: "executeTime",
    align: 'center',
    width: 180,
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    width: 80,
    fixed: 'right',
    align: 'center'
  },
]);
const rowSelection: TableRowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});
//选中的
const selectedKeys = ref();
const scroll = {
  x: 810,
};
// 获取当前时间
var dateNow = dayjs().format("YYYY-MM-DD");
var dateBefore = dayjs().subtract(1, 'month').format("YYYY-MM-DD")

const detailVisible = ref(false);
const modalVisible = ref(false);
const modalTitle = ref("");
const modalType = ref(1);

//生成查询条件
const generateFormModel = () => {
  return {
    //系统模块
    title: "",
    //操作人员
    operateBy: "",
    //执行时间
    executeTime: [dateBefore, dateNow],
  };
};

//查询项
const searchParams = ref(generateFormModel());


//表格页码发生变化
const onPageChange = async (val: number) => {
  pagination.current = val;
  await fetchData();
}

//表格每页数量发生变化
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchData();
}

// 查询
const onQueryClick = async () => {
  pagination.current = 1;
  selectedKeys.value = [];
  await fetchData();
}

const reset = async () => {
  pagination.current = 1;
  selectedKeys.value = [];
  searchParams.value = generateFormModel();
  await fetchData();
}

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const data: any = {
      pageSize: pagination.pageSize,
      current: pagination.current,
      ...searchParams.value,
      executeTime: ''
    };
    if (searchParams.value.executeTime && searchParams.value.executeTime.length > 0) {
      data.params = {
        beginTime: dayjs(searchParams.value.executeTime[0]).format("YYYY-MM-DD 00:00:00"),
        endTime: dayjs(searchParams.value.executeTime[1]).format("YYYY-MM-DD 23:59:59")
      }
    }else{
      data.params = {
        beginTime: undefined,
        endTime: undefined
      }
    }
    const res: any = await listExecuteLog(data);
    renderData.value = res.rows;
    pagination.total = res.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

const detailModel = ref<any>({});

// 详情
const handleDetail = async (record: any) => {
  detailVisible.value = true;
  record.statusStr = getDictLabel("sys_execute_status", record.status);
  detailModel.value = record;
};
//详情关闭
const handleSubmitForm = () => {
  detailVisible.value = false;
}
const handleFormCancel = () => {
  detailVisible.value = false;
}
//确认框

/**
 * 删除弹框
 */
const handleDeleteClick = () => {
  if (!selectedKeys.value || selectedKeys.value.length <= 0) {
    notification({code: 201, msg: "请选择要删除的数据"})
    return;
  }
  modalVisible.value = true;
  modalTitle.value = "删除所选记录";
  modalType.value = 1;
};

/**
 * 清空按钮
 */
const handleDeleteAllClick = () => {
  modalVisible.value = true;
  modalType.value = 2;
  modalTitle.value = "清空所有数据";
};

/**
 * 删除-确定
 */
const handleModalOk = async () => {
  let res;
  modalVisible.value = false;
  setLoading(true);
  try {
    if (modalType.value == 1) {
      if (!selectedKeys.value || selectedKeys.value.length <= 0) {
        notification({code: 201, msg: "请选择要删除的数据"})
        return;
      }
      res = await delExecuteLog(selectedKeys.value);
    } else {
      res = await cleanExecuteLog();
    }
    notification(res);
    if (res.code == 200) {
      fetchData();
    }
  } catch (e) {
    console.error(e);
  } finally {
    setLoading(false);
  }
};


/**
 * 删除弹框-取消
 */
const handleModalCancel = () => {
  modalVisible.value = false;
}

/**
 * 实例创建完成后立即执行
 */
onMounted(() => {
  fetchData();
})
</script>
<style lang="less" scoped>
.row-mp-30 {
  margin-top: 30px;
  padding-right: 30px;
}
</style>

