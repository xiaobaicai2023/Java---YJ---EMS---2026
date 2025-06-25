package com.yunpower.system.mapper;

import com.yunpower.system.domain.WebtopoDeviceSvgnode;

import java.util.List;

/**
 * svg与设备绑定Mapper接口
 */
public interface WebtopoDeviceSvgnodeMapper {
    /**
     * 查询svg与设备绑定
     *
     * @param id svg与设备绑定主键
     * @return svg与设备绑定
     */
    public WebtopoDeviceSvgnode selectWebtopoDeviceSvgnodeByProjectId(Long id);

    /**
     * 查询svg与设备绑定列表
     *
     * @param webtopoDeviceSvgnode svg与设备绑定
     * @return svg与设备绑定集合
     */
    public List<WebtopoDeviceSvgnode> selectWebtopoDeviceSvgnodeList(WebtopoDeviceSvgnode webtopoDeviceSvgnode);

    /**
     * 新增svg与设备绑定
     *
     * @param webtopoDeviceSvgnode svg与设备绑定
     * @return 结果
     */
    public int insertWebtopoDeviceSvgnode(WebtopoDeviceSvgnode webtopoDeviceSvgnode);

    /**
     * 修改svg与设备绑定
     *
     * @param webtopoDeviceSvgnode svg与设备绑定
     * @return 结果
     */
    public int updateWebtopoDeviceSvgnode(WebtopoDeviceSvgnode webtopoDeviceSvgnode);

    /**
     * 删除svg与设备绑定
     *
     * @param projectId svg与设备绑定主键
     * @return 结果
     */
    public int deleteWebtopoDeviceSvgnodeByProjectId(Long projectId);

    /**
     * 批量删除svg与设备绑定
     *
     * @param projectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWebtopoDeviceSvgnodeByProjectIds(Long[] projectIds);

    /**
     * 解除svg与设备绑定
     *
     * @param webtopoDeviceSvgnode svg与设备绑定
     * @return 结果
     */
    public int unbindWebtopoDeviceSvgnode(WebtopoDeviceSvgnode webtopoDeviceSvgnode);
}
