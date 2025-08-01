package com.yunpower.collect.protocols.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 通道初始化
 * */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加HTTP编解码器
        pipeline.addLast(new HttpServerCodec());
        // 添加ChunkedWriteHandler以处理大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        // 添加HttpObjectAggregator以将多个消息转换为单一的FullHttpRequest或FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(65536));
        // 添加WebSocketServerProtocolHandler以处理WebSocket升级握手、Ping/Pong心跳等
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new IdleStateHandler(60, 60, 0, TimeUnit.SECONDS));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", "WebSocket", true, 65536 * 10));
        // 添加自定义的处理器
        pipeline.addLast(new WebSocketChannelHandler());
    }
}
