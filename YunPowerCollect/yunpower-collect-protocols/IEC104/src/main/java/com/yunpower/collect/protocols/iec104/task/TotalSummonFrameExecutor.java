package com.yunpower.collect.protocols.iec104.task;

import com.yunpower.collect.protocols.iec104.common.SendDataFrameHelper;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 发送总召唤
 * @Author: Jiajiaglam
 * @date: 2023-12-25 13:34
 * @description:
 */
public class TotalSummonFrameExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TotalSummonFrameExecutor.class);

    private final ChannelHandlerContext ctx;

    public TotalSummonFrameExecutor(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            LOGGER.debug("发送总召唤指令");
            LOGGER.info("Netty启动顺序.....6（发送总召唤指令）");
            SendDataFrameHelper.sendTotalSummonFrame(ctx.channel(), 1, 6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
