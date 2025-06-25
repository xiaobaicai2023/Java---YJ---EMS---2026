<!--
 * 功能：站点管理
 * 作者：曹晓桐
 * 日期：2023-11-11
-->
<template>
  <div>
    <a-card class="content">
      <!-- 表单搜索 -->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left"
            auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="stationName" label="站点名称">
                  <a-input v-model="searchModel.stationName" placeholder="请输入站点名称，支持模糊查询" allow-clear />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-space direction="vertical" :size="18">
                  <a-button type="primary" @click="search">
                    <template #icon>
                      <icon-search />
                    </template>
                    搜索
                  </a-button>
                </a-space>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
      </a-row>

      <!-- 分割线 -->
      <a-divider style="margin-top: 0" />

      <!-- 按钮 -->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleGroupAdd">
              <template #icon>
                <icon-plus />
              </template>
              新建
            </a-button>
            <a-button type="primary" @click="handleStationAdd">
              <template #icon>
                <icon-plus />
              </template>
              新建站点
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table row-key="id" ref="tableRef" :hide-expand-button-on-empty="true" :scroll="{ x: 2080 }"
        :bordered="{ wrapper: true, cell: true }" :loading="loading" :columns="columns" :data="renderData" show-page-size>
        <template #stationType="{ record }">
          <dict-tag :options="sys_station_type" :value="record.stationType" />
        </template>
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag" />
        </template>
        <template #operate="{ record }">
          <a-button v-if="record.groupOrStation === 2" size="small" type="text" @click="handleStopFlagClick(record)"
            :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : '启用' }}
          </a-button>
          <a-button v-if="record.groupOrStation === 2" size="small" type="text" status="success"
            @click="handleUpdate(record)">编辑</a-button>

        </template>
      </a-table>

      <!-- 删除弹框 start -->
      <a-modal v-model:visible="operateModalModel.visible" class="modal-box">
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
      <a-drawer :width="700" :visible="formDrawer.groupVisible" @ok="handleSubmitForm" @cancel="handleFormCancel"
        :mask-closable="false">
        <template #title>
          新建
        </template>
        <a-row :gutter="12">
          <a-col :span="24">
            <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
              :rules="formDrawer.rules">

              <!-- 分组名称 -->
              <a-form-item field="stationName" label="分组名称" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formDrawer.formModel.stationName" allow-clear />
              </a-form-item>

              <!-- 所属企业 -->
              <a-form-item field="entId" label="所属企业">
                <a-select v-model="formDrawer.formModel.entId" placeholder="请选择企业" allow-clear>
                  <a-option v-for="(item, index) in enterpriseData" :value="item.id">{{ item.entName }}</a-option>
                </a-select>
              </a-form-item>

              <!-- 显示顺序  -->
              <a-form-item field="orderNum" label="显示顺序">
                <a-input-number v-model="formDrawer.formModel.orderNum" />
              </a-form-item>

              <!-- 启用状态 -->
              <a-form-item field="stopFlag" label="启用状态">
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                  v-model="formDrawer.formModel.stopFlag"><template #checked>启用</template><template
                    #unchecked>停用</template></a-switch>
              </a-form-item>

              <!-- 联系人员 -->
              <a-form-item field="linkName" label="联系人员">
                <a-input v-model="formDrawer.formModel.linkName" allow-clear />
              </a-form-item>

              <!-- 联系电话 -->
              <a-form-item field="linkPhone" label="联系电话">
                <a-input v-model="formDrawer.formModel.linkPhone" allow-clear />
              </a-form-item>

              <!-- 站点简介 -->
              <a-form-item field="description" label="站点简介">
                <a-textarea v-model="formDrawer.formModel.description" allow-clear :max-length="200" :auto-size="true"
                  :show-word-limit="true" placeholder="请输入站点简介，最多不能超过200字" />
              </a-form-item>
            </a-form>
          </a-col></a-row>
      </a-drawer>


      <a-drawer :width="1300" :visible="formDrawer.visible" @ok="handleSubmitForm" @cancel="handleFormCancel"
        :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? '编辑站点' : '添加站点' }}
        </template>
        <a-form ref="formRef" :model="formDrawer.formModel" label-align="right" :rules="formDrawer.rules">

          <a-row :gutter="24" class="row-mp-30">
            <a-col :span="12">
              <!-- 站点名称 -->
              <a-form-item field="stationName" label="站点名称" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formDrawer.formModel.stationName" allow-clear />
              </a-form-item>

              <!-- 站点编号 -->
              <a-form-item field="stationSn" label="站点编号">
                <a-input disabled v-model="formDrawer.formModel.stationSn" allow-clear />
              </a-form-item>

              <!-- 上级目录  -->
              <a-form-item field="parentId" label="上级目录">
                <a-select v-model="formDrawer.formModel.parentId" placeholder="请选择上级目录" allow-clear
                  @change="parentIdChange">
                  <a-option v-for="(item, index) in renderGroupData" :key="index" :value="item.id.toString()">{{
                    item.stationName }}</a-option>
                </a-select>
              </a-form-item>

              <!-- 电站类型  -->
              <a-form-item field="stationType" label="电站类型">
                <a-select v-model="formDrawer.formModel.stationType" allow-clear placeholder="电站类型">
                  <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                    :value="dict.value"></a-option>
                </a-select>
              </a-form-item>

              <!-- 建站时间 -->
              <a-form-item field="buildSiteTime" label="建站时间">
                <a-date-picker v-model="formDrawer.formModel.buildSiteTime" placeholder="请选择建站时间" />
              </a-form-item>

              <!-- 投运时间 -->
              <a-form-item field="useSiteTime" label="投运时间">
                <a-date-picker v-model="formDrawer.formModel.useSiteTime" placeholder="请选择投运时间" allow-clear />
              </a-form-item>

              <!-- 电压等级 -->
              <a-form-item field="voltageLevel" label="电压等级">
                <a-input-number v-model="formDrawer.formModel.voltageLevel" allow-clear>
                  <template #append>
                    kV
                  </template>
                </a-input-number>
              </a-form-item>

              <!-- 报警开关  -->
              <a-form-item field="openAlarm" label="报警开关">
                <template #extra>
                  <span>
                    <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill /></i>
                    站点报警总开关
                  </span>
                </template>
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                  v-model="formDrawer.formModel.openAlarm"><template #checked>开启</template><template
                    #unchecked>关闭</template></a-switch>
              </a-form-item>

              <!-- 是否停用 -->
              <a-form-item field="stopFlag" label="启用状态">
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                  v-model="formDrawer.formModel.stopFlag"><template #checked>启用</template><template
                    #unchecked>停用</template></a-switch>
              </a-form-item>

              <!-- 站点照片 -->
              <a-form-item field="picUrl" label="站点照片">
                <a-upload :limit="1" list-type="picture-card" :file-list="formDrawer.fileList"
                  image-preview :action="(`${baseUrl}/file/upload`)" :headers="headers" />
              </a-form-item>

              <!-- 联系人员 -->
              <a-form-item field="linkName" label="联系人员" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formDrawer.formModel.linkName" allow-clear />
              </a-form-item>

              <!-- 联系电话 -->
              <a-form-item field="linkPhone" label="联系电话" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formDrawer.formModel.linkPhone" allow-clear />
              </a-form-item>

              <!-- 站点简介 -->
              <a-form-item field="description" label="站点简介">
                <a-textarea v-model="formDrawer.formModel.description" allow-clear :max-length="200" :auto-size="true"
                  :show-word-limit="true" placeholder="请输入站点简介，最多不能超过200字" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-divider orientation="left">位置信息</a-divider>

              <!-- 站点省市 -->
              <a-form-item field="province" label="站点省市">
                <a-cascader path-mode @change="addressChange" v-model="formDrawer.formModel.stationAddressId"
                  :virtual-list-props="{ height: 400 }" :options="addressOptions" placeholder="请选择站点省市"
                  :load-more="loadAddressMore" />
              </a-form-item>


              <!-- 电站坐标 -->
              <a-form-item field="coordinate" label="电站坐标" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formDrawer.formModel.coordinate" allow-clear />
              </a-form-item>

              <!-- 详细地址 -->
              <a-form-item field="stationAddress" label="详细地址">
                <a-input v-model="formDrawer.formModel.stationAddress" allow-clear />
              </a-form-item>

              <a-divider orientation="left">光伏配置</a-divider>
              <!-- 装机容量  -->
              <a-form-item field="sationVolume" label="装机容量" tooltip="请输入装机容量">
                <a-input-number v-model="formDrawer.formModel.sationVolume">
                  <template #append>
                    kVA
                  </template></a-input-number>
              </a-form-item>

              <!-- 方位角 -->
              <a-form-item field="azimuth" label="方位角" tooltip="请输入方位角" :validate-trigger="['change', 'blur', 'input']">
                <a-input-number v-model="formDrawer.formModel.azimuth" allow-clear>
                  <template #append>
                    °
                  </template></a-input-number>
              </a-form-item>

              <!-- 方位角度 -->
              <a-form-item field="dipAngle" label="方位角度" tooltip="请输入方位角度"
                :validate-trigger="['change', 'blur', 'input']">
                <a-input-number v-model="formDrawer.formModel.dipAngle" allow-clear>
                  <template #append>
                    °
                  </template></a-input-number>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-drawer>
      <MapSearch  :visible="false" />
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { computed, getCurrentInstance, onMounted, ref } from 'vue';
import useLoading from '@/hooks/loading';
import { TableColumnData, TableInstance } from "@arco-design/web-vue";
import { listStation, getStation, addStation, updateStation } from "@/api/system/station";
import { listEnterprise } from "@/api/system/enterprise";
import { FormInstance } from '@arco-design/web-vue/es/form';
import { getToken } from '@/utils/auth';
import { getAddress, listAddressAll } from '@/api/system/address';
import MapSearch from '@/components/map-search/index.vue'
import { notification } from "@/hooks/my-design";
/*************************** 变量区域 begin ***************************/
//生成查询条件对象
const generateSearchModel = () => {
  return {
    stationName: ''
  };
};

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_normal_disable, sys_station_type } = proxy?.useDict("sys_normal_disable", "sys_station_type");
//******* 这里编写动态获取下拉列表 end ******

//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const { loading, setLoading } = useLoading(true);
//设置表格列
const columns = computed<TableColumnData[]>(() => [
  {
    title: "编号",
    dataIndex: 'id',
    align: 'center',
    width: 80
  },
  {
    title: "电站名称",
    dataIndex: 'stationName',
    width: 200
  },
  {
    title: "电站编号",
    dataIndex: 'stationSn',
    width: 200
  },
  {
    title: "电站类型",
    dataIndex: 'stationType',
    slotName: 'stationType',
    align: 'center',
    width: 150
  },
  {
    title: "装机容量",
    dataIndex: 'sationVolume',
    width: 150
  },
  {
    title: "联系人员",
    dataIndex: 'linkName',
    width: 150
  },
  {
    title: "联系电话",
    dataIndex: 'linkPhone',
    width: 150
  },
  {
    title: "方位角",
    dataIndex: 'azimuth',
    align: "center",
    width: 150
  },
  {
    title: "方位角度",
    dataIndex: 'dipAngle',
    align: "center",
    width: 150
  },
  {
    title: "投运时间",
    dataIndex: 'useSiteTime',
    width: 150
  },
  {
    title: "报警开关",
    dataIndex: 'openAlarm',
    align: "center",
    width: 150
  },
  {
    title: "显示顺序",
    dataIndex: 'orderNum',
    align: "center",
    width: 100
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
    width: 200,
    fixed: "right"
  }
]);

//表格实例
const tableRef = ref<TableInstance>();
const renderData = ref<any[]>([]);
const renderGroupData = ref<any[]>([]);
const enterpriseData = ref<any[]>([]);
const addressOptions = ref<any>([])
const addressLoading = ref<boolean>(false);

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


const formRef = ref<FormInstance>();

//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    groupVisible: false,
    rules: {
      entId: [{ required: true, message: '请选择企业' }],
      stationName: [{ required: true, message: '请输入站点名称' }],
    },
    fileList: [],
    formModel: {
      id: 0,
      entId: '',
      parentId: '',
      groupId: '',
      stationName: '',
      stationSn: '',
      groupOrStation: 0,
      stationType: '',
      logicCode: '',
      buildSiteTime: '',
      voltageLevel: 0,
      sationVolume: 0,
      azimuth: 0,
      dipAngle: 0,
      useSiteTime: '',
      coordinate: '',
      stationAddressId: [['']],
      stationAddress: '',
      picUrl: '',
      description: '',
      linkName: '',
      linkPhone: '',
      openAlarm: 1,
      orderNum: 0,
      remark: '',
      createBy: '',
      createTime: '',
      updateBy: '',
      updateTime: '',
      province: 0,
      city: 0,
      county: 0,
      stopFlag: 1,
      deleteFlag: 0
    }
  };
};

//switch选中值
const checkedValue = ref<number>(0);
const unCheckedValue = ref<number>(1);

//表单模型
const formDrawer = ref(generateFormDrawerModel());

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

//重置查询条件
const search = () => {
  fetchData();
}

/**
 * 新增分组数据
 * @param row 数据行
 */
const handleGroupAdd = async () => {
  formDrawer.value = generateFormDrawerModel();
  formDrawer.value.groupVisible = true;
  formDrawer.value.formModel.groupOrStation = 1;
  formDrawer.value.formModel.parentId = "0";
  await fetchEnterpriseData()
}

/**
 * 新增电站数据
 * @param row 数据行
 */
const handleStationAdd = async () => {
  formDrawer.value = generateFormDrawerModel();
  formDrawer.value.visible = true;
  formDrawer.value.formModel.groupOrStation = 2;
  await fetchGroupData();
  addressOptions.value = await fetchAddressData(0);
}

/**
 * 加载地址
 */
const loadAddressMore = async (option: any, done: any) => {
  console.log("loadAddressMore", option);
  let nodes: any = await fetchAddressData(option.value);
  done(nodes);
}

/**
 * 地址选择
 * @param path
 */
const addressChange = (path: any) => {
  console.log("addressChange", path)
  formDrawer.value.formModel.province = path["0"];
  formDrawer.value.formModel.city = path["1"];
  formDrawer.value.formModel.county = path["2"];
}

/**
 * 停用弹框
 */
const handleStopFlagClick = async (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.stationName;
  operateModalModel.value.value = record.stopFlag == 0 ? 1 : 0;
  operateModalModel.value.type = 0;
}

/**
 * 操作提示弹框取消
 */
const handleOperateModelCancle = () => {
  operateModalModel.value = generateOperateModalModel();
}

/**
 * 操作提示弹框确认
 */
const handleOperateModelOk = async () => {
  let result;
  if (operateModalModel.value.type == 0) {
    let info = await getStation(operateModalModel.value.id);
    if (info.code == 200) {
      result = await updateStation({ ...info.data, stopFlag: operateModalModel.value.value });
    } else {
      result = info;
    }
  }
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}


/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  await fetchGroupData();
  addressOptions.value = await fetchAddressData(0);
  let result = await getStation(record.id);
  if (result.code == 200) {
    result.data.parentId = result.data.parentId == 0 ? '' : result.data.parentId += '';
    result.data.stationType += "";
    if (result.data.voltageLevel && typeof (result.data.voltageLevel) == 'string') {
      result.data.voltageLevel = parseFloat(result.data.voltageLevel);
    }
    formDrawer.value.formModel = result.data;
    formDrawer.value.formModel.stationAddressId = [[await getAddressDetail(result.data.province), await getAddressDetail(result.data.city), await getAddressDetail(result.data.county)]]
    console.log("1", formDrawer.value.formModel.stationAddressId)
    formDrawer.value.visible = true;
    console.log("2")
  }
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    let result;
    formDrawer.value.formModel.picUrl = formDrawer.value.fileList[0];
    if (formDrawer.value.formModel.id == 0) {
      result = await addStation(formDrawer.value.formModel);
    } else {
      result = await updateStation(formDrawer.value.formModel);
    }
    notification(result);
    if (result.code == 200) {
      await fetchData();
      formDrawer.value = generateFormDrawerModel();
    }
  }
}

/**
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
}
/**
 * 上级目录值发生改变
 * @param val
 */
const parentIdChange = async (val: any) => {
  console.log(val)
  if (val == '') {
    formDrawer.value.formModel.entId = '';
  } else {
    formDrawer.value.formModel.entId = renderGroupData.value.find(item => item.id == formDrawer.value.formModel.parentId).entId;
  }
  console.log("endId", formDrawer.value.formModel.entId);
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listStation({
      ...searchModel.value,
    });
    if (res.code == 200 && res.data && res.data.length > 0) {
      let groupStation = res.data.filter((item: any) => item.groupOrStation === 1)
      if (groupStation && groupStation.length > 0) {
        let data: any = [];
        groupStation.forEach((item: any) => {
          let childrenList = res.data.filter((itemC: any) => itemC.groupId === item.id)
          data.push({
            id: item.id,
            stationName: item.stationName,
            children: childrenList
          })
        })
        renderData.value = data;
      }
      // renderData.value = res.data;
      tableRef.value?.expandAll(true);
    }

  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};


/**
 * 查询表格数据
 */
const fetchGroupData = async () => {
  try {
    const res = await listStation({
      groupOrStation: 1
    });
    if (res.code == 200 && res.data && res.data.length > 0) {
      renderGroupData.value = res.data;
    }

  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 查询公司列表
 */
const fetchEnterpriseData = async () => {
  try {
    const res = await listEnterprise({ pageSize: 1000 });
    enterpriseData.value = res.rows;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 查询地址
 * @param parentId 父类ID
 */
const fetchAddressData = async (parentId: any) => {
  let data: any = [];
  addressLoading.value = true;
  try {
    const res = await listAddressAll({ parentId: parentId });
    if (res.code == 200 && res.data.length > 0) {
      res.data.forEach((item: any) => {
        let info: any = {
          value: item.id,
          label: item.name
        };
        if (item.level == 3) {
          info["isLeaf"] = true;
        }
        data.push(info)
      })
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    addressLoading.value = false;
  }
  return data;
};

/**
 * 获取地址详情信息
 * @param id
 */
const getAddressDetail = async (id: any) => {
  if (!id) {
    return ''
  }
  let res = await getAddress(id);
  if (res.code == 200) {
    return res.data.name;
  } else {
    return '';
  }
}

//路径
const baseUrl = import.meta.env.VITE_API_BASE_URL;
//请求头
const headers: Record<string, string> = {
  'Authorization': `Bearer ${getToken()}`
}

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
  tableRef.value?.expandAll(true);
})
</script>

<style scoped></style>
