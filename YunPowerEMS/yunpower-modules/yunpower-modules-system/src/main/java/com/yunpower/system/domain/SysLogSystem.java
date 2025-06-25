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
 * 系统日志对象 sys_log_system
 * 
 * @author JUNFU.WANG
 * @date 2024-01-18
 */
@ApiModel("系统日志对象")
public class SysLogSystem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 错误消息 */
    @ApiModelProperty("错误消息")
    @Excel(name = "错误消息")
    private String errorMsg;

    /** 发生时间 */
    @ApiModelProperty("发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发生时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date occurTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setErrorMsg(String errorMsg) 
    {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() 
    {
        return errorMsg;
    }

    public void setOccurTime(Date occurTime) 
    {
        this.occurTime = occurTime;
    }

    public Date getOccurTime() 
    {
        return occurTime;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("errorMsg", getErrorMsg())
            .append("occurTime", getOccurTime())
            .toString();
    }
}
