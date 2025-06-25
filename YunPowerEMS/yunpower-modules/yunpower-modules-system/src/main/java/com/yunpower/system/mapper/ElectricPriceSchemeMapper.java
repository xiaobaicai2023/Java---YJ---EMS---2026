package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.ElectricPriceScheme;
import org.apache.ibatis.annotations.Param;

/**
 * 电价标准Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
public interface ElectricPriceSchemeMapper {
    /**
     * 查询电价标准
     *
     * @param id 电价标准主键
     * @return 电价标准
     */
    public ElectricPriceScheme selectElectricPriceSchemeById(Long id);

    /**
     * 查询电价标准
     */
    public ElectricPriceScheme selectElectricPriceScheme(ElectricPriceScheme electricPriceScheme);

    /**
     * 查询电价标准列表
     *
     * @param electricPriceScheme 电价标准
     * @return 电价标准集合
     */
    public List<ElectricPriceScheme> selectElectricPriceSchemeList(ElectricPriceScheme electricPriceScheme);

    /**
     * 新增电价标准
     *
     * @param electricPriceScheme 电价标准
     * @return 结果
     */
    public int insertElectricPriceScheme(ElectricPriceScheme electricPriceScheme);

    /**
     * 修改电价标准
     *
     * @param electricPriceScheme 电价标准
     * @return 结果
     */
    public int updateElectricPriceScheme(ElectricPriceScheme electricPriceScheme);

    /**
     * 删除电价标准
     *
     * @param id 电价标准主键
     * @return 结果
     */
    public int deleteElectricPriceSchemeById(Long id);

    /**
     * 批量删除电价标准
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteElectricPriceSchemeByIds(Long[] ids);

    /**
     * 校验配置年份是否唯一：新增是只校验deptId，修改和启用时还要校验id
     *
     * @param deptId 站点（部门）ID
     * @param effectYear     生效年份
     * @return 结果
     */
    public ElectricPriceScheme checkEffectYearUnique(@Param("deptId") Long deptId, @Param("effectYear") Integer effectYear);
}
