package com.yunpower.system.service;

import com.yunpower.system.api.domain.DashboardConfigCard;

import java.util.List;

/**
 * 仪表盘卡片配置Service接口
 * 
 * @author yunpower
 * @date 2024-06-05
 */
public interface IDashboardConfigCardService 
{
    /**
     * 查询仪表盘卡片配置
     * 
     * @param id 仪表盘卡片配置主键
     * @return 仪表盘卡片配置
     */
    public DashboardConfigCard selectDashboardConfigCardById(Long id);

    /**
     * 查询仪表盘卡片配置
     *
     * @param dashboardConfigId 配置页key
     * @param cardKey 卡片key
     * @return 仪表盘卡片配置
     */
    public DashboardConfigCard selectInfoByConfigIdWidthCardKey(Long dashboardConfigId,String cardKey);
    
    /**
     * 查询仪表盘卡片配置列表
     * 
     * @param dashboardConfigCard 仪表盘卡片配置
     * @return 仪表盘卡片配置集合
     */
    public List<DashboardConfigCard> selectDashboardConfigCardList(DashboardConfigCard dashboardConfigCard);

    /**
     * 新增仪表盘卡片配置
     * 
     * @param dashboardConfigCard 仪表盘卡片配置
     * @return 结果
     */
    public int insertDashboardConfigCard(DashboardConfigCard dashboardConfigCard);

    /**
     * 修改仪表盘卡片配置
     * 
     * @param dashboardConfigCard 仪表盘卡片配置
     * @return 结果
     */
    public int updateDashboardConfigCard(DashboardConfigCard dashboardConfigCard);

    /**
     * 修改仪表盘卡片配置状态
     *
     * @param id 仪表盘卡片配置主键
     * @param state 状态
     * @return 结果
     */
    public int updateDashboardConfigCardState(Long id, Integer state);

    /**
     * 批量删除仪表盘卡片配置
     * 
     * @param ids 需要删除的仪表盘卡片配置主键集合
     * @return 结果
     */
    public int deleteDashboardConfigCardByIds(Long[] ids);

    /**
     * 删除仪表盘卡片配置信息
     * 
     * @param id 仪表盘卡片配置主键
     * @return 结果
     */
    public int deleteDashboardConfigCardById(Long id);

}
