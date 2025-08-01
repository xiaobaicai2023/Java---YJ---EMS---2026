<!--
 * 功能：企业管理
 * 作者：曹晓桐
 * 日期：2023-11-7
-->
<template>
  <div>
    <a-card class="content">
      <!-- 表单搜索 -->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left"
                  auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="entName" :label="$t('manage.system.enterprise')">
                  <a-input v-model="searchModel.entName" :placeholder="$t('manage.system.placeholder')" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-space direction="vertical" :size="18">
                  <a-button type="primary" @click="search">
                    <template #icon>
                      <icon-search/>
                    </template>
                    {{ $t('global.search') }}
                  </a-button>
                </a-space>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
      </a-row>

      <!-- 分割线 -->
      <a-divider style="margin-top: 0"/>

      <!-- 按钮 -->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('manage.system.newEnterprise') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table row-key="id" :loading="loading" :pagination="pagination" :bordered="{ wrapper: true, cell: true }"
               :scroll="{ x: 1300 }" :columns="columns" :data="renderData"
               @page-change="onPageChange"
               @page-size-change="onPageSizeChange" show-page-size>
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"/>
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" @click="handleStopFlagClick(record)"
                    :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdate(record)">{{
              $t('global.edit')
            }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
        </template>
      </a-table>

      <!-- 操作弹框 start -->
      <a-modal v-model:visible="operateModalModel.visible" class="modal-box">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                             style="color: rgb(var(--red-6)); margin-right: 5px"/>
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
          {{ $t('global.confirmTip') }}
        </template>
        <a-spin :loading="operateModalModel.loading" style="width: 100%;height: 100%">
          <div style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{
              operateModalModel.name
            }}】的数据项？
          </div>
        </a-spin>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">{{
                  $t('global.cancel')
                }}
              </a-button>
              <a-button type="primary" :loading="operateModalModel.loading" @click="handleOperateModelOk">{{ $t('global.confirm') }}</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>

      <!-- 添加右弹层 start -->
      <a-drawer :width="1300" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm"
                @cancel="handleFormCancel"
                :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? $t('manage.system.editEnterprise') : $t('manage.system.addEnterprise') }}
        </template>
        <a-spin :loading="formDrawer.loading" style="width: 100%; height: 100%">
          <a-form ref="formRef" :model="formDrawer.formModel" label-align="right" auto-label-width
                  :rules="formDrawer.rules">
            <a-row :gutter="12" class="row-mp-30">
              <a-col :span="12">
                <!-- 企业名称 -->
                <a-form-item field="entName" :label="$t('manage.system.enterprise')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.entName" :max-length="64" allow-clear/>
                </a-form-item>

                <!-- 企业类型 -->
                <a-form-item field="entType" :label="$t('manage.system.enterpriseType')">
                  <a-select v-model="formDrawer.formModel.entType"
                            :placeholder="$t('manage.system.selectEnterpriseType')"
                            allow-clear>
                    <a-option v-for="dict in sys_enterprise_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 企业Logo-->
                <a-form-item field="entLogo" :label="$t('manage.system.enterpriseLogo')">
                  <a-upload :limit="1" list-type="picture-card" action="/"
                            :file-list="formDrawer.fileLogoList"
                            image-preview
                            :custom-request="customRequest"
                            @success="handleUploadLogo"
                            @beforeRemove="handleDeleteLogo"
                  />
                </a-form-item>

                <!-- 统一社会信用代码 -->
                <a-form-item field="creditCode" :label="$t('manage.system.creditCode')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.creditCode" :max-length="18" allow-clear/>
                </a-form-item>

                <!-- 企业法人 -->
                <a-form-item field="legalPerson" :label="$t('manage.system.legalPerson')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.legalPerson" :max-length="64" allow-clear/>
                </a-form-item>

                <!-- 联系人员 -->
                <a-form-item field="linkName" :label="$t('manage.system.contactPerson')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.linkName" :max-length="20" allow-clear/>
                </a-form-item>

                <!-- 联系电话 -->
                <a-form-item field="linkMobile" :label="$t('manage.system.contactPhone')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.linkMobile" :max-length="11" allow-clear
                           :placeholder="$t('global.please')"/>
                </a-form-item>

                <!-- 联系地址 -->
                <a-form-item field="linkAddress" :label="$t('manage.system.contactAddress')">
                  <a-textarea v-model="formDrawer.formModel.linkAddress" allow-clear :max-length="200" :auto-size="true"
                              :show-word-limit="true"/>
                </a-form-item>

                <!-- 邮箱 -->
                <a-form-item field="email" :label="$t('manage.system.email')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.email" allow-clear
                           :max-length="30"
                           :placeholder="$t('manage.company.list.companyNameMaximum')"/>
                </a-form-item>

                <!-- 企业简介 -->
                <a-form-item field="entIntro" :label="$t('manage.system.profile')">
                  <a-textarea v-model="formDrawer.formModel.entIntro" allow-clear :max-length="200" :auto-size="true"
                              :show-word-limit="true" :placeholder="$t('manage.system.inputProfile')"/>
                </a-form-item>
              </a-col>
              <a-col :span="12">

                <!-- 经营范围 -->
                <a-form-item field="businessScope" :label="$t('manage.system.businessScope')">
                  <a-textarea v-model="formDrawer.formModel.businessScope" allow-clear :max-length="200"
                              :show-word-limit="true" :label="$t('manage.system.inputBusinessScope')"/>
                </a-form-item>

                <!-- 注册资本 -->
                <a-form-item field="registeredCapital" :label="$t('manage.system.registeredCapital')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input-number v-model="formDrawer.formModel.registeredCapital" :max-length="64" allow-clear>
                    <template #append>
                      万元
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 成立日期 -->
                <a-form-item field="foundDate" :label="$t('manage.system.establishmentDate')">
                  <a-date-picker v-model="formDrawer.formModel.foundDate"
                                 :placeholder="$t('manage.system.selectEstablishmentDate')"/>
                </a-form-item>

                <!-- 营业期限开始 -->
                <a-form-item field="openStartDate" :label="$t('manage.system.business')">
                  <a-date-picker v-model="formDrawer.formModel.openStartDate"
                                 :placeholder="$t('manage.system.selectBusiness')"/>
                </a-form-item>

                <!-- 营业期限结束 -->
                <a-form-item field="openEndDate" :label="$t('manage.system.businessEnd')">
                  <a-date-picker v-model="formDrawer.formModel.openEndDate"
                                 :placeholder="$t('manage.system.selectBusinessEnd')"/>
                </a-form-item>

                <!-- 住所 -->
                <a-form-item field="address" :label="$t('manage.system.residence')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.address" allow-clear
                           :max-length="200"
                           :placeholder="$t('global.please')"/>
                </a-form-item>

                <!-- 登记机关 -->
                <a-form-item field="regOffice" :label="$t('manage.system.registration')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.regOffice" allow-clear
                           :max-length="64"
                           :placeholder="$t('manage.company.list.companyNameMaximum')"/>
                </a-form-item>

                <!-- 发证日期 -->
                <a-form-item field="issueDate" :label="$t('manage.system.certificateDate')">
                  <a-date-picker v-model="formDrawer.formModel.issueDate"
                                 :placeholder="$t('manage.system.selectCertificateDate')"/>
                </a-form-item>

                <!-- 逻辑代码 -->
                <a-form-item field="logicCode" :label="$t('manage.system.logicCode')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.logicCode" allow-clear
                           :placeholder="$t('global.please')" :max-length="64"/>
                </a-form-item>

                <!-- 备注说明 -->
                <a-form-item field="remark" :label="$t('manage.account.role.remarks')">
                  <a-textarea v-model="formDrawer.formModel.remark" allow-clear :max-length="200" :auto-size="true"
                              :show-word-limit="true" :placeholder="$t('manage.account.role.remarksTip')"/>
                </a-form-item>

                <!-- 是否停用 -->
                <a-form-item field="stopFlag" :label="$t('manage.system.disableOrNot')">
                  <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                            v-model="formDrawer.formModel.stopFlag">
                    <template #checked>{{ $t('global.normal') }}</template>
                    <template
                        #unchecked>{{ $t('global.outOfService') }}
                    </template>
                  </a-switch>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-spin>
      </a-drawer>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, reactive, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {notification} from "@/hooks/my-design";
import {listEnterprise, getEnterprise, delEnterprise, addEnterprise, updateEnterprise} from "@/api/system/enterprise";
import {BasePagination} from '@/api/common';
import {FormInstance} from '@arco-design/web-vue/es/form';
import {getToken} from '@/utils/auth';
import {update} from 'lodash';

/*************************** 变量区域 begin ***************************/
//生成查询条件对象
const generateSearchModel = () => {
  return {
    entName: '',
  };
};

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_enterprise_type,
  sys_normal_disable,
} = proxy?.useDict(
    "sys_enterprise_type",
    "sys_normal_disable",
);
//******* 这里编写动态获取下拉列表 end ******

//查询表单对象
const searchModel = ref(generateSearchModel());

//加载中
const {loading, setLoading} = useLoading(true);


//表格分页参数
const pagination: any = reactive({...BasePagination()});

//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    align: 'center',
    width: 80
  }, {
    title: "企业名称",
    dataIndex: 'entName',
    width: 230,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "社会信用代码",
    dataIndex: 'creditCode',
    width: 200,
  },
  {
    title: "企业法人",
    dataIndex: 'legalPerson',
    width: 150,
    align: 'center',
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "联系电话",
    dataIndex: 'linkMobile',
    width: 150,
    align: 'center',
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "成立日期",
    dataIndex: 'foundDate',
    width: 120,
    align: 'center',
  },
  {
    title: "启用状态",
    dataIndex: 'stopFlag',
    slotName: 'stopFlag',
    align: 'center',
    width: 100,
  },
  {
    title: "操作",
    dataIndex: 'operate',
    slotName: 'operate',
    align: 'center',
    fixed: "right",
    width: 150,
  }
]);

//表格数据
const renderData = ref<any[]>([]);

//删除弹框
const generateOperateModalModel = () => {
  return {
    //0 状态 1删除
    type: 0,
    visible: false,
    title: "",
    id: 0,
    value: 0,
    name: "",
    loading: false,
  };
};

//删除弹框模型
const operateModalModel = ref(generateOperateModalModel());

const formRef = ref<FormInstance>();

//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {
      entName: [{required: true, message: "请输入企业名称"}],
      // creditCode: [{required: true, message: "请输入社会信用代码"}],
    },
    fileLogoList: [],
    formModel: {
      id: 0,
      entName: '',
      entSn: '',
      entType: '',
      entLogo: '',
      entIntro: '',
      creditCode: '',
      legalPerson: '',
      linkName: '',
      linkMobile: '',
      linkAddress: '',
      email: '',
      businessScope: '',
      registeredCapital: 0,
      foundDate: '',
      openStartDate: '',
      openEndDate: '',
      address: '',
      regOffice: '',
      issueDate: '',
      logicCode: '',
      remark: '',
      createBy: '',
      createTime: '',
      updateBy: '',
      updateTime: '',
      stopFlag: 0,
      deleteFlag: 0,
    },
  };
};

//switch选中值
const checkedValue = ref<number>(0);
const unCheckedValue = ref<number>(1);
//表单模型
const formDrawer = ref(generateFormDrawerModel());

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

//重置查询条件
const search = () => {
  pagination.current = 1;

  fetchData();
}

//重置查询条件
const reset = () => {
  searchModel.value = generateSearchModel();
}

/**
 * 表格页码发生变化
 * @param val 变更值
 */
const onPageChange = (val: number) => {
  pagination.current = val;
  fetchData();
}

/**
 * 表格每页数量发生变化
 * @param val 变更值
 */
const onPageSizeChange = (val: number) => {
  pagination.pageSize = val;
  fetchData();
}


/**
 * 新增数据
 * @param row 数据行
 */
const handleAdd = () => {
  formDrawer.value.visible = true;
}


/**
 * 停用弹框
 */
const handleStopFlagClick = async (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.entName;
  operateModalModel.value.value = record.stopFlag == 0 ? 1 : 0;
  operateModalModel.value.type = 0;
}

/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  let result: any = await getEnterprise(record.id);
  try {
    setLoading(true);
    if (result.code == 200) {
      result.data.registeredCapital = parseFloat(result.data.registeredCapital);
      formDrawer.value.formModel = result.data;
      if (result.data.entLogo && result.data.entLogo.length > 0) {
        const logoItem = {
          name: '',
          url: result.data.entLogo
        }
        formDrawer.value.fileLogoList[0] = logoItem;
      } else {
        formDrawer.value.fileLogoList = []
      }
      formDrawer.value.visible = true;
    }
  } catch (e) {

  } finally {
    setLoading(false);
  }

}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = '删除';
  operateModalModel.value.name = record.entName;
  operateModalModel.value.type = 1;
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
      let info: any = await getEnterprise(operateModalModel.value.id);
      if (info.code == 200) {
        result = await updateEnterprise({...info.data, stopFlag: operateModalModel.value.value});
      } else {
        result = info;
      }
    } else {
      result = await delEnterprise(operateModalModel.value.id);
    }
    notification(result);
    if (result.code == 200) {
      pagination.current = 1;
      handleOperateModelCancle();
      await fetchData();
    }
  } catch (e) {
    console.error(e);
  } finally {
    setLoading(false);
    // operateModalModel.value.loading = false;
  }
}

const handleUploadLogo = (item: any) => {
  item.response = JSON.parse(item.response);
  console.log(item.response, 'lgoo')
  formDrawer.value.formModel.entLogo = item.response.data.url;
}

const handleDeleteLogo = (item: any) => {
  formDrawer.value.fileLogoList = [];
  formDrawer.value.formModel.entLogo = "";
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    try {
      formDrawer.value.loading = true;
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addEnterprise(formDrawer.value.formModel);
      } else {
        result = await updateEnterprise(formDrawer.value.formModel);
      }
      notification(result);
      if (result.code == 200) {
        handleFormCancel();
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
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = {...generateFormDrawerModel()};
  formRef?.value?.resetFields();
}

/**
 * 自定义上传
 */
const baseUrl = import.meta.env.VITE_API_BASE_URL;
const customRequest = (option: any) => {
  const {onProgress, onError, onSuccess, fileItem, name} = option
  const xhr = new XMLHttpRequest();
  if (xhr.upload) {
    xhr.upload.onprogress = function (event) {
      let percent;
      if (event.total > 0) {
        // 0 ~ 1
        percent = event.loaded / event.total;
      }
      onProgress(percent, event);
    };
  }
  xhr.onerror = function error(e) {
    onError(e);
  };
  xhr.onload = function onload() {
    if (xhr.status < 200 || xhr.status >= 300) {
      return onError(xhr.responseText);
    }
    onSuccess(xhr.response);
  };
  const token = getToken()
  const formData = new FormData();
  formData.append(name || 'file', fileItem.file);
  xhr.open('post', baseUrl + '/file/upload', true);
  xhr.setRequestHeader('Authorization', `Bearer ${token}`);
  xhr.send(formData);

  return {
    abort() {
      xhr.abort()
    }
  }
};

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res: any = await listEnterprise({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchModel.value,
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

onMounted(() => {
  fetchData();
})
</script>

<style scoped></style>
