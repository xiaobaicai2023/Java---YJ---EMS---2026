package com.yunpower.datav.service.impl;

import com.yunpower.common.core.DoubleUtils;
import com.yunpower.datav.domain.vo.AssignDataVo;
import com.yunpower.datav.enums.DatavDateDimEnum;
import com.yunpower.datav.enums.DatavStatVarEnum;
import com.yunpower.datav.enums.DatavStatVarSecondEnum;
import com.yunpower.datav.service.ISpecialDataVService;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.datav.domain.ShardingQuery;
import com.yunpower.datav.service.ShardingCommonService;
import com.yunpower.datav.utils.DatavStatUtils;
import com.yunpower.datav.utils.EnergyConverter;
import com.yunpower.system.api.RemoteDictDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


/**
 * 大屏专用数据（集合）需要直接访问采集数据
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/26 16:58
 */
@Service
public class SpecialDataVServiceImpl implements ISpecialDataVService {

    private static final Logger log = LoggerFactory.getLogger(SpecialDataVServiceImpl.class);
    @Autowired
    private ShardingCommonService shardingCommonService;

    @Autowired(required = false)
    private RemoteDictDataService remoteDictDataService;

    //region 获取指定接口数据

    /**
     * 获取指定接口数据（第2部分）
     * 第1部分在System模块中
     *
     * @param assignType 指定类型的key
     * @param createDate 项目创建日期
     * @return 结果
     */
    @Override
    public AssignDataVo getAssignedData(String assignType, String createDate, String stationSn) {
        String _assignType = stationSn + "_" + assignType;
        switch (assignType) {
            //年减排量
            case "yearEmissionReduction":
                _assignType = stationSn + "_year_pv_power";
                return getYearEmissionReduction(_assignType);
            //总减排量
            case "total_emission_reduction":
                _assignType = stationSn + "_total_pv_power";
                return getTotalEmissionReduction(_assignType, createDate);
            //总用电量
            case "total_electric_use":
                return getTotalData(_assignType, createDate, "总用电量", "kWh");
            //总发电量
            case "total_pv_power":
                return getTotalData(_assignType, createDate, "总发电量", "kWh");
            //总充电量
            case "total_charge_power":
                return getTotalData(_assignType, createDate, "总充电量", "kWh");
            //总储能量
            case "total_storage_power":
                return getTotalData(_assignType, createDate, "总储能量", "kWh");
            //年发电量
            case "year_pv_power":
                return getTotalData(_assignType, null, "年发电量", "kWh");
            //年充电量
            case "year_charge_power":
                return getTotalData(_assignType, null, "年充电量", "kWh");
            //年储能量
            case "year_storage_power":
                return getTotalData(_assignType, null, "年储能量", "kWh");
        }
        return null;
    }

    /**
     * 获取指定接口数据（第2部分）
     * 第1部分在System模块中
     *
     * @param assignType 指定类型的key
     * @param stationSn  站点sn
     * @return 结果
     */
    public AssignDataVo getAssignedData(String assignType,String stationSn){
        //获取二级分类变量 总、年、月、日
        DatavStatVarSecondEnum statVarSecondEnum = DatavStatVarSecondEnum.fromKey(assignType);
        if (statVarSecondEnum == null) {
            return null;
        }

        //获取一级分类变量 用电量、发电量、用水量、等
        //根据这个来获取变量sn  站点sn + 一级分类变量
        DatavStatVarEnum statVarEnum = DatavStatUtils.getFirstLevelEnum(statVarSecondEnum);

        // 【特殊处理】 大屏左上角统计=>年减排量 过去一年减少碳排放：23 CO₂吨，相当于植树 293 棵
        if(DatavStatVarSecondEnum.YEAR_EMISSION_REDUCTION_CONVERT.equals(statVarSecondEnum)){
            return getYearEmissionReductionV2(stationSn);
        }

        // 【特殊处理】 减排量按照 按照发电数据来计算
        if(DatavStatVarEnum.EMISSION_REDUCTION.equals(statVarEnum)){
            return getEmissionReduction(stationSn,statVarEnum,statVarSecondEnum);
        }

        //计算其他统计数据
        double value = getDatavStatVarData(stationSn,statVarEnum,statVarSecondEnum);
        log.debug("大屏统计变量查询：{} => {} {} value:{}",assignType,statVarSecondEnum.getValue(),statVarEnum.getKey(),value);

        AssignDataVo dataVo = new AssignDataVo(statVarSecondEnum.getKey(), value, statVarSecondEnum.getValue(), DatavStatVarEnum.WATER_USE.equals(statVarEnum) ? "t":"kWh");
        EnergyConverter.convertEnergy(dataVo);
        return dataVo;
    }


    /**
     * 大屏统计变量数据查询
     * */
    private double getDatavStatVarData(String stationSn,DatavStatVarEnum statVarEnum,DatavStatVarSecondEnum statVarSecondEnum){
        double value = 0;
        try{
            //变量=> 站点_一级分类变量
            String varSn = stationSn + "_" + statVarEnum.getKey();
            //查询条件
            ShardingQuery query = null;
            Map<String, Object> dateToParam = DateUtils.dateToParamForDayFormat(DateUtils.getTime(), DateUtils.getTime());

            //总值 取月表的实时值 runtime_value
            if(DatavDateDimEnum.TOTAL.equals(statVarSecondEnum.getDateDimEnum())){
                query = new ShardingQuery(varSn, 1, 1, dateToParam, 1D);
            }else{
                if(DatavDateDimEnum.YEAR.equals(statVarSecondEnum.getDateDimEnum())){
                    dateToParam = DateUtils.dateToParamForYear(DateUtils.getCurrYear());
                }else if(DatavDateDimEnum.MONTH.equals(statVarSecondEnum.getDateDimEnum())){
                    dateToParam = DateUtils.dateToParamForMonth(new Date());
                }
                //取月表的累计值 runtime_value
                query = new ShardingQuery(varSn, 2, 5, dateToParam, 1D);
            }

            //获取数据
            value = shardingCommonService.selectDatavStatVarData(query);

        }catch (Exception ex){
            log.error("获取大屏统计变量变量查询异常：=> {} {}",statVarSecondEnum.getValue(),statVarEnum.getKey(),ex);
        }
        return value;
    }


    /**
     * 年减排量
     * -- 过去一年减少碳排放：23 CO₂吨，相当于植树 293 棵
     */
    private AssignDataVo getYearEmissionReduction(String assignType) {
        // 获取年发电量
        float total = packageTotalData(assignType, null);

        // 计算年减排量
        float C02 = 0f, treeNum = 0f;
        if (total > 0) {
            String coalValue = remoteDictDataService.getDictValue("energy_unit_convert", "electric_to_coal", SecurityConstants.INNER);
            float coal = coalValue == null ? 0f : Float.parseFloat(coalValue);
            C02 = total * coal;

            // 计算植树量
            String treeValue = remoteDictDataService.getDictValue("energy_unit_convert", "tree_to_co2", SecurityConstants.INNER);
            float tree = treeValue == null ? 0f : Float.parseFloat(treeValue);
            treeNum = C02 / tree;
        }

        double resCO2 = DoubleUtils.floatToDouble(C02);
        double resTree = DoubleUtils.floatToDouble(treeNum);
        if (C02 < 1) { // 保留4位小数
            resCO2 = DoubleUtils.floatToDoubleNPoint(C02,4);
            resTree = DoubleUtils.floatToDoubleNPoint(treeNum,4);
        }

        // 组装
        String value = StringUtils.format("过去一年减少碳排放：{} CO₂吨，相当于植树 {} 棵", resCO2, resTree);

        return new AssignDataVo(assignType, value, "年减排量", "t/y");
    }


    /**
     * 年减排量
     * -- 过去一年减少碳排放：23 CO₂吨，相当于植树 293 棵
     */
    private AssignDataVo getYearEmissionReductionV2(String stationSn) {
        // 获取年发电量
        ShardingQuery query = new ShardingQuery(stationSn + "_" +DatavStatVarEnum.PV_POWER, 2, 5,  DateUtils.dateToParamForYear(DateUtils.getCurrYear()), 1D);
        double value = shardingCommonService.selectDatavStatVarData(query);

        // 计算年减排量
        double C02 = 0f, treeNum = 0f;
        if (value > 0) {
            String coalValue = remoteDictDataService.getDictValue("energy_unit_convert", "electric_to_coal", SecurityConstants.INNER);
            float coal = coalValue == null ? 0f : Float.parseFloat(coalValue);
            C02 = value * coal;

            // 计算植树量
            String treeValue = remoteDictDataService.getDictValue("energy_unit_convert", "tree_to_co2", SecurityConstants.INNER);
            float tree = treeValue == null ? 0f : Float.parseFloat(treeValue);
            treeNum = C02 / tree;
        }

        double resCO2 = DoubleUtils.fmt2Point(C02);
        double resTree = DoubleUtils.fmt2Point(treeNum);
        if (C02 < 1) { // 保留4位小数
            resCO2 = DoubleUtils.fmtNPoint(C02,4);
            resTree = DoubleUtils.fmtNPoint(treeNum,4);
        }
        // 组装
        return new AssignDataVo("yearEmissionReduction", StringUtils.format("过去一年减少碳排放：{} CO₂吨，相当于植树 {} 棵", resCO2, resTree), "年减排量", "t/y");
    }

    /**
     *
     * .
     */
    private AssignDataVo getTotalEmissionReduction(String assignType, String createDate) {
        // 获取总发电量
        float total = packageTotalData(assignType, createDate);

        // 计算总减排量
        float coal = 0f;
        if (total > 0) {
            String dictValue = remoteDictDataService.getDictValue("energy_unit_convert", "electric_to_coal", SecurityConstants.INNER);
            coal = dictValue == null ? 0f : Float.parseFloat(dictValue);
        }

        float resTotal = total * coal;
        double resStr =DoubleUtils.floatToDouble(resTotal);
        if (resTotal < 1f) { // 保留4位小数
	        resStr = DoubleUtils.floatToDoubleNPoint(resTotal,4);
        }

        return new AssignDataVo(assignType, resStr, "总减排量", "t");
    }


    /**
     * 减排量=>发电量
     * */
    private DatavStatVarSecondEnum convertEmissionReductionToPv(DatavStatVarSecondEnum statVarSecondEnum){
        DatavStatVarSecondEnum newStatVarSecondEnum;
        switch (statVarSecondEnum){
            case TOTAL_EMISSION_REDUCTION:
                newStatVarSecondEnum = DatavStatVarSecondEnum.TOTAL_PV_POWER;
            case YEAR_EMISSION_REDUCTION:
                newStatVarSecondEnum = DatavStatVarSecondEnum.YEAR_PV_POWER;
            case MONTH_EMISSION_REDUCTION:
                newStatVarSecondEnum = DatavStatVarSecondEnum.MONTH_PV_POWER;
            case DAY_EMISSION_REDUCTION:
                newStatVarSecondEnum = DatavStatVarSecondEnum.DAY_PV_POWER;
            default:
                newStatVarSecondEnum = DatavStatVarSecondEnum.TOTAL_PV_POWER;
        }
        return newStatVarSecondEnum;
    }

    /**
     * 计算减排量
     * 取发电量数据
     * */
    private AssignDataVo getEmissionReduction(String stationSn,DatavStatVarEnum statVarEnum,DatavStatVarSecondEnum statVarSecondEnum) {
        log.debug("大屏变量统计查询 减排量=>发电量");
        DatavStatVarEnum newStatVarEnum = DatavStatVarEnum.PV_POWER;
        DatavStatVarSecondEnum newStatVarSecondEnum =  convertEmissionReductionToPv(statVarSecondEnum);
        double value = getDatavStatVarData(stationSn,newStatVarEnum,newStatVarSecondEnum);

        // 计算总减排量
        double coal = 0;
        if (value > 0) {
            String dictValue = remoteDictDataService.getDictValue("energy_unit_convert", "electric_to_coal", SecurityConstants.INNER);
            coal = dictValue == null ? 0 : DoubleUtils.stringToDouble(dictValue);
        }

        double resTotal = value * coal;
        double resStr = DoubleUtils.fmt2Point(resTotal);
        if (resTotal < 1f) { // 保留4位小数
            resStr = DoubleUtils.fmtNPoint(resTotal,4);
        }
        AssignDataVo dataVo =  new AssignDataVo(statVarSecondEnum.getKey(), resStr, statVarSecondEnum.getValue(), "t");
        EnergyConverter.convertEnergy(dataVo);
        return dataVo;
    }


    /**
     * 总用电量
     * 总发电量
     * 总充电量
     * 总储能量
     * 年发电量
     * 年充电量
     * 年储能量
     */
    private AssignDataVo getTotalData(String assignType, String createDate, String label, String unit) {
        float total = packageTotalData(assignType, createDate);
        AssignDataVo dataVo = new AssignDataVo(assignType, total, label, unit);
        EnergyConverter.convertEnergy(dataVo);
        return dataVo;
    }

    // 封装数据
    private Float packageTotalData(String assignType, String createDate) {
        if (createDate == null) {
            createDate = DateUtils.getCurrYear() + "-01-01";
        }
        Map<String, Object> dateToParamForYear = DateUtils.dateToParamForMonth(createDate, DateUtils.getDate());
        ShardingQuery query = new ShardingQuery(assignType, 2, 5, dateToParamForYear, 1D);
        Float total = shardingCommonService.selectTotalDataForYear(query);
        return total == null ? 0 : total;
    }
    //endregion
}
