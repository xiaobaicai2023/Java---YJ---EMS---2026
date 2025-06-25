package com.yunpower.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.system.domain.SysCommonEnterprise;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.service.ISysCommonEnterpriseService;
import com.yunpower.system.service.ISysStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.common.core.constant.UserConstants;
import com.yunpower.common.core.exception.ServiceException;
import com.yunpower.common.core.text.Convert;
import com.yunpower.common.core.utils.SpringUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysRole;
import com.yunpower.system.api.domain.SysUser;
import com.yunpower.system.domain.vo.TreeSelect;
import com.yunpower.system.mapper.SysDeptMapper;
import com.yunpower.system.mapper.SysRoleMapper;
import com.yunpower.system.service.ISysDeptService;

/**
 * 部门管理 服务实现
 *
 * @author yunpower
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private ISysCommonEnterpriseService commonEnterpriseService;

    @Autowired
    private ISysStationService stationService;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 获取用户授权范围内的第一个部门ID
     * 注意：@DataScope 只有实体类继承了 BaseEntity 才会进行处理
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public Long getUserFirstAuthDeptId(SysDept dept) {
        return deptMapper.getUserFirstAuthDeptId(dept);
    }

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDept dept) {
        List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
        return buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = depts.stream().map(SysDept::getId).collect(Collectors.toList());
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构（只有ID和名称）
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        long deptId = StringUtils.isNull(dept.getId()) ? -1L : dept.getId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getId() != deptId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserSupper()) && StringUtils.isNotNull(deptId)) {
            SysDept dept = new SysDept();
            dept.setId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     * 开发人：junfu.wang at 2023-10-08
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());

        // 如果父节点为：不正常状态，不允许新增子节点。 update by junfu.wang at 2023-10-08
        if (!UserConstants.DEPT_NORMAL.equals(info.getStopFlag())) {
            throw new ServiceException("部门停用，不允许新增");
        }

        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        dept.setCreateBy(SecurityUtils.getNickName());
        dept.setCreateTime(DateUtils.getNowDate());
        if (dept.getStopFlag() == null) {
            dept.setStopFlag(0);
        }
        dept.setDeleteFlag(0);

        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getId(), newAncestors, oldAncestors);
        }

        dept.setUpdateBy(SecurityUtils.getNickName());
        dept.setUpdateTime(DateUtils.getNowDate());

        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStopFlag()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 生成部门表
     * 触发：
     * 1.新增/修改/删除 企业时
     * 2.新增/修改/删除 站点时
     */
    @Override
    public Boolean autoGenDeptSync() throws Exception {
        // 获取企业&站点组合列表
        List<SysCommonEnterprise> entStationList = commonEnterpriseService.selectEnterpriseStationList();

        // 如果无企业&站点，则清空部门表
        if (entStationList.size() == 0) {
            deptMapper.deleteDeptAll();
        }

        // 不能暴力清空表再插入，因为有的部门已经已经赋予权限了，重新插入会造成ID的改变
        // 存在则更新；不存在则插入；删除的相应删除
        // 部门主键ID必须从三位数100开始，所以初始化表时必须有一条数据
        for (SysCommonEnterprise enterprise : entStationList) {

            //region 1.处理企业数据
            SysDept dto = convertEnterprise(enterprise);
            dto.setParentId(0L);
            dto.setAncestors("0");

            int rows;
            SysDept deptEntOld = deptMapper.selectDeptByDeptSn(enterprise.getEntSn());
            if (deptEntOld == null) {
                rows = deptMapper.insertDept(dto);
            } else {
                dto.setId(deptEntOld.getId());
                rows = deptMapper.updateDept(dto);
            }

            if (rows == 0) {
                throw new ServiceException("自动生成部门数据失败！");
            }
            //endregion

            //region 2.处理站点数据
            // 获取站点列表
            SysStation sysStation = new SysStation();
            sysStation.setEntId(enterprise.getId());
            List<SysStation> stationList = stationService.selectStationListAll(sysStation);
            stationList = stationService.buildStationTree(stationList);

            for (SysStation station : stationList) {
                recursionSaveFn(station, dto.getId(), dto.getAncestors());
            }
            //endregion
        }

        return true;
    }

    // 递归保存部门信息
    private void recursionSaveFn(SysStation station, Long parent_id, String parent_logic) {
        SysDept dto = convertStation(station);
        dto.setParentId(parent_id);
        dto.setAncestors(parent_logic + "," + parent_id);

        int rows;
        SysDept dept_sta_old = deptMapper.selectDeptByDeptSn(station.getStationSn());
        if (dept_sta_old == null) {
            rows = deptMapper.insertDept(dto);
        } else {
            dto.setId(dept_sta_old.getId());
            rows = deptMapper.updateDept(dto);
        }

        if (rows == 0) {
            throw new ServiceException("自动生成部门数据失败！");
        }

        // 反向更新站点表的部门ID
        SysStation update = new SysStation();
        update.setId(station.getId());
        update.setDeptId(dto.getId());
        stationService.updateSysStation(update);

        // 更新当前父ID和逻辑代码
        parent_id = dto.getId();
        parent_logic = dto.getAncestors();

        if (station.getChildren().size() > 0) {
            for (SysStation child : station.getChildren()) {
                recursionSaveFn(child, parent_id, parent_logic);
            }
        }
    }

    // 企业实体类转换
    private SysDept convertEnterprise(SysCommonEnterprise enterprise) {
        SysDept sysDept = new SysDept();
        sysDept.setEntId(enterprise.getId());
        sysDept.setParentId(0L);
        sysDept.setOriginType(1);
        sysDept.setCorreId(enterprise.getId());
        sysDept.setIsCanSelect(0);
        sysDept.setDeptName(enterprise.getEntName());
        sysDept.setDeptSn(enterprise.getEntSn());
        sysDept.setLeader(enterprise.getLinkName());
        sysDept.setMobile(enterprise.getLinkMobile());
        sysDept.setEmail(enterprise.getEmail());
        sysDept.setOrderNum(Integer.parseInt(String.valueOf(enterprise.getId())));
        sysDept.setRemark(enterprise.getRemark());
        sysDept.setCreateBy(enterprise.getCreateBy());
        sysDept.setCreateTime(enterprise.getCreateTime());
        sysDept.setUpdateBy(enterprise.getUpdateBy());
        sysDept.setUpdateTime(enterprise.getUpdateTime());
        sysDept.setStopFlag(enterprise.getStopFlag());
        sysDept.setDeleteFlag(enterprise.getDeleteFlag());

        return sysDept;
    }

    // 站点实体类转换
    private SysDept convertStation(SysStation station) {
        SysDept sysDept = new SysDept();
        sysDept.setEntId(station.getEntId());
        sysDept.setParentId(0L);
        sysDept.setOriginType(2);
        sysDept.setCorreId(station.getId());
        sysDept.setIsCanSelect(station.getGroupOrStation() == 2 ? 1 : 0);
        sysDept.setDeptName(station.getStationName());
        sysDept.setDeptSn(station.getStationSn());
        sysDept.setLeader(station.getLinkName());
        sysDept.setMobile(station.getLinkPhone());
        sysDept.setOrderNum(station.getOrderNum());
        sysDept.setRemark(station.getRemark());
        sysDept.setCreateBy(station.getCreateBy());
        sysDept.setCreateTime(station.getCreateTime());
        sysDept.setUpdateBy(station.getUpdateBy());
        sysDept.setUpdateTime(station.getUpdateTime());
        sysDept.setStopFlag(station.getStopFlag());
        sysDept.setDeleteFlag(station.getDeleteFlag());

        return sysDept;
    }
}
