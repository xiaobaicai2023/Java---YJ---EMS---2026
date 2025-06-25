import { defineStore } from 'pinia';
import { Notification } from '@arco-design/web-vue';
import type { NotificationReturn } from '@arco-design/web-vue/es/notification/interface';
import type { RouteRecordNormalized } from 'vue-router';
import defaultSettings from '@/config/settings.json';
import {getMenuList, logout} from '@/api/user';
import { AppState } from './types';
import router from '@/router';
import { NOT_FOUND_ROUTE, REDIRECT_MAIN } from '@/router/routes/base';

//默认模版
const DEFAULT_LAYOUT = () => import('@/layout/default-layout.vue');
//业务页面
const viewsModules = import.meta.glob('../../../views/**/*.vue');
//公共组件页面
const componentsModules = import.meta.glob('../../../components/**/*.vue');
//所有页面
const modules = { ...viewsModules, ...componentsModules };
const useAppStore = defineStore('app', {
  state: (): AppState => ({ ...defaultSettings }),
  getters: {
    appCurrentSetting(state: AppState): AppState {
      return { ...state };
    },
    appDevice(state: AppState) {
      return state.device;
    },
    appAsyncMenus(state: AppState): RouteRecordNormalized[] {
      return state.serverMenu as unknown as RouteRecordNormalized[];
    },
    appAsyncFatherMenu(state: AppState) {
      return state.serverFatherMenu;
    },
  },

  actions: {
    // Update app settings
    updateSettings(partial: Partial<AppState>) {
      // @ts-ignore-next-line
      this.$patch(partial);
    },

    // Change theme color
    toggleTheme(dark: boolean) {
      if (dark) {
        this.theme = 'dark';
        document.body.setAttribute('arco-theme', 'dark');
      } else {
        this.theme = 'light';
        document.body.removeAttribute('arco-theme');
      }
    },
    toggleDevice(device: string) {
      this.device = device;
    },
    toggleMenu(value: boolean) {
      this.hideMenu = value;
    },
    async fetchServerMenuConfig() {
      let notifyInstance: NotificationReturn | null = null;
      try {
        const res = await getMenuList();
        if (res.code !== 200 || !res.data.length) {
          await logout()
        } else {
          this.generateRoutes(res.data);
        }
      } catch (error) {
        notifyInstance = Notification.error({
          id: '菜单获取失败',
          content: 'error',
          closable: true,
        });
      }
    },
    clearServerMenu() {
      this.serverMenu = [];
      this.serverFatherMenu = [];
    },
    generateRoutes(menuList: any[]) {
      //生成路由
      const sdata = JSON.parse(JSON.stringify(menuList));
      const sidebarRoutes = filterAsyncRouter(sdata);
      this.serverFatherMenu = sidebarRoutes;
      let redirect = findFirstItemPath(sidebarRoutes, '');
      redirect = redirect.replace('//', '/');
      let baseRouter = {
        path: '/',
        name: 'layout',
        component: DEFAULT_LAYOUT,
        redirect: redirect,
        children: [...sidebarRoutes, REDIRECT_MAIN, NOT_FOUND_ROUTE],
      };
      router.addRoute(baseRouter);
    },
    activeMenu(activePath: string) {
      let children: any = [];
      this.serverFatherMenu.forEach((item) => {
        if (item.path == activePath) {
          children = item.children;
        }
      });
      if (children && children.length > 0) {
        this.serverMenu = children;
        //获取第一个路由
        return findFirstItem(children);
      }
      return '';
    },
  },
});
/**
 * 过滤路由
 * @param asyncRouterMap 路由文件
 * @returns
 */
const filterAsyncRouter = (asyncRouterMap: any) => {
  return asyncRouterMap.filter((route: any) => {
    route.component = loadView(route.component);
    route.props = loadProps(route.props);
    route.meta.requiresAuth = true;
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children);
    }
    route.redirect = null;

    return true;
  });
};

/***
 * 加载视图
 */
const reg = '../../..';
const loadView = (view: string) => {
  if (!view || view == '' || view.lastIndexOf('.vue') == -1) {
    return '';
  }
  if (view && view.length > 0) {
    let res;
    for (const path in modules) {
      if (path.split(reg)[1] === view) {
        res = () => modules[path]();
      }
    }
    return res;
  } else {
    return '';
  }
};
const loadProps = (props: string) => {
  try {
    return props && props.length > 0 ? JSON.parse(props) : undefined;
  } catch (e) {
    return {};
  }
};

/**
 * 获取第一个路由地址
 * @param data
 * @returns
 */
export function findFirstItemPath(data: any[], path: string): any | null {
  for (const item of data) {
    if (!item.children) {
      path += `/${item.path}`;
      return path;
    } else {
      path += `/${item.path}`;
      const result = findFirstItemPath(item.children, path);
      if (result) {
        return result;
      }
    }
  }
  return null;
}

/**
 * 获取第一个路由地址
 * @param data
 * @returns
 */
export function findFirstItem(data: any): any | null {
  data.sort((a: any, b: any) => {
    return (a.meta.order || 0) - (b.meta.order || 0);
  });
  data = data.filter((item:any) => item.meta?.hideInMenu !== true);
  for (const item of data) {
    if (!item.children || item.meta.hideMenu) {
      return item;
    } else {
      const result = findFirstItem(item.children);
      if (result) {
        return result;
      }
    }
  }
  return null;
}

export default useAppStore;
