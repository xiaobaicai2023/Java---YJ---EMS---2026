package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysCommonApiApply;

/**
 * 第三方接入申请Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface SysCommonApiApplyMapper
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
     * 删除第三方接入申请
     *
     * @param id 第三方接入申请主键
     * @return 结果
     */
    public int deleteSysCommonApiApplyById(Long id);

    /**
     * 批量删除第三方接入申请
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCommonApiApplyByIds(Long[] ids);
}
