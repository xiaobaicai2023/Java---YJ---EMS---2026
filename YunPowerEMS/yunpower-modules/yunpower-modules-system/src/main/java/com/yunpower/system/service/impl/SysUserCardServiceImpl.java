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
import com.yunpower.system.mapper.SysUserCardMapper;
import com.yunpower.system.domain.SysUserCard;
import com.yunpower.system.service.ISysUserCardService;

/**
 * 用户充值卡Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysUserCardServiceImpl implements ISysUserCardService {
    @Autowired
    private SysUserCardMapper sysUserCardMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询用户充值卡
     *
     * @param id 用户充值卡主键
     * @return 用户充值卡
     */
    @Override
    public SysUserCard selectSysUserCardById(Long id) {
        return sysUserCardMapper.selectSysUserCardById(id);
    }

    /**
     * 查询用户充值卡列表
     *
     * @param sysUserCard 用户充值卡
     * @return 用户充值卡
     */
    @Override
    public List<SysUserCard> selectSysUserCardList(SysUserCard sysUserCard) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        sysUserCard.setDeptId(publicService.getCurrentStation());
        return sysUserCardMapper.selectSysUserCardList(sysUserCard);
    }

    /**
     * 新增用户充值卡
     *
     * @param sysUserCard 用户充值卡
     * @return 结果
     */
    @Override
    public int insertSysUserCard(SysUserCard sysUserCard) {
        if (sysUserCard.getEntId() == null || sysUserCard.getEntId() <= 0) {
            sysUserCard.setEntId(publicService.getCurrentEnterprise());
        }
        if (sysUserCard.getDeptId() == null || sysUserCard.getDeptId() <= 0) {
            sysUserCard.setDeptId(publicService.getCurrentStation());
        }
        sysUserCard.setCreateBy(SecurityUtils.getNickName());
        sysUserCard.setCreateTime(DateUtils.getNowDate());
        if (sysUserCard.getStopFlag() == null) {
            sysUserCard.setStopFlag(0);
        }
        sysUserCard.setDeleteFlag(0);
        return sysUserCardMapper.insertSysUserCard(sysUserCard);
    }

    /**
     * 修改用户充值卡
     *
     * @param sysUserCard 用户充值卡
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysUserCard(SysUserCard sysUserCard) {
        sysUserCard.setCreateBy(null);
        sysUserCard.setCreateTime(null);
        sysUserCard.setUpdateBy(SecurityUtils.getNickName());
        sysUserCard.setUpdateTime(DateUtils.getNowDate());
        return sysUserCardMapper.updateSysUserCard(sysUserCard);
    }

    /**
     * 修改用户充值卡状态
     *
     * @param id    用户充值卡主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysUserCardState(SysUserCard sysUserCard, Long id, Integer state) {
        sysUserCard.setId(id);
        sysUserCard.setStopFlag(state);
        sysUserCard.setUpdateBy(SecurityUtils.getNickName());
        sysUserCard.setUpdateTime(DateUtils.getNowDate());
        return sysUserCardMapper.updateSysUserCard(sysUserCard);
    }

    /**
     * 批量删除用户充值卡
     *
     * @param ids 需要删除的用户充值卡主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysUserCardByIds(SysUserCard sysUserCard, Long[] ids) {
        Map<String, Object> params = sysUserCard.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        sysUserCard.setParams(params);
        sysUserCard.setDeleteFlag(1);
        return sysUserCardMapper.updateSysUserCard(sysUserCard);
    }

    /**
     * 删除用户充值卡信息
     *
     * @param id 用户充值卡主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysUserCardById(SysUserCard sysUserCard, Long id) {
        sysUserCard.setId(id);
        sysUserCard.setDeleteFlag(1);
        return sysUserCardMapper.updateSysUserCard(sysUserCard);
    }
}
