package com.yunpower.system.domain.jsonvo;

import java.util.List;

/**
 * @title: 图表返回Y轴数据
 * @Author: Jiajiaglam
 * @date: 2023-11-04 13:54
 * @description:
 */
public class ChartYAxisVo {

    /**
     * 显示名称
     */
    private String name;

    /**
     * 分组名称：堆叠图表用，普通图表为空即可
     */
    private String groupName;

    /**
     * 图表类型，如：bar, line
     */
    private String chartType;

    /**
     * 图表使用第几个Y轴
     */
    private Integer yAxisIndex;

    /**
     * 图表数据单位
     */
    private String yAxisUnit;

    /**
     * 数据列表
     */
    private List<Object> dataList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public Integer getyAxisIndex() {
        return yAxisIndex;
    }

    public void setyAxisIndex(Integer yAxisIndex) {
        this.yAxisIndex = yAxisIndex;
    }

    public String getyAxisUnit() {
        return yAxisUnit;
    }

    public void setyAxisUnit(String yAxisUnit) {
        this.yAxisUnit = yAxisUnit;
    }

    public List<Object> getDataList() {
        return dataList;
    }

    public void setDataList(List<Object> dataList) {
        this.dataList = dataList;
    }
}
