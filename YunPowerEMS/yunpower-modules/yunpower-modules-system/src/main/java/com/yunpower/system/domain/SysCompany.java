package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 公司对象 sys_company
 *
 * @author JUNFU.WANG
 * @date 2023-10-11
 */
@ApiModel("公司对象")
public class SysCompany extends BaseEntity
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

    /** 公司名称 */
    @ApiModelProperty("公司名称")
    @Excel(name = "公司名称")
    private String companyName;

    /** 公司英文名称 */
    @ApiModelProperty("公司英文名称")
    @Excel(name = "公司英文名称")
    private String companyNameEn;

    /** 逻辑代码 */
    @ApiModelProperty("逻辑代码")
    @Excel(name = "逻辑代码")
    private String logicCode;

    /** 公司分组 */
    @ApiModelProperty("公司分组")
    @Excel(name = "公司分组")
    private Integer groupId;

    /** 公司类型（1公司 2部门） */
    @ApiModelProperty("公司类型（1公司 2部门）")
    @Excel(name = "公司类型", readConverterExp = "1=公司,2=部门")
    private Integer companyType;

    /** 所属行业 */
    @ApiModelProperty("所属行业")
    @Excel(name = "所属行业")
    private Long industryGroupId;

    /** 公司Logo */
    @ApiModelProperty("公司Logo")
    @Excel(name = "公司Logo")
    private String logoUrl;

    /** 公司评级ABCD */
    @ApiModelProperty("公司评级ABCD")
    @Excel(name = "公司评级ABCD")
    private String companyGrade;

    /** 公司规模 */
    @ApiModelProperty("公司规模")
    @Excel(name = "公司规模")
    private String companyScale;

    /** 地址省 */
    @ApiModelProperty("地址省")
    @Excel(name = "地址省")
    private Integer province;

    /** 地址市 */
    @ApiModelProperty("地址市")
    @Excel(name = "地址市")
    private Integer city;

    /** 地址县 */
    @ApiModelProperty("地址县")
    @Excel(name = "地址县")
    private Integer county;

    /** 街道地址 */
    @ApiModelProperty("街道地址")
    @Excel(name = "街道地址")
    private String address;

    /** 邮政编码 */
    @ApiModelProperty("邮政编码")
    @Excel(name = "邮政编码")
    private String postCode;

    /** 联系人员 */
    @ApiModelProperty("联系人员")
    @Excel(name = "联系人员")
    private String linkName;

    /** 联系电话 */
    @ApiModelProperty("联系电话")
    @Excel(name = "联系电话")
    private String linkMobile;

    /** 固定电话 */
    @ApiModelProperty("固定电话")
    @Excel(name = "固定电话")
    private String linkPhone;

    /** 传真号码 */
    @ApiModelProperty("传真号码")
    @Excel(name = "传真号码")
    private String linkFax;

    /** 邮箱Email */
    @ApiModelProperty("邮箱Email")
    @Excel(name = "邮箱Email")
    private String email;

    /** 发票名称 */
    @ApiModelProperty("发票名称")
    @Excel(name = "发票名称")
    private String invoiceTitle;

    /** 发票税号 */
    @ApiModelProperty("发票税号")
    @Excel(name = "发票税号")
    private String invoiceTax;

    /** 发票地址 */
    @ApiModelProperty("发票地址")
    @Excel(name = "发票地址")
    private String invoiceAddress;

    /** 发票电话 */
    @ApiModelProperty("发票电话")
    @Excel(name = "发票电话")
    private String invoicePhone;

    /** 开户行 */
    @ApiModelProperty("开户行")
    @Excel(name = "开户行")
    private String invoiceBack;

    /** 银行帐号 */
    @ApiModelProperty("银行帐号")
    @Excel(name = "银行帐号")
    private String invoiceAccount;

    /** 公司简介 */
    @ApiModelProperty("公司简介")
    @Excel(name = "公司简介")
    private String description;

    /** 公司网址 */
    @ApiModelProperty("公司网址")
    @Excel(name = "公司网址")
    private String homePage;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    /** 行业名称 */
    @ApiModelProperty("行业名称")
    @Excel(name = "行业名称")
    private String industryGroupName;

    /** 子公司（子部门） */
    private List<SysCompany> children = new ArrayList<>();

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

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyNameEn(String companyNameEn)
    {
        this.companyNameEn = companyNameEn;
    }

    public String getCompanyNameEn()
    {
        return companyNameEn;
    }

    public void setLogicCode(String logicCode)
    {
        this.logicCode = logicCode;
    }

    public String getLogicCode()
    {
        return logicCode;
    }

    public void setGroupId(Integer groupId)
    {
        this.groupId = groupId;
    }

    public Integer getGroupId()
    {
        return groupId;
    }

    public void setCompanyType(Integer companyType)
    {
        this.companyType = companyType;
    }

    public Integer getCompanyType()
    {
        return companyType;
    }

    public void setIndustryGroupId(Long industryGroupId)
    {
        this.industryGroupId = industryGroupId;
    }

    public Long getIndustryGroupId()
    {
        return industryGroupId;
    }

    public void setLogoUrl(String logoUrl)
    {
        this.logoUrl = logoUrl;
    }

    public String getLogoUrl()
    {
        return logoUrl;
    }

    public void setCompanyGrade(String companyGrade)
    {
        this.companyGrade = companyGrade;
    }

    public String getCompanyGrade()
    {
        return companyGrade;
    }

    public void setCompanyScale(String companyScale)
    {
        this.companyScale = companyScale;
    }

    public String getCompanyScale()
    {
        return companyScale;
    }

    public void setProvince(Integer province)
    {
        this.province = province;
    }

    public Integer getProvince()
    {
        return province;
    }

    public void setCity(Integer city)
    {
        this.city = city;
    }

    public Integer getCity()
    {
        return city;
    }

    public void setCounty(Integer county)
    {
        this.county = county;
    }

    public Integer getCounty()
    {
        return county;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    public String getPostCode()
    {
        return postCode;
    }

    public void setLinkName(String linkName)
    {
        this.linkName = linkName;
    }

    public String getLinkName()
    {
        return linkName;
    }

    public void setLinkMobile(String linkMobile)
    {
        this.linkMobile = linkMobile;
    }

    public String getLinkMobile()
    {
        return linkMobile;
    }

    public void setLinkPhone(String linkPhone)
    {
        this.linkPhone = linkPhone;
    }

    public String getLinkPhone()
    {
        return linkPhone;
    }

    public void setLinkFax(String linkFax)
    {
        this.linkFax = linkFax;
    }

    public String getLinkFax()
    {
        return linkFax;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setInvoiceTitle(String invoiceTitle)
    {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceTitle()
    {
        return invoiceTitle;
    }

    public void setInvoiceTax(String invoiceTax)
    {
        this.invoiceTax = invoiceTax;
    }

    public String getInvoiceTax()
    {
        return invoiceTax;
    }

    public void setInvoiceAddress(String invoiceAddress)
    {
        this.invoiceAddress = invoiceAddress;
    }

    public String getInvoiceAddress()
    {
        return invoiceAddress;
    }

    public void setInvoicePhone(String invoicePhone)
    {
        this.invoicePhone = invoicePhone;
    }

    public String getInvoicePhone()
    {
        return invoicePhone;
    }

    public void setInvoiceBack(String invoiceBack)
    {
        this.invoiceBack = invoiceBack;
    }

    public String getInvoiceBack()
    {
        return invoiceBack;
    }

    public void setInvoiceAccount(String invoiceAccount)
    {
        this.invoiceAccount = invoiceAccount;
    }

    public String getInvoiceAccount()
    {
        return invoiceAccount;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setHomePage(String homePage)
    {
        this.homePage = homePage;
    }

    public String getHomePage()
    {
        return homePage;
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

    public List<SysCompany> getChildren() {
        return children;
    }

    public void setChildren(List<SysCompany> children) {
        this.children = children;
    }

    public String getIndustryGroupName() {
        return industryGroupName;
    }

    public void setIndustryGroupName(String industryGroupName) {
        this.industryGroupName = industryGroupName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("parentId", getParentId())
                .append("companyName", getCompanyName())
                .append("companyNameEn", getCompanyNameEn())
                .append("logicCode", getLogicCode())
                .append("companyGroup", getGroupId())
                .append("companyType", getCompanyType())
                .append("catalogId", getIndustryGroupId())
                .append("logoUrl", getLogoUrl())
                .append("companyGrade", getCompanyGrade())
                .append("companyScale", getCompanyScale())
                .append("province", getProvince())
                .append("city", getCity())
                .append("county", getCounty())
                .append("address", getAddress())
                .append("postCode", getPostCode())
                .append("linkName", getLinkName())
                .append("linkMobile", getLinkMobile())
                .append("linkPhone", getLinkPhone())
                .append("linkFax", getLinkFax())
                .append("email", getEmail())
                .append("invoiceTitle", getInvoiceTitle())
                .append("invoiceTax", getInvoiceTax())
                .append("invoiceAddress", getInvoiceAddress())
                .append("invoicePhone", getInvoicePhone())
                .append("invoiceBack", getInvoiceBack())
                .append("invoiceAccount", getInvoiceAccount())
                .append("description", getDescription())
                .append("homePage", getHomePage())
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
