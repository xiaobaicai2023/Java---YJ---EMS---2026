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
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left"
                  auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="stationName" :label="$t('manage.system.siteName')">
                  <a-input v-model="searchModel.stationName" :placeholder="$t('manage.system.pleaseInput')"
                           allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-space direction="vertical" :size="18">
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

      <!-- 分割线 -->
      <a-divider style="margin-top: 0"/>

      <!-- 按钮 -->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleGroupAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('global.new') }}{{ $t('manage.system.group') }}
            </a-button>
            <a-button type="primary" @click="handleStationAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('global.new') }}{{ $t('manage.system.site') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table row-key="id" ref="tableRef" :hide-expand-button-on-empty="true" :scroll="{ x: 1890 }"
               :bordered="{ wrapper: true, cell: true }" :loading="loading" :columns="columns" :data="renderData"
               show-page-size>
        <template #stationType="{ record }">
          <dict-tag :options="sys_station_type" :value="record.stationType"/>
        </template>
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"/>
        </template>
        <template #operate="{ record }">
          <a-button v-if="record.groupOrStation === 2" size="small" type="text" @click="handleStopFlagClick(record)"
                    :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success"
                    @click="handleUpdate(record)">
            {{ $t('global.edit') }}
          </a-button>
          <a-button size="small" type="text" status="danger"
                    @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
        </template>
      </a-table>

      <!-- 删除弹框 start -->
      <a-modal v-model:visible="operateModalModel.visible">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                             style="color: rgb(var(--red-6)); margin-right: 5px"/>
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
          {{ $t('global.confirmTip') }}
        </template>
        <a-spin :loading="operateModalModel.loading" style="width: 100%;height: 100%">
          <div style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{
              operateModalModel.name
            }}】的数据项？
          </div>
        </a-spin>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">{{
                  $t('global.cancel')
                }}
              </a-button>
              <a-button type="primary" :loading="operateModalModel.loading" @click="handleOperateModelOk">
                {{ $t('global.confirm') }}
              </a-button>
            </a-space>
          </div>
        </template>
      </a-modal>

      <!-- 添加右弹层 start -->
      <a-drawer :width="700" :visible="formDrawer.groupVisible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm"
                @cancel="handleFormCancel"
                :mask-closable="false">
        <template #title>
          {{ formDrawer.groupTitle }}
        </template>
        <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
          <a-row :gutter="12">
            <a-col :span="24">
              <a-form ref="formGroupRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                      :rules="formDrawer.rules">

                <!-- 分组名称 -->
                <a-form-item field="stationName" :label="$t('manage.station.list.groupName')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.stationName" allow-clear/>
                </a-form-item>

                <!-- 所属企业 -->
                <a-form-item field="entId" :label="$t('manage.station.list.affiliatedEnterprise')">
                  <a-select v-model="formDrawer.formModel.entId" :placeholder="$t('manage.station.list.businessSelect')"
                            allow-clear>
                    <a-option v-for="(item, index) in enterpriseData" :value="item.id">{{ item.entName }}</a-option>
                  </a-select>
                </a-form-item>

                <!-- 显示顺序  -->
                <a-form-item field="orderNum" :label="$t('global.displayOrder')">
                  <a-input-number v-model="formDrawer.formModel.orderNum"/>
                </a-form-item>

                <!-- 启用状态 -->
                <a-form-item field="stopFlag" :label="$t('global.status')">
                  <a-switch :checked-value="stopFlagCheckedValue" :unchecked-value="stopFlagUnCheckedValue"
                            v-model="formDrawer.formModel.stopFlag">
                    <template #checked>{{ $t('global.enable') }}</template>
                    <template
                        #unchecked>{{ $t('global.forbidden') }}
                    </template>
                  </a-switch>
                </a-form-item>

                <!-- 联系人员 -->
                <a-form-item field="linkName" :label="$t('manage.station.list.contactPerson')">
                  <a-input v-model="formDrawer.formModel.linkName" :max-length="32" allow-clear/>
                </a-form-item>

                <!-- 联系电话 -->
                <a-form-item field="linkPhone" :label="$t('manage.station.list.contactNumber')">
                  <a-input v-model="formDrawer.formModel.linkPhone" :max-length="32" allow-clear/>
                </a-form-item>

                <!-- 站点简介 -->
                <a-form-item field="description" :label="$t('manage.station.list.siteProfile')">
                  <a-textarea v-model="formDrawer.formModel.description" allow-clear :max-length="200" :auto-size="true"
                              :show-word-limit="true" :placeholder="$t('manage.station.list.sitePlaceHolder')"/>
                </a-form-item>
              </a-form>
            </a-col>
          </a-row>
        </a-spin>
      </a-drawer>


      <a-drawer :width="1300" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm"
                @cancel="handleFormCancel"
                :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? $t('manage.station.list.editSite') : $t('manage.station.list.addSite') }}
        </template>
        <a-form ref="formRef" :model="formDrawer.formModel" label-align="right" :rules="formDrawer.rules"
                auto-label-width>
          <a-spin style="width:100%;height:100%;" :loading="formDrawer.loading">
            <a-row :gutter="24" class="row-mp-30">
              <a-col :span="12">
                <!-- 站点名称 -->
                <a-form-item field="stationName" :label="$t('manage.station.list.siteName')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.stationName" allow-clear/>
                </a-form-item>

                <!-- 站点编号 -->
                <a-form-item field="stationSn" class="star-from-item" disabled
                             :label="$t('manage.station.list.stationNumber')">
                  <a-input readonly v-model="formDrawer.formModel.stationSn" allow-clear/>
                </a-form-item>

                <!-- 上级目录  -->
                <a-form-item field="parentId" :label="$t('manage.station.list.parentDirectory')">
                  <a-select v-model="formDrawer.formModel.parentId"
                            :placeholder="$t('manage.station.list.directorySelect')" allow-clear
                            @change="parentIdChange">
                    <a-option v-for="(item, index) in renderGroupData" :key="index" :value="Number(item.id)">{{
                        item.stationName
                      }}
                    </a-option>
                  </a-select>
                </a-form-item>
                <!-- 电站类型  -->
                <a-form-item field="stationType" :label="$t('manage.station.list.stationType')">
                  <a-select v-model="formDrawer.formModel.stationType" allow-clear
                            :placeholder="$t('manage.station.list.stationTypeSelect')">
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 光伏类型-->
                <a-form-item field="pvType" v-if="formDrawer.formModel.stationType==2"
                             :label="$t('manage.station.list.pvType')">
                  <a-select v-model="formDrawer.formModel.pvType" allow-clear
                            :placeholder="$t('manage.station.list.pvTypeSelect')">
                    <a-option v-for="dict in sys_pv_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 建站时间 -->
                <a-form-item field="buildSiteTime" :label="$t('manage.station.list.stationTime')">
                  <a-date-picker v-model="formDrawer.formModel.buildSiteTime"
                                 :placeholder="$t('manage.station.list.stationTimeSelect')"/>
                </a-form-item>

                <!-- 投运时间 -->
                <a-form-item field="useSiteTime" :label="$t('manage.station.list.operationalTime')">
                  <a-date-picker v-model="formDrawer.formModel.useSiteTime"
                                 :placeholder="$t('manage.station.list.launchTimeSelect')" allow-clear/>
                </a-form-item>

                <!-- 电压等级 -->
                <a-form-item field="voltageLevel" :label="$t('manage.station.list.voltageLevel')">
                  <a-input-number v-model="formDrawer.formModel.voltageLevel" allow-clear>
                    <template #append>
                      kV
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 额定容量 -->
                <a-form-item field="capacityKva" :label="$t('manage.station.list.ratedCapacity')">
                  <a-input-number v-model="formDrawer.formModel.capacityKva" allow-clear>
                    <template #append>
                      kVa
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 设备上限 -->
                <a-form-item field="deviceLimit" :label="$t('manage.station.list.limit')">
                  <a-input-number v-model="formDrawer.formModel.deviceLimit" allow-clear>
                  </a-input-number>
                </a-form-item>

                <!-- 测点上限 -->
                <a-form-item field="varLimit" :label="$t('manage.station.list.station')">
                  <a-input-number v-model="formDrawer.formModel.varLimit" allow-clear>
                  </a-input-number>
                </a-form-item>

                <!-- 报警开关  -->
                <a-form-item field="openAlarm" :label="$t('manage.station.list.alarmSwitch')">
                  <template #extra>
                  <span>
                    <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                    <!-- 站点报警总开关 -->
                    {{ $t('manage.station.list.alarmFlag') }}
                  </span>
                  </template>
                  <a-switch :checked-value="alarmCheckedValue" :unchecked-value="alarmUnCheckedValue"
                            v-model="formDrawer.formModel.openAlarm">
                    <template #checked>{{ $t('global.on') }}</template>
                    <template
                        #unchecked>{{ $t('global.off') }}
                    </template>
                  </a-switch>
                </a-form-item>

                <!-- 是否停用 -->
                <a-form-item field="stopFlag" :label="$t('manage.station.list.disableOrNot')">
                  <a-switch :checked-value="stopFlagCheckedValue" :unchecked-value="stopFlagUnCheckedValue"
                            v-model="formDrawer.formModel.stopFlag">
                    <template #checked>{{ $t('global.enable') }}</template>
                    <template
                        #unchecked>{{ $t('global.forbidden') }}
                    </template>
                  </a-switch>
                </a-form-item>

                <!-- 站点照片 -->
                <a-form-item field="picUrl" :label="$t('manage.station.list.sitePhoto')">
                  <a-upload response-url-key="url" :limit="1" list-type="picture-card" :file-list="formDrawer.fileList"
                            image-preview
                            :action="(`${baseUrl}/file/upload`)"
                            @success="handleUploadSuccess"
                            @beforeRemove="handleRemove"
                            :headers="headers"/>
                </a-form-item>

                <!-- 联系人员 -->
                <a-form-item field="linkName" :label="$t('manage.station.list.contactPerson')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.linkName" :max-length="32" allow-clear/>
                </a-form-item>

                <!-- 联系电话 -->
                <a-form-item field="linkPhone" :label="$t('manage.station.list.contactNumber')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.linkPhone" :max-length="32" allow-clear/>
                </a-form-item>

                <!-- 站点简介 -->
                <a-form-item field="description" :label="$t('manage.station.list.siteProfile')">
                  <a-textarea v-model="formDrawer.formModel.description" allow-clear :max-length="200" :auto-size="true"
                              :show-word-limit="true" :placeholder="$t('manage.station.list.sitePlaceHolder')"/>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-divider orientation="left">{{ $t('manage.station.list.information') }}</a-divider>

                <!-- 站点省市 -->
                <a-form-item field="province" :label="$t('manage.station.list.stationProvince')">
                  <a-cascader path-mode @change="addressChange" v-model="formDrawer.formModel.stationAddressId"
                              :virtual-list-props="{ height: 400 }" :options="addressOptions"
                              placeholder="请选择站点省市"
                              :load-more="loadAddressMore"/>
                </a-form-item>

                <!-- 详细地址 -->
                <a-form-item field="stationAddress" :label="$t('manage.station.list.detailedAddress')">
                  <a-input v-model="formDrawer.formModel.stationAddress" allow-clear/>
                </a-form-item>

                <!-- 电站坐标 -->
                <a-form-item field="coordinate" :label="$t('manage.station.list.coordinates')">
                  <a-input-search v-model="formDrawer.formModel.coordinate" @search="handleSelectLocation"
                                  search-button>
                    <template #button-icon>
                      <icon-location/>
                    </template>
                    <template #button-default>
                      <!-- 定位 -->
                      {{ $t('manage.station.list.orientation') }}
                    </template>
                  </a-input-search>
                </a-form-item>

                <template v-if="formDrawer.formModel.stationType==2">
                  <a-divider orientation="left" style="margin-top: 40px;">{{ $t('manage.station.list.pvConfig') }}
                  </a-divider>
                  <!-- 装机容量  -->
                  <a-form-item field="sationVolume" :label="$t('manage.station.list.capacity')"
                               :tooltip="$t('manage.station.list.capacityPlaceholder')">
                    <a-input-number v-model="formDrawer.formModel.sationVolume">
                      <template #append>
                        kVA
                      </template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 方位角度 -->
                  <a-form-item field="azimuth" :label="$t('manage.station.list.azimuth')"
                               :tooltip="$t('manage.station.list.azimuthPlaceholder')"
                               :validate-trigger="['change', 'blur', 'input']">
                    <a-input-number v-model="formDrawer.formModel.azimuth" allow-clear>
                      <template #append>
                        °
                      </template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 组件倾角 -->
                  <a-form-item field="dipAngle" :label="$t('manage.station.list.inclination')"
                               :tooltip="$t('manage.station.list.inclinationPlaceholder')"
                               :validate-trigger="['change', 'blur', 'input']">
                    <a-input-number v-model="formDrawer.formModel.dipAngle" allow-clear>
                      <template #append>
                        °
                      </template>
                    </a-input-number>
                  </a-form-item>
                </template>

                <a-divider orientation="left" style="margin-top: 40px;">{{ $t('manage.station.list.otherConfig') }}
                </a-divider>
                <!-- 电价标准 -->
                <a-form-item field="schemeId" :label="$t('manage.station.list.priceStandard')">
                  <a-select v-model="formDrawer.formModel.schemeId"
                            :placeholder="$t('manage.station.list.tariffSelect')"
                            allow-clear>
                    <a-option v-for="(item, index) in renderPriceSchemeData" :key="index" :value="Number(item.id)">{{
                        item.electricName
                      }}
                    </a-option>
                  </a-select>
                </a-form-item>

              </a-col>
            </a-row>
          </a-spin>
        </a-form>
      </a-drawer>
      <a-modal :width="1200" :title="$t('manage.station.list.picking')" v-model:visible="mapVisible"
               @ok="handleMapSuccess" @cancel="handleMapCancel">
        <a-space direction="vertical" :size="16" style="display: block;">
          <a-row justify="center">
            <a-col :span="12">
              <a-space>
                <a-input id="tipinput" v-model="mapSearchName"
                         :placeholder="$t('manage.station.list.keywordsPlaceholder')" allow-clear/>
                <a-button type="primary" @click="mapSearch">
                  <template #icon>
                    <icon-search/>
                  </template>
                  {{ $t('global.search') }}
                </a-button>
                <div>
                  {{ $t('manage.station.list.coordinatesResult') }}{{ mapLocation }}
                </div>
              </a-space>
            </a-col>
          </a-row>
          <a-row justify="center">
            <a-col>
              <div id="container">
              </div>
              <div id="divCoordinate" style="display: none;"></div>
            </a-col>
          </a-row>
        </a-space>
      </a-modal>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, nextTick, onMounted, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {FileItem, TableColumnData, TableInstance} from "@arco-design/web-vue";
import {
  listStation,
  getStation,
  addStation,
  updateStation,
  listStationAll,
  changeStationStatus, delStation
} from "@/api/system/station";
import {listEnterprise} from "@/api/system/enterprise";
import {FormInstance} from '@arco-design/web-vue/es/form';
import {getToken} from '@/utils/auth';
import {getAddress, listAddressAll} from '@/api/system/address'
import {listPriceSchemeAll} from '@/api/system/price-scheme';
import AMapLoader from "@amap/amap-jsapi-loader";
import {Message} from "@arco-design/web-vue";
import {notification} from "@/hooks/my-design";
import {getStationList} from "@/api/public";
import {useAppStore, useCompanyStore, useUserStore} from "@/store";
import {storeToRefs} from "pinia";

/*************************** 变量区域 begin ***************************/
//地图
window._AMapSecurityConfig = {
  securityJsCode: import.meta.env.VITE_AMAP_API_CODE,
};
const mapSearchName = ref<any>("");
const mapLocation = ref<any>("");
let map: any = null;
let placeSearch: any = null
let geolocation: any = null
let citysearch: any = null
let cityCode: any = ref<any>('');
let cityName: any = ref<any>('');
let divCoordinate: any = null;

//生成查询条件对象
const generateSearchModel = () => {
  return {
    stationName: ''
  };
};

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_normal_disable,
  sys_station_type,
  sys_pv_type
} = proxy?.useDict("sys_normal_disable", "sys_station_type", "sys_pv_type");
//******* 这里编写动态获取下拉列表 end ******

//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const {loading, setLoading} = useLoading(true);
//设置表格列
const columns = computed<TableColumnData[]>(() => [

  {
    title: "电站名称",
    dataIndex: 'stationName',
    width: 300,
    fixed: "left"
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
    align: 'center',
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
    title: "方位角度",
    dataIndex: 'azimuth',
    align: "center",
    width: 150
  },
  {
    title: "组件倾角",
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
    width: 180,
    fixed: "right"
  }
]);

//表格实例
const tableRef = ref<TableInstance>();
//表格数据
const renderData = ref<any[]>([]);
//分组数据
const renderGroupData = ref<any[]>([]);
//企业
const enterpriseData = ref<any[]>([]);
//地址信息
const addressOptions = ref<any>([])
//地址加载
const addressLoading = ref<boolean>(false);
//电价
const renderPriceSchemeData = ref<any[]>([]);
//地图显示
const mapVisible = ref<any>(false);
//删除弹框
const generateOperateModalModel = () => {
  return {
    //0 状态 1删除
    type: 0,
    visible: false,
    title: "",
    id: 0,
    value: 0,
    name: "",
    loading: false,
  };
};

//删除弹框模型
const operateModalModel = ref(generateOperateModalModel());


const formRef = ref<FormInstance>();
const formGroupRef = ref<FormInstance>();
//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    loading: false,
    groupVisible: false,
    groupTitle: '',
    rules: {
      entId: [{required: true, message: '请选择企业'}],
      stationName: [{required: true, message: '请输入站点名称'}],
      // stationSn: [{required: true, message: ''}],
      parentId: [{required: true, message: '请选择上级目录'}],
      stationType: [{required: true, message: '请选择电站类型'}],
    },
    fileList: [],
    formModel: {
      id: 0,
      entId: '',
      parentId: 0,
      groupId: '',
      stationName: '',
      stationSn: '',
      groupOrStation: 1,
      stationType: '',
      pvType: undefined,
      logicCode: '',
      buildSiteTime: '',
      voltageLevel: 0,
      capacityKva: 0,
      sationVolume: 0,
      deviceLimit: 0, //设备上限
      varLimit: 0, //测点上限
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
      stopFlag: 0,
      schemeId: 0,
      deleteFlag: 0
    }
  };
};

//报警switch选中值
const alarmCheckedValue = ref<number>(1);
const alarmUnCheckedValue = ref<number>(0);
// 状态选中值
const stopFlagCheckedValue = ref<number>(0);
const stopFlagUnCheckedValue = ref<number>(1);

//表单模型
const formDrawer = ref(generateFormDrawerModel());

const companyStore = useCompanyStore();
const {setCompanyList, updateCompanyValue} = companyStore; // 设置机构

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

const handleMapSuccess = () => {
  formDrawer.value.formModel.coordinate = mapLocation.value;
  handleMapCancel();
  map?.destroy();
}

const handleMapCancel = () => {
  mapVisible.value = false;
}

//重置查询条件
const search = async () => {
  await fetchData();
}

const mapSearch = () => {
  if (loading.value) {
    Message.success("定位中..." as string)
    return;
  }
  placeSearch.search(mapSearchName.value);
}

const handleSelectLocation = (value: string, ev: MouseEvent) => {
  mapVisible.value = true;
  AMapLoader.load({
    key: import.meta.env.VITE_AMAP_API_KEY, // 申请好的Web端开发者Key，首次调用 load 时必填
    version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: ["AMap.PlaceSearch", "AMap.Geolocation", "AMap.CitySearch", 'AMap.AutoComplete'], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
  })
      .then((AMap) => {

        map = new AMap.Map("container", {
          // 设置地图容器id
          viewMode: "2D", // 是否为3D地图模式
          zoom: 11, // 初始化地图级别
          resizeEnable: true
        });
        map.setDefaultCursor("pointer");

        //定位
        geolocation = new AMap.Geolocation({
          enableHighAccuracy: true,//是否使用高精度定位，默认:true
          timeout: 10000,          //超过10秒后停止定位，默认：5s
          position: 'RB',    //定位按钮的停靠位置
          offset: [10, 20], //定位按钮与设置的停靠位置的偏移量，默认：[10, 20]
          zoomToAccuracy: true,   //定位成功后是否自动调整地图视野到定位点

        });
        map.addControl(geolocation);

        //城市
        citysearch = new AMap.CitySearch();
        geolocation.getCurrentPosition(function (status: any, result: any) {

        });

        citysearch.getLocalCity(function (status: any, result: any) {
          if (status === 'complete' && result.info === 'OK') {
            if (result && result.city && result.bounds) {
              var cityinfo = result.city;
              var citybounds = result.bounds;
              cityCode.value = result.infocode;
              cityName.value = cityinfo;
              // document.getElementById('info').innerHTML = '您当前所在城市：' + cityinfo;
              //地图显示当前城市
              map.setBounds(citybounds);
              loading.value = false
              placeSearch = new AMap.PlaceSearch({
                map: map,
                autoFitView: true // 是否自动调整地图视野使绘制的 Marker点都处于视口的可见范围
              });  //构造地点查询类

              divCoordinate = document.getElementById('divCoordinate');
              map.on('mousemove', function (e: any) {
                divCoordinate.style.display = 'block';
                divCoordinate.style.left = e.originEvent.pixel.x + 50 + 'px';
                divCoordinate.style.top = e.originEvent.pixel.y + 70 + 'px';
                let str = `${e.lnglat.lng},${e.lnglat.lat}`;
                // location.value = str;
                divCoordinate.innerHTML = str;
              });
              map.on('mouseout', function (e: any) {
                divCoordinate.style.display = 'none';
              });
              map.on('click', function (e: any) {
                let str = `${e.lnglat.lng},${e.lnglat.lat}`;
                mapLocation.value = str;
              });

              AMap.Event.addListener(placeSearch, 'markerClick', function (marker: any) {
                let location = marker.data.location;
                let str = `${location.lng},${location.lat}`;
                mapLocation.value = str;
              });
            }
          } else {
            // document.getElementById('info').innerHTML = result.info;
          }
        });

      })
      .catch((e) => {
        console.log(e);
      });
}
/**
 * 新增分组数据
 * @param row 数据行
 */
const handleGroupAdd = async () => {
  formDrawer.value = generateFormDrawerModel();
  formDrawer.value.groupVisible = true;
  formDrawer.value.formModel.groupOrStation = 1;
  formDrawer.value.formModel.stationType = "-1";
  formDrawer.value.formModel.parentId = 0;
  formDrawer.value.groupTitle = '新建';
  await fetchEnterpriseData()
}

/**
 * 新增电站数据
 * @param row 数据行
 */
const handleStationAdd = async () => {
  formDrawer.value = generateFormDrawerModel();
  formDrawer.value.visible = true;
  formDrawer.value.formModel.parentId = undefined;
  formDrawer.value.formModel.groupOrStation = 2;
  await fetchGroupData();
  await fetchPriceSchemeData();
  addressOptions.value = await fetchAddressData(0);
}

/**
 * 上传成功
 */
const handleUploadSuccess = (fileItem: FileItem) => {
  let res = fileItem.response;
  if (res && res.code == 200) {
    formDrawer.value.formModel.picUrl = res.data.url;
  }
}

const handleRemove = (fileItem: FileItem) => {
  formDrawer.value.fileList = [];
  formDrawer.value.formModel.picUrl = '';
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
  operateModalModel.value.visible = false;
  setLoading(true);
  try {
    if (operateModalModel.value.type == 1) {
      result = await delStation(operateModalModel.value.id);
    } else {
      result = await changeStationStatus(operateModalModel.value.id, operateModalModel.value.value);
    }
    notification(result);

    if (result.code == 200) {
      handleOperateModelCancle();
      await fetchData();
      setTimeout(async () => {
        await setCompanyList();
      }, 1000 * 3)
    }
  } catch (e) {
    console.error(e);
  } finally {
    setLoading(false);
  }

}


/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  if (record.groupOrStation !== 2) {
    await fetchEnterpriseData();
  }
  await fetchGroupData();
  await fetchPriceSchemeData();
  addressOptions.value = await fetchAddressData(0);
  let result = await getStation(record.id);
  if (result.code == 200) {
    result.data.stationType += "";
    result.data.parentId = result.data.parentId ? result.data.parentId : undefined;
    result.data.schemeId = result.data.schemeId ? result.data.schemeId : undefined;
    if (result.data.voltageLevel && typeof (result.data.voltageLevel) == 'string') {
      result.data.voltageLevel = parseFloat(result.data.voltageLevel);
    }
    formDrawer.value.formModel = result.data;
    formDrawer.value.formModel.stationAddressId = [[await getAddressDetail(result.data.province), await getAddressDetail(result.data.city), await getAddressDetail(result.data.county)]]
    // formDrawer.value.visible = true;
    if (record.groupOrStation === 2) {
      formDrawer.value.visible = true;
    } else {
      formDrawer.value.groupVisible = true;
      formDrawer.value.groupTitle = '编辑';
    }
    let img: any = [{
      name: result.data.stationName,
      url: result.data.picUrl
    }]
    formDrawer.value.fileList = result.data.picUrl ? img : [];
  }
}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = '删除';
  operateModalModel.value.name = record.stationName;
  operateModalModel.value.type = 1;
  operateModalModel.value.visible = true;
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  let validate;
  if (formDrawer.value.formModel.groupOrStation == 1) {
    validate = await formGroupRef.value?.validate();
  } else {
    validate = await formRef.value?.validate();
  }
  if (!validate) {
    formDrawer.value.loading = true;
    try {
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addStation(formDrawer.value.formModel);
      } else {
        result = await updateStation(formDrawer.value.formModel);
      }
      notification(result);
      if (result.code == 200) {
        await fetchData();

        setTimeout(async () => {
          await setCompanyList();
        }, 1000 * 30)

        formDrawer.value = generateFormDrawerModel();
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
  formGroupRef.value?.resetFields();
  formRef.value?.resetFields();
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
    renderData.value = [];
    if (res.code == 200) {
      // let groupStation = res.data.filter((item: any) => item.groupOrStation === 1)
      // if (groupStation && groupStation.length > 0) {
      //   let data: any = [];
      //   groupStation.forEach((item: any) => {
      //     let childrenList = res.data.filter((itemC: any) => itemC.groupId === item.id)
      //     data.push({
      //       id: item.id,
      //       stationName: item.stationName,
      //       children: childrenList
      //     })
      //   })
      //   renderData.value = data;
      // }
      renderData.value = res.data;
      // renderData.value = res.data;
      tableRef.value?.expandAll(true);
    } else {
      renderData.value = [];
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

const fetchPriceSchemeData = async () => {
  try {
    const res = await listPriceSchemeAll({
      // effectYear: '2024'
    });
    if (res.code == 200 && res.data && res.data.length > 0) {
      renderPriceSchemeData.value = res.data;
    }

  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
}

/**
 * 查询公司列表
 */
const fetchEnterpriseData = async () => {
  try {
    const res = await listEnterprise({pageSize: 1000});
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
    const res = await listAddressAll({parentId: parentId});
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

<style scoped lang="less">
#container {
  width: 100%;
  height: 600px;
}

#panel {
  max-height: 90%;
  width: 100%;
}

#divCoordinate {
  display: none;
  position: absolute;
  height: 26px;
  line-height: 26px;
  padding: 0 10px;
  border: 1px solid #999;
  background: #fff;
  z-index: 99;
  color: #666;
}

.star-from-item {
  :deep(.arco-form-item-label)::before {
    content: '*';
    color: red;
    position: relative;
    left: -2px;
    top: 2px;
    font-weight: bold;
  }
}
</style>
