<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :span="16" style="margin-top: 4px;">
        <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                label-align="left"
                auto-label-width>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item field="title" :label="$t('manage.logs.operate.systemModules')">
                <a-input v-model="searchParams.title" :placeholder="$t('manage.logs.operate.systemModulesPlaceholder')"
                         allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item field="occurTime" :label="$t('manage.logs.system.timeOfOccurrence')">
                <a-range-picker v-model="searchParams.occurTime" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-col :span="8">
        <a-divider style="height: 35px" direction="vertical"/>
        <a-space :size="18" style="margin-top: 4px;">
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
      <template #operate="{ record }">
        <a-button size="small" type="text" status="success" @click="handleDetail(record.id)">{{ $t('global.detail') }}
        </a-button>
      </template>
    </a-table>
    <!-- 详情 -->
    <a-drawer :width="1200" :visible="detailVisible" @ok="handleSubmitForm" @cancel="handleFormCancel"
              :mask-closable="false" hide-cancel>
      <template #title>
        {{ $t('global.detail') }}
      </template>
      <div style="width: 1150px;">
        <hljs v-if="detailModel.errorMsg" :code="detailModel.errorMsg || ''"/>
      </div>
      <!-- <a-row :gutter="24" style="padding: 10px;">
        <a-col>
          <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }" :column="1"
            size="large">
            <a-descriptions-item label="发生时间：">
              <div>{{ detailModel.occurTime }}</div>
            </a-descriptions-item>
            <a-descriptions-item label="系统信息：">
              <div style="width: 1100px;">
              </div>
            </a-descriptions-item>

          </a-descriptions>
        </a-col>
      </a-row> -->
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
import dayjs from "dayjs";
import {notification} from "@/hooks/my-design";
import {getDictLabel} from "@/utils/dict";
import {cleanSystemLog, delSystemLog, getSystemLog, listSystemLog} from "@/api/system/system-log";

//******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_oper_type,
  sys_execute_status,
  sys_oper_cate
} = proxy?.useDict("sys_oper_type", "sys_execute_status", "sys_oper_cate");
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
    title: "系统信息",
    dataIndex: "errorMsg",
    align: 'left',
  },
  {
    title: "发生时间",
    dataIndex: "occurTime",
    align: 'center',
    width: 180,
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    width: 100,
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
  // x: 1210,
};
// 获取当前时间
var dateNow = dayjs().format("YYYY-MM-DD");
var dateBefore = dayjs().subtract(6, 'day').format("YYYY-MM-DD")

const detailVisible = ref(false);
const modalVisible = ref(false);
const modalTitle = ref("");
const modalType = ref(1);

//生成查询条件
const generateFormModel = () => {
  return {
    //系统信息
    title: "",
    //操作时间
    occurTime: [dateBefore, dateNow],
  };
};

//查询项
const searchParams = ref(generateFormModel());


//表格页码发生变化
const onPageChange = (val: number) => {
  pagination.current = val;
  fetchData();
}

//表格每页数量发生变化
const onPageSizeChange = (val: number) => {
  pagination.pageSize = val;
  fetchData();
}

// 查询
const onQueryClick = () => {
  pagination.current = 1;
  selectedKeys.value = [];
  fetchData();
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
      occurTime: '',
    }

    if(searchParams.value.occurTime && searchParams.value.occurTime.length > 0) {
      data.params = {
        beginTime: dayjs(searchParams.value.occurTime[0]).format("YYYY-MM-DD 00:00:00"),
        endTime: dayjs(searchParams.value.occurTime[1]).format("YYYY-MM-DD 23:59:59")
      }
    }else{
      data.params = {
        beginTime: undefined,
        endTime: undefined
      }
    }
    const res: any = await listSystemLog(data);
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
const handleDetail = async (id: any) => {
  let res = await getSystemLog(id)
  if (res.code == 200) {
    detailVisible.value = true;
    detailModel.value = res.data;
  }
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
      res = await delSystemLog(selectedKeys.value);
    } else {
      res = await cleanSystemLog();
    }
    notification(res);
    if (res.code == 200) {
      await fetchData();
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

