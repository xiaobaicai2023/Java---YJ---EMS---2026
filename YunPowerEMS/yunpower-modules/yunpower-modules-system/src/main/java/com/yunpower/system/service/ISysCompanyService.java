package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysCompany;

/**
 * 公司Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ISysCompanyService {
    /**
     * 查询公司
     *
     * @param id 公司主键
     * @return 公司
     */
    public SysCompany selectSysCompanyById(Long id);

    /**
     * 查询公司列表
     *
     * @param sysCompany 公司
     * @return 公司集合
     */
    public List<SysCompany> selectSysCompanyList(SysCompany sysCompany);

    /**
     * 新增公司
     *
     * @param sysCompany 公司
     * @return 结果
     */
    public int insertSysCompany(SysCompany sysCompany);

    /**
     * 修改公司
     *
     * @param sysCompany 公司
     * @return 结果
     */
    public int updateSysCompany(SysCompany sysCompany);

    /**
     * 修改公司状态
     *
     * @param id    公司主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysCompanyState(SysCompany sysCompany, Long id, Integer state);

    /**
     * 批量删除公司
     *
     * @param ids 需要删除的公司主键集合
     * @return 结果
     */
    public int deleteSysCompanyByIds(SysCompany sysCompany, Long[] ids);

    /**
     * 删除公司信息
     *
     * @param id 公司主键
     * @return 结果
     */
    public int deleteSysCompanyById(SysCompany sysCompany, Long id);

    /**
     * 构建树结构
     * 递归
     *
     * @param companyList 公司列表
     * @return 结果
     */
    public List<SysCompany> buildCompanyTree(List<SysCompany> companyList);
}
