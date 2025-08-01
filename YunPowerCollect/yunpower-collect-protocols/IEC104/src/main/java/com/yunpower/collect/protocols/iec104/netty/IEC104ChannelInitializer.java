package com.yunpower.collect.protocols.iec104.netty;

import com.yunpower.collect.protocols.iec104.handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

@Setter
@Accessors(chain = true)
public class IEC104ChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IEC104ChannelInitializer.class);

    private IDataHandler dataHandler;

    @Override
    protected void initChannel(SocketChannel channel){
        // 获取远程地址和端口
        InetSocketAddress remoteAddress = channel.remoteAddress();
        String remoteHost = remoteAddress.getHostString();
        int remotePort = remoteAddress.getPort();
        LOGGER.info("IEC104Channel初始化通道成功，远程地址：{}，端口：{}", remoteHost, remotePort);

        ChannelPipeline pipeline = channel.pipeline();

        // MDC
        pipeline.addLast(new MDCChannelHandler());

        // 沾包拆包工具
        pipeline.addLast(new Unpack104Handler());

        // 数据检查工具
        pipeline.addLast(new CheckIEC104Handler());

        // 拦截U帧处理器
        pipeline.addLast(new SysUFrameHandler());

        // 拦截S帧处理器
        pipeline.addLast(new SysSFrameHandler());

        // 收到104报文后发送一条确认帧
        pipeline.addLast(new SendConfirmFrameHandler());

        // 编码器
        pipeline.addLast(new BytesEncoder());

        // 编码器
        pipeline.addLast(new DataEncoder());

        // 解码器
        pipeline.addLast(new DataDecoder());

        pipeline.addLast(new IEC104SocketChannelHandler(dataHandler));

        LOGGER.info("Netty启动顺序.....2");
    }
}