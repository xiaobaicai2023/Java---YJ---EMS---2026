<template>
  <template v-if="name?.includes('icon-ykite')">
    <custom-icon :type="name" :style="iconStyle"></custom-icon>
  </template>
  <template v-else>
    <component :is="name" :style="iconStyle"></component>
  </template>
</template>
<script setup lang="ts">
defineProps({
  name: {
    type: String,
    default: ''
  },
  defaultName: {
    type: String,
    default: ''
  },
  iconStyle: {
    type: Object,
    default: () => ({})
  }
})
</script>
<style scoped lang="less">

</style>