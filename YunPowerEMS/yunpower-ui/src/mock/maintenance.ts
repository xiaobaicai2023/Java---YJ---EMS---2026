import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const maintenanceData = Mock.mock({
      'list|50': [
        {
          "maintenanceName": "@ctitle(5)", //生成维保名称，长度为2-5个汉字,
          "site|1000-2000": 2000,  //随机生成站点
          "device": "@ctitle(5)", //生成设备名称，长度为2-5个汉字
          "maintenancePerson": "@ctitle(3)", //生成维保人，长度为3个汉字
          "maintenanceTime|1": Mock.mock('@datetime("y-MM-dd HH:mm:ss")'),//维保时间
        }
      ],
    });

    //工单信息
    Mock.mock(new RegExp('/manage/maintenance'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: maintenanceData.list.slice((p - 1) * ps, p * ps),
        total: 50,
      });
    });
  },
})
