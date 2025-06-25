package com.yunpower.system.domain.jsonvo;

/**
 * @title: 电度电价配置表[seasonal_range] - 字段（List格式，图表页面使用）
 * @Author: Jiajiaglam
 * @date: 2023-11-02 10:27
 * @description:
 */
public class SeasonalRangeChartVo {
    /** 开始时间 */
    private Integer startTime;

    /** 结束时间 */
    private Integer endTime;

    /** 峰谷类型：1尖峰 2峰 3平 4谷 5深谷 */
    private String name;



    /** 计费电价（基本电价+加征电价）*/
    private Float chargePrice;

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getChargePrice() {
        return chargePrice;
    }

    public void setChargePrice(Float chargePrice) {
        this.chargePrice = chargePrice;
    }
}
