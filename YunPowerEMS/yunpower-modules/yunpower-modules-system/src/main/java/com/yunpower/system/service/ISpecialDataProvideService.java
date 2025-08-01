package com.yunpower.system.service;

import com.yunpower.system.domain.vo.time.TimesVo;
import com.yunpower.system.domain.vo.AssignDataVo;

/**
 * 大屏专用数据（集合）
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/23 10:16
 */
public interface ISpecialDataProvideService {

    /**
     * 获取工单列表
     *
     * @param headType   表头类型：1长表头 2短表头
     * @param staticType 数据类型：记录状态
     * @return 结果
     */
    public Object getDevopsOrderList(Integer headType, Integer staticType, TimesVo timesVo, Long deptId);

    /**
     * 获取工单统计数据
     *
     * @param staticType 统计类型（报警：1按级别 2按类型 3按日期）（工单：1按类型 2按日期 3按状态）
     * @return 结果
     */
    public Object getDevopsOrderStatic(Integer staticType, TimesVo timesVo, Long deptId);

    /**
     * 获取指定接口数据（第1部分）
     * 第2部分在DataV模块中
     *
     * @param deptId     站点（部门）ID
     * @param assignType 指定类型的key
     * @return 结果
     */
    public AssignDataVo getAssignedData(Long deptId, String assignType);
}
