package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.system.mapper.SysLogOperMapper;
import com.yunpower.system.service.ISysLogOperService;
import com.yunpower.common.datasource.annotation.LogDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.api.domain.SysLogOper;

/**
 * 操作日志 服务层处理
 * 
 * @author yunpower
 */
//多数据源（日志数据库，注解在类上适用）
@LogDataSource
@Service
public class SysLogOperServiceImpl implements ISysLogOperService
{
    @Autowired
    private SysLogOperMapper operLogMapper;

    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     * @return 结果
     */
    @Override
    public int insertOperlog(SysLogOper operLog)
    {
        return operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysLogOper> selectOperLogList(SysLogOper operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysLogOper selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
