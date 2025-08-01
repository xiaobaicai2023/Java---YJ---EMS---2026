<template>
  <a-layout-footer class="footer">
    <a-space>
      <span>{{VITE_COPYRIGHT.copyright}}</span>
      <a-link href="https://beian.miit.gov.cn/">{{VITE_COPYRIGHT.record}}</a-link>
    </a-space>
  </a-layout-footer>
</template>

<script lang="ts" setup>
import {onMounted} from "vue";

const VITE_COPYRIGHT = JSON.parse(import.meta.env.VITE_COPYRIGHT);
</script>

<style lang="less" scoped>
  .footer {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 40px;
    color: var(--color-text-2);
    text-align: center;
  }
</style>
