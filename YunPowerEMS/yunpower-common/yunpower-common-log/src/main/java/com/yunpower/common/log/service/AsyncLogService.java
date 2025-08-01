package com.yunpower.common.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.system.api.RemoteLogService;
import com.yunpower.system.api.domain.SysLogOper;

/**
 * 异步调用日志服务
 * 
 * @author yunpower
 */
@Service
public class AsyncLogService
{
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysLogOper sysLogOper) throws Exception
    {
        remoteLogService.saveLog(sysLogOper, SecurityConstants.INNER);
    }
}
