package com.yunpower.system.mapper;

import com.yunpower.system.api.domain.DashboardConfigCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仪表盘卡片配置Mapper接口
 * 
 * @author yunpower
 * @date 2024-06-05
 */
@Mapper
public interface DashboardConfigCardMapper 
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
     * @param dashboardConfigId 配置页id
     * @param cardKey 卡片key
     * @return 仪表盘卡片配置
     */
    public DashboardConfigCard selectInfoByConfigIdWidthCardKey(@Param("dashboardConfigId") Long dashboardConfigId, @Param("cardKey") String cardKey);

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
     * 删除仪表盘卡片配置
     * 
     * @param id 仪表盘卡片配置主键
     * @return 结果
     */
    public int deleteDashboardConfigCardById(Long id);

    /**
     * 批量删除仪表盘卡片配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDashboardConfigCardByIds(Long[] ids);

    /**
     * 批量删除仪表盘卡片配置
     *
     * @param dashboardConfigId 页面配置ID
     * @param cardKeyList cardKey集合
     * @return 结果
     */
    public int deleteByConfigIdWidthCardKeyList(@Param("dashboardConfigId") Long dashboardConfigId,@Param("cardKeyList") List<String> cardKeyList);

    /**
     * 通过 dashboardConfigId + cardKey 检查是否存在
     * */
    public int CheckExitByConfigIdWithCardKey(@Param("dashboardConfigId") Long dashboardConfigId, @Param("cardKey") String cardKey);
}
