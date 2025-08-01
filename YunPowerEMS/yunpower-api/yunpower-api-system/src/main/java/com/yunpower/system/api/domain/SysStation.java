package com.yunpower.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import com.yunpower.common.core.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 电站对象 sys_station
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
@ApiModel("电站对象")
public class SysStation extends BaseEntity
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

    /** 上级ID */
    @ApiModelProperty("上级ID")
    @Excel(name = "上级ID")
    private Long parentId;

    /** 分组ID */
    @ApiModelProperty("分组ID")
    @Excel(name = "分组ID")
    private Long groupId;

    /** 电站名称 */
    @ApiModelProperty("电站名称")
    @Excel(name = "电站名称")
    private String stationName;

    /** 电站编号 */
    @ApiModelProperty("电站编号")
    @Excel(name = "电站编号")
    private String stationSn;

    /** 记录类型（1分组 2站点） */
    @ApiModelProperty("记录类型（1分组 2站点）")
    @Excel(name = "记录类型", readConverterExp = "1=分组,2=站点")
    private Integer groupOrStation;

    /** 电站类型（1配电 2光伏） */
    @ApiModelProperty("电站类型（1配电 2光伏）")
    @Excel(name = "电站类型", readConverterExp = "1=配电,2=光伏")
    private Integer stationType;

    /** 光伏类型 */
    @ApiModelProperty("光伏类型")
    @Excel(name = "光伏类型")
    private Integer pvType;

    /** 逻辑代码（企业+站点） */
    @ApiModelProperty("逻辑代码（企业+站点）")
    @Excel(name = "逻辑代码")
    private String logicCode;

    /** 建站时间 */
    @ApiModelProperty("建站时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "建站时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date buildSiteTime;

    /** 电压等级 */
    @ApiModelProperty("电压等级")
    @Excel(name = "电压等级")
    private String voltageLevel;

    /** 最大容量 */
    @ApiModelProperty("最大容量")
    @Excel(name = "最大容量")
    private Integer capacityKva;

    /** 设备上限 */
    @ApiModelProperty("设备上限")
    @Excel(name = "设备上限")
    private Integer deviceLimit;

    /** 测点上限 */
    @ApiModelProperty("测点上限")
    @Excel(name = "测点上限")
    private Integer varLimit;

    /** 装机容量 */
    @ApiModelProperty("装机容量")
    @Excel(name = "装机容量")
    private Integer sationVolume;

    /** 方位角 */
    @ApiModelProperty("方位角度")
    @Excel(name = "方位角度")
    private Double azimuth;

    /** 组件倾角 */
    @ApiModelProperty("组件倾角")
    @Excel(name = "组件倾角")
    private Double dipAngle;

    /** 投运时间 */
    @ApiModelProperty("投运时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投运时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date useSiteTime;

    /** 电站坐标 */
    @ApiModelProperty("电站坐标")
    @Excel(name = "电站坐标")
    private String coordinate;

    /**
     * 国家
     */
    @ApiModelProperty("国家")
    @Excel(name = "国家")
    private String country;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private Long province;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private Long city;

    /**
     * 县（区）
     */
    @ApiModelProperty("县（区）")
    private Long county;

    /** 电站地址 */
    @ApiModelProperty("电站地址")
    @Excel(name = "电站地址")
    private String stationAddress;

    /** 电站照片 */
    @ApiModelProperty("电站照片")
    @Excel(name = "电站照片")
    private String picUrl;

    /** 电站简介 */
    @ApiModelProperty("电站简介")
    @Excel(name = "电站简介")
    private String description;

    /** 联系人员 */
    @ApiModelProperty("联系人员")
    @Excel(name = "联系人员")
    private String linkName;

    /** 联系电话 */
    @ApiModelProperty("联系电话")
    @Excel(name = "联系电话")
    private String linkPhone;

    /** 报警开关 */
    @ApiModelProperty("报警开关")
    @Excel(name = "报警开关")
    private Integer openAlarm;

    /** 电价标准 */
    @ApiModelProperty("电价标准")
    @Excel(name = "电价标准")
    private Long schemeId;

    /** 显示顺序 */
    @ApiModelProperty("显示顺序")
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常1删除） */
    @ApiModelProperty("是否删除（0正常1删除）")
    private Integer deleteFlag;

    /** 父部门名称 */
    private String parentName;

    /** 子站点（子部门） */
    private List<SysStation> children = new ArrayList<SysStation>();


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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Long getGroupId()
    {
        return groupId;
    }

    public void setStationName(String stationName)
    {
        this.stationName = stationName;
    }

    @Xss(message = "站点名称不能包含脚本字符")
    @NotBlank(message = "站点名称不能为空")
    @Size(min = 0, max = 30, message = "站点名称长度不能超过30个字符")
    public String getStationName()
    {
        return stationName;
    }

    public void setStationSn(String stationSn)
    {
        this.stationSn = stationSn;
    }

    @NotBlank(message = "站点编号不能为空")
    public String getStationSn()
    {
        return stationSn;
    }

    public void setGroupOrStation(Integer groupOrStation)
    {
        this.groupOrStation = groupOrStation;
    }

    public Integer getGroupOrStation()
    {
        return groupOrStation;
    }

    public void setStationType(Integer stationType)
    {
        this.stationType = stationType;
    }

    public Integer getStationType()
    {
        return stationType;
    }

    public Integer getPvType() {
        return pvType;
    }

    public void setPvType(Integer pvType) {
        this.pvType = pvType;
    }

    public void setLogicCode(String logicCode)
    {
        this.logicCode = logicCode;
    }

    public String getLogicCode()
    {
        return logicCode;
    }

    public void setBuildSiteTime(Date buildSiteTime)
    {
        this.buildSiteTime = buildSiteTime;
    }

    public Date getBuildSiteTime()
    {
        return buildSiteTime;
    }

    public void setVoltageLevel(String voltageLevel)
    {
        this.voltageLevel = voltageLevel;
    }

    public String getVoltageLevel()
    {
        return voltageLevel;
    }

    public Integer getCapacityKva() {
        return capacityKva;
    }

    public void setCapacityKva(Integer capacityKva) {
        this.capacityKva = capacityKva;
    }

    public Integer getDeviceLimit() {
        return deviceLimit;
    }

    public void setDeviceLimit(Integer deviceLimit) {
        this.deviceLimit = deviceLimit;
    }

    public Integer getVarLimit() {
        return varLimit;
    }

    public void setVarLimit(Integer varLimit) {
        this.varLimit = varLimit;
    }

    public void setSationVolume(Integer sationVolume)
    {
        this.sationVolume = sationVolume;
    }

    public Integer getSationVolume()
    {
        return sationVolume;
    }

    public void setAzimuth(Double azimuth)
    {
        this.azimuth = azimuth;
    }

    public Double getAzimuth()
    {
        return azimuth;
    }

    public void setDipAngle(Double dipAngle)
    {
        this.dipAngle = dipAngle;
    }

    public Double getDipAngle()
    {
        return dipAngle;
    }

    public void setUseSiteTime(Date useSiteTime)
    {
        this.useSiteTime = useSiteTime;
    }

    public Date getUseSiteTime()
    {
        return useSiteTime;
    }

    public void setCoordinate(String coordinate)
    {
        this.coordinate = coordinate;
    }

    public String getCoordinate()
    {
        return coordinate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getProvince() {
        return province;
    }

    public void setProvince(Long province) {
        this.province = province;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getCounty() {
        return county;
    }

    public void setCounty(Long county) {
        this.county = county;
    }

    public void setStationAddress(String stationAddress)
    {
        this.stationAddress = stationAddress;
    }

    public String getStationAddress()
    {
        return stationAddress;
    }

    public void setPicUrl(String picUrl)
    {
        this.picUrl = picUrl;
    }

    public String getPicUrl()
    {
        return picUrl;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setLinkName(String linkName)
    {
        this.linkName = linkName;
    }

    public String getLinkName()
    {
        return linkName;
    }

    public void setLinkPhone(String linkPhone)
    {
        this.linkPhone = linkPhone;
    }

    public String getLinkPhone()
    {
        return linkPhone;
    }

    public void setOpenAlarm(Integer openAlarm)
    {
        this.openAlarm = openAlarm;
    }

    public Integer getOpenAlarm()
    {
        return openAlarm;
    }

    public Long getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Long schemeId) {
        this.schemeId = schemeId;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysStation> getChildren() {
        return children;
    }

    public void setChildren(List<SysStation> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("parentId", getParentId())
                .append("groupId", getGroupId())
                .append("stationName", getStationName())
                .append("stationSn", getStationSn())
                .append("groupOrStation", getGroupOrStation())
                .append("stationType", getStationType())
                .append("logicCode", getLogicCode())
                .append("buildSiteTime", getBuildSiteTime())
                .append("voltageLevel", getVoltageLevel())
                .append("capacityKva", getCapacityKva())
                .append("sationVolume", getSationVolume())
                .append("azimuth", getAzimuth())
                .append("dipAngle", getDipAngle())
                .append("useSiteTime", getUseSiteTime())
                .append("coordinate", getCoordinate())
                .append("country", getCountry())
                .append("province", getProvince())
                .append("city", getCity())
                .append("county", getCounty())
                .append("stationAddress", getStationAddress())
                .append("picUrl", getPicUrl())
                .append("description", getDescription())
                .append("linkName", getLinkName())
                .append("linkPhone", getLinkPhone())
                .append("openAlarm", getOpenAlarm())
                .append("schemeId", getSchemeId())
                .append("orderNum", getOrderNum())
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
