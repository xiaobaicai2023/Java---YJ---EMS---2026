<!--
 * 功能: 参数值配置
 * 作者: 王晓明
 * 日期: 2023-11-11
-->
<template>
  <div>
    <a-card>
      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchParams" label-align="left" auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="dictLabel" :label="$t('manage.system.dictionaryLabel')">
                  <a-input v-model="searchParams.dictLabel" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8" style="text-align: left">
                <a-space :size="15">
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
      <!-- 查询条件 end-->

      <a-divider style="margin-top: 0"/>

      <!-- 操作条 start-->
      <a-row class="operationBar">
        <a-col :span="12">
          <a-space :size="15">
            <a-button type="primary" @click="addDeviceClick">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('manage.system.newDictionary') }}{{ $t('manage.system.value') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 操作条 end-->

      <!-- 表格 start-->
      <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
               :columns="tabColumns" :data="renderData" :scroll="{ x: 1360 }" @page-change="onPageChange"
               @page-size-change="onPageSizeChange">
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"/>
        </template>
        <template #iconClass="{ record }">
          <div v-if="record.iconClass && record.iconClass.length > 0">
            <component :is="record.iconClass"></component>
          </div>
        </template>
        <template #listClass="{ record }">
          <div v-if="record.listClass && record.listClass.length > 0">
            <a-color-picker v-model="record.listClass" disabled/>
          </div>
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" @click="
            handleStopFlagClick(record)
            " :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" @click="editDeviceClick(record.id)">
            {{ $t('global.edit') }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
        </template>
      </a-table>
      <!-- 表格 end-->

      <!-- 对话框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                             style="color: rgb(var(--red-6)); margin-right: 5px"/>
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
          确认提示
        </template>
        <div style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{
            operateModalModel.name
          }}】的数据项？
        </div>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">取消</a-button>
              <a-button type="primary" @click="handleOperateModelOk">确认</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>
      <!-- 对话框 end -->

      <!-- 编辑页 start-->
      <a-drawer :title="eiitDrawerTitle" :width="750" :visible="editDrawerVisible" @ok="editDrawerHandleOk"
                @cancel="editDrawerHandleCancel" :mask-closable="false" :closable="false">
        <div>
          <a-form auto-label-width ref="formRef" class="form" :model="form" :rules="formRules">
            <a-form-item field="dictLabel" label="字典标签">
              <a-input v-model="form.dictLabel"/>
            </a-form-item>
            <a-form-item field="dictValue" label="字典键值">
              <a-input v-model="form.dictValue"/>
            </a-form-item>
            <a-form-item field="orderNum" label="字典排序">
              <a-input-number v-model="form.orderNum" :precision="0" :step="1"/>
            </a-form-item>
            <a-form-item field="iconClass" label="图标样式">
              <select-icon @change="selectIconChange">
                <a-input v-model="form.iconClass" placeholder="点击此处选择图标" allow-clear/>
              </select-icon>
            </a-form-item>
            <!--显示样式-->
            <a-form-item field="listClass"
                         :class="!form.listClass?'color-wrapper':''"
                         label="显示样式">
              <a-color-picker
                  v-model="form.listClass"
                  :historyColors="history"
                  showHistory
                  showPreset
                  @popup-visible-change="addHistory"
              />
              <icon-close-circle class="delete-icon" @click="handleDeleteColor()"/>
            </a-form-item>
            <a-form-item field="stopFlag" label="启用状态">
              <a-switch v-model="form.stopFlag" :checked-value="0" :unchecked-value="1"/>
            </a-form-item>
            <a-form-item field="remark" label="字典说明">
              <a-textarea placeholder="请输入参数解释，最多不超过200字" show-word-limit :max-length="200"
                          v-model="form.remark"
                          allow-clear/>
            </a-form-item>
          </a-form>
        </div>
      </a-drawer>
      <!-- 编辑页 end-->
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, computed, onMounted, nextTick, watchEffect} from "vue";
import {BasePagination} from "@/api/common";
import {
  listDictData, getDictData, addDictData, updateDictData, delDictData, changeDictDataStatus
} from "@/api/manage/system/dictionary-value";
import useLoading from "@/hooks/loading";
import {FormInstance, TableColumnData} from "@arco-design/web-vue";
import {useRouter} from "vue-router";
import selectIcon from "@/components/select-icon/index.vue";
import {notification} from "@/hooks/my-design";
//编辑框开关
const editDrawerVisible = ref(false);
//加载中
const {loading, setLoading} = useLoading(true);
const eiitDrawerTitle = ref("编辑")
const showPicker = ref<boolean>(false)

//删除弹框
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

//删除弹框模型
const operateModalModel = ref(generateOperateModalModel());
const history = ref(['#165DFF']);
// 颜色选择器历史颜色
const addHistory = (visible: boolean, color: string) => {
  if (!visible) {
    const index = history.value.indexOf(color);
    if (index !== -1) {
      history.value.splice(index, 1);
    }
    history.value.unshift(color);
  }
};


//编辑页抽屉开关
const editDeviceClick = async (id: string) => {
  let {data} = await getDictData(id)
  form.value = data
  editDrawerVisible.value = true;
  eiitDrawerTitle.value = "编辑"
};

//编辑页输入框
const form = ref({
  remark: undefined,
  id: 0,
  dictSn: '',
  dictLabel: undefined,
  dictValue: undefined,
  orderNum: undefined,
  iconClass: '',
  listClass: '',
  isDefault: 0,
  isMultiLevel: 0,
  stopFlag: 0,
  deleteFlag: 0
});
const formRef = ref<FormInstance>();
const formRules = reactive({
  dictLabel: [
    {
      required: true,
      message: "字典标签必填"
    }
  ],
  dictValue: [
    {
      required: true,
      message: "字典键值必填"
    }],
  orderNum: [
    {
      required: true,
      message: "字典排序必填"
    }]
})
//编辑确认
const editDrawerHandleOk = async () => {
  const valid = await formRef.value?.validate();
  if (!valid) {
    let res = null
    if (form.value.id) {
      res = await updateDictData({...form.value})
    } else {
      form.value.dictSn = dictType as string
      res = await addDictData({...form.value})
    }
    if (res.code === 200) {
      // 清空主键id
      form.value.id = 0
      //调用后端添加接口
      editDrawerVisible.value = false;
      pagination.current = 1;
      await fetchData();
    }
    notification(res);
  }
};

const handleDeleteColor = async () => {
  form.value.listClass = "";
  await nextTick();
  if (form.value.listClass == "#FF0000" || form.value.listClass == "rgb(255, 0, 0)") {
    form.value.listClass = "";
  }
};


const search = async () => {
  pagination.current = 1;
  await fetchData();
}


/**
 * 选择图标
 * @param iconName 图标名称
 */
const selectIconChange = (iconName: string) => {
  form.value.iconClass = iconName;
};

const handleRemoveColor = () => {
  form.value.listClass = '';
};

//编辑取消
const editDrawerHandleCancel = () => {
  // 清空主键id
  form.value.id = 0
  editDrawerVisible.value = false;

};

//添加分组
const addDeviceClick = () => {
  editDrawerVisible.value = true;
  eiitDrawerTitle.value = "新增"
  formRef.value?.resetFields()
};


/**
 * 停用弹框
 */
const handleStopFlagClick = async (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.dictLabel;
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
  operateModalModel.value.name = record.dictLabel;
  operateModalModel.value.type = 1;
}

/**
 * 操作提示弹框取消
 */
const handleOperateModelCancle = () => {
  operateModalModel.value = generateOperateModalModel();
}

/**
 * 删除提示弹框确认
 */
const handleOperateModelOk = async () => {

  let result;
  if (operateModalModel.value.type == 0) {
    result = await changeDictDataStatus(operateModalModel.value.id, operateModalModel.value.value);
  } else {
    result = await delDictData(operateModalModel.value.id);
  }
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}

//生成查询条件
const generateFormModel = () => {
  return {
    dictLabel: "",
  };
};

//查询项
const searchParams = ref(generateFormModel());


//表格分页配置
const pagination: any = reactive({...BasePagination()});

//表格页码发生变化
const onPageChange = async (val: number) => {
  pagination.current = val;
  await fetchData();
};

//表格每页数量发生变化
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchData();
};

//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    width: 80,
    align: "center",
  },
  {
    title: "字典标签",
    dataIndex: "dictLabel",
    width: 240,
    align: "left",
  },
  {
    title: "字典键值",
    width: 200,
    dataIndex: "dictValue",
    align: "left",
  },
  {
    title: "图标样式",
    width: 120,
    dataIndex: "iconClass",
    slotName: "iconClass",
    align: "center",
  },
  {
    title: "显示样式",
    width: 120,
    dataIndex: "listClass",
    slotName: "listClass",
    align: "center",
  },
  {
    title: "字典排序",
    width: 80,
    dataIndex: "orderNum",
    align: "center",
  },
  {
    title: "字典说明",
    width: 200,
    dataIndex: "remark",
    align: "left",
  },
  {
    title: "启用状态",
    width: 100,
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: "center",
    fixed: "right"
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 180,
    slotName: "operate",
    align: "center",
    fixed: "right"
  },
]);

//表格数据
const renderData = ref<any[]>([]);
const router = useRouter()
let {dictType} = router.currentRoute.value.query
//查询数据
const fetchData = async () => {
  setLoading(true);
  if (!dictType) {
    setLoading(false);
    return false
  }
  try {
    const {rows, total} = await listDictData({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
      dictSn: dictType,
    });
    renderData.value = rows;
    pagination.total = total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};
onMounted(async () => {
  await fetchData();
})
</script>

<style lang="less"  scoped>
.operationBar {
  margin-bottom: 16px;
}

.row-mp-30 {
  margin-top: 30px;
  padding-right: 30px;
}

.color-wrapper{
  :deep(.arco-color-picker-preview) {
    background: none !important;
  }
}

</style>
