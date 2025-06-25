import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const workOrderData = Mock.mock({
      'list|50': [
        {
          "workorderName": "@ctitle(5)", //生成工单名称，长度为2-5个汉字,
          "site|1000-2000": 2000,  //随机生成站点
          "eventName": "@ctitle(5)", //生成事件名称，长度为2-5个汉字
          "director|1": '@cname', //负责人
          "collaborator|1": '@cname', //协作人
          "creatTime|1": Mock.mock('@datetime("y-MM-dd HH:mm")'),//创建时间
          "planTime|1": Mock.mock('@datetime("y-MM-dd HH:mm")')+"~"+Mock.mock('@datetime("y-MM-dd HH:mm")'),//计划时间
          "finishTime|1": Mock.mock('@datetime("y-MM-dd HH:mm")'),//完成时间
          "overtimeStatus|1": ["正常", "已超时"],    //超时状态
          "workorderState|1": ["已完成", "已取消", "进行中", "延迟处理"],   //接入协议
        },
      ], 
    });
      //用户信息
      Mock.mock(new RegExp('/manage/workorder/'), (params: GetParams) => {
        const { current = 1, pageSize = 50 } = qs.parseUrl(params.url).query;
        const p = current as number;
        const ps = pageSize as number;
        return successResponseWrap({
          list: workOrderData.list.slice((p - 1) * ps, p * ps),
          total: 50,
        });
      });
  },
})
