package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.SysCommonApiInterfaceMapper;
import com.yunpower.system.domain.SysCommonApiInterface;
import com.yunpower.system.service.ISysCommonApiInterfaceService;

/**
 * 数据接口Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysCommonApiInterfaceServiceImpl implements ISysCommonApiInterfaceService {
    @Autowired
    private SysCommonApiInterfaceMapper sysCommonApiInterfaceMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询数据接口
     *
     * @param id 数据接口主键
     * @return 数据接口
     */
    @Override
    public SysCommonApiInterface selectSysCommonApiInterfaceById(Long id) {
        return sysCommonApiInterfaceMapper.selectSysCommonApiInterfaceById(id);
    }

    /**
     * 查询数据接口列表
     *
     * @param sysCommonApiInterface 数据接口
     * @return 数据接口
     */
    @Override
    public List<SysCommonApiInterface> selectSysCommonApiInterfaceList(SysCommonApiInterface sysCommonApiInterface) {
        return sysCommonApiInterfaceMapper.selectSysCommonApiInterfaceList(sysCommonApiInterface);
    }

    /**
     * 新增数据接口
     *
     * @param sysCommonApiInterface 数据接口
     * @return 结果
     */
    @Override
    public int insertSysCommonApiInterface(SysCommonApiInterface sysCommonApiInterface) {
        sysCommonApiInterface.setCreateBy(SecurityUtils.getNickName());
        sysCommonApiInterface.setCreateTime(DateUtils.getNowDate());
        if (sysCommonApiInterface.getStopFlag() == null) {
            sysCommonApiInterface.setStopFlag(0);
        }
        sysCommonApiInterface.setDeleteFlag(0);
        return sysCommonApiInterfaceMapper.insertSysCommonApiInterface(sysCommonApiInterface);
    }

    /**
     * 修改数据接口
     *
     * @param sysCommonApiInterface 数据接口
     * @return 结果
     */
    @Override
    public int updateSysCommonApiInterface(SysCommonApiInterface sysCommonApiInterface) {
        sysCommonApiInterface.setUpdateBy(SecurityUtils.getNickName());
        sysCommonApiInterface.setUpdateTime(DateUtils.getNowDate());
        return sysCommonApiInterfaceMapper.updateSysCommonApiInterface(sysCommonApiInterface);
    }

    /**
     * 修改数据接口状态
     *
     * @param id    数据接口主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateSysCommonApiInterfaceState(Long id, Integer state) {
        SysCommonApiInterface sysCommonApiInterface = new SysCommonApiInterface();
        sysCommonApiInterface.setId(id);
        sysCommonApiInterface.setStopFlag(state);
        sysCommonApiInterface.setUpdateBy(SecurityUtils.getNickName());
        sysCommonApiInterface.setUpdateTime(DateUtils.getNowDate());
        return sysCommonApiInterfaceMapper.updateSysCommonApiInterface(sysCommonApiInterface);
    }

    /**
     * 批量删除数据接口
     *
     * @param ids 需要删除的数据接口主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonApiInterfaceByIds(Long[] ids) {
        return sysCommonApiInterfaceMapper.deleteSysCommonApiInterfaceByIds(ids);
    }

    /**
     * 删除数据接口信息
     *
     * @param id 数据接口主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonApiInterfaceById(Long id) {
        return sysCommonApiInterfaceMapper.deleteSysCommonApiInterfaceById(id);
    }
}
