package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysCommonApiApply;

/**
 * 第三方接入申请Service接口
 * 
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ISysCommonApiApplyService 
{
    /**
     * 查询第三方接入申请
     * 
     * @param id 第三方接入申请主键
     * @return 第三方接入申请
     */
    public SysCommonApiApply selectSysCommonApiApplyById(Long id);

    /**
     * 查询第三方接入申请列表
     * 
     * @param sysCommonApiApply 第三方接入申请
     * @return 第三方接入申请集合
     */
    public List<SysCommonApiApply> selectSysCommonApiApplyList(SysCommonApiApply sysCommonApiApply);

    /**
     * 新增第三方接入申请
     * 
     * @param sysCommonApiApply 第三方接入申请
     * @return 结果
     */
    public int insertSysCommonApiApply(SysCommonApiApply sysCommonApiApply);

    /**
     * 修改第三方接入申请
     * 
     * @param sysCommonApiApply 第三方接入申请
     * @return 结果
     */
    public int updateSysCommonApiApply(SysCommonApiApply sysCommonApiApply);

    /**
     * 修改第三方接入申请状态
     *
     * @param id 第三方接入申请主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysCommonApiApplyState(Long id, Integer state);

    /**
     * 批量删除第三方接入申请
     * 
     * @param ids 需要删除的第三方接入申请主键集合
     * @return 结果
     */
    public int deleteSysCommonApiApplyByIds(Long[] ids);

    /**
     * 删除第三方接入申请信息
     * 
     * @param id 第三方接入申请主键
     * @return 结果
     */
    public int deleteSysCommonApiApplyById(Long id);
}
