<!--
 * 功能: 通道管理-通讯设备
 * 作者: 王晓明
 * 日期: 2023-11-12
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
              <a-col :span="8">
                <a-form-item field="deviceName" :label="$t('manage.channel.device.deviceName')">
                  <a-input v-model="searchModel.deviceName" :placeholder="$t('manage.channel.list.fuzzyQuery')"
                           allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="connectModel" :label="$t('manage.channel.device.accessProtocol')">
                  <a-select v-model="searchModel.connectModel"
                            :placeholder="$t('manage.channel.device.protocolPlaceholder')" allow-clear>
                    <a-option v-for="dict in sys_protocol_code" :key="dict.id" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item :label="$t('manage.channel.device.owningChannel')">
                  <a-select v-model="searchModel.channelId" :options="renderChannelAllData"
                            :field-names="{ value: 'id', label: 'channelName' }"
                            :placeholder="$t('manage.channel.device.allChannels')" allow-clear>
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
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('manage.channel.device.addDevice') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 start-->
      <a-table row-key="id" ref="tableRef" :loading="loading" :pagination="pagination" :scroll="{ x: 1870 }"
               :bordered="{ wrapper: true, cell: true }" :columns="(columns as TableColumnData[])" :data="renderData">
        <!-- 产品类型 -->
        <template #proType="{ record }">
          <dict-tag :options="sys_equip_type" :value="record.proType"/>
        </template>

        <!-- 连接协议 -->
        <template #connectModel="{ record }">
          <dict-tag :options="sys_protocol_code" :value="record.connectModel"/>
        </template>

        <!-- 设备/公共地址 //TODO -->
        <template #dz="{ record }">
          {{ record.sbdz || record.ggdz }}
        </template>

        <!-- 注册码/IP -->
        <template #registrationCode="{ record }">
          {{ record.accessType == 1 ? record.registrationCode : record.connIp }}
        </template>
        <!-- 通道状态 -->
        <template #isActive="{ record }">
          <span v-if="record.isActive === 0" class="circle-device"></span>
          <span v-else class="circle-device pass"></span>
        </template>
        <!-- 上/下线时间 -->
        <template #time="{ record }">
          {{ record.stopFlag == 0 ? record.onlineTime : record.offlineTime }}
        </template>
        <template #operate="{ record }">
          <a-button size="small" type="text" @click="handleStopFlag(record)"
                    :status="record.stopFlag == 1 ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdate(record.id)">{{ $t('global.edit') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleDetail(record.id)">{{ $t('global.detail') }}
          </a-button>
          <a-button size="small" type="text" status="success" disabled>{{ $t('manage.channel.device.disposition') }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
        </template>
      </a-table>
      <!-- 表格内容 end-->

      <!-- 操作弹框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible" class="modal-box">
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
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">{{ $t('global.cancel') }}
              </a-button>
              <a-button :loading="operateModalModel.loading" type="primary" @click="handleOperateModelOk">
                {{ $t('global.confirm') }}
              </a-button>
            </a-space>
          </div>
        </template>
      </a-modal>
      <!-- 操作弹框 end -->

      <!-- 添加/编辑 右弹层 start -->
      <a-drawer :width="1500" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm" @cancel="handleFormCancel"
                :mask-closable="false">
        <template #title>
          {{
            formDrawer.formModel.id ? $t('manage.channel.device.editingEquipment') : $t('manage.channel.device.addDevice')
          }}
        </template>
        <a-form ref="formRef" :model="formDrawer.formModel" label-align="right" :rules="formDrawer.rules"
                auto-label-width>
          <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
            <a-row :gutter="24">
              <a-col :span="12">
                <a-divider orientation="left">{{ $t('manage.channel.device.deviceInformation') }}</a-divider>
                <!-- 设备名称 -->
                <a-form-item field="deviceName" :label="$t('manage.channel.device.deviceName')"
                             :validate-trigger="['change', 'blur']">
                  <a-input v-model="formDrawer.formModel.deviceName" allow-clear/>
                </a-form-item>

                <!-- 设备编码 -->
                <a-form-item field="deviceSn" class="star-from-item" :label="$t('manage.channel.device.deviceCoding')">
                  <a-input v-model="formDrawer.formModel.deviceSn" disabled/>
                </a-form-item>

                <!-- 所属通道 -->
                <a-form-item field="channelId" :label="$t('manage.channel.device.owningChannel')">
                  <a-select v-model="formDrawer.formModel.channelId" :options="renderChannelData"
                            :field-names="{ value: 'id', label: 'channelName' }"
                            :placeholder="$t('manage.channel.device.channelSelect')" allow-clear>
                  </a-select>
                </a-form-item>

                <!-- 连接协议 -->
                <a-form-item field="connectModel" :label="$t('manage.channel.device.connectionProtocol')"
                             :validate-trigger="['change', 'blur']">
                  <a-select v-model="formDrawer.formModel.connectModel"
                            :placeholder="$t('manage.channel.device.protocolPlaceholder')" allow-clear
                            @change="handleConnectModelChange">
                    <a-option v-for="dict in sys_protocol_code" :key="dict.id" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 连接超时 -->
                <a-form-item field="timeout" :label="$t('manage.channel.device.timeout')"
                             :validate-trigger="['change', 'blur']">
                  <a-input-number :precision="0" v-model="formDrawer.formModel.timeout" allow-clear>
                    <template
                        #append>秒
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 连接报警 -->
                <a-form-item field="connectAlarm" :label="$t('manage.channel.device.alarm')">
                  <a-switch :checked-value="1" :unchecked-value="0"
                            v-model="formDrawer.formModel.connectAlarm">
                    <template #checked>{{ $t('global.on') }}</template>
                    <template
                        #unchecked>{{ $t('global.off') }}
                    </template>
                  </a-switch>
                </a-form-item>

                <!-- 启用状态 -->
                <a-form-item field="stopFlag" :label="$t('manage.channel.list.enabledState')">
                  <a-switch :checked-value="0" :unchecked-value="1"
                            v-model="formDrawer.formModel.stopFlag">
                    <template #checked>{{ $t('global.enable') }}</template>
                    <template
                        #unchecked>{{ $t('global.outOfService') }}
                    </template>
                  </a-switch>
                </a-form-item>

                <a-divider orientation="left" style="margin-top: 40px;">
                  {{ $t('manage.channel.device.productInformation') }}
                </a-divider>

                <!-- 产品类型 -->
                <a-form-item field="proType" :label="$t('manage.channel.device.productType')"
                             :validate-trigger="['change', 'blur']">
                  <a-select v-model="formDrawer.formModel.proType"
                            :placeholder="$t('manage.channel.device.productSelect')" allow-clear>
                    <a-option v-for="dict in sys_equip_type" :key="dict.value" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 生产厂家 -->
                <a-form-item field="proFactory" :label="$t('manage.channel.device.manufacturer')">
                  <a-tree-select allow-search v-model="formDrawer.formModel.proFactory" :data="renderCompanyData"
                                 :placeholder="$t('manage.channel.device.manufacturerSelect')"
                                 allow-clear></a-tree-select>
                </a-form-item>

                <!-- 产品型号 -->
                <a-form-item field="proModel" :label="$t('manage.channel.device.productModel')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.proModel" allow-clear/>
                </a-form-item>

                <!-- 产品版本 -->
                <a-form-item field="proVer" :label="$t('manage.channel.device.productVersion')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.proVer" allow-clear/>
                </a-form-item>

                <!-- 产品sn号 -->
                <a-form-item field="proSn" :label="$t('manage.channel.device.productSn')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-input v-model="formDrawer.formModel.proSn" allow-clear/>
                </a-form-item>

                <a-divider orientation="left" style="margin-top: 40px;">
                  {{ $t('manage.channel.device.communicationCard') }}
                </a-divider>

                <!-- 运营商 -->
                <a-form-item field="cardOperator" :label="$t('manage.channel.device.operator')"
                             :validate-trigger="['change', 'blur']">
                  <a-select v-model="formDrawer.formModel.cardOperator"
                            :placeholder="$t('manage.channel.device.operatorSelect')" allow-clear>
                    <a-option v-for="dict in sys_communication_operator" :key="dict.value" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 物联卡号 -->
                <a-form-item field="cardNo" :label="$t('manage.channel.device.cardNumber')"
                             :validate-trigger="['change', 'blur']">
                  <a-input v-model="formDrawer.formModel.cardNo" allow-clear/>
                </a-form-item>

                <!-- IP地址-->
                <a-form-item field="cardIp" :label="$t('manage.channel.device.ipAddress')"
                             :validate-trigger="['change', 'blur']">
                  <a-input v-model="formDrawer.formModel.cardIp" allow-clear/>
                </a-form-item>

                <!-- 流量标准 -->
                <a-form-item field="cardFlow" :label="$t('manage.channel.device.flowStandard')"
                             :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.cardFlow" allow-clear>
                    <template
                        #append>{{ $t('manage.channel.device.M') }}
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 资费标准 -->
                <a-form-item field="cardPostage" :label="$t('manage.channel.device.tariffStandard')"
                             :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.cardPostage" allow-clear>
                    <template
                        #append>{{ $t('manage.channel.device.RMB') }}
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 资费到期 -->
                <a-form-item field="cardExpireDate" :label="$t('manage.channel.device.tariffDue')">
                  <a-date-picker v-model="formDrawer.formModel.cardExpireDate"/>
                </a-form-item>


              </a-col>
              <a-col :span="12">
                <!-- 接入方式1 -->
                <a-divider orientation="left"
                           v-if="['IEC104'].includes(formDrawer.formModel.connectModel)">
                  {{ $t('manage.channel.device.protocol') }}：{{ formDrawer.formModel.connectModelLabel }}
                </a-divider>

                <!--              <div v-if="['MODBUSTCP','MODBUSRTU'].includes(formDrawer.formModel.connectModel)">-->
                <!--                &lt;!&ndash; 设备地址：MODBUSTCP、MODBUSRTU显示 &ndash;&gt;-->
                <!--                <a-form-item field="sbdz" label="设备地址" :validate-trigger="['change', 'blur']">-->
                <!--                  <a-input-number v-model="formDrawer.formModel.sbdz" allow-clear/>-->
                <!--                </a-form-item>-->
                <!--              </div>-->

                <div v-if="formDrawer.formModel.connectModel == 'IEC104'">
                  <!-- 传送原因长度 -->
                  <a-form-item field="csyyLength" :label="$t('manage.channel.device.reasonLength')"
                               :validate-trigger="['change', 'blur']">
                    <a-input-number v-model="formDrawer.formModel.csyyLength">
                      <template #append>{{ $t('manage.channel.device.byte') }}</template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 公共地址 -->
                  <a-form-item field="ggdz" :label="$t('manage.channel.device.publicAddress')"
                               :validate-trigger="['change', 'blur']">
                    <a-input-number v-model="formDrawer.formModel.ggdz"
                                    :placeholder="$t('manage.channel.device.inputDigit')"/>
                  </a-form-item>

                  <!-- 公共地址长度 -->
                  <a-form-item field="ggdzLength" :label="$t('manage.channel.device.publicLength')"
                               :validate-trigger="['change', 'blur']">
                    <a-input-number v-model="formDrawer.formModel.ggdzLength">
                      <template #append>{{ $t('manage.channel.device.byte') }}</template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 信息体地址长度 -->
                  <a-form-item field="infoLength" :label="$t('manage.channel.device.messageLength')"
                               :validate-trigger="['change', 'blur']">
                    <a-input-number v-model="formDrawer.formModel.infoLength">
                      <template #append>{{ $t('manage.channel.device.byte') }}</template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 连接超时（T0） -->
                  <a-form-item field="timeoutT0" :label="$t('manage.channel.device.timeoutT0')"
                               :validate-trigger="['change', 'blur']">
                    <a-input-number v-model="formDrawer.formModel.timeoutT0">
                      <template #append>{{ $t('manage.channel.list.seconds') }}</template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 连接超时（T1） -->
                  <a-form-item field="timeoutT1" :label="$t('manage.channel.device.timeoutT1')"
                               :validate-trigger="['change', 'blur']">
                    <a-input-number v-model="formDrawer.formModel.timeoutT1" placeholder="15">
                      <template #append>{{ $t('manage.channel.list.seconds') }}</template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 连接超时（T2） -->
                  <a-form-item field="timeoutT2" :label="$t('manage.channel.device.timeoutT2')"
                               :validate-trigger="['change', 'blur']">
                    <a-input-number v-model="formDrawer.formModel.timeoutT2" placeholder="15">
                      <template #append>{{ $t('manage.channel.list.seconds') }}</template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 连接超时（T3） -->
                  <a-form-item field="timeoutT3" :label="$t('manage.channel.device.timeoutT3')">
                    <a-input-number v-model="formDrawer.formModel.timeoutT3" placeholder="20">
                      <template #append>{{ $t('manage.channel.list.seconds') }}</template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 总召采集周期 -->
                  <a-form-item field="assCallCycle" :label="$t('manage.channel.device.collectionCycle')">
                    <a-input-number v-model="formDrawer.formModel.assCallCycle">
                      <template #append>{{ $t('manage.channel.list.seconds') }}</template>
                    </a-input-number>
                  </a-form-item>

                  <!-- 电度总召周期 -->
                  <a-form-item field="elcCallCycle" :label="$t('manage.channel.device.electricalDegree')">
                    <a-input-number v-model="formDrawer.formModel.elcCallCycle">
                      <template #append>{{ $t('manage.channel.list.seconds') }}</template>
                    </a-input-number>
                  </a-form-item>
                </div>
              </a-col>
            </a-row>
          </a-spin>
        </a-form>
      </a-drawer>
      <!-- 添加/编辑 右弹层 end -->

      <!-- 详情 右弹层 start  -->
      <a-drawer :width="1400" :title="detailDataList.title" :visible="formDrawer.visibleDetail" hide-cancel
                @cancel="handleFormCancel" @ok="handleFormCancel" unmountOnClose :mask-closable="false">
        <a-row :gutter="24" style="padding: 0 20px;">
          <a-col :span="14">
            <a-divider orientation="left">{{ $t('manage.channel.device.deviceInformation') }}</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.deviceInfoData" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
            <a-divider orientation="left">{{ $t('manage.channel.device.productInformation') }}</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.proInfoData" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
            <a-divider orientation="left">{{ $t('manage.channel.device.communicationCard') }}</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.cardInfoData" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
          </a-col>
          <a-col :span="10">
           <template v-if="formDrawer.formModel.connectModel == 'IEC104'">
             <a-divider orientation="left">{{ formDrawer.formModel.connectModel }}
               {{ $t('manage.channel.device.protocol') }}
             </a-divider>
             <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.sbdzInfoData" :column="1" size="large">
               <template #value="{ value, index, data }">
                 <a-skeleton v-if="formDrawer.loading" :animation="true">
                   <a-skeleton-line :rows="1"/>
                 </a-skeleton>
                 <span v-else class="span-status">
                  <template v-if="data.field == 'isActive'">
                    <span v-if="value === 0" class="circle-device"></span>
                    <span v-else class="circle-device pass"></span>
                    <!--                    <span class="circle-device" :class="{ 'pass': value == 0 }"></span>-->
                  </template>
                    <template v-else-if="data.field == 'stopFlag'">
                      <stop-flag :value="value"/>
                  </template>
                  <template v-else>{{ value }}</template>
                </span>
               </template>
             </a-descriptions>
           </template>

            <a-divider orientation="left">{{ $t('manage.channel.device.systemInformation') }}</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.sysInfoData" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
          </a-col>
        </a-row>
      </a-drawer>
      <!-- 详情 右弹层 end  -->
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, reactive, ref} from 'vue';
import {TableInstance} from "@arco-design/web-vue";
import {notification} from "@/hooks/my-design";
import {BasePagination} from '@/api/common';
import {FormInstance} from '@arco-design/web-vue/es/form';
import {listChannelAll} from '@/api/system/channel';
import useLoading from '@/hooks/loading';
import {getDictLabel,} from '@/utils/dict';
import {useRouter} from 'vue-router';
import {
  listChannelDevice,
  getChannelDevice,
  addChannelDevice,
  updateChannelDevice,
  changeChannelDeviceStatus, delChannelDevice
} from '@/api/system/channel-device';
import {listCompanyAll} from '@/api/system/company';
import {handleTreeNodeData} from '@/utils/ruoyi';
import {listAreaMapAll} from '@/api/system/area-map';

/*************************** 变量区域 begin ***************************/

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_protocol_code,
  sys_equip_type,
  sys_communication_operator
} = proxy?.useDict("sys_protocol_code", "sys_equip_type", "sys_communication_operator");
//******* 这里编写动态获取下拉列表 end ******

//路由
const router = useRouter();
//生成查询条件对象
const generateSearchModel = () => {
  return {
    deviceName: "",
    connectModel: '',
    channelId: 0,
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
    align: "center",
    width: 80,
    fixed: "left",
  },
  {
    title: "设备名称",
    dataIndex: "deviceName",
    width: 200,
    align: "left",
    fixed: "left",
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "所属站点",
    dataIndex: "stationName",
    slotName: "stationName",
    width: 200,
    align: "left",
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "所属通道",
    dataIndex: "channelName",
    slotName: "channelName",
    width: 240,
    align: "left",
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "产品类型",
    dataIndex: "proType",
    slotName: "proType",
    width: 120,
    align: "left",
  },
  {
    title: "接入协议",
    dataIndex: "connectModel",
    slotName: "connectModel",
    width: 180,
    align: "left",
  },
  {
    title: "设备/公共地址",
    dataIndex: "ggdz",
    width: 140,
    slotName: "dz",
    align: "center",
  },
  {
    title: "资费到期",
    dataIndex: "cardExpireDate",
    width: 180,
    align: "center",
  },
  {
    title: "上/下线时间",
    dataIndex: "time",
    width: 180,
    slotName: "time",
    align: "center",
  },
  {
    title: "设备状态",
    dataIndex: "isActive",
    slotName: "isActive",
    width: 120,
    align: "center",
    fixed: "right",
  },
  {
    title: "操作",
    dataIndex: "operation",
    width: 270,
    slotName: "operate",
    align: "center",
    fixed: "right",
  },
]);

//表格数据
const renderData = ref<any[]>([]);
//通道数据
const renderChannelData = ref<any[]>([]);
//通道数据
const defaultChannelData = ref<any[]>([{
  id: 0,
  channelName: '全部通道'
}]);
const renderChannelAllData = ref<any[]>([]);
//生产厂家
const renderCompanySourceData = ref<any>([]);
const renderCompanyData = ref<any>([]);
//通讯设备数据区域
const renderAreaMaData = ref<any>([]);
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
    loading: false,
  };
};
//操作弹框模型
const operateModalModel = ref(generateOperateModalModel());
//表格示例
const formRef = ref<FormInstance>();
//生成表单模型
const generateFormDrawerModel = () => {
  return {
    loading: false,
    visible: false,
    visibleDetail: false,
    rules: {
      deviceName: [{required: true, message: "请输入设备名称"}],
      channelId: [{required: true, message: "请选择通道"}],
      connectModel: [{required: true, message: "请选择连接协议"}],
      timeout: [{required: true, message: "请输入连接超时时间"}],
    },
    formModel: {
      stationName: '',
      channelName: '',
      activeUpdateTime: '',
      assCallCycle: 120,
      cardExpireDate: '',
      cardFlow: 0,
      cardIp: '',
      cardNo: '',
      cardOperator: undefined,
      cardPostage: 0,
      channelId: undefined,
      channelSn: '',
      connectAlarm: 0,
      connectModel: undefined,
      connectModelLabel: undefined,
      connectModelValue: undefined,
      createBy: '',
      createTime: '',
      csyyLength: 2,
      deleteFlag: 0,
      deptId: 0,
      deviceName: '',
      deviceSn: '',
      elcCallCycle: 300,
      entId: 0,
      ggdz: 1,
      ggdzLength: 2,
      id: 0,
      infoLength: 3,
      isActive: 0,
      offlineTime: '',
      onlineTime: '',
      proFactory: undefined,
      proModel: '',
      proSn: '',
      proType: undefined,
      proVer: '',
      remark: '',
      sbdz: 1,
      stationType: undefined,
      stopFlag: 0,
      timeout: 60,
      timeoutT0: 30,
      timeoutT1: 15,
      timeoutT2: 10,
      timeoutT3: 20,
      updateBy: '',
      updateTime: '',
      // dataArea: [] as any,
    }
  };
};
//数据区域
const areaInfo: any = {
  areaName: '',
  addStart: '',
  addLength: undefined,
  area: '',
  rw: ''
}
//表单模型
const formDrawer = ref(generateFormDrawerModel());
//switch选中值
const checkedValue = ref<number>(0);
//switch未选中值
const unCheckedValue = ref<number>(1);
//详情
const detailDataList = ref<any>({});

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
  await fetchCompanyData();
  formDrawer.value.visible = true;
  // formDrawer.value.formModel.dataArea.push({ ...areaInfo })
}

/**
 * 连接协议-改变
 */
const handleConnectModelChange = async () => {
  formDrawer.value.formModel.connectModelLabel = undefined;
  let label = getDictLabel("sys_protocol_code", formDrawer.value.formModel.connectModel)
  if (label) {
    formDrawer.value.formModel.connectModelLabel = label;
  }
  //获取协议value
  // formDrawer.value.formModel.dataArea = [{ ...areaInfo }];
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
const handleUpdate = async (id: any) => {
  let result = await getChannelDevice(id);
  if (result.code == 200) {
    let label = getDictLabel("sys_protocol_code", result.data.connectModel)
    if (label) {
      result.data.connectModelLabel = label;
    }
    formDrawer.value.formModel = result.data;
    formDrawer.value.visible = true;
  }
}


/**
 * 查看详情
 * @param id
 */
const handleDetail = async (id: any) => {
  formDrawer.value.visibleDetail = true;
  formDrawer.value.loading = true;
  try {
    let res = await getChannelDevice(id);
    if (res.code == 200) {
      formDrawer.value.formModel = res.data;
      detailDataList.value = getDatail();
    }
  } catch (error) {

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
  operateModalModel.value.name = record.deviceName;
  operateModalModel.value.type = 1;
}

/**
 * 获取详情
 */
const getDatail = () => {
  const result = {
    title: formDrawer.value.formModel.deviceName,
    deviceInfoData: [
      {
        label: '所属站点:',
        value: formDrawer.value.formModel.stationName || '-',
      },
      {
        label: '电站类型:',
        value: getDictLabel('sys_station_type', formDrawer.value.formModel.stationType) || '-',
      },
      {
        label: '所属通道:',
        value: formDrawer.value.formModel.channelName || '-',
      },
      {
        label: '设备名称:',
        value: formDrawer.value.formModel.deviceName || '-',
      },
      {
        label: '设备编码:',
        value: formDrawer.value.formModel.deviceSn || '-',
      },
      {
        label: '连接协议:',
        value: getDictLabel('sys_protocol_code', formDrawer.value.formModel.connectModel) || '-',
      },
      {
        label: '连接超时:',
        value: formDrawer.value.formModel.timeout || '-',
      }
    ],
    proInfoData: [{
      label: '产品类型:',
      value: getDictLabel('sys_equip_type', formDrawer.value.formModel.proType) || '-',
    },
      {
        label: '生产厂家:',
        value: getProFactoryName(formDrawer.value.formModel.proFactory) || '-',
      },
      {
        label: '产品型号:',
        value: formDrawer.value.formModel.proModel || '-',
      },
      {
        label: '产品版本:',
        value: formDrawer.value.formModel.proVer || '-',
      },
      {
        label: '产品sn码:',
        value: formDrawer.value.formModel.proSn || '-',
      }],
    cardInfoData: [{
      label: '运营商:',
      value: getDictLabel('sys_communication_operator', formDrawer.value.formModel.cardOperator) || '-',
    },
      {
        label: '物联卡号:',
        value: formDrawer.value.formModel.cardNo || '-',
      },
      {
        label: 'IP地址:',
        value: formDrawer.value.formModel.cardIp || '-',
      },
      {
        label: '流量标准:',
        value: formDrawer.value.formModel.cardFlow || '-',
      },
      {
        label: '资费标准:',
        value: formDrawer.value.formModel.cardPostage || '-',
      },
      {
        label: '资费到期:',
        value: formDrawer.value.formModel.cardExpireDate || '-',
      }],
    sbdzInfoData: [
      // {
      //   label: '设备地址:',
      //   value: formDrawer.value.formModel.sbdz || '-',
      // },
      {
        label: '传送原因长度:',
        value: formDrawer.value.formModel.csyyLength || '-',
      }, {
        label: '公共地址:',
        value: formDrawer.value.formModel.ggdz || '-',
      }, {
        label: '公共地址长度:',
        value: formDrawer.value.formModel.ggdzLength || '-',
      }, {
        label: '信息体地址长度:',
        value: formDrawer.value.formModel.infoLength || '-',
      }, {
        label: '连接超时（T0）:',
        value: formDrawer.value.formModel.timeoutT0 || '-',
      }, {
        label: '连接超时（T1）:',
        value: formDrawer.value.formModel.timeoutT1 || '-',
      }, {
        label: '连接超时（T2）:',
        value: formDrawer.value.formModel.timeoutT2 || '-',
      }, {
        label: '连接超时（T3）:',
        value: formDrawer.value.formModel.timeoutT3 || '-',
      }, {
        label: '总召采集周期:',
        value: formDrawer.value.formModel.assCallCycle || '-',
      }, {
        label: '电度总召周期:',
        value: formDrawer.value.formModel.elcCallCycle || '-',
      }, {
        label: '设备状态:',
        value: formDrawer.value.formModel.isActive,
        field: 'isActive'
      }, {
        label: '激活时间:',
        value: formDrawer.value.formModel.activeUpdateTime || '-',
      }, {
        label: '上线时间:',
        value: formDrawer.value.formModel.onlineTime || '-',
      }, {
        label: '下线时间:',
        value: formDrawer.value.formModel.offlineTime || '-',
      }, {
        label: '启用状态:',
        value: formDrawer.value.formModel.stopFlag,
        field: 'stopFlag'
      }],
    sysInfoData: [
      {
        label: '创建人员:',
        value: formDrawer.value.formModel.createBy || '-',
      }, {
        label: '创建时间:',
        value: formDrawer.value.formModel.createTime || '-',
      }, {
        label: '更新人员:',
        value: formDrawer.value.formModel.updateBy || '-',
      }, {
        label: '更新时间:',
        value: formDrawer.value.formModel.updateTime || '-',
      }]
  }
  return result;
}

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
    name: record.deviceName
  }
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
  try {
    if (operateModalModel.value.type == 1) {
      result = await delChannelDevice(operateModalModel.value.id);
    } else {
      result = await changeChannelDeviceStatus(operateModalModel.value.id, operateModalModel.value.value);
    }
    notification(result);
    if (result.code == 200) {
      handleOperateModelCancle();
      await fetchData();
    }
  } catch (e) {
    console.error(e);
  } finally {
    setLoading(false);
  }
}

/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formRef.value?.validate();
  if (!validate) {
    formDrawer.value.loading = true;
    try{
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addChannelDevice(formDrawer.value.formModel);
      } else {
        result = await updateChannelDevice(formDrawer.value.formModel);
      }
      notification(result);
      if (result.code == 200) {
        handleFormCancel();
        await fetchData();
      }
    }catch (e) {
      console.error(e);
    }finally {
      formDrawer.value.loading = false;
    }


  } else {
    formRef.value?.scrollToField(validate[Object.keys(validate)[0]].field);
  }
}

/**
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
  formRef.value.resetFields();
}

/**
 * 查询通道列表
 */
const fetchChannelData = async () => {
  try {
    const res = await listChannelAll({});
    renderChannelData.value = res.data;
    renderChannelAllData.value = defaultChannelData.value.concat(res.data);
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
};

/**
 * 获取公司
 */
const fetchCompanyData = async () => {
  try {
    const res = await listCompanyAll({companyGroup: 1});
    if (res.code == 200) {
      renderCompanySourceData.value = res.data;
      renderCompanyData.value = handleTreeNodeData(res.data, "id", "companyName");
    }
  } catch (ex) {
    console.error("添加设备-获取生产厂家失败", ex)
  }
}

/**
 * 获取厂家名称
 * @param id id
 */
const getProFactoryName = (id: any) => {
  let companyName = '';
  if (!id) {
    return companyName;
  }
  if (renderCompanySourceData.value) {
    companyName = renderCompanySourceData.value.find((item: any) => item.id == id)?.companyName;
  }
  return companyName;
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listChannelDevice({
      ...searchModel.value,
      channelId: searchModel.value.channelId != 0 ? searchModel.value.channelId : undefined
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

onMounted(async () => {
  let channelId = router.currentRoute.value.query.channelId;
  searchModel.value.channelId = channelId ? Number(channelId) : 0;
  await fetchData();
  await fetchChannelData();
})
</script>

<style lang="less" scoped>

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
