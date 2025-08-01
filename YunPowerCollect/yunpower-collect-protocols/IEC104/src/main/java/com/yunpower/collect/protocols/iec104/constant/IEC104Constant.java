package com.yunpower.collect.protocols.iec104.constant;

/**
 * @title: 静态变量
 * @Author: Jiajiaglam
 * @date: 2023-12-11 16:12
 * @description:
 */
public class IEC104Constant {

    /**
     * 开始字符
     */
    public static final byte HEAD_DATA = 0x68;

    /**
     * 注册码 开始字符
     */
    public static final byte REGISTER_HEAD_DATA = 0x36;

    /**
     * 控制域长度
     */
    public static final byte CPNTROL_LENGTH = 0x04;

    /**
     * APCI 长度
     */
    public static final byte APCI_LENGTH = 0x06;

    /**
     * APCI 中 发送序号低位坐标
     */
    public static final int ACCEPT_LOW_INDEX = 2;

    /**
     * APCI 中 发送序号高位坐标
     */
    public static final int ACCEPT_HIGH_INDEX = 3;

    /**
     * 最大接收序号
     */
    public static final Short SEND_MAX = 32767;

    /**
     * 最小接收序号
     */
    public static final Short SEND_MIN = 0;
}
