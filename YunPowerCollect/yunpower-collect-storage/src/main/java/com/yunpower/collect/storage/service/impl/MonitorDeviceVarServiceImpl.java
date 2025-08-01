package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.CommunicationDevice;
import com.yunpower.collect.storage.domain.MonitorDeviceVar;
import com.yunpower.collect.storage.mapper.MonitorDeviceVarMapper;
import com.yunpower.collect.storage.service.IMonitorDeviceVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 监控设备变量Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class MonitorDeviceVarServiceImpl implements IMonitorDeviceVarService {
    @Autowired
    private MonitorDeviceVarMapper monitorDeviceVarMapper;

    /**
     * 查询监控设备变量
     *
     * @param id 监控设备变量主键
     * @return 监控设备变量
     */
    @Override
    public MonitorDeviceVar selectMonitorDeviceVarById(Long id) {
        return monitorDeviceVarMapper.selectMonitorDeviceVarById(id);
    }

    /**
     * 查询监控设备变量列表
     *
     * @param monitorDeviceVar 监控设备变量
     * @return 监控设备变量
     */
    @Override
    public List<MonitorDeviceVar> selectMonitorDeviceVarList(MonitorDeviceVar monitorDeviceVar) {
        return monitorDeviceVarMapper.selectMonitorDeviceVarList(monitorDeviceVar);
    }

    /**
     * 通过通讯设备查询变量信息
     * */
    @Override
    public List<MonitorDeviceVar> selectMonitorDeviceVarListByChannelDevice(Long comDeviceId){
        return monitorDeviceVarMapper.selectMonitorDeviceVarListByChannelDevice(comDeviceId);
    }

    /**
     * 获取通讯设备下，所有变量及寄存器地址
     *
     * @param deviceList 通讯设备列表
     * @return 结果
     */
    @Override
    public Map<Long, Map<Integer, MonitorDeviceVar>> selectStorageVarList(List<CommunicationDevice> deviceList) {
//        Map<Long, Map<Integer, MonitorDeviceVar>> result = new HashMap<>();
//        Map<Integer, MonitorDeviceVar> temp;
//
//        for (CommunicationDevice item : deviceList) {
//            List<MonitorDeviceVar> deviceVarList = monitorDeviceVarMapper.selectMonitorDeviceVarListByChannelDevice(item.getId());
//
//            temp = new HashMap<>();
//            for (MonitorDeviceVar var : deviceVarList) {
//                if (var.getMessageAddress() == null || var.getMessageAddress() < 0) {
//                    continue;
//                }
//                temp.put(var.getMessageAddress(), var);
//            }
//            result.put(item.getId(), temp);
//        }
//
//        return result;
        return null;
    }
}
