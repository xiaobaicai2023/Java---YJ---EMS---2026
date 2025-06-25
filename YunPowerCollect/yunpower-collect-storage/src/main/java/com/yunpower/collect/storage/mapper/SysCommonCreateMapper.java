package com.yunpower.collect.storage.mapper;


import com.yunpower.collect.storage.domain.SysCommonCreate;

import java.util.List;

/**
 * 生成记录Mapper接口
 * 
 * @author JUNFU.WANG
 * @date 2023-10-24
 */
public interface SysCommonCreateMapper 
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
     * 删除生成记录
     * 
     * @param createTable 生成记录主键
     * @return 结果
     */
    public int deleteSysCommonCreateByCreateTable(String createTable);

    /**
     * 批量删除生成记录
     * 
     * @param createTables 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCommonCreateByCreateTables(String[] createTables);
}
