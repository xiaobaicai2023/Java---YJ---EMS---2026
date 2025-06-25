import { acceptHMRUpdate, defineStore } from "pinia";
import { dictState } from "./type";
import { toRaw } from "vue";
const useDictStore = defineStore("dict", {
  persist: false,
  state: (): dictState => ({
    dict: new Array(),
  }),
  actions: {
    getDict(_key: string) {
      if (_key == null && _key == "") {
        return null;
      }
      try {
        for (let i = 0; i < this.dict.length; i++) {
          if (this.dict[i].key == _key) {
            return this.dict[i].value;
          }
        }
      } catch (e) {
        return null;
      }
    },
    // 设置字典
    setDict(_key: string, value: any) {
      if (_key !== null && _key !== "") {
        this.dict.push({
					key: _key,
					value: toRaw(value)
				});
      }
    },
    // 删除字典
    removeDict(_key: string) {
      var bln = false;
      try {
        for (let i = 0; i < this.dict.length; i++) {
          if (this.dict[i].key == _key) {
            this.dict.splice(i, 1);
            return true;
          }
        }
      } catch (e) {
        bln = false;
      }
      return bln;
    },
    // 清空字典
    cleanDict() {
      this.$reset()
    },
    // 初始字典
    initDict() {},
  },
});
// 确保传递正确的 store 声明
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useDictStore, import.meta.hot))
}
export default useDictStore;
