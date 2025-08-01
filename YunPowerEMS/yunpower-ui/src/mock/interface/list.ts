import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const interfaceData = Mock.mock({
      'list|50': [
        {
          "id|+1": 1,//id自增1
          "interfaceName": "@ctitle(5)", //生成接口名称，长度为2-5个汉字,
          "interfaceMethod|1": ["/health-web/GetContentByST"],  //接口方法
          "requestPath": Mock.mock('@url'),//请求路径  
          "requestMethod|1": ["GET", "POST", "HEAD"],   //请求方式
          "callCount|1-100": 3,   //调用次数
          "status|0-1": 1,    //状态
        },
      ],
    });
      //用户信息
      Mock.mock(new RegExp('/manage/interface/list/'), (params: GetParams) => {
        const { current = 1, pageSize = 50 } = qs.parseUrl(params.url).query;
        const p = current as number;
        const ps = pageSize as number;
        return successResponseWrap({
          list: interfaceData.list.slice((p - 1) * ps, p * ps),
          total: 50,
        });
      });
  },
})