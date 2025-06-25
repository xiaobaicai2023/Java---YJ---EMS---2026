import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const listData = Mock.mock({
      'list|50': [
        {
          'id|+1': 1,
          'channelName|3-6':/[A-Z]/,
          'stationOwing|3-7':/[A-Z]/,
          "stationType|1": [
            "光伏",
            "配电"
          ],
          "accessWay|1": [
            "拨号",
            "固定IP"
          ],
          "registerCode|1":[
            () => Mock.mock('@integer(1000,2000)'),
            () => Mock.mock('@ip')
          ],
          "status|1": [
            0,1
          ],
          "upTime": Random.now('month'),
          "downTime": Random.now('day'),
          "channelCode|3-7": /[A-Z]/,
          "ifActive|1":true,
        },
      ],
    });

    //通道列表信息
    Mock.mock(new RegExp('/manage/list'), (params: GetParams) => {
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
