import { Notification } from '@arco-design/web-vue';
import {useThemeStore} from "@/store";
const themeStore = useThemeStore()
/**
 * 修改主题包
 * @param data 主题参数
 */
export const updateLinkTag = function (data: any): Promise<boolean> {
	const cssUrl = `${data.unpkgHost}${data.packageName}/css/arco.css`;
	return new Promise((resolve, reject) => {
		// 删除旧的 link 标签
		let oldLinkTag = document.getElementById('pro-custom-theme') as HTMLLinkElement | null;
		if (oldLinkTag) {
			oldLinkTag.parentNode?.removeChild(oldLinkTag);
		}

		// 创建新的 link 标签
		const linkTag = document.createElement('link');
		linkTag.rel = 'stylesheet';
		linkTag.id = 'pro-custom-theme';
		linkTag.type = 'text/css';
		linkTag.href = data.unpkgHost ? cssUrl : "";

		// 监测下载完成
		linkTag.onload = () => {
			themeStore.updateThemePackage(data);
			resolve(true);
		};

		// 监测下载失败
		linkTag.onerror = () => {
			console.log('下载失败');
			reject(false);
		};

		// 将新的 link 标签添加到 body 标签中
		document.body.appendChild(linkTag);
	});
}
/**
 * 切换主题包
 * @param themesData 主题参数
 */
export const changeTheme = async function (themesData: any): Promise<boolean> {
	// 显示切换主题的通知
	Notification.info({
		title: '正在切换主题',
		content: '',
		id: 'your_id',
	});

	try {
		const result = await updateLinkTag(themesData);
		Notification.success({
			id: 'your_id',
			title: '安装主题',
			content: '主题安装成功',
		});
		return result;
	} catch (error) {
		Notification.error({
			id: 'your_id',
			title: '安装主题',
			content: '主题安装失败，请稍候重试',
		});
		return false;
	}
};

export const resetTheme = function () {
	const localTheme = themeStore.themePackage;
	if (localTheme) {
		let oldLinkTag = document.getElementById('pro-custom-theme') as HTMLLinkElement | null;
		if (oldLinkTag) {
			oldLinkTag.parentNode?.removeChild(oldLinkTag);
			themeStore.resetThemePackage();
		}
	}
}
