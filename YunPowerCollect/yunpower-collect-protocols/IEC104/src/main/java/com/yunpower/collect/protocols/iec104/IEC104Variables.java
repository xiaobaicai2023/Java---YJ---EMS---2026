package com.yunpower.collect.protocols.iec104;

import com.yunpower.collect.protocols.iec104.constant.IEC104Constant;
import com.yunpower.mq.publisher.service.PublisherService;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 全局变量
 * */
public class IEC104Variables {

    //存储所有设备地址：从设备中获取，格式==> ctx.channel().id_accept：公共地址
    private static final ConcurrentHashMap<String, Short> PUBLIC_TERMINAL_ADDRESS = new ConcurrentHashMap<>();

    //存储所有接收序号，格式==> ctx.channel().id：accept
    private static final ConcurrentHashMap<String, Short> CHANNEL_READ_ACCEPT = new ConcurrentHashMap<>();

    //存储所有发送序号，格式==> ctx.channel().id：send
    private static final ConcurrentHashMap<String, Short> CHANNEL_READ_SEND = new ConcurrentHashMap<>();

    //存储所有线程：目的是为了取消某个线程，格式==> ctx.channel().id：future
    public static Map<String, ScheduledFuture<?>> GLOBAL_SCHEDULED_FUTURE_MAP = new HashMap<>();

    /**
     * MQ 生产者
     * */
    public static PublisherService publisherService;

    /**
     * 终端地址
     * 默认值：1
     */
    public static short getTerminalAddress(ChannelHandlerContext ctx, short accept) {
        Short terminalAddress = PUBLIC_TERMINAL_ADDRESS.get(ctx.channel().id().asShortText() + "_" + accept);
        return terminalAddress == null ? 1 : terminalAddress;
    }

    public static void setTerminalAddress(ChannelHandlerContext ctx, short accept, short terminalAddress) {
        PUBLIC_TERMINAL_ADDRESS.put(ctx.channel().id().asShortText() + "_" + accept, terminalAddress);
    }

    /**
     * 接收序号 锁
     */
    private static final Boolean acceptLock = true;

    /**
     * 接收序号
     * 默认值：0
     */
    public static Short getAccept(ChannelHandlerContext ctx) {
        Short accept = CHANNEL_READ_ACCEPT.get(ctx.channel().id().asShortText());
        return accept == null ? 0 : accept;
    }

    public static void setAccept(ChannelHandlerContext ctx, Short accept) {
        synchronized (acceptLock) {
            CHANNEL_READ_ACCEPT.put(ctx.channel().id().asShortText(), accept);
        }
    }

    /**
     * 发送序号 锁
     */
    private static final Boolean sendLock = true;

    /**
     * 发送序号
     * 默认值：0
     */
    public static Short getSend(ChannelHandlerContext ctx) {
        synchronized (sendLock) {
            Short send = CHANNEL_READ_SEND.get(ctx.channel().id().asShortText());
            send = (send == null ? 0 : send);
            send++;
            Short saveSend = send;

            if (send > IEC104Constant.SEND_MAX) {
                saveSend = IEC104Constant.SEND_MIN;
            }
            CHANNEL_READ_SEND.put(ctx.channel().id().asShortText(), saveSend);
            return send;
        }
    }
}
