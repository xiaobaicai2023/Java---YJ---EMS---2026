import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const noticeListData = Mock.mock({
      "list|100": [
        {
          //编号
          "id|+1":1,
          //公告标题
          "title":/登录名称\d{3}/,
          //公告类型
          "type|1": ["通知","公告"],
          //创建人
          "creator":/创建人\d{3}/,
          //创建日期
          "createDate":Random.date("yyyy-MM-dd"),
          // 启用状态
          "status|1": [true,false],
          // 公告内容
          "content":"11111",
        },
      ],
    });

    //用户信息
    Mock.mock(new RegExp('/manage/notice/list/'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: noticeListData.list.slice((p - 1) * ps, p * ps),
        total: 100,
      });
    });
  },
})