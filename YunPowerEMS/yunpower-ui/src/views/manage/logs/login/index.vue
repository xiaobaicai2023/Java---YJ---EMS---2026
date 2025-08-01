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
              <a-form-item field="name" :label="$t('manage.logs.login.loginName')">
                <a-input v-model="searchParams.userName" :placeholder="$t('manage.logs.login.loginNamePlaceholder')"
                         allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item field="address" :label="$t('manage.logs.login.loginAddress')">
                <a-input v-model="searchParams.ipaddr" :placeholder="$t('manage.logs.login.loginAddressPlaceholder')"
                         allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item field="time" :label="$t('manage.logs.login.loginTime')">
                <a-range-picker v-model="searchParams.loginTime"/>
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
    <a-divider style="margin-top: 0"/>

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
    </a-table>
    <!-- 表格 end-->
    <a-modal width="400px" v-model:visible="modalVisible" class="modal-box">
      <template #title>
        <icon-close-circle v-if="modalTitle == '删除'" size="18" style="color:rgb(var(--red-6)); margin-right: 5px;"/>
        <icon-exclamation-circle v-else size="18" style="color:rgb(var(--orange-6)); margin-right: 5px;"/>
        {{ $t('global.confirmTip') }}
      </template>
      <div style="text-align: center;">你确定要【{{ modalTitle }}】吗？</div>
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
import {computed, onMounted, reactive, ref} from "vue";
import {BasePagination} from "@/api/common";
import {notification} from "@/hooks/my-design";
import {listLogininfor, delLogininfor, cleanLogininfor} from "@/api/system/logininfor";
import {PaginationProps, TableColumnData, TableRowSelection} from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import dayjs from "dayjs";

//加载中
const {loading, setLoading} = useLoading(true);
//表格数据
const renderData = ref<any[]>([]);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "登录账号",
    dataIndex: "userName",
    align: 'center',
    width: 180,
  },
  {
    title: "登录地址",
    dataIndex: "ipaddr",
    align: 'center',
    width: 180,
  },
  {
    title: "登录地点",
    align: 'center',
    width: 200,
    ellipsis: true,
    tooltip: {position: 'top'},
    dataIndex: "loginLocation",
  },
  {
    title: "浏览器",
    align: 'center',
    width: 200,
    dataIndex: "browser",
  },
  {
    title: "操作系统",
    align: 'center',
    width: 200,
    dataIndex: "os",
  },
  {
    title: "操作信息",
    ellipsis: true,
    align: 'left',
    tooltip: {position: 'top'},
    dataIndex: "msg",
  },
  {
    title: "登录时间",
    dataIndex: "loginTime",
    align: 'center',
    width: 180,
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
  // x: 1500,
};
// 获取当前时间
var dateNow = dayjs().format("YYYY-MM-DD");
var dateBefore = dayjs().subtract(1, 'month').format("YYYY-MM-DD")

const modalVisible = ref(false);
const modalTitle = ref("");
const modalType = ref(1);

//生成查询条件
const generateFormModel = () => {
  return {
    //登录名称
    userName: "",
    //登陆地址
    ipaddr: "",
    //登录时间
    loginTime: [dateBefore, dateNow],
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
  await fetchData();
}

const reset = async () => {
  pagination.current = 1;
  searchParams.value = generateFormModel();
  await fetchData();
}

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const data: any = {
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
      loginTime: '',
    }

    if(searchParams.value.loginTime && searchParams.value.loginTime.length>0){
      data.params = {
        beginTime: dayjs(searchParams.value.loginTime[0]).format("YYYY-MM-DD 00:00:00"),
        endTime: dayjs(searchParams.value.loginTime[1]).format("YYYY-MM-DD 23:59:59")
      }
    }else{
      data.params = {
        beginTime: undefined,
        endTime: undefined
      }
    }
    const res: any = await listLogininfor(data);
    renderData.value = res.rows;
    pagination.total = res.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};


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
        notification({code: 201, msg: "请选择要删除的数据"})
        return;
      }
      res = await delLogininfor(selectedKeys.value);
    } else {
      res = await cleanLogininfor();
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
  console.log("取消");
}

/**
 * 实例创建完成后立即执行
 */
onMounted(async () => {
  await fetchData();
})
</script>
<style lang="less" scoped>
.row-mp-30 {
  margin-top: 30px;
  padding-right: 30px;
}
</style>
