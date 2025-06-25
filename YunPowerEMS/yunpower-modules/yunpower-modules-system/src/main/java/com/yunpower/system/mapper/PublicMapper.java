package com.yunpower.system.mapper;

import com.yunpower.system.api.domain.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: 公共方法
 * @Author: Jiajiaglam
 * @date: 2023-10-02 16:42
 * @description:
 */
public interface PublicMapper {

    /**
     * 查询一张数据表是否存在（理论上用户是没有这个权限的）
     *
     * @param databaseName 数据库名称
     * @param tableName    表名
     * @return 1存在 0不存在
     */
    public Integer judgeTableIfExsits(@Param("databaseName") String databaseName, @Param("tableName") String tableName);

    /**
     * 获取某类型的数据表（理论上用户是没有这个权限的）
     *
     * @param databaseName 数据库名称
     * @param prefixName   表前缀
     * @return 结果
     */
    public List<String> getSomeTables(@Param("databaseName") String databaseName, @Param("prefixName") String prefixName);
}
