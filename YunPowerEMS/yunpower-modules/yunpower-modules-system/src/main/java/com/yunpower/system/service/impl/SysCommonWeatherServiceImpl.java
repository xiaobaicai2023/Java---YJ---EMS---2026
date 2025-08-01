package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.SysCommonWeatherMapper;
import com.yunpower.system.domain.SysCommonWeather;
import com.yunpower.system.service.ISysCommonWeatherService;

/**
 * 通用天气数据Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysCommonWeatherServiceImpl implements ISysCommonWeatherService {
    @Autowired
    private SysCommonWeatherMapper sysCommonWeatherMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询通用天气数据
     *
     * @param id 通用天气数据主键
     * @return 通用天气数据
     */
    @Override
    public SysCommonWeather selectSysCommonWeatherById(Long id) {
        return sysCommonWeatherMapper.selectSysCommonWeatherById(id);
    }

    /**
     * 查询通用天气数据列表
     *
     * @param sysCommonWeather 通用天气数据
     * @return 通用天气数据
     */
    @Override
    public List<SysCommonWeather> selectSysCommonWeatherList(SysCommonWeather sysCommonWeather) {
        return sysCommonWeatherMapper.selectSysCommonWeatherList(sysCommonWeather);
    }

    /**
     * 新增通用天气数据
     *
     * @param sysCommonWeather 通用天气数据
     * @return 结果
     */
    @Override
    public int insertSysCommonWeather(SysCommonWeather sysCommonWeather) {
        sysCommonWeather.setCreateBy(SecurityUtils.getNickName());
        sysCommonWeather.setCreateTime(DateUtils.getNowDate());
        if (sysCommonWeather.getStopFlag() == null) {
            sysCommonWeather.setStopFlag(0);
        }
        sysCommonWeather.setDeleteFlag(0);
        return sysCommonWeatherMapper.insertSysCommonWeather(sysCommonWeather);
    }

    /**
     * 修改通用天气数据
     *
     * @param sysCommonWeather 通用天气数据
     * @return 结果
     */
    @Override
    public int updateSysCommonWeather(SysCommonWeather sysCommonWeather) {
        sysCommonWeather.setUpdateBy(SecurityUtils.getNickName());
        sysCommonWeather.setUpdateTime(DateUtils.getNowDate());
        return sysCommonWeatherMapper.updateSysCommonWeather(sysCommonWeather);
    }

    /**
     * 修改通用天气数据状态
     *
     * @param id    通用天气数据主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateSysCommonWeatherState(Long id, Integer state) {
        SysCommonWeather sysCommonWeather = new SysCommonWeather();
        sysCommonWeather.setId(id);
        sysCommonWeather.setStopFlag(state);
        sysCommonWeather.setUpdateBy(SecurityUtils.getNickName());
        sysCommonWeather.setUpdateTime(DateUtils.getNowDate());
        return sysCommonWeatherMapper.updateSysCommonWeather(sysCommonWeather);
    }

    /**
     * 批量删除通用天气数据
     *
     * @param ids 需要删除的通用天气数据主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonWeatherByIds(Long[] ids) {
        return sysCommonWeatherMapper.deleteSysCommonWeatherByIds(ids);
    }

    /**
     * 删除通用天气数据信息
     *
     * @param id 通用天气数据主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonWeatherById(Long id) {
        return sysCommonWeatherMapper.deleteSysCommonWeatherById(id);
    }
}
