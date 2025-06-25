import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    const deviceGroupData = Mock.mock({
      "list|100": [
        {
          // 主键
          "key|+1": 1,
          // 设备组名称
          "groupName": /北区中心\d{3}/,
          // 是否设备组
          "isDeviceGroup":true,
          // 设备组下的设备
          "children":[],
        },
      ],
    });

    const deviceData = Mock.mock({
      "list|200-400": [{
        // 设备主键
        "key|+1":1,
        // 设备名称
        "deviceName":/分接箱\d{3}柜/,
        // 设备组主键
        "parentKey":"@integer(1,100)",
        // 设备组主键
        "groupName":"",
        // 设备编码
        "status|1": [0, 1],
        // 设备状态
        "code":/\d{3}_\d{3}_\d{3}/,
        // 所属通道
        "channel":/创新园区-科创中心\d{2}/,
        // 设备类型
        "type":/低压配电柜\d{2}/,
        // 额定电压
        "ratedVoltage|3":/[0-9]/,
        // 额定电流
        "ratedCurrent|2":/[0-9]/,
        // 额定功率
        "ratedPower|3":/[0-9]/,
        // 通讯设备
        "communicationDevice": /通讯设备\d{3}/,
        // 生产厂家
        "manufacturer": /厂家\d{3}/,
        // 产品型号
        "productModel": /型号\d{3}/,
        // 产品版本
        "productVersion": /版本\d{3}/,
        // 产品SN码
        "SNCode": /SN__\d{3}/,
        // 安规国家
        "safetyRegulationCountry":/国家\d{3}/,
        // 组件容量
        "componentCapacity":/\d{3}/,
        // 电流负载率
        "loadFactor":/\d{3}%/,
        // 是否关口表
        "isGateTable|1":[true, false],
        // 电能质量
        "isQualityAnalysis|1":[true, false],
        // 监控画面
        "monitor":/监控\d{3}/,
        // 备注内容
        "note":/\d{50}/,
        // 计费信息
        "chargingInformation":{
          // DTU端口
          "DTUPort":/DTU端口\d{3}/,
          // 绑定用户
          "boundUser":/绑定用户\d{3}/,
          // 付费模式
          "paymentModel":/付费模式\d{3}/,
          // 付费规则
          "paymentRule":/付费规则\d{3}/,
          // 付费周期
          "paymentCycle":/付费周期\d{3}/,
        },
        // 关联设备
        "associatedDevices":{
          // 设备主键
          "key|2-3":["@integer(1,200)"]
        }
      }],
    });

    for(var i=0;i<deviceData.list.length;i++){
      for(var k=0;k<deviceGroupData.list.length;k++){
        if(deviceData.list[i].parentKey===deviceGroupData.list[k].key){
          deviceData.list[i].groupName=deviceGroupData.list[k].groupName;
          deviceGroupData.list[k].children.push(deviceData.list[i])
          break;
        }
      }
    }

    const alarmData = Mock.mock({
      "list|100": [
        {
          "key|+1": 1,
          "name": /报警名称\d{3}/,
          "state": /报警状态\d{1}/,
          "level": /报警等级\d{3}/,
          "device": /报警设备\d{3}/,
          "variable": /报警变量\d{3}/,
          "content": /报警内容\d{3}/,
          "alarmTime":Random.date()+" "+Random.time(),
          "recoveryTime":Random.date()+" "+Random.time(),
        }
      ]
    });

    const deviceGroupList = [
      {
        value: 0,
        label: "全部",
      },
    ];
    
    for(var i=0;i<deviceGroupData.list.length;i++){
      deviceGroupList.push({
        value: deviceGroupData.list[i].key,
        label: deviceGroupData.list[i].groupName,
      })
    }

    const code = Mock.mock(/\d{3}_\d{3}_\d{3}/);

    // 设备组与设备表格数据
    Mock.mock(new RegExp("/power/energy/device/list/devicelist"), (params: GetParams) => {
      const { current = 1, pageSize = 50 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      var indexArray:number[]=[];
      for(var i=(p - 1) * ps+1;i<=p*ps;i++){
        indexArray.push(i);
      }
      return successResponseWrap({
        list: deviceGroupData.list.slice((p - 1) * ps, p * ps),
        total: 100,
        grouplist:deviceGroupList,
        array:indexArray,
      });
    });

    // 报警信息表格数据
    Mock.mock(new RegExp("/power/energy/device/list/alarm"), (params: GetParams) => {
      const { current = 1, pageSize = 50 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: alarmData.list.slice((p - 1) * ps, p * ps),
        total: 100,
      });
    });


    //设备编码、关联设备中设备组
    Mock.mock(new RegExp("/power/energy/device/list/create/init"), () => {
      return successResponseWrap({
        deviceCode:code,
        grouplist:deviceGroupList,
      });
    });

    //选择设备分组后自动加载表格
    Mock.mock(new RegExp("/power/energy/device/list/create/select"), (params: GetParams) => {
      const { groupKey=0 } = qs.parseUrl(params.url).query;
      const key = groupKey as number;
      var devices;
      if(key==0){
        devices=deviceData.list
      }
      for(var i=0;i<deviceGroupData.list.length;i++){
        if(deviceGroupData.list[i].key==key){
          devices = deviceGroupData.list[i].children;
          break;
        }
      }
      return successResponseWrap({
        devices:devices,
      });
    });

    //按下添加按钮后更新表格、按下详情按钮加载关联设备(根据传入key返回设备列表)
    Mock.mock(new RegExp("/power/energy/device/list/create/add"), (params: GetParams) => {
      const { deviceIndexs,total=0}  = qs.parseUrl(params.url).query;
      const keys = deviceIndexs as string[];
      const length =total as number;
      const addedDevices=[];
      if(length==1){
        for(var i=0;i<deviceData.list.length;i++){ 
          if(deviceData.list[i].key==keys){
          addedDevices.push(deviceData.list[i]);
          }
        }
      }else{
        for(var i=0;i<keys.length;i++){
          for(var j=0;j<deviceData.list.length;j++){ 
            if(deviceData.list[j].key==keys[i]){
              addedDevices.push(deviceData.list[j]);
            }
          }
        }
      }
      return successResponseWrap({
        devices :addedDevices
      });
    });


  },
})
