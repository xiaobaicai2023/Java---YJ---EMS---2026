package com.yunpower.collect.protocols.iec104.task;

import com.yunpower.collect.protocols.iec104.handler.IDataHandler;
import com.yunpower.collect.protocols.iec104.message.MessageBody;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static com.yunpower.common.core.constant.Constants.CHANNEL_SN;

/**
 * @title: 数据处理
 * @Author: Jiajiaglam
 * @date: 2023-12-25 16:24
 * @description:
 */
public class DataHandlerExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHandlerExecutor.class);

    private final ChannelHandlerContext ctx;

    private final IDataHandler dataHandler;

    private final MessageBody msg;

    private final String connectKey;

    private final String dateTime;

    private final String channelSn;

    public DataHandlerExecutor(ChannelHandlerContext ctx, IDataHandler dataHandler, MessageBody msg, String connectKey, String dateTime, String channelSn) {
        this.ctx = ctx;
        this.dataHandler = dataHandler;
        this.msg = msg;
        this.connectKey = connectKey;
        this.dateTime = dateTime;
        this.channelSn = channelSn;
    }

    @Override
    public void run() {
        if (channelSn != null) {
            MDC.put(CHANNEL_SN, channelSn);
        }

        try {
            if (dataHandler != null) {
                this.dataHandler.channelRead(ctx, msg, connectKey, dateTime);
            }
        } catch (Exception e) {
            LOGGER.error("this.dataHandler.channelRead error", e);
        } finally {
            MDC.clear();
        }
    }
}
