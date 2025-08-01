package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.common.datasource.annotation.LogDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.SysLogSystemMapper;
import com.yunpower.system.domain.SysLogSystem;
import com.yunpower.system.service.ISysLogSystemService;

/**
 * 系统日志Service业务层处理
 * 
 * @author JUNFU.WANG
 * @date 2024-01-18
 */
//多数据源（日志数据库，注解在类上适用）
@LogDataSource
@Service
public class SysLogSystemServiceImpl implements ISysLogSystemService 
{
    @Autowired
    private SysLogSystemMapper sysLogSystemMapper;

    /**
     * 查询系统日志
     * 
     * @param id 系统日志主键
     * @return 系统日志
     */
    @Override
    public SysLogSystem selectSysLogSystemById(Long id)
    {
        return sysLogSystemMapper.selectSysLogSystemById(id);
    }

    /**
     * 查询系统日志列表
     * 
     * @param sysLogSystem 系统日志
     * @return 系统日志
     */
    @Override
    public List<SysLogSystem> selectSysLogSystemList(SysLogSystem sysLogSystem)
    {
        return sysLogSystemMapper.selectSysLogSystemList(sysLogSystem);
    }

    /**
     * 新增系统日志
     * 
     * @param sysLogSystem 系统日志
     * @return 结果
     */
    @Override
    public int insertSysLogSystem(SysLogSystem sysLogSystem)
    {
        return sysLogSystemMapper.insertSysLogSystem(sysLogSystem);
    }

    /**
     * 修改系统日志
     * 
     * @param sysLogSystem 系统日志
     * @return 结果
     */
    @Override
    public int updateSysLogSystem(SysLogSystem sysLogSystem)
    {
        return sysLogSystemMapper.updateSysLogSystem(sysLogSystem);
    }

    /**
     * 批量删除系统日志
     * 
     * @param ids 需要删除的系统日志主键
     * @return 结果
     */
    @Override
    public int deleteSysLogSystemByIds(Long[] ids)
    {
        return sysLogSystemMapper.deleteSysLogSystemByIds(ids);
    }

    /**
     * 删除系统日志信息
     * 
     * @param id 系统日志主键
     * @return 结果
     */
    @Override
    public int deleteSysLogSystemById(Long id)
    {
        return sysLogSystemMapper.deleteSysLogSystemById(id);
    }
}
