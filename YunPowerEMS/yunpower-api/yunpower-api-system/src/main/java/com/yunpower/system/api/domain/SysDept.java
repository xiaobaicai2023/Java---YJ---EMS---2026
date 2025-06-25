package com.yunpower.system.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门（由企业、分组、站点三生成）对象 sys_common_dept
 * SysUser 有引用，所以放到此处
 *
 * @author yunpower
 * @date 2023-09-13
 */
@ApiModel("部门对象")
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    @ApiModelProperty("编号ID")
    private Long id;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    @Excel(name = "企业ID")
    private Long entId;

    /**
     * 上级ID（部门ID）
     */
    @ApiModelProperty("上级ID")
    @Excel(name = "上级ID", readConverterExp = "部门ID")
    private Long parentId;

    /**
     * 来源类型（1企业 2站点）
     */
    @ApiModelProperty("来源类型（1企业 2站点）")
    @Excel(name = "来源类型", readConverterExp = "1=企业,2=站点")
    private Integer originType;

    /**
     * 对应ID
     */
    @ApiModelProperty("对应ID")
    @Excel(name = "对应ID")
    private Long correId;

    /**
     * 逻辑代码
     */
    @ApiModelProperty("逻辑代码")
    @Excel(name = "逻辑代码")
    private String ancestors;

    /**
     * 是否可选择（0否 1是）
     */
    @ApiModelProperty("是否可选择（0否 1是）")
    @Excel(name = "是否可选择", readConverterExp = "0=否,1=是")
    private Integer isCanSelect;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    @Excel(name = "部门名称")
    private String deptName;

    /**
     * 部门编码
     */
    @ApiModelProperty("部门编码")
    @Excel(name = "部门编码")
    private String deptSn;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    @Excel(name = "负责人")
    private String leader;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    @Excel(name = "联系电话")
    private String mobile;

    /**
     * 联系邮箱
     */
    @ApiModelProperty("联系邮箱")
    @Excel(name = "联系邮箱")
    private String email;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    /**
     * 是否停用（0正常 1停用）
     */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /**
     * 是否删除（0正常 1删除）
     */
    @ApiModelProperty("是否删除（0正常 1删除）")
    private Integer deleteFlag;

    /**
     * 父部门名称
     */
    private String parentName;

    /**
     * 站点类型
     */
    private Integer stationType;

    /**
     * 子部门
     */
    private List<SysDept> children = new ArrayList<SysDept>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOriginType(Integer originType) {
        this.originType = originType;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getOriginType() {
        return originType;
    }

    public void setCorreId(Long correId) {
        this.correId = correId;
    }

    public Long getCorreId() {
        return correId;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getAncestors() {
        return ancestors;
    }

    public Integer getIsCanSelect() {
        return isCanSelect;
    }

    public void setIsCanSelect(Integer isCanSelect) {
        this.isCanSelect = isCanSelect;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    public String getDeptName() {
        return deptName;
    }

    public String getDeptSn() {
        return deptSn;
    }

    public void setDeptSn(String deptSn) {
        this.deptSn = deptSn;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeader() {
        return leader;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getMobile() {
        return mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 100, message = "邮箱长度不能超过100个字符")
    public String getEmail() {
        return email;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setStopFlag(Integer stopFlag) {
        this.stopFlag = stopFlag;
    }

    public Integer getStopFlag() {
        return stopFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getStationType() {
        return stationType;
    }

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    public List<SysDept> getChildren() {
        return children;
    }

    public void setChildren(List<SysDept> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("parentId", getParentId())
                .append("originType", getOriginType())
                .append("correId", getCorreId())
                .append("ancestors", getAncestors())
                .append("deptName", getDeptName())
                .append("leader", getLeader())
                .append("mobile", getMobile())
                .append("email", getEmail())
                .append("orderNum", getOrderNum())
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
