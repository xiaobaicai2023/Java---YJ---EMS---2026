package com.yunpower.collect.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yunpower.collect.storage.domain.handler.JsonCommonTypeHandler;
import com.yunpower.collect.storage.domain.jsonvo.JsonCommonVo;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * 报警类型对象 alarm_trigger_category
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Data
public class AlarmTriggerCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    private Long id;

    /** 企业ID */
    private Long entId;

    /** 部门ID */
    private Long deptId;

    /** 类型名称 */
    private String triggerName;

    /** 类型编码 */
    private String triggerSn;

    /** 报警级别 */
    private Integer triggerLevel;

    /** 触发类型 */
    private String triggerType;

    /** 适用类型（1模拟量 2状态量） */
    private Integer suitType;

    /** 报警提示 */
    private Integer isAlarm;

    /** 报警方式（JSON格式） */
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> alarmMethod;

    /** 是否停用（0正常 1停用） */
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    private Integer deleteFlag;

    /** 报警级别名称 */
    @Transient
    private String triggerLevelName;
}
