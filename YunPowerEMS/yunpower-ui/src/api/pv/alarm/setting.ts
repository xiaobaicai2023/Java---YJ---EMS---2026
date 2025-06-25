import axios from "axios";
import qs from "qs";

/**
 * 设备分组对象
 */
export interface AlarmSettingEntity {
  //变量编号
  varId: number;
  //变量名称
  varName:string;
  //变量编码
  code:string;
  //变量类型
  varType:string;
  //设备编号
  deviceId:number;
  //所属设备
  deviceName:string;
  //报警类型
  type: string;
  //报警级别
  level: string;
  //触发条件
  trigger: string;
  //启用状态
  status:string;
  // 短信发送
  isSendMessage:boolean;
  // 接收人员类型
  receiverType:string;
  // 接收人员
  receiver:string[];
  // 阈值A
  thresholdA:number;
}

/*
* 查询设备组参数
*/
export interface deviceQueryParams{
  deviceId: number;
}

/*
* 添加变量参数
*/
export interface addVarParams{
  varIndexs: string[];
  total: number;
}

/**
 * 用户查询对象
 */
export interface AlarmSettingParams extends Partial<AlarmSettingEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页对象
 */
export interface AlarmSettingListPage {
  list: AlarmSettingEntity[];
  total: number;
}

 //选择设备分组后自动加载表格
 export function queryVarByDevice(params: deviceQueryParams): any {
  return axios.get("/pv/alarm/setting/select", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

//获取用户列表
export function queryAlarmSettingList(params: AlarmSettingParams): any {
  return axios.get<AlarmSettingListPage>("/pv/alarm/setting/init", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

// 通过变量id添加变量
export function addVar(params: addVarParams): any {
  return axios.get("/pv/alarm/setting/add", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
