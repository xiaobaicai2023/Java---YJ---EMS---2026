package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.SysStation;

import java.util.List;

/**
 * @title: 电站Service接口
 * @Author: Jiajiaglam
 * @date: 2023-12-28 16:13
 * @description:
 */
public interface ISysStationService {
    /**
     * 查询站点列表
     * */
    public List<SysStation> selectStationList();
}
