package com.yunpower.system.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.annotation.Excel.ColumnType;
import com.yunpower.common.core.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 角色信息对象 sys_role
 * SysUser 有引用，所以放到此处
 *
 * @author yunpower
 * @date 2023-09-13
 */
@ApiModel("角色信息对象")
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    @ApiModelProperty("编号ID")
    @Excel(name = "角色编号", cellType = ColumnType.NUMERIC)
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
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    @Excel(name = "角色名称")
    private String roleName;

    /**
     * 权限字符
     */
    @ApiModelProperty("权限字符")
    @Excel(name = "权限字符")
    private String roleKey;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /**
     * 菜单范围（1全部菜单 2仅浏览 3自定义菜单）
     */
    @ApiModelProperty("菜单范围（1全部菜单 2仅浏览 3自定义菜单）")
    @Excel(name = "菜单范围", readConverterExp = "1=全部菜单,2=仅浏览,3=自定义菜单")
    private Integer menuScope;

    /**
     * 数据范围（1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5仅本人数据权限）
     */
    @ApiModelProperty("数据范围")
    @Excel(name = "数据范围", readConverterExp = "1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private Integer dataScope;

    /**
     * 菜单树选择项是否关联显示（父子联动）
     */
    @ApiModelProperty("菜单树选择项是否关联显示")
    @Excel(name = "菜单树选择项是否关联显示")
    private boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示（父子联动）
     */
    @ApiModelProperty("部门树选择项是否关联显示")
    @Excel(name = "部门树选择项是否关联显示")
    private boolean deptCheckStrictly;

    /**
     * 是否停用（0正常 1停用）
     */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /**
     * 是否删除（0正常1删除）
     */
    @ApiModelProperty("是否删除（0正常 1删除）")
    private Integer deleteFlag;

    /** 扩展 */

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    private boolean flag = false;

    /**
     * 菜单组
     */
    private Long[] menuIds;

    /**
     * 部门组（数据权限）
     */
    private Long[] deptIds;

    /**
     * 角色菜单权限
     */
    private Set<String> permissions;

    public SysRole() {

    }

    public SysRole(Long roleId) {
        this.id = roleId;
    }

    public boolean isAdmin() {
        return isAdmin(this.dataScope);
    }

    public static boolean isAdmin(Integer dataScope) {
        return dataScope != null && 1 == dataScope;
    }

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

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey() {
        return roleKey;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setMenuScope(Integer menuScope) {
        this.menuScope = menuScope;
    }

    public Integer getMenuScope() {
        return menuScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public Integer getDataScope() {
        return dataScope;
    }

    public boolean isMenuCheckStrictly() {
        return menuCheckStrictly;
    }

    public void setMenuCheckStrictly(boolean menuCheckStrictly) {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public boolean isDeptCheckStrictly() {
        return deptCheckStrictly;
    }

    public void setDeptCheckStrictly(boolean deptCheckStrictly) {
        this.deptCheckStrictly = deptCheckStrictly;
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long[] getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds) {
        this.menuIds = menuIds;
    }

    public Long[] getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(Long[] deptIds) {
        this.deptIds = deptIds;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("roleName", getRoleName())
                .append("roleKey", getRoleKey())
                .append("orderNum", getOrderNum())
                .append("menuScope", getMenuScope())
                .append("dataScope", getDataScope())
                .append("menuCheckStrictly", isMenuCheckStrictly())
                .append("deptCheckStrictly", isDeptCheckStrictly())
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
