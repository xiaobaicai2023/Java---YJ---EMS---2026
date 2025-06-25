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
                <a-form-item field="deviceName" label="设备名称">
                  <a-input v-model="searchModel.deviceName" placeholder="支持模糊查询" allow-clear />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="connectModel" label="接入协议">
                  <a-select v-model="searchModel.connectModel" placeholder="请选择连接协议" allow-clear>
                    <a-option v-for="dict in sys_protocol_code" :key="dict.id" :label="dict.label"
                      :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="所属通道">
                  <a-select v-model="searchModel.channelId" :options="renderChannelAllData"
                    :field-names="{ value: 'id', label: 'channelName' }" placeholder="全部通道" allow-clear>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 35px" direction="vertical" />
        <a-col :flex="'86px'" style="text-align: right">
          <a-space :size="18">
            <a-button type="primary" @click="search">
              <template #icon>
                <icon-search />
              </template>
              查询
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

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
              添加设备
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 start-->
      <a-table row-key="id" ref="tableRef" :loading="loading" :pagination="pagination" :scroll="{ x: 1870 }"
        :bordered="{ wrapper: true, cell: true }" :columns="(columns as TableColumnData[])" :data="renderData">
        <!-- 产品类型 -->
        <template #proType="{ record }">
          <dict-tag :options="sys_equip_type" :value="record.proType" />
        </template>

        <!-- 连接协议 -->
        <template #connectModel="{ record }">
          <dict-tag :options="sys_protocol_code" :value="record.connectModel" />
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
        <template #stopFlag="{ record }">
          <span v-if="record.stopFlag === 1" class="circle-device"></span>
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
          <a-button size="small" type="text" status="success" @click="handleUpdate(record.id)">编辑</a-button>
          <a-button size="small" type="text" status="success" @click="handleDetail(record.id)">详情
          </a-button>
          <a-button size="small" type="text" status="success" disabled>配置
          </a-button>
        </template>
      </a-table>
      <!-- 表格内容 end-->

      <!-- 操作弹框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible" class="modal-box">
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
      <!-- 操作弹框 end -->

      <!-- 添加/编辑 右弹层 start -->
      <a-drawer :width="1500" :visible="formDrawer.visible" @ok="handleSubmitForm" @cancel="handleFormCancel"
        :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? '编辑设备' : '添加设备' }}
        </template>
        <a-form ref="formRef" :model="formDrawer.formModel" label-align="right" :rules="formDrawer.rules"
          auto-label-width>
          <a-row :gutter="24">
            <a-col :span="12">

              <a-divider orientation="left">设备信息</a-divider>
              <!-- 设备名称 -->
              <a-form-item field="deviceName" label="设备名称" :validate-trigger="['change', 'blur']">
                <a-input v-model="formDrawer.formModel.deviceName" allow-clear />
              </a-form-item>

              <!-- 设备编码 -->
              <a-form-item field="deviceSn" label="设备编码">
                <a-input v-model="formDrawer.formModel.deviceSn" disabled />
              </a-form-item>

              <!-- 所属通道 -->
              <a-form-item field="channelId" label="所属通道">
                <a-select v-model="formDrawer.formModel.channelId" :options="renderChannelData"
                  :field-names="{ value: 'id', label: 'channelName' }" placeholder="请选择通道" allow-clear>
                </a-select>
              </a-form-item>

              <!-- 连接协议 -->
              <a-form-item field="connectModel" label="连接协议" :validate-trigger="['change', 'blur']">
                <a-select v-model="formDrawer.formModel.connectModel" placeholder="请选择连接协议" allow-clear
                  @change="handleConnectModelChange">
                  <a-option v-for="dict in sys_protocol_code" :key="dict.id" :label="dict.label"
                    :value="dict.value"></a-option>
                </a-select>
              </a-form-item>

              <!-- 连接超时 -->
              <a-form-item field="timeout" label="连接超时" :validate-trigger="['change', 'blur']">
                <a-input-number :precision="0" v-model="formDrawer.formModel.timeout" allow-clear><template
                    #append>秒</template></a-input-number>
              </a-form-item>

              <!-- 连接报警 -->
              <a-form-item field="connectAlarm" label="连接报警">
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                  v-model="formDrawer.formModel.connectAlarm"><template #checked>开启</template><template
                    #unchecked>关闭</template></a-switch>
              </a-form-item>

              <!-- 启用状态 -->
              <a-form-item field="stopFlag" label="启用状态">
                <a-switch :checked-value="checkedValue" :unchecked-value="unCheckedValue"
                  v-model="formDrawer.formModel.stopFlag"><template #checked>启用</template><template
                    #unchecked>停用</template></a-switch>
              </a-form-item>

              <a-divider orientation="left">产品信息</a-divider>

              <!-- 产品类型 -->
              <a-form-item field="proType" label="产品类型" :validate-trigger="['change', 'blur']">
                <a-select v-model="formDrawer.formModel.proType" placeholder="请选择产品类型" allow-clear>
                  <a-option v-for="dict in sys_equip_type" :key="dict.value" :label="dict.label"
                    :value="dict.value"></a-option>
                </a-select>
              </a-form-item>

              <!-- 生产厂家 -->
              <a-form-item field="proFactory" label="生产厂家">
                <a-tree-select allow-search v-model="formDrawer.formModel.proFactory" :data="renderCompanyData"
                  placeholder="请选择生产厂家" allow-clear></a-tree-select>
              </a-form-item>

              <!-- 产品型号 -->
              <a-form-item field="proModel" label="产品型号" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formDrawer.formModel.proModel" allow-clear />
              </a-form-item>

              <!-- 产品版本 -->
              <a-form-item field="proVer" label="产品版本" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formDrawer.formModel.proVer" allow-clear />
              </a-form-item>

              <!-- 产品sn号 -->
              <a-form-item field="proSn" label="产品sn号" :validate-trigger="['change', 'blur', 'input']">
                <a-input v-model="formDrawer.formModel.proSn" allow-clear />
              </a-form-item>

              <a-divider orientation="left">通讯卡</a-divider>

              <!-- 运营商 -->
              <a-form-item field="cardOperator" label="运营商" :validate-trigger="['change', 'blur']">
                <a-select v-model="formDrawer.formModel.cardOperator" placeholder="请选择运营商" allow-clear>
                  <a-option v-for="dict in sys_communication_operator" :key="dict.value" :label="dict.label"
                    :value="dict.value"></a-option>
                </a-select>
              </a-form-item>

              <!-- 物联卡号 -->
              <a-form-item field="cardNo" label="物联卡号" :validate-trigger="['change', 'blur']">
                <a-input v-model="formDrawer.formModel.cardNo" allow-clear />
              </a-form-item>

              <!-- IP地址-->
              <a-form-item field="cardIp" label="IP地址" :validate-trigger="['change', 'blur']">
                <a-input v-model="formDrawer.formModel.cardIp" allow-clear />
              </a-form-item>

              <!-- 流量标准 -->
              <a-form-item field="cardFlow" label="流量标准" :validate-trigger="['change', 'blur']">
                <a-input-number v-model="formDrawer.formModel.cardFlow" allow-clear><template
                    #append>M/月</template></a-input-number>
              </a-form-item>

              <!-- 资费标准 -->
              <a-form-item field="cardPostage" label="资费标准" :validate-trigger="['change', 'blur']">
                <a-input-number v-model="formDrawer.formModel.cardPostage" allow-clear><template
                    #append>元/月</template></a-input-number>
              </a-form-item>

              <!-- 资费到期 -->
              <a-form-item field="cardExpireDate" label="资费到期">
                <a-date-picker v-model="formDrawer.formModel.cardExpireDate" />
              </a-form-item>


            </a-col>
            <a-col :span="12">
              <a-divider orientation="left">协议：{{ formDrawer.formModel.connectModelLabel }}</a-divider>
              <!-- 接入方式非“104协议”显示此模块 -->
              <div v-if="formDrawer.formModel.connectModel != 'IEC104'">
                <!-- 设备地址 -->
                <a-form-item field="sbdz" label="设备地址" :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.sbdz" allow-clear />
                </a-form-item>
              </div>
              <!-- 接入方式104协议显示此模块 -->
              <div v-else>

                <!-- 传送原因长度 -->
                <a-form-item field="csyyLength" label="传送原因长度" :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.csyyLength">
                    <template #append>字节</template>
                  </a-input-number>
                </a-form-item>

                <!-- 公共地址 -->
                <a-form-item field="ggdz" label="公共地址" :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.ggdz" placeholder="输入数字" />
                </a-form-item>

                <!-- 公共地址长度 -->
                <a-form-item field="ggdzLength" label="公共地址长度" :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.ggdzLength">
                    <template #append>字节</template>
                  </a-input-number>
                </a-form-item>

                <!-- 信息体地址长度 -->
                <a-form-item field="infoLength" label="信息体地址长度" :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.infoLength">
                    <template #append>字节</template>
                  </a-input-number>
                </a-form-item>

                <!-- 连接超时（T0） -->
                <a-form-item field="timeoutT0" label="连接超时（T0）" :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.timeoutT0">
                    <template #append>秒</template>
                  </a-input-number>
                </a-form-item>

                <!-- 连接超时（T1） -->
                <a-form-item field="timeoutT1" label="连接超时（T1）" :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.timeoutT1" placeholder="15">
                    <template #append>秒</template>
                  </a-input-number>
                </a-form-item>

                <!-- 连接超时（T2） -->
                <a-form-item field="timeoutT2" label="连接超时（T2）" :validate-trigger="['change', 'blur']">
                  <a-input-number v-model="formDrawer.formModel.timeoutT2" placeholder="15">
                    <template #append>秒</template>
                  </a-input-number>
                </a-form-item>

                <!-- 连接超时（T3） -->
                <a-form-item field="timeoutT3" label="连接超时（T3）">
                  <a-input-number v-model="formDrawer.formModel.timeoutT3" placeholder="20">
                    <template #append>秒</template>
                  </a-input-number>
                </a-form-item>

                <!-- 总召采集周期 -->
                <a-form-item field="assCallCycle" label="总召采集周期">
                  <a-input-number v-model="formDrawer.formModel.assCallCycle">
                    <template #append>秒</template>
                  </a-input-number>
                </a-form-item>

                <!-- 电度总召周期 -->
                <a-form-item field="elcCallCycle" label="电度总召周期">
                  <a-input-number v-model="formDrawer.formModel.elcCallCycle">
                    <template #append>秒</template>
                  </a-input-number>
                </a-form-item>
              </div>

              <a-divider orientation="left">数据区</a-divider>

              <a-form-item hide-label>
                <a-button type="primary" @click="handleAreaAdd">添加数据区</a-button>
              </a-form-item>
              <a-form-item hide-label>
                <a-table row-key="id" :scroll="{ y: 600 }" :bordered="{ wrapper: true, cell: true }"
                  :columns="(areaColumns as TableColumnData[])" :data="formDrawer.formModel.dataArea"
                  :pagination="false">
                  <!-- 名称 -->
                  <template #areaName="{ record }">
                    <a-input v-model="record.areaName" />
                  </template>

                  <!-- 起始地址 -->
                  <template #addStart="{ record }">
                    <a-input-number v-model="record.addStart" />
                  </template>

                  <!-- 长度 -->
                  <template #addLength="{ record }">
                    <a-input-number v-model="record.addLength" />
                  </template>

                  <!-- 寄存器 -->
                  <template #area="{ record, rowIndex }">
                    <a-select v-model="record.area" placeholder="请选择寄存器" :options="renderAreaMaData"
                      :field-names="{ value: 'area', label: 'area' }" allow-clear @change="handleAreaChange(rowIndex)">
                    </a-select>
                  </template>

                  <template #operate="{ rowIndex }">
                    <a-button size="small" type="primary" status="danger"
                      @click="handleAreaDelete(rowIndex)">移除</a-button>
                  </template>
                </a-table>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-drawer>
      <!-- 添加/编辑 右弹层 end -->

      <!-- 详情 右弹层 start  -->
      <a-drawer :width="1400" :title="detailDataList.title" :visible="formDrawer.visibleDetail" hide-cancel
        @cancel="handleFormCancel" @ok="handleFormCancel" unmountOnClose :mask-closable="false">
        <a-row :gutter="24" style="padding: 0 20px;">
          <a-col :span="14">
            <a-divider orientation="left">设备信息</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.deviceInfoData" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1" />
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
            <a-divider orientation="left">产品信息</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.proInfoData" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1" />
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
            <a-divider orientation="left">通讯卡</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.cardInfoData" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1" />
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
            <a-divider orientation="left">数据区</a-divider>
            <a-skeleton v-if="formDrawer.loading" :animation="true">
              <a-skeleton-line :rows="1" />
            </a-skeleton>
            <a-table v-else row-key="id" :scroll="{ y: 600 }" :bordered="{ wrapper: true, cell: true }"
              :columns="(areaDetailColumns as TableColumnData[])" :data="formDrawer.formModel.dataArea"
              :pagination="false">
              <template #area="{ record }">
                <!-- <a-select v-model="record.area" placeholder="请选择寄存器" :options="renderAreaMaData"
                  :field-names="{ value: 'area', label: 'area' }" allow-clear>
                </a-select> -->
                {{ record.area }}
              </template>
            </a-table>
          </a-col>
          <a-col :span="10">
            <a-divider orientation="left">{{ formDrawer.formModel.connectModel }}协议</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.sbdzInfoData" :column="1" size="large">
              <template #value="{ value, index, data }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1" />
                </a-skeleton>
                <span v-else class="span-status">
                  <template v-if="data.field == 'isActive' || data.field == 'stopFlag'">
                    <span class="circle-device" :class="{ 'pass': value == 0 }"></span>
                  </template>
                  <template v-else>{{ value }}</template>
                </span>
              </template>
            </a-descriptions>

            <a-divider orientation="left">系统信息</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.sysInfoData" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1" />
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
import { computed, getCurrentInstance, onMounted, reactive, ref } from 'vue';
import { Message, TableInstance } from "@arco-design/web-vue";
import { notification } from "@/hooks/my-design";
import { BasePagination } from '@/api/common';
import { FormInstance } from '@arco-design/web-vue/es/form';
import { listChannelAll } from '@/api/system/channel';
import useLoading from '@/hooks/loading';
import { getDictLabel, getDictValue } from '@/utils/dict';
import { useRouter } from 'vue-router';
import { listChannelDevice, getChannelDevice, addChannelDevice, updateChannelDevice, changeChannelDeviceStatus } from '@/api/system/channel-device';
import { listCompanyAll } from '@/api/system/company';
import { handleTreeNodeData } from '@/utils/ruoyi';
import { listAreaMapAll } from '@/api/system/area-map';

/*************************** 变量区域 begin ***************************/

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_protocol_code, sys_equip_type, sys_communication_operator } = proxy?.useDict("sys_protocol_code", "sys_equip_type", "sys_communication_operator");
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
const { loading, setLoading } = useLoading(true);
//表格分页参数
const pagination: any = reactive({ ...BasePagination() });
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
    tooltip: { position: 'top' },
  },
  {
    title: "所属站点",
    dataIndex: "stationName",
    slotName: "stationName",
    width: 200,
    align: "left",
    ellipsis: true,
    tooltip: { position: 'top' },
  },
  {
    title: "所属通道",
    dataIndex: "channelName",
    slotName: "channelName",
    width: 240,
    align: "left",
    ellipsis: true,
    tooltip: { position: 'top' },
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
    align: "left",
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
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    width: 120,
    align: "center",
    fixed: "right",
  },
  {
    title: "操作",
    dataIndex: "operation",
    width: 230,
    slotName: "operate",
    align: "center",
    fixed: "right",
  },
]);
//数据区域
const areaColumns = [{
  title: '名称',
  dataIndex: 'areaName',
  slotName: 'areaName',
  width: 150,

}, {
  title: '起始地址',
  dataIndex: 'addStart',
  slotName: 'addStart',
  width: 150,
  align: "center",
}, {
  title: '长度',
  dataIndex: 'addLength',
  slotName: 'addLength',
  width: 100,
  align: "center",
}, {
  title: '寄存区',
  dataIndex: 'area',
  slotName: 'area',
  width: 150,
  align: "center",
}, {
  title: '操作',
  dataIndex: 'operate',
  slotName: 'operate',
  width: 100,
  align: 'center'
}];

//数据区域
const areaDetailColumns = [{
  title: '名称',
  dataIndex: 'areaName',
  slotName: 'areaName',
  width: 150,

}, {
  title: '起始地址',
  dataIndex: 'addStart',
  slotName: 'addStart',
  width: 150,
  align: "center",
}, {
  title: '长度',
  dataIndex: 'addLength',
  slotName: 'addLength',
  width: 100,
  align: "center",
}, {
  title: '寄存区',
  dataIndex: 'area',
  slotName: 'area',
  width: 150,
  align: "center",
}];

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
    loading: false,
    visible: false,
    visibleDetail: false,
    rules: {
      deviceName: [{ required: true, message: "请输入设备名称" }],
      channelId: [{ required: true, message: "请选择通道" }],
      connectModel: [{ required: true, message: "请选择连接协议" }],
      timeout: [{ required: true, message: "请输入连接超时时间" }],
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
      dataArea: [] as any,
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
  await fetchAreaMapAll();
  formDrawer.value.visible = true;
  formDrawer.value.formModel.dataArea.push({ ...areaInfo })
}

/**
 * 添加数据区域
 */
const handleAreaAdd = () => {
  if (!formDrawer.value.formModel.connectModel) {
    Message.error({
      content: '请先连接协议',
      duration: 2 * 1000,
    });
    return false
  }
  formDrawer.value.formModel.dataArea.push({ ...areaInfo })
}

/**
 * 移除数据区域
 */
const handleAreaDelete = (rowIndex: any) => {
  formDrawer.value.formModel.dataArea?.splice(rowIndex, 1)
}

const handleAreaChange = (rowIndex: any) => {
  let info = formDrawer.value.formModel.dataArea[rowIndex];
  if (info) {

    let areaInfo = renderAreaMaData.value.find((item: any) => item.area == info.area);
    if (areaInfo) {
      info.rw = areaInfo.rw;
    }
  }
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
  await fetchAreaMapAll();
  formDrawer.value.formModel.dataArea = [{ ...areaInfo }];
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
    await fetchAreaMapAll();
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
      {
        label: '设备地址:',
        value: formDrawer.value.formModel.sbdz || '-',
      }, {
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
        label: '是否激活:',
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
  let result = await changeChannelDeviceStatus(operateModalModel.value.id, operateModalModel.value.value);
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
  } else {
    formRef.value?.scrollToField(validate[Object.keys(validate)[0]].field);
  }
}

/**
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
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
    const res = await listCompanyAll({ companyGroup: 1 });
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
 * 查询通讯设备数据区域类型（公共）列表
 */
const fetchAreaMapAll = async () => {
  try {
    const res = await listAreaMapAll({ protocol: formDrawer.value.formModel.connectModel || '' });
    if (res.code == 200) {
      renderAreaMaData.value = res.data;
    }
  } catch (ex) {

  }
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

<style scoped></style>
