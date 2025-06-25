package com.yunpower.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 路由配置信息
 * 新能源菜单返回的结果不一样，所以新建一个
 * 
 * @author junfu.wang
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnergyRouterVo
{
    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 请求参数
     */
    private String props;

    /**
     * 其他元素
     */
    private EnergyMetaVo meta;

    /**
     * 子路由
     */
    private List<EnergyRouterVo> children;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public EnergyMetaVo getMeta()
    {
        return meta;
    }

    public void setMeta(EnergyMetaVo meta)
    {
        this.meta = meta;
    }

    public List<EnergyRouterVo> getChildren()
    {
        return children;
    }

    public void setChildren(List<EnergyRouterVo> children)
    {
        this.children = children;
    }
}
