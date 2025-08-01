package com.yunpower.collect.protocols.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * WebSocketServer服务
 */
@Component
public class WebSocketServer implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    @Value("${yunpower.collect.protocols.websocket.address}")
    private String address;

    @Value("${yunpower.collect.protocols.websocket.port}")
    private Integer port;

    private final ExecutorService executorService;
    private volatile boolean running = false;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    public WebSocketServer() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 启动服务
     */
    public void startServer() {
        executorService.submit(() -> {
            try {
                bind();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // 处理异常
            } finally {
                shutdownNetty();
            }
        });
    }


    /**
     * 绑定端口
     */
    public void bind() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new WebSocketServerInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(this.address, this.port).sync();
            LOGGER.info("WebSocketServer Netty 启动成功，监听地址为：{}:{}", address, port);
            serverChannel = future.channel();
            serverChannel.closeFuture().sync();
        } catch (Exception exception) {
            LOGGER.error("WebSocketServer 异常 ", exception);
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            LOGGER.info("WebSocketServer结束！");
        }
    }

    @Override
    public void start() {
        LOGGER.info("WebSocketServer Netty 启动中...");
        if (!running) {
            running = true;
            startServer();
        }
    }

    @Override
    public void stop() {
        LOGGER.info("WebSocketServer停止中...");
        if (running) {
            running = false;
            shutdownNetty();
            shutdownExecutor();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return 5;
    }

    /**
     * 停止netty服务
     */
    public void shutdownNetty() {
        LOGGER.info("WebSocketServer Netty 停止中...");
        if (serverChannel != null) {
            serverChannel.close();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }


    /**
     * 停止线程
     */
    public void shutdownExecutor() {
        LOGGER.info("WebSocketServer 线程停止中...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            LOGGER.info("WebSocketServer停止成功");
        }
    }

}
