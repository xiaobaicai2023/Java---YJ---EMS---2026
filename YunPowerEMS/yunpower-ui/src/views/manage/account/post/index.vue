<template>
  <div>
    <a-card class="content">

      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px;">
          <a-form :model="searchParams" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left" auto-label-width>
            <a-row :gutter="24">
              <a-col :span="22">
                <a-form-item field="postName" :label="$t('manage.account.post.jobTitle')">
                  <a-input v-model="searchParams.postName" :placeholder="$t('manage.account.post.jobTitlePlaceholder')"
                           allow-clear/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-col :span="16" style="text-align: left">
          <a-space :size="18">
            <a-divider style="height: 35px" direction="vertical"/>
            <a-button type="primary" @click="fetchData">
              <template #icon>
                <icon-search/>
              </template>
              {{ $t('global.search') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

      <a-divider style="margin-top: 0"/>

      <!-- 操作条 start-->
      <a-row style="margin-bottom: 16px;">
        <a-col :span="12">
          <a-button type="primary" @click="createClick">
            <template #icon>
              <icon-plus/>
            </template>
            {{ $t('manage.account.post.newJob') }}
          </a-button>
        </a-col>
      </a-row>
      <!-- 操作条 end-->

      <!-- 表格 start-->
      <a-table row-key="id" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="pagination"
               :columns="(tabColumns as TableColumnData[])" :data="renderData" @page-change="onPageChange"
               @page-size-change="onPageSizeChange">
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"/>
        </template>

        <template #operate="{ record }">
          <a-button size="small" type="text"
                    @click="handleModalClick(record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable'), record.id, record.stopFlag == 0 ? 1 : 0)"
                    :status="record.stopFlag ? 'normal' : 'warning'">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="editChannelClick(record.id)">
            {{ $t('global.edit') }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleModalClick('删除', record.id,record.id)">
            {{ $t('global.delete') }}
          </a-button>
        </template>

      </a-table>
      <!-- 表格 end-->

    </a-card>

    <!-- 删除弹框 start -->
    <a-modal width="400px" v-model:visible="modalVisible" class="modal-box">
      <template #title>
        <icon-close-circle v-if="modalTitle == '删除'" size="18" style="color:rgb(var(--red-6)); margin-right: 5px;"/>
        <icon-exclamation-circle v-else size="18" style="color:rgb(var(--orange-6)); margin-right: 5px;"/>
        {{ $t('global.confirmTip') }}
      </template>
      <div style="text-align: center;">你确定要【{{ modalTitle }}】当前记录吗？</div>
      <template #footer>
        <div style="text-align: center">
          <a-space>
            <a-button type="primary" status="danger" @click="modalVisible = false;">{{ $t('global.cancel') }}</a-button>
            <a-button type="primary" @click="handleModalOk">{{ $t('global.confirm') }}</a-button>
          </a-space>
        </div>
      </template>
    </a-modal>
    <!-- 删除弹框 end -->

    <!-- 添加右弹层 start -->
    <a-drawer :width="750" :visible="drawerVisible" @ok="handleFormOK" :ok-loading="drawerLoading"
              @cancel="handleFormCancel" :mask-closable="false">
      <template #title>
        {{ formData.id ? $t('manage.account.post.editorialPost') : $t('manage.account.post.addPost') }}
      </template>
      <a-spin style="width:100%;height:100%" :loading="drawerLoading">
        <a-row :gutter="24" class="row-mp-30">
          <a-col :span="24">
            <a-form ref="formRef" :model="formData" label-align="right" :rules="formRules" auto-label-width>
              <a-form-item field="postName" :label="$t('manage.account.post.jobTitle')" :validate-trigger="['blur']">
                <a-input v-model="formData.postName" :max-length="20" allow-clear
                         :placeholder="$t('manage.account.post.titleMaximumPlaceholder')"/>
              </a-form-item>

              <a-form-item field="postSn" :label="$t('manage.account.post.postCode')" :validate-trigger="['blur']">
                <a-input v-model="formData.postSn" :max-length="20" allow-clear
                         :placeholder="$t('manage.account.post.postCodePlaceholder')"/>
              </a-form-item>

              <a-form-item field="orderNum" :label="$t('global.displaySequence')">
                <a-input-number v-model="formData.orderNum"/>
              </a-form-item>

              <a-form-item field="status" :label="$t('manage.account.post.positionStatus')">
                <a-switch v-model="formData.stopFlag" :checked-value="checkedValue"
                          :unchecked-value="unCheckedValue">
                  <template #checked>{{ $t('global.normal') }}</template>
                  <template
                      #unchecked>{{ $t('global.outOfService') }}
                  </template>
                </a-switch>
              </a-form-item>

              <a-form-item field="remark" :label="$t('manage.account.post.remarks')">
                <a-textarea v-model="formData.remark" allow-clear :max-length="200" :auto-size="true"
                            :show-word-limit="true" :placeholder="$t('manage.account.post.remarksPlaceholder')"/>
              </a-form-item>

            </a-form>
          </a-col>
        </a-row>
      </a-spin>
    </a-drawer>
    <!-- 添加右弹层 end -->
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, reactive, ref, Ref} from "vue";
import {BasePagination} from "@/api/common";
import {
  listPost,
  getPost,
  addPost,
  updatePost,
  delPost
} from "@/api/manage/account/post";
import {TableColumnData} from "@arco-design/web-vue";
import {notification} from "@/hooks/my-design";
import useLoading from "@/hooks/loading";
import {FormInstance} from '@arco-design/web-vue/es/form';

const drawerVisible = ref(false);
const drawerLoading = ref(false);

//加载中
const {loading, setLoading} = useLoading(true);
//表格数据
const renderData = ref<any[]>([]);
//表格分页配置
const pagination: any = reactive({...BasePagination()});
//设置表格列
const tabColumns = computed<TableColumnData[]>(() => [
  {
    title: "编号",
    dataIndex: "id",
    align: 'center',
    width: 80
  },
  {
    title: "岗位名称",
    dataIndex: "postName",
    align: 'center',
    width: 240
  },
  {
    title: "岗位编码",
    dataIndex: "postSn",
    align: 'center',
    width: 140
  },
  {
    title: "排序",
    dataIndex: "orderNum",
    align: 'center',
    width: 100
  },
  {
    title: "启用状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
    width: 180,
    align: 'center'
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 160,
    slotName: 'operate',
    align: 'center'
  },
]);

//生成查询条件
const generateFormModel = () => {
  return {
    id: 0,
    //岗位名称
    postName: "",
  };
};

//查询项
const searchParams = ref(generateFormModel());

const checkedValue = ref<number>(0);
const unCheckedValue = ref<number>(1);

const generateFormDataModel = () => {
  return {
    id: null,
    createBy: '',
    createTime: '',
    updateBy: '',
    updateTime: '',
    remark: '',
    entId: null,
    deptId: null,
    postName: '',
    postSn: '',
    orderNum: 0,
    stopFlag: 0,
    deleteFlag: null
  };
};

const formData = ref(generateFormDataModel());

// 检验规则
const formRules = {
  postName: [{required: true, message: '请输入岗位名称'}],
  postSn: [{required: true, message: '请输入设备号'}],
  orderNum: [{required: true, message: '请输入岗位顺序'}]
}
const formRef = ref<FormInstance>();

//重置查询条件
const reset = () => {
  searchParams.value = generateFormModel();
}

//表格页码发生变化
const onPageChange = (val: number) => {
  pagination.current = val;
  fetchData();
}

//表格每页数量发生变化
const onPageSizeChange = (val: number) => {
  pagination.pageSize = val;
  fetchData();
}

//查询数据
const fetchData = async () => {
  setLoading(true);
  try {
    const res = await listPost({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchParams.value,
    });
    console.log(res)
    renderData.value = res.rows;
    pagination.total = res.total;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};

const createClick = () => {
  drawerVisible.value = true;
};

//新增、编辑 抽屉确定
const handleFormOK = () => {
  formRef.value?.validate(async (error) => {
    if (error) return
    drawerLoading.value = true;
    try {
      let res;
      // 新增
      if (!formData.value.id) {
        res = await addPost(formData.value)
      } else {
        res = await updatePost(formData.value)
      }
      notification(res)
      if (res.code === 200) {
        drawerVisible.value = false;
        formRef.value?.resetFields();
        fetchData();
      }
    } catch (e) {
      console.error(e);
    } finally {
      drawerLoading.value = false;
    }
  });
}
//新增、编辑 抽屉取消
const handleFormCancel = () => {
  drawerVisible.value = false;
  formRef.value?.resetFields()
}
//编辑 抽屉打开
const editChannelClick = async (id: number) => {
  const res = await getPost(id)
  console.log('编辑', res)
  formData.value = res.data
  drawerVisible.value = true;
}


const modalVisible = ref(false);
const modalTitle = ref("");
const modalBizId = ref<number>();
const modalBizValue = ref<number>();
// 通用modal 框
const handleModalClick = async (title: string, bizId: number, bizValue: number) => {
  modalVisible.value = true;
  modalTitle.value = title;
  modalBizId.value = bizId;
  modalBizValue.value = bizValue;
};
/**
 * 删除、启用、禁用  模态框开启
 */
const handleModalOk = () => {
  modalVisible.value = false;
  setLoading(true);
  try {
    if (modalTitle.value === "删除") {
      handleDelete();
    } else {
      handleEditStatus();
    }
  } catch (e) {
    console.error(e);
  } finally {
    setLoading(false);
  }
};
// 删除
const handleDelete = async () => {
  let res = await delPost(modalBizId.value as number)
  notification(res)
  await fetchData()
}
// 编辑状态  停用、禁用
const handleEditStatus = async () => {
  const findList = renderData.value.find(item => item.id === modalBizId.value)
  const params = {
    ...findList,
    stopFlag: modalBizValue.value
  }
  const res = await updatePost(params)
  notification(res)
  if (res.code === 200) {
    modalVisible.value = false;
    await fetchData()
  }
}

onMounted(() => {
  fetchData();
})
</script>
<style lang="less" scoped>
.row-mp-30 {
  margin-top: 30px;
  padding-right: 30px;
}

.desContent {
  margin-top: 20px;
  margin-left: 100px;
  margin-right: 100px;
}

.formTitle {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-neutral-6);
  border-bottom: 1px dashed var(--color-neutral-8);
}
</style>
