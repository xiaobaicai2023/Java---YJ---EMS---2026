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
import com.yunpower.system.mapper.ElectricPriceSchemeMapper;
import com.yunpower.system.domain.ElectricPriceScheme;
import com.yunpower.system.service.IElectricPriceSchemeService;

/**
 * 电价标准Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@Service
public class ElectricPriceSchemeServiceImpl implements IElectricPriceSchemeService {
    @Autowired
    private ElectricPriceSchemeMapper electricPriceSchemeMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询电价标准
     *
     * @param id 电价标准主键
     * @return 电价标准
     */
    @Override
    public ElectricPriceScheme selectElectricPriceSchemeById(Long id) {
        return electricPriceSchemeMapper.selectElectricPriceSchemeById(id);
    }

    /**
     * 查询电价标准
     */
    @Override
    public ElectricPriceScheme selectElectricPriceScheme(ElectricPriceScheme electricPriceScheme) {
        return electricPriceSchemeMapper.selectElectricPriceScheme(electricPriceScheme);
    }

    /**
     * 查询电价标准列表
     *
     * @param electricPriceScheme 电价标准
     * @return 电价标准
     */
    @Override
    public List<ElectricPriceScheme> selectElectricPriceSchemeList(ElectricPriceScheme electricPriceScheme) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        electricPriceScheme.setDeptId(publicService.getCurrentStation());
        return electricPriceSchemeMapper.selectElectricPriceSchemeList(electricPriceScheme);
    }

    /**
     * 新增电价标准
     *
     * @param electricPriceScheme 电价标准
     * @return 结果
     */
    @Override
    public int insertElectricPriceScheme(ElectricPriceScheme electricPriceScheme) {
        if (electricPriceScheme.getEntId() == null || electricPriceScheme.getEntId() <= 0) {
            electricPriceScheme.setEntId(publicService.getCurrentEnterprise());
        }
        if (electricPriceScheme.getDeptId() == null || electricPriceScheme.getDeptId() <= 0) {
            electricPriceScheme.setDeptId(publicService.getCurrentStation());
        }
        electricPriceScheme.setCreateBy(SecurityUtils.getNickName());
        electricPriceScheme.setCreateTime(DateUtils.getNowDate());
        if (electricPriceScheme.getStopFlag() == null) {
            electricPriceScheme.setStopFlag(0);
        }
        electricPriceScheme.setDeleteFlag(0);
        return electricPriceSchemeMapper.insertElectricPriceScheme(electricPriceScheme);
    }

    /**
     * 修改电价标准
     *
     * @param electricPriceScheme 电价标准
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateElectricPriceScheme(ElectricPriceScheme electricPriceScheme) {
        electricPriceScheme.setCreateBy(null);
        electricPriceScheme.setCreateTime(null);
        electricPriceScheme.setUpdateBy(SecurityUtils.getNickName());
        electricPriceScheme.setUpdateTime(DateUtils.getNowDate());
        return electricPriceSchemeMapper.updateElectricPriceScheme(electricPriceScheme);
    }

    /**
     * 修改电价标准状态
     *
     * @param id    电价标准主键
     * @param state 状态
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int updateElectricPriceSchemeState(ElectricPriceScheme electricPriceScheme, Long id, Integer state) {
        electricPriceScheme.setId(id);
        electricPriceScheme.setStopFlag(state);
        electricPriceScheme.setUpdateBy(SecurityUtils.getNickName());
        electricPriceScheme.setUpdateTime(DateUtils.getNowDate());
        return electricPriceSchemeMapper.updateElectricPriceScheme(electricPriceScheme);
    }

    /**
     * 批量删除电价标准
     *
     * @param ids 需要删除的电价标准主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteElectricPriceSchemeByIds(ElectricPriceScheme electricPriceScheme, Long[] ids) {
        Map<String, Object> params = electricPriceScheme.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("ids", ids);

        electricPriceScheme.setParams(params);
        electricPriceScheme.setDeleteFlag(1);
        return electricPriceSchemeMapper.updateElectricPriceScheme(electricPriceScheme);
    }

    /**
     * 删除电价标准信息
     *
     * @param id 电价标准主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "s")
    public int deleteElectricPriceSchemeById(ElectricPriceScheme electricPriceScheme, Long id) {
        electricPriceScheme.setId(id);
        electricPriceScheme.setDeleteFlag(1);
        return electricPriceSchemeMapper.updateElectricPriceScheme(electricPriceScheme);
    }

    /**
     * 校验配置年份是否唯一：新增是只校验deptId，修改和启用时还要校验id
     *
     * @param deptId     站点（部门）ID
     * @param effectYear 生效年份
     * @return 结果
     */
    @Override
    public ElectricPriceScheme checkEffectYearUnique(Long deptId, Integer effectYear) {
        return electricPriceSchemeMapper.checkEffectYearUnique(deptId, effectYear);
    }
}
