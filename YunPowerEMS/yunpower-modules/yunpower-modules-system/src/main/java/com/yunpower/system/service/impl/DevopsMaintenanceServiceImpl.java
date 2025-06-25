package com.yunpower.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.DevopsMaintenanceMapper;
import com.yunpower.system.domain.DevopsMaintenance;
import com.yunpower.system.service.IDevopsMaintenanceService;

/**
 * 维保记录Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class DevopsMaintenanceServiceImpl implements IDevopsMaintenanceService {
    @Autowired
    private DevopsMaintenanceMapper devopsMaintenanceMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询维保记录
     *
     * @param id 维保记录主键
     * @return 维保记录
     */
    @Override
    public DevopsMaintenance selectDevopsMaintenanceById(Long id) {
        return devopsMaintenanceMapper.selectDevopsMaintenanceById(id);
    }

    /**
     * 查询维保记录列表
     *
     * @param devopsMaintenance 维保记录
     * @return 维保记录
     */
    @Override
    public List<DevopsMaintenance> selectDevopsMaintenanceList(DevopsMaintenance devopsMaintenance) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        devopsMaintenance.setDeptId(publicService.getCurrentStation());
        return devopsMaintenanceMapper.selectDevopsMaintenanceList(devopsMaintenance);
    }

    /**
     * 新增维保记录
     *
     * @param devopsMaintenance 维保记录
     * @return 结果
     */
    @Override
    public int insertDevopsMaintenance(DevopsMaintenance devopsMaintenance) {
        if (devopsMaintenance.getEntId() == null || devopsMaintenance.getEntId() <= 0) {
            devopsMaintenance.setEntId(publicService.getCurrentEnterprise());
        }
        if (devopsMaintenance.getDeptId() == null || devopsMaintenance.getDeptId() <= 0) {
            devopsMaintenance.setDeptId(publicService.getCurrentStation());
        }
        devopsMaintenance.setCreateBy(SecurityUtils.getNickName());
        devopsMaintenance.setCreateTime(DateUtils.getNowDate());
        if (devopsMaintenance.getStopFlag() == null) {
            devopsMaintenance.setStopFlag(0);
        }
        devopsMaintenance.setDeleteFlag(0);
        return devopsMaintenanceMapper.insertDevopsMaintenance(devopsMaintenance);
    }

    /**
     * 修改维保记录
     *
     * @param devopsMaintenance 维保记录
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateDevopsMaintenance(DevopsMaintenance devopsMaintenance) {
        devopsMaintenance.setCreateBy(null);
        devopsMaintenance.setCreateTime(null);
        devopsMaintenance.setUpdateBy(SecurityUtils.getNickName());
        devopsMaintenance.setUpdateTime(DateUtils.getNowDate());
        return devopsMaintenanceMapper.updateDevopsMaintenance(devopsMaintenance);
    }

    /**
     * 修改维保记录状态
     *
     * @param id    维保记录主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateDevopsMaintenanceState(DevopsMaintenance devopsMaintenance, Long id, Integer state) {
        devopsMaintenance.setId(id);
        devopsMaintenance.setStopFlag(state);
        devopsMaintenance.setUpdateBy(SecurityUtils.getNickName());
        devopsMaintenance.setUpdateTime(DateUtils.getNowDate());
        return devopsMaintenanceMapper.updateDevopsMaintenance(devopsMaintenance);
    }

    /**
     * 批量删除维保记录
     *
     * @param ids 需要删除的维保记录主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteDevopsMaintenanceByIds(DevopsMaintenance devopsMaintenance, Long[] ids) {
        Map<String, Object> params = devopsMaintenance.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        devopsMaintenance.setParams(params);
        devopsMaintenance.setDeleteFlag(1);
        return devopsMaintenanceMapper.updateDevopsMaintenance(devopsMaintenance);
    }

    /**
     * 删除维保记录信息
     *
     * @param id 维保记录主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteDevopsMaintenanceById(DevopsMaintenance devopsMaintenance, Long id) {
        devopsMaintenance.setId(id);
        devopsMaintenance.setDeleteFlag(1);
        return devopsMaintenanceMapper.updateDevopsMaintenance(devopsMaintenance);
    }
}
