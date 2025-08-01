package com.yunpower.system.domain.vo.time;

import java.io.Serializable;
import java.util.List;

/**
 * 装配时间实体类
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/24 14:11
 */
public class TimesVo {
    /**
     * 日-日期
     */
    private String dayTime;

    /**
     * 月-日期
     */
    private String monthTime;

    /**
     * 年-日期
     */
    private String yearTime;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 时间维度
     */
    private Integer dateDim;

    /**
     * 时间数组
     */
    private List<TimeInfo> timeList;

    public static class TimeInfo implements Serializable {
        private String title;
        private Float value;
        private String key;

        public TimeInfo() {

        }

        public TimeInfo(String key, String title, Float value) {
            this.key = key;
            this.title = title;
            this.value = value;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Float getValue() {
            return value;
        }

        public void setValue(Float value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getMonthTime() {
        return monthTime;
    }

    public void setMonthTime(String monthTime) {
        this.monthTime = monthTime;
    }

    public String getYearTime() {
        return yearTime;
    }

    public void setYearTime(String yearTime) {
        this.yearTime = yearTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDateDim() {
        return dateDim;
    }

    public void setDateDim(Integer dateDim) {
        this.dateDim = dateDim;
    }

    public List<TimeInfo> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<TimeInfo> timeList) {
        this.timeList = timeList;
    }
}
