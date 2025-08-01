package com.yunpower.collect.protocols.iec104.handler;

import com.yunpower.collect.protocols.iec104.message.MessageBody;
import io.netty.channel.ChannelHandlerContext;

/**
 * @title: 数据处理
 * @Author: Jiajiaglam
 * @date: 2023-12-12 15:22
 * @description:
 */
public interface IDataHandler {
    /**
     * 建立连接
     */
    void handlerAdded(ChannelHandlerContext ctx) throws Exception;

    /**
     * 收到消息
     */
    void channelRead(ChannelHandlerContext ctx, MessageBody msg, String connectKey, String dateTime) throws Exception;
}