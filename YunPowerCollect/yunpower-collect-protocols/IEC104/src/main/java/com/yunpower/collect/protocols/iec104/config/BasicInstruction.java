package com.yunpower.collect.protocols.iec104.config;

/**
 * @title: 104规约的基本指令封装
 * @Author: Jiajiaglam
 * @date: 2023-12-12 13:45
 * @description:
 */
public class BasicInstruction {
    /**
     * 建立网络连接或链路启动
     * 主站发送
     */
    public static final byte[] START = new byte[]{0x68, 0x04, 0x07, 0x00, 0x00, 0x00};

    /**
     * 链路启动确认指令
     * 从站发送
     */
    public static final byte[] START_YES = new byte[]{0x68, 0x04, 0x0B, 0x00, 0x00, 0x00};

    /**
     * U帧测试指令
     * 主站发送
     */
    public static final byte[] TEST_U = new byte[]{0x68, 0x04, (byte) 0x43, 0x00, 0x00, 0x00};

    /**
     * U（心跳）帧测试确认指令
     * 从站发送（应答）
     */
    public static final byte[] TEST_U_YES = new byte[]{0x68, 0x04, (byte) 0x83, 0x00, 0x00, 0x00};

    /**
     * 停止链路
     * 主站发送
     */
    public static final byte[] STOP = new byte[]{0x68, 0x04, 0x13, 0x00, 0x00, 0x00};

    /**
     * 停止链路确认
     * 从站发送
     */
    public static final byte[] STOP_YES = new byte[]{0x68, 0x04, 0x23, 0x00, 0x00, 0x00};

    /**
     * S（确认）帧测试帧（用于确认接收的I帧）
     * 主站发送
     */
    public static final byte[] TEST_S = new byte[]{0x68, 0x04, 0x01, 0x00, 0x02, 0x00};
    public static final byte[] TEST_S_01 = new byte[]{0x68, 0x04, 0x01, 0x00, 0x01, 0x00};

    /**
     * 总召唤命令
     * 主站发送
     */
    public static final byte[] GENERAL_INTERROGATION = new byte[]{0x68, 0x0E, 0x00, 0x00, 0x00, 0x00, 0x64, 0x01, 0x06, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x14};

    /**
     * 主站发送 → 总召唤：
     * 68（ 启动符）0E（ 长度）00 00（发送序号）00 00（ 接收序号）64（ 类型标示:总召唤） 01（ 可变
     * 结构限定词）06 00（ 传输原因：激活） 01 00（ 公共地址即装置地址）00 00 00（ 信息体地址）14
     * （ 区分是总召唤还是分组召唤， 2002 年修改后的规约中没有分组召唤）。
     *
     * 从站发送 → 总召唤确认（发送帧的镜像，除传送原因不同） ：
     * 68（ 启动符）0E（ 长度）00 00（发送序号）00 00（ 接收序号）64（ 类型标示:总召唤） 01（ 可变
     * 结构限定词）07 00（ 传输原因：激活确认）01 00（ 公共地址即装置地址）00 00 00（ 信息体地址）
     * 14（ 同上）
     *
     * 从站发送 → YC 帧（类型标示符09 带品质描述的遥测，传输原因： 14 响应总召唤） ：
     * 68（ 启动符）13（ 长度）06 00（ 发送序号）02 00（ 接收序号）09（ 类型标示：带品质描述的遥测）
     * 82（ 可变结构限定词，有2 个连续遥测上送）14 00（ 传输原因：响应总召唤）01 00（公共地址）
     * 01 40 00（信息体地址，从0X4001 开始第0 号遥测）A1 10（ 遥测值10A1）00（ 品质描述）89 15（ 遥
     * 测值1589）00（ 品质描述）
     * IEC-104 规约报文实例分析
     *
     * 从站发送 → YX 帧（类型标示符为01 的单点遥信，传输原因： 14 响应总召唤） ：
     * 68（启动符）1A（ 长度）02 00（发送序号）02 00（ 接收序号）01（ 类型标示：单点遥信）04（ 可
     * 变结构限定词，有4 个遥信上送）14 00（ 传输原因：响应总召唤）01 00（ 公共地址即装置地址）
     * 01 00 00（ 信息体基地址）00（ 第1 号遥信，分）01（ 第2 号遥信，合）00（ 第3 号遥信，分）00（ 第
     * 4 号遥信，分）
     *
     * 从站发送 → 结束总召唤帧（主站发送总召唤命令，从站才对应发送结束总召唤帧） ：
     * 68（ 启动符）0E（ 长度）08 00（发送序号）02 00（接收序号）64（ 类型标示：总召唤）01（ 可变
     * 结构限定词）0A 00（传输原因：激活结束）01 00（ 公共地址） 00 00 00 （ 信息体地址）14（区分
     * 是总召唤还是分组召唤， 02 年修改后的规约中没有分组召唤）
     *
     * 主站发送 → S 帧：
     * 68 04 01 00 0A 00
     * */


    /**
     * 104通讯过程:
     * a：建立链路、启动传输
     * b：是否启动确认 (未确认等待启动帧返回a)
     * c：主站召唤
     * d：回答全数据
     * e：回答结束否 (等待d结束,未结束返回d)
     * f：有无定时任务
     * g：遥控遥调
     * h：对时测试
     * i：超时处理
     * j：接收变化数据
     * k：S-FORMAT确认 (确认完毕,返回f)
     */


    /**
     *
     *
     * 遥测：09----带品质描述的遥测量，每个遥测值占3个字节
     *      0a----带3个字节时标的且具有品质描述的遥测值，每个遥测值占6个字节
     *      0b---不带时标的标度化值，每个遥测值占3个字节
     *      0c---带3个字节时标的标度化值，每个遥测值占6个字节
     *      0d---带品质描述的浮点值，每个遥测值占5个字节
     *      0e---带3个字节时标且具有品质描述的浮点值，每个遥测值占8个字节
     *      15---不带品质描述的遥测值，每个遥测值占2个字节
     * 遥信：01---不带时标的单点遥信，每个遥信占1个字节
     *      03---不带时标的双点遥信，每个遥信占1个字节
     *      14---具有状态变位检测的成组单点遥信，每个字节包括8个遥信
     * SOE：02---带3个字节短时标的单点遥信
     *      04---带3个字节短时标的双点遥信
     *      1e---带7个字节时标的单点遥信
     *      1f---带7个字节时标的双点遥信
     * 遥脉：0f---不带时标的电度量，每个电度量占5个字节
     *      10---带3个字节短时标的电度量，每个电度量占8个字节
     *      25---带7个字节长时标的电度量，每个电度量占12个字节
     * 其他：2d---单点遥控
     * 2e---双点遥控
     *      2f---双电遥调
     *      64---召唤全数据
     *      65---召唤全电度
     *      67---时钟同步命令
     *
     * */

    /**
     * 单点信息
     */
    public static final Integer SINGEL_POINT_TYPE = 1;

    /**
     * 单点带长时标信息
     */
    public static final Integer SINGEL_POINT_TIME_TYPE = 30;

    /**
     * 双点信息
     */
    public static final Integer DOUBLE_POINT_TYPE = 3;

    /**
     * 测量值，规一化值
     */
    public static final Integer NORMALIZED_INTEGER_TYPE = 9;

    /**
     * 测量值，规一化值 带时间
     */
    public static final Integer NORMALIZED_INTEGER_TIME_TYPE = 34;

    /**
     * 测量值，标度化值
     */
    public static final Integer SCALING_INTEGER_TYPE = 11;

    /**
     * 测量值，短浮点数
     */
    public static final Integer SHORT_FLOAT_TYPE = 13;

    /**
     * 电能累计值
     */
    public static final Integer CUMULATIVE_ELECTRIC_ENERGY_MEASUREMENT = 15;

    /**
     * 测量值，标度化值 带时标
     */
    public static final Integer SHORT_INTEGER_TYPE_TIME = 35;

    /**
     * 测量值，短浮点数 带时标
     */
    public static final Integer SHORT_FLOAT_TYPE_TIME = 36;

    /**
     * 测量值，无品质位规一化值
     */
    public static final Integer NOQUALITY_NORMALIZED_INTEGER_TYPE = 21;

    /**
     * 总召唤
     */
    public static final Integer TOTAL_SUMMONTYPE_TYPE = 0x64;

    /**
     * 电度总召唤
     */
    public static final Integer PULSE_TOTAL_SUMMONTYPE_TYPE = 0x65;

    /**
     * 设置短浮点命令
     */
    public static final Integer SHORT_FLOAT_COMMAND_TYPE = 0x32;

    /**
     * 单点遥控命令
     */
    public static final Integer SINGLE_BOOLEAN_COMMAND_TYPE = 0x2D;

    /**
     * 归一化值遥调命令
     */
    public static final Integer NORMALIZATION_COMMAND_TYPE = 0x30;

    /**
     * 双点遥控命令
     */
    public static final Integer DOUBLE_BOOLEAN_COMMAND_TYPE = 0x2E;

    /**
     * 对时帧
     */
    public static final Integer DATESYNCHRONIZATION_TYPE = 0x67;
}
