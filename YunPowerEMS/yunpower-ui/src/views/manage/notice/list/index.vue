<template>
  <div>
    <a-card class="content" :title="$t('manage.notice.list.queryTable')">
      <!-- 表单搜索 -->
      <a-row>
        <a-col :flex="1">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left"
            auto-label-width>
            <a-row :gutter="16">
              <a-col :span="12">
                <a-form-item field="title" :label="$t('manage.notice.list.noticeTitle')">
                  <a-input v-model="searchModel.noticeTitle" :placeholder="$t('manage.notice.list.noticePlaceholder')" allow-clear />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item :label="$t('manage.notice.list.noticeType')">
                  <a-select v-model="searchModel.noticeType" :placeholder="$t('manage.notice.list.typePlaceholder')" allow-clear>
                    <a-option v-for="dict in sys_notice_type" :key="dict.value" :label="dict.label"
                      :value="dict.value" /></a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 35px" direction="vertical" />
        <a-col :flex="'86px'" style="text-align: right">
          <a-space direction="vertical" :size="18">
            <a-button type="primary" @click="search">
              <template #icon>
                <icon-search />
              </template>
              {{$t('global.search')}}
            </a-button>
          </a-space>
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
              {{$t('manage.notice.list.newNotice')}}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table row-key="id" :bordered="{ wrapper: true, cell: true }" :loading="loading" :pagination="pagination"
        :columns="columns" :data="renderData" @page-change="onPageChange"
        @page-size-change="onPageSizeChange" show-page-size>
        <template #noticeType="{ record }">
          <dict-tag :options="sys_notice_type" :value="record.noticeType" />
        </template>
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag" />
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

      <!-- 添加右弹层 start -->
      <a-drawer :width="750" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm" @cancel="handleFormCancel"
        :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? $t('manage.notice.list.editNotice') : $t('manage.notice.list.newNotice') }}
        </template>
        <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
          <a-row :gutter="24" class="row-mp-30">
            <a-col :span="24">
              <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                      :rules="formDrawer.rules">
                <!-- 公告标题 -->
                <a-form-item field="noticeTitle" :label="$t('manage.notice.list.noticeTitle')" :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.noticeTitle" allow-clear :placeholder="$t('manage.notice.list.noticePlaceholder')" />
                </a-form-item>

                <!-- 公告类型 -->
                <a-form-item field="noticeType" :label="$t('manage.notice.list.noticeType')" :validate-trigger="['change', 'blur', 'input']">
                  <a-select v-model="formDrawer.formModel.noticeType" :placeholder="$t('manage.notice.list.typePlaceholder')" allow-clear>
                    <a-option v-for="dict in sys_notice_type" :key="dict.value" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>
                <!-- 启用状态 -->
                <a-form-item field="stopFlag" :label="$t('manage.notice.list.enabledstate')">
                  <a-switch :checked-value="0" :unchecked-value="1" v-model="formDrawer.formModel.stopFlag"><template
                      #checked>{{$t('global.on')}}</template><template #unchecked>{{$t('global.outOfService')}}</template></a-switch>
                </a-form-item>

                <!-- 公告内容 -->
                <a-form-item field="stopFlag" :label="$t('manage.notice.list.noticeContent')">
                  <editor v-if="formDrawer.visible" ref="editorRef" style=" width: 650px;border: 1px solid #ccc;"
                          :value="formDrawer.formModel.noticeContent" @change="editorChange" />
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
import { listNotice, getNotice, addNotice, updateNotice, delNotice, changeNoticeStatus } from "@/api/manage/notice/list";
import { BasePagination } from '@/api/common';
import { FormInstance } from '@arco-design/web-vue/es/form';
import editor from "@/components/editor/index.vue";
import {notification} from "@/hooks/my-design";
import { listGroup } from '@/api/system/group';
import { handleCascaderOptionData } from "@/utils/ruoyi";
/*************************** 变量区域 begin ***************************/
//生成查询条件对象
const generateSearchModel = () => {
  return {
    noticeTitle: "",
    noticeType: ""
  };
};

//******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_notice_type } = proxy?.useDict("sys_notice_type");
//******* 这里写动态获取下拉列表 end ******

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
    align: 'center',
    width: 150
  },
  {
    title: "公告标题",
    dataIndex: "noticeTitle"
  },
  {
    title: "公告类型",
    dataIndex: "noticeType",
    slotName: "noticeType",
    align: 'center',
    width: 200
  },
  {
    title: "创建人",
    dataIndex: "createBy",
    align: 'center',
    width: 200
  },
  {
    title: "创建日期",
    dataIndex: "createTime",
    align: 'center',
    width: 200
  },
  {
    title: "状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 180,
    slotName: 'operate',
    align: 'center'
  },
]);

//表格数据
const renderData = ref<any[]>([]);
const formRef = ref<FormInstance>();
//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {
      noticeTitle: [{ required: true, message: '请输入公告标题' }],
      noticeType: [{ required: true, message: '请选择公告类型' }],
    },
    formModel: {
      id: 0,
      noticeTitle: "",
      noticeType: "",
      stopFlag: 0,
      noticeContent: "",
      groupId: "",
    }
  };
};

//表单模型
const formDrawer = ref(generateFormDrawerModel());

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
 * 搜索
 */
const search = async () => {
  pagination.current = 1;
  await fetchData();
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
 * 停用
 * @param record
 */
const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.noticeTitle;
  operateModalModel.value.value = record.stopFlag == 0 ? 1 : 0;
  operateModalModel.value.type = 0;
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
  let result = await getNotice(record.id);
  if (result.code == 200) {
    result.data.noticeType += "";
    formDrawer.value.formModel = result.data;
    formDrawer.value.visible = true;
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
  operateModalModel.value.name = record.noticeTitle;
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
      result = await changeNoticeStatus(operateModalModel.value.id, operateModalModel.value.value);
    } else {
      result = await delNotice(operateModalModel.value.id);
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
        result = await addNotice(formDrawer.value.formModel);
      } else {
        result = await updateNotice(formDrawer.value.formModel);
      }
      notification(result);
      handleFormCancel();
      fetchData();
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
  formRef.value?.resetFields()
  formDrawer.value = generateFormDrawerModel();
  formDrawer.value.formModel.noticeContent = "";
}

/**
 * 输入查询
 */
const handleQuery = async () => {
  pagination.current = 1;
  await fetchData();
}

const editorChange = (html: any) => {
  formDrawer.value.formModel.noticeContent = html;
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listNotice({
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
