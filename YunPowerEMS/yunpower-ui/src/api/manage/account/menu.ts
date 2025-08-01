import axios from "axios";
import qs from "qs";

/**
 * 获取菜单列表
 * @param params 搜索参数
 * @returns
 */
export function listMenu(params: any) {
  return axios.get("/system/menu/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

// 查询菜单详细
export function getMenu(menuId:any) {
  return axios.get('/system/menu/' + menuId);
}

/**
 * 新增菜单
 * @param data 
 * @returns 
 */
export function addMenu(data:any) {
  return axios.post("/system/menu",data);
}

/**
 * 修改菜单
 * @param data 菜单数据
 * @returns 
 */
export function updateMenu(data:any) {
  return axios.put("/system/menu",data);
}

/**
 * 删除菜单
 * @param menuId 菜单ID
 * @returns 
 */
export function delMenu(menuId:any) {
  return axios.delete('/system/menu/' + menuId);
}

/**
 * 修改菜单
 * @param data 公司对象
 * @returns 0失败 1成功
 */
export function changeMenuStatus(id: any,state:any) {
  return axios.put(`/system/menu/changeStatus/${id}/${state}`);
}


/**
 * 获取树形菜单
 * @param params 搜索参数
 * @returns
 */
export function treeselect() {
  return axios.get("/system/menu/treeselect");
}


/**
 * 获取树形菜单
 * @param params 搜索参数
 * @returns
 */
export function roleMenuTreeselect(id:any) {
  return axios.get("/system/menu/roleMenuTreeselect/" + id);
}
