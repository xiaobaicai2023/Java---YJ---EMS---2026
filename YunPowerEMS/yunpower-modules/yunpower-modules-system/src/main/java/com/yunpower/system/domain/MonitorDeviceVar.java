package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 监控设备变量对象 monitor_device_var
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("监控设备变量对象")
public class MonitorDeviceVar extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 企业ID */
    @ApiModelProperty("企业ID")
    private Long entId;

    /** 部门ID */
    @ApiModelProperty("部门ID")
    private Long deptId;

    /** 电站类型（1配电 2光伏 3储能 5用水 6流量 7计碳） */
    @ApiModelProperty("电站类型（1配电 2光伏 3储能 5用水 6流量 7计碳）")
    @Excel(name = "电站类型", readConverterExp = "1=配电,2=光伏,3=储能,6=流量,7计碳")
    private Integer stationType;

    /** 所属通道ID */
    @ApiModelProperty("所属通道ID")
    private Long channelId;

    /** 所属通道编码 */
    @ApiModelProperty("所属通道编码")
    private String channelSn;

    /** 所属通信设备ID */
    @ApiModelProperty("所属通信设备ID")
    private Long comDeviceId;

    /** 所属通信设备编码 */
    @ApiModelProperty("所属通信设备编码")
    private String comDeviceSn;

    /** 监控设备ID */
    @ApiModelProperty("监控设备ID")
    private Long deviceId;

    /** 监控设备名称 */
    @ApiModelProperty("监控设备名称")
    @Excel(name = "设备名称")
    @Transient
    private String deviceName;

    /** 监控设备编码 */
    @ApiModelProperty("监控设备编码")
    @Excel(name = "设备编码")
    private String deviceSn;

    /** 变量名称 */
    @ApiModelProperty("变量名称")
    @Excel(name = "变量名称")
    private String varName;

    /** 变量编码 */
    @ApiModelProperty("变量编码")
    @Excel(name = "变量编码，自动生成")
    private String varSn;

    /** 索引地图变量编码 */
    @ApiModelProperty("索引地图变量编码")
    @Excel(name = "索引编码")
    private String varMapSn;

    /** 变量单位 */
    @ApiModelProperty("变量单位")
    @Excel(name = "变量单位")
    private String unit;

    /** 是否监控 */
    @ApiModelProperty("是否监控")
    @Excel(name = "是否监控", readConverterExp = "0=否,1=是")
    private Integer isMonitor;

    /** 变量类型（1模拟量 2状态量） */
    @ApiModelProperty("变量类型（1模拟量 2状态量）")
    @Excel(name = "变量类型", readConverterExp = "1=模拟量,2=状态量")
    private Integer varType;

    /** 变量通用类型 */
    @ApiModelProperty("通用类型")
    @Excel(name = "通用类型", readConverterExp = "0=不指定,1=电能（用电）,2=电能（发电）,3=电流A,4=电流B,5=电流C,6=电压A,7=电压B,8=电压C,20=线电压UCA,19=线电压UBC,18=线电压UAB,9=有功功率,10=无功功率,11=视在功率,12=功率因素,13=温度,14=湿度,15=需量,16=负载率,17=状态")
    private Integer variableType;

    /** 数据来源（1IO型 2内存型） */
    @ApiModelProperty("数据来源（1IO型 2内存型）")
    @Excel(name = "数据来源", readConverterExp = "1=IO型,2=内存型")
    private Integer origin;

    /** 数据区域 */
    @ApiModelProperty("数据区域")
    private String registerName;

    /** 数据地址 */
    @ApiModelProperty("数据地址")
    private Integer registerIndex;

    /** 寄存器地址 */
    @ApiModelProperty("寄存器地址")
    @Excel(name = "寄存器地址")
    private Integer registerAddress;

    /** 信息体地址 */
    @ApiModelProperty("信息体地址")
    @Excel(name = "信息体地址")
    private Integer messageAddress;

    /** 数据标识 */
    @ApiModelProperty("数据标识")
    @Excel(name = "数据标识")
    private String dataAddress;

    /** 读写类型（1只读 2只写 3读写） */
    @ApiModelProperty("读写类型（1只读 2只写 3读写）")
    private Integer rw;

    /** 计算公式 */
    @ApiModelProperty("计算公式")
    @Excel(name = "计算公式")
    private String computeFormula;

    /** 缺失值处理（1不计算 2使用最近值 3使用0值 4使用1值） */
    @ApiModelProperty("缺失值处理（1不计算 2使用最近值 3使用0值 4使用1值）")
    private Integer deletionHandle;

    /** 零值计算（0不计算 1计算） */
    @ApiModelProperty("零值计算（0不计算 1计算）")
    private Integer zeroCompute;

    /** 补当前数据（0不补 1补） */
    @ApiModelProperty("补当前数据（0不补 1补）")
    private Integer repairData;

    /** 数据类型 */
    @ApiModelProperty("数据类型")
    @Excel(name = "数据类型", readConverterExp = "1=2字节无符号整数,2=2字节整数低位在前,3=2字节整数高位在前,4=2字节整数最高位符号位,5=4字节无符号整数,6=4字节无符号整数1234,7=4字节无符号整数2143,8=4字节无符号整数3412,9=4字节无符号整数4321,10=4字节有符号整数,11=4字节有符号整数1234,12=4字节有符号整数2143,13=4字节有符号整数3412,14=4字节有符号整数4321,15=4字节整数最高位符号位,16=4字节浮点1234,17=4字节浮点2143,18=4字节浮点3412,19=4字节浮点4321")
    private Integer dataType;

    /** 初始赋值 */
    @ApiModelProperty("初始赋值")
    @Excel(name = "初始赋值")
    private Float initValue;

    /** 数据基值 */
    @ApiModelProperty("数据基值")
    @Excel(name = "数据基值")
    private Float baseValue;

    /** 数据系数 */
    @ApiModelProperty("数据系数")
    @Excel(name = "数据系数")
    private Float coefficient;

    /** 存盘周期（m）0不存盘 */
    @ApiModelProperty("存盘周期（m）0不存盘")
    @Excel(name = "存盘周期", readConverterExp = "5=5分钟,10=10分钟,15=15分钟,30=30分钟,60=1小时,0=不存盘")
    private Integer saveCycle;

    /** 数据修正（0不操作 1上报原值 2上报0值） */
    @ApiModelProperty("数据修正（0不操作 1操作）")
    @Excel(name = "数据修正", readConverterExp = "0=不操作,1=操作")
    private Integer dataFix;

    /** 是否累积量 */
    @ApiModelProperty("是否累积量")
    @Excel(name = "是否累积量", readConverterExp = "0=否,1=是")
    private Integer isAccumulation;

    /** 累积类型（1小时累积 2天累积） */
    @ApiModelProperty("累积类型（1小时累积 2天累积）")
    private Integer accumulationType;

    /** 数据值过滤（0否 1是） */
    @ApiModelProperty("数据值过滤（0否 1是）")
    private Integer isDataFilter;

    /** 绝对值大于 */
    @ApiModelProperty("绝对值大于")
    private Float moreAbs;

    /** 替换值1 */
    @ApiModelProperty("替换值1")
    private Float replaceValueUpper;

    /** 绝对值小于 */
    @ApiModelProperty("绝对值小于")
    private Float lessAbs;

    /** 替换值2 */
    @ApiModelProperty("替换值2")
    private Float replaceValueLower;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    private Integer deleteFlag;

    /** 通道名称 */
    @ApiModelProperty("通道名称")
    @Transient
    private String channelName;

    /** 通讯设备名称 */
    @ApiModelProperty("通讯设备名称")
    @Transient
    private String comDeviceName;

    /** 索引地图变量名称 */
    @ApiModelProperty("索引地图变量名称")
    @Transient
    private String varMapName;

    /** 站点名称 */
    @ApiModelProperty("站点名称")
    private String stationName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setEntId(Long entId)
    {
        this.entId = entId;
    }

    public Long getEntId()
    {
        return entId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setStationType(Integer stationType)
    {
        this.stationType = stationType;
    }

    public Integer getStationType()
    {
        return stationType;
    }

    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

    public Long getChannelId()
    {
        return channelId;
    }

    public void setChannelSn(String channelSn)
    {
        this.channelSn = channelSn;
    }

    public String getChannelSn()
    {
        return channelSn;
    }

    public void setComDeviceId(Long comDeviceId)
    {
        this.comDeviceId = comDeviceId;
    }

    public Long getComDeviceId()
    {
        return comDeviceId;
    }

    public void setComDeviceSn(String comDeviceSn)
    {
        this.comDeviceSn = comDeviceSn;
    }

    public String getComDeviceSn()
    {
        return comDeviceSn;
    }

    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceSn(String deviceSn)
    {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn()
    {
        return deviceSn;
    }

    public void setVarName(String varName)
    {
        this.varName = varName;
    }

    public String getVarName()
    {
        return varName;
    }

    public void setVarSn(String varSn)
    {
        this.varSn = varSn;
    }

    public String getVarSn()
    {
        return varSn;
    }

    public String getVarMapSn() {
        return varMapSn;
    }

    public void setVarMapSn(String varMapSn) {
        this.varMapSn = varMapSn;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }

    public Integer getIsMonitor() {
        return isMonitor;
    }

    public void setIsMonitor(Integer isMonitor) {
        this.isMonitor = isMonitor;
    }

    public void setVarType(Integer varType)
    {
        this.varType = varType;
    }

    public Integer getVarType()
    {
        return varType;
    }

    public Integer getVariableType() {
        return variableType;
    }

    public void setVariableType(Integer variableType) {
        this.variableType = variableType;
    }

    public void setOrigin(Integer origin)
    {
        this.origin = origin;
    }

    public Integer getOrigin()
    {
        return origin;
    }

    public void setRegisterName(String registerName)
    {
        this.registerName = registerName;
    }

    public String getRegisterName()
    {
        return registerName;
    }

    public void setRegisterIndex(Integer registerIndex)
    {
        this.registerIndex = registerIndex;
    }

    public Integer getRegisterIndex()
    {
        return registerIndex;
    }

    public Integer getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(Integer registerAddress) {
        this.registerAddress = registerAddress;
    }

    public Integer getMessageAddress() {
        return messageAddress;
    }

    public void setMessageAddress(Integer messageAddress) {
        this.messageAddress = messageAddress;
    }

    public String getDataAddress() {
        return dataAddress;
    }

    public void setDataAddress(String dataAddress) {
        this.dataAddress = dataAddress;
    }

    public void setRw(Integer rw)
    {
        this.rw = rw;
    }

    public Integer getRw()
    {
        return rw;
    }

    public void setComputeFormula(String computeFormula)
    {
        this.computeFormula = computeFormula;
    }

    public String getComputeFormula()
    {
        return computeFormula;
    }

    public void setDeletionHandle(Integer deletionHandle)
    {
        this.deletionHandle = deletionHandle;
    }

    public Integer getDeletionHandle()
    {
        return deletionHandle;
    }

    public Integer getZeroCompute() {
        return zeroCompute;
    }

    public void setZeroCompute(Integer zeroCompute) {
        this.zeroCompute = zeroCompute;
    }

    public Integer getRepairData() {
        return repairData;
    }

    public void setRepairData(Integer repairData) {
        this.repairData = repairData;
    }

    public void setDataType(Integer dataType)
    {
        this.dataType = dataType;
    }

    public Integer getDataType()
    {
        return dataType;
    }

    public void setInitValue(Float initValue)
    {
        this.initValue = initValue;
    }

    public Float getInitValue()
    {
        return initValue;
    }

    public void setBaseValue(Float baseValue)
    {
        this.baseValue = baseValue;
    }

    public Float getBaseValue()
    {
        return baseValue;
    }

    public void setCoefficient(Float coefficient)
    {
        this.coefficient = coefficient;
    }

    public Float getCoefficient()
    {
        return coefficient;
    }

    public void setSaveCycle(Integer saveCycle)
    {
        this.saveCycle = saveCycle;
    }

    public Integer getSaveCycle()
    {
        return saveCycle;
    }

    public Integer getDataFix() {
        return dataFix;
    }

    public void setDataFix(Integer dataFix) {
        this.dataFix = dataFix;
    }

    public void setIsAccumulation(Integer isAccumulation)
    {
        this.isAccumulation = isAccumulation;
    }

    public Integer getIsAccumulation()
    {
        return isAccumulation;
    }

    public void setAccumulationType(Integer accumulationType)
    {
        this.accumulationType = accumulationType;
    }

    public Integer getAccumulationType()
    {
        return accumulationType;
    }

    public Integer getIsDataFilter() {
        return isDataFilter;
    }

    public void setIsDataFilter(Integer isDataFilter) {
        this.isDataFilter = isDataFilter;
    }

    public void setMoreAbs(Float moreAbs)
    {
        this.moreAbs = moreAbs;
    }

    public Float getMoreAbs()
    {
        return moreAbs;
    }

    public void setReplaceValueUpper(Float replaceValueUpper)
    {
        this.replaceValueUpper = replaceValueUpper;
    }

    public Float getReplaceValueUpper()
    {
        return replaceValueUpper;
    }

    public void setLessAbs(Float lessAbs)
    {
        this.lessAbs = lessAbs;
    }

    public Float getLessAbs()
    {
        return lessAbs;
    }

    public void setReplaceValueLower(Float replaceValueLower)
    {
        this.replaceValueLower = replaceValueLower;
    }

    public Float getReplaceValueLower()
    {
        return replaceValueLower;
    }

    public void setStopFlag(Integer stopFlag)
    {
        this.stopFlag = stopFlag;
    }

    public Integer getStopFlag()
    {
        return stopFlag;
    }

    public void setDeleteFlag(Integer deleteFlag)
    {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag()
    {
        return deleteFlag;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getComDeviceName() {
        return comDeviceName;
    }

    public void setComDeviceName(String comDeviceName) {
        this.comDeviceName = comDeviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getVarMapName() {
        return varMapName;
    }

    public void setVarMapName(String varMapName) {
        this.varMapName = varMapName;
    }


    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("stationType", getStationType())
                .append("channelId", getChannelId())
                .append("channelSn", getChannelSn())
                .append("comDeviceId", getComDeviceId())
                .append("comDeviceSn", getComDeviceSn())
                .append("deviceId", getDeviceId())
                .append("deviceSn", getDeviceSn())
                .append("varName", getVarName())
                .append("varSn", getVarSn())
                .append("unit", getUnit())
                .append("varType", getVarType())
                .append("origin", getOrigin())
                .append("registerName", getRegisterName())
                .append("registerIndex", getRegisterIndex())
                .append("messageAddress", getMessageAddress())
                .append("rw", getRw())
                .append("computeFormula", getComputeFormula())
                .append("deletionHandle", getDeletionHandle())
                .append("dataType", getDataType())
                .append("initValue", getInitValue())
                .append("baseValue", getBaseValue())
                .append("coefficient", getCoefficient())
                .append("saveCycle", getSaveCycle())
                .append("isAccumulation", getIsAccumulation())
                .append("accumulationType", getAccumulationType())
                .append("isDataFilter", getIsDataFilter())
                .append("moreAbs", getMoreAbs())
                .append("replaceValueUpper", getReplaceValueUpper())
                .append("lessAbs", getLessAbs())
                .append("replaceValueLower", getReplaceValueLower())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("stopFlag", getStopFlag())
                .append("deleteFlag", getDeleteFlag())
                .toString();
    }
}
