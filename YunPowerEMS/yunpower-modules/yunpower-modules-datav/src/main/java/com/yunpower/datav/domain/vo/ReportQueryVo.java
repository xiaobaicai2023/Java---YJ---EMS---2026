package com.yunpower.datav.domain.vo;

import com.yunpower.datav.domain.dto.ReportConfigDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 报表 - 查询参数
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/20 7:37
 */
@Data
public class ReportQueryVo {
    /**
     * 模板ID
     */
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    /**
     * 日-日期
     */
    private String dayTime;

    /**
     * 月-日期
     */
    private String monthTime;

    /**
     * 年-日期
     */
    private String yearTime;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 时间维度
     */
    private Integer dateDim;

    /**
     * 是否是峰谷数据
     */
    private Boolean isFengGuData = false;

    /**
     * 是否电费
     */
    private Boolean isCharge = false;

    /**
     * 数值类型（1实时 2平均值 3最小值 4最大值 5累积值）
     */
    private Integer changeType = 1;

    /**
     * 时间数组
     */
    private List<ChartQueryVo.TimeInfo> timeList;

    /**
     * 表头列表（平行结构）
     */
    private List<ReportConfigDto> headConfigList;
}
