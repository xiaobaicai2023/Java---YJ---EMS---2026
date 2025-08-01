export interface companyType {
  id?: string | number;
  entId?: number;
  parentId?: number;
  originType?: number;
  correId?: number;
  ancestors?: string;
  isCanSelect?: number;
  deptName?: string;
  deptSn?: string;
  leader?: string;
  mobile?: string;
  email?: string;
  orderNum?: number;
  stopFlag?: number;
  deleteFlag?: number;
  children?: companyType[];
}
// export interface companyListType {
//
// }
export interface companyState {
  companyList: Array<companyType>;
  companyValue: string | number | undefined;
  selectCompany: companyType;
}
