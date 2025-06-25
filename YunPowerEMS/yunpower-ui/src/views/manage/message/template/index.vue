<!--
 * 功能：消息模板
 * 作者：曹晓桐
 * 日期：2023-11-7
-->
<template>
  <div>
    <a-card class="content">
      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left"
            auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="templateType" :label="$t('manage.message.template.templateType')">
                  <a-select v-model="searchModel.templateType" :placeholder="$t('manage.message.template.typePlaceholder')" allow-clear>
                    <a-option key="0" :label="$t('manage.message.template.allTemplates')" value="0" />
                    <a-option v-for="dict in sys_template_type" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="topic" :label="$t('manage.message.template.messageTopic')">
                  <a-select v-model="searchModel.topic" :placeholder="$t('manage.message.template.topicPlaceholder')" allow-clear>
                    <a-option key="0" :label="$t('manage.message.template.allTopics')" value="0" />
                    <a-option v-for="dict in sys_message_topic" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
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
      <!-- 查询条件 end-->

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
              {{$t('manage.message.template.newTemplate')}}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table row-key="id" ref="tableRef" :loading="loading" :pagination="pagination" @page-change="onPageChange"
        :scroll="{ x: 1500 }" @page-size-change="onPageSizeChange" :bordered="{ wrapper: true, cell: true }"
        :columns="columns" :data="renderData">

        <!-- 模板类型 -->
        <template #templateType="{ record }">
          <dict-tag :options="sys_template_type" :value="record.templateType" />
        </template>

        <!-- 消息主题 -->
        <template #topic="{ record }">
          <dict-tag :options="sys_message_topic" :value="record.topic" />
        </template>

        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"></stop-flag>
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" @click="handleStopFlag(record)"
            :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdate(record)">{{$t('global.edit')}}</a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{$t('global.delete')}}
          </a-button>
        </template>
      </a-table>

      <!-- 操作弹框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible" class="modal-box">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
            style="color: rgb(var(--red-6)); margin-right: 5px" />
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px" />
          {{$t('global.confirmTip')}}
        </template>
        <div style="text-align: center;">是否确认{{ operateModalModel.title }}编号为【{{ operateModalModel.name }}】的数据项？</div>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">{{$t('global.cancel')}}</a-button>
              <a-button type="primary" @click="handleOperateModelOk">{{$t('global.confirm')}}</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>

      <!-- 添加右弹层 start -->
      <a-drawer :width="750" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm" @cancel="handleFormCancel"
        :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? $t('manage.message.template.editTemplate') : $t('manage.message.template.addTemplate') }}
        </template>
        <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                      :rules="formDrawer.rules">

                <!-- 模板类型 -->
                <a-form-item field="templateType" :label="$t('manage.message.template.templateType')">
                  <a-select v-model="formDrawer.formModel.templateType" :placeholder="$t('manage.message.template.typePlaceholder')" allow-clear>
                    <a-option v-for="dict in sys_template_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)" />
                  </a-select>
                </a-form-item>

                <!-- 消息主题 -->
                <a-form-item field="topic" :label="$t('manage.message.template.messageTopic')">
                  <a-select v-model="formDrawer.formModel.topic" :placeholder="$t('manage.message.template.topicPlaceholder')" allow-clear
                            @change="handleTopicChange">
                    <a-option v-for="dict in sys_message_topic" :key="dict.value" :label="dict.label" :value="dict.value" />
                  </a-select>
                </a-form-item>

                <!-- 主题代码  -->
                <a-form-item field="topicSn" :label="$t('manage.message.template.subjectCode')">
                  <a-input readonly v-model="formDrawer.formModel.topicSn" />
                </a-form-item>

                <!-- 模板ID  -->
                <a-form-item field="templateId" :label="$t('manage.message.template.templateID')">
                  <a-input v-model="formDrawer.formModel.templateId" />
                </a-form-item>

                <!-- 短信渠道 -->
                <a-form-item field="smsChannel" :label="$t('manage.message.template.shortMessage')">
                  <a-select v-model="formDrawer.formModel.smsChannel" :placeholder="$t('manage.message.template.shortMessageSelect')" allow-clear>
                    <a-option v-for="dict in sys_sms_channel" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)" />
                  </a-select>
                </a-form-item>

                <!-- 短信签名  -->
                <a-form-item field="smsSign" :label="$t('manage.message.template.shortMessageSign')">
                  <a-input v-model="formDrawer.formModel.smsSign" />
                </a-form-item>

                <!-- 启用状态 -->
                <a-form-item field="stopFlag" :label="$t('manage.message.template.enabledState')">
                  <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                            v-model="formDrawer.formModel.stopFlag"><template #checked>{{$t('global.enable')}}</template><template
                      #unchecked>{{$t('global.forbidden')}}</template></a-switch>
                </a-form-item>

                <a-form-item field="templateContent" :label="$t('manage.message.template.templateContent')">
                  <a-textarea :placeholder="$t('manage.message.template.templatePlaceholder')" :max-length="200"
                              v-model="formDrawer.formModel.templateContent" allow-clear />
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
import { TableInstance } from "@arco-design/web-vue";
import { BasePagination } from '@/api/common';
import { notification } from "@/hooks/my-design";
import { FormInstance } from '@arco-design/web-vue/es/form';
import { addMessageTemplate, changeMessageTemplateStatus, delMessageTemplate, getMessageTemplate, listMessageTemplate, updateMessageTemplate } from '@/api/system/message-template';

/*************************** 变量区域 begin ***************************/

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_message_topic, sys_template_type, sys_sms_channel } = proxy?.useDict("sys_message_topic", "sys_template_type", "sys_sms_channel");
//******* 这里编写动态获取下拉列表 end ******

//生成查询条件对象
const generateSearchModel = () => {
  return {
    templateType: "0",
    topic: "0",
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const { loading, setLoading } = useLoading(true);
//表格分页参数
const pagination: any = reactive({ ...BasePagination() });
//表格实例
const tableRef = ref<TableInstance>();
//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    align: 'center',
    fixed: "left",
    width: 80
  },
  {
    title: "模板类型",
    dataIndex: "templateType",
    slotName: "templateType",
    align: 'center',
    fixed: "left",
    width: 120
  },
  {
    title: "消息主题",
    dataIndex: "topic",
    slotName: "topic",
    align: 'center',
    fixed: "left",
    width: 120
  },
  {
    title: "主题代码",
    dataIndex: "topicSn",
    align: 'center',
    width: 120
  },
  {
    title: "模板ID",
    dataIndex: "templateId",
    align: 'center',
    width: 120
  },
  {
    title: "短信签名",
    dataIndex: "smsSign",
    align: 'center',
    width: 180,
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "模板内容",
    dataIndex: "templateContent",
    align: 'center',
    width: 500,
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100,
    fixed: "right",
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 180,
    slotName: 'operate',
    align: 'center',
    fixed: "right",
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
//表格示例
const formRef = ref<FormInstance>();
//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {
      templateType: [{ required: true, message: "请输入模板类型" }],
      topic: [{ required: true, message: "请选择消息主题" }],
      topicSn: [{ required: true, message: "请选择消息主题" }],
    },
    formModel: {
      id: 0,
      remark: undefined,
      smsChannel: undefined,
      smsSign: undefined,
      stopFlag: 0,
      templateContent: undefined,
      templateId: undefined,
      templateType: undefined,
      topic: undefined,
      topicSn: undefined,
      deleteFlag: 0,

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


/**
 * 消息主题改变
 */
const handleTopicChange = () => {
  formDrawer.value.formModel.topicSn = formDrawer.value.formModel.topic;
}

/**
 * 表格页码发生变化
 * @param val
 */
const onPageChange = async (val: number) => {
  pagination.current = val;
  await fetchData();
}

/**
 * 表格每页数量发生变化
 * @param val 值
 */
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchData();
}

//重置查询条件
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

/**
 * 新增数据
 * @param row 数据行
 */
const handleAdd = async () => {
  formDrawer.value.visible = true;
}

/**
 * 删除空数据
 * @param data
 */
const removeEmptyChildren = (data: any) => {
  data.forEach((item: any, index: number) => {
    if (item.children && item.children.length === 0) {
      delete data[index].children;
    } else if (item.children) {
      removeEmptyChildren(item.children);
    }
  });
}

/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  let result = await getMessageTemplate(record.id);
  if (result.code == 200) {
    result.data.templateId += "";
    formDrawer.value.formModel = result.data;
    formDrawer.value.visible = true;
  }
}

const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "$t('global.enable')";
  operateModalModel.value.name = record.id;
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
  operateModalModel.value.name = record.id;
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
      result = await changeMessageTemplateStatus(operateModalModel.value.id, operateModalModel.value.value);
    } else {
      result = await delMessageTemplate(operateModalModel.value.id);
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
        result = await addMessageTemplate(formDrawer.value.formModel);
      } else {
        result = await updateMessageTemplate(formDrawer.value.formModel);
      }
      notification(result);
      if (result.code == 200) {
        handleFormCancel();
        await fetchData();
      }
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
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listMessageTemplate({
      templateType: searchModel.value.templateType == '0' ? undefined : searchModel.value.templateType,
      topic: searchModel.value.topic == '0' ? undefined : searchModel.value.topic,
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

onMounted(async () => {
  await fetchData();
})
</script>

<style scoped></style>
