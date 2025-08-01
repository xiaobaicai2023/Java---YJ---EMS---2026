package com.yunpower.collect.protocols.iec104.common.asdu;

import com.yunpower.collect.protocols.iec104.common.apdu.Apdu;
import com.yunpower.collect.protocols.iec104.common.apdu.Asdu;
import com.yunpower.collect.protocols.iec104.common.apdu.Vsq;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ASDU 数据内容的基类
 */
public abstract class AbstractDataFrameType {

	/**
	 * Work 4 builders
	 */
	@Getter
	@Setter
	protected List<String> work4Builders;

	/**
	 * 读取ByteBuf组装DataType
	 */
	public abstract void loadByteBuf(ByteBuf is, Vsq vsq);

	/**
	 * 由DataType编码为byte的list
	 */
	public abstract void encode(List<Byte> buffer);

	/**
	 * 由DataType（asdu的数据部分） 新建Asdu
	 */
	public abstract Asdu generateBack();

	/**
	 * 用以对该格式帧进行处理
	 */
	public abstract byte[][] handleAndAnswer(Apdu apdu) throws Exception;
}
