package com.yunpower.system.controller;

import com.yunpower.common.core.utils.GenRandomUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.domain.CommunicationChannel;
import com.yunpower.system.domain.CommunicationDevice;
import com.yunpower.system.domain.jsonvo.DevicesDataAreaVo;
import com.yunpower.system.service.ICommunicationChannelService;
import com.yunpower.system.service.ICommunicationDeviceService;
import com.yunpower.system.service.ISysDeptService;
import com.yunpower.common.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 通讯设备
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "T 通讯设备表")
@RestController
@RequestMapping("/channel-device")
public class CommunicationDeviceController extends BaseController {
    @Autowired
    private ICommunicationDeviceService communicationDeviceService;

    @Autowired
    private ICommunicationChannelService communicationChannelService;

    @Autowired
    private ISysDeptService deptService;

    /**
     * 查询通讯设备列表（不分页）
     */
    @ApiOperation("查询通讯设备列表（不分页）")
    @RequiresPermissions("system:channel-device:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(CommunicationDevice communicationDevice) {
        List<CommunicationDevice> list = communicationDeviceService.selectCommunicationDeviceList(communicationDevice);
        return success(list);
    }

    /**
     * 获取通讯设备详细信息
     */
    @ApiOperation("获取通讯设备详细信息")
    @RequiresPermissions("system:channel-device:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        CommunicationDevice device = communicationDeviceService.selectCommunicationDeviceById(id);
        if(device == null){
            return AjaxResult.error("未查询到通道设备");
        }
        packageDevice(device);
        return success(device);
    }

    /**
     * 通过通讯设备ID获取数据区域列表
     */
    @ApiOperation("通过通讯设备ID获取数据区域列表")
    @RequiresPermissions("system:channel-device:list")
    @GetMapping("/getDataArealist")
    public AjaxResult getDataArealist(Long id) {
        List<String> areaList = new ArrayList<>();
        CommunicationDevice communicationDevice = communicationDeviceService.selectCommunicationDeviceById(id);
        if (communicationDevice != null) {
            for (DevicesDataAreaVo item : communicationDevice.getDataArea()) {
                if (!areaList.contains(item.getArea())) {
                    areaList.add(item.getArea());
                }
            }
        }
        return success(areaList);
    }

    /**
     * 查询指定站点下所有通讯设备的状态
     * @return 设备信息
     */
    @GetMapping("/status/all/{deptId}")
    public AjaxResult statusAll(@PathVariable("deptId") Long deptId) {
        return success(communicationDeviceService.statusAllByDeptId(deptId));
    }

    /**
     * 远程内部调用-查询所有通讯设备的状态
     * @return 设备信息
     */
    @InnerAuth
    @GetMapping("/status/all/inner/{deptId}")
    public Boolean statusAllInnerAuth(@PathVariable("deptId") Long deptId) {
        return communicationDeviceService.statusAllByDeptId(deptId);
    }


    /**
     * 额外组装数据
     * */
    private void packageDevice(CommunicationDevice device){
        SysDept dept = deptService.selectDeptById(device.getDeptId());
        if (dept != null) {
            device.setStationName(dept.getDeptName());
        }
        CommunicationChannel channel = communicationChannelService.selectCommunicationChannelById(device.getChannelId());
        if (channel != null) {
            device.setChannelName(channel.getChannelName());
        }
    }
}
