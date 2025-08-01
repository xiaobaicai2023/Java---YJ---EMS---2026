package com.yunpower.datav.domain.vo.echarts;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * echarts=>xAxis
 *
 * @author: XIAOTONG.CAO
 */
@Data
public class EchartsAxisVo implements Serializable {

    /**
     * 类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样 false
     */
    private Boolean boundaryGap;

    /**
     * 坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样 false
     */
    private String position;

    private Boolean alignTicks = true;

    private AxisTick axisTick = new AxisTick();

    private Map<String,Object> splitLine;

    /**
     * 数据
     */
    private List<String> data;

    public EchartsAxisVo() {

    }

    public EchartsAxisVo(String type, List<String> data, String name, String position,Boolean boundaryGap,Boolean showAxisTick) {
        this.type = type;
        this.boundaryGap = boundaryGap;
        this.data = data;
        this.name = name;
        this.position = position;
        axisTick.setShow(showAxisTick);
        packageSplitLine();
    }

    @Data
    private static class AxisTick implements Serializable {
        private Boolean show = true;
        private Boolean alignWithLabel = true;
    }

    private void packageSplitLine(){
        Map<String,Object> map = new HashMap<>();
        map.put("type","dashed");
        map.put("color","#394C5E");
        this.splitLine = new HashMap<>();
        this.splitLine.put("lineStyle",map);
    }
}
