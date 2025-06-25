import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const postData = Mock.mock({
      'list|50': [
        {
          'id|+1': 1,
          'positionName|3-6':/[A-Z]/,
          'positionCode|3-7':/[a-z]/,
          'sort|+1': 1,
          "status|1": [
            0,1
          ],
          "createTime": Random.now('month'),
        },
      ],
    });

    //通道列表信息
    Mock.mock(new RegExp('/manage/account/post'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: postData.list.slice((p - 1) * ps, p * ps),
        total: 50,
      });
    });
  },
})
