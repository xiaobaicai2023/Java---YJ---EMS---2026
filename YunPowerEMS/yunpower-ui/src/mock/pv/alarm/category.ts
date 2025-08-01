import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'


setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const alarmTypeData = Mock.mock({
      'list|100': [
        {
          'id|+1': 1,
          'name':/名称\d{3}/,
          'code':/\d{3}_\d{3}_\d{3}/,
          'level|1':["一级","二级","三级"],
          'trigger':/触发类型\d{3}/,
          'methods|1':[["window","wechat","message"],["wechat","message"],["window","message"],
          ["window","wechat"],["window"],["wechat"],["message"]],
          'status|1':[0,1],
        }
      ],
    });

    const triggerList = [

    ];
    
    for(var i=0;i<alarmTypeData.list.length;i++){
      triggerList.push({
        value: alarmTypeData.list[i].trigger,
        label: alarmTypeData.list[i].trigger,
      })
    }

    //用户信息
    Mock.mock(new RegExp('/pv/alarm/category/'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: alarmTypeData.list.slice((p - 1) * ps, p * ps),
        total: 100,
        grouplist:triggerList,
      });
    });
  },
})