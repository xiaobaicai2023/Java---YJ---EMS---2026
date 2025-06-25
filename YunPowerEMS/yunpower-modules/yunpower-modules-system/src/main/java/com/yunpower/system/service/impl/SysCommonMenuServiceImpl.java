package com.yunpower.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.yunpower.system.domain.vo.*;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.system.mapper.SysCommonMenuMapper;
import com.yunpower.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.common.core.constant.Constants;
import com.yunpower.common.core.constant.UserConstants;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.SysRole;
import com.yunpower.system.api.domain.SysUser;
import com.yunpower.system.domain.SysCommonMenu;
import com.yunpower.system.mapper.SysRoleMapper;
import com.yunpower.system.mapper.SysRoleMenuMapper;
import com.yunpower.system.service.ISysCommonMenuService;


/**
 * 菜单 业务层处理
 *
 * @author yunpower
 */
@Service
public class SysCommonMenuServiceImpl implements ISysCommonMenuService {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private SysCommonMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private PublicServiceImpl publicService;

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysCommonMenu> selectMenuList(Long userId) {
        return selectMenuList(new SysCommonMenu(), userId);
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysCommonMenu> selectMenuList(SysCommonMenu menu, Long userId) {
        List<SysCommonMenu> menuList = null;

        SysUser sysUser = userMapper.selectUserById(userId);

        // 管理员显示所有菜单信息
        if (sysUser != null && sysUser.getIsSupper() == 1) {
            menuList = menuMapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 根据用户查询【企业专属】系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysCommonMenu> selectEnterpriseMenuList(Long userId) {
        return selectEnterpriseMenuList(new SysCommonMenu(), userId);
    }

    /**
     * 查询【企业专属】菜单列表
     *
     * @param menu   菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysCommonMenu> selectEnterpriseMenuList(SysCommonMenu menu, Long userId) {
        Long entId = publicService.getCurrentEnterprise();

        List<SysCommonMenu> menuList = null;

        SysUser sysUser = userMapper.selectUserById(userId);

        // 管理员显示所有菜单信息
        if (sysUser != null && sysUser.getIsSupper() == 1) {
            menuList = menuMapper.selectEnterpriseMenuList(entId);
        } else {
            menu.getParams().put("entId", entId);
            menu.getParams().put("userId", userId);
            ///menuList = menuMapper.selectEnterprisesMenuListByUserId(menu);
            // 可以共用：既然给用户分配了权限，那么一定是企业菜单范围内的
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        List<String> perms = menuMapper.selectMenuPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysCommonMenu> selectMenuTreeByUserId(Long userId) {
        List<SysCommonMenu> menus = null;

        SysUser sysUser = userMapper.selectUserById(userId);

        if (sysUser != null && SecurityUtils.isAdmin(sysUser.getIsSupper())) {
            menus = menuMapper.selectMenuTreeAll(null);
        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId, null);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysCommonMenu> selectMenuTreeByUserId(Long userId, Integer menuModel) {
        List<SysCommonMenu> menus = null;
        SysUser sysUser = userMapper.selectUserById(userId);

        if (sysUser == null) {
            return null;
        }
        //如果用户是超管或者角色里拥有全部菜单权限 则返回所有菜单
        if(SecurityUtils.isAdmin(sysUser.getIsSupper()) || hasAllMenu(sysUser.getId())){
             menus = menuMapper.selectMenuTreeAll(menuModel);
        }else{
            menus = menuMapper.selectMenuTreeByUserId(userId, menuModel);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 检查用户是否有全部菜单权限
     * */
    private boolean hasAllMenu(Long userId){
        //检查用户角色
        List<SysRole> roleList = roleMapper.selectRolePermissionByUserId(userId);
        if(StringUtils.isEmpty(roleList)){
            return false;
        }
        //检查是否有 超管角色 和 全部菜单角色
        //menuScope 菜单范围（1全部菜单 2仅浏览 3自定义菜单）
        Integer menuScopeAll = 1;
        String roleKey ="admin";
        return roleList.stream().anyMatch(item -> menuScopeAll.equals(item.getMenuScope()) || roleKey.equals(item.getRoleKey()));
    }

    /**
     * 根据用户ID查询已选择的模块
     *
     * @param userId 用户ID
     * @return 模块列表
     */
    @Override
    public List<Integer> selectMenuModelByUserId(Long userId) {
        return menuMapper.selectMenuModelByUserId(userId);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    ////**
    /// * 根据角色ID查询【企业专属】菜单树信息 - 保留
    /// *
    /// * @param roleId 角色ID
    /// * @return 选中菜单列表
    /// */
    ///@Override
    ///public List<Long> selectEnterprisesMenuListByRoleId(Long roleId) {
    ///    SysRole role = roleMapper.selectRoleById(roleId);
    ///    ///Long entId = publicService.getCurrentEnterprise();
    ///    ///return menuMapper.selectEnterprisesMenuListByRoleId(entId, roleId, role.isMenuCheckStrictly());
    ///    // 既然分配了角色，那么一定是在企业菜单范围内
    ///    return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    ///}

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysCommonMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysCommonMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(1 == menu.getVisible());
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getRequestQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath(), router.getHidden()));
            List<SysCommonMenu> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getRequestUrl());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath(), router.getHidden()));
                children.setQuery(menu.getRequestQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端路由所需要的菜单
     * 新前端与若依前端结构不一样，因此重做一个
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<EnergyRouterVo> buildEnergyMenus(List<SysCommonMenu> menus, String parentName) {
        List<EnergyRouterVo> routers = new LinkedList<EnergyRouterVo>();
        for (SysCommonMenu menu : menus) {
            EnergyRouterVo router = new EnergyRouterVo();
            router.setName(getEnergyRouteName(menu, parentName));
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getRequestUrl());
            router.setProps(menu.getRequestQuery());
            router.setMeta(new EnergyMetaVo(menu.getMenuName(), 0 == menu.getIsAdminVisit(), menu.getIcon(), menu.getOrderNum(), 1 == menu.getVisible()));

            // 子菜单
            List<SysCommonMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                // 如果是目录
                router.setChildren(buildEnergyMenus(cMenus, router.getName()));
            } else if (isMenuFrame(menu)) {
                // 如果是菜单
                router.setMeta(null);

                List<EnergyRouterVo> childrenList = new ArrayList<EnergyRouterVo>();
                EnergyRouterVo children = new EnergyRouterVo();
                children.setName(getEnergyRouteName(menu, router.getName()));
                children.setPath(menu.getPath());
                children.setComponent(menu.getRequestUrl());
                children.setProps(menu.getRequestQuery());
                children.setMeta(new EnergyMetaVo(menu.getMenuName(), 0 == menu.getIsAdminVisit(), menu.getIcon(), menu.getOrderNum(), 1 == menu.getVisible()));
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                // 如果是内链
                router.setPath("/");
                router.setComponent(menu.getRequestUrl());
                router.setProps(menu.getRequestQuery());
                router.setMeta(new EnergyMetaVo(menu.getMenuName(), 0 == menu.getIsAdminVisit(), menu.getIcon(), menu.getOrderNum(), 1 == menu.getVisible()));

                List<EnergyRouterVo> childrenList = new ArrayList<EnergyRouterVo>();
                EnergyRouterVo children = new EnergyRouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setName(StringUtils.capitalize(routerPath));
                children.setPath(routerPath);
                children.setComponent(menu.getRequestUrl());
                children.setProps(menu.getRequestQuery());
                children.setMeta(new EnergyMetaVo(menu.getMenuName(), 0 == menu.getIsAdminVisit(), menu.getIcon(), menu.getOrderNum(), 1 == menu.getVisible()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysCommonMenu> buildMenuTree(List<SysCommonMenu> menus) {
        List<SysCommonMenu> returnList = new ArrayList<SysCommonMenu>();
        List<Long> tempList = menus.stream().map(SysCommonMenu::getId).collect(Collectors.toList());
        for (Iterator<SysCommonMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysCommonMenu menu = (SysCommonMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysCommonMenu> menus) {
        List<SysCommonMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysCommonMenu selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysCommonMenu menu) {
        menu.setCreateBy(SecurityUtils.getNickName());
        menu.setCreateTime(DateUtils.getNowDate());
        if (menu.getStopFlag() == null) {
            menu.setStopFlag(0);
        }
        menu.setDeleteFlag(0);

        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysCommonMenu menu) {
        menu.setUpdateBy(SecurityUtils.getNickName());
        menu.setUpdateTime(DateUtils.getNowDate());
        return menuMapper.updateMenu(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean checkMenuNameUnique(SysCommonMenu menu) {
        Long menuId = StringUtils.isNull(menu.getId()) ? -1L : menu.getId();
        SysCommonMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != menuId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysCommonMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由名称（新能源）
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getEnergyRouteName(SysCommonMenu menu, String parentName) {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            return StringUtils.EMPTY;
        }

        String orginPath = innerLinkReplaceEach(menu.getPath());
        String[] pathArr = orginPath.split("/");
        StringBuilder routerName = new StringBuilder();
        if (StringUtils.isNotEmpty(parentName)) {
            routerName.append(parentName);
        }
        for (String arr : pathArr) {
            if (StringUtils.isNotEmpty(arr)) {
                if (routerName.toString().equals("")) {
                    routerName.append(StringUtils.uncapitalize(arr));
                } else {
                    routerName.append(StringUtils.capitalize(arr));
                }
            }
        }
        return routerName.toString();
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysCommonMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue()
                && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getOpenType())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysCommonMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getRequestUrl()) && !isMenuFrame(menu)) {
            component = menu.getRequestUrl();
        } else if (StringUtils.isEmpty(menu.getRequestUrl()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getRequestUrl()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysCommonMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getOpenType().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysCommonMenu menu) {
        return menu.getOpenType().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysCommonMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysCommonMenu> getChildPerms(List<SysCommonMenu> list, int parentId) {
        List<SysCommonMenu> returnList = new ArrayList<SysCommonMenu>();
        for (Iterator<SysCommonMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysCommonMenu t = (SysCommonMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<SysCommonMenu> list, SysCommonMenu t) {
        // 得到子节点列表
        List<SysCommonMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysCommonMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysCommonMenu> getChildList(List<SysCommonMenu> list, SysCommonMenu t) {
        List<SysCommonMenu> tlist = new ArrayList<SysCommonMenu>();
        Iterator<SysCommonMenu> it = list.iterator();
        while (it.hasNext()) {
            SysCommonMenu n = (SysCommonMenu) it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysCommonMenu> list, SysCommonMenu t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"}, new String[]{"", "", "", "/", "/"});
    }


    /**
     * 修改菜单状态
     *
     * @param id    菜单主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateMenuState(SysCommonMenu menu, Long id, Integer state){
        menu.setId(id);
        menu.setStopFlag(state);
        menu.setUpdateBy(SecurityUtils.getNickName());
        menu.setUpdateTime(DateUtils.getNowDate());
        return menuMapper.updateMenu(menu);
    }
}
