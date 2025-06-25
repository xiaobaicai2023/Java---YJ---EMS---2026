import axios from "axios";
import type { RouteRecordNormalized } from "vue-router";
import { UserState } from "@/store/modules/user/types";
import qs from "qs";

export interface LoginData {
  username: string;
  password: string;
  browserFlag: string;
}

export interface LoginRes {
  token: string;
}
export function login(data: LoginData) {
  return axios.post<LoginRes>("/auth/login", data);
}

export function getCaptcha(params: any) {
  return axios.get<any>("/code", {
    params,
  });
}

export function getUserInfo() {
  return axios.get<UserState>("/system/user/getInfo");
}

export function logout() {
  return axios.delete<LoginRes>("/auth/logout");
}

export function getMenuList() {
  return axios.get<RouteRecordNormalized[]>("/system/menu/getEnergyRouters");
}
// 获取主题库
export function getThemesList(params:any) {
  return axios.get<any>("https://arco.design/themes/api/open/themes/list",{
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
export function updatePwd(params: any) {
  return axios.put(`/system/user/profile/updatePwd?newPassword=${params.newPassword}&oldPassword=${params.oldPassword}`);
}
