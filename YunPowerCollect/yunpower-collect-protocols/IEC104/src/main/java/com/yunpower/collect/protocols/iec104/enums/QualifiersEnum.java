package com.yunpower.collect.protocols.iec104.enums;

import lombok.Getter;

/**
 * @title: 限定词
 * @Author: Jiajiaglam
 * @date: 2023-12-11 16:41
 * @description:
 */
public enum QualifiersEnum {

    /**
     * 总召唤限定词
     */
    generalCallQualifiers(TypeIdentifierEnum.generalCall, 0x20),
    /**
     * 总召唤限定词 支持 老版的分组
     */
    generalCallGroupingQualifiers(TypeIdentifierEnum.generalCall, 0x14),
    /**
     * 复位进程限定词
     */
    resetPprocessQualifiers(TypeIdentifierEnum.resetPprocess, 0x01),
    /**
     * 初始化原因 当地电源合上
     */
    localCloseUpQualifiers(TypeIdentifierEnum.initEnd, 0x00),
    /**
     * 初始化原因 当地手动复位
     */
    localMmanualResetQualifiers(TypeIdentifierEnum.initEnd, 0x01),
    /**
     * 远方复位
     */
    distanceResetQualifiers(TypeIdentifierEnum.initEnd, 0x02),
    /**
     * 品质描述词  遥测
     */
    qualityQualifiers(null, 0x00),
    /**
     * 设置命令限定词  选择预置参数 1000 0000
     */
    prefabParameterQualifiers(null, 0x40),
    /**
     * 设置命令限定词  执行激活参数
     */
    activationParameterQualifiers(null, 0x0F);

    @Getter
    private byte value;

    @Getter
    private TypeIdentifierEnum typeIdentifier;

    QualifiersEnum(TypeIdentifierEnum typeIdentifier, int value) {
        this.value = (byte) value;
        this.typeIdentifier = typeIdentifier;
    }

    /**
     * 根据传输类型 和 限定词的关系 返回 限定词的类型
     *
     * @param typeIdentifier 传输类型
     * @param value          限定词
     * @return 限定词的类型
     */
    public static QualifiersEnum getQualifiersEnum(TypeIdentifierEnum typeIdentifier, byte value) {
        for (QualifiersEnum type : QualifiersEnum.values()) {
            if (type.getValue() == value && type.getTypeIdentifier() == typeIdentifier) {
                return type;
            }
        }
        // 品质描述词和设置参数 限定词对应多个值 所以需要做特殊处理
        QualifiersEnum qualifiersEnum = null;
        if ((TypeIdentifierEnum.normalizedTelemetry.equals(typeIdentifier)
                || TypeIdentifierEnum.scaledTelemetry.equals(typeIdentifier)
                || TypeIdentifierEnum.shortFloatingPointTelemetry.equals(typeIdentifier))
                && qualityQualifiers.getValue() == value) {
            qualifiersEnum = qualityQualifiers;
        }
        if ((TypeIdentifierEnum.readOneParameter.equals(typeIdentifier)
                || TypeIdentifierEnum.readMultipleParameter.equals(typeIdentifier)
                || TypeIdentifierEnum.prefabActivationOneParameter.equals(typeIdentifier)
                || TypeIdentifierEnum.prefabActivationMultipleParameter.equals(typeIdentifier))
                && qualityQualifiers.getValue() == value) {
            qualifiersEnum = qualityQualifiers;
        }
        return qualifiersEnum;
    }
}
