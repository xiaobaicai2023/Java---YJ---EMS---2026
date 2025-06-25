package com.yunpower.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunpower.system.domain.SysMessageTemplate;
import com.yunpower.system.mapper.SysMessageTemplateMapper;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISysMessageTemplateService;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.datascope.annotation.DataScope;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息模板Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class SysMessageTemplateServiceImpl implements ISysMessageTemplateService {
    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询消息模板
     *
     * @param id 消息模板主键
     * @return 消息模板
     */
    @Override
    public SysMessageTemplate selectSysMessageTemplateById(Long id) {
        return sysMessageTemplateMapper.selectSysMessageTemplateById(id);
    }

    /**
     * 查询消息模板列表
     *
     * @param sysMessageTemplate 消息模板
     * @return 消息模板
     */
    @Override
    public List<SysMessageTemplate> selectSysMessageTemplateList(SysMessageTemplate sysMessageTemplate) {
        //获取数据加上 entId==当前站点企业ID
        //解释：一个企业一套模板，没必要一个站点一个
        sysMessageTemplate.setEntId(publicService.getCurrentEnterprise());
        return sysMessageTemplateMapper.selectSysMessageTemplateList(sysMessageTemplate);
    }

    /**
     * 新增消息模板
     *
     * @param sysMessageTemplate 消息模板
     * @return 结果
     */
    @Override
    public int insertSysMessageTemplate(SysMessageTemplate sysMessageTemplate) {
        if (sysMessageTemplate.getEntId() == null || sysMessageTemplate.getEntId() <= 0) {
            sysMessageTemplate.setEntId(publicService.getCurrentEnterprise());
        }
        if (sysMessageTemplate.getDeptId() == null || sysMessageTemplate.getDeptId() <= 0) {
            sysMessageTemplate.setDeptId(publicService.getCurrentStation());
        }
        sysMessageTemplate.setCreateBy(SecurityUtils.getNickName());
        sysMessageTemplate.setCreateTime(DateUtils.getNowDate());
        if (sysMessageTemplate.getStopFlag() == null) {
            sysMessageTemplate.setStopFlag(0);
        }
        sysMessageTemplate.setDeleteFlag(0);
        return sysMessageTemplateMapper.insertSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * 修改消息模板
     *
     * @param sysMessageTemplate 消息模板
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysMessageTemplate(SysMessageTemplate sysMessageTemplate) {
        sysMessageTemplate.setCreateBy(null);
        sysMessageTemplate.setCreateTime(null);
        sysMessageTemplate.setUpdateBy(SecurityUtils.getNickName());
        sysMessageTemplate.setUpdateTime(DateUtils.getNowDate());
        return sysMessageTemplateMapper.updateSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * 修改消息模板状态
     *
     * @param id    消息模板主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateSysMessageTemplateState(SysMessageTemplate sysMessageTemplate, Long id, Integer state) {
        sysMessageTemplate.setId(id);
        sysMessageTemplate.setStopFlag(state);
        sysMessageTemplate.setUpdateBy(SecurityUtils.getNickName());
        sysMessageTemplate.setUpdateTime(DateUtils.getNowDate());
        return sysMessageTemplateMapper.updateSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * 批量删除消息模板
     *
     * @param ids 需要删除的消息模板主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysMessageTemplateByIds(SysMessageTemplate sysMessageTemplate, Long[] ids) {
        Map<String, Object> params = sysMessageTemplate.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        sysMessageTemplate.setParams(params);
        sysMessageTemplate.setDeleteFlag(1);
        return sysMessageTemplateMapper.updateSysMessageTemplate(sysMessageTemplate);
    }

    /**
     * 删除消息模板信息
     *
     * @param id 消息模板主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteSysMessageTemplateById(SysMessageTemplate sysMessageTemplate, Long id) {
        sysMessageTemplate.setId(id);
        sysMessageTemplate.setDeleteFlag(1);
        return sysMessageTemplateMapper.updateSysMessageTemplate(sysMessageTemplate);
    }
}
