package com.yunpower.collect.protocols.iec104.handler;

import com.yunpower.collect.protocols.iec104.constant.IEC104Constant;
import com.yunpower.common.core.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 检查104报文
 * @Author: Jiajiaglam
 * @date: 2023-12-11 16:15
 * @description: 过滤器顺序（2）
 */
public class CheckIEC104Handler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckIEC104Handler.class);

    /**
     * 拦截系统消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result = (ByteBuf) msg;
        byte[] data = new byte[result.readableBytes()];
        result.readBytes(data);
        LOGGER.info("接收到的报文：{}", ByteUtils.byteArrayToHexString(data));
        // 【注册码】报文，以66开头，偶数位
        if (data[0] == IEC104Constant.REGISTER_HEAD_DATA) {
            // 拦截注册码
            LOGGER.info("非104报文，注册码！（2）");
            result.writeBytes(data);
            ctx.fireChannelRead(msg);
            return;
        }

        // 拦截系统消息
        if (data.length < IEC104Constant.APCI_LENGTH || data[0] != IEC104Constant.HEAD_DATA) {
            LOGGER.error("报文无效");
            ReferenceCountUtil.release(result);
        } else {
            result.writeBytes(data);
            ctx.fireChannelRead(msg);
        }
    }
}
