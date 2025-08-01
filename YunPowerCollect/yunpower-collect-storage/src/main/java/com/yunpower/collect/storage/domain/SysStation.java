package com.yunpower.collect.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 电站对象 sys_station
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
@Data
public class SysStation extends BaseEntity
{
    /** 编号ID */
    private Long id;

    /** 企业ID */
    private Long entId;

    /** 部门ID */
    private Long deptId;

    /** 上级ID */
    private Long parentId;

    /** 分组ID */
    private Long groupId;

    /** 电站名称 */
    private String stationName;

    /** 电站编号 */
    private String stationSn;

    /** 记录类型（1分组 2站点） */
    private Integer groupOrStation;

    /** 电站类型（1配电 2光伏） */
    private Integer stationType;

    /** 光伏类型 */
    private Integer pvType;

    /** 逻辑代码（企业+站点） */
    private String logicCode;

    /** 建站时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buildSiteTime;

    /** 电压等级 */
    private String voltageLevel;

    /** 最大容量 */
    private Integer capacityKva;

    /** 装机容量 */
    private Integer sationVolume;

    /** 方位角 */
    private Double azimuth;

    /** 组件倾角 */
    private Double dipAngle;

    /** 投运时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date useSiteTime;

    /** 电站坐标 */
    private String coordinate;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private Long province;

    /**
     * 市
     */
    private Long city;

    /**
     * 县（区）
     */
    private Long county;

    /** 电站地址 */
    private String stationAddress;

    /** 电站照片 */
    private String picUrl;

    /** 电站简介 */
    private String description;

    /** 联系人员 */
    private String linkName;

    /** 联系电话 */
    private String linkPhone;

    /** 报警开关 */
    private Integer openAlarm;

    /** 电价标准 */
    private Long schemeId;

    /** 显示顺序 */
    private Integer orderNum;

    /** 是否停用（0正常 1停用） */
    private Integer stopFlag;

    /** 是否删除（0正常1删除） */
    private Integer deleteFlag;
}
