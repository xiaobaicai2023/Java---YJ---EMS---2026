<!--
* 功能：维保记录
* 作者：cxt
* 日期：2023-11-18
-->
<template>
  <div>
    <a-card class="content">

      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left" auto-label-width>
            <a-row :gutter="16">
              <!-- <a-col :span="6">
                <a-form-item field="stationType" label="站点类型">
                  <a-select v-model="searchModel.stationType" placeholder="请选择站点类型" allow-clear>
                    <a-option :key="0" label="全部类型" :value="Number(-100)" />
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                      :value="Number(dict.value)" />
                  </a-select>
                </a-form-item>
              </a-col> -->

              <!--能源类型-->
              <a-col :span="6">
                <a-form-item field="stationType" :label="$t('global.energy')">
                  <a-select v-model="searchModel.stationType" :placeholder="$t('global.pleaseSelect')"
                            allow-clear>
                    <a-option :key="0" :label="'全部类型'" :value="Number(-100)"/>
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"/>
                  </a-select>
                </a-form-item>
              </a-col>

              <a-col :span="6">
                <a-form-item field="orderTitle" :label="$t('pv.maintenance.list.maintenanceName')">
                  <a-input v-model="searchModel.maintainTitle" :placeholder="$t('global.fuzzySearch')" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item field="time" :label="$t('pv.maintenance.list.maintenanceTime')">
                  <a-range-picker v-model="searchModel.time"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item hide-label>
                  <a-button type="primary" @click="search">
                    <template #icon>
                      <icon-search/>
                    </template>
                    {{ $t('global.search') }}
                  </a-button>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

      <!-- 分割线 -->
      <a-divider style="margin-top: 0"/>

      <!-- 操作按钮 start-->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('pv.maintenance.list.newMaintenance') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 操作按钮 end-->

      <!-- 表格内容  start-->
      <a-table :scroll="{ x: 1110 }" row-key="id" ref="tableRef" :loading="loading"
               :bordered="{ wrapper: true, cell: true }" :columns="columns" :data="renderData"
               @page-change="onPageChange" :pagination="pagination" @page-size-change="onPageSizeChange">

        <!-- 设备 -->
        <template #deviceIdFor="{ record }">
          {{ record.deviceIdFor.map((item: any) => item.name).join(',') }}
        </template>

        <!-- 维保人 -->
        <template #userNameFor="{ record }">
          {{ record.userNameFor.map((item: any) => item.name).join(',') }}
        </template>

        <!-- 维保时间 -->
        <template #maintainTime="{ record }">
          {{ record.maintainTime || '-' }}
        </template>

        <template #operate="{ record }">
          <a-button size="small" type="text" status="success" @click="handleUpdate(record.id)">{{
              $t('global.edit')
            }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{
              $t('global.delete')
            }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleDetail(record.id)">{{
              $t('global.detail')
            }}
          </a-button>
        </template>
      </a-table>
      <!-- 表格内容 end -->


      <!-- 添加/编辑维保 -->
      <a-drawer :width="700" :esc-to-close="false" :ok-loading="formDrawer.loading" :visible="formDrawer.visible"
                @ok="handleSubmitForm"
                @cancel="handleFormCancel" :mask-closable="false">
        <template #title>
          {{
            formDrawer.formModel.id ? $t('pv.maintenance.list.editMaintenance') : $t('pv.maintenance.list.newMaintenance')
          }}
        </template>

        <a-spin :loading="formDrawer.loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
          <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                  :rules="formDrawer.rules">

            <!-- 维保名称 -->
            <a-form-item field="maintainTitle" :label="$t('pv.maintenance.list.maintenanceName')">
              <a-input v-model="formDrawer.formModel.maintainTitle"/>
            </a-form-item>

            <!-- 站点类型 -->
            <a-form-item field="stationType" :label="$t('pv.maintenance.list.siteType')">
              <a-select disabled v-model="formDrawer.formModel.stationType"
                        :placeholder="$t('pv.maintenance.list.pleaseSelectSite')" allow-clear>
                <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                          :value="Number(dict.value)"></a-option>
              </a-select>
            </a-form-item>

            <!-- 关联站点 -->
            <a-form-item field="stationId" 
			            :label="$t('pv.maintenance.record.relatedSite')" 
			            :content-flex="false"
                        :merge-props="false">
              <a-tree-select allow-search :allow-clear="false" v-model="formDrawer.formModel.stationId"
                             :data="renderStationData" @change="handleStationChange"
                             :placeholder="$t('pv.maintenance.record.ElectSite')" 
							 :fieldNames="{key: 'id', title: 'deptName', children: 'children'}" 
							 :filter-tree-node="filterTreeNode">
						 </a-tree-select>
            </a-form-item>

            <!-- 关联设备 -->
            <a-form-item field="deviceIdFor" :label="$t('pv.maintenance.record.relatedDevice')">
              <a-space direction="vertical" fill :size="16">
                <!-- 添加按钮 -->
                <a-button type="primary" @click="handleDeviceAdd">
                  <template #icon>
                    <icon-plus/>
                  </template>
                  {{ $t('power.energy.device.addDevice') }}
                </a-button>
                <a-table row-key="value" :bordered="{ wrapper: true, cell: true }" :pagination="false"
                         :data="formDrawer.formModel.deviceIdFor" :columns="deviceColumns"
                         v-bind:hide-expand-button-on-empty="true">
                  <template #operate="{ rowIndex }">
                    <a-button size="small" type="primary" status="danger"
                              @click="handleDeviceDel(rowIndex)">{{ $t('global.remove') }}
                    </a-button>
                  </template>
                </a-table>
              </a-space>
            </a-form-item>

            <!-- 维保人员 -->
            <a-form-item field="userIds" :label="$t('pv.maintenance.record.mPerson')"
                         :validate-trigger="['change', 'input']">
              <a-select multiple @change="handleUserChange" v-model="formDrawer.formModel.userIds" allow-clear
                        :placeholder="$t('pv.maintenance.record.selectMPerson')">
                <a-option v-for="item in renderUserData" :key="item.id" :label="item.nickName"
                          :value="item.id"></a-option>
              </a-select>
            </a-form-item>

            <!-- $t('pv.maintenance.list.maintenanceTime') -->
            <a-form-item field="maintainTime" :label="$t('pv.maintenance.list.maintenanceTime')">
              <a-date-picker show-time v-model="formDrawer.formModel.maintainTime" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>

            <!-- 维保内容 -->
            <a-form-item field="maintainContent" :label="$t('pv.maintenance.record.mContent')">
              <a-textarea :placeholder="$t('pv.maintenance.record.pleaseMContent')" :max-length="200"
                          v-model="formDrawer.formModel.maintainContent" allow-clear/>
            </a-form-item>

            <!-- 图片描述 -->
            <a-form-item field="maintainFiles" :label="$t('pv.maintenance.list.pictureDesc')">
              <a-upload accept="image/*" :download="true" list-type="picture-card"
                        :file-list="formDrawer.formModel.filesImage" image-preview :action="(`${baseUrl}/file/upload`)"
                        :headers="headers" @change="handleUploadChange"/>
            </a-form-item>
          </a-form>
        </a-spin>
      </a-drawer>


      <!-- 维保详情 start -->
      <a-drawer :width="700" :esc-to-close="false" :visible="formDrawer.detailVisible" @ok="handleFormCancel"
                @cancel="handleFormCancel" :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.maintainTitle }}
        </template>
        <a-spin :loading="formDrawer.loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
          <a-row>
            <a-col style="height:100vh;border-right: 1px solid var(--color-border);">
              <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                              :column="1" size="large">
                <!-- 维保名称 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.maintenanceName')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.maintainTitle }}</span>
                </a-descriptions-item>

                <!-- 维保站点 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.record.mSite')}`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel?.stationName || '-' }}</span>
                </a-descriptions-item>

                <!-- 维保设备 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.record.mDevice')}`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>
                    <a-table :bordered="{ wrapper: true, cell: true }" :data="formDrawer.formModel.deviceIdFor"
                             :pagination="false">
                      <template #columns>
                        <a-table-column :title="$t('power.energy.device.name')" data-index="name">
                        </a-table-column>
                        <a-table-column :title="$t('pv.maintenance.record.deviceCode')" data-index="value">
                        </a-table-column></template>
                    </a-table>
                  </span>
                </a-descriptions-item>

                <!-- 维保人员 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.record.mPerson')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.userNameFor.map((item: any) => item.name).join(',') }}</span>
                </a-descriptions-item>


                <!-- 维保时间 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.maintenanceTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.maintainTime }}</span>
                </a-descriptions-item>

                <!-- 维保内容 -->
                <a-descriptions-item label="维保内容:" :label="`${$t('pv.maintenance.record.mContent)')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.maintainContent }}</span>
                </a-descriptions-item>

                <!-- 图片描述 -->
                <a-descriptions-item label="图片描述:" :label="`${$t('pv.maintenance.record.mContent)')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <a-upload v-else-if="formDrawer.formModel.filesImage && formDrawer.formModel.filesImage.length > 0"
                            disabled :download="true" list-type="picture-card"
                            :file-list="formDrawer.formModel.filesImage"
                            image-preview :limit="formDrawer.formModel.filesImage.length"/>
                </a-descriptions-item>

              </a-descriptions>
              <a-divider orientation="left">{{ $t('pv.maintenance.list.systemInfo') }}</a-divider>
              <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                              :column="1" size="large">

                <!-- 创建人员 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.createS')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.createBy }}</span>
                </a-descriptions-item>

                <!-- 创建时间 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.createTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.createTime }}</span>
                </a-descriptions-item>

                <!-- 更新人员 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.updateS')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.updateBy }}</span>
                </a-descriptions-item>

                <!-- 更新时间 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.updateTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.updateTime }}</span>
                </a-descriptions-item>
              </a-descriptions>
            </a-col>
          </a-row>
        </a-spin>
      </a-drawer>

      <!-- 选择设备 start -->
      <a-modal width="600px" :visible="modalModel.visible" :footer="false" title-align="start"
               :title="formDrawer.formModel?.stationName || '-'" @cancel="handleModalClose">
        <a-row style="margin-bottom: 16px">
          <a-col style="text-align: center;">
            <a-tabs v-if="formDrawer.formModel.stationType == props.stationType" v-model:active-key="tabsActiveKey"
                    @change="handleTabsChange">
              <a-tab-pane v-for="(item, index) in tabsList" :key="item.value" :title="item.key">
              </a-tab-pane>
            </a-tabs>
            <a-table ref="tableDeviceRef" :bordered="{ wrapper: true, cell: true }" :scroll="{ y: 500 }"
                     :pagination="false" :data="modalModel.renderData" :columns="deviceModalColumns"
                     v-bind:hide-expand-button-on-empty="true" :default-expand-all-rows="true">
              <template #operate="{ record }">
                <a-button v-if="record.isGroup == 0" size="small" type="primary"
                          @click="handleDeviceSelect(record)">选择
                </a-button>
              </template>
            </a-table>
          </a-col>
        </a-row>
      </a-modal>
      <!-- 选择设备 end -->

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
      <!-- 操作弹框 end -->
    </a-card>

  </div>
</template>


<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, reactive, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {FileItem, Message, TableInstance} from "@arco-design/web-vue";
import {BasePagination} from '@/api/common';
import {FormInstance} from '@arco-design/web-vue/es/form';
import dayjs from 'dayjs';
import {getToken} from '@/utils/auth';
import {useUserStore} from '@/store'
import {
  addMaintenance,
  delMaintenance,
  getMaintenance,
  listMaintenance,
  updateMaintenance
} from '@/api/system/maintenance';
import {JsonCommonVo, getStationList} from '@/api/public';
import {findById, processSelectableByCompany} from '@/utils/ruoyi';
import {listUserAll} from '@/api/manage/account/user';
import {StationTypeEnum, getPVTab, listFusionGroup} from '@/api/system/device';
import {useTimeoutFn} from '@vueuse/core';
import {notification} from "@/hooks/my-design";

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
const {sys_station_type} = proxy?.useDict("sys_station_type");
//******* 这里编写动态获取下拉列表 end ******

/*************************** 变量区域 end ***************************/

//用户信息
const userStore = useUserStore()
// 获取当前时间
var dateNow = dayjs().format("YYYY-MM-DD");
var dateBefore = dayjs().subtract(1, 'month').format("YYYY-MM-DD")
//生成查询条件对象
const generateSearchModel = () => {
  return {
    stationType: props.stationType,
    maintainTitle: "",
    time: [dateBefore, dateNow]
  };
};
//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const {loading, setLoading} = useLoading(true);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
//表格数据
const renderData = ref<any[]>([]);
//站点列表
const renderStationData = ref<any>([]);
//人员数据
const renderUserData = ref<any[]>([]);
//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "维保名称",
    dataIndex: "maintainTitle",
    align: 'left',
    fixed: 'left',
    width: 200,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "站点",
    dataIndex: "stationName",
    width: 150,
    ellipsis: true,
    tooltip: {position: 'top'},
    align: 'left',
  },
  {
    title: "设备",
    dataIndex: "deviceIdFor",
    slotName: "deviceIdFor",
    width: 200,
    align: 'left',
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "维保人",
    dataIndex: "userNameFor",
    slotName: "userNameFor",
    align: 'left',
    width: 200,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "维保时间",
    dataIndex: "maintainTime",
    slotName: "maintainTime",
    width: 180,
    align: 'center',
    fixed: 'right'
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 180,
    slotName: 'operate',
    align: 'left',
    fixed: 'right'
  },
]);

//设备列表-列
const deviceColumns = computed<any[]>(() => [
  {
    title: "设备名称",
    dataIndex: "name",
    align: "left",
    fixed: "left",
    width: 250,
  },
  {
    title: "设备编码",
    dataIndex: "value",
    align: "left",
    fixed: "left",
    width: 250,
  },
  {
    title: "操作",
    dataIndex: "operate",
    width: 80,
    slotName: "operate",
    align: "center",
    fixed: "right",
  },
]);

const deviceModalColumns = computed<any[]>(() => [
  {
    title: "设备名称",
    dataIndex: "deviceName",
    slotName: "deviceName",
    align: "left",
    width: 200,
  },
  {
    title: "设备编码",
    dataIndex: "deviceSn",
    slotName: "deviceSn",
    align: "center",
    width: 200,
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: "operate",
    width: 120,
    align: "center",
    fixed: "right",
  },
]);

//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    detailVisible: false,
    loading: false,
    rules: {
      maintainTitle: [{required: true, message: "请输入维保名称"}],
      stationType: [{required: true, message: "请选择站点类型"}],
      stationId: [{required: true, message: "请选择站点"}],
      deviceIdFor: [{required: true, message: "请选择设备"}],
      userIds: [{required: true, message: "请选择维保人员"}],
      maintainTime: [{required: true, message: "请选择维保时间"}],
    },
    formModel: {
      id: 0,
      /** 维保名称 */
      maintainTitle: undefined,
      /**站点 */
      stationId: undefined,
      stationType: props.stationType,
      stationName: "",
      /** 维保人员 */
      userIds: [],
      userNameFor: [] as JsonCommonVo[],
      /**维保时间 */
      maintainTime: dayjs().format("YYYY-MM-DD HH:mm:ss"),
      /**维保内容 */
      maintainContent: undefined,
      /**图片描述 */
      maintainFiles: [] as JsonCommonVo[],
      /**设备列表 */
      deviceIdFor: [] as JsonCommonVo[],
      stopFlag: 0,
      filesImage: [] as FileItem[],
      createBy: '',
      createTime: '',
      updateBy: '',
      updateTime: '',
    }
  };
};
//表格实例
const formRef = ref<FormInstance>();
//表单模型
const formDrawer = ref(generateFormDrawerModel());
//路径
const baseUrl = import.meta.env.VITE_API_BASE_URL;
//请求头
const headers: Record<string, string> = {
  'Authorization': `Bearer ${getToken()}`
}

//选择设备模型
const generateModalModel = () => {
  return {
    visible: false,
    pvGroupName: "",
    //表格数据
    renderData: []
  }
}
//设备弹框模型
const modalModel = ref(generateModalModel());

//选中的tab
const tabsActiveKey = ref<any>(0);
//tab集合
const tabsList = ref<any>([])
//表格实例
const tableDeviceRef = ref<TableInstance>();
//展开/折叠控制值
const expandConfigValue = ref<boolean>(false);

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
/*************************** 方法区域 begin ***************************/

//搜索
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

//表格页码发生变化
const onPageChange = async (val: number) => {
  pagination.current = val;
  await fetchData();
}

//表格每页数量发生变化
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchData();
}

/**
 * 上传成功
 * @param fileItem
 */
const handleUploadChange = (fileList: FileItem[], fileItem: FileItem) => {
  if (fileItem.status == 'done') {
    let list: JsonCommonVo[] = [];
    fileList.forEach((item: FileItem, index) => {
      list.push({
        name: item.response ? item.response.data.name : item.name,
        value: item.response ? item.response.data.url : item.url,
      });
    })
    formDrawer.value.formModel.maintainFiles = list;
  }
}

/**
 * 添加维保记录
 */
const handleAdd = async () => {
  formDrawer.value = generateFormDrawerModel();
  formDrawer.value.visible = true;
  await fetchStationData();
  await fetchUserData();
}

/**
 * 选择设备
 */
const handleDeviceAdd = async () => {
  if (!formDrawer.value.formModel.stationId || formDrawer.value.formModel.stationId <= 0) {
    Message.error({
      content: '请先选择站点',
      duration: 2 * 1000,
    });
    return false
  }
  if (formDrawer.value.formModel.stationType == StationTypeEnum.pv) {
    //获取tabList
    await fetchTabData();
  } else {
    await fetchDeviceData();
  }
  modalModel.value.visible = true;
  useTimeoutFn(() => {
    handleConfigExpand();
  }, 100)
}

const handleConfigExpand = () => {
  tableDeviceRef.value?.expandAll(true);
}

/**
 * 编辑维保记录
 */
const handleUpdate = async (id: number) => {
  try {
    await handleAdd();
    formDrawer.value.loading = true;
    let res = await getMaintenance(id);
    if (res.code == 200) {
      let maintainFiles = res.data.maintainFiles;
      if (maintainFiles && maintainFiles.length > 0) {
        let list: FileItem[] = [];
        maintainFiles.forEach((item: any, index: number) => {
          list.push({
            uid: index.toString(),
            name: item.name,
            url: item.value,
          })
        })
        res.data.filesImage = list;
      }
      //处理人员
      let userNameFor = res.data.userNameFor;
      if (userNameFor && userNameFor.length > 0) {
        let list: any = [];
        userNameFor.forEach((item: any, index: number) => {
          list.push(Number(item.value));
        })
        res.data.userIds = list;
      }
      formDrawer.value.formModel = res.data;
      handleStationChange(res.data.stationId, 1);
    }

  } catch (ex) {

  } finally {
    formDrawer.value.loading = false;
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
  operateModalModel.value.name = record.maintainTitle;
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
  operateModalModel.value.visible = false;
  setLoading(true);
  let result = await delMaintenance(operateModalModel.value.id);
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}

/**
 * 查看详情
 */
const handleDetail = async (id: number) => {
  try {
    formDrawer.value = generateFormDrawerModel();
    formDrawer.value.detailVisible = true;
    formDrawer.value.loading = true;
    let res = await getMaintenance(id);
    if (res.code == 200) {

      let maintainFiles = res.data.maintainFiles;
      if (maintainFiles && maintainFiles.length > 0) {
        let list: FileItem[] = [];
        maintainFiles.forEach((item: any, index: number) => {
          list.push({
            uid: index.toString(),
            name: item.name,
            url: item.value,
          })
        })
        res.data.filesImage = list;
      }

      formDrawer.value.formModel = res.data;
    }
  } catch (ex) {
    console.log("查看详情-报错", ex);
  } finally {
    formDrawer.value.loading = false;
  }
}

/**
 * 表单提交
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    formDrawer.value.loading = true;
    try {
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addMaintenance(formDrawer.value.formModel);
      } else {
        result = await updateMaintenance(formDrawer.value.formModel);
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
 * 设备选中
 * @param record
 */
const handleDeviceSelect = (record: any) => {
  let info: any = formDrawer.value.formModel.deviceIdFor.find(item => item.value == record.deviceSn);
  if (!info) {
    formDrawer.value.formModel.deviceIdFor.push({
      name: record.deviceName,
      value: record.deviceSn
    })
  }
}

/**
 * 设备删除
 * @param val
 */
const handleDeviceDel = (val: number) => {
  formDrawer.value.formModel.deviceIdFor.splice(val, 1);
}

/**
 * 表单-取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
  formRef.value?.resetFields();
}

/**
 * table切换
 */
const handleTabsChange = async () => {
  modalModel.value.pvGroupName = tabsList.value.find((item: any) => item.value == tabsActiveKey.value).key;
  await fetchDeviceData();
  tableDeviceRef.value?.expandAll(true);
}

/**
 * 站点发生改变
 * @param record
 */
const handleStationChange = async (val: any, type?: number) => {
  const info = findById(renderStationData.value, val);
  console.log("=====", info.id)
  console.log("=====", info.deptName)
  
  if (info) {
    formDrawer.value.formModel.stationName = info.deptName;
  }

  if(!type){
    formDrawer.value.formModel.deviceIdFor = [];
  }
}

/**
 * 站点搜索
 * @param searchValue
 * @param nodeData
 */
const filterTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.deptName.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
}

/**
 * 维保人员改变
 */
const handleUserChange = (val: any) => {
  ;
  let list: JsonCommonVo[] = [];
  let userIds: any = formDrawer.value.formModel.userIds;
  if (userIds && userIds.length > 0) {
    renderUserData.value.forEach((item: any) => {
      if (userIds.indexOf(item.id) >= 0) {
        list.push({
          name: item.nickName,
          value: item.id,
        })
      }
    })
  }
  formDrawer.value.formModel.userNameFor = list;
}

/**
 * 选择设备-关闭
 */
const handleModalClose = () => {
  modalModel.value = generateModalModel();
}

/**
 * 获取tab
 */
const fetchTabData = async () => {
  try {
    let res = await getPVTab();
    if (res.code == 200 && res.data && res.data.length > 0) {
      tabsList.value = res.data;
      tabsActiveKey.value = res.data[0].value;
      await handleTabsChange();
    }
  } catch (error) {

  }
}


/**
 * 获取设备列表
 */
const fetchDeviceData = async () => {
  try {
    const res = await listFusionGroup({
      deptId: formDrawer.value.formModel.stationId,
      stationType: formDrawer.value.formModel.stationType,
      groupName: formDrawer.value.formModel.stationType == props.stationType ? modalModel.value.pvGroupName : undefined,
    });
    modalModel.value.renderData = res.data;
    tableDeviceRef.value?.expandAll(true);
  } catch (err) {
    console.log("维保管理-设备列表-错误", err)
  } finally {

  }
}

/**
 * 查询用户数据
 */
const fetchUserData = async () => {
  try {
    const res = await listUserAll({});
    if (res.code = 200) {
      renderUserData.value = res.data;
    }
  } catch (err) {
    console.log("维保管理-用户列表-错误", err)
  } finally {

  }
};

/**
 * 获取站点信息
 */
const fetchStationData = async () => {
  try {
    let res = await getStationList({});
    if (res.code == 200) {
      processSelectableByCompany(res.data)
      renderStationData.value = res.data;
    }
  } catch (e) {
    console.log("维保管理-站点列表错误", e);
  } finally {

  }
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const params = {
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      stationType: searchModel.value.stationType == -100 ? undefined : searchModel.value.stationType,
      maintainTitle: searchModel.value.maintainTitle,
    }

    if (searchModel.value.time) {
      params['params'] = {
        beginTime: dayjs(searchModel.value.time[0]).format("YYYY-MM-DD 00:00:00"),
        endTime: dayjs(searchModel.value.time[1]).format("YYYY-MM-DD 23:59:59")
      }
    }

    const res: any = await listMaintenance(params);
    renderData.value = res.rows;
    pagination.total = res.total;
  } catch (err) {
    console.error("维保记录列表-错误", err)
  } finally {
    setLoading(false);
  }
};
/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
})

</script>
