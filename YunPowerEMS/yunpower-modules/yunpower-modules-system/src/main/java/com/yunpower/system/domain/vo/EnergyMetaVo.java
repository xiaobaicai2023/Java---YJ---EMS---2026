package com.yunpower.system.domain.vo;

/**
 * 路由显示信息
 * 新能源菜单返回的结果不一样，所以新建一个
 *
 * @author junfu.wang
 */
public class EnergyMetaVo {
    /**
     * 中文名称
     */
    private String locale;

    /**
     * 是否需要认证
     */
    private boolean requiresAuth;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 多个角色，用于切换
     */
    private String[] roles;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private boolean hideInMenu;

    public EnergyMetaVo() {
    }

    public EnergyMetaVo(String locale, boolean requiresAuth, String icon, Integer order, boolean hideInMenu) {
        this.locale = locale;
        this.requiresAuth = requiresAuth;
        this.icon = icon;
        this.order = order;
        this.hideInMenu = hideInMenu;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isRequiresAuth() {
        return requiresAuth;
    }

    public void setRequiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String[] getRoles() {
        return new String[]{"*"};
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean isHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }
}
