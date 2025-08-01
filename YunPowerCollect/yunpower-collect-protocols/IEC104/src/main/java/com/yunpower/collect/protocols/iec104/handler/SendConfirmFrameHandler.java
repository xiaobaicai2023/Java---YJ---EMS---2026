package com.yunpower.collect.protocols.iec104.handler;

import com.yunpower.collect.protocols.iec104.constant.IEC104Constant;
import com.yunpower.collect.protocols.iec104.message.MessageBody;
import com.yunpower.collect.protocols.iec104.utils.Encoder104;
import com.yunpower.collect.protocols.iec104.utils.IEC104Utils;
import com.yunpower.common.core.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 发送确认帧
 * @Author: Jiajiaglam
 * @date: 2023-12-26 14:22
 * @description: 过滤器顺序（5）
 */
public class SendConfirmFrameHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendConfirmFrameHandler.class);

    /**
     * 拦截系统消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf result = (ByteBuf) msg;
            byte[] bytes = new byte[result.readableBytes()];
            result.readBytes(bytes);

            //发送确认帧
            if (bytes.length > IEC104Constant.APCI_LENGTH && bytes[0] == IEC104Constant.HEAD_DATA) {
                sInstructionHandler(ctx, bytes);
            }
            result.writeBytes(bytes);
            ctx.fireChannelRead(result);

        } catch (Exception ex) {
            LOGGER.error("发送确认帧异常：" + ex.getMessage());
            ex.printStackTrace();

            //获取传输原因
            //short transfer = ByteUtils.byteArrayToShort(ByteUtils.reByte(ByteUtils.getByte(bytes, 8, 2)));
            //LOGGER.info("传输原因：{}", transfer);
        }
    }

    /**
     * 发送确认帧
     */
    private void sInstructionHandler(ChannelHandlerContext ctx, byte[] bytes) {
        try {
            short send = IEC104Utils.getSend(ByteUtils.getByte(bytes, 2, 4));
            byte[] control = IEC104Utils.getScontrol(send);
            MessageBody messageBody = new MessageBody(control);
            byte[] cmd = Encoder104.encoder(messageBody);
            ctx.channel().writeAndFlush(cmd);

            LOGGER.debug("Netty启动顺序.....3-2（S帧，确认帧）");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
