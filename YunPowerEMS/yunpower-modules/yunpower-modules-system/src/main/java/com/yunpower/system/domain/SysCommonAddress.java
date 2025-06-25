package com.yunpower.system.domain;

import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 全国四级行政区对象 sys_common_address
 *
 * @author yunpower
 * @date 2023-10-07
 */
@ApiModel("全国四级行政区对象")
public class SysCommonAddress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键（省市镇县编码） */
    @ApiModelProperty("主键（省市镇县编码）")
    private Long id;

    /** 父级code */
    @ApiModelProperty("父级code")
    @Excel(name = "父级code")
    private Long parentId;

    /** 等级：1省 2市 3县/县级市/区 4镇/街道 5村/社区 */
    @ApiModelProperty("等级：1省 2市 3县/县级市/区 4镇/街道 5村/社区")
    @Excel(name = "等级：1省 2市 3县/县级市/区 4镇/街道 5村/社区")
    private Integer level;

    /** 地区名称 */
    @ApiModelProperty("地区名称")
    @Excel(name = "地区名称")
    private String name;

    /** 状态：1启用 0已作废 */
    @ApiModelProperty("状态：1启用 0已作废")
    @Excel(name = "状态：1启用 0已作废")
    private Integer valid;
    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setValid(Integer valid)
    {
        this.valid = valid;
    }

    public Integer getValid()
    {
        return valid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("parentId", getParentId())
                .append("level", getLevel())
                .append("name", getName())
                .append("valid", getValid())
                .toString();
    }
}
