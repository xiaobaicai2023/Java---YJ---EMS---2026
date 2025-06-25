<template>
  <!-- 一级菜单 -->
  <a-layout-sider
      class="layout-sider-father"
      :width="menuFatherWidth"
      :style="backgroundStyle"
  >
    <div class="sider-logo center-flex">
      <img alt="logo" src="@/assets/logo.png" :width="32"/>
    </div>
    <div class="layout-sider-father-list">
      <div class="layout-sider-father-list-item"
           v-for="(item, index) in renderFatherMenu"
           :key="index"
           :class="{'layout-sider-father-list-item-active': item.path === fatherMenuActive,'local-fixed': item.meta.locale=='个人'}"
           @click="fatherMenuActiveChange(item.path)">
        <div class="line" :class="{'line-active': item.path === fatherMenuActive && isDark}"
             v-if="item.path === fatherMenuActive"/>
        <div class="layout-sider-father-list-item-text center-flex"
             :class="{'layout-sider-father-list-item-text-active': item.path === fatherMenuActive && isDark}">
          <result-icon :name="item.meta.icon" default-name="icon-settings"
                       :iconStyle="{ fontSize: '20px', marginBottom: '4px' }"></result-icon>
          <div v-if="item.meta.locale!='个人'">{{ item.meta.locale }}</div>
        </div>
      </div>
    </div>
  </a-layout-sider>
</template>
<script lang="ts" setup>
import {computed, ref} from 'vue';
import {useAppStore} from '@/store';
import useThemes from '@/hooks/themes';
import {useRouter} from 'vue-router';
import {openWindow, regexUrl} from '@/utils';

const router = useRouter();
const {isDark} = useThemes();
const appStore = useAppStore();
const menuFatherWidth = ref<number>(55);
const renderFatherMenu = computed(() => appStore.appAsyncFatherMenu);
/**
 * 当前选中的值
 * */
const fatherMenuActive = computed(() => {
  const path = router.currentRoute.value.path;
  let activePath = path;
  if (path !== undefined && path.lastIndexOf('/') > 0) {
    const tmpPath = path.substring(1, path.length);
    activePath = '/' + tmpPath.substring(0, tmpPath.indexOf('/'));
  }
  activeMenu(activePath);
  return activePath;
});

/**
 * 激活菜单
 */
const activeMenu = (activePath: string) => {
  return appStore.activeMenu(activePath);
};

/**
 * 切换父菜单
 * */
const fatherMenuActiveChange = (value: string) => {
  if (regexUrl.test(value)) {
    openWindow(value);
    return;
  } else {
    let routeInfo = activeMenu(value);
    router.push({name: routeInfo.name})
  }
};

/**
 * 一级菜单背景
 * */
const backgroundStyle = computed(() => {
  const background = isDark.value
      ? 'var(--color-bg-3)'
      : 'linear-gradient(rgb(var(--primary-6)), rgb(var(--primary-5)))';
  return {background};
});
</script>

<style scoped lang="less">
.center-flex {
  display: flex;
  align-items: center;
  justify-content: center;
}

.layout-sider-father {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 99;
  height: 100%;
  text-align: center;

  .sider-logo {
    color: var(--color-text-1);
    height: 45px;

    img {
      width: 32px;
    }
  }

  .layout-sider-father-list {
    flex: 1;

    .layout-sider-father-list-item {
      cursor: pointer;
      position: relative;
      height: 50px;
      margin-bottom: 10px;
      font-weight: 500;
      color: hsla(0, 0%, 100%, 0.6);

      &:hover {
        background-color: rgba(250, 251, 250, 0.12);
      }

      .line {
        height: 50px;
        width: 4px;
        background-color: #fff;
        border-radius: 0 100px 100px 0;
        position: absolute;
        left: 0;
        z-index: 1;
      }

      .line-active {
        background-color: rgb(var(--primary-6));

      }

      .layout-sider-father-list-item-text {
        height: 100%;
        font-size: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        flex-direction: column;
        white-space: nowrap;
      }

      .layout-sider-father-list-item-text-active {
        color: rgb(var(--primary-6));
      }
    }

    .layout-sider-father-list-item-active {
      color: #fff;
      font-weight: 800;
      background-color: #fafbfa1f;
    }
  }
}

.local-fixed {
  position: absolute !important;
  left: 0;
  bottom: 0;
  z-index: 1000;
  width: 100%;
}

:deep(.arco-layout-sider-children) {
  overflow: -moz-scrollbars-none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
  scrollbar-width: none; /* Firefox */
  overflow-y: scroll; /* 保持垂直滚动条可用 */
}

:deep(.arco-layout-sider-children::-webkit-scrollbar) {
  display: none;
}
</style>
