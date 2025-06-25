<!--
 * 功能：表格
 * 作者：闫李壮
 * 日期：2024-06-04
-->
<template>
  <div class="table-list-view">
    <ul class="table-list-header" :style="tableCss.columns">
      <li v-for="(item, index) in newColumns" :key="index"
          :class="preview==2?'list-header-preview':'table-list-header-default'"
          :style="{ width: item.title=='时间'? `calc(120% / ${columns.length})`:item.title=='事件'? `calc(220% / ${columns.length})`:item.title=='操作' ||item.title=='状态' ?`calc(70% / ${columns.length})` :`calc(100% / ${columns.length})` }">
        <a-typography-paragraph :style="{ width: '100%', color: tableCss.columns.color }"
                                :ellipsis="{ rows: 1, showTooltip: true }" class="table-list-text">
          {{ item.title }}
        </a-typography-paragraph>
      </li>
    </ul>
    <TransitionGroup name="list" tag="ul" class="table-list-body">
      <li v-for="item in data" :key="item.id" :class="{ openHover: tableCss.data.background }">
        <template v-for="(headItem, headIndex) in columns">
          <a-typography-paragraph
              v-if="headItem.dataIndex!='id'"
              :ellipsis="{ rows: 1, showTooltip: true }"
              :style="{ width: headItem.title=='时间'?`calc((120% / ${columns.length}))`:headItem.title=='事件'? `calc(220% / ${columns.length})`: headItem.title=='操作' ||headItem.title=='状态'?`calc(70% / ${columns.length})`: `calc(100% / ${columns.length})`, color: tableCss.data.color }"
              class="table-list-text">
            {{ item[headItem.dataIndex] }}
          </a-typography-paragraph>
        </template>
      </li>
    </TransitionGroup>
  </div>
</template>
<script lang="ts" setup>

import {computed, watch, watchEffect} from "vue";

const props = defineProps({
  data: {
    type: Array,
    default: () => []
  },
  columns: {
    type: Array,
    default: () => []
  },
  tableCss: {
    type: Object,
    default: () => ({
      columns: {
        color: 'var(--color-text-1)',
        background: ''
      },
      data: {
        color: 'var(--color-text-1)',
        background: ''
      }
    })
  },
  preview: {
    type: Number,
    default: 0
  }
})
const newColumns = computed(() => {
  const result: any = [];
  props.columns.filter((item: any) => {
    if (item.title && item.title != '编号') {
      result.push(item)
    }
  })
  return result
})

</script>
<style lang="less" scoped>
.table-list-view {
  height: 100%;
  overflow: hidden;

  ul {
    list-style: none;
    padding: 0;
    margin: 0;
  }

  .table-list-header, .table-list-body li {
    display: flex;
    align-items: center;
    justify-content: space-between;
    text-align: left;
    padding-right: 10px;
    padding-left: 16px;
    box-sizing: border-box;
  }
}

.table-list-header-default {
  font-weight: bold;
  padding-top: 6px;
  padding-bottom: 4px;
}

.list-header-preview {
  font-weight: bold;
  padding-top: 0;
  padding-bottom: 0;
}

.table-list-text {
  margin-bottom: 0;
}

.openHover:hover {
  background: rgba(52, 144, 227, 0.05);
}

.list-move, /* 对移动中的元素应用的过渡 */
.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

/* 确保将离开的元素从布局流中删除
  以便能够正确地计算移动的动画。 */
.list-leave-active {
  position: absolute;
}

.arco-empty {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
</style>
