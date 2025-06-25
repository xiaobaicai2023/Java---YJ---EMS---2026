package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysFiles;

/**
 * 上传文件管理Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface SysFilesMapper
{
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
     * 删除上传文件管理
     *
     * @param id 上传文件管理主键
     * @return 结果
     */
    public int deleteSysFilesById(Long id);

    /**
     * 批量删除上传文件管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysFilesByIds(Long[] ids);
}
