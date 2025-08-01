package com.yunpower.collect.protocols.iec104.common.asdu.typemodel;

import com.yunpower.collect.protocols.iec104.exception.IEC104Exception;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 应用数据单元类型的基类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformationBodyAddress {

    public static final int OCCUPYBYTES = 3;

    /**
     * 信息体地址
     * 共三位16进制字节
     * 高低位逆转
     */
    protected int address;

    /**
     * Information body address
     */
    public InformationBodyAddress(byte b1, byte b2, byte b3) {
        this.address = (b1 & 0xff) | ((b2 & 0xff) << 8) | ((b3 & 0xff) << 16);
    }

    /**
     * Information body address
     */
    public InformationBodyAddress(ByteBuf is) throws IEC104Exception {
        if (is.readableBytes() < OCCUPYBYTES) {
            throw new IEC104Exception(3301, "可用字节不足，不能进行读取");
        }
        this.address = (is.readByte() & 0xff) | ((is.readByte() & 0xff) << 8) | ((is.readByte() & 0xff) << 16);
    }

    /**
     * Encode *
     */
    public void encode(List<Byte> buffer) {
        buffer.add((byte) address);
        buffer.add((byte) (address >> 8));
        buffer.add((byte) (address >> 16));
    }

    @Override
    public String toString() {
        return "信息体地址为:" + address + ";";
    }
}
