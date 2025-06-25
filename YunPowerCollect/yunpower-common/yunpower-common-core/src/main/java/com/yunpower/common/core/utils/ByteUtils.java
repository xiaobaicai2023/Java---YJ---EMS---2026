package com.yunpower.common.core.utils;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

/**
 * @title: byte 工具类
 * @Author: Jiajiaglam
 * @date: 2023-12-11 16:17
 * @description:
 */
public class ByteUtils {
    /**
     * int 转换成 byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 根据个数解析double[]
     * 取 8 * num 的有效数据
     */
    public static double[] byteToDoubles(byte[] bytes, int num) {
        double[] doubles = new double[num];
        for (int i = 0; i < num; i++) {
            byte[] date = byteToByte(bytes, i * 8, 8);
            double value = ByteToDouble(date);
            doubles[i] = value;
        }
        return doubles;
    }

    public static byte[] double2Bytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((data & 0xff000000) >> 24);
        bytes[1] = (byte) ((data & 0xff0000) >> 16);
        bytes[2] = (byte) ((data & 0xff00) >> 8);
        bytes[3] = (byte) (data & 0xff);
        return bytes;
    }

    /**
     * 8字节bytes数组转为double
     */
    public static double ByteToDouble(byte[] bytes) {
        long doubleValue = byteToLong(bytes);
        return Double.longBitsToDouble(doubleValue);
    }

    /**
     * 8字节bytes数组转为long
     */
    public static long byteToLong(byte[] bytes1) {
        //高低位转换 看协议是否需要
        byte[] bytes = reByte(bytes1);
        byte[] b = new byte[8];
        int i = b.length - 1;
        int j = bytes.length - 1;
        for (; i >= 0; i--, j--) {
            if (j >= 0) {
                b[i] = bytes[j];
            } else {
                b[i] = 0;
            }
        }
        long v0 = (long) (b[0] & 0xff) << 56;
        long v1 = (long) (b[1] & 0xff) << 48;
        long v2 = (long) (b[2] & 0xff) << 40;
        long v3 = (long) (b[3] & 0xff) << 32;
        long v4 = (long) (b[4] & 0xff) << 24;
        long v5 = (long) (b[5] & 0xff) << 16;
        long v6 = (long) (b[6] & 0xff) << 8;
        long v7 = (long) (b[7] & 0xff);
        return v0 + v1 + v2 + v3 + v4 + v5 + v6 + v7;
    }

    /**
     * 大小端 高低位转换
     */
    public static byte[] reByte(byte[] bytes) {
        int n = bytes.length;
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = bytes[n - 1 - i];
        }
        return b;
    }

    /**
     * short 转换成 byte[]
     */
    public static byte[] shortToByteArray(short val) {
        byte[] b = new byte[2];
        b[0] = (byte) ((val >> 8) & 0xff);
        b[1] = (byte) (val & 0xff);
        return b;
    }

    /**
     * byte[] 转换成 int
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * byte[] 转换成short
     */
    public static short byteArrayToShort(byte[] bytes) {
        short value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (1 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * 日期转换成 CP56Time2a
     */
    public static byte[] date2Hbyte(Date date) {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 毫秒需要转换成两个字节其中 低位在前高位在后
        // 先转换成short
        int millisecond = calendar.get(Calendar.SECOND) * 1000 + calendar.get(Calendar.MILLISECOND);

        // 默认的高位在前
        byte[] millisecondByte = intToByteArray(millisecond);
        bOutput.write((byte) millisecondByte[3]);
        bOutput.write((byte) millisecondByte[2]);

        // 分钟 只占6个比特位 需要把前两位置为零
        bOutput.write((byte) calendar.get(Calendar.MINUTE));

        // 小时需要把前三位置零
        bOutput.write((byte) calendar.get(Calendar.HOUR_OF_DAY));

        // 星期日的时候 week 是0
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SUNDAY) {
            week = 7;
        } else {
            week--;
        }

        // 前三个字节是 星期 因此需要将星期向左移5位  后五个字节是日期  需要将两个数字相加 相加之前需要先将前三位置零
        bOutput.write((byte) (week << 5) + (calendar.get(Calendar.DAY_OF_MONTH)));

        // 前四字节置零
        bOutput.write((byte) ((byte) calendar.get(Calendar.MONTH) + 1));
        bOutput.write((byte) (calendar.get(Calendar.YEAR) - 2000));
        return bOutput.toByteArray();
    }

    /**
     * CP56Time2a转换成时间
     */
    public static Date byte2Hdate(byte[] dataByte) {
        int year = (dataByte[6] & 0x7F) + 2000;
        int month = dataByte[5] & 0x0F;
        int day = dataByte[4] & 0x1F;
        int hour = dataByte[3] & 0x1F;
        int minute = dataByte[2] & 0x3F;
        int second = dataByte[1] > 0 ? dataByte[1] : (int) (dataByte[1] & 0xff);
        int millisecond = dataByte[0] > 0 ? dataByte[0] : (int) (dataByte[0] & 0xff);
        millisecond = (second << 8) + millisecond;
        second = millisecond / 1000;
        millisecond = millisecond % 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }

    public static String byteArrayToHexString(byte[] array) {
        return byteArray2HexString(array, Integer.MAX_VALUE, false);
    }

    public static String byteArray2HexString(byte[] arrBytes, int count, boolean blank) {
        String ret = "";
        if (arrBytes == null || arrBytes.length < 1) {
            return ret;
        }
        if (count > arrBytes.length) {
            count = arrBytes.length;
        }
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            ret = Integer.toHexString(arrBytes[i] & 0xFF).toUpperCase();
            if (ret.length() == 1) {
                builder.append("0").append(ret);
            } else {
                builder.append(ret);
            }
            if (blank) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param origin 待转化符串
     * @return 字符串
     */
    public static String hexToString(String origin) {
        if (origin == null || origin.equals("")) {
            return null;
        }

        origin = origin.replace(" ", "");
        byte[] baKeyword = new byte[origin.length() / 2];

        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(origin.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            origin = new String(baKeyword, StandardCharsets.UTF_8);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return origin;
    }

    /**
     * 返回指定位置的数组
     */
    public static byte[] getByte(byte[] bytes, int start, int length) {
        byte[] ruleByte = new byte[length];
        int index = 0;
        while (index < length) {
            ruleByte[index++] = bytes[start++];
        }
        return ruleByte;
    }

    /**
     * 浮点转换为字节
     */
    public static byte[] float2byte(float f) {
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;

        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];

        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;

        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }

    /**
     * 十六进制字符串转换成byte数组
     */
    public static byte[] hexStringToBytes(String hexStr) {
        hexStr = hexStr.replaceAll(" ", "");
        hexStr = hexStr.toUpperCase();
        int len = (hexStr.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * byte ->int - >double  (int *0.01)
     */
    public static double[] byteToIntToDouble(byte[] bytes) {
        int[] ints = byteToInt2s(bytes);
        return intToDouble(ints);
    }

    public static int[] byteToInt2s(byte[] bytes) {
        int num = bytes.length / 2;
        return byteToInt2S(bytes, num);
    }

    public static double[] intToDouble(int[] ints) {
        int length = ints.length;
        double[] doubles = new double[length];
        for (int i = 0; i < length; i++) {
            doubles[i] = ints[i] * 0.01;
        }
        return doubles;
    }

    /**
     * 2字节bytes数组转换为无符号短整型
     */
    public static int byteToInt2(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length];
        if (bytes.length == 2) {
            newBytes[1] = bytes[0];
            newBytes[0] = bytes[1];
        } else {
            newBytes = bytes;
        }
        return Unpooled.wrappedBuffer(newBytes).readUnsignedShort();
    }

    public static int[] byteToInt2S(byte[] bytes, int num) {
        int[] ints = new int[num];
        for (int i = 0; i < num; i++) {
            byte[] date = byteToByte(bytes, i * 2, 2);
            int value = byteToInt2(date);
            ints[i] = value;
        }
        return ints;
    }

    /**
     * 2字节bytes数组转为short
     */
    public static short byteToShort(byte[] bytes) {
        return (short) (((bytes[1] << 8) | bytes[0] & 0xff));
    }

    /**
     * 截取 字节数组
     *
     * @param bytes  源
     * @param start  起始位置
     * @param length 长度
     */
    public static byte[] byteToByte(byte[] bytes, int start, int length) {
        byte[] nByte = new byte[length];
        System.arraycopy(bytes, start, nByte, 0, length);
        return nByte;
    }
}
