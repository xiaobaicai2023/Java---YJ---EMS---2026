package com.yunpower.common.datasource.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.lang.annotation.*;

/**
 * @title: 日志数据源
 * @Author: Jiajiaglam
 * @date: 2023-10-17 10:21
 * @description:
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS("logdb")
public @interface LogDataSource {

}
