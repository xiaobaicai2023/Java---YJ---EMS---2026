package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysEnterpriseMenu;

/**
 * 企业专属菜单Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
public interface ISysEnterpriseMenuService
{
    /**
     * 查询企业专属菜单
     *
     * @param id 企业专属菜单主键
     * @return 企业专属菜单
     */
    public SysEnterpriseMenu selectSysEnterpriseMenuById(Long id);

    /**
     * 查询企业专属菜单列表
     *
     * @param sysEnterpriseMenu 企业专属菜单
     * @return 企业专属菜单集合
     */
    public List<SysEnterpriseMenu> selectSysEnterpriseMenuList(SysEnterpriseMenu sysEnterpriseMenu);

    /**
     * 新增企业专属菜单
     *
     * @param sysEnterpriseMenu 企业专属菜单
     * @return 结果
     */
    public int insertSysEnterpriseMenu(SysEnterpriseMenu sysEnterpriseMenu);

    /**
     * 新增企业专属菜单
     * @param menuIds 公共菜单ID集合
     * @return 结果
     */
    public int insertSysEnterpriseMenu(Long[] menuIds);

    /**
     * 修改企业专属菜单
     *
     * @param sysEnterpriseMenu 企业专属菜单
     * @return 结果
     */
    public int updateSysEnterpriseMenu(SysEnterpriseMenu sysEnterpriseMenu);

    /**
     * 批量删除企业专属菜单
     *
     * @param ids 需要删除的企业专属菜单主键集合
     * @return 结果
     */
    public int deleteSysEnterpriseMenuByIds(Long[] ids);

    /**
     * 删除企业专属菜单信息
     *
     * @param id 企业专属菜单主键
     * @return 结果
     */
    public int deleteSysEnterpriseMenuById(Long id);
}
