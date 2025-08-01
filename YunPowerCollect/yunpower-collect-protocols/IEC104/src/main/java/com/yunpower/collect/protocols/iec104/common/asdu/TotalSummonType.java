package com.yunpower.collect.protocols.iec104.common.asdu;

import com.yunpower.collect.protocols.iec104.common.apdu.Apdu;
import com.yunpower.collect.protocols.iec104.common.apdu.Asdu;
import com.yunpower.collect.protocols.iec104.common.apdu.Vsq;
import com.yunpower.collect.protocols.iec104.common.asdu.typemodel.InformationBodyAddress;
import com.yunpower.collect.protocols.iec104.config.BasicInstruction;
import com.yunpower.collect.protocols.iec104.exception.IEC104Exception;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 总召唤帧
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TotalSummonType extends AbstractDataFrameType {

    /**
     * TYPEID
     */
    public static final int TYPEID = BasicInstruction.TOTAL_SUMMONTYPE_TYPE;

    private InformationBodyAddress address = new InformationBodyAddress(20);

    private int value;

    @Override
    public void encode(List<Byte> buffer) {
        address.encode(buffer);
        buffer.add((byte) value);
    }

    @Override
    public Asdu generateBack() {
        Asdu asdu = new Asdu();
        asdu.setTypeId(100);
        asdu.setDataFrame(this);
        asdu.getVsq().setNum(1);
        asdu.getVsq().setSq(0);
        asdu.getCot().setNot(7);
        asdu.setOriginatorAddress(0);
        asdu.setCommonAddress(1);
        return asdu;
    }

    @Override
    public void loadByteBuf(ByteBuf is, Vsq vsq) {
        try {
            this.address = new InformationBodyAddress(is);
            this.value = (is.readByte() & 0xff);
        } catch (IEC104Exception e) {
            if (e.getCode() == 3301) {
                return;
            }
        }
    }

    @Override
    public byte[][] handleAndAnswer(Apdu apdu) throws Exception {
        return null;
    }

    @Override
    public String toString() {
        String cmd = address.toString();
        return "总召唤令；" + cmd + "\n召唤值：" + this.value + "\n";
    }
}
