package com.yunpower.common.datasource.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.lang.annotation.*;

/**
 * @title: 分表数据源
 * @Author: Jiajiaglam
 * @date: 2023-10-17 10:52
 * @description: ShardingDataSource 这个名称可能会和 Sharding 的默认命名有冲突
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS("sharding")
public @interface ShardingDataSource {

}
