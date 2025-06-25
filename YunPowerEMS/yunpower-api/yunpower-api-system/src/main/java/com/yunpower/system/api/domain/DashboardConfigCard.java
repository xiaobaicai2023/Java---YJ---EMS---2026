package com.yunpower.system.api.domain;

import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
/**
 * 仪表盘卡片配置对象 dashboard_config_card
 *
 * @author yunpower
 * @date 2024-06-05
 */
@ApiModel("仪表盘卡片配置对象")
public class DashboardConfigCard extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    @Excel(name = "企业ID")
    private Long entId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @Excel(name = "部门ID")
    private Long deptId;

    /**
     * 配置ID
     */
    @ApiModelProperty("配置ID")
    @Excel(name = "配置ID")
    @NotNull(message = "dashboardConfigId 不能为空")
    @Min(value = 1, message = "配置页面Id错误")
    private Long dashboardConfigId;

    /**
     * 卡片名称
     */
    @ApiModelProperty("卡片名称")
    @Excel(name = "卡片名称")
    @NotEmpty(message = "卡片名称 不能为空")
    @Length(max = 20, message = "卡片名称最大20位")
    private String cardName;

    /**
     * 卡片key
     */
    @ApiModelProperty("卡片key")
    @Excel(name = "卡片key")
    @NotEmpty(message = "卡片key 不能为空")
    private String cardKey;

    /**
     * 日期维度（5、10、15、30分钟、时、日、月、年）取字典sys_config_page_x值
     */
    @ApiModelProperty("日期维度（日、月、年）取字典sys_config_page_x值")
    @Excel(name = "日期维度")
//    @NotNull(message = "日期维度 不能为空")
    private Integer dateDim;

    /**
     * 二级日期维度（5、10、15、30分钟、1时）取字典sys_config_page_x值
     */
    @ApiModelProperty("二级维度（5、10、15、30分钟、1时）取字典sys_config_page_x值")
    @Excel(name = "二级日期维度")
//    @NotNull(message = "二级日期维度 不能为空")
    private Integer secondDateDim;

    /**
     * 是否全日期（发电时用到）（0否 1是）
     */
    @ApiModelProperty("是否全日期（发电时用到）（0否 1是）")
    @Excel(name = "是否全日期", readConverterExp = "0=否,1=是")
    private Integer isFullDate;

    /**
     * 指标度量（1指定变量  2通用变量）
     */
    @ApiModelProperty("指标度量（1指定变量  2通用变量）")
    @Excel(name = "指标度量", readConverterExp = "1=指定变量,2=通用变量")
//    @NotNull(message = "指标度量 不能为空")
    private Integer indicator;

    /**
     * 图表类型
     */
    @ApiModelProperty("图表类型")
    @Excel(name = "图表类型")
    private Integer chartType;

    /**
     * 卡片配置json
     */
    @ApiModelProperty("卡片配置json")
    @Excel(name = "卡片配置json")
    private String cardConfig;

    /**
     * 卡片配置json 用于预览查看
     */
    @ApiModelProperty("卡片配置json 用于预览查看")
    @Excel(name = "卡片配置json 用于预览查看")
    private String cardConfigPre;

    /**
     * 是否停用（0正常 1停用）
     */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /**
     * 是否删除（0正常 1删除）
     */
    @ApiModelProperty("是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    /**
     *页面配置
     */
    @Transient
    private Long pageType;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getEntId() {
        return entId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDashboardConfigId(Long dashboardConfigId) {
        this.dashboardConfigId = dashboardConfigId;
    }

    public Long getDashboardConfigId() {
        return dashboardConfigId;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardKey(String cardKey) {
        this.cardKey = cardKey;
    }

    public String getCardKey() {
        return cardKey;
    }

    public void setDateDim(Integer dateDim) {
        this.dateDim = dateDim;
    }

    public Integer getDateDim() {
        return dateDim;
    }


    public Integer getSecondDateDim() {
        return secondDateDim;
    }

    public void setSecondDateDim(Integer secondDateDim) {
        this.secondDateDim = secondDateDim;
    }

    public void setIsFullDate(Integer isFullDate) {
        this.isFullDate = isFullDate;
    }

    public Integer getIsFullDate() {
        return isFullDate;
    }

    public void setIndicator(Integer indicator) {
        this.indicator = indicator;
    }

    public Integer getIndicator() {
        return indicator;
    }

    public void setChartType(Integer chartType) {
        this.chartType = chartType;
    }

    public Integer getChartType() {
        return chartType;
    }

    public void setCardConfig(String cardConfig) {
        this.cardConfig = cardConfig;
    }

    public String getCardConfig() {
        return cardConfig;
    }

    public void setCardConfigPre(String cardConfigPre) {
        this.cardConfigPre = cardConfigPre;
    }

    public String getCardConfigPre() {
        return cardConfigPre;
    }

    public void setStopFlag(Integer stopFlag) {
        this.stopFlag = stopFlag;
    }

    public Integer getStopFlag() {
        return stopFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public Long getPageType() {
        return pageType;
    }

    public void setPageType(Long pageType) {
        this.pageType = pageType;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("dashboardConfigId", getDashboardConfigId())
                .append("cardName", getCardName())
                .append("cardKey", getCardKey())
                .append("dateDim", getDateDim())
                .append("isFullDate", getIsFullDate())
                .append("indicator", getIndicator())
                .append("chartType", getChartType())
                .append("cardConfig", getCardConfig())
                .append("cardConfigPre", getCardConfigPre())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("stopFlag", getStopFlag())
                .append("deleteFlag", getDeleteFlag())
                .toString();
    }
}
