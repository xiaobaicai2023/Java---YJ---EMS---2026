package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysMessageTemplate;

/**
 * 消息模板Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ISysMessageTemplateService {
    /**
     * 查询消息模板
     *
     * @param id 消息模板主键
     * @return 消息模板
     */
    public SysMessageTemplate selectSysMessageTemplateById(Long id);

    /**
     * 查询消息模板列表
     *
     * @param sysMessageTemplate 消息模板
     * @return 消息模板集合
     */
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate sysMessageTemplate);

    /**
     * 新增消息模板
     *
     * @param sysMessageTemplate 消息模板
     * @return 结果
     */
    public int insertSysMessageTemplate(SysMessageTemplate sysMessageTemplate);

    /**
     * 修改消息模板
     *
     * @param sysMessageTemplate 消息模板
     * @return 结果
     */
    public int updateSysMessageTemplate(SysMessageTemplate sysMessageTemplate);

    /**
     * 修改消息模板状态
     *
     * @param id    消息模板主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysMessageTemplateState(SysMessageTemplate sysMessageTemplate, Long id, Integer state);

    /**
     * 批量删除消息模板
     *
     * @param ids 需要删除的消息模板主键集合
     * @return 结果
     */
    public int deleteSysMessageTemplateByIds(SysMessageTemplate sysMessageTemplate, Long[] ids);

    /**
     * 删除消息模板信息
     *
     * @param id 消息模板主键
     * @return 结果
     */
    public int deleteSysMessageTemplateById(SysMessageTemplate sysMessageTemplate, Long id);
}
