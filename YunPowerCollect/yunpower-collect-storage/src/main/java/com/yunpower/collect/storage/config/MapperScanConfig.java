package com.yunpower.collect.storage.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @title: 程序注解配置
 * @Author: Jiajiaglam
 * @date: 2023-07-15 9:24
 * @description: Mapper包扫描
 */
@Configuration
@MapperScan("com.yunpower.collect.storage.**.mapper")
public class MapperScanConfig {

}
