package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.api.domain.SysStation;
import org.apache.ibatis.annotations.Param;

/**
 * 电站Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
public interface ISysStationService {
    /**
     * 查询电站
     *
     * @param id 电站主键
     * @return 电站
     */
    public SysStation selectSysStationById(Long id);

    public SysStation selectSysStationBySn(String stationSn);

    /**
     * 通过部门ID获取站点ID（数据库是一一对应的）
     *
     * @param deptId 部门ID
     * @return 电站
     */
    public SysStation selectSysStationByDeptId(Long deptId);

    /**
     * 查询电站列表
     *
     * @param sysStation 电站
     * @return 电站集合
     */
    public List<SysStation> selectSysStationList(SysStation sysStation);

    /**
     * 获取站点列表，不限制数据权限
     *
     * @param sysStation 电站
     * @return 电站集合
     */
    public List<SysStation> selectStationListAll(SysStation sysStation);

    /**
     * 根据ID查询所有子站点
     *
     * @param id 站点ID
     * @return 站点列表
     */
    public List<SysStation> selectChildrenStationById(Long id);

    /**
     * 根据ID查询所有子站点（正常状态）
     *
     * @param id 站点ID
     * @return 子站点数
     */
    public int selectNormalChildrenStationById(Long id);

    /**
     * 查询站点是否存在子节点
     *
     * @param id 站点ID
     * @return 结果
     */
    public boolean hasChildrenById(Long id);

    /**
     * 查询站点是否存在用户
     *
     * @param id 站点ID
     * @return 结果
     */
    public boolean checkStationExistUser(Long id);

    /**
     * 修改子元素关系
     *
     * @param stations 子元素
     * @return 结果
     */
    public int updateStationChildren(@Param("stations") List<SysStation> stations);

    /**
     * 新增电站
     *
     * @param sysStation 电站
     * @return 结果
     */
    public int insertSysStation(SysStation sysStation);

    /**
     * 修改电站
     *
     * @param sysStation 电站
     * @return 结果
     */
    public int updateSysStation(SysStation sysStation);

    /**
     * 修改电站状态
     *
     * @param id    电站主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysStationState(SysStation sysStation, Long id, Integer state);

    /**
     * 修改电站报警状态
     *
     * @param id    电站主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysStationAlarmState(SysStation sysStation, Long id, Integer state);

    /**
     * 批量删除电站
     *
     * @param ids 需要删除的电站主键集合
     * @return 结果
     */
    public int deleteSysStationByIds(SysStation sysStation, Long[] ids);

    /**
     * 删除电站信息
     *
     * @param id 电站主键
     * @return 结果
     */
    public int deleteSysStationById(SysStation sysStation, Long id);

    /**
     * 构建树结构
     * 递归
     *
     * @return 结果
     */
    public List<SysStation> buildStationTree();

    /**
     * 构建树结构
     * 递归
     *
     * @param stationList 站点列表
     * @return 结果
     */
    public List<SysStation> buildStationTree(List<SysStation> stationList);
}
