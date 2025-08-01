package com.yunpower.system.service.impl;

import com.yunpower.common.core.exception.ServiceException;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.domain.DashboardConfig;
import com.yunpower.system.api.domain.DashboardConfigCard;
import com.yunpower.system.mapper.DashboardConfigCardMapper;
import com.yunpower.system.mapper.DashboardConfigMapper;
import com.yunpower.system.service.IDashboardConfigCardService;
import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 仪表盘卡片配置Service业务层处理
 * 
 * @author yunpower
 * @date 2024-06-05
 */
@Service
public class DashboardConfigCardServiceImpl implements IDashboardConfigCardService 
{
    @Autowired
    private DashboardConfigCardMapper dashboardConfigCardMapper;

    @Autowired
    private DashboardConfigMapper dashboardConfigMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询仪表盘卡片配置
     * 
     * @param id 仪表盘卡片配置主键
     * @return 仪表盘卡片配置
     */
    @Override
    public DashboardConfigCard selectDashboardConfigCardById(Long id)
    {
        return dashboardConfigCardMapper.selectDashboardConfigCardById(id);
    }

    /**
     * 查询仪表盘卡片配置
     *
     * @param dashboardConfigId 配置页key
     * @param cardKey 卡片key
     * @return 仪表盘卡片配置
     */
    public DashboardConfigCard selectInfoByConfigIdWidthCardKey(Long dashboardConfigId,String cardKey)
    {
        DashboardConfigCard configCard  = dashboardConfigCardMapper.selectInfoByConfigIdWidthCardKey(dashboardConfigId,cardKey);
        if(configCard != null){
            //检查主配置是否存在
            DashboardConfig dashboardConfig = dashboardConfigMapper.selectDashboardConfigById(configCard.getDashboardConfigId());
            if(dashboardConfig == null){
                throw new ServiceException("页面配置不存在");
            }
            configCard.setPageType(dashboardConfig.getPageType());
        }
        return configCard;
    }

    /**
     * 查询仪表盘卡片配置列表
     * 
     * @param dashboardConfigCard 仪表盘卡片配置
     * @return 仪表盘卡片配置
     */
    @Override
    public List<DashboardConfigCard> selectDashboardConfigCardList(DashboardConfigCard dashboardConfigCard)
    {
        return dashboardConfigCardMapper.selectDashboardConfigCardList(dashboardConfigCard);
    }

    /**
     * 新增仪表盘卡片配置
     * 
     * @param dashboardConfigCard 仪表盘卡片配置
     * @return 结果
     */
    @Override
    public int insertDashboardConfigCard(DashboardConfigCard dashboardConfigCard)
    {
        if (dashboardConfigCard.getEntId() == null || dashboardConfigCard.getEntId() <= 0) {
            dashboardConfigCard.setEntId(publicService.getCurrentEnterprise());
        }
        if (dashboardConfigCard.getDeptId() == null || dashboardConfigCard.getDeptId() <= 0) {
            dashboardConfigCard.setDeptId(publicService.getCurrentStation());
        }
        dashboardConfigCard.setCreateBy(SecurityUtils.getNickName());
        dashboardConfigCard.setCreateTime(DateUtils.getNowDate());
        if (dashboardConfigCard.getStopFlag() == null) {
            dashboardConfigCard.setStopFlag(0);
        }
        dashboardConfigCard.setDeleteFlag(0);
        //检查主配置是否存在
        DashboardConfig dashboardConfig = dashboardConfigMapper.selectDashboardConfigById(dashboardConfigCard.getDashboardConfigId());
        if(StringUtils.isNull(dashboardConfig)){
            throw new ServiceException("页面配置不存在");
        }
        //检查cardKey是否已经存在
        int result = dashboardConfigCardMapper.CheckExitByConfigIdWithCardKey(dashboardConfigCard.getDashboardConfigId(), dashboardConfigCard.getCardKey());
        if(result > 0){
            throw new ServiceException("当前配置已存在");
        }
        return dashboardConfigCardMapper.insertDashboardConfigCard(dashboardConfigCard);
    }

    /**
     * 修改仪表盘卡片配置
     * 
     * @param dashboardConfigCard 仪表盘卡片配置
     * @return 结果
     */
    @Override
    public int updateDashboardConfigCard(DashboardConfigCard dashboardConfigCard)
    {
        if(StringUtils.isNull(dashboardConfigCard.getId())  || dashboardConfigCard.getId() <=0){
            return this.insertDashboardConfigCard(dashboardConfigCard);
        }
        dashboardConfigCard.setUpdateBy(SecurityUtils.getNickName());
        dashboardConfigCard.setUpdateTime(DateUtils.getNowDate());
        return dashboardConfigCardMapper.updateDashboardConfigCard(dashboardConfigCard);
    }

    /**
     * 修改仪表盘卡片配置状态
     *
     * @param id 仪表盘卡片配置主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateDashboardConfigCardState(Long id, Integer state) {
        DashboardConfigCard dashboardConfigCard = new DashboardConfigCard();
        dashboardConfigCard.setId(id);
        dashboardConfigCard.setStopFlag(state);
        dashboardConfigCard.setUpdateBy(SecurityUtils.getNickName());
        dashboardConfigCard.setUpdateTime(DateUtils.getNowDate());
        return dashboardConfigCardMapper.updateDashboardConfigCard(dashboardConfigCard);
    }

    /**
     * 批量删除仪表盘卡片配置
     * 
     * @param ids 需要删除的仪表盘卡片配置主键
     * @return 结果
     */
    @Override
    public int deleteDashboardConfigCardByIds(Long[] ids)
    {
        return dashboardConfigCardMapper.deleteDashboardConfigCardByIds(ids);
    }

    /**
     * 删除仪表盘卡片配置信息
     * 
     * @param id 仪表盘卡片配置主键
     * @return 结果
     */
    @Override
    public int deleteDashboardConfigCardById(Long id)
    {
        return dashboardConfigCardMapper.deleteDashboardConfigCardById(id);
    }
}
