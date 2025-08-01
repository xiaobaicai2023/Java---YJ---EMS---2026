package com.yunpower.system.domain.jsonvo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 仪表盘-大屏页面配置
 */
public class DashboardBiPageVo implements Serializable {

    /**
     * 头部配置
     */
    private List<ItemInfo> header;

    /**
     * 内容
     */
    private List<ItemInfo> content;

    /**
     * 核心区域-主配置
     */
    private List<ItemInfo> coreMainData;

    /**
     * 核心区域-子统计
     */
    private Map<String,String> coreSubStatData;

    /**
     * 整体背景
     */
    private MediaInfo background;

    /**
     * 核心区域-背景
     */
    private MediaInfo coreBackground;

    /**
     * 卡片配置
     */
    private CardStructure cardList;

    /**
     * 子项
     */
    public static class ItemInfo implements Serializable {
        private Integer id;
        private String key;
        private String label;
        private Boolean value = false;

        /**
         * 给前端返回卡片类型
         * */
        private Integer chartType;

        /**
         * 给前端返回卡片名称
         * */
        private String cardName;

        /**
         * 给前端返回卡片配置ID
         * */
        private Long configId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Boolean getValue() {
            return value;
        }

        public void setValue(Boolean value) {
            this.value = value;
        }



        public Integer getChartType() {
            return chartType;
        }

        public void setChartType(Integer chartType) {
            this.chartType = chartType;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public Long getConfigId() {
            return configId;
        }

        public void setConfigId(Long configId) {
            this.configId = configId;
        }
    }

    /**
     * 媒体信息
     */
    public static class MediaInfo implements Serializable {
        private String type;
        private String url;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * 卡片结构
     */
    public static class CardStructure implements Serializable {

        /**
         * 左侧-卡片配置
         */
        private List<ItemInfo> leftTop;

        /**
         * 右侧卡片配置
         */
        private List<ItemInfo> rightTop;

        /**
         * 底部配置
         */
        private List<ItemInfo> bottomList;


        public List<ItemInfo> getLeftTop() {
            return leftTop;
        }

        public void setLeftTop(List<ItemInfo> leftTop) {
            this.leftTop = leftTop;
        }

        public List<ItemInfo> getRightTop() {
            return rightTop;
        }

        public void setRightTop(List<ItemInfo> rightTop) {
            this.rightTop = rightTop;
        }

        public List<ItemInfo> getBottomList() {
            return bottomList;
        }

        public void setBottomList(List<ItemInfo> bottomList) {
            this.bottomList = bottomList;
        }
    }


    public CardStructure getCardList() {
        return cardList;
    }

    public void setCardList(CardStructure cardList) {
        this.cardList = cardList;
    }

    public MediaInfo getCoreBackground() {
        return coreBackground;
    }

    public void setCoreBackground(MediaInfo coreBackground) {
        this.coreBackground = coreBackground;
    }

    public MediaInfo getBackground() {
        return background;
    }

    public void setBackground(MediaInfo background) {
        this.background = background;
    }

    public Map<String,String> getCoreSubStatData() {
        return coreSubStatData;
    }

    public void setCoreSubStatData(Map<String,String> coreSubStatData) {
        this.coreSubStatData = coreSubStatData;
    }

    public List<ItemInfo> getCoreMainData() {
        return coreMainData;
    }

    public void setCoreMainData(List<ItemInfo> coreMainData) {
        this.coreMainData = coreMainData;
    }

    public List<ItemInfo> getContent() {
        return content;
    }

    public void setContent(List<ItemInfo> content) {
        this.content = content;
    }

    public List<ItemInfo> getHeader() {
        return header;
    }

    public void setHeader(List<ItemInfo> header) {
        this.header = header;
    }
}
