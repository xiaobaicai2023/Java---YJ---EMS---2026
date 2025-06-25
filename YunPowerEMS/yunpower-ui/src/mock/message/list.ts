import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const messageListData = Mock.mock({
      'list|50': [
        {
          "id|+1": 1,//id自增1
          "userName": "@cname", //用户姓名
          "mobileNumber|11": /[0-9]/,  //手机号码
          "messageType|1": ["短信", "微信"],    //消息类型
          "messageSubject|1": ["用户登录"],   //消息主题
          "code|6": /[0-9]/,  //验证码
          "validity|0-10": 10,  //有效期
          "sendTime": Random.datetime(),  //发送时间
          "submitInformation|1": ["提交成功"],  //提交信息
          "sendstatus|1": ["已使用", "发送成功", "发送失败"],    //发送状态
          "status|0-1": 1,
          "SMSchannel|1": ["云捷讯通"],  //短信渠道
          "templateID|2-4":  /[0-9]/,    //模板ID
          "sendPerson": "@cname",  //发送人
          "sendContent": Random.zip()+"为您的手机绑定验证码，请于"+5+"分钟内填写。如非本人操作，请忽略本短信。", //发送内容
        },
      ],
    });
      //用户信息
      Mock.mock(new RegExp('/manage/message/list/'), (params: GetParams) => {
        const { current = 1, pageSize = 50 } = qs.parseUrl(params.url).query;
        const p = current as number;
        const ps = pageSize as number;
        return successResponseWrap({
          list: messageListData.list.slice((p - 1) * ps, p * ps),
          total: 50,
        });
      });
  },
})