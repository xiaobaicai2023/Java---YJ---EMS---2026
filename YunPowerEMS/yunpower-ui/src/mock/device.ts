import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const ChanneldeviceData = Mock.mock({
      'list|50': [
        {
          "id|+1": 1,//id自增1
          "deviceName": "@ctitle(5)", //生成设备名称，长度为2-5个汉字,
          "site|1000-2000": 2000,  //随机生成站点
          "channel|1": ["安然纳米井网", "安然纳米配电", "创新园区-科创中心", "华钟制药"],    //所属通道
          "deviceType|1": ["DTU", "通讯管理机"],   //设备类型
          "AccessProtocol|1": ["104协议", "ModbusRTU", "ModbusTCP", "MQTT"],   //接入协议
          "address|1-3": 3,    //设备/公共地址
          "tariffDate": Random.date(),   //资费到期
          "deviceStatus|0-1": 1,    //设备状态
          "time": Random.datetime(),//上/下线时间
        },
      ],
    });
      //用户信息
      Mock.mock(new RegExp('/manage/channel/device/'), (params: GetParams) => {
        const { current = 1, pageSize = 50 } = qs.parseUrl(params.url).query;
        const p = current as number;
        const ps = pageSize as number;
        return successResponseWrap({
          list: ChanneldeviceData.list.slice((p - 1) * ps, p * ps),
          total: 50,
        });
      });
  },
})
