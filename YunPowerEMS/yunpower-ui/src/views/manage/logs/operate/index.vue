<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :flex="1" style="margin-top: 4px;">
        <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left"
          auto-label-width>
          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-item field="title" :label="$t('manage.logs.operate.systemModules')">
                <a-input v-model="searchParams.title" :placeholder="$t('manage.logs.operate.systemModulesPlaceholder')" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item field="address" :label="$t('manage.logs.operate.operators')">
                <a-input v-model="searchParams.operName" :placeholder="$t('manage.logs.operate.operatorsPlaceholder')" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item field="time" :label="$t('manage.logs.operate.operationTime')">
                <a-range-picker v-model="searchParams.operTime" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-divider style="height: 35px" direction="vertical" />
      <a-col :flex="'86px'" style="margin-top: 4px;">
        <a-space :size="18">
          <a-button type="primary" @click="onQueryClick">
            <template #icon>
              <icon-search />
            </template>
            {{$t('global.search')}}
          </a-button>
          <a-button @click="reset">
            <template #icon>
              <icon-refresh />
            </template>
            {{$t('global.reset')}}
          </a-button>
        </a-space>
      </a-col>
    </a-row>
    <!-- 查询条件 end-->
    <a-divider style="margin: 5px 0 15px;" />

    <!-- 操作条 start-->
    <a-row style="margin-bottom: 16px;">
      <a-col :span="24">
        <a-space>
          <a-button type="primary" @click="handleDeleteClick">
            {{$t('global.batchDeletion')}}
          </a-button>
          <a-button type="primary" status="danger" @click="handleDeleteAllClick">
            {{$t('global.empty')}}
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
      <template #businessType="{ record }">
        <dict-tag :options="sys_oper_type" :value="record.businessType" />
      </template>
      <!-- 操作类别 -->
      <template #operatorType="{ record }">
        <dict-tag :options="sys_oper_cate" :value="record.operatorType" />
      </template>
      <template #status="{ record }">
        <dict-tag :options="sys_execute_status" :value="record.status" />
      </template>
      <template #operate="{ record }">
        <a-button size="small" type="text" status="success" @click="handleDetail(record)">{{$t('global.detail')}}</a-button>
      </template>
    </a-table>
    <!-- 详情 -->
    <a-drawer :width="750" :visible="detailVisible" @ok="handleSubmitForm" @cancel="handleFormCancel"
      :mask-closable="false" hide-cancel>
      <template #title>
        {{$t('global.detail')}}
      </template>
      <a-row :gutter="24" style="padding: 10px;">
        <a-col>
          <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }" :column="1"
            size="large">
            <a-descriptions-item :label="$t('manage.logs.operate.operationModule')">
              <div>{{ detailModel.title }}</div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.operate.loginInformation')">
              <div>{{ detailModel.operName }} / {{ detailModel.operIp }}</div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.operate.requestAddress')">
              <div>{{ detailModel.operUrl }}</div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.operate.requestMode')">
              <div>{{ detailModel.requestMethod }}</div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.operate.operationMethod')">
              <div>{{ detailModel.method }}</div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.operate.requestParameter')">
              <div style="width: 600px;">
                <hljs v-if="detailModel.operParam" :code="detailModel.operParam || ''" :format="true"/>
              </div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.operate.returnParameter')">
              <div style="width: 600px;">
                <hljs v-if="detailModel.operParam" :code="detailModel.jsonResult || ''" :format="true"/>
              </div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.operate.errorMessage')">
              <div style="width: 600px;">
                <hljs v-if="detailModel.errorMsg" :code="detailModel.errorMsg || ''" :format="true"/>
              </div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.operate.operatingTime')">
              <div>{{ detailModel.operTime }}</div>
            </a-descriptions-item>
            <a-descriptions-item :label="$t('manage.logs.operate.operatingState')">
              <div>{{ detailModel.statusStr }}</div>
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>
    </a-drawer>
    <!-- 表格 end-->
    <a-modal width="400px" v-model:visible="modalVisible" class="modal-box">
      <template #title>
        <icon-close-circle v-if="modalTitle == '删除'" size="18" style="color:rgb(var(--red-6)); margin-right: 5px;" />
        <icon-exclamation-circle v-else size="18" style="color:rgb(var(--orange-6)); margin-right: 5px;" />
        {{$t('global.confirmTip')}}
      </template>
      <div style="text-align: center;">你确定要{{ modalTitle }}吗？</div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleModalCancel">{{$t('global.cancel')}}</a-button>
            <a-button type="primary" @click="handleModalOk">{{$t('global.confirm')}}</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
  </a-card>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref, getCurrentInstance } from "vue";
import { Pagination } from "@/api/common";
import { listOperLog, delOperLog, cleanOperLog } from "@/api/system/oper-log";
import { PaginationProps, TableColumnData, TableRowSelection } from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import dayjs from "dayjs";
import { getDictLabel } from "@/utils/dict";
import { notification } from "@/hooks/my-design";

//******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_oper_type, sys_execute_status, sys_oper_cate } = proxy?.useDict("sys_oper_type", "sys_execute_status", "sys_oper_cate");
//******* 这里写动态获取下拉列表 end ******

//加载中
const { loading, setLoading } = useLoading(true);
//表格数据
const renderData = ref<any[]>([]);
//表格分页配置
const pagination: PaginationProps = Pagination;
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "系统模块",
    dataIndex: "title",
    align: 'left',
    fixed: 'left',
    width: 150,
  },
  {
    title: "业务类型",
    dataIndex: "businessType",
    slotName: "businessType",
    align: 'center',
    fixed: 'left',
    width: 100,
  },
  {
    title: "操作类型",
    dataIndex: "operatorType",
    slotName: "operatorType",
    align: 'center',
    fixed: 'left',
    width: 100,
  },
  {
    title: "操作人员",
    width: 150,
    ellipsis: true,
    align: 'center',
    dataIndex: "operName",
  },
  {
    title: "操作部门",
    align: 'center',
    width: 150,
    dataIndex: "deptName",
  },
  {
    title: "主机",
    ellipsis: true,
    width: 150,
    align: 'center',
    dataIndex: "operIp",
  },
  {
    title: "操作地点",
    dataIndex: "operLocation",
    align: 'center',
    width: 150,
  },
  {
    title: "操作状态",
    dataIndex: "status",
    slotName: 'status',
    align: 'center',
    width: 100,
  },
  {
    title: "操作时间",
    dataIndex: "operTime",
    align: 'center',
    width: 180,
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    width: 100,
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
  x: 1210,
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
    operName: "",
    //操作时间
    operTime: [dateBefore, dateNow],
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
  // console.log(searchParams);
  selectedKeys.value = [];
  pagination.current = 1;
  await fetchData();
}

const reset = async () => {
  selectedKeys.value = [];
  pagination.current = 1;
  searchParams.value = generateFormModel();
  await fetchData();
}

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const data:any = {
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
      operTime: '',
    }

    if(searchParams.value.operTime && searchParams.value.operTime.length > 0){
      data.params = {
        beginTime: dayjs(searchParams.value.operTime[0]).format("YYYY-MM-DD 00:00:00"),
        endTime: dayjs(searchParams.value.operTime[1]).format("YYYY-MM-DD 23:59:59")
      }
    }else{
      data.params = {
        beginTime: undefined,
        endTime: undefined
      }
    }
    const res: any = await listOperLog(data);
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
    notification({ code: 201, msg: "请选择要删除的数据" })
    return;
  }
  modalVisible.value = true;
  modalTitle.value = "删除所选记录";
  modalType.value = 1;
  console.log(selectedKeys.value);
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
        notification({ code: 201, msg: "请选择要删除的数据" })
        return;
      }
      res = await delOperLog(selectedKeys.value);
    } else {
      res = await cleanOperLog();
    }
    notification(res);
    if (res.code == 200) {
      await fetchData();
    }
  }catch (e) {
    console.error(e);
  }finally {
    setLoading(false);
  }
};


/**
 * 删除弹框-取消
 */
const handleModalCancel = () => {
  modalVisible.value = false;
  console.log("取消");
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

