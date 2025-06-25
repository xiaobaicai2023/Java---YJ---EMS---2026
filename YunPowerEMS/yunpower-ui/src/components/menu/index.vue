<script setup lang="ts">
import { ref, h, computed, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { RouteRecordRaw, RouteLocationNormalized } from 'vue-router';
import { useAppStore, useCompanyStore } from '@/store';
import { listenerRouteChange } from '@/utils/route-listener';
import { openWindow, regexUrl } from '@/utils';
import useMenuTree from './use-menu-tree';
import qs from 'qs';

const { t } = useI18n();
const appStore = useAppStore();
const router = useRouter();
const route = useRoute();
const { menuTree } = useMenuTree();
const collapsed = computed({
  get() {
    if (appStore.device === 'desktop') return appStore.menuCollapse;
    return false;
  },
  set(value: boolean) {
    appStore.updateSettings({ menuCollapse: value });
  },
});

const topMenu = computed(() => appStore.topMenu);
const openKeys = ref<string[]>([]);
const selectedKey = ref<string[]>([]);

const goto = (item: RouteRecordRaw) => {
  if (item.path === 'gobi') {
    const params = {
      stationId: useCompanyStore().companyValue,
      referer: window.location.href,
      isPre: 0,
    };
    let url = `${window.location.origin}/bi?${qs.stringify(params)}`;
    window.open(url, '_blank');
    return;
  }
  // 打开外部链接
  if (regexUrl.test(item.path)) {
    openWindow(item.path);
    selectedKey.value = [item.name as string];
    return;
  }
  // 消除外部链接副作用
  const { hideInMenu, activeMenu } = item.meta as any;
  if (route.name === item.name && !hideInMenu && !activeMenu) {
    selectedKey.value = [item.name as string];
    return;
  }
  // 跳转路由
  router.push({
    name: item.name,
  });
};

const findMenuOpenKeys = (target: string) => {
  const result: string[] = [];
  let isFind = false;
  const backtrack = (item: RouteRecordRaw, keys: string[]) => {
    if (item.name === target) {
      isFind = true;
      result.push(...keys);
      return;
    }
    if (item.children?.length) {
      item.children.forEach((el) => {
        backtrack(el, [...keys, el.name as string]);
      });
    }
  };
  menuTree.value.forEach((el: RouteRecordRaw) => {
    if (isFind) return; // 性能优化
    backtrack(el, [el.name as string]);
  });
  return result;
};

const checkRootPath = (newRoute: RouteLocationNormalized) => {
  let newRootPath = getRootPath(newRoute.path);
  let currentRootPath = getRootPath(router.currentRoute.value.path);
  if (newRootPath !== currentRootPath) {
    appStore.activeMenu(newRootPath);
  }
};

const getRootPath = (path: string) => {
  let activePath = '';
  if (path !== undefined && path.lastIndexOf('/') > 0) {
    const tmpPath = path.substring(1, path.length);
    activePath = '/' + tmpPath.substring(0, tmpPath.indexOf('/'));
  }
  return activePath;
};

const setCollapse = (val: boolean) => {
  if (appStore.device === 'desktop') appStore.updateSettings({ menuCollapse: val });
};

// 监听路由变化
onMounted(() => {
  listenerRouteChange((newRoute) => {
    const { requiresAuth, activeMenu, hideInMenu } = newRoute.meta;
    if (requiresAuth && (!hideInMenu || activeMenu)) {
      checkRootPath(newRoute);
      const menuOpenKeys = findMenuOpenKeys((activeMenu || newRoute.name) as string);
      const keySet = new Set([...menuOpenKeys, ...openKeys.value]);
      openKeys.value = [...keySet];

      selectedKey.value = [activeMenu || menuOpenKeys[menuOpenKeys.length - 1]];
    }
  }, true);
});
</script>

<template>
  <a-menu
      v-model:collapsed="collapsed"
      v-model:open-keys="openKeys"
      :show-collapse-button="appStore.device !== 'mobile'"
      :auto-open="false"
      :selected-keys="selectedKey"
      :auto-open-selected="true"
      :level-indent="34"
      style="height: 100%;width:100%;"
      @collapse="setCollapse"
  >
    <template v-for="element in menuTree" :key="element.name">
      <a-sub-menu
          v-if="element.children && element.children.length !== 0"
          :key="element.name"
      >
        <template #icon>
          <result-icon :name="element.meta.icon"
                       v-if="element.meta.icon && element.meta.icon !== '#'"
                       :iconStyle="{ fontSize: '18px' }"></result-icon>
        </template>
        <template #title>
          {{ !appStore.menuFromServer ? t(element.meta.locale || '') : element.meta.locale || '' }}
        </template>
        <template v-for="child in element.children" :key="child.name">
          <a-menu-item
              v-if="!child.children || child.children.length === 0"
              :key="child.name"
              @click="goto(child)"
          >
            <template #icon>
              <result-icon :name="child.meta.icon"
                           v-if="child.meta.icon && child.meta.icon !== '#'"
                           :iconStyle="{ fontSize: '18px' }"></result-icon>
            </template>
            {{ !appStore.menuFromServer ? t(child.meta.locale || '') : child.meta.locale || '' }}
          </a-menu-item>
          <a-sub-menu
              v-else
              :key="child.name"
          >
            <template #icon>
              <result-icon :name="child.meta.icon"
                           v-if="child.meta.icon && child.meta.icon !== '#'"
                           :iconStyle="{ fontSize: '18px' }"></result-icon>
            </template>
            <template #title>
              {{ !appStore.menuFromServer ? t(child.meta.locale || '') : child.meta.locale || '' }}
            </template>
            <template v-for="subChild in child.children" :key="subChild.name">
              <a-menu-item
                  v-if="!subChild.children || subChild.children.length === 0"
                  :key="subChild.name"
                  @click="goto(subChild)"
              >
                <template #icon>
                  <result-icon :name="subChild.meta.icon"
                               v-if="subChild.meta.icon && subChild.meta.icon !== '#'"
                               :iconStyle="{ fontSize: '18px' }"></result-icon>
                </template>
                {{ !appStore.menuFromServer ? t(subChild.meta.locale || '') : subChild.meta.locale || '' }}
              </a-menu-item>
            </template>
          </a-sub-menu>
        </template>
      </a-sub-menu>
      <a-menu-item
          v-else
          :key="element.name"
          @click="goto(element)"
      >
        <template #icon>
          <result-icon :name="element.meta.icon"
                       v-if="element.meta.icon && element.meta.icon !== '#'"
                       :iconStyle="{ fontSize: '18px' }"></result-icon>
        </template>
        {{ !appStore.menuFromServer ? t(element.meta.locale || '') : element.meta.locale || '' }}
      </a-menu-item>
    </template>
  </a-menu>
</template>

<style lang="less" scoped>
:deep(.arco-menu-inner) {
  .arco-menu-inline-header {
    display: flex;
    align-items: center;
  }
}

:deep(.arco-menu-collapse-button) {
  bottom: 50px;
}
</style>
