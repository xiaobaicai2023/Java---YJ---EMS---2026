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
      'list|55': [
        {
          'id|2': /[0-9]/,
          'nickName|4-8':/[A-Z]/,
          'trueName|4-8': /[A-Z]/,
          'phone|11': /[0-9]/,
          'status|1': [0, 1],
          'address|10-15':/[A-Z]/,
          'registerTime': Random.datetime(),
        },
      ],
    });

    const deviceGroupData = Mock.mock({
      'list|100': [
        {
          'id|+1': 1,
          'categoryName|3-5':/[A-Z]/,
          'logicCode|4-8': /[A-Z]/,
          'status|1': [0, 1]
        },
      ],
    });

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

    //用户信息
    Mock.mock(new RegExp('/manage/device/group/'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: deviceGroupData.list.slice((p - 1) * ps, p * ps),
        total: 100,
      });
    });
  },
})
