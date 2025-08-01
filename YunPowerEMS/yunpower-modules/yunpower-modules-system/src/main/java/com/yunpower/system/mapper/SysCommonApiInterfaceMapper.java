package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysCommonApiInterface;

/**
 * 数据接口Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface SysCommonApiInterfaceMapper
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
     * 删除数据接口
     *
     * @param id 数据接口主键
     * @return 结果
     */
    public int deleteSysCommonApiInterfaceById(Long id);

    /**
     * 批量删除数据接口
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCommonApiInterfaceByIds(Long[] ids);
}
