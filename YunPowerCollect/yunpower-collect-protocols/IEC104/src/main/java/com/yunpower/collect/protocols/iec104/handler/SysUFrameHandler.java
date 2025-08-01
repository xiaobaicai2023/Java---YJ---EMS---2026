package com.yunpower.collect.protocols.iec104.handler;


import com.yunpower.collect.protocols.iec104.IEC104Variables;
import com.yunpower.collect.protocols.iec104.enums.UControlEnum;
import com.yunpower.collect.protocols.iec104.future.FutureNameEnum;
import com.yunpower.collect.protocols.iec104.future.FutureUtils;
import com.yunpower.collect.protocols.iec104.task.SendTestFrameExecutor;
import com.yunpower.collect.protocols.iec104.task.TotalSummonFrameExecutor;
import com.yunpower.collect.protocols.iec104.utils.IEC104Utils;
import com.yunpower.common.core.service.ScheduledService;
import com.yunpower.common.core.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @title: 处理U帧的报文（Master）
 * @Author: Jiajiaglam
 * @date: 2023-12-12 13:39
 * @description: 过滤器顺序（3）
 */
public class SysUFrameHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysUFrameHandler.class);

    /**
     * 拦截系统消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result = (ByteBuf) msg;
        byte[] bytes = new byte[result.readableBytes()];
        result.readBytes(bytes);

        if (isSysInstruction(bytes)) {
            UControlEnum uControlEnum = IEC104Utils.getUcontrol(ByteUtils.getByte(bytes, 2, 4));
            if (uControlEnum != null) {
                LOGGER.debug("接收到U帧！");
                uInstructionHandler(ctx, result, uControlEnum);
                return;
            }
        }

        result.writeBytes(bytes);
        ctx.fireChannelRead(result);
    }

    /**
     * 判断是否是系统报文
     */
    private boolean isSysInstruction(byte[] bytes) {
        // U帧只有6字节
        return bytes.length == 6;
    }

    /**
     * 处理U帧
     */
    private void uInstructionHandler(ChannelHandlerContext ctx, ByteBuf in, UControlEnum uControlEnum) {
        in.readBytes(new byte[in.readableBytes()]);
        if (uControlEnum == UControlEnum.TEST_FR_U_YES) {
            //检查此线程是否已经启动
            ScheduledFuture<?> exist = IEC104Variables.GLOBAL_SCHEDULED_FUTURE_MAP.get(FutureNameEnum.TOTAL_SUMMON_FRAME.getValue() + ctx.channel().id().asShortText());
            if (exist == null) {
                //有的管理机只有发送总召才上传数据，有的管理机只要数据发生变化就上传
                LOGGER.info("收到测试确认指令，准备发送总召唤，每分钟一次");
                ScheduledFuture<?> totalSummonFrameFuture = ScheduledService.getInstance().scheduleWithFixedDelay(new TotalSummonFrameExecutor(ctx), 0, 1, TimeUnit.MINUTES);
                IEC104Variables.GLOBAL_SCHEDULED_FUTURE_MAP.put(FutureNameEnum.TOTAL_SUMMON_FRAME.getValue() + ctx.channel().id().asShortText(), totalSummonFrameFuture);
            } else {
                LOGGER.debug("收到测试确认指令");
            }
        } else if (uControlEnum == UControlEnum.START_DT_YES) {
            //停止发送确认帧
            FutureUtils.futureCancle(ctx, FutureNameEnum.START_FRAME_NAME.getValue());

            //检查此线程是否已经启动
            ScheduledFuture<?> exist = IEC104Variables.GLOBAL_SCHEDULED_FUTURE_MAP.get(FutureNameEnum.TEST_FRAME_NAME.getValue() + ctx.channel().id().asShortText());
            if (exist == null) {
                LOGGER.info("收到启动指令确认指令，停止发送确认帧，开始发送测试帧");
                ScheduledFuture<?> testFrameFuture = ScheduledService.getInstance().scheduleAtFixedRate(new SendTestFrameExecutor(ctx), 0, 5, TimeUnit.SECONDS);
                IEC104Variables.GLOBAL_SCHEDULED_FUTURE_MAP.put(FutureNameEnum.TEST_FRAME_NAME.getValue() + ctx.channel().id().asShortText(), testFrameFuture);
            } else {
                LOGGER.info("收到启动指令确认指令，停止发送确认帧");
            }
        } else if (uControlEnum == UControlEnum.STOP_DT_YES) {
            LOGGER.info("收到停止确认指令，停止发送测试帧");
            FutureUtils.futureCancle(ctx, FutureNameEnum.TEST_FRAME_NAME.getValue());
        } else {
            LOGGER.error("U报文无效");
        }
    }
}
