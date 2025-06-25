<!--
* 功能：消息列表
* 作者：曹晓桐
* 日期：2023-10-15
-->
<template>
  <a-card class="content">
    <!-- 查询条件 start-->
    <a-row>
      <a-col :flex="1" style="margin-top: 4px;">
        <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left"
          auto-label-width>
          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-item field="userName" :label="$t('manage.message.list.userName')">
                <a-input v-model="searchParams.userName" :placeholder="$t('manage.message.list.namePlaceholder')" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item field="mobile" :label="$t('manage.message.list.mobileNumber')">
                <a-input v-model="searchParams.mobile" :placeholder="$t('manage.message.list.numberPlaceholder')" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item field="time" :label="$t('manage.message.list.sendTime')">
                <a-range-picker v-model="searchParams.sendTime" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-divider style="height: 35px" direction="vertical" />
      <a-col :flex="'86px'" style="text-align: right">
        <a-space :size="18">
          <a-button type="primary" @click="onQueryClick">
            <template #icon>
              <icon-search />
            </template>
            {{$t('global.search')}}
          </a-button>
        </a-space>
      </a-col>
    </a-row>
    <!-- 查询条件 end-->
    <a-divider style="margin-top: 0" />

    <!-- 操作条 start-->
    <a-row style="margin-bottom: 16px;">
      <a-col :span="24">
        <a-space>
          <a-button type="primary" @click="handleDeleteClick">
            {{$t('global.batchDeletion')}}
          </a-button>
          <a-button type="primary" status="danger" @click="handleDeleteAllClick">
            {{$t('global.empty')}}
          </a-button>
        </a-space>
      </a-col>
    </a-row>
    <!-- 操作条 end-->

    <!-- 表格 start-->
    <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
      :columns="(tabColumns as TableColumnData[])" :data="renderData" @page-change="onPageChange"
      @page-size-change="onPageSizeChange" v-model:selectedKeys="selectedKeys" :row-selection="rowSelection"
      :scroll="scroll">

      <!-- 模板类型 -->
      <template #templateType="{ record }">
        <dict-tag :options="sys_template_type" :value="record.templateType" />
      </template>

      <!-- 消息主题 -->
      <!-- <template #topic="{ record }">
        <dict-tag :options="sys_message_topic" :value="record.topic" />
      </template> -->

      <!-- 发送状态 -->
      <template #sendStatus="{ record }">
        <!-- 发送状态（-10已使用 0发送成功 1发送失败） -->
        <a-tag v-if="record.sendStatus == -10" color="arcoblue">{{$t('manage.message.list.used')}}</a-tag>
        <a-tag v-if="record.sendStatus == 0" color="green">{{$t('manage.message.list.success')}}</a-tag>
        <a-tag v-if="record.sendStatus == 1" color="red">{{$t('manage.message.list.fail')}}</a-tag>
      </template>
      <template #operate="{ record }">
        <a-space>
          <a-button size="small" type="text" status="success" @click="handleDetailClick(record.id)">{{$t('global.detail')}}</a-button>
        </a-space>
      </template>
    </a-table>
    <!-- 表格 end-->
    <a-modal width="400px" v-model:visible="modalVisible" class="modal-box">
      <template #title>
        <icon-close-circle v-if="modalTitle == '删除'" size="18" style="color:rgb(var(--red-6)); margin-right: 5px;" />
        <icon-exclamation-circle v-else size="18" style="color:rgb(var(--orange-6)); margin-right: 5px;" />
        {{$t('global.confirmTip')}}
      </template>
      <div style="text-align: center;">你确定要【{{ modalTitle }}】吗？</div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="handleModalCancel">{{$t('global.cancel')}}</a-button>
            <a-button type="primary" @click="handleModalOk">{{$t('global.confirm')}}</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>


    <!-- 详情 右弹层 start  -->
    <a-drawer :width="700" :title="$t('manage.message.list.messageDetails')" :visible="formDrawer.visible" hide-cancel @cancel="handleFormCancel"
      @ok="handleFormCancel" unmountOnClose :mask-closable="false">
      <a-row :gutter="24" style="padding: 10px;">
        <a-col>
          <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }" :column="1"
            size="large">
            <!-- 用户姓名 -->
            <a-descriptions-item :label="$t('manage.message.list.userNameColon')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.userName }}</span>
            </a-descriptions-item>

            <!-- 手机号码 -->
            <a-descriptions-item :label="$t('manage.message.list.mobileNumberColon')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.mobile }}</span>
            </a-descriptions-item>

            <!-- 消息类型 -->
            <a-descriptions-item :label="$t('manage.message.list.messageType')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.templateTypeName }}</span>
            </a-descriptions-item>

            <!-- 消息主题 -->
            <a-descriptions-item :label="$t('manage.message.list.messageSubject')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.topic }}</span>
            </a-descriptions-item>

            <!-- 验证码 -->
            <a-descriptions-item :label="$t('manage.message.list.verificationCode')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.code }}</span>
            </a-descriptions-item>


            <!-- 有效期 -->
            <a-descriptions-item :label="$t('manage.message.list.validity')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.effectMinute }}</span>
            </a-descriptions-item>


            <!-- 提交信息 -->
            <a-descriptions-item :label="$t('manage.message.list.submitInformation')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.sendContent }}</span>
            </a-descriptions-item>

            <!-- 短信渠道 -->
            <a-descriptions-item :label="$t('manage.message.list.shortMessageChannel')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.smsChannelName }}</span>
            </a-descriptions-item>

            <!-- 模板ID -->
            <a-descriptions-item :label="$t('manage.message.list.templateIdColon')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.templateId }}</span>
            </a-descriptions-item>

            <!-- 发送人 -->
            <a-descriptions-item :label="$t('manage.message.list.sender')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.sendBy }}</span>
            </a-descriptions-item>

            <!-- 发送时间 -->
            <a-descriptions-item :label="$t('manage.message.list.sendingTime')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>{{ formDrawer.formModel.sendTime }}</span>
            </a-descriptions-item>

            <!-- 发送状态 -->
            <a-descriptions-item :label="$t('manage.message.list.sendingState')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>
                <template v-if="formDrawer.formModel.sendStatus == -10">{{$t('manage.message.list.used')}}</template>
                <template v-if="formDrawer.formModel.sendStatus == 0">{{$t('manage.message.list.success')}}</template>
                <template v-if="formDrawer.formModel.sendStatus == 1">{{$t('manage.message.list.fail')}}</template>
              </span>
            </a-descriptions-item>

            <!-- 发送内容 -->
            <a-descriptions-item :label="$t('manage.message.list.sendContent')">
              <a-skeleton v-if="formDrawer.loading" :animation="true">
                <a-skeleton-line :rows="1" />
              </a-skeleton>
              <span v-else>
                <div style="width: 550px;">
                  <hljs v-if="formDrawer.formModel.sendContent"  :code="formDrawer.formModel.sendContent || ''" />
                </div>
              </span>
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>
    </a-drawer>
    <!-- 详情 右弹层 end  -->

  </a-card>
</template>

<script lang="ts" setup>
import { computed, getCurrentInstance, onMounted, reactive, ref } from "vue";
import { BasePagination } from "@/api/common";
import { TableColumnData, TableRowSelection } from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import { notification } from "@/hooks/my-design";
import dayjs from "dayjs";
import { cleanMessage, delMessageIds, getMessage, listMessage } from "@/api/system/message";
import { getDictLabel } from "@/utils/dict";


//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const { sys_message_topic, sys_template_type, sys_sms_channel } = proxy?.useDict("sys_message_topic", "sys_template_type", "sys_sms_channel");
//******* 这里编写动态获取下拉列表 end ******

//加载中
const { loading, setLoading } = useLoading(true);
//表格数据
const renderData = ref<any[]>([]);
//表格分页参数
const pagination: any = reactive({ ...BasePagination() });
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "用户姓名",
    dataIndex: "userName",
    width: 120,
    align: "center",
    fixed: "left",
  },
  {
    title: "手机号码",
    dataIndex: "mobile",
    align: "center",
    width: 130,
  },
  {
    title: "消息类型",
    dataIndex: "templateType",
    slotName: "templateType",
    width: 100,
    align: "center",
  },
  {
    title: "消息主题",
    dataIndex: "topic",
    // slotName: "topic",
    width: 180,
    align: "center",
  },
  {
    title: "验证码",
    dataIndex: "code",
    width: 100,
    align: "center",
  },
  {
    title: "有效期",
    dataIndex: "effectMinute",
    width: 100,
    align: "center",
  },
  {
    title: "发送时间",
    dataIndex: "sendTime",
    width: 180,
    align: "center",
  },
  {
    title: "提交信息",
    dataIndex: "sendContent",
    width: 240,
    align: "left",
  },
  {
    title: "发送状态",
    dataIndex: "sendStatus",
    slotName: "sendStatus",
    align: "center",
    width: 100,
    fixed: "right",
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 100,
    slotName: "operate",
    align: "center",
    fixed: "right",
  },
]);
const rowSelection: TableRowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});
//选中的
const selectedKeys = ref();
const scroll = {
  x: 1280,
};
// 获取当前时间
var dateNow = dayjs().format("YYYY-MM-DD");
var dateBefore = dayjs().format("YYYY-MM-01");

const modalVisible = ref(false);
const modalTitle = ref("");
const modalType = ref(1);

//生成查询条件
const generateFormModel = () => {
  return {
    //用户姓名
    userName: "",
    //手机号码
    mobile: "",
    //发送时间
    sendTime: [dateBefore, dateNow],
  };
};

//查询项
const searchParams = ref(generateFormModel());

const generateFormDrawerModel = () => {
  return {
    loading: false,
    visible: false,
    formModel: {
      createBy: undefined,
      createTime: undefined,
      updateB: undefined,
      updateTime: undefined,
      remark: undefined,
      id: undefined,
      entId: undefined,
      deptId: undefined,
      userName: undefined,
      mobile: undefined,
      templateType: undefined,
      templateTypeName: undefined,
      topic: undefined,
      code: undefined,
      effectMinute: undefined,
      returnResult: undefined,
      smsChannel: undefined,
      smsChannelName: undefined,
      templateId: undefined,
      sendBy: undefined,
      sendTime: undefined,
      sendStatus: undefined,
      sendContent: undefined,
      stopFlag: undefined,
      deleteFlag: undefined,
    }
  };
};
//表单模型
const formDrawer = ref(generateFormDrawerModel());

/**
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
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

// 查询
const onQueryClick = async () => {
  console.log(searchParams);
  await fetchData();
}

const handleDetailClick = async (id: number) => {
  formDrawer.value.loading = true;
  formDrawer.value.visible = true;
  try {
    let res = await getMessage(id);
    if (res.code == 200) {
      if (res.data.templateType) {
        res.data.templateTypeName = getDictLabel("sys_template_type", res.data.templateType)
      }
      if (res.data.smsChannel) {
        res.data.smsChannelName = getDictLabel("sys_sms_channel", res.data.smsChannel)
      }
      formDrawer.value.formModel = res.data;
    }
  } catch (e) {
    console.log(e)
  } finally {
    formDrawer.value.loading = false;
  }
}

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listMessage({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
      sendTime: '',
      params: {
        beginTime: searchParams.value.sendTime[0],
        endTime: searchParams.value.sendTime[1]
      }
    });
    renderData.value = res.rows;
    pagination.total = res.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};


//确认框

/**
 * 删除弹框
 */
const handleDeleteClick = () => {
  if (!selectedKeys.value || selectedKeys.value.length <= 0) {
    notification({ code: 201, msg: "请选择要删除的数据" })
    return;
  }
  modalVisible.value = true;
  modalTitle.value = "删除所选记录";
  modalType.value = 1;
  console.log(selectedKeys.value);
};

/**
 * 清空按钮
 */
const handleDeleteAllClick = () => {
  modalVisible.value = true;
  modalType.value = 2;
  modalTitle.value = "清空所有数据";
};

/**
 * 删除-确定
 */
const handleModalOk = async () => {
  let res;
  modalVisible.value = false;
  setLoading(true);
  try {
    if (modalType.value == 1) {
      if (!selectedKeys.value || selectedKeys.value.length <= 0) {
        notification({ code: 201, msg: "请选择要删除的数据" })
        return;
      }
      res = await delMessageIds(selectedKeys.value);
    } else {
      res = await cleanMessage();
    }
    notification(res);
    if (res.code == 200) {
      await fetchData();
    }
  }catch (e) {
    console.error(e);
  }finally {
    setLoading(false);
  }
};


/**
 * 删除弹框-取消
 */
const handleModalCancel = () => {
  modalVisible.value = false;
  console.log("取消");
}

/**
 * 实例创建完成后立即执行
 */
onMounted(async () => {
  await fetchData();
})
</script>
<style lang="less" scoped>
.row-mp-30 {
  margin-top: 30px;
  padding-right: 30px;
}
</style>
