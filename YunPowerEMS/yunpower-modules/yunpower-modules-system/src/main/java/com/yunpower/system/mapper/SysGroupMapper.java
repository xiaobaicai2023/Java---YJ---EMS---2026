package com.yunpower.system.mapper;

import com.yunpower.system.api.domain.SysGroup;

import java.util.List;

/**
 * 常用分组Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface SysGroupMapper
{
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
     * 删除常用分组
     *
     * @param id 常用分组主键
     * @return 结果
     */
    public int deleteSysGroupById(Long id);

    /**
     * 批量删除常用分组
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysGroupByIds(Long[] ids);
}
