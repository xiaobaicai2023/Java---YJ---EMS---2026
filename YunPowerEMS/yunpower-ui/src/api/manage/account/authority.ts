import axios from "axios";
import qs from "qs";

/**
 * 获取站点
 * @param params 查询参数
 * @returns 站点列表
 */
export function listDept(params: any): any {
  return axios.get("/system/dept/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}


/**
 * 获取站点树
 * @param params 查询参数
 * @returns 站点列表树结构
 */
export function deptTree(id:any){
  if(id && id > 0){
    return axios.get(`system/role/deptTree/${id}`);
  }else{
    return axios.get(`system/role/deptTree`);
  }
}


/**
 * 获取站点下的成员
 * @param deptId 
 * @returns 
 */
export function getDeptMember(deptId:number) {
  return axios.get(`/system/dept/list/exclude/${deptId}`)
}
