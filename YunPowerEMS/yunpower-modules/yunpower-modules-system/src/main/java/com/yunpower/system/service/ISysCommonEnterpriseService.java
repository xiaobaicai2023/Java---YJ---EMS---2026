package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysCommonEnterprise;

/**
 * 企业信息Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
public interface ISysCommonEnterpriseService
{
    /**
     * 查询企业信息
     *
     * @param id 企业信息主键
     * @return 企业信息
     */
    public SysCommonEnterprise selectSysCommonEnterpriseById(Long id);

    /**
     * 通过企业编码查询
     * */
    public SysCommonEnterprise selectSysCommonEnterpriseByEntSn(String entSn);

    /**
     * 通过企业统一社会信用代码查询
     * */
    public SysCommonEnterprise selectSysCommonEnterpriseByCreditCode(String creditCode);

    /**
     * 查询企业信息列表
     *
     * @param sysCommonEnterprise 企业信息
     * @return 企业信息集合
     */
    public List<SysCommonEnterprise> selectSysCommonEnterpriseList(SysCommonEnterprise sysCommonEnterprise);

    /**
     * 取出"企业-站点"关联列表
     * @return 企业信息集合
     */
    public List<SysCommonEnterprise> selectEnterpriseStationList();

    /**
     * 根据ID查询所有子站点（正常状态）
     *
     * @param id 站点ID
     * @return 子站点数
     */
    public int selectNormalChildrenStationById(Long id);

    /**
     * 查询站点是否存在子节点
     *
     * @param id 站点ID
     * @return 结果
     */
    public boolean hasChildrenStationById(Long id);

    /**
     * 查询站点是否存在用户
     *
     * @param id 站点ID
     * @return 结果
     */
    public boolean checkStationExistUser(Long id);

    /**
     * 新增企业信息
     *
     * @param sysCommonEnterprise 企业信息
     * @return 结果
     */
    public int insertSysCommonEnterprise(SysCommonEnterprise sysCommonEnterprise);

    /**
     * 修改企业信息
     *
     * @param sysCommonEnterprise 企业信息
     * @return 结果
     */
    public int updateSysCommonEnterprise(SysCommonEnterprise sysCommonEnterprise);

    /**
     * 修改企业信息状态
     *
     * @param id 企业信息主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysCommonEnterpriseState(Long id, Integer state);

    /**
     * 批量删除企业信息
     *
     * @param ids 需要删除的企业信息主键集合
     * @return 结果
     */
    public int deleteSysCommonEnterpriseByIds(Long[] ids);

    /**
     * 删除企业信息信息
     *
     * @param id 企业信息主键
     * @return 结果
     */
    public int deleteSysCommonEnterpriseById(Long id);
}
