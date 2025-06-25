package com.yunpower.system.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.domain.R;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysRole;
import com.yunpower.system.api.domain.SysUser;
import com.yunpower.system.api.model.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.yunpower.system.service.ISysCommonConfigService;
import com.yunpower.system.service.ISysDeptService;
import com.yunpower.system.service.ISysPermissionService;
import com.yunpower.system.service.ISysPostService;
import com.yunpower.system.service.ISysRoleService;
import com.yunpower.system.service.ISysUserService;

import static com.yunpower.common.core.constant.UserConstants.PASSWORD_MIN_LENGTH;

/**
 * 用户信息
 *
 * @author yunpower
 */
@Api(tags = "Y 用户信息表")
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysCommonConfigService configService;

    @Value("${app.upload.file-domain}")
    private String fileDomain;

    /**
     * 获取用户列表
     */
    @ApiOperation("获取用户列表")
    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        packageInfo(list);
        return getDataTable(list);
    }

    private void packageInfo(List<SysUser> list) {
        for (SysUser item : list) {
            //取出用户的岗位
            item.setPostNames(userService.selectUserPostGroup(item.getLogonName()));
            //取出用户的角色
            item.setRoleNames(userService.selectUserRoleGroup(item.getLogonName()));
        }
    }

    /**
     * 获取用户列表（不分页）
     */
    @ApiOperation("获取用户列表（不分页）")
    @RequiresPermissions("system:user:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        packageInfo(list);
        return success(list);
    }

    @ApiOperation("导出用户信息")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @ApiOperation("导入用户信息")
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @ApiOperation("导出用户模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 获取当前用户信息
     */
    @ApiOperation("根据用户名获取当前用户信息")
    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    /**
     * 注册用户信息
     */
    @ApiOperation("注册用户信息")
    @InnerAuth
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody SysUser sysUser) {
        String username = sysUser.getLogonName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return R.fail("当前系统没有开启注册功能！");
        }
        if (!userService.checkUserNameUnique(sysUser)) {
            return R.fail("保存用户'" + username + "'失败，注册账号已存在");
        }
        return R.ok(userService.registerUser(sysUser));
    }

    /**
     * 登录后修改用户信息
     */
    @ApiOperation("登录后修改用户信息")
    @InnerAuth
    @PostMapping("/modifyLoginInfo")
    public R<Boolean> modifyLoginInfo(@RequestBody SysUser sysUser) {
        return R.ok(userService.modifyLoginInfo(sysUser));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation("获取当前用户信息")
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = userService.selectUserById(SecurityUtils.getUserId());
        if (user == null) {
            throw new RuntimeException("登录用户不存在");
        }
        //用户可在前端自行修改头像
        //user.setHeadPic(user.getHeadPic());
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        //普通用户没有改过密码的则返回true
        ajax.put("initPassword", !user.isAdmin() && StringUtils.isNull(user.getPwdUpdateDate()));
        return ajax;
    }

    /**
     * 根据用户编号获取详细信息
     */
    @ApiOperation("根据编号获取详细信息")
    @RequiresPermissions("system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put("roles", SysUser.isAdmin(sysUser.getIsSupper()) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
            ajax.put("posts", postService.selectPostAll());
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @ApiOperation("新增用户")
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {

        if (StringUtils.isEmpty(user.getLogonPwd())) {
            return error("密码不能为空");
        }
        if (user.getLogonPwd().length() < PASSWORD_MIN_LENGTH) {
            return error("密码长度至少" + PASSWORD_MIN_LENGTH + "位");
        }
        if (user.getLogonPwd().length() > 32) {
            return error("密码长度超出限制");
        }

        if (!user.getLogonPwd().equals(user.getRawPassword())) {
            return error("密码和确认的密码不一致，请重新输入");
        }

        // 设置默认值
        user.setPostId(0);
        user.setUserLevel(0);
        user.setMarry(0);
        user.setAge(0);
        user.setCardType(0);
        user.setIsAuth(0);
        user.setProvince(0);
        user.setCity(0);
        user.setCounty(0);
        user.setTown(0);
        user.setVillage(0);
        user.setLoginTimes(0);

        // 普通用户添加用户时不能设置此项，所以默认给0
        if (user.getIsSupper() == null) {
            user.setIsSupper(0);
        }

        deptService.checkDeptDataScope(user.getDeptId());
        roleService.checkRoleDataScope(user.getRoleIds());

        if (!userService.checkNickNameUnique(user)) {
            return error("新增用户（" + user.getNickName() + "）失败，用户姓名已存在");
        } else if (!userService.checkUserNameUnique(user)) {
            return error("新增用户（" + user.getLogonName() + "）失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getMobile()) && !userService.checkPhoneUnique(user)) {
            return error("新增用户（" + user.getLogonName() + "）失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("新增用户（" + user.getLogonName() + "）失败，邮箱账号已存在");
        }

        user.setCreateBy(SecurityUtils.getUsername());
        user.setLogonPwd(SecurityUtils.encryptPassword(user.getLogonPwd()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @ApiOperation("修改用户")
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getId());
        deptService.checkDeptDataScope(user.getDeptId());
        roleService.checkRoleDataScope(user.getRoleIds());

        if (!userService.checkNickNameUnique(user)) {
            return error("修改用户（" + user.getNickName() + "）失败，用户姓名已存在");
        } else if (!userService.checkUserNameUnique(user)) {
            return error("修改用户（" + user.getLogonName() + "）失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getMobile()) && !userService.checkPhoneUnique(user)) {
            return error("修改用户（" + user.getLogonName() + "）失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("修改用户（" + user.getLogonName() + "）失败，邮箱账号已存在");
        }
        return toAjax(userService.updateUser(user));
    }

    /**
     * 重置密码
     */
    @ApiOperation("重置密码")
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        if (user.getLogonPwd().length() < PASSWORD_MIN_LENGTH) {
            return error("密码长度至少" + PASSWORD_MIN_LENGTH + "位");
        }
        if (user.getLogonPwd().length() > 32) {
            return error("密码长度超出限制");
        }
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getId());
        user.setLogonPwd(SecurityUtils.encryptPassword(user.getLogonPwd()));
        user.setUpdateBy(SecurityUtils.getUsername());
        user.setUpdateTime(new Date());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @ApiOperation("修改用户状态")
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        SysUser user = userService.selectUserById(id);
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getId());

        SysUser update = new SysUser();
        update.setId(id);
        update.setStopFlag(state);
        update.setUpdateBy(SecurityUtils.getUsername());
        update.setUpdateTime(new Date());
        return toAjax(userService.updateUserStatus(update));
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @RequiresPermissions("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, SecurityUtils.getUserId())) {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @ApiOperation("根据编号获取授权角色")
    @RequiresPermissions("system:user:query")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(user.getIsSupper()) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @ApiOperation("用户授权角色")
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        roleService.checkRoleDataScope(roleIds);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    @ApiOperation("获取部门树列表")
    @RequiresPermissions("system:user:list")
    @GetMapping("/deptTree")
    public AjaxResult deptTree(SysDept dept) {
        return success(deptService.selectDeptTreeList(dept));
    }
}
