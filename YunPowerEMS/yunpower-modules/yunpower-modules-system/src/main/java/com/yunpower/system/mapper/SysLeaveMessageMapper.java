package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysLeaveMessage;

/**
 * 留言回复管理Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface SysLeaveMessageMapper
{
    /**
     * 查询留言回复管理
     *
     * @param id 留言回复管理主键
     * @return 留言回复管理
     */
    public SysLeaveMessage selectSysLeaveMessageById(Long id);

    /**
     * 查询留言回复管理列表
     *
     * @param sysLeaveMessage 留言回复管理
     * @return 留言回复管理集合
     */
    public List<SysLeaveMessage> selectSysLeaveMessageList(SysLeaveMessage sysLeaveMessage);

    /**
     * 新增留言回复管理
     *
     * @param sysLeaveMessage 留言回复管理
     * @return 结果
     */
    public int insertSysLeaveMessage(SysLeaveMessage sysLeaveMessage);

    /**
     * 修改留言回复管理
     *
     * @param sysLeaveMessage 留言回复管理
     * @return 结果
     */
    public int updateSysLeaveMessage(SysLeaveMessage sysLeaveMessage);

    /**
     * 删除留言回复管理
     *
     * @param id 留言回复管理主键
     * @return 结果
     */
    public int deleteSysLeaveMessageById(Long id);

    /**
     * 批量删除留言回复管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysLeaveMessageByIds(Long[] ids);
}
