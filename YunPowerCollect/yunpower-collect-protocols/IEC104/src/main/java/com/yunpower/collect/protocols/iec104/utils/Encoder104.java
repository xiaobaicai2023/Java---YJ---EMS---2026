package com.yunpower.collect.protocols.iec104.utils;

import com.yunpower.collect.protocols.iec104.enums.TypeIdentifierEnum;
import com.yunpower.collect.protocols.iec104.message.MessageBody;
import com.yunpower.collect.protocols.iec104.message.MessageInfo;
import com.yunpower.common.core.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @title: 编码
 * @Author: Jiajiaglam
 * @date: 2023-12-12 14:01
 * @description:
 */
public class Encoder104 {

    public static byte[] encoder(MessageBody messageBody) throws IOException {
        IEC104Utils.setMeaageAttribute(messageBody);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(messageBody.getStart());
        byte[] apduBytes = getApduBytes(messageBody);
        int messageLen = apduBytes.length;
        messageBody.setApuuLength(messageLen);
        bytes.write((byte) messageLen);
        bytes.write(apduBytes);
        return bytes.toByteArray();
    }

    private static byte[] getApduBytes(MessageBody messageBody) throws IOException {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();

        // 控制域
        bOutput.write(messageBody.getControl());

        // U帧或者S帧
        if (messageBody.getTypeIdentifier() == null) {
            return bOutput.toByteArray();
        }

        // 类型标识
        bOutput.write((byte) messageBody.getTypeIdentifier().getValue());

        // 可变结构限定词
        bOutput.write(IEC104Utils.getChangedQualifiers(messageBody));

        // 传输原因
        bOutput.write(ByteUtils.shortToByteArray(messageBody.getTransferReason()));

        // 终端地址
        bOutput.write((IEC104Utils.getTerminalAddressByte(messageBody.getTerminalAddress())));

        //如果是是连续的则数据地址 只需要在开头写以后的数据单元就不需要再写了
        if (messageBody.isContinuous()) {
            bOutput.write(IEC104Utils.intToMessageAddress(messageBody.getMessageAddress()));
            // 地址只取三个字节
            if (messageBody.isMessage()) {
                for (MessageInfo ruleDetail104Message : messageBody.getMessages()) {
                    bOutput.write(ruleDetail104Message.getMessageInfos());
                    if (messageBody.isQualifiers() && TypeIdentifierEnum.isTelemetry(messageBody.getTypeIdentifier())) {
                        // 0（每个信息元素后缀1个字节）
                        bOutput.write(messageBody.getQualifiersType().getValue());
                    }
                }
            }

            if (messageBody.isQualifiers() && !TypeIdentifierEnum.isTelemetry(messageBody.getTypeIdentifier())) {
                bOutput.write(messageBody.getQualifiersType().getValue());
            }

            if (messageBody.isTimeScaleExit()) {
                bOutput.write(ByteUtils.date2Hbyte(messageBody.getTimeScale()));
            }
        } else {
            for (MessageInfo ruleDetail104Message : messageBody.getMessages()) {
                bOutput.write(IEC104Utils.intToMessageAddress(ruleDetail104Message.getMessageAddress()));
                if (messageBody.isMessage()) {
                    bOutput.write(ruleDetail104Message.getMessageInfos());
                }
                if (messageBody.isQualifiers() && TypeIdentifierEnum.isTelemetry(messageBody.getTypeIdentifier())) {
                    // 0（每个信息元素后缀1个字节）
                    bOutput.write(messageBody.getQualifiersType().getValue());
                }
            }

            if (messageBody.isQualifiers() && !TypeIdentifierEnum.isTelemetry(messageBody.getTypeIdentifier())) {
                bOutput.write(messageBody.getQualifiersType().getValue());
            }

            if (messageBody.isTimeScaleExit()) {
                bOutput.write(ByteUtils.date2Hbyte(messageBody.getTimeScale()));
            }
        }
        return bOutput.toByteArray();
    }
}
