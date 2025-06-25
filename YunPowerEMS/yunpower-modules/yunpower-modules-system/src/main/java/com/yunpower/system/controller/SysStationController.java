package com.yunpower.system.controller;

import com.yunpower.common.core.constant.UserConstants;
import com.yunpower.common.core.utils.ChineseToPinyinUtils;
import com.yunpower.common.core.utils.GenRandomUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.service.IAutoGenSyncService;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysDeptService;
import com.yunpower.system.service.ISysStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 电站
 *
 * @author JUNFU.WANG
 * @date 2023-09-28
 */
@Api(tags = "Z 站点管理表")
@RestController
@RequestMapping("/station")
public class SysStationController extends BaseController {
    @Autowired
    private ISysStationService sysStationService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IPublicService publicService;

    @Autowired
    private IAutoGenSyncService autoGenSyncService;

    /**
     * 查询电站列表（不分页，树结构）
     */
    @ApiOperation("查询电站列表（不分页，树结构）")
    @RequiresPermissions("system:station:list")
    @GetMapping("/list")
    public AjaxResult list(SysStation sysStation) {
        List<SysStation> list = sysStationService.selectSysStationList(sysStation);
        list = sysStationService.buildStationTree(list);
        return success(list);
    }

    /**
     * 查询电站列表（不分页）
     */
    @ApiOperation("查询电站列表（不分页）")
    @RequiresPermissions("system:station:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(SysStation sysStation) {
        List<SysStation> list = sysStationService.selectSysStationList(sysStation);
        return success(list);
    }

    /**
     * 导出电站列表
     */
    @ApiOperation("导出电站列表")
    @RequiresPermissions("system:station:export")
    @Log(title = "电站", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysStation sysStation) {
        List<SysStation> list = sysStationService.selectSysStationList(sysStation);
        ExcelUtil<SysStation> util = new ExcelUtil<SysStation>(SysStation.class);
        util.exportExcel(response, list, "电站数据");
    }

    /**
     * 获取电站详细信息
     */
    @ApiOperation("获取电站详细信息")
    @RequiresPermissions("system:station:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysStationService.selectSysStationById(id));
    }

    /**
     * 新增电站
     */
    @ApiOperation("新增电站")
    @RequiresPermissions("system:station:add")
    @Log(title = "电站", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysStation sysStation) throws Exception {
        if (sysStation.getGroupOrStation() == 2 && StringUtils.intIsBlack0(sysStation.getStationType())) {
            return error("请选择站点类型");
        }
        if (sysStation.getGroupOrStation() != 2) {
            sysStation.setStationType(0);
        }

        // 设置默认值
        String sn = ChineseToPinyinUtils.ToFirstChar(sysStation.getStationName());

        // 查询是否已经存在当前分组、电站
        SysStation exist = sysStationService.selectSysStationBySn(sn);
        if (exist != null) {
            sn += GenRandomUtils.GenRandomNumber(4);
        }
        if (sn.length() > 64) {
            sn = sn.substring(0, 60);
        }
        sysStation.setStationSn(sn);

        // 理论上企业ID是不能指定的，只能在当前企业下添加站点
        if (sysStation.getEntId() == null || sysStation.getEntId() <= 0) {
            sysStation.setEntId(publicService.getCurrentEnterprise());
        }

        sysStation.setDeptId(0L);
        sysStation.setGroupId(0L);
        if (sysStation.getOrderNum() == null) {
            sysStation.setOrderNum(0);
        }
        if (sysStation.getPvType() == null) {
            sysStation.setPvType(0);
        }
        if (sysStation.getSchemeId() == null) {
            sysStation.setSchemeId(0L);
        }
        int rows = sysStationService.insertSysStation(sysStation);
        if (rows > 0) {
            // 异步、事务
            autoGenSyncService.autoGenSyncForStation(sn);
        }

        return toAjax(rows);
    }

    /**
     * 修改电站
     */
    @ApiOperation("修改电站")
    @RequiresPermissions("system:station:edit")
    @Log(title = "电站", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysStation sysStation) throws Exception {
        if (sysStation.getGroupOrStation() != null && sysStation.getGroupOrStation() == 2 && StringUtils.intIsBlack0(sysStation.getStationType())) {
            return error("请选择站点类型");
        }
        if (sysStation.getId().equals(sysStation.getParentId())) {
            return error("修改站点'" + sysStation.getStationName() + "'失败，上级站点不能是自己");
        }
        if (UserConstants.DEPT_DISABLE.equals(sysStation.getStopFlag()) && sysStationService.selectNormalChildrenStationById(sysStation.getId()) > 0) {
            return error("该站点包含未停用的子站点！");
        }

        int rows = sysStationService.updateSysStation(sysStation);
        if (rows > 0) {
            // 异步、事务
            autoGenSyncService.autoGenSyncForDept();
        }
        return toAjax(rows);
    }

    /**
     * 修改电站状态
     */
    @ApiOperation("修改电站状态")
    @RequiresPermissions("system:station:state")
    @Log(title = "电站", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) throws Exception {
        int rows = sysStationService.updateSysStationState(new SysStation(), id, state);
        if (rows > 0) {
            // 异步、事务
            autoGenSyncService.autoGenSyncForDept();
        }
        return toAjax(rows);
    }

    /**
     * 删除电站
     */
    @ApiOperation("删除电站")
    @RequiresPermissions("system:station:remove")
    @Log(title = "电站", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) throws Exception {
        if (sysStationService.hasChildrenById(id)) {
            return warn("存在子站点，不允许删除");
        }
        if (sysStationService.checkStationExistUser(id)) {
            return warn("站点存在用户，不允许删除");
        }
        SysStation sysStation = sysStationService.selectSysStationById(id);
        if (sysStation == null) {
            return toAjax(1);
        }
        int rows = sysStationService.deleteSysStationById(new SysStation(), id);
        if (rows > 0) {
            // 异步、事务
            autoGenSyncService.autoGenSyncForDept();
        }
        return toAjax(rows);
    }

    /**
     * 获取电站报警开关
     */
    @ApiOperation("获取电站报警开关")
    @GetMapping(value = "/getAlarmStatus")
    public AjaxResult getAlarmStatus() {
        Long deptId = publicService.getCurrentStation();
        SysStation station = sysStationService.selectSysStationByDeptId(deptId);
        return success(station == null ? 0 : station.getOpenAlarm());
    }

    /**
     * 修改电站报警状态
     */
    @ApiOperation("修改电站报警状态")
    @RequiresPermissions("system:station:state")
    @Log(title = "电站", businessType = BusinessType.UPDATE)
    @PutMapping("/changeAlarmStatus/{state}")
    public AjaxResult changeAlarmStatus(@PathVariable Integer state) {
        Long id = publicService.getCurrentStation();
        return toAjax(sysStationService.updateSysStationAlarmState(new SysStation(), id, state));
    }
}
