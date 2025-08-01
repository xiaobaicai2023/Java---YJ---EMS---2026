package com.yunpower.datav.domain.vo.echarts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunpower.common.core.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * echarts=>Series
 *
 * @author: XIAOTONG.CAO
 */
@Data
public class EchartsSeriesVo implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * type
     */
    private String type;

    /**
     * 面积图
     */
    private Map<String, Object> areaStyle;

    /**
     * 数据
     */
    private List<Double> data;

    /**
     * 堆叠图-按日查询用到
     */
    List<Object> stackDaySeriesData;

    /**
     * 堆叠组
     */
    private String stack;

    private List<ObjDataInfo> ObjData;

    /**
     * 平滑
     */
    private Boolean smooth;

    /**
     * 颜色
     */
    private String color;

    /**
     * y 轴的 index
     */
    @JsonProperty("yAxisIndex")
    private Integer yAxisIndex;

    /**
     * x 轴的 index
     */
    @JsonProperty("xAxisIndex")
    private Integer xAxisIndex;

    /**
     * 图表标线
     */
    private MarkLine markLine = new MarkLine();

    /**
     * 柱状图宽度
     * */
    private String barMaxWidth;

    /**
     * 折线图样式
     */
    private LineStyle lineStyle;

    /**
     * 标记的大小
     * */
    private Integer symbolSize = 4;

    /**
     * 标记最大值最小值
     */
    private Map<String, Object> markPoint;


    public EchartsSeriesVo() {

    }

    public EchartsSeriesVo(String name, String type, List<Double> data, String color) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.smooth = true;
        if (StringUtils.isNotBlank(color)) {
            this.color = color;
        }
        this.lineStyle = new LineStyle();
//        this.markPoint = getMarkPointMap();
    }

    /**
     * 堆叠图-按日查询用到
     * */
    public EchartsSeriesVo(String name, String type, List<Object> data) {
        this.name = name;
        this.type = type;
        this.stackDaySeriesData = data;
        this.smooth = true;
        if (StringUtils.isNotBlank(color)) {
            this.color = color;
        }
        this.lineStyle = new LineStyle();
//        this.markPoint = getMarkPointMap();
    }

    public EchartsSeriesVo(String name, String type, List<Double> data, String color, String stack) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.smooth = true;
        if (StringUtils.isNotBlank(color)) {
            this.color = color;
        }
        this.lineStyle = new LineStyle();
//        this.markPoint = getMarkPointMap();
        this.stack = stack;
    }

    private Map<String, Object> getMarkPointMap() {
        // 创建内层的List<Map<String, String>>
        List<Map<String, String>> dataList = Arrays.asList(
                new HashMap<String, String>() {{
                    put("type", "max");
                    put("name", "Max");
                }},
                new HashMap<String, String>() {{
                    put("type", "min");
                    put("name", "Min");
                }}
        );

        // 创建内层的Map<String, Object>
        Map<String, Object> markPointMap = new HashMap<>();
        markPointMap.put("data", dataList);
        return markPointMap;
    }
    //region markLine

    /**
     * 标记线
     */
    @Data
    public static class MarkLine implements Serializable {
        private List<MarkLineData> data = new ArrayList<>();

        public MarkLine() {

        }

        public MarkLine(List<MarkLineData> data) {
            this.data = data;
        }
    }

    @Data
    public static class MarkLineData implements Serializable {
        @JsonProperty("xAxis")
        private String xAxis;

        @JsonProperty("yAxis")
        private String yAxis;
        private MarkLineLabel label = new MarkLineLabel();

        public MarkLineData() {

        }

        public MarkLineData(String xAxis, String yAxis, String formatter) {
            this.xAxis = xAxis;
            this.yAxis = yAxis;
            label.setFormatter(formatter);
        }
    }

    @Data
    public static class MarkLineLabel implements Serializable {
        private String formatter;
    }

    @Data
    public static class ObjDataInfo implements Serializable {
        private String name;
        private Object value;

        public ObjDataInfo() {
        }

        public ObjDataInfo(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }

    @Data
    public static class LineStyle implements Serializable {
        private Integer opacity;
        private Integer width = 2;
    }
    //endregion
}
