package com.yunpower.collect.protocols.websocket;

import com.alibaba.fastjson2.JSON;
import com.yunpower.collect.protocols.websocket.domain.WebSocketMessage;
import com.yunpower.common.core.enums.WebSocketMessageEnum;
import com.yunpower.common.core.vo.WebSocketBaseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WebSocketChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketChannelHandler.class);
    private static final String BIZ_SN_KEY = "bizSn";
    private static final String JSON_PATTERN = "\\{(?:[^{}]|\\{.*\\})*\\}|\\[(?:[^\\[\\]]|\\[.*\\])*\\]";

    /**
     * 添加通道
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("WebSocket 添加通道成功: {}", ctx.channel().id().asLongText());
        WebSocketConfig.getChannelGroup().add(ctx.channel());
    }

    /**
     * 移除通道
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("WebSocket 通道断开: {}", ctx.channel().id().asLongText());
        WebSocketConfig.getChannelGroup().remove(ctx.channel());
        removeBizSnChannel(ctx);
        ctx.close();
    }

    /**
     * 读取客户端发送的消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        LOGGER.info("WebSocket 收到消息：{}", msg.text());
        if ("ping".equals(msg.text())) {
            LOGGER.info("WebSocketServer Netty 收到客户端的心跳消息，回复心跳响应");
            ctx.writeAndFlush(new TextWebSocketFrame("pong"));
            return;
        }
        if (!JSON.isValidObject(msg.text())) {
            handleInvalidMessage(ctx, msg.text());
            return;
        }
        WebSocketMessage webSocketMessage = JSON.parseObject(msg.text(), WebSocketMessage.class);
        String action = webSocketMessage.getAction();
        String bizSn = webSocketMessage.getBizSn();
        if (StringUtils.isNotEmpty(action) && StringUtils.isNotEmpty(bizSn)) {
            handleAction(ctx, action, bizSn);
        } else {
            handleInvalidMessage(ctx, msg.text());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 处理心跳事件
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            LOGGER.info("WebSocketServer Netty IdleStateEvent:{}", event.toString());
            LOGGER.info("WebSocketServer Netty 客户端无操作 断开链接！");
            WebSocketConfig.getChannelGroup().remove(ctx.channel());
            ctx.close();
        }
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("WebSocket 异常：", cause);
        WebSocketConfig.getChannelGroup().remove(ctx.channel());
        removeBizSnChannel(ctx);
        ctx.close();
    }

    /**
     * 删除业务设备与channel的对应关系
     */
    private void removeBizSnChannel(ChannelHandlerContext ctx) {
        WebSocketConfig.remove(ctx.channel());
    }

    private void handleAction(ChannelHandlerContext ctx, String action, String bizSn) {
        boolean result = false;
        switch (action) {
            case "subscribe":
                WebSocketConfig.subscribe(bizSn, ctx.channel());
                result = true;
                break;
            case "unsubscribe":
                WebSocketConfig.unsubscribe(bizSn, ctx.channel());
                result = true;
                break;
            default:
                handleUnknownAction(ctx, action);
                break;
        }
        if (result) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "0");
            map.put("msg", "接收到消息");
            map.put("data", ctx.channel().id().asLongText());
            WebSocketBaseMessage baseMessage = new WebSocketBaseMessage(WebSocketMessageEnum.SUBSCRIBE_RESPONSE_MESSAGE,map);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(baseMessage)));
        }
    }


    private void handleUnknownAction(ChannelHandlerContext ctx, String action) {
        // 处理未知的动作
        LOGGER.error("WebSocketChannelHandler.channelRead0 Unknown action: {}", action);
        Map<String, String> result = new HashMap<>();
        result.put("code", "1");
        result.put("msg", "Unknown action: " + action);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(result)));
    }

    private void handleInvalidMessage(ChannelHandlerContext ctx, String message) {
        // 处理无效的消息
        LOGGER.error("WebSocketChannelHandler.channelRead0 Invalid message: {}", message);
        Map<String, String> result = new HashMap<>();
        result.put("code", "2");
        result.put("msg", "Invalid message format");
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(result)));
    }

    public static boolean isValidJson(String json) {
        return json != null && json.trim().matches(JSON_PATTERN);
    }
}
