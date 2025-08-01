package com.yunpower.system.controller;

import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysGroup;
import com.yunpower.system.service.ISysGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 常用分组
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "F 分组通用表")
@RestController
@RequestMapping("/group")
public class SysGroupController extends BaseController {
    @Autowired
    private ISysGroupService sysGroupService;

    /**
     * 查询常用分组列表
     */
    @ApiOperation("查询常用分组列表")
    @RequiresPermissions("system:group:list")
    @GetMapping("/list")
    public TableDataInfo list(SysGroup sysGroup) {
        if (sysGroup.getMapId() == null) {
            return getDataTable("请输入MapId");
        }

        startPage();
        List<SysGroup> list = sysGroupService.selectSysGroupList(sysGroup);
        return getDataTable(list);
    }

    /**
     * 查询常用分组列表（不分页，树形结构）
     */
    @ApiOperation("查询常用分组列表（不分页，树形结构）")
    @RequiresPermissions("system:group:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(SysGroup sysGroup) {
        if (sysGroup.getMapId() == null) {
            return error("请输入MapId");
        }

        List<SysGroup> list = sysGroupService.selectSysGroupList(sysGroup);
        list = sysGroupService.buildGroupTree(list);
        return success(list);
    }

    /**
     * 查询常用分组【下拉】列表（不分页，树形结构）
     */
    @ApiOperation("查询常用分组【下拉】列表（不分页，树形结构）")
    @GetMapping("/listSelect")
    public AjaxResult listSelect(SysGroup sysGroup) {
        if (sysGroup.getMapId() == null) {
            return error("请输入MapId");
        }

        List<SysGroup> list = sysGroupService.selectSysGroupList(sysGroup);
        return success(sysGroupService.buildGroupTreeSelect(list));
    }

    /**
     * 导出常用分组列表
     */
    @ApiOperation("导出常用分组列表")
    @RequiresPermissions("system:group:export")
    @Log(title = "常用分组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysGroup sysGroup) {
        List<SysGroup> list = sysGroupService.selectSysGroupList(sysGroup);
        ExcelUtil<SysGroup> util = new ExcelUtil<SysGroup>(SysGroup.class);
        util.exportExcel(response, list, "常用分组数据");
    }

    /**
     * 获取常用分组详细信息
     */
    @ApiOperation("获取常用分组详细信息")
    @RequiresPermissions("system:group:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysGroupService.selectSysGroupById(id));
    }

    /**
     * 新增常用分组
     */
    @ApiOperation("新增常用分组")
    @RequiresPermissions("system:group:add")
    @Log(title = "常用分组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated  @RequestBody SysGroup sysGroup) {
        if (StringUtils.longIsBlack0(sysGroup.getMapId())) {
            return error("mapid不能为空");
        }
        if (sysGroup.getOrderNum() == null) {
            sysGroup.setOrderNum(0);
        }
        if (sysGroup.getIsShow() == null) {
            sysGroup.setIsShow(0);
        }
        if (sysGroup.getIsSystem() == null) {
            sysGroup.setIsSystem(0);
        }
        if (sysGroup.getExt1() == null) {
            sysGroup.setExt1(0);
        }
        if (sysGroup.getExt2() == null) {
            sysGroup.setExt2(0);
        }
        return toAjax(sysGroupService.insertSysGroup(sysGroup));
    }

    /**
     * 修改常用分组
     */
    @ApiOperation("修改常用分组")
    @RequiresPermissions("system:group:edit")
    @Log(title = "常用分组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysGroup sysGroup) {
        return toAjax(sysGroupService.updateSysGroup(sysGroup));
    }

    /**
     * 修改常用分组状态
     */
    @ApiOperation("修改常用分组状态")
    @RequiresPermissions("system:group:state")
    @Log(title = "常用分组", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(sysGroupService.updateSysGroupState(new SysGroup(), id, state));
    }

    /**
     * 删除常用分组
     */
    @ApiOperation("删除常用分组")
    @RequiresPermissions("system:group:remove")
    @Log(title = "常用分组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysGroupService.deleteSysGroupByIds(new SysGroup(), ids));
    }


    /**
     * 远程内部调用-根据类别代码查询分组信息
     * @param groupSn 类别代码
     * @return 常用分组信息
     */
    @InnerAuth
    @GetMapping("/info/groupSn/{groupSn}")
    public SysGroup getInfoByGroupSn(@PathVariable("groupSn") String groupSn) {
        return sysGroupService.selectSysGroupByGroupSn(groupSn);
    }

    /**
     * 远程内部调用-根据分组ID查询分组信息
     * @param groupId 分组ID
     * @return 常用分组信息
     */
    @InnerAuth
    @GetMapping("/info/groupId/{groupId}")
    public SysGroup getInfoByGroupId(@PathVariable("groupId") Long groupId) {
        return sysGroupService.selectSysGroupById(groupId);
    }

    /**
     * 远程内部调用-根据条件查询list
     * @param sysGroup 查询条件
     * @return 常用分组集合
     */
    @InnerAuth
    @PostMapping("/list/search")
    public List<SysGroup> getListBySearch(@RequestBody SysGroup sysGroup) {
        return sysGroupService.selectSysGroupList(sysGroup);
    }
}
