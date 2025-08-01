package com.yunpower.datav.domain.vo.report;

import lombok.Data;

import java.util.List;

/**
 * 表头存储
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/20 11:31
 */
@Data
public class TableHeader {
    private String title;
    private String dataIndex;
    private Integer minWidth = 135;
    private List<TableHeader> children;

    public TableHeader(String title, String dataIndex, List<TableHeader> childList) {
        this.title = title;
        this.dataIndex = dataIndex;
        this.children = childList;
    }
}
