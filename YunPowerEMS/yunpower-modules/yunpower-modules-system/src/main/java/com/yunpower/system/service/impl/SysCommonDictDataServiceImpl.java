package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.system.mapper.SysCommonDictDataMapper;
import com.yunpower.system.service.ISysCommonDictDataService;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.common.security.utils.DictUtils;
import com.yunpower.system.api.domain.SysCommonDictData;

/**
 * 字典 业务层处理
 *
 * @author yunpower
 */
@Service
public class SysCommonDictDataServiceImpl implements ISysCommonDictDataService {
    @Autowired
    private SysCommonDictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysCommonDictData> selectDictDataList(SysCommonDictData dictData) {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典信息
     */
    @Override
    public SysCommonDictData selectDictDataBy(String dictType, String dictValue) {
        return dictDataMapper.selectDictDataBy(dictType, dictValue);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    @Override
    public String selectDictValue(String dictType, String dictLabel) {
        return dictDataMapper.selectDictValue(dictType, dictLabel);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysCommonDictData selectDictDataById(Long dictCode) {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysCommonDictData data = selectDictDataById(dictCode);
            dictDataMapper.deleteDictDataById(dictCode);
            List<SysCommonDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictSn());
            DictUtils.setDictCache(data.getDictSn(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysCommonDictData data) {
        data.setCreateBy(SecurityUtils.getNickName());
        data.setCreateTime(DateUtils.getNowDate());
        if (data.getStopFlag() == null) {
            data.setStopFlag(0);
        }
        data.setDeleteFlag(0);

        int row = dictDataMapper.insertDictData(data);
        if (row > 0) {
            List<SysCommonDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictSn());
            DictUtils.setDictCache(data.getDictSn(), dictDatas);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysCommonDictData data) {
        data.setUpdateBy(SecurityUtils.getNickName());
        data.setUpdateTime(DateUtils.getNowDate());
        int row = dictDataMapper.updateDictData(data);
        if (row > 0) {
            List<SysCommonDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictSn());
            DictUtils.setDictCache(data.getDictSn(), dictDatas);
        }
        return row;
    }

    /**
     * 修改通用字典-数据状态
     *
     * @param id    通用字典-数据主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateSysCommonDictDataState(Long id, Integer state) {
        SysCommonDictData sysCommonDictData = new SysCommonDictData();
        sysCommonDictData.setId(id);
        sysCommonDictData.setStopFlag(state);
        sysCommonDictData.setUpdateBy(SecurityUtils.getNickName());
        sysCommonDictData.setUpdateTime(DateUtils.getNowDate());
        return dictDataMapper.updateDictData(sysCommonDictData);
    }

}
