package com.yunpower.collect.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yunpower.collect.storage.domain.handler.JsonCommonTypeHandler;
import com.yunpower.collect.storage.domain.jsonvo.JsonCommonVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

/**
 * 报警管理对象 alarm_trigger
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Data
public class AlarmTrigger extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long entId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 电站类型
     */
    private Integer stationType;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备编码
     */
    private String deviceSn;

    /**
     * 变量ID
     */
    private Long varId;

    /**
     * 变量编码
     */
    private String varSn;

    /**
     * 变量类型（1模拟量 2状态量）
     */
    private Integer varType;

    /**
     * 报警类型
     */
    private Long categoryId;

    /**
     * 报警级别
     */
    private Integer triggerLevel;

    /**
     * 触发条件
     */
    private String triggerConditionName;

    /**
     * 触发条件编码
     */
    private String triggerConditionSn;

    /**
     * 阈值
     */
    private Float threshold;

    /**
     * 实际值
     */
    private Double realValue;

    /**
     * 报警内容
     */
    private String triggerContent;

    /**
     * 是否发送短信（0否 1是）
     */
    private Integer isSendSms;

    /**
     * 接收人员类型（1按岗位发送 1指定具体人员）
     */
    private Integer receiveType;

    /**
     * 具体岗位或人员（JSON格式）
     */
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> receiveConcrete;

    /**
     * 发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date happenTime;

    /**
     * 恢复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recoverTime;

    /**
     * 确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

    /**
     * 确认人员
     */
    private String confirmBy;

    /**
     * 确认过程
     */
    private String confirmContent;

    /**
     * 是否自动恢复（0否 1是）
     */
    private Integer isAuto;

    /**
     * 报警状态
     */
    private Integer triggerStatus;

    /**
     * 是否停用（0正常 1停用）
     */
    private Integer stopFlag;

    /**
     * 是否删除（0正常 1删除）
     */
    private Integer deleteFlag;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 变量名称
     */
    private String varName;

    /**
     * 变量类型（1模拟量 2状态量）
     */
    @Transient
    private String varTypeName;

    /**
     * 报警类型
     */
    private String categoryName;

    /**
     * 报警状态
     */
    @Transient
    private String triggerStatusName;

    /**
     * 报警级别
     */
    private String triggerLevelName;

    /**
     * 报警名称
     * 报警名称为拼接：[设备名称]触发条件
     */
    @Transient
    private String triggerName;

    /**
     * 站点名称
     */
    @Transient
    private String stationName;

    /**
     * 统计数据
     */
    @Transient
    private Integer statisticValue;

    /**
     * 查询返回日期格式
     */
    @Transient
    private String formattedDatetime;

    /**
     * 日期维度
     */
    @Transient
    private Integer dateDim;

}
