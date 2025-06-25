<!--
 * 功能：参数配置
 * 作者：王小明
 * 日期：2023-11-7
-->
<template>
  <div>
    <a-card class="content">
      <!-- 表单搜索 -->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left"
            auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="paramKey" :label="$t('manage.system.parameter')">
                  <a-input v-model="searchModel.paramKey" :placeholder="$t('manage.system.key')" allow-clear />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-space direction="vertical" :size="18">
                  <a-button type="primary" @click="search">
                    <template #icon>
                      <icon-search />
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
      <a-divider style="margin-top: 0" />

      <!-- 按钮 -->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus />
              </template>
              {{ $t('manage.system.newParameter') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table row-key="id" :loading="loading" :pagination="pagination" :bordered="{ wrapper: true, cell: true }"
        :columns="columns" :data="renderData" @page-change="onPageChange"
        @page-size-change="onPageSizeChange" show-page-size :scroll="{x:1145}">
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"></stop-flag>
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" @click="handleStopFlag(record)"
            :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdate(record)">{{ $t('global.edit') }}</a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
        </template>
      </a-table>

      <!-- 操作弹框 start -->
      <a-modal width="400px" v-model:visible="operateModalModel.visible" class="modal-box">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
            style="color: rgb(var(--red-6)); margin-right: 5px" />
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px" />
          确认提示
        </template>
        <div style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{ operateModalModel.name }}】的数据项？</div>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">取消</a-button>
              <a-button type="primary" @click="handleOperateModelOk">确认</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>

      <!-- 添加右弹层 start -->
      <a-drawer :width="750" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm" @cancel="handleFormCancel"
        :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? $t('manage.system.editParameter') : $t('manage.system.newParameter') }}
        </template>
        <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
          <a-row :gutter="24" class="row-mp-30">
            <a-col :span="24">
              <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right" :rules="formDrawer.rules">

                <a-form-item field="paramKey" :label="$t('manage.system.keyName')">
                  <a-input :max-length="100" :placeholder="$t('global.please')" v-model="formDrawer.formModel.paramKey" />
                </a-form-item>
                <a-form-item field="paramValue" :label="$t('manage.system.value')">
                  <a-input :max-length="500" :placeholder="$t('manage.system.inputValue')" v-model="formDrawer.formModel.paramValue" />
                </a-form-item>
                <a-form-item field="isSystem" :label="$t('manage.system.systemBuiltIn')">
                  <a-switch :checked-value="unCheckedValue" :unchecked-value="checkedValue"
                            v-model="formDrawer.formModel.isSystem"><template #checked>是</template><template
                      #unchecked>否</template></a-switch>
                </a-form-item>
                <a-form-item field="enabledState" :label="$t('global.status')">
                  <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                            v-model="formDrawer.formModel.stopFlag"><template #checked>{{$t('global.enable')}}</template><template
                      #unchecked>{{$t('global.forbidden')}}</template></a-switch>
                </a-form-item>
                <a-form-item field="paramInfo" :label="$t('manage.system.parameter')">
                  <a-textarea :placeholder="$t('manage.system.inputInterpretation')" :max-length="200" v-model="formDrawer.formModel.paramInfo"
                              allow-clear />
                </a-form-item>

              </a-form>
            </a-col>
          </a-row>
        </a-spin>
      </a-drawer>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { computed, getCurrentInstance, onMounted, reactive, ref } from 'vue';
import useLoading from '@/hooks/loading';
import { notification } from "@/hooks/my-design";
import { BasePagination } from '@/api/common';
import { FormInstance } from '@arco-design/web-vue/es/form';
import { listConfig, getConfig, addConfig, updateConfig, delConfig } from '@/api/manage/system/parameterSetting';

/*************************** 变量区域 begin ***************************/
//生成查询条件对象
const generateSearchModel = () => {
  return {
    paramKey: ''
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());

//加载中
const { loading, setLoading } = useLoading(true);


//表格分页参数
const pagination: any = reactive({ ...BasePagination() });

//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    width: 80,
    align: "center",
  },
  {
    title: "KEY名称",
    dataIndex: "paramKey",
    width: 200,
    align: "left",
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "VALU值",
    width: 300,
    dataIndex: "paramValue",
    align: "left",
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "参数解释",
    width: 300,
    dataIndex: "paramInfo",
    align: "left",
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "启用状态",
    width: 100,
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: "center",
    fixed:"right"
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 165,
    slotName: "operate",
    align: "center",
    fixed:"right"
  },
]);

//表格数据
const renderData = ref<any[]>([]);

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

const formRef = ref<FormInstance>();

//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {
      paramKey: [{ required: true, message: "请输入KEY名称" }],
      paramValue: [{ required: true, message: "请输入VALUE值" }],
    },
    formModel: {
      id: 0,
      paramKey: '',
      paramValue: '',
      paramInfo: '',
      isSystem: 1,
      stopFlag: 0
    }
  };
};

//表单模型
const formDrawer = ref(generateFormDrawerModel());

//switch选中值
const checkedValue = ref<number>(0);
//switch未选中值
const unCheckedValue = ref<number>(1);

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

//重置查询条件
const search = async () => {
  pagination.current = 1;
  await fetchData();
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
const handleAdd = () => {
  formDrawer.value.visible = true;
}

/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  let result = await getConfig(record.id);
  if (result.code == 200) {
    formDrawer.value.formModel = result.data;
    formDrawer.value.visible = true;
  }
}

const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.paramKey;
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
  operateModalModel.value.name = record.paramKey;
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
      let info = await getConfig(operateModalModel.value.id);
      if (info.code == 200) {
        result = await updateConfig({ ...info.data, stopFlag: operateModalModel.value.value });
      } else {
        result = info;
      }
    } else {
      result = await delConfig(operateModalModel.value.id);
    }
    notification(result);
    if (result.code == 200) {
      pagination.current = 1;
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
        result = await addConfig(formDrawer.value.formModel);
        if (result.code == 200) {
          pagination.current = 1;
        }
      } else {
        result = await updateConfig(formDrawer.value.formModel);
      }
      notification(result);
      handleFormCancel();
      await fetchData();
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
  formRef?.value?.resetFields();
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listConfig({
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
