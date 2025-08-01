import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const authorityData = Mock.mock({
      "list|50": [
        {
          // 主键
          "key|+1": 1,
          // 站点组名称
          "mainName": /创新园\d{3}/,
          // 是否站点组
          "isAuthorityGroup":true,
          // 站点组下的站点
          'children|1-1':[
            {
              'key|+1': 200,
              "subName": /公司\d{3}/,
              "isSubGroup":true,
              'children|3-5': [
                {
                  'key|+1': 100,
                  'id|+1': 1,
                  'stationName|3-6':/[A-Z]/,
                  'stationCode|3-7':/[a-z]/,
                  'stationType|1': [
                    "用电","光伏"
                  ],
                  "status|1": [
                    0,1
                  ],
                  "createTime": Random.now('month'),
                },
              ],
            }
          ]
        },
      ],
    });

    //通道列表信息
    Mock.mock(new RegExp('/manage/account/authority'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: authorityData.list.slice((p - 1) * ps, p * ps),
        total: 50,
      });
    });

  },
})
