<!--
* 功能：接入申请
* 作者：张怡静
* 日期：2023-10-15
-->
<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :flex="1" style="margin-top: 4px;">
        <a-form :model="searchParams" label-align="left" auto-label-width>
          <a-row :gutter="24">
            <a-col :span="22">
              <a-form-item field="companyName" :label="$t('manage.interface.apply.accessCompany')">
                <a-input v-model="searchParams.companyName"
                         :placeholder="$t('manage.interface.apply.accessPlaceholder')" allow-clear/>
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
    <a-row style="margin-bottom: 16px;">
      <a-col :span="12">
        <a-button type="primary" @click="handleAdd">
          <template #icon>
            <icon-plus/>
          </template>
          {{ $t('manage.interface.apply.newApplication') }}
        </a-button>
      </a-col>
    </a-row>
    <!-- 操作条 end-->

    <!-- 表格 start-->
    <a-table row-key="id" ref="tableRef" :loading="loading" :bordered="{ wrapper: true, cell: true }"
             :columns="(tabColumns as TableColumnData[])" :data="renderData" :scroll="{x: 1390}">
      <template #stopFlag="{ record }">
        {{ record.stopFlag == 1 ? $t('global.enable') : $t('global.forbidden') }}
      </template>
      <template #operate="{ record }">
        <a-button size="small" type="text"
                  @click="handleStop(record.stopFlag ? $t('global.forbidden') : $t('global.enable'), record.id)"
                  :status="record.stopFlag ? 'normal' : 'warning'">
          {{ record.stopFlag ? $t('global.forbidden') : $t('global.enable') }}
        </a-button>
        <a-button size="small" type="text" status="success"
                  @click="handleUpdate(record)">{{ $t('global.edit') }}
        </a-button>
        <a-button size="small" type="text" status="danger"
                  @click="handleDelete('删除', record.id)">{{ $t('global.delete') }}
        </a-button>
        <a-button size="small" type="text" status="success"
                  @click="handleDetail(record)">{{ $t('global.detail') }}
        </a-button>
        <a-button size="small" type="text" status="success"
                  @click="createToken(record)">{{ $t('manage.interface.apply.refreshToken') }}
        </a-button>
      </template>
    </a-table>
    <!-- 表格 end-->

    <!-- 编辑抽屉 start -->
    <a-drawer :width="750" :visible="formData.visible" :ok-loading="formData.loading" @ok="createDrawerHandleOk"
              @cancel="createDrawerHandleCancel"
              :mask-closable="false">
      <template #title>
        {{ formData.formModel.id ? $t('global.edit') : $t('manage.interface.apply.newApplication') }}
      </template>
      <a-spin style="width:100%;height:100%" :loading="formData.loading">
        <a-row :gutter="24" class="row-mp-30">
          <a-col :span="24">
            <div>
              <a-form ref="formRef" :model="formData.formModel" class="form" auto-label-width>
                <a-form-item field="companyName" :label="$t('manage.interface.apply.accessCompany')" :rules="[
                {
                  match: /^[a-zA-Z0-9\u4e00-\u9fa5]{1,20}$/,
                  message: '输入汉字、字母或数字，最多20字符',
                },
              ]">
                  <a-input v-model="formData.formModel.companyName" :max-length="20" allow-clear show-word-limit
                           :placeholder="$t('manage.interface.list.interfacePlaceholder')"/>
                </a-form-item>

                <a-form-item field="linkName" :label="$t('manage.interface.apply.contactPerson')">
                  <a-input v-model="formData.formModel.linkName"/>
                </a-form-item>

                <a-form-item field="linkMobile" :label="$t('manage.interface.apply.contactNumber')">
                  <a-input v-model="formData.formModel.linkMobile"/>
                </a-form-item>

                <a-form-item field="appId" :label="$t('manage.interface.apply.uniqueIdentification')">
                  <a-input v-model="formData.formModel.appId" :disabled='tempdisable'/>
                  <a-button type="primary" @click="createIdentification"
                            style="margin-left: 10px">{{ $t('manage.interface.apply.generateIdentification') }}
                  </a-button>
                </a-form-item>

                <a-form-item field="appKey" :label="$t('manage.interface.apply.accountNumber')">
                  <a-input v-model="formData.formModel.appKey"/>
                  <a-button type="primary" @click="createAccount"
                            style="margin-left: 10px">{{ $t('manage.interface.apply.generateAccount') }}
                  </a-button>
                </a-form-item>

                <a-form-item field="appSecret" :label="$t('manage.interface.apply.secretKey')">
                  <a-input v-model="formData.formModel.appSecret"/>
                  <a-button type="primary" @click="createPassword"
                            style="margin-left: 10px">{{ $t('manage.interface.apply.generateKey') }}
                  </a-button>
                </a-form-item>

                <a-form-item field="token" :label="$t('manage.interface.apply.token')">
                  <a-input v-model="formData.formModel.token" disabled/>
                </a-form-item>

                <a-form-item field="effectMinute" :label="$t('manage.interface.apply.tokenValidity')">
                  <a-input-number v-model="formData.formModel.effectMinute">
                    <template #append>
                      {{ $t('manage.interface.apply.minutes') }}
                    </template>
                  </a-input-number>
                </a-form-item>

                <a-form-item field="remark" :label="$t('manage.interface.apply.applicationInstructions')" :rules="[
                {
                  maxLength: 200,
                  message: $t('manage.interface.apply.applicationWordLimit'),
                },
              ]" row-class="keep-margin">
                  <a-textarea v-model="formData.formModel.remark" :max-length="200" allow-clear show-word-limit
                              :placeholder="$t('manage.interface.apply.applicationPlaceholder')"/>
                </a-form-item>
              </a-form>
            </div>
          </a-col>
        </a-row>
      </a-spin>
    </a-drawer>
    <!-- 编辑抽屉 end -->

    <!-- 详情抽屉 start -->
    <a-drawer :width="750" :visible="visibledetail" @ok="handleCanceldetail" @cancel="handleCanceldetail" unmountOnClose
              :hide-cancel="true">
      <template #title>
        {{ $t('global.detail') }}
      </template>
      <a-row :gutter="24" class="row-mp-30">
        <a-col :span="24">
          <div>
            <a-descriptions class="desContent" :data="interfaceApplyList" :column="1" :size="'large'"
                            :label-style="{textAlign: 'right'}">
            </a-descriptions>
          </div>
        </a-col>
      </a-row>
    </a-drawer>
    <!-- 详情抽屉 end -->

    <!-- 删除对话框 start -->
    <a-modal width="400px" v-model:visible="delModalModel.visible" class="modal-box">
      <template #title>
        <icon-close-circle size="18" style="color: rgb(var(--red-6)); margin-right: 5px"/>
        {{ $t('global.confirmTip') }}
      </template>
      <div style="text-align: center">
        你确定要【删除】当前记录吗？
      </div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleDelModelCancel">{{ $t('global.cancel') }}</a-button>
            <a-button type="primary" @click="handleDelModelOk">{{ $t('global.confirm') }}</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
    <!-- 删除对话框 end -->

    <!-- 启用/停用对话框 start -->
    <a-modal width="400px" v-model:visible="StopModalModel.visible" class="modal-box">
      <template #title>
        <icon-exclamation-circle size="18" style="color:rgb(var(--orange-6)); margin-right: 5px;"/>
        {{ $t('global.confirmTip') }}
      </template>
      <div style="text-align: center;">你确定要【{{ StopModalModel.title }}】当前记录吗？</div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleStopModelCancel">{{ $t('global.cancel') }}</a-button>
            <a-button type="primary" @click="handleStopModelOk">{{ $t('global.confirm') }}</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
    <!-- 启用/停用对话框 end -->

  </a-card>
</template>

<script lang="ts" setup>
import {computed, reactive, ref, Ref, onMounted} from "vue";
import {BasePagination} from "@/api/common";
import {
  listApply,
  getApply,
  addApply,
  updateApply,
  delApply,
  addAccount,
  addSecret,
  addToken
} from "@/api/manage/interface/apply";
import {TableColumnData, TableInstance} from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import {FormInstance} from '@arco-design/web-vue/es/form';
import {notification} from "@/hooks/my-design";

/*************************** 变量区域 begin ***************************/

    //switch选中值
    //表格实例
const tableRef = ref<TableInstance>();
//加载中
const {loading, setLoading} = useLoading(true);
//表格数据
const renderData = ref<any[]>([]);
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
    title: "接入公司",
    dataIndex: "companyName",
    width: 240,
    fixed: 'left'
  },
  {
    title: "联系人",
    dataIndex: "linkName",
    width: 240
  },
  {
    title: "唯一标识",
    dataIndex: "appId",
    width: 240
  },
  {
    title: "令牌",
    dataIndex: "appKey",
    width: 240
  },
  {
    title: "状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100,
    //    fixed: 'right'
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 300,
    slotName: 'operate',
    align: 'center',
    fixed: 'right'
  },
]);
//生成查询条件
const generateFormModel = () => {
  return {
    id: 0,
    //接入公司
    companyName: "",
  };
};
//查询项
const searchParams = ref(generateFormModel());
//生成表单
const generateFormDataModel = () => {
  return {
    visible: false,

    formModel: {
      //编号
      id: 0,
      //接入公司
      companyName: '',
      //联系人
      linkName: '',
      //唯一标识
      appId: '',
      //令牌
      token: '',
      //状态
      stopFlag: 0,
      //联系电话
      linkMobile: '',
      //账号
      appKey: '',
      //密钥
      appSecret: '',
      //令牌有效期
      effectMinute: 0,
      //申请说明
      remark: '',
      applyContent: "",
    }
  };
};
//生成查询条件对象
const generateSearchModel = () => {
  return {
    companyName: "",
  };
};

const formTitle = ref<string>("新建菜单");
//编辑表单对象
const formData = ref(generateFormDataModel());
//编辑表单实例
const formRef = ref<FormInstance>();

//删除弹框
const generateDelModalModel = () => {
  return {
    visible: false,
    title: "",
    id: 0
  };
};
//删除弹框模型
const delModalModel = ref(generateDelModalModel());

//启用弹框
const generateStopModalModel = () => {
  return {
    visible: false,
    title: "",
    id: 0
  };
};
//启用弹框模型
const StopModalModel = ref(generateStopModalModel());
const tempdisable = true;
//详情页显示控制
const visibledetail = ref(false);
//详情页数据定义
const interfaceApplyList = ref([
  {label: '接入公司：', value: '',},
])

/*************************** 变量区域 end ***************************/

/*************************** 方法区域 begin ***************************/

/**
 * 重置查询条件
 */
const reset = () => {
  searchParams.value = generateFormModel();
};

/**
 * 查询菜单列表
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listApply({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
    });
    renderData.value = res.rows;
    pagination.total = res.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};


/**
 * 编辑菜单页
 */
const handleUpdate = async (record: any) => {
  let result = await getApply(record.id);
  if (result.code == 200) {
    result.data.noticeType += "";
    formData.value.formModel = result.data;
    formData.value.visible = true;
  }
}
/**
 * 新建菜单
 */
const handleAdd = () => {
  formTitle.value = "新建菜单"
  //重置表单
  formData.value = generateFormDataModel();
  //显示弹框
  formData.value.visible = true;
};

/**
 * 新建菜单页确定
 */
const createDrawerHandleOk = async () => {
  const res = await formRef.value?.validate();
  if (!res) {
    formData.value.loading = true;
    try {
      let result;
      if (formData.value.formModel.id == 0) {
        result = await addApply(formData.value.formModel);
      } else {
        result = await updateApply(formData.value.formModel);
      }
      notification(result);
      createDrawerHandleCancel();
      fetchData();
    } catch (e) {
      console.error(e);
    } finally {
      formData.value.loading = false;
    }


  } else {
    console.log(formData.value);
  }
};

/**
 * 新建菜单页取消
 */
const createDrawerHandleCancel = () => {
  formRef.value?.resetFields()
  formData.value = generateFormDataModel();
  formData.value.formModel.applyContent = "";
};

/**
 * 详情页弹出
 */
const handleDetail = (val: any) => {
  formData.value.formModel = val;
  visibledetail.value = true;

  interfaceApplyList.value = [
    {label: '接入公司：', value: formData.value.formModel?.companyName || '--'},
    {label: '联系人员：', value: formData.value.formModel?.linkName || '--'},
    {label: '联系电话：', value: formData.value.formModel?.linkMobile || '--'},
    {label: '唯一标识：', value: formData.value.formModel?.appId || '--'},
    {label: '账号：', value: formData.value.formModel?.appKey || '--'},
    {label: '密钥：', value: formData.value.formModel?.appSecret || '--'},
    {label: '令牌：', value: formData.value.formModel?.token || '--'},
    {label: '令牌有效期：', value: formData.value.formModel?.effectMinute?.toString() + "分钟" || '--'},
    {label: '申请说明：', value: formData.value.formModel?.remark || '--'},
  ]
}

/**
 * 详情页取消
 */
const handleCanceldetail = () => {
  visibledetail.value = false;
}
/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (title: string, bizId: string) => {
  delModalModel.value.visible = true;
  delModalModel.value.id = bizId;
  delModalModel.value.title = title;
}

/**
 * 删除提示弹框取消
 */
const handleDelModelCancel = () => {
  delModalModel.value = generateDelModalModel();
}

/**
 * 删除提示弹框确认
 */
const handleDelModelOk = async () => {
  delModalModel.value.visible = false;
  setLoading(true);
  try {
    let result = await delApply(delModalModel.value.id);
    notification(result);
    delModalModel.value.visible = false;
    fetchData();
  }catch (e) {
    console.error(e);
  }finally {
    setLoading(false);
  }
}
/**
 * 启用数据
 * @param record 数据行
 */
const handleStop = (title: string, bizId: number) => {
  StopModalModel.value.visible = true;
  StopModalModel.value.id = bizId,
      StopModalModel.value.title = title;
}

/**
 * 启用提示弹框取消
 */
const handleStopModelCancel = () => {
  StopModalModel.value = generateStopModalModel();
}

/**
 * 启用提示弹框确认
 */
const handleStopModelOk = async () => {
  console.log(StopModalModel.value.id);
  let update = {
    id: 0,
    stopFlag: 0,
  };
  update["id"] = StopModalModel.value.id;
  update["stopFlag"] = StopModalModel.value.title == "启用" ? 1 : 0;
  updateApply(update).then((res: any) => {
    StopModalModel.value.visible = false;
    fetchData();
  });
}
/**
 * 生成唯一标识
 */
const createIdentification = async (record: any) => {
  let result = await addToken(record.id);
  if (result.code == 200) {
    formData.value.formModel.appId = result.msg;
  }

}
/**
 * 生成账号
 */
const createAccount = async (record: any) => {
  let result = await addAccount(record.id);
  if (result.code == 200) {
    formData.value.formModel.appKey = result.msg;
  }
}
/**
 * 生成密钥
 */
const createPassword = async (record: any) => {
  let result = await addSecret(record.id);
  if (result.code == 200) {
    formData.value.formModel.appSecret = result.msg;
  }
}
/**
 * 生成令牌
 */
const createToken = async (record: any) => {
  let result = await addToken(formData.value.formModel.token);
  if (result.code == 200) {
    formData.value.formModel.token = result.msg;
    fetchData();
  }
}

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

.desContent {
  margin-top: 20px;
  margin-left: 100px;
  margin-right: 100px;
}
</style>
