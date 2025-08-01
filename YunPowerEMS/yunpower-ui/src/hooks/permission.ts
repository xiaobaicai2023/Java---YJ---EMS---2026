import { RouteLocationNormalized, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store'

export default function usePermission() {
  const userStore = useUserStore()
  return {
    accessRouter(route: RouteLocationNormalized | RouteRecordRaw) {
      return (
        !route.meta?.requiresAuth ||
        !route.meta?.roles ||
        route.meta?.roles?.includes('*')
      )
    },
    findFirstPermissionRoute(_routers: any, roles: string[]) {
      const cloneRouters = [..._routers];
      while (cloneRouters.length) {
        const firstElement = cloneRouters.shift();
        if (firstElement?.meta?.roles == roles)
          return { name: firstElement.name };
        if (firstElement?.children) {
          cloneRouters.push(...firstElement.children);
        }
      }
      return null;
    },
    // You can add any rules you want
  }
}
