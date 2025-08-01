package com.yunpower.system.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.yunpower.system.domain.SysCommonMenu;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.api.domain.SysGroup;

/**
 * Treeselect树结构实体类
 *
 * @author yunpower
 */
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() {

    }

    public TreeSelect(SysGroup group) {
        this.id = group.getId();
        this.label = group.getGroupName();
        this.children = group.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(SysDept dept) {
        this.id = dept.getId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(SysCommonMenu menu) {
        this.id = menu.getId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelect> children) {
        this.children = children;
    }
}
