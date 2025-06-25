package com.yunpower.system.service;

import com.yunpower.system.domain.WebtopoDeviceSvgnode;

import java.util.List;

/**
 * svg与设备绑定Service接口
 */
public interface IWebtopoDeviceSvgnodeService {
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
     * 批量删除svg与设备绑定
     *
     * @param projectIds 需要删除的svg与设备绑定主键集合
     * @return 结果
     */
    public int deleteWebtopoDeviceSvgnodeByProjectIds(Long[] projectIds);

    /**
     * 删除svg与设备绑定信息
     *
     * @param projectId svg与设备绑定主键
     * @return 结果
     */
    public int deleteWebtopoDeviceSvgnodeByProjectId(Long projectId);

    /**
     * 解除svg与设备绑定
     *
     * @param webtopoDeviceSvgnode svg与设备绑定
     * @return 结果
     */
    public int unbindWebtopoDeviceSvgnode(WebtopoDeviceSvgnode webtopoDeviceSvgnode);
}
