<template>
  <div>
    <a-tag v-if="value == 0" color="rgb(var(--primary-6))">{{ $t('global.enable') }}</a-tag>
    <a-tag v-else  color="rgb(var(--red-6))">{{ $t('global.forbidden') }}</a-tag>
  </div>
</template>

<script lang="ts" setup>

const props = defineProps({
  // 当前的值
  value: [Number, String, Array],
})

</script>

<style scoped lang="less"></style>
