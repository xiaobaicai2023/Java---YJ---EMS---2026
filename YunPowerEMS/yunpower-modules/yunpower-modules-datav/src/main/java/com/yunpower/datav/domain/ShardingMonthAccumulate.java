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

import java.util.Date;

/**
 * 变量累积数据月存储对象 sharding_month_accumulate
 * 
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@ApiModel("变量累积数据月存储对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShardingMonthAccumulate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

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
    @ApiModelProperty("年")
    @Excel(name = "年")
    private Integer recordYear;

    /** 月 */
    @ApiModelProperty("月")
    @Excel(name = "月")
    private Integer recordMonth;

    /** 日 */
    @ApiModelProperty("日")
    @Excel(name = "日")
    private Integer recordDay;

    /** 周 */
    @ApiModelProperty("周")
    @Excel(name = "周")
    private Integer recordWeek;

    /** 时 */
    @ApiModelProperty("时")
    @Excel(name = "时")
    private Integer recordHour;

    /** 累积数据 */
    @ApiModelProperty("累积数据")
    @Excel(name = "累积数据")
    private Double accuData;

    /** 峰谷名称 */
    @ApiModelProperty("峰谷名称")
    @Excel(name = "峰谷名称")
    private String seasonalTypeName;

    /** 计费电价 */
    @ApiModelProperty("计费电价")
    @Excel(name = "计费电价")
    private Float chargePrice;

    /** 完成标记 */
    @ApiModelProperty("完成标记")
    @Excel(name = "完成标记")
    private Integer isComplete;
}
