package com.yunpower.collect.protocols.iec104.utils;

import com.yunpower.collect.protocols.iec104.enums.QualifiersEnum;
import com.yunpower.collect.protocols.iec104.enums.TypeIdentifierEnum;
import com.yunpower.collect.protocols.iec104.message.MessageBody;
import com.yunpower.collect.protocols.iec104.message.MessageInfo;
import com.yunpower.common.core.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 104协议转码工具
 * @Author: Jiajiaglam
 * @date: 2023-12-12 14:04
 * @description:
 */
public class Decoder104 {

    /**
     * 将 bytes 转换成指定的数据结构
     */
    public static MessageBody encoder(byte[] bytes) {
        MessageBody messageBody = new MessageBody();
        int index = 0;
        messageBody.setStart(bytes[index++]);
        messageBody.setApuuLength(bytes[index++] & 0xFF);
        messageBody.setControl(ByteUtils.getByte(bytes, index, 4));
        index += 4;

        // 如果只有APCI 就此返回
        if (messageBody.getApuuLength() <= 4) {
            messageBody.setMessages(new ArrayList<>());
            return messageBody;
        }

        // 下面是返回ASDU的结构
        messageBody.setTypeIdentifier(TypeIdentifierEnum.getTypeIdentifierEnum(bytes[index++]));

        // 添加可变结构限定词
        IEC104Utils.setChanged(messageBody, bytes[index++]);

        messageBody.setTransferReason(ByteUtils.byteArrayToShort(ByteUtils.getByte(bytes, index, 2)));
        index += 2;
        messageBody.setTerminalAddress(IEC104Utils.getTerminalAddressShort(ByteUtils.getByte(bytes, index, 2)));
        index += 2;
        IEC104Utils.setMeaageAttribute(messageBody);
        setMessage(messageBody, bytes, index);
        return messageBody;
    }

    /**
     * 对消息进行编码
     */
    public static void setMessage(MessageBody messageBody, byte[] bytes, int index) {
        if (messageBody.isContinuous()) {
            setContinuoustMessage(messageBody, bytes, index);
        } else {
            setNoContinuoustMessage(messageBody, bytes, index);
        }
    }

    /**
     * 设置连续地址的消息
     */
    public static void setContinuoustMessage(MessageBody messageBody, byte[] bytes, int index) {
        List<MessageInfo> messages = new ArrayList<>();
        int mesageIndex = index;

        // 连续的 前三个字节是地址 
        // 如果是地址联系的只需要设置一个初始地址就可以了
        // TODO 此处不处理地址
        int messageAddress = IEC104Utils.messageAddressToInt(ByteUtils.getByte(bytes, mesageIndex, 3));
        messageBody.setMessageAddress(messageAddress);
        mesageIndex += 3;

        if (messageBody.isMessage()) {
            // 获取每个消息的长度
            int messageLength = getMessageLength(messageBody);
            if (messageLength == 0) {
                return;
            }
            int messageSize = 0;

            while (messageSize < messageBody.getMeasgLength()) {
                MessageInfo messageObj = new MessageInfo();
                messageObj.setMessageAddress(messageAddress);
                byte[] messageInfos = ByteUtils.getByte(bytes, mesageIndex, messageLength);
                mesageIndex += messageLength;
                messageObj.setMessageInfos(messageInfos);
                //对数据的值进行解析
                setMessageValue(messageBody, messageObj);

                // 判断是否有限定词
                // 0（每个信息元素后缀1个字节）
                if (messageBody.isQualifiers() && TypeIdentifierEnum.isTelemetry(messageBody.getTypeIdentifier())) {
                    messageBody.setQualifiersType(QualifiersEnum.getQualifiersEnum(messageBody.getTypeIdentifier(), bytes[mesageIndex++]));
                }

                messageSize++;
                messageAddress++;
                messages.add(messageObj);
            }
        }

        // 判断是否有限定词
        if (messageBody.isQualifiers() && !TypeIdentifierEnum.isTelemetry(messageBody.getTypeIdentifier())) {
            messageBody.setQualifiersType(QualifiersEnum.getQualifiersEnum(messageBody.getTypeIdentifier(), bytes[mesageIndex++]));
        }

        if (messageBody.isTimeScaleExit()) {
            messageBody.setTimeScale(ByteUtils.byte2Hdate(ByteUtils.getByte(bytes, mesageIndex, 7)));
        }
        messageBody.setMessages(messages);
    }


    /**
     * 设置不连续地址的消息
     */
    public static void setNoContinuoustMessage(MessageBody messageBody, byte[] bytes, int index) {
        List<MessageInfo> messages = new ArrayList<>();
        int mesageIndex = index;

        // 获取每个消息的长度
        int messageLength = getMessageLength(messageBody);
        if (messageLength == 0) {
            return;
        }
        int messageSize = 0;

        while (messageSize < messageBody.getMeasgLength()) {
            MessageInfo messageObj = new MessageInfo();

            // 消息地址
            messageObj.setMessageAddress(IEC104Utils.messageAddressToInt(ByteUtils.getByte(bytes, mesageIndex, 3)));
            mesageIndex += 3;

            if (messageBody.isMessage()) {
                // 消息集合
                byte[] messageInfos = ByteUtils.getByte(bytes, mesageIndex, messageLength);
                mesageIndex += messageLength;
                messageObj.setMessageInfos(messageInfos);
                //对数据的值进行解析
                setMessageValue(messageBody, messageObj);
            } else {
                messageObj.setMessageInfos(new byte[]{});
            }

            if (messageBody.isQualifiers() && TypeIdentifierEnum.isTelemetry(messageBody.getTypeIdentifier())) {
                // 判断是否有限定词
                // 0（每个信息元素后缀1个字节）
                messageBody.setQualifiersType(QualifiersEnum.getQualifiersEnum(messageBody.getTypeIdentifier(), bytes[mesageIndex++]));
            }

            messageSize++;
            messages.add(messageObj);
        }

        // 判断是否有限定词
        if (messageBody.isQualifiers() && !TypeIdentifierEnum.isTelemetry(messageBody.getTypeIdentifier())) {
            messageBody.setQualifiersType(QualifiersEnum.getQualifiersEnum(messageBody.getTypeIdentifier(), bytes[mesageIndex++]));
        }

        if (messageBody.isTimeScaleExit()) {
            messageBody.setTimeScale(ByteUtils.byte2Hdate(ByteUtils.getByte(bytes, mesageIndex, 7)));
        }

        messageBody.setMessages(messages);
    }

    /**
     * 根据类型对数据的值进行解析
     */
    //todo 解析数据值 目前解析遥测值不正确
    private static void setMessageValue(MessageBody messageBody, MessageInfo messageInfo) {
        switch (messageBody.getTypeIdentifier().getValue()) {
            case 0x09:
                // 遥测 测量值 归一化值 遥测
                break;
            case 0x0B:
                // 遥测 测量值  标度化值 遥测
                break;
            case 0x66:
                // 读单个参数
                break;
            case (byte) 0x84:
                //  读多个参数
                break;
            case 0x30:
                // 预置单个参数命令
                break;
            case (byte) 0x88:
                // 预置多个个参数
                break;
            case 0x0D:
                break;
            default:
        }
    }

    /**
     * 根据类型标识返回消息长度
     */
    private static int getMessageLength(MessageBody messageBody) {
        if (messageBody.getTypeIdentifier() == null) {
            return 0;
        }
        return messageBody.getTypeIdentifier().getMessageLength();
    }
}
