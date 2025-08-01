import axios from 'axios';
import qs from "qs";

// 获取个人信息
export function getUserProfile(data: any) {
  return axios.get('/system/user/profile', data)
}

// 修改用户个人信息
export function updateUserProfile(data: any) {
  return axios.put('/system/user/profile', data)
}

// 用户密码重置
export function updateUserPwd(data: any) {
  return axios.put(`/system/user/profile/updatePwd`, qs.stringify(data));
}

