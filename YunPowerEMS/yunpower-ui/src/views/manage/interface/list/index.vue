<!--
* 功能：数据接口
* 作者：张怡静
* 日期：2023-10-20
-->
<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :flex="1">
        <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left"
          auto-label-width>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item field="categoryName" :label="$t('manage.interface.list.interfaceName')">
                <a-input v-model="searchModel.interfaceName" :placeholder="$t('manage.interface.list.interfaceNamePlaceholder')" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item field="logicCode" :label="$t('manage.interface.list.interfaceMethods')">
                <a-input v-model="searchModel.interfaceMethod" :placeholder="$t('manage.interface.list.methodPlaceholder')" allow-clear />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-divider style="height: 35px" direction="vertical" />
      <a-col :flex="'86px'" style="text-align: right">
        <a-space :size="18">
          <a-button type="primary" @click="fetchData">
            <template #icon>
              <icon-search />
            </template>
            {{$t('global.search')}}
          </a-button>
        </a-space>
      </a-col>
    </a-row>
    <!-- 查询条件 end-->
    <a-divider style="margin-top: 0" />

    <!-- 操作条 start-->
    <a-row style="margin-bottom: 16px;">
      <a-col :span="12">
        <a-button type="primary" @click="handleAdd">
          <template #icon>
            <icon-plus />
          </template>
          {{$t('manage.interface.list.createInterface')}}
        </a-button>
      </a-col>
    </a-row>
    <!-- 操作条 end-->

    <!-- 表格 start-->
    <a-table row-key="id" ref="tableRef" :loading="loading" :bordered="{ wrapper: true, cell: true }"
      :pagination="pagination" :columns="(tabColumns as TableColumnData[])" :data="renderData" @page-change="onPageChange"
      :scroll="{ x: 1420 }" @page-size-change="onPageSizeChange" show-page-size>
      <template #requestType="{ record }">
        <dict-tag :options="sys_request_type" :value="record.requestType" />
      </template>
      <template #stopFlag="{ record }">
        {{ record.stopFlag == 0 ? $t('global.enable') : $t('global.forbidden') }}
      </template>
      <template #operate="{ record }">
        <a-button size="small" type="text" @click="handleStop(record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable'), record.id,record.stopFlag == 0 ? 1 : 0)"
          :stopFlag="record.stopFlag == 0 ? 'normal' : 'warning'">
          {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
        </a-button>
        <a-button size="small" type="text" status="success" @click="handleUpdate(record)">{{$t('global.edit')}}</a-button>
        <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{$t('global.delete')}}</a-button>
      </template>
    </a-table>
    <!-- 表格 end-->

    <!-- 编辑抽屉 start -->
    <a-drawer :width="750" :visible="formDrawer.visible" @ok="handleSubmitForm" :ok-loading="formDrawer.loading" @cancel="handleFormCancel"
      :mask-closable="false">
      <template #title>
        {{ formDrawer.formModel.id > 0 ? $t('manage.interface.list.editInterface') : $t('manage.interface.list.createInterface') }}
      </template>
      <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
        <a-row :gutter="24" class="row-mp-30">
          <a-col :span="24">
            <a-form ref="formRef" :model="formDrawer.formModel" class="form" label-align="right" :rules="formDrawer.rules" auto-label-width>
              <a-form-item field="interfaceName" :label="$t('manage.interface.list.interfaceName')">

                <a-input v-model="formDrawer.formModel.interfaceName" :max-length="20" allow-clear show-word-limit
                         :placeholder="$t('manage.interface.list.interfacePlaceholder')" />
              </a-form-item>

              <a-form-item field="interfaceMethod" :label="$t('manage.interface.list.interfaceMethods')">
                <a-input v-model="formDrawer.formModel.interfaceMethod" :placeholder="$t('manage.interface.list.methodPlaceholder')" />
              </a-form-item>

              <a-form-item field="requestPath" :label="$t('manage.interface.list.requestPath')">
                <a-input v-model="formDrawer.formModel.requestPath" :placeholder="$t('manage.interface.list.pathPlaceholder')" />
              </a-form-item>

              <!-- 请求方式 -->
              <a-form-item field="requestType" :label="$t('manage.interface.list.requestMode')" :validate-trigger="['change', 'blur', 'input']">
                <a-select v-model="formDrawer.formModel.requestType" :placeholder="$t('manage.interface.list.requestModeSelect')" allow-clear>
                  <a-option v-for="dict in sys_request_type" :key="dict.value" :label="dict.label"
                            :value="dict.value"></a-option>
                </a-select>
              </a-form-item>

              <a-form-item field="remark" :label="$t('manage.interface.list.description')" row-class="keep-margin">
                <a-textarea v-model="formDrawer.formModel.remark" auto-size :max-length="200" allow-clear show-word-limit
                            :placeholder="$t('manage.interface.list.descriptionPlaceholder')" />
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      </a-spin>
    </a-drawer>
    <!-- 编辑抽屉 end -->

    <!-- 删除对话框 start -->
    <a-modal width="400px" v-model:visible="delModalModel.visible" class="modal-box">
      <template #title>
        <icon-close-circle size="18" style="color: rgb(var(--red-6)); margin-right: 5px" />
        {{$t('global.confirmTip')}}
      </template>
      <div style="text-align: center">
        你确定要【删除】当前记录吗？
      </div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleDelModelCancle">{{$t('global.cancel')}}</a-button>
            <a-button type="primary" @click="handleDelModelOk">{{$t('global.confirm')}}</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
    <!-- 删除对话框 end -->

    <!-- 启用/停用对话框 start -->
    <a-modal width="400px" v-model:visible="StopModalModel.visible" class="modal-box">
      <template #title>
        <icon-exclamation-circle size="18" style="color:rgb(var(--orange-6)); margin-right: 5px;" />
        {{$t('global.confirmTip')}}
      </template>
      <div style="text-align: center;">你确定要【{{ StopModalModel.title }}】当前记录吗？</div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleStopModelCancle">{{$t('global.cancel')}}</a-button>
            <a-button type="primary" @click="handleStopModelOk">{{$t('global.confirm')}}</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
    <!-- 启用/停用对话框 end -->
  </a-card>
</template>

<script lang="ts" setup>
import { computed, ref, onMounted, getCurrentInstance, reactive } from "vue";
import { BasePagination } from "@/api/common";
import { listInterface, getInterface, addInterface, updateInterface, delInterface } from "@/api/manage/interface/list";
import { TableColumnData } from "@arco-design/web-vue";
import { notification } from "@/hooks/my-design";
import useLoading from "@/hooks/loading";
import { FormInstance } from '@arco-design/web-vue/es/form';
/*************************** 变量区域 begin ***************************/
//生成查询条件对象
const generateSearchModel = () => {
  return {
    interfaceName: "",
    interfaceMethod: ""
  };
};

//******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_request_type } = proxy?.useDict("sys_request_type");
//******* 这里写动态获取下拉列表 end ******

//查询表单对象
const searchModel = ref(generateSearchModel());

//加载中
const { loading, setLoading } = useLoading(true);




//表格分页参数
const pagination: any = reactive({ ...BasePagination()});

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
    title: "接口名称",
    dataIndex: "interfaceName",
    width: 240,
    fixed: 'left'
  },
  {
    title: "接口方法",
    dataIndex: "interfaceMethod",
    width: 240
  },
  {
    title: "请求路径",
    dataIndex: "requestPath",
    width: 240,
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "请求方式",
    dataIndex: "requestType",
    slotName: "requestType",
    width: 100,
    align: 'center'
  },
  {
    title: "调用次数",
    dataIndex: "callCount",
    width: 100,
    align: 'center'
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
    width: 160,
    slotName: 'operate',
    align: 'center',
    fixed: 'right'
  },
]);


//表格数据
const renderData = ref<any[]>([]);

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
    id: 0,
    value: 0
  };
};
//启用弹框模型
const StopModalModel = ref(generateStopModalModel());

//编辑表单实例
const formRef = ref<FormInstance>();

//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    rules: {
      interfaceName: [{ required: true, message: '请输入接口名称' }],
      interfaceMethod: [{ required: true, message: '请输入接口方法' }],
      requestPath: [{ required: true, message: '请输入请求路径' }],
      requestType: [{ required: true, message: '请选择请求方式' }],
      remark: [{ maxLength: 200, message: '最多不超过200字' }]
    },
    formModel: {
      //编号
      id: 0,
      //接口名称
      interfaceName: "",
      //接口方法
      interfaceMethod: "",
      //请求路径
      requestPath: "",
      //请求方式
      requestType: "",
      //调用次数
      callCount: 0,
      //状态
      stopFlag: 0,
      //参数说明
      remark: "",
    }
  };
};

const formTitle = ref<string>("新建菜单");
//表单模型
const formDrawer = ref(generateFormDrawerModel());

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

/**
 * 搜索
 */
const search = () => {
  pagination.current = 1 ;

  fetchData();
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
  //重置表单
  formDrawer.value = generateFormDrawerModel();
  //显示弹框
  formDrawer.value.visible = true;
}

/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  let result = await getInterface(record.id);
  if (result.code == 200) {
    result.data.requestType += "";
    formDrawer.value.formModel = result.data;
    formDrawer.value.visible = true;
  }
}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  delModalModel.value.visible = true;
  delModalModel.value.id = record.id;
    delModalModel.value.title = record.postName;
}

/**
 * 删除提示弹框取消
 */
const handleDelModelCancle = () => {
  delModalModel.value = generateDelModalModel();
}

/**
 * 删除提示弹框确认
 */
const handleDelModelOk = async () => {
  delModalModel.value.visible = false;
  setLoading(true);
  try{
    let result = await delInterface(delModalModel.value.id);
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
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    formDrawer.value.loading = true;
    try {
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addInterface(formDrawer.value.formModel);
      } else {
        result = await updateInterface(formDrawer.value.formModel);
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
}

/**
 * 启用数据
 * @param record 数据行
 */
const handleStop = (title: string, bizId: number, value: any) => {
  StopModalModel.value.visible = true;
  StopModalModel.value.id = bizId,
    StopModalModel.value.title = title;
  StopModalModel.value.value = value;

}

/**
 * 启用提示弹框取消
 */
const handleStopModelCancle = () => {
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
  update.id = StopModalModel.value.id;
  update.stopFlag = StopModalModel.value.value
  updateInterface(update).then((res: any) => {
    notification(res);
    if (res.code == 200) {
      StopModalModel.value.visible = false;
      fetchData();
    }
  });
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listInterface({
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
<style lang="less" scoped>
.row-mp-30 {
  margin-top: 30px;
  padding-right: 30px;
}
</style>
