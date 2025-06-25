package com.yunpower.system.mapper;

import com.yunpower.system.domain.DashboardConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 仪表盘配置Mapper接口
 * 
 * @author yunpower
 * @date 2024-05-30
 */
@Mapper
public interface DashboardConfigMapper 
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
     * @param pageKey 仪表盘配置key
     * @return 仪表盘配置
     */
    public DashboardConfig selectDashboardConfigByPageKey(String pageKey);

    /**
     * 查询仪表盘配置列表
     *
     * @param dashboardConfig 仪表盘配置
     * @return 仪表盘配置集合
     */
    public DashboardConfig selectDashboardConfig(DashboardConfig dashboardConfig);

    /**
     * 查询仪表盘配置列表
     * 
     * @param dashboardConfig 仪表盘配置
     * @return 仪表盘配置集合
     */
    public List<DashboardConfig> selectDashboardConfigList(DashboardConfig dashboardConfig);

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
     * 删除仪表盘配置
     * 
     * @param id 仪表盘配置主键
     * @return 结果
     */
    public int deleteDashboardConfigById(Long id);

    /**
     * 批量删除仪表盘配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDashboardConfigByIds(Long[] ids);
}
