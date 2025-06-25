package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.ElectricPriceSchemeConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: 电度电价配置Mapper接口
 * @Author: Jiajiaglam
 * @date: 2023-12-28 15:16
 * @description:
 */
public interface ElectricPriceSchemeConfigMapper {

    /**
     * 查询电度电价配置列表
     *
     * @param affterYear 生效年份
     * @return 电度电价配置集合
     */
    public List<ElectricPriceSchemeConfig> selectElectricPriceSchemeConfigList(@Param("affterYear") Integer affterYear);
}
