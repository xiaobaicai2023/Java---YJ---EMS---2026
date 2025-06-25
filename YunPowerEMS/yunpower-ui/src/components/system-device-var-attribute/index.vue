<template>
  <div>
    <a-card class="content">
      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left"
                  auto-label-width>
            <a-row :gutter="24">
              <a-col :span="8">
                <a-form-item field="menu" :label="$t('power.energy.device.attrName')">
                  <a-input v-model="searchParams.indexName" :max-length="20" :placeholder="$t('global.please')"
                           allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="menu" :label="$t('power.energy.device.attrCode')">
                  <a-input v-model="searchParams.indexSn" :placeholder="$t('global.please')" :max-length="64"
                           allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="menu" :label="$t('power.energy.device.attrType')">
                  <a-select v-model="searchParams.varType" :placeholder="$t('global.pleaseSelect')" allow-clear>
                    <a-option v-for="dict in sys_variable_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"></a-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-col :span="6" style="text-align: left">
          <a-space :size="18">
            <a-divider style="height: 35px" direction="vertical"/>
            <a-button type="primary" @click="fetchData">
              <template #icon>
                <icon-search/>
              </template>
              {{ $t('global.search') }}
            </a-button>
            <a-button type="primary" @click="handleReset">
              <template #icon>
                <icon-refresh/>
              </template>
              {{ $t('global.reset') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

      <a-divider style="margin-top: 0"/>


      <!-- 按钮 -->
      <a-row style="margin-bottom: 20px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleDeviceVarAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{$t('power.energy.device.attrAdd')}}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <a-table row-key="id" ref="tableRef" v-bind:hide-expand-button-on-empty="true" :loading="loading"
               :pagination="false"
               :bordered="{ wrapper: true, cell: true }" :columns="(tabColumns as TableColumnData[])" :data="renderData"
               :default-expand-all-rows="true" show-empty-tree>
        <!--变量类型-->
        <template #varType="{ record }">
          <template v-for="item in sys_variable_type" :key="item.value">
            <span v-if="item.value == record.varType">{{ item.label }}</span>
          </template>
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" @click="handleStopFlag(record)"
                    :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" @click="handleDeviceVarUpdate(record)">{{
              $t('global.edit')
            }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{
              $t('global.delete')
            }}
          </a-button>
        </template>
      </a-table>

      <!-- 添加右弹层 start -->
      <a-drawer :width="750" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm"
                @cancel="handleFormCancel"
                :mask-closable="false">
        <template #title>
          {{ formDrawer.id ? $t('power.energy.device.editGroup') : $t('power.energy.device.addGroup') }}
        </template>
        <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
          <a-row :gutter="24" class="row-mp-30">
            <a-col :span="24">
              <a-form ref="formRef" auto-label-width :model="formDrawer" label-align="right"
                      :rules="rules">
                <!-- 上级目录-->
                <a-form-item field="parentId" :label="$t('global.parentDirectory')">
                  <a-tree-select allow-search v-model="formDrawer.parentId" :data="parentList"
                                 :placeholder="$t('manage.account.menu.rootDirectory')"
                                 @change="handleTreeChange"
                                 :filter-tree-node="filterTreeNode"></a-tree-select>
                </a-form-item>

                <!-- 属性名称-->
                <a-form-item field="indexName" :label="$t('power.energy.device.attrName')"
                             :validate-trigger="['change', 'input']">
                  <a-input :placeholder="$t('menu.diagram.project.power.list.projectNamePlaceholder')"
                           :max-length="20"
                           v-model="formDrawer.indexName"/>
                </a-form-item>

                <!-- 属性编码-->
                <a-form-item field="indexSn" :label="$t('power.energy.device.attrCode')">
                  <a-input :placeholder="$t('global.please')"
                           :max-length="64"
                           v-model="formDrawer.indexSn"/>
                </a-form-item>

                <!-- 属性单位-->
                <a-form-item field="unit" :label="$t('power.energy.device.attrUnit')">
                  <a-input :placeholder="$t('global.please')"
                           :max-length="32"
                           v-model="formDrawer.unit"/>
                </a-form-item>

                <!-- 适用类型-->
                <a-form-item field="varType" :label="$t('power.energy.device.attrType')">
                  <a-radio-group v-model="formDrawer.varType">
                    <template v-if="formDrawer.parentId==0">
                      <a-radio v-for="item in sys_variable_type" :key="item.value" :value="Number(item.value)"
                               v-show="item.value!='0'">
                        {{ item.label }}
                      </a-radio>
                    </template>
                    <template v-else>
                      <a-radio v-for="item in sys_variable_type" :key="item.value" :value="Number(item.value)">
                        {{ item.label }}
                      </a-radio>
                    </template>
                  </a-radio-group>
                </a-form-item>

                <!-- 显示顺序  -->
                <a-form-item field="orderNum" :label="$t('global.displayOrder')">
                  <a-input-number v-model="formDrawer.orderNum"/>
                </a-form-item>

                <!-- 启用状态 -->
                <a-form-item field="stopFlag" :label="$t('global.status')">
                  <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                            v-model="formDrawer.stopFlag">
                    <template #checked>{{ $t('global.enable') }}</template>
                    <template
                        #unchecked>{{ $t('global.forbidden') }}
                    </template>
                  </a-switch>
                </a-form-item>
                <a-form-item field="remark" :label="$t('pv.device.remark')">
                  <a-textarea :placeholder="$t('pv.device.placeholder')" :max-length="200"
                              v-model="formDrawer.remark"
                              allow-clear/>
                </a-form-item>

              </a-form>
            </a-col>
          </a-row>
        </a-spin>
      </a-drawer>

      <!-- 操作弹框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                             style="color: rgb(var(--red-6)); margin-right: 5px"/>
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
          确认提示
        </template>
        <div v-if="operateModalModel.type < 2" style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{
            operateModalModel.name
          }}】的数据项？
        </div>
        <div v-if="operateModalModel.type == 2" style="text-align: center;">是否确认为【{{ operateModalModel.name }}】数据{{
            operateModalModel.title
          }}？
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
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, reactive, ref} from "vue";
import useLoading from "@/hooks/loading";
import {addVarMap, changeVarStatus, delVarMap, getVarMap, listVarMapAll, updateVarMap} from "@/api/system/var-map";
import {TableColumnData, TreeNodeData} from "@arco-design/web-vue";
import {handleTree, handleTreeNodeData} from "@/utils/ruoyi";
import {notification} from "@/hooks/my-design";
import {StationTypeEnum} from "@/api/system/device";

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_variable_type
} = proxy?.useDict("sys_variable_type");
//******* 这里编写动态获取下拉列表 end ******

const props = defineProps({
  stationType: {
    type: Number,
    default: StationTypeEnum.power
  }
})

const formRef = ref<any>(null);
const renderData = ref<any>([]);
//默认上级目录
const defaultParentList = ref<any>(
    [
      {
        key: 0,
        title: "根目录"
      }
    ]
);
//上级目录
const parentList = ref<any>();
const checkedValue = ref<any>(0);
const unCheckedValue = ref<any>(1);
//加载中
const {loading, setLoading} = useLoading(true);
const tabColumns = computed<any[]>(() => [
  {
    title: "属性名称",
    dataIndex: "indexName",
    align: "left",
  },
  {
    title: "属性编码",
    dataIndex: "indexSn",
    align: "center",
  },
  {
    title: "单位",
    dataIndex: "unit",
    align: "center",
  },
  {
    title: "适用类型",
    dataIndex: "varType",
    slotName: "varType",
    align: "center",
  },
  {
    title: "排序",
    dataIndex: "orderNum",
    align: "center",
  },
  {
    title: "操作",
    dataIndex: "operations",
    slotName: "operate",
    align: "center",
    width: 210
  },
]);

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
//生成表单模型
const generateFormDrawerModel: any = () => {
  return {
    id: 0,
    visible: false,
    loading: false,
    indexName: '', //属性名称
    indexSn: '', //属性编码
    unit: '', //单位
    varType: 1, //适用类型
    stopFlag: 0,
    parentId: 0,
    parentName: undefined,
    orderNum: 0,
    remark: '',
  };
};

const rules = reactive({
  indexName: [{required: true, message: "请输入属性名称"},
    {
      validator: (value: any, cb: any) => {
        const validPattern = /^[a-zA-Z0-9\u4e00-\u9fa5]{1,20}$/;
        if (!validPattern.test(value)) {
          cb('请输入汉字、字母或数字，最多20字符')
        } else {
          cb();
        }
      }
    }],
});

//表单模型
const formDrawer = ref(generateFormDrawerModel());
//生成查询条件
const generateFormModel = () => {
  return {
    //属性名称
    indexName: "",
    indexSn: "",
    varType: undefined,
  };
};
//查询项
const searchParams = ref(generateFormModel());
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
    name: record.indexName
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
    name: record.indexName,
    // value: record.id
  }
}

const handleDeviceVarUpdate = (record: any) => {
  formDrawer.value.visible = true;
  getVarDetail(record.id);
}

const getVarDetail = async (id: number) => {
  try {
    formDrawer.value.loading = true;
    const res: any = await getVarMap(id);
    if (res.code == 200) {
      formDrawer.value = {...formDrawer.value, ...res.data};
    }
  } catch (e) {

  } finally {
    formDrawer.value.loading = false;
  }
}

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const res: any = await listVarMapAll({stationType: props.stationType, ...searchParams.value});
    renderData.value = handleTree(res.data);
    let treeData = handleTreeNodeData(res.data, "id", "indexName");
    parentList.value = defaultParentList.value.concat(treeData);
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

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
  let result: any = {code: 201};
  operateModalModel.value.visible = false;
  setLoading(true);
  try {
    if (operateModalModel.value.type == 0) {
      result = await changeVarStatus(operateModalModel.value.id, operateModalModel.value.value);
    } else if (operateModalModel.value.type == 1) {
      result = await delVarMap(operateModalModel.value.id);
    }

    if (result.code == 200) {
      handleOperateModelCancle();
      await fetchData();
    }
    notification(result);
  } catch (e) {

  } finally {
    setLoading(false);
  }
}

/**
 * 选择上级目录
 */
const handleParentChange = () => {
  if (formDrawer.value.parentId > 0) {
    let info: any = findItemByKey(parentList.value, formDrawer.value.parentId);
    if (info) {
      formDrawer.value.parentName = info.title;
    }
  }
}

const findItemByKey = (list: any, key: number) => {
  for (let i = 0; i < list.length; i++) {
    const item = list[i];
    if (item.key === key) {
      return item;
    }
    if (item.children && item.children.length > 0) {
      const foundItem: any = findItemByKey(item.children, key);
      if (foundItem) {
        return foundItem;
      }
    }
  }
  return null;
}


const handleSubmitForm = async () => {
  const valid = await formRef.value?.validate();
  if (!valid) {
    try {
      let res: any;
      formDrawer.value.loading = true;
      await handleParentChange();
      if (formDrawer.value.id == 0) {
        res = await addVarMap(formDrawer.value);
      } else {
        res = await updateVarMap(formDrawer.value);
      }
      if (res.code == 200) {
        handleFormCancel();
        await fetchData();
      }
      notification(res);
    } catch (e) {
    } finally {
      formDrawer.value.loading = false;
    }
  }

}

const handleFormCancel = () => {
  formDrawer.value.visible = false;
  formDrawer.value = generateFormDrawerModel();
  formRef.value?.resetFields();
}

const handleDeviceVarAdd = () => {
  formDrawer.value.visible = true;
}

const handleReset = async () => {
  searchParams.value = generateFormModel();
  await fetchData();
}

const filterTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.title.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
}

const handleTreeChange = (id) => {
  if (id == 0) {
    formDrawer.value.varType = 1;
  } else {
    formDrawer.value.varType = 0;
  }
}

onMounted(async () => {
  await fetchData();
})
</script>

<style scoped>

</style>a