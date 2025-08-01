package com.yunpower.system.service;

import java.util.List;
import java.util.Map;

import com.yunpower.system.domain.ElectricPriceSchemeConfig;
import com.yunpower.system.domain.jsonvo.SeasonalRangeChartVo;

/**
 * 电度电价配置Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
public interface IElectricPriceSchemeConfigService {
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
     * @param SchemeId 电价标准ID
     * @param month    当前月份
     * @return 电度电价配置
     */
    public ElectricPriceSchemeConfig selectElectricPriceSchemeConfigBySchemeId(Long SchemeId, Integer month);

    /**
     * 通过电价标准ID取出所有的电度电价配置
     *
     * @param schemeId 电价标准ID
     * @return 电度电价配置
     */
    public List<ElectricPriceSchemeConfig> selectElectricPriceSchemeBySchemeId(Long schemeId);

    /**
     * 通过电价标准ID重新计算所有的分时电价
     *
     * @param schemeId 电价标准ID
     * @return 结果
     */
    public Integer recalculateSeasonalPriceBySchemeId(Long schemeId);

    /**
     * 重新计算分时电价
     *
     * @param id 电度电价配置主键
     * @return 结果
     */
    public Integer recalculateSeasonalPrice(Long id);

    /**
     * 查询电度电价配置列表
     *
     * @param electricPriceSchemeConfig 电度电价配置
     * @return 电度电价配置集合
     */
    public List<ElectricPriceSchemeConfig> selectElectricPriceSchemeConfigList(ElectricPriceSchemeConfig electricPriceSchemeConfig);

    /**
     * 通过电价标准ID获取尖峰平谷时间段
     *
     * @param schemeId 电价标准ID
     * @return 结果
     */
    public Map<String, List<SeasonalRangeChartVo>> selectSeasonalRangeList(Long schemeId);

    /**
     * 通过电价标准ID获取尖峰平谷时间段-堆叠图-日查询
     *
     * @param schemeId 电价标准ID
     * @return 结果
     */
    public List<Map<String, Object>> selectSeasonalRangeListForDayStack(Long schemeId);

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
     * 批量删除电度电价配置
     *
     * @param ids 需要删除的电度电价配置主键集合
     * @return 结果
     */
    public int deleteElectricPriceSchemeConfigByIds(Long[] ids);

    /**
     * 删除电度电价配置信息
     *
     * @param id 电度电价配置主键
     * @return 结果
     */
    public int deleteElectricPriceSchemeConfigById(Long id);

    /**
     * 删除电度电价配置信息（通过电价标准ID）
     *
     * @param schemeId 电价标准ID
     * @return 结果
     */
    public int deleteElectricPriceSchemeConfigBySchemeId(Long schemeId);

    /**
     * 自动生成电价标准配置
     */
    public void autoGenElectricSchemeSync(Long entId, Long deptId) throws Exception;
}
