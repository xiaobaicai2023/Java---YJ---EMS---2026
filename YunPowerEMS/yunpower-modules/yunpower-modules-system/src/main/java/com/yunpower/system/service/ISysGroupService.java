package com.yunpower.system.service;

import com.yunpower.system.domain.vo.TreeSelect;
import com.yunpower.system.api.domain.SysGroup;

import java.util.List;

/**
 * 常用分组Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ISysGroupService {
    /**
     * 查询常用分组
     *
     * @param id 常用分组主键
     * @return 常用分组
     */
    public SysGroup selectSysGroupById(Long id);

    public SysGroup selectSysGroupByGroupSn(String groupSn);

    /**
     * 查询常用分组列表
     *
     * @param sysGroup 常用分组
     * @return 常用分组集合
     */
    public List<SysGroup> selectSysGroupList(SysGroup sysGroup);


    /**
     * 获取【分组】子数据
     *
     * @param id           主键
     * @param isGetCurrent 是否包含当前ID的数据
     * @return 结果
     */
    public List<SysGroup> getGroupChildren(Long id, boolean isGetCurrent);

    /**
     * 构建前端所需要树结构
     *
     * @param groups 分组列表
     * @return 树结构列表
     */
    public List<SysGroup> buildGroupTree(List<SysGroup> groups);

    /**
     * 构建前端所需要【下拉】树结构
     *
     * @param groups 分组列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildGroupTreeSelect(List<SysGroup> groups);

    /**
     * 新增常用分组
     *
     * @param sysGroup 常用分组
     * @return 结果
     */
    public int insertSysGroup(SysGroup sysGroup);

    /**
     * 修改常用分组
     *
     * @param sysGroup 常用分组
     * @return 结果
     */
    public int updateSysGroup(SysGroup sysGroup);

    /**
     * 修改常用分组状态
     *
     * @param id    常用分组主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysGroupState(SysGroup sysGroup, Long id, Integer state);

    /**
     * 批量删除常用分组
     *
     * @param ids 需要删除的常用分组主键集合
     * @return 结果
     */
    public int deleteSysGroupByIds(SysGroup sysGroup, Long[] ids);

    /**
     * 删除常用分组信息
     *
     * @param id 常用分组主键
     * @return 结果
     */
    public int deleteSysGroupById(SysGroup sysGroup, Long id);

    /**
     * 新增【光伏站点】时自动生成光伏设备目录
     * 固定的目录
     */
    public boolean autoGenPvGroupSync(Long parentId, Long deptId) throws Exception;
}
