package com.yunpower.collect.protocols.iec104.utils;

import com.yunpower.collect.protocols.iec104.entity.TeleSignallingInfoEntity;
import com.yunpower.collect.protocols.iec104.enums.TypeIdentifierEnum;
import com.yunpower.collect.protocols.iec104.enums.UControlEnum;
import com.yunpower.collect.protocols.iec104.message.MessageBody;
import com.yunpower.common.core.utils.ByteUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @title: IEC104工具类
 * @Author: Jiajiaglam
 * @date: 2023-12-11 16:22
 * @description:
 */
public class IEC104Utils {
    private static final int controlLength = 4;

    /**
     * I 格式 低位在前
     *
     * @param accept 接收序列号
     * @param send   发送序列号
     */
    public static byte[] getIcontrol(short accept, short send) {
        byte[] control = new byte[4];
        send = (short) (send << 1); // 向左移动一位 保证低位的 D0 是0
        control[0] = (byte) ((send));
        control[1] = (byte) ((send >> 8));
        accept = (short) (accept << 1);
        control[2] = (byte) ((accept));
        control[3] = (byte) ((accept >> 8));
        return control;
    }

    /**
     * 返回控制域中的接收序号
     */
    public static short getAccept(byte[] control) {
        int accept = 0;
        short acceptLow = (short) (control[2] & 0xff);
        short acceptHigh = (short) (control[3] & 0xff);
        accept += acceptLow;
        accept += acceptHigh << 8;
        accept = accept >> 1;
        return (short) accept;

    }

    /**
     * 返回控制域中的发送序号
     */
    public static short getSend(byte[] control) {
        int send = 0;
        short acceptLow = (short) (control[0] & 0xff);
        short acceptHigh = (short) (control[1] & 0xff);
        send += acceptLow;
        send += acceptHigh << 8;
        send = send >> 1;
        return (short) send;
    }

    /**
     * S帧格式
     */
    public static byte[] getScontrol(short accept) {
        byte[] control = new byte[4];
        short send = 1; // 向左移动一位 保证低位的 D0 是0
        control[0] = (byte) ((send));
        control[1] = (byte) ((send >> 8));
        accept = (short) (accept << 1);
        control[2] = (byte) ((accept));
        control[3] = (byte) ((accept >> 8));
        return control;
    }

    /**
     * 返回U帧
     */
    public static UControlEnum getUcontrol(byte[] control) {
        if (control.length < controlLength || control[1] != 0 || control[3] != 0 || control[2] != 0) {
            return null;
        }
        int controlInt = ByteUtils.byteArrayToInt(control);
        for (UControlEnum ucontrolEnum : UControlEnum.values()) {
            if (ucontrolEnum.getValue() == controlInt) {
                return ucontrolEnum;
            }
        }
        return null;
    }


    /**
     * 解析地址域
     *
     * @param low  第一个地址
     * @param high 第二个地址
     */
    public static String address(int low, int high) {

        String lowString = String.format("%02X", low);
        String highString = String.format("%02X", high);

        return highString + lowString;
    }

    /**
     * int转换成16进制字符串 不需要0x
     *
     * @param b 需要转换的int值
     * @return 16进制的String
     */
    public static String toHexStringNo0x(int b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase();
    }

    /**
     * 返回消息地址 其中低位在前
     */
    public static byte[] intToMessageAddress(int i) {
        byte[] result = new byte[3];
        result[0] = (byte) (i & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        return result;
    }

    /**
     * int转换成16进制字符串
     *
     * @param b 需要转换的int值
     * @return 16进制的String
     */
    public static String toHexString(int b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return "0x" + hex.toUpperCase();
    }

    /**
     * 消息地址 只有三个
     */
    public static int messageAddressToInt(byte[] bytes) {
        int value = 0;
        for (int i = 2; i >= 0; i--) {
            int shift = (2 - i) * 8;
            value += (bytes[2 - i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * 设置可以变限定词
     */
    public static void setChanged(MessageBody body, byte byteItem) {
        // 第一位是 0 则是有序的
        body.setContinuous((byteItem & 0x80) == 0 ? false : true);

        // 先将第一位数置零 然后转换成int
        body.setMeasgLength(byteItem & (byte) 0x7F);
    }

    /**
     * 返回可变限定词数组
     */
    public static byte getChangedQualifiers(MessageBody body) {
        // 将长度转换成 byte
        byte changedQualifiers = (byte) body.getMeasgLength();

        // 判断SQ 置   isContinuous false SQ = 0;否则 SQ =1 ,  同时将SQ置 设置在 可变限定词的 D7位置
        int sq = body.isContinuous() ? 0x80 : 0;
        changedQualifiers = (byte) (sq | changedQualifiers);
        return changedQualifiers;
    }

    public static void setMeaageAttribute(MessageBody body) {
        boolean isMessage = !(TypeIdentifierEnum.generalCall.equals(body.getTypeIdentifier())  //总召唤无此项
                || TypeIdentifierEnum.timeSynchronization.equals(body.getTypeIdentifier()) // 时钟同步
                || TypeIdentifierEnum.resetPprocess.equals(body.getTypeIdentifier()) // 复位进程
                || TypeIdentifierEnum.initEnd.equals(body.getTypeIdentifier()));
        body.setMessage(isMessage);

        boolean isQualifiers = !(TypeIdentifierEnum.timeSynchronization.equals(body.getTypeIdentifier())  // 时钟同步
                || TypeIdentifierEnum.onePointTeleindication.equals(body.getTypeIdentifier()) //单点摇信
                || TypeIdentifierEnum.twoPointTeleindication.equals(body.getTypeIdentifier()) // 双点摇信
                || TypeIdentifierEnum.onePointTelecontrol.equals(body.getTypeIdentifier()) // 单命令遥控
                || TypeIdentifierEnum.twoPointTelecontrol.equals(body.getTypeIdentifier())); // 双命令遥控
        body.setQualifiers(isQualifiers);
        boolean isTimeScale = TypeIdentifierEnum.timeSynchronization.equals(body.getTypeIdentifier())  // 时钟同步
                || TypeIdentifierEnum.onePointTimeTeleindication.equals(body.getTypeIdentifier()) // 摇信带时标 单点
                || TypeIdentifierEnum.twoPointTimeTeleindication.equals(body.getTypeIdentifier()); //摇信带时标 双点
        body.setTimeScaleExit(isTimeScale);
    }

    /**
     * short 转换成两个 字节后是163  00    也就是  value[1] 中才有值
     * test 在D7位置 因此 值应该和  01000000 做与运算
     * P/N 0肯定确认  1否定确认
     *
     * @return 肯定或否定确认
     */
    public static boolean isYes(byte[] values) {
        return (values[0] & 1 << 6) == 0;
    }

    /**
     * short 转换成两个 字节后是163  00     也就是  value[1] 中才有值
     * test 在D7位置 因此 值应该和 10000000 做与运算
     * tets 0 为试验  1 试验
     *
     * @return 是否试验
     */
    public static boolean isTets(byte[] values) {
        return (values[0] & 1 << 7) != 0;
    }

    /**
     * 返回具体的原因
     */
    public static short getTransferReasonShort(byte[] values) {
        byte transferReason = values[0];
        // 前两位置零
        transferReason = (byte) (transferReason & 0x3E);
        return transferReason;
    }


    public static short getTransferReasonShort(boolean isTets, boolean isYes, short transferReason) {
        int t = isTets ? 1 : 0;
        int y = isYes ? 0 : 1;
        int transferReasonInt = t << 7 | transferReason;
        transferReasonInt = y << 6 | transferReasonInt;

        return (short) (transferReasonInt << 8);
    }


    /**
     * 返回终端地址对应的byte数组 其中低位在前
     */
    public static byte[] getTerminalAddressByte(short terminalAddress) {
        byte[] b = new byte[2];
        b[1] = (byte) ((terminalAddress >> 8) & 0xff);
        b[0] = (byte) (terminalAddress & 0xff);
        return b;
    }

    /**
     * 时标CP56Time2a解析
     *
     * @param b 时标CP56Time2a（长度为7 的int数组）
     * @return 解析结果
     */
    public static String TimeScale(int b[], TeleSignallingInfoEntity teleSignallingInfoEntity) {

        String str = "";
        int year = (b[6] & 0x7F) + 2000;
        int month = b[5] & 0x0F;
        int day = b[4] & 0x1F;
        int week = (b[4] & 0xE0) / 32;
        int hour = b[3] & 0x1F;
        int minute = b[2] & 0x3F;
        int second = (b[1] << 8) + b[0];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second / 1000);
        Date date = calendar.getTime();
        teleSignallingInfoEntity.setTime(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(date));
        str += "时标CP56Time2a:" + year + "-"
                + String.format("%02d", month) + "-"
                + String.format("%02d", day) + "," + hour + ":" + minute + ":"
                + second / 1000 + "." + second % 1000;
        return str + "\n";
    }

    /**
     * 返回回终端地址 其中低位在前
     */
    public static short getTerminalAddressShort(byte[] terminalAddress) {
        short value = 0;
        value += (terminalAddress[0] & 0xFF);
        value += (terminalAddress[1] & 0xFF) << 8;
        return value;
    }

    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static int[] hexStringToIntArray(String s) {
        int len = s.length();
        int[] b = new int[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (int) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return b;
    }
}
