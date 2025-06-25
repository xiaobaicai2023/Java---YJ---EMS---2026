package com.yunpower.system.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.yunpower.system.domain.SysCompany;
import com.yunpower.system.mapper.SysCompanyMapper;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysCompanyService;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公司Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysCompanyServiceImpl implements ISysCompanyService {
    @Autowired
    private SysCompanyMapper sysCompanyMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询公司
     *
     * @param id 公司主键
     * @return 公司
     */
    @Override
    public SysCompany selectSysCompanyById(Long id) {
        return sysCompanyMapper.selectSysCompanyById(id);
    }

    /**
     * 查询公司列表
     *
     * @param sysCompany 公司
     * @return 公司
     */
    @Override
    public List<SysCompany> selectSysCompanyList(SysCompany sysCompany) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        sysCompany.setDeptId(publicService.getCurrentStation());
        List<SysCompany> companyList = sysCompanyMapper.selectSysCompanyList(sysCompany);
        for (SysCompany item : companyList) {
            item.setChildren(getChildrenFn(item));
        }
        return companyList;
    }

    /**
     * 循环取出子公司（部门）
     */
    private List<SysCompany> getChildrenFn(SysCompany dto) {
        SysCompany sysCompany = new SysCompany();
        sysCompany.setParentId(dto.getId());
        List<SysCompany> childrenList = sysCompanyMapper.selectSysCompanyList(sysCompany);
        for (SysCompany item : childrenList) {
            if (item.getParentId() == 0) {
                continue;
            }
            item.setChildren(getChildrenFn(item));
        }
        return childrenList;
    }

    /**
     * 新增公司
     *
     * @param sysCompany 公司
     * @return 结果
     */
    @Override
    public int insertSysCompany(SysCompany sysCompany) {
        if (sysCompany.getEntId() == null || sysCompany.getEntId() <= 0) {
            sysCompany.setEntId(publicService.getCurrentEnterprise());
        }
        if (sysCompany.getDeptId() == null || sysCompany.getDeptId() <= 0) {
            sysCompany.setDeptId(publicService.getCurrentStation());
        }
        sysCompany.setCreateBy(SecurityUtils.getNickName());
        sysCompany.setCreateTime(DateUtils.getNowDate());
        if (sysCompany.getStopFlag() == null) {
            sysCompany.setStopFlag(0);
        }
        sysCompany.setDeleteFlag(0);
        return sysCompanyMapper.insertSysCompany(sysCompany);
    }

    /**
     * 修改公司
     *
     * @param sysCompany 公司
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysCompany(SysCompany sysCompany) {
        sysCompany.setCreateBy(null);
        sysCompany.setCreateTime(null);
        sysCompany.setUpdateBy(SecurityUtils.getNickName());
        sysCompany.setUpdateTime(DateUtils.getNowDate());
        return sysCompanyMapper.updateSysCompany(sysCompany);
    }

    /**
     * 修改公司状态
     *
     * @param id    公司主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysCompanyState(SysCompany sysCompany, Long id, Integer state) {
        sysCompany.setId(id);
        sysCompany.setStopFlag(state);
        sysCompany.setUpdateBy(SecurityUtils.getNickName());
        sysCompany.setUpdateTime(DateUtils.getNowDate());
        return sysCompanyMapper.updateSysCompany(sysCompany);
    }

    /**
     * 批量删除公司
     *
     * @param ids 需要删除的公司主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysCompanyByIds(SysCompany sysCompany, Long[] ids) {
        Map<String, Object> params = sysCompany.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        sysCompany.setParams(params);
        sysCompany.setDeleteFlag(1);
        return sysCompanyMapper.updateSysCompany(sysCompany);
    }

    /**
     * 删除公司信息
     *
     * @param id 公司主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysCompanyById(SysCompany sysCompany, Long id) {
        sysCompany.setId(id);
        sysCompany.setDeleteFlag(1);
        return sysCompanyMapper.updateSysCompany(sysCompany);
    }

    /**
     * 构建树结构
     * 递归
     *
     * @param companyList 公司列表
     * @return 结果
     */
    @Override
    public List<SysCompany> buildCompanyTree(List<SysCompany> companyList) {
        List<SysCompany> returnList = new ArrayList<SysCompany>();
        List<Long> tempList = companyList.stream().map(SysCompany::getId).collect(Collectors.toList());
        for (SysCompany sta : companyList) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(sta.getParentId())) {
                recursionFn(companyList, sta);
                returnList.add(sta);
            }
        }
        if (returnList.isEmpty()) {
            returnList = companyList;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysCompany> list, SysCompany t) {
        // 得到子节点列表
        List<SysCompany> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysCompany tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysCompany> getChildList(List<SysCompany> list, SysCompany t) {
        List<SysCompany> tlist = new ArrayList<SysCompany>();
        Iterator<SysCompany> it = list.iterator();
        while (it.hasNext()) {
            SysCompany n = (SysCompany) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysCompany> list, SysCompany t) {
        return getChildList(list, t).size() > 0;
    }
}
