package com.yunpower.collect.protocols.iec104.handler;

import com.yunpower.collect.protocols.iec104.constant.IEC104Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @title: 解决 TCP 拆包和沾包的问题
 * @Author: Jiajiaglam
 * @date: 2023-12-11 16:09
 * @description: 过滤器顺序（1）
 */
public class Unpack104Handler extends ByteToMessageDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Unpack104Handler.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            //测试时打印报文
            //byte[] print = new byte[in.readableBytes()];
            //in.readBytes(print);
            //LOGGER.info("拆包沾包报文：" + ByteUtils.byteArrayToHexString(print));

            // 记录包头开始的index
            int beginReader = 0;

            // 新包长度
            int newDataLength = 0;

            while (in.isReadable()) {
                // 获取包头开始的index
                beginReader = in.readerIndex();

                // 记录一个标志用于重置
                in.markReaderIndex();

                // 读到了协议的开始标志，结束while循环
                if (in.readByte() == IEC104Constant.HEAD_DATA) {
                    // 标记当前包为新包
                    // 读取包长度
                    byte newDataLengthByte = in.readByte();
                    newDataLength = newDataLengthByte & 0xFF;
                    break;
                }
            }

            // 有可能是注册码或系统消息
            if (newDataLength == 0) {
                LOGGER.info("非104报文！（1）");
                // 从头读取信息
                in.readerIndex(0);
                // 将refCnt加1
                in.retain();
                // 传递消息至下一个处理器
                ctx.fireChannelRead(in);
                return;
            }

            // 数据帧长度不够
            if (in.readableBytes() < newDataLength) {
                in.readerIndex(beginReader);
                return;
            }

            newDataLength = newDataLength + 2;

            // 恢复指针
            in.readerIndex(beginReader);
            ByteBuf data = in.readBytes(newDataLength);
            out.add(data);

        } catch (Exception ex) {
            LOGGER.error("拆包和沾包异常：" + ex.getMessage());
            ex.printStackTrace();

            // 说明当前包有问题，不往下进行
            // 直接传递给下一个处理器
            //in.readerIndex(0);
            //in.retain();
            //ctx.fireChannelRead(in);
        }
    }
}