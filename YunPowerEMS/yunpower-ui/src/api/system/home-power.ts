import axios from "axios";
import qs from "qs";

/**
 * | 能耗概览 | 今日用电量 | today_electric_use |
 * | 能耗概览 | 昨日用电量 | yesterday_electric_use |
 * | 能耗概览 | 本月用电量 | month_electric_use |
 * | 能耗概览 | 今年用电量 | year_electric_use |
 * | 能耗概览 | 总用电量 | total_electric_use |
 * | 能耗概览 | 用电量明细 | electric_use_detail |
 * | 能耗概览 | 用电能耗统计（按日） | electric_use_stat_day |
 * | 能耗概览 | 用电能耗统计（按月） | electric_use_stat_month |
 * | 能耗概览 | 用电能耗统计（按年） | electric_use_stat_year |
 * | 能耗概览 | 峰谷平尖（按日） | ppfv_electric_day |
 * | 能耗概览 | 峰谷平尖（按月） | ppfv_electric_month |
 * | 能耗概览 | 峰谷平尖（按年） | ppfv_electric_year |
 * | 能耗概览 | 平均功率因素（按日） | power_factor_day |
 * | 能耗概览 | 平均功率因素（按月） | power_factor_month |
 * | 能耗概览 | 平均功率因素（按年） | power_factor_year |
 * | 能耗概览 | 有功功率（按日） | active_power_day |
 * | 能耗概览 | 有功功率（按月） | active_power_month |
 * | 能耗概览 | 有功功率（按年） | active_power_year |
 * | 能耗概览 | 容量需量（按日） | capacity_demand_day |
 * | 能耗概览 | 容量需量（按月） | capacity_demand_month |
 * | 能耗概览 | 容量需量（按年） | capacity_demand_year |
 * | 能耗概览 | 容量需量 | capacity_demand |
 * | 能耗概览 | 分组能耗（按日） | group_energy_day |
 * | 能耗概览 | 分组能耗（按月） | group_energy_month |
 * | 能耗概览 | 分组能耗（按年） | group_energy_year |
 */

export enum PowerModuleEunm {
  today_electric_use = "today_electric_use",
  yesterday_electric_use = "yesterday_electric_use",
  month_electric_use = "month_electric_use",
  year_electric_use = "year_electric_use",
  total_electric_use = "total_electric_use",
  electric_use_detail = "electric_use_detail",
  electric_use_stat_day = "electric_use_stat_day",
  electric_use_stat_month = "electric_use_stat_month",
  electric_use_stat_year = "electric_use_stat_year",
  ppfv_electric_day = "ppfv_electric_day",
  ppfv_electric_month = "ppfv_electric_month",
  ppfv_electric_year = "ppfv_electric_year",
  power_factor_day = "power_factor_day",
  power_factor_month = "power_factor_month",
  power_factor_year = "power_factor_year",
  active_power_day = "active_power_day",
  active_power_month = "active_power_month",
  active_power_year = "active_power_year",
  capacity_demand_day = "capacity_demand_day",
  capacity_demand_month = "capacity_demand_month",
  capacity_demand_year = "capacity_demand_year",
  capacity_demand = "capacity_demand",
  group_energy_day = "group_energy_day",
  group_energy_month = "group_energy_month",
  group_energy_year = "group_energy_year",
  energy_steam_flow = 'energy-steam-flow',
  pv_power_history_day = 'pv-power-history-day',
  pv_power_history_month = 'pv-power-history-month',
  pv_power_history_year = 'pv-power-history-year',
  pv_power_inverter_day = 'pv-power-inverter-day',
  pv_power_inverter_month = 'pv-power-inverter-month',
  pv_power_inverter_year = 'pv-power-inverter-year'
}

/**
 * 普通图表
 * @param params 查询参数
 * @returns 结果
 */
export function getChart(params: any) {
  return axios.get("/datav/power/chart/getChart", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 堆叠图表
 * @param params 查询参数
 * @returns 结果
 */
export function getStackChart(params: any) {
  PowerModuleEunm.active_power_month;
  return axios.get("/datav/power/chart/getStackChart", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 获取页面图表种类
 * @param params 查询参数
 * @returns 结果
 */
export function getPageValueList(pageSign: string) {
  let params = {
    pageSign: pageSign,
  };
  return axios.get("/datav/power/chart/getPageValueList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 获取通用【单日期】图表数据
 * @param params 查询参数
 * @returns 结果
 */
export function getChartBySingleDate(params: any) {
  return axios.get("/datav/power/chart/getChartBySingleDate", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}


/**
 * 获取通用【双日期】图表数据
 * @param params 查询参数
 * @returns 结果
 */
export function getChartByDoubleDate(params: any) {
  return axios.get("/datav/power/chart/getChartByDoubleDate", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * / 用电 / 数据查询 / 峰谷数据
 * @param params 查询参数
 * @returns 结果
 */
export function ppfvList(params: any) {
  return axios.get("/datav/power/chart/ppfvList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * / 用电 / 数据查询 / 基础数据
 * @param params 查询参数
 * @returns 结果
 */
export function basicDataList(params: any) {
  return axios.get("/datav/power/chart/basicDataList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}


/**
 * / 用电 / 能耗分析 / 峰谷分析
 * @param params 查询参数
 * @returns 结果
 */
export function getSeasonalRangeList(params: any) {
  return axios.get("/system/price-scheme-config/getSeasonalRangeList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 新增电度电价配置
 * @param data 电价标准对象
 * @returns 0失败 1成功
 */
export function addPriceSchemeConfig(data: any) {
  return axios.post("/system/price-scheme-config", data);
}

/**
 * 修改电度电价配置
 * @param data 电价标准对象
 * @returns 0失败 1成功
 */
export function updatePriceSchemeConfig(data: any) {
  return axios.put("/system/price-scheme-config", data);
}

/**
 * 获取电度电价配置详细信息
 * @param id 查询参数
 * @returns 结果
 */
export function getPriceSchemeConfig(id: any) {
  return axios.get(`/system/price-scheme-config/${id}`);
}

/**
 * 能源流向
 * @param params 查询参数
 * @returns 结果
 */
export function getEnergySteamDate(params: any) {
  return axios.get("/datav/power/chart/getEnergySteamDate", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 光伏电站【辅助分析】图表数据
 * @param params 查询参数
 * @returns 结果
 */
export function getAuxiliaryAnalysis(params: any) {
  return axios.get("/datav/power/chart/getAuxiliaryAnalysis", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
