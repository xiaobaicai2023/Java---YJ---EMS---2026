package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysCommonWeather;

/**
 * 通用天气数据Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface SysCommonWeatherMapper
{
    /**
     * 查询通用天气数据
     *
     * @param id 通用天气数据主键
     * @return 通用天气数据
     */
    public SysCommonWeather selectSysCommonWeatherById(Long id);

    /**
     * 查询通用天气数据列表
     *
     * @param sysCommonWeather 通用天气数据
     * @return 通用天气数据集合
     */
    public List<SysCommonWeather> selectSysCommonWeatherList(SysCommonWeather sysCommonWeather);

    /**
     * 新增通用天气数据
     *
     * @param sysCommonWeather 通用天气数据
     * @return 结果
     */
    public int insertSysCommonWeather(SysCommonWeather sysCommonWeather);

    /**
     * 修改通用天气数据
     *
     * @param sysCommonWeather 通用天气数据
     * @return 结果
     */
    public int updateSysCommonWeather(SysCommonWeather sysCommonWeather);

    /**
     * 删除通用天气数据
     *
     * @param id 通用天气数据主键
     * @return 结果
     */
    public int deleteSysCommonWeatherById(Long id);

    /**
     * 批量删除通用天气数据
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCommonWeatherByIds(Long[] ids);
}
