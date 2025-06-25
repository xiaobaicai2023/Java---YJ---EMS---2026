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
import com.yunpower.system.mapper.AlarmTriggerCategoryMapper;
import com.yunpower.system.domain.AlarmTriggerCategory;
import com.yunpower.system.service.IAlarmTriggerCategoryService;

/**
 * 报警类型Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class AlarmTriggerCategoryServiceImpl implements IAlarmTriggerCategoryService {
    @Autowired
    private AlarmTriggerCategoryMapper alarmTriggerCategoryMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询报警类型
     *
     * @param id 报警类型主键
     * @return 报警类型
     */
    @Override
    public AlarmTriggerCategory selectAlarmTriggerCategoryById(Long id) {
        return alarmTriggerCategoryMapper.selectAlarmTriggerCategoryById(id);
    }

    /**
     * 查询报警类型列表
     *
     * @param alarmTriggerCategory 报警类型
     * @return 报警类型
     */
    @Override
    public List<AlarmTriggerCategory> selectAlarmTriggerCategoryList(AlarmTriggerCategory alarmTriggerCategory) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        alarmTriggerCategory.setDeptId(publicService.getCurrentStation());
        return alarmTriggerCategoryMapper.selectAlarmTriggerCategoryList(alarmTriggerCategory);
    }

    /**
     * 新增报警类型
     *
     * @param alarmTriggerCategory 报警类型
     * @return 结果
     */
    @Override
    public int insertAlarmTriggerCategory(AlarmTriggerCategory alarmTriggerCategory) {
        if (alarmTriggerCategory.getEntId() == null || alarmTriggerCategory.getEntId() <= 0) {
            alarmTriggerCategory.setEntId(publicService.getCurrentEnterprise());
        }
        if (alarmTriggerCategory.getDeptId() == null || alarmTriggerCategory.getDeptId() <= 0) {
            alarmTriggerCategory.setDeptId(publicService.getCurrentStation());
        }
        alarmTriggerCategory.setCreateBy(SecurityUtils.getNickName());
        alarmTriggerCategory.setCreateTime(DateUtils.getNowDate());
        if (alarmTriggerCategory.getStopFlag() == null) {
            alarmTriggerCategory.setStopFlag(0);
        }
        alarmTriggerCategory.setDeleteFlag(0);
        return alarmTriggerCategoryMapper.insertAlarmTriggerCategory(alarmTriggerCategory);
    }

    /**
     * 修改报警类型
     *
     * @param alarmTriggerCategory 报警类型
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateAlarmTriggerCategory(AlarmTriggerCategory alarmTriggerCategory) {
        alarmTriggerCategory.setCreateBy(null);
        alarmTriggerCategory.setCreateTime(null);
        alarmTriggerCategory.setUpdateBy(SecurityUtils.getNickName());
        alarmTriggerCategory.setUpdateTime(DateUtils.getNowDate());
        return alarmTriggerCategoryMapper.updateAlarmTriggerCategory(alarmTriggerCategory);
    }

    /**
     * 修改报警类型状态
     *
     * @param id    报警类型主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateAlarmTriggerCategoryState(AlarmTriggerCategory alarmTriggerCategory, Long id, Integer state) {
        alarmTriggerCategory.setId(id);
        alarmTriggerCategory.setStopFlag(state);
        alarmTriggerCategory.setUpdateBy(SecurityUtils.getNickName());
        alarmTriggerCategory.setUpdateTime(DateUtils.getNowDate());
        return alarmTriggerCategoryMapper.updateAlarmTriggerCategory(alarmTriggerCategory);
    }

    /**
     * 批量删除报警类型
     *
     * @param ids 需要删除的报警类型主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteAlarmTriggerCategoryByIds(AlarmTriggerCategory alarmTriggerCategory, Long[] ids) {
        Map<String, Object> params = alarmTriggerCategory.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        alarmTriggerCategory.setParams(params);
        alarmTriggerCategory.setDeleteFlag(1);
        return alarmTriggerCategoryMapper.updateAlarmTriggerCategory(alarmTriggerCategory);
    }

    /**
     * 删除报警类型信息
     *
     * @param id 报警类型主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteAlarmTriggerCategoryById(AlarmTriggerCategory alarmTriggerCategory, Long id) {
        alarmTriggerCategory.setId(id);
        alarmTriggerCategory.setDeleteFlag(1);
        return alarmTriggerCategoryMapper.updateAlarmTriggerCategory(alarmTriggerCategory);
    }
}
