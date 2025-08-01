<!--
 * 功能：公司列表
 * 作者：JIHONG.YANG
 * 日期：2023-10-25
-->

<template>
  <div>
    <a-card class="content">
      <!-- 查询条件 start -->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
            label-align="left" auto-label-width>
            <a-row :gutter="24">
              <a-col :span="8">
                <a-form-item field="companyName" :label="$t('manage.company.list.companyName')">
                  <a-input v-model="searchModel.companyName" :placeholder="$t('manage.company.list.companyNamePlaceholder')" @press-enter="search" allow-clear />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="groupId" :label="$t('manage.company.list.companyGrouping')">
                  <a-select v-model="searchModel.groupId" allow-clear>
                    <a-option :value="0" :label="$t('manage.company.list.groupAll')"></a-option>
                    <a-option v-for="dict in sys_company_group" :key="dict.value" :value="Number(dict.value)"
                      :label="dict.label"></a-option>
                  </a-select>
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
      <!-- 查询条件 end -->

      <!-- 分割线 -->
      <a-divider style="margin-top: 0" />

      <!-- 操作条 start -->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">

              <template #icon>
                <icon-plus />
              </template>
              {{$t('manage.company.list.newCompany')}}
            </a-button>
            <a-button @click="handleExpand"> {{$t('global.expand')}}/{{$t('global.fold')}} </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 操作条 end -->

      <!-- 表格 start -->
      <a-table row-key="id" ref="tableRef" :loading="loading" v-bind:hide-expand-button-on-empty="true"
        :bordered="{ wrapper: true, cell: true }" :scroll="{ x: 1350 }" :pagination="pagination"
        :columns="(columns as TableColumnData[])" :data="renderData" @page-change="onPageChange"
        @page-size-change="onPageSizeChange" show-page-size>

        <template #stopFlag="{ record }">
          <!-- {{ record.stopFlag == 1 ? "启用" : "停用" }} -->
          <stop-flag :value="record.stopFlag" />
        </template>

        <template #groupId="{ record }">
          <dict-tag :options="sys_company_group" :value="record.groupId" />
        </template>

        <template #operate="{ record }">
          <a-button size="small" type="text" @click="handleStopFlag(record)"
            :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdate(record)">{{$t('global.edit')}}</a-button>
          <a-button size="small" type="text" @click="handleDetail(record.id)">{{$t('global.detail')}}</a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{$t('global.delete')}}</a-button>
        </template>
      </a-table>
      <!-- 表格 end -->

      <!-- 操作弹框 start -->
      <a-modal width="auto" v-model:visible="operateModalModel.visible" class="modal-box">

        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
            style="color: rgb(var(--red-6)); margin-right: 5px" />
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px" />
          {{$t('global.confirmTip')}}
        </template>
        <div style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{ operateModalModel.name }}】的数据项？</div>

        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">{{$t('global.cancel')}}</a-button>
              <a-button type="primary" @click="handleOperateModelOk">{{$t('global.confirm')}}</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>
      <!-- 操作弹框 end -->

      <!-- 编辑、添加右弹层 start -->
      <a-drawer :width="900" :visible="formDrawer.visible" :ok-loading="formDrawer.loading"  @ok="handleSubmitForm" @cancel="handleFormCancel"
        :mask-closable="false">

        <template #title>
          {{ formDrawer.formModel.id ? $t('manage.company.list.editCompany') : $t('manage.company.list.addCompany') }}
        </template>
        <a-spin style="width: 100%;height: auto;" :loading="formDrawer.loading">
          <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
            :rules="formDrawer.rules">
            <a-row :gutter="24">
              <a-col :span="12">
                <a-divider orientation="left">{{$t('manage.company.list.companyInformation')}}</a-divider>

                <!-- 公司名称 -->
                <a-form-item field="companyName" :label="$t('manage.company.list.companyName')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.companyName" :max-length="30" allow-clear :placeholder="$t('manage.company.list.companyNameMaximum')" />
                </a-form-item>

                <!-- 上级目录 -->
                <a-form-item field="parentId" :label="$t('manage.company.list.parentDirectory')">
                  <a-tree-select :disabled="formDrawer.formModel.id > 0" allow-search
                    v-model="formDrawer.formModel.parentId" :data="parentList" :placeholder="$t('manage.company.list.rootDirectory')" :field-names="{
          key: 'id',
          title: 'companyName',
          children: 'children'
        }"></a-tree-select>
                </a-form-item>

                <!-- 公司分组 -->
                <a-form-item field="groupId" :label="$t('manage.company.list.companyGrouping')" :validate-trigger="['change', 'blur', 'input']">
                  <a-select v-model="formDrawer.formModel.groupId" :placeholder="$t('manage.company.list.groupSelect')" allow-clear>
                    <a-option v-for="dict in sys_company_group" :key="dict.value" :value="Number(dict.value)"
                      :label="dict.label"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 所属行业 -->
                <a-form-item field="industryGroupId" :label="$t('manage.company.list.industry')" :validate-trigger="['change', 'blur', 'input']">
                  <a-cascader v-model="formDrawer.formModel.industryGroupId"
                    :field-names="{ value: 'id', label: 'groupName' }" :options="groupTreeData" expand-trigger="hover"
                    :placeholder="$t('manage.company.list.pleaseSelect')" />
                </a-form-item>

                <!-- 公司地址 -->
                <a-form-item field="address" :label="$t('manage.company.list.companyAddress')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.address" allow-clear :max-length="200" />
                </a-form-item>

                <!-- 联系人员 -->
                <a-form-item field="linkName" :label="$t('manage.company.list.contactPerson')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.linkName" allow-clear :max-length="30" />
                </a-form-item>

                <!-- 联系电话 -->
                <a-form-item field="linkMobile" :label="$t('manage.company.list.contactNumber')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.linkMobile" allow-clear :max-length="11"  />
                </a-form-item>

                <!-- 固定电话 -->
                <a-form-item field="linkPhone" :label="$t('manage.company.list.landlineTelephone')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.linkPhone" allow-clear :max-length="11" />
                </a-form-item>

                <!-- 邮箱地址 -->
                <a-form-item field="email" :label="$t('manage.company.list.emailAddress')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.email" allow-clear :max-length="30" />
                </a-form-item>

                <!-- 公司网址 -->
                <a-form-item field="homePage" :label="$t('manage.company.list.companyWebsite')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.homePage" allow-clear :max-length="64" />
                </a-form-item>

                <!-- 公司简介 -->
                <a-form-item field="description" :label="$t('manage.company.list.companyProfile')">
                  <a-textarea v-model="formDrawer.formModel.description" allow-clear :max-length="200"
                    :auto-size="{ minRows: 2, maxRows: 5 }" :show-word-limit="true" :placeholder="$t('manage.company.list.profilePlaceholder')" />
                </a-form-item>
              </a-col>

              <a-col :span="12">
                <a-divider orientation="left">{{$t('manage.company.list.invoiceInformation')}}</a-divider>
                <!-- 发票名称 -->
                <a-form-item field="invoiceTitle" :label="$t('manage.company.list.invoiceName')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.invoiceTitle" allow-clear :max-length="64" />
                </a-form-item>

                <!-- 发票税号 -->
                <a-form-item field="invoiceTax" :label="$t('manage.company.list.invoiceTaxNumber')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.invoiceTax" allow-clear :max-length="18"/>
                </a-form-item>

                <!-- 发票地址 -->
                <a-form-item field="invoiceAddress" :label="$t('manage.company.list.invoiceAddress')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.invoiceAddress" allow-clear :max-length="200" />
                </a-form-item>

                <!-- 发票电话 -->
                <a-form-item field="invoicePhone" :label="$t('manage.company.list.invoiceTelephone')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.invoicePhone" allow-clear :max-length="11" />
                </a-form-item>

                <!-- 开户行 -->
                <a-form-item field="invoiceBack" :label="$t('manage.company.list.bankOfDeposit')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.invoiceBack" allow-clear :max-length="64"/>
                </a-form-item>

                <!-- 银行帐号 -->
                <a-form-item field="invoiceAccount" :label="$t('manage.company.list.accountNumber')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.invoiceAccount" allow-clear :max-length="64" />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-spin>
      </a-drawer>
      <!-- 编辑、添加右弹层 end -->

      <!-- 详情右弹层 start -->
      <a-drawer :width="900" :visible="formDrawer.visibleDetail" @ok="handleDetailOk" @cancel="handleDetailOk"
        :hide-cancel="true" :mask-closable="false" unmountOnClose>

        <template #title>
          {{ formDrawer.formModel.companyName }}
        </template>
        <a-row :gutter="24" style="padding: 0 20px;">
          <a-col :span="12">
            <a-divider orientation="left">公司信息</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.company" :column="1" size="large">

              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1" />
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>

          </a-col>
          <a-col :span="12">
            <a-divider orientation="left">发票信息</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.invoice" :column="1" size="large">

              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1" />
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
          </a-col>
        </a-row>
      </a-drawer>
      <!-- 详情右弹层 end -->
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { computed, getCurrentInstance, onMounted, reactive, ref } from 'vue';
import useLoading from '@/hooks/loading';
import { TableColumnData, TableInstance, TreeNodeData } from "@arco-design/web-vue";
import { getCompany, delCompany, addCompany, updateCompany, listCompanyAll, changeCompanyStatus, listCompany } from "@/api/system/company";
import { BasePagination } from '@/api/common';
import { FormInstance } from '@arco-design/web-vue/es/form';
import { handleRemoveChildren} from "@/utils/ruoyi";
import { getGroup, listGroupAll } from '@/api/system/group';
import { getDictLabel } from '@/utils/dict';
import { notification } from "@/hooks/my-design";

/*************************** 变量区域 begin ***************************/

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_company_group,
  sys_normal_disable,

} = proxy?.useDict(
  "sys_company_group",
  "sys_normal_disable",
);
//******* 这里编写动态获取下拉列表 end ******

//表格实例
const tableRef = ref<TableInstance>();
//展开/折叠控制值
const expandValue = ref<boolean>(false);
//加载中
const { loading, setLoading } = useLoading(true);
//表格数据
const renderData = ref<any[]>([]);
//所属行业
const groupTreeData = ref<any[]>([]);
//默认上级目录
const defaultParentList = ref<any>(
  [
    {
      id: 0,
      companyName: "根目录"
    }
  ]
);
//上级目录
const parentList = ref<TreeNodeData[]>();
//表格分页参数
const pagination: any = reactive({ ...BasePagination() });
//详情
const detailDataList = ref<any>({});
//设置表格列
const columns = computed<TableColumnData[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    align: 'center',
    fixed: 'left',
    width: 80,
  }, {
    title: "公司名称",
    dataIndex: 'companyName',
    align: 'left',
    width: 300,
    fixed: "left",
  },
  {
    title: "公司分组",
    dataIndex: 'groupId',
    slotName: 'groupId',
    align: 'left',
    width: 150,
  },
  {
    title: "所属行业",
    dataIndex: 'industryGroupName',
    align: 'left',
    width: 200,
  },
  {
    title: "联系人",
    dataIndex: 'linkName',
    align: 'left',
    width: 150,
  },
  {
    title: "联系电话",
    dataIndex: 'linkMobile',
    align: 'left',
    width: 150,
  },
  {
    title: "启用状态",
    dataIndex: 'stopFlag',
    slotName: 'stopFlag',
    align: 'center',
    fixed: "right",
    width: 100
  },
  {
    title: "操作",
    dataIndex: 'operate',
    slotName: 'operate',
    align: 'center',
    fixed: "right",
    width: 220
  }
]);
//生成查询条件对象
const generateSearchModel = () => {
  return {
    // 公司名称
    companyName: undefined,
    groupId: 0
  };
};
//查询表单对象
const searchModel = ref(generateSearchModel());
//生成表单模型
const generateFormDrawerModel = () => {
  return {
    loading: false,
    visible: false,
    visibleDetail: false,
    rules: {
      companyName: [{ required: true, message: "请输入公司名称" }],
      parentId: [{ required: true, message: "请选择上级目录" }],
      groupId: [{ required: true, message: "请选择公司分组" }],
      industryGroupId: [{ required: true, message: "请选择所属行业" }],
    },
    formModel: {
      id: 0,
      entId: 0,
      deptId: 0,
      parentId: undefined,
      companyName: undefined,
      companyNameEn: undefined,
      logicCode: undefined,
      groupId: undefined,
      companyType: undefined,
      industryGroupId: undefined,
      logoUrl: undefined,
      companyGrade: undefined,
      companyScale: undefined,
      province: 0,
      city: 0,
      county: 0,
      address: undefined,
      postCode: undefined,
      linkName: undefined,
      linkMobile: undefined,
      linkPhone: undefined,
      linkFax: undefined,
      email: undefined,
      invoiceTitle: undefined,
      invoiceTax: undefined,
      invoiceAddress: undefined,
      invoicePhone: undefined,
      invoiceBack: undefined,
      invoiceAccount: undefined,
      description: undefined,
      homePage: undefined,
      remark: undefined,
      createBy: undefined,
      createTime: undefined,
      updateBy: undefined,
      updateTime: undefined,
      stopFlag: 0,
      deleteFlag: 0,
    }
  };
};
//编辑表单对象
const formDrawer = ref(generateFormDrawerModel());
//编辑表单实例
const formRef = ref<FormInstance>();
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
 * 查询条件
 */
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

/**
 * 重置查询条件
 */
const reset = () => {
  searchModel.value = generateSearchModel();
}

/**
 * 展开/折叠
 */
const handleExpand = () => {
  expandValue.value = !expandValue.value;
  tableRef.value?.expandAll(expandValue.value);
}

/**
 * 表格页码发生变化
 * @param val 变更值
 */
const onPageChange = async (val: number) => {
  pagination.current = val;
  await fetchData();
}

/**
 * 表格每页数量发生变化
 * @param val 变更值
 */
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchData();
}

/**
 * 新增数据
 * @param row 数据行
 */
const handleAdd = async () => {
  formDrawer.value.visible = true;
  formDrawer.value.loading = true;
  try {
    await fetchParentData();
    await fetchDataGroup();
  } catch (ex) {
    console.error("handleAdd", ex)
  } finally {
    formDrawer.value.loading = false;
  }

}

/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  formDrawer.value.visible = true;
  formDrawer.value.loading = true;
  try {
    await fetchParentData();
    await fetchDataGroup();
    let result = await getCompany(record.id);
    if (result.code == 200) {
      formDrawer.value.formModel = result.data;
      formDrawer.value.visible = true;
    }
  } catch (ex) {
    console.error("handleAdd", ex)
  } finally {
    formDrawer.value.loading = false;
  }
}


/**
 * 停用
 * @param record
 */
const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.companyName;
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
  operateModalModel.value.name = record.companyName;
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
  let result:any = {};
  operateModalModel.value.visible = false;
  setLoading(true);
  try {
    if (operateModalModel.value.type == 0) {
      result = await changeCompanyStatus(operateModalModel.value.id, operateModalModel.value.value);
    } else {
      result = await delCompany(operateModalModel.value.id);
    }
    notification(result);
    if (result.code == 200) {
      handleOperateModelCancle();
      await fetchData();
    }
  }catch (e) {
    console.error(e);
  }finally {
    setLoading(false);
  }
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    formDrawer.value.loading = true;
    try {
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addCompany(formDrawer.value.formModel);
      } else {
        result = await updateCompany(formDrawer.value.formModel);
      }
      notification(result);
      fetchData();
      formDrawer.value = generateFormDrawerModel();
    }catch (e) {
      console.error(e);
    }finally {
      formDrawer.value.loading = false;
    }
  }
}

/**
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
  formRef.value?.resetFields();
}

/**
 * 详情页弹出
 */
const handleDetail = async (id: any) => {
  formDrawer.value.visibleDetail = true;
  formDrawer.value.loading = true;
  try {
    let res: any = await getCompany(id);
    if (res.code == 200) {
      formDrawer.value.formModel = res.data;
      let parentName = '';
      let resParent: any = await getCompany(res.data.parentId);
      if (resParent.code == 200 && resParent.data) {
        parentName = resParent.data.companyName;
      }
      let industryGroupName = '';
      let resGroup: any = await getGroup(res.data.industryGroupId);
      if (resGroup.code == 200 && resGroup.data) {
        industryGroupName = resGroup.data.groupName;
      }
      detailDataList.value = {
        company: [
          { label: '公司名称：', value: res.data.companyName || '--' },
          { label: '上级目录：', value: parentName || '--' },
          { label: '公司分组：', value: getDictLabel("sys_company_group", res.data.groupId) || '--' },
          { label: '所属行业：', value: industryGroupName || '--' },
          { label: '公司地址：', value: res.data.address || '--' },
          { label: '联系人员：', value: res.data.linkName || '--' },
          { label: '联系电话：', value: res.data.linkMobile || '--' },
          { label: '固定电话：', value: res.data.linkPhone || '--' },
          { label: '邮箱地址：', value: res.data.email || '--' },
          { label: '公司网址：', value: res.data.homePage || '--' },
          { label: '公司简介：', value: res.data.description || '--' }
        ],
        invoice: [
          { label: '发票名称：', value: res.data.invoiceTitle || '--' },
          { label: '发票税号：', value: res.data.invoiceTax || '--' },
          { label: '发票地址：', value: res.data.invoiceAddress || '--' },
          { label: '发票电话：', value: res.data.invoicePhone || '--' },
          { label: '开户行：', value: res.data.invoiceBack || '--' },
          { label: '银行帐号：', value: res.data.invoiceAccount || '--' }
        ]
      }

    }
  } catch (e) {

  } finally {
    formDrawer.value.loading = false;
  }
}



/**
 * 详情页确定
 */
const handleDetailOk = () => {
  formDrawer.value = generateFormDrawerModel();
}

/**
 * 查询所属行业
 */
const fetchDataGroup = async () => {
  try {
    const res = await listGroupAll({
      mapId: 1
    });
    handleRemoveChildren(res.data);
    groupTreeData.value = res.data;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};


/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listCompany({
      companyName: searchModel.value.companyName,
      groupId: searchModel.value.groupId == 0 ? undefined : searchModel.value.groupId
    });

    // let treeData = handleTreeNodeData(res.rows, "id", "companyName");
    // parentList.value = defaultParentList.value.concat(treeData);
    renderData.value = res.rows;
    pagination.total = res.total;
  } catch (err) {
    console.error("fetchData", err);
  } finally {
    setLoading(false);
  }
};

/**
 * 查询表格数据
 */
const fetchParentData = async () => {
  try {
    const res = await listCompanyAll({});
    // let treeData = handleTreeNodeData(res.rows, "id", "companyName");
    parentList.value = defaultParentList.value.concat(res.data);
  } catch (err) {
    console.error("fetchParentData", err);
  } finally {
  }
};

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
  // tableRef.value?.expandAll(true);
})
</script>

<style scoped>
.title {
  font-size: 16px;
  font-weight: 600;
  line-height: 1.6;
  color: var(--color-neutral-6);
  border-bottom: 1px dashed var(--color-neutral-8);
  margin: 0 0 25px 60px;
}

.desContent {
  margin-top: 20px;
  margin-left: 100px;
  margin-right: 100px;
}
</style>
