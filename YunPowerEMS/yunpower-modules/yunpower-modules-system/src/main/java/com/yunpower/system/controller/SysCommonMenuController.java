package com.yunpower.system.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.yunpower.common.core.constant.UserConstants;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.SysCommonDictData;
import com.yunpower.system.api.domain.SysUser;
import com.yunpower.system.domain.AlarmTriggerConfig;
import com.yunpower.system.service.ISysCommonDictService;
import com.yunpower.system.service.ISysUserService;
import com.yunpower.common.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.system.domain.SysCommonMenu;
import com.yunpower.system.service.ISysCommonMenuService;

/**
 * 菜单信息
 *
 * @author yunpower
 */
@Api(tags = "C 菜单信息表")
@RestController
@RequestMapping("/menu")
public class SysCommonMenuController extends BaseController {
    @Autowired
    private ISysCommonMenuService menuService;

    @Autowired
    private ISysCommonDictService commonDictService;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取菜单列表（不分页）
     */
    @ApiOperation("获取菜单列表（不分页）")
    @RequiresPermissions("system:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SysCommonMenu menu) {
        Long userId = SecurityUtils.getUserId();
        List<SysCommonMenu> menus = menuService.selectMenuList(menu, userId);
        return success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @ApiOperation("根据菜单编号获取详细信息")
    @RequiresPermissions("system:menu:query")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation("获取菜单下拉列表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysCommonMenu menu) {
        Long userId = SecurityUtils.getUserId();
        List<SysCommonMenu> menus = menuService.selectMenuList(menu, userId);
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @ApiOperation("加载对应角色菜单列表树")
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        Long userId = SecurityUtils.getUserId();
        List<SysCommonMenu> menus = menuService.selectMenuList(userId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * 新增菜单
     */
    @ApiOperation("新增菜单")
    @RequiresPermissions("system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysCommonMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getOpenType()) && !StringUtils.ishttp(menu.getPath())) {
            return error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @ApiOperation("修改菜单")
    @RequiresPermissions("system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysCommonMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getOpenType()) && !StringUtils.ishttp(menu.getPath())) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getId().equals(menu.getParentId())) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @ApiOperation("删除菜单")
    @RequiresPermissions("system:menu:remove")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return warn("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return warn("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }

    /**
     * 获取路由信息（若依）
     *
     * @return 路由信息
     */
    @ApiOperation("获取菜单路由信息")
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysCommonMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return success(menuService.buildMenus(menus));
    }

    /**
     * 获取（新能源）路由信息
     * 前端菜单获取逻辑：
     * 1、先获取主模块，共9个
     * 2、然后取出所有菜单，分别放入9个模块中
     *
     * @param menuModel 模块标识（可选）
     * @return 路由信息
     */
    @ApiOperation("获取（新能源）菜单路由信息")
    @GetMapping("getEnergyRouters")
    public AjaxResult getEnergyRouters(Integer menuModel) {
        Long userId = SecurityUtils.getUserId();
        List<SysCommonMenu> menus = menuService.selectMenuTreeByUserId(userId, menuModel);
        return success(menuService.buildEnergyMenus(menus, null));
    }

    /**
     * 根据用户ID查询已选择的模块
     *
     * @return 模块列表
     */
    @ApiOperation("获取（新能源）菜单模块")
    @GetMapping("getEnergyMenuModels")
    public AjaxResult getEnergyMenuModels() {
        //当前用户
        Long userId = SecurityUtils.getUserId();

        //取出枚举中的模块
        List<SysCommonDictData> dictDataList = commonDictService.selectDictDataByType("sys_menu_model");

        SysUser sysUser = userService.selectUserById(userId);

        //管理员
        if (sysUser != null && SecurityUtils.isAdmin(sysUser.getIsSupper())) {
            dictDataList = dictDataList.stream().sorted(Comparator.comparing(SysCommonDictData::getDictValue)).collect(Collectors.toList());
            return success(dictDataList);
        } else {
            List<Integer> menuModels = menuService.selectMenuModelByUserId(userId);

            List<SysCommonDictData> newDictList = new ArrayList<>();
            for (SysCommonDictData item : dictDataList) {
                if (menuModels.contains(Integer.parseInt(item.getDictValue()))) {
                    newDictList.add(item);
                }
            }
            if (!newDictList.isEmpty()) {
                newDictList = newDictList.stream().sorted(Comparator.comparing(SysCommonDictData::getDictValue)).collect(Collectors.toList());
            }
            return success(newDictList);
        }
    }


    /**
     * 修改菜单状态
     */
    @ApiOperation("修改菜单状态")
    @RequiresPermissions("system:menu:state")
    @Log(title = "菜单", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(menuService.updateMenuState(new SysCommonMenu(), id, state));
    }
}