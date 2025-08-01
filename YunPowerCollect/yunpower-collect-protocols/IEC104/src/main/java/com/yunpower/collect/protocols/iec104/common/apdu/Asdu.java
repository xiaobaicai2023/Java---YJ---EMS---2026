package com.yunpower.collect.protocols.iec104.common.apdu;

import com.yunpower.collect.protocols.iec104.common.asdu.AbstractDataFrameType;
import com.yunpower.collect.protocols.iec104.container.AsduTypeAnnotationContainer;
import com.yunpower.collect.protocols.iec104.container.DataTypeClasses;
import com.yunpower.collect.protocols.iec104.exception.IEC104Exception;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * ASDU实体
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Asdu {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 位于该帧的第六位 ASDU第一位
     * 应用数据单元类型
     * 列举几个常用的type
     * 表1 在监视方向的报文类型
     * 1     ：= 单点信息                           M_SP_NA_1
     * 3     ：= 双点信息                           M_DP_NA_1
     * 9     ：= 测量值, 规一化值                    M_ME_NA_1
     * 13    ：= 测量值, 短浮点数                    M_ME_NC_1
     * 30    ：= 带CP56Time2a时标的单点信息          M_SP_TB_1
     * 31    ：= 带CP56Time2a时标的双点信息          M_DP_TB_1
     *
     * 在控制方向的系统命令
     * 100   ：= 总召唤命令                         C_IC_NA_1
     */
    protected int typeId;

    /**
     * 位于该帧的第七位  asdu的第二位
     * vsq 可变限定词  分为 sq 和 num
     * 可变结构限定词  ASDU第一位
     * 该值为二位16进制数  先转成8位二进制
     * 二进制第8位 为0顺序信息元素寻址
     * 二进制第8位 为1 单一信息元素寻址
     * 剩下7位转为10进制 数值为信息元素数目
     */
    protected Vsq vsq = new Vsq();

    /**
     * 传送原因   包含测试状态；认可方式；原因序号  位于该帧的第8位  asdu的第三位
     * causeOfTransmission
     * 3：突发，自发
     * 4：初始化
     * 6：激活
     * 7：激活确认
     * 8：停止激活
     * 9：停止激活确认
     * 10：激活终止
     * 20：响应站召唤
     */
    protected Cot cot = new Cot();

    /**
     * Set test *
     */
    public void setTest(boolean test) {
        this.getCot().setTest(test);
    }

    /**
     * Set negative confirm *
     */
    public void setNegativeConfirm(boolean negativeConfirm) {
        this.getCot().setNegativeConfirm(negativeConfirm);
    }

    /**
     * Set not *
     */
    public void setNot(int not) {
        this.getCot().setNot(not);
    }

    /**
     * 源地址  位于该帧的第9位  asdu的第4位
     */
    protected int originatorAddress;

    /**
     * 公共地址
     * 子站端保持和主站端一致即可
     * 应用数据单元地址 位于该帧的第10,11位  asdu的第5,6位
     * 低位在前，高位在后
     */
    protected int commonAddress;

    /**
     * 数据单元 数据单元的类型是有typeId决定的
     * 类型不同里面说承载的数据也不同
     */
    protected AbstractDataFrameType dataFrame;

    /**
     * 隐藏信息，大部分是没有的这个看双方协商
     */
    protected byte[] privateInformation;

    /**
     * Load byte buf asdu
     */
    public Asdu loadByteBuf(ByteBuf dataInputStream) throws Exception {
        if (dataInputStream.readableBytes() < 7) {
            throw new IEC104Exception(3301, "可用字节不足，不能进行读取");
        }

        //获取类型表示配置文件
        this.typeId = dataInputStream.readByte() & 0xff;
        vsq = new Vsq().readByte(dataInputStream.readByte());
        cot = new Cot().readByte(dataInputStream.readByte());
        originatorAddress = dataInputStream.readByte();

        //公共地址
        byte[] commAddress = new byte[2];
        dataInputStream.readBytes(commAddress);
        commonAddress = commAddress[0] + ((commAddress[1] & 0xff) << 8);

        //信息体
        if (typeId < 128) {
            Map<Integer, DataTypeClasses> map = AsduTypeAnnotationContainer.getInstance().getDataTypes();
            if (map.containsKey(typeId)) {
                dataFrame = (AbstractDataFrameType) (map.get(typeId).getTypeClass().newInstance());
                Method load = map.get(typeId).getLoad();
                load.invoke(dataFrame, dataInputStream, this.getVsq());
            } else {
                byte[] unknown = new byte[dataInputStream.readableBytes()];
                dataInputStream.readBytes(unknown);
                LOGGER.error("无法转换信息对象，由于类型标识未知: " + typeId);
            }
            if (dataFrame != null) {
                LOGGER.debug(dataFrame.toString());
            }
            privateInformation = null;
        } else {
            LOGGER.debug("");
        }
        return this;
    }

    /**
     * Encode
     */
    public void encode(List<Byte> buffer) {

        buffer.add((byte) typeId);

        vsq.encode(buffer);

        cot.encode(buffer);

        buffer.add((byte) originatorAddress);

        buffer.add((byte) commonAddress);

        buffer.add((byte) (commonAddress >> 8));

        dataFrame.encode(buffer);
    }
}

