// import Mock, { Random } from 'mockjs'
// import setupMock, {
//   successResponseWrap,
// } from '@/utils/setup-mock'
// import qs from 'query-string';
// import { GetParams } from '@/types/global'

// setupMock({
//   setup() {
//     // Mock.XHR.prototype.withCredentials = true;
//     const menuData = Mock.mock({
//       "list|50": [
//         {
//           // 主键
//           "key|+1": 1,
//           // 是否菜单组
//           "ismenuGroup":true,
//            // 菜单组名称
//           'menuName':/系统管理\d{3}/,
//           'requestAddress':"#",
//           'permissionTag|':"-",
//           'menuType|1': [
//             "目录"
//           ],
//           "status|1": [0,1],
//           // 菜单组下的菜单
//           'children|1-1':[
//             {
//               'key|+1': 200,
//               "subName": /用户管理\d{3}/,
//               "isSubGroup":true,
//               'menuName|3-6':/[A-Z]/,
//               'requestAddress|':/\/system\/[a-z]{3,4}/,
//               'permissionTag|':/system:user:[a-z]{3,4}/,
//               'menuType|1': [
//                 "菜单"
//               ],
//               "status|1": [0],
//               'children|3-5': [
//                 {
//                   'key|+1': 100,
//                   'menuName|3-6':/[A-Z]/,
//                   'requestAddress':"#",
//                   'permissionTag|':/system:user:[a-z]{3,4}/,
//                   'menuType|1': [
//                     "按钮"
//                   ],
//                   "status": 2
//                 },
//               ],
//             }
//           ]
//         },
//       ],
//     });

//     let sortValue = 1;

//     function assignSortValue(obj) {
//       obj.sort = sortValue++;
//       if (obj.children) {
//         obj.children.forEach(children => {
//           assignSortValue(children);
//         });
//       }
//     }

//     menuData.list.forEach(item => {
//       assignSortValue(item);
//     });

//     //通道列表信息
//     Mock.mock(new RegExp('/manage/account/menu'), (params: GetParams) => {
//       const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
//       const p = current as number;
//       const ps = pageSize as number;
//       return successResponseWrap({
//         list: menuData.list.slice((p - 1) * ps, p * ps),
//         total: 50,
//       });
//     });

//   },
// })
