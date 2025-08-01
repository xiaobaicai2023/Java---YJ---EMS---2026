<!--
* 功能：基础报表
* 作者：闫李壮
* 日期：2024-6-6
-->
<template>
  <div>
    <a-card>
      <div class="action-bar-view">
        <a-tabs type="rounded" @change="tabsChange" hide-content>
          <a-tab-pane v-for="item in tabsList" :key="item.id">
            <template #title>
              {{ item.reportName }}
              <a-popover position="rt" v-if="item.visibilityType === 2">
                <icon-info-circle-fill style="color: rgb(var(--red-6))"/>
                <template #content>
                  私有
                </template>
              </a-popover>
            </template>
          </a-tab-pane>
        </a-tabs>
        <a-space>
          <time-bar @change="timeChange" is-current-date-disabled
                    :date-unit="props.reportType === 2 ? ['month','year']: ['day','month','year']"/>
          <a-select v-model="searchForm.changeType" :style="{width:'220px'}" placeholder="请选择"
                    v-if="props.reportType === 1 && timeParams.timeUnit !== 0">
            <a-option value="2">平均值</a-option>
            <a-option value="3">最小值</a-option>
            <a-option value="4">最大值</a-option>
            <a-option value="5">累计值</a-option>
          </a-select>
          <a-checkbox v-model="searchForm.isCharge" v-if="reportType === 2">电费</a-checkbox>
          <a-button type="primary" @click="fetchTableData">
            查询
          </a-button>
          <a-button @click="myExport">
            <template #icon>
              <icon-to-bottom/>
            </template>
            下载
          </a-button>
        </a-space>
      </div>
      <a-table :loading="loading"
               :columns="tableInfo.header"
               :data="tableInfo.tableData"
               :bordered="{cell:true}"
               :pagination="false"/>
    </a-card>

  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {tableExport} from "@/api/download";
import {useRoute, useRouter} from "vue-router";
import useLoading from '@/hooks/loading';
import {getBasicTable, getTemplateData} from "@/api/power/energy/search/report-templates";
import {ReportTypeEnum, StationTypeEnum} from "@/api/system/device";
import {getTimeObject} from "@/utils/charts";
import dayjs from "dayjs";
import {notification} from "@/hooks/my-design";

const route = useRoute()
const router = useRouter();
const {loading, setLoading} = useLoading(true)

const props = defineProps({
  // 站点类型
  stationType: {
    type: Number,
    default: StationTypeEnum.power,
  },
  // 1基础报表 2峰谷报表
  reportType: {
    type: Number,
    default: ReportTypeEnum.basic
  }
});
/**
 * @desc 时间切换组件
 */
const timeParams = ref<any>({});
/**
 * @desc tabs数据
 */
const tabsList = ref<any[]>([]);
/**
 * @desc table数据
 */
const tableInfo: any = ref({
  header: [],
  tableData: []
});

const searchForm = ref({
  templateId: '',
  changeType: '',
  isFengGuData: props.reportType !== 1,
  isCharge: false
})
/**
 * @desc 切换tabs
 * @param key
 */
const tabsChange = (key: any) => {
  searchForm.value.templateId = key;
  fetchTableData();
};
/**
 * @desc 日期切换
 */
const timeChange = (time: any) => {
  timeParams.value = time;
  searchForm.value.changeType = '';
  if (time.timeUnit !== 0) {
    searchForm.value.changeType = '2';
  }
}
/**
 * @desc 获取模板列表
 */
const fetchTabsData = async () => {
  setLoading(true);
  try {
    const res = await getTemplateData({reportType: props.reportType, stationType: props.stationType,});
    if (res.code === 200 && res.data.length) {
      tabsList.value = res.data;
      searchForm.value.templateId = res.data[0].id;
    }
  } catch (e) {

  } finally {
    setLoading(false);
  }
};
/**
 * @desc 查询数据
 */
const fetchTableData = async () => {
  try {
    setLoading(true);
    const params = {
      ...searchForm.value,
      ...getTimeObject(timeParams.value)
    }
    if (props.reportType === 2) {
      params.changeType = 5;
    }
    const res = await getBasicTable(params);
    if (res.code === 200) {
      tableInfo.value = res.data;
      handleTableColumnsWidth(tableInfo.value.header);
      console.log(tableInfo.value.header)
    } else {
      tableInfo.value = [{ header: [],tableData: [] }];
      notification(res);
    }
  } catch (e) {
    console.log(e);
  } finally {
    setLoading(false);
  }
};
const handleTableColumnsWidth = (arr: any[]) => {
  for (let i = 0; i < arr.length; i++) {
    const item = arr[i];
    const { dataIndex, children } = item;

    if (dataIndex === 'date') {
      item.width = 155;
      item.fixed = 'left';
    } else {
      item.width = 135;
    }

    item.align = 'center';

    if (children && children.length) {
      handleTableColumnsWidth(children);
    }
  }
};

/**
 * @desc 下载
 */
const myExport = async () => {
  setLoading(true);
  const {header: column, tableData: data} = tableInfo.value;
  const result = tabsList.value.find((item: any) => item.id === searchForm.value.templateId);
  await tableExport({column, data},result.reportName);
  setLoading(false);
};
/**
 * @desc 跳转配置模板页
 * reportType:报表类型（1普通 2峰谷）
 */
const handleConfigView = () => {
  const newPath = route.path.replace(props.reportType === 1 ? '/report/basic' : '/report/pv', '/report/template');
  router.push({
    path: newPath,
    query: {
      stationType: props.stationType,
      reportType: props.reportType
    }
  })
};

onMounted(async () => {
  await fetchTabsData();
  await fetchTableData();
});
</script>

<style lang="less" scoped>
.action-bar-view {
  display: flex;
  justify-content: center;
  margin-bottom: 12px;

  .arco-tabs {
    flex: 1;
  }
}

.arco-card {
  margin-bottom: 12px;
}
</style>
