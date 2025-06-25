<template>
  <div>
    <a-card class="content">
      <!-- 查询条件 start-->
      <a-row>
        <a-col :flex="1" style="margin-top: 4px">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left" auto-label-width>
            <a-row :gutter="12">
              <!--设备名称-->
              <!--<a-col :span="8">-->
              <!--<a-form-item field="catalogId" :label="$t('power.energy.device.name')">-->
              <!--<a-tree-select v-model="searchModel.deviceId" :data="renderDeviceData" placeholder="全部设备" :fieldNames="{-->
              <!--key: 'id', title: 'deviceName', children: 'children'-->
              <!--}" :allow-search="true" :allow-clear="true" :filter-tree-node="filterTreeNode"-->
              <!--@clear="handleDeviceClear" @change="handleDeviceChange"></a-tree-select>-->
              <!--</a-form-item>-->
              <!--</a-col>-->

              <!--能源类型-->
              <a-col :span="8">
                <a-form-item field="stationType" :label="$t('global.energy')">
                  <a-select v-model="searchModel.stationType" :placeholder="$t('global.pleaseSelect')" allow-clear>
                    <a-option :key="0" :label="'全部类型'" :value="Number(-100)" />
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label" :value="Number(dict.value)" />
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="state" :label="$t('pv.alarm.list.eventStatus')">
                  <a-select v-model="searchModel.triggerStatus" @clear="handleTriggerStatusClear" placeholder="全部状态" allow-clear>
                    <a-option key="-10" label="全部状态" value="-10"></a-option>
                    <a-option v-for="dict in sys_trigger_status" :key="dict.value" :label="dict.label" :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="time" :label="$t('pv.alarm.list.selectDate')">
                  <a-range-picker v-model="searchModel.time" />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 35px" direction="vertical" />
        <a-col :flex="'86px'" style="text-align: right">
          <a-space :size="18">
            <a-button type="primary" @click="onSearch">
              <template #icon>
                <icon-search />
              </template>
              {{ $t('global.search') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->
      <a-divider style="margin-top: 0" />
      <!-- 表格 start-->
      <a-table
        row-key="id"
        :loading="loading"
        :bordered="{ wrapper: true, cell: true }"
        :pagination="pagination"
        :columns="(tabColumns as TableColumnData[])"
        :data="renderData"
        @page-change="onPageChange"
        @page-size-change="onPageSizeChange"
      >
      </a-table>
      <!-- 表格 end-->
    </a-card>
    <!-- 图表 start -->
    <a-card class="general-card" style="margin-top: 12px" :title="$t('pv.alarm.list.levelStatistics')">
      <CutomChart :options="chartLevelData" autoresize height="460px" />
    </a-card>
    <a-card class="general-card" style="margin-top: 12px" :title="$t('pv.alarm.list.typeStatistics')">
      <CutomChart :options="chartCategoryData" autoresize height="460px" />
    </a-card>
    <!-- 图表 end -->
  </div>
</template>

<script lang="ts" setup>
  import { computed, ref, reactive, getCurrentInstance } from 'vue';
  import { BasePagination } from '@/api/common';
  import { TableColumnData } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import dayjs from 'dayjs';
  import { onMounted } from 'vue';
  import { getCategoryStaticList, getLevelStaticList, getStatisticlist } from '@/api/system/trigger';
  import { handleChartData } from '@/utils/charts';
  import { StationTypeEnum, listFusionGroup } from '@/api/system/device';
  import { processSelectable } from '@/utils/ruoyi';

  const props = defineProps({
    stationType: {
      type: Number,
      default: StationTypeEnum.power,
    },
  });

  //加载中
  const { loading, setLoading } = useLoading(true);

  //******* 这里编写动态获取下拉列表 start ******
  const proxy = getCurrentInstance()?.appContext.config.globalProperties;
  const { sys_alaram_level, sys_trigger_status, sys_station_type } = proxy?.useDict('sys_alaram_level', 'sys_trigger_status', 'sys_station_type');
  //******* 这里编写动态获取下拉列表 end ******

  //表格数据
  const renderData = ref<any>([]);
  //设备数据
  const renderDeviceData = ref<any>([
    {
      id: 0,
      deviceSn: '0',
      selectable: true,
      isGroup: 0,
      deviceName: '全部设备',
      children: [],
    },
  ]);
  //报警类型数据-下拉框
  const renderAlarmCategoryData = ref<any>([
    {
      id: 0,
      triggerName: '全部类型',
      triggerSn: 'all',
    },
  ]);
  //报警类型数据-下拉框-自定义字段
  const alarmCategoryfieldNames = { value: 'id', label: 'triggerName' };

  //表格分页配置
  const pagination: any = reactive({ ...BasePagination() });
  //报警级别统计图
  const chartLevelData = ref<any>([]);
  //报警类型统计图
  const chartCategoryData = ref<any>([]);
  const customColumns = ref<any>([]);
  //设置表格列
  const tabColumns = computed<TableColumnData[]>(() => customColumns.value);
  var dateNow = dayjs().format('YYYY-MM-DD');
  var dateBefore = dayjs().format('YYYY-MM-01');

  //报警事件查询项
  const searchModel = ref({
    id: 0,
    //设备名称
    deviceId: 0,
    //事件状态
    triggerStatus: '-10',
    // 选择日期
    time: [dateBefore, dateNow],
    // 能源类型
    stationType: props.stationType,
  });

  /**
   * 搜索
   */
  const onSearch = async () => {
    await fetchData();
    await fetchLevelData();
    await fetchCategoryData();
  };

  /**
   * 表格页码发生变化
   * @param val
   */
  const onPageChange = async (val: number) => {
    pagination.current = val;
    await fetchData();
  };

  /**
   * 表格每页数量发生变化
   * @param val
   */
  const onPageSizeChange = async (val: number) => {
    pagination.pageSize = val;
    await fetchData();
  };

  /**
   * 设备搜索
   * @param searchValue
   * @param nodeData
   */
  const filterTreeNode = (searchValue: any, nodeData: any) => {
    return nodeData.deviceName.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
  };

  /**
   * 选择设备
   */
  const handleDeviceChange = (val: any) => {
    if (searchModel.value.deviceId) {
      console.log(val);
    }
  };

  /**
   * 设备清空
   */
  const handleDeviceClear = () => {
    searchModel.value.deviceId = 0;
  };

  /**
   * 事件状态清空
   */
  const handleTriggerStatusClear = () => {
    searchModel.value.triggerStatus = '-10';
  };

  /**
   * 获取设备列表
   */
  const fetchDeviceData = async () => {
    try {
      let param = {
        stationType: props.stationType,
      };
      const res = await listFusionGroup(param);
      processSelectable(res.data);
      renderDeviceData.value[0].children = res.data;
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
    }
  };

  /**
   * 查询数据
   */
  const fetchData = async () => {
    setLoading(true);
    try {
      const res: any = await getStatisticlist({
        deviceId: searchModel.value.deviceId > 0 ? searchModel.value.deviceId : '',
        triggerStatus: searchModel.value.triggerStatus != '-10' ? searchModel.value.triggerStatus : '',
        // 能源类型
        stationType: searchModel.value.stationType != -100 ? searchModel.value.stationType : '',
        params: {
          beginTime: searchModel.value.time[0],
          endTime: searchModel.value.time[1],
        },
      });
      renderData.value= [];
      if (res.code == 200) {
        pagination.total = res.total;
        if (res.rows && res.rows.length > 0) {
          let columns: TableColumnData[] = [];
          let data: any = [];
          res.rows.forEach((item: any, index: number) => {
            let info: any = {};
            item.forEach((cItem: any, cIndex: number) => {
              if (index == 0) {
                columns.push({
                  title: cItem.key,
                  dataIndex: cItem.key,
                  align: cIndex <= 1 ? 'left' : 'center',
                });
              }
              info[cItem.key] = cItem.value;
            });
            data.push(info);
          });
          customColumns.value = columns;
          renderData.value = data;
        }
      }
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      setLoading(false);
    }
  };

  /**
   * 报警级别统计图
   */
  const fetchLevelData = async () => {
    try {
      let res = await getLevelStaticList({
        stationType: searchModel.value.stationType != -100 ? searchModel.value.stationType : '',
        beginTime: searchModel.value.time[0],
        endTime: searchModel.value.time[1],
        triggerStatus: searchModel.value.triggerStatus != '-10' ? searchModel.value.triggerStatus : '',
      });
      if (res.code == 200) {
        chartLevelData.value = handleChartData(res.data);
      } else {
        chartLevelData.value = [];
      }
    } catch (ex) {
    } finally {
    }
  };

  /**
   * 报警类型统计图
   */
  const fetchCategoryData = async () => {
    try {
      let res = await getCategoryStaticList({
        stationType: searchModel.value.stationType != -100 ? searchModel.value.stationType : '',
        beginTime: searchModel.value.time[0],
        endTime: searchModel.value.time[1],
        triggerStatus: searchModel.value.triggerStatus != '-10' ? searchModel.value.triggerStatus : '',
      });
      if (res.code == 200) {
        chartCategoryData.value = handleChartData(res.data);
      } else {
        chartCategoryData.value = [];
      }
    } catch (ex) {
    } finally {
    }
  };

  /**
   * 首次加载
   */
  onMounted(async () => {
    fetchDeviceData();
    await fetchData();
    await fetchLevelData();
    await fetchCategoryData();
  });
</script>
<style lang="less" scoped>
  .row-mp-30 {
    margin-top: 30px;
    padding-right: 30px;
  }

  .main-chart {
    height: 400px;
    padding: 0px;
    margin: 0px;
  }
</style>
