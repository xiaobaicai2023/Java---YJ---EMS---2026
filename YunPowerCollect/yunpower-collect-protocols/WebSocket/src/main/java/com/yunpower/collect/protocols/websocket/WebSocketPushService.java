package com.yunpower.collect.protocols.websocket;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * 推送服务
 */
public class WebSocketPushService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketPushService.class);

    /**
     * 推送给指定设备
     *
     * @param bizSn 指定设备
     * @param msg   信息
     */
    public static void pushMsgToOne(String bizSn, String msg) {
        if (StrUtil.isBlank(bizSn) || StrUtil.isBlank(msg)) {
            return;
        }
        Set<Channel> channelList = WebSocketConfig.getBizSnChannelMap().get(bizSn);
        if (ObjectUtil.isNotEmpty(channelList)) {
            channelList.forEach((channel -> {
                log.info("WebSocketPushService.pushMsgToOne channel{},bizSn:{},msg:{}",channel.id().asLongText(),bizSn,msg);
                channel.writeAndFlush(new TextWebSocketFrame(msg));
            }));
        }
    }

    /**
     * 推送给所有用户
     *
     * @param msg 信息
     */
    public static void pushMsgToAll(String msg) {
        WebSocketConfig.getBizSnChannelMap().forEach((key,value)->{
            value.forEach(item->{
                item.writeAndFlush(new TextWebSocketFrame(msg));
            });
        });
    }
}
