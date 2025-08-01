package com.yunpower.system.domain;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunpower.system.domain.handler.DevicesDataAreaTypeHandler;
import com.yunpower.system.domain.jsonvo.DevicesDataAreaVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 通讯设备对象 communication_device
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("通讯设备对象")
public class CommunicationDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 企业ID */
    @ApiModelProperty("企业ID")
    @Excel(name = "企业ID")
    private Long entId;

    /** 部门ID */
    @ApiModelProperty("部门ID")
    @Excel(name = "部门ID")
    private Long deptId;

    /** 电站类型（1配电 2光伏） */
    @ApiModelProperty("电站类型（1配电 2光伏）")
    @Excel(name = "电站类型", readConverterExp = "1=配电,2=光伏")
    private Integer stationType;

    /** 设备名称 */
    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备编码 */
    @ApiModelProperty("设备编码")
    @Excel(name = "设备编码")
    private String deviceSn;

    /** 所属通道ID */
    @ApiModelProperty("所属通道ID")
    @Excel(name = "所属通道ID")
    private Long channelId;

    /** 所属通道编码 */
    @ApiModelProperty("所属通道编码")
    @Excel(name = "所属通道编码")
    private String channelSn;

    /** 连接协议（1104 2ModbusRTU 3ModbusTCP 4DLT645） */
    @ApiModelProperty("连接协议（1104 2ModbusRTU 3ModbusTCP 4DLT645）")
    @Excel(name = "连接协议", readConverterExp = "1=104,2=ModbusRTU,3=ModbusTCP,4=DLT645")
    private String connectModel;

    /** 连接超时（s） */
    @ApiModelProperty("连接超时（s）")
    @Excel(name = "连接超时", readConverterExp = "s")
    private Integer timeout;

    /** 上线时间 */
    @ApiModelProperty("上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上线时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;

    /** 下线时间 */
    @ApiModelProperty("下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下线时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date offlineTime;

    /** 是否激活 */
    @ApiModelProperty("是否激活")
    @Excel(name = "是否激活")
    private Integer isActive;

    /** 激活时间 */
    @ApiModelProperty("激活时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "激活时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date activeUpdateTime;

    /** 通道连接报警 */
    @ApiModelProperty("通道连接报警")
    @Excel(name = "通道连接报警")
    private Integer connectAlarm;

    /** 设备地址（Modbus） */
    @ApiModelProperty("设备地址（Modbus）")
    @Excel(name = "设备地址", readConverterExp = "M=odbus")
    private Integer sbdz;

    /** 传送原因长度 */
    @ApiModelProperty("传送原因长度")
    @Excel(name = "传送原因长度")
    private Integer csyyLength;

    /** 公共地址（104） */
    @ApiModelProperty("公共地址（104）")
    @Excel(name = "公共地址", readConverterExp = "1=04")
    private Integer ggdz;

    /** 公共地址长度 */
    @ApiModelProperty("公共地址长度")
    @Excel(name = "公共地址长度")
    private Integer ggdzLength;

    /** 信息体地址长度 */
    @ApiModelProperty("信息体地址长度")
    @Excel(name = "信息体地址长度")
    private Integer infoLength;

    /** 超时T0（s） */
    @ApiModelProperty("超时T0（s）")
    @Excel(name = "超时T0", readConverterExp = "s=")
    private Integer timeoutT0;

    /** 超时T1（s） */
    @ApiModelProperty("超时T1（s）")
    @Excel(name = "超时T1", readConverterExp = "s=")
    private Integer timeoutT1;

    /** 超时T2（s） */
    @ApiModelProperty("超时T2（s）")
    @Excel(name = "超时T2", readConverterExp = "s=")
    private Integer timeoutT2;

    /** 超时T3（s） */
    @ApiModelProperty("超时T3（s）")
    @Excel(name = "超时T3", readConverterExp = "s=")
    private Integer timeoutT3;

    /** 总召采集周期 */
    @ApiModelProperty("总召采集周期")
    @Excel(name = "总召采集周期")
    private Integer assCallCycle;

    /** 电度总召周期 */
    @ApiModelProperty("电度总召周期")
    @Excel(name = "电度总召周期")
    private Integer elcCallCycle;

    /** 数据区域 */
    @ApiModelProperty("数据区域（JSON格式）")
    @Excel(name = "数据区域")
    @TableField(typeHandler = DevicesDataAreaTypeHandler.class)
    private List<DevicesDataAreaVo> dataArea;

    /** 产品类型（1通讯管理机 2DTU） */
    @ApiModelProperty("产品类型（1通讯管理机 2DTU）")
    @Excel(name = "产品类型", readConverterExp = "1=通讯管理机,2=DTU")
    private Integer proType;

    /** 生产厂家 */
    @ApiModelProperty("生产厂家")
    @Excel(name = "生产厂家")
    private Integer proFactory;

    /** 产品版本 */
    @ApiModelProperty("产品版本")
    @Excel(name = "产品版本")
    private String proVer;

    /** 产品SN号 */
    @ApiModelProperty("产品SN号")
    @Excel(name = "产品SN号")
    private String proSn;

    /** 产品型号 */
    @ApiModelProperty("产品型号")
    @Excel(name = "产品型号")
    private String proModel;

    /** 运营商 */
    @ApiModelProperty("运营商")
    @Excel(name = "运营商")
    private Integer cardOperator;

    /** 物联卡号 */
    @ApiModelProperty("物联卡号")
    @Excel(name = "物联卡号")
    private String cardNo;

    /** IP地址 */
    @ApiModelProperty("IP地址")
    @Excel(name = "IP地址")
    private String cardIp;

    /** 流量标准 */
    @ApiModelProperty("流量标准")
    @Excel(name = "流量标准")
    private Integer cardFlow;

    /** 资费标准 */
    @ApiModelProperty("资费标准")
    @Excel(name = "资费标准")
    private Float cardPostage;

    /** 资费到期 */
    @ApiModelProperty("资费到期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "资费到期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date cardExpireDate;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    /** 站点名称（部门名称） */
    @Transient
    private String stationName;

    /** 通道名称 */
    @Transient
    private String channelName;

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

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceSn(String deviceSn)
    {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn()
    {
        return deviceSn;
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

    public void setConnectModel(String connectModel)
    {
        this.connectModel = connectModel;
    }

    public String getConnectModel()
    {
        return connectModel;
    }

    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    public Integer getTimeout()
    {
        return timeout;
    }

    public void setOnlineTime(Date onlineTime)
    {
        this.onlineTime = onlineTime;
    }

    public Date getOnlineTime()
    {
        return onlineTime;
    }

    public void setOfflineTime(Date offlineTime)
    {
        this.offlineTime = offlineTime;
    }

    public Date getOfflineTime()
    {
        return offlineTime;
    }

    public void setIsActive(Integer isActive)
    {
        this.isActive = isActive;
    }

    public Integer getIsActive()
    {
        return isActive;
    }

    public void setActiveUpdateTime(Date activeUpdateTime)
    {
        this.activeUpdateTime = activeUpdateTime;
    }

    public Date getActiveUpdateTime()
    {
        return activeUpdateTime;
    }

    public void setConnectAlarm(Integer connectAlarm)
    {
        this.connectAlarm = connectAlarm;
    }

    public Integer getConnectAlarm()
    {
        return connectAlarm;
    }

    public void setSbdz(Integer sbdz)
    {
        this.sbdz = sbdz;
    }

    public Integer getSbdz()
    {
        return sbdz;
    }

    public void setCsyyLength(Integer csyyLength)
    {
        this.csyyLength = csyyLength;
    }

    public Integer getCsyyLength()
    {
        return csyyLength;
    }

    public void setGgdz(Integer ggdz)
    {
        this.ggdz = ggdz;
    }

    public Integer getGgdz()
    {
        return ggdz;
    }

    public void setGgdzLength(Integer ggdzLength)
    {
        this.ggdzLength = ggdzLength;
    }

    public Integer getGgdzLength()
    {
        return ggdzLength;
    }

    public void setInfoLength(Integer infoLength)
    {
        this.infoLength = infoLength;
    }

    public Integer getInfoLength()
    {
        return infoLength;
    }

    public void setTimeoutT0(Integer timeoutT0)
    {
        this.timeoutT0 = timeoutT0;
    }

    public Integer getTimeoutT0()
    {
        return timeoutT0;
    }

    public void setTimeoutT1(Integer timeoutT1)
    {
        this.timeoutT1 = timeoutT1;
    }

    public Integer getTimeoutT1()
    {
        return timeoutT1;
    }

    public void setTimeoutT2(Integer timeoutT2)
    {
        this.timeoutT2 = timeoutT2;
    }

    public Integer getTimeoutT2()
    {
        return timeoutT2;
    }

    public void setTimeoutT3(Integer timeoutT3)
    {
        this.timeoutT3 = timeoutT3;
    }

    public Integer getTimeoutT3()
    {
        return timeoutT3;
    }

    public void setAssCallCycle(Integer assCallCycle)
    {
        this.assCallCycle = assCallCycle;
    }

    public Integer getAssCallCycle()
    {
        return assCallCycle;
    }

    public void setElcCallCycle(Integer elcCallCycle)
    {
        this.elcCallCycle = elcCallCycle;
    }

    public Integer getElcCallCycle()
    {
        return elcCallCycle;
    }

    public List<DevicesDataAreaVo> getDataArea() {
        return dataArea;
    }

    public void setDataArea(List<DevicesDataAreaVo> dataArea) {
        this.dataArea = dataArea;
    }

    public void setProType(Integer proType)
    {
        this.proType = proType;
    }

    public Integer getProType()
    {
        return proType;
    }

    public void setProFactory(Integer proFactory)
    {
        this.proFactory = proFactory;
    }

    public Integer getProFactory()
    {
        return proFactory;
    }

    public void setProVer(String proVer)
    {
        this.proVer = proVer;
    }

    public String getProVer()
    {
        return proVer;
    }

    public void setProSn(String proSn)
    {
        this.proSn = proSn;
    }

    public String getProSn()
    {
        return proSn;
    }

    public void setProModel(String proModel)
    {
        this.proModel = proModel;
    }

    public String getProModel()
    {
        return proModel;
    }

    public void setCardOperator(Integer cardOperator)
    {
        this.cardOperator = cardOperator;
    }

    public Integer getCardOperator()
    {
        return cardOperator;
    }

    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    public String getCardNo()
    {
        return cardNo;
    }

    public void setCardIp(String cardIp)
    {
        this.cardIp = cardIp;
    }

    public String getCardIp()
    {
        return cardIp;
    }

    public void setCardFlow(Integer cardFlow)
    {
        this.cardFlow = cardFlow;
    }

    public Integer getCardFlow()
    {
        return cardFlow;
    }

    public void setCardPostage(Float cardPostage)
    {
        this.cardPostage = cardPostage;
    }

    public Float getCardPostage()
    {
        return cardPostage;
    }

    public void setCardExpireDate(Date cardExpireDate)
    {
        this.cardExpireDate = cardExpireDate;
    }

    public Date getCardExpireDate()
    {
        return cardExpireDate;
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("stationType", getStationType())
                .append("deviceName", getDeviceName())
                .append("deviceSn", getDeviceSn())
                .append("channelId", getChannelId())
                .append("channelSn", getChannelSn())
                .append("connectModel", getConnectModel())
                .append("timeout", getTimeout())
                .append("onlineTime", getOnlineTime())
                .append("offlineTime", getOfflineTime())
                .append("isActive", getIsActive())
                .append("activeUpdateTime", getActiveUpdateTime())
                .append("connectAlarm", getConnectAlarm())
                .append("sbdz", getSbdz())
                .append("csyyLength", getCsyyLength())
                .append("ggdz", getGgdz())
                .append("ggdzLength", getGgdzLength())
                .append("infoLength", getInfoLength())
                .append("timeoutT0", getTimeoutT0())
                .append("timeoutT1", getTimeoutT1())
                .append("timeoutT2", getTimeoutT2())
                .append("timeoutT3", getTimeoutT3())
                .append("assCallCycle", getAssCallCycle())
                .append("elcCallCycle", getElcCallCycle())
                .append("dataArea", getDataArea())
                .append("proType", getProType())
                .append("proFactory", getProFactory())
                .append("proVer", getProVer())
                .append("proSn", getProSn())
                .append("proModel", getProModel())
                .append("cardOperator", getCardOperator())
                .append("cardNo", getCardNo())
                .append("cardIp", getCardIp())
                .append("cardFlow", getCardFlow())
                .append("cardPostage", getCardPostage())
                .append("cardExpireDate", getCardExpireDate())
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
