package com.yunpower.collect.protocols.iec104.handler;

import com.yunpower.collect.protocols.iec104.message.MessageBody;
import com.yunpower.collect.protocols.iec104.service.ExecuteService;
import com.yunpower.collect.protocols.iec104.task.TotalSummonFrameExecutor;
import com.yunpower.common.core.service.ScheduledService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @title: 自定义处理消息类
 * @Author: Jiajiaglam
 * @date: 2023-12-12 16:34
 * @description:
 */
@Service
public class DataHandlerImpl implements IDataHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHandlerImpl.class);

    /**
     * 其实这个方法没有用到
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Netty启动顺序.....4（发送总召唤指令）");
        ScheduledService.getInstance().schedule(new TotalSummonFrameExecutor(ctx), 0, TimeUnit.MINUTES);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, MessageBody messageBody, String connectKey, String dateTime) throws Exception {
        LOGGER.debug("Netty启动顺序.....5");

        ExecuteService executeService = new ExecuteService();
        // 通道注册
        if (messageBody.getIsRegisterCode() == 1) {
            executeService.doRegister(ctx, connectKey, messageBody.getUtf8String());
            return;
        }

        // 无数据
        if (messageBody.getMeasgLength() == 0) {
            return;
        }

        LOGGER.debug("Netty启动顺序.....5-1（数据）");

        // 将数据存储到数据库
        executeService.doStorage(ctx, connectKey, messageBody, dateTime);
    }
}
