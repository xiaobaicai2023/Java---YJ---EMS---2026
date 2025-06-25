package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.SysCommonDictData;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author yunpower
 */
public interface SysCommonDictDataMapper {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysCommonDictData> selectDictDataByType(String dictType);
}
