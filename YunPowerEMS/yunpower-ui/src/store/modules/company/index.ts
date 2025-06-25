import { defineStore } from "pinia";
import { companyState, companyType } from "./type";
import { getStationList, getStation, setStation } from "@/api/public";
import { findById, processSelectableByCompany } from "@/utils/ruoyi";

const useCompany = defineStore("company", {
  persist: true,
  state: (): companyState => ({
    // 公司列表
    companyList: [],
    companyValue: "",
    // 选择的公司
    selectCompany: {},
  }),
  actions: {
    // 设置公司列表
    async setCompanyList() {
      let res = await getStationList({});
      if (res.code == 200 && res.data && res.data.length > 0) {
        processSelectableByCompany(res.data);
        this.companyList = res.data;
        //获取选中的站点
        let selectRes = await getStation();
        if (selectRes.code == 200) {
          this.companyValue = selectRes.data;
          this.selectCompany = findById(res.data, selectRes.data);
        }
      }else{
        this.companyList = [];
      }
    },
    async updateCompanyValue(data: companyType) {
      let res = await setStation(data.id);
      if(res.code == 200){
        this.companyValue = data.id;
        this.updateSelectCompany(data)
      }
      return res;
    },
    // 修改已选择的公司
    updateSelectCompany(data: companyType) {
      this.selectCompany = data;
    },
  },
  getters: {
    getCompanyValue(): string | number {
      return this.companyValue || 0;
    },
    // 获取选择的公司
    getSelectCompany(): companyType {
      return this.selectCompany;
    },
  },
});

export default useCompany;
