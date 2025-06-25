package com.yunpower.system.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunpower.system.domain.handler.AssociatedDevicesTypeHandler;
import com.yunpower.system.domain.jsonvo.AssociatedDevicesVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 能源监控设备对象 monitor_device
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("能源监控设备对象")
public class MonitorDevice extends BaseEntity
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

    /** 电能类型（1用电 2发电 3并网 4购电） */
    @ApiModelProperty("电能类型（1用电 2发电 3并网 4购电）")
    @Excel(name = "电能类型", readConverterExp = "1=用电,2=发电,3=并网,4=购电")
    private Integer electricType;

    /** 所属通道ID */
    @ApiModelProperty("所属通道ID")
    private Long channelId;

    /** 所属通道编码 */
    @ApiModelProperty("所属通道编码")
    @Excel(name = "通道编码")
    private String channelSn;

    /** 所属通信设备ID */
    @ApiModelProperty("所属通信设备ID")
    private Long comDeviceId;

    /** 所属通信设备编码 */
    @ApiModelProperty("所属通信设备编码")
    @Excel(name = "通信设备编码")
    private String comDeviceSn;

    /** 设备名称 */
    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备编号 */
    @ApiModelProperty("设备编码")
    @Excel(name = "设备编码，自动生成")
    private String deviceSn;

    /** 设备地址 */
    @ApiModelProperty("设备地址")
    @Excel(name = "设备地址")
    private Integer sbdz;

    /** 电表表号 */
    @ApiModelProperty("电表表号")
    @Excel(name = "电表表号")
    private String meterNumber;

    /** 设备分组 */
    @ApiModelProperty("设备分组")
    @Excel(name = "分组编号")
    private Long groupId;

    /** 是否关口表 */
    @ApiModelProperty("是否关口表")
    private Integer isPass;

    /** 生产厂家 */
    @ApiModelProperty("生产厂家")
    @Excel(name = "生产厂家编号")
    private Long proFactory;

    /** 设备类型 */
    @ApiModelProperty("设备类型")
    @Excel(name = "设备类型编号")
    private Long proType;

    /** 产品版本 */
    @ApiModelProperty("产品版本")
    @Excel(name = "产品版本")
    private String proVer;

    /** 购买价格 */
    @ApiModelProperty("购买价格")
    @Excel(name = "购买价格")
    private Float buyPrice;

    /** 产品SN号 */
    @ApiModelProperty("产品SN号")
    @Excel(name = "产品SN码")
    private String proSn;

    /** 产品型号 */
    @ApiModelProperty("产品型号")
    @Excel(name = "产品型号")
    private String proModel;

    /** 安规国家 */
    @ApiModelProperty("安规国家")
    @Excel(name = "安规国家")
    private String agStandard;

    /** 额定电压 */
    @ApiModelProperty("额定电压")
    @Excel(name = "额定电压")
    private Float ratedVol;

    /** 额定电流 */
    @ApiModelProperty("额定电流")
    @Excel(name = "额定电流")
    private Float ratedEle;

    /** 额定功率 */
    @ApiModelProperty("额定功率")
    @Excel(name = "额定功率")
    private Float ratedPower;

    /** 电流负载率 */
    @ApiModelProperty("电流负载率")
    @Excel(name = "电流负载率")
    private Float eleLoadRate;

    /** 电能质量分析 */
    @ApiModelProperty("电能质量分析")
    private Integer isAnalysisEnergy;

    /** 关联监控 */
    @ApiModelProperty("关联监控")
    private Integer monitorId;

    /** 发电属性 */
    @ApiModelProperty("发电属性")
    private String electricAttribute;

    /** 温度属性 */
    @ApiModelProperty("温度属性")
    private String temperatureAttribute;

    /** 其它属性 */
    @ApiModelProperty("其它属性")
    private String ortherAttribute;

    /** 组件容量 */
    @ApiModelProperty("组件容量")
    @Excel(name = "组件容量")
    private Float ratedVolume;

    /** 方位角度 */
    @ApiModelProperty("方位角")
    @Excel(name = "方位角")
    private Float azimuth;

    /** 倾角角度 */
    @ApiModelProperty("倾角")
    @Excel(name = "倾角")
    private Float dipAngle;

    /** 生产日期 */
    @ApiModelProperty("生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date manufactureDate;

    /** 使用年限 */
    @ApiModelProperty("使用年限")
    @Excel(name = "使用年限")
    private Integer useLife;

    /** 性能衰减 */
    @ApiModelProperty("性能衰减")
    @Excel(name = "性能衰减")
    private Float attenuationRate;

    /** 是否抄表 */
    @ApiModelProperty("是否抄表")
    private Integer isReading;

    /** 抄表变量 */
    @ApiModelProperty("抄表变量")
    private Integer readingVar;

    /** DTU端口 */
    @ApiModelProperty("DTU端口")
    private Integer dtuCmd;

    /** 绑定用户 */
    @ApiModelProperty("绑定用户")
    private Integer bindUserId;

    /** 付费模式 */
    @ApiModelProperty("付费模式")
    private Integer chargingType;

    /** 付费规则 */
    @ApiModelProperty("付费规则")
    private Integer ruleId;

    /** 付费周期 */
    @ApiModelProperty("付费周期")
    private Integer checkoutType;

    /** 关联设备（JSON格式） */
    @ApiModelProperty("关联设备（JSON格式）")
    @TableField(typeHandler = AssociatedDevicesTypeHandler.class)
    private List<AssociatedDevicesVo> associatedDevices;

    /** 连接超时（s） */
    @ApiModelProperty("连接超时")
    @Excel(name = "连接超时")
    private Integer timeout;

    /** 上线时间 */
    @ApiModelProperty("上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;

    /** 下线时间 */
    @ApiModelProperty("下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date offlineTime;

    /** 是否激活 */
    @ApiModelProperty("是否激活")
    private Integer isActive;

    /** 激活时间 */
    @ApiModelProperty("激活时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activeUpdateTime;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    private Integer deleteFlag;

    /** 是否分组 */
    @Transient
    private Integer isGroup = 0;

    /**
     * 列表类型
     * 图表页面左树展示类型
     * //情形0：全部
     * //情形1：逆变器
     * //情形2：汇流箱、并网柜、电能表、气象站
     * //情形3：逆变器、汇流箱、并网柜、电能表
     */
    @Transient
    private Integer listStyle = 0;

    /**
     * 分组类型：2用电分组 3光伏分组
     */
    @Transient
    private Long mapId = 2L;

    /** 子列表 */
    List<MonitorDevice> children = new ArrayList<>();

    /** 通道名称 */
    @Transient
    private String channelName;

    /** 通讯设备名称 */
    @Transient
    private String comDeviceName;

    /** 分组名称 */
    @Transient
    private String groupName;

    /** 厂家名称 */
    @Transient
    private String companyName;

    /** 设备类型 */
    @Transient
    private String proTypeName;

    /** 监控设备日实时数据 - 扩展 */
    private MonitorDeviceDay deviceDayRuntime;

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

    public void setElectricType(Integer electricType)
    {
        this.electricType = electricType;
    }

    public Integer getElectricType()
    {
        return electricType;
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

    public Integer getSbdz() {
        return sbdz;
    }

    public void setSbdz(Integer sbdz) {
        this.sbdz = sbdz;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Long getGroupId()
    {
        return groupId;
    }

    public void setIsPass(Integer isPass)
    {
        this.isPass = isPass;
    }

    public Integer getIsPass()
    {
        return isPass;
    }

    public void setProFactory(Long proFactory)
    {
        this.proFactory = proFactory;
    }

    public Long getProFactory()
    {
        return proFactory;
    }

    public void setProType(Long proType)
    {
        this.proType = proType;
    }

    public Long getProType()
    {
        return proType;
    }

    public void setProVer(String proVer)
    {
        this.proVer = proVer;
    }

    public String getProVer()
    {
        return proVer;
    }

    public void setBuyPrice(Float buyPrice)
    {
        this.buyPrice = buyPrice;
    }

    public Float getBuyPrice()
    {
        return buyPrice;
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

    public void setAgStandard(String agStandard)
    {
        this.agStandard = agStandard;
    }

    public String getAgStandard()
    {
        return agStandard;
    }

    public void setRatedVol(Float ratedVol)
    {
        this.ratedVol = ratedVol;
    }

    public Float getRatedVol()
    {
        return ratedVol;
    }

    public void setRatedEle(Float ratedEle)
    {
        this.ratedEle = ratedEle;
    }

    public Float getRatedEle()
    {
        return ratedEle;
    }

    public void setRatedPower(Float ratedPower)
    {
        this.ratedPower = ratedPower;
    }

    public Float getRatedPower()
    {
        return ratedPower;
    }

    public void setEleLoadRate(Float eleLoadRate)
    {
        this.eleLoadRate = eleLoadRate;
    }

    public Float getEleLoadRate()
    {
        return eleLoadRate;
    }

    public void setIsAnalysisEnergy(Integer isAnalysisEnergy)
    {
        this.isAnalysisEnergy = isAnalysisEnergy;
    }

    public Integer getIsAnalysisEnergy()
    {
        return isAnalysisEnergy;
    }

    public void setMonitorId(Integer monitorId)
    {
        this.monitorId = monitorId;
    }

    public Integer getMonitorId()
    {
        return monitorId;
    }

    public void setElectricAttribute(String electricAttribute)
    {
        this.electricAttribute = electricAttribute;
    }

    public String getElectricAttribute()
    {
        return electricAttribute;
    }

    public void setTemperatureAttribute(String temperatureAttribute)
    {
        this.temperatureAttribute = temperatureAttribute;
    }

    public String getTemperatureAttribute()
    {
        return temperatureAttribute;
    }

    public void setOrtherAttribute(String ortherAttribute)
    {
        this.ortherAttribute = ortherAttribute;
    }

    public String getOrtherAttribute()
    {
        return ortherAttribute;
    }

    public void setIsReading(Integer isReading)
    {
        this.isReading = isReading;
    }

    public Integer getIsReading()
    {
        return isReading;
    }

    public void setRatedVolume(Float ratedVolume)
    {
        this.ratedVolume = ratedVolume;
    }

    public Float getRatedVolume()
    {
        return ratedVolume;
    }

    public void setAzimuth(Float azimuth)
    {
        this.azimuth = azimuth;
    }

    public Float getAzimuth()
    {
        return azimuth;
    }

    public void setDipAngle(Float dipAngle)
    {
        this.dipAngle = dipAngle;
    }

    public Float getDipAngle()
    {
        return dipAngle;
    }

    public void setManufactureDate(Date manufactureDate)
    {
        this.manufactureDate = manufactureDate;
    }

    public Date getManufactureDate()
    {
        return manufactureDate;
    }

    public void setUseLife(Integer useLife)
    {
        this.useLife = useLife;
    }

    public Integer getUseLife()
    {
        return useLife;
    }

    public void setAttenuationRate(Float attenuationRate)
    {
        this.attenuationRate = attenuationRate;
    }

    public Float getAttenuationRate()
    {
        return attenuationRate;
    }

    public void setReadingVar(Integer readingVar)
    {
        this.readingVar = readingVar;
    }

    public Integer getReadingVar()
    {
        return readingVar;
    }

    public void setDtuCmd(Integer dtuCmd)
    {
        this.dtuCmd = dtuCmd;
    }

    public Integer getDtuCmd()
    {
        return dtuCmd;
    }

    public void setBindUserId(Integer bindUserId)
    {
        this.bindUserId = bindUserId;
    }

    public Integer getBindUserId()
    {
        return bindUserId;
    }

    public void setChargingType(Integer chargingType)
    {
        this.chargingType = chargingType;
    }

    public Integer getChargingType()
    {
        return chargingType;
    }

    public void setRuleId(Integer ruleId)
    {
        this.ruleId = ruleId;
    }

    public Integer getRuleId()
    {
        return ruleId;
    }

    public void setCheckoutType(Integer checkoutType)
    {
        this.checkoutType = checkoutType;
    }

    public Integer getCheckoutType()
    {
        return checkoutType;
    }

    public List<AssociatedDevicesVo> getAssociatedDevices() {
        return associatedDevices;
    }

    public void setAssociatedDevices(List<AssociatedDevicesVo> associatedDevices) {
        this.associatedDevices = associatedDevices;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(Date offlineTime) {
        this.offlineTime = offlineTime;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Date getActiveUpdateTime() {
        return activeUpdateTime;
    }

    public void setActiveUpdateTime(Date activeUpdateTime) {
        this.activeUpdateTime = activeUpdateTime;
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

    public Integer getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Integer isGroup) {
        this.isGroup = isGroup;
    }

    public Integer getListStyle() {
        return listStyle;
    }

    public void setListStyle(Integer listStyle) {
        this.listStyle = listStyle;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public List<MonitorDevice> getChildren() {
        return children;
    }

    public void setChildren(List<MonitorDevice> children) {
        this.children = children;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProTypeName() {
        return proTypeName;
    }

    public void setProTypeName(String proTypeName) {
        this.proTypeName = proTypeName;
    }

    public MonitorDeviceDay getDeviceDayRuntime() {
        if (deviceDayRuntime == null) {
            return new MonitorDeviceDay();
        } else {
            return deviceDayRuntime;
        }
    }

    public void setDeviceDayRuntime(MonitorDeviceDay deviceDayRuntime) {
        this.deviceDayRuntime = deviceDayRuntime;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("stationType", getStationType())
                .append("electricType", getElectricType())
                .append("channelId", getChannelId())
                .append("channelSn", getChannelSn())
                .append("comDeviceId", getComDeviceId())
                .append("comDeviceSn", getComDeviceSn())
                .append("deviceName", getDeviceName())
                .append("deviceSn", getDeviceSn())
                .append("catalogId", getGroupId())
                .append("isPass", getIsPass())
                .append("proFactory", getProFactory())
                .append("proType", getProType())
                .append("proVer", getProVer())
                .append("buyPrice", getBuyPrice())
                .append("proSn", getProSn())
                .append("proModel", getProModel())
                .append("agStandard", getAgStandard())
                .append("ratedVol", getRatedVol())
                .append("ratedEle", getRatedEle())
                .append("ratedPower", getRatedPower())
                .append("eleLoadRate", getEleLoadRate())
                .append("isAnalysisEnergy", getIsAnalysisEnergy())
                .append("monitorId", getMonitorId())
                .append("electricAttribute", getElectricAttribute())
                .append("temperatureAttribute", getTemperatureAttribute())
                .append("ortherAttribute", getOrtherAttribute())
                .append("isReading", getIsReading())
                .append("ratedVolume", getRatedVolume())
                .append("azimuth", getAzimuth())
                .append("dipAngle", getDipAngle())
                .append("manufactureDate", getManufactureDate())
                .append("useLife", getUseLife())
                .append("attenuationRate", getAttenuationRate())
                .append("readingVar", getReadingVar())
                .append("dtuCmd", getDtuCmd())
                .append("bindUserId", getBindUserId())
                .append("chargingType", getChargingType())
                .append("ruleId", getRuleId())
                .append("checkoutType", getCheckoutType())
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
