package com.yunpower.collect.protocols.iec104.container;

import io.netty.channel.ChannelId;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 储存着104的所有连接
 */
@Data
public class LinkContainer {

    private static class LazyHolder {
        private static final LinkContainer INSTANCE = new LinkContainer();
    }

    /**
     * Gets instance *
     */
    public static final LinkContainer getInstance() {
        return LazyHolder.INSTANCE;
    }

    private LinkContainer() {
    }

    private Map<ChannelId, IEC104Link> links = new ConcurrentHashMap<>();


    /**
     * Get link iec 104 link
     */
    public IEC104Link getLink(ChannelId channelId) {
        return this.links.get(channelId);
    }

    /**
     * Get link iec 104 link
     */
    public IEC104Link getLink(String ip, int port) {
        for (Map.Entry<ChannelId, IEC104Link> e : this.links.entrySet()) {
            if (e.getValue().getOppositeIp().equals(ip) && e.getValue().getOppositePort() == port) {
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * Get slave link iec 104 link
     */
    public IEC104Link getSlaveLink(String ip, int port) {
        for (Map.Entry<ChannelId, IEC104Link> e : this.links.entrySet()) {
            if (e.getValue().getOppositeIp().equals(ip) && e.getValue().getOppositePort() == port && e.getValue().getOppositeRole() == IEC104Link.Role.SLAVER) {
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * Get master links list
     */
    public List<IEC104Link> getMasterLinks(String ip) {
        List<IEC104Link> links = new ArrayList<>();
        for (Map.Entry<ChannelId, IEC104Link> e : this.links.entrySet()) {
            if (e.getValue().getOppositeIp().equals(ip) && e.getValue().getOppositeRole() == IEC104Link.Role.MASTER) {
                links.add(e.getValue());
            }
        }
        return links;
    }
}
