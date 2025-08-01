package com.yunpower.datav.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * 日数据存储对象 sharding_day
 * 
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("日数据存储对象")
public class ShardingDay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 变量名称 */
    @ApiModelProperty("变量名称")
    @Excel(name = "变量名称")
    private String variableName;

    /** 存盘时间 */
    @ApiModelProperty("存盘时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "存盘时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;

    /** 数据 */
    @ApiModelProperty("数据")
    @Excel(name = "数据")
    private Double dataValue;

    /** 年 */
    @Transient
    private Integer year;

    /** 月 */
    @Transient
    private Integer month;

    /** 日 */
    @Transient
    private Integer day;

    /** 时 */
    @Transient
    private Integer hour;

    /** 分 */
    @Transient
    private Integer minute;
}
