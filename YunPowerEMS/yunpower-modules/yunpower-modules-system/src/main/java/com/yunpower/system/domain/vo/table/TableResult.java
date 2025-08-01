package com.yunpower.system.domain.vo.table;

import java.util.List;
import java.util.Map;

/**
 * 表格 - 数据
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/20 11:31
 */
public class TableResult {
    private List<TableHeader> header;
    private List<Map<String, Object>> tableData;

    public TableResult(List<TableHeader> header, List<Map<String, Object>> tableData) {
        this.header = header;
        this.tableData = tableData;
    }

    public List<TableHeader> getHeader() {
        return header;
    }

    public void setHeader(List<TableHeader> header) {
        this.header = header;
    }

    public List<Map<String, Object>> getTableData() {
        return tableData;
    }

    public void setTableData(List<Map<String, Object>> tableData) {
        this.tableData = tableData;
    }
}
