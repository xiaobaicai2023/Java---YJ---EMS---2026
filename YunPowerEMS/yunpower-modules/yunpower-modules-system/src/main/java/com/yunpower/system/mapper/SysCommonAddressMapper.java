package com.yunpower.system.mapper;

import com.yunpower.system.domain.SysCommonAddress;

import java.util.List;

/**
 * 全国四级行政区Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface SysCommonAddressMapper
{
    /**
     * 查询全国四级行政区
     *
     * @param id 全国四级行政区主键
     * @return 全国四级行政区
     */
    public SysCommonAddress selectSysCommonAddressById(Long id);

    /**
     * 查询全国四级行政区列表
     *
     * @param sysCommonAddress 全国四级行政区
     * @return 全国四级行政区集合
     */
    public List<SysCommonAddress> selectSysCommonAddressList(SysCommonAddress sysCommonAddress);

    /**
     * 新增全国四级行政区
     *
     * @param sysCommonAddress 全国四级行政区
     * @return 结果
     */
    public int insertSysCommonAddress(SysCommonAddress sysCommonAddress);

    /**
     * 修改全国四级行政区
     *
     * @param sysCommonAddress 全国四级行政区
     * @return 结果
     */
    public int updateSysCommonAddress(SysCommonAddress sysCommonAddress);

    /**
     * 删除全国四级行政区
     *
     * @param id 全国四级行政区主键
     * @return 结果
     */
    public int deleteSysCommonAddressById(Long id);

    /**
     * 批量删除全国四级行政区
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCommonAddressByIds(Long[] ids);

    /**
     * 根据省市区县id获取三级中文地址
     * @param ids ids
     * */
    public List<SysCommonAddress> selectAddressName(Long[] ids);
}
