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
import com.yunpower.system.mapper.SysLeaveMessageMapper;
import com.yunpower.system.domain.SysLeaveMessage;
import com.yunpower.system.service.ISysLeaveMessageService;

/**
 * 留言回复管理Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysLeaveMessageServiceImpl implements ISysLeaveMessageService {
    @Autowired
    private SysLeaveMessageMapper sysLeaveMessageMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询留言回复管理
     *
     * @param id 留言回复管理主键
     * @return 留言回复管理
     */
    @Override
    public SysLeaveMessage selectSysLeaveMessageById(Long id) {
        return sysLeaveMessageMapper.selectSysLeaveMessageById(id);
    }

    /**
     * 查询留言回复管理列表
     *
     * @param sysLeaveMessage 留言回复管理
     * @return 留言回复管理
     */
    @Override
    public List<SysLeaveMessage> selectSysLeaveMessageList(SysLeaveMessage sysLeaveMessage) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        sysLeaveMessage.setDeptId(publicService.getCurrentStation());
        return sysLeaveMessageMapper.selectSysLeaveMessageList(sysLeaveMessage);
    }

    /**
     * 新增留言回复管理
     *
     * @param sysLeaveMessage 留言回复管理
     * @return 结果
     */
    @Override
    public int insertSysLeaveMessage(SysLeaveMessage sysLeaveMessage) {
        if (sysLeaveMessage.getEntId() == null || sysLeaveMessage.getEntId() <= 0) {
            sysLeaveMessage.setEntId(publicService.getCurrentEnterprise());
        }
        if (sysLeaveMessage.getDeptId() == null || sysLeaveMessage.getDeptId() <= 0) {
            sysLeaveMessage.setDeptId(publicService.getCurrentStation());
        }
        sysLeaveMessage.setCreateBy(SecurityUtils.getNickName());
        sysLeaveMessage.setCreateTime(DateUtils.getNowDate());
        if (sysLeaveMessage.getStopFlag() == null) {
            sysLeaveMessage.setStopFlag(0);
        }
        sysLeaveMessage.setDeleteFlag(0);
        return sysLeaveMessageMapper.insertSysLeaveMessage(sysLeaveMessage);
    }

    /**
     * 修改留言回复管理
     *
     * @param sysLeaveMessage 留言回复管理
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysLeaveMessage(SysLeaveMessage sysLeaveMessage) {
        sysLeaveMessage.setCreateBy(null);
        sysLeaveMessage.setCreateTime(null);
        sysLeaveMessage.setUpdateBy(SecurityUtils.getNickName());
        sysLeaveMessage.setUpdateTime(DateUtils.getNowDate());
        return sysLeaveMessageMapper.updateSysLeaveMessage(sysLeaveMessage);
    }

    /**
     * 修改留言回复管理状态
     *
     * @param id    留言回复管理主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysLeaveMessageState(SysLeaveMessage sysLeaveMessage, Long id, Integer state) {
        sysLeaveMessage.setId(id);
        sysLeaveMessage.setStopFlag(state);
        sysLeaveMessage.setUpdateBy(SecurityUtils.getNickName());
        sysLeaveMessage.setUpdateTime(DateUtils.getNowDate());
        return sysLeaveMessageMapper.updateSysLeaveMessage(sysLeaveMessage);
    }

    /**
     * 批量删除留言回复管理
     *
     * @param ids 需要删除的留言回复管理主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysLeaveMessageByIds(SysLeaveMessage sysLeaveMessage, Long[] ids) {
        Map<String, Object> params = sysLeaveMessage.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        sysLeaveMessage.setParams(params);
        sysLeaveMessage.setDeleteFlag(1);
        return sysLeaveMessageMapper.updateSysLeaveMessage(sysLeaveMessage);
    }

    /**
     * 删除留言回复管理信息
     *
     * @param id 留言回复管理主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysLeaveMessageById(SysLeaveMessage sysLeaveMessage, Long id) {
        sysLeaveMessage.setId(id);
        sysLeaveMessage.setDeleteFlag(1);
        return sysLeaveMessageMapper.updateSysLeaveMessage(sysLeaveMessage);
    }
}
