package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.DevopsMaintenance;

/**
 * 维保记录Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IDevopsMaintenanceService {
    /**
     * 查询维保记录
     *
     * @param id 维保记录主键
     * @return 维保记录
     */
    public DevopsMaintenance selectDevopsMaintenanceById(Long id);

    /**
     * 查询维保记录列表
     *
     * @param devopsMaintenance 维保记录
     * @return 维保记录集合
     */
    public List<DevopsMaintenance> selectDevopsMaintenanceList(DevopsMaintenance devopsMaintenance);

    /**
     * 新增维保记录
     *
     * @param devopsMaintenance 维保记录
     * @return 结果
     */
    public int insertDevopsMaintenance(DevopsMaintenance devopsMaintenance);

    /**
     * 修改维保记录
     *
     * @param devopsMaintenance 维保记录
     * @return 结果
     */
    public int updateDevopsMaintenance(DevopsMaintenance devopsMaintenance);

    /**
     * 修改维保记录状态
     *
     * @param id    维保记录主键
     * @param state 状态
     * @return 结果
     */
    public int updateDevopsMaintenanceState(DevopsMaintenance devopsMaintenance, Long id, Integer state);

    /**
     * 批量删除维保记录
     *
     * @param ids 需要删除的维保记录主键集合
     * @return 结果
     */
    public int deleteDevopsMaintenanceByIds(DevopsMaintenance devopsMaintenance, Long[] ids);

    /**
     * 删除维保记录信息
     *
     * @param id 维保记录主键
     * @return 结果
     */
    public int deleteDevopsMaintenanceById(DevopsMaintenance devopsMaintenance, Long id);
}
