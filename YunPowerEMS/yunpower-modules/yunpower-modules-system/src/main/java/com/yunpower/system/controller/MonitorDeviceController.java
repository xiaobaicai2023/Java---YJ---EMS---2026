package com.yunpower.system.controller;

import com.yunpower.system.domain.CommunicationChannel;
import com.yunpower.system.domain.CommunicationDevice;
import com.yunpower.system.domain.MonitorDevice;
import com.yunpower.system.domain.SysCompany;
import com.yunpower.system.service.*;
import com.yunpower.common.core.enums.StationTypeGroupEnum;
import com.yunpower.common.core.enums.vo.EnumSOVO;
import com.yunpower.common.core.utils.ChineseToPinyinUtils;
import com.yunpower.common.core.utils.GenRandomUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.bean.BeanUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.FeignMonitorDevice;
import com.yunpower.system.api.domain.SysCommonDictData;
import com.yunpower.system.api.domain.SysGroup;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.domain.*;
import com.yunpower.system.domain.jsonvo.AssociatedDevicesVo;
import com.yunpower.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 能源监控设备
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "J监控设备表")
@RestController
@RequestMapping("/device")
public class MonitorDeviceController extends BaseController {
    @Autowired
    private IMonitorDeviceService monitorDeviceService;

    @Autowired
    private ISysCommonDictService dictTypeService;

    @Autowired
    private ICommunicationChannelService communicationChannelService;

    @Autowired
    private ICommunicationDeviceService communicationDeviceService;

    @Autowired
    private ISysGroupService sysGroupService;

    @Autowired
    private ISysCompanyService sysCompanyService;

    @Autowired
    private ISysStationService stationService;

    @Autowired
    private IPublicService publicService;

    //region 查询能源监控设备列表（融合分组）

    /**
     * 查询能源监控设备列表（融合分组）
     */
    @ApiOperation("查询能源监控设备列表（融合分组）")
    @RequiresPermissions("system:device:list")
    @GetMapping("/listFusionGroup")
    public AjaxResult listFusionGroup(MonitorDevice monitorDevice) {
        if (StringUtils.intIsBlack0(monitorDevice.getStationType())) {
            return error("请传入stationType");
        }

        //字典表 dict_sn 站点类型：sys_station_type，分组类型：sys_group_map
        //SELECT * FROM sys_common_dict_data WHERE dict_sn='sys_station_type'
        //SELECT * FROM sys_common_dict_data WHERE dict_sn='sys_group_map';
        //站点类型：sys_station_type 枚举：1用电、2光伏、5用水、6流量、3储能、7计碳
        //分组类型：sys_group_map 枚举：2用电设备分组、3光伏设备分组、6用水设备分组、7流量设备分组、8储能设备分组、9计碳设备分组
        monitorDevice.setMapId(StationTypeGroupEnum.getEnumByKey(monitorDevice.getStationType()));

        List<MonitorDevice> list = monitorDeviceService.selectMonitorDeviceListFusionGroup(monitorDevice);
        packageDevice(list);
        return success(list);
    }
    //endregion

    //region 查询能源监控设备列表

    /**
     * 查询能源监控设备列表
     */
    @ApiOperation("查询能源监控设备列表")
    @RequiresPermissions("system:device:list")
    @GetMapping("/list")
    public TableDataInfo list(MonitorDevice monitorDevice) {
        startPage();
        List<MonitorDevice> list = monitorDeviceService.selectMonitorDeviceList(monitorDevice);
        packageDevice(list);
        return getDataTable(list);
    }
    //endregion

    //region 查询能源监控设备列表（不分页）

    /**
     * 查询能源监控设备列表（不分页）
     */
    @ApiOperation("查询能源监控设备列表（不分页）")
    @RequiresPermissions("system:device:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(MonitorDevice monitorDevice) {
        List<SysGroup> list = monitorDeviceService.selectMonitorDeviceListForGroup(monitorDevice);
        return success(list);
    }
    //endregion

    //region 获取【设备连接】信息

    /**
     * 获取【设备连接】信息
     */
    @ApiOperation("获取【设备连接】信息")
    @GetMapping("/getAssociatedDevices/{id}")
    public AjaxResult getAssociatedDevices(@PathVariable("id") Long id) {
        MonitorDevice monitorDevice = monitorDeviceService.selectMonitorDeviceById(id);
        List<MonitorDevice> list = new ArrayList<>();
        if (monitorDevice == null || monitorDevice.getAssociatedDevices().isEmpty()) {
            return success(list);
        }

        for (AssociatedDevicesVo item : monitorDevice.getAssociatedDevices()) {
            MonitorDevice device = monitorDeviceService.selectMonitorDeviceById(item.getId());
            if (StringUtils.isNull(device)) {
                continue;
            }
            list.add(device);
        }
        packageDevice(list);
        return success(list);
    }
    //endregion

    //region 公共：封装设备具体信息
    private void packageDevice(List<MonitorDevice> list) {
        if (list.isEmpty()) {
            return;
        }

        for (MonitorDevice item : list) {
            if (item.getIsGroup() == 1) {
                // 递归获取子列表的数据
                packageDevice(item.getChildren());
                continue;
            }

            CommunicationChannel channel = communicationChannelService.selectCommunicationChannelById(item.getChannelId());
            item.setChannelName(channel == null ? "" : channel.getChannelName());

            CommunicationDevice communicationDevice = communicationDeviceService.selectCommunicationDeviceById(item.getComDeviceId());
            item.setComDeviceName(communicationDevice == null ? "" : communicationDevice.getDeviceName());

            SysGroup group = sysGroupService.selectSysGroupById(item.getGroupId());
            item.setGroupName(group == null ? "" : group.getGroupName());

            SysCompany company = sysCompanyService.selectSysCompanyById(item.getProFactory());
            item.setCompanyName(company == null ? "" : company.getCompanyName());

            SysGroup typeGroup = sysGroupService.selectSysGroupById(item.getProType());
            item.setProTypeName(typeGroup == null ? "" : typeGroup.getGroupName());
        }

    }
    //endregion

    //region 导出能源监控设备列表

    /**
     * 【导出】能源监控设备
     * 注意：设备编码不可修改，不可新增，但可以删除
     */
    @ApiOperation("【导出】能源监控设备")
    @RequiresPermissions("system:device:export")
    @Log(title = "能源监控设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MonitorDevice monitorDevice) {
        List<MonitorDevice> list = monitorDeviceService.selectMonitorDeviceList(monitorDevice);
        ExcelUtil<MonitorDevice> util = new ExcelUtil<MonitorDevice>(MonitorDevice.class);
        util.exportExcel(response, list, "监控设备数据");
    }

    /**
     * 【导入】能源监控设备
     */
    @ApiOperation("【导入】能源监控设备")
    @Log(title = "能源监控设备", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:device:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<MonitorDevice> util = new ExcelUtil<MonitorDevice>(MonitorDevice.class);
        List<MonitorDevice> deviceList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = monitorDeviceService.importDevice(deviceList, updateSupport, operName);
        return success(message);
    }
    //endregion

    //region 获取能源监控设备详细信息

    /**
     * 获取能源监控设备详细信息
     */
    @ApiOperation("获取能源监控设备详细信息")
    @RequiresPermissions("system:device:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(monitorDeviceService.selectMonitorDeviceById(id));
    }
    //endregion

    //region 新增能源监控设备

    /**
     * 新增能源监控设备
     */
    @ApiOperation("新增能源监控设备")
    @RequiresPermissions("system:device:add")
    @Log(title = "能源监控设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MonitorDevice monitorDevice) {
        //分组必须填写，因为列表取是需要分组的，也方便管理
        if (StringUtils.longIsBlack0(monitorDevice.getGroupId())) {
            return error("请选择分组");
        }

        if (StringUtils.longIsBlack0(monitorDevice.getComDeviceId())) {
            return error("请选择通讯设备");
        }

        //站点类型（获取当前站点的站点类型）
        Long deptId = publicService.getCurrentStation();
        SysStation station = stationService.selectSysStationByDeptId(deptId);
        if (StringUtils.isNull(station)) {
            return error("当前站点异常");
        }

        //编辑页面会自动传入站点类型，无需再设置
        if (StringUtils.isNull(monitorDevice.getStationType())) {
            monitorDevice.setStationType(station.getStationType());
        }

        //设备数量是否超上限
        if (station.getDeviceLimit() > 0) {
            int count = monitorDeviceService.selectDeviceCountByDeptId(deptId);
            if (count >= station.getDeviceLimit()) {
                return error("当前站点设备数量已超上限");
            }
        }

        //电表可以设置表号
        if (StringUtils.isEmpty(monitorDevice.getDeviceSn())) {
            //获取最后一条记录
            int maxSuffix = 0;
            monitorDevice.setDeptId(publicService.getCurrentStation());
            MonitorDevice last = monitorDeviceService.selectLastMonitorDevice(monitorDevice);
            if (last != null) {
                maxSuffix = StringUtils.getMaxSuffix(last.getDeviceSn(), maxSuffix);
            }
            //规则：站点编码_中文转英文4位+2位随机
            monitorDevice.setDeviceSn(station.getStationSn() + "_" + StringUtils.generateDeviceSn(monitorDevice.getDeviceName(), 8, ++maxSuffix, false));
        }
        monitorDevice.setIsActive(0);

        //region //默认值
        if (StringUtils.isNull(monitorDevice.getBuyPrice())) {
            monitorDevice.setBuyPrice(0f);
        }
        if (StringUtils.isNull(monitorDevice.getRatedVol())) {
            monitorDevice.setRatedVol(0f);
        }
        if (StringUtils.isNull(monitorDevice.getRatedEle())) {
            monitorDevice.setRatedEle(0f);
        }
        if (StringUtils.isNull(monitorDevice.getRatedPower())) {
            monitorDevice.setRatedPower(0f);
        }
        if (StringUtils.isNull(monitorDevice.getEleLoadRate())) {
            monitorDevice.setEleLoadRate(0f);
        }
        if (StringUtils.isNull(monitorDevice.getMonitorId())) {
            monitorDevice.setMonitorId(0);
        }
        if (StringUtils.isNull(monitorDevice.getIsReading())) {
            monitorDevice.setIsReading(0);
        }
        if (StringUtils.isNull(monitorDevice.getRatedVolume())) {
            monitorDevice.setRatedVolume(0f);
        }
        if (StringUtils.isNull(monitorDevice.getAzimuth())) {
            monitorDevice.setAzimuth(0f);
        }
        if (StringUtils.isNull(monitorDevice.getDipAngle())) {
            monitorDevice.setDipAngle(0f);
        }
        if (StringUtils.isNull(monitorDevice.getUseLife())) {
            monitorDevice.setUseLife(0);
        }
        if (StringUtils.isNull(monitorDevice.getAttenuationRate())) {
            monitorDevice.setAttenuationRate(0f);
        }
        if (StringUtils.isNull(monitorDevice.getReadingVar())) {
            monitorDevice.setReadingVar(0);
        }
        if (StringUtils.isNull(monitorDevice.getDtuCmd())) {
            monitorDevice.setDtuCmd(0);
        }
        if (StringUtils.isNull(monitorDevice.getSbdz())) {
            monitorDevice.setSbdz(0);
        }
        if (StringUtils.isNull(monitorDevice.getProFactory())) {
            monitorDevice.setProFactory(0L);
        }
        //endregion

        return toAjax(monitorDeviceService.insertMonitorDevice(monitorDevice));
    }
    //endregion

    //region 修改能源监控设备

    /**
     * 修改能源监控设备
     */
    @ApiOperation("修改能源监控设备")
    @RequiresPermissions("system:device:edit")
    @Log(title = "能源监控设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MonitorDevice monitorDevice) {
        //分组必须填写，因为列表取是需要分组的，也方便管理
        if (StringUtils.longIsBlack0(monitorDevice.getGroupId())) {
            return error("请选择分组");
        }

        if (StringUtils.longIsBlack0(monitorDevice.getComDeviceId())) {
            return error("请选择通讯设备");
        }

        return toAjax(monitorDeviceService.updateMonitorDevice(monitorDevice));
    }
    //endregion

    //region 修改能源监控设备状态

    /**
     * 修改能源监控设备状态
     */
    @ApiOperation("修改能源监控设备状态")
    @RequiresPermissions("system:device:state")
    @Log(title = "能源监控设备", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(monitorDeviceService.updateMonitorDeviceState(new MonitorDevice(), id, state));
    }
    //endregion

    //region 删除能源监控设备

    /**
     * 删除能源监控设备
     */
    @ApiOperation("删除能源监控设备")
    @RequiresPermissions("system:device:remove")
    @Log(title = "能源监控设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        if (monitorDeviceService.hasChildrenById(id)) {
            return warn("该设备下存在变量，不允许删除");
        }
        return toAjax(monitorDeviceService.deleteMonitorDeviceById(id));
    }
    //endregion

    //region 光伏设备列表页动态Tab
    @ApiOperation("光伏设备列表页动态Tab")
    @GetMapping("/getPVTab")
    public AjaxResult getPVTab() {
        //取出所有报警级别
        List<SysCommonDictData> pvGroupList = dictTypeService.selectDictDataByType("sys_pv_groups");

        List<EnumSOVO> tabList = new ArrayList<>();

        for (SysCommonDictData item : pvGroupList) {
            EnumSOVO vo = new EnumSOVO();
            vo.setKey(item.getDictLabel());
            vo.setValue(item.getDictValue());
            tabList.add(vo);
        }

        return success(tabList);
    }
    //endregion


    /**
     * 远程内部调用-根据设备号查询设备信息
     *
     * @param deviceSn 设备号
     * @return 设备信息
     */
    @InnerAuth
    @GetMapping("/info/deviceSn/{deviceSn}")
    public FeignMonitorDevice getInfoByGroupId(@PathVariable("deviceSn") String deviceSn) {
        FeignMonitorDevice result = new FeignMonitorDevice();
        MonitorDevice monitorDevice = monitorDeviceService.selectMonitorDeviceBySn(deviceSn);
        if (StringUtils.isNull(monitorDevice)) {
            return null;
        }
        BeanUtils.copyProperties(monitorDevice, result);
        return result;
    }

    /**
     * 远程内部调用-查询所有设备的状态
     *
     * @return 设备信息
     */
    @InnerAuth
    @GetMapping("/status/all/inner/{deptId}")
    public Boolean statusAllInnerAuth(@PathVariable("deptId") Long deptId) {
        return monitorDeviceService.statusAllByDeptId(deptId);
    }

}
