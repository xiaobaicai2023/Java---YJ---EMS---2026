import {blobValidate, tansParams} from '@/utils/ruoyi';
import {ElMapExportTable} from "table-excel";
import {Message} from '@arco-design/web-vue';
import {saveAs} from 'file-saver'
import axios from 'axios'
import errorCode from '@/utils/errorCode';
import dayjs from "dayjs";

// 通用下载方法
export function download(url: any, params: any, filename: any) {
	Message.loading({
		content: "正在下载数据，请稍候",
		duration: 6000 * 1000
	});
	return axios.post(url, params, {
		transformRequest: [(params) => {
			return tansParams(params)
		}],
		headers: {'Content-Type': 'application/x-www-form-urlencoded'},
		responseType: 'blob',
	}).then(async (data: any) => {
		const isBlob = await blobValidate(data);
		if (isBlob) {
			Message.clear();
			Message.success("导出成功");
			const blob = new Blob([data])
			saveAs(blob, filename)
		} else {
			const resText = await data.text();
			const rspObj: any = JSON.parse(resText);
			const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
			Message.error(errMsg);
		}

	}).catch((r) => {
		Message.clear();
		console.error(r)
		Message.error('下载文件出现错误，请联系管理员！')
	})
}

interface ITable {
	column: Array<any>,
	data: Array<any>
}

/**
 *  表格文件下载-纯前端
 * @param tableInfo 表格文件
 * @param xlsxName 文件民名称
 * @return Boolean
 */
export async function tableExport(tableInfo: ITable, xlsxName: string) {
	Message.loading({
		content: "正在下载数据，请稍候",
		duration: 6000 * 1000
	});
	try {
		await new ElMapExportTable(tableInfo).download(`${xlsxName} ${dayjs().format('YYYY-MM-DD HH:mm:ss')}`);
		Message.clear();
		Message.success("导出成功");
	} catch (error) {
		Message.clear();
		Message.error("导出失败");
	}
}
