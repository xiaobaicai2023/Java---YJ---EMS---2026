<template>
  <div>
    <a-space>
    <template v-for="(item, index) in props.options" >
      <template v-if="values.includes(item.value)">
        <a-tag
          :key="item.id"
          :index="index"
          :color="item.listClass ?  item.listClass : 'rgb(var(--primary-6))'"
          >{{item.label}}</a-tag
        >
      </template>
    </template>
  </a-space>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';

const props = defineProps({
  // 数据
  options: {
    type: Array,
    default: null,
  },
  // 当前的值
  value: [Number, String, Array],
  // 当未找到匹配的数据时，显示value
  showValue: {
    type: Boolean,
    default: true,
  },
})

const values = computed(() => {
  if (props.value !== null && typeof props.value !== "undefined") {
    return Array.isArray(props.value) ? props.value : [String(props.value)];
  } else {
    return [];
  }
});
</script>

<style scoped lang="less">
</style>
