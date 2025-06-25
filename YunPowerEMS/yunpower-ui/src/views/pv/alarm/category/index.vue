<!--
 * 功能：报警类型
 * 作者：曹晓桐
 * 日期：2023-11-7
-->
<template>
  <div>
    <a-card class="content">
      <!-- 操作条 start-->
      <a-row style="margin-bottom: 16px;">
        <a-col :span="24">
          <a-button type="primary" @click="handleAdd">
            <template #icon>
              <icon-plus />
            </template>
            {{ $t('global.new') }}
          </a-button>
        </a-col>
      </a-row>
      <!-- 操作条 end-->

      <!-- 表格内容 -->
      <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
        :columns="columns" :data="renderData" @page-change="onPageChange"
        @page-size-change="onPageSizeChange" show-page-size>
        <!-- 报警级别 -->
        <template #triggerLevel="{ record }">
          <dict-tag :options="sys_alaram_level" :value="record.triggerLevel" />
        </template>
        <!-- 触发类型 -->
        <template #triggerType="{ record }">
          <dict-tag :options="sys_trigger_type" :value="record.triggerType" />
        </template>
        <!-- 触发类型 -->
        <template #suitType="{ record }">
          <a-tag v-if="record.suitType == 1" color="rgb(var(--primary-6))">模拟量</a-tag>
          <a-tag v-else color="rgb(var(--primary-6))">状态量</a-tag>
        </template>
        <!-- 报警方式 -->
        <template #alarmMethod="{ record }">
          <a-space>
            <template v-for="(item, index) in record.alarmMethod" :key="index">
              <a-tag v-if="item.value == 1" color="rgb(var(--primary-6))">{{ item.name }}</a-tag>
            </template>
          </a-space>
        </template>
        <!-- 状态 -->
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag" />
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
      <a-modal width="auto" v-model:visible="operateModalModel.visible" class="modal-box">
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
          {{ formDrawer.formModel.id ? $t('pv.alarm.category.editAlarmType') : $t('pv.alarm.category.addAlarmType') }}
        </template>
       <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
         <a-row :gutter="24" class="row-mp-30">
           <a-col :span="24">
             <a-form ref="formRef" :model="formDrawer.formModel" auto-label-width label-align="right"
                     :rules="formDrawer.rules">

               <!-- 类型名称 -->
               <a-form-item field="triggerName" :label="$t('pv.alarm.category.typeName')" :validate-trigger="['change', 'blur', 'input']">
                 <a-input v-model="formDrawer.formModel.triggerName" allow-clear />
               </a-form-item>

               <!-- 类型编码 -->
               <a-form-item field="triggerSn" :label="$t('pv.alarm.category.typeCode')">
                 <a-input v-model="formDrawer.formModel.triggerSn" disabled />
               </a-form-item>

               <!-- 报警级别  -->
               <a-form-item field="triggerLevel" :label="$t('pv.alarm.category.alarmLevel')">
                 <a-select v-model="formDrawer.formModel.triggerLevel" :placeholder="$t('pv.alarm.category.electAlarmLevel')" allow-clear>
                   <a-option v-for="dict in sys_alaram_level" :key="dict.value" :label="dict.label"
                             :value="dict.value"></a-option>
                 </a-select>
               </a-form-item>

               <!-- 触发类型 -->
               <a-form-item field="triggerType" :label="$t('pv.alarm.category.triggerType')">
                 <a-select v-model="formDrawer.formModel.triggerType" :placeholder="$t('pv.alarm.category.electTriggerType')" allow-clear>
                   <a-option v-for="dict in sys_trigger_type" :key="dict.id" :label="dict.label"
                             :value="dict.value"></a-option>
                 </a-select>
               </a-form-item>

               <!-- 适用类型 -->
               <a-form-item field="suitType" :label="$t('pv.alarm.category.suitableType')">
                 <a-radio-group v-model="formDrawer.formModel.suitType">
                   <a-radio value="1">{{ $t('pv.alarm.category.analog') }}</a-radio>
                   <a-radio value="2">{{ $t('pv.alarm.category.state') }}</a-radio>
                 </a-radio-group>
               </a-form-item>

               <!-- 启用状态 -->
               <a-form-item field="stopFlag" :label="$t('global.status')">
                 <a-switch :checked-value="0" :unchecked-value="1" v-model="formDrawer.formModel.stopFlag"></a-switch>
               </a-form-item>

               <!-- 报警提示 -->
               <a-form-item field="isAlarm" :label="$t('pv.alarm.category.alarmPrompt')">
                 <a-switch :checked-value="1" :unchecked-value="0" v-model="formDrawer.formModel.isAlarm"></a-switch>
               </a-form-item>

               <!-- 报警方式 -->
               <a-form-item field="alarmMethodIds" :label="$t('pv.alarm.category.alarmMethod')">
                 <a-checkbox-group v-model="formDrawer.formModel.alarmMethodIds" direction="vertical">
                   <a-checkbox v-for="(item, index) in formDrawer.alarmMethodList" :key="index" :value="item.name">{{
                       item.name }}</a-checkbox>
                 </a-checkbox-group>
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
import { listTriggerCategory, getTriggerCategory, addTriggerCategory, updateTriggerCategory, delTriggerCategory, changeTriggerCategoryStatus } from "@/api/system/trigger-category";
import { BasePagination, Pagination } from '@/api/common';
import { FormInstance } from '@arco-design/web-vue/es/form';
import { StationTypeEnum } from '@/api/system/device';

/*************************** 变量区域 begin ***************************/
//接受组件参数
const props = defineProps({
  stationType: {
    type: Number,
    default: StationTypeEnum.power,
  },
})
//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_trigger_type,
  sys_alaram_level,
  sys_yes_no,
  sys_normal_disable,
} = proxy?.useDict(
  "sys_trigger_type",
  "sys_alaram_level",
  "sys_yes_no",
  "sys_normal_disable",
);
//******* 这里编写动态获取下拉列表 end ******


//加载中
const { loading, setLoading } = useLoading(true);
//基础分页
const basePagination = BasePagination();

//表格分页参数
const pagination = Pagination;

//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "报警类型名称",
    dataIndex: "triggerName",
    width: 200,
    fixed: "left",
  },
  {
    title: "类型代码",
    dataIndex: "triggerSn",
    width: 150,
  },
  {
    title: "报警级别",
    dataIndex: "triggerLevel",
    slotName: "triggerLevel",
    width: 120,
    align: 'left',
  },
  {
    title: "触发类型",
    dataIndex: "triggerType",
    slotName: "triggerType",
    width: 100,
    align: 'left',
  },
  {
    title: "适用类型",
    dataIndex: "suitType",
    slotName: "suitType",
    width: 100,
    align: 'center',
  },
  {
    title: "报警方式",
    dataIndex: "alarmMethod",
    slotName: "alarmMethod",
    width: 140,
    align: 'left',
  },
  {
    title: "状态",
    slotName: "stopFlag",
    width: 100,
    align: 'center',
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 165,
    slotName: 'operate',
    align: 'center',
    fixed:'right'
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
    alarmMethodList: [{
      name: "窗口",
      value: "0",
    }, {
      name: "微信",
      value: "0",
    }, {
      name: "短信",
      value: "0",
    }],
    rules: {
      triggerName: [{ required: true, message: "请输入类型名称" }],
      triggerLevel: [{ required: true, message: "请选择报警级别" }],
      triggerType: [{ required: true, message: "请选择触发类型" }],
      alarmMethodIds: [{ required: true, message: "请选择报警方式" }]
    },
    formModel: {
      id: 0,
      entId: 0,
      deptId: 0,
      triggerName: '',
      triggerSn: '',
      triggerLevel: '',
      triggerType: '',
      suitType: '1',
      isAlarm: 1,
      alarmMethod: '',
      remark: '',
      createBy: '',
      createTime: '',
      updateBy: '',
      updateTime: '',
      stopFlag: 0,
      deleteFlag: 0,
      alarmMethodIds: [],
    }
  };
};

//表单模型
const formDrawer = ref(generateFormDrawerModel());

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

//重置查询条件
const search = () => {
  pagination.current = basePagination.current;
  pagination.pageSize = basePagination.pageSize;
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
  formDrawer.value.visible = true;
}

/**
 * 停用
 * @param record
 */
const handleStopFlag = (record: any) => {
  operateModalModel.value = {
    visible: true,
    type: 0,
    title: record.stopFlag == 0 ? "停用" : "启用",
    id: record.id,
    value: record.stopFlag == 0 ? 1 : 0,
    name: record.triggerName
  }
}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  operateModalModel.value = {
    visible: true,
    type: 1,
    title: "删除",
    id: record.id,
    name: record.triggerName,
    value: record.id
  }
}

/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  let result = await getTriggerCategory(record.id);
  if (result.code == 200) {
    result.data.triggerLevel+="";
    result.data.suitType+="";

    let arr :any = [];
    result.data.alarmMethod.forEach(item => {
      if (item.value == 1) {
        arr.push(item.name)
      }
    })
    result.data.alarmMethodIds = arr;
    formDrawer.value.formModel = result.data;
    formDrawer.value.visible = true;
  }
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
  if (operateModalModel.value.type == 0) {
    result = await changeTriggerCategoryStatus(operateModalModel.value.id, operateModalModel.value.value);
  } else {
    result = await delTriggerCategory(operateModalModel.value.id);
  }
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}


/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();

  if (!validate) {
    formDrawer.value.loading = true;
    let alarmMethodIds: any = formDrawer.value.formModel.alarmMethodIds
    let alarmMethodList: any = [];
    formDrawer.value.alarmMethodList.forEach(item => {
      if (alarmMethodIds.indexOf(item.name) > -1) {
        item.value = "1";
      }
      alarmMethodList.push(item);
    })

    try{
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addTriggerCategory({ ...formDrawer.value.formModel, alarmMethod: alarmMethodList });
      } else {
        result = await updateTriggerCategory({ ...formDrawer.value.formModel, alarmMethod: alarmMethodList });
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
    const res: any = await listTriggerCategory({
      pageSize: pagination.pageSize,
      pageNum: pagination.current
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
