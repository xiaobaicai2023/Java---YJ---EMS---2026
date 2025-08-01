package com.yunpower.datav.domain.vo;

import lombok.Data;

/**
 * 专用模板、特殊接口查询参数
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/23 9:17
 */
@Data
public class SpecialQueryVo {
    /**
     * 列表类型：1报警列表 2工单列表
     */
    private Integer tableType;

    /**
     * 表头类型：1长表头 2短表头
     */
    private Integer headType;

    /**
     * 数据类型：记录状态
     */
    private Integer staticType;

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
}
