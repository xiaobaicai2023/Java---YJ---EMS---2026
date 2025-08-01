package com.yunpower.collect.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yunpower.collect.storage.domain.handler.SeasonalRangeTypeHandler;
import com.yunpower.collect.storage.domain.jsonvo.SeasonalRangeVo;
import lombok.Data;

import java.util.List;

/**
 * 电度电价配置对象 electric_price_scheme_config
 * 
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@Data
public class ElectricPriceSchemeConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    private Long id;

    /** 电价标准ID */
    private Long schemeId;

    /** 生效年份 */
    private Integer effectYear;

    /** 生效月份集合 */
    private String effectMonth;

    /** 电价类型：1分时电价 2统一电价 */
    private Integer priceType;

    /** 统一电价 */
    private Float uniformPrice;

    /** 尖峰电价 */
    private Float jianPrice;

    /** 峰段电价 */
    private Float fengPrice;

    /** 平段电价 */
    private Float pingPrice;

    /** 谷段电价 */
    private Float guPrice;

    /** 深谷电价 */
    private Float shenPrice;

    /** 全天电价 */
    private Float wholePrice;

    /** 时间段（JSON格式） */
    @TableField(typeHandler = SeasonalRangeTypeHandler.class)
    private List<SeasonalRangeVo> seasonalRange;
}
