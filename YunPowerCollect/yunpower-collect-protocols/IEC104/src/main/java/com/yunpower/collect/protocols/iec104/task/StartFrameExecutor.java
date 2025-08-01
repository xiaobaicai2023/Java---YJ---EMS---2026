package com.yunpower.collect.protocols.iec104.task;

import com.yunpower.collect.protocols.iec104.config.BasicInstruction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 发送启动帧
 * @Author: Jiajiaglam
 * @date: 2023-12-25 14:02
 * @description:
 */
public class StartFrameExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartFrameExecutor.class);

    private final ChannelHandlerContext ctx;

    public StartFrameExecutor(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            LOGGER.debug("发送启动链路指令");
            LOGGER.info("Netty启动顺序.....3-1（链路启动）");
            ctx.channel().writeAndFlush(BasicInstruction.START);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
