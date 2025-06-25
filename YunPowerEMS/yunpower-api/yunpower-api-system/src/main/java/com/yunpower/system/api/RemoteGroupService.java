package com.yunpower.system.api;

import com.yunpower.system.api.factory.RemoteGroupFallbackFactory;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import com.yunpower.system.api.domain.SysGroup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 常用分组
 *
 * @author XIAOTONG.CAO
 */
@FeignClient(contextId = "remoteGroupService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteGroupFallbackFactory.class)
public interface RemoteGroupService {

    /**
     * 远程内部调用-根据类别代码查询分组信息
     * @param groupSn 类别代码
     * @return 常用分组信息
     */
    @GetMapping("/group/info/groupSn/{groupSn}")
    SysGroup getInfoByGroupSn(@PathVariable("groupSn") String groupSn, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 远程内部调用-根据分组ID查询分组信息
     * @param groupId 分组ID
     * @return 常用分组信息
     */
    @GetMapping("/group/info/groupId/{groupId}")
    SysGroup getInfoByGroupId(@PathVariable("groupId") Long groupId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据条件查询list
     * @param sysGroup 查询条件
     * @return 常用分组集合
     */
    @PostMapping("/group/list/search")
    public List<SysGroup> getListBySearch(@RequestBody SysGroup sysGroup, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
