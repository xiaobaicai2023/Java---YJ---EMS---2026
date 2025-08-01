package com.yunpower.collect.protocols.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketHeardHandle extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHeardHandle.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ("ping".equals(msg)) {
            LOGGER.info("WebSocketServer Netty 收到客户端的心跳消息，回复心跳响应");
            ctx.writeAndFlush("pong");
        } else {
            // 处理其他消息
            ctx.fireChannelRead(msg);
        }
    }
}
