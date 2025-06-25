package com.yunpower.system.service;

import java.util.List;
import com.yunpower.system.api.domain.SysLogOper;

/**
 * 操作日志 服务层
 * 
 * @author yunpower
 */
public interface ISysLogOperService
{
    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     * @return 结果
     */
    public int insertOperlog(SysLogOper operLog);

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<SysLogOper> selectOperLogList(SysLogOper operLog);

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    public int deleteOperLogByIds(Long[] operIds);

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    public SysLogOper selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    public void cleanOperLog();
}
