package com.yunpower.system.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.enums.DataAuthEnum;
import com.yunpower.common.core.enums.MenuAuthEnum;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysRole;
import com.yunpower.system.api.domain.SysUser;
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
import com.yunpower.system.domain.SysUserRole;
import com.yunpower.system.service.ISysDeptService;
import com.yunpower.system.service.ISysRoleService;
import com.yunpower.system.service.ISysUserService;

/**
 * 角色信息
 *
 * @author yunpower
 */
@Api(tags = "J 角色信息表")
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;

    @ApiOperation("获取角色列表")
    @RequiresPermissions("system:role:list")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    /**
     * 获取角色列表（不分页）
     */
    @ApiOperation("获取角色列表（不分页）")
    @RequiresPermissions("system:role:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        return success(list);
    }

    @ApiOperation("导出角色信息")
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:role:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        util.exportExcel(response, list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @ApiOperation("根据编号获取详细信息")
    @RequiresPermissions("system:role:query")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @ApiOperation("新增角色")
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setMenuCheckStrictly(true); //菜单树选择项是否关联显示（父子联动）
        role.setDeptCheckStrictly(true); //部门树选择项是否关联显示（父子联动）
        role.setCreateBy(SecurityUtils.getUsername());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @ApiOperation("修改角色")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getId());
        if (!roleService.checkRoleNameUnique(role)) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setMenuCheckStrictly(true); //菜单树选择项是否关联显示（父子联动）
        role.setDeptCheckStrictly(true); //部门树选择项是否关联显示（父子联动）
        role.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 修改保存数据权限
     */
    @ApiOperation("修改数据权限")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getId());

        role.setMenuCheckStrictly(true); //父子联动
        role.setDeptCheckStrictly(true); //父子联动
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 角色状态修改
     */
    @ApiOperation("修改角色状态")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        SysRole role = roleService.selectRoleById(id);
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getId());

        SysRole update = new SysRole();
        update.setId(id);
        update.setStopFlag(state);
        update.setUpdateBy(SecurityUtils.getUsername());
        update.setUpdateTime(new Date());
        return toAjax(roleService.updateRoleStatus(update));
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @ApiOperation("获取角色选择框列表")
    @RequiresPermissions("system:role:query")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        return success(roleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     */
    @ApiOperation("查询已分配用户角色列表")
    @RequiresPermissions("system:role:list")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
    @ApiOperation("查询未分配用户角色列表")
    @RequiresPermissions("system:role:list")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
    @ApiOperation("取消授权用户")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        if (StringUtils.longIsBlack0(userRole.getUserId())) {
            return error("请输入userId");
        }
        if (StringUtils.longIsBlack0(userRole.getRoleId())) {
            return error("请输入roleId");
        }
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
    @ApiOperation("批量取消授权用户")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(@RequestBody SysUserRole userRole) {
        if (StringUtils.longIsBlack0(userRole.getRoleId())) {
            return error("请输入roleId");
        }
        if (userRole.getUserIds() == null || userRole.getUserIds().length == 0) {
            return error("请输入userIds");
        }
        return toAjax(roleService.deleteAuthUsers(userRole.getRoleId(), userRole.getUserIds()));
    }

    /**
     * 批量选择用户授权
     */
    @ApiOperation("批量选择用户授权")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(@RequestBody SysUserRole userRole) {
        if (StringUtils.longIsBlack0(userRole.getRoleId())) {
            return error("请输入roleId");
        }
        if (userRole.getUserIds() == null || userRole.getUserIds().length == 0) {
            return error("请输入userIds");
        }
        roleService.checkRoleDataScope(userRole.getRoleId());
        return toAjax(roleService.insertAuthUsers(userRole.getRoleId(), userRole.getUserIds()));
    }

    /**
     * 获取对应角色部门树列表
     */
    @ApiOperation("获取对应角色部门树列表")
    @RequiresPermissions("system:role:query")
    @GetMapping(value = "/deptTree/{roleId}")
    public AjaxResult deptTree(@PathVariable("roleId") Long roleId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
        return ajax;
    }

    @ApiOperation("获取菜单权限选择列表")
    @RequiresPermissions("system:role:edit")
    @GetMapping("/menuSelectList")
    public TableDataInfo getMenuSelectList() {
        Integer isSuper = SecurityUtils.getUserSupper();
        return getDataTable(MenuAuthEnum.getEnumList(isSuper));
    }

    @ApiOperation("获取数据权限选择列表")
    @RequiresPermissions("system:role:edit")
    @GetMapping("/dataSelectList")
    public TableDataInfo getDataSelectList() {
        Integer isSuper = SecurityUtils.getUserSupper();
        return getDataTable(DataAuthEnum.getEnumList(isSuper));
    }
}
