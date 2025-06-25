<template>
  <a-trigger trigger="click" :popup-translate="[155, 10]">
    <slot>
      <a-button>Click Me</a-button>
    </slot>
    <template #content>
      <div class="icon-box">
        <div class="icon-header">
          <a-input v-model="searchVal" placeholder="选择图标" allow-clear/>
        </div>
        <div class="icon-list">
          <div class="icon-list-item" :class="{'active-icon': activeIconName === name}" v-for="(name,index) in myIcons"
               :key="index" @click="handleSelect(name)">
            <component v-if="!name.includes('icon-ykite')" :is="name" style="font-size: 24px"></component>
            <component v-else is="custom-icon" style="font-size: 24px" :type="name"></component>
          </div>
        </div>
      </div>
    </template>
  </a-trigger>
</template>

<script setup lang="ts">
import ArcoVueIcon from '@arco-design/web-vue/es/icon';
import iconList from "@/components/select-icon/index";
import {computed, onMounted, ref} from "vue";

const props = defineProps({
  name: {
    type: String,
    default: ''
  }
})
const emit = defineEmits(['change'])

// 选中的icon
const activeIconName = ref<string>('')
// 搜索
const searchVal = ref('')
// 获取格式化后的 icon
const formatIcons: any[] = []
// 大驼峰格式为 -
const formatName = (str: string): string => {
  return str.replace(/([A-Z])/g, function (match) {
    return '-' + match.toLowerCase();
  }).slice(1);
}
for (const key in ArcoVueIcon) {
  if (key !== 'install') {
    formatIcons.push(formatName(ArcoVueIcon[key].name))
  }
}

const myIcons = computed(() => {
  if (!searchVal.value) return formatIcons.concat(iconList);
  const filteredFormatIcons = formatIcons.filter(item => item.indexOf(searchVal.value) !== -1);
  const filteredIconFont = iconList.filter(item => item.indexOf(searchVal.value) !== -1);
  return [...filteredFormatIcons, ...filteredIconFont];
});

// 传递父组件 icon 名字
const handleSelect = (iconName: string) => {
  activeIconName.value = iconName
  emit('change', iconName)
}

onMounted(() => {
  activeIconName.value = <string>props.name
})
</script>

<style lang="less" scoped>
.icon-box {
  border: 1px solid var(--color-border-1);
  background-color: var(--color-bg-popup);

  .icon-header {
    padding: 10px 10px;
  }
}

.icon-list {
  width: 400px;
  height: 400px;
  overflow-y: auto;
  padding: 0 10px;

  .icon-list-item {
    display: inline-block;
    padding: 5px;

    svg {
      color: var(--color-text-2);
    }
  }

  .icon-list-item:hover {
    svg {
      color: white;
    }

    background: rgb(var(--primary-6));
  }

  .active-icon {
    svg {
      color: white;
    }

    background-color: rgb(var(--primary-6));
  }
}

// 隐藏 图标滚动条
.icon-list::-webkit-scrollbar {
  display: none;
}
</style>
