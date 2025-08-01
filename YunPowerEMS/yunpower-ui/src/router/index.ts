import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css";
import { createRouter, createWebHistory } from "vue-router";

import createRouteGuard from "./guard";
import { appRoutes } from "./routes";
import { DEFAULT_LAYOUT, NOT_FOUND_ROUTE, REDIRECT_MAIN } from "./routes/base";
NProgress.configure({ showSpinner: false }); // NProgress Configuration

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/login/index.vue"),
      meta: {
        requiresAuth: false,
      },
    },{
      path: "/login/demo",
      name: "loginDemo",
      component: () => import("@/views/login/login-demo.vue"),
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: "/webtopo/diagram/design/:projectId(\\d+)",
      name: "webtopoDiagramDesign",
      component: () => import("@/views/diagram/design/index.vue"),
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: "/",
      name: "layout",
      component: DEFAULT_LAYOUT,
      // redirect: "/power/energy/preview",
      children: [REDIRECT_MAIN],
    },
  ],
  scrollBehavior() {
    return { top: 0 };
  },
});
createRouteGuard(router);
export default router;
