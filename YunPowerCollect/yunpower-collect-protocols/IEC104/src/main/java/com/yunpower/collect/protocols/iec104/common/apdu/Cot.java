package com.yunpower.collect.protocols.iec104.common.apdu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 传输原因
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cot {

	/**
	 * Read byte cot
	 */
	public Cot readByte(Byte value) {
		original = value;
		test=(original&0xff)>>7==1;
		negativeConfirm=(original&0x40)>>6==1;
		not=value&0x3f;
		return this;
	}

	/**
	 * cot 的具体值
	 */
	byte original;

	/**
	 * 测试状态   第八位
	 * 0---false---未试验
	 * 1---true----已试验
	 */
	private boolean test;

	/**
	 * 认证方式   第七位
	 * 0---false----肯定认可
	 * 1---true-----否定认可
	 */
	boolean negativeConfirm;

	/**
	 * 传输原因序号   1-6位
	 * 3：突发，自发
	 * 4：初始化
	 * 6：激活
	 * 7：激活确认
	 * 8：停止激活
	 * 9：停止激活确认
	 * 10：激活终止
	 * 20：响应站召唤
	 */
	int not;

	/**
	 * Encode *
	 */
	public void encode(List<Byte> buffer) {
		if (this.isTest()) {
			if (this.isNegativeConfirm()) {
				buffer.add((byte) (this.getNot() | 0xC0));
			} else {
				buffer.add((byte) (this.getNot() | 0x80));
			}
		} else {
			if (this.isNegativeConfirm()) {
				buffer.add((byte) (this.getNot() | 0x40));
			} else {
				buffer.add((byte) this.getNot());
			}
		}
	}
}
