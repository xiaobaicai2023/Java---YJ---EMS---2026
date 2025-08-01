package com.yunpower.datav.controller;

import com.alibaba.fastjson.JSON;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.datav.domain.dto.ReportConfigDto;
import com.yunpower.datav.domain.vo.ReportQueryVo;
import com.yunpower.datav.service.CommonReportService;
import com.yunpower.system.api.RemoteReportTemplateService;
import com.yunpower.system.api.domain.ReportTemplates;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用表格接口
 *
 * @author JUNFU.WANG
 * @date: 2024-06-19 10:00
 */
@Api(tags = ">>> 通用表格接口 <<<")
@RestController
@RequestMapping("/common/table")
public class CommonTableController extends BaseController {
    @Autowired(required = false)
    private RemoteReportTemplateService remoteReportTemplateService;

    @Autowired
    private CommonReportService commonReportService;

    /**
     * 获取>>通用表格数据
     * <pre>
     *   templateId   ：模板Id必填
     *   dayTime      ：日，如：2024-06-20（三选一）
     *   monthTime    ：月，如：2024-06（三选一）
     *   yearTime     ：年，如：2024（三选一）
     *   changeType   ：基础报表时取月数据时传（2平均值 3最小值 4最大值 5累积值）
     *   isFengGuData ：true：取峰谷平数据，false：取基础数据（峰谷报表）
     *   isCharge     ：true：取峰谷数据的电费，falae：取峰谷数据的电能（峰谷报表）
     * </pre>
     *
     * @param reportQueryVo 参数
     * @return 结果
     */
    @ApiOperation("获取>>通用表格数据")
    @GetMapping("/getBasicTable")
    public AjaxResult getBasicTableData(@Validated ReportQueryVo reportQueryVo) {
        ReportTemplates reportTemplate = remoteReportTemplateService.getInfo(reportQueryVo.getTemplateId(), SecurityConstants.INNER);
        if (reportTemplate == null) {
            return error("获取模板配置错误");
        }
        if (StringUtils.isEmpty(reportTemplate.getHeadConfig())) {
            return error("请先配置表头");
        }

        reportQueryVo.setHeadConfigList(JSON.parseArray(reportTemplate.getHeadConfig(), ReportConfigDto.class));

        //如果时间都为空 则默认取今日的数据
        if (StringUtils.isEmpty(reportQueryVo.getDayTime()) && StringUtils.isEmpty(reportQueryVo.getMonthTime()) && StringUtils.isEmpty(reportQueryVo.getYearTime())) {
            reportQueryVo.setDayTime(DateUtils.getDate());
        }

        return success(commonReportService.handleReportData(reportQueryVo));
    }
}
