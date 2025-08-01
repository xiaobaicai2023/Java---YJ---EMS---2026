package com.yunpower.datav.domain.vo.report;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 返回数据
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/20 11:31
 */
@Data
public class TableResult {
    private List<TableHeader> header;
    private List<Map<String, String>> tableData;

    public TableResult(List<TableHeader> header, List<Map<String, String>> tableData) {
        this.header = header;
        this.tableData = tableData;
    }
}
