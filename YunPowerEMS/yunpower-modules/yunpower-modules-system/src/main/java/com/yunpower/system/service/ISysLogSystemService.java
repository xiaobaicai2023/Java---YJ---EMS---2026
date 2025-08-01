package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysLogSystem;

/**
 * 系统日志Service接口
 * 
 * @author JUNFU.WANG
 * @date 2024-01-18
 */
public interface ISysLogSystemService 
{
    /**
     * 查询系统日志
     * 
     * @param id 系统日志主键
     * @return 系统日志
     */
    public SysLogSystem selectSysLogSystemById(Long id);

    /**
     * 查询系统日志列表
     * 
     * @param sysLogSystem 系统日志
     * @return 系统日志集合
     */
    public List<SysLogSystem> selectSysLogSystemList(SysLogSystem sysLogSystem);

    /**
     * 新增系统日志
     * 
     * @param sysLogSystem 系统日志
     * @return 结果
     */
    public int insertSysLogSystem(SysLogSystem sysLogSystem);

    /**
     * 修改系统日志
     * 
     * @param sysLogSystem 系统日志
     * @return 结果
     */
    public int updateSysLogSystem(SysLogSystem sysLogSystem);

    /**
     * 批量删除系统日志
     * 
     * @param ids 需要删除的系统日志主键集合
     * @return 结果
     */
    public int deleteSysLogSystemByIds(Long[] ids);

    /**
     * 删除系统日志信息
     * 
     * @param id 系统日志主键
     * @return 结果
     */
    public int deleteSysLogSystemById(Long id);
}
