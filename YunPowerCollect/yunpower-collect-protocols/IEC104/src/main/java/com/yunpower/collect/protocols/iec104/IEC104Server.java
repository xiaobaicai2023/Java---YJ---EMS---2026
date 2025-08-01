package com.yunpower.collect.protocols.iec104;

import com.yunpower.collect.protocols.iec104.handler.IDataHandler;
import com.yunpower.collect.protocols.iec104.netty.IEC104ChannelInitializer;
import com.yunpower.mq.publisher.service.PublisherService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * IEC104Server服务
 */
@Component
public class IEC104Server implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(IEC104Server.class);

    @Value("${yunpower.collect.protocols.iec104.address}")
    private String address;

    @Value("${yunpower.collect.protocols.iec104.port}")
    private Integer port;

    private final ExecutorService executorService;
    private volatile boolean running = false;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    @Autowired
    private PublisherService publisherService;

    //自定义处理类 和 配置类
    @Autowired
    private IDataHandler dataHandler;

    public IEC104Server() {
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
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        IEC104Variables.publisherService = publisherService;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new IEC104ChannelInitializer().setDataHandler(dataHandler))
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(this.address, this.port).sync();
            LOGGER.info("IEC104Server Netty 启动成功，监听地址为：{}:{}", address, port);
            LOGGER.info("Netty启动顺序.....1");
            serverChannel = future.channel();
            serverChannel.closeFuture().sync();
        } catch (Exception exception) {
            LOGGER.error("IEC104Server 异常 ", exception);
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            LOGGER.info("IEC104Server结束！");
        }
    }

    @Override
    public void start() {
        LOGGER.info("IEC104Server Netty 启动中...");
        if (!running) {
            running = true;
            startServer();
        }
    }

    @Override
    public void stop() {
        LOGGER.info("IEC104Server停止中...");
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
        return 2;
    }

    /**
     * 停止netty服务
     */
    public void shutdownNetty() {
        LOGGER.info("IEC104Server Netty 停止中...");
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
        LOGGER.info("IEC104Server 线程停止中...");
        executorService.shutdown();
        try {

            if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            LOGGER.info("IEC104Server停止成功");
        }
    }

}
