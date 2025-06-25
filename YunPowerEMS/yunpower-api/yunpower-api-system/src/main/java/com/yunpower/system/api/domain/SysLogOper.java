package com.yunpower.system.api.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 操作日志记录对象 sys_log_oper
 * 
 * @author yunpower
 * @date 2023-09-14
 */
@ApiModel("操作日志记录对象")
public class SysLogOper extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 模块标题 */
    @ApiModelProperty("模块标题")
    @Excel(name = "模块标题")
    private String title;

    /** 业务类型（0其它 1新增 2修改 3删除） */
    @ApiModelProperty("业务类型")
    @Excel(name = "业务类型", readConverterExp = "0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据")
    private Integer businessType;

    /** 业务类型数组 */
    private Integer[] businessTypes;

    /** 方法名称 */
    @ApiModelProperty("方法名称")
    @Excel(name = "方法名称")
    private String method;

    /** 请求方式 */
    @ApiModelProperty("请求方式")
    @Excel(name = "请求方式")
    private String requestMethod;

    /** 操作类别（0其它 1后台用户 2手机端用户） */
    @ApiModelProperty("操作类别（0其它 1后台用户 2手机端用户）")
    @Excel(name = "操作类别", readConverterExp = "0=其它,1=后台用户,2=手机端用户")
    private Integer operatorType;

    /** 操作人员 */
    @ApiModelProperty("操作人员")
    @Excel(name = "操作人员")
    private String operName;

    /** 部门名称 */
    @ApiModelProperty("部门名称")
    @Excel(name = "部门名称")
    private String deptName;

    /** 请求URL */
    @ApiModelProperty("请求URL")
    @Excel(name = "请求URL")
    private String operUrl;

    /** 主机地址 */
    @ApiModelProperty("主机地址")
    @Excel(name = "主机地址")
    private String operIp;

    /** 操作地点 */
    @ApiModelProperty("操作地点")
    @Excel(name = "操作地点")
    private String operLocation;

    /** 请求参数 */
    @ApiModelProperty("请求参数")
    @Excel(name = "请求参数")
    private String operParam;

    /** 返回参数 */
    @ApiModelProperty("返回参数")
    @Excel(name = "返回参数")
    private String jsonResult;

    /** 操作状态（0正常 1异常） */
    @ApiModelProperty("操作状态（0正常 1异常）")
    @Excel(name = "操作状态", readConverterExp = "0=正常,1=异常")
    private Integer status;

    /** 错误消息 */
    @ApiModelProperty("错误消息")
    @Excel(name = "错误消息")
    private String errorMsg;

    /** 操作时间 */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /** 消耗时间 */
    @ApiModelProperty("消耗时间")
    @Excel(name = "消耗时间", suffix = "毫秒")
    private Long costTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setBusinessType(Integer businessType) 
    {
        this.businessType = businessType;
    }

    public Integer getBusinessType() 
    {
        return businessType;
    }

    public Integer[] getBusinessTypes()
    {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes)
    {
        this.businessTypes = businessTypes;
    }

    public void setMethod(String method) 
    {
        this.method = method;
    }

    public String getMethod() 
    {
        return method;
    }
    public void setRequestMethod(String requestMethod) 
    {
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod() 
    {
        return requestMethod;
    }
    public void setOperatorType(Integer operatorType) 
    {
        this.operatorType = operatorType;
    }

    public Integer getOperatorType() 
    {
        return operatorType;
    }
    public void setOperName(String operName) 
    {
        this.operName = operName;
    }

    public String getOperName() 
    {
        return operName;
    }
    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }
    public void setOperUrl(String operUrl) 
    {
        this.operUrl = operUrl;
    }

    public String getOperUrl() 
    {
        return operUrl;
    }
    public void setOperIp(String operIp) 
    {
        this.operIp = operIp;
    }

    public String getOperIp() 
    {
        return operIp;
    }
    public void setOperLocation(String operLocation) 
    {
        this.operLocation = operLocation;
    }

    public String getOperLocation() 
    {
        return operLocation;
    }
    public void setOperParam(String operParam) 
    {
        this.operParam = operParam;
    }

    public String getOperParam() 
    {
        return operParam;
    }
    public void setJsonResult(String jsonResult) 
    {
        this.jsonResult = jsonResult;
    }

    public String getJsonResult() 
    {
        return jsonResult;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setErrorMsg(String errorMsg) 
    {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() 
    {
        return errorMsg;
    }
    public void setOperTime(Date operTime) 
    {
        this.operTime = operTime;
    }

    public Date getOperTime() 
    {
        return operTime;
    }
    public void setCostTime(Long costTime)
    {
        this.costTime = costTime;
    }

    public Long getCostTime()
    {
        return costTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("businessType", getBusinessType())
            .append("method", getMethod())
            .append("requestMethod", getRequestMethod())
            .append("operatorType", getOperatorType())
            .append("operName", getOperName())
            .append("deptName", getDeptName())
            .append("operUrl", getOperUrl())
            .append("operIp", getOperIp())
            .append("operLocation", getOperLocation())
            .append("operParam", getOperParam())
            .append("jsonResult", getJsonResult())
            .append("status", getStatus())
            .append("errorMsg", getErrorMsg())
            .append("operTime", getOperTime())
            .append("costTime", getCostTime())
            .toString();
    }
}
