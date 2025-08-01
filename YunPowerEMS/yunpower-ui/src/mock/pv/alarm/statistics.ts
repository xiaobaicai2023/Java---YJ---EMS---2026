import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const alarmStatisticData = Mock.mock({
      'list|4': [
        {
          'id|+1': 1,
          'station':/所属电站\d{3}/,
          'device': /设备名称\d{3}/,
          'levelOneDetails|15':[{
            'count|0-10':0
          }],
          'levelOne':0,
          'levelTwoDetails|15':[{
            'count|0-10':0
          }],
          'levelTwo':0,
          'levelThreeDetails|15':[{
            'count|0-10':0
          }],
          'levelThree':0,
          'total': 0,
        },
      ],
    });
    for(var i=0;i<alarmStatisticData.list.length;i++){
      for(var j=0;j<alarmStatisticData.list[i].levelOneDetails.length;j++){
        alarmStatisticData.list[i].levelOne+=alarmStatisticData.list[i].levelOneDetails[j].count
      }
      for(var j=0;j<alarmStatisticData.list[i].levelOneDetails.length;j++){
        alarmStatisticData.list[i].levelTwo+=alarmStatisticData.list[i].levelTwoDetails[j].count
      }
      for(var j=0;j<alarmStatisticData.list[i].levelOneDetails.length;j++){
        alarmStatisticData.list[i].levelThree+=alarmStatisticData.list[i].levelThreeDetails[j].count
      }

      alarmStatisticData.list[i].total=alarmStatisticData.list[i].levelOne+
      alarmStatisticData.list[i].levelTwo+alarmStatisticData.list[i].levelThree;
    }
    //用户信息
    Mock.mock(new RegExp('/pv/alarm/statistic/'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: alarmStatisticData.list.slice((p - 1) * ps, p * ps),
        total: 4,
      });
    });
  },
})