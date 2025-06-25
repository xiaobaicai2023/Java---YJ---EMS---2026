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
import com.yunpower.system.mapper.AlarmTriggerMapper;
import com.yunpower.system.domain.AlarmTrigger;
import com.yunpower.system.service.IAlarmTriggerService;

/**
 * 报警管理Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class AlarmTriggerServiceImpl implements IAlarmTriggerService {
    @Autowired
    private AlarmTriggerMapper alarmTriggerMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询报警管理
     *
     * @param id 报警管理主键
     * @return 报警管理
     */
    @Override
    public AlarmTrigger selectAlarmTriggerById(Long id) {
        return alarmTriggerMapper.selectAlarmTriggerById(id);
    }

    /**
     * 查询报警管理列表
     *
     * @param alarmTrigger 报警管理
     * @return 报警管理
     */
    @Override
    public List<AlarmTrigger> selectAlarmTriggerList(AlarmTrigger alarmTrigger) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        if (alarmTrigger.getDeptId() == null || alarmTrigger.getDeptId() <= 0) {
            alarmTrigger.setDeptId(publicService.getCurrentStation());
        }
        return alarmTriggerMapper.selectAlarmTriggerList(alarmTrigger);
    }

    /**
     * 根据报警级别统计数据
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    @Override
    public List<AlarmTrigger> selectStatisticDataByAlarmLevelDate(AlarmTrigger alarmTrigger) {
        if (alarmTrigger.getDeptId() == null || alarmTrigger.getDeptId() <= 0) {
            alarmTrigger.setDeptId(publicService.getCurrentStation());
        }
        return alarmTriggerMapper.selectStatisticDataByAlarmLevelDate(alarmTrigger);
    }

    /**
     * 根据报警类型统计数据
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    @Override
    public List<AlarmTrigger> selectStatisticDataByCategory(AlarmTrigger alarmTrigger) {
        if (alarmTrigger.getDeptId() == null || alarmTrigger.getDeptId() <= 0) {
            alarmTrigger.setDeptId(publicService.getCurrentStation());
        }
        return alarmTriggerMapper.selectStatisticDataByCategory(alarmTrigger);
    }

    /**
     * 根据报警级别统计数据
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    @Override
    public List<AlarmTrigger> selectStatisticDataByAlarmLevel(AlarmTrigger alarmTrigger) {
        return alarmTriggerMapper.selectStatisticDataByAlarmLevel(alarmTrigger);
    }

    /**
     * 根据日期统计数据
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    @Override
    public List<AlarmTrigger> selectStatisticByDate(AlarmTrigger alarmTrigger) {
        return alarmTriggerMapper.selectStatisticByDate(alarmTrigger);
    }

    /**
     * 获取不重复的设备列表
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    @Override
    public List<AlarmTrigger> selectDistinctDevice(AlarmTrigger alarmTrigger) {
        if (alarmTrigger.getDeptId() == null || alarmTrigger.getDeptId() <= 0) {
            alarmTrigger.setDeptId(publicService.getCurrentStation());
        }
        return alarmTriggerMapper.selectDistinctDevice(alarmTrigger);
    }

    /**
     * 新增报警管理
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    @Override
    public int insertAlarmTrigger(AlarmTrigger alarmTrigger) {
        if (alarmTrigger.getEntId() == null || alarmTrigger.getEntId() <= 0) {
            alarmTrigger.setEntId(publicService.getCurrentEnterprise());
        }
        if (alarmTrigger.getDeptId() == null || alarmTrigger.getDeptId() <= 0) {
            alarmTrigger.setDeptId(publicService.getCurrentStation());
        }
        alarmTrigger.setCreateBy(SecurityUtils.getNickName());
        alarmTrigger.setCreateTime(DateUtils.getNowDate());
        if (alarmTrigger.getStopFlag() == null) {
            alarmTrigger.setStopFlag(0);
        }
        alarmTrigger.setDeleteFlag(0);
        return alarmTriggerMapper.insertAlarmTrigger(alarmTrigger);
    }

    /**
     * 修改报警管理
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateAlarmTrigger(AlarmTrigger alarmTrigger) {
        alarmTrigger.setCreateBy(null);
        alarmTrigger.setCreateTime(null);
        alarmTrigger.setUpdateBy(SecurityUtils.getNickName());
        alarmTrigger.setUpdateTime(DateUtils.getNowDate());
        return alarmTriggerMapper.updateAlarmTrigger(alarmTrigger);
    }

    /**
     * 批量删除报警管理
     *
     * @param ids 需要删除的报警管理主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteAlarmTriggerByIds(AlarmTrigger alarmTrigger, Long[] ids) {
        Map<String, Object> params = alarmTrigger.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        alarmTrigger.setParams(params);
        alarmTrigger.setDeleteFlag(1);
        return alarmTriggerMapper.updateAlarmTrigger(alarmTrigger);
    }

    /**
     * 删除报警管理信息
     *
     * @param id 报警管理主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteAlarmTriggerById(AlarmTrigger alarmTrigger, Long id) {
        alarmTrigger.setId(id);
        alarmTrigger.setDeleteFlag(1);
        return alarmTriggerMapper.updateAlarmTrigger(alarmTrigger);
    }
}
