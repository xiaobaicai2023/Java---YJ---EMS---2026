package com.yunpower.system.controller;

import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.system.domain.vo.time.TimesVo;
import com.yunpower.system.service.ISpecialDataProvideService;
import com.yunpower.system.service.impl.PublicServiceImpl;
import com.yunpower.system.utils.GenerateTimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 大屏专用数据（集合）
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/22 10:17
 */
@Api(tags = "D 大屏专用数据")
@RestController
@RequestMapping("/common-provide")
public class SpecialDataProvideController extends BaseController {

    @Autowired
    private ISpecialDataProvideService specialDataProvideService;
    @Autowired
    private PublicServiceImpl publicServiceImpl;

    /**
     * 通用>>获取数据接口
     */
    @ApiOperation("通用>>获取数据接口")
    @InnerAuth
    @GetMapping("/inner/tableList")
    public Object GetSpecialData(@RequestParam Map<String, Object> param) {
        Object dataType = param.get("dataType");
        Object tableType = param.get("tableType");
        Object headType = param.get("headType");
        Object staticType = param.get("staticType");
        Object deptId = param.get("deptId");

        int _dataType = dataType == null ? 1 : Integer.parseInt(dataType.toString());
        int _tableType = tableType == null ? 1 : Integer.parseInt(tableType.toString());
        int _headType = headType == null ? 1 : Integer.parseInt(headType.toString());
        int _staticType = staticType == null ? 0 : Integer.parseInt(staticType.toString());
        long _deptId = deptId == null ? publicServiceImpl.getCurrentStation() : Long.parseLong(deptId.toString());

        // 获取表格数据
        if (_dataType == 1) {
            Object dayTime = param.get("dayTime");
            Object monthTime = param.get("monthTime");
            Object yearTime = param.get("yearTime");

            TimesVo timesVo = new TimesVo();
            timesVo.setDayTime(dayTime == null ? null : dayTime.toString());
            timesVo.setMonthTime(monthTime == null ? null : monthTime.toString());
            timesVo.setYearTime(yearTime == null ? null : yearTime.toString());
            GenerateTimeUtils.packageTime(timesVo);
            return specialDataProvideService.getDevopsOrderList(_headType, _staticType, timesVo, _deptId);
        }

        // 获取统计图表数据
        if (_dataType == 2) {
            Object dayTime = param.get("dayTime");
            Object monthTime = param.get("monthTime");
            Object yearTime = param.get("yearTime");

            TimesVo timesVo = new TimesVo();
            timesVo.setDayTime(dayTime == null ? null : dayTime.toString());
            timesVo.setMonthTime(monthTime == null ? null : monthTime.toString());
            timesVo.setYearTime(yearTime == null ? null : yearTime.toString());

            return specialDataProvideService.getDevopsOrderStatic(_staticType, timesVo, _deptId);
        }

        return null;
    }
}
