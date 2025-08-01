package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.SysStation;
import com.yunpower.collect.storage.mapper.SysStationMapper;
import com.yunpower.collect.storage.service.ISysStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title:
 * @Author: Jiajiaglam
 * @date: 2023-12-28 16:16
 * @description:
 */
@Service
public class SysStationServiceImpl implements ISysStationService {

    @Autowired
    private SysStationMapper stationMapper;

    /**
     * 查询站点列表
     * */
    @Override
    public List<SysStation> selectStationList(){
        return stationMapper.selectSysStationList();
    }
}
