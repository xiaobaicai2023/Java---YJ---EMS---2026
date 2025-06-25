import imgUrl_one from '@/assets/bi/template-old.png';
import imgUrl_two from '@/assets/bi/template-new.png';

/**
 * 统计数据下拉框
 */
const coreSubStatOption = [
	{
		id: 1,
		label: "年发电",
		key: "year_pv_power",
		value: false,
	},
	{
		id: 2,
		label: "年储能",
		key: "year_storage_power",
		value: false,
	},
	{
		id: 3,
		label: "年充电",
		key: "year_charge_power",
		value: false,
	}]
/**
 * 模板
 */
const previewList = [{
	id: '1',
	imgUrl: imgUrl_one,
	name: '默认模板'
}, {
	id: '2',
	imgUrl: imgUrl_two,
	name: '电力科技'
}];
// 筛选出单选框和复选框的初始值
const reduceArr = (arr: Array<any>) => {
	return arr.reduce((accumulator: Array<string>, item: any) => {
		if (item.value) {
			accumulator.push(item.key);
		}
		return accumulator;
	}, [])
};
export {
	previewList,
	coreSubStatOption,
	reduceArr
}
