package com.yunpower.system.service.impl;

import com.yunpower.common.core.constant.CacheConstants;
import com.yunpower.common.core.constant.HttpStatus;
import com.yunpower.common.core.exception.ServiceException;
import com.yunpower.common.core.text.Convert;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.redis.service.RedisService;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.mapper.PublicMapper;
import com.yunpower.system.mapper.SysStationMapper;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: 公共方法
 * @Author: Jiajiaglam
 * @date: 2023-09-27 15:24
 * @description:
 */
@Service
public class PublicServiceImpl implements IPublicService {

    @Autowired
    private PublicMapper publicMapper;

    @Autowired
    private SysStationMapper stationMapper;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private RedisService redisService;

    /**
     * 保存用户选择的站点（部门）
     */
    @Override
    public boolean setCurrentStation(Long deptId) {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return false;
        }

        // 如果 deptId 不为空，则保存到缓存，否则清空原有缓存
        if (StringUtils.isNotNull(deptId)) {
            redisService.setCacheObject(getCacheKey(userId), deptId);
            return true;
        } else {
            redisService.deleteObject(getCacheKey(userId));
            return false;
        }
    }

    /**
     * 保存用户权限范围内的第一个站点，并返回结果
     *
     * @return 站点
     */
    @Override
    public Long setCurrentStation() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return 0L;
        }

        // 取有数据权限的第一个站点
        Long deptId = deptService.getUserFirstAuthDeptId(new SysDept());

        // 如果 deptId 不为空，则保存到缓存，否则清空原有缓存
        if (!StringUtils.longIsBlack0(deptId)) {
            redisService.setCacheObject(getCacheKey(userId), deptId);
            return deptId;
        } else {
            redisService.deleteObject(getCacheKey(userId));
            return 0L;
        }
    }

    /**
     * 获取用户当前站点（部门）ID
     * 如果用户未选择，那么默认将他授权范围内的第一个站点返回来
     */
    @Override
    public Long getCurrentStation() {
        Long userId = SecurityUtils.getUserId();
        Long deptId = Convert.toLong(redisService.getCacheObject(getCacheKey(userId)));
        if (deptId == null) {
            deptId = setCurrentStation();
        }
        return deptId;
    }

    /**
     * 获取当前站点信息
     *
     * @return 电站
     */
    @Override
    public SysStation getCurrentStationInfo() {
        // 获取当前站点ID
        Long deptId = getCurrentStation();
        // 获取当前站点站点信息
        return stationMapper.selectSysStationByDeptId(deptId);
    }

    /**
     * 获取用户当前企业
     * 如果不存在，则抛出异常
     */
    @Override
    public Long getCurrentEnterprise() {
        Long userId = SecurityUtils.getUserId();
        Long deptId = Convert.toLong(redisService.getCacheObject(getCacheKey(userId)));

        if (deptId == null) {
            deptId = setCurrentStation();
        }

        SysDept sysDept = deptService.selectDeptById(deptId);
        if (sysDept != null) {
            return sysDept.getEntId();
        } else {
            throw new ServiceException("站点异常，请重新选择", HttpStatus.NON_STATION);
        }
    }

    /**
     * 设置cache key
     *
     * @param userId 用户ID
     * @return 缓存键key
     */
    private String getCacheKey(Long userId) {
        return CacheConstants.SYS_SELECT_STATION_KEY + userId;
    }

    /**
     * 查询一张数据表是否存在
     *
     * @param databaseName 数据库名称
     * @param tableName    表名
     * @return 1存在 0不存在
     */
    public boolean judgeTableIfExsits(String databaseName, String tableName) {
        return publicMapper.judgeTableIfExsits(databaseName, tableName) > 0;
    }

    /**
     * 获取某类型的数据表
     *
     * @param databaseName 数据库名称
     * @param prefixName   表前缀
     * @return 结果
     */
    public List<String> getSomeTables(String databaseName, String prefixName) {
        return publicMapper.getSomeTables(databaseName, prefixName);
    }
}
