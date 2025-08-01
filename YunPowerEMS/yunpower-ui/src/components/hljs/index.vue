<template>
  <div class="hljs-container">
    <highlightjs language="JavaScript" :autodetect="false" :code="codeStr"></highlightjs>
  </div>
</template>

<script setup lang="ts">
import * as beautify from 'js-beautify';
import { onMounted, ref } from 'vue';
const props = defineProps({
  code: {
    type: String,
    default: ''
  },
  format: {
    type: Boolean,
    default: false
  },
})
const codeStr = ref<any>("");
onMounted(() => {
  if (props.format) {
    codeStr.value = beautify.js_beautify(props.code, { indent_size: 2 });
  } else {
    codeStr.value = props.code;
  }
})
</script>

<style scoped lang="less">
/* 语法高亮 */
.hljs-container {
  position: relative;
  display: block;
  width: 100%;
  height: 100%;
  padding: 20px 5px 2px;
  overflow-x: hidden;
  line-height: 20px;
  text-align: left;
  background: #21252b;
  box-shadow: 0 10px 30px 0 rgb(0 0 0 / 40%);
}

/** 3个点 */
.hljs-container::before {
  position: absolute;
  top: 10px;
  left: 15px;
  width: 12px;
  height: 12px;
  overflow: visible;
  font-weight: 700;
  font-size: 16px;
  line-height: 12px;
  white-space: nowrap;
  text-indent: 75px;
  background-color: #fc625d;
  border-radius: 16px;
  box-shadow: 20px 0 #fdbc40, 40px 0 #35cd4b;
  content: attr(codetype);
}

/** 滚动条 */
:deep(.hljs) {
  overflow-x: auto;
}

:deep(.hljs::-webkit-scrollbar) {
  width: 12px !important;
  height: 12px !important;
}

:deep(.hljs::-webkit-scrollbar-thumb) {
  height: 30px !important;
  background: #d1d8e6;
  background-clip: content-box;
  border: 2px solid transparent;
  border-radius: 19px;
  opacity: 0.8;
}

:deep(.hljs::-webkit-scrollbar-thumb:hover) {
  background: #a5b3cf;
  background-clip: content-box;
  border: 2px solid transparent;
}

:deep(.hljs::-webkit-scrollbar-track-piece) {
  width: 30px;
  height: 30px;
  background: #333;
}

::-webkit-scrollbar-button {
  display: none;
}
</style>
