package com.yunpower.system.service;

import com.yunpower.system.domain.DashboardConfig;

import java.util.List;
import java.util.Map;

/**
 * 仪表盘配置Service接口
 *
 * @author yunpower
 * @date 2024-05-30
 */
public interface IDashboardConfigService
{
    /**
     * 查询仪表盘配置
     *
     * @param id 仪表盘配置主键
     * @return 仪表盘配置
     */
    public DashboardConfig selectDashboardConfigById(Long id);

    /**
     * 查询仪表盘配置
     *
     * @param pageKey 页面配置key
     * @return 仪表盘配置
     */
    public DashboardConfig selectDashboardConfigByPageKey(String pageKey);

    /**
     * 查询仪表盘配置-站点id
     *
     * @param stationId 站点id
     * @param isPre 是否预览
     * @return 仪表盘配置
     */
    public Map<String,Object> selectDashboardConfigByStationId(Long stationId, Integer isPre);

    /**
     * 查询仪表盘配置列表
     *
     * @param dashboardConfig 仪表盘配置
     * @return 仪表盘配置集合
     */
    public List<DashboardConfig> selectDashboardConfigList(DashboardConfig dashboardConfig);

    /**
     * 查询仪表盘配置列表
     *
     * @param dashboardConfig 仪表盘配置
     * @return 仪表盘配置集合
     */
    public List<DashboardConfig> selectDefaultDashboardConfigList(DashboardConfig dashboardConfig);


    /**
     * 新增仪表盘配置
     *
     * @param dashboardConfig 仪表盘配置
     * @return 结果
     */
    public int insertDashboardConfig(DashboardConfig dashboardConfig);

    /**
     * 修改仪表盘配置
     *
     * @param dashboardConfig 仪表盘配置
     * @return 结果
     */
    public int updateDashboardConfig(DashboardConfig dashboardConfig);

    /**
     * 修改仪表盘配置状态
     *
     * @param id 仪表盘配置主键
     * @param state 状态
     * @return 结果
     */
    public int updateDashboardConfigState(Long id, Integer state);

    /**
     * 批量删除仪表盘配置
     *
     * @param ids 需要删除的仪表盘配置主键集合
     * @return 结果
     */
    public int deleteDashboardConfigByIds(Long[] ids);

    /**
     * 删除仪表盘配置信息
     *
     * @param id 仪表盘配置主键
     * @return 结果
     */
    public int deleteDashboardConfigById(Long id);
}
