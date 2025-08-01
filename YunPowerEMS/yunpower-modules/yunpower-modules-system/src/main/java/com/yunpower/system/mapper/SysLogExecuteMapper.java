package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysLogExecute;

/**
 * 执行日志Mapper接口
 * 
 * @author JUNFU.WANG
 * @date 2024-01-18
 */
public interface SysLogExecuteMapper 
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
     * 删除执行日志
     * 
     * @param id 执行日志主键
     * @return 结果
     */
    public int deleteSysLogExecuteById(Long id);

    /**
     * 批量删除执行日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysLogExecuteByIds(Long[] ids);
}
