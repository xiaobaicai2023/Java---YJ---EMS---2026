import Mock, { Random } from 'mockjs'
import setupMock, {
  successResponseWrap,
} from '@/utils/setup-mock'
import qs from 'query-string';
import { GetParams } from '@/types/global'

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;
    // 设备组
    const deviceGroupData = Mock.mock({
      "list|5": [
        {
          // 主键
          "id|+1": 1,
          // 设备组名称
          "groupName": /设备组\d{3}/,
          // 设备组下的设备
          "children":[],
        },
      ],
    });

    // 单个设备
    const deviceData = Mock.mock({
      "list|50": [
        {
          // 主键
          "id|+1": 1,
          // 设备名称
          "deviceName": /设备\d{3}/,
          // 设备组编号
          "groupId":"@integer(1,5)",
          // 设备下的变量
          "children":[],
        },
      ],
    });
    const deviceList=[{
      value:0,
      label:"全部",
    }]
    for(var i=0;i<deviceData.list.length;i++){
      deviceList.push({
        value:deviceData.list[i].id,
        label:deviceData.list[i].deviceName,
      })
    }

    // 变量
    const AlarmSettingData = Mock.mock({
      'list|200': [
        {
          //变量编号
          'varId|+1':1,
          //变量名称
          'varName':/变量名称\d{3}/,
          //变量编码
          'code':/\d{3}_\d{3}_\d{3}/,
          //变量类型
          'varType|1':["模拟量","状态量"],
          //设备编号
          'deviceId':"@integer(1,50)",
          //所属设备
          'deviceName':"",
          //报警类型
          'type': /报警类型\d{3}/,
          //报警级别
          'level': /报警级别\d{3}/,
          //触发条件
          'trigger': /触发条件\d{3}/,
          //启用状态
          'status|1':[0, 1],
          // 短信发送
          'isSendMessage|1':[true, false],
          'receiverType|1':['staff','job'],
          // 接收人员
          'receiver|1':[['s1','s2','s3'],['j1','j2','j3']],
          // 阈值A
          'thresholdA|3':/[0-9]/,
        },
      ],
    });

    for(var i=0;i<AlarmSettingData.list.length;i++){
      for(var j=0;j<deviceData.list.length;j++){
        if(deviceData.list[j].id===AlarmSettingData.list[i].deviceId){
          AlarmSettingData.list[i].deviceName=deviceData.list[j].deviceName;
          deviceData.list[j].children.push(AlarmSettingData.list[i]);
          break;
        }
      }
    };
    for(var i=0;i<deviceData.list.length;i++){
      for(var j=0;j<deviceGroupData.list.length;j++){
        if(deviceGroupData.list[j].id===deviceData.list[i].groupId){
          deviceGroupData.list[j].children.push(deviceData.list[i]);
          break;
        }
      }
    };
    const treeData:any[]=[];
    for (var i=0;i<deviceGroupData.list.length;i++) {
      treeData.push({
        title:deviceGroupData.list[i].groupName,
        key:deviceGroupData.list[i].groupId,
        children:[]
      });
      for(var j=0;j<deviceGroupData.list[i].children.length;j++){
        treeData[i].children.push({
          title:deviceGroupData.list[i].children[j].deviceName,
          key:deviceGroupData.list[i].id+'-'+deviceGroupData.list[i].children[j].id,
         })
      }
    };

    //用户信息
    Mock.mock(new RegExp('/pv/alarm/setting/init'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: AlarmSettingData.list.slice((p - 1) * ps, p * ps),
        total: 200,
        treeData:treeData,
        deviceList:deviceList,
      });
    });

    //选择设备分组后自动加载表格
    Mock.mock(new RegExp("/pv/alarm/setting/select"), (params: GetParams) => {
    const { deviceId=0 } = qs.parseUrl(params.url).query;
    const id = deviceId as number;
    var analogVars=[];
    var stateVars=[];
    // 模拟量
      if(id==0){
        for(var i=0;i<AlarmSettingData.list.length;i++){
          if(AlarmSettingData.list[i].varType==="模拟量"){
            analogVars.push(AlarmSettingData.list[i]);
          }else{
            stateVars.push(AlarmSettingData.list[i]);
          }
        }
      }
      for(var i=0;i<deviceData.list.length;i++){
        if(deviceData.list[i].id==id){
          for(var j=0;j<deviceData.list[i].children.length;j++){
            if(deviceData.list[i].children[j].varType==="模拟量"){
              analogVars.push(deviceData.list[i].children[j]);
            }else{
              stateVars.push(deviceData.list[i].children[j]);
            }
          }
        }
      }
    return successResponseWrap({
      analogVars:analogVars,
      stateVars:stateVars,
    });
    });

    //根据传入key返回设备列表
    Mock.mock(new RegExp("/pv/alarm/setting/add"), (params: GetParams) => {
      const {varIndexs,total=0}  = qs.parseUrl(params.url).query;
      const ids = varIndexs as string[];
      const length =total as number;
      const addedAnalogVars=[];
      const addedStateVars=[];
      if(length==0){
        ;
      }
      else if(length==1){
        for(var i=0;i<AlarmSettingData.list.length;i++){
          if(AlarmSettingData.list[i].varId==ids){
            if(AlarmSettingData.list[i].varType==="模拟量"){
              addedAnalogVars.push(AlarmSettingData.list[i]);
            }else{
              addedStateVars.push(AlarmSettingData.list[i]);
            }
          }
        }
      }else{
        for(var i=0;i<ids.length;i++){
          for(var j=0;j<AlarmSettingData.list.length;j++){
            if(AlarmSettingData.list[j].varId==ids[i]){
              if(AlarmSettingData.list[j].varType==="模拟量"){
                addedAnalogVars.push(AlarmSettingData.list[j]);
              }else{
                addedStateVars.push(AlarmSettingData.list[j]);
              }
            }
          }
        }
      }
      return successResponseWrap({
        addedAnalogVars:addedAnalogVars,
        addedStateVars:addedStateVars,
      });
    });

  },
})
