package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.SysCommonMenu;
import org.apache.ibatis.annotations.Param;

/**
 * 菜单表 数据层
 *
 * @author yunpower
 */
public interface SysCommonMenuMapper {
    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectMenuList(SysCommonMenu menu);

    /**
     * 查询【企业专属】菜单列表
     *
     * @param entId 企业ID
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectEnterpriseMenuList(Long entId);

    /**
     * 根据用户所有权限
     *
     * @return 权限列表
     */
    public List<String> selectMenuPerms();

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectMenuListByUserId(SysCommonMenu menu);

    /**
     * 根据用户查询【企业专属】菜单列表 - 保留
     * @param menu 菜单信息
     * @return 菜单列表
     */
    ///public List<SysCommonMenu> selectEnterprisesMenuListByUserId(SysCommonMenu menu);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectMenuTreeAll(Integer menuModel);

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysCommonMenu> selectMenuTreeByUserId(@Param("userId") Long userId, @Param("menuModel") Integer menuModel);

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
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    public List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    /**
     * 根据角色ID查询【企业专属】菜单树信息 - 保留
     *
     * @param entId 企业ID
     * @param roleId 角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    ///public List<Long> selectEnterprisesMenuListByRoleId(@Param("entId") Long entId, @Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

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
     * @return 结果
     */
    public int hasChildByMenuId(Long menuId);

    /**
     * 新增菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int insertMenu(SysCommonMenu menu);

    /**
     * 修改菜单信息
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
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    public SysCommonMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
}
