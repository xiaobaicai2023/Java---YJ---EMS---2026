package com.yunpower.collect.storage.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @title: 日数据实体类
 * @Author: Jiajiaglam
 * @date: 2023-07-17 15:48
 * @description:
 */
@Data
public class DayDataEntity implements Serializable {

    private String variableName;

    private String saveTime;

    private Double dataValue;
}
