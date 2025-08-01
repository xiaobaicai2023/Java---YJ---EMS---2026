import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const alarmEventData = Mock.mock({
      'list|100': [
        {
          'id|+1': 1,
          'name':/事件名称\d{3}/,
          'station':/所属站点\d{3}/,
          'device': /所属设备\d{3}/,
          "type":/报警类型\d{3}/,
          'level|1':["一级","二级","三级"],
          'occurrenceTime':Random.date('yyyy-MM-dd HH:mm:ss'),
          'recoveryTime':Random.date('yyyy-MM-dd HH:mm:ss'),
          'state|1':["已恢复","发生中","不处理","延迟处理"],
          'varName':/变量名称\d{3}/,
          'varCode':/变量代码\d{3}/,
          'resolver':'',
          'process':'',
        },
      ],
    });


    //用户信息
    Mock.mock(new RegExp('/pv/alarm/list/'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: alarmEventData.list.slice((p - 1) * ps, p * ps),
        total: 100,
      });
    });
  },
})