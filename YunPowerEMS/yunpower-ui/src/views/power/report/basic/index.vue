<template>
  <div class="container">
    <a-card class="general-card" title="查询表格">
      <!-- 查询条件 -->
      <a-row>
        <a-col :flex="1">
          <a-col :flex="1">
            <a-form :model="formModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                    label-align="left">
              <a-row :gutter="16">
                <a-col :span="8">
                  <a-form-item field="trueName" label="用户姓名">
                    <a-input v-model="formModel.trueName" placeholder="请输入用户姓名" allow-clear />
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item field="phone" label="用户电话">
                    <a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item field="phone" label="用户电话">
                    <a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item field="phone" label="用户电话">
                    <a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item field="phone" label="用户电话">
                    <a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item field="phone" label="用户电话">
                    <a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
                  </a-form-item>
                </a-col>
              </a-row>
            </a-form>
          </a-col>
        </a-col>
        <a-divider style="height: 84px" direction="vertical" />
        <a-col :flex="'86px'" style="text-align: right">
          <a-space direction="vertical" :size="18">
            <a-button type="primary" @click="search">
              <template #icon>
                <icon-search />
              </template>
              查询
            </a-button>
            <a-button @click="reset">
              <template #icon>
                <icon-refresh />
              </template>
              重置
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <a-divider style="margin-top: 0" />
      <!-- 表格 -->
      <a-table row-key="id" :loading="loading" :pagination="pagination" :columns="columns"
               :data="renderData" :bordered="false" @page-change="onPageChange"></a-table>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { computed, reactive, ref } from 'vue';
import useLoading from '@/hooks/loading';
import { UserEntity, UserQueryParams, queryUserList } from '@/api/pv/demo';
import { BasePagination } from '@/api/common';
//生成查询条件
const generateFormModel = () => {
  return {
    trueName: '',
    phone: '',
  };
};
const formModel = ref(generateFormModel());

//加载中
const { loading, setLoading } = useLoading(true);
//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "编号",
    dataIndex: 'id',
  },
  {
    title: "昵称",
    dataIndex: 'nickName',
  },
  {
    title: "姓名",
    dataIndex: 'trueName',
  },
  {
    title: "手机号",
    dataIndex: 'phone',
  },
  {
    title: "注册时间",
    dataIndex: 'registerTime',
  },
  {
    title: "状态",
    dataIndex: 'status',
    slotName: 'status',
  },
  {
    title: "用户地址",
    dataIndex: 'address',
  },
  {
    title: "操作",
    dataIndex: 'operations',
    slotName: 'operations',
  }
]);

//表格数据
const renderData = ref<UserEntity[]>([]);
//表格分页参数
const pagination: any = reactive({ ...BasePagination()});

//查询方法
const search = () => {
  fetchData({
    ...pagination,
    ...formModel.value,
  } as unknown as UserQueryParams);
};

//重置查询
const reset = () => {
  formModel.value = generateFormModel();
};

const onPageChange = (current: number) => {
  fetchData({ ...pagination, current });
};

const fetchData = async (
    params: UserQueryParams = { current: 1, pageSize: 20 }
) => {
  setLoading(true);
  try {
    const { data } = await queryUserList(params);
    renderData.value = data.list;
    pagination.current = params.current;
    pagination.total = data.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};
fetchData();
</script>

<style lang="less" scoped>
:deep(.arco-table-th) {
  &:last-child {
    .arco-table-th-item-title {
      margin-left: 16px;
    }
  }
}

.action-icon {
  margin-left: 12px;
  cursor: pointer;
}

.active {
  color: #0960bd;
  background-color: #e3f4fc;
}

.setting {
  display: flex;
  align-items: center;
  width: 200px;

  .title {
    margin-left: 12px;
    cursor: pointer;
  }
}
</style>
