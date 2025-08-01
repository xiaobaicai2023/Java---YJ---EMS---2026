<template>
  <a-layout class="layout" :class="{ mobile: appStore.hideMenu }">
    <FatherMenu/>
    <!--  paddingTop: navbar ? '45px' : '', zIndex: 101 -->
    <a-layout-sider v-if="renderMenu" v-show="!hideMenu" class="layout-sider-second" breakpoint="xl" :collapsed="collapsed"
                    :collapsible="true" :width="menuWidth" :style="{ zIndex: 101 }" :hide-trigger="true"
                    @collapse="setCollapsed">
      <a-typography-title class="ykite-title" ellipsis :heading="6">
        {{ title }}
      </a-typography-title>
      <div class="menu-wrapper">
        <Menu/>
      </div>
    </a-layout-sider>
    <a-drawer v-if="hideMenu" :visible="drawerVisible" placement="left" :footer="false" mask-closable
              :closable="false" @cancel="drawerCancel">
      <Menu/>
    </a-drawer>
    <a-layout class="layout-content-box" :style="paddingStyle">
      <a-layout-header v-if="navbar" class="layout-navbar"
                       :style="{ width: `calc(100% - ${!hideMenu ? menuWidth + 55 + 'px' : 8 + 'vh'})` }">
        <NavBar :drawerVisible="drawerVisible"/>
      </a-layout-header>
      <TabBar v-if="appStore.tabBar"/>
      <a-layout-content style="padding: 15px 15px 0 15px">
        <PageLayout v-if="isRouterAlive"/>
      </a-layout-content>
      <Footer v-if="footer"/>
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
import Footer from '@/components/footer/index.vue'
import Menu from '@/components/menu/index.vue'
import FatherMenu from '@/components/menu/father/index.vue'
import NavBar from '@/components/navbar/index.vue'
import TabBar from '@/components/tab-bar/index.vue'
import usePermission from '@/hooks/permission'
import useResponsive from '@/hooks/responsive'
import {useAppStore, useUserStore, useCompanyStore} from '@/store'
import {computed, nextTick, onMounted, provide, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import PageLayout from './page-layout.vue'
import {getDict} from '@/api/public'
import {findFirstItem} from '@/utils/ruoyi'
import useThemes from '@/hooks/themes';
import qs from "qs";

const {isDark} = useThemes();
const isInit = ref(false)
const appStore = useAppStore()
const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
const permission = usePermission()
useResponsive(true)
const navbarHeight = `45px`
const navbar = computed(() => appStore.navbar)
const fatherMenu = computed(() => appStore.appAsyncFatherMenu)
const title = import.meta.env.VITE_APP_TITLE;
// const fatherMenu = ref<any>([]);
// const menu = async () => {
//   // await appStore.fetchFatherMenuConfig();
//   fatherMenu.value = appStore.appAsyncFatherMenu;
// }
// menu();
const renderMenu = computed(() => appStore.menu && !appStore.topMenu)
const hideMenu = computed(() => appStore.hideMenu)
const footer = computed(() => appStore.footer)
const menuWidth = computed(() => {
  return appStore.menuCollapse ? 48 : appStore.menuWidth
})
const collapsed = computed(() => {
  return appStore.menuCollapse
})
const paddingStyle = computed(() => {
  const paddingLeft =
      renderMenu.value && !hideMenu.value
          ? {paddingLeft: `${menuWidth.value + 55}px`}
          : {paddingLeft: `55px`}
  const paddingTop = navbar.value ? {paddingTop: navbarHeight} : {}
  return {...paddingLeft, ...paddingTop}
})
const setCollapsed = (val: boolean) => {
  if (!isInit.value) return // for page initialization menu state problem
  appStore.updateSettings({menuCollapse: val})
}
const fatherMenuActive = ref(appStore.lastIndex)
const fatherMenuActiveChange = (index: number, value: string) => {
  fatherMenuActive.value = index
  appStore.switchMenu(value);
  appStore.lastIndex = index;
  //获取第一个菜单
  let first = findFirstItem(appStore.serverMenu);
  if (first) {
    router.push({
      name: first.name,
    });
  }
}

watch(
    () => userStore.role,
    (roleValue) => {
      if (roleValue && !permission.accessRouter(route))
        router.push({name: 'notFound'})
    }
)
const drawerVisible = ref(false)
const drawerCancel = () => {
  drawerVisible.value = false
}
provide('toggleDrawerMenu', () => {
  drawerVisible.value = !drawerVisible.value
})

// 局部组件刷新
const isRouterAlive = ref(true);
provide('reload', () => {
  isRouterAlive.value = false;
  nextTick(() => {
    isRouterAlive.value = true;
  });
})

onMounted(() => {
  isInit.value = true
})
</script>

<style scoped lang="less">
@nav-size-height: 45px;
@layout-max-width: 1100px;

.layout {
  width: 100%;
  height: 100%;
}

.layout-navbar {
  position: fixed;
  top: 0;
  //left: 0;
  right: 0;
  z-index: 100;
  width: 100%;
  height: @nav-size-height;
}

.layout-sider-father {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 99;
  height: 100%;
  text-align: center;

  .layout-sider-father-logo {
    color: var(--color-text-1);
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .layout-sider-father-menu {
    cursor: pointer;
    margin-top: 2px;
    padding: 15px 10px;
    font-weight: 500;
    color: var(--color-text-1);
    background-color: rgb(var(--primary-7));
  }

  .father-menu-active {
    font-weight: 800;
    color: white;
  }
}

.layout-sider-second {
  position: fixed;
  top: 0;
  left: 55px;
  height: 100%;
  transition: all 0.2s cubic-bezier(0.34, 0.69, 0.1, 1);

  &::after {
    position: absolute;
    top: 0;
    right: -1px;
    display: block;
    width: 1px;
    height: 100%;
    background-color: var(--color-border);
    content: '';
  }

  > :deep(.arco-layout-sider-children) {
    overflow-y: hidden;
  }
}

.menu-wrapper {
  height: 100%;
  overflow: auto;
  overflow-x: hidden;

  :deep(.arco-menu) {
    ::-webkit-scrollbar {
      width: 12px;
      height: 4px;
    }

    ::-webkit-scrollbar-thumb {
      background-color: var(--color-text-4);
      background-clip: padding-box;
      border: 4px solid transparent;
      border-radius: 7px;
    }

    ::-webkit-scrollbar-thumb:hover {
      background-color: var(--color-text-3);
    }
  }

  :deep(.arco-menu-inner) {
    height: 95%;
  }
}

.layout-content-box {
  min-height: 100vh;
  overflow-y: hidden;
  background-color: var(--color-bg-2);
  transition: padding 0.2s cubic-bezier(0.34, 0.69, 0.1, 1);
  min-width: 1440px;
  overflow-x: auto;
}

:deep(.arco-menu-inner)::-webkit-scrollbar {
  display: none;
}


.level-one-content {
  flex: 1 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  overflow-y: hidden;

  > :deep(.arco-layout-sider-children) {
    overflow-y: hidden;
  }
}

.ibe-carbon-level-one {
  display: flex;
  flex-direction: column;
  font-family: PingFangSC;
  font-weight: 500;
  font-size: 12px;
  color: hsla(0, 0%, 100%, .6);
  letter-spacing: .4px;
  grid-row-gap: 10px;
  padding-top: 10px
}

.ibe-carbon-level-one .level-one-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 50px;
  justify-content: center;
  cursor: pointer
}

.ibe-carbon-level-one .level-one-item .name {
  text-align: center;
  width: 50px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-top: 5px;
}

.ibe-carbon-level-one .level-one-item .iconfont {
  font-size: 20px;
  height: 20px;
  width: 20px
}

.ibe-carbon-level-one .level-one-item .iconfont .menu-item-name-icon {
  width: 20px;
  height: 20px;
  min-height: 20px;
  max-width: 20px
}

.ibe-carbon-level-one .level-one-item-select {
  background-color: rgba(250, 251, 250, .12)
}

.ibe-carbon-level-one .level-one-item-select .select-line {
  height: 50px;
  width: 4px;
  background-color: #fff;
  border-radius: 0 100px 100px 0;
  position: absolute;
  left: 0;
  z-index: 1
}

.ibe-carbon-level-one .level-one-item:hover {
  background-color: rgba(250, 251, 250, .12)
}

.ibe-carbon-level-one-my .level-one-item {
  border-top: 1px solid rgba(250, 251, 250, .2)
}

.ykite-title {
  height: @nav-size-height;
  margin: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  text-decoration: none;
  background-color: var(--color-bg-2);
}
</style>
