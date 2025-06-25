import axios from "axios";
import qs from "qs";

// 设备组
export interface DeviceGroupEntity {
  // 主键
    key: string;
    // 设备组名称
    groupName: string;
    // 是否设备组
    isDeviceGroup: boolean;
    // 设备组下的设备
    children:DeviceEntity[];
  }


// 设备
export interface DeviceEntity {
      // 设备主键
      key: string;
      // 设备名称
      deviceName:string;
      // 设备组主键
      parentKey:string;
      // 设备名称
      groupName:string;
      // 设备编码
      deviceSn:string;
      // 设备状态
      deleteFlag:string;
      // 所属通道
      channelId:string;
      // 设备类型
      proType:string;
      // 额定电压
      ratedVol:string;
      // 额定电流
      ratedEle:string;
      // 额定功率
      ratedPower:string;
      // 通讯设备
      communicationDevice:string;
      // 生产厂家
      proFactory:string;
      // 产品型号
      proModel:string;
      // 产品版本
      proVer:string;
      // 产品SN码
      proSn:string;
      // 安规国家
      agStandard:string;
      // 组件容量
      ratedVolume:string;
      // 电流负载率
      eleLoadRate:string;
      // 是否关口表
      isPass:boolean;
      // 电能质量分析
      isAnalysisEnergy:boolean;
      // 监控画面
      monitor:string;
      // 备注内容
      remark:string;
      // 计费信息
      chargingInformation:{
        // DTU端口
          dtuCmd:string;
        // 绑定用户
          bindUserId:string;
        // 付费模式
          chargingType:string;
        // 付费规则
          ruleId:string;
        // 付费周期
          checkoutType:string;
      };
      // 关联设备
      associatedDevices:{
        // 设备主键
        key:string[]
      };
  }


// 报警信息
export interface AlarmEntity {
// 主键
  key: string;
  // 报警名称
  name: string;
  // 报警状态
  state: string;
  // 报警等级
  level:string;
  // 报警设备
  device:string;
  // 报警变量
  variable:string;
  // 报警内容
  content:string;
  // 创建时间
  alarmTime:Date;
  // 更新时间
  recoveryTime:Date;
}
/**
 * 查询对象
 */
export interface DeviceListParams {
  current: number;
  pageSize: number;
}

export interface AlarmQueryParams {
  current: number;
  pageSize: number;
}
/**
 * 分页对象
 */
export interface DeviceListPage {
  list: DeviceEntity[];
  total: number;
}

/*
* 查询设备组参数
*/
export interface groupQueryParams{
  groupKey: number;
}

/*
* 添加设备参数
*/
export interface addDeviceParams{
  deviceIndexs: string[];
  total: number;
}

//获取设备列表
export function queryDeviceList(params: DeviceListParams): any {
  return axios.get("/power/energy/device/list/devicelist", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

//获取警告列表
export function queryAlarmList(params: AlarmQueryParams): any {
  return axios.get("/power/energy/device/list/alarm", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

//设备编码、关联设备中设备组
export function queryDeviceCode(): any {
  return axios.get("/power/energy/device/list/create/init", {
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

 //选择设备分组后自动加载表格
export function queryDeviceByGroup(params: groupQueryParams): any {
  return axios.get("/power/energy/device/list/create/select", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

// 通过设备key添加设备
export function addDevice(params: addDeviceParams): any {
  return axios.get("/power/energy/device/list/create/add", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
