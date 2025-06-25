package com.yunpower.system.controller;

import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.DashboardConfigCard;
import com.yunpower.system.service.IDashboardConfigCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 仪表盘卡片配置
 * 
 * @author yunpower
 * @date 2024-06-05
 */
@Api(tags = "D 仪表盘卡片配置表")
@RestController
@RequestMapping("/dashboard/card/config")
public class DashboardConfigCardController extends BaseController
{
    @Autowired
    private IDashboardConfigCardService dashboardConfigCardService;

    /**
     * 查询仪表盘卡片配置列表
     */
    @ApiOperation("查询仪表盘卡片配置列表")
    @RequiresPermissions("system:card:list")
    @GetMapping("/list")
    public TableDataInfo list(DashboardConfigCard dashboardConfigCard)
    {
        startPage();
        List<DashboardConfigCard> list = dashboardConfigCardService.selectDashboardConfigCardList(dashboardConfigCard);
        return getDataTable(list);
    }

    /**
     * 查询仪表盘卡片配置列表
     */
    @ApiOperation("查询仪表盘卡片配置列表-不带分页")
    @RequiresPermissions("system:card:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(DashboardConfigCard dashboardConfigCard)
    {
        List<DashboardConfigCard> list = dashboardConfigCardService.selectDashboardConfigCardList(dashboardConfigCard);
        return success(list);
    }

    /**
     * 导出仪表盘卡片配置列表
     */
    @ApiOperation("导出仪表盘卡片配置列表")
    @RequiresPermissions("system:card:export")
    @Log(title = "仪表盘卡片配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DashboardConfigCard dashboardConfigCard)
    {
        List<DashboardConfigCard> list = dashboardConfigCardService.selectDashboardConfigCardList(dashboardConfigCard);
        ExcelUtil<DashboardConfigCard> util = new ExcelUtil<DashboardConfigCard>(DashboardConfigCard.class);
        util.exportExcel(response, list, "仪表盘卡片配置数据");
    }


    /**
     * 获取仪表盘卡片配置详细信息-id
     */
    @ApiOperation("获取仪表盘卡片配置详细信息")
    @RequiresPermissions("system:card:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dashboardConfigCardService.selectDashboardConfigCardById(id));
    }

    /**
     * 获取仪表盘卡片配置详细信息-dashboardConfigId-cardKey
     */
    @ApiOperation("获取仪表盘卡片配置详细信息")
    @RequiresPermissions("system:card:query")
    @GetMapping(value = "/{dashboardConfigId}/{cardKey}")
    public AjaxResult getInfoByConfigIdWithCardKey(@PathVariable("dashboardConfigId") Long dashboardConfigId, @PathVariable("cardKey") String cardKey)
    {
        return success(dashboardConfigCardService.selectInfoByConfigIdWidthCardKey(dashboardConfigId,cardKey));
    }

    /**
     * 新增仪表盘卡片配置
     */
    @ApiOperation("新增仪表盘卡片配置")
    @RequiresPermissions("system:card:add")
    @Log(title = "仪表盘卡片配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated  @RequestBody DashboardConfigCard dashboardConfigCard)
    {
        return toAjax(dashboardConfigCardService.insertDashboardConfigCard(dashboardConfigCard));
    }

    /**
     * 修改仪表盘卡片配置
     */
    @ApiOperation("修改仪表盘卡片配置")
    @RequiresPermissions("system:card:edit")
    @Log(title = "仪表盘卡片配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody DashboardConfigCard dashboardConfigCard)
    {
        return toAjax(dashboardConfigCardService.updateDashboardConfigCard(dashboardConfigCard));
    }

    /**
     * 修改仪表盘卡片配置状态
     */
    @ApiOperation("修改仪表盘卡片配置状态")
    @RequiresPermissions("system:card:state")
    @Log(title = "仪表盘卡片配置", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state)
    {
        return toAjax(dashboardConfigCardService.updateDashboardConfigCardState(id, state));
    }

    /**
     * 删除仪表盘卡片配置
     */
    @ApiOperation("删除仪表盘卡片配置")
    @RequiresPermissions("system:card:remove")
    @Log(title = "仪表盘卡片配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dashboardConfigCardService.deleteDashboardConfigCardByIds(ids));
    }

    /**
     * datav项目调用-获取卡片配置
     * 说明：@innerAuth+AOP 实现 yunpower-api 远程接口
     */
    @ApiOperation("datav项目调用-获取卡片配置")
    @InnerAuth
    @GetMapping("/inner/{dashboardConfigId}/{cardKey}")
    public DashboardConfigCard innerGetInfo(@PathVariable("dashboardConfigId") Long dashboardConfigId, @PathVariable("cardKey") String cardKey)
    {
        return dashboardConfigCardService.selectInfoByConfigIdWidthCardKey(dashboardConfigId,cardKey);
    }
}
