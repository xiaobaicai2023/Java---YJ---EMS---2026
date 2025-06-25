package com.yunpower.collect.protocols.modbus;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 *
 * */
public class ModbusChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModbusChannelInitializer.class);

    @Override
    protected void initChannel(SocketChannel ch){
        // 获取远程地址和端口
        InetSocketAddress remoteAddress = ch.remoteAddress();
        String remoteHost = remoteAddress.getHostString();
        int remotePort = remoteAddress.getPort();
        LOGGER.info("ModbusChannel初始化通道成功，远程地址：{}，端口：{}", remoteHost, remotePort);
    }
}