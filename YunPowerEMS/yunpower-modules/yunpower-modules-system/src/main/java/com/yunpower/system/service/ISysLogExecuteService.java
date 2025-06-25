package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysLogExecute;

/**
 * 执行日志Service接口
 * 
 * @author JUNFU.WANG
 * @date 2024-01-18
 */
public interface ISysLogExecuteService 
{
    /**
     * 查询执行日志
     * 
     * @param id 执行日志主键
     * @return 执行日志
     */
    public SysLogExecute selectSysLogExecuteById(Long id);

    /**
     * 查询执行日志列表
     * 
     * @param sysLogExecute 执行日志
     * @return 执行日志集合
     */
    public List<SysLogExecute> selectSysLogExecuteList(SysLogExecute sysLogExecute);

    /**
     * 新增执行日志
     * 
     * @param sysLogExecute 执行日志
     * @return 结果
     */
    public int insertSysLogExecute(SysLogExecute sysLogExecute);

    /**
     * 修改执行日志
     * 
     * @param sysLogExecute 执行日志
     * @return 结果
     */
    public int updateSysLogExecute(SysLogExecute sysLogExecute);

    /**
     * 批量删除执行日志
     * 
     * @param ids 需要删除的执行日志主键集合
     * @return 结果
     */
    public int deleteSysLogExecuteByIds(Long[] ids);

    /**
     * 删除执行日志信息
     * 
     * @param id 执行日志主键
     * @return 结果
     */
    public int deleteSysLogExecuteById(Long id);
}
