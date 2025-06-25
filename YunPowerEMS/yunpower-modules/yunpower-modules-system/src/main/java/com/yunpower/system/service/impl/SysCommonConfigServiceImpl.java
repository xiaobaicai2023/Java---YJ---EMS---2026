package com.yunpower.system.service.impl;

import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.common.core.constant.CacheConstants;
import com.yunpower.common.core.constant.UserConstants;
import com.yunpower.common.core.exception.ServiceException;
import com.yunpower.common.core.text.Convert;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.redis.service.RedisService;
import com.yunpower.system.domain.SysCommonConfig;
import com.yunpower.system.mapper.SysCommonConfigMapper;
import com.yunpower.system.service.ISysCommonConfigService;

/**
 * 参数配置 服务层实现
 *
 * @author yunpower
 */
@Service
public class SysCommonConfigServiceImpl implements ISysCommonConfigService {
    @Autowired
    private SysCommonConfigMapper configMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysCommonConfig selectConfigById(Long configId) {
        SysCommonConfig config = new SysCommonConfig();
        config.setId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisService.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysCommonConfig config = new SysCommonConfig();
        config.setParamKey(configKey);
        SysCommonConfig retConfig = configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig)) {
            redisService.setCacheObject(getCacheKey(configKey), retConfig.getParamValue());
            return retConfig.getParamValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysCommonConfig> selectConfigList(SysCommonConfig config) {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysCommonConfig config) {
        config.setCreateBy(SecurityUtils.getNickName());
        config.setCreateTime(DateUtils.getNowDate());
        if (config.getStopFlag() == null) {
            config.setStopFlag(0);
        }
        config.setDeleteFlag(0);

        int row = configMapper.insertConfig(config);
        if (row > 0) {
            redisService.setCacheObject(getCacheKey(config.getParamKey()), config.getParamValue());
        }
        return row;
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysCommonConfig config) {
        SysCommonConfig temp = configMapper.selectConfigById(config.getId());
        if (!StringUtils.equals(temp.getParamKey(), config.getParamKey())) {
            redisService.deleteObject(getCacheKey(temp.getParamKey()));
        }

        config.setUpdateBy(SecurityUtils.getNickName());
        config.setUpdateTime(DateUtils.getNowDate());
        int row = configMapper.updateConfig(config);
        if (row > 0) {
            redisService.setCacheObject(getCacheKey(config.getParamKey()), config.getParamValue());
        }
        return row;
    }

    /**
     * 修改参数配置状态
     *
     * @param id    主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateConfigState(Long id, Integer state) {
        SysCommonConfig sysCommonConfig = new SysCommonConfig();
        sysCommonConfig.setId(id);
        sysCommonConfig.setStopFlag(state);
        sysCommonConfig.setUpdateBy(SecurityUtils.getNickName());
        sysCommonConfig.setUpdateTime(DateUtils.getNowDate());
        return configMapper.updateConfig(sysCommonConfig);
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysCommonConfig config = selectConfigById(configId);
            if (UserConstants.YES.equals(config.getIsSystem())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getParamKey()));
            }
            configMapper.deleteConfigById(configId);
            redisService.deleteObject(getCacheKey(config.getParamKey()));
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<SysCommonConfig> configsList = configMapper.selectConfigList(new SysCommonConfig());
        for (SysCommonConfig config : configsList) {
            redisService.setCacheObject(getCacheKey(config.getParamKey()), config.getParamValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        Collection<String> keys = redisService.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisService.deleteObject(keys);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysCommonConfig config) {
        Long configId = StringUtils.isNull(config.getId()) ? -1L : config.getId();
        SysCommonConfig info = configMapper.checkConfigKeyUnique(config.getParamKey());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != configId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
