package com.yunpower.system.service;

import com.yunpower.system.api.domain.SysStation;

import java.util.List;

/**
 * @title: 公共方法
 * @Author: Jiajiaglam
 * @date: 2023-09-27 15:25
 * @description:
 */
public interface IPublicService {
    /**
     * 保存用户选择的站点（部门）
     */
    public boolean setCurrentStation(Long deptId);

    /**
     * 保存用户权限范围内的第一个站点，并返回结果
     *
     * @return 站点
     */
    public Long setCurrentStation();

    /**
     * 获取用户当前站点（部门）
     * 如果用户未选择，那么默认将他授权范围内的第一个站点返回来
     */
    public Long getCurrentStation();

    /**
     * 获取当前站点信息
     *
     * @return 电站
     */
    public SysStation getCurrentStationInfo();

    /**
     * 获取用户当前企业
     */
    public Long getCurrentEnterprise();

    /**
     * 查询一张数据表是否存在
     *
     * @param databaseName 数据库名称
     * @param tableName    表名
     * @return 1存在 0不存在
     */
    public boolean judgeTableIfExsits(String databaseName, String tableName);

    /**
     * 获取某类型的数据表
     *
     * @param databaseName 数据库名称
     * @param prefixName   表前缀
     * @return 结果
     */
    public List<String> getSomeTables(String databaseName, String prefixName);
}
