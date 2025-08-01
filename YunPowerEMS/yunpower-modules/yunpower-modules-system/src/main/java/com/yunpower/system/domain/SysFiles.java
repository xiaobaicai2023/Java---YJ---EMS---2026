package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 上传文件管理对象 sys_files
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("上传文件管理对象")
public class SysFiles extends BaseEntity
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

    /** 数据表名 */
    @ApiModelProperty("数据表名")
    @Excel(name = "数据表名")
    private String tableName;

    /** 记录ID */
    @ApiModelProperty("记录ID")
    @Excel(name = "记录ID")
    private Long recordId;

    /** 文件服务提供商（0本地存储 1腾讯云 2阿里云） */
    @ApiModelProperty("文件服务提供商（0本地存储 1腾讯云 2阿里云）")
    @Excel(name = "文件服务提供商", readConverterExp = "0=本地存储,1=腾讯云,2=阿里云")
    private Integer serviceType;

    /** 文件名称 */
    @ApiModelProperty("文件名称")
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件大小 */
    @ApiModelProperty("文件大小")
    @Excel(name = "文件大小")
    private Integer fileSize;

    /** 文件类型 */
    @ApiModelProperty("文件类型")
    @Excel(name = "文件类型")
    private String contentType;

    /** 本地路径 */
    @ApiModelProperty("本地路径")
    @Excel(name = "本地路径")
    private String absolutePath;

    /** 存储路径 */
    @ApiModelProperty("存储路径")
    @Excel(name = "存储路径")
    private String savePath;

    /** 存储桶 */
    @ApiModelProperty("存储桶")
    @Excel(name = "存储桶")
    private String bucketName;

    /** 唯一标识 */
    @ApiModelProperty("唯一标识")
    @Excel(name = "唯一标识")
    private String onlyKey;

    /** 存储频率 */
    @ApiModelProperty("存储频率")
    @Excel(name = "存储频率")
    private String storageClass;

    /** ETag */
    @ApiModelProperty("ETag")
    @Excel(name = "ETag")
    private String etag;

    /** 访问地址 */
    @ApiModelProperty("访问地址")
    @Excel(name = "访问地址")
    private String webUrl;

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

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getRecordId()
    {
        return recordId;
    }

    public void setServiceType(Integer serviceType)
    {
        this.serviceType = serviceType;
    }

    public Integer getServiceType()
    {
        return serviceType;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileSize(Integer fileSize)
    {
        this.fileSize = fileSize;
    }

    public Integer getFileSize()
    {
        return fileSize;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setAbsolutePath(String absolutePath)
    {
        this.absolutePath = absolutePath;
    }

    public String getAbsolutePath()
    {
        return absolutePath;
    }

    public void setSavePath(String savePath)
    {
        this.savePath = savePath;
    }

    public String getSavePath()
    {
        return savePath;
    }

    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    public String getBucketName()
    {
        return bucketName;
    }

    public void setOnlyKey(String onlyKey)
    {
        this.onlyKey = onlyKey;
    }

    public String getOnlyKey()
    {
        return onlyKey;
    }

    public void setStorageClass(String storageClass)
    {
        this.storageClass = storageClass;
    }

    public String getStorageClass()
    {
        return storageClass;
    }

    public void setEtag(String etag)
    {
        this.etag = etag;
    }

    public String getEtag()
    {
        return etag;
    }

    public void setWebUrl(String webUrl)
    {
        this.webUrl = webUrl;
    }

    public String getWebUrl()
    {
        return webUrl;
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
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("tableName", getTableName())
                .append("recordId", getRecordId())
                .append("serviceType", getServiceType())
                .append("fileName", getFileName())
                .append("fileSize", getFileSize())
                .append("contentType", getContentType())
                .append("absolutePath", getAbsolutePath())
                .append("savePath", getSavePath())
                .append("bucketName", getBucketName())
                .append("onlyKey", getOnlyKey())
                .append("storageClass", getStorageClass())
                .append("etag", getEtag())
                .append("webUrl", getWebUrl())
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
