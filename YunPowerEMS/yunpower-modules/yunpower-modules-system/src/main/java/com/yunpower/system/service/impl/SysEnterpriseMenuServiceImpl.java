package com.yunpower.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.yunpower.system.domain.SysEnterpriseMenu;
import com.yunpower.system.mapper.SysEnterpriseMenuMapper;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysEnterpriseMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业专属菜单Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
@Service
public class SysEnterpriseMenuServiceImpl implements ISysEnterpriseMenuService {
    @Autowired
    private SysEnterpriseMenuMapper sysEnterpriseMenuMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询企业专属菜单
     *
     * @param id 企业专属菜单主键
     * @return 企业专属菜单
     */
    @Override
    public SysEnterpriseMenu selectSysEnterpriseMenuById(Long id) {
        return sysEnterpriseMenuMapper.selectSysEnterpriseMenuById(id);
    }

    /**
     * 查询企业专属菜单列表
     *
     * @param sysEnterpriseMenu 企业专属菜单
     * @return 企业专属菜单
     */
    @Override
    public List<SysEnterpriseMenu> selectSysEnterpriseMenuList(SysEnterpriseMenu sysEnterpriseMenu) {
        return sysEnterpriseMenuMapper.selectSysEnterpriseMenuList(sysEnterpriseMenu);
    }

    /**
     * 新增企业专属菜单
     *
     * @param sysEnterpriseMenu 企业专属菜单
     * @return 结果
     */
    @Override
    public int insertSysEnterpriseMenu(SysEnterpriseMenu sysEnterpriseMenu) {
        if (sysEnterpriseMenu.getEntId() == null || sysEnterpriseMenu.getEntId() <= 0) {
            sysEnterpriseMenu.setEntId(publicService.getCurrentEnterprise());
        }
        return sysEnterpriseMenuMapper.insertSysEnterpriseMenu(sysEnterpriseMenu);
    }

    /**
     * 新增企业专属菜单
     *
     * @param menuIds 公共菜单ID集合
     * @return 结果
     */
    @Override
    public int insertSysEnterpriseMenu(Long[] menuIds) {
        int rows = 0;
        if (menuIds.length == 0) {
            return rows;
        }

        Long entId = publicService.getCurrentEnterprise();

        // 先清空
        sysEnterpriseMenuMapper.deleteSysEnterpriseMenuByEntId(entId);

        List<SysEnterpriseMenu> list = new ArrayList<>();
        for (Long menuid : menuIds) {
            SysEnterpriseMenu em = new SysEnterpriseMenu();
            em.setEntId(entId);
            em.setMenuId(menuid);
            list.add(em);
        }
        return sysEnterpriseMenuMapper.batchInsertEnterpriseMenu(list);
    }

    /**
     * 修改企业专属菜单
     *
     * @param sysEnterpriseMenu 企业专属菜单
     * @return 结果
     */
    @Override
    public int updateSysEnterpriseMenu(SysEnterpriseMenu sysEnterpriseMenu) {
        return sysEnterpriseMenuMapper.updateSysEnterpriseMenu(sysEnterpriseMenu);
    }

    /**
     * 批量删除企业专属菜单
     *
     * @param ids 需要删除的企业专属菜单主键
     * @return 结果
     */
    @Override
    public int deleteSysEnterpriseMenuByIds(Long[] ids) {
        return sysEnterpriseMenuMapper.deleteSysEnterpriseMenuByIds(ids);
    }

    /**
     * 删除企业专属菜单信息
     *
     * @param id 企业专属菜单主键
     * @return 结果
     */
    @Override
    public int deleteSysEnterpriseMenuById(Long id) {
        return sysEnterpriseMenuMapper.deleteSysEnterpriseMenuById(id);
    }
}
