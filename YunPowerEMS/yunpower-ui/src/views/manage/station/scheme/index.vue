<!--
 * 功能：电价配置
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
                <a-form-item field="electricName" :label="$t('manage.station.scheme.priceName')">
                  <a-input v-model="searchModel.electricName"
                           :placeholder="$t('manage.station.scheme.priceNamePlaceholder')" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-button type="primary" @click="search">
                  <template #icon>
                    <icon-search/>
                  </template>
                  {{ $t('global.search') }}
                </a-button>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

      <!-- 分割线 -->
      <a-divider style="margin-top: 0"/>

      <!-- 按钮 -->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('manage.station.scheme.newStandard') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 -->
      <a-table row-key=id ref="tableRef" :loading="loading" :pagination="pagination" @page-change="onPageChange"
               @page-size-change="onPageSizeChange" :bordered="{ wrapper: true, cell: true }"
               :columns="columns" :data="renderData" :scroll="{ x: 1270 }">
        <!-- 地区 -->
        <template #addressStr="{ record }">
          {{ record.addressStr }}
        </template>
        <!-- 用电分类 -->
        <template #electricGroup="{ record }">
          <dict-tag :options="sys_use_electric_categroy" :value="record.electricGroup"/>
        </template>
        <!-- 用电标准 -->
        <!-- <template #electricStandard="{ record }">
          <dict-tag :options="sys_use_electric_categroy_type" :value="record.electricStandard" />
        </template> -->
        <!-- 电压等级 -->
        <template #voltageLevel="{ record }">
          <dict-tag :options="sys_voltage_level" :value="record.voltageLevel"/>
        </template>
        <!-- 生效年份 -->
        <template #effectYear="{ record }">
          {{ `${record.effectYear || '--'}年` }}
        </template>
        <!-- 状态 -->
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"></stop-flag>
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" status="success" @click="handleConfig(record)">
            {{ $t('manage.station.scheme.configurePrice') }}
          </a-button>
          <a-button size="small" type="text" @click="handleStopFlag(record)"
                    :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : '启用' }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdate(record)">{{ $t('global.edit') }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleRecalculate(record)">
            <template #icon>
              <icon-sync/>
            </template>
            {{ $t('manage.station.scheme.recalculationTariff') }}
          </a-button>
        </template>
      </a-table>

      <!-- 操作弹框 start -->
      <a-modal width="450px" :closable="false" :mask-closable="false" :esc-to-close="false"
               v-model:visible="operateModalModel.visible"
               class="modal-box">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                             style="color: rgb(var(--red-6)); margin-right: 5px"/>
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
          {{ $t('global.confirmTip') }}
        </template>
        <div style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{
            operateModalModel.name
          }}】的数据项？
        </div>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" :disabled="operateModalModel.loading"
                        @click="handleOperateModelCancle">{{ $t('global.cancel') }}
              </a-button>
              <a-button type="primary" :loading="operateModalModel.loading" @click="handleOperateModelOk">
                {{ $t('global.confirm') }}
              </a-button>
            </a-space>
          </div>
        </template>
      </a-modal>


      <!-- 添加右弹层 start -->
      <a-drawer :width="750" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm"
                @cancel="handleFormCancel"
                :mask-closable="false">
        <template #title>
          {{
            formDrawer.formModel.id ? $t('manage.station.scheme.editStandard') : $t('manage.station.scheme.addStandard')
          }}
        </template>
        <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                      :rules="formDrawer.rules">

                <!-- 电价名称 -->
                <a-form-item field="electricName" :label="$t('manage.station.scheme.tariffName')">
                  <a-input v-model="formDrawer.formModel.electricName"/>
                </a-form-item>

                <!-- 站点省市 -->
                <a-form-item field="electricAddressId" v-if="formDrawer.visible" :label="$t('manage.station.scheme.applicableArea')">
                  <a-cascader path-mode @change="addressChange" v-model="formDrawer.formModel.electricAddressId"
                              :virtual-list-props="{ height: 400 }" :options="addressOptions"
                              :placeholder="$t('manage.station.scheme.areaSelect')"
                              :load-more="loadAddressMore"/>
                </a-form-item>

                <!-- 用电分类 -->
                <a-form-item field="electricGroup" :label="$t('manage.station.scheme.electricityType')">
                  <a-select v-model="formDrawer.formModel.electricGroup"
                            :placeholder="$t('manage.station.scheme.electricitySelect')" allow-clear>
                    <a-option v-for="dict in sys_use_electric_categroy" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"/>
                  </a-select>
                </a-form-item>

                <!-- 用电标准 -->
                <a-form-item field="electricStandard" :label="$t('manage.station.scheme.electricityStandard')">
                  <a-select v-model="formDrawer.formModel.electricStandard"
                            :placeholder="$t('manage.station.scheme.standardSelect')" allow-clear>
                    <a-option v-for="dict in sys_use_electric_categroy_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"/>
                  </a-select>
                </a-form-item>

                <!-- 电压等级 -->
                <a-form-item field="voltageLevel" :label="$t('manage.station.scheme.voltageLevel')">
                  <a-select v-model="formDrawer.formModel.voltageLevel"
                            :placeholder="$t('manage.station.scheme.voltageLevelSelect')" allow-clear>
                    <a-option v-for="dict in sys_voltage_level" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"/>
                  </a-select>
                </a-form-item>

                <!-- 生效年份 -->
                <a-form-item field="effectYearDate" :label="$t('manage.station.scheme.effectiveYear')">
                  <component is="a-year-picker" :placeholder="$t('manage.station.scheme.effectiveYearSelect')"
                             format="YYYY" @change="onChange"
                             v-model="formDrawer.formModel.effectYearDate" style="width: 200px;"></component>
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

                <!-- 备注 -->
                <a-form-item field="remark" :label="$t('manage.station.pageConfig.remark')">
                  <a-textarea :placeholder="$t('manage.station.pageConfig.remarksMmaximum')" :max-length="200"
                              v-model="formDrawer.formModel.remark"
                              allow-clear/>
                </a-form-item>

              </a-form>
            </a-col>
          </a-row>
        </a-spin>

      </a-drawer>
      <!-- 添加右弹层 end -->

      <!-- 配置右弹层 start -->
      <a-drawer :width="1160" :visible="formDrawer.visibleConfig" :ok-loading="formDrawer.configLoading"
                @ok="handleConfigSubmitForm"
                @cancel="handleConfigFormCancel" :mask-closable="false">
        <template #title>
          {{ formDrawer.formConfigModel.electricName }}
        </template>
        <a-spin style="width: 100%;height: auto;" :tip="$t('global.loading')" :loading="formDrawer.configLoading">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form auto-label-width :model="formDrawer.formConfigModel" label-align="right"
                      :rules="formDrawer.rules">
                <!-- 生效年份 -->
                <a-form-item field="electricName" :label="$t('manage.station.scheme.effectiveYear')">
                  {{ formDrawer.formConfigModel.effectYear }}
                </a-form-item>
                <!-- 生效年份 -->
                <a-form-item :label="$t('manage.station.scheme.wattHour')" :content-flex="false" :merge-props="false">
                  <a-space direction="vertical" fill :size="16">
                    <!-- 电价配置 -->
                    <a-card v-for="(item, itemIndex) in formDrawer.formConfigModel.configList" class="content"
                            :title="`${$t('manage.station.pageConfig.disposition')}${itemIndex + 1}`">
                      <template #extra>
                        <a-button type="primary" status="danger" @click="handelDelConfig(itemIndex)">
                          {{ $t('global.delete') }}
                        </a-button>
                      </template>
                      <!-- 生效月份 -->
                      <a-form-item :field="`configList[${itemIndex}].effectMonth`"
                                   :label="$t('manage.station.scheme.effectiveMonth')">
                        <a-select :placeholder="$t('manage.station.scheme.effectiveMonthSelect')"
                                  v-model="item.effectMonthList"
                                  @change="handleEffectMonthChange(itemIndex)" multiple allow-clear>
                          <a-option v-for="itemMonth in monthData" :key="itemMonth.id" :value="itemMonth.value"
                                    :disabled="itemMonth.disabled" :label="itemMonth.label"/>
                        </a-select>
                      </a-form-item>
                      <!-- 电价类型 -->
                      <a-form-item :field="`configList[${itemIndex}].priceType`"
                                   :label="$t('manage.station.scheme.priceType')">
                        <a-radio-group v-model="item.priceType">
                          <a-radio v-for="dict in sys_electirc_price_type" :value="Number(dict.value)">
                            {{ dict.label }}
                          </a-radio>
                        </a-radio-group>
                      </a-form-item>
                      <!-- 全天电价 -->
                      <a-form-item v-if="item.priceType == 2" :field="`configList[${itemIndex}].uniformPrice`"
                                   :label="$t('manage.station.scheme.allDay')">
                        <a-input-number v-model="item.uniformPrice" :min="0" :step="1" :precision="2" allow-clear/>
                      </a-form-item>

                      <!-- 单价 -->
                      <a-form-item v-if="item.priceType == 1" :label="$t('manage.station.scheme.setUnitPrice')">
                        <a-space direction="vertical" fill :size="16">
                          <a-space>
                            <a-input-number v-model="item.jianPrice" allow-clear :min="0" :step="1" :precision="4">
                              <template #prefix>尖峰</template>
                              <template #suffix>元</template>
                            </a-input-number>
                            <a-input-number v-model="item.fengPrice" allow-clear :min="0" :step="1" :precision="4">
                              <template #prefix>峰段</template>
                              <template #suffix>元</template>
                            </a-input-number>
                            <a-input-number v-model="item.pingPrice" allow-clear :min="0" :step="1" :precision="4">
                              <template #prefix>平段</template>
                              <template #suffix>元</template>
                            </a-input-number>
                            <a-input-number v-model="item.guPrice" allow-clear :min="0" :step="1" :precision="4">
                              <template #prefix>谷段</template>
                              <template #suffix>元</template>
                            </a-input-number>
                            <a-input-number v-model="item.shenPrice" allow-clear :min="0" :step="1" :precision="4">
                              <template #prefix>深谷</template>
                              <template #suffix>元</template>
                            </a-input-number>
                          </a-space>
                          <a-button type="primary" @click="handleConfigRangeAdd(itemIndex)">
                            <template #icon>
                              <icon-plus/>
                            </template>
                            <!-- 添加时段 -->
                            {{ $t('manage.station.scheme.addPeriod') }}
                          </a-button>
                          <a-table :bordered="{ wrapper: true, cell: true }" :pagination="false"
                                   :data="item.seasonalRange" :columns="rangeColumns">
                            <!-- 开始时间 -->
                            <template #startTime="{ record }">
                              <a-select v-model="record.startTime" allow-clear>
                                <a-option v-for="dict in dayHourData" :key="dict.id" :label="dict.label"
                                          :value="dict.label"></a-option>
                              </a-select>
                            </template>
                            <!-- 结束时间 -->
                            <template #endTime="{ record }">
                              <a-select v-model="record.endTime" allow-clear>
                                <a-option v-for="dict in dayHourData" :key="dict.id" :label="dict.label"
                                          :value="dict.label"></a-option>
                              </a-select>
                            </template>
                            <!-- 峰谷属性 -->
                            <template #seasonalType="{ record, rowIndex }">
                              <a-select v-model="record.seasonalType" allow-clear
                                        @change="handleSeasonalTypeChange(itemIndex, rowIndex)">
                                <a-option v-for="dict in sys_seasonal_type" :key="dict.id" :label="dict.label"
                                          :value="Number(dict.value)"></a-option>
                              </a-select>
                            </template>
                            <!-- 收费价格 -->
                            <template #chargePrice="{ record }">
                              <a-input-number v-model="record.chargePrice" allow-clear :min="0" :step="1"
                                              :precision="4">
                                <template #suffix>元</template>
                              </a-input-number>
                            </template>
                            <template #operate="{ record, rowIndex }">
                              <a-button size="small" type="text" status="danger"
                                        @click="handleConfigRangeDelete(itemIndex, rowIndex)">{{ $t('global.delete') }}
                              </a-button>
                            </template>
                          </a-table>

                        </a-space>

                      </a-form-item>
                    </a-card>
                    <!-- 添加按钮 -->
                    <a-button type="primary" @click="handleConfigAdd">
                      <template #icon>
                        <icon-plus/>
                      </template>
                      <!-- 添加配置 -->
                      {{ $t('manage.station.pageConfig.addconfig') }}
                    </a-button>
                  </a-space>
                </a-form-item>

              </a-form>
            </a-col>
          </a-row>
        </a-spin>
      </a-drawer>
      <!-- 添加右弹层 end -->
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, reactive, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {TableInstance} from "@arco-design/web-vue";
import {BasePagination} from '@/api/common';
import {FormInstance} from '@arco-design/web-vue/es/form';
import {
  getPriceScheme,
  addPriceScheme,
  updatePriceScheme,
  delPriceScheme,
  listPriceScheme,
  changePriceSchemeStatus,
  recalculateSeasonalPriceBySchemeId
} from "@/api/system/price-scheme";
import dayjs from 'dayjs';
import {getAddress, getAddressNameByIds, listAddressAll} from '@/api/system/address';
import {getPriceSchemeConfig, updatePriceSchemeConfig} from '@/api/system/home-power';
import {getDictLabel} from '@/utils/dict';
import {notification} from "@/hooks/my-design";

/*************************** 变量区域 begin ***************************/

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_use_electric_categroy,
  sys_use_electric_categroy_type,
  sys_voltage_level,
  sys_electirc_price_type,
  sys_seasonal_type
} = proxy?.useDict("sys_use_electric_categroy", "sys_use_electric_categroy_type", "sys_voltage_level", "sys_electirc_price_type", "sys_seasonal_type");
//******* 这里编写动态获取下拉列表 end ******

//生成查询条件对象
const generateSearchModel = () => {
  return {
    electricName: "",
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const {loading, setLoading} = useLoading(true);
//地址信息
const addressOptions = ref<any>([]);
//地址加载
const addressLoading = ref<boolean>(false);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
//表格实例
const tableRef = ref<TableInstance>();
//生成月份数据
const generateMonthData = () => {
  const data = [];
  for (let i = 0; i < 12; i++) {
    data.push({
      id: i + 1,
      label: `${i + 1}月份`,
      value: i + 1,
      disabled: false
    });
  }
  return data;
};
//月份数据
const monthData = ref(generateMonthData());
//生成小时段数据
const generateHourData = () => {
  const data = [];
  // 整点
  for (let i = 0; i <= 24; i++) {
    data.push({
      id: i + 1,
      label: `${String(i).padStart(2, "0")}:00`,
    });
  }
  // 半点
  // for (let i = 0; i < 48; i++) {
  //   const hour = Math.floor(i / 2);
  //   const minute = i % 2 === 0 ? "00" : "30";
  //   data.push({
  //     id: i + 1,
  //     label: `${String(hour).padStart(2, "0")}:${minute}`,
  //   });
  // }
  return data;
};
//时间段
const dayHourData = ref(generateHourData());

const rangeColumns = computed<any[]>(() => [
  {
    title: "开始时间",
    dataIndex: "startTime",
    slotName: "startTime",
    align: 'center',
    width: 150,
  },
  {
    title: "结束时间",
    dataIndex: "endTime",
    slotName: "endTime",
    align: 'center',
    width: 150,
  },
  {
    title: "峰谷属性",
    dataIndex: "seasonalType",
    slotName: "seasonalType",
    align: 'center',
    width: 150,
  },
  {
    title: "收费价格",
    dataIndex: "chargePrice",
    slotName: "chargePrice",
    align: 'center',
    width: 180,
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: "operate",
    align: 'center',
    width: 80,
  }
]);

//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    align: 'center',
    width: 80,
    fixed: 'left'
  },
  {
    title: "电价名称",
    dataIndex: "electricName",
    align: 'center',
    width: 200,
    ellipsis: true,
    tooltip: {position: 'top'},
    fixed: 'left'
  },
  {
    title: "地区",
    dataIndex: "addressStr",
    slotName: "addressStr",
    align: 'left',
    width: 300
  },
  {
    title: "用电分类",
    dataIndex: "electricGroup",
    slotName: "electricGroup",
    align: 'center',
    width: 150
  },
  {
    title: "电压等级",
    dataIndex: "voltageLevel",
    slotName: "voltageLevel",
    align: 'center',
    width: 100
  },
  {
    title: "生效年份",
    dataIndex: "effectYear",
    slotName: "effectYear",
    align: 'center',
    width: 100
  },
  {
    title: "状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100,
    fixed: 'right'
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 340,
    slotName: 'operate',
    align: 'center',
    fixed: 'right'
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
    name: "",
    loading: false
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
    visibleConfig: false,
    configLoading: false,
    rules: {
      electricName: [{required: true, message: "请输入电价名称"}],
      electricAddressId: [{required: true, message: "请选择适用地区"}],
      electricGroup: [{required: true, message: "请选择用电分类"}],
      electricStandard: [{required: true, message: "请选择用电标准"}],
      voltageLevel: [{required: true, message: "请选择电压等级"}],
      effectYearDate: [{required: true, message: "请选择生效年份"}]
    },
    formModel: {
      id: 0,
      /**电价名称 */
      electricName: undefined,
      electricAddressId: undefined,
      province: undefined,
      city: undefined,
      county: undefined,
      /**用电分类 */
      electricGroup: undefined,
      /**用电标准 */
      electricStandard: undefined,
      /**电压等级 */
      voltageLevel: undefined,
      /**生效年份 */
      effectYearDate: dayjs().format("YYYY-01-01"),
      effectYear: dayjs().year(),
      stopFlag: 0,
      remark: undefined
    },
    formConfigModel: {
      schemeId: 0,
      id: 0,
      electricName: "",
      effectYear: 0,
      configList: [] as SchemeConfig[]
    }
  };
};

/**
 * 时间段
 */
interface SeasonalRange {
  id?: number;
  endTime?: string;
  startTime?: string;
  chargePrice?: number;
  seasonalName?: string;
  seasonalType?: number;
}

/**
 * 电价配置
 */
interface SchemeConfig {
  /**电价标准ID */
  schemeId?: number;
  /**生效年份 */
  effectYear?: number;
  effectMonthList?: Number[],
  /**生效月份集合 */
  effectMonth?: string;
  /**电价类型：1分时电价 2统一电价 */
  priceType?: number;
  /**全天电价 */
  uniformPrice?: number;
  /**尖峰电价 */
  jianPrice?: number;
  /**峰段电价 */
  fengPrice?: number;
  /**平段电价 */
  pingPrice?: number;
  /**谷段电价 */
  guPrice?: number;
  /**深谷电价 */
  shenPrice?: number;
  /**时间段（JSON格式） */
  seasonalRange: SeasonalRange[],
}

//表单模型
const formDrawer = ref(generateFormDrawerModel());
//switch选中值
const checkedValue = ref<number>(0);
//switch未选中值
const unCheckedValue = ref<number>(1);

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

/**
 * 表格页码发生变化
 * @param val
 */
const onPageChange = async (val: number) => {
  pagination.current = val;
  await fetchData();
}

/**
 * 表格每页数量发生变化
 * @param val 值
 */
const onPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchData();
}

/**
 * 搜索
 */
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

/**
 * 新增数据
 * @param row 数据行
 */
const handleAdd = async () => {
  formDrawer.value.visible = true;
  addressOptions.value = await fetchAddressData(0);
}

const onChange = (dateString: any, date: any) => {
  console.log('onChange: ', dateString, date);
  formDrawer.value.formModel.effectYear = dateString;
}


/**
 * 电价配置
 * @param record
 */
const handleConfig = async (record: any) => {
  formDrawer.value.formConfigModel.schemeId = record.id;
  formDrawer.value.formConfigModel.electricName = record.electricName
  formDrawer.value.formConfigModel.effectYear = record.effectYear;
  formDrawer.value.visibleConfig = true;
  formDrawer.value.configLoading = true;
  try {
    let res = await getPriceSchemeConfig(record.id);
    if (res.code == 200) {
      res.rows.forEach((item: SchemeConfig) => {
        item.effectMonthList = item.effectMonth?.split(",").map(Number);
      });
      formDrawer.value.formConfigModel.configList = res.rows;
    }
  } catch (ex) {

  } finally {
    formDrawer.value.configLoading = false;
  }
}

/**
 * 添加配置
 */
const handleConfigAdd = async () => {
  const newConfig: SchemeConfig = {
    /**电价标准ID */
    schemeId: formDrawer.value.formConfigModel.schemeId,
    /**生效年份 */
    effectYear: formDrawer.value.formConfigModel.effectYear,
    priceType: 2,
    /**全天电价 */
    uniformPrice: 0,
    /**尖峰电价 */
    jianPrice: 0,
    /**峰段电价 */
    fengPrice: 0,
    /**平段电价 */
    pingPrice: 0,
    /**谷段电价 */
    guPrice: 0,
    /**深谷电价 */
    shenPrice: 0,
    seasonalRange: [] as SeasonalRange[]
  };
  formDrawer.value.formConfigModel.configList.push(newConfig);
}

/**
 * 添加时段
 */
const handleConfigRangeAdd = (val: number) => {
  let info = formDrawer.value.formConfigModel.configList[val];
  if (info) {
    info.seasonalRange?.push({
      id: (info.seasonalRange?.length || 0) + 1,
      endTime: undefined,
      startTime: undefined,
      chargePrice: undefined,
      seasonalName: undefined,
      seasonalType: undefined
    });
  }
}

/**
 * 选择月份
 * @param val
 */
const handleEffectMonthChange = (val: number) => {
  let info = formDrawer.value.formConfigModel.configList[val];
  if (info) {
    info.effectMonth = info.effectMonthList?.join(",");
  }
}

/**
 * 删除配置
 * @param val
 */
const handelDelConfig = (val: number) => {
  formDrawer.value.formConfigModel.configList.splice(val, 1);
}

/**
 * 删除时间配置
 * @param val
 */
const handleConfigRangeDelete = (index: number, rangeIndex: number) => {
  let info = formDrawer.value.formConfigModel.configList[index];
  if (info) {
    info.seasonalRange?.splice(rangeIndex, 1);
  }
}

const handleSeasonalTypeChange = (index: number, rangeIndex: number) => {
  let info = formDrawer.value.formConfigModel.configList[index];
  if (info) {
    let configInfo = info.seasonalRange[rangeIndex];
    if (configInfo) {
      configInfo.seasonalName = getDictLabel("sys_seasonal_type", configInfo.seasonalType);
    }
  }
}
/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (record: any) => {
  addressOptions.value = await fetchAddressData(0);
  let result = await getPriceScheme(record.id);
  if (result.code == 200) {
    result.data.effectYearDate = dayjs(`${result.data.effectYear}-01-01`).format("YYYY-MM-DD")
    result.data.electricAddressId = [[await getAddressDetail(result.data.province), await getAddressDetail(result.data.city), await getAddressDetail(result.data.county)]]
    formDrawer.value.formModel = result.data;
    formDrawer.value.visible = true;
  }
}

const getAddressName = async (ids: any) => {
  try {
    let res = await getAddressNameByIds(ids);
    if (res.code == 200) {
      return res.msg;
    }
  } catch (ex) {
    console.log(ex)
  } finally {

  }
}

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

const handleStopFlag = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = record.stopFlag == 0 ? "停用" : "启用";
  operateModalModel.value.name = record.electricName;
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
  operateModalModel.value.name = record.electricName;
  operateModalModel.value.type = 1;
}

const handleRecalculate = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = '重算电价';
  operateModalModel.value.name = record.electricName;
  operateModalModel.value.type = 2;
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
  let result: any = {code: 201};
  operateModalModel.value.visible = false;
  setLoading(true);
  try {
    if (operateModalModel.value.type == 0) {
      result = await changePriceSchemeStatus(operateModalModel.value.id, operateModalModel.value.value);
    } else if (operateModalModel.value.type == 1) {
      result = await delPriceScheme(operateModalModel.value.id);
    } else {
      result = await recalculateSeasonalPriceBySchemeId(operateModalModel.value.id);
    }
  } catch (ex) {
    console.error("handleOperateModelOk error", ex)
  } finally {
    notification(result);
    setLoading(false);
    if (result.code == 200) {
      handleOperateModelCancle();
      await fetchData();
    }
  }
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();

  if (!validate) {
    formDrawer.value.loading = true;
    let result;
    try {
      if (formDrawer.value.formModel.id == 0) {
        result = await addPriceScheme(formDrawer.value.formModel);
      } else {
        result = await updatePriceScheme(formDrawer.value.formModel);
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
 * 配置保存
 */
const handleConfigSubmitForm = async () => {
  formDrawer.value.configLoading = true;
  try {
    let result = await updatePriceSchemeConfig(formDrawer.value.formConfigModel.configList);
    notification(result);
    if (result.code == 200) {
      handleFormCancel();
      await fetchData();
    }
  } catch (e) {
    console.error(e);
  } finally {
    formDrawer.value.configLoading = false;
  }

}

/**
 * 表单取消
 */
const handleConfigFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
}

/**
 * 地址选择
 * @param path
 */
const addressChange = (path: any) => {
  formDrawer.value.formModel.province = path["0"];
  formDrawer.value.formModel.city = path["1"];
  formDrawer.value.formModel.county = path["2"];
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
 * 查询地址
 * @param parentId 父类ID
 */
const fetchAddressData = async (parentId: any) => {
  let data: any = [];
  addressLoading.value = true;
  try {
    const res = await listAddressAll({parentId: parentId});
    if (res.code == 200 && res.data) {
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
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listPriceScheme({
      ...searchModel.value,
    });
    if (res.code == 200 && res.rows) {
      const promises = res.rows.map(async (item: any) => {
        const addressStr = await getAddressName([item.province, item.city, item.county]);
        item.addressStr = addressStr;
        return addressStr;
      });
      const addressStrs = await Promise.all(promises);
      addressStrs.forEach((addressStr: string, index: number) => {
        console.log(res.rows[index].addressStr);
      });
      renderData.value = res.rows;
      pagination.total = res.total;
    }

  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
})
</script>
