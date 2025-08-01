import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const interfaceapplyData = Mock.mock({
      'list|50': [
        {
          "id|+1": 1,//id自增1
          "accessCompany": "@ctitle(5)", //接入公司
          "contacts": "@cname",  //联系人
          "uniqueIdentification|2-4":  /[a-z][A-Z][0-9]/,    //唯一标识
          "token|2-4": /[a-z][A-Z][0-9]/,   //令牌
          "status|0-1": 1,    //状态
          "contactEle|11": /[0-9]/,  //联系电话
          "account|2-4": /[a-z][A-Z][0-9]/, //账号
          "password|2-4": /[a-z][A-Z][0-9]/, //密钥
          "validity|1-100": 100, //令牌有效期
          "remark": '@cparagraph'
        },
      ],
    });
      //用户信息
      Mock.mock(new RegExp('/manage/interface/apply/'), (params: GetParams) => {
        const { current = 1, pageSize = 50 } = qs.parseUrl(params.url).query;
        const p = current as number;
        const ps = pageSize as number;
        return successResponseWrap({
          list: interfaceapplyData.list.slice((p - 1) * ps, p * ps),
          total: 50,
        });
      });
  },
})