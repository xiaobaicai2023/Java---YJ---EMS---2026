package com.yunpower.collect.storage.domain;

import lombok.Data;

/**
 * 通用字典-数据对象 sys_common_dict_data
 * yunpower-common-security 中有引用，所以放到此处
 *
 * @author yunpower
 * @date 2023-09-14
 */
@Data
public class SysCommonDictData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    private Long id;

    /**
     * 字典编码
     */
    private String dictSn;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典排序
     */
    private Integer orderNum;

    /**
     * 图标样式
     */
    private String iconClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（1是 0否）
     */
    private Integer isDefault;

    /**
     * 是否多级（1是 0否）
     */
    private Integer isMultiLevel;

    /**
     * 是否停用（0正常 1停用）
     */
    private Integer stopFlag;

    /**
     * 是否删除（0正常 1删除）
     */
    private Integer deleteFlag;
}
