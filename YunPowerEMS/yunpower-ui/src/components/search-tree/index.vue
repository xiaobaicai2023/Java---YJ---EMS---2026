<template>
  <a-card class="tree-card" :title="title" :header-style="{ borderBottom: 0 }"
    :body-style="{ padding: '0px 10px 10px 10px' }">
    <!-- <div id="search-radio">
      <slot />
    </div> -->
    <div class="tree-container border">
      <div class="tree-box">
        <a-input-search v-if="props.search" placeholder="请输入关键词" style="margin-bottom: 8px;" v-model="searchKey" />
        <!-- 树 start-->
        <a-spin :loading="loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
          <a-tree class="search-tree" ref="treeRef" v-model:selected-keys="selectedKeys"
            v-model:checked-keys="checkedKeys" :data="treeData2" :checkable="props.multiple" @select="handleSelect"
            @check="handleTreeCheck" :fieldNames="fieldNames">
            <template #icon="{ node }">
              <icon-storage v-if="node.isGroup == 1" />
              <icon-thunderbolt v-else />
            </template>
          </a-tree>
        </a-spin>
        <!-- 树 end-->
      </div>
      <!-- 复选框 start -->
      <div v-if="props.multiple" class="all-check-box">
        <a-checkbox :value="checkedAllValue" @change="handleCheckedAll">全选</a-checkbox>
      </div>
      <!-- 复选框 end -->
    </div>
    <!-- 时间选择框 start -->
    <div class="section-box" v-if="props.time">
      <div class="label">时间设置</div>
      <a-radio-group v-model="extraActive" type="button" style="width: 100%; text-align: center;" @change="radioChange">
        <a-radio :value="0">
          {{$t('power.energy.preview.day')}}
        </a-radio>
        <a-radio :value="1">
          {{$t('power.energy.preview.month')}}
        </a-radio>
        <a-radio :value="2">
          {{$t('power.energy.preview.year')}}
        </a-radio>
      </a-radio-group>
      <div style="margin-top:16px;" v-if="props.pickType === 0">
        <component :is="extraActive === 0 ?'a-date-picker' : extraActive === 1 ? 'a-month-picker' : 'a-year-picker'"
          v-model="pickerVal" style="width: 100%;" :allow-clear="false" @change="pickerChange"></component>
      </div>
      <div style="margin-top: 16px" v-if="props.pickType === 1">
        <a-range-picker :allow-clear="false" v-model="rangePickerVal"
          :mode="extraActive === 0 ? 'date' : extraActive === 1 ? 'month' : 'year'" @change="onRangeChange" />
      </div>
    </div>
    <!-- 时间选择框 end -->


  </a-card>
  <a-card v-if="props.search">
    <a-button type="primary" long @click="onSearch">查询</a-button>
  </a-card>
</template>

<script setup lang="ts">
import { TreeInstance } from '@arco-design/web-vue';
import { onMounted } from 'vue';
import { computed, ref } from 'vue'
import { StationTypeEnum, listFusionGroup } from '@/api/system/device';
import dayjs from 'dayjs';
import useLoading from '@/hooks/loading';

/**
 * @description 单向数据流
 * @param {Array} data 树形数据
 * @param {Boolean} multiple 是否多选
 * @param {Boolean} search 是否可搜索
 */
const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  // 树形数据
  data: {
    type: Array,
    default: () => []
  },
  // 是否多选
  multiple: {
    type: Boolean,
    default: false
  },
  // 是否可搜索
  search: {
    type: Boolean,
    default: false
  },
  checkStrictly: {
    type: Boolean,
    default: false
  },// 是否可搜索
  time: {
    type: Boolean,
    default: false
  },
  pickType: {
    type: Number,
    default: 0
  },
  listStyle: {
    type: Number,
    default: 0
  },
  stationType: {
    type: Number,
    default: StationTypeEnum.power
  },
  needCheck: {
    type: Boolean,
    default: true,
  }
})
/**
 * @description 父组件 自定义函数
 *  @param onSelect 点击节点触发
 *  @param onSearch 搜索触发
 */

//加载中
const { loading, setLoading } = useLoading(false);

const emit = defineEmits(["onChange", "onSelect", "onSearch"]);
// 树 实例
const treeRef = ref<TreeInstance>()
// 搜索关键字
const searchKey = ref('')
// 选中树的值
const selectedKeys = ref<any>([])
// 选择复选框的值
const checkedKeys = ref<any>([])
//选中的设备号集合
const checkedDeviceSnIds = ref<any>([])
// 选择的日期单位
const extraActive = ref<string | number | boolean>(0)
// 日期选择器 - 值
const pickerVal = ref<any>(dayjs().format("YYYY-MM-DD"))
// 范围选择器 - 值
const rangePickerVal = ref<string[]>([dayjs().startOf('month').format("YYYY-MM-DD"), dayjs().format("YYYY-MM-DD")])
// 日-月-年 change
const radioChange = (index: string | number | boolean) => {

  extraActive.value = index
  if (props.pickType === 0) {
    pickerVal.value = extraActive.value == 0 ? dayjs().format("YYYY-MM-DD") : extraActive.value == 1 ? dayjs().format("YYYY-MM") : dayjs().format("YYYY")
  } else {
    if (extraActive.value == 0) {
      rangePickerVal.value = [dayjs().startOf('month').format("YYYY-MM-DD"), dayjs().format("YYYY-MM-DD")]
    } else if (extraActive.value == 1) {
      rangePickerVal.value = [dayjs().startOf('year').format("YYYY-MM"), dayjs().format("YYYY-MM")]
    } else {
      rangePickerVal.value = [dayjs().format("YYYY"), dayjs().format("YYYY")]
    }
  }
  onChange();
}
// 日期选择
const pickerChange = (val: string) => {
  onChange();
}
const onRangeChange = (val: any) => {
  onChange();
}


// 是否全选
const checkedAllValue = ref<boolean>(false);

/**
 * 选中
 */
const handleTreeCheck = (checkedKeys: any, data: any) => {
  console.log("handleTreeCheck", checkedKeys, data);
  if (data.checkedNodes.length == 0) {
    checkedDeviceSnIds.value = [];
  } else {
    let ids: any = [];
    data.checkedNodes.forEach((item: any) => {
      if (item.isGroup == 0) {
        ids.push(item.deviceSn);
      }
    })
    checkedDeviceSnIds.value = ids;
  }
  console.log("checkedDeviceSnIds", checkedDeviceSnIds.value)
  if (props.multiple) {
    onChange();
  }
}

/**
 * 全选 handleCheckedAll
 */
const handleCheckedAll = (val: any) => {
  treeRef.value?.checkAll(val);
}

/**
 * @description 过滤数据方法
 * @param keyword 关键字
 */
function filterData(keyword: any) {
  const loop = (data: any) => {
    const result: any[] = [];
    data.forEach((item: { title: string; children: any; }) => {
      if (item.title.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
        result.push({ ...item });
      } else if (item.children) {
        const filterData = loop(item.children);
        if (filterData.length) {
          result.push({
            ...item,
            children: filterData
          })
        }
      }
    })
    return result;
  }

  return loop(props.data);
}

// tree 点击节点触发
const handleSelect = (node: any, data: any) => {
  console.log("handleSelect", selectedKeys.value, data)
  if (!props.multiple) {
    if (data.selectedNodes && data.selectedNodes.length == 1) {
      let info = data.selectedNodes[0];
      if (info.isGroup == 1) {
        selectedKeys.value = [];
      } else {
        onChange();
      }
    }
  } else {
    selectedKeys.value = [];
  }
}

// 查询
const onSearch = () => {
  emit('onSearch', { checkedKeys: checkedKeys.value })
}

/**
 * 获取时间
 */
const getRangePickerTime = () => {
  if (props.pickType === 1 && rangePickerVal.value) {
    if (extraActive.value == 0) {
      return {
        beginTime: rangePickerVal.value[0],
        endTime: rangePickerVal.value[1]
      }
    } else if (extraActive.value == 1) {
      return {
        beginTime: dayjs(rangePickerVal.value[0]).format("YYYY-MM-DD"),
        endTime: getLastDayOfMonth(rangePickerVal.value[1])
      }
    } else {
      return {
        beginTime: dayjs(`${rangePickerVal.value[0]}-01-01`).format("YYYY-MM-DD"),
        endTime: getLastDayOfYear(rangePickerVal.value[1])
      }
    }
  }
}

/**
 * 发生改变
 */
const onChange = () => {
  let params = {
    deviceSn: selectedKeys.value.length > 0 ? selectedKeys.value[0] : '',
    deviceSns: checkedDeviceSnIds.value.length > 0 ? checkedDeviceSnIds.value : [],
    timeUnit: extraActive.value,
    dayTime: props.pickType === 0 && extraActive.value == 0 ? pickerVal.value : null,
    monthTime: props.pickType === 0 && extraActive.value == 1 ? pickerVal.value : null,
    yearTime: props.pickType === 0 && extraActive.value == 2 ? pickerVal.value : null,
    ...getRangePickerTime()
  }

  console.log("search-tree onchange", params)
  emit('onChange', params)
}

const getLastDayOfYear = (year: any) => {
  // 获取某年的第一天
  const firstDayOfYear = dayjs(`${year}-01-01`);

  // 将该日期设置为下一年的第一天
  const firstDayOfNextYear = firstDayOfYear.add(1, 'year');

  // 将该日期减去一天
  const lastDayOfYear = firstDayOfNextYear.subtract(1, 'day');

  return dayjs(lastDayOfYear).format("YYYY-MM-DD");
}

const getLastDayOfMonth = (dateString: any) => {
  // 使用 dayjs 函数创建一个日期对象，并将其设置为某月的第一天
  const firstDayOfMonth = dayjs(dateString);

  // 将该日期设置为下一个月的第一天
  const firstDayOfNextMonth = firstDayOfMonth.add(1, 'month');

  // 将该日期减去一天
  const lastDayOfMonth = firstDayOfNextMonth.subtract(1, 'day');

  return dayjs(lastDayOfMonth).format("YYYY-MM-DD");
}

const treeData2 = ref<any>();

const fieldNames = ref<any>({
  key: 'deviceSn',
  title: 'deviceName',
  children: 'children',
  // isGroup:'isGroup'
  // disabled:'isGroup',
  // disableCheckbox:'isGroup'
});


function findFirstNonGroup(data: any[]): any | null {
  for (const item of data) {
    if (item.isGroup === 0) {
      return item;
    } else if (item.children) {
      const result = findFirstNonGroup(item.children);
      if (result) {
        return result;
      }
    }
  }
  return null;
}

/**
 * 查询数据
 */
const fetchData = async () => {
  setLoading(true)
  try {
    let params: any = {
      stationType: props.stationType
    };
    if (props.listStyle > 0) {
      params.listStyle = props.listStyle;
    }
    let res = await listFusionGroup(params);
    if (res.code == 200 && res.data && res.data.length > 0) {
      treeData2.value = res.data;
      if (props.needCheck) {
        const firstNonGroup = findFirstNonGroup(res.data);
        if (firstNonGroup) {
          if (props.multiple) {
            checkedKeys.value.push(firstNonGroup.deviceSn);
            checkedDeviceSnIds.value.push(firstNonGroup.deviceSn);
          } else {
            selectedKeys.value.push(firstNonGroup.deviceSn)
          }
        }
      }
    }
  } catch (error) {

  }
  finally {
    setLoading(false)
  }

}

onMounted(async () => {
  await fetchData();
  treeRef.value?.expandAll(true);
  //选中第一个设备
  if(props.needCheck){
    onChange();
  }
});

</script>

<style lang="less" scoped>
.tree-card {

  .tree-container {
    border-radius: 2px;

    .tree-box {
      margin: 5px;
      white-space: nowrap !important;
      overflow-x: auto !important;
      max-height: 600px;


    }

    .all-check-box {
      margin-top: 20px;
      padding: 10px 0 10px 15px;
      border-top: 1px solid var(--color-neutral-4);
    }
  }
}


:deep(.section-box) {
  .label {
    margin: 10px 0;
  }
}

.footer {
  margin-top: 10px;
}

.border {
  border: 1px solid var(--color-neutral-4);
}

:deep(#search-radio) {
  width: 100%;
  margin-bottom: 10px;

  .arco-radio-group-button {
    width: 100%;

    .arco-radio-button {
      width: 50%;
      text-align: center;
    }
  }
}

:deep(.arco-tree-node-selected) {
  .arco-tree-node-title {
    background-color: var(--color-fill-2);
    border-color: transparent;
  }

}
</style>
