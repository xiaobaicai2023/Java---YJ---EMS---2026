package com.yunpower.collect.protocols.iec104.common.apdu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * vsq 可变限定词 分为 sq 和 num
 * 可变结构限定词 ASDU 第一位
 * 该值为二位16进制数 先转成8位二进制
 * 二进制第8位 为0 单一信息元素寻址
 * 二进制第8位 为1 连续信息元素寻址
 * 剩下7位转为10进制 数值为信息元素数目
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vsq {

	/**
	 * Read byte vsq
	 */
	public Vsq readByte(Byte value) {
		original = value;
		//可变结构限定词，转为二进制后获取第8位
		sq = (value&0xff)>>7;
		num = value&0x7f;
		return this;
	}

	/**
	 * vsq 的具体值
	 */
	byte original;

	/**
	 * 标制  是 顺序元素 还是 单一元素   第8位
	 */
	int sq;

	/**
	 * 信息体元素地址数量   0-7位
	 */
	int num;

	/**
	 * Encode *
	 */
	public void encode(List<Byte> buffer) {
		if (this.getSq() == 1) {
			buffer.add((byte) (this.getNum() | 0x80));
		} else {
			buffer.add((byte) this.getNum());
		}
	}
}
