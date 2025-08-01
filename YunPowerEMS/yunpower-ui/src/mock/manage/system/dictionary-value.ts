import Mock from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    const listData = Mock.mock({
      'list|3': [
        {
          'id|+1': 1,
          "sex|1": ['男', '女', '未知'],
          "value|3-6":/[A-Z]/,
          "dictionarySort|1":/[1-3]/,
          "dictionarySpecification|3-6":/ss[A-Z]/,
          "status|1":[0,1],
          "operations|3-6":/[A-Z]/,
        },
      ],
    });


    //通道列表信息
    Mock.mock(new RegExp('/manage/system/value'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: listData.list.slice((p - 1) * ps, p * ps),
        total: 3,
      });
    });
  },
})
