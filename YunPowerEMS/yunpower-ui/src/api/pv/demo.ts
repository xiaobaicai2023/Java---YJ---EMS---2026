import axios from "axios";
import qs from "qs";

/**
 * 用户对象
 */
export interface UserEntity {
  //主键
  id: number;
  //昵称
  nickName: string;
  //姓名
  trueName: string;
  //电话
  phone: string;
  //注册时间
  registerTime: string;
  //用户状态
  status: number;
  //地址
  address: string;
}

/**
 * 用户查询对象
 */
export interface UserQueryParams extends Partial<UserEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页对象
 */
export interface UserListPage {
  list: UserEntity[];
  total: number;
}

//获取用户列表
export function queryUserList(params: UserQueryParams): any {
  return axios.get<UserListPage>("/manage/user/", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
