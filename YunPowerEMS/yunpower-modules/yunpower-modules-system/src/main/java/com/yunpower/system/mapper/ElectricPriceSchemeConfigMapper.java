package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.ElectricPriceSchemeConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 电度电价配置Mapper接口
 * 
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
public interface ElectricPriceSchemeConfigMapper 
{
    /**
     * 查询电度电价配置
     * 
     * @param id 电度电价配置主键
     * @return 电度电价配置
     */
    public ElectricPriceSchemeConfig selectElectricPriceSchemeConfigById(Long id);

    /**
     * 通过电价ID和月份获取符合的配置
     *
     * @param schemeId 电价标准ID
     * @param month    当前月份
     * @return 电度电价配置
     */
    public ElectricPriceSchemeConfig selectElectricPriceSchemeConfigBySchemeId(@Param("schemeId") Long schemeId, @Param("month") Integer month);

    /**
     * 通过电价标准ID获取尖峰平谷时间段
     *
     * @param schemeId 电价标准ID
     * @return 电度电价配置
     */
    public ElectricPriceSchemeConfig selectElectricPriceSchemeConfigForSchemeId(@Param("schemeId") Long schemeId);

    /**
     * 通过电价标准ID获取尖峰平谷时间段
     *
     * @param schemeId 电价标准ID
     * @param month 当前月
     * @return 电度电价配置
     */
    public ElectricPriceSchemeConfig selectElectricPriceSchemeConfigForFenDuanTu(@Param("schemeId") Long schemeId, @Param("month") Integer month);

    /**
     * 查询电度电价配置列表
     * 
     * @param electricPriceSchemeConfig 电度电价配置
     * @return 电度电价配置集合
     */
    public List<ElectricPriceSchemeConfig> selectElectricPriceSchemeConfigList(ElectricPriceSchemeConfig electricPriceSchemeConfig);

    /**
     * 新增电度电价配置
     * 
     * @param electricPriceSchemeConfig 电度电价配置
     * @return 结果
     */
    public int insertElectricPriceSchemeConfig(ElectricPriceSchemeConfig electricPriceSchemeConfig);

    /**
     * 修改电度电价配置
     * 
     * @param electricPriceSchemeConfig 电度电价配置
     * @return 结果
     */
    public int updateElectricPriceSchemeConfig(ElectricPriceSchemeConfig electricPriceSchemeConfig);

    /**
     * 删除电度电价配置
     * 
     * @param id 电度电价配置主键
     * @return 结果
     */
    public int deleteElectricPriceSchemeConfigById(Long id);

    /**
     * 批量删除电度电价配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteElectricPriceSchemeConfigByIds(Long[] ids);

    /**
     * 删除电度电价配置信息（通过电价标准ID）
     *
     * @param schemeId 电价标准ID
     * @return 结果
     */
    public int deleteElectricPriceSchemeConfigBySchemeId(Long schemeId);
}
