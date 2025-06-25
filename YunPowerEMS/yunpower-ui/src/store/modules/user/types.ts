export type RoleType = "" | "*" | "admin" | "user";

export interface UserInfo {
  createBy: string;
  createTime: string;
  updateBy: string;
  updateTime: string;
  remark: string;
  id: number;
  entId: number;
  deptId: number;
  logicCode: string;
  logonName: string;
  logonPwd: string;
  nickName: string;
  mobile: string;
  email: string;
  postId: number;
  isSupper: number;
  userLevel: number;
  sex: number;
  marry: string;
  birthday: string;
  age: string;
  trueName: string;
  cardType: string;
  cardNumber: string;
  cardPicFront: string;
  cardPicBack: string;
  isAuth: string;
  country: string;
  province: string;
  city: string;
  county: string;
  town: string;
  village: string;
  detailAddress: string;
  postCode: string;
  phone: string;
  fax: string;
  headPic: string;
  personDes: string;
  regTime: string;
  regIp: string;
  lastLoginTime: string;
  lastLoginIp: string;
  loginTimes: string;
  stopFlag: number;
  deleteFlag: number;
  roleIds: string;
  postIds: string;
  roleId: string;
  admin: boolean;
}

export interface UserState {
  token?: string | undefined;
  user: UserInfo;
  roles: string[];
  permissions: string[];
  initPassword: boolean,
}
