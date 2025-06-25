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
     * 查询通讯设备列表
     */
    @ApiOperation("查询通讯设备列表")
    @RequiresPermissions("system:channel-device:list")
    @GetMapping("/list")
    public TableDataInfo list(CommunicationDevice communicationDevice) {
        startPage();
        List<CommunicationDevice> list = communicationDeviceService.selectCommunicationDeviceList(communicationDevice);
        if (list != null && !list.isEmpty()) {
            list.forEach(this::packageDevice);
        }
        return getDataTable(list);
    }

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
     * 导出通讯设备列表
     */
    @ApiOperation("导出通讯设备列表")
    @RequiresPermissions("system:channel-device:export")
    @Log(title = "通讯设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CommunicationDevice communicationDevice) {
        List<CommunicationDevice> list = communicationDeviceService.selectCommunicationDeviceList(communicationDevice);
        ExcelUtil<CommunicationDevice> util = new ExcelUtil<CommunicationDevice>(CommunicationDevice.class);
        util.exportExcel(response, list, "通讯设备数据");
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
     * 新增通讯设备
     */
    @ApiOperation("新增通讯设备")
    @RequiresPermissions("system:channel-device:add")
    @Log(title = "通讯设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CommunicationDevice communicationDevice) {
        if (StringUtils.longIsBlack0(communicationDevice.getChannelId())) {
            return error("请选择通道");
        }

        // 获取通道信息
        CommunicationChannel channel = communicationChannelService.selectCommunicationChannelById(communicationDevice.getChannelId());
        if (StringUtils.isNull(channel) || channel.getStopFlag() == 1) {
            return error("通道无效，请先配置通道");
        }
        communicationDevice.setStationType(channel.getStationType());
        communicationDevice.setChannelSn(channel.getChannelSn());

        // 设置默认值
        communicationDevice.setDeviceSn(GenRandomUtils.GenRandom(10).toUpperCase());
        communicationDevice.setIsActive(0);

        if (communicationDevice.getProType() == null) {
            communicationDevice.setProType(0);
        }
        if (communicationDevice.getProFactory() == null) {
            communicationDevice.setProFactory(0);
        }
        if (communicationDevice.getCardOperator() == null) {
            communicationDevice.setCardOperator(0);
        }
        if (communicationDevice.getCardFlow() == null) {
            communicationDevice.setCardFlow(0);
        }
        if (communicationDevice.getCardPostage() == null) {
            communicationDevice.setCardPostage(0f);
        }
        return toAjax(communicationDeviceService.insertCommunicationDevice(communicationDevice));
    }

    /**
     * 修改通讯设备
     */
    @ApiOperation("修改通讯设备")
    @RequiresPermissions("system:channel-device:edit")
    @Log(title = "通讯设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CommunicationDevice communicationDevice) {
        if (StringUtils.longIsBlack0(communicationDevice.getChannelId())) {
            return error("请选择通道");
        }

        // 获取通道信息
        CommunicationChannel channel = communicationChannelService.selectCommunicationChannelById(communicationDevice.getChannelId());
        if (StringUtils.isNull(channel) || channel.getStopFlag() == 1) {
            return error("通道无效，请先配置通道");
        }
        communicationDevice.setStationType(channel.getStationType());
        return toAjax(communicationDeviceService.updateCommunicationDevice(communicationDevice));
    }

    /**
     * 修改通讯设备状态
     */
    @ApiOperation("修改通讯设备状态")
    @RequiresPermissions("system:channel-device:state")
    @Log(title = "通讯设备", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(communicationDeviceService.updateCommunicationDeviceState(new CommunicationDevice(), id, state));
    }

    /**
     * 删除通讯设备
     */
    @ApiOperation("删除通讯设备")
    @RequiresPermissions("system:channel-device:remove")
    @Log(title = "通讯设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        if (communicationDeviceService.hasChildrenById(id)) {
            return warn("该通讯设备下存在用电设备，不允许删除");
        }
        return toAjax(communicationDeviceService.deleteCommunicationDeviceById(new CommunicationDevice(), id));
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
