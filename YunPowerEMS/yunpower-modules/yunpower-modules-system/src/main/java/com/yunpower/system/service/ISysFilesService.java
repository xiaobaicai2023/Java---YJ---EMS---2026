package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysFiles;

/**
 * 上传文件管理Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ISysFilesService {
    /**
     * 查询上传文件管理
     *
     * @param id 上传文件管理主键
     * @return 上传文件管理
     */
    public SysFiles selectSysFilesById(Long id);

    /**
     * 查询上传文件管理列表
     *
     * @param sysFiles 上传文件管理
     * @return 上传文件管理集合
     */
    public List<SysFiles> selectSysFilesList(SysFiles sysFiles);

    /**
     * 新增上传文件管理
     *
     * @param sysFiles 上传文件管理
     * @return 结果
     */
    public int insertSysFiles(SysFiles sysFiles);

    /**
     * 修改上传文件管理
     *
     * @param sysFiles 上传文件管理
     * @return 结果
     */
    public int updateSysFiles(SysFiles sysFiles);

    /**
     * 修改上传文件管理状态
     *
     * @param id    上传文件管理主键
     * @param state 状态
     * @return 结果
     */
    public int updateSysFilesState(SysFiles sysFiles, Long id, Integer state);

    /**
     * 批量删除上传文件管理
     *
     * @param ids 需要删除的上传文件管理主键集合
     * @return 结果
     */
    public int deleteSysFilesByIds(SysFiles sysFiles, Long[] ids);

    /**
     * 删除上传文件管理信息
     *
     * @param id 上传文件管理主键
     * @return 结果
     */
    public int deleteSysFilesById(SysFiles sysFiles, Long id);
}
