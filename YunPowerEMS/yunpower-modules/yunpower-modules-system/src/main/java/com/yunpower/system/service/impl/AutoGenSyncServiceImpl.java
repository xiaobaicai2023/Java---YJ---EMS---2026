package com.yunpower.system.service.impl;

import com.yunpower.system.service.*;
import com.yunpower.system.api.domain.SysStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @title: 自动执行，异步完成的业务
 * @Author: Jiajiaglam
 * @date: 2024-02-23 14:06
 * @description:
 */
@Service
public class AutoGenSyncServiceImpl implements IAutoGenSyncService {
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysGroupService groupService;

    @Autowired
    private ISysStationService stationService;

    @Autowired
    private IElectricPriceSchemeConfigService electricPriceSchemeConfigService;

    /**
     * 创建【站点】时，完成相关的业务（异步执行）
     * 将来此处要放到MQ中，防止多人同时操作时发生冲突
     */
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void autoGenSyncForStation(String stationSn) throws Exception {

        // 1.生成部门表
        boolean result = deptService.autoGenDeptSync();
        if (!result) {
            throw new RuntimeException("生成部门表报错");
        }

        // 2.如果是光伏站点，则生成光伏设备目录
        SysStation currStation = stationService.selectSysStationBySn(stationSn);
        if (currStation.getStationType() == 2) { //光伏
            boolean result2 = groupService.autoGenPvGroupSync(currStation.getParentId(), currStation.getDeptId());
            if (!result2) {
                throw new RuntimeException("生成光伏目录报错");
            }
        }

        // 3.生成电价标准
        if (currStation.getGroupOrStation() == 2) { //电站
            electricPriceSchemeConfigService.autoGenElectricSchemeSync(currStation.getEntId(), currStation.getDeptId());
        }
    }

    /**
     * 创建、修改【企业】和修改【电站】时，完成相关的业务（异步执行）
     */
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void autoGenSyncForDept() throws Exception {
        // 生成部门表
        boolean result = deptService.autoGenDeptSync();
        if (!result) {
            throw new RuntimeException("生成部门表报错");
        }
    }
}
