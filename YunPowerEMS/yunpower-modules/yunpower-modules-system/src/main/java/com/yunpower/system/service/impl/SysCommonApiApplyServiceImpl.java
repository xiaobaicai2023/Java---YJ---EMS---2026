package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.SysCommonApiApplyMapper;
import com.yunpower.system.domain.SysCommonApiApply;
import com.yunpower.system.service.ISysCommonApiApplyService;

/**
 * 第三方接入申请Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysCommonApiApplyServiceImpl implements ISysCommonApiApplyService {
    @Autowired
    private SysCommonApiApplyMapper sysCommonApiApplyMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询第三方接入申请
     *
     * @param id 第三方接入申请主键
     * @return 第三方接入申请
     */
    @Override
    public SysCommonApiApply selectSysCommonApiApplyById(Long id) {
        return sysCommonApiApplyMapper.selectSysCommonApiApplyById(id);
    }

    /**
     * 查询第三方接入申请列表
     *
     * @param sysCommonApiApply 第三方接入申请
     * @return 第三方接入申请
     */
    @Override
    public List<SysCommonApiApply> selectSysCommonApiApplyList(SysCommonApiApply sysCommonApiApply) {
        return sysCommonApiApplyMapper.selectSysCommonApiApplyList(sysCommonApiApply);
    }

    /**
     * 新增第三方接入申请
     *
     * @param sysCommonApiApply 第三方接入申请
     * @return 结果
     */
    @Override
    public int insertSysCommonApiApply(SysCommonApiApply sysCommonApiApply) {
        sysCommonApiApply.setCreateBy(SecurityUtils.getNickName());
        sysCommonApiApply.setCreateTime(DateUtils.getNowDate());
        if (sysCommonApiApply.getStopFlag() == null) {
            sysCommonApiApply.setStopFlag(0);
        }
        sysCommonApiApply.setDeleteFlag(0);
        return sysCommonApiApplyMapper.insertSysCommonApiApply(sysCommonApiApply);
    }

    /**
     * 修改第三方接入申请
     *
     * @param sysCommonApiApply 第三方接入申请
     * @return 结果
     */
    @Override
    public int updateSysCommonApiApply(SysCommonApiApply sysCommonApiApply) {
        sysCommonApiApply.setUpdateBy(SecurityUtils.getNickName());
        sysCommonApiApply.setUpdateTime(DateUtils.getNowDate());
        return sysCommonApiApplyMapper.updateSysCommonApiApply(sysCommonApiApply);
    }

    /**
     * 修改第三方接入申请状态
     *
     * @param id    第三方接入申请主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateSysCommonApiApplyState(Long id, Integer state) {
        SysCommonApiApply sysCommonApiApply = new SysCommonApiApply();
        sysCommonApiApply.setId(id);
        sysCommonApiApply.setStopFlag(state);
        sysCommonApiApply.setUpdateBy(SecurityUtils.getNickName());
        sysCommonApiApply.setUpdateTime(DateUtils.getNowDate());
        return sysCommonApiApplyMapper.updateSysCommonApiApply(sysCommonApiApply);
    }

    /**
     * 批量删除第三方接入申请
     *
     * @param ids 需要删除的第三方接入申请主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonApiApplyByIds(Long[] ids) {
        return sysCommonApiApplyMapper.deleteSysCommonApiApplyByIds(ids);
    }

    /**
     * 删除第三方接入申请信息
     *
     * @param id 第三方接入申请主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonApiApplyById(Long id) {
        return sysCommonApiApplyMapper.deleteSysCommonApiApplyById(id);
    }
}
