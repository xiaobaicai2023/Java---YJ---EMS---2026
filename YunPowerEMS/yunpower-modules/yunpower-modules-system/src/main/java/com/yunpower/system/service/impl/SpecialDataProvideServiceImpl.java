package com.yunpower.system.service.impl;

import com.yunpower.common.core.enums.DateFormatEnum;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.domain.*;
import com.yunpower.system.domain.vo.AssignDataVo;
import com.yunpower.system.domain.vo.table.TableHeader;
import com.yunpower.system.domain.vo.table.TableResult;
import com.yunpower.system.domain.vo.time.TimesVo;
import com.yunpower.system.service.*;
import com.yunpower.system.utils.GenerateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 大屏专用数据（集合）
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/23 10:17
 */
@Service
public class SpecialDataProvideServiceImpl implements ISpecialDataProvideService {

    @Autowired
    private IDevopsOrderService devopsOrderService;

    @Autowired
    private IMonitorDeviceService monitorDevicerService;

    @Autowired
    private IMonitorDeviceVarService monitorDeviceVarService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysCommonDictDataService dictDataService;

    @Autowired
    private ICommunicationChannelService communicationChannelService;

    @Autowired
    private IPublicService publicService;

    //region 获取工单列表

    /**
     * 获取工单列表
     *
     * @param headType   表头类型：1长表头 2短表头
     * @param staticType 数据类型：记录状态
     * @return 结果
     */
    @Override
    public Object getDevopsOrderList(Integer headType, Integer staticType, TimesVo timesVo, Long deptId) {
        DevopsOrder devopsOrder = new DevopsOrder();
        devopsOrder.setDeptId(deptId);
        devopsOrder.setParams(DateUtils.dateToParamForDayFormat(timesVo.getBeginTime(), timesVo.getEndTime()));
        if (staticType != null) {
            if (staticType == 0) {
                //工单状态（-1已取消 0新工单 1已接单 2进行中 3已完成 10已验证 20延迟处理）
                devopsOrder.getParams().put("status", Arrays.asList(0, 1, 2, 20));
            } else {
                devopsOrder.setStatus(staticType);
            }
        }
        devopsOrder.getParams().put("limit", 10);
        List<DevopsOrder> devopsOrderList = devopsOrderService.selectDevopsOrderList(devopsOrder);

        //region *** 组装表头 **
        List<TableHeader> headerList = new ArrayList<>();
        TableHeader dateHeader = new TableHeader("编号", "id", null);
        headerList.add(dateHeader);
        String titles = "工单名称,站点,类别,负责人,时间,状态,操作";
        if (headType == 2) {
            titles = "工单名称,时间,状态,操作";
        }
        packageHeader(headerList, titles);
        //endregion

        //region *** 组装表体 **
        List<Map<String, Object>> tableData = new ArrayList<>();

        for (DevopsOrder item : devopsOrderList) {
            packageOrderInfo(item);

            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("工单名称", item.getOrderTitle());
            map.put("时间", DateUtils.parseDateToStr(DateFormatEnum.YYYY_MM_DD_HH_MM.getValue(), item.getCreateTime()));
            map.put("状态", item.getStatusName());
            map.put("操作", "处理");
            if (headType == 1) {
                map.put("站点", item.getStationName());
                map.put("类别", item.getOrderTypeName());
                map.put("负责人", item.getChargeBy());
            }

            tableData.add(map);
        }

        //endregion

        return new TableResult(headerList, tableData);
    }

    private void packageOrderInfo(DevopsOrder item) {
        //站点（部门）&设备
        SysDept dept = deptService.selectDeptById(item.getStationId());
        if (dept != null) {
            item.setStationName(dept.getDeptName());
        }

        //工单类型
        String typeName = dictDataService.selectDictLabel("sys_order_type", item.getOrderType().toString());
        item.setOrderTypeName(typeName);

        //工单状态
        String statusName = dictDataService.selectDictLabel("sys_order_status", item.getStatus().toString());
        item.setStatusName(statusName);
    }
    //endregion

    //region *** 组装表头 ***
    private void packageHeader(List<TableHeader> headerList, String titles) {
        String[] args = titles.split(",");
        for (String arr : args) {
            TableHeader dateHeader = new TableHeader(arr, arr, null);
            headerList.add(dateHeader);
        }
    }
    //endregion

    //region 获取工单统计数据

    /**
     * 获取工单统计数据
     *
     * @param staticType 统计类型（工单：1按类型 2按日期 3按状态）
     * @return 结果
     */
    @Override
    public Object getDevopsOrderStatic(Integer staticType, TimesVo timesVo, Long deptId) {
        GenerateTimeUtils.packageTime(timesVo);

        //按类型
        if (staticType == 1) {
            DevopsOrder devopsOrder = new DevopsOrder();
            devopsOrder.setDeptId(deptId);
            devopsOrder.setParams(DateUtils.dateToParamForDayFormat(timesVo.getBeginTime(), timesVo.getEndTime()));
            List<DevopsOrder> list = devopsOrderService.selectStatisticByOrderType(devopsOrder);

            Map<String, Object> map = new HashMap<>();
            for (DevopsOrder item : list) {
                String typeName = dictDataService.selectDictLabel("sys_order_type", item.getOrderType().toString());
                map.put(typeName, item.getStatisticValue());
            }
            return map;
        }

        //按日期
        if (staticType == 2) {
            DevopsOrder devopsOrder = new DevopsOrder();
            devopsOrder.setDateDim(timesVo.getDateDim());
            devopsOrder.setDeptId(deptId);
            devopsOrder.setParams(DateUtils.dateToParamForDayFormat(timesVo.getBeginTime(), timesVo.getEndTime()));
            List<DevopsOrder> list = devopsOrderService.selectStatisticByDate(devopsOrder);

            Map<String, Object> map = new HashMap<>();
            for (DevopsOrder item : list) {
                map.put(item.getFormattedDatetime(), item.getStatisticValue());
            }
            return map;
        }

        //按状态
        if (staticType == 3) {
            DevopsOrder devopsOrder = new DevopsOrder();
            devopsOrder.setDeptId(deptId);
            devopsOrder.setParams(DateUtils.dateToParamForDayFormat(timesVo.getBeginTime(), timesVo.getEndTime()));
            List<DevopsOrder> list = devopsOrderService.selectStatisticByStatus(devopsOrder);

            Map<String, Object> map = new HashMap<>();
            for (DevopsOrder item : list) {
                String statusName = dictDataService.selectDictLabel("sys_order_status", item.getStatus().toString());
                map.put(statusName, item.getStatisticValue());
            }
            return map;
        }

        return null;
    }

    //endregion

    //region 获取指定接口数据

    /**
     * 获取指定接口数据
     *
     * @param deptId     站点（部门）ID
     * @param assignType 指定类型的key
     * @return 结果
     */
    @Override
    public AssignDataVo getAssignedData(Long deptId, String assignType) {
        deptId = (deptId == null ? publicService.getCurrentStation() : deptId);

        switch (assignType) {
            case "network":                 //网络通断
                return getNetworkStatus(deptId, assignType);
            case "stationNum":              //站点数量
                return getStationAmount(deptId, assignType);
            case "deviceNum":                //设备数量
                return getDeviceAmount(deptId, assignType);
            case "pointNum":                //点位数量
                return getPointAmount(deptId, assignType);
            case "yearEmissionReduction":   //年减排量
                return new AssignDataVo(0);
        }
        return null;
    }

    /**
     * 网络通断
     */
    private AssignDataVo getNetworkStatus(Long deptId, String assignType) {
        CommunicationChannel communicationChannel = new CommunicationChannel();
        communicationChannel.setDeptId(deptId);
        communicationChannel.setStopFlag(0);
        List<CommunicationChannel> list = communicationChannelService.selectCommunicationChannelList(communicationChannel);

        AssignDataVo dataVo = new AssignDataVo(assignType, true, "网络通断", "");
        for (CommunicationChannel item : list) {
            if (item.getIsActive() == 0) {
                dataVo.setValue(false);
                break;
            }
        }
        return dataVo;
    }

    /**
     * 站点数量
     */
    private AssignDataVo getStationAmount(Long deptId, String assignType) {
        return new AssignDataVo(assignType, 1, "站点数量", "");
    }

    /**
     * 设备数量
     */
    private AssignDataVo getDeviceAmount(Long deptId, String assignType) {
        Integer count = monitorDevicerService.selectDeviceCountByDeptId(deptId);
        return new AssignDataVo(assignType, count, "设备数量", "");
    }

    /**
     * 点位数量
     */
    private AssignDataVo getPointAmount(Long deptId, String assignType) {
        Integer count = monitorDeviceVarService.selectDeviceVarCountByDeptId(deptId);
        return new AssignDataVo(assignType, count, "点位数量", "");
    }
    //endregion
}
