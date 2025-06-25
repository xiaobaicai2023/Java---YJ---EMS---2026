package com.yunpower.collect.storage.service;


import com.yunpower.collect.storage.domain.SysCommonCreate;

import java.util.List;

/**
 * 生成记录Service接口
 * 
 * @author JUNFU.WANG
 * @date 2023-10-24
 */
public interface ISysCommonCreateService 
{
    /**
     * 查询生成记录
     * 
     * @param createTable 生成记录主键
     * @return 生成记录
     */
    public SysCommonCreate selectSysCommonCreateByCreateTable(String createTable);

    /**
     * 查询生成记录列表
     * 
     * @param sysCommonCreate 生成记录
     * @return 生成记录集合
     */
    public List<SysCommonCreate> selectSysCommonCreateList(SysCommonCreate sysCommonCreate);

    /**
     * 新增生成记录
     * 
     * @param sysCommonCreate 生成记录
     * @return 结果
     */
    public int insertSysCommonCreate(SysCommonCreate sysCommonCreate);

    /**
     * 修改生成记录
     * 
     * @param sysCommonCreate 生成记录
     * @return 结果
     */
    public int updateSysCommonCreate(SysCommonCreate sysCommonCreate);

    /**
     * 批量删除生成记录
     * 
     * @param createTables 需要删除的生成记录主键集合
     * @return 结果
     */
    public int deleteSysCommonCreateByCreateTables(String[] createTables);

    /**
     * 删除生成记录信息
     * 
     * @param createTable 生成记录主键
     * @return 结果
     */
    public int deleteSysCommonCreateByCreateTable(String createTable);
}
