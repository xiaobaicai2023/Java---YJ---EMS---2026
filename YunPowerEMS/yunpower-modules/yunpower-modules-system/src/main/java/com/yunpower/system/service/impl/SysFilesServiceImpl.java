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
import com.yunpower.system.mapper.SysFilesMapper;
import com.yunpower.system.domain.SysFiles;
import com.yunpower.system.service.ISysFilesService;

/**
 * 上传文件管理Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysFilesServiceImpl implements ISysFilesService {
    @Autowired
    private SysFilesMapper sysFilesMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询上传文件管理
     *
     * @param id 上传文件管理主键
     * @return 上传文件管理
     */
    @Override
    public SysFiles selectSysFilesById(Long id) {
        return sysFilesMapper.selectSysFilesById(id);
    }

    /**
     * 查询上传文件管理列表
     *
     * @param sysFiles 上传文件管理
     * @return 上传文件管理
     */
    @Override
    public List<SysFiles> selectSysFilesList(SysFiles sysFiles) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        sysFiles.setDeptId(publicService.getCurrentStation());
        return sysFilesMapper.selectSysFilesList(sysFiles);
    }

    /**
     * 新增上传文件管理
     *
     * @param sysFiles 上传文件管理
     * @return 结果
     */
    @Override
    public int insertSysFiles(SysFiles sysFiles) {
        if (sysFiles.getEntId() == null || sysFiles.getEntId() <= 0) {
            sysFiles.setEntId(publicService.getCurrentEnterprise());
        }
        if (sysFiles.getDeptId() == null || sysFiles.getDeptId() <= 0) {
            sysFiles.setDeptId(publicService.getCurrentStation());
        }
        sysFiles.setCreateBy(SecurityUtils.getNickName());
        sysFiles.setCreateTime(DateUtils.getNowDate());
        if (sysFiles.getStopFlag() == null) {
            sysFiles.setStopFlag(0);
        }
        sysFiles.setDeleteFlag(0);
        return sysFilesMapper.insertSysFiles(sysFiles);
    }

    /**
     * 修改上传文件管理
     *
     * @param sysFiles 上传文件管理
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysFiles(SysFiles sysFiles) {
        sysFiles.setCreateBy(null);
        sysFiles.setCreateTime(null);
        sysFiles.setUpdateBy(SecurityUtils.getNickName());
        sysFiles.setUpdateTime(DateUtils.getNowDate());
        return sysFilesMapper.updateSysFiles(sysFiles);
    }

    /**
     * 修改上传文件管理状态
     *
     * @param id    上传文件管理主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysFilesState(SysFiles sysFiles, Long id, Integer state) {
        sysFiles.setId(id);
        sysFiles.setStopFlag(state);
        sysFiles.setUpdateBy(SecurityUtils.getNickName());
        sysFiles.setUpdateTime(DateUtils.getNowDate());
        return sysFilesMapper.updateSysFiles(sysFiles);
    }

    /**
     * 批量删除上传文件管理
     *
     * @param ids 需要删除的上传文件管理主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysFilesByIds(SysFiles sysFiles, Long[] ids) {
        Map<String, Object> params = sysFiles.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        sysFiles.setParams(params);
        sysFiles.setDeleteFlag(1);
        return sysFilesMapper.updateSysFiles(sysFiles);
    }

    /**
     * 删除上传文件管理信息
     *
     * @param id 上传文件管理主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysFilesById(SysFiles sysFiles, Long id) {
        sysFiles.setId(id);
        sysFiles.setDeleteFlag(1);
        return sysFilesMapper.updateSysFiles(sysFiles);
    }
}
