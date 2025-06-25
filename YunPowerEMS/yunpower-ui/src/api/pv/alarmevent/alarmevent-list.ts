import axios from "axios";
import qs from "qs";


export interface AlarmeventEntity {
  // 主键
    key: string;
    // 报警名称
    alarmName: string;
    //状态
    state: string;
    // 等级
    level: string;
    // 电站
    powerStation: string;
    // 设备
    equipment: {
        name: string;
        serialNumber: string;
    };
    // 报警开始时间
    startTime: {
        time: string;
        date: string;
        timezone: string;
    };
    // 报警结束时间
    recoveryTime: {
      time: string;
      date: string;
      timezone: string;
    };
}

/**
 * 查询对象
 */
export interface AlarmeventQueryParams extends Partial<AlarmeventEntity> {
  current: number;
  pageSize: number;
}
/**
 * 分页对象
 */
export interface AlarmeventListPage {
  list: AlarmeventEntity[];
  total: number;
}

//获取用户列表
export function queryAlarmeventList(params: AlarmeventQueryParams): any {
  return axios.get<AlarmeventListPage>("/manage/alarmevent", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
