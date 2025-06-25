package com.yunpower.collect.protocols.websocket;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocketConfig
 */
public class WebSocketConfig {
    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    /**
     * 定义一个channel组，管理所有的channel
     * GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
     * -- GETTER --
     *  获取channel组

     */
    @Getter
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存放各自业务设备与Chanel的对应信息
     * -- GETTER --
     */
    @Getter
    private static final ConcurrentHashMap<String, Set<Channel>> bizSnChannelMap = new ConcurrentHashMap<>();

    private WebSocketConfig() {}

    /**
     * 订阅
     * */
    public static void subscribe(String bizSn, Channel channel) {
        bizSnChannelMap.computeIfAbsent(bizSn, k -> new CopyOnWriteArraySet<>()).add(channel);
        log.info("subscribe:{},list:{}",bizSn,bizSnChannelMap.get(bizSn));
    }

    /**
     * 取消订阅
     * */
    public static void unsubscribe(String bizSn, Channel channel) {
        Set<Channel> channelList = bizSnChannelMap.get(bizSn);
        if (channelList != null) {
            channelList.remove(channel);
            if (channelList.isEmpty()) {
                bizSnChannelMap.remove(bizSn);
            }
        }
    }

    /**
     * 取消订阅
     * */
    public static void remove(Channel channel) {
        for (Map.Entry<String, Set<Channel>> entry : bizSnChannelMap.entrySet()) {
            String key = entry.getKey(); // 获取键
            Set<Channel> channelList = entry.getValue(); // 获取值
            if (channelList != null) {
                channelList.remove(channel);
                if (channelList.isEmpty()) {
                    bizSnChannelMap.remove(key);
                }
            }
        }
    }
}
