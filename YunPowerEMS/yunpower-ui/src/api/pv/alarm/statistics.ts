import axios from "axios";
import qs from "qs";

/**
 * 设备分组对象
 */
export interface AlarmStatisticEntity {
  //编号
  id: number;
  //所属站点
  station:string;
  //设备名称
  device: string;
  //一级报警
  levelOne:number;
  //二级报警
  levelTwo:number;
  //三级报警
  levelThree:number;
  // 总计
  total:number;
}

/**
 * 用户查询对象
 */
export interface AlarmStatisticParams extends Partial<AlarmStatisticEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页对象
 */
export interface AlarmStatisticListPage {
  list: AlarmStatisticEntity[];
  total: number;
}

//获取用户列表
export function queryAlarmStatisticList(params: AlarmStatisticParams): any {
  return axios.get<AlarmStatisticListPage>("/pv/alarm/statistic/", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
