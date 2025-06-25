<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :flex="1" style="margin-top: 4px;">
        <a-form :model="searchParams" label-align="left" auto-label-width>
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item field="station" :label="$t('manage.account.authority.siteName')">
                <a-input v-model="searchParams.deptName" :placeholder="$t('manage.account.authority.siteNamePlaceholder')" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-button type="primary" @click="fetchData">
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

    <a-divider style="margin-top: 0" />

    <!-- 表格 start-->
    <a-table row-key="id" :columns="tabColumns" :data="renderData" :loading="loading" show-empty-tree
      :bordered="{ wrapper: true, cell: true }" :default-expand-all-rows="true">
      <template #originType="{ record }">
        <a-tag v-if="record.originType === 1" color="rgb(var(--arcoblue-6))">{{$t('manage.account.authority.firm')}}</a-tag>
        <a-tag v-if="record.originType === 2 && record.isCanSelect === 0 " color="rgb(var(--green-6))">{{$t('manage.account.authority.group')}}</a-tag>
        <a-tag v-if="record.originType === 2 && record.isCanSelect === 1 " color="rgb(var(--orange-6))">{{$t('manage.account.authority.site')}}</a-tag>
      </template>
      <template #stopFlag="{ record }">
        <stop-flag :value="record.stopFlag"></stop-flag>
      </template>
      <template #operate="{ record }">
        <a-button size="small" type="text" status="success" @click="viewRoleClick(record)"
          v-if="record.originType === 2">{{$t('manage.account.authority.viewMember')}}
        </a-button>
      </template>
    </a-table>
    <!-- 表格 end-->

    <!-- 查看成员弹出层 start -->
    <a-modal width="auto" v-model:visible="roleModalVisible" class="modal-box" :footer="false">
      <template #title>
        【{{ roleModalTitle }}】{{$t('manage.account.authority.permissionMember')}}
      </template>
      <a-table row-key="id" :bordered="{ wrapper: true, cell: true }" hide-expand-button-on-empty
        :columns="userTabColumns" :data="filteredData1">
        <template #stopFlag="{ record }">
          {{ record.stopFlag === 1 ? $t('global.enable') : record.stopFlag === 0 ? $t('global.forbidden') : '' }}
        </template>
      </a-table>
    </a-modal>
    <!-- 查看成员弹出层 end -->
  </a-card>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { listDept, getDeptMember } from "@/api/manage/account/authority";
import { TableColumnData } from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import { handleTree } from "@/utils/ruoyi";

/*************************** 变量区域 begin ***************************/

//加载中
const { loading, setLoading } = useLoading(true);
//站点列
const tabColumns: TableColumnData[] = [
  // {
  //   title: "编号",
  //   dataIndex: "id",
  //   slotName: "id",
  //   align: 'center',
  //   width: 200
  // },
  {
    title: "站点名称",
    dataIndex: "deptName",
    slotName: "deptName",
    width: 300
  },
  {
    title: "站点编码",
    dataIndex: "deptSn",
    width: 150
  },
  {
    title: "站点类型",
    dataIndex: "originType",
    slotName: "originType",
    align: 'center',
    width: 100
  },
  {
    title: "启用状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
    width: 180,
    align: 'center'
  }
];
//站点数据
const renderData = ref<TableColumnData[]>([]);
// 成员列
const userTabColumns: TableColumnData[] = [
  {
    title: "用户姓名",
    dataIndex: "leader",
    width: 240
  },
  {
    title: "登录账号",
    dataIndex: "deptSn",
    width: 240
  },
  {
    title: "手机",
    dataIndex: "mobile",
    align: 'center',
    width: 140
  },
  {
    title: "用户状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100
  }
];
// 成员数据
const filteredData1 = ref([])
//查询项
const searchParams = ref({
  deptName: ""
});

// 站点展示表格单元格合并设置
const roleModalTitle = ref<string>('');
const roleModalVisible = ref(false);

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

/**
 * 查询站点
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listDept(searchParams.value);
    renderData.value = handleTree(res.data);
  } catch (err) {
  } finally {
    setLoading(false);
  }
};

/**
 * 查看成员
 * @param record 记录
 */
const viewRoleClick = async (record: any) => {
  const res = await getDeptMember(record.id)
  filteredData1.value = res.data
  roleModalTitle.value = record.deptName;
  roleModalVisible.value = true;
};

/*************************** 方法区域 end ***************************/

onMounted(() => {
  fetchData();
})

</script>

<style lang="less" scoped>
.group-name {
  font-weight: 600;
}
</style>
