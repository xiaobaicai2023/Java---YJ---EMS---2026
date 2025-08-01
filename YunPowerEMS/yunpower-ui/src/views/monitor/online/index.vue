<template>
  <div>
    <a-card class="content">

      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left"
            auto-label-width>
            <a-row :gutter="16">
              <a-col :span="6">
                <a-form-item field="ipaddr" :label="$t('monitor.online.address')">
                  <a-input v-model="searchModel.ipaddr" :placeholder="$t('monitor.online.addressPlaceholder')"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item field="userName" :label="$t('monitor.online.username')">
                  <a-input v-model="searchModel.userName" :placeholder="$t('monitor.online.namePlaceholder')"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-button type="primary" @click="search">
                  <template #icon>
                    <icon-search />
                  </template>
                  {{$t('global.search')}}
                </a-button>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

      <!-- 分割线 -->
      <a-divider style="margin-top: 0" />

      <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
        :scroll="{ x: 1000 }" :columns="(tabColumns as TableColumnData[])" :data="renderData" @page-change="onPageChange"
        @page-size-change="onPageSizeChange">
        <template #loginTime="{ record }">
          {{ parseTime(record.loginTime) }}
        </template>
        <template #operate="{ record }">
          <a-popconfirm position="left" :content="$t('monitor.online.userOut')" @ok="handleForceLogout(record.tokenId)">
            <a-button size="small" type="text" status="danger">{{$t('global.forceOut')}}</a-button>
          </a-popconfirm>
        </template>
      </a-table>
    </a-card>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { TableColumnData } from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import { BasePagination } from "@/api/common";
import { forceLogout, listOnline } from "@/api/system/online";
import dayjs from 'dayjs';
import { notification } from "@/hooks/my-design";

/*************************** 变量区域 begin ***************************/


//生成查询条件对象
const generateSearchModel = () => {
  return {
    ipaddr: undefined,
    userName: undefined
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());
//表格分页参数
const pagination: any = reactive({ ...BasePagination() });
//加载中
const { loading, setLoading } = useLoading(true);
//站点列
const tabColumns: TableColumnData[] = [
  {
    title: "会话编号",
    dataIndex: "tokenId",
    align: 'left',
    width: 200,
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "登录名称",
    dataIndex: "userName",
    align: 'left',
    width: 150,
  },
  {
    title: "主机",
    dataIndex: "ipaddr",
    align: 'left',
    width: 150,
  },
  {
    title: "登录地点",
    dataIndex: "loginLocation",
    align: 'left',
    width: 200,
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "操作系统",
    dataIndex: "os",
    align: 'left',
    width: 120,
  },
  {
    title: "浏览器",
    dataIndex: "browser",
    align: 'left',
    width: 120,
  },
  {
    title: "登录时间",
    dataIndex: "loginTime",
    slotName: "loginTime",
    align: 'left',
    width: 180,
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 80,
    slotName: 'operate',
    align: 'center',
    fixed: "right"
  },
];
//站点数据
const renderData = ref<TableColumnData[]>([]);

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

/**
 * 表格页码发生变化
 * @param val
 */
const onPageChange = async (val: number) => {
  pagination.current = val;
  await fetchData();
}

/**
 * 表格每页数量发生变化
 * @param val 值
 */
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchData();
}

//重置查询条件
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

const parseTime = (timestamp: any) => {
  return dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss');
}

const handleForceLogout = async (token: any) => {
  let result = await forceLogout(token);
  notification(result);
  if (result.code == 200) {
    await fetchData();
  }
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listOnline({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchModel.value
    });
    renderData.value = res.rows;
    pagination.total = res.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
})
</script>
