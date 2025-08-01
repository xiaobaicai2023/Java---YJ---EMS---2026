import axios from "axios";
import qs from "qs";

/**
 * 查询岗位列表
 * @param params 查询参数
 * @returns 
 */
export function listPost(params:any) {
  return axios.get("/system/post/list", {
		params,
		paramsSerializer: (obj) => {
			return qs.stringify(obj);
		},
	});
}

/**
 * 查询岗位列表
 * @param params 查询参数
 * @returns 
 */
export function listPostAll(params:any) {
  return axios.get("/system/post/listAll", {
		params,
		paramsSerializer: (obj) => {
			return qs.stringify(obj);
		},
	});
}

/**
 * 查询岗位详细
 * @param postId 岗位ID
 * @returns 
 */
export function getPost(postId:any) {
	return axios.get('/system/post/' + postId);
}

/**
 * 新增岗位
 * @param data 岗位信息
 * @returns 
 */
export function addPost(data:any) {
	return axios.post('/system/post',data);
}

/**
 * 修改岗位
 * @param data 岗位信息
 * @returns 
 */
export function updatePost(data:any) {
	return axios.put('/system/post',data);
}

/**
 * 删除岗位
 * @param postId 岗位ID
 * @returns 
 */
export function delPost(postId:any) {
	return axios.delete('/system/post/' + postId);
}


