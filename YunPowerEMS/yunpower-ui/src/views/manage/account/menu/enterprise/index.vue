<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :flex="1">
        <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                label-align="left"
                auto-label-width>
          <a-row :gutter="24">
            <a-col :span="22">
              <a-form-item field="menu" label="企业名称">
                <a-input v-model="searchParams.menuName" placeholder="请输入企业名称，支持模糊查询" allow-clear/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-col :span="16" style="text-align: left">
        <a-space :size="18">
          <a-divider style="height: 35px" direction="vertical" />
          <a-button type="primary" @click="fetchData">
            <template #icon>
              <icon-search/>
            </template>
            查询
          </a-button>
        </a-space>
      </a-col>
    </a-row>
    <!-- 查询条件 end-->
    <a-divider style="margin-top: 0"/>

   <!-- 操作条 start-->
    <a-row style="margin-bottom: 16px;">
      <a-col :span="2">
        <a-space :size="15">
          <a-button type="primary" @click="createClick">
            <template #icon>
              <icon-plus/>
            </template>
            配置菜单
          </a-button>

          <a-button @click="loadClick">
            展开/折叠
          </a-button>

        </a-space>
      </a-col>
    </a-row>
    <!-- 操作条 end -->


    <!-- 表格 start-->
    <a-table  v-if="renderData && renderData.length" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
             :columns="(tabColumns as TableColumnData[])" :data="renderData" :default-expand-all-rows="true"
             @page-change="onPageChange" @page-size-change="onPageSizeChange" :row-selection="rowSelection" >
      <template #menuName="{ record }">
        <div v-if="record.isMenuGroup" class="group-name">
          {{ record.mainName }}
        </div>
        <div v-else-if="record.isSubGroup" class="group-name">
          {{ record.subName }}
        </div>
        <div v-else-if="!record.isMenuGroup" class="group-name">
          {{ record.menuName }}
        </div>
      </template>
      <template #menuType="{ record }">
        <a-space v-if="record.menuType==='目录'">
         <a-tag color="rgb(var(--arcoblue-6))">目录</a-tag>
        </a-space>
        <a-space v-if="record.menuType==='菜单'">
         <a-tag color="rgb(var(--green-6))">菜单</a-tag>
        </a-space>
        <a-space v-if="record.menuType==='按钮'">
         <a-tag color="rgb(var(--orange-6))">按钮</a-tag>
        </a-space>
      </template>
      <template #status="{ record }">
        <a-space v-if="record.status===1">
          <a-tag color="rgb(var(--green-6))">显示</a-tag>
        </a-space>
        <a-space v-if="record.status===0">
          <a-tag color="rgb(var(--red-5))">隐藏</a-tag>
        </a-space>
        <a-space v-else-if="record.status===2">
          -
        </a-space>
      </template>
    </a-table>
    <!-- 表格 end-->

    <!-- 添加右弹层 start -->
    <a-drawer :width="auto" :visible="createDrawerVisible" @ok="createDrawerHandleOk" @cancel="createDrawerHandleCancel" :mask-closable="false">
      <template #title>
        配置菜单
      </template>

      <a-row :gutter="1" class="row-mp-30">
        <a-space class="tree-content" direction="vertical"  :size="10" fill >
          <div>
            <a-checkbox class="all-check-box" type="primary" @click="toggleExpanded1">
              {{
                expandedKeys1?.length ? '折叠' : '展开'
              }}
            </a-checkbox>
              <a-checkbox class="all-check-box" type="primary" @click="toggleChecked1">
              {{
                checkedKeys1?.length ? '全不选' : '全选'
              }}
            </a-checkbox>
          </div>

          <a-tree
            :checkable="true" :check-strictly="true"
            v-model:selected-keys="selectedKeys1"
            v-model:checked-keys="checkedKeys1"
            v-model:expanded-keys="expandedKeys1"
            :data="treeData1"
          >
            <template #icon>
              <IconStar/>
            </template>
          </a-tree>
        </a-space>
      </a-row>
    </a-drawer>
    <!-- 添加右弹层 end -->

    <!-- 对话框 start -->
    <a-modal width="400px"  v-model:visible="modalVisible" class="modal-box">
      <template #title>
        <icon-close-circle v-if="modalTitle == '删除'" size="18" style="color:rgb(var(--red-6)); margin-right: 5px;"/>
        <icon-exclamation-circle v-else size="18" style="color:rgb(var(--orange-6)); margin-right: 5px;"/>
        确认提示
      </template>
      <div style="text-align: center;">你确定要【{{ modalTitle }}】当前记录吗？</div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleModalCancel">取消</a-button>
            <a-button type="primary" @click="handleModalOk">确认</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
    <!-- 对话框 end -->

  </a-card>
</template>

<script lang="ts" setup>
import {computed, ref,Ref,reactive} from "vue";
import { BasePagination } from "@/api/common";
import {AccountMenuEntity, AccountMenuParams,listMenu} from "@/api/manage/account/menu";
import {PaginationProps, TableColumnData} from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import {FormInstance} from '@arco-design/web-vue/es/form';

const value0 = ref(0);
const expandedKeys = ref([]);
const selectedRow: Ref<AccountMenuEntity | null> = ref(null);
const editDrawerVisible = ref(false);
const createDrawerVisible = ref(false);
const rowSelection = {
  type: 'radio'
};

const allCheckedKeys1: string[] = ['1', '1-1', '1-2', '1-3', '1-4', '1-4-1', '1-4-2', '1-4-3','2', '2-1', '2-2', '2-3'];
const allExpandedKeys1: string[] = ['1', '2', '1-4'];

const selectedKeys1: Ref<string[]> = ref([]);
const checkedKeys1: Ref<string[]> = ref([]);
const expandedKeys1: Ref<string[]> = ref([]);

const toggleChecked1 = () => {
  checkedKeys1.value = checkedKeys1.value.length ? [] : allCheckedKeys1;
};

const toggleExpanded1 = () => {
  expandedKeys1.value = expandedKeys1.value.length ? [] : allExpandedKeys1;
};

// 树的数据
const treeData1 = reactive([{
  title: '北区中心',
  key: '1',
  children: [
    {
      title: '分接箱1柜',
      key: '1-1',
    },
    {
      title: '分接箱2柜',
      key: '1-2',
    },
    {
      title: '低压',
      key: '1-3',
    },
    {
      title: '分接箱',
      key: '1-4',
      children: [
        {
          title: '消防电源',
          key: '1-4-1',

        },
        {
          title: '库房电源',
          key: '1-4-2',
        },
        {
          title: '备用01',
          key: '1-4-3',

        },
      ]
    },
  ]
}, {
  title: '南区中心',
  key: '2',
  children: [
    {
      title: '电梯电源',
      key: '2-1',
    },
    {
      title: '大楼监控',
      key: '2-2',
    },
    {
      title: '厨房电源',
      key: '2-3',
    },
  ]
}])

//加载中
const {loading, setLoading} = useLoading(true);
//表格数据
const renderData = ref<AccountMenuEntity[]>([]);
//表格分页配置
const pagination: any = reactive({ ...BasePagination()});
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "菜单名称",
    dataIndex: "menuName",
    slotName:"menuName",
    width:240
  },
  {
    title: "排序",
    dataIndex: "sort",
    width:140
  },
  {
    title: "请求地址",
    dataIndex: "requestAddress",
    width:140
  },
  {
    title: "权限标识",
    dataIndex: "permissionTag",
    width:140
  },
  {
    title: "菜单类型",
    dataIndex: "menuType",
    slotName:"menuType",
    align: 'center',
    width:100
  },
  {
    title: "菜单显示",
    dataIndex: "status",
    slotName: "status",
    align: 'center',
    width:100
  }
]);

//生成查询条件
const generateFormModel = () => {
  return {
    id: 0,
    //菜单名称
    menuName: "",
  };
};

//查询项
const searchParams = ref(generateFormModel());

//重置查询条件
const reset = () => {
  searchParams.value = generateFormModel();
}

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

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const { data } = await listMenu({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
    } as unknown as AccountMenuParams);
    expandedKeys.value = [];
    renderData.value = data.list;
    data.list.forEach((value) => {
      expandedKeys.value.push(value.key);
    });
    pagination.total = data.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};
fetchData();

const formData = ref({
  parentId:'',
  openId:'',
  mainName:'',
  isMenuGroup:true,
  subName:'',
  isSubGroup:true,
  key:0,
  menuName: '',
  sort:'',
  requestAddress:'',
  permissionTag:'',
  menuType:'',
  status: 0,
  menuIcon:'',
  menuDisplay:'',
  remark:'',
});
const formRef = ref<FormInstance>();

//上级目录 通过接口获取
const parentList = reactive([
  {
    value: 0,
    label: "根目录",
  },
  {
    value: 1,
    label: "北区一号",
  }
])
// 打开方式
const openList = reactive([
  {
    value: 0,
    label: "打开方式1",
  },
  {
    value: 1,
    label: "打开方式2",
  }
])

// 编辑页
const editMenuClick = (val:any) => {
  selectedRow.value = val;
  editDrawerVisible.value = true;
};

//编辑页确定
const editDrawerHandleOk =async () => {
  const res = await formRef.value?.validate();
  if (!res) {

    //调用后端添加接口
    editDrawerVisible.value = false;
    pagination.current = 1;
    fetchData();
  } else {
    console.log(formData.value)
  }
};

//编辑页取消
const editDrawerHandleCancel = () => {
  editDrawerVisible.value = false;
}

// 新建用户
const createClick = () => {
  createDrawerVisible.value = true;
};
//新建用户页确定
const createDrawerHandleOk = async () => {
  const res = await formRef.value?.validate();
  if (!res) {
    console.log({...formData.value})

    //调用后端添加接口
    createDrawerVisible.value = false;
    pagination.current = 1;
    fetchData();
  } else {
    console.log(formData.value)
  }
}
//新建用户页取消取消
const createDrawerHandleCancel = () => {
  createDrawerVisible.value = false;
}

const modalVisible = ref(false);
const modalTitle = ref("");
const modalBizId = ref("");
const handleModalClick = (title: string, bizId: string) => {
  modalVisible.value = true;
  modalTitle.value = title;
  modalBizId.value = bizId;
};
const handleModalOk = () => {
  modalVisible.value = false;
};
const handleModalCancel = () => {
  modalVisible.value = false;
}

</script>
<style lang="less" scoped>
.row-mp-30 {
  margin-top: 30px;
  padding-right: 30px;
}
.group-name {
  font-weight: 600;
}
.all-check-box {
  padding: 0 0 10px 15px;
}

</style>
