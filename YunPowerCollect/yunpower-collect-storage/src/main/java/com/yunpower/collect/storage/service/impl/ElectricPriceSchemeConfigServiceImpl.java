package com.yunpower.collect.storage.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.yunpower.collect.storage.domain.ElectricPriceSchemeConfig;
import com.yunpower.collect.storage.domain.jsonvo.SeasonalRangeVo;
import com.yunpower.collect.storage.mapper.ElectricPriceSchemeConfigMapper;
import com.yunpower.collect.storage.service.IElectricPriceSchemeConfigService;
import com.yunpower.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: 电度电价配置Service业务层处理
 * @Author: Jiajiaglam
 * @date: 2023-12-28 15:15
 * @description:
 */
@Service
public class ElectricPriceSchemeConfigServiceImpl implements IElectricPriceSchemeConfigService {
    @Autowired
    private ElectricPriceSchemeConfigMapper electricPriceSchemeConfigMapper;

    /**
     * 通过电价标准ID获取尖峰平谷时间段
     * 电价标准ID-YearMonth：<时：（seasonalName, price）>
     */
    @Override
    public Map<String, Map<Integer, SeasonalRangeVo>> selectSeasonalRangeList() {

        //电价是按月制定的，因此在此处封装：电价标准ID-YearMonth：<时：（seasonalName, price）>
        Map<String, Map<Integer, SeasonalRangeVo>> result = new HashMap<>();

        //获取今年及以后的配置列表
        int year = DateUtil.thisYear();
        List<ElectricPriceSchemeConfig> schemeConfigList = electricPriceSchemeConfigMapper.selectElectricPriceSchemeConfigList(year);

        for (ElectricPriceSchemeConfig item : schemeConfigList) {
            if (StrUtil.isEmpty(item.getEffectMonth())) {
                continue;
            }

            String[] monArr = item.getEffectMonth().split(",");
            for (String mon : monArr) {
                Map<Integer, SeasonalRangeVo> hourTemp = new HashMap<>();

                //说明：最后一个时间段应该是 xx:xx ~ 24:00
                //在时间上是不存在24点的（24点为第二天0点），所以选择了String类型
                //因此，在取时间时用【前闭后开】
                for (SeasonalRangeVo range : item.getSeasonalRange()) {
                    int startHour = Integer.parseInt(range.getStartTime().split(":")[0]);
                    int endHour = Integer.parseInt(range.getEndTime().split(":")[0]);
                    for (int i = startHour; i < endHour; i++) {
                        hourTemp.put(i, range);
                    }
                }
                String key = item.getSchemeId() + "-" + item.getEffectYear() + StringUtils.padl(Integer.parseInt(mon), 2);
                result.put(key, hourTemp);
            }
        }
        return result;
    }



}
