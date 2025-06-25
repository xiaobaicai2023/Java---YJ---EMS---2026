package com.yunpower.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunpower.system.domain.AlarmTriggerConfig;
import com.yunpower.system.mapper.AlarmTriggerConfigMapper;
import com.yunpower.system.service.IAlarmTriggerConfigService;
import com.yunpower.system.service.IPublicService;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 报警配置Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class AlarmTriggerConfigServiceImpl implements IAlarmTriggerConfigService {
    @Autowired
    private AlarmTriggerConfigMapper alarmTriggerConfigMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询报警配置
     *
     * @param id 报警配置主键
     * @return 报警配置
     */
    @Override
    public AlarmTriggerConfig selectAlarmTriggerConfigById(Long id) {
        return alarmTriggerConfigMapper.selectAlarmTriggerConfigById(id);
    }

    /**
     * 查询报警配置列表
     *
     * @param alarmTriggerConfig 报警配置
     * @return 报警配置
     */
    @Override
    public List<AlarmTriggerConfig> selectAlarmTriggerConfigList(AlarmTriggerConfig alarmTriggerConfig) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        alarmTriggerConfig.setDeptId(publicService.getCurrentStation());
        return alarmTriggerConfigMapper.selectAlarmTriggerConfigList(alarmTriggerConfig);
    }

    /**
     * 新增报警配置
     *
     * @param alarmTriggerConfig 报警配置
     * @return 结果
     */
    @Override
    public int insertAlarmTriggerConfig(AlarmTriggerConfig alarmTriggerConfig) {
        if (alarmTriggerConfig.getEntId() == null || alarmTriggerConfig.getEntId() <= 0) {
            alarmTriggerConfig.setEntId(publicService.getCurrentEnterprise());
        }
        if (alarmTriggerConfig.getDeptId() == null || alarmTriggerConfig.getDeptId() <= 0) {
            alarmTriggerConfig.setDeptId(publicService.getCurrentStation());
        }
        alarmTriggerConfig.setCreateBy(SecurityUtils.getNickName());
        alarmTriggerConfig.setCreateTime(DateUtils.getNowDate());
        if (alarmTriggerConfig.getStopFlag() == null) {
            alarmTriggerConfig.setStopFlag(0);
        }
        alarmTriggerConfig.setDeleteFlag(0);
        return alarmTriggerConfigMapper.insertAlarmTriggerConfig(alarmTriggerConfig);
    }

    /**
     * 修改报警配置
     *
     * @param alarmTriggerConfig 报警配置
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateAlarmTriggerConfig(AlarmTriggerConfig alarmTriggerConfig) {
        alarmTriggerConfig.setCreateBy(null);
        alarmTriggerConfig.setCreateTime(null);
        alarmTriggerConfig.setUpdateBy(SecurityUtils.getNickName());
        alarmTriggerConfig.setUpdateTime(DateUtils.getNowDate());
        return alarmTriggerConfigMapper.updateAlarmTriggerConfig(alarmTriggerConfig);
    }

    /**
     * 修改报警配置状态
     *
     * @param id    报警配置主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateAlarmTriggerConfigState(AlarmTriggerConfig alarmTriggerConfig, Long id, Integer state) {
        alarmTriggerConfig.setId(id);
        alarmTriggerConfig.setStopFlag(state);
        alarmTriggerConfig.setUpdateBy(SecurityUtils.getNickName());
        alarmTriggerConfig.setUpdateTime(DateUtils.getNowDate());
        return alarmTriggerConfigMapper.updateAlarmTriggerConfig(alarmTriggerConfig);
    }

    /**
     * 批量删除报警配置
     *
     * @param ids 需要删除的报警配置主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteAlarmTriggerConfigByIds(AlarmTriggerConfig alarmTriggerConfig, Long[] ids) {
        Map<String, Object> params = alarmTriggerConfig.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        alarmTriggerConfig.setParams(params);
        alarmTriggerConfig.setDeleteFlag(1);
        return alarmTriggerConfigMapper.updateAlarmTriggerConfig(alarmTriggerConfig);
    }

    /**
     * 删除报警配置信息
     *
     * @param id 报警配置主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteAlarmTriggerConfigById(AlarmTriggerConfig alarmTriggerConfig, Long id) {
        alarmTriggerConfig.setId(id);
        alarmTriggerConfig.setDeleteFlag(1);
        return alarmTriggerConfigMapper.updateAlarmTriggerConfig(alarmTriggerConfig);
    }
}
