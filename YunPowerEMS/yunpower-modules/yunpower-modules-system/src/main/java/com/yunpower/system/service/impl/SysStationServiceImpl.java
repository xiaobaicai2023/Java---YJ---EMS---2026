package com.yunpower.system.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.yunpower.common.core.constant.UserConstants;
import com.yunpower.common.core.exception.ServiceException;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.SysStationMapper;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.service.ISysStationService;

/**
 * 电站Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 * @description 站点、用户、角色数据查询受 @DataScope 限制
 */
@Service
public class SysStationServiceImpl implements ISysStationService {
    @Autowired
    private SysStationMapper sysStationMapper;

    /**
     * 查询电站
     *
     * @param id 电站主键
     * @return 电站
     */
    @Override
    public SysStation selectSysStationById(Long id) {
        return sysStationMapper.selectSysStationById(id);
    }

    @Override
    public SysStation selectSysStationBySn(String stationSn) {
        return sysStationMapper.selectSysStationBySn(stationSn);
    }

    @Override
    public SysStation selectSysStationByDeptId(Long deptId) {
        return sysStationMapper.selectSysStationByDeptId(deptId);
    }

    /**
     * 查询电站列表
     *
     * @param sysStation 电站
     * @return 电站
     */
    @Override
    @DataScope(deptAlias = "s")
    public List<SysStation> selectSysStationList(SysStation sysStation) {
        return sysStationMapper.selectSysStationList(sysStation);
    }

    /**
     * 获取站点列表，不限制数据权限
     *
     * @param sysStation 电站
     * @return 电站集合
     */
    @Override
    public List<SysStation> selectStationListAll(SysStation sysStation) {
        return sysStationMapper.selectStationListAll(sysStation);
    }

    @Override
    public List<SysStation> selectChildrenStationById(Long id) {
        return sysStationMapper.selectChildrenStationById(id);
    }

    @Override
    public int selectNormalChildrenStationById(Long id) {
        return sysStationMapper.selectNormalChildrenStationById(id);
    }

    @Override
    public boolean hasChildrenById(Long id) {
        return sysStationMapper.hasChildrenById(id) > 0;
    }

    @Override
    public boolean checkStationExistUser(Long id) {
        return sysStationMapper.checkStationExistUser(id) > 0;
    }

    @Override
    public int updateStationChildren(List<SysStation> stations) {
        return sysStationMapper.updateStationChildren(stations);
    }

    /**
     * 新增电站
     *
     * @param sysStation 电站
     * @return 结果
     */
    @Override
    public int insertSysStation(SysStation sysStation) {
        if (sysStation.getParentId() == null || sysStation.getParentId() == 0) {
            sysStation.setParentId(0L);
            sysStation.setLogicCode("0");
        }

        // 检查上级状态是否正常
        if (sysStation.getParentId() > 0) {
            SysStation station = sysStationMapper.selectSysStationById(sysStation.getParentId());
            if (!UserConstants.DEPT_NORMAL.equals(station.getStopFlag())) {
                throw new ServiceException("上级站点停用，不允许新增");
            }
            sysStation.setLogicCode(station.getLogicCode() + "," + station.getId());
        }

        sysStation.setCreateBy(SecurityUtils.getNickName());
        sysStation.setCreateTime(DateUtils.getNowDate());
        if (sysStation.getStopFlag() == null) {
            sysStation.setStopFlag(0);
        }
        sysStation.setDeleteFlag(0);
        return sysStationMapper.insertSysStation(sysStation);
    }

    /**
     * 修改电站
     *
     * @param sysStation 电站
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysStation(SysStation sysStation) {
        SysStation oldStation = sysStationMapper.selectSysStationById(sysStation.getId());
        SysStation newParentStation = sysStationMapper.selectSysStationById(sysStation.getParentId());
        if (StringUtils.isNotNull(oldStation) && StringUtils.isNotNull(newParentStation)) {
            String newAncestors = newParentStation.getLogicCode() + "," + newParentStation.getId();
            String oldAncestors = oldStation.getLogicCode();
            sysStation.setLogicCode(newAncestors);
            updateDeptChildren(sysStation.getId(), newAncestors, oldAncestors);
        }

        sysStation.setCreateBy(null);
        sysStation.setCreateTime(null);
        sysStation.setUpdateBy(SecurityUtils.getNickName());
        sysStation.setUpdateTime(DateUtils.getNowDate());
        return sysStationMapper.updateSysStation(sysStation);
    }

    /**
     * 修改电站状态
     *
     * @param id    电站主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysStationState(SysStation sysStation, Long id, Integer state) {
        sysStation.setId(id);
        sysStation.setStopFlag(state);
        sysStation.setUpdateBy(SecurityUtils.getNickName());
        sysStation.setUpdateTime(DateUtils.getNowDate());
        return sysStationMapper.updateSysStation(sysStation);
    }

    /**
     * 修改电站报警状态
     *
     * @param id    电站主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateSysStationAlarmState(SysStation sysStation, Long id, Integer state) {
        //通过部门ID查找对应的站点ID
        SysStation stationInfo  = this.selectSysStationByDeptId(id);
        if(stationInfo == null){
            throw new ServiceException("站点信息错误");
        }
        sysStation.setId(stationInfo.getId());
        sysStation.setOpenAlarm(state);
        sysStation.setUpdateBy(SecurityUtils.getNickName());
        sysStation.setUpdateTime(DateUtils.getNowDate());
        return sysStationMapper.updateSysStation(sysStation);
    }

    /**
     * 修改子元素关系
     *
     * @param stationId    被修改的站点ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long stationId, String newAncestors, String oldAncestors) {
        List<SysStation> children = sysStationMapper.selectChildrenStationById(stationId);
        for (SysStation child : children) {
            child.setLogicCode(child.getLogicCode().replaceFirst(oldAncestors, newAncestors));
        }
        if (!children.isEmpty()) {
            sysStationMapper.updateStationChildren(children);
        }
    }

    /**
     * 批量删除电站
     *
     * @param ids 需要删除的电站主键
     * @return 结果
     */
    @Override
    public int deleteSysStationByIds(SysStation sysStation, Long[] ids) {
        Map<String, Object> params = sysStation.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        sysStation.setParams(params);
        sysStation.setDeleteFlag(1);
        return sysStationMapper.updateSysStation(sysStation);
    }

    /**
     * 删除电站信息
     *
     * @param id 电站主键
     * @return 结果
     */
    @Override
    public int deleteSysStationById(SysStation sysStation, Long id) {
        sysStation.setId(id);
        sysStation.setDeleteFlag(1);
        return sysStationMapper.updateSysStation(sysStation);
    }

    /**
     * 构建树结构
     * 递归
     *
     * @return 结果
     */
    public List<SysStation> buildStationTree() {
        List<SysStation> stationList = sysStationMapper.selectStationListAll(new SysStation());
        return buildStationTree(stationList);
    }

    /**
     * 构建树结构
     * 递归
     *
     * @param stationList 站点列表
     * @return 结果
     */
    public List<SysStation> buildStationTree(List<SysStation> stationList) {
        List<SysStation> returnList = new ArrayList<SysStation>();
        List<Long> tempList = stationList.stream().map(SysStation::getId).collect(Collectors.toList());
        for (SysStation sta : stationList) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(sta.getParentId())) {
                recursionFn(stationList, sta);
                returnList.add(sta);
            }
        }
        if (returnList.isEmpty()) {
            returnList = stationList;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysStation> list, SysStation t) {
        // 得到子节点列表
        List<SysStation> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysStation tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysStation> getChildList(List<SysStation> list, SysStation t) {
        List<SysStation> tlist = new ArrayList<SysStation>();
        Iterator<SysStation> it = list.iterator();
        while (it.hasNext()) {
            SysStation n = (SysStation) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysStation> list, SysStation t) {
        return getChildList(list, t).size() > 0;
    }

}
