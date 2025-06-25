import type { Router, RouteRecordNormalized } from 'vue-router';
import NProgress from 'nprogress'; // progress bar

import usePermission from '@/hooks/permission';
import { useUserStore, useAppStore } from '@/store';
import { appRoutes } from '../routes';
import { NOT_FOUND } from '../constants';
import { isLogin } from '@/utils/auth';
export default function setupPermissionGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const appStore = useAppStore();
    const userStore = useUserStore();
    const Permission = usePermission();
    const permissionsAllow = Permission.accessRouter(to);
    if (isLogin() && appStore.menuFromServer) {
      await userStore.info();
      if (!appStore.appAsyncFatherMenu.length) {
        await appStore.fetchServerMenuConfig();
        next({ ...to, replace: true })
      }else{
        next();
      }
    } else {
      // eslint-disable-next-line no-lonely-if
      if (permissionsAllow) next();
      else {
        const destination =
          Permission.findFirstPermissionRoute(appRoutes, userStore.roles) ||
          NOT_FOUND;
        next(destination);
      }
    }
    NProgress.done();
  });
}
