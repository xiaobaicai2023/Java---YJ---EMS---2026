package com.yunpower.collect.storage.domain.handler;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.yunpower.collect.storage.domain.jsonvo.SeasonalRangeVo;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @title: 自定义类型处理器
 * @Author: Jiajiaglam
 * @date: 2023-11-24 9:51
 * @description:
 */
@MappedJdbcTypes(JdbcType.JAVA_OBJECT)
@MappedTypes({List.class})
public class SeasonalRangeTypeHandler extends AbstractJsonTypeHandler<List<SeasonalRangeVo>> {
    @Override
    protected List<SeasonalRangeVo> parse(String json) {
        return JSON.parseArray(json, SeasonalRangeVo.class);
    }

    @Override
    protected String toJson(List<SeasonalRangeVo> obj) {
        return JSON.toJSONString(obj);
    }
}