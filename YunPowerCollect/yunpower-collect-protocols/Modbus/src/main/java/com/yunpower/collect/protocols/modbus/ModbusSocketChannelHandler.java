package com.yunpower.collect.protocols.modbus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ModbusSocketChannelHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModbusSocketChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        LOGGER.info("---------------read---------------");
        LOGGER.info("R客户端：{},{}", ctx.channel().id().asShortText(),msg);

        String ipAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        String connectKey = ipAddress + ":" + port;
        LOGGER.info("R远程信息服务器：{}", connectKey);
        ctx.writeAndFlush("Message received: " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Client encountered an error", cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Client channel is active");
        ctx.writeAndFlush("Hello from client!\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Client channel is inactive");
    }
}
