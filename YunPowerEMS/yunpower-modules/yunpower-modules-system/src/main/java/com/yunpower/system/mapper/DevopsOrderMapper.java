package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.DevopsOrder;

/**
 * 工单管理Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface DevopsOrderMapper
{
    /**
     * 查询工单管理
     *
     * @param id 工单管理主键
     * @return 工单管理
     */
    public DevopsOrder selectDevopsOrderById(Long id);

    /**
     * 查询工单管理列表
     *
     * @param devopsOrder 工单管理
     * @return 工单管理集合
     */
    public List<DevopsOrder> selectDevopsOrderList(DevopsOrder devopsOrder);

    /**
     * 按工单类型统计
     */
    public List<DevopsOrder> selectStatisticByOrderType(DevopsOrder devopsOrder);

    /**
     * 按工单状态统计
     */
    public List<DevopsOrder> selectStatisticByStatus(DevopsOrder devopsOrder);

    /**
     * 按日期统计
     */
    public List<DevopsOrder> selectStatisticByDate(DevopsOrder devopsOrder);

    /**
     * 新增工单管理
     *
     * @param devopsOrder 工单管理
     * @return 结果
     */
    public int insertDevopsOrder(DevopsOrder devopsOrder);

    /**
     * 修改工单管理
     *
     * @param devopsOrder 工单管理
     * @return 结果
     */
    public int updateDevopsOrder(DevopsOrder devopsOrder);

    /**
     * 删除工单管理
     *
     * @param id 工单管理主键
     * @return 结果
     */
    public int deleteDevopsOrderById(Long id);

    /**
     * 批量删除工单管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDevopsOrderByIds(Long[] ids);
}
