import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const templateData = Mock.mock({
      'list|50': [
        {
          "id|+1": 1,//id自增1
          "templateType|1": ["短信", "微信"], //模板类型
          "subject|0-3": 3,  //主题
          "templateID|2-4":  /[0-9]/,    //模板ID
          "SMSSIGN|1": ["云捷讯通"],   //短信签名
          "status|0-1": 1,    //启用状态
          "templateContent": Random.zip()+"为您的手机绑定验证码，请于"+5+"分钟内填写。如非本人操作，请忽略本短信。",  //模板内容
        },
      ],
    });
      //用户信息
      Mock.mock(new RegExp('/manage/message/template/'), (params: GetParams) => {
        const { current = 1, pageSize = 50 } = qs.parseUrl(params.url).query;
        const p = current as number;
        const ps = pageSize as number;
        return successResponseWrap({
          list: templateData.list.slice((p - 1) * ps, p * ps),
          total: 50,
        });
      });
  },
})