package com.yunpower.system.controller;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.domain.ElectricPriceScheme;
import com.yunpower.system.domain.ElectricPriceSchemeConfig;
import com.yunpower.system.domain.jsonvo.SeasonalRangeChartVo;
import com.yunpower.system.domain.jsonvo.SeasonalRangeVo;
import com.yunpower.system.service.IElectricPriceSchemeConfigService;
import com.yunpower.system.service.IElectricPriceSchemeService;
import com.yunpower.system.service.IPublicService;
import com.yunpower.common.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 电度电价配置
 *
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@Api(tags = "D 电度电价配置表")
@RestController
@RequestMapping("/price-scheme-config")
public class ElectricPriceSchemeConfigController extends BaseController {
    @Autowired
    private IElectricPriceSchemeConfigService electricPriceSchemeConfigService;

    @Autowired
    private IElectricPriceSchemeService electricPriceSchemeService;

    @Autowired
    private IPublicService publicService;

    /**
     * 获取电度电价配置详细信息
     */
    @ApiOperation("获取电度电价配置详细信息")
    @RequiresPermissions("system:price-scheme-config:query")
    @GetMapping(value = "/{schemeId}")
    public TableDataInfo getInfo(@PathVariable("schemeId") Long schemeId) {
        ElectricPriceSchemeConfig electricPriceSchemeConfig = new ElectricPriceSchemeConfig();
        electricPriceSchemeConfig.setSchemeId(schemeId);
        List<ElectricPriceSchemeConfig> list = electricPriceSchemeConfigService.selectElectricPriceSchemeConfigList(electricPriceSchemeConfig);
        return getDataTable(list);
    }

    /**
     * 新增电度电价配置（直接调用修改接口，不需要新增接口）
     @ApiOperation("新增电度电价配置")
     @RequiresPermissions("system:price-scheme-config:add")
     @Log(title = "电度电价配置", businessType = BusinessType.INSERT)
     @PostMapping public AjaxResult add(@RequestBody List<ElectricPriceSchemeConfig> electricPriceSchemeConfigList) {
     int rows = 0;
     for (ElectricPriceSchemeConfig item : electricPriceSchemeConfigList) {
     packageScheme(item);
     rows = electricPriceSchemeConfigService.insertElectricPriceSchemeConfig(item);
     }
     return toAjax(rows);
     }*/

    /**
     * 修改电度电价配置
     */
    @ApiOperation("修改电度电价配置")
    @RequiresPermissions("system:price-scheme-config:edit")
    @Log(title = "电度电价配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody List<ElectricPriceSchemeConfig> electricPriceSchemeConfigList) {
        if (electricPriceSchemeConfigList.isEmpty()) {
            return error("无提交数据");
        }

        //暴力删除所有配置，然后再重新插入
        Long schemeId = electricPriceSchemeConfigList.get(0).getSchemeId();
        if (schemeId == null || schemeId <= 0) {
            return error("未查询到电价标准");
        }
        electricPriceSchemeConfigService.deleteElectricPriceSchemeConfigBySchemeId(schemeId);

        int rows = 0;
        for (ElectricPriceSchemeConfig item : electricPriceSchemeConfigList) {
            packageScheme(item);
            rows = electricPriceSchemeConfigService.insertElectricPriceSchemeConfig(item);
        }
        return toAjax(rows);
    }

    public void packageScheme(ElectricPriceSchemeConfig item) {
        //没有用到，给个默认值
        item.setWholePrice(0f);

        //如果选择了全天电价，则加一条记录到 seasonal_range，否则给 uniform_price 设为0
        if (item.getPriceType() == 1) {
            item.setUniformPrice(0f);
        } else {
            List<SeasonalRangeVo> rangeVos = new ArrayList<>();
            SeasonalRangeVo vo = new SeasonalRangeVo();
            vo.setStartTime("00:00");
            vo.setEndTime("24:00");
            vo.setSeasonalType(3);
            vo.setSeasonalName("平");
            vo.setChargePrice(item.getUniformPrice());
            rangeVos.add(vo);
            item.setSeasonalRange(rangeVos);
        }
    }

    ///**
    // * 重新计算分时电价
    // * 更新当前站点下的（变量）数据
    // */
    //@ApiOperation("重新计算分时电价")
    //@RequiresPermissions("system:price-scheme-config:edit")
    //@Log(title = "电度电价配置", businessType = BusinessType.UPDATE)
    //@GetMapping(value = "/recalculateSeasonalPrice/{id}")
    //public AjaxResult recalculateSeasonalPrice(@PathVariable("id") Long id) {
    //    return toAjax(electricPriceSchemeConfigService.recalculateSeasonalPrice(id));
    //}

    /**
     * 重新计算所有的分时电价（通过电价标准ID）
     * 更新当前站点下的（变量）数据
     */
    @ApiOperation("重新计算所有的分时电价（通过电价标准ID）")
    @RequiresPermissions("system:price-scheme-config:edit")
    @Log(title = "电度电价配置", businessType = BusinessType.UPDATE)
    @GetMapping(value = "/recalculateSeasonalPriceBySchemeId/{schemeId}")
    public AjaxResult recalculateSeasonalPriceBySchemeId(@PathVariable("schemeId") Long schemeId) {
        int result = electricPriceSchemeConfigService.recalculateSeasonalPriceBySchemeId(schemeId);
        if (result == 0) {
            return error("未查询到配置信息");
        }
        if (result == -1) {
            return error("当前站点绑定的不是此电价标准");
        }

        return toAjax(result);
    }

    /**
     * 通过电价标准ID获取尖峰平谷时间段
     *
     * @param schemeId 电价标准ID
     * @return 结果
     */
    @ApiOperation("通过电价标准ID获取尖峰平谷时间段")
    @GetMapping("/getSeasonalRangeList/{schemeId}")
    public AjaxResult getSeasonalRangeList(@PathVariable("schemeId") Long schemeId) {
        return success(electricPriceSchemeConfigService.selectSeasonalRangeList(schemeId));
    }

    /**
     * 获取站点配置的尖峰平谷时间段
     * 峰谷分析用到
     *
     * @return 结果
     */
    @ApiOperation("获取站点配置的尖峰平谷时间段")
    @GetMapping("/getSeasonalRangeList")
    public AjaxResult getSeasonalRangeList() {
        // 获取当前站点信息
        SysStation station = publicService.getCurrentStationInfo();
        if (station != null) {
            return success(electricPriceSchemeConfigService.selectSeasonalRangeList(station.getSchemeId()));
        } else {
            return error("请先配置电价标准");
        }
    }


    /**
     * 获取站点配置的尖峰平谷时间段
     * 峰谷分析用到
     *
     * @return 结果
     */
    @InnerAuth
    @ApiOperation("获取站点配置的尖峰平谷时间段-内部调用")
    @GetMapping("/getSeasonalRangeList/innerAuth")
    public Map<String, List<SeasonalRangeChartVo>> getSeasonalRangeListInner(@RequestParam("schemeId") Long schemeId)  {
        return electricPriceSchemeConfigService.selectSeasonalRangeList(getDefaultSchemeId(schemeId
        ));
    }

    private Long getDefaultSchemeId(Long schemeId) {
        // 获取当前站点信息
        SysStation station = publicService.getCurrentStationInfo();
        if(StringUtils.isNull(schemeId) || schemeId <= 0){
            ElectricPriceScheme query = new ElectricPriceScheme();
            query.setDeptId(station.getDeptId());
            query.setEffectYear(DateUtils.getCurrYear());
            ElectricPriceScheme priceScheme = electricPriceSchemeService.selectElectricPriceScheme(query);
            if(priceScheme!=null){
                schemeId = priceScheme.getId();
            }
        }
        return schemeId;
    }

    /**
     * 获取站点配置的尖峰平谷价格
     * 峰谷分析用到
     *
     * @return 结果
     */
    @InnerAuth
    @ApiOperation("获取站点配置的尖峰平谷价格-内部调用")
    @GetMapping("/getSeasonalRangeListForDayStack/innerAuth")
    public List<Map<String, Object>> getSeasonalRangeListForDayStack()  {
        return electricPriceSchemeConfigService.selectSeasonalRangeListForDayStack(getDefaultSchemeId(null));
    }
}
