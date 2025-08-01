package com.yunpower.collect.protocols.iec104.handler;

import com.yunpower.collect.protocols.iec104.constant.IEC104Constant;
import com.yunpower.collect.protocols.iec104.utils.IEC104Utils;
import com.yunpower.common.core.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 处理S帧的问题
 * @Author: Jiajiaglam
 * @date: 2023-12-12 14:10
 * @description: 过滤器顺序（4）
 */
public class SysSFrameHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysSFrameHandler.class);

    /**
     * 拦截系统消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result = (ByteBuf) msg;
        byte[] bytes = new byte[result.readableBytes()];
        result.readBytes(bytes);

        if (isSysInstruction(bytes)) {
            LOGGER.debug("接收到S帧，序号：{}", IEC104Utils.getAccept(ByteUtils.getByte(bytes, 2, 4)));
            ReferenceCountUtil.release(result);
            return;
        }

        result.writeBytes(bytes);
        ctx.fireChannelRead(result);
    }

    /**
     * 判断是否是系统报文
     */
    private boolean isSysInstruction(byte[] bytes) {
        // U帧只有6字节
        if (bytes.length != IEC104Constant.APCI_LENGTH) {
            return false;
        }

        /**
         * 控制单元ACPI
         * 启动字符（1字节）  APDU长度（1字节）   控制域1（1字节）   控制域2（1字节）   控制域3（1字节）   控制域4（1字节）
         *
         * 应用规约数据单元APDU
         * APCI（就是上面的）  类型标识（1字节）    可变结构限定词（1字节）     传送原因（2字节）    公共地址（2字节）    信息体（最长243字节）
         *
         * S帧：控制域1 两个 bit位是：1 0
         * I帧：控制域1 第1个bit位是：0
         * U帧：控制域1 两个 bit位是：1 1
         */

        // 判断S帧的方法
        if (bytes[IEC104Constant.ACCEPT_LOW_INDEX] == 1 && bytes[IEC104Constant.ACCEPT_HIGH_INDEX] == 0) {
            return true;
        } else {
            return false;
        }
    }
}
