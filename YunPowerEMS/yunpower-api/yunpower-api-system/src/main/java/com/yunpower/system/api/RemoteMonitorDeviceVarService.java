package com.yunpower.system.api;

import com.yunpower.system.api.factory.RemoteMonitorDeviceVarFallbackFactory;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import com.yunpower.system.api.domain.FeignMonitorDeviceVar;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 监控设备变量
 *
 * @author XIAOTONG.CAO
 */
@FeignClient(contextId = "remoteMonitorDeviceVarService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteMonitorDeviceVarFallbackFactory.class)

public interface RemoteMonitorDeviceVarService {


    /**
     * 根据条件查询list
     * @param request 查询条件
     * @return 常用分组集合
     */
    @PostMapping("/device-var/list/inner")
    public List<FeignMonitorDeviceVar> getListInner(@RequestBody FeignMonitorDeviceVar request, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程内部调用-根据变量编码查询
     *
     * @param varSn 变量编码
     * @return 变量数据
     */
    @GetMapping("/device-var/getInfo/inner/{varSn}")
    public FeignMonitorDeviceVar getInfoInnerAuth(@PathVariable("varSn") String varSn, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
