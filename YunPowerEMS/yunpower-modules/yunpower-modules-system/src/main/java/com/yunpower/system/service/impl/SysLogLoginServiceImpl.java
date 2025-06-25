package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.common.datasource.annotation.LogDataSource;
import com.yunpower.system.mapper.SysLogLoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.api.domain.SysLogLogin;
import com.yunpower.system.service.ISysLogLoginService;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author yunpower
 */
//多数据源（日志数据库，注解在类上适用）
@LogDataSource
@Service
public class SysLogLoginServiceImpl implements ISysLogLoginService {

    @Autowired
    private SysLogLoginMapper logininforMapper;

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    @Override
    public int insertLogininfor(SysLogLogin logininfor) {
        return logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLogLogin> selectLogininforList(SysLogLogin logininfor) {
        return logininforMapper.selectLogininforList(logininfor);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds) {
        return logininforMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        logininforMapper.cleanLogininfor();
    }
}
