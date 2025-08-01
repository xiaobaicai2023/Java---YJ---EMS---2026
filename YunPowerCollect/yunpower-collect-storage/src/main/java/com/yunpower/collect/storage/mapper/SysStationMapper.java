package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.SysStation;

import java.util.List;

/**
 * @title: 电站Mapper接口
 * @Author: Jiajiaglam
 * @date: 2023-12-28 16:18
 * @description:
 */
public interface SysStationMapper {
    /**
     * 查询电站列表
     *
     * @return 电站集合
     */
    public List<SysStation> selectSysStationList();
}
