package com.yunpower.collect.protocols.iec104.netty;

import cn.hutool.core.date.DateUtil;
import com.yunpower.collect.protocols.iec104.IEC104Variables;
import com.yunpower.collect.protocols.iec104.future.FutureUtils;
import com.yunpower.collect.protocols.iec104.handler.IDataHandler;
import com.yunpower.collect.protocols.iec104.message.MessageBody;
import com.yunpower.collect.protocols.iec104.task.DataHandlerExecutor;
import com.yunpower.collect.storage.StorageVariables;
import com.yunpower.collect.storage.domain.CommunicationChannel;
import com.yunpower.common.core.constant.Constants;
import com.yunpower.common.core.constant.RabbitConstants;
import com.yunpower.common.core.entity.amqp.DeviceStateMessage;
import com.yunpower.common.core.service.ScheduledService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.date.DatePattern.PURE_DATETIME_PATTERN;
import static com.yunpower.common.core.constant.Constants.CHANNEL_SN;
import static com.yunpower.common.core.constant.Constants.CHANNEL_SN_KEY;

/**
 * @title: 通道入站处理程序
 * @Author: Jiajiaglam
 * @date: 2023-07-15 8:18
 * @description:
 */
public class IEC104SocketChannelHandler extends SimpleChannelInboundHandler<MessageBody> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IEC104SocketChannelHandler.class);

    private IDataHandler dataHandler;

    //用于保存客户端的通道组
    private static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public IEC104SocketChannelHandler(IDataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * 准备就绪
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 获取上下文属性中的 channelSn
        MDC.put(CHANNEL_SN, ctx.channel().attr(CHANNEL_SN_KEY).get());

        LOGGER.info("--------------active----------------");
        LOGGER.info("A客户端：{}", ctx.channel().id().asShortText());

        String ipAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        String connectKey = ipAddress + ":" + port;
        LOGGER.info("A远程信息服务器：{}", connectKey);

        String localIpAddress = ((InetSocketAddress) ctx.channel().localAddress()).getHostString();
        int localPort = ((InetSocketAddress) ctx.channel().localAddress()).getPort();
        LOGGER.info("A本地信息服务器：{}", localIpAddress + ":" + localPort);

        LOGGER.info("Netty启动顺序.....3");
        MDC.clear();
    }

    /**
     * 读取客户端发送来的消息
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, MessageBody msg) throws Exception {
        // 获取上下文属性中的 channelSn
        MDC.put(CHANNEL_SN, ctx.channel().attr(CHANNEL_SN_KEY).get());

        LOGGER.debug("---------------read---------------");
        LOGGER.debug("R客户端：{}", ctx.channel().id().asShortText());

        String ipAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        String connectKey = ipAddress + ":" + port;
        LOGGER.debug("R远程信息服务器：{}", connectKey);
        LOGGER.debug("接收到数据：{}", msg.getHexString());
        // *** 特别注意，这里要多线程处理接收到信息，防止消息阻塞 Start ***

        // 处理接收到的数据
        // 当前日期，格式（YYYYMMDDHHMMSS）
        String dateTime = DateUtil.format(new Date(), PURE_DATETIME_PATTERN);
        ScheduledService.getInstance().schedule(new DataHandlerExecutor(ctx, dataHandler, msg, connectKey, dateTime, ctx.channel().attr(CHANNEL_SN_KEY).get()), 0, TimeUnit.SECONDS);
        // *** 特别注意，这里要多线程处理接收到信息，防止消息阻塞 End ***
        // 数据已处理完，无需在流水线继续传递
        //super.channelRead(ctx, msg);

        //处理完成后清除MDC
        MDC.clear();
    }

    /**
     * 接入客户端
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("新客户端连接：{}", ctx.channel().id().asShortText());
        clients.add(ctx.channel());
    }

    /**
     * 断开客户端
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String ipAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        LOGGER.info("客户端连接断开：{}", ctx.channel().id().asShortText());
        clients.remove(ctx.channel());
        //取消所有正在执行的线程
        FutureUtils.cancleAllFuture(ctx);

        String connectKey = ipAddress + ":" + port;
        CommunicationChannel channel = StorageVariables.REGISTER_LIST.get(connectKey);
        if(channel != null){
            //记录通道状态-下线
            DeviceStateMessage message = DeviceStateMessage.builder()
                    .channelSn(channel.getChannelSn())
                    .deviceSn(channel.getChannelSn())
                    .state(0)
                    .type(Constants.CHANNEL_SUFFIX)
                    .changeTime(DateUtil.now())
                    .build();
            IEC104Variables.publisherService.sendMessage(RabbitConstants.STATECHANGE_CONSUMER_KEY + channel.getId() % 4, message);
        }
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.info("客户端异常：{}", cause.getMessage(), cause);
        clients.remove(ctx.channel());
        //取消所有正在执行的线程
        FutureUtils.cancleAllFuture(ctx);
        //关闭通道
        ctx.channel().close();
    }
}
