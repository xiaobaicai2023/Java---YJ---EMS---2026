package com.yunpower.collect.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 数据表查询条件
 *
 * @author: XIAOTONG.CAO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShardingQuery extends BaseEntity {
    /** 变量名称 */
    private String variableName;

    /** 存盘时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;

    /** 存盘时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date minTime;

    /**
     * 日期天
     */
    private Integer daySign;

    /**
     * 值类型
     * */
    private Integer valueType;

    /**
     * 数值类型（1实时 2平均值 3最小值 4最大值）
     * */
    private Integer changeType;

    private Integer dateDim;

    /**
     * 是否电费
     */
    private Boolean isCharge = false;
}
