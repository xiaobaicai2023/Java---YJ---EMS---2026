package com.yunpower.collect.storage.domain.jsonvo;

/**
 * @title: 电度电价配置表[seasonal_range] - 字段（JSON格式）
 * @Author: Jiajiaglam
 * @date: 2023-11-02 10:27
 * @description:
 */
public class SeasonalRangeVo {
    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    /** 峰谷类型：1尖峰 2峰 3平 4谷 5深谷 */
    private Integer seasonalType;

    /** 峰谷类型：1尖峰 2峰 3平 4谷 5深谷 */
    private String seasonalName;

    /** 计费电价（基本电价+加征电价）*/
    private Float chargePrice;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getSeasonalType() {
        return seasonalType;
    }

    public void setSeasonalType(Integer seasonalType) {
        this.seasonalType = seasonalType;
    }

    public String getSeasonalName() {
        return seasonalName;
    }

    public void setSeasonalName(String seasonalName) {
        this.seasonalName = seasonalName;
    }

    public Float getChargePrice() {
        return chargePrice;
    }

    public void setChargePrice(Float chargePrice) {
        this.chargePrice = chargePrice;
    }
}
