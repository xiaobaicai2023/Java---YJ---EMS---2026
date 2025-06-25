package com.yunpower.collect.protocols.websocket.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息体
 * */
@Data
public class WebSocketMessage implements Serializable {

    /**
     * 订阅subscribe和取消订阅unsubscribe
     * */
    private String action;

    /**
     * 业务编码
     * */
    private String bizSn;
}
