package com.yunpower.datav.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * 月统计数据存储对象 sharding_month
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("月统计数据存储对象")
public class ShardingMonth extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    @ApiModelProperty("编号ID")
    private Long id;

    /**
     * 变量名称
     */
    @ApiModelProperty("变量名称")
    @Excel(name = "变量名称")
    private String variableName;

    /**
     * 日期天
     */
    @ApiModelProperty("日期天")
    @Excel(name = "日期天")
    private Integer daySign;

    /**
     * 最小值
     */
    @ApiModelProperty("最小值")
    @Excel(name = "最小值")
    private Double minValue;

    /**
     * 最小值发生时间
     */
    @ApiModelProperty("最小值发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最小值发生时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date minTime;

    /**
     * 最大值
     */
    @ApiModelProperty("最大值")
    @Excel(name = "最大值")
    private Double maxValue;

    /**
     * 最大值发生时间
     */
    @ApiModelProperty("最大值发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最大值发生时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date maxTime;

    /**
     * 平均值
     */
    @ApiModelProperty("平均值")
    @Excel(name = "平均值")
    private Double avgValue;

    /**
     * 累积标记：1时累积 2天累积
     */
    @ApiModelProperty("累积标记：1时累积 2天累积")
    @Excel(name = "累积标记：1时累积 2天累积")
    private Integer accuSign;

    /**
     * 累积值
     */
    @ApiModelProperty("累积值")
    @Excel(name = "累积值")
    private Double accuValue;

    /**
     * 实时值
     */
    @ApiModelProperty("实时值")
    @Excel(name = "实时值")
    private Double runTimeValue;

    /**
     * 总记录数
     */
    @ApiModelProperty("总记录数")
    @Excel(name = "总记录数")
    private Integer totalCount;

    /**
     * 统计时间
     */
    @ApiModelProperty("统计时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "统计时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;

    /**
     * 年
     */
    @Transient
    private Integer year;

    /**
     * 月
     */
    @Transient
    private Integer month;

    /**
     * 日
     */
    @Transient
    private Integer day;

    /**
     * 时
     */
    @Transient
    private Integer hour;

    /**
     * 分
     */
    @Transient
    private Integer minute;

    /**
     * yyyy-MM-dd HH:mm
     * */
    @Transient
    private String formattedDatetime;

    /**
     * 统计数据
     */
    @Transient
    private Double statisticValue;

    /**
     * 环比
     */
    @Transient
    private Double chainValue;

    /**
     * 指定字段的值
     * */
    @Transient
    private Double dataValue;

    /**
     * 尖峰平谷标识
     */
    @Transient
    private String seasonalTypeName;

    public ShardingMonth(){

    }

    public ShardingMonth(String formattedDatetime,Double value){
        this.formattedDatetime = formattedDatetime;
        this.dataValue = value;
    }
}
