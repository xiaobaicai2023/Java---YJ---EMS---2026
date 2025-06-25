package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysMessage;

/**
 * 消息Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface SysMessageMapper
{
    /**
     * 查询消息
     *
     * @param id 消息主键
     * @return 消息
     */
    public SysMessage selectSysMessageById(Long id);

    /**
     * 查询消息列表
     *
     * @param sysMessage 消息
     * @return 消息集合
     */
    public List<SysMessage> selectSysMessageList(SysMessage sysMessage);

    /**
     * 新增消息
     *
     * @param sysMessage 消息
     * @return 结果
     */
    public int insertSysMessage(SysMessage sysMessage);

    /**
     * 修改消息
     *
     * @param sysMessage 消息
     * @return 结果
     */
    public int updateSysMessage(SysMessage sysMessage);

    /**
     * 删除消息
     *
     * @param id 消息主键
     * @return 结果
     */
    public int deleteSysMessageById(Long id);

    /**
     * 批量删除消息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysMessageByIds(Long[] ids);

    /**
     * 清空消息信息
     * @return 结果
     */
    public int cleanMessage();
}
