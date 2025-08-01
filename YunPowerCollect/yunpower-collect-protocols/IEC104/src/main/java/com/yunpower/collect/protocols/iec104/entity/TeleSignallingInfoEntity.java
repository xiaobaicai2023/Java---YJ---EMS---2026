package com.yunpower.collect.protocols.iec104.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: 遥信：远程信号。采集并传送各种保护告警和开关量信息。
 * @Author: Jiajiaglam
 * @date: 2023-12-11 16:31
 * @description:
 */
@Data
public class TeleSignallingInfoEntity implements Serializable {

    /**
     * 遥信类型
     * 1：不带时标的单点遥信
     * 3：不带师表的双点遥信
     * 30：带CP56Time2a时标的单点遥信
     * 31：CP56Time2a时标的双点遥信
     */
    private int type;

    /**
     * 信息对象地址
     */
    private int infoAddress;

    /**
     * 值
     */
    private int value;

    /**
     * 带时标时的 时间值
     */
    private Date time;
}
