package com.yunpower.collect.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 变量累积数据月存储对象 sharding_month_accumulate
 * 
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShardingMonthAccumulate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    private Long id;

    /** 变量名称 */
    private String variableName;

    /** 存盘时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;
    /**
     * 上一个小时runtime值
     */
    private Double dataValue;

    /**
     * 当前数据
     */
    private Double runtimeValue;

    /** 年 */
    private Integer recordYear;

    /** 月 */
    private Integer recordMonth;

    /** 日 */
    private Integer recordDay;

    /** 周 */
    private Integer recordWeek;

    /** 时 */
    private Integer recordHour;

    /** 累积数据 */
    private Double accuData;

    /** 峰谷名称 */
    private String seasonalTypeName;

    /** 计费电价 */
    private Float chargePrice;

    /** 完成标记 */
    private Integer isComplete;
}
