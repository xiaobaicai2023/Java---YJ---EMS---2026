<!--
 * 功能：参数配置
 * 作者：王晓明
 * 日期：2023-11-7
-->
<template>
  <div>
    <a-card class="content">
      <!-- 表单搜索 -->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
            label-align="left" auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="dictName" :label="$t('manage.system.dictionaryName')">
                  <a-input v-model="searchParams.dictName" :placeholder="$t('global.fuzzySearch')" allow-clear />
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
      <!-- 操作条 start-->
      <a-row class="operationBar">
        <a-col :span="12">
          <a-space :size="15">
            <a-button type="primary" @click="addDeviceClick">
              <template #icon>
                <icon-plus />
              </template>
              {{ $t('manage.system.newDictionary') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 操作条 end-->

      <!-- 表格 start-->
      <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
        :columns="tabColumns" :data="renderData" @page-change="onPageChange" @page-size-change="onPageSizeChange">
        <template #stopFlag="{ record }" :scroll="{x:1200}">
          <stop-flag :value="record.stopFlag" />
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" @click="
            handleStopFlagClick(record)
            " :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable')}}
          </a-button>

          <a-button size="small" type="text" @click="editDeviceClick(record)">
            {{ $t('global.edit') }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">
            {{ $t('global.delete') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="() => {
            router.push({
              path: '/manage/system/dictionaryValue', query: {
                dictType: record.dictSn
              }
            });
          }
            ">{{ $t('manage.system.list') }}
          </a-button>
        </template>
      </a-table>
      <!-- 表格 end-->

      <!-- 删除弹框 start -->
      <a-modal  width="450px" v-model:visible="operateModalModel.visible">
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

      <!-- 编辑页 start-->
      <a-drawer :title="drawerTitle" :width="750" :visible="editDrawerVisible" :ok-loading="editLoading" @ok="editDrawerHandleOk"
        @cancel="editDrawerHandleCancel" :mask-closable="false" :closable="false">
        <a-spin style="width:100%;height:100%" :loading="editLoading">
          <a-form auto-label-width ref="formRef" class="form" :model="form" :rules="formRules">
            <a-form-item field="dictName" :label="$t('manage.system.dictionaryName')">
              <a-input v-model="form.dictName" />
            </a-form-item>
            <a-form-item field="dictSn" :label="$t('manage.system.dictionaryParameters')">
              <a-input v-model="form.dictSn" />
              <template #extra>
                  <span>
                    <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill /></i>
                    {{$t('manage.system.keyValue')}}：sys_user_sex
                  </span>
              </template>
            </a-form-item>
            <a-form-item field="stopFlag" :label="$t('global.status')">
              <a-switch :checked-value="0" :unchecked-value="1" v-model="form.stopFlag"><template
                  #checked>{{ $t('global.enable') }}</template><template #unchecked>{{ $t('global.forbidden') }}</template></a-switch>
            </a-form-item>

            <a-form-item field="remark" :label="$t('manage.system.dictionaryExplanation')">
              <a-textarea :placeholder="$t('manage.system.inputExplanation')" :max-length="200" v-model="form.remark" allow-clear />
            </a-form-item>
          </a-form>
        </a-spin>
      </a-drawer>
      <!-- 编辑页 end-->
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from "vue";
import { BasePagination } from "@/api/common";
import {
  listDict,
  addDict,
  updateDict,
  optionselect,
  delDict,
  changeDictStatus,
} from "@/api/manage/system/dictionary";
import useLoading from "@/hooks/loading";
import { FormInstance, TableColumnData, Notification } from "@arco-design/web-vue";
import { notification } from "@/hooks/my-design";
import router from "@/router";

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

//编辑框开关
const editDrawerVisible = ref(false);
// 编辑框loading
const editLoading = ref(false);
//加载中
const { loading, setLoading } = useLoading(true);
// 记录字典下拉项信息
const selectOptions = ref<any>([])
// 记录抽屉的标题名称
const drawerTitle = ref<string>("编辑")
// 获取字典下拉数据
const getOptionList = async () => {
  const { data } = await optionselect()
  selectOptions.value = data
}
//编辑页抽屉开关
const editDeviceClick = (row: any) => {
  getOptionList()
  editDrawerVisible.value = true;
  form.value = row
  drawerTitle.value = "编辑"
};
// 表单校验规则
const formRules = reactive({
  dictName: [
    {
      required: true,
      message: "字典名称必填"
    }
  ],
  dictSn: [
    {
      required: true,
      message: "字典参数必填"
    }],
  remark: [{
    required: true,
    message: "字典说明必填"
  }]
})
//编辑页输入框
const form = ref({
  createBy: "",
  createTime: "",
  deleteFlag: 0,
  dictName: "",
  dictSn: "",
  id: 0,
  params: {},
  remark: "",
  stopFlag: 0,
  updateBy: "",
  updateTime: ""
});


const formRef = ref<FormInstance>();

//编辑确认
const editDrawerHandleOk = async () => {
  const valid = await formRef.value?.validate();
  if (!valid) {
    editLoading.value = true;
    try {
      let res = null;
      if (form.value.id) {
        // 编辑
        res = await updateDict(form.value)
      } else {
        // 新增
        res = await addDict(form.value)
      }
      if (res.code === 200) {
        //调用后端添加接口
        editDrawerVisible.value = false;
        pagination.current = 1;
        fetchData();
      }else{
        Notification.error({
          title: '提示',
          content: res.msg,
        });
      }
    }catch (e) {
      console.error(e);
    }finally {
      editLoading.value = false;
    }
  }
};

//编辑取消
const editDrawerHandleCancel = () => {
  editDrawerVisible.value = false;
  formRef.value?.resetFields();
  fetchData();
};

//添加分组
const addDeviceClick = async () => {
  getOptionList()
  editDrawerVisible.value = true;
  drawerTitle.value = "新增";
  await formRef.value?.resetFields();
};


//生成查询条件
const generateFormModel = () => {
  return {
    dictName: "",
  };
};

//查询项
const searchParams = ref(generateFormModel());

//表格分页参数
const pagination: any = reactive({ ...BasePagination() });

//表格页码发生变化
const onPageChange = (val: number) => {
  pagination.current = val;
  fetchData();
};

//表格每页数量发生变化
const onPageSizeChange = (val: number) => {
  pagination.pageSize = val;
  fetchData();
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
    title: "字典名称",
    dataIndex: "dictName",
    width: 180,
    align: "left",
  },
  {
    title: "字典参数",
    width: 300,
    dataIndex: "dictSn",
    align: "left",
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "字典说明",
    width: 300,
    dataIndex: "remark",
    align: "left",
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
    width: 220,
    slotName: "operate",
    align: "center",
    fixed:"right"
  },
]);

//表格数据
const renderData = ref<any[]>([]);


/**
 * 停用弹框
 */
const handleStopFlagClick = async (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.dictName;
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
  operateModalModel.value.name = record.dictName;
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
  operateModalModel.value.visible = false;
  setLoading(true);
  try{
    if (operateModalModel.value.type == 0) {
      result = await changeDictStatus(operateModalModel.value.id, operateModalModel.value.value);
    } else {
      result = await delDict(operateModalModel.value.id);
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

//重置查询条件
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const { rows, total } = await listDict({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
    } as any);
    renderData.value = rows;
    pagination.total = total;
    pagination.current = pagination.current;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

onMounted(async () => {
  await fetchData()
})
</script>

<style scoped>
.operationBar {
  margin-bottom: 16px;
}

.row-mp-30 {
  margin-top: 30px;
  padding-right: 30px;
}
</style>
