package com.yunpower.system.api;

import com.yunpower.system.api.factory.RemoteMonitorDeviceFallbackFactory;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 通讯设备
 *
 * @author XIAOTONG.CAO
 */
@FeignClient(contextId = "remoteCommunicationDeviceService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteMonitorDeviceFallbackFactory.class)

public interface RemoteCommunicationDeviceService {
    /**
     * 查询所有通讯设备的状态
     * @param deptId 部门ID
     * @return 设备状态 true在线 false离线
     */
    @GetMapping("/channel-device/status/all/inner/{deptId}")
    public Boolean statusAll(@PathVariable("deptId") Long deptId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
