import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'
// import {extraActive} from '@/views/power/energy/search/basic/index.vue'
// import {extraActive} from '@/views/power/energy/search/basic/index.vue'

let currentIndex = 0;
setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const basicData = Mock.mock({
      'list|50': [
        {
          'device': () => {
            currentIndex++;
            return `1#350KVA-${currentIndex}变压器`;
          },
          'parameter':"用电量",
        },
      ],
    });

    const extraActive1 = 0;
    // 生成随机数据并添加到每个项中
    basicData.list.forEach((item: any) => {
      for (let date = 1; date <= 24; date++) {
        item[`${extraActive1}${date}`] = Mock.mock('@float(0, 100, 0 , 2)');
      }
    });

    const extraActive2 = 1;
    // 生成随机数据并添加到每个项中
    basicData.list.forEach((item: any) => {
      for (let date = 1; date <= 31; date++) {
        item[`${extraActive2}${date}`] = Mock.mock('@float(0, 100, 0 , 2)');
      }
    });

    const extraActive3 = 2;
    // 生成随机数据并添加到每个项中
    basicData.list.forEach((item: any) => {
      for (let date = 1; date <= 12; date++) {
        item[`${extraActive3}${date}`] = Mock.mock('@float(0, 100, 0 , 2)');
      }
    });

    //通道列表信息
    Mock.mock(new RegExp('/power/energy/search/basic'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: basicData.list.slice((p - 1) * ps, p * ps),
        total: 50,
      });
    });
  },
})
