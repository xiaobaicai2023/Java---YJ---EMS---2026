import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const userData = Mock.mock({
      'list|50': [
        {
          'id|+1': 1,
          'userName|3-6':/[A-Z]/,
          'loginAccount|3-7':/[a-z]/,
          'phoneNumber|1': /^1[3456789]\d{9}$/,
          'station|5-9':/[A-Z]/,
          'position|3-6':/[A-Z]/,
          "role|1": [
            "角色1",
            "角色2",
            "角色3"
          ],
          "status|1": [
            0,1
          ],
          "createTime": Random.now('month'),
          "lastTime": Random.now('day'),
        },
      ],
    });

    userData.list[0].userName = '超级管理员';

    //通道列表信息
    Mock.mock(new RegExp('/manage/account/user'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: userData.list.slice((p - 1) * ps, p * ps),
        total: 50,
      });
    });
  },
})
