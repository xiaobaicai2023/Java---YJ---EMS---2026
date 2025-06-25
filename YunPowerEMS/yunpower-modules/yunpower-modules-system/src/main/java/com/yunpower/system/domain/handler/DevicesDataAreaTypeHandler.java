package com.yunpower.system.domain.handler;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.yunpower.system.domain.jsonvo.DevicesDataAreaVo;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @title: 自定义类型处理器
 * @Author: Jiajiaglam
 * @date: 2024-01-08 14:04
 * @description:
 */
@MappedJdbcTypes(JdbcType.JAVA_OBJECT)
@MappedTypes({List.class})
public class DevicesDataAreaTypeHandler extends AbstractJsonTypeHandler<List<DevicesDataAreaVo>> {
    @Override
    protected List<DevicesDataAreaVo> parse(String json) {
        return JSON.parseArray(json, DevicesDataAreaVo.class);
    }

    @Override
    protected String toJson(List<DevicesDataAreaVo> obj) {
        return JSON.toJSONString(obj);
    }
}
