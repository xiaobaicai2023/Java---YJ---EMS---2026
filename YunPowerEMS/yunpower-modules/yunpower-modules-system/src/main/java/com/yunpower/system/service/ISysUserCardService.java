package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysUserCard;

/**
 * 用户充值卡Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ISysUserCardService {
    /**
     * 查询用户充值卡
     *
     * @param id 用户充值卡主键
     * @return 用户充值卡
     */
    public SysUserCard selectSysUserCardById(Long id);

    /**
     * 查询用户充值卡列表
     *
     * @param sysUserCard 用户充值卡
     * @return 用户充值卡集合
     */
    public List<SysUserCard> selectSysUserCardList(SysUserCard sysUserCard);

    /**
     * 新增用户充值卡
     *
     * @param sysUserCard 用户充值卡
     * @return 结果
     */
    public int insertSysUserCard(SysUserCard sysUserCard);

    /**
     * 修改用户充值卡
     *
     * @param sysUserCard 用户充值卡
     * @return 结果
     */
    public int updateSysUserCard(SysUserCard sysUserCard);

    /**
     * 修改用户充值卡状态
     *
     * @param id    用户充值卡主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysUserCardState(SysUserCard sysUserCard, Long id, Integer state);

    /**
     * 批量删除用户充值卡
     *
     * @param ids 需要删除的用户充值卡主键集合
     * @return 结果
     */
    public int deleteSysUserCardByIds(SysUserCard sysUserCard, Long[] ids);

    /**
     * 删除用户充值卡信息
     *
     * @param id 用户充值卡主键
     * @return 结果
     */
    public int deleteSysUserCardById(SysUserCard sysUserCard, Long id);
}
