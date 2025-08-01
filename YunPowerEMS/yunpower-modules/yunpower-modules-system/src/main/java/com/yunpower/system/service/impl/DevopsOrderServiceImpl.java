package com.yunpower.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunpower.system.domain.DevopsOrder;
import com.yunpower.system.mapper.DevopsOrderMapper;
import com.yunpower.system.service.IDevopsOrderService;
import com.yunpower.system.service.IPublicService;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工单管理Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class DevopsOrderServiceImpl implements IDevopsOrderService {
    @Autowired
    private DevopsOrderMapper devopsOrderMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询工单管理
     *
     * @param id 工单管理主键
     * @return 工单管理
     */
    @Override
    public DevopsOrder selectDevopsOrderById(Long id) {
        return devopsOrderMapper.selectDevopsOrderById(id);
    }

    /**
     * 查询工单管理列表
     *
     * @param devopsOrder 工单管理
     * @return 工单管理
     */
    @Override
    public List<DevopsOrder> selectDevopsOrderList(DevopsOrder devopsOrder) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        if(devopsOrder.getDeptId() == null || devopsOrder.getDeptId() <= 0) {
            devopsOrder.setDeptId(publicService.getCurrentStation());
        }
        return devopsOrderMapper.selectDevopsOrderList(devopsOrder);
    }

    /**
     * 按工单类型统计
     */
    @Override
    public List<DevopsOrder> selectStatisticByOrderType(DevopsOrder devopsOrder) {
        return devopsOrderMapper.selectStatisticByOrderType(devopsOrder);
    }

    /**
     * 按工单状态统计
     */
    @Override
    public List<DevopsOrder> selectStatisticByStatus(DevopsOrder devopsOrder) {
        return devopsOrderMapper.selectStatisticByStatus(devopsOrder);
    }

    /**
     * 按日期统计
     */
    @Override
    public List<DevopsOrder> selectStatisticByDate(DevopsOrder devopsOrder) {
        return devopsOrderMapper.selectStatisticByDate(devopsOrder);
    }

    /**
     * 新增工单管理
     *
     * @param devopsOrder 工单管理
     * @return 结果
     */
    @Override
    public int insertDevopsOrder(DevopsOrder devopsOrder) {
        if (devopsOrder.getEntId() == null || devopsOrder.getEntId() <= 0) {
            devopsOrder.setEntId(publicService.getCurrentEnterprise());
        }
        if (devopsOrder.getDeptId() == null || devopsOrder.getDeptId() <= 0) {
            devopsOrder.setDeptId(publicService.getCurrentStation());
        }
        devopsOrder.setCreateBy(SecurityUtils.getNickName());
        devopsOrder.setCreateTime(DateUtils.getNowDate());
        if (devopsOrder.getStopFlag() == null) {
            devopsOrder.setStopFlag(0);
        }
        devopsOrder.setDeleteFlag(0);
        return devopsOrderMapper.insertDevopsOrder(devopsOrder);
    }

    /**
     * 修改工单管理
     *
     * @param devopsOrder 工单管理
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateDevopsOrder(DevopsOrder devopsOrder) {
        devopsOrder.setCreateBy(null);
        devopsOrder.setCreateTime(null);
        devopsOrder.setUpdateBy(SecurityUtils.getNickName());
        devopsOrder.setUpdateTime(DateUtils.getNowDate());
        return devopsOrderMapper.updateDevopsOrder(devopsOrder);
    }

    /**
     * 修改工单管理状态
     *
     * @param id    工单管理主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateDevopsOrderState(DevopsOrder devopsOrder, Long id, Integer state) {
        devopsOrder.setId(id);
        devopsOrder.setStopFlag(state);
        devopsOrder.setUpdateBy(SecurityUtils.getNickName());
        devopsOrder.setUpdateTime(DateUtils.getNowDate());
        return devopsOrderMapper.updateDevopsOrder(devopsOrder);
    }

    /**
     * 批量删除工单管理
     *
     * @param ids 需要删除的工单管理主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteDevopsOrderByIds(DevopsOrder devopsOrder, Long[] ids) {
        Map<String, Object> params = devopsOrder.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        devopsOrder.setParams(params);
        devopsOrder.setDeleteFlag(1);
        return devopsOrderMapper.updateDevopsOrder(devopsOrder);
    }

    /**
     * 删除工单管理信息
     *
     * @param id 工单管理主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteDevopsOrderById(DevopsOrder devopsOrder, Long id) {
        devopsOrder.setId(id);
        devopsOrder.setDeleteFlag(1);
        return devopsOrderMapper.updateDevopsOrder(devopsOrder);
    }
}
