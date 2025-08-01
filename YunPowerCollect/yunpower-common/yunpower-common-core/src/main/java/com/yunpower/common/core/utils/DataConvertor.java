package com.yunpower.common.core.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.Formatter;

/**
 * 字符串的处理工具
 */
public class DataConvertor {

    /**
     * 字节数字转16进制字符串
     */
    public static String Byte2String(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }
        Formatter f = new Formatter();
        for (int i = 0; i < ba.length; ++i) {
            f.format("%02x ", ba[i]);
        }
        return f.toString();
    }

    /**
     * Byte append string
     */
    public static String byteAppend(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = bytes.length - 1; i >= 0; i--) {
            stringBuffer.append(String.format("%02d", bytes[i]));
        }
        return stringBuffer.toString();
    }

    /**
     * 字节数缓冲区字转16进制字符串
     */
    public static String ByteBuf2String(ByteBuf buf) {
        if (!buf.isReadable()) {
            return null;
        }
        byte[] bs = ByteBufUtil.getBytes(buf);
        return Byte2String(bs);
    }

    /**
     * 字节数缓冲区字转16进制字符串 并解除指向
     */
    public static String ByteBuf2StringAndRelease(ByteBuf buf) {
        String s = ByteBuf2String(buf);
        ReferenceCountUtil.release(buf);
        return s;
    }

}
