package com.yunpower.datav.controller;

import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.datav.domain.vo.AssignDataVo;
import com.yunpower.datav.domain.vo.SpecialQueryVo;
import com.yunpower.datav.service.ISpecialDataVService;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.system.api.RemoteDashboardConfigCardService;
import com.yunpower.system.api.RemotePublicService;
import com.yunpower.system.api.RemoteSpecialDataVService;
import com.yunpower.system.api.domain.SysStation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大屏专用数据
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/21 18:14
 */
@Api(tags = ">>> 大屏专用数据接口 <<<")
@RestController
@RequestMapping("/common/special")
public class SpecialDataVController extends BaseController {
    @Autowired(required = false)
    private RemoteSpecialDataVService remoteSpecialDataVService;

    @Autowired(required = false)
    private RemoteDashboardConfigCardService remoteDashboardConfigCardService;

    @Autowired
    private ISpecialDataVService specialDataVService;

    @Autowired
    private RemotePublicService publicService;

    @Value("${app.database.create-date}")
    private String createDate;

    /**
     * 获取>>专用模板【表格】数据
     * <pre>
     *     dataType   数据类型：1表格 2图表
     *     tableType  表格类型：1报警 2工单
     *     headType   表头类型：1长表头 2短表头
     *     staticType 数据类型：记录状态
     * </pre>
     *
     * @param specialQueryVo 查询参数
     * @return 结果
     */
    @ApiOperation("获取>>专用模板【表格】数据")
    @GetMapping("/getSpecialList")
    public AjaxResult getSpecialList(@Validated SpecialQueryVo specialQueryVo) {
        Map<String, Object> param = new HashMap<>();
        param.put("dataType", 1);
        param.put("tableType", specialQueryVo.getTableType());
        param.put("headType", specialQueryVo.getHeadType());
        param.put("staticType", specialQueryVo.getStaticType());
        return success(remoteSpecialDataVService.getSpecialData(param, SecurityConstants.INNER));
    }

    /**
     * 获取>>专用模板【统计】数据
     * <pre>
     *     dataType   数据类型：1表格 2图表
     *     tableType  图表类型：1报警 2工单
     *     staticType 统计类型（1按类型 2按级别）（1按类型 2按日期）
     * </pre>
     *
     * @param specialQueryVo 查询参数
     * @return 结果
     */
    @ApiOperation("获取>>专用模板【统计】数据")
    @GetMapping("/getSpecialStatic")
    public AjaxResult getSpecialStatic(@Validated SpecialQueryVo specialQueryVo) {
        Map<String, Object> param = new HashMap<>();
        param.put("dataType", 2);
        param.put("tableType", specialQueryVo.getTableType());
        param.put("staticType", specialQueryVo.getStaticType());
        param.put("dayTime", specialQueryVo.getDayTime());
        param.put("monthTime", specialQueryVo.getMonthTime());
        param.put("yearTime", specialQueryVo.getYearTime());
        return success(remoteSpecialDataVService.getSpecialData(param, SecurityConstants.INNER));
    }

    /**
     * 获取>>大屏固定数据（废弃）
     */
    @ApiOperation("获取>>大屏固定数据")
    @GetMapping("/getSpecialData")
    public AjaxResult getSpecialData(String assignType) {
        SysStation station = publicService.getCurrentStationInfo(SecurityConstants.INNER);
        return success(specialDataVService.getAssignedData(assignType, station.getStationSn()));
    }

    /**
     * 获取>>大屏配置数据
     */
    @ApiOperation("获取>>大屏配置数据")
    @GetMapping("/getBiConfig/{stationId}/{isPre}")
    public AjaxResult getSpecialData(@PathVariable("stationId") Long stationId, @PathVariable("isPre") Integer isPre) {
        Map<String, Object> config = remoteDashboardConfigCardService.getDashboardConfigByStationId(stationId, isPre, SecurityConstants.INNER);
        if (StringUtils.isEmpty(config)) {
            throw new RuntimeException("当前站点未配置大屏界面，请配置后再访问！");
        }
        String stationSn = (String) config.get("stationSn");
        //计算年减排量
        if (config.containsKey("content")) {
            Map<String, Object> content = (Map<String, Object>) config.get("content");
            if (content.containsKey("yearEmissionReduction")) {
                content.put("yearEmissionReduction", specialDataVService.getAssignedData("yearEmissionReduction", stationSn).getValue());
            }
        }

        //计算coreMain
        if (config.containsKey("coreMain")) {
            Map<String, Object> coreMain = (Map<String, Object>) config.get("coreMain");
            String key = (String) coreMain.get("key");
            AssignDataVo assignDataVo = specialDataVService.getAssignedData(key, stationSn);
            if (assignDataVo != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("label", assignDataVo.getLabel());
                map.put("key", key);
                map.put("value", assignDataVo.getValue());
                map.put("unit", assignDataVo.getUnit());
                config.put("coreMain", map);
            }
        }

        //计算coreSubStatData
        if (config.containsKey("coreSubStatData")) {
            List<Map<String, Object>> listMap = (List<Map<String, Object>>) config.get("coreSubStatData");
            List<Map<String, Object>> newListMap = new ArrayList<>();
            listMap.forEach(item -> {
                String key = (String) item.get("key");
                AssignDataVo assignDataVo = specialDataVService.getAssignedData((String) item.get("key"), stationSn);
                if (assignDataVo != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("label", assignDataVo.getLabel());
                    map.put("key", key);
                    map.put("value", assignDataVo.getValue());
                    map.put("unit", assignDataVo.getUnit());
                    newListMap.add(map);
                }
            });
            config.put("coreSubStatData", newListMap);
        }
        return success(config);
    }
}
