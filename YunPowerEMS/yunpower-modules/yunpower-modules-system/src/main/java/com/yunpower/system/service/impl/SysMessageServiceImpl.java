package com.yunpower.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.SysMessageMapper;
import com.yunpower.system.domain.SysMessage;
import com.yunpower.system.service.ISysMessageService;

/**
 * 消息Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysMessageServiceImpl implements ISysMessageService {
    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询消息
     *
     * @param id 消息主键
     * @return 消息
     */
    @Override
    public SysMessage selectSysMessageById(Long id) {
        return sysMessageMapper.selectSysMessageById(id);
    }

    /**
     * 查询消息列表
     *
     * @param sysMessage 消息
     * @return 消息
     */
    @Override
    public List<SysMessage> selectSysMessageList(SysMessage sysMessage) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        sysMessage.setDeptId(publicService.getCurrentStation());
        return sysMessageMapper.selectSysMessageList(sysMessage);
    }

    /**
     * 新增消息
     *
     * @param sysMessage 消息
     * @return 结果
     */
    @Override
    public int insertSysMessage(SysMessage sysMessage) {
        if (sysMessage.getEntId() == null || sysMessage.getEntId() <= 0) {
            sysMessage.setEntId(publicService.getCurrentEnterprise());
        }
        if (sysMessage.getDeptId() == null || sysMessage.getDeptId() <= 0) {
            sysMessage.setDeptId(publicService.getCurrentStation());
        }
        sysMessage.setCreateBy(SecurityUtils.getNickName());
        sysMessage.setCreateTime(DateUtils.getNowDate());
        if (sysMessage.getStopFlag() == null) {
            sysMessage.setStopFlag(0);
        }
        sysMessage.setDeleteFlag(0);
        return sysMessageMapper.insertSysMessage(sysMessage);
    }

    /**
     * 修改消息
     *
     * @param sysMessage 消息
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysMessage(SysMessage sysMessage) {
        sysMessage.setCreateBy(null);
        sysMessage.setCreateTime(null);
        sysMessage.setUpdateBy(SecurityUtils.getNickName());
        sysMessage.setUpdateTime(DateUtils.getNowDate());
        return sysMessageMapper.updateSysMessage(sysMessage);
    }

    /**
     * 修改消息状态
     *
     * @param id    消息主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysMessageState(SysMessage sysMessage, Long id, Integer state) {
        sysMessage.setId(id);
        sysMessage.setStopFlag(state);
        sysMessage.setUpdateBy(SecurityUtils.getNickName());
        sysMessage.setUpdateTime(DateUtils.getNowDate());
        return sysMessageMapper.updateSysMessage(sysMessage);
    }

    /**
     * 批量删除消息
     *
     * @param ids 需要删除的消息主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysMessageByIds(SysMessage sysMessage, Long[] ids) {
        Map<String, Object> params = sysMessage.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        sysMessage.setParams(params);
        sysMessage.setDeleteFlag(1);
        return sysMessageMapper.updateSysMessage(sysMessage);
    }

    /**
     * 删除消息信息
     *
     * @param id 消息主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysMessageById(SysMessage sysMessage, Long id) {
        sysMessage.setId(id);
        sysMessage.setDeleteFlag(1);
        return sysMessageMapper.updateSysMessage(sysMessage);
    }

    /**
     * 清空消息信息
     */
    @Override
    public void cleanMessage() {
        sysMessageMapper.cleanMessage();
    }
}
