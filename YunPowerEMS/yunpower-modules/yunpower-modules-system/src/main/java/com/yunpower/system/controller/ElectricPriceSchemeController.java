package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.system.service.IPublicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.domain.ElectricPriceScheme;
import com.yunpower.system.service.IElectricPriceSchemeService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 电价标准
 *
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@Api(tags = "D 电价标准表")
@RestController
@RequestMapping("/price-scheme")
public class ElectricPriceSchemeController extends BaseController {
    @Autowired
    private IElectricPriceSchemeService electricPriceSchemeService;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询电价标准列表
     */
    @ApiOperation("查询电价标准列表")
    @RequiresPermissions("system:price-scheme:list")
    @GetMapping("/list")
    public TableDataInfo list(ElectricPriceScheme electricPriceScheme) {
        startPage();
        List<ElectricPriceScheme> list = electricPriceSchemeService.selectElectricPriceSchemeList(electricPriceScheme);
        return getDataTable(list);
    }

    /**
     * 查询电价标准列表（不分页）
     */
    @ApiOperation("查询电价标准列表（不分页）")
    @RequiresPermissions("system:price-scheme:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(ElectricPriceScheme electricPriceScheme) {
        List<ElectricPriceScheme> list = electricPriceSchemeService.selectElectricPriceSchemeList(electricPriceScheme);
        return success(list);
    }

    /**
     * 导出电价标准列表
     */
    @ApiOperation("导出电价标准列表")
    @RequiresPermissions("system:price-scheme:export")
    @Log(title = "电价标准", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ElectricPriceScheme electricPriceScheme) {
        List<ElectricPriceScheme> list = electricPriceSchemeService.selectElectricPriceSchemeList(electricPriceScheme);
        ExcelUtil<ElectricPriceScheme> util = new ExcelUtil<ElectricPriceScheme>(ElectricPriceScheme.class);
        util.exportExcel(response, list, "电价标准数据");
    }

    /**
     * 获取电价标准详细信息
     */
    @ApiOperation("获取电价标准详细信息")
    @RequiresPermissions("system:price-scheme:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(electricPriceSchemeService.selectElectricPriceSchemeById(id));
    }

    /**
     * 新增电价标准
     */
    @ApiOperation("新增电价标准")
    @RequiresPermissions("system:price-scheme:add")
    @Log(title = "电价标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectricPriceScheme electricPriceScheme) {
        // 判断是否有相同年份的，一年只能配置一条
        if (electricPriceSchemeService.checkEffectYearUnique(publicService.getCurrentStation(), electricPriceScheme.getEffectYear()) != null) {
            return error("当前生效年份配置已经存在");
        }

        electricPriceScheme.setParentId(0L);
        electricPriceScheme.setCapacityPrice(0f);
        electricPriceScheme.setDemandPrice(0f);
        electricPriceScheme.setDemandPercent(0f);
        electricPriceScheme.setDemandRate(0f);
        electricPriceScheme.setElectricUp(0f);
        electricPriceScheme.setElectricUpRate(0f);
        electricPriceScheme.setAdditivePrice(0f);
        return toAjax(electricPriceSchemeService.insertElectricPriceScheme(electricPriceScheme));
    }

    /**
     * 修改电价标准
     */
    @ApiOperation("修改电价标准")
    @RequiresPermissions("system:price-scheme:edit")
    @Log(title = "电价标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectricPriceScheme electricPriceScheme) {
        // 判断是否有相同年份的，一年只能配置一条
        if (electricPriceSchemeService.checkEffectYearUnique(publicService.getCurrentStation(), electricPriceScheme.getEffectYear()) != null) {
            return error("当前生效年份配置已经存在");
        }
        return toAjax(electricPriceSchemeService.updateElectricPriceScheme(electricPriceScheme));
    }

    /**
     * 修改电价标准状态
     */
    @ApiOperation("修改电价标准状态")
    @RequiresPermissions("system:price-scheme:state")
    @Log(title = "电价标准", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        ElectricPriceScheme info = electricPriceSchemeService.selectElectricPriceSchemeById(id);
        if(info == null) {
            return error("未找到对应数据");
        }
        // 停用可以，但是启用前得判断一下是否已经有同年份的配置
        if (state == 0 && electricPriceSchemeService.checkEffectYearUnique(publicService.getCurrentStation(), info.getEffectYear()) != null) {
            return error("当前生效年份配置已经存在");
        }
        return toAjax(electricPriceSchemeService.updateElectricPriceSchemeState(new ElectricPriceScheme(), id, state));
    }

    /**
     * 删除电价标准
     */
    @ApiOperation("删除电价标准")
    @RequiresPermissions("system:price-scheme:remove")
    @Log(title = "电价标准", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(electricPriceSchemeService.deleteElectricPriceSchemeByIds(new ElectricPriceScheme(), ids));
    }
}
