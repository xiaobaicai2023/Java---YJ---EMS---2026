<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :flex="1" style="margin-top: 4px;">
        <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                label-align="left"
                auto-label-width>
          <a-row :gutter="24">
            <a-col :span="22">
              <a-form-item field="menu" :label="$t('manage.account.menu.menuName')">
                <a-input v-model="searchParams.menuName" :placeholder="$t('manage.account.menu.menuNamePlaceholder')"
                         allow-clear/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-col :span="16" style="text-align: left">
        <a-space :size="18">
          <a-divider style="height: 35px" direction="vertical"/>
          <a-button type="primary" @click="fetchData">
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
    <a-row style="margin-bottom: 16px">
      <a-col :span="2">
        <a-space :size="15">
          <a-button type="primary" @click="handleAdd">
            <template #icon>
              <icon-plus/>
            </template>
            {{ $t('manage.account.menu.newMenu') }}
          </a-button>

          <a-button @click="handleExpand"> {{ $t('global.expand') }}/{{ $t('global.fold') }}</a-button>
          <!--<a-button @click="setMenuClick"> {{// $t('manage.account.menu.enterprisemenu')}}（暂不可用） </a-button>-->
        </a-space>
      </a-col>
    </a-row>
    <!-- 操作条 end-->

    <!-- 表格 start-->
    <a-table row-key="id" ref="tableRef" v-bind:hide-expand-button-on-empty="true" :loading="loading"
             :pagination="false"
             :bordered="{ wrapper: true, cell: true }" :columns="(tabColumns as TableColumnData[])" :data="renderData"
             :default-expand-all-rows="true" show-empty-tree>
      <template #icon="{ record }">
        <div v-if="record.icon && record.icon != '#' && record.icon.indexOf('icon-') >= 0">
          <result-icon :name="record.icon"></result-icon>
        </div>
        <div v-else>
          #
        </div>
      </template>
      <template #permissionSign="{ record }">
        {{ record.permissionSign && record.permissionSign.length > 0 ? record.permissionSign : '#' }}
      </template>
      <template #requestUrl="{ record }">
        {{ record.requestUrl && record.requestUrl.length > 0 ? record.requestUrl : '#' }}
      </template>
      <template #menuType="{ record }">
        <!-- M目录 C菜单 F按钮 -->
        <a-space v-if="record.menuType === 'M'">
          <a-tag color="rgb(var(--arcoblue-6))">{{ $t('manage.account.menu.menuCatalogue') }}</a-tag>
        </a-space>
        <a-space v-if="record.menuType === 'C'">
          <a-tag color="rgb(var(--green-6))">{{ $t('manage.account.menu.menuText') }}</a-tag>
        </a-space>
        <a-space v-if="record.menuType === 'F'">
          <a-tag color="rgb(var(--orange-6))">{{ $t('manage.account.menu.menuButton') }}</a-tag>
        </a-space>
      </template>
      <template #visible="{ record }">
        <a-space>
          <a-tag v-if="record.visible === 0" color="rgb(var(--green-6))">{{
              $t('manage.account.menu.menuReveal')
            }}
          </a-tag>
          <a-tag v-if="record.visible === 1" color="rgb(var(--red-5))">{{
              $t('manage.account.menu.menuHidden')
            }}
          </a-tag>
          <span v-else-if="record.visible === 2"> -</span>
          <stop-flag :value="record.stopFlag"></stop-flag>
        </a-space>

      </template>
      <template #operate="{ record }">
        <a-button size="small" type="text" :status="record.stopFlag == 1 ? 'normal' : 'warning'"
                  @click="handleStop(record)">
          {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
        </a-button>
        <a-button size="small" type="text" status="success" @click="handleUpdate(record)">{{ $t('global.edit') }}
        </a-button>
        <a-button size="small" type="text" status="danger" @click="handleDelete(record)">
          {{ $t('global.delete') }}
        </a-button>
      </template>
    </a-table>
    <!-- 表格 end-->

    <!-- 编辑右弹层 start -->
    <a-drawer :width="750" :visible="createDrawerVisible" :ok-loading="drawerLoading" @ok="createDrawerHandleOk"
              @cancel="createDrawerHandleCancel"
              :mask-closable="false">
      <template #title> {{ $t('manage.account.menu.addMenu') }}</template>
      <a-spin style="width:100%;height:100%" :loading="drawerLoading">
        <a-row :gutter="12">
          <a-col :span="24">
            <a-form ref="formRef" :model="formData" class="form" :rules="formRules" label-align="right"
                    auto-label-width>
              <!-- 模块类型 -->
              <a-form-item field="menuModel" :label="$t('manage.account.menu.moduleType')"
                           :validate-trigger="['change', 'blur']">
                <a-select v-model="formData.menuModel" :placeholder="$t('manage.account.menu.moduleTypeSelect')"
                          allow-clear>
                  <a-option v-for="dict in sys_menu_model" :key="dict.id" :label="dict.label"
                            :value="Number(dict.value)"></a-option>
                </a-select>
              </a-form-item>
              <a-form-item field="menuName" :label="$t('manage.account.menu.menuName')">
                <a-input v-model="formData.menuName" :max-length="50" allow-clear
                         :placeholder="$t('manage.account.menu.menuNameTip')"/>
              </a-form-item>
              <a-form-item field="parentId" :label="$t('manage.account.menu.parentDirectory')">
                <a-tree-select allow-search v-model="formData.parentId" :data="parentList"
                               :placeholder="$t('manage.account.menu.rootDirectory')"
                               :filter-tree-node="filterTreeNode"></a-tree-select>
              </a-form-item>

              <a-form-item field="menuType" :label="$t('manage.account.menu.menuType')">
                <a-radio-group v-model="formData.menuType">
                  <a-radio value="M">{{ $t('manage.account.menu.menuCatalogue') }}</a-radio>
                  <a-radio value="C">{{ $t('manage.account.menu.menuText') }}</a-radio>
                  <a-radio value="F">{{ $t('manage.account.menu.menuButton') }}</a-radio>
                </a-radio-group>
              </a-form-item>

              <a-form-item field="icon" :label="$t('manage.account.menu.menuIcon')" v-if="formData.menuType === 'M'">
                <select-icon @change="selectIconChange">
                  <a-input v-model="formData.icon" :placeholder="$t('manage.account.menu.chooseIcon')" allow-clear/>
                </select-icon>
              </a-form-item>
              <a-form-item field="path" :label="$t('manage.account.menu.routingAddress')"
                           v-if="formData.menuType === 'C' || formData.menuType === 'M'">
                <template #extra>
                <span>
                  <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                  {{ $t('manage.account.menu.routeAddressTip') }}
                </span>
                </template>
                <a-input v-model="formData.path" autocomplete="off"/>
              </a-form-item>
              <a-form-item field="requestUrl" :label="$t('manage.account.menu.componentPath')"
                           v-if="formData.menuType === 'C' || formData.menuType === 'M'">
                <template #extra>
                <span>
                  <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                  {{ $t('manage.account.menu.componentPathTip') }}
                </span>
                </template>
                <a-input v-model="formData.requestUrl" autocomplete="off"/>
              </a-form-item>
              <a-form-item field="permissionSign" :label="$t('manage.account.menu.permissionCharacter')"
                           v-if="formData.menuType === 'C' || formData.menuType === 'F'">
                <template #extra>
                <span>
                  <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                  控制器中定义的权限字符，如：@PreAuthorize('@ss.hasPermi('system:user:list')'
                </span>
                </template>
                <a-input v-model="formData.permissionSign" autocomplete="off"/>
              </a-form-item>
              <a-form-item field="requestQuery" :label="$t('manage.account.menu.routeParameter')"
                           v-if="formData.menuType !== 'F'">
                <template #extra>
                <span>
                  <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                  {{ $t('manage.account.menu.routeParameterTip') }} {'id': 1, 'name': 'ry'}
                </span>
                </template>
                <a-input v-model="formData.requestQuery" autocomplete="off"/>
              </a-form-item>
              <a-form-item field="orderNum" :label="$t('global.displaySequence')">
                <a-input-number v-model="formData.orderNum"/>
              </a-form-item>
              <a-form-item field="visible" :label="$t('manage.account.menu.displayStatus')"
                           v-if="formData.menuType != 'F'">
                <template #extra>
                <span>
                  <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                  {{ $t('manage.account.menu.displayStatusTip') }}
                </span>
                </template>
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                          v-model="formData.visible">
                  <template #checked>{{ $t('global.reveal') }}</template>
                  <template #unchecked>{{ $t('global.hidden') }}</template>
                </a-switch>
              </a-form-item>
              <a-form-item field="stopFlag" :label="$t('manage.account.menu.menuStatus')"
                           v-if="formData.menuType != 'F'">
                <template #extra>
                <span>
                  <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                  {{ $t('manage.account.menu.menuStatusTip') }}
                </span>
                </template>
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                          v-model="formData.stopFlag">
                  <template #checked>{{ $t('global.normal') }}</template>
                  <template #unchecked>{{ $t('global.outOfService') }}</template>
                </a-switch>
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      </a-spin>
    </a-drawer>
    <!-- 编辑添加右弹层 end -->


    <!-- 删除对话框 start -->
    <a-modal width="400px" v-model:visible="operateModalModel.visible" class="modal-box">
      <template #title>
        <icon-close-circle size="18" style="color: rgb(var(--red-6)); margin-right: 5px"/>
        {{ $t('global.confirmTip') }}
      </template>
      <div style="text-align: center">
        是否确认{{ operateModalModel.title }}名称为【{{ operateModalModel.name }}】的数据项？
      </div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleDeleteCancel">{{ $t('global.cancel') }}</a-button>
            <a-button type="primary" @click="handleDeleteOk">{{ $t('global.confirm') }}</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
    <!-- 删除对话框 end -->
  </a-card>
</template>

<script lang="ts" setup>
import {computed, ref, Ref, reactive, onMounted, getCurrentInstance} from "vue";
import {BasePagination} from "@/api/common";
import {listMenu, getMenu, addMenu, updateMenu, delMenu, changeMenuStatus} from "@/api/manage/account/menu";
import {TableColumnData, TableInstance, TreeNodeData} from "@arco-design/web-vue";
import {notification} from "@/hooks/my-design";
import useLoading from "@/hooks/loading";
import {FormInstance} from "@arco-design/web-vue/es/form";
import router from "@/router";
import selectIcon from "@/components/select-icon/index.vue";
import {handleTree, handleTreeNodeData} from "@/utils/ruoyi";
import {changeCompanyStatus, delCompany} from "@/api/system/company";

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {sys_menu_model} = proxy?.useDict("sys_menu_model");
//******* 这里编写动态获取下拉列表 end ******

/*************************** 变量区域 begin ***************************/

//表单验证规则
const formRules = {
  menuModel: [{required: true, message: '请选择模块类型', trigger: "blur"}],
  menuName: [{required: true, message: '请输入菜单名称', trigger: "blur"}],
  icon: [{required: true, message: '请选择图标', trigger: "blur"}],
  path: [{required: true, message: '请输入路由地址', trigger: "blur"}],
  orderNum: [{required: true, message: '请输入显示顺序', trigger: "blur"}],
}
//switch选中值
const checkedValue = ref<number>(0);
const unCheckedValue = ref<number>(1);
//新增菜单弹框显示控制
const createDrawerVisible = ref(false);
// 菜单弹窗loading
const drawerLoading = ref(false);
//表格实例
const tableRef = ref<TableInstance>();
//展开/折叠控制值
const expandValue = ref<boolean>(false);
//加载中
const {loading, setLoading} = useLoading(true);
//表格数据
const renderData = ref<any[]>([]);
//默认上级目录
const defaultParentList = ref<TreeNodeData[]>(
    [
      {
        key: 0,
        title: "根目录"
      }
    ]
);
//上级目录
const parentList = ref<TreeNodeData[]>();
//表格分页配置
const pagination: any = reactive({...BasePagination()});
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "菜单名称",
    dataIndex: "menuName",
    slotName: "menuName",
    width: 240,
  }, {
    title: "排序",
    dataIndex: "orderNum",
    align: "center",
    width: 100,
  }, {
    title: "图标",
    dataIndex: "icon",
    slotName: "icon",
    align: "center",
    width: 100,
  },
  {
    title: "权限标识",
    dataIndex: "permissionSign",
    slotName: "permissionSign",
    ellipsis: true,
    tooltip: {position: 'top'},
    width: 140,
  },
  {
    title: "路由地址",
    dataIndex: "path",
    slotName: "path",
    ellipsis: true,
    tooltip: {position: 'top'},
    width: 140,
  },
  {
    title: "菜单类型",
    dataIndex: "menuType",
    slotName: "menuType",
    align: "center",
    width: 100,
  },
  {
    title: "菜单显示",
    dataIndex: "visible",
    slotName: "visible",
    align: "center",
    width: 100,
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 120,
    slotName: "operate",
    align: "center",
  },
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
//生成表单
const generateFormDataModel = () => {
  return {
    id: 0,
    menuModel: undefined,
    parentId: 0,
    menuName: "",
    icon: "",
    menuType: "M",
    path: "",
    orderNum: 0,
    isFrame: "",
    isCache: 0,
    visible: 0,
    stopFlag: 0,
    requestUrl: "",
    permissionSign: "",
    requestQuery: "",
    openType: 1,
    isAdminVisit: 1,
  };
};
const formTitle = ref<string>("新建菜单");
//编辑表单对象
const formData = ref(generateFormDataModel());
//编辑表单实例
const formRef = ref<FormInstance>();
//删除业务ID
const modalBizId = ref(0);
//操作弹框
const generateOperateModalModel = () => {
  return {
    //0 状态 1删除
    type: 0,
    visible: false,
    title: "",
    id: 0,
    value: 0,
    name: ""
  };
};

//操作弹框模型
const operateModalModel = ref(generateOperateModalModel());

/*************************** 变量区域 end ***************************/

/*************************** 方法区域 begin ***************************/

/**
 * 重置查询条件
 */
const reset = () => {
  searchParams.value = generateFormModel();
};

/**
 * 展开/折叠
 */
const handleExpand = () => {
  expandValue.value = !expandValue.value;
  tableRef.value?.expandAll(expandValue.value);
}

/**
 * 选择图标
 * @param iconName 图标名称
 */
const selectIconChange = (iconName: string) => {
  formData.value.icon = iconName;
};

/**
 * 查询菜单列表
 */
const fetchData = async () => {
  setLoading(true);
  try {
    let res = await listMenu({...searchParams.value});
    renderData.value = handleTree(res.data);

    let treeData = handleTreeNodeData(res.data, "id", "menuName");
    parentList.value = defaultParentList.value.concat(treeData);
  } catch (err) {
  } finally {
    setLoading(false);
  }
};

/**
 * 菜单搜索
 * @param searchValue
 * @param nodeData
 */
const filterTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.title.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
}

// 编辑页
const handleUpdate = async (val: any) => {
  //获取菜单详情
  let res = await getMenu(val.id);
  formData.value = res.data;
  formTitle.value = "编辑菜单";
  createDrawerVisible.value = true;
};

const handleStop = async (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.menuName;
  operateModalModel.value.value = record.stopFlag == 0 ? 1 : 0;
  operateModalModel.value.type = 0;
}

/**
 * 新建菜单
 */
const handleAdd = () => {
  formTitle.value = "新建菜单"
  //重置表单
  formData.value = generateFormDataModel();
  //显示弹框
  createDrawerVisible.value = true;
};

/**
 * 新建菜单页确定
 */
const createDrawerHandleOk = async () => {
  const res = await formRef.value?.validate();
  if (!res) {
    // console.log({...formData.value});
    drawerLoading.value = true;
    try {
      let result;
      if (formData.value.id == 0) {
        result = await addMenu(formData.value);
      } else {
        result = await updateMenu(formData.value);
      }
      notification(result);
      createDrawerVisible.value = false;
      pagination.current = 1;
      await fetchData();
    }catch (e){
      console.error(e);
    }finally {
      drawerLoading.value = false;
    }
  } else {
    console.log(formData.value);
  }
};

/**
 * 新建菜单页取消
 */
const createDrawerHandleCancel = () => {
  createDrawerVisible.value = false;
  formData.value = generateFormDataModel();
};

/**
 * 删除按钮
 * @param title 标题
 * @param bizId 业务ID
 */
const handleDelete = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = '删除';
  operateModalModel.value.name = record.menuName;
  operateModalModel.value.type = 1;
};

/**
 * 删除确定按钮
 */
const handleDeleteOk = async () => {
  operateModalModel.value.visible = false;
  setLoading(true);
  try {
    let result: any = {};
    if (operateModalModel.value.type == 0) {
      result = await changeMenuStatus(operateModalModel.value.id, operateModalModel.value.value)
    } else {
      result = await delMenu(operateModalModel.value.id);
    }

    notification(result);
    if (result.code == 200) {
      await fetchData();
    }
  } catch (e) {
    console.error(e);
  } finally {
    setLoading(false);
  }
};

/**
 * 删除取消按钮
 */
const handleDeleteCancel = () => {
  operateModalModel.value.visible = false;
};

/**
 * 配置企业菜单
 */
const setMenuClick = () => {
  router.push({name: "manageAccountMenuEnterprise"});
};


/*************************** 方法区域 end ***************************/

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

.group-name {
  font-weight: 600;
}
</style>
