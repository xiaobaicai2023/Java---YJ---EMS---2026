package com.yunpower.common.core.constant;

import io.netty.util.AttributeKey;

public class Constants {

    /**
     * 监控设备变量 cache key
     */
    public static final String MONITOR_DEVICE_VAR_SN_KEY = "device_varsn:";

    /**
     * 监控设备变量存盘时间 cache key
     */
    public static final String MONITOR_DEVICE_VAR_SN_SAVE_TIME_KEY = "device_varsn_savetime:";

    /**
     * 设备状态 cache key
     */
    public static final String DEVICE_STATE_KEY = "device_state:";

    /**
     * 设备变量报警 cache key 一分钟有效
     */
    public static final String MONITOR_DEVICE_VAR_ALARM_KEY = "device_varsn_alarm:";

    /**
     * 设备变量报警数量 cache key 1天有效
     */
    public static final String MONITOR_DEVICE_VAR_ALARM_COUNT_KEY = "device_varsn_alarm_count:";

    /**
     * 通道-后缀
     */
    public static final String CHANNEL_SUFFIX = "channel";

    /**
     * 设备状态-通道
     */
    public static final String DEVICE_STATE_CHANNEL_KEY = DEVICE_STATE_KEY + CHANNEL_SUFFIX + ":";

    /**
     * 通道设备-后缀
     */
    public static final String CHANNEL_DEVICE_SUFFIX = "channel_device";
    /**
     * 设备状态-通道设备
     */
    public static final String DEVICE_STATE_CHANNEL_DEVICE_KEY = DEVICE_STATE_KEY + CHANNEL_DEVICE_SUFFIX + ":";

    /**
     * 监控设备-后缀
     */
    public static final String MONITOR_DEVICE_SUFFIX = "monitor_device";

    /**
     * 设备状态-监控设备
     */
    public static final String DEVICE_STATE_MONITOR_DEVICE_KEY = DEVICE_STATE_KEY + MONITOR_DEVICE_SUFFIX + ":";



    /**
     * 请求ID
     * */
    public static final String CHANNEL_SN = "channelSn";
    public static final String CHANNEL_SN_DEFAULT = "default";
    public static final AttributeKey<String> CHANNEL_SN_KEY = AttributeKey.valueOf(CHANNEL_SN);

    public static final String CALCULATE_MEMORY_VARIABLES = "memory-var";

}
