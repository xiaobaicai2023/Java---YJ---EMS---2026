package com.yunpower.system.api;

import com.yunpower.system.api.factory.RemoteMonitorDeviceFallbackFactory;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import com.yunpower.system.api.domain.FeignMonitorDevice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 监控设备
 *
 * @author XIAOTONG.CAO
 */
@FeignClient(contextId = "remoteMonitorDeviceService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteMonitorDeviceFallbackFactory.class)

public interface RemoteMonitorDeviceService {
    /**
     * 根据设备编号查询
     * @param deviceSn 设备号
     * @return 设备信息
     */
    @GetMapping("/device/info/deviceSn/{deviceSn}")
    public FeignMonitorDevice getInfoByDeviceSn(@PathVariable("deviceSn") String deviceSn, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 查询所有设备的状态
     * @param deptId 部门ID
     * @return 设备状态 true在线 false离线
     */
    @GetMapping("/device/status/all/inner/{deptId}")
    public Boolean statusAll(@PathVariable("deptId") Long deptId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
