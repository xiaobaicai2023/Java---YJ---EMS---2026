package com.yunpower.collect.protocols.iec104.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @title: 数组编码器
 * @Author: Jiajiaglam
 * @date: 2023-12-12 14:12
 * @description:
 */
public class BytesEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
        out.writeBytes(msg);
    }
}