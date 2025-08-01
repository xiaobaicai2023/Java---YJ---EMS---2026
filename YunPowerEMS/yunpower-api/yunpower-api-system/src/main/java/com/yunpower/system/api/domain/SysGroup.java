package com.yunpower.system.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用分组对象 sys_group
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("常用分组对象")
public class SysGroup extends BaseEntity {
    private static final long serialVersionUID = 2457570053466550317L;
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
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @Excel(name = "部门ID")
    private Long deptId;

    /**
     * 字典枚举ID
     */
    @ApiModelProperty("字典枚举ID")
    @Excel(name = "字典枚举ID")
    private Long mapId;

    /**
     * 上级ID
     */
    @ApiModelProperty("上级ID")
    @Excel(name = "上级ID")
    private Long parentId;

    /**
     * 上级名称
     */
    @ApiModelProperty("上级名称")
    @Excel(name = "上级名称")
    private String parentName;

    /**
     * 类别名称
     */
    @ApiModelProperty("类别名称")
    @Excel(name = "类别名称")
    @Length(max = 20,message = "分组名称最多20位")
    private String groupName;

    /**
     * 类别代码
     */
    @ApiModelProperty("类别代码")
    @Excel(name = "类别代码")
    private String groupSn;

    /**
     * 逻辑代码
     */
    @ApiModelProperty("逻辑代码")
    @Excel(name = "逻辑代码")
    private String logicCode;

    /**
     * 级联逻辑代码（企业+分组）
     */
    @ApiModelProperty("级联逻辑代码（企业+分组）")
    @Excel(name = "级联逻辑代码")
    private String cascadeLogicCode;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /**
     * 是否显示（0否 1是）
     */
    @ApiModelProperty("是否显示（0否 1是）")
    @Excel(name = "是否显示", readConverterExp = "0=否,1=是")
    private Integer isShow;

    /**
     * 是否系统（0否 1是）
     */
    @ApiModelProperty("是否系统（0否 1是）")
    @Excel(name = "是否系统", readConverterExp = "0=否,1=是")
    private Integer isSystem;

    /**
     * 扩展字段1
     */
    @ApiModelProperty("扩展字段1")
    private Integer ext1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty("扩展字段2")
    private Integer ext2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty("扩展字段3")
    private String ext3;

    /**
     * 扩展字段4
     */
    @ApiModelProperty("扩展字段4")
    private String ext4;

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
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    /**
     * 子列表
     */
    private List<SysGroup> children = new ArrayList<SysGroup>();

    /**
     * 子列表
     * <p>
     * java 中泛型标记符：
     * E - Element (在集合中使用，因为集合中存放的是元素)
     * T - Type（Java 类）
     * K - Key（键）
     * V - Value（值）
     * N - Number（数值类型）
     * ？ - 表示不确定的 java 类型
     */
    private List<?> childrenList;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getEntId() {
        return entId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupSn(String groupSn) {
        this.groupSn = groupSn;
    }

    public String getGroupSn() {
        return groupSn;
    }

    public void setLogicCode(String logicCode) {
        this.logicCode = logicCode;
    }

    public String getLogicCode() {
        return logicCode;
    }

    public void setCascadeLogicCode(String cascadeLogicCode) {
        this.cascadeLogicCode = cascadeLogicCode;
    }

    public String getCascadeLogicCode() {
        return cascadeLogicCode;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

    public Integer getIsSystem() {
        return isSystem;
    }

    public Integer getExt1() {
        return ext1;
    }

    public void setExt1(Integer ext1) {
        this.ext1 = ext1;
    }

    public Integer getExt2() {
        return ext2;
    }

    public void setExt2(Integer ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
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

    public List<SysGroup> getChildren() {
        return children;
    }

    public void setChildren(List<SysGroup> children) {
        this.children = children;
    }

    public List<?> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<?> childrenList) {
        this.childrenList = childrenList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("mapId", getMapId())
                .append("parentId", getParentId())
                .append("parentName", getParentName())
                .append("groupName", getGroupName())
                .append("groupSn", getGroupSn())
                .append("logicCode", getLogicCode())
                .append("cascadeLogicCode", getCascadeLogicCode())
                .append("orderNum", getOrderNum())
                .append("isShow", getIsShow())
                .append("isSystem", getIsSystem())
                .append("remark", getRemark())
                .append("ext1", getExt1())
                .append("ext2", getExt2())
                .append("ext3", getExt3())
                .append("ext4", getExt4())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("stopFlag", getStopFlag())
                .append("deleteFlag", getDeleteFlag())
                .toString();
    }
}
