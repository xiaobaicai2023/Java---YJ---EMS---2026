package com.yunpower.collect.protocols.iec104.handler;

/**
 * @title: 处理数据
 * @Author: Jiajiaglam
 * @date: 2023-12-12 15:23
 * @description:
 */
public interface IChannelHandler {
    /**
     * 发送信息
     */
    void writeAndFlush(byte[] cmd);
}
