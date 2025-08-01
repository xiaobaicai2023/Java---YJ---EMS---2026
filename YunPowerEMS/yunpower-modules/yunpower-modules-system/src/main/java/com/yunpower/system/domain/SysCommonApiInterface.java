package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 数据接口对象 sys_common_api_interface
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("数据接口对象")
public class SysCommonApiInterface extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 接口名称 */
    @ApiModelProperty("接口名称")
    @Excel(name = "接口名称")
    private String interfaceName;

    /** 接口方法 */
    @ApiModelProperty("接口方法")
    @Excel(name = "接口方法")
    private String interfaceMethod;

    /** 请求路径 */
    @ApiModelProperty("请求路径")
    @Excel(name = "请求路径")
    private String requestPath;

    /** 请求方式（1GET 2POST 3PUT 4PATCH 5DELETE 6HEAD 7OPTIONS） */
    @ApiModelProperty("请求方式（1GET 2POST 3PUT 4PATCH 5DELETE 6HEAD 7OPTIONS）")
    @Excel(name = "请求方式", readConverterExp = "1=GET,2=POST,3=PUT,4=PATCH,5=DELETE,6=HEAD,7=OPTIONS")
    private Integer requestType;

    /** 请求参数 */
    @ApiModelProperty("请求参数")
    @Excel(name = "请求参数")
    private String requestQuery;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setInterfaceName(String interfaceName)
    {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceName()
    {
        return interfaceName;
    }

    public void setInterfaceMethod(String interfaceMethod)
    {
        this.interfaceMethod = interfaceMethod;
    }

    public String getInterfaceMethod()
    {
        return interfaceMethod;
    }

    public void setRequestPath(String requestPath)
    {
        this.requestPath = requestPath;
    }

    public String getRequestPath()
    {
        return requestPath;
    }

    public void setRequestType(Integer requestType)
    {
        this.requestType = requestType;
    }

    public Integer getRequestType()
    {
        return requestType;
    }

    public void setRequestQuery(String requestQuery)
    {
        this.requestQuery = requestQuery;
    }

    public String getRequestQuery()
    {
        return requestQuery;
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


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("interfaceName", getInterfaceName())
                .append("interfaceMethod", getInterfaceMethod())
                .append("requestPath", getRequestPath())
                .append("requestType", getRequestType())
                .append("requestQuery", getRequestQuery())
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
