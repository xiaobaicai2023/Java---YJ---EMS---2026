<template>
  <a-card class="time-bar-card">
    <a-space>
      <a-tree-select
          v-model="selectTreeNode"
          :data="treeData"
          placeholder="请选择设备"
          style="width: 300px"
          :fieldNames="{
            key: 'deviceSn', title: 'deviceName', children: 'children'
          }"
          :allow-search="true"
          allow-clear
      ></a-tree-select>
      <time-bar @change="timerChange" isCurrentDateDisabled />
      <a-button type="primary" @click="searchConfig">查询</a-button>
    </a-space>
  </a-card>
</template>

<script setup lang="ts">

import {onMounted, ref} from "vue";
import {listFusionGroup} from "@/api/system/device";

const emit = defineEmits(['search']);

const props = defineProps({
  stationType: {
    type: Number,
    default: 1
  }
})
// 树 数据
const treeData = ref([]);
// 树 选中节点
const selectTreeNode = ref('');
const time = ref<any>({});
const getTreeData = async () => {
  const res = await listFusionGroup({
    stationType: props.stationType
  })
  if (res.code === 200) {
    res.data.forEach((item:any) => {
      item.selectable = false;
    })
    treeData.value = res.data
  }
};
/**
 * 时间控件改变
 * @param timeData
 */
const timerChange = (timeData: any) => {
  time.value = timeData
};
/**
 * 获取设备列表
 */
const searchConfig = () => {
  const params = {
    deviceSn: selectTreeNode.value,
    ...time.value
  }
  emit('search', params)
};
// 默认选中第一个
const setDefaultNode = () => {
  const treeNode:any = treeData.value;
  if (treeNode.length && treeNode.length > 0) {
    const firstNode = treeNode[0];
    if (firstNode?.children && firstNode.children.length > 0) {
      selectTreeNode.value = firstNode.children[0].deviceSn;
      searchConfig();
    }
  }
}
onMounted(async () => {
  await getTreeData();
  setDefaultNode()
});
</script>

<style scoped lang="less">

</style>
