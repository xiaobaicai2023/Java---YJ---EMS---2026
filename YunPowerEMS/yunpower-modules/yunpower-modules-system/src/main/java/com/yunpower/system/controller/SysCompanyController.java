package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.system.api.domain.SysGroup;
import com.yunpower.system.service.ISysGroupService;
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
import com.yunpower.system.domain.SysCompany;
import com.yunpower.system.service.ISysCompanyService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 公司
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "G 公司管理表")
@RestController
@RequestMapping("/company")
public class SysCompanyController extends BaseController {
    @Autowired
    private ISysCompanyService sysCompanyService;

    @Autowired
    private ISysGroupService groupService;

    /**
     * 查询公司列表（分页，树结构）
     */
    @ApiOperation("查询公司列表（分页，树结构）")
    @RequiresPermissions("system:company:list")
    @GetMapping("/list")
    public TableDataInfo list(SysCompany sysCompany) {
        //取出父公司，然后再循环取出子公司，做到分页且树结构
        sysCompany.setParentId(0L);
        startPage();
        List<SysCompany> list = sysCompanyService.selectSysCompanyList(sysCompany);
        packageInfo(list);
        return getDataTable(list);
    }

    private void packageInfo(List<SysCompany> list) {
        for (SysCompany item : list) {
            //行业名称
            SysGroup group = groupService.selectSysGroupById(item.getIndustryGroupId());
            item.setIndustryGroupName(group == null ? "" : group.getGroupName());
        }
    }

    /**
     * 查询公司列表（不分页，树结构）
     */
    @ApiOperation("查询公司列表（不分页，树结构）")
    @RequiresPermissions("system:company:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(SysCompany sysCompany) {
        List<SysCompany> list = sysCompanyService.selectSysCompanyList(sysCompany);
        packageInfo(list);
        list = sysCompanyService.buildCompanyTree(list);
        return success(list);
    }

    /**
     * 导出公司列表
     */
    @ApiOperation("导出公司列表")
    @RequiresPermissions("system:company:export")
    @Log(title = "公司", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCompany sysCompany) {
        List<SysCompany> list = sysCompanyService.selectSysCompanyList(sysCompany);
        ExcelUtil<SysCompany> util = new ExcelUtil<SysCompany>(SysCompany.class);
        util.exportExcel(response, list, "公司数据");
    }

    /**
     * 获取公司详细信息
     */
    @ApiOperation("获取公司详细信息")
    @RequiresPermissions("system:company:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysCompanyService.selectSysCompanyById(id));
    }

    /**
     * 新增公司
     */
    @ApiOperation("新增公司")
    @RequiresPermissions("system:company:add")
    @Log(title = "公司", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCompany sysCompany) {
        if (sysCompany.getParentId() == 0) {
            sysCompany.setCompanyType(1); //公司
        } else {
            sysCompany.setCompanyType(2); //部门
        }
        return toAjax(sysCompanyService.insertSysCompany(sysCompany));
    }

    /**
     * 修改公司
     */
    @ApiOperation("修改公司")
    @RequiresPermissions("system:company:edit")
    @Log(title = "公司", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCompany sysCompany) {
        if (sysCompany.getParentId() == 0) {
            sysCompany.setCompanyType(1); //公司
        } else {
            sysCompany.setCompanyType(2); //部门
        }
        return toAjax(sysCompanyService.updateSysCompany(sysCompany));
    }

    /**
     * 修改公司状态
     */
    @ApiOperation("修改公司状态")
    @RequiresPermissions("system:company:state")
    @Log(title = "公司", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(sysCompanyService.updateSysCompanyState(new SysCompany(), id, state));
    }

    /**
     * 删除公司
     */
    @ApiOperation("删除公司")
    @RequiresPermissions("system:company:remove")
    @Log(title = "公司", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysCompanyService.deleteSysCompanyByIds(new SysCompany(), ids));
    }
}
