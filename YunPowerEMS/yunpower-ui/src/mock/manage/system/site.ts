import Mock from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    const listData = Mock.mock({
      'list|50': [{   
      // 主键
      "key|+1": 1,
      // 设备组名称
      "groupName|3-6": /[A-Z]/,
      // 是否组
      "isGroup":true,
      'children': [
        {
          // 主键
          "key|+1": 300,
          "enterpriseName|3-6": /[A-Z]/,
          // 是否设备组
          "isDeviceGroup":true,
          'children|1-5': [{
          'key|+1': 200,
          'id|+1': 1,
          "name|3-6": /[A-Z]/,
          "siteCode|3-6":/[A-Z]/,
          "siteType|3-6":/[A-Z]/,
          "contactPerson|3-6":/[A-Z]/,
          "contactNum|11":/[0-9]/,
          "commissionDate|1":/[A-Z]/,
          "status|1":[0,1],
          "operations|3-6":/[A-Z]/,
          "isNum":true,
        }]
        },
      ],
    }]
    });


    //通道列表信息
    Mock.mock(new RegExp('/manage/system/site'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: listData.list.slice((p - 1) * ps, p * ps),
        total: 50,
      });
    });
  },
})
