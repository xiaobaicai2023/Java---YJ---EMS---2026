package com.yunpower.collect.protocols.iec104.handler;

import com.yunpower.collect.protocols.iec104.IEC104Variables;
import com.yunpower.collect.protocols.iec104.constant.IEC104Constant;
import com.yunpower.collect.protocols.iec104.message.MessageBody;
import com.yunpower.collect.protocols.iec104.utils.Decoder104;
import com.yunpower.collect.protocols.iec104.utils.IEC104Utils;
import com.yunpower.common.core.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;

import static com.yunpower.common.core.constant.Constants.CHANNEL_SN;
import static com.yunpower.common.core.constant.Constants.CHANNEL_SN_KEY;

/**
 * @title: 解码器
 * @Author: Jiajiaglam
 * @date: 2023-12-12 14:13
 * @description: 过滤器顺序（6）
 */
public class DataDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            // 获取上下文属性中的 channelSn
            MDC.put(CHANNEL_SN, ctx.channel().attr(CHANNEL_SN_KEY).get());

            byte[] data = new byte[in.readableBytes()];
            in.readBytes(data);

            String hex = ByteUtils.byteArrayToHexString(data);
            LOGGER.debug("接收到原数据：{}", hex);

            // 判断是否是注册码（以66开头）
            if (data[0] == IEC104Constant.REGISTER_HEAD_DATA) {
                String utf8Str = ByteUtils.hexToString(hex);
                MessageBody messageBody = new MessageBody();
                messageBody.setHexString(hex);
                messageBody.setUtf8String(utf8Str);
                messageBody.setIsRegisterCode(1);
                out.add(messageBody);
                return;
            }

            short send = IEC104Utils.getSend(ByteUtils.getByte(data, 2, 4));
            LOGGER.debug("解码器，接收序号：{}", send);

            IEC104Variables.setAccept(ctx, send);
            MessageBody messageBody = Decoder104.encoder(data);
            messageBody.setHexString(hex);

            // 把接收到的终端地址记录下来，key=ctx.channel().id_accept
            IEC104Variables.setTerminalAddress(ctx, send, messageBody.getTerminalAddress());

            out.add(messageBody);
        } catch (Exception ex) {
            LOGGER.error("DataDecoder error", ex);
        } finally {
            MDC.clear();
        }
    }
}
