package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.SysCommonEnterpriseMapper;
import com.yunpower.system.domain.SysCommonEnterprise;
import com.yunpower.system.service.ISysCommonEnterpriseService;

/**
 * 企业信息Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
@Service
public class SysCommonEnterpriseServiceImpl implements ISysCommonEnterpriseService {
    @Autowired
    private SysCommonEnterpriseMapper sysCommonEnterpriseMapper;

    /**
     * 查询企业信息
     *
     * @param id 企业信息主键
     * @return 企业信息
     */
    @Override
    public SysCommonEnterprise selectSysCommonEnterpriseById(Long id) {
        return sysCommonEnterpriseMapper.selectSysCommonEnterpriseById(id);
    }

    @Override
    public SysCommonEnterprise selectSysCommonEnterpriseByEntSn(String entSn) {
        return sysCommonEnterpriseMapper.selectSysCommonEnterpriseByEntSn(entSn);
    }

    /**
     * 通过企业统一社会信用代码查询
     * */
    @Override
    public SysCommonEnterprise selectSysCommonEnterpriseByCreditCode(String creditCode){
        return sysCommonEnterpriseMapper.selectSysCommonEnterpriseByCreditCode(creditCode);
    }

    /**
     * 查询企业信息列表
     *
     * @param sysCommonEnterprise 企业信息
     * @return 企业信息
     */
    @Override
    public List<SysCommonEnterprise> selectSysCommonEnterpriseList(SysCommonEnterprise sysCommonEnterprise) {
        return sysCommonEnterpriseMapper.selectSysCommonEnterpriseList(sysCommonEnterprise);
    }

    /**
     * 取出"企业-站点"关联列表
     *
     * @return 企业信息
     */
    @Override
    public List<SysCommonEnterprise> selectEnterpriseStationList() {
        return sysCommonEnterpriseMapper.selectEnterpriseStationList();
    }

    @Override
    public int selectNormalChildrenStationById(Long id) {
        return sysCommonEnterpriseMapper.selectNormalChildrenStationById(id);
    }

    @Override
    public boolean hasChildrenStationById(Long id) {
        return sysCommonEnterpriseMapper.hasChildrenStationById(id) > 0;
    }

    @Override
    public boolean checkStationExistUser(Long id) {
        return sysCommonEnterpriseMapper.checkStationExistUser(id) > 0;
    }

    /**
     * 新增企业信息
     *
     * @param sysCommonEnterprise 企业信息
     * @return 结果
     */
    @Override
    public int insertSysCommonEnterprise(SysCommonEnterprise sysCommonEnterprise) {
        sysCommonEnterprise.setCreateBy(SecurityUtils.getNickName());
        sysCommonEnterprise.setCreateTime(DateUtils.getNowDate());
        if (sysCommonEnterprise.getStopFlag() == null) {
            sysCommonEnterprise.setStopFlag(0);
        }
        sysCommonEnterprise.setDeleteFlag(0);
        return sysCommonEnterpriseMapper.insertSysCommonEnterprise(sysCommonEnterprise);
    }

    /**
     * 修改企业信息
     *
     * @param sysCommonEnterprise 企业信息
     * @return 结果
     */
    @Override
    public int updateSysCommonEnterprise(SysCommonEnterprise sysCommonEnterprise) {
        sysCommonEnterprise.setUpdateBy(SecurityUtils.getNickName());
        sysCommonEnterprise.setUpdateTime(DateUtils.getNowDate());
        return sysCommonEnterpriseMapper.updateSysCommonEnterprise(sysCommonEnterprise);
    }

    /**
     * 修改企业信息状态
     *
     * @param id    企业信息主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateSysCommonEnterpriseState(Long id, Integer state) {
        SysCommonEnterprise sysCommonEnterprise = new SysCommonEnterprise();
        sysCommonEnterprise.setId(id);
        sysCommonEnterprise.setStopFlag(state);
        sysCommonEnterprise.setUpdateBy(SecurityUtils.getNickName());
        sysCommonEnterprise.setUpdateTime(DateUtils.getNowDate());
        return sysCommonEnterpriseMapper.updateSysCommonEnterprise(sysCommonEnterprise);
    }

    /**
     * 批量删除企业信息
     *
     * @param ids 需要删除的企业信息主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonEnterpriseByIds(Long[] ids) {
        return sysCommonEnterpriseMapper.deleteSysCommonEnterpriseByIds(ids);
    }

    /**
     * 删除企业信息信息
     *
     * @param id 企业信息主键
     * @return 结果
     */
    @Override
    public int deleteSysCommonEnterpriseById(Long id) {
        SysCommonEnterprise sysCommonEnterprise = new SysCommonEnterprise();
        sysCommonEnterprise.setId(id);
        sysCommonEnterprise.setDeleteFlag(1);
        sysCommonEnterprise.setUpdateBy(SecurityUtils.getNickName());
        sysCommonEnterprise.setUpdateTime(DateUtils.getNowDate());
        return sysCommonEnterpriseMapper.updateSysCommonEnterprise(sysCommonEnterprise);
    }
}
