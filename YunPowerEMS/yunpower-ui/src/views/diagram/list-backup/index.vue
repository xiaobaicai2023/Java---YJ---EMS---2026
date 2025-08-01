<template>
  <div>
    <a-card class="content">

      <!-- 操作条 start-->
      <a-row style="margin-bottom: 16px;">
        <a-col :span="12">
          <a-button type="primary" @click="handleAdd">
            <template #icon>
              <icon-plus />
            </template>
            {{$t('menu.diagram.project.power.list.create')}}
          </a-button>
        </a-col>
      </a-row>
      <!-- 操作条 end-->

      <!-- 表格内容 start -->
      <a-table row-key="id" ref="tableRef" :hide-expand-button-on-empty="true" :scroll="{ x: 1140 }"
        :bordered="{ wrapper: true, cell: true }" :loading="loading" :columns="columns" :data="renderData"
        @page-change="onPageChange" @page-size-change="onPageSizeChange" show-page-size>
        <template #stationType="{ record }">
          <dict-tag :options="sys_station_type" :value="record.stationType" />
        </template>
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag" />
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" status="success" @click="handleDesign(record.id)">{{$t('global.design')}}</a-button>
          <a-button size="small" type="text" status="success" @click="handleEdit(record.id)">{{$t('global.edit')}}</a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{$t('global.delete')}}</a-button>
        </template>
      </a-table>
      <!-- 表格内容 end -->

      <!-- 添加右弹层 start -->
      <a-drawer :width="750" :visible="formDrawer.visible" :mask-closable="false" @ok="handleSubmitForm"
        @cancel="handleFormCancel">
        <template #title>
          {{ formDrawer.formModel.id ? $t('menu.diagram.project.power.list.editItem') : $t('menu.diagram.project.power.list.addItem') }}
        </template>
        <a-spin style="width: 100%;height: auto;" :loading="formDrawer.loading">
          <a-row :gutter="24" class="row-mp-30">
            <a-col :span="24">
              <a-form ref="formRef" :model="formDrawer.formModel" label-align="right" :rules="formDrawer.rules"
                auto-label-width>

                <!-- 站点类型 -->
                <a-form-item field="stationType" :label="$t('menu.diagram.project.power.list.siteType')">
                  <a-select :disabled="formDrawer.formModel.id == 0 ? false : true"
                    v-model="formDrawer.formModel.stationType" :placeholder="$t('menu.diagram.project.power.list.siteTypeSelect')" allow-clear>
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                      :value="Number(dict.value)"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 项目名称 -->
                <a-form-item field="projectName" :label="$t('menu.diagram.project.power.list.projectName')" :validate-trigger="['blur']">
                  <a-input v-model="formDrawer.formModel.projectName" :max-length="20" allow-clear
                    :placeholder="$t('menu.diagram.project.power.list.projectNamePlaceholder')" />
                </a-form-item>

                <!-- 状态  -->
                <a-form-item field="stopFlag" :label="$t('menu.diagram.project.power.list.projectStatus')">
                  <a-switch v-model="formDrawer.formModel.stopFlag" :checked-value="1" :unchecked-value="0"><template
                      #checked>{{$t('global.normal')}}</template><template #unchecked>{{$t('global.outOfService')}}</template></a-switch>
                </a-form-item>

                <a-form-item field="remark" :label="$t('global.remarks')">
                  <a-textarea v-model="formDrawer.formModel.remark" allow-clear :max-length="200" :auto-size="true"
                    :show-word-limit="true" :placeholder="$t('menu.diagram.project.power.list.remarksPlaceholder')" />
                </a-form-item>

              </a-form>
            </a-col>
          </a-row>
        </a-spin>
      </a-drawer>
      <!-- 添加右弹层 end -->
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
      <!-- 操作弹框 end -->
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { computed, getCurrentInstance, onMounted, reactive, ref } from "vue"
import { BasePagination } from "@/api/common";
import useLoading from '@/hooks/loading';
import {
  addWebtopoProject, listWebtopoProject, updateWebtopoProject, getWebtopoProject, delWebtopoProject
} from "@/api/system/webtopo";
import { FormInstance, TableColumnData, TableInstance } from "@arco-design/web-vue";
import { notification } from "@/hooks/my-design";
import { StationTypeEnum } from "@/api/system/device";
import { useRouter } from "vue-router";

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_station_type } = proxy?.useDict("sys_station_type");
//******* 这里编写动态获取下拉列表 end ******

/*************************** 变量区域 begin ***************************/
//路由
const router = useRouter();
//组件参数
const props = defineProps({
  stationType: {
    type: Number,
    default: StationTypeEnum.power,
  }
})
//生成查询条件
const generateFormModel = () => {
  return {
    projectName: "",
  };
};
//表格分页配置
const pagination: any = reactive({ ...BasePagination() });
//查询项
const searchParams = ref(generateFormModel());
//加载中
const { loading, setLoading } = useLoading(true);
//表格实例
const tableRef = ref<TableInstance>();
//表格数据
const renderData = ref<any[]>([]);
//设置表格列
const columns = computed<TableColumnData[]>(() => [
  {
    title: "项目名称",
    dataIndex: 'projectName',
    width: 300,
    fixed: "left"
  },
  {
    title: "电站类型",
    dataIndex: 'stationType',
    slotName: 'stationType',
    align: 'center',
    width: 150
  }, {
    title: "备注",
    dataIndex: 'remark',
    width: 270,
    fixed: "left"
  },
  {
    title: "创建时间",
    dataIndex: 'createTime',
    align: 'center',
    width: 180
  },
  {
    title: "启用状态",
    dataIndex: 'stopFlag',
    slotName: "stopFlag",
    fixed: "right",
    align: 'center',
    width: 100
  },
  {
    title: "操作",
    dataIndex: 'operate',
    slotName: 'operate',
    align: "center",
    width: 180,
    fixed: "right"
  }
])


const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {
      stationType: [{ required: true, message: "请选择站点类型" }],
      projectName: [{ required: true, message: "请输入项目名称" }],
    },
    formModel: {
      id: 0,
      projectName: "",
      stationType: props.stationType,
      stopFlag: 1,
      remark: "",
    },
  };
};
//表单模型
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
 * 添加接线图
 */
const handleAdd = async () => {
  formDrawer.value = generateFormDrawerModel();
  formDrawer.value.visible = true;
}

//表格页码发生变化
const onPageChange = (val: number) => {
  pagination.current = val;
  fetchData();
}

//表格每页数量发生变化
const onPageSizeChange = (val: number) => {
  console.log("pageSize", val);
  pagination.pageSize = val;
  fetchData();
}


/**
 * 设计接线图
 * @param id
 */
const handleDesign = async (id: any) => {
  router.push({
    path: `/webtopo/diagram/design/${id}`,
  });
}

/**
 * 编辑项目
 */
const handleEdit = async (id: any) => {
  formDrawer.value = generateFormDrawerModel();
  formDrawer.value.visible = true;
  formDrawer.value.loading = true;
  try {
    let res = await getWebtopoProject(id);
    if (res.code == 200) {
      formDrawer.value.formModel = res.data;
    }
  } catch (err) {

  } finally {
    formDrawer.value.loading = false;
  }
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    let result;
    if (formDrawer.value.formModel.id == 0) {
      result = await addWebtopoProject(formDrawer.value.formModel);
    } else {
      result = await updateWebtopoProject(formDrawer.value.formModel);
    }
    notification(result);
    if (result.code == 200) {
      await fetchData();
      formDrawer.value = generateFormDrawerModel();
    }
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
  operateModalModel.value.name = record.projectName;
  operateModalModel.value.type = 1;
}

/**
 * 取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
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
  let result = await delWebtopoProject(operateModalModel.value.id);
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}
/**
 * 获取数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    let res = await listWebtopoProject({
      ...searchParams.value,
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
    });
    if (res.code == 200) {
      renderData.value = res.rows;
      pagination.total = res.total;
    }
  } catch (err) {
  } finally {
    setLoading(false);
  }
}


/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
})
</script>

<style lang="scss" scoped></style>
