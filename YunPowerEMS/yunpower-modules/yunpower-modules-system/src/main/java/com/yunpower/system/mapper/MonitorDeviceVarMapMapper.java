package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.MonitorDeviceVarMap;

/**
 * 监控设备变量索引地图Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface MonitorDeviceVarMapMapper
{
    /**
     * 查询监控设备变量索引地图
     *
     * @param id 监控设备变量索引地图主键
     * @return 监控设备变量索引地图
     */
    public MonitorDeviceVarMap selectMonitorDeviceVarMapById(Long id);

    /**
     * 通过索引编码获取
     *
     * @param indexSn 索引编码
     * @return 结果
     */
    public MonitorDeviceVarMap selectMonitorDeviceVarMapByIndexSn(String indexSn);

    /**
     * 查询监控设备变量索引地图列表
     *
     * @param monitorDeviceVarMap 监控设备变量索引地图
     * @return 监控设备变量索引地图集合
     */
    public List<MonitorDeviceVarMap> selectMonitorDeviceVarMapList(MonitorDeviceVarMap monitorDeviceVarMap);

    /**
     * 新增监控设备变量索引地图
     *
     * @param monitorDeviceVarMap 监控设备变量索引地图
     * @return 结果
     */
    public int insertMonitorDeviceVarMap(MonitorDeviceVarMap monitorDeviceVarMap);

    /**
     * 修改监控设备变量索引地图
     *
     * @param monitorDeviceVarMap 监控设备变量索引地图
     * @return 结果
     */
    public int updateMonitorDeviceVarMap(MonitorDeviceVarMap monitorDeviceVarMap);

    /**
     * 删除监控设备变量索引地图
     *
     * @param id 监控设备变量索引地图主键
     * @return 结果
     */
    public int deleteMonitorDeviceVarMapById(Long id);

    /**
     * 批量删除监控设备变量索引地图
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMonitorDeviceVarMapByIds(Long[] ids);

    /**
     * 是否含有子索引
     */
    public int hasChildrenById(Long id);

    /**
     * 校验索引编码是否唯一
     */
    public int checkIndexSnUnique(String indexSn);
}
