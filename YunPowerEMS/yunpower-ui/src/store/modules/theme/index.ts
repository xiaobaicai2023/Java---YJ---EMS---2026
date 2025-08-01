import type { RouteLocationNormalized } from 'vue-router'
import { defineStore } from 'pinia'

const useThemeStore = defineStore('theme', {
  persist: true,
  state: () => ({
    themePackage:{}
  }),

  getters: {
    getThemePackage():any {
      return this.themePackage
    }
  },

  actions: {
    updateThemePackage(themePackage: any) {
      this.themePackage = themePackage
    },
    resetThemePackage() {
      this.$reset()
    }
  },
})

export default useThemeStore
