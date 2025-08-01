package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.MonitorDeviceVarMapMapper;
import com.yunpower.system.domain.MonitorDeviceVarMap;
import com.yunpower.system.service.IMonitorDeviceVarMapService;

/**
 * 监控设备变量索引地图Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class MonitorDeviceVarMapServiceImpl implements IMonitorDeviceVarMapService {
    @Autowired
    private MonitorDeviceVarMapMapper monitorDeviceVarMapMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询监控设备变量索引地图
     *
     * @param id 监控设备变量索引地图主键
     * @return 监控设备变量索引地图
     */
    @Override
    public MonitorDeviceVarMap selectMonitorDeviceVarMapById(Long id) {
        return monitorDeviceVarMapMapper.selectMonitorDeviceVarMapById(id);
    }

    /**
     * 通过索引编码获取
     *
     * @param indexSn 索引编码
     * @return 结果
     */
    @Override
    public MonitorDeviceVarMap selectMonitorDeviceVarMapByIndexSn(String indexSn) {
        return monitorDeviceVarMapMapper.selectMonitorDeviceVarMapByIndexSn(indexSn);
    }

    /**
     * 查询监控设备变量索引地图列表
     *
     * @param monitorDeviceVarMap 监控设备变量索引地图
     * @return 监控设备变量索引地图
     */
    @Override
    public List<MonitorDeviceVarMap> selectMonitorDeviceVarMapList(MonitorDeviceVarMap monitorDeviceVarMap) {
        return monitorDeviceVarMapMapper.selectMonitorDeviceVarMapList(monitorDeviceVarMap);
    }

    /**
     * 新增监控设备变量索引地图
     *
     * @param monitorDeviceVarMap 监控设备变量索引地图
     * @return 结果
     */
    @Override
    public int insertMonitorDeviceVarMap(MonitorDeviceVarMap monitorDeviceVarMap) {
        monitorDeviceVarMap.setCreateBy(SecurityUtils.getNickName());
        monitorDeviceVarMap.setCreateTime(DateUtils.getNowDate());
        if (monitorDeviceVarMap.getStopFlag() == null) {
            monitorDeviceVarMap.setStopFlag(0);
        }
        monitorDeviceVarMap.setDeleteFlag(0);
        return monitorDeviceVarMapMapper.insertMonitorDeviceVarMap(monitorDeviceVarMap);
    }

    /**
     * 修改监控设备变量索引地图
     *
     * @param monitorDeviceVarMap 监控设备变量索引地图
     * @return 结果
     */
    @Override
    public int updateMonitorDeviceVarMap(MonitorDeviceVarMap monitorDeviceVarMap) {
        monitorDeviceVarMap.setUpdateBy(SecurityUtils.getNickName());
        monitorDeviceVarMap.setUpdateTime(DateUtils.getNowDate());
        return monitorDeviceVarMapMapper.updateMonitorDeviceVarMap(monitorDeviceVarMap);
    }

    /**
     * 修改监控设备变量索引地图状态
     *
     * @param id    监控设备变量索引地图主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateMonitorDeviceVarMapState(Long id, Integer state) {
        MonitorDeviceVarMap monitorDeviceVarMap = new MonitorDeviceVarMap();
        monitorDeviceVarMap.setId(id);
        monitorDeviceVarMap.setStopFlag(state);
        monitorDeviceVarMap.setUpdateBy(SecurityUtils.getNickName());
        monitorDeviceVarMap.setUpdateTime(DateUtils.getNowDate());
        return monitorDeviceVarMapMapper.updateMonitorDeviceVarMap(monitorDeviceVarMap);
    }

    /**
     * 批量删除监控设备变量索引地图
     *
     * @param ids 需要删除的监控设备变量索引地图主键
     * @return 结果
     */
    @Override
    public int deleteMonitorDeviceVarMapByIds(Long[] ids) {
        return monitorDeviceVarMapMapper.deleteMonitorDeviceVarMapByIds(ids);
    }

    /**
     * 删除监控设备变量索引地图信息
     *
     * @param id 监控设备变量索引地图主键
     * @return 结果
     */
    @Override
    public int deleteMonitorDeviceVarMapById(Long id) {
        return monitorDeviceVarMapMapper.deleteMonitorDeviceVarMapById(id);
    }

    /**
     * 判断是否存在子节点
     *
     * @param id 监控设备变量索引地图主键
     * @return 结果
     */
    @Override
    public boolean hasChildrenById(Long id) {
        return monitorDeviceVarMapMapper.hasChildrenById(id) > 0;
    }

    /**
     * 校验索引编码是否唯一
     */
    @Override
    public boolean checkIndexSnUnique(String indexSn) {
        return monitorDeviceVarMapMapper.checkIndexSnUnique(indexSn) > 0;
    }
}
