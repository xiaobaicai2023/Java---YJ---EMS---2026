package com.yunpower.system.service;

/**
 * @title: 自动执行，异步完成的业务
 * @Author: Jiajiaglam
 * @date: 2024-02-23 14:05
 * @description:
 */
public interface IAutoGenSyncService {

    /**
     * 创建【站点】时，完成相关的业务（异步执行）
     * 将来此处要放到MQ中，防止多人同时操作时发生冲突
     */
    public void autoGenSyncForStation(String stationSn) throws Exception;

    /**
     * 创建、修改【企业】和修改【电站】时，完成相关的业务（异步执行）
     */
    public void autoGenSyncForDept() throws Exception;
}
