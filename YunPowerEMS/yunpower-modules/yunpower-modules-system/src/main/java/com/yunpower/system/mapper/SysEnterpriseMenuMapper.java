package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysEnterpriseMenu;

/**
 * 企业专属菜单Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
public interface SysEnterpriseMenuMapper
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
     * 批量插入企业专属菜单
     * @param enterpriseMenuList 企业专属菜单
     * @return 结果
     */
    public int batchInsertEnterpriseMenu(List<SysEnterpriseMenu> enterpriseMenuList);

    /**
     * 修改企业专属菜单
     *
     * @param sysEnterpriseMenu 企业专属菜单
     * @return 结果
     */
    public int updateSysEnterpriseMenu(SysEnterpriseMenu sysEnterpriseMenu);

    /**
     * 删除企业专属菜单
     *
     * @param id 企业专属菜单主键
     * @return 结果
     */
    public int deleteSysEnterpriseMenuById(Long id);

    /**
     * 通过企业ID删除企业专属菜单
     * @param entId 企业ID
     * @return 结果
     */
    public int deleteSysEnterpriseMenuByEntId(Long entId);

    /**
     * 批量删除企业专属菜单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysEnterpriseMenuByIds(Long[] ids);
}
