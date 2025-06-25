<template>
  <div>
    <a-card class="general-card" title="查询表格">
      <!-- 表单搜索 -->
      <a-row>
        <a-col :flex="1">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
            label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="entId" label="企业ID">
                  <a-input v-model="searchModel.entId" placeholder="请输入企业ID" @press-enter="search" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="parentId" label="上级ID">
                  <a-input v-model="searchModel.parentId" placeholder="请输入上级ID" @press-enter="search" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="groupId" label="分组ID">
                  <a-input v-model="searchModel.groupId" placeholder="请输入分组ID" @press-enter="search" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="stationName" label="电站名称">
                  <a-input v-model="searchModel.stationName" placeholder="请输入电站名称" @press-enter="search" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="stationSn" label="电站编号">
                  <a-input v-model="searchModel.stationSn" placeholder="请输入电站编号" @press-enter="search" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="groupOrStation" label="记录类型">
                  <a-select v-model="searchModel.groupOrStation" placeholder="请选择记录类型">
                    <a-option v-for="dict in sys_protocol_code" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="logicCode" label="逻辑代码">
                  <a-input v-model="searchModel.logicCode" :max-length="64" placeholder="请输入逻辑代码" @press-enter="search" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="stopFlag" label="启用状态">
                  <a-select v-model="searchModel.stopFlag" placeholder="请选择是否停用">
                    <a-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="deleteFlag" label="是否删除">
                  <a-input v-model="searchModel.deleteFlag" placeholder="请输入是否删除" @press-enter="search" />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>

        <a-divider style="height: 84px" direction="vertical" />
        <a-col :flex="'86px'" style="text-align: right">
          <a-space direction="vertical" :size="18">
            <a-button type="primary" @click="search">
              <template #icon>
                <icon-search />
              </template>
              搜索
            </a-button>
            <a-button @click="reset">
              <template #icon>
                <icon-refresh />
              </template>
              重置
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
              新建
            </a-button>
            <a-upload action="/">
              <template #upload-button>
                <a-button>
                  批量操作
                </a-button>
              </template>
            </a-upload>
          </a-space>
        </a-col>
        <a-col :span="12" style="display: flex; align-items: center; justify-content: end">
          <a-button>
            <template #icon>
              <icon-download />
            </template>
            下载
          </a-button></a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table row-key="id" :loading="loading" :pagination="pagination" :columns="columns"
        :data="renderData" @page-change="onPageChange" @page-size-change="onPageSizeChange" show-page-size>
        <template #operate="{ record }">
          <a-button size="small" type="text" status="success" @click="handleUpdate(record)">编辑</a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">删除
          </a-button>
        </template>
      </a-table>

      <!-- 删除弹框 start -->
      <a-modal width="400px" v-model:visible="delModalModel.visible" class="modal-box">
        <template #title>
          <icon-close-circle size="18" style="color:rgb(var(--red-6)); margin-right: 5px;" />
          确认提示
        </template>
        <div style="text-align: center;">是否确认删除名称为"{{ delModalModel.title }}"的数据项？</div>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleDelModelCancle">取消</a-button>
              <a-button type="primary" @click="handleDelModelOk">确认</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>

      <!-- 添加右弹层 start -->
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { computed, getCurrentInstance, onMounted, reactive, ref } from 'vue';
import useLoading from '@/hooks/loading';
import { notification } from "@/hooks/my-design";
import { listStation, getStation, delStation, addStation, updateStation } from "@/api/system/station";
import { BasePagination } from '@/api/common';
import { FormInstance } from '@arco-design/web-vue/es/form';

/*************************** 变量区域 begin ***************************/
//生成查询条件对象
const generateSearchModel = () => {
  return {
    entId: '',
    parentId: '',
    groupId: '',
    stationName: '',
    stationSn: '',
    groupOrStation: '',
    stationType: '',
    logicCode: '',
    stopFlag: '',
    deleteFlag: ''
  };
};

//******* 这里写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_protocol_code,sys_normal_disable } = proxy?.useDict("sys_protocol_code","sys_normal_disable");
//******* 这里写动态获取下拉列表 end ******

//查询表单对象
const searchModel = ref(generateSearchModel());

//加载中
const { loading, setLoading } = useLoading(true);

//表格分页参数
const pagination: any = reactive({ ...BasePagination()});

//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "企业ID",
    dataIndex: 'entId',
  },
  {
    title: "上级ID",
    dataIndex: 'parentId',
  },
  {
    title: "分组ID",
    dataIndex: 'groupId',
  },
  {
    title: "电站名称",
    dataIndex: 'stationName',
  },
  {
    title: "电站编号",
    dataIndex: 'stationSn',
  },
  {
    title: "记录类型（1分组 2站点）",
    dataIndex: 'groupOrStation',
  },
  {
    title: "电站类型（1配电 2光伏）",
    dataIndex: 'stationType',
  },
  {
    title: "逻辑代码（企业+站点）",
    dataIndex: 'logicCode',
  },
  {
    title: "建站时间",
    dataIndex: 'buildSiteTime',
  },
  {
    title: "电压等级",
    dataIndex: 'voltageLevel',
  },
  {
    title: "装机容量",
    dataIndex: 'sationVolume',
  },
  {
    title: "方位角度",
    dataIndex: 'azimuth',
  },
  {
    title: "组件倾角",
    dataIndex: 'dipAngle',
  },
  {
    title: "投运时间",
    dataIndex: 'useSiteTime',
  },
  {
    title: "电站坐标",
    dataIndex: 'coordinate',
  },
  {
    title: "电站地址",
    dataIndex: 'stationAddress',
  },
  {
    title: "电站照片",
    dataIndex: 'picUrl',
  },
  {
    title: "电站简介",
    dataIndex: 'description',
  },
  {
    title: "联系人员",
    dataIndex: 'linkName',
  },
  {
    title: "联系电话",
    dataIndex: 'linkPhone',
  },
  {
    title: "报警开关",
    dataIndex: 'openAlarm',
  },
  {
    title: "显示顺序",
    dataIndex: 'orderNum',
  },
  {
    title: "备注说明",
    dataIndex: 'remark',
  },
  {
    title: "启用状态（0正常 1停用）",
    dataIndex: 'stopFlag',
  },
  {
    title: "操作",
    dataIndex: 'operate',
    slotName: 'operate',
  }
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

const formRef = ref<FormInstance>();

//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    rules: {
    },
    formModel: {
      //如果某些字段有默认，请在此处自行设定
      id: null
    }
  };
};

//表单模型
const formDrawer = ref(generateFormDrawerModel());

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

//重置查询条件
const search = () => {
  pagination.current = 1 ;

  fetchData();
}

//重置查询条件
const reset = () => {
  searchModel.value = generateSearchModel();
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
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  let result = await getStation(record.id);
  if (result.code == 200) {
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
  delModalModel.value.id = record.id,
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
  let result = await delStation(delModalModel.value.id);
  notification(result);
  fetchData();
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    let result;
    if (formDrawer.value.formModel.id == 0) {
      result = await addStation(formDrawer.value.formModel);
    } else {
      result = await updateStation(formDrawer.value.formModel);
    }
    notification(result);
    fetchData();
  }
}

/**
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
}

/**
 * 输入查询
 */
const handleQuery = () => {
  //todo 查询
}

/**
 * 日期选择器-值改变
 * @param dateString 日期字符串数组
 * @param date 日期数组
 */
const onRangePickerChange = (dateString: any, date: any) => {
  console.log("onRangePickerChange", dateString, date)
}

/**
 * 日期选择器-选择日期
 * @param dateString 日期字符串数组
 * @param date 日期数组
 */
const onRangePickerSelect = (dateString: any, date: any) => {
  console.log("onRangePickerSelect", dateString, date)
}

/**
 * 日期选择器-确认
 * @param dateString 日期字符串数组
 * @param date 日期数组
 */
const onRangePickerOk = (dateString: any, date: any) => {
  console.log("onRangePickerOk", dateString, date)
  console.log(formDrawer.value.formModel.rangeDate)
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listStation({
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
