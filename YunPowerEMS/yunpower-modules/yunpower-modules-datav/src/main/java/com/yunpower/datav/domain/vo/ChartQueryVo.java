package com.yunpower.datav.domain.vo;

import com.yunpower.datav.domain.dto.ChartConfigDto;
import com.yunpower.system.api.domain.DashboardConfigCard;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 图表 - 查询参数
 *
 * @author XIAOTONG.CAO
 * @date: 2024-06-12 10:00
 */
@Data
public class ChartQueryVo implements Serializable {

    /**
     * 配置ID
     */
    @NotNull(message = "configId 不能为空")
    private Long configId;

    /**
     * 卡片key
     */
    @NotEmpty(message = "cardKey 不能为空")
    private String cardKey;

    /**
     * 天日期
     */
    private String dayTime;

    /**
     * 月日期
     */
    private String monthTime;

    /**
     * 年日期
     */
    private String yearTime;

    /**
     * 设备编号
     */
    private String deviceSn;

    /**
     * 配置项
     */
    private DashboardConfigCard dashboardConfigCard;

    /**
     * 卡片图表配置
     */
    private ChartConfigDto chartConfig;

    /**
     * 时间数组
     */
    private List<TimeInfo> timeList;

    /**
     * 时间数组
     */
    private List<TimeInfo> chainTimeList;

    /**
     * 时间数组
     */
    private List<TimeInfo> yoyTimeList;

    /**
     * 数据查询开始时间
     */
    private String beginTime;

    /**
     * 数据查询结束时间
     */
    private String endTime;

    /**
     * 环比-开始时间
     */
    private String chainBeginTime;

    /**
     * 环比-结束时间
     */
    private String chainEndTime;

    /**
     * 同比-开始时间
     */
    private String yoyBeginTime;

    /**
     * 同比-结束时间
     */
    private String yoyEndTime;


    /**
     * 是否展示表格
     * */
    private Integer isTable = 0;

    @Data
    public static class TimeInfo implements Serializable{
        private String title;
        private Double value;
        private String key;

        /**
         * 堆叠图的时候用到
         * */
        private Integer key2;

        public TimeInfo(){

        }

        public TimeInfo(String key,String title,Double value){
            this.key = key;
            this.title = title;
            this.value = value;
        }

        public TimeInfo(String key,String title,Double value,Integer key2){
            this.key = key;
            this.title = title;
            this.value = value;
            this.key2 = key2;
        }
    }
}
