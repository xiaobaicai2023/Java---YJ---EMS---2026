import Mock from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    const listData = Mock.mock({
      'list|50': [
        {
          'id|+1': 1,
          "name|3-6": /[A-Z]/,
          "socialCreditCode|3-6":/[A-Z]/,
          "enterpriseLegalPerson|3-6":/ss[A-Z]/,
          "phoneNumber|11":/[0-9]/,
          "establishDate|3-6":/[A-Z]/,
          "alarmSwitch":true,
          "status|1":[0,1],
          "operations|3-6":/[A-Z]/,
        },
      ],
    });


    //通道列表信息
    Mock.mock(new RegExp('/manage/system/enterprise'), (params: GetParams) => {
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
