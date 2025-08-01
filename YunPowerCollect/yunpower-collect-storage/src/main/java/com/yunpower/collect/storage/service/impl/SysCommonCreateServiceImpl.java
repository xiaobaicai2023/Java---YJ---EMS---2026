package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.SysCommonCreate;
import com.yunpower.collect.storage.mapper.SysCommonCreateMapper;
import com.yunpower.collect.storage.service.ISysCommonCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 生成记录Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-24
 */
@Service
public class SysCommonCreateServiceImpl implements ISysCommonCreateService {
    @Autowired
    private SysCommonCreateMapper sysCommonCreateMapper;

    /**
     * 查询生成记录
     *
     * @param createTable 生成记录主键
     * @return 生成记录
     */
    @Override
    public SysCommonCreate selectSysCommonCreateByCreateTable(String createTable) {
        return sysCommonCreateMapper.selectSysCommonCreateByCreateTable(createTable);
    }

    /**
     * 查询生成记录列表
     *
     * @param sysCommonCreate 生成记录
     * @return 生成记录
     */
    @Override
    public List<SysCommonCreate> selectSysCommonCreateList(SysCommonCreate sysCommonCreate) {
        return sysCommonCreateMapper.selectSysCommonCreateList(sysCommonCreate);
    }

    /**
     * 新增生成记录
     *
     * @param sysCommonCreate 生成记录
     * @return 结果
     */
    @Override
    public int insertSysCommonCreate(SysCommonCreate sysCommonCreate) {
        return sysCommonCreateMapper.insertSysCommonCreate(sysCommonCreate);
    }

    /**
     * 修改生成记录
     *
     * @param sysCommonCreate 生成记录
     * @return 结果
     */
    @Override
    public int updateSysCommonCreate(SysCommonCreate sysCommonCreate) {
        return sysCommonCreateMapper.updateSysCommonCreate(sysCommonCreate);
    }

    /**
     * 批量删除生成记录
     *
     * @param createTables 需要删除的生成记录主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonCreateByCreateTables(String[] createTables) {
        return sysCommonCreateMapper.deleteSysCommonCreateByCreateTables(createTables);
    }

    /**
     * 删除生成记录信息
     *
     * @param createTable 生成记录主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonCreateByCreateTable(String createTable) {
        return sysCommonCreateMapper.deleteSysCommonCreateByCreateTable(createTable);
    }
}
