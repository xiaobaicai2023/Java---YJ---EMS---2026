package com.yunpower.collect.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 日数据存储对象 sharding_day
 * 
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@Data
public class ShardingDay extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 变量名称 */
    private String variableName;

    /** 存盘时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;

    /** 数据 */
    private Double dataValue;
}
