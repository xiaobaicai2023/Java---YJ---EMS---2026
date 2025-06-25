package com.yunpower.system.domain.vo.table;

import java.util.List;

/**
 * 表格 - 表头
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/20 11:31
 */
public class TableHeader {
    private String title;
    private String dataIndex;
    private List<TableHeader> children;

    public TableHeader(String title, String dataIndex, List<TableHeader> childList) {
        this.title = title;
        this.dataIndex = dataIndex;
        this.children = childList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public List<TableHeader> getChildren() {
        return children;
    }

    public void setChildren(List<TableHeader> children) {
        this.children = children;
    }
}
