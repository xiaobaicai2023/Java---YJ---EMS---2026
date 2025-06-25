import axios from "axios";
import qs from "qs";

/**
 * 设备分组对象
 */
export interface AlarmTypeEntity {
  //编号
  id: number;
  // 报警类型名称
  name:string;
  // 类型代码
  code:string;
  // 报警级别
  level:number;
  // 触发类型
  trigger:string;
  // 报警方式
  methods:string[];
  // 状态
  status:number;
}

/**
 * 用户查询对象
 */
export interface AlarmTypeParams extends Partial<AlarmTypeEntity> {
  current: number;
  pageSize: number;
}

export interface AlarmIdParams {
  id:number
}
/**
 * 用户分页对象
 */
export interface AlarmTypeListPage {
  list: AlarmTypeEntity[];
  total: number;
}

//获取用户列表
export function queryAlarmTypeList(params: AlarmTypeParams): any {
  return axios.get<AlarmTypeListPage>("/pv/alarm/category/", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
