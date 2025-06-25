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
import com.yunpower.system.domain.SysNotice;
import com.yunpower.system.mapper.SysNoticeMapper;
import com.yunpower.system.service.ISysNoticeService;

/**
 * 公告 服务层实现
 *
 * @author yunpower
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService {
    @Autowired
    private SysNoticeMapper noticeMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId) {
        return noticeMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        notice.setDeptId(publicService.getCurrentStation());
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice) {
        if (notice.getEntId() == null || notice.getEntId() <= 0) {
            notice.setEntId(publicService.getCurrentEnterprise());
        }
        if (notice.getDeptId() == null || notice.getDeptId() <= 0) {
            notice.setDeptId(publicService.getCurrentStation());
        }
        if (notice.getStopFlag() == null) {
            notice.setStopFlag(0);
        }

        notice.setCreateBy(SecurityUtils.getNickName());
        notice.setCreateTime(DateUtils.getNowDate());
        notice.setDeleteFlag(0);

        return noticeMapper.insertNotice(notice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateNotice(SysNotice notice) {
        notice.setCreateBy(null);
        notice.setCreateTime(null);
        notice.setUpdateBy(SecurityUtils.getNickName());
        notice.setUpdateTime(DateUtils.getNowDate());
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 修改消息状态
     *
     * @param id    消息主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateNoticeState(SysNotice notice, Long id, Integer state) {
        notice.setId(id);
        notice.setStopFlag(state);
        notice.setUpdateBy(SecurityUtils.getNickName());
        notice.setUpdateTime(DateUtils.getNowDate());
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteNoticeById(SysNotice notice, Long noticeId) {
        notice.setId(noticeId);
        notice.setDeleteFlag(1);
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteNoticeByIds(SysNotice notice, Long[] noticeIds) {
        Map<String, Object> params = notice.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", noticeIds);

        notice.setParams(params);
        notice.setDeleteFlag(1);
        return noticeMapper.updateNotice(notice);
    }
}
