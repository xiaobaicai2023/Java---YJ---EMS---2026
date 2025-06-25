package com.yunpower.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.common.core.constant.UserConstants;
import com.yunpower.common.core.exception.ServiceException;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.system.domain.SysPost;
import com.yunpower.system.mapper.SysPostMapper;
import com.yunpower.system.mapper.SysUserPostMapper;
import com.yunpower.system.service.ISysPostService;

/**
 * 岗位信息 服务层处理
 *
 * @author yunpower
 * @description 用户、角色、岗位全局共享
 * 岗位不设条件，所有人可见
 */
@Service
public class SysPostServiceImpl implements ISysPostService {
    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return postMapper.selectPostList(post);
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostAll() {
        return postMapper.selectPostAll();
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(Long postId) {
        return postMapper.selectPostById(postId);
    }

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostNameUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getId()) ? -1L : post.getId();
        SysPost info = postMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostCodeUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getId()) ? -1L : post.getId();
        SysPost info = postMapper.checkPostCodeUnique(post.getPostSn());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId) {
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int insertPost(SysPost post) {
        if (post.getEntId() == null || post.getEntId() <= 0) {
            post.setEntId(publicService.getCurrentEnterprise());
        }
        if (post.getDeptId() == null || post.getDeptId() <= 0) {
            post.setDeptId(publicService.getCurrentStation());
        }
        if (post.getStopFlag() == null) {
            post.setStopFlag(0);
        }

        post.setCreateBy(SecurityUtils.getNickName());
        post.setCreateTime(DateUtils.getNowDate());
        post.setDeleteFlag(0);

        return postMapper.insertPost(post);
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int updatePost(SysPost post) {
        post.setCreateBy(null);
        post.setCreateTime(null);
        post.setUpdateBy(SecurityUtils.getNickName());
        post.setUpdateTime(DateUtils.getNowDate());
        return postMapper.updatePost(post);
    }

    /**
     * 修改消息状态
     *
     * @param id    消息主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updatePostState(Long id, Integer state) {
        SysPost sysPost = new SysPost();
        sysPost.setId(id);
        sysPost.setStopFlag(state);
        sysPost.setUpdateBy(SecurityUtils.getNickName());
        sysPost.setUpdateTime(DateUtils.getNowDate());
        return postMapper.updatePost(sysPost);
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int deletePostById(Long postId) {
        SysPost post = selectPostById(postId);
        if (post == null) {
            throw new ServiceException("要删除的岗位不存在");
        }
        if (countUserPostById(postId) > 0) {
            throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
        }

        SysPost sysPost = new SysPost();
        sysPost.setId(postId);
        sysPost.setDeleteFlag(1);
        return postMapper.updatePost(sysPost);
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    @Override
    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("ids", postIds);

        SysPost sysPost = new SysPost();
        sysPost.setParams(params);
        sysPost.setDeleteFlag(1);
        return postMapper.updatePost(sysPost);
    }
}
