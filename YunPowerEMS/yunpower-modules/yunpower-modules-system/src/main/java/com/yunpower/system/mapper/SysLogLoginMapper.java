package com.yunpower.system.mapper;

import java.util.List;
import com.yunpower.system.api.domain.SysLogLogin;

/**
 * 系统访问日志情况信息 数据层
 * 
 * @author yunpower
 */
public interface SysLogLoginMapper
{
    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    public int insertLogininfor(SysLogLogin logininfor);

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    public List<SysLogLogin> selectLogininforList(SysLogLogin logininfor);

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     * 
     * @return 结果
     */
    public int cleanLogininfor();
}
