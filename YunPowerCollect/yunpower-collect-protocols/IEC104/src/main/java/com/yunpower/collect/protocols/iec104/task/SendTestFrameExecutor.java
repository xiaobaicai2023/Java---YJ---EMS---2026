package com.yunpower.collect.protocols.iec104.task;

import com.yunpower.collect.protocols.iec104.config.BasicInstruction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 发送测试帧
 * @Author: Jiajiaglam
 * @date: 2023-12-25 16:05
 * @description:
 */
public class SendTestFrameExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendTestFrameExecutor.class);

    private final ChannelHandlerContext ctx;

    public SendTestFrameExecutor(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            LOGGER.debug("发送测试链路指令");
            LOGGER.info("Netty启动顺序.....3-3（发送测试指令）");
            ctx.channel().writeAndFlush(BasicInstruction.TEST_U);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
