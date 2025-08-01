package com.yunpower.collect.protocols.iec104.message;

import com.yunpower.collect.protocols.iec104.enums.QualifiersEnum;
import lombok.Data;

/**
 * @title: 报文中的消息部分
 * @Author: Jiajiaglam
 * @date: 2023-12-11 16:40
 * @description:
 */
@Data
public class MessageInfo {
    /**
     * 消息地址 字节
     */
    private int messageAddress;

    /**
     * 信息元素集合 1 2 4 个字节
     */
    private byte[] messageInfos;

    /**
     * 限定词
     */
    private QualifiersEnum qualifiersType;

    /**
     * 消息详情
     */
    private int messageInfoLength;
}
