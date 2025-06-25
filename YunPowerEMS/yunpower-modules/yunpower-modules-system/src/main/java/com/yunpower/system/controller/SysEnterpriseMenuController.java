package com.yunpower.system.controller;

import java.util.List;

import com.yunpower.common.core.enums.MenuAuthEnum;
import com.yunpower.common.core.enums.vo.EnumIOVO;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.domain.SysCommonMenu;
import com.yunpower.system.service.ISysCommonMenuService;
import com.yunpower.common.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yunpower.system.service.ISysEnterpriseMenuService;

/**
 * 企业专属菜单
 *
 * @author JUNFU.WANG
 * @date 2023-09-28
 */
@Api(tags = "Q 企业专属菜单表")
@RestController
@RequestMapping("/ent-menu")
public class SysEnterpriseMenuController extends BaseController
{
    @Autowired
    private ISysEnterpriseMenuService sysEnterpriseMenuService;

    @Autowired
    private ISysCommonMenuService menuService;

    /**
     * 获取企业专属菜单列表（不分页）
     * 用于：配置企业菜单列表，与用户无关
     */
    @ApiOperation("获取企业专属菜单列表（不分页）")
    @RequiresPermissions("system:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SysCommonMenu menu)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysCommonMenu> menus = menuService.selectEnterpriseMenuList(menu, userId);
        return success(menus);
    }

    /**
     * 获取企业专属菜单下拉树列表
     * 用于：数据权限配置
     */
    @ApiOperation("获取企业专属菜单下拉列表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysCommonMenu menu)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysCommonMenu> menus = menuService.selectEnterpriseMenuList(menu, userId);
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @ApiOperation("加载对应角色企业专属菜单列表树")
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysCommonMenu> menus = menuService.selectEnterpriseMenuList(userId);
        AjaxResult ajax = AjaxResult.success();
        // 可以共用：既然给用户分配了权限，那么一定是企业菜单范围内的
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * 新增企业专属菜单
     */
    @ApiOperation("新增企业专属菜单")
    @RequiresPermissions("system:menu:add")
    @Log(title = "企业专属菜单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Long[] menuIds)
    {
        return toAjax(sysEnterpriseMenuService.insertSysEnterpriseMenu(menuIds));
    }

    /**
     * 获取菜单权限下拉列表
     */
    @ApiOperation("获取菜单权限下拉列表")
    @GetMapping(value = "/menuScopeList")
    public AjaxResult menuScopeList()
    {
        Integer isSupper=SecurityUtils.getUserSupper();
        List<EnumIOVO> menuScopeList= MenuAuthEnum.getEnumList(isSupper);
        return success(menuScopeList);
    }
}
