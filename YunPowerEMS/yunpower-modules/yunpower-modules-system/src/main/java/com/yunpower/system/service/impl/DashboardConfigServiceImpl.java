package com.yunpower.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yunpower.common.core.exception.ServiceException;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.bean.BeanUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.DashboardConfigCard;
import com.yunpower.system.api.domain.SysStation;
import com.yunpower.system.domain.DashboardConfig;
import com.yunpower.system.domain.jsonvo.DashboardBiPageVo;
import com.yunpower.system.domain.vo.AssignDataVo;
import com.yunpower.system.mapper.DashboardConfigCardMapper;
import com.yunpower.system.mapper.DashboardConfigMapper;
import com.yunpower.system.service.IDashboardConfigService;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.ISpecialDataProvideService;
import com.yunpower.system.service.ISysStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 仪表盘配置Service业务层处理
 *
 * @author yunpower
 * @date 2024-05-30
 */
@Service
public class DashboardConfigServiceImpl implements IDashboardConfigService {

    private static final Logger log = LoggerFactory.getLogger(DashboardConfigServiceImpl.class);
    @Autowired
    private DashboardConfigMapper dashboardConfigMapper;

    @Autowired
    private DashboardConfigCardMapper dashboardConfigCardMapper;

    @Autowired
    private IPublicService publicService;

    @Autowired
    private ISysStationService stationService;

    @Autowired
    private ISpecialDataProvideService specialDataProvideService;

    /**
     * 查询仪表盘配置
     *
     * @param id 仪表盘配置主键
     * @return 仪表盘配置
     */
    @Override
    public DashboardConfig selectDashboardConfigById(Long id) {
        return dashboardConfigMapper.selectDashboardConfigById(id);
    }

    /**
     * 查询仪表盘配置
     *
     * @param pageKey 页面配置key
     * @return 仪表盘配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DashboardConfig selectDashboardConfigByPageKey(String pageKey) {
        if (StringUtils.isEmpty(pageKey)) {
            throw new ServiceException("页面key不能为空");
        }
        DashboardConfig config = dashboardConfigMapper.selectDashboardConfigByPageKey(pageKey);
        if (config != null) {
            return config;
        }
        int lastDashIndex = pageKey.lastIndexOf('-');
        if (lastDashIndex == -1) {
            throw new ServiceException("pageKey格式错误");
        }
        //截取后拼接上默认标记
        String pageKeyStr = pageKey.substring(0, lastDashIndex) + "-default";
        //判断下是否有默认模板
        DashboardConfig query = new DashboardConfig();
        query.setEntId(0L);
        query.setDeptId(0L);
        query.setPageKey(pageKeyStr);
        DashboardConfig template = dashboardConfigMapper.selectDashboardConfig(query);
        //没有默认模板则创建一个空的返回去
        if (template == null) {
            DashboardConfig insertConfig = new DashboardConfig();
            insertConfig.setPageKey(pageKey);
            insertConfig.setPageType(2L);
            insertConfig.setPageConfig("[]");
            int result = this.insertDashboardConfig(insertConfig);
            if(result <=0){
                throw new ServiceException("创建页面模板失败");
            }
            return insertConfig;
        }
        //拷贝默认模板数据
        DashboardConfig newCard = new DashboardConfig();
        BeanUtils.copyProperties(template, newCard);
        newCard.setPageKey(pageKey);
        newCard.setId(null);

        Map<String, String> cardKeyMap = new HashMap<>();

        //新增卡片内容
        //查询默认模板的卡片配置
        DashboardConfigCard cardQuery = new DashboardConfigCard();
        cardQuery.setDashboardConfigId(template.getId());
        cardQuery.setStopFlag(0);
        cardQuery.setDeleteFlag(0);
        List<DashboardConfigCard> newConfigCardList = new ArrayList<>();
        List<DashboardConfigCard> configCardList = dashboardConfigCardMapper.selectDashboardConfigCardList(cardQuery);
        if (configCardList != null) {
            for (DashboardConfigCard configCard : configCardList) {
                configCard.setId(null);
                String newKey = String.valueOf(new Date().getTime() + (new Random().nextInt(9000) + 1000));
                cardKeyMap.put(configCard.getCardKey(), newKey);
                configCard.setCardKey(newKey);
                newConfigCardList.add(configCard);
            }
            newCard.setPageConfig(newCard.getPageConfig().replaceAll(template.getPageKey(),newCard.getPageKey()));
            for (Map.Entry<String, String> map : cardKeyMap.entrySet()) {
                newCard.setPageConfig(newCard.getPageConfig().replaceAll(map.getKey(),map.getValue()));
            }
            int result = this.insertDashboardConfig(newCard);
            if (result <= 0) {
                throw new ServiceException("新增卡片失败");
            }
            for (DashboardConfigCard configCard : newConfigCardList) {
                configCard.setEntId(newCard.getEntId());
                configCard.setDeptId(newCard.getDeptId());
                configCard.setCreateBy(newCard.getCreateBy());
                configCard.setDashboardConfigId(newCard.getId());
                dashboardConfigCardMapper.insertDashboardConfigCard(configCard);
            }
        }
        return newCard;
    }

    /**
     * 查询仪表盘配置-站点id
     *
     * @param stationId 站点id
     * @param isPre     是否预览 1预览
     * @return 仪表盘配置
     */
    @Override
    public Map<String, Object> selectDashboardConfigByStationId(Long stationId, Integer isPre) {
        DashboardConfig query = new DashboardConfig();
        if (stationId == null || stationId <= 0) {
            stationId = publicService.getCurrentStation();
        }

        SysStation station = stationService.selectSysStationByDeptId(stationId);

        query.setDeptId(stationId);
        query.setPageType(1L);
        query.setDeleteFlag(0);
        query.setStopFlag(0);
        DashboardConfig config = dashboardConfigMapper.selectDashboardConfig(query);
        if (config == null) {
            return null;
        }
        String configStr = config.getPageConfig();
        if (isPre == 1) {
            configStr = config.getPageConfigPre();
        }
        DashboardBiPageVo biPageVo = JSON.parseObject(configStr, DashboardBiPageVo.class);

        Map<String, Object> map = new HashMap<>();
        map.put("pageName", config.getPageName());
        map.put("configId", config.getId());
        map.put("stationSn", station.getStationSn());
        if (config.getPageType() == 1 && StringUtils.isNotEmpty(config.getPageKey())) {
            String[] split = config.getPageKey().split("-");
            if (split.length == 2) {
                map.put("pageTemplate", split[1]);
            }
        }
        //处理背景
        handleBackground(map, biPageVo);
        //处理头部-日期
        handleHeader(map, biPageVo);
        //处理站点、设备、点位、转化率
        handleContent(map, biPageVo, stationId);
        //处理中间区域总统计
        handleCoreMain(map, biPageVo);
        //处理中间子区域统计
        handleCoreSubStatData(map, biPageVo);
        //处理中间区域背景图
        handleCoreBackground(map, biPageVo);
        //处理大屏卡片
        handleCardList(map, biPageVo, config.getId());
        return map;
    }

    /**
     * 查询仪表盘配置列表
     *
     * @param dashboardConfig 仪表盘配置
     * @return 仪表盘配置
     */
    @Override
    public List<DashboardConfig> selectDashboardConfigList(DashboardConfig dashboardConfig) {
        return dashboardConfigMapper.selectDashboardConfigList(dashboardConfig);
    }

    /**
     * 查询默认仪表盘配置列表
     *
     * @param dashboardConfig 仪表盘配置
     * @return 仪表盘配置
     */
    @Override
    public List<DashboardConfig> selectDefaultDashboardConfigList(DashboardConfig dashboardConfig) {
        return dashboardConfigMapper.selectDashboardConfigList(dashboardConfig);
    }

    /**
     * 新增仪表盘配置
     *
     * @param dashboardConfig 仪表盘配置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertDashboardConfig(DashboardConfig dashboardConfig) {
        if (dashboardConfig.getEntId() == null || dashboardConfig.getEntId() <= 0) {
            dashboardConfig.setEntId(publicService.getCurrentEnterprise());
        }
        if (dashboardConfig.getDeptId() == null || dashboardConfig.getDeptId() <= 0) {
            dashboardConfig.setDeptId(publicService.getCurrentStation());
        }
        dashboardConfig.setCreateBy(SecurityUtils.getNickName());
        dashboardConfig.setCreateTime(DateUtils.getNowDate());
        if (dashboardConfig.getStopFlag() == null) {
            dashboardConfig.setStopFlag(0);
        }
        dashboardConfig.setDeleteFlag(0);

        //每个站点只能添加一种模版
        if (dashboardConfig.getPageType() == 1) {
            DashboardConfig query = new DashboardConfig();
            query.setDeptId(dashboardConfig.getDeptId());
            query.setPageType(1L);
            query.setDeleteFlag(0);
            DashboardConfig temp = dashboardConfigMapper.selectDashboardConfig(query);
            if (temp != null) {
                throw new ServiceException("每个站点只能添加一种模版");
            }
        }
        int result = dashboardConfigMapper.insertDashboardConfig(dashboardConfig);
        if (result <= 0) {
            throw new ServiceException("新增失败");
        }
        log.info("dashboardConfig.id=>{}", dashboardConfig.getId());
        return dashboardConfig.getId().intValue();
    }

    /**
     * 修改仪表盘配置
     *
     * @param dashboardConfig 仪表盘配置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDashboardConfig(DashboardConfig dashboardConfig) {
        if (StringUtils.isNull(dashboardConfig.getId()) || dashboardConfig.getId() <= 0) {
            return this.insertDashboardConfig(dashboardConfig);
        }
        List<String> cardKeyList = null;
        if (dashboardConfig.getPageType() == 2) {
            cardKeyList = checkCardKey(dashboardConfig);
        }
        dashboardConfig.setUpdateBy(SecurityUtils.getNickName());
        dashboardConfig.setUpdateTime(DateUtils.getNowDate());
        int result = dashboardConfigMapper.updateDashboardConfig(dashboardConfig);
        //如果是普通页面则删除没用的卡片
        if (result > 0 && dashboardConfig.getPageType() == 2L) {
            dashboardConfigCardMapper.deleteByConfigIdWidthCardKeyList(dashboardConfig.getId(), cardKeyList);
        }
        return result;
    }

    /**
     * 修改仪表盘配置状态
     *
     * @param id    仪表盘配置主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateDashboardConfigState(Long id, Integer state) {
        DashboardConfig dashboardConfig = new DashboardConfig();
        dashboardConfig.setId(id);
        dashboardConfig.setStopFlag(state);
        dashboardConfig.setUpdateBy(SecurityUtils.getNickName());
        dashboardConfig.setUpdateTime(DateUtils.getNowDate());
        return dashboardConfigMapper.updateDashboardConfig(dashboardConfig);
    }

    /**
     * 批量删除仪表盘配置
     *
     * @param ids 需要删除的仪表盘配置主键
     * @return 结果
     */
    @Override
    public int deleteDashboardConfigByIds(Long[] ids) {
        return dashboardConfigMapper.deleteDashboardConfigByIds(ids);
    }

    /**
     * 删除仪表盘配置信息
     *
     * @param id 仪表盘配置主键
     * @return 结果
     */
    @Override
    public int deleteDashboardConfigById(Long id) {
        return dashboardConfigMapper.deleteDashboardConfigById(id);
    }


    /*
     * 检查布局i是否重复
     * @param dashboardConfig 页面参数上
     * @return i集合
     **/
    public List<String> checkCardKey(DashboardConfig dashboardConfig) {
        JSONArray jsonArray = JSON.parseArray(dashboardConfig.getPageConfig());
        List<String> cardKeyList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            String iStr = jsonArray.getJSONObject(i).getString("i");
            if (cardKeyList.contains(iStr)) {
                throw new ServiceException("卡片数据重复");
            }
            cardKeyList.add(iStr);
        }
        return cardKeyList;
    }

    //region 处理大屏显示

    /**
     * 处理整体背景图片
     *
     * @param map      配置map
     * @param biPageVo 页面配置
     */
    private void handleBackground(Map<String, Object> map, DashboardBiPageVo biPageVo) {
        if (biPageVo.getBackground() == null) {
            return;
        }
        Map<String, Object> background = new HashMap<>();
        background.put("type", biPageVo.getBackground().getType());
        background.put("url", biPageVo.getBackground().getUrl());
        map.put("background", background);
    }


    /**
     * 处理头部日期
     *
     * @param map      配置map
     * @param biPageVo 页面配置
     */
    private void handleHeader(Map<String, Object> map, DashboardBiPageVo biPageVo) {
        if (StringUtils.isEmpty(biPageVo.getHeader())) {
            return;
        }
        Map<String, Object> header = new HashMap<>();
        biPageVo.getHeader().forEach(item -> {
            if (item.getValue()) {
                header.put(item.getKey(), true);
            }
        });
        map.put("header", header);
    }

    /**
     * 处理中间内容
     *
     * @param map      配置map
     * @param biPageVo 页面配置
     */
    private void handleContent(Map<String, Object> map, DashboardBiPageVo biPageVo, Long stationId) {
        if (StringUtils.isEmpty(biPageVo.getContent())) {
            return;
        }
        Map<String, Object> content = new HashMap<>();
        biPageVo.getContent().forEach(item -> {
            if (item.getValue()) {
                //获取指定接口数据
                AssignDataVo assignDataVo = Optional
                        .ofNullable(specialDataProvideService.getAssignedData(stationId, item.getKey()))
                        .orElseGet(() -> new AssignDataVo(0));
                content.put(item.getKey(), assignDataVo.getValue());
            }
        });
        map.put("content", content);
    }

    /**
     * 处理核心统计数据
     *
     * @param map      配置map
     * @param biPageVo 页面配置
     */
    private void handleCoreMain(Map<String, Object> map, DashboardBiPageVo biPageVo) {
        if (StringUtils.isEmpty(biPageVo.getCoreMainData())) {
            return;
        }
        Optional<DashboardBiPageVo.ItemInfo> first = biPageVo.getCoreMainData().stream()
                .filter(DashboardBiPageVo.ItemInfo::getValue)
                .findFirst();
        if (first.isPresent()) {
            Map<String, Object> coreMain = new HashMap<>();
            coreMain.put("label", first.get().getLabel());
            coreMain.put("key", first.get().getKey());
            coreMain.put("value", 0f); // TODO: Replace with actual value
            coreMain.put("unit", "");
            map.put("coreMain", coreMain);
        }
    }

    /**
     * 处理核心统计数据
     *
     * @param map      配置map
     * @param biPageVo 页面配置
     */
    private void handleCoreSubStatData(Map<String, Object> map, DashboardBiPageVo biPageVo) {
        if (StringUtils.isEmpty(biPageVo.getCoreSubStatData())) {
            return;
        }
        List<Map<String, Object>> content = new ArrayList<>();
        for (Map.Entry<String, String> entry : biPageVo.getCoreSubStatData().entrySet()) {
            Map<String, Object> coreSubStat = new HashMap<>();
            coreSubStat.put("label", "");
            coreSubStat.put("key", entry.getValue());
            coreSubStat.put("value", 0f);
            coreSubStat.put("unit", "");
            content.add(coreSubStat);
        }
        map.put("coreSubStatData", content);
    }

    /**
     * 处理中间区域背景
     *
     * @param map      配置map
     * @param biPageVo 页面配置
     */
    private void handleCoreBackground(Map<String, Object> map, DashboardBiPageVo biPageVo) {
        if (biPageVo.getCoreBackground() == null) {
            return;
        }
        Map<String, Object> coreBackground = new HashMap<>();
        coreBackground.put("type", biPageVo.getCoreBackground().getType());
        coreBackground.put("url", biPageVo.getCoreBackground().getUrl());
        map.put("coreBackground", coreBackground);
    }

    /**
     * 处理卡片信息
     *
     * @param map      配置map
     * @param biPageVo 页面配置
     */
    private void handleCardList(Map<String, Object> map, DashboardBiPageVo biPageVo, Long dashboardConfigId) {
        if (biPageVo.getCardList() == null) {
            return;
        }
        //获取配置的所有卡片
        DashboardConfigCard query = new DashboardConfigCard();
        query.setDashboardConfigId(dashboardConfigId);
        query.setDeleteFlag(0);
        query.setStopFlag(0);
        List<DashboardConfigCard> configCardList = dashboardConfigCardMapper.selectDashboardConfigCardList(query);
        if (StringUtils.isEmpty(configCardList)) {
            return;
        }

        //按照cardKey分组
        Map<String, DashboardConfigCard> cardKeyMap = configCardList.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(DashboardConfigCard::getCardKey),
                        itemMap -> itemMap.entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> entry.getValue().get(0)  // 只保留每组中的第一条记录
                                ))
                ));
        biPageVo.getCardList().getLeftTop().forEach(item -> {
            if (cardKeyMap.containsKey(item.getKey())) {
                DashboardConfigCard card = cardKeyMap.get(item.getKey());
                if (card != null) {
                    item.setCardName(card.getCardName());
                    item.setConfigId(card.getDashboardConfigId());
                    item.setChartType(card.getChartType());
                }
            }
        });
        biPageVo.getCardList().getRightTop().forEach(item -> {
            if (cardKeyMap.containsKey(item.getKey())) {
                DashboardConfigCard card = cardKeyMap.get(item.getKey());
                if (card != null) {
                    item.setCardName(card.getCardName());
                    item.setConfigId(card.getDashboardConfigId());
                    item.setChartType(card.getChartType());
                }
            }
        });
        biPageVo.getCardList().getBottomList().forEach(item -> {
            if (cardKeyMap.containsKey(item.getKey())) {
                DashboardConfigCard card = cardKeyMap.get(item.getKey());
                if (card != null) {
                    item.setCardName(card.getCardName());
                    item.setConfigId(card.getDashboardConfigId());
                    item.setChartType(card.getChartType());
                }
            }
        });
        Map<String, Object> cardList = new HashMap<>();
        cardList.put("leftTop", biPageVo.getCardList().getLeftTop());
        cardList.put("rightTop", biPageVo.getCardList().getRightTop());
        cardList.put("bottomList", biPageVo.getCardList().getBottomList());
        map.put("cardList", cardList);
    }

    // endregion
}
