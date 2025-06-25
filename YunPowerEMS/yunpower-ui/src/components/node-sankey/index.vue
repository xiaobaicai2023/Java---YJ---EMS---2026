<template>
  <a-collapse class="card-collapse" v-for="(item, index) in list" :key="index" :default-active-key="['1']">
    <a-collapse-item :header="`子数据${item.name}`" key="1">
<!--      {{item}}-->
      <a-form-item field="name" label="选择变量">
        <a-input-search search-button>
          <template #button-icon>
            <icon-search />
          </template>
          <template #button-default> 选择变量 </template>
        </a-input-search>
      </a-form-item>
      <a-form-item field="name" label="变量编码">
        <a-input />
      </a-form-item>
      <a-form-item label="子数据区">
        <a-space style="width: 100%" direction="vertical" fill :size="16">
          <a-button type="primary" @click="handleAddChildList(item,index)">
            <template #icon>
              <icon-plus />
            </template>
            添加子数据区
          </a-button>
          <node-sankey :list="item.children" :level="item.level + 1" />
        </a-space>
      </a-form-item>
    </a-collapse-item>
  </a-collapse>
</template>

<script setup lang="ts">
import {defineComponent} from "vue";

const props = defineProps({
  list: {
    type: Array,
    default: []
  },
  level: {
    type: String,
    default:[]
  }
})

const emit = defineEmits(["handleAddChildList"])


const handleAddChildList = (parent) => {
  let obj = {
    a: 1,
    children: []
  }
  if (parent.children) {
    let childName = `${parent.children.length + 1}`;
    let childLevel = parent.level + 1;
    parent.children.push({
      name: `${parent.name}-${childName}`,
      level: childLevel,
      children: []
    });
  } else {
    console.log("Cannot add child item to this menu item.");
  }
  // emit("handleAddChildList", {obj,index})
}
</script>
<script lang="ts">
export default {
  name: 'node-sankey',
}

</script>
<style scoped>

</style>