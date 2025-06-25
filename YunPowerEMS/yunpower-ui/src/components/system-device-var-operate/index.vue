<template>
  <a-card class="container">
    <!-- 表单 -->
    <a-spin :loading="loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
      <a-form ref="formRef" :model="formData" :rules="formRule" auto-label-width>
        <a-space direction="vertical" :size="16">
          <a-row :gutter="24">
            <a-col :span="12">
              <a-divider orientation="left">{{ $t('power.energy.device.varInfo') }}</a-divider>
              <!-- 变量名称 -->
              <a-form-item field="varName" :label="$t('power.energy.device.varName')">
                <a-input v-model="formData.varName" allow-clear/>
              </a-form-item>

              <!-- 变量编号 -->
              <a-form-item field="varSn" :label="$t('power.energy.device.varCode')">
                <a-select :disabled="formData.id > 0 ? true:false" v-model="formData.varSn" :options="sys_stat_var_sn_code"
                          :placeholder="$t('power.energy.device.selectVarCode')"/>
              </a-form-item>

              <!-- 用电设备 -->
              <a-form-item field="deviceKey" :label="$t('power.energy.device.electricityDevice')">
                <a-tree-select allow-search v-model="formData.deviceKey" :data="deviceData"
                               :placeholder="$t('power.energy.device.selectElectricityDevice')"
                               @change="handleDeviceChange" @clear="handleDeviceClear" :fieldNames="{
                    key: 'key', title: 'deviceName', children: 'children'
                  }" allow-clear></a-tree-select>
              </a-form-item>

              <!-- 变量属性 -->
              <a-form-item field="varMapId" :label="$t('power.energy.device.varAttribute')">
                <a-cascader v-model="formData.varMapId" :options="varMapSnData" expand-trigger="hover" allow-search
                            @change="handleVarMapChange" @clear="handleVarMapClear"
                            :placeholder="$t('power.energy.device.selectVarAttribute')"/>
              </a-form-item>
              <!-- 变量单位 -->
              <a-form-item field="unit" :label="$t('power.energy.device.varUnit')">
                <a-input v-model="formData.unit" disabled/>
              </a-form-item>

              <!-- 变量类型 -->
              <a-form-item field="variableType" :label="$t('power.energy.device.varType')">
                <a-select v-model="formData.variableType" :placeholder="$t('power.energy.device.selectVarType')"
                          allow-clear>
                  <a-option v-for="dict in sys_config_variable_type" :key="dict.value" :label="dict.label"
                            :value="Number(dict.value)"></a-option>
                </a-select>
              </a-form-item>


              <!-- 是否监控 -->
              <!--              <a-form-item field="isMonitor" :label="$t('power.energy.device.isMonitor')">-->
              <!--                <a-switch :checked-value="1" :unchecked-value="0" v-model="formData.isMonitor">-->
              <!--                  <template #checked>是</template><template #unchecked>否</template></a-switch>-->
              <!--              </a-form-item>-->

              <!-- 启用状态 -->
              <a-form-item field="stopFlag" :label="$t('global.status')">
                <a-switch :checked-value="0" :unchecked-value="1" v-model="formData.stopFlag">
                  <template #checked>{{ $t('global.enable') }}</template>
                  <template #unchecked>{{ $t('global.forbidden') }}</template>
                </a-switch>
              </a-form-item>

              <a-divider orientation="left" style="margin-top: 40px;">{{
                  $t('power.energy.device.dataSource')
                }}
              </a-divider>
              <!-- 来源类型 -->
              <a-form-item field="origin" :label="$t('power.energy.device.sourceType')" @change="handleOriginChange">
                <!-- 1IO型 2内存型 -->
                <a-radio-group v-model="formData.origin">
                  <a-radio v-for="dict in sys_data_origin" :key="dict.value" :value="Number(dict.value)">{{
                      dict.label
                    }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <div v-if="formData.origin == 1">
                <!-- 通讯设备 -->
                <a-form-item field="comDeviceId" :label="$t('power.energy.device.communication')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <!-- <a-select v-model="formData.comDeviceId" :options="channelDeviceData" @clear="handleChannelDeviceClear"
                    @change="handleChannelDeviceChange" placeholder="请选择通讯设备" allow-clear
                    :fieldNames="{ value: 'id', label: 'deviceName' }">
                  </a-select> -->
                  <a-input v-model="formData.comDeviceName" disabled/>
                </a-form-item>
                <!-- 数据区域 -->
                <!-- <a-form-item field="areaName" label="数据区域" :validate-trigger="['change', 'blur', 'input']">
                  <a-select v-model="formData.areaName" :options="channelDeviceAreaData" placeholder="请选择数据区域"
                    :fieldNames="{ value: 'areaName', label: 'areaName' }" allow-clear
                    @change="handleDeviceAreaDataChange">
                  </a-select>
                </a-form-item> -->
                <!--   -->
                <a-form-item v-if="formData.connectModel=='IEC104'" field="messageAddress"
                             :label="$t('power.energy.device.infoAddress')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input-number v-model="formData.messageAddress"/>
                </a-form-item>
                <!--寄存器地址-->
                <a-form-item v-else-if="formData.connectModel=='MODBUSTCP' || formData.connectModel== 'MODBUSRTU'"
                             field="registerAddress" :label="$t('power.energy.device.registerAddress')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input-number v-model="formData.registerAddress"/>
                </a-form-item>
                <a-form-item v-else-if="formData.connectModel=='DLT645' ||  formData.connectModel== 'TianGang'"
                             field="dataAddress" :label="$t('power.energy.device.dataIdentifier')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-select v-model="formData.dataAddress" v-if="formData.connectModel=='DLT645'"
                            :placeholder="$t('global.pleaseSelect')" allow-clear>
                    <a-option v-for="dict in sys_config_variable_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"></a-option>
                  </a-select>
                  <a-input v-if="formData.connectModel=='TianGang'" v-model="formData.dataAddress"
                           :placeholder="$t('global.please')"/>
                </a-form-item>
                <!-- 读写类型 -->
                <a-form-item field="rw" :label="$t('power.energy.device.readWriteType')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <!-- 1只读 2只写 3读写 -->
                  <!-- <a-radio-group v-model="formData.rw">
                    <a-radio v-for="dict in sys_rw" disabled :key="dict.value" :value="Number(dict.value)">{{ dict.label
                    }}</a-radio>
                  </a-radio-group> -->
                  <a-radio-group v-model="formData.rw">
                    <a-radio v-for="dict in sys_rw" :key="dict.value" :value="Number(dict.value)">{{
                        dict.label
                      }}
                    </a-radio>
                  </a-radio-group>
                </a-form-item>
              </div>
              <div v-else>
                <!-- 计算公式 -->
                <a-form-item field="computeFormula" :label="$t('power.energy.device.formula')">
                  <a-grid :cols="5" :colGap="20" :rowGap="5" class="grid-calculator">
                    <a-grid-item :span="{ xl: 5 }" suffix>
                      <a-textarea v-model="computeFormulaStr" allow-clear :auto-size="true"/>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button type="outline" status="success" @click="handleVarTypeSelect(1)">
                        {{ $t('pv.alarm.category.analog') }}
                      </a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('+')">+</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('-')">-</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('*')">*</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('/')">/</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button type="outline" status="success" @click="handleVarTypeSelect(2)">
                        {{ $t('pv.alarm.category.state') }}
                      </a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('7')">7</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('8')">8</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('9')">9</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('0')">0</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button type="primary" status="danger" @click="handleCalculatorClear">
                        {{ $t('messageBox.tab.button') }}
                      </a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('4')">4</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('5')">5</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('6')">6</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button status="danger" @click="handleCalculatorCancle">{{ $t('global.cancel') }}</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button type="primary" status="warning" @click="handleCalculatorBack">回退</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('1')">1</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('2')">2</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button @click="handleCalculatorInput('3')">3</a-button>
                    </a-grid-item>
                    <a-grid-item class="calculator-item">
                      <a-button type="primary" @click="handleCalculatorOk">{{ $t('global.confirm') }}</a-button>
                    </a-grid-item>
                  </a-grid>
                </a-form-item>
                <!-- 计算公式 -->
<!--                <a-form-item field="deletionHandle" :label="$t('power.energy.device.missingValue')">-->
                  <!-- 1不计算 2使用最近值 3使用0值 4使用1值 -->
<!--                  <a-radio-group v-model="formData.deletionHandle">-->
<!--                    <a-radio v-for="dict in sys_deletion_type" :key="dict.value" :value="Number(dict.value)">{{-->
<!--                        dict.label-->
<!--                      }}-->
<!--                    </a-radio>-->
<!--                  </a-radio-group>-->
<!--                </a-form-item>-->
                <!-- 零值计算 -->
<!--                <a-form-item field="zeroCompute" :label="$t('power.energy.device.calculation')">-->
<!--                  &lt;!&ndash; 0不计算 1计算 &ndash;&gt;-->
<!--                  <a-radio-group v-model="formData.zeroCompute">-->
<!--                    <a-radio :value="0">不计算</a-radio>-->
<!--                    <a-radio :value="1">计算</a-radio>-->
<!--                  </a-radio-group>-->
<!--                </a-form-item>-->
<!--                &lt;!&ndash; 补充当前数据 &ndash;&gt;-->
<!--                <a-form-item field="repairData" :label="$t('power.energy.device.supplement')">-->
<!--                  <template #extra>-->
<!--                  <span :style="{ color: 'rgb(var(&#45;&#45;orange-7))', paddingLeft:'4px'}">-->
<!--                    <i class="icon" :style="{ color: 'rgb(var(&#45;&#45;orange-5))' }"><icon-exclamation-circle-fill/></i>-->
<!--                    &lt;!&ndash; 站点报警总开关 &ndash;&gt;-->
<!--                    如果逆变器停止发电后上传0值时，需要选择：零值计算（计算），补当前数据（补前一条）其它情况下不选-->
<!--                  </span>-->
<!--                  </template>-->

<!--                  &lt;!&ndash; 0不补 1补前一条 &ndash;&gt;-->
<!--                  <a-radio-group v-model="formData.repairData">-->
<!--                    <a-radio :value="0">不补</a-radio>-->
<!--                    <a-radio :value="1">补前一条</a-radio>-->
<!--                  </a-radio-group>-->
<!--                </a-form-item>-->
              </div>
            </a-col>
            <a-col :span="12">

              <a-divider orientation="left">{{ $t('power.energy.device.dataProcessing') }}</a-divider>
              <!-- 数据类型 -->
              <a-form-item field="dataType" :label="$t('power.energy.device.dataType')">
                <a-select v-model="formData.dataType" :placeholder="$t('power.energy.device.selectDataType')"
                          allow-clear>
                  <a-option v-for="dict in sys_data_type" :key="dict.value" :label="dict.label"
                            :value="Number(dict.value)"></a-option>
                </a-select>
              </a-form-item>

              <!-- 初始赋值 -->
              <a-form-item field="initValue" :label="$t('power.energy.device.inAssignment')">
                <a-input-number v-model="formData.initValue" allow-clear
                                :placeholder="$t('power.energy.device.pleaseNum')">
                </a-input-number>
              </a-form-item>

              <!-- 数据基值 -->
              <a-form-item field="baseValue" :label="$t('power.energy.device.dataBaseValue')">
                <a-input-number v-model="formData.baseValue" allow-clear
                                :placeholder="$t('power.energy.device.pleaseNum')">
                </a-input-number>
              </a-form-item>

              <!-- 数据系数 -->
              <a-form-item field="coefficient" :label="$t('power.energy.device.dataCoefficient')">
                <a-input-number v-model="formData.coefficient" allow-clear placeholder="请输入数字">
                </a-input-number>
              </a-form-item>

              <!-- 存盘周期 -->
              <a-form-item field="saveCycle" :label="$t('power.energy.device.savePeriod')">
                <a-select v-model="formData.saveCycle" :placeholder="$t('power.energy.device.selectSavePeriod')"
                          allow-clear>
                  <a-option v-for="dict in sys_save_cycle" :key="dict.value" :label="dict.label"
                            :value="Number(dict.value)"></a-option>
                </a-select>
              </a-form-item>

              <!-- 数据修正 -->
              <a-form-item field="dataFix" v-if="stationType == 2" :label="$t('power.energy.device.dataFix')">
                <template #extra>
                <span :style="{ color: 'rgb(var(--orange-7))', paddingLeft:'4px'}">
                  <i class="icon" :style="{ color: 'rgb(var(--orange-5))' }"><icon-exclamation-circle-fill/></i>
                  <!-- 站点报警总开关 -->
                  逆变器“发电量”选择“操作”，其它变量选择不操作
                </span>
                </template>

                <!-- 0不补 1补前一条 -->
                <a-radio-group v-model="formData.dataFix">
                  <a-radio :value="0">不操作</a-radio>
                  <a-radio :value="1">操作</a-radio>
                </a-radio-group>
              </a-form-item>

              <!-- 是否累积量 -->
              <a-form-item field="isAccumulation" :label="$t('power.energy.device.isCumulative')">
                <a-switch :checked-value="1" :unchecked-value="0" v-model="formData.isAccumulation">
                  <template #checked>{{ $t('global.yes') }}</template>
                  <template #unchecked>{{ $t('global.no') }}</template>
                </a-switch>
              </a-form-item>

              <!-- 累积类型 -->
<!--              <a-form-item v-if="formData.isAccumulation == 1" field="accumulationType"-->
<!--                           :label="$t('power.energy.device.cumulativeType')">-->
<!--                &lt;!&ndash; 1小时累积 2天累积 &ndash;&gt;-->
<!--                <a-radio-group v-model="formData.accumulationType">-->
<!--                  <a-radio v-for="dict in sys_accumulate_type" :key="dict.value" :value="Number(dict.value)">{{-->
<!--                      dict.label-->
<!--                    }}-->
<!--                  </a-radio>-->
<!--                </a-radio-group>-->
<!--              </a-form-item>-->


              <a-divider orientation="left" style="margin-top: 40px">{{
                  $t('power.energy.device.dataFilter')
                }}
              </a-divider>
              <!-- 数据过滤 -->
              <a-form-item field="isDataFilter" :label="$t('power.energy.device.dataFilter')">
                <a-switch :checked-value="1" :unchecked-value="0" v-model="formData.isDataFilter">
                  <template #checked>{{ $t('pv.alarm.setting.open') }}</template>
                  <template #unchecked>{{ $t('pv.alarm.setting.close') }}</template>
                </a-switch>
              </a-form-item>
              <a-form-item hide-label>
                <a-row>
                  <a-col :span="12">
                    <a-form-item field="lessAbs" :label="$t('power.energy.device.absoluteValue')">
                      <a-input-number v-model="formData.lessAbs"/>
                    </a-form-item>
                  </a-col>
                  <a-col :span="12">
                    <a-form-item field="replaceValueUpper" :label="$t('power.energy.device.replace')">
                      <a-input-number v-model="formData.replaceValueUpper"/>
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form-item>
              <a-form-item hide-label>
                <template #extra>
                  <span :style="{ color: 'rgb(var(--orange-7))', paddingLeft:'10px'}">
                    <i class="icon" :style="{ color: 'rgb(var(--orange-5))' }"><icon-exclamation-circle-fill/></i>
                    <!-- 站点报警总开关 -->
                    数据过滤仅判断累积值，当替换值为“9.999”时不做处理
                  </span>
                </template>
                <a-row style="margin-top: -20px;">
                  <a-col :span="12">
                    <a-form-item field="moreAbs" :label="$t('power.energy.device.absoluteValueGreaterThan')">
                      <a-input-number v-model="formData.moreAbs"/>
                    </a-form-item>
                  </a-col>
                  <a-col :span="12">
                    <a-form-item field="replaceValueUpper" :label="$t('power.energy.device.replace')">
                      <a-input-number v-model="formData.replaceValueLower"/>
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form-item>

            </a-col>
          </a-row>
        </a-space>
        <div class="actions">
          <a-space>
            <a-button type="primary" status="danger" @click="onCancleClick">{{ $t('global.cancel') }}</a-button>
            <a-button type="primary" :disabled="submitLoading" :loading="submitLoading"
                      @click="onSubmitClick">{{ $t('global.save') }}
            </a-button>
          </a-space>
        </div>
      </a-form>
    </a-spin>

    <!-- 选择设备弹框 -->
    <a-modal width="800px" :visible="modalModel.visible" :footer="false" title-align="start"
             :title="modalModel.serarchModel.varType == 1 ? '请选择变量(模拟量)' : '请选择变量(状态量)'"
             @cancel="handleModalClose">
      <a-form :model="modalModel.serarchModel" auto-label-width>
        <a-row :gutter="16">
          <a-col :span="10">
            <a-form-item field="deviceId" label="用能设备">
              <a-tree-select v-model="modalModel.serarchModel.deviceId" :data="modalModel.deviceData"
                             placeholder="请选择用能设备" :fieldNames="{
                  key: 'id', title: 'deviceName', children: 'children'
                }" :allow-search="true" :allow-clear="true" :filter-tree-node="filterTreeNode"></a-tree-select>
            </a-form-item>
          </a-col>
          <a-col :span="10">
            <a-form-item field="varName" label="变量名称">
              <a-input v-model="modalModel.serarchModel.varName" allow-clear
                       placeholder="变量名称，支持模糊搜索"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <!-- 模块类型 -->
            <a-button type="primary" @click="searchDeviceVar">
              <template #icon>
                <icon-search/>
              </template>
              查询
            </a-button>
          </a-col>
        </a-row>
      </a-form>
      <a-table :loading="modalModel.loading" :scroll="{ y: 400 }" row-key="id" :bordered="{ wrapper: true, cell: true }"
               :columns="(deviceVarTabColumns as TableColumnData[])" :data="modalModel.renderData"
               @page-change="onDeviceVarPageChange" :pagination="pagination"
               @page-size-change="onDeviceVarPageSizeChange">
        <template #operate="{ record }">
          <a-button size="small" type="primary" @click="handleDeviceVarChoose(record.varSn)">选择</a-button>
        </template>
      </a-table>
    </a-modal>

  </a-card>
</template>
<script setup lang="ts">
import {FormInstance, TreeNodeData, TableColumnData, TableRowSelection, Message} from '@arco-design/web-vue';
import {notification} from "@/hooks/my-design";
import {computed, getCurrentInstance, onMounted, reactive, ref, watch, watchEffect} from 'vue';
import useLoading from '@/hooks/loading';
import {handleCascaderOptionData, handleTreeNodeData, processSelectable} from '@/utils/ruoyi';
import {listChannelDeviceAll} from '@/api/pv/channel/device';
import {StationTypeEnum, getDevice, listFusionGroup} from '@/api/system/device';
import {BasePagination} from '@/api/common';
import {useRouter, useRoute} from 'vue-router'
import {listVarMapAll} from '@/api/system/var-map';
import {addDeviceVar, getDeviceVar, listDeviceVar, updateDeviceVar} from '@/api/system/device-var';
import {getChannelDevice} from '@/api/system/channel-device';

//路由信息
const router = useRouter()
//组件参数
const props = ref<any>({
  varType: 1,
  bizId: 0,
})

const defalutProps = defineProps({
  stationType: {
    type: Number,
    default: StationTypeEnum.power,
  }
})
//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_data_type,
  sys_save_cycle,
  sys_data_origin,
  sys_rw,
  sys_deletion_type,
  sys_accumulate_type,
  sys_config_variable_type,
  sys_stat_var_sn_code
} = proxy?.useDict("sys_data_type", "sys_save_cycle", "sys_data_origin", "sys_rw", "sys_deletion_type", "sys_accumulate_type", 'sys_config_variable_type', "sys_stat_var_sn_code");
//******* 这里编写动态获取下拉列表 end ******
//通讯设备
const channelDeviceData = ref<any>([]);
const channelDeviceAreaData = ref<any>([]);
const channelDeviceTreeData = ref<TreeNodeData[]>([]);
//变量
const varMapSnSourceData = ref<any>([]);
const varMapSnData = ref<any>([]);
const variableTypeData = ref<any>([]); //变量类型
//设备
const deviceData = ref<any>([]);
//计算器值
const computeFormulaStr = ref<string>("");
const calculatorValue = ref<any>([]);
const calculatorSymbol = ref<any>(["+", "-", "*", "/"])
const calculatorSymbolWithZero = ref<any>([...calculatorSymbol.value, "0"])
//加载中
const {loading, setLoading} = useLoading();
//表单实例
const formRef = ref<FormInstance>();
const submitLoading = ref<boolean>(false);
//生成表单模型
const generateFormModel = () => {
  return {
    id: 0,
    entId: undefined,
    deptId: undefined,
    stationType: defalutProps.stationType,
    channelId: undefined,
    channelSn: undefined,
    comDeviceId: undefined,
    comDeviceSn: undefined,
    comDeviceName: undefined,
    deviceId: undefined,
    deviceKey: undefined,
    deviceSn: undefined,
    varMapId: undefined,
    varMapSn: undefined,
    varName: undefined,
    varSn: 'sys',
    unit: undefined,
    varType: undefined,
    variableType: 0,//变量类型
    origin: 1,
    registerName: undefined,
    registerIndex: undefined,
    areaName: undefined,
    messageAddress: undefined,
    rw: 1,
    computeFormula: '',
    deletionHandle: 1,
    zeroCompute: 0, //零值计算
    repairData: 0, //补充当前数据
    dataType: 19,
    initValue: 0,
    baseValue: 0.0,
    coefficient: 1,
    saveCycle: 5,
    isAccumulation: 0,
    accumulationType: 1,
    isDataFilter: 0,
    moreAbs: 0,
    replaceValueUpper: 0,
    lessAbs: 0,
    replaceValueLower: 0,
    stopFlag: 0,
    isMonitor: 0,
    varIds: [] as number[],
    connectModel: '-', //通讯协议
    registerAddress: undefined, //寄存器地址
    dataAddress: undefined, //数据标识
    dataFix: 0, //数据修正
  };
};
//表单模型
const formData = ref(generateFormModel());
//表单规则
const formRule = {
  varName: [{required: true, message: "请输入变量名称"}],
  varSn: [{required: true, message: "请输入变量编号"}],
  deviceKey: [{required: true, message: "请选择用能设备"}],
  // varMapId: [{ required: true, message: "请选择变量属性" }],
  // unit: [{ required: true, message: "请选择变量属性" }],
  comDeviceId: [{required: true, message: "请选择通讯设备"}],
  // areaName: [{ required: true, message: "请选择数据区域" }],
  // variableTypeData: [{ required: true, message: "请选择变量类型" }],
  messageAddress: [{required: true, message: "请输入信息体地址"}],
  registerAddress: [{required: true, message: "请输入寄存器地址"}],
  dataAddress: [{required: true, message: "请选择数据标识"}],
  rw: [{required: true, message: "请选择用数据区域"}],
  dataType: [{required: true, message: "请选择数据类型"}],
  saveCycle: [{required: true, message: "请选择存盘周期"}],
  // computeFormula: [{ required: true, message: "请输入计算公式" }],
}
//变量列
const deviceVarTabColumns = computed<TableColumnData[]>(() => [
  {
    title: "设备名称",
    dataIndex: "deviceName",
    align: 'left',
    width: 150
  }, {
    title: "变量名称",
    dataIndex: "varName",
    align: 'left',
    width: 150
  }, {
    title: "变量编码",
    dataIndex: "varSn",
    align: 'left',
    width: 150
  }, {
    title: "单位",
    dataIndex: "unit",
    align: 'left',
    width: 100
  },
  {
    title: "操作",
    dataIndex: "operate",
    slotName: 'operate',
    width: 80,
    align: 'center'
  }
]);
//生成弹框模型
const generateModalModel = () => {
  return {
    loading: false,
    visible: false,
    //搜索条件
    serarchModel: {
      stationType: defalutProps.stationType,
      varType: 1,
      varName: '',
      deviceId: undefined
    },
    //表格数据
    renderData: [],
    deviceData: []
  }
}
//弹框模型
const modalModel = ref(generateModalModel());
//表格分页参数
const pagination: any = reactive({...BasePagination()});

/**
 * 设备搜索
 * @param searchValue
 * @param nodeData
 */
const filterTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.deviceName.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
}

/**
 * 表格页码发生变化-变量列表
 * @param val
 */
const onDeviceVarPageChange = async (val: number) => {
  pagination.current = val;
  await fetchDeviceVarData();
}

/**
 * 表格每页数量发生变化-变量列表
 * @param val 值
 */
const onDeviceVarPageSizeChange = async (val: number) => {
  pagination.pageSize = val;
  await fetchDeviceVarData();
}

/**
 * 选择变量
 */
const handleVarTypeSelect = async (varType: number) => {
  modalModel.value = generateModalModel();
  modalModel.value.visible = true;
  modalModel.value.serarchModel.varType = varType;
  pagination.current = 1;
  await fetchDeviceData(1);
  await fetchDeviceVarData();
}

/**
 * 添加设备
 */
const handleModalClose = () => {
  modalModel.value = generateModalModel();
}

const searchDeviceVar = async () => {
  pagination.current = 1;
  await fetchDeviceVarData();
}

/**
 * 设备选中
 */
const handleDeviceVarChoose = (val: any) => {
  try {
    handleCalculatorInput(val)
  } catch (error) {
    console.log("handleDeviceVarChoose", error)
  } finally {
    handleModalClose();
  }
}

/**
 * 保存
 */
const onSubmitClick = async () => {
  if (submitLoading.value) {
    Message.error({
      content: "保存中...",
      duration: 2 * 1000,
    });
    return;
  }
  submitLoading.value = true;
  try {
    const validate = await formRef.value?.validate();
    if (!validate) {
      let result;
      if (formData.value.id == 0) {
        result = await addDeviceVar(formData.value);
      } else {
        result = await updateDeviceVar(formData.value);
      }
      notification(result);
      if (result.code == 200) {
        back()
      }
    }
  } catch (e) {

  } finally {
    submitLoading.value = false;
  }
}


/**
 * 监听数组变化
 */
watchEffect(() => {
  let str: string = "";
  if (calculatorValue.value.length > 0) {
    calculatorValue.value.forEach((item: any) => {
      str += item;
    })
  }
  computeFormulaStr.value = str;
})

/**
 * 计算器输入
 * @param value
 */
const handleCalculatorInput = (val: any) => {
  //第一位不能为 + - * / 0
  if (calculatorValue.value.length == 0) {
    if (calculatorSymbolWithZero.value.indexOf(val) != -1) {
      return;
    }
  }
  //如果最后一位是 / 且 输入的值是 0 则不允许
  if (calculatorSymbol.value.indexOf(calculatorValue.value[calculatorValue.value.length - 1]) != -1 && calculatorSymbolWithZero.value.indexOf(val) != -1) {
    return;
  }
  calculatorValue.value.push(val);
}

/**
 * 分割
 * @param s 字符串
 */
const splitExpression = (s: any) => {
  let result = [];
  let currentStr = '';
  for (let i = 0; i < s.length; i++) {
    if (calculatorSymbol.value.indexOf(s[i]) != -1) {
      if (currentStr) {
        result.push(currentStr);
        currentStr = '';
      }
      result.push(s[i]);
    } else {
      currentStr += s[i];
    }
  }
  if (currentStr) {
    result.push(currentStr);
  }
  return result;
}


/**
 * 确定
 * @param val
 */
const handleCalculatorOk = (val: any) => {
  formData.value.computeFormula = computeFormulaStr.value;
}

/**
 * 取消
 * @param val
 */
const handleCalculatorCancle = (val: any) => {
  computeFormulaStr.value = formData.value.computeFormula;
}

/**
 * 清空
 */
const handleCalculatorClear = () => {
  calculatorValue.value = [];
  formData.value.computeFormula = '';
}

/**
 * 回退
 */
const handleCalculatorBack = () => {
  calculatorValue.value.pop();
}

/**
 * 取消
 */
const onCancleClick = () => {
  back()
}

/**
 * 选择设备
 * @param val
 */
const handleDeviceChange = async (val: any) => {
  if (val) {
    let arr = val.split("@");
    if (arr.length == 2) {
      formData.value.deviceId = arr[0];
      formData.value.deviceSn = arr[1];

      //获取对应的通讯设备
      await getDeviceDetail();
    }
  } else {

  }
}

const getDeviceDetail = async () => {
  let res = await getDevice(formData.value.deviceId);
  if (res.code == 200) {
    res = await getChannelDevice(res.data.comDeviceId);
    if (res.code == 200) {
      formData.value.comDeviceId = res.data.id;
      formData.value.comDeviceName = res.data.deviceName;
      formData.value.comDeviceSn = res.data.deviceSn;
      channelDeviceAreaData.value = res.data.dataArea;
      formData.value.channelId = res.data.channelId;
      formData.value.connectModel = res.data.connectModel;
      handleAreaName();
    }
  }
}

const handleDeviceAreaDataChange = () => {
  if (channelDeviceAreaData.value) {
    let info = channelDeviceAreaData.value.find((item: any) => item.areaName == formData.value.areaName);
    if (info) {
      formData.value.rw = info.rw;
      formData.value.registerName = info.area;
    }
  }
}

const handleAreaName = () => {
  if (channelDeviceAreaData.value) {
    let info = channelDeviceAreaData.value.find((item: any) => item.area == formData.value.registerName);
    if (info) {
      formData.value.areaName = info.areaName;
    }
  }
}

/**
 * 设备清空
 */
const handleDeviceClear = () => {
  formData.value.deviceId = undefined;
  formData.value.deviceSn = undefined;
  formData.value.comDeviceId = undefined;
  formData.value.comDeviceName = undefined;
  formData.value.comDeviceSn = undefined;
  formData.value.registerName = undefined;
  channelDeviceAreaData.value = [];
  formData.value.rw = undefined;
  formData.value.messageAddress = undefined;
}


const handleOriginChange = async () => {
  if (formData.value.origin == 2) {
    formData.value.comDeviceId = undefined;
    formData.value.comDeviceName = undefined;
    formData.value.comDeviceSn = undefined;
    formData.value.registerName = undefined;
    formData.value.areaName = undefined;
    channelDeviceAreaData.value = [];
    formData.value.rw = 1;
    formData.value.messageAddress = undefined;
  } else {
    handleCalculatorClear();
    await getDeviceDetail();
  }
}

/**
 * 选择变量属性的时候
 * @param val
 */
const handleVarMapChange = (val: any) => {
  const findData = varMapSnSourceData.value.find((item: any) => item.id === val);
  if (findData) {
    formData.value.varMapSn = findData.indexSn;
    formData.value.unit = findData.unit;
    console.log(`varMapSn=${formData.value.varMapSn} unit=${formData.value.unit}`)
  }
}

/**
 * 选择变量属性的时候
 * @param val
 */
const handleVarMapId = () => {
  const findData = formData.value.varMapSn ? varMapSnSourceData.value.find((item: any) => item.indexSn === formData.value.varMapSn) : undefined;
  if (findData) {
    formData.value.varMapId = findData.id;
  }
}

/**
 * 变量属性清空的时候
 */
const handleVarMapClear = () => {
  formData.value.varMapId = undefined;
  formData.value.varMapSn = undefined;
  formData.value.unit = undefined;
  console.log(`varMapSn=${formData.value.varMapSn} unit=${formData.value.unit}`)
}


/**
 * 获取变量属性
 * varType: props.value.varType
 */
const fetchVarMapSnData = async () => {
  try {
    const res = await listVarMapAll({});
    if (res.code == 200) {
      varMapSnSourceData.value = res.data;
      varMapSnData.value = handleCascaderOptionData(res.data, "id", "indexName");
      // console.log(varMapSnData.value, 'varMapSnData')
    }
  } catch (ex) {
    console.error("添加变量-变量属性失败", ex)
  }
}

/**
 * 获取设备明细
 */
const fetchDeviceVarInfoData = async () => {
  if (props.value.bizId && props.value.bizId > 0) {
    setLoading(true);
    try {
      const res = await getDeviceVar(props.value.bizId);
      if (res.code == 200) {
        if (res.data.deviceId && res.data.deviceSn && res.data.deviceSn.length > 0) {
          res.data.deviceKey = `${res.data.deviceId}@${res.data.deviceSn}`;
        }
        formData.value = res.data;

        if (res.data.computeFormula && res.data.computeFormula.length > 0) {
          calculatorValue.value = splitExpression(res.data.computeFormula);
        }
        await getDeviceDetail();
        handleVarMapId();
      }
    } catch (ex) {
      console.error("添加设备-获取设备信息失败", ex)
    } finally {
      setLoading(false);
    }
  }
}

/**
 * 获取设备列表
 */
const fetchDeviceData = async (type: number) => {
  try {
    const res = await listFusionGroup({
      stationType: defalutProps.stationType,
    });
    processSelectable(res.data, true);
    if (type == 1) {
      modalModel.value.deviceData = res.data;
    } else {
      deviceData.value = res.data;
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
}


/**
 * 获取变量列表
 */
const fetchDeviceVarData = async () => {
  modalModel.value.loading = true;
  try {
    const res: any = await listDeviceVar({
      ...modalModel.value.serarchModel,
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
    });
    if (res.code == 200) {
      modalModel.value.renderData = res.rows;
      pagination.total = res.total;
    }
  } catch (err) {
    console.error("fetchDeviceVarData", err)
  } finally {
    modalModel.value.loading = false;
  }
}

/**
 * 获取通讯设备
 */
const fetchChannelDevice = async () => {
  try {
    const res = await listChannelDeviceAll({});
    if (res.code == 200) {
      channelDeviceData.value = res.data;
      channelDeviceTreeData.value = handleTreeNodeData(res.data, "id", "deviceName");
    }
  } catch (error) {

  }
}


/**
 * 返回上一级页面
 */
const back = () => {
  router.push({
    path: router.currentRoute.value.query.redirect as string,
  });
}


/**
 * 获取所有数据
 */
const fetchAllData = async () => {
  await Promise.all([
    //获取变量属性
    fetchVarMapSnData(),
    //获取通讯设备
    fetchChannelDevice(),
    //获取设备
    fetchDeviceData(0),
    //获取设备变量详情
  ]);
  await fetchDeviceVarInfoData()
}
/**
 * 首次渲染
 */
onMounted(async () => {
  props.value = {
    stationType: router.currentRoute.value.query.stationType,
    varType: router.currentRoute.value.query.varType,
    bizId: router.currentRoute.value.query.bizId
  }
  formData.value = generateFormModel();
  formData.value.stationType = defalutProps.stationType;
  formData.value.varType = props.value.varType;
  setLoading(true);
  try {
    await fetchAllData();
  } catch (error) {
  } finally {
    setLoading(false);
  }
})
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 40px 20px;
  overflow: hidden;
}

.actions {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: 60px;
  padding: 14px 20px 14px 0;
  background: var(--color-bg-2);
  text-align: right;
  box-shadow: 0 10px 30px 0 rgb(0 0 0 / 40%);
}


.grid-calculator .calculator-item {
  height: 48px;
  width: 80px;
  line-height: 48px;
  color: var(--color-white);
  text-align: center;

  .arco-btn {
    width: 80px;
    height: 40px;
  }
}
</style>
