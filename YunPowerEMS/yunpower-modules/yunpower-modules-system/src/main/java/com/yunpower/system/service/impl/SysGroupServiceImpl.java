package com.yunpower.system.service.impl;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.SysCommonDictData;
import com.yunpower.system.api.domain.SysGroup;
import com.yunpower.system.domain.vo.TreeSelect;
import com.yunpower.system.mapper.SysGroupMapper;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysCommonDictService;
import com.yunpower.system.service.ISysGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 常用分组Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysGroupServiceImpl implements ISysGroupService {
    @Autowired
    private SysGroupMapper sysGroupMapper;

    @Autowired
    private ISysCommonDictService dictTypeService;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询常用分组
     *
     * @param id 常用分组主键
     * @return 常用分组
     */
    @Override
    public SysGroup selectSysGroupById(Long id) {
        return sysGroupMapper.selectSysGroupById(id);
    }

    @Override
    public SysGroup selectSysGroupByGroupSn(String groupSn) {
        return sysGroupMapper.selectSysGroupByGroupSn(groupSn);
    }

    /**
     * 查询常用分组列表
     *
     * @param sysGroup 常用分组
     * @return 常用分组
     */
    @Override
    public List<SysGroup> selectSysGroupList(SysGroup sysGroup) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）

        //这里有一种特殊情况，有些公共数据（如1-行业、5-图表配置）这个就不能限制deptid了
        if (sysGroup.getMapId() != 1 && sysGroup.getMapId() != 5 && sysGroup.getDeptId() == null) {
            sysGroup.setDeptId(publicService.getCurrentStation());
        }

        //排个序
        if (sysGroup.getMapId() != null && sysGroup.getMapId() == 5) {
            Map<String, Object> params = sysGroup.getParams();
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("orderBy", "order by order_num asc");

            sysGroup.setParams(params);
        }
        List<SysGroup> list = sysGroupMapper.selectSysGroupList(sysGroup);

        //按模块查询，那么将其子项也取出来
        List<SysGroup> newGroupList = new ArrayList<>();
        if (!StringUtils.longIsBlack0(sysGroup.getId())) {
            for (SysGroup group : list) {
                newGroupList.addAll(getGroupChildren(group.getId(), true));
            }
            return newGroupList;
        }
        return list;
    }

    /**
     * 获取【分组】子数据
     *
     * @param id           主键
     * @param isGetCurrent 是否包含当前ID的数据
     * @return 结果
     */
    @Override
    public List<SysGroup> getGroupChildren(Long id, boolean isGetCurrent) {
        List<SysGroup> returnList = new ArrayList<>();

        if (null == id || id <= 0L) {
            return returnList;
        }

        SysGroup group = selectSysGroupById(id);
        if (group == null) {
            return returnList;
        }

        if (isGetCurrent) {
            returnList.add(group);
        }

        SysGroup sysGroup = new SysGroup();
        sysGroup.setParentId(id);
        sysGroup.setMapId(group.getMapId());
        sysGroup.setStopFlag(0);
        List<SysGroup> list = selectSysGroupList(sysGroup);
        if (list.isEmpty()) {
            return returnList;
        }

        for (SysGroup item : list) {
            returnList.add(item);

            //递归获取子列表
            List<SysGroup> nextList = getGroupChildren(item.getId(), false);
            returnList.addAll(nextList);
        }

        return returnList;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param groups 分组列表
     * @return 树结构列表
     */
    @Override
    public List<SysGroup> buildGroupTree(List<SysGroup> groups) {
        List<SysGroup> returnList = new ArrayList<SysGroup>();
        List<Long> tempList = groups.stream().map(SysGroup::getId).collect(Collectors.toList());
        for (SysGroup group : groups) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(group.getParentId())) {
                recursionFn(groups, group);
                returnList.add(group);
            }
        }
        if (returnList.isEmpty()) {
            returnList = groups;
        }
        return returnList;
    }

    /**
     * 构建前端所需要【下拉】树结构
     *
     * @param groups 分组列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildGroupTreeSelect(List<SysGroup> groups) {
        List<SysGroup> groupTrees = buildGroupTree(groups);
        return groupTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysGroup> list, SysGroup t) {
        // 得到子节点列表
        List<SysGroup> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysGroup tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysGroup> getChildList(List<SysGroup> list, SysGroup t) {
        List<SysGroup> tlist = new ArrayList<SysGroup>();
        Iterator<SysGroup> it = list.iterator();
        while (it.hasNext()) {
            SysGroup n = (SysGroup) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }

        //排序
        return tlist.stream().sorted(Comparator.comparing(SysGroup::getOrderNum)).collect(Collectors.toList());
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysGroup> list, SysGroup t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 新增常用分组
     *
     * @param sysGroup 常用分组
     * @return 结果
     */
    @Override
    public int insertSysGroup(SysGroup sysGroup) {
        if (sysGroup.getEntId() == null || sysGroup.getEntId() <= 0) {
            sysGroup.setEntId(publicService.getCurrentEnterprise());
        }
        if (sysGroup.getDeptId() == null || sysGroup.getDeptId() <= 0) {
            sysGroup.setDeptId(publicService.getCurrentStation());
        }
        sysGroup.setCreateBy(SecurityUtils.getNickName());
        sysGroup.setCreateTime(DateUtils.getNowDate());
        if (sysGroup.getStopFlag() == null) {
            sysGroup.setStopFlag(0);
        }
        sysGroup.setDeleteFlag(0);
        return sysGroupMapper.insertSysGroup(sysGroup);
    }

    /**
     * 修改常用分组
     *
     * @param sysGroup 常用分组
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysGroup(SysGroup sysGroup) {
        sysGroup.setCreateBy(null);
        sysGroup.setCreateTime(null);
        sysGroup.setUpdateBy(SecurityUtils.getNickName());
        sysGroup.setUpdateTime(DateUtils.getNowDate());
        return sysGroupMapper.updateSysGroup(sysGroup);
    }

    /**
     * 修改常用分组状态
     *
     * @param id    常用分组主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysGroupState(SysGroup sysGroup, Long id, Integer state) {
        sysGroup.setId(id);
        sysGroup.setStopFlag(state);
        sysGroup.setUpdateBy(SecurityUtils.getNickName());
        sysGroup.setUpdateTime(DateUtils.getNowDate());
        return sysGroupMapper.updateSysGroup(sysGroup);
    }

    /**
     * 批量删除常用分组
     *
     * @param ids 需要删除的常用分组主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysGroupByIds(SysGroup sysGroup, Long[] ids) {

        Map<String, Object> params = sysGroup.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        sysGroup.setParams(params);
        sysGroup.setDeleteFlag(1);
        return sysGroupMapper.updateSysGroup(sysGroup);
    }

    /**
     * 删除常用分组信息
     *
     * @param id 常用分组主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysGroupById(SysGroup sysGroup, Long id) {
        sysGroup.setId(id);
        sysGroup.setDeleteFlag(1);
        return sysGroupMapper.updateSysGroup(sysGroup);
    }

    /**
     * 新增【光伏站点】时自动生成光伏设备目录
     * 固定的目录
     */
    @Override
    public boolean autoGenPvGroupSync(Long parentId, Long deptId) throws Exception {
        // 先从字典表中获取要生成的目录
        List<SysCommonDictData> pvGroupsList = dictTypeService.selectDictDataByType("sys_pv_groups");
        if (pvGroupsList == null || pvGroupsList.isEmpty()) {
            return true;
        }

        // 获取父目录信息
        SysGroup parent = selectSysGroupById(parentId);

        // 生成目录
        for (SysCommonDictData item : pvGroupsList) {
            SysGroup dto = new SysGroup();
            dto.setEntId(parent == null ? 0 : parent.getEntId()); //继承父目录
            dto.setDeptId(deptId); //部门生成以后会反写到站点表，取站点表反写后的ID
            dto.setMapId(3L);
            dto.setParentId(0L);
            dto.setGroupName(item.getDictLabel());
            dto.setOrderNum(Integer.parseInt(item.getDictValue()));
            dto.setIsShow(0);
            dto.setIsSystem(1);
            dto.setExt1(0);
            dto.setExt2(0);
            dto.setCreateTime(DateUtils.getNowDate());
            dto.setStopFlag(0);
            dto.setDeleteFlag(0);
            int row = insertSysGroup(dto);
            if (row == 0) {
                return false;
            }
        }
        return true;
    }
}
