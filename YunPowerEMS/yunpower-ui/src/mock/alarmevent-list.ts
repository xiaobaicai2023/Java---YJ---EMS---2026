import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const AlarmeventData = Mock.mock({
      'list|55': [
        {
          "key|4": /[0-9]/,
          "alarmName": /交流过频\d{1}/,
          "state|1":["已恢复","发生中"],
          "level|1":["警告","危险","一般"],
          "powerStation":/户用光伏演示电站\d{2}/,
          "equipment":{
            "name":/逆变器\d{2}/,
            "serialNumber":/INVERTER\d{4}/,
          },
          "startTime":{
            "time":Random.datetime().split(' ')[1],
            "date":Random.datetime().split(' ')[0],
            "timezone":"UTC+08:00",
          },
          "recoveryTime":{
            "time":Random.datetime().split(' ')[1],
            "date":Random.datetime().split(' ')[0],
            "timezone":"UTC+08:00",
          },
        },
      ],
    });
      //用户信息
      Mock.mock(new RegExp('/manage/alarmevent'), (params: GetParams) => {
        const { current = 1, pageSize = 50 } = qs.parseUrl(params.url).query;
        const p = current as number;
        const ps = pageSize as number;
        return successResponseWrap({
          list: AlarmeventData.list.slice((p - 1) * ps, p * ps),
          total: 55,
        });
      });
  },
})
