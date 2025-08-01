package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysMessage;

/**
 * 消息Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ISysMessageService {
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
     * 修改消息状态
     *
     * @param id    消息主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysMessageState(SysMessage sysMessage, Long id, Integer state);

    /**
     * 批量删除消息
     *
     * @param ids 需要删除的消息主键集合
     * @return 结果
     */
    public int deleteSysMessageByIds(SysMessage sysMessage, Long[] ids);

    /**
     * 删除消息信息
     *
     * @param id 消息主键
     * @return 结果
     */
    public int deleteSysMessageById(SysMessage sysMessage, Long id);

    /**
     * 清空消息信息
     */
    public void cleanMessage();
}
