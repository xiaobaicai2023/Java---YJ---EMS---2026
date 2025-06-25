package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysUserCard;

/**
 * 用户充值卡Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface SysUserCardMapper
{
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
     * 删除用户充值卡
     *
     * @param id 用户充值卡主键
     * @return 结果
     */
    public int deleteSysUserCardById(Long id);

    /**
     * 批量删除用户充值卡
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUserCardByIds(Long[] ids);
}
