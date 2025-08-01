<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :flex="1" style="margin-top: 4px;">
        <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                label-align="left"
                auto-label-width>
          <a-row :gutter="24">
            <a-col :span="6">
              <a-form-item field="nickName" :label="$t('manage.account.user.userName')">
                <a-input v-model="searchParams.nickName" :placeholder="$t('manage.account.user.userNamePlaceholder')"
                         allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item field="logicCode" :label="$t('manage.account.user.phoneNumber')">
                <a-input v-model="searchParams.mobile" :placeholder="$t('manage.account.user.numberPlaceholder')"
                         allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item :label="$t('manage.account.user.organizationChart')">
                <a-tree-select :placeholder="$t('manage.account.user.chartPlaceholder')" allow-search
                               v-model="searchParams.deptId" :data="renderDeptData"
                               @clear="clearOragnizational" allow-clear></a-tree-select>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item :label="$t('manage.account.user.userPost')">
                <a-select v-model="searchParams.postId" :placeholder="$t('manage.account.user.userPostPlaceholder')"
                          allow-clear>
                  <a-option v-for="item in renderPostData" :key="item.id" :label="item.postName"
                            :value="item.id"></a-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-divider style="height: 35px" direction="vertical"/>
      <a-col :flex="'86px'" style="text-align: right">
        <a-space :size="18">
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
    <a-row style="margin-bottom: 16px;">
      <a-col :span="12">
        <a-button type="primary" @click="handleAdd">
          <template #icon>
            <icon-plus/>
          </template>
          {{ $t('manage.account.user.newUser') }}
        </a-button>
      </a-col>
    </a-row>
    <!-- 操作条 end-->

    <!-- 表格 start-->
    <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
             :scroll="{ x: 1890 }" :columns="(tabColumns as TableColumnData[])" :data="renderData"
             @page-change="onPageChange"
             @page-size-change="onPageSizeChange">
      <template #nickName="{ record }">
        <span class="nickName" :class="{ 'isSupper': record.isSupper == 1 }">{{ record.nickName }}</span>
      </template>
      <template #logonName="{ record }">
        <span class="nickName" :class="{ 'isSupper': record.isSupper == 1 }">{{ record.logonName }}</span>
      </template>
      <template #dept="{ record }">
        {{ record.dept.deptName }}
      </template>
      <template #stopFlag="{ record }">
        <stop-flag :value="record.stopFlag"></stop-flag>
      </template>
      <template #operate="{ record }">
        <template v-if="record.isSupper != 1 || userStore.user.isSupper == 1">
          <a-button size="small" type="text" @click="handleStopFlag(record)"
                    :status="record.stopFlag ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdateClick(record.id)">
            {{ $t('global.edit') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdatePwd(record)">
            {{ $t('manage.account.user.changePassword') }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
        </template>

      </template>

    </a-table>
    <!-- 表格 end-->

    <!-- 添加右弹层 start -->
    <a-drawer :loading="formDrawer.loading" :width="1000" :ok-loading="formDrawer.loading" :visible="formDrawer.visible"
              @ok="createDrawerHandleOk"
              @cancel="createDrawerHandleCancel" :mask-closable="false">
      <template #title>
        {{ formDrawer.formModel.id > 0 ? $t('manage.account.user.editUser') : $t('manage.account.user.addUser') }}
      </template>
      <a-form ref="formRef" :rules="formDrawer.rules" :model="formDrawer.formModel" class="form" label-align="right"
              auto-label-width>
        <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
          <a-row :gutter="24" class="row-mp-30">
            <a-col :span="14">
              <a-form-item field="nickName" :label="$t('manage.account.user.userName')"
                           :validate-trigger="['change', 'input']">
                <a-input v-model="formDrawer.formModel.nickName" :max-length="20" allow-clear/>
              </a-form-item>
              <a-form-item field="logonName" :label="$t('manage.account.user.loginAccount')"
                           :validate-trigger="['change', 'input']">
                <template #extra>
                  <span>
                    <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                    <!-- 站点报警总开关 -->
                    只能使用字母、数字和下划线
                  </span>
                </template>
                <a-input v-model="formDrawer.formModel.logonName" :max-length="20" allow-clear/>
              </a-form-item>
              <a-form-item v-if="formDrawer.formModel.id == 0" field="logonPwd"
                           :label="$t('manage.account.user.loginPassword')"
                           :validate-trigger="['change', 'input']">
                <a-input-password autocomplete="new-password" v-model="formDrawer.formModel.logonPwd" :max-length="20"
                                  allow-clear></a-input-password>
              </a-form-item>
              <a-form-item v-if="formDrawer.formModel.id == 0" field="rawPassword"
                           :label="$t('manage.account.user.passwordConfirmation')"
                           :validate-trigger="['change', 'input']">
                <a-input-password autocomplete="new-password" v-model="formDrawer.formModel.rawPassword"
                                  :max-length="20"
                                  allow-clear></a-input-password>
              </a-form-item>

              <a-form-item field="mobile" :label="$t('manage.account.user.phoneNumber')"
                           :validate-trigger="['change', 'input']">
                <a-input v-model="formDrawer.formModel.mobile" :max-length="11" allow-clear/>
              </a-form-item>
              <a-form-item field="email" :label="$t('manage.account.user.contactEmail')">
                <a-input v-model="formDrawer.formModel.email" :max-length="20" allow-clear/>
              </a-form-item>
              <a-form-item field="sex" :label="$t('manage.account.user.userGender')">
                <a-select v-model="formDrawer.formModel.sex" :max-length="20" allow-clear
                          :placeholder="$t('manage.account.user.genderSelect')">
                  <a-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label"
                            :value="dict.value"></a-option>
                </a-select>
              </a-form-item>
              <a-form-item field="postIds" :label="$t('manage.account.user.position')"
                           :validate-trigger="['change', 'input']">
                <a-select multiple :max-tag-count="3" @change="treeChange" v-model="formDrawer.formModel.postIds"
                          allow-clear :placeholder="$t('manage.account.user.positionSelect')">
                  <a-option v-for="item in renderPostData" :key="item.id" :label="item.postName"
                            :value="item.id"></a-option>
                </a-select>
              </a-form-item>
              <a-form-item field="deptId" :label="$t('manage.account.user.department')"
                           :validate-trigger="['change', 'input']">
                <a-tree-select :placeholder="$t('manage.account.user.departmentSelect')" allow-search
                               v-model="formDrawer.formModel.deptId"
                               :data="renderDeptData"></a-tree-select>
              </a-form-item>
              <a-form-item field="isSupper" v-if="userStore.user.isSupper == 1"
                           :label="$t('manage.account.user.superManagement')">
                <a-switch :checked-value="1" :unchecked-value="0" v-model="formDrawer.formModel.isSupper">
                  <template #checked>{{ $t('global.yes') }}</template>
                  <template #unchecked>{{ $t('global.no') }}</template>
                </a-switch>
              </a-form-item>
              <!--              <template v-else>-->
              <!--                <a-form-item field="isSupper" v-if="formDrawer.formModel.isSupper == 1" :label="$t('manage.account.user.superManagement')">-->
              <!--                  <a-switch :checked-value="1" :unchecked-value="0" v-model="formDrawer.formModel.isSupper">-->
              <!--                    <template-->
              <!--                        #checked>{{ $t('global.yes') }}-->
              <!--                    </template>-->
              <!--                    <template #unchecked>{{ $t('global.no') }}</template>-->
              <!--                  </a-switch>-->
              <!--                </a-form-item>-->
              <!--              </template>-->
              <a-form-item field="stopFlag" :label="$t('manage.account.user.enabledState')">
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                          v-model="formDrawer.formModel.stopFlag">
                  <template #checked>{{ $t('global.enable') }}</template>
                  <template
                      #unchecked>{{ $t('global.forbidden') }}
                  </template>
                </a-switch>
              </a-form-item>
              <a-form-item field="personDes" :label="$t('manage.account.user.remarks')">
                <a-textarea v-model="formDrawer.formModel.personDes" allow-clear
                            :placeholder="$t('manage.account.user.remarksPlaceholder')"/>
              </a-form-item>
            </a-col>
            <a-col :span="1">
              <a-divider direction="vertical" style="height: 650px"/>
            </a-col>
            <a-col :span="9">
              <a-divider orientation="left">{{ $t('manage.account.user.userRole') }}</a-divider>
              <a-checkbox-group v-model="formDrawer.formModel.roleIds" direction="vertical">
                <a-checkbox v-for="item in renderRoleData" :value="item.id"
                            :disabled="formDrawer.formModel.isSupper == 1">{{
                    item.roleName
                  }}
                </a-checkbox>
              </a-checkbox-group>
            </a-col>
          </a-row>
        </a-spin>
      </a-form>
    </a-drawer>
    <!-- 添加右弹层 end -->

    <!-- 操作弹框 start -->
    <a-modal width="450px" v-model:visible="operateModalModel.visible" class="modal-box"
             @close="handleOperateModelCancle">
      <template #title>
        <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                           style="color: rgb(var(--red-6)); margin-right: 5px"/>
        <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
        {{ operateModalModel.type < 2 ? $t('global.confirmTip') : $t('manage.account.user.tips') }}
      </template>
      <div v-if="operateModalModel.type < 2" style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{
          operateModalModel.name
        }}】的数据项？
      </div>
      <div v-else style="text-align: center;">
        <a-form :model="operateModalModel" layout="vertical">
          <a-form-item field="pwd" :label="operateModalModel.name">
            <a-input v-model="operateModalModel.pwd" :style="{ width: '450px' }"
                     :placeholder="$t('manage.account.user.newPassword')"
                     allow-clear/>
          </a-form-item>
        </a-form>
      </div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleOperateModelCancle">{{ $t('global.cancel') }}
            </a-button>
            <a-button type="primary" :loading="operateModalModel.loading" @click="handleOperateModelOk">
              {{ $t('global.confirm') }}
            </a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
  </a-card>
</template>

<script lang="ts" setup>
import {computed, getCurrentInstance, reactive, ref, Ref} from "vue";
import {BasePagination} from "@/api/common";
import {listUser, addUser, updateUser, delUser, getUser, changeUserStatus, resetPwd} from "@/api/manage/account/user";
import {TableColumnData, TreeNodeData, Message} from "@arco-design/web-vue";
import {notification} from "@/hooks/my-design";
import {FormInstance} from '@arco-design/web-vue/es/form';
import {onMounted} from "vue";
import {listPostAll} from "@/api/manage/account/post";
import {listRoleAll} from "@/api/manage/account/role";
import useLoading from "@/hooks/loading";
import {listDept} from "@/api/manage/account/authority";
import {handleTreeNodeData} from "@/utils/ruoyi";
import {useUserStore} from "@/store";
import {getUserInfo} from "@/api/user";

const userStore = useUserStore();

//******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {sys_user_sex} = proxy?.useDict("sys_user_sex");
//******* 这里写动态获取下拉列表 end ******

//switch选中值
const checkedValue = ref<number>(0);
const unCheckedValue = ref<number>(1);
//加载中
const {loading, setLoading} = useLoading(true);
//表格数据
const renderData = ref<any[]>([]);
//岗位数据
const renderPostData = ref<any[]>([]);
//部门数据
const renderDeptData = ref<TreeNodeData[]>([]);
//角色数据
const renderRoleData = ref<any[]>([]);
//表格分页配置
const pagination: any = reactive({...BasePagination()});
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    align: 'center',
    width: 80,
    fixed: 'left'
  },
  {
    title: "用户姓名",
    dataIndex: "nickName",
    slotName: "nickName",
    fixed: 'left',
    width: 150
  },
  {
    title: "登录账号",
    dataIndex: "logonName",
    slotName: "logonName",
    width: 150
  },
  {
    title: "手机",
    dataIndex: "mobile",
    align: 'center',
    width: 140
  },
  {
    title: "部门",
    dataIndex: "dept",
    slotName: 'dept',
    align: 'left',
    width: 250
  },
  {
    title: "岗位",
    dataIndex: "postNames",
    align: 'left',
    width: 250,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "角色",
    dataIndex: "roleNames",
    align: 'left',
    ellipsis: true,
    tooltip: {position: 'top'},
    width: 250
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    width: 180,
    align: 'center'
  },
  {
    title: "最后登录时间",
    dataIndex: "lastLoginTime",
    slotName: "lastLoginTime",
    width: 180,
    align: 'center'
  },
  {
    title: "状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100,
    fixed: 'right'
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 250,
    slotName: 'operate',
    align: 'center',
    fixed: 'right'
  },
]);

//生成查询条件
const generateFormModel = () => {
  return {
    //用户姓名
    nickName: "",
    //手机号码
    mobile: "",
    //组架结构
    deptId: "",
    //岗位
    postId: ""
  };
};

//查询项
const searchParams = ref(generateFormModel());

//生成表单模型
const generateFormDrawerModel = () => {
  return {
    loading: false,
    visible: false,
    rules: {
      nickName: [{required: true, message: "请输入用户姓名"}, {
        minLength: 2,
        maxLength: 20,
        message: "用户姓名长度必须介于 2 和 20 之间"
      }],
      logonName: [{
        required: true,
        message: '请输入登录账号'
      }, {
        minLength: 2,
        maxLength: 20,
        message: "登录账号长度必须介于 2 和 20 之间"
      },
        {
          validator: (value: any, cb: any) => {
            const validPattern = /^[a-zA-Z0-9_]+$/;
            if (!validPattern.test(value)) {
              cb('登录账号只能使用字母、数字和下划线')
            } else {
              cb()
            }
          }
        }],
      logonPwd: [{required: true, message: "请输入登录密码"}, {
        minLength: 5,
        maxLength: 20,
        message: "登录密码长度必须介于 5 和 20 之间"
      }],
      rawPassword: [{required: true, message: "请输入登录密码"}, {
        minLength: 5,
        maxLength: 20,
        message: "登录密码长度必须介于 5 和 20 之间"
      }],
      mobile: [{required: true, message: "请输入手机号码"}, {
        match: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
        message: "请输入正确的手机号码"
      }],
      postIds: [{required: true, message: "请选择岗位"}, {type: 'array', minLength: 1, message: "最少选择一个岗位"}],
      deptId: [{required: true, message: "请选择部门"}]
    },
    fileLogoList: [],
    formModel: {
      id: 0,
      deptId: '',
      nickName: '',
      logonName: '',
      logonPwd: '',
      rawPassword: '',
      mobile: '',
      email: '',
      sex: '',
      isSupper: 0,
      stopFlag: 0,
      personDes: '',
      postIds: [],
      roleIds: []
    }
  };
};

//表单模型
const formDrawer = ref(generateFormDrawerModel());

const formRef = ref<FormInstance>();

//操作弹框
const generateOperateModalModel = () => {
  return {
    //0 状态 1删除
    type: 0,
    loading: false,
    visible: false,
    title: "",
    id: 0,
    value: 0,
    pwd: "",
    name: ""
  };
};
//操作弹框模型
const operateModalModel = ref(generateOperateModalModel());


//重置组织架构
const clearOragnizational = (value: any) => {
  searchParams.value.deptId = "";
};
/**
 * 表格页码发生变化
 * @param val
 */
const onPageChange = (val: number) => {
  pagination.current = val;
  fetchData();
}

/**
 * 表格每页数量发生变化
 */
const onPageSizeChange = (val: number) => {
  pagination.pageSize = val;
  fetchData();
}

const treeChange = async (val: any) => {
  console.log("treeChange", val)
  console.log("treeChange", formDrawer.value.formModel.postIds)
}

/**
 * 查询用户数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listUser({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
    });
    if (res.code = 200) {
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
 * 查询岗位数据
 */
const fetchPostData = async () => {
  try {
    const res = await listPostAll({});
    if (res.code = 200) {
      renderPostData.value = res.data;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};


/**
 * 查询站点数据
 */
const fetchDeptData = async () => {
  try {
    const res = await listDept({});
    if (res.code = 200) {
      renderDeptData.value = handleTreeNodeData(res.data, "id", "deptName", "parentId");
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 查询角色数据
 */
const fetchRoleData = async () => {
  try {
    const res = await listRoleAll({});
    if (res.code = 200) {
      renderRoleData.value = res.data;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 编辑按钮
 */
const handleUpdateClick = async (val: any) => {
  try {
    formDrawer.value.visible = true;
    formDrawer.value.loading = true;
    await fetchPostData();
    await fetchDeptData();
    await fetchRoleData();
    let res: any = await getUser(val);
    if (res.code == 200) {
      let info = {
        id: res.data.id,
        deptId: res.data.deptId,
        nickName: res.data.nickName,
        logonName: res.data.logonName,
        mobile: res.data.mobile,
        email: res.data.email,
        sex: res.data.sex != null ? res.data.sex += '' : '',
        isSupper: res.data.isSupper,
        stopFlag: res.data.stopFlag,
        personDes: res.data.personDes,
        postIds: res.postIds,
        roleIds: res.roleIds,
        logonPwd: '',
        rawPassword: '',
      }
      console.log("info", info);
      formDrawer.value.formModel = info;
    }
  } catch (e) {
  } finally {
    formDrawer.value.loading = false;
  }

};

/**
 * 添加用户
 */
const handleAdd = async () => {
  formDrawer.value.visible = true;
  // console.log(store)
  await fetchPostData();
  await fetchDeptData();
  await fetchRoleData();
};

/**
 * 新建用户页确定
 */
const createDrawerHandleOk = async () => {
  const res = await formRef.value?.validate();
  if (!res) {
    formDrawer.value.loading = true;
    try {
      let info = formDrawer.value.formModel;
      let result;
      if (info.isSupper == 1) {
        info.roleIds = [];
      }
      if (info.id > 0) {
        result = await updateUser({...info, sex: info.sex ? parseInt(info.sex) : null});
      } else {
        result = await addUser(info);
      }
      notification(result);
      if (result.code == 200) {
        createDrawerHandleCancel();
        pagination.current = 1;
        await fetchData();
      }
    } catch (e) {
      console.error(e);
    } finally {
      formDrawer.value.loading = false;
    }
  }
}

/**
 * 新建用户页取消
 */
const createDrawerHandleCancel = () => {
  formDrawer.value = generateFormDrawerModel();
}


/**
 * 停用
 * @param record
 */
const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.nickName;
  operateModalModel.value.value = record.stopFlag == 0 ? 1 : 0;
  operateModalModel.value.type = 0;
}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = '删除';
  operateModalModel.value.name = record.nickName;
  operateModalModel.value.type = 1;
}

/**
 * 修改密码
 * @param record 数据行
 */
const handleUpdatePwd = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.value = null;
  operateModalModel.value.name = `输入"${record.nickName}"的新密码`;
  operateModalModel.value.type = 2;
}
/**
 * 删除提示弹框取消
 */
const handleOperateModelCancle = () => {
  operateModalModel.value = generateOperateModalModel();
}

/**
 * 删除提示弹框确认
 */
const handleOperateModelOk = async () => {
  let result;
  operateModalModel.value.visible = false;
  setLoading(true);
  try {
    if (operateModalModel.value.type == 0) {
      result = await changeUserStatus(operateModalModel.value.id, operateModalModel.value.value);
    } else if (operateModalModel.value.type == 1) {
      result = await delUser(operateModalModel.value.id);
    } else {
      if (!operateModalModel.value.pwd || operateModalModel.value.pwd.length <= 0) {
        Message.error({
          content: "请输入新密码",
          duration: 5 * 1000,
        });
        return false;
      } else {
        result = await resetPwd({id: operateModalModel.value.id, logonPwd: operateModalModel.value.pwd})
      }
    }
    notification(result);
    if (result.code == 200) {
      handleOperateModelCancle();
      await fetchData();
    }
  } catch (e) {
    console.error(e);
  } finally {
    setLoading(false);
  }
}

onMounted(async () => {
  await fetchData();
  await fetchDeptData();
  await fetchPostData();
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
</style>
