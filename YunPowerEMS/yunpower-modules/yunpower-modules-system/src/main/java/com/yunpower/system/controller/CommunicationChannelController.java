package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.utils.GenRandomUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysDeptService;
import com.yunpower.system.service.ISysStationService;
import com.yunpower.common.log.annotation.Log;
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
import com.yunpower.system.domain.CommunicationChannel;
import com.yunpower.system.service.ICommunicationChannelService;

/**
 * 通讯通道
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "T 通讯通道表")
@RestController
@RequestMapping("/channel")
public class CommunicationChannelController extends BaseController {
    @Autowired
    private ICommunicationChannelService communicationChannelService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysStationService stationService;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询通讯通道列表
     */
    @ApiOperation("查询通讯通道列表")
    @RequiresPermissions("system:channel:list")
    @GetMapping("/list")
    public TableDataInfo list(CommunicationChannel communicationChannel) {
        startPage();

        if(StringUtils.isNotBlank(communicationChannel.getRegistrationCode())){
            communicationChannel.getParams().put("registrationCode",communicationChannel.getRegistrationCode());
            communicationChannel.setRegistrationCode(null);
        }
        List<CommunicationChannel> list = communicationChannelService.selectCommunicationChannelList(communicationChannel);
        for (CommunicationChannel item : list) {
            SysDept dept = deptService.selectDeptById(item.getDeptId());
            if (dept != null) {
                item.setStationName(dept.getDeptName());
            }
        }
        return getDataTable(list);
    }

    /**
     * 查询通讯通道列表（不分页）
     */
    @ApiOperation("查询通讯通道列表（不分页）")
    @RequiresPermissions("system:channel:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(CommunicationChannel communicationChannel) {
        List<CommunicationChannel> list = communicationChannelService.selectCommunicationChannelList(communicationChannel);
        return success(list);
    }

    /**
     * 导出通讯通道列表
     */
    @ApiOperation("导出通讯通道列表")
    @RequiresPermissions("system:channel:export")
    @Log(title = "通讯通道", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CommunicationChannel communicationChannel) {
        List<CommunicationChannel> list = communicationChannelService.selectCommunicationChannelList(communicationChannel);
        ExcelUtil<CommunicationChannel> util = new ExcelUtil<CommunicationChannel>(CommunicationChannel.class);
        util.exportExcel(response, list, "通讯通道数据");
    }

    /**
     * 获取通讯通道详细信息
     */
    @ApiOperation("获取通讯通道详细信息")
    @RequiresPermissions("system:channel:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {

        CommunicationChannel channel = communicationChannelService.selectCommunicationChannelById(id);
        if(channel == null){
            return AjaxResult.error("未查询到通讯通道");
        }
        SysDept dept = deptService.selectDeptById(channel.getDeptId());
        if (dept != null) {
            channel.setStationName(dept.getDeptName());
        }
        return success(channel);
    }

    /**
     * 新增通讯通道
     */
    @ApiOperation("新增通讯通道")
    @RequiresPermissions("system:channel:add")
    @Log(title = "通讯通道", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CommunicationChannel communicationChannel) {
        //tcp的要通讯注册码
        if (communicationChannel.getAccessType() == 1 && !communicationChannel.getRegistrationCode().startsWith("66")) {
            return error("注册码必须以66开头");
        }

        //注册码不能重复
        CommunicationChannel exit = communicationChannelService.selectCommunicationChannelByRegisterCode(communicationChannel.getRegistrationCode());
        if (StringUtils.isNotNull(exit)) {
            return error("注册码已存在");
        }

        //站点类型（获取当前站点的站点类型）
        Long deptId = publicService.getCurrentStation();
        SysStation station = stationService.selectSysStationByDeptId(deptId);
        if (StringUtils.isNull(station)) {
            return error("当前站点异常");
        }
        communicationChannel.setStationType(station.getStationType());
        communicationChannel.setChannelSn(GenRandomUtils.GenRandom(8).toUpperCase());
        communicationChannel.setCodeStart(0);
        communicationChannel.setCodeLength(0);
        communicationChannel.setIsActive(0);
        return toAjax(communicationChannelService.insertCommunicationChannel(communicationChannel));
    }

    /**
     * 修改通讯通道
     */
    @ApiOperation("修改通讯通道")
    @RequiresPermissions("system:channel:edit")
    @Log(title = "通讯通道", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CommunicationChannel communicationChannel) {
        //tcp的要通讯注册码
        if (communicationChannel.getAccessType() == 1 && !communicationChannel.getRegistrationCode().startsWith("66")) {
            return error("注册码必须以66开头");
        }

        //注册码不能重复
        CommunicationChannel exit = communicationChannelService.selectCommunicationChannelByRegisterCode(communicationChannel.getRegistrationCode());
        if (StringUtils.isNotNull(exit) && !exit.getId().equals(communicationChannel.getId())) {
            return error("注册码重复");
        }

        return toAjax(communicationChannelService.updateCommunicationChannel(communicationChannel));
    }

    /**
     * 修改通讯通道状态
     */
    @ApiOperation("修改通讯通道状态")
    @RequiresPermissions("system:channel:state")
    @Log(title = "通讯通道", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(communicationChannelService.updateCommunicationChannelState(id, state));
    }

    /**
     * 删除通讯通道
     */
    @ApiOperation("删除通讯通道")
    @RequiresPermissions("system:channel:remove")
    @Log(title = "通讯通道", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        if (communicationChannelService.hasChildrenById(id)) {
            return warn("该通道下存在通讯设备，不允许删除");
        }
        return toAjax(communicationChannelService.deleteCommunicationChannelById(id));
    }
}
