package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysUser;
import com.yunpower.system.service.ISysDeptService;
import com.yunpower.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.common.core.constant.CacheConstants;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.redis.service.RedisService;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysLogLogin;
import com.yunpower.system.service.ISysLogLoginService;

/**
 * 系统访问记录
 *
 * @author yunpower
 */
@Api(tags = "R 系统访问记录表")
@RestController
@RequestMapping("/logininfor")
public class SysLogLoginController extends BaseController {
    @Autowired
    private ISysLogLoginService logininforService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private RedisService redisService;

    @ApiOperation("获取系统访问记录列表")
    @RequiresPermissions("system:logininfor:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogLogin logininfor) {
        startPage();
        List<SysLogLogin> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @ApiOperation("导出系统访问记录")
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:logininfor:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogLogin logininfor) {
        List<SysLogLogin> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogLogin> util = new ExcelUtil<SysLogLogin>(SysLogLogin.class);
        util.exportExcel(response, list, "登录日志");
    }

    @ApiOperation("删除系统访问记录")
    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    @ApiOperation("清空系统访问记录")
    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        logininforService.cleanLogininfor();
        return success();
    }

    @ApiOperation("账户解锁")
    @RequiresPermissions("system:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public AjaxResult unlock(@PathVariable("userName") String userName) {
        redisService.deleteObject(CacheConstants.PWD_ERR_CNT_KEY + userName);
        return success();
    }

    @ApiOperation("新增系统访问记录")
    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLogLogin logininfor) {
        //部门名称
        if (StringUtils.isBlank(logininfor.getDeptName())) {
            SysUser sysUser = userService.selectUserByUserName(logininfor.getUserName());
            if (sysUser != null) {
                SysDept sysDept = deptService.selectDeptById(sysUser.getDeptId());
                if (sysDept != null) {
                    logininfor.setDeptName(sysDept.getDeptName());
                }
            }
        }
        return toAjax(logininforService.insertLogininfor(logininfor));
    }
}