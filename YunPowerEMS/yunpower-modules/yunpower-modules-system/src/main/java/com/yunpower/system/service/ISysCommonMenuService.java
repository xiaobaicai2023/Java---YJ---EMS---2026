package com.yunpower.system.service;

import java.util.List;
import java.util.Set;

import com.yunpower.system.domain.AlarmTriggerConfig;
import com.yunpower.system.domain.SysCommonMenu;
import com.yunpower.system.domain.vo.EnergyRouterVo;
import com.yunpower.system.domain.vo.RouterVo;
import com.yunpower.system.domain.vo.TreeSelect;

/**
 * 菜单 业务层
 *
 * @author yunpower
 */
public interface ISysCommonMenuService {
    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectMenuList(Long userId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu   菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectMenuList(SysCommonMenu menu, Long userId);

    /**
     * 根据用户查询【企业专属】系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectEnterpriseMenuList(Long userId);

    /**
     * 根据用户查询【企业专属】系统菜单列表
     *
     * @param menu   菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectEnterpriseMenuList(SysCommonMenu menu, Long userId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectMenuTreeByUserId(Long userId, Integer menuModel);

    /**
     * 根据用户ID查询已选择的模块
     *
     * @param userId 用户ID
     * @return 模块列表
     */
    public List<Integer> selectMenuModelByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<Long> selectMenuListByRoleId(Long roleId);

    ////**
    /// * 根据角色ID查询【企业专属】菜单树信息 - 保留
    /// *
    /// * @param roleId 角色ID
    /// * @return 选中菜单列表
    /// */
    ///public List<Long> selectEnterprisesMenuListByRoleId(Long roleId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysCommonMenu> menus);

    /**
     * 构建前端路由所需要的菜单
     * 新前端与若依前端结构不一样，因此重做一个
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<EnergyRouterVo> buildEnergyMenus(List<SysCommonMenu> menus, String parentName);

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysCommonMenu> buildMenuTree(List<SysCommonMenu> menus);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysCommonMenu> menus);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    public SysCommonMenu selectMenuById(Long menuId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkMenuExistRole(Long menuId);

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int insertMenu(SysCommonMenu menu);

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int updateMenu(SysCommonMenu menu);

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int deleteMenuById(Long menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean checkMenuNameUnique(SysCommonMenu menu);


    /**
     * 修改菜单状态
     *
     * @param id    菜单主键
     * @param state 状态
     * @return 结果
     */
    public int updateMenuState(SysCommonMenu menu, Long id, Integer state);
}
