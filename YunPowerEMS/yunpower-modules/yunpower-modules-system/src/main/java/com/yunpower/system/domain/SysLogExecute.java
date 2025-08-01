package com.yunpower.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 执行日志对象 sys_log_execute
 * 
 * @author JUNFU.WANG
 * @date 2024-01-18
 */
@ApiModel("执行日志对象")
public class SysLogExecute extends BaseEntity
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

    /** 任务标题 */
    @ApiModelProperty("任务标题")
    @Excel(name = "任务标题")
    private String title;

    /** 执行方法 */
    @ApiModelProperty("执行方法")
    @Excel(name = "执行方法")
    private String method;

    /** 操作类别（0其它 1后台用户 2手机端用户） */
    @ApiModelProperty("操作类别（0其它 1后台用户 2手机端用户）")
    @Excel(name = "操作类别", readConverterExp = "0=其它,1=后台用户,2=手机端用户")
    private Integer operateType;

    /** 执行人员 */
    @ApiModelProperty("执行人员")
    @Excel(name = "执行人员")
    private String operateBy;

    /** 执行结果 */
    @ApiModelProperty("执行结果")
    @Excel(name = "执行结果")
    private String operateResult;

    /** 操作状态（0正常 1异常） */
    @ApiModelProperty("操作状态（0正常 1异常）")
    @Excel(name = "操作状态", readConverterExp = "0=正常,1=异常")
    private Integer status;

    /** 执行时间 */
    @ApiModelProperty("执行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date executeTime;

    /** 消耗时间（秒） */
    @ApiModelProperty("消耗时间（秒）")
    @Excel(name = "消耗时间", readConverterExp = "秒")
    private Integer costTime;

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

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setMethod(String method) 
    {
        this.method = method;
    }

    public String getMethod() 
    {
        return method;
    }

    public void setOperateType(Integer operateType) 
    {
        this.operateType = operateType;
    }

    public Integer getOperateType() 
    {
        return operateType;
    }

    public String getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(String operateBy) {
        this.operateBy = operateBy;
    }

    public void setOperateResult(String operateResult)
    {
        this.operateResult = operateResult;
    }

    public String getOperateResult() 
    {
        return operateResult;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setExecuteTime(Date executeTime) 
    {
        this.executeTime = executeTime;
    }

    public Date getExecuteTime() 
    {
        return executeTime;
    }

    public void setCostTime(Integer costTime) 
    {
        this.costTime = costTime;
    }

    public Integer getCostTime() 
    {
        return costTime;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("entId", getEntId())
            .append("deptId", getDeptId())
            .append("title", getTitle())
            .append("method", getMethod())
            .append("operateType", getOperateType())
            .append("operateBy", getOperateBy())
            .append("operateResult", getOperateResult())
            .append("status", getStatus())
            .append("executeTime", getExecuteTime())
            .append("costTime", getCostTime())
            .toString();
    }
}
