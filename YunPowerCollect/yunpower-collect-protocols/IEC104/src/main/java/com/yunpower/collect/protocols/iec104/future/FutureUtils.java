package com.yunpower.collect.protocols.iec104.future;

import com.yunpower.collect.protocols.iec104.IEC104Variables;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @title: 线程工具类
 * @Author: Jiajiaglam
 * @date: 2023-12-26 13:54
 * @description:
 */
public class FutureUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FutureUtils.class);

    /**
     * 线程等待
     */
    public static void futureWait(ChannelHandlerContext ctx, String name) {
        try {
            ScheduledFuture<?> future = IEC104Variables.GLOBAL_SCHEDULED_FUTURE_MAP.get(name + ctx.channel().id().asShortText());
            if (future == null) {
                return;
            }
            future.wait();
            LOGGER.info("线程{}进入等待！", name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程唤醒
     */
    public static void futureNotify(ChannelHandlerContext ctx, String name) {
        try {
            ScheduledFuture<?> future = IEC104Variables.GLOBAL_SCHEDULED_FUTURE_MAP.get(name + ctx.channel().id().asShortText());
            if (future == null) {
                return;
            }
            future.notify();
            LOGGER.info("线程{}已唤醒！", name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程取消
     */
    public static void futureCancle(ChannelHandlerContext ctx, String name) {
        try {
            ScheduledFuture<?> future = IEC104Variables.GLOBAL_SCHEDULED_FUTURE_MAP.get(name + ctx.channel().id().asShortText());
            if (future == null) {
                return;
            }
            future.cancel(true);
            LOGGER.info("线程{}已取消！", name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消所有线程
     */
    public static void cancleAllFuture(ChannelHandlerContext ctx) {
        List<String> futureNameList = FutureNameEnum.getEnumsList();
        for (String name : futureNameList) {
            futureCancle(ctx, name);
        }
    }
}
