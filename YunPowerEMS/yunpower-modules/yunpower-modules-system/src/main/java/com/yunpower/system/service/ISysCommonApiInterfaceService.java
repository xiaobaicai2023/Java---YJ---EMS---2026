package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysCommonApiInterface;

/**
 * 数据接口Service接口
 * 
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ISysCommonApiInterfaceService 
{
    /**
     * 查询数据接口
     * 
     * @param id 数据接口主键
     * @return 数据接口
     */
    public SysCommonApiInterface selectSysCommonApiInterfaceById(Long id);

    /**
     * 查询数据接口列表
     * 
     * @param sysCommonApiInterface 数据接口
     * @return 数据接口集合
     */
    public List<SysCommonApiInterface> selectSysCommonApiInterfaceList(SysCommonApiInterface sysCommonApiInterface);

    /**
     * 新增数据接口
     * 
     * @param sysCommonApiInterface 数据接口
     * @return 结果
     */
    public int insertSysCommonApiInterface(SysCommonApiInterface sysCommonApiInterface);

    /**
     * 修改数据接口
     * 
     * @param sysCommonApiInterface 数据接口
     * @return 结果
     */
    public int updateSysCommonApiInterface(SysCommonApiInterface sysCommonApiInterface);

    /**
     * 修改数据接口状态
     *
     * @param id 数据接口主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysCommonApiInterfaceState(Long id, Integer state);

    /**
     * 批量删除数据接口
     * 
     * @param ids 需要删除的数据接口主键集合
     * @return 结果
     */
    public int deleteSysCommonApiInterfaceByIds(Long[] ids);

    /**
     * 删除数据接口信息
     * 
     * @param id 数据接口主键
     * @return 结果
     */
    public int deleteSysCommonApiInterfaceById(Long id);
}
