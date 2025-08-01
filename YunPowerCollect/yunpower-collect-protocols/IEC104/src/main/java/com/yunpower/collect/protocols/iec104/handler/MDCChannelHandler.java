package com.yunpower.collect.protocols.iec104.handler;

import com.yunpower.collect.storage.StorageVariables;
import com.yunpower.collect.storage.domain.CommunicationChannel;
import com.yunpower.common.core.constant.Constants;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.InetSocketAddress;

import static com.yunpower.common.core.constant.Constants.CHANNEL_SN;
import static com.yunpower.common.core.constant.Constants.CHANNEL_SN_KEY;

public class MDCChannelHandler extends ChannelDuplexHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckIEC104Handler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String ipAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        String connectKey = ipAddress + ":" + port;
        CommunicationChannel channel = StorageVariables.REGISTER_LIST.get(connectKey);

        String channelSn = channel == null ? Constants.CHANNEL_SN_DEFAULT : channel.getChannelSn();
        MDC.put(CHANNEL_SN, channelSn);
        ctx.channel().attr(CHANNEL_SN_KEY).set(channelSn);
        LOGGER.info("MDCChannelHandler-数据上报开始写入 channelSn {}", channelSn);

        //ByteBuf result = (ByteBuf) msg;
        //byte[] bytes = new byte[result.readableBytes()];
        //LOGGER.info("MDCChannelHandler-接收到的报文：{}", ByteUtils.byteArrayToHexString(bytes));

        // 处理入站事件
        try {
            super.channelRead(ctx, msg);
        } finally {
            MDC.clear();
        }
    }
}
