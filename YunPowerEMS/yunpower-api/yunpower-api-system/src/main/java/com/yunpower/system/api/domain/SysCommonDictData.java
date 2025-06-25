package com.yunpower.system.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.annotation.Excel.ColumnType;
import com.yunpower.common.core.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 通用字典-数据对象 sys_common_dict_data
 * yunpower-common-security 中有引用，所以放到此处
 *
 * @author yunpower
 * @date 2023-09-14
 */
@ApiModel("通用字典-数据对象")
public class SysCommonDictData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    @ApiModelProperty("编号ID")
    private Long id;

    /**
     * 字典编码
     */
    @ApiModelProperty("字典编码")
    @Excel(name = "字典编码")
    private String dictSn;

    /**
     * 字典标签
     */
    @ApiModelProperty("字典标签")
    @Excel(name = "字典标签")
    private String dictLabel;

    /**
     * 字典键值
     */
    @ApiModelProperty("字典键值")
    @Excel(name = "字典键值")
    private String dictValue;

    /**
     * 字典排序
     */
    @ApiModelProperty("字典排序")
    @Excel(name = "字典排序", cellType = ColumnType.NUMERIC)
    private Integer orderNum;

    /**
     * 图标样式
     */
    @ApiModelProperty("图标样式")
    @Excel(name = "图标样式")
    private String iconClass;

    /**
     * 表格回显样式
     */
    @ApiModelProperty("表格回显样式")
    @Excel(name = "表格回显样式")
    private String listClass;

    /**
     * 是否默认（1是 0否）
     */
    @ApiModelProperty("是否默认（1是 0否）")
    @Excel(name = "是否默认", readConverterExp = "1=是,0=否")
    private Integer isDefault;

    /**
     * 是否多级（1是 0否）
     */
    @ApiModelProperty("是否多级（1是 0否）")
    @Excel(name = "是否多级", readConverterExp = "1=是,0=否")
    private Integer isMultiLevel;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDictSn(String dictSn) {
        this.dictSn = dictSn;
    }

    @NotBlank(message = "字典编码不能为空")
    @Size(min = 0, max = 100, message = "字典编码长度不能超过100个字符")
    public String getDictSn() {
        return dictSn;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    @NotBlank(message = "字典标签不能为空")
    @Size(min = 0, max = 100, message = "字典标签长度不能超过100个字符")
    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    @NotBlank(message = "字典键值不能为空")
    @Size(min = 0, max = 100, message = "字典键值长度不能超过100个字符")
    public String getDictValue() {
        return dictValue;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public void setListClass(String listClass) {
        this.listClass = listClass;
    }

    public String getListClass() {
        return listClass;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsMultiLevel(Integer isMultiLevel) {
        this.isMultiLevel = isMultiLevel;
    }

    public Integer getIsMultiLevel() {
        return isMultiLevel;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("dictSn", getDictSn())
                .append("dictLabel", getDictLabel())
                .append("dictValue", getDictValue())
                .append("orderNum", getOrderNum())
                .append("iconClass", getIconClass())
                .append("listClass", getListClass())
                .append("isDefault", getIsDefault())
                .append("isMultiLevel", getIsMultiLevel())
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
