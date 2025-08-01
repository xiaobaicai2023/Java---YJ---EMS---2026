package com.yunpower.system.service.impl;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.domain.CommunicationDevice;
import com.yunpower.system.mapper.CommunicationDeviceMapper;
import com.yunpower.system.service.ICommunicationDeviceService;
import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通讯设备Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class CommunicationDeviceServiceImpl implements ICommunicationDeviceService {
    @Autowired
    private CommunicationDeviceMapper communicationDeviceMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询通讯设备
     *
     * @param id 通讯设备主键
     * @return 通讯设备
     */
    @Override
    public CommunicationDevice selectCommunicationDeviceById(Long id) {
        return communicationDeviceMapper.selectCommunicationDeviceById(id);
    }

    /**
     * 查询通讯设备
     *
     * @param deviceSn 通讯设备主键
     * @return 通讯设备
     */
    @Override
    public CommunicationDevice selectCommunicationDeviceBySn(String deviceSn) {
        return communicationDeviceMapper.selectCommunicationDeviceBySn(deviceSn);
    }

    /**
     * 查询通讯设备列表
     *
     * @param communicationDevice 通讯设备
     * @return 通讯设备
     */
    @Override
    public List<CommunicationDevice> selectCommunicationDeviceList(CommunicationDevice communicationDevice) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        communicationDevice.setDeptId(publicService.getCurrentStation());
        return communicationDeviceMapper.selectCommunicationDeviceList(communicationDevice);
    }

    /**
     * 查询该[通讯设备]下是否有[用电设备]
     *
     * @param id 通讯设备ID
     * @return 结果
     */
    @Override
    public boolean hasChildrenById(Long id) {
        return communicationDeviceMapper.hasChildrenById(id) > 0;
    }

    /**
     * 通过部门ID查询所有通讯机器状态
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    @Transactional(readOnly = true)
    public boolean statusAllByDeptId(Long deptId) {
        if (StringUtils.isNull(deptId) || deptId <= 0) {
            return false;
        }
        //停用的设备数量
        int result = communicationDeviceMapper.statusAllByDeptId(deptId);
        //大与0则说明有停用的设备 状态就是离线
        return !(result > 0);
    }
}
