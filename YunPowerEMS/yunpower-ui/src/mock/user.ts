import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
  failResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { MockParams } from '@/types/mock'
import { isLogin } from '@/utils/auth'
import { GetParams } from '@/types/global'




setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;

    // 用户信息
    Mock.mock(new RegExp('/api/user/info'), () => {
      if (isLogin()) {
        const role = window.localStorage.getItem('userRole') || 'admin'
        return successResponseWrap({
          name: '王立群',
          avatar:
            '//lf1-xgcdn-tos.pstatp.com/obj/vcloud/vadmin/start.8e0e4855ee346a46ccff8ff3e24db27b.png',
          email: 'wangliqun@email.com',
          job: 'frontend',
          jobName: '前端艺术家',
          organization: 'Frontend',
          organizationName: '前端',
          location: 'beijing',
          locationName: '北京',
          introduction: '人潇洒，性温存',
          personalWebsite: 'https://www.arco.design',
          phone: '150****0000',
          registrationDate: '2013-05-10 12:10:00',
          accountId: '15012312300',
          certification: 1,
          role,
        })
      }
      return failResponseWrap(null, '未登录', 50008)
    })

    // // 登录
    // Mock.mock(new RegExp('/api/user/login'), (params: MockParams) => {
    //   const { username, password } = JSON.parse(params.body)
    //   if (!username) {
    //     return failResponseWrap(null, '用户名不能为空', 50000)
    //   }
    //   if (!password) {
    //     return failResponseWrap(null, '密码不能为空', 50000)
    //   }
    //   if (username === 'admin' && password === 'admin') {
    //     window.localStorage.setItem('userRole', 'admin')
    //     return successResponseWrap({
    //       token: '12345',
    //     })
    //   }
    //   if (username === 'user' && password === 'user') {
    //     window.localStorage.setItem('userRole', 'user')
    //     return successResponseWrap({
    //       token: '54321',
    //     })
    //   }
    //   return failResponseWrap(null, '账号或者密码错误', 50000)
    // })

    // // 登出
    // Mock.mock(new RegExp('/api/user/logout'), () => {
    //   return successResponseWrap(null)
    // })

    //用户的服务端菜单
    // Mock.mock(new RegExp('/api/user/menu'), () => {
    //   const menuList = [
    //     {
    //       path: '/dashboard',
    //       name: 'dashboard',
    //       component: '',
    //       meta: {
    //         locale: 'menu.dashboard',
    //         requiresAuth: true,
    //         icon: 'icon-dashboard',
    //         order: 1,
    //       },
    //       children: [
    //         {
    //           path: 'app',
    //           name: 'App',
    //           component: () => import('@/views/dashboard/app/index.vue'),
    //           meta: { requiresAuth: true, locale: 'menu.dashboard.app' },
    //         },
    //       ],
    //     }, {
    //       path: '/home',
    //       name: 'homepage',
    //       component: () => import('@/views/homepage/index.vue'),
    //       meta: { requiresAuth: true, locale: 'menu.homepage', icon: 'icon-home' },
    //     },
    //     {
    //       path: "/energy/preview",
    //       name: "energyPreview",
    //       component: () => import("@/views/power/energy/preview/index.vue"),
    //       meta: {
    //         locale: "能耗概览",
    //         requiresAuth: true,
    //         icon: "icon-dashboard",
    //         order: 1,
    //       },
    //     },
    //     {
    //       path: "/energyquery",
    //       name: "energyQuery",
    //       component:'',
    //       meta: {
    //         locale: "能耗查询",
    //         requiresAuth: true,
    //         icon: "icon-dashboard",
    //         order: 1,
    //       },
    //       children: [
    //         {
    //           path: "realtime",
    //           name: "energyQueryRealtime",
    //           component: () =>
    //             import("@/views/power/energy/query/realtime/index.vue"),
    //           meta: {
    //             locale: "实时能耗",
    //             requiresAuth: true,
    //             roles: ["*"],
    //             order: 1,
    //           },
    //         },
    //         {
    //           path: "history",
    //           name: "energyQueryHistory",
    //           component: () => import("@/views/power/energy/query/history/index.vue"),
    //           meta: {
    //             locale: "历史能耗",
    //             requiresAuth: true,
    //             roles: ["*"],
    //             order: 2,
    //           },
    //         },
    //         {
    //           path: "peak",
    //           name: "energyQueryPeak",
    //           component: () => import("@/views/power/energy/query/peak/index.vue"),
    //           meta: {
    //             locale: "峰谷数据",
    //             requiresAuth: true,
    //             roles: ["*"],
    //             order: 3,
    //           },
    //         },
    //       ],
    //     }
    //   ]
    //   return successResponseWrap(menuList)
    // })
    
    // const userData = Mock.mock({
    //   'list|55': [
    //     {
    //       'id|2': /[0-9]/,
    //       'nickName|4-8':/[A-Z]/,
    //       'trueName|4-8': /[A-Z]/,
    //       'phone|11': /[0-9]/,
    //       'status|1': [0, 1],
    //       'address|10-15':/[A-Z]/,
    //       'registerTime': Random.datetime(),
    //     },
    //   ],
    // });

    //用户信息
    Mock.mock(new RegExp('/manage/user'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: userData.list.slice((p - 1) * ps, p * ps),
        total: 55,
      });
    });
  },
})
