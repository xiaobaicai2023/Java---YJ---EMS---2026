package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 主菜单对象 sys_common_menu
 * 
 * @author yunpower
 * @date 2023-09-14
 */
@ApiModel("主菜单对象")
public class SysCommonMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 菜单一级分类标识 */
    @ApiModelProperty("菜单一级分类标识")
    @Excel(name = "菜单一级分类标识")
    private Integer menuModel;

    /** 菜单名称 */
    @ApiModelProperty("菜单名称")
    @Excel(name = "菜单名称")
    private String menuName;

    /** 上级ID */
    @ApiModelProperty("上级ID")
    @Excel(name = "上级ID")
    private Long parentId;

    /** 父菜单名称 */
    private String parentName;

    /** 菜单类型（M目录 C菜单 F按钮） */
    @ApiModelProperty("菜单类型（M目录 C菜单 F按钮）")
    @Excel(name = "菜单类型", readConverterExp = "M=目录,C=菜单,F=按钮")
    private String menuType;

    /** 路由地址 */
    @ApiModelProperty("路由地址")
    @Excel(name = "路由地址")
    private String path;

    /** 请求路径 */
    @ApiModelProperty("请求路径")
    @Excel(name = "请求路径")
    private String requestUrl;

    /** 请求参数 */
    @ApiModelProperty("请求参数")
    @Excel(name = "请求参数")
    private String requestQuery;

    /** 权限标识 */
    @ApiModelProperty("权限标识")
    @Excel(name = "权限标识")
    private String permissionSign;

    /** 打开方式（0外链 1页签） */
    @ApiModelProperty("打开方式（0外链 1页签）")
    @Excel(name = "打开方式", readConverterExp = "0=外链,1=页签")
    private Integer openType;

    /** 是否缓存（0缓存 1不缓存） */
    @ApiModelProperty("是否缓存（0缓存 1不缓存）")
    private String isCache;

    /** 菜单图标 */
    @ApiModelProperty("菜单图标")
    @Excel(name = "菜单图标")
    private String icon;

    /** 菜单显示（0显示 1隐藏） */
    @ApiModelProperty("菜单显示（0显示 1隐藏）")
    @Excel(name = "菜单显示", readConverterExp = "0=显示,1=隐藏")
    private Integer visible;

    /** 显示顺序 */
    @ApiModelProperty("显示顺序")
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 是否拥有admin权限才可访问 */
    @ApiModelProperty("是否拥有admin权限才可访问")
    @Excel(name = "是否拥有admin权限才可访问")
    private Integer isAdminVisit;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    private Integer deleteFlag;

    /** 子菜单 */
    private List<SysCommonMenu> children = new ArrayList<SysCommonMenu>();

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public Integer getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(Integer menuModel) {
        this.menuModel = menuModel;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    public String getMenuName() 
    {
        return menuName;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    @NotBlank(message = "菜单类型不能为空")
    public String getMenuType() 
    {
        return menuType;
    }
    public void setPath(String path) 
    {
        this.path = path;
    }

    @Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
    public String getPath() 
    {
        return path;
    }
    public void setRequestUrl(String requestUrl) 
    {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() 
    {
        return requestUrl;
    }
    public void setRequestQuery(String requestQuery) 
    {
        this.requestQuery = requestQuery;
    }

    public String getRequestQuery() 
    {
        return requestQuery;
    }
    public void setPermissionSign(String permissionSign) 
    {
        this.permissionSign = permissionSign;
    }

    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    public String getPermissionSign() 
    {
        return permissionSign;
    }
    public void setOpenType(Integer openType) 
    {
        this.openType = openType;
    }

    public Integer getOpenType() 
    {
        return openType;
    }

    public String getIsCache() {
        return isCache;
    }

    public void setIsCache(String isCache) {
        this.isCache = isCache;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIcon() 
    {
        return icon;
    }
    public void setVisible(Integer visible) 
    {
        this.visible = visible;
    }

    public Integer getVisible() 
    {
        return visible;
    }
    public void setOrderNum(Integer orderNum) 
    {
        this.orderNum = orderNum;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() 
    {
        return orderNum;
    }

    public Integer getIsAdminVisit() {
        return isAdminVisit;
    }

    public void setIsAdminVisit(Integer isAdminVisit) {
        this.isAdminVisit = isAdminVisit;
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

    public List<SysCommonMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysCommonMenu> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("menuType", getMenuType())
            .append("path", getPath())
            .append("requestUrl", getRequestUrl())
            .append("requestQuery", getRequestQuery())
            .append("permissionSign", getPermissionSign())
            .append("openType", getOpenType())
            .append("IsCache", getIsCache())
            .append("icon", getIcon())
            .append("visible", getVisible())
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
