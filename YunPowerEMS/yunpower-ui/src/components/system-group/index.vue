<!--
 * 功能：通用分组模板 通过mapId 来区分
 * 作者：曹晓桐
 * 日期：2023-11-7
-->
<template>
  <div>
    <a-card class="content">
      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left"
                  auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="categoryName" :label="$t('power.energy.categoryName')">
                  <a-input v-model="searchModel.groupName" :placeholder="$t('global.fuzzySearch')" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="logicCode" :label="$t('power.energy.logicalCode')">
                  <a-input v-model="searchModel.logicCode" :placeholder="$t('global.fuzzySearch')" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item :label="$t('power.energy.classificationStatus')">
                  <a-select v-model="searchModel.stopFlag" :placeholder="$t('global.pleaseSelect')" allow-clear>
                    <a-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
                              :value="dict.value"/>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 35px" direction="vertical"/>
        <a-col :flex="'86px'" style="text-align: right">
          <a-space :size="18">
            <a-button type="primary" @click="search">
              <template #icon>
                <icon-search/>
              </template>
              {{ $t('global.search') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

      <!-- 分割线 -->
      <a-divider style="margin-top: 0"/>

      <!-- 按钮 -->
      <a-row style="margin-bottom: 16px" v-if="stationType!=2">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('global.new') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table row-key="id" ref="tableRef" :loading="loading" :pagination="false"
               :bordered="{ wrapper: true, cell: true }" :columns="columns" :data="renderData">
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"></stop-flag>
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" @click="handleStopFlag(record)"
                    :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button v-if="record.isSystem == 0 && stationType!=2" size="small" type="text" status="success" @click="handleUpdate(record)">
            {{ $t('global.edit') }}
          </a-button>
          <a-button v-if="record.isSystem == 0 && stationType!=2" size="small" type="text" status="danger" @click="handleDelete(record)">
            {{ $t('global.delete') }}
          </a-button>
        </template>
      </a-table>

      <!-- 操作弹框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible" class="modal-box">
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

      <!-- 添加右弹层 start -->
      <a-drawer :width="750" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm"
                @cancel="handleFormCancel"
                :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? $t('power.energy.device.editGroup') : $t('power.energy.device.addGroup') }}
        </template>
        <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
          <a-row :gutter="24" class="row-mp-30">
            <a-col :span="24">
              <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                      :rules="formDrawer.rules">

                <a-form-item field="groupName" :label="$t('global.groupName')">
                  <a-input :placeholder="$t('menu.diagram.project.power.list.projectNamePlaceholder')"
                           v-model="formDrawer.formModel.groupName"/>
                </a-form-item>

                <a-form-item field="parentId" :label="$t('global.parentDirectory')">
                  <a-tree-select allow-search v-model="formDrawer.formModel.parentId" :fieldNames="{
                  key: 'id', title: 'groupName', children: 'children'
                }" :data="parentList" placeholder="根目录"></a-tree-select>
                </a-form-item>

                <!-- 显示顺序  -->
                <a-form-item field="orderNum" :label="$t('global.displayOrder')">
                  <a-input-number v-model="formDrawer.formModel.orderNum"/>
                </a-form-item>

                <!-- 启用状态 -->
                <a-form-item field="stopFlag" :label="$t('global.status')">
                  <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                            v-model="formDrawer.formModel.stopFlag">
                    <template #checked>{{ $t('global.enable') }}</template>
                    <template
                        #unchecked>{{ $t('global.forbidden') }}
                    </template>
                  </a-switch>
                </a-form-item>
                <a-form-item field="remark" :label="$t('pv.device.remark')">
                  <a-textarea :placeholder="$t('pv.device.placeholder')" :max-length="200"
                              v-model="formDrawer.formModel.remark"
                              allow-clear/>
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
import {computed, getCurrentInstance, onMounted, reactive, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {TableInstance, TreeNodeData} from "@arco-design/web-vue";
import {notification} from "@/hooks/my-design";
import {BasePagination} from '@/api/common';
import {FormInstance} from '@arco-design/web-vue/es/form';
import {getGroup, addGroup, updateGroup, delGroup, listGroupAll} from "@/api/system/group";
import {dictDataMultiLevel} from '@/api/public';
import {StationTypeEnum} from "@/api/system/device";

/*************************** 变量区域 begin ***************************/

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {sys_normal_disable} = proxy?.useDict("sys_normal_disable");
//******* 这里编写动态获取下拉列表 end ******

//接受组件参数
const props = defineProps({
  mapId: {
    type: Number,
    default: '',
  },
  stationType: {
    type: Number,
    default: StationTypeEnum.power,
  }
})
// console.log("接受组件参数", props.mapId);
//生成查询条件对象
const generateSearchModel = () => {
  return {
    //分类名称
    groupName: "",
    //逻辑代码
    logicCode: "",
    //状态
    stopFlag: '',
    //
    mapId: props.mapId
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const {loading, setLoading} = useLoading(true);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
//表格实例
const tableRef = ref<TableInstance>();
//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    align: 'center',
    width: 100
  },
  {
    title: "分组名称",
    dataIndex: "groupName"
  },
  {
    title: "逻辑代码",
    dataIndex: "logicCode"
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
      groupName: [{required: true, message: "请输入分组名称"}],
      parentId: [{required: true, message: "请选择上级目录"}],
      orderNum: [{required: true, message: "请输入顺序"}],
    },
    formModel: {
      id: 0,
      groupName: '',
      parentId: 0,
      orderNum: 0,
      stopFlag: 0,
      mapId: props.mapId,
      remark: ""
    }
  };
};
//表单模型
const formDrawer = ref(generateFormDrawerModel());
//switch选中值
const checkedValue = ref<number>(0);
//switch未选中值
const unCheckedValue = ref<number>(1);
//默认上级目录
const defaultParentList = ref<any>(
    [
      {
        id: 0,
        groupName: "根目录"
      }
    ]
);
//上级目录
const parentList = ref<any>();

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

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
  //获取是否有多级
  let res: any = await dictDataMultiLevel("sys_group_map", props.mapId);
  if (res.code == 200) {
    if (res.data == 1) {
      parentList.value = defaultParentList.value.concat(renderData.value);
    } else {
      parentList.value = defaultParentList.value;
    }
  }
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
  let result: any = await getGroup(record.id);
  if (result.code == 200) {
    formDrawer.value.formModel = result.data;
    // formDrawer.value.formModel.parentId = formDrawer.value.formModel.parentId ? formDrawer.value.formModel.parentId : undefined;
    formDrawer.value.visible = true;
    handleAdd();
  }
}

const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.groupName;
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
  operateModalModel.value.name = record.groupName;
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
  if (operateModalModel.value.type == 0) {
    let info: any = await getGroup(operateModalModel.value.id);
    if (info.code == 200) {
      result = await updateGroup({...info.data, stopFlag: operateModalModel.value.value});
    } else {
      result = info;
    }
  } else {
    result = await delGroup(operateModalModel.value.id);
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
    try {
      formDrawer.value.loading = true;
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addGroup(formDrawer.value.formModel);
      } else {
        result = await updateGroup(formDrawer.value.formModel);
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
  formDrawer.value = generateFormDrawerModel();
  formRef.value?.resetFields();
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listGroupAll({
      ...searchModel.value,
    });
    removeEmptyChildren(res.data);
    renderData.value = res.data;
    tableRef.value?.expandAll(true);
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
  tableRef.value?.expandAll(true);
})
</script>

<style scoped></style>
