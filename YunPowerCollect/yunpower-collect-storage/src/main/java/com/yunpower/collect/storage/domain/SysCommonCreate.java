package com.yunpower.collect.storage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 生成记录对象 sys_common_create
 * 
 * @author JUNFU.WANG
 * @date 2023-10-24
 */
public class SysCommonCreate
{
    private static final long serialVersionUID = 1L;

    /** 生成表 */
    private String createTable;

    /** 生成类型：1日表 2月表 */
    private Integer createType;

    public SysCommonCreate() {
    }

    public SysCommonCreate(int type) {
        this.createType = type;
    }

    public void setCreateTable(String createTable) 
    {
        this.createTable = createTable;
    }

    public String getCreateTable() 
    {
        return createTable;
    }

    public void setCreateType(Integer createType) 
    {
        this.createType = createType;
    }

    public Integer getCreateType() 
    {
        return createType;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("createTable", getCreateTable())
            .append("createType", getCreateType())
            .toString();
    }
}
