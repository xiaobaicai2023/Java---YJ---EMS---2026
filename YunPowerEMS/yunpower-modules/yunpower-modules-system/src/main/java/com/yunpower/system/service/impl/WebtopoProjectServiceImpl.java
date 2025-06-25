package com.yunpower.system.service.impl;

import com.yunpower.system.domain.WebtopoProject;
import com.yunpower.system.domain.WebtopoProjectDevice;
import com.yunpower.system.mapper.WebtopoProjectMapper;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.IWebtopoProjectService;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目列Service业务层处理
 */
@Service
public class WebtopoProjectServiceImpl implements IWebtopoProjectService {
    @Autowired
    private WebtopoProjectMapper webtopoProjectMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询项目列
     *
     * @param projectId 项目列主键
     * @return 项目列
     */
    @Override
    public WebtopoProject selectWebtopoProjectByProjectId(Long projectId) {
        return webtopoProjectMapper.selectWebtopoProjectByProjectId(projectId);
    }

    /**
     * 查询项目列列表
     *
     * @param webtopoProject 项目列
     * @return 项目列
     */
    @Override
    public List<WebtopoProject> selectWebtopoProjectList(WebtopoProject webtopoProject) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        webtopoProject.setDeptId(publicService.getCurrentStation());
        return webtopoProjectMapper.selectWebtopoProjectList(webtopoProject);
    }

    /**
     * 新增项目列
     *
     * @param webtopoProject 项目列
     * @return 结果
     */
    @Transactional
    @Override
    public int insertWebtopoProject(WebtopoProject webtopoProject) {
        if (webtopoProject.getEntId() == null || webtopoProject.getEntId() <= 0) {
            webtopoProject.setEntId(publicService.getCurrentEnterprise());
        }
        if (webtopoProject.getDeptId() == null || webtopoProject.getDeptId() <= 0) {
            webtopoProject.setDeptId(publicService.getCurrentStation());
        }
        webtopoProject.setCreateBy(SecurityUtils.getNickName());
        webtopoProject.setCreateTime(DateUtils.getNowDate());
        if (webtopoProject.getStopFlag() == null) {
            webtopoProject.setStopFlag(0);
        }
        webtopoProject.setDeleteFlag(0);
        int rows = webtopoProjectMapper.insertWebtopoProject(webtopoProject);
        insertWebtopoProjectDevice(webtopoProject);
        return rows;
    }

    /**
     * 修改项目列
     *
     * @param webtopoProject 项目列
     * @return 结果
     */
    @Transactional
    @Override
    public int updateWebtopoProject(WebtopoProject webtopoProject, boolean isDelete) {
        webtopoProject.setCreateBy(null);
        webtopoProject.setCreateTime(null);
        webtopoProject.setUpdateBy(SecurityUtils.getNickName());
        webtopoProject.setUpdateTime(DateUtils.getNowDate());
        if (isDelete) {
            webtopoProjectMapper.deleteWebtopoProjectDeviceByProjectId(webtopoProject.getId());
            insertWebtopoProjectDevice(webtopoProject);
        }
        return webtopoProjectMapper.updateWebtopoProject(webtopoProject);
    }

    /**
     * 批量删除项目列
     *
     * @param projectIds 需要删除的项目列主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteWebtopoProjectByProjectIds(Long[] projectIds) {
        webtopoProjectMapper.deleteWebtopoProjectDeviceByProjectIds(projectIds);
        return webtopoProjectMapper.deleteWebtopoProjectByProjectIds(projectIds);
    }

    /**
     * 删除项目列信息
     *
     * @param projectId 项目列主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteWebtopoProjectByProjectId(Long projectId) {
        webtopoProjectMapper.deleteWebtopoProjectDeviceByProjectId(projectId);
        return webtopoProjectMapper.deleteWebtopoProjectByProjectId(projectId);
    }

    /**
     * 新增项目关联设备信息
     *
     * @param webtopoProject 项目列对象
     */
    public void insertWebtopoProjectDevice(WebtopoProject webtopoProject) {
        List<WebtopoProjectDevice> projectDeviceList = webtopoProject.getDeviceList();
        Long projectId = webtopoProject.getId();
        if (StringUtils.isNotNull(projectDeviceList)) {
            List<WebtopoProjectDevice> list = new ArrayList<WebtopoProjectDevice>();
            for (WebtopoProjectDevice webtopoProjectDevice : projectDeviceList) {
                webtopoProjectDevice.setProjectId(projectId);
                list.add(webtopoProjectDevice);
            }
            if (list.size() > 0) {
                webtopoProjectMapper.batchWebtopoProjectDevice(list);
            }
        }
    }
}
