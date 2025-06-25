package com.yunpower.datav.domain.vo.echarts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * echarts=>option
 *
 * @author: XIAOTONG.CAO
 */
@Data
public class EchartsOption implements Serializable {

    /**
     * 图例组件
     * */
    private Map<String,Object> legend;

    /**
     * x轴数据
     * */
    @JsonProperty("xAxis")
    private List<EchartsAxisVo> xAxis;

    /**
     * y轴数据
     * */
    @JsonProperty("yAxis")
    private List<EchartsAxisVo> yAxis;

    /**
     *
     * */
    private List<String> unitList;

    /**
     * Series
     * */
    private List<EchartsSeriesVo> series = new ArrayList<>();

}
