package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.SysNotice;

/**
 * 公告 服务层
 *
 * @author yunpower
 */
public interface ISysNoticeService {
    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    public SysNotice selectNoticeById(Long noticeId);

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    public int insertNotice(SysNotice notice);

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    public int updateNotice(SysNotice notice);

    /**
     * 修改公告状态
     *
     * @param id    消息主键
     * @param state 状态
     * @return 结果
     */
    public int updateNoticeState(SysNotice notice, Long id, Integer state);

    /**
     * 删除公告信息
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    public int deleteNoticeById(SysNotice notice, Long noticeId);

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    public int deleteNoticeByIds(SysNotice notice, Long[] noticeIds);
}
