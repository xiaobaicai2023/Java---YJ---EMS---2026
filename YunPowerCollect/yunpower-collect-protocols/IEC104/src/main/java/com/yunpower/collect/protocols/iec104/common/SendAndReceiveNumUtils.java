package com.yunpower.collect.protocols.iec104.common;

import com.yunpower.collect.protocols.iec104.common.apdu.Apdu;
import com.yunpower.collect.protocols.iec104.container.IEC104Link;
import com.yunpower.collect.protocols.iec104.container.LinkContainer;
import com.yunpower.common.core.utils.DataConvertor;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用以处理接收和发送序列号的处理类
 */
public class SendAndReceiveNumUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendAndReceiveNumUtils.class);

    private static final int INTERVAL = 5;

    /**
     * 为发送的i帧组装接收和发送序号
     *
     * @param apdu      apdu
     * @param channelId channel id
     */
    public static void setSendAndReceiveNum(Apdu apdu, ChannelId channelId) {
        IEC104Link link = LinkContainer.getInstance().getLink(channelId);
        int send = link.getISend();
        int receive = link.getIReceive();
        apdu.setSendSeqNum(send++);
        apdu.setReceiveSeqNum(receive);
        link.setISend(send);
        LinkContainer.getInstance().getLinks().put(channelId, link);
    }

    /**
     * 组装i帧的发送和接收序号后发出
     *
     * @param apdu    apdu
     * @param channel channel
     * @throws Exception exception
     */
    public static void sendIFrame(Apdu apdu, Channel channel) throws Exception {
        setSendAndReceiveNum(apdu, channel.id());
        byte[] bb = apdu.encode();
        LOGGER.debug("向104对端发出数据帧：" + DataConvertor.Byte2String(bb));
        channel.writeAndFlush(Unpooled.copiedBuffer(bb));
    }

    /**
     * 接收到i帧，处理接收和发送序号
     *
     * @param apdu      apdu
     * @param channelId channel id
     */
    public static void receiveIFrame(Apdu apdu, ChannelId channelId) {
        IEC104Link link = LinkContainer.getInstance().getLink(channelId);
        int send = link.getISend();
        int receive = link.getIReceive();
        int send1 = apdu.getSendSeqNum();
        int receive1 = apdu.getReceiveSeqNum();
        link.setLinkState(IEC104Link.LinkState.NORMAL);

        //我方丢失通道 对方放出的i帧
        if (receive < send1) {
            apdu.loseReceive();
            link.setLinkState(IEC104Link.LinkState.LOSEREC);
        }

        //通道对方丢失 我方发出的i帧
        if (send < receive1) {
            apdu.loseSend();
            link.setLinkState(IEC104Link.LinkState.LOSESEND);
        }
        link.setIReceive(++send1);
        link.setISend(receive1);
        sendSFrame(link);
        LinkContainer.getInstance().getLinks().put(channelId, link);
    }

    /**
     * 向通道内发送s帧
     *
     * @param link link
     */
    public static void sendSFrame(IEC104Link link) {
        if (link.getIReceive() != 0 && link.getIReceive() % INTERVAL == 0) {
            Apdu apdu1 = new Apdu();
            apdu1.setReceiveSeqNum(link.getIReceive());
            apdu1.setApciType(Apdu.ApciType.S_FORMAT);
            try {
                byte[] bs = apdu1.encode();
                LOGGER.debug("发送s帧：" + DataConvertor.Byte2String(bs));
                link.getChannel().writeAndFlush(Unpooled.copiedBuffer(bs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
