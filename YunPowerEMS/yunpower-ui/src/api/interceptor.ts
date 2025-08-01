import {useUserStore} from '@/store';
import {getToken, getTokenHandler} from '@/utils/auth';
import {Message, Modal} from '@arco-design/web-vue';
import type {AxiosRequestConfig, AxiosResponse} from 'axios';
import cache from "@/utils/cache";
import axios from 'axios';

// 是否显示重新登录
let isRelogin = {show: false};

// 拦截器
export interface HttpResponse<T = unknown> {
	status: number;
	msg: string;
	code: number;
	data: T;
}

if (import.meta.env.VITE_API_BASE_URL) {
	axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL;
}

axios.interceptors.request.use(
	(config: AxiosRequestConfig) => {
		// 在拦截器中添加Token信息
		const tokenHandler = getTokenHandler();
		const token = getToken();
		if (token && tokenHandler) {
			if (!config.headers) {
				config.headers = {};
			}
			config.headers[tokenHandler] = `Bearer ${token}`;
		}
		//30秒超时
		config.timeout = 30 * 1000;
		config.timeoutErrorMessage = '接口超时';
		if (config.method === 'post' || config.method === 'put'||config.method === 'delete') {
			const requestObj = {
				url: config.url,
				data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
				time: new Date().getTime()
			}
			const sessionObj = cache.session.getJSON('sessionObj')
			if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
				cache.session.setJSON('sessionObj', requestObj)
			} else {
				const s_url = sessionObj.url;                // 请求地址
				const s_data = sessionObj.data;              // 请求数据
				const s_time = sessionObj.time;              // 请求时间
				const interval = 1000;                       // 间隔时间(ms)，小于此时间视为重复提交
				if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
					const message = '数据正在处理，请勿重复提交';
					console.error(`[${s_url}]: ` + message)
					return Promise.reject(new Error(message))
				} else {
					cache.session.setJSON('sessionObj', requestObj)
				}
			}
		}
		return config;
	},
	(error) => {
		// do something
		return Promise.reject(error);
	}
);
// add response interceptors
axios.interceptors.response.use(
	(response: AxiosResponse<any>): Promise<AxiosResponse<any>> => {
		const whiteUrlList = ['https://arco.design/themes/api/open/themes/list'];
		if (['blob', 'arraybuffer'].includes(response.config.responseType as string) || whiteUrlList.includes(response.config.url as string)) {
			return response.data;
		}
		const {code, msg} = response.data;
		const codeNumber = Number(code);
		if (codeNumber !== 200) {
			if (codeNumber == 401) {
				//检查是否已经弹出提示框
				if (!isRelogin.show) {
					isRelogin.show = true;
					Modal.error({
						title: '系统提示',
						content: '登录状态已过期，您可以继续留在该页面，或者重新登录',
						okText: '重新登录',
						maskClosable: false,
						async onOk() {
							const userStore = useUserStore();
							await userStore.logout();
							window.location.reload();
						},
					});
				}
			}
		}
		return response.data;
	},
	(error) => {
		Message.error({
			content: error.msg || '后端接口异常',
			duration: 5 * 1000,
		});
		return Promise.reject(error);
	}
);
