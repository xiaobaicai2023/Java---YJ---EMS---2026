package com.yunpower.collect.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yunpower.collect.storage.domain.handler.JsonCommonTypeHandler;
import com.yunpower.collect.storage.domain.jsonvo.JsonCommonVo;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * 报警配置对象 alarm_trigger_config
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Data
public class AlarmTriggerConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    private Long id;

    /** 企业ID */
    private Long entId;

    /** 部门ID */
    private Long deptId;

    /**
     * 电站类型
     */
    private Integer stationType;

    /** 设备ID */
    private Long deviceId;

    /** 设备编码 */
    private String deviceSn;

    /** 变量ID */
    private Long varId;

    /** 变量编码 */
    private String varSn;

    /** 变量类型（1模拟量 2状态量） */
    private Integer varType;

    /** 报警类型 */
    private Long categoryId;

    /** 阈值 */
    private Float threshold;

    /** 是否发送短信（0否 1是） */
    private Integer isSendSms;

    /** 接收人员类型（1按岗位发送 1指定具体人员） */
    private Integer receiveType;

    /** 具体岗位或人员（JSON格式） */
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> receiveConcrete;

    /** 报警开始时间 */
    private String startTime;

    /** 报警结束时间 */
    private String endTime;

    /** 是否停用（0正常 1停用） */
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    private Integer deleteFlag;

    /** 设备名称 */
    @Transient
    private String deviceName;

    /** 变量名称 */
    @Transient
    private String varName;

    /** 变量类型（1模拟量 2状态量） */
    @Transient
    private String varTypeName;

    /** 报警类型 */
    @Transient
    private String categoryName;

    /** 批量新增时用到 */
    @Transient
    private String[] varIds;

    /**
     * 报警类型
     * */
    @Transient
    private AlarmTriggerCategory category;

    /**
     * 表达式引擎
     * */
    @Transient
    private String aviator;

    /**
     * 触发条件
     */
    @Transient
    private String triggerConditionName;

    /**
     * 触发条件编码
     */
    @Transient
    private String triggerConditionSn;
}
