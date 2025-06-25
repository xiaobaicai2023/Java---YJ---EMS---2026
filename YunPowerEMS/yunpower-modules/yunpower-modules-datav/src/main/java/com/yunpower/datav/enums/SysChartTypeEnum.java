package com.yunpower.datav.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图表枚举
 */
@Getter
public enum SysChartTypeEnum {
    LINE_CHART(1, "折线图", "line"),
    BAR_CHART(2, "柱状图", "bar"),
    BAR_LINE_CHART(3, "条形图", "bar"),
    STACKED_CHART(4, "堆叠图", ""),
    AREA_CHART(5, "面积图", ""),
    PIE_CHART(6, "饼图", "pie"),
    K_LINE_CHART(7, "K线图", ""),
    DROP_CHART(8, "水滴图", ""),
    DASHBOARD(9, "仪表盘", ""),
    SANKEY_CHART(10, "桑基图", ""),
    SEGMENTED_CHART(11, "分段图", ""),
    RANKING_CHART(12, "排行图", ""),
    STATUS_VALUE(13, "状态量", ""),
    SINGLE_VALUE(14, "单值", ""),
    ALARM_LIST(15, "报警列表"),
    WORK_ORDER_LIST(16, "工单列表"),
    ALARM_STATISTICS(17, "报警统计"),
    WORK_ORDER_STATISTICS(18, "工单统计", ""),
    POLAR_COORDINATE_CHART(19, "极坐标图", ""),
    POLAR_STACKED_CHART(20, "极坐标堆叠图", ""),
    FIXED_VALUE_CHART(21, "固定值", "");

    /**
     * 编码
     */
    private Integer key;

    /**
     * 名称
     */
    private String value;

    private String type;

    SysChartTypeEnum(int key, String value, String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    SysChartTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据key获取type
     *
     * @param key key值
     */
    public static String getTypeByKey(Integer key) {
        for (SysChartTypeEnum item : SysChartTypeEnum.values()) {
            if (item.getKey().equals(key)) {
                return item.getType();
            }
        }
        return null;
    }

    //指定变量1.状态量 2.桑基 3.排行榜 4.饼图 5.水滴 6.仪表盘
    public static final List<Integer> SpecialVarList = new ArrayList<>(
            Arrays.asList(
                    STATUS_VALUE.getKey()
                    , SANKEY_CHART.getKey()
                    , RANKING_CHART.getKey()
                    , PIE_CHART.getKey()
                    , SEGMENTED_CHART.getKey()
                    , ALARM_LIST.getKey()
                    , WORK_ORDER_LIST.getKey()
                    , ALARM_STATISTICS.getKey()
                    , WORK_ORDER_STATISTICS.getKey()
                    , DROP_CHART.getKey()
                    , DASHBOARD.getKey()));

}
