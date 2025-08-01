package com.yunpower.collect.protocols.iec104.handler;

import com.yunpower.collect.protocols.iec104.IEC104Variables;
import com.yunpower.collect.protocols.iec104.message.MessageBody;
import com.yunpower.collect.protocols.iec104.utils.Encoder104;
import com.yunpower.collect.protocols.iec104.utils.IEC104Utils;
import com.yunpower.common.core.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 编码器
 * @Author: Jiajiaglam
 * @date: 2023-12-12 14:14
 * @description:
 */
public class DataEncoder extends MessageToByteEncoder<MessageBody> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageBody messageBody, ByteBuf out) throws Exception {
        try {
            byte[] bytes = Encoder104.encoder(messageBody);
            short accept = IEC104Variables.getAccept(ctx);
            short send = IEC104Variables.getSend(ctx);

            LOGGER.debug("编码器，接收序号：{}<->发送序号：{}", accept, send);

            //终端地址应该是接收到的，然后再返回才对。因为一个通道可能有两个设备，不能以通道区分
            short terminalAddress = IEC104Variables.getTerminalAddress(ctx, accept);

            // 替换终端地址 发送序号和接收序号
            byte[] terminalAddressBytes = IEC104Utils.getTerminalAddressByte(terminalAddress);
            byte[] icontrol = IEC104Utils.getIcontrol(accept, send);

            System.arraycopy(icontrol, 0, bytes, 2, icontrol.length);

            bytes[10] = terminalAddressBytes[0];
            bytes[11] = terminalAddressBytes[1];
            LOGGER.debug("解码：{}", ByteUtils.byteArrayToHexString(bytes));
            out.writeBytes(bytes);

        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}
