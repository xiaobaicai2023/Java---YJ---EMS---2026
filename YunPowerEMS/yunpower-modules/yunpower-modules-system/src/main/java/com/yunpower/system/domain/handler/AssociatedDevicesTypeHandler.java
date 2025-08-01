package com.yunpower.system.domain.handler;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.yunpower.system.domain.jsonvo.AssociatedDevicesVo;
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
public class AssociatedDevicesTypeHandler extends AbstractJsonTypeHandler<List<AssociatedDevicesVo>> {
    @Override
    protected List<AssociatedDevicesVo> parse(String json) {
        return JSON.parseArray(json, AssociatedDevicesVo.class);
    }

    @Override
    protected String toJson(List<AssociatedDevicesVo> obj) {
        return JSON.toJSONString(obj);
    }
}