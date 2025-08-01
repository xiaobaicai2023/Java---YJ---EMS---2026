package com.yunpower.system.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yunpower.common.core.constant.UserConstants;
import com.yunpower.common.core.exception.ServiceException;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.security.utils.DictUtils;
import com.yunpower.system.api.domain.SysCommonDictData;
import com.yunpower.system.api.domain.SysCommonDict;
import com.yunpower.system.mapper.SysCommonDictDataMapper;
import com.yunpower.system.mapper.SysCommonDictMapper;
import com.yunpower.system.service.ISysCommonDictService;

/**
 * 字典 业务层处理
 *
 * @author yunpower
 */
@Service
public class SysCommonDictServiceImpl implements ISysCommonDictService {
    @Autowired
    private SysCommonDictMapper dictTypeMapper;

    @Autowired
    private SysCommonDictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<SysCommonDict> selectDictTypeList(SysCommonDict dictType) {
        return dictTypeMapper.selectDictTypeList(dictType);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysCommonDict> selectDictTypeAll() {
        return dictTypeMapper.selectDictTypeAll();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysCommonDictData> selectDictDataByType(String dictType) {
        List<SysCommonDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = dictDataMapper.selectDictDataByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysCommonDict selectDictTypeById(Long dictId) {
        return dictTypeMapper.selectDictTypeById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public SysCommonDict selectDictTypeByType(String dictType) {
        return dictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysCommonDict dictType = selectDictTypeById(dictId);
            if (dictDataMapper.countDictDataByType(dictType.getDictSn()) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            dictTypeMapper.deleteDictTypeById(dictId);
            DictUtils.removeDictCache(dictType.getDictSn());
        }
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        SysCommonDictData dictData = new SysCommonDictData();
        dictData.setStopFlag(0);
        Map<String, List<SysCommonDictData>> dictDataMap = dictDataMapper.selectDictDataList(dictData).stream().collect(Collectors.groupingBy(SysCommonDictData::getDictSn));
        for (Map.Entry<String, List<SysCommonDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysCommonDictData::getOrderNum)).collect(Collectors.toList()));
        }
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(SysCommonDict dict) {
        dict.setCreateBy(SecurityUtils.getNickName());
        dict.setCreateTime(DateUtils.getNowDate());
        if (dict.getStopFlag() == null) {
            dict.setStopFlag(0);
        }
        dict.setDeleteFlag(0);

        int row = dictTypeMapper.insertDictType(dict);
        if (row > 0) {
            DictUtils.setDictCache(dict.getDictSn(), null);
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDictType(SysCommonDict dict) {
        SysCommonDict oldDict = dictTypeMapper.selectDictTypeById(dict.getId());
        dictDataMapper.updateDictDataType(oldDict.getDictSn(), dict.getDictSn());

        dict.setUpdateBy(SecurityUtils.getNickName());
        dict.setUpdateTime(DateUtils.getNowDate());
        int row = dictTypeMapper.updateDictType(dict);
        if (row > 0) {
            List<SysCommonDictData> dictDatas = dictDataMapper.selectDictDataByType(dict.getDictSn());
            DictUtils.setDictCache(dict.getDictSn(), dictDatas);
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public boolean checkDictTypeUnique(SysCommonDict dict) {
        Long dictId = StringUtils.isNull(dict.getId()) ? -1L : dict.getId();
        SysCommonDict dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictSn());
        if (StringUtils.isNotNull(dictType) && dictType.getId().longValue() != dictId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 修改通用字典状态
     *
     * @param id    通用字典主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateSysCommonDictState(Long id, Integer state) {
        SysCommonDict sysCommonDict = new SysCommonDict();
        sysCommonDict.setId(id);
        sysCommonDict.setStopFlag(state);
        sysCommonDict.setUpdateBy(SecurityUtils.getNickName());
        sysCommonDict.setUpdateTime(DateUtils.getNowDate());
        return dictTypeMapper.updateDictType(sysCommonDict);
    }

}
