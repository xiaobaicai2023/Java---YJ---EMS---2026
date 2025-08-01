package com.yunpower.collect.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 月统计数据存储对象 sharding_month
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShardingMonth extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    private Long id;

    /**
     * 变量名称
     */
    private String variableName;

    /**
     * 日期天
     */
    private Integer daySign;

    /**
     * 最小值
     */
    private Double minValue;

    /**
     * 最小值发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date minTime;

    /**
     * 最大值
     */
    private Double maxValue;

    /**
     * 最大值发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date maxTime;

    /**
     * 平均值
     */
    private Double avgValue;

    /**
     * 累积标记：1时累积 2天累积
     */
    private Integer accuSign;

    /**
     * 累积值
     */
    private Double accuValue;

    /**
     * 实时值
     */
    private Double runTimeValue;

    /**
     * 总记录数
     */
    private Integer totalCount;

    /**
     * 统计时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;
}
