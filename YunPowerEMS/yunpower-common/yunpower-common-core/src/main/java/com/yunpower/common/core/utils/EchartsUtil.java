package com.yunpower.common.core.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Echarts工具类
 *
 * @author: XIAOTONG.CAO
 */
public class EchartsUtil{

    //图例位置
    private static final Map<Integer, Map<String, Object>> LEGEND_POSITION_MAP = new HashMap<>();

    //初始化
    static {

        //左上
        Map<String, Object> pos1 = new HashMap<>();
        pos1.put("left", 0);
        pos1.put("top", 0);
        LEGEND_POSITION_MAP.put(1, pos1);

        //左下
        Map<String, Object> pos2 = new HashMap<>();
        pos2.put("left", 0);
        pos2.put("bottom", 0);
        LEGEND_POSITION_MAP.put(2, pos2);

        //中上
        Map<String, Object> pos3 = new HashMap<>();
        pos3.put("left", "center");
        pos3.put("top", 0);
        LEGEND_POSITION_MAP.put(3, pos3);

        //中下
        Map<String, Object> pos4 = new HashMap<>();
        pos4.put("left", "center");
        pos4.put("bottom", 0);
        LEGEND_POSITION_MAP.put(4, pos4);
    }

    /**
     * 根据传入的位置id获取图例位置
     * @param position 位置id
     * */
    public static Map<String, Object> getLegendPosition(Integer position) {
        return Optional.ofNullable(LEGEND_POSITION_MAP.get(position))
                .orElse(Collections.emptyMap());
    }
}
