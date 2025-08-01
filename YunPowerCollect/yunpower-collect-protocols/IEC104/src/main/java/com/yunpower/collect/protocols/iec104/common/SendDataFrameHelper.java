package com.yunpower.collect.protocols.iec104.common;

import com.yunpower.collect.protocols.iec104.common.apdu.Apdu;
import com.yunpower.collect.protocols.iec104.common.apdu.Asdu;
import com.yunpower.collect.protocols.iec104.common.asdu.TotalSummonType;
import com.yunpower.collect.protocols.iec104.common.asdu.typemodel.InformationBodyAddress;
import io.netty.channel.Channel;

/**
 * @title: 发送帧管理工具
 * @Author: Jiajiaglam
 * @date: 2023-12-15 10:07
 * @description:
 */
public class SendDataFrameHelper {
    /**
     * 发送总召唤 帧
     *
     * @param channel 通道对象
     * @param address 公共地址位
     * @param cause   发送的原因
     * @throws Exception 异常
     */
    public static void sendTotalSummonFrame(Channel channel, Integer address, Integer cause) throws Exception {
        Apdu apdu = new Apdu();
        Asdu asdu;
        TotalSummonType dataFrameType = new TotalSummonType();
        dataFrameType.setAddress(new InformationBodyAddress(0));
        dataFrameType.setValue(20);
        asdu = dataFrameType.generateBack();
        asdu.setNot(cause);
        asdu.setCommonAddress(address);
        apdu.setAsdu(asdu);
        SendAndReceiveNumUtils.sendIFrame(apdu, channel);
    }
}
