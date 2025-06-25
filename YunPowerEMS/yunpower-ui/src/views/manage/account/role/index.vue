<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :span="8" style="margin-top: 4px;">
        <a-form :model="searchParams" label-align="left" auto-label-width>
          <a-row>
            <a-col>
              <a-form-item field="roleName" :label="$t('manage.account.role.roleName')">
                <a-input v-model="searchParams.roleName" :placeholder="$t('manage.account.role.roleNamePlaceholder')"
                         allow-clear/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-col :span="8" style="text-align: left">
        <a-space :size="18">
          <a-divider style="height: 35px" direction="vertical"/>
          <a-button type="primary" @click="handleQuery">
            <template #icon>
              <icon-search/>
            </template>
            {{ $t('global.search') }}
          </a-button>
        </a-space>
      </a-col>
    </a-row>

    <!-- 查询条件 end-->
    <a-divider style="margin-top: 0"/>

    <!-- 操作条 start-->
    <a-row style="margin-bottom: 16px;">
      <a-col :span="12">
        <a-button type="primary" @click="handleRoleAdd">

          <template #icon>
            <icon-plus/>
          </template>
          {{ $t('manage.account.role.creatingRole') }}
        </a-button>
      </a-col>
    </a-row>
    <!-- 操作条 end-->

    <!-- 表格 start-->
    <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
             :scroll="{ x: 1000 }" :columns="(tabColumnsOfRole as TableColumnData[])" :data="renderData"
             @page-change="onPageChange" @page-size-change="onPageSizeChange">

      <template #stopFlag="{ record }">
        <!-- {{ record.stopFlag == 0 ? '启用' : '停用' }} -->
        <stop-flag :value="record.stopFlag"/>
      </template>

      <template #dataScope="{ record }">
        <dict-tag :options="sys_data_auth_type" :value="record.dataScope"/>
      </template>

      <template #operate="{ record }">
        <a-button v-if="!record.admin" size="small" type="text"
                  @click="handleModalClick(record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable'), record.id, record.stopFlag == 0 ? 1 : 0, 0)"
                  :status="record.stopFlag == 1 ? 'normal' : 'warning'">
          {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
        </a-button>
        <a-button v-if="!record.admin" size="small" type="text" status="success"
                  @click="handleRoleUpdate(record.id)">{{ $t('global.edit') }}
        </a-button>
        <a-button v-if="!record.admin" size="small" type="text" status="danger"
                  @click="handleModalClick('删除', record.id, 0, 1)">{{ $t('global.delete') }}
        </a-button>
        <a-button v-if="!record.admin" size="small" type="text" status="success"
                  @click="handleUpdateDept(record.id, record.dataScope)">{{ $t('manage.account.role.dataPermission') }}
        </a-button>
        <a-button v-if="!record.admin" size="small" type="text" status="success"
                  @click="handleViewAuthUserClick(record.id, record.roleName)">
          {{ $t('manage.account.authority.viewMember') }}
        </a-button>
      </template>
    </a-table>
    <!-- 表格 end-->

    <!-- 添加右弹层 start -->

    <a-drawer :width="1000" :visible="formRoleDrawer.visible && !formRoleDrawer.editDept"
              :ok-loading="formRoleDrawer.loading" @ok="handleRoleSubmitForm"
              @cancel="handleRoleFormCancel" :mask-closable="false">
      <template #title>
        {{
          formRoleDrawer.formModel.id && formRoleDrawer.formModel.id > 0 ? $t('manage.account.role.editorRole') :
              $t('manage.account.role.addRole')
        }}
      </template>
      <a-form ref="formRef" :model="formRoleDrawer.formModel" class="form" label-align="right"
              :rules="formRoleDrawer.rules" auto-label-width>
        <a-spin style="width:100%;height:100%" :loading="formRoleDrawer.loading">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-card :bordered="false" :title="$t('manage.account.role.roleInformation')">
                <!-- 角色名称 -->
                <a-form-item field="roleName" :label="$t('manage.account.role.roleName')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formRoleDrawer.formModel.roleName" :max-length="20" allow-clear
                           :placeholder="$t('manage.account.role.roleNamePlaceholder')"/>
                </a-form-item>
                <!-- 权限字符 -->
                <a-form-item field="roleKey" :label="$t('manage.account.role.PermissionCharacter')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <template #extra>
                  <span>
                    <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                    控制器中定义的权限字符，如：'@PreAuthorize(\'@ss.hasRole("admin")\')
                  </span>
                  </template>
                  <a-input v-model="formRoleDrawer.formModel.roleKey" allow-clear/>
                </a-form-item>
                <!-- 显示顺序 -->
                <a-form-item field="orderNum" :label="$t('global.displaySequence')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input-number v-model="formRoleDrawer.formModel.orderNum"/>
                </a-form-item>
                <!-- 启用状态 -->
                <a-form-item field="stopFlag" :label="$t('manage.account.role.enabledState')">
                  <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                            v-model="formRoleDrawer.formModel.stopFlag">

                    <template #checked>{{ $t('global.normal') }}</template>

                    <template #unchecked>{{ $t('global.outOfService') }}</template>
                  </a-switch>
                </a-form-item>
                <a-form-item field="remark" :label="$t('manage.account.role.remarks')">
                  <a-textarea v-model="formRoleDrawer.formModel.remark" :max-length="200" allow-clear
                              :placeholder="$t('manage.account.role.remarksTip')"/>
                </a-form-item>
              </a-card>
            </a-col>
            <a-col :span="12">
              <a-card :bordered="false" :title="$t('manage.account.role.MenuPermissions')">
                <a-form-item field="menuScope" :label="$t('manage.account.role.MenuPermissions')">
                  <a-select v-model="formRoleDrawer.formModel.menuScope" allow-clear
                            :placeholder="$t('manage.account.role.menuPermissionSelect')" @change="menuScopeChange">
                    <a-option v-for="dict in sys_menu_auth_type" :key="dict.value" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>
                <a-form-item v-if="formRoleDrawer.formModel.menuScope == '3'" field="menuPermission"
                             :label="$t('manage.account.role.menuLabel')">
                  <a-row>
                    <a-col>
                      <a-checkbox v-model="expandMenuValue"
                                  @change="handleMenuTreeExpand">{{ $t('global.expand') }}/{{ $t('global.fold') }}
                      </a-checkbox>
                      <a-checkbox v-model="checkedAllMenuValue"
                                  @change="handleMenuTreeCheckedAll">
                        {{ $t('global.checkAll') }}/{{ $t('global.checkOutAll') }}
                      </a-checkbox>
                    </a-col>
                    <a-col style="border-radius: 2px;border: 1px solid var(--color-neutral-4);">
                      <div class="tree-container">
                        <div class="tree-box">
                          <a-tree ref="menuTreeRef" checked-strategy="all" :data="formRoleDrawer.menuTreeList"
                                  :checkable="true" v-model:checked-keys="formRoleDrawer.formModel.menuIds"
                                  :fieldNames="{
        key: 'id',
        title: 'label',
        children: 'children'
      }" @check="handleMenuTreeCheck"/>
                        </div>
                      </div>
                    </a-col>
                  </a-row>
                </a-form-item>
              </a-card>
            </a-col>
          </a-row>
        </a-spin>
      </a-form>
    </a-drawer>

    <!-- 数据权限 start -->
    <a-drawer :width="500" :visible="formRoleDrawer.visible && formRoleDrawer.editDept" :ok-loading="formRoleDrawer.loading" @ok="handleDeptSubmitForm"
              @cancel="handleRoleFormCancel" :mask-closable="false">

      <template #title>
        {{ $t('manage.account.role.dataPermission') }}
      </template>
      <a-form ref="dataFormRef" :model="formRoleDrawer.formModel" class="form" label-align="right"
              :rules="formRoleDrawer.rules" auto-label-width>
        <a-spin style="width:100%;height:100%" :loading="formRoleDrawer.loading">
          <a-row :gutter="16">
            <a-col v-if="formRoleDrawer.editDept">
              <a-card :bordered="false">
                <a-form-item field="menuScope" :label="$t('manage.account.role.MenuPermissions')">
                  <a-select v-model="formRoleDrawer.formModel.dataScope" allow-clear
                            :placeholder="$t('manage.account.role.permissionTip')" @change="dataScopeChange">
                    <a-option v-for="dict in sys_data_auth_type" :key="dict.value" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>
                <a-form-item v-if="formRoleDrawer.formModel.dataScope == '2'" field="dataPermission"
                             :label="$t('manage.account.role.menuLabel')">
                  <a-row>
                    <a-col>
                      <a-checkbox v-model="expandDeptValue"
                                  @change="handleDeptTreeExpand">{{ $t('global.expand') }}/{{ $t('global.fold') }}
                      </a-checkbox>
                      <a-checkbox v-model="checkedAllDeptValue"
                                  @change="handleDeptTreeCheckedAll">
                        {{ $t('global.checkAll') }}/{{ $t('global.checkOutAll') }}
                      </a-checkbox>
                    </a-col>
                    <a-col style="border-radius: 2px;border: 1px solid var(--color-neutral-4);">
                      <div class="tree-container">
                        <div class="tree-box">
                          <a-tree ref="deptTreeRef" :data="formRoleDrawer.deptTreeList" :checkable="true"
                                  v-model:checked-keys="formRoleDrawer.formModel.deptIds" :fieldNames="{
        key: 'id',
        title: 'label',
        children: 'children'
      }" @check="handleDeptTreeCheck"/>
                        </div>
                      </div>
                    </a-col>
                  </a-row>
                </a-form-item>
              </a-card>
            </a-col>
          </a-row>
        </a-spin>
      </a-form>
    </a-drawer>
    <!-- 数据权限 end -->

    <!-- 添加右弹层 end -->

    <!-- 查看成员弹出层 start -->
    <a-modal width="auto" v-model:visible="authUserModalVisible" class="modal-box" :footer="false" title-align="start">

      <template #title>
        【{{ authUserSearchParams.roleName }}】{{ $t('manage.account.role.roleMember') }}
      </template>
      <a-row style="margin-bottom: 24px;">
        <a-button type="primary" @click="handleAuthUserAddClick">

          <template #icon>
            <icon-plus/>
          </template>
          {{ $t('manage.account.role.addUser') }}
        </a-button>
      </a-row>
      <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }"
               :pagination="authUserPagination" :columns="(tabColumnsOfUser as TableColumnData[])"
               :data="authUserRenderData">

        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"/>
        </template>

        <template #operate="{ record }">
          <a-button size="small" type="text"
                    @click="handleAuthUserCanleClick('取消授权', record.id, authUserSearchParams.roleId)">
            {{ $t('manage.account.role.CancelAuthorization') }}
          </a-button>
        </template>
      </a-table>
    </a-modal>
    <!-- 查看成员弹出层 end -->

    <!-- 添加用户对话框 -->
    <a-modal width="auto" v-model:visible="addAuthUserModalVisible" :footer="false" title-align="start"
             @close="addAuthUserModalClose">

      <template #title>
        {{ $t('manage.account.role.userSelect') }}
      </template>
      <div>
        <a-form ref="userFormRef" :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                label-align="left" auto-label-width>
          <a-row :gutter="24">
            <a-col :span="10">
              <a-form-item field="userName" :label="$t('manage.account.role.userName')">
                <a-input v-model="authUserSearchParams.nickName" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="10">
              <a-form-item field="phoneNumber" :label="$t('manage.account.role.phoneNumber')">
                <a-input v-model="authUserSearchParams.mobile" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :flex="'86px'" style="text-align: right">
              <a-space :size="18" :span="6">
                <a-button type="primary" @click="fetchUnallocatedUserList">

                  <template #icon>
                    <icon-search/>
                  </template>
                  {{ $t('global.search') }}
                </a-button>
              </a-space>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
               :columns="(tabColumnsOfUserAdd as TableColumnData[])" :data="addAuthUserRenderData"
               :row-selection="rowSelection" v-model:selectedKeys="selectedKeys">

        <template #status>
          {{ $t('global.enable') }}
        </template>
      </a-table>
      <div style="text-align: center">
        <a-button @click="handleAuthUserAddClickOk" type="primary" style="margin-top: 20px;">添加选中用户</a-button>
      </div>
    </a-modal>

    <!-- 删除提示框 -->
    <a-modal width="400px" v-model:visible="modalVisible" class="modal-box">
      <template #title>
        <icon-close-circle v-if="modalBizType == 1" size="18" style="color:rgb(var(--red-6)); margin-right: 5px;"/>
        <icon-exclamation-circle v-else size="18" style="color:rgb(var(--orange-6)); margin-right: 5px;"/>
        {{ $t('global.confirmTip') }}
      </template>
      <div style="text-align: center;">你确定要【{{ modalTitle }}】当前记录吗？</div>

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
import {computed, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {BasePagination} from "@/api/common";
import {roleMenuTreeselect, treeselect} from "@/api/manage/account/menu";
import {
  listRole,
  dataScope,
  getRole,
  updateRole,
  addRole,
  delRole,
  changeRoleStatus,
  allocatedUserList,
  unallocatedUserList,
  authUserCancel,
  authUserSelectAll
} from "@/api/manage/account/role";
import {PaginationProps, TableColumnData, TreeInstance, TableRowSelection} from "@arco-design/web-vue";
import {notification} from "@/hooks/my-design";
import useLoading from "@/hooks/loading";
import {FormInstance} from '@arco-design/web-vue/es/form';
import {deptTree} from "@/api/manage/account/authority";
import {update} from "lodash";

/*************************** 变量区域 begin ***************************/

    //******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {sys_data_auth_type, sys_menu_auth_type} = proxy?.useDict("sys_data_auth_type", "sys_menu_auth_type");

//加载中
const {loading, setLoading} = useLoading(true);
const dataFormRef = ref<any>(null);
const formRef = ref<any>(null);
//表格数据
const renderData = ref<any>([]);
//授权用户表格数据
const authUserRenderData = ref<any>([]);
//添加授权用户表格数据
const addAuthUserRenderData = ref<any>([]);
//表格分页配置
const pagination: any = reactive({...BasePagination()});
//表格分页配置
const authUserPagination: any = reactive({...BasePagination()});
//添加授权用户-多选
const selectedKeys = ref();
const rowSelection: TableRowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true
});
//生成查询条件
const generateSearchModel = () => {
  return {
    //角色名称
    roleName: "",
  };
};

//生成查询条件-已授权
const generateAuthUserSearchModel = () => {
  return {
    //用户名称
    nickName: "",
    //角色Id
    roleId: 0,
    //角色名称
    roleName: "",
    //手机号码
    mobile: ""
  };
};
//查询项
const searchParams = ref(generateSearchModel());
//授权用户-查询项
const authUserSearchParams = ref(generateAuthUserSearchModel());
const authUserModalVisible = ref<boolean>(false);
const addAuthUserModalVisible = ref<boolean>(false);
//设置表格列
const tabColumnsOfRole = computed<TableColumnData[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    align: 'center',
    width: 80
  },
  {
    title: "角色名称",
    dataIndex: "roleName",
    width: 180
  },
  {
    title: "权限字符",
    dataIndex: "roleKey",
    width: 140
  },
  {
    title: "权限范围",
    dataIndex: "dataScope",
    slotName: 'dataScope',
    width: 160,
    align: 'left',
  },
  {
    title: "排序",
    dataIndex: "orderNum",
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
    title: "操作",
    dataIndex: "operations",
    width: 280,
    slotName: 'operate',
    align: 'center',
    fixed: "right"
  },
]);

// 角色成员列
const tabColumnsOfUser = computed<TableColumnData[]>(() => [
  {
    title: "用户姓名",
    dataIndex: "nickName",
    width: 240
  },
  {
    title: "登录账号",
    dataIndex: "logonName",
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
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 100,
    slotName: 'operate',
    align: 'center',
    fixed: 'right'
  },
]);


// 角色成员列
const tabColumnsOfUserAdd = computed<TableColumnData[]>(() => [
  {
    title: "用户姓名",
    dataIndex: "nickName",
    width: 240
  },
  {
    title: "登录账号",
    dataIndex: "logonName",
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
]);

//switch选中值
const checkedValue = ref<number>(0);
const unCheckedValue = ref<number>(1);

//生成角色表单
const generateRoleFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {
      roleName: [{required: true, message: '请输入角色名称'}],
      roleKey: [{required: true, message: '请输入权限字符'}],
      orderNum: [{required: true, message: '请输入显示顺序'}],
    },
    editDept: false,
    menuTreeList: [],
    deptTreeList: [],
    formModel: {
      id: 0,
      //角色名称
      roleName: "",
      //权限字符
      roleKey: "",
      //排序
      orderNum: 0,
      //状态
      stopFlag: 0,
      //备注
      remark: "",
      //菜单范围（1全部菜单 2仅浏览 3自定义菜单）
      menuScope: "",
      //菜单ID集合
      menuIds: [],
      halfCheckedKeys: [],
      //数据范围
      dataScope: "",
      //权限
      deptIds: []
    }
  };
};

//角色表单模型
const formRoleDrawer = ref(generateRoleFormDrawerModel());

//角色表单实例
const formRoleRef = ref<FormInstance>();


//树结构实例-菜单
const menuTreeRef = ref<TreeInstance>();
//展开/折叠控制值-菜单权限
const expandMenuValue = ref<boolean>(true);
//全选/全不全-菜单权限
const checkedAllMenuValue = ref<boolean>(false);

//树结构实例-数据权限
const deptTreeRef = ref<TreeInstance>();
//展开/折叠控制值-数据权限
const expandDeptValue = ref<boolean>(false);
//全选/全不全-数据权限
const checkedAllDeptValue = ref<boolean>(false);
/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

    //表格页码发生变化
const onPageChange = async (val: number) => {
      pagination.current = val;
      await fetchRoleData();
    }
//表格每页数量发生变化
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchRoleData();
}

/**
 * 查询角色列表
 */
const handleQuery = async () => {
  pagination.current = 1;
  await fetchRoleData();
};

/**
 * 查询角色列表
 */
const fetchRoleData = async () => {
  setLoading(true);
  try {
    const res: any = await listRole({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
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
};

/**
 * 查询菜单树
 */
const fetchMenuTreeData = async () => {
  try {
    const res: any = await treeselect();
    if (res.code == 200) {
      formRoleDrawer.value.menuTreeList = res.data;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 查询选中的菜单树
 */
const fetchRoleMenuTreeselect = async (id: any) => {
  try {
    const res: any = await roleMenuTreeselect(id);
    if (res.code == 200) {
      formRoleDrawer.value.formModel.menuIds = res.checkedKeys;
      formRoleDrawer.value.menuTreeList = res.menus;
      // menuTreeRef.value?.checkNode(res.checkedKeys,true,false);
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 查询部门树
 */
const fetchDeptTreeData = async (id: number) => {
  try {
    const res: any = await deptTree(id);
    if (res.code == 200) {
      formRoleDrawer.value.formModel.deptIds = res.checkedKeys;
      formRoleDrawer.value.deptTreeList = res.depts;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 新增角色
 */
const handleRoleAdd = async () => {
  formRoleDrawer.value = generateRoleFormDrawerModel();
  formRoleDrawer.value.visible = true;
  await fetchMenuTreeData();
};

/**
 * 角色-保存
 */
const handleRoleSubmitForm = async () => {
  //表单校验
  const res = await formRoleRef.value?.validate();
  if (!res) {
    formRoleDrawer.value.loading = true;
    try {
      let menuIds = formRoleDrawer.value.formModel.menuIds;
      let halfCheckedKeys = menuTreeRef.value?.getHalfCheckedNodes();
      if (halfCheckedKeys && halfCheckedKeys.length > 0) {
        let ids: any = [];
        halfCheckedKeys.forEach((item: any) => {
          ids.push(item.id)
        })
        menuIds = menuIds.concat(ids);
      }
      let result;
      if (formRoleDrawer.value.formModel.id > 0) {
        result = await updateRole({...formRoleDrawer.value.formModel, menuIds: menuIds});
      } else {
        result = await addRole({...formRoleDrawer.value.formModel, menuIds: menuIds});
      }
      notification(result);
      fetchRoleData();
      formRoleDrawer.value = generateRoleFormDrawerModel();
    } catch (e) {
      console.error(e);
    } finally {
      formRoleDrawer.value.loading = false;
    }
  }
}


/**
 * 部门权限-保存
 */
const handleDeptSubmitForm = async () => {
  //表单校验
  const res = await formRoleRef.value?.validate();
  if (!res) {
    formRoleDrawer.value.loading = true;
    try {
      let data = {
        dataScope: formRoleDrawer.value.formModel.dataScope,
        deptIds: null || [],
        id: formRoleDrawer.value.formModel.id
      }
      if (formRoleDrawer.value.formModel.deptIds.length > 0) {
        data.deptIds = formRoleDrawer.value.formModel.deptIds;
      }
      let result = await dataScope(data);
      notification(result);
      if (result.code == 200) {
        handleRoleFormCancel();
        await fetchRoleData();
      }
    }catch (e) {
      console.error(e);
    }finally {
      formRoleDrawer.value.loading = false;
    }
  }
}

/**
 * 角色-取消
 */
const handleRoleFormCancel = () => {
  formRef?.value?.resetFields();
  dataFormRef?.value?.resetFields();
  formRoleDrawer.value = generateRoleFormDrawerModel();
  expandMenuValue.value = false;
  checkedAllMenuValue.value = false;
  expandDeptValue.value = false;
  checkedAllDeptValue.value = false;
  checkedAllMenuValue.value = false;
}


/**
 * 菜单-展开/折叠
 */
const handleMenuTreeExpand = () => {
  menuTreeRef.value?.expandAll(expandMenuValue.value);
}

/**
 * 菜单-展开/折叠 checkedAllMenuValue
 */
const handleMenuTreeCheckedAll = () => {
  menuTreeRef.value?.checkAll(checkedAllMenuValue.value);
}

/**
 * 菜单-选中
 */
const handleMenuTreeCheck = (checkedKeys: any, data: any) => {
  console.log("handleMenuTreeCheck", checkedKeys, data);
  formRoleDrawer.value.formModel.menuIds = checkedKeys;
  formRoleDrawer.value.formModel.halfCheckedKeys = data.halfCheckedKeys;
}


/**
 * 菜单选项改变
 * @param value
 */
const menuScopeChange = (value: any) => {
  console.log("menuScopeChange", value);
  if (value != '3') {
    formRoleDrawer.value.formModel.menuIds = [];
  }
}

/**
 * 权限-展开/折叠
 */
const handleDeptTreeExpand = () => {
  deptTreeRef.value?.expandAll(expandDeptValue.value);
}

/**
 * 权限-展开/折叠 checkedAllMenuValue
 */
const handleDeptTreeCheckedAll = () => {
  deptTreeRef.value?.checkAll(checkedAllDeptValue.value);
}

/**
 * 权限-选中
 */
const handleDeptTreeCheck = (checkedKeys: any, data: any) => {
  console.log("handleDeptTreeCheck", checkedKeys, data);
  formRoleDrawer.value.formModel.deptIds = checkedKeys;
}

/**
 * 权限选项改变
 * @param value
 */
const dataScopeChange = (value: any) => {
  console.log("dataScopeChange", value);
  if (value != '2') {
    formRoleDrawer.value.formModel.deptIds = [];
  }
}

//编辑角色
const handleRoleUpdate = async (id: any) => {
  let res: any = await getRole(id);
  if (res.code == 200) {
    formRoleDrawer.value = generateRoleFormDrawerModel();
    formRoleDrawer.value.visible = true;
    res.data.menuScope = res.data.menuScope || "";
    res.data.dataScope = res.data.dataScope || "";
    res.data.menuScope += "";
    res.data.dataScope += "";
    formRoleDrawer.value.formModel = res.data;
    await fetchRoleMenuTreeselect(id);

  }
};

/**
 * 编辑数据权限
 * @param id
 */
const handleUpdateDept = (id: number, dataScope: string) => {
  formRoleDrawer.value.visible = true;
  formRoleDrawer.value.editDept = true;
  formRoleDrawer.value.formModel.id = id;
  formRoleDrawer.value.formModel.dataScope = (dataScope || "") + "";
  fetchDeptTreeData(id);
}

const addAuthUserModalClose = () => {
  addAuthUserModalVisible.value = false;
  authUserSearchParams.value.nickName = '';
  authUserSearchParams.value.mobile = '';
  selectedKeys.value = [];
}


const modalVisible = ref(false);
const modalTitle = ref("");
const modalBizId = ref("");
const modalBizType = ref(0);
const modalBizValue = ref(0);
const handleModalClick = (title: string, bizId: string, bizValue: number, bizType: number) => {
  modalVisible.value = true;
  modalTitle.value = title;
  modalBizId.value = bizId;
  modalBizValue.value = bizValue;
  modalBizType.value = bizType;
};

/**
 * 查看
 */
const handleViewAuthUserClick = async (id: any, roleName: string) => {
  authUserModalVisible.value = true;
  authUserSearchParams.value.roleId = id;
  authUserSearchParams.value.roleName = roleName;
  await fetchAllocatedUserList();
}

/**
 * 查询角色已授权用户列表
 */
const fetchAllocatedUserList = async () => {
  const res: any = await allocatedUserList({
    pageSize: 10000,
    current: 1,
    roleId: authUserSearchParams.value.roleId,
  });
  if (res.code == 200) {
    authUserRenderData.value = res.rows;
  }
}

/**
 * 取消授权用户
 */
const handleAuthUserCanleClick = async (title: string, bizId: string, bizRoleId: number) => {
  modalVisible.value = true;
  modalTitle.value = title;
  modalBizId.value = bizId;
  modalBizValue.value = bizRoleId;
  modalBizType.value = 2;
}

/**
 * 添加授权用户
 */
const handleAuthUserAddClick = async () => {
  addAuthUserModalVisible.value = true;
  await fetchUnallocatedUserList();
}


/**
 * 保存授权的用户
 */
const handleAuthUserAddClickOk = async () => {
  try {
    if (!selectedKeys.value || selectedKeys.value.length <= 0) {
      notification({code: 201, msg: "请选择要添加的数据"});
      return;
    }
    console.log(selectedKeys.value, 'selectedKeys')
    let res: any = await authUserSelectAll({roleId: authUserSearchParams.value.roleId, userIds: selectedKeys.value});
    notification(res);
    if (res.code == 200) {
      addAuthUserModalClose();
      await fetchAllocatedUserList();
    }
  } catch (ex) {
    console.log("handleAuthUserAddClickOk", ex)
  }
}

/**
 * 查询角色未授权用户列表
 */
const fetchUnallocatedUserList = async () => {
  const res: any = await unallocatedUserList({
    pageSize: 1000,
    current: 1,
    ...authUserSearchParams.value,
  });
  if (res.code == 200) {
    addAuthUserRenderData.value = res.rows;
  }
}

/**
 * 弹框-确认
 */
const handleModalOk = async () => {
  let res: any = null;
  modalVisible.value = false;
  setLoading(true);
  try {
    if (modalBizType.value == 0) {
      res = await changeRoleStatus(modalBizId.value, modalBizValue.value);
    } else if (modalBizType.value == 1) {
      res = await delRole(modalBizId.value);
    } else {
      res = await authUserCancel({userId: modalBizId.value, roleId: modalBizValue.value});
    }
    notification(res);
    if (res.code == 200) {
      if (modalBizType.value == 2) {
        await fetchAllocatedUserList();
      } else {
        await fetchRoleData();
      }
    }
  }catch(e){
    console.error(e);
  }finally {
    setLoading(false);
  }
};


const handleModalCancel = () => {
  modalVisible.value = false;
  modalTitle.value = "";
  modalBizId.value = "";
  modalBizType.value = 0;
  modalBizValue.value = 0;
}

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchRoleData();
})
</script>

<style lang="less" scoped>
.row-mp-30 {
  margin-top: 30px;
  padding-right: 30px;
}

.desContent {
  margin-top: 20px;
  margin-left: 100px;
  margin-right: 100px;
}

.formTitle {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-neutral-6);
  border-bottom: 1px dashed var(--color-neutral-8);
}

.all-check-box {
  padding: 10px 0 10px 15px;
}


.tree-container {
  height: 500px;
  overflow: auto;
  border-radius: 2px;

  .tree-box {
    margin: 15px;
  }

  .all-check-box {
    margin-top: 20px;
    padding: 10px 0 10px 15px;
    border-top: 1px solid var(--color-neutral-4);
  }
}
</style>
