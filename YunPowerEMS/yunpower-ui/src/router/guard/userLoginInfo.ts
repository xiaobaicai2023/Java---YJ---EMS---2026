import type { Router, LocationQueryRaw } from 'vue-router';
import NProgress from 'nprogress'; // progress bar
import { useRoute } from 'vue-router';

import { useAppStore, useUserStore } from '@/store';
import {clearToken, isLogin} from '@/utils/auth';
import { WHITE_LIST } from '../constants';

export default function setupUserLoginInfoGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    NProgress.start();
    const userStore = useUserStore();
    if(to.name == "login"){
      clearToken()
    }
    const route = useRoute();
    if (isLogin()) {
      if (userStore.roles.length > 0) {
        next();
      } else {
        try {
          await userStore.info();
          next();
        } catch (error) {
          await userStore.logout();
          next({
            name: 'login',
            query: {
              redirect: to.fullPath,
              ...to.query,
            } as LocationQueryRaw,
          });
        }
      }
    } else {
      const hasExit = WHITE_LIST.some((item: any) => {
        return item.name != 'notFound' && item.name === to.name;
      });
      if (hasExit) {
        next();
        return;
      }
      next({
        name: 'login',
        query: {
          redirect: to.fullPath,
          ...to.query,
        } as LocationQueryRaw,
      });
    }
  });
}
