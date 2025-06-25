<!--
 * 功能：通道列表
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
                  label-align="left" auto-label-width>
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="channelName" :label="$t('manage.channel.list.channelName')">
                  <a-input v-model="searchModel.channelName" :placeholder="$t('manage.channel.list.fuzzyQuery')"
                           allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="registrationCode" :label="$t('manage.channel.list.registrationCode')">
                  <a-input v-model="searchModel.registrationCode" :placeholder="$t('manage.channel.list.fuzzyQuery')"
                           allow-clear/>
                </a-form-item>
              </a-col>
              <!-- <a-col :span="8">
                <a-form-item label="分类状态">
                  <a-select v-model="searchModel.stopFlag" placeholder="请选择分类状态" allow-clear>
                    <a-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </a-select>
                </a-form-item>
              </a-col> -->
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
              {{ $t('manage.channel.list.addChannel') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 start-->
      <a-table row-key="id" ref="tableRef" :loading="loading" :pagination="pagination" :scroll="{ x: 1350 }"
               :bordered="{ wrapper: true, cell: true }" :columns="(columns as TableColumnData[])" :data="renderData">
        <!-- 电站类型 -->
        <template #stationType="{ record }">
          <dict-tag :options="sys_station_type" :value="record.stationType"/>
        </template>
        <!-- 接入方式 -->
        <template #accessType="{ record }">
          <dict-tag :options="sys_channel_type" :value="record.accessType"/>
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
          <a-button size="small" type="text" status="success" @click="handleToDevice(record.id)">
            {{ $t('manage.channel.list.equipment') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleRealTime(record)">实时
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
              <a-button type="primary" :loading="operateModalModel.loading" @click="handleOperateModelOk">
                {{ $t('global.confirm') }}
              </a-button>
            </a-space>
          </div>
        </template>
      </a-modal>
      <!-- 操作弹框 end -->

      <!-- 添加/编辑 右弹层 start -->
      <a-drawer :width="750" :visible="formDrawer.visible" :ok-loading="formDrawer.loading" @ok="handleSubmitForm"
                @cancel="handleFormCancel"
                :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? $t('manage.channel.list.editGrouping') : $t('manage.channel.list.addGroup') }}
        </template>
        <a-spin style="width:100%;height:100%" :loading="formDrawer.loading">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                      :rules="formDrawer.rules">

                <!-- 通道名称 -->
                <a-form-item field="channelName" :label="$t('manage.channel.list.channelName')"
                             :validate-trigger="['change', 'blur']">
                  <a-input v-model="formDrawer.formModel.channelName" allow-clear/>
                </a-form-item>

                <!--  通道编码 -->
                <a-form-item field="channelSn" class="star-from-item" :label="$t('manage.channel.list.channelCoding')">
                  <a-input v-model="formDrawer.formModel.channelSn" disabled/>
                </a-form-item>

                <!-- 接入方式 -->
                <a-form-item field="accessType" :label="$t('manage.channel.list.accessMode')"
                             :validate-trigger="['change', 'blur']">
                  <a-radio-group v-model="formDrawer.formModel.accessType">
                    <a-radio v-for="dict in sys_channel_type" :key="dict.value" :value="Number(dict.value)">{{
                        dict.label
                      }}
                    </a-radio>
                  </a-radio-group>
                </a-form-item>

                <!-- TCP -->
                <div v-if="formDrawer.formModel.accessType == 1">
                  <!-- 通讯注册码 -->
                  <a-form-item field="registrationCode" :label="$t('manage.channel.list.communicationCode')"
                               :validate-trigger="['change', 'blur']">
                    <a-input v-model="formDrawer.formModel.registrationCode" allow-clear/>
                    <template #extra>
                    <span>
                      <i class="icon" :style="{ color: 'rgb(var(--primary-6))' }"><icon-exclamation-circle-fill/></i>
                      {{ $t('manage.channel.list.codeRule') }}
                    </span>
                    </template>
                  </a-form-item>

                  <!-- 起始位置 -->
                  <!-- <a-form-item field="codeStart" label="起始位置" :validate-trigger="['change', 'blur']">
                    <a-input-number :min="0" :precision="0" v-model="formDrawer.formModel.codeStart" allow-clear />
                  </a-form-item> -->

                  <!-- 注册码长度 -->
                  <!-- <a-form-item field="codeLength" label="注册码长度" :validate-trigger="['change', 'blur']">
                    <a-input-number :min="0" :precision="0" v-model="formDrawer.formModel.codeLength" allow-clear />
                  </a-form-item> -->

                  <!-- 注册回应码 -->
                  <a-form-item field="registrationRsp" :label="$t('manage.channel.list.responseCode')"
                               :validate-trigger="['change', 'blur']">
                    <a-input v-model="formDrawer.formModel.registrationRsp" allow-clear/>
                  </a-form-item>
                </div>

                <!-- MQTT -->
                <div v-if="formDrawer.formModel.accessType == 2">

                  <!-- 连接地址 -->
                  <a-form-item field="connIp" :label="$t('manage.channel.list.connectionAddress')"
                               :validate-trigger="['change', 'blur']">
                    <a-input v-model="formDrawer.formModel.connIp" allow-clear/>
                  </a-form-item>

                  <!-- 连接端口 -->
                  <a-form-item field="connPort" :label="$t('manage.channel.list.connectingPort')"
                               :validate-trigger="['change', 'blur']">
                    <a-input-number :min="0" :max="65535" :precision="0" v-model="formDrawer.formModel.connPort"
                                    allow-clear/>
                  </a-form-item>

                  <!-- 连接帐号 -->
                  <a-form-item field="connUsername" :label="$t('manage.channel.list.connectionAccount')"
                               :validate-trigger="['change', 'blur']">
                    <a-input v-model="formDrawer.formModel.connUsername" allow-clear/>
                  </a-form-item>

                  <!-- 连接密码 -->
                  <a-form-item field="connPwd" :label="$t('manage.channel.list.connectionPassword')"
                               :validate-trigger="['change', 'blur']">
                    <a-input v-model="formDrawer.formModel.connPwd" allow-clear/>
                  </a-form-item>

                  <!-- 订阅主题 -->
                  <a-form-item field="subscribeTopic" :label="$t('manage.channel.list.topic')"
                               :validate-trigger="['change', 'blur']">
                    <a-input v-model="formDrawer.formModel.subscribeTopic" allow-clear/>
                  </a-form-item>

                  <!-- 发布主题 -->
                  <a-form-item field="publishTopic" :label="$t('manage.channel.list.releaseTopic')"
                               :validate-trigger="['change', 'blur']">
                    <a-input v-model="formDrawer.formModel.publishTopic" allow-clear/>
                  </a-form-item>
                </div>

                <!-- 上报地址 -->
                <a-form-item field="ipAddress" :label="$t('manage.channel.list.reportAddress')"
                             :validate-trigger="['change', 'blur']">
                  <a-input v-model="formDrawer.formModel.ipAddress" allow-clear/>
                </a-form-item>

                <!-- 上报端口 -->
                <a-form-item field="port" :label="$t('manage.channel.list.reportingPort')"
                             :validate-trigger="['change', 'blur']">
                  <a-input-number :min="0" :max="65535" :precision="0" v-model="formDrawer.formModel.port" allow-clear/>
                </a-form-item>

                <!-- 连接超时 -->
                <a-form-item field="timeout" :label="$t('manage.channel.list.connectionTimeout')"
                             :validate-trigger="['change', 'blur']">
                  <a-input-number :precision="0" v-model="formDrawer.formModel.timeout" allow-clear>
                    <template
                        #append>{{ $t('manage.channel.list.seconds') }}
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 调度周期 -->
                <a-form-item field="scheduleInterval" :label="$t('manage.channel.list.schedulingCycle')"
                             :validate-trigger="['change', 'blur']">
                  <a-input-number :precision="0" v-model="formDrawer.formModel.scheduleInterval" allow-clear>
                    <template
                        #append>{{ $t('manage.channel.list.ms') }}
                    </template>
                  </a-input-number>
                </a-form-item>

                <!-- 连接报警 -->
                <a-form-item field="connectAlarm" :label="$t('manage.channel.list.connectionAlarm')"
                             :validate-trigger="['change', 'blur']">
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
                        #unchecked>{{ $t('global.forbidden') }}
                    </template>
                  </a-switch>
                </a-form-item>
              </a-form>
            </a-col>
          </a-row>
        </a-spin>
      </a-drawer>
      <!-- 添加/编辑 右弹层 end -->

      <!-- 详情 右弹层 start  -->
      <a-drawer :width="1150" :title="detailDataList.title" :visible="formDrawer.visibleDetail" hide-cancel
                @cancel="handleFormCancel" @ok="handleFormCancel" unmountOnClose :mask-closable="false">
        <a-row :gutter="24" style="padding: 10px;">
          <a-col :span="12">
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.leftData" :column="1" size="large">
              <template #value="{ value }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else>{{ value }}</span>
              </template>
            </a-descriptions>
          </a-col>
          <a-col :span="12">
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.rightDataOne" :column="1" size="large">
              <template #value="{ value, index, data }">
                <a-skeleton v-if="formDrawer.loading" :animation="true">
                  <a-skeleton-line :rows="1"/>
                </a-skeleton>
                <span v-else class="span-status">
                  <template v-if="data.field == 'isActive'">
                    <span v-if="value === 0" class="circle-device"></span>
                    <span v-else class="circle-device pass"></span>
                  </template>
                   <template v-else-if="data.field == 'stopFlag'">
                      <stop-flag :value="value"/>
                  </template>
                  <template v-else-if="data.field == 'connectAlarm'">
                    <a-tag v-if="value == 1" color="rgb(var(--primary-6))">{{ $t('global.enable') }}</a-tag>
                    <a-tag v-else color="rgb(var(--red-6))">{{ $t('global.forbidden') }}</a-tag>
                  </template>
                  <template v-else>{{ value }}</template>
                </span>
              </template>
            </a-descriptions>
            <a-divider orientation="left">系统信息</a-divider>
            <a-descriptions :align="{ label: 'right' }" :label-style="{
          color: 'rgb(var(--color-neutral-10))',
        }" :data="detailDataList.rightDataTwo" :column="1" size="large">
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
import {BasePagination} from '@/api/common';
import {notification} from "@/hooks/my-design";
import {FormInstance} from '@arco-design/web-vue/es/form';
import {
  addChannel,
  changeChannelStatus,
  delChannel,
  getChannel,
  listChannel,
  updateChannel
} from '@/api/system/channel';
import useLoading from '@/hooks/loading';
import {getDictLabel} from '@/utils/dict';
import {useRouter} from 'vue-router';

/*************************** 变量区域 begin ***************************/

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {sys_station_type, sys_channel_type} = proxy?.useDict("sys_station_type", "sys_channel_type");
//******* 这里编写动态获取下拉列表 end ******

//路由
const router = useRouter();
//生成查询条件对象
const generateSearchModel = () => {
  return {
    //通道名称
    channelName: "",
    //注册码
    registrationCode: "",
    //所属站点
    stationOwing: "",
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
    width: 80,
    fixed: 'left'
  },
  {
    title: "通道名称",
    dataIndex: "channelName",
    fixed: 'left',
    width: 200,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  // TODO：缺字段
  {
    title: "所属站点",
    dataIndex: "stationName",
    width: 200,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "站点类型",
    dataIndex: "stationType",
    slotName: "stationType",
    align: 'center',
    width: 120
  },
  {
    title: "接入方式",
    dataIndex: "accessType",
    slotName: "accessType",
    align: 'left',
    width: 120
  },
  {
    title: "注册码/IP",
    dataIndex: "registrationCode",
    slotName: "registrationCode",
    align: 'left',
    width: 120
  },
  {
    title: "上/下线时间",
    dataIndex: "time",
    slotName: "time",
    width: 180,
    align: 'center'
  },
  {
    title: "通道状态",
    dataIndex: "isActive",
    slotName: "isActive",
    align: 'center',
    width: 100,
    fixed: 'right'
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 270,
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
      channelName: [{required: true, message: "请输入通道名称"}],
      accessType: [{required: true, message: "请选择接入方式"}],
      registrationCode: [{required: true, message: "请输入通讯注册码"}, {
        validator: (value: any, cb: any) => {
          if (!isStartWith66(value)) {
            cb('注册码必须以“66”开头')
          } else {
            cb()
          }
        }
      }],
      codeStart: [{required: true, message: "请输入起始位置"}],
      codeLength: [{required: true, message: "请输入注册码长度"}],
      // registrationRsp: [{ required: true, message: "请输入注册回应码" }],
      connIp: [{required: true, message: "请输入连接地址"}, {type: 'ip', message: "请输入的连接地址"}],
      connPort: [{required: true, message: "请输入连接端口"}],
      connUsername: [{required: true, message: "请输入连接帐号"}],
      connPwd: [{required: true, message: "请输入连接密码"}],
      subscribeTopic: [{required: true, message: "请输入订阅主题"}],
      publishTopic: [{required: true, message: "请输入发布主题"}],
      ipAddress: [{required: true, message: "请输入上报地址"}, {type: 'ip', message: "请输入正确的上报地址"}],
      port: [{required: true, message: "请输入上报端口"}],
      timeout: [{required: true, message: "请输入连接超时"}],
      scheduleInterval: [{required: true, message: "请输入调度周期"}],
    },
    formModel: {
      id: 0,
      stationName: '',
      accessType: 1,
      activeUpdateTime: '',
      channelName: '',
      channelSn: '',
      codeLength: undefined,
      codeStart: 0,
      connIp: '',
      connPort: 0,
      connPwd: '',
      connUsername: '',
      connectAlarm: 0,
      ipAddress: '',
      isActive: 0,
      offlineTime: '',
      onlineTime: '',
      port: undefined,
      publishTopic: '',
      registrationCode: undefined,
      registrationRsp: '',
      remark: '',
      scheduleInterval: 300,
      stationType: undefined,
      stopFlag: 0,
      subscribeTopic: '',
      timeout: 300,
      createBy: undefined,
      createTime: undefined,
      updateBy: undefined,
      updateTime: undefined,
    }
  };
};
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

const isStartWith66 = (s: any) => {
  const pattern = /^66/;
  return pattern.test(s);
}

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
const handleUpdate = async (id: any) => {
  let result = await getChannel(id);
  if (result.code == 200) {
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
    let res = await getChannel(id);
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
    title: formDrawer.value.formModel.channelName,
    leftData: [
      {
        label: '所属站点:',
        value: formDrawer.value.formModel.stationName || '-',
      },
      {
        label: '电站类型:',
        value: getDictLabel('sys_station_type', formDrawer.value.formModel.stationType) || '-',
      },
      {
        label: '通道名称:',
        value: formDrawer.value.formModel.channelName || '-',
      },
      {
        label: '通道编码:',
        value: formDrawer.value.formModel.channelSn || '-',
      },
      {
        label: '接入方式:',
        value: getDictLabel('sys_channel_type', formDrawer.value.formModel.accessType) || '-',
      },
      {
        label: '通道注册码:',
        value: formDrawer.value.formModel.registrationCode || '-',
      },
      {
        label: '起始位置:',
        value: formDrawer.value.formModel.codeStart || '-',
      },
      {
        label: '注册码长度:',
        value: formDrawer.value.formModel.codeLength || '-',
      },
      {
        label: '注册回应码:',
        value: formDrawer.value.formModel.registrationRsp || '-',
      },
      {
        label: '连接地址:',
        value: formDrawer.value.formModel.connIp || '-',
      },
      {
        label: '连接端口:',
        value: formDrawer.value.formModel.connPort || '-',
      },
      {
        label: '连接帐号:',
        value: formDrawer.value.formModel.connUsername || '-',
      },
      {
        label: '连接密码:',
        value: formDrawer.value.formModel.connPwd || '-',
      },
      {
        label: '订阅主题:',
        value: formDrawer.value.formModel.subscribeTopic || '-',
      },
      {
        label: '发布主题:',
        value: formDrawer.value.formModel.publishTopic || '-',
      }, {
        label: '上报地址:',
        value: formDrawer.value.formModel.ipAddress || '-',
      }, {
        label: '上报端口:',
        value: formDrawer.value.formModel.port || '-',
      }, {
        label: '连接超时(s):',
        value: formDrawer.value.formModel.timeout || '-',
      }, {
        label: '调度周期(ms):',
        value: formDrawer.value.formModel.scheduleInterval || '-',
      },
    ],
    rightDataOne: [{
      label: '通道状态:',
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
      label: '连接报警:',
      value: formDrawer.value.formModel.connectAlarm || '-',
      field: 'connectAlarm'
    }, {
      label: '启用状态:',
      value: formDrawer.value.formModel.stopFlag,
      field: 'stopFlag'
    }],
    rightDataTwo: [{
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
 * 跳转到通道设备
 * @param id
 */
const handleToDevice = (id: any) => {
  router.push({
    path: "/setting/channel/device",
    query: {
      channelId: id,
    }
  });
}

/**
 * 跳转到实时
 * @param id
 */
const handleRealTime = (record: any) => {
  router.push({
    path: "/setting/channel/real-time",
    query: {
      channelSn: record.channelSn,
    }
  });
}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = '删除';
  operateModalModel.value.name = record.channelName;
  operateModalModel.value.type = 1;
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
    name: record.channelName
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
  let result: any = {};
  operateModalModel.value.visible = false;
  setLoading(true);
  try {
    if (operateModalModel.value.type == 1) {
      result = await delChannel(operateModalModel.value.id);
    } else {
      result = await changeChannelStatus(operateModalModel.value.id, operateModalModel.value.value);
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

    try {
      let result: any = {};
      if (formDrawer.value.formModel.id == 0) {
        result = await addChannel(formDrawer.value.formModel);
      } else {
        result = await updateChannel(formDrawer.value.formModel);
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


  } else {
    formRef.value?.scrollToField(validate[Object.keys(validate)[0]].field);
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
    const res = await listChannel({
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

onMounted(async () => {
  await fetchData();
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
