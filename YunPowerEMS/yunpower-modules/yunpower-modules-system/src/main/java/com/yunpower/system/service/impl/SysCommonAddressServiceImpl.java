package com.yunpower.system.service.impl;

import com.yunpower.system.domain.SysCommonAddress;
import com.yunpower.system.mapper.SysCommonAddressMapper;
import com.yunpower.system.service.ISysCommonAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 全国四级行政区Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysCommonAddressServiceImpl implements ISysCommonAddressService {
    @Autowired
    private SysCommonAddressMapper sysCommonAddressMapper;

    /**
     * 查询全国四级行政区
     *
     * @param id 全国四级行政区主键
     * @return 全国四级行政区
     */
    @Override
    public SysCommonAddress selectSysCommonAddressById(Long id) {
        return sysCommonAddressMapper.selectSysCommonAddressById(id);
    }

    /**
     * 根据ID获取名称
     *
     * @param id 全国四级行政区主键
     * @return 名称
     */
    @Override
    public String getAddressNameById(Long id) {
        SysCommonAddress sysCommonAddress = sysCommonAddressMapper.selectSysCommonAddressById(id);
        return sysCommonAddress == null ? "" : sysCommonAddress.getName();
    }

    /**
     * 查询全国四级行政区列表
     *
     * @param sysCommonAddress 全国四级行政区
     * @return 全国四级行政区
     */
    @Override
    public List<SysCommonAddress> selectSysCommonAddressList(SysCommonAddress sysCommonAddress) {
        return sysCommonAddressMapper.selectSysCommonAddressList(sysCommonAddress);
    }

    /**
     * 新增全国四级行政区
     *
     * @param sysCommonAddress 全国四级行政区
     * @return 结果
     */
    @Override
    public int insertSysCommonAddress(SysCommonAddress sysCommonAddress) {
        return sysCommonAddressMapper.insertSysCommonAddress(sysCommonAddress);
    }

    /**
     * 修改全国四级行政区
     *
     * @param sysCommonAddress 全国四级行政区
     * @return 结果
     */
    @Override
    public int updateSysCommonAddress(SysCommonAddress sysCommonAddress) {
        return sysCommonAddressMapper.updateSysCommonAddress(sysCommonAddress);
    }

    /**
     * 批量删除全国四级行政区
     *
     * @param ids 需要删除的全国四级行政区主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonAddressByIds(Long[] ids) {
        return sysCommonAddressMapper.deleteSysCommonAddressByIds(ids);
    }

    /**
     * 删除全国四级行政区信息
     *
     * @param id 全国四级行政区主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonAddressById(Long id) {
        return sysCommonAddressMapper.deleteSysCommonAddressById(id);
    }

    /**
     * 根据省市区县id获取三级中文地址
     *
     * @param provinceId 省id
     * @param cityId     市id
     * @param countyId   区县id
     */
    @Override
    public String getAddressName(Long provinceId, Long cityId, Long countyId) {
        List<SysCommonAddress> resultList = sysCommonAddressMapper.selectAddressName(new Long[]{provinceId, cityId, countyId});
        return Optional.ofNullable(resultList)
                .orElseGet(Collections::emptyList) // 如果 resultList 为空，则返回一个空列表
                .stream()
                .map(SysCommonAddress::getName)
                .collect(Collectors.joining(""));
    }
}
