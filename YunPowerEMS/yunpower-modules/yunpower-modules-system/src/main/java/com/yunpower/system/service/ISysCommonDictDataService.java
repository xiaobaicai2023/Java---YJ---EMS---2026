package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.api.domain.SysCommonDictData;

/**
 * 字典 业务层
 *
 * @author yunpower
 */
public interface ISysCommonDictDataService {
    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public List<SysCommonDictData> selectDictDataList(SysCommonDictData dictData);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典信息
     */
    public SysCommonDictData selectDictDataBy(String dictType, String dictValue);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * 根据字典类型和字典标签查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典键值
     */
    public String selectDictValue(String dictType, String dictLabel);

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    public SysCommonDictData selectDictDataById(Long dictCode);

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    public void deleteDictDataByIds(Long[] dictCodes);

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int insertDictData(SysCommonDictData dictData);

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int updateDictData(SysCommonDictData dictData);

    /**
     * 修改通用字典-数据状态
     *
     * @param id    通用字典-数据主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysCommonDictDataState(Long id, Integer state);
}
