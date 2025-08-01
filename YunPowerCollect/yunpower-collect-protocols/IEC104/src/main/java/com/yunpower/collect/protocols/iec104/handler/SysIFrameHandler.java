package com.yunpower.collect.protocols.iec104.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 处理I帧问题
 * @Author: Jiajiaglam
 * @date: 2023-12-28 8:07
 * @description:
 */
public class SysIFrameHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysIFrameHandler.class);

    /**
     * 拦截系统消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result = (ByteBuf) msg;
        byte[] bytes = new byte[result.readableBytes()];
        result.readBytes(bytes);

        // 拦截总召唤确认帧，然后根据限定词回复总召唤
        // 680E9C13A62064010700010000000014
        // TODO

        result.writeBytes(bytes);
        ctx.fireChannelRead(result);
    }
}
  