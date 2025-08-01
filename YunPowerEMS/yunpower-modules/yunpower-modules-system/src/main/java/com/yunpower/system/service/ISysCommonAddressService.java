package com.yunpower.system.service;

import com.yunpower.system.domain.SysCommonAddress;

import java.util.List;

/**
 * 全国四级行政区Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ISysCommonAddressService {
    /**
     * 查询全国四级行政区
     *
     * @param id 全国四级行政区主键
     * @return 全国四级行政区
     */
    public SysCommonAddress selectSysCommonAddressById(Long id);

    /**
     * 根据ID获取名称
     *
     * @param id 全国四级行政区主键
     * @return 名称
     */
    public String getAddressNameById(Long id);

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
     * 批量删除全国四级行政区
     *
     * @param ids 需要删除的全国四级行政区主键集合
     * @return 结果
     */
    public int deleteSysCommonAddressByIds(Long[] ids);

    /**
     * 删除全国四级行政区信息
     *
     * @param id 全国四级行政区主键
     * @return 结果
     */
    public int deleteSysCommonAddressById(Long id);

    /**
     * 根据省市区县id获取三级中文地址
     * @param provinceId 省id
     * @param cityId 市id
     * @param countyId 区县id
     * */
    public String getAddressName(Long provinceId, Long cityId, Long countyId);
}
