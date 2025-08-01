package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.system.domain.SysLogExecute;
import com.yunpower.system.mapper.SysLogExecuteMapper;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysLogExecuteService;
import com.yunpower.common.datasource.annotation.LogDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 执行日志Service业务层处理
 * 
 * @author JUNFU.WANG
 * @date 2024-01-18
 */
//多数据源（日志数据库，注解在类上适用）
@LogDataSource
@Service
public class SysLogExecuteServiceImpl implements ISysLogExecuteService
{
    @Autowired
    private SysLogExecuteMapper sysLogExecuteMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询执行日志
     * 
     * @param id 执行日志主键
     * @return 执行日志
     */
    @Override
    public SysLogExecute selectSysLogExecuteById(Long id)
    {
        return sysLogExecuteMapper.selectSysLogExecuteById(id);
    }

    /**
     * 查询执行日志列表
     * 
     * @param sysLogExecute 执行日志
     * @return 执行日志
     */
    @Override
    public List<SysLogExecute> selectSysLogExecuteList(SysLogExecute sysLogExecute)
    {
        return sysLogExecuteMapper.selectSysLogExecuteList(sysLogExecute);
    }

    /**
     * 新增执行日志
     * 
     * @param sysLogExecute 执行日志
     * @return 结果
     */
    @Override
    public int insertSysLogExecute(SysLogExecute sysLogExecute)
    {
        if (sysLogExecute.getEntId() == null || sysLogExecute.getEntId() <= 0) {
            sysLogExecute.setEntId(publicService.getCurrentEnterprise());
        }
        if (sysLogExecute.getDeptId() == null || sysLogExecute.getDeptId() <= 0) {
            sysLogExecute.setDeptId(publicService.getCurrentStation());
        }
        return sysLogExecuteMapper.insertSysLogExecute(sysLogExecute);
    }

    /**
     * 修改执行日志
     * 
     * @param sysLogExecute 执行日志
     * @return 结果
     */
    @Override
    public int updateSysLogExecute(SysLogExecute sysLogExecute)
    {
        return sysLogExecuteMapper.updateSysLogExecute(sysLogExecute);
    }

    /**
     * 批量删除执行日志
     * 
     * @param ids 需要删除的执行日志主键
     * @return 结果
     */
    @Override
    public int deleteSysLogExecuteByIds(Long[] ids)
    {
        return sysLogExecuteMapper.deleteSysLogExecuteByIds(ids);
    }

    /**
     * 删除执行日志信息
     * 
     * @param id 执行日志主键
     * @return 结果
     */
    @Override
    public int deleteSysLogExecuteById(Long id)
    {
        return sysLogExecuteMapper.deleteSysLogExecuteById(id);
    }
}
