import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const roleData = Mock.mock({
      'list|50': [
        {
          'id|+1': 1,
          'roleName|3-6':/[A-Z]/,
          'permissionCharacters|3-7':/[a-z]/,
          'permissionScope|1':[
            "全部企业数据权限",
            "当前企业全部数据权限",
            "本站点数据权限",
            "本站点及以下数据权限",
            "自定义数据权限"
          ],
          'sort|+1': 1,
          "status|1": [
            0,1
          ],
        },
      ],
    });

    //通道列表信息
    Mock.mock(new RegExp('/manage/account/role'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: roleData.list.slice((p - 1) * ps, p * ps),
        total: 50,
      });
    });
  },
})
