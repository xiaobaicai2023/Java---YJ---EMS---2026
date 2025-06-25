<!--
 * 功能：大屏配置
 * 作者：闫李壮
 * 日期：2024-6-15
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
                <!-- 站点类型 -->
                <a-form-item field="station" label="选择站点">
                  <a-tree-select allow-search
                                 v-model="searchModel.deptId"
                                 :data="siteList"
                                 placeholder="请选择站点"
                                 allow-clear
                                 :fieldNames="{key: 'id', title: 'deptName'}"
                                 :filter-tree-node="filterTreeNode">
                  </a-tree-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <!-- 模块类型 -->
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
              新建大屏
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <!-- 表格内容 start-->
      <a-table :loading="loading"
               :bordered="{ wrapper: true, cell: true }"
               :columns="columns"
               :data="renderData"
               :pagination="pagination"
               @page-change="handlePageChange"
               @page-size-change="handlePageSizeChange">
        <!-- 分组/站点 -->
        <template #groupName="{ record }">
          {{ record.groupName }} / {{ record.stationName }}
        </template>
        <!--模板类型-->
        <template #pageType="{ record }">
          {{ record.pageTemplate == 1 ? '默认模板' : record.pageTemplate == 2 ? '电力科技' : '-' }}
        </template>
        <!-- 状态 -->
        <template #stopFlag="{ record }">
          <stop-flag :value="record.stopFlag"></stop-flag>
        </template>
        <!-- 操作 -->
        <template #operate="{ record }">
          <a-button size="small" type="text" style="color:#0E88F6" status="success" @click="handlePreview(record)">预览
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleCardConfig(record)">配置卡片</a-button>
          <a-button size="small" type="text" :status="record.stopFlag == 1 ? 'normal' : 'warning'"
                    @click="handleStop(record)">
            {{ record.stopFlag == 0 ? $t('global.forbidden') : $t('global.enable') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdate(record.id)">{{ $t('global.edit') }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
        </template>
      </a-table>
      <!-- 表格内容 end-->
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {Modal} from "@arco-design/web-vue";
import {BasePagination} from '@/api/common';
import {getConfigList, deleteConfigList, updateConfigStatus} from "@/api/manage/station";
import {useRouter} from 'vue-router';
import {getStationList} from '@/api/public';
import {processSelectableByCompany} from '@/utils/ruoyi';
import {notification} from "@/hooks/my-design";
import qs from "qs";


/*************************** 变量区域 begin ***************************/
//参数

//查询表单对象
const searchModel = ref({
  deptId: '',
  pageType: 1
});
//加载中
const {loading, setLoading} = useLoading(false);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
//设置表格列
const columns: any = [
  {
    title: "单位",
    dataIndex: "entName",
    slotName: "entName",
  },
  {
    title: "分组/站点",
    dataIndex: "groupName",
    slotName: "groupName",
  },
  {
    title: "模板",
    dataIndex: "pageType",
    slotName: "pageType",
  },
  {
    title: "标题",
    dataIndex: "pageName",
    slotName: "pageName",
  },
  {
    title: "启用状态",
    dataIndex: "stopFlag",
    slotName: "stopFlag",
    align: 'center',
    width: 100,
    fixed: "right"
  },
  {
    title: "操作",
    dataIndex: "operate",
    width: 320,
    slotName: 'operate',
    align: 'center',
    fixed: "right"
  },
];

const siteList = ref<any>([]);

//路由
const router = useRouter();

const renderData = ref<any>([]);
/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

// 查询条件
const search = async () => {
  pagination.current = 1;
  await fetchData();
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
 * 表格分页发生改变时触发
 * @param val
 */
const handlePageChange = (val:number) => {
  pagination.current = val;
  fetchData();
}
/**
 * 表格每页数据数量发生改变时触发
 * @param val
 */
const handlePageSizeChange = (val:number) => {
  pagination.pageSize = val;
  fetchData();
}
/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const res: any = await getConfigList({
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      ...searchModel.value,
    });
    if (res.code === 200) {
      renderData.value = res.rows
      pagination.total = res.total;
    } else {
      notification(res)
    }
  } catch (err) {

  } finally {
    setLoading(false);
  }
}

/**
 * 获取站点信息
 */
const fetchStationData = async () => {
  try {
    let res = await getStationList({});
    if (res.code == 200) {
      processSelectableByCompany(res.data)
      siteList.value = res.data;
    }
  } catch (e) {

  } finally {

  }
}

// 删除
const handleDelete = (item: any) => {
  Modal.error({
    title: '提示',
    content: `你确定要删除 ${item.pageName} 吗？`,
    hideCancel: false,
    bodyStyle: {
      textAlign: 'center'
    },
    onOk: async () => {
      try {
        setLoading(true)
        const res = await deleteConfigList(item.id)
        if (res.code == 200) {
          notification('success', '删除成功')
          await fetchData();
        }
      } catch {

      } finally {
        setLoading(false)
      }
    },
  });
}

// 新建
const handleCardConfig = (record: any) => {
  router.push({
    path: "/setting/station/dataList",
    query: {
      id: record.id,
      entName: record.entName,
      stationName: record.stationName,
      deptId: record.deptId,
    }
  });
}
// 停用
const handleStop = (record: any) => {
  const text = record.stopFlag == 1 ? '启用' : '停用';
  const state = record.stopFlag == 1 ? 0 : 1;
  Modal.warning({
    title: '提示',
    content: `你确定要${text} ${record.pageName} 吗？`,
    hideCancel: false,
    bodyStyle: {
      textAlign: 'center'
    },
    onOk: async () => {
      const data = {
        id: record.id,
        state: state
      }
      try {
        setLoading(true)
        const res = await updateConfigStatus(data)
        if (res.code == 200) {
          notification('success', text + '成功')
          await fetchData();
        }
      } catch {

      } finally {
        setLoading(false)
      }
    },
  });
}

// 预览
const handlePreview = (record: any) => {
  const params = {
    referer: window.location.href,
    stationId: record.deptId,
    isPre: 0
  }
  window.open(`${window.location.origin}/bi?${qs.stringify(params)}`, "_blank")
}
// 编辑
const handleUpdate = (id: number) => {
  router.push({
    path: "/setting/station/largeScreenDetail",
    query: {
      id
    }
  })
}

// 新建
const handleAdd = () => {
  router.push({
    path: "/setting/station/largeScreenDetail"
  })
}

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchStationData();
  await fetchData();
})
</script>

<style scoped></style>
