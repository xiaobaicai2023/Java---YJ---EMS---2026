import {defineStore} from "pinia";
import {
    login as userLogin,
    logout as userLogout,
    getUserInfo,
    LoginData,
} from "@/api/user";
import {setToken, clearToken, getToken} from "@/utils/auth";
import {removeRouteListener} from "@/utils/route-listener";
import {UserInfo, UserState} from "./types";
import useAppStore from "../app";
import useDictStore from "../dict";
import {Message} from "@arco-design/web-vue";
import { useCompanyStore } from '@/store'

const useUserStore = defineStore("user", {
    state: (): UserState => ({
        token: getToken() || "",
        user: {} as UserInfo,
        roles: [],
        permissions: [],
        initPassword: false,
    }),

    getters: {
        userInfo(state: UserState): UserState {
            return {...state};
        },
    },

    actions: {
        switchRoles() {
            return new Promise((resolve) => {
                resolve(null);
            });
        },
        setInfo(partial: Partial<UserState>) {
            this.$patch(partial);
        },
        // Reset user's information
        resetInfo() {
            this.$reset();
        },
        // Update app settings
        setState(partial: Partial<UserState>) {
            this.$patch(partial);
        },
        // Get user's information
        async info() {
            const res: any = await getUserInfo();
            const initLogin = Number(localStorage.getItem('initLogin')) || 0;
            if (res.code == 200) {
                this.setInfo({
                    permissions: res.permissions,
                    roles: res.roles,
                    user: res.user,
                    initPassword:res.initPassword
                });
            } else {
                await this.logout();
            }
        },

        // Login
        async login(loginForm: LoginData) {
            try {
                const res: any = await userLogin(loginForm);
                if (res.code != 200) {
                    Message.error({
                        content: res.msg,
                        duration: 5 * 1000,
                    })
                    throw new Error(res.msg);
                } else {
                    setToken(res.data.access_token);
                    const companyStore = useCompanyStore();
                    // 设置公司列表
                    await companyStore.setCompanyList();
                }
            } catch (err) {
                clearToken();
                throw err;
            }
        },
        logoutCallBack() {
            const appStore = useAppStore();
            this.resetInfo();
            clearToken();
            removeRouteListener();
            appStore.clearServerMenu();
            const dictStore = useDictStore();
            dictStore.cleanDict();
        },
        // Logout
        async logout() {
            try {
                let res = await userLogout();
            } finally {
                this.logoutCallBack();
            }
        },
    },
});

export default useUserStore;
