package com.yunpower.datav.service;

import com.yunpower.datav.domain.vo.ReportQueryVo;

/**
 * 通用报表服务
 * @Author: Jiajiaglam
 * @Date: 2024/6/20 7:50
 */
public interface CommonReportService {
    /**
     * 处理报表数据
     * @param reportQueryVo 查询参数
     */
    Object handleReportData(ReportQueryVo reportQueryVo);
}
