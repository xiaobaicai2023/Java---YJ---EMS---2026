import pinia, {useAppStore} from '@/store';
import {ToolTipFormatterParams} from '@/types/echarts';
import * as echarts from 'echarts';
import {computed} from 'vue';

const appStore = useAppStore(pinia);
const isDark = computed(() => {
	return appStore.theme === 'dark';
});

//检查Y轴数据
function checkYAxis(param: any) {
	return param.yAxis && param.yAxis.length > 0;
}

/**
 * 检查Y轴单位
 * @param param 后端参数
 * @returns
 */
function checkYAxisUnit(param: any) {
	return param.yAxisUnit && param.yAxisUnit.length > 0;
}

/**
 * 处理Y轴单位
 * @param param
 */
function handleYAxis(param: any) {
	let yAxis: any = [];
	param.yAxisUnit.forEach((item: any, index: number) => {
		yAxis.push({
			type: 'value',
			name: item,
			position: index == 0 ? 'left' : 'right',
			alignTicks: true,
			axisLabel: {
				formatter: `{value}`,
			},
			splitLine: {
				lineStyle: {
					// color: isDark ? "#3F3F3F" : "#E5E6EB",
				},
			},
		});
	});
	return yAxis;
}

/**
 * 处理普通图表数据
 * @param param
 */
function handleNormalSeries(param: any) {
	let series: any = [];
	param.yAxis.forEach((item: any) => {
		series.push({
			name: item.name,
			type: item.chartType,
			stack: item.groupName,
			yAxisIndex: item.yAxisIndex || 0,
			barMaxWidth: item.chartType == 'bar' ? (item.dataList.length > 10 ? '100%' : '35') : null,
			smooth: true,
			emphasis: {
				focus: 'series',
			},
			tooltip: {
				valueFormatter: function (value: any) {
					return `${value} ${item.yAxisUnit}`;
				},
			},
			data: item.dataList,
		});
	});
	return series;
}

/**
 * 处理普通图表数据
 * @param param
 */
function handleUnit(param: any) {
	let unit: any = [];
	param.yAxis.forEach((item: any) => {
		if (item.yAxisUnit && item.yAxisUnit.length > 0) {
			unit.push(item.yAxisUnit);
		}
	});
	if (unit.length <= 0) {
		unit = param.dataUnit;
	}
	return unit;
}

/**
 * 处理堆叠图表
 * @param param
 */
const handleStackChart = (param: any) => {
	param.type = param.type || 'stack';
	return handleChartData(param);
};

/**
 * 处理普通图表
 * @param param
 */
const handleChartData = (param: any) => {
	//设置Y轴数据单位
	const dataUnit = checkYAxis(param) ? handleUnit(param) : [];
	console.log("dataUnit=>", dataUnit);
	return {
		color: param.colorList || ['#7BCFA3', '#F3B07E', '#015A45', '#D25F00', '#FFB65D', '#23C343', '#F53F3F', '#CB272D'],
		tooltip: {
			show: true,
			trigger: 'axis',
			formatter(params: any) {
				const [firstElement] = params;
				return `<div>
					<p class="tooltip-title">${firstElement.axisValueLabel}</p>
					${tooltipItemsHtmlString(params, dataUnit)}
				</div>`;
			},
			className: 'echarts-tooltip-diy',
		},
		legend: {left: 0, bottom: 0, type: 'scroll'},
		grid: {
			top: '12%',
			left: 20,
			bottom: 50,
			right: 20,
			containLabel: true,
		},
		dataZoom: [{type: 'inside'}, {backgroundColor: '#7BCFA3', height: 10, opacity: 0, moveHandleSize: 0}],
		xAxis: [
			{
				type: 'category',
				data: param.xAxis || [],
				axisLine: {lineStyle: {color: isDark ? '#3f3f3f' : '#A9AEB8'}},
				axisTick: {show: true, alignWithLabel: true, lineStyle: {color: '#86909C'}},
				axisLabel: {color: '#86909C'},
			},
		],
		yAxis: checkYAxisUnit(param) ? handleYAxis(param) : [{type: 'value'}],
		series: checkYAxis(param) ? handleNormalSeries(param) : [],
	};
};

/**
 * 颜色Map
 */
const colorMap = new Map([
	['峰', '#D25F00'],
	['平', '#A2DEC8'],
	['尖', '#015745'],
	['谷', '#177B60'],
	['深谷', '#68BDA1'],
]);
/**
 * 处理时段图表
 */
const handleSeasonalRangeChart = (param: any) => {
	let seriesData: any[] = [];
	let legend: any[] = [];

	Object.keys(param).forEach((key, index) => {
		legend.push(key);
		param[key].forEach((item: any) => {
			seriesData.push({
				name: key,
				value: [index, item.startTime, item.endTime],
				itemStyle: {
					color: colorMap.get(key),
				},
			});
		});
	});

	const renderCustomItem = (params: any, api: any) => {
		const categoryIndex = api.value(0);
		const start = api.coord([api.value(1), categoryIndex]);
		const end = api.coord([api.value(2), categoryIndex]);
		const rectShape = echarts.graphic.clipRectByRect(
			{
				x: start[0],
				y: start[1] - 5,
				width: end[0] - start[0],
				height: 10,
			},
			{
				x: params.coordSys.x,
				y: params.coordSys.y,
				width: params.coordSys.width,
				height: params.coordSys.height,
			}
		);

		return (
			rectShape && {
				type: 'rect',
				shape: rectShape,
				style: api.style(),
			}
		);
	};

	return {
		tooltip: {
			show: true,
			formatter: (params: any) => {
				return `
                    <div class="content-panel">
                        <p>
                            <span style="background-color: ${params.color}" class="tooltip-item-icon"></span>
                            <span>${params.name}</span>
                        </p>
                        <span class="tooltip-value">
                            ${param[params.data.name][0].chargePrice} 元
                        </span>
                    </div>`;
			},
			appendToBody: true,
			className: 'echarts-tooltip-diy'
		},
		legend: {
			data: legend,
			icon: 'roundRect',
			left: 0,
			bottom: 0,
			selectedMode: false
		},
		grid: {
			top: 10,
			left: 10,
			right: 10,
		},
		xAxis: [
			{
				type: 'value',
				axisLine: {
					show: true,
				},
				data: Array.from({length: 24}, (_, i) => i),
				splitNumber: 24,
			},
		],
		yAxis: {
			axisLine: {
				onZero: false,
				show: false,
			},
			data: [],
		},
		series: [
			{
				type: 'custom',
				renderItem: renderCustomItem,
				encode: {
					x: [1, 2],
					y: 0,
				},
				data: seriesData,
			},
			{
				show: false,
				type: 'radar',
				renderItem: renderCustomItem,
				encode: {
					x: [1, 2],
					y: 0,
				},
				data: seriesData,
			},
		],
	};
};


const tooltipItemsHtmlString = (items: ToolTipFormatterParams[], yAxisUnit: any[]) => {
	return items
		.map(
			(el) => `
			<div class="content-panel">
				<p>
					<span style="background-color: ${el.color}" class="tooltip-item-icon"></span>
					<span>${el.seriesName}</span>
				</p>
				<span class="tooltip-value">
				${Number(el.value ? el.value : 0).toLocaleString() + ' ' + yAxisUnit[el.componentIndex]}
				</span>
    </div>`
		)
		.join('');
};

/**
 * 处理普通图表
 */
const handleDataVChartData = () => {
	return {
		grid: {
			top: 4,
			left: 20,
			right: 0,
			bottom: '2%',
			containLabel: true,
		},
		backgroundColor: 'rgba(0, 0, 0, 0)',
		xAxis: {
			type: 'category',
			data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
			axisTick: {
				show: false,
			},
			axisLine: {
				show: true,
				lineStyle: {
					color: '#FFF',
					width: 3,
				},
			},
			axisLabel: {
				color: '#ffffff',
				fontWeight: 'bold',
				fontSize: 10,
			},
		},
		yAxis: {
			axisLine: {
				show: false,
			},
			axisTick: {
				show: false,
			},
			axisLabel: {
				show: false,
			},
			splitLine: {
				show: false,
			},
		},
		series: [
			{
				data: [50, 30, 30, 90, 40, 10, 50, 80, 90, 80, 50, 30],
				type: 'bar',
				itemStyle: {
					color: function (params: any) {
						// 根据数据索引设置不同的颜色
						const colorList = ['#1acfff', '#ffffff'];
						return colorList[params.dataIndex < 6 ? 0 : 1];
					},
				},
				barMaxWidth: '30%',
			},
		],
	};
};

function getHoursSinceMidnightWithSeconds() {
	const now = new Date(); // 获取当前时间
	const hours = [];
	for (let hour = 0; hour <= now.getHours(); hour++) {
		hours.push(hour);
	}
	return hours;
}

/**
 * K线
 * @returns
 */
const handleDataVChartCandlestickData = () => {
	const upColor = '#ec0000';
	const upBorderColor = '#8A0000';
	const downColor = '#00da3c';
	const downBorderColor = '#008F28';
	let hours = getHoursSinceMidnightWithSeconds();
	let candlestick: any = [];
	var prevClose = 1000;
	hours.forEach((item) => {
		var open = prevClose * (Math.random() * 0.8 - 0.4 + 1); // 控制在 -40% 到 +40% 波动
		var close = open * (Math.random() * 0.8 - 0.4 + 1); // 控制在 -40% 到 +40% 波动
		var high = Math.max(open, close) * (Math.random() * 0.4 + 1.2); // 控制在 +60% 波动
		var low = Math.min(open, close) * (Math.random() * 0.4 + 0.4); // 控制在 -60% 波动
		candlestick.push([open, close, low, high]);
		prevClose = close;
	});
	return {
		grid: {
			top: 0,
			left: 20,
			right: 0,
			bottom: '2%',
			containLabel: true,
		},
		backgroundColor: 'rgba(0, 0, 0, 0)',
		xAxis: {
			data: hours,
			axisTick: {
				show: false,
			},
			axisLine: {
				show: false,
			},
			axisLabel: {
				color: '#ffffff',
				fontSize: 6,
				fontWeight: 'bold',
			},
		},
		yAxis: {
			axisLine: {
				show: false,
			},
			axisTick: {
				show: false,
			},
			axisLabel: {
				show: false,
			},
			splitLine: {
				show: false,
			},
		},
		series: [
			{
				type: 'candlestick',
				data: candlestick,
				itemStyle: {
					color: upColor,
					color0: downColor,
					borderColor: upBorderColor,
					borderColor0: downBorderColor,
				},
			},
		],
	};
};

/**
 * 处理配置化图表
 */
const handleConfigChart = (option: any) => {
	return {
		tooltip: {
			show: true,
			trigger: 'axis',
			appendToBody: true,
			formatter(params: any) {
				const [firstElement] = params;
				return `<div>	
					<p class="tooltip-title">${firstElement.axisValueLabel}</p>
					${tooltipItemsHtmlString(params, option.unitList)}
				</div>`;
			},
			className: 'echarts-tooltip-diy',
		},
		legend: option.legend ? option.legend : {},
		emphasis: {
			focus: 'series',
		},
		grid: option.grid ? option.grid : {
			containLabel: true,
			top: 50,
			left: 20,
			right: 55,
			bottom: 30,
		},
		dataZoom: [
			{
				type: 'inside',
				orient: option.yAxis.length && option.yAxis[0].type === 'category' ? 'vertical' : 'horizontal'
			},
		],
		xAxis: option.xAxis,
		yAxis: option.yAxis && option.yAxis.length ? option.yAxis : [{type: 'value'}],
		series: option.series,
	};
};
/**
 * 处理堆叠图
 * @param option
 * @param type 'large' 为大屏配置
 */
const handleZhiChart = (option: any, type?: string) => {
	// 直接修改 lineSeries，而不是重新赋值，避免不必要的对象创建
	const lineSeries = option.echartsOption.series[0];
	if (!lineSeries) return
	lineSeries.data = lineSeries.stackDaySeriesData;

	const renderItem = (params: any, api: any) => {
		const x0 = api.value(0);
		const x1 = api.value(1);
		const yValue = api.value(2);
		const start = api.coord([x0, yValue]);
		const size = api.size([x1 - x0, yValue]);

		return {
			type: 'rect',
			shape: {
				x: start[0],
				y: start[1],
				width: size[0],
				height: size[1]
			},
			style: api.style()
		};
	};

	return {
		tooltip: {
			formatter(params: any) {
				return `
						<div class="content-panel">
							<p>
								<span style="background-color: ${params.color}" class="tooltip-item-icon"></span>
								<span>${params.seriesName}</span>
							</p>
							<span class="tooltip-value">
								${params.value[params.seriesIndex === 1 ? 2 : 1] + ' ' + option.echartsOption.unitList[params.seriesIndex]}
							</span>
					</div>`
			},
			appendToBody: true,
			className: 'echarts-tooltip-diy'
		},
		emphasis: {
			focus: 'series',
		},
		grid: {
			containLabel: true,
			top: type === 'large' ? 40 : 50,
			left: 20,
			right: type === 'large' ? 20 : 55,
			bottom: type === 'large' ? 10 : 30,
		},
		xAxis: {
			type: 'value',
			min: 0,
			max: 24,
			splitNumber: 24,
			splitLine: {
				show: false
			}
		},
		legend: {
			...option.echartsOption.legend,
			show: type !== 'large'
		},
		yAxis: option.echartsOption.yAxis,
		series: [
			lineSeries,
			{
				type: 'custom',
				name: '电价',
				yAxisIndex: 1,
				renderItem: renderItem,
				label: {
					show: true,
					position: 'top',
					formatter: (params: any) => `${params.value[4]}:\n${params.value[2]}元`
				},
				encode: {
					x: [0, 1],
					y: 2,
					itemName: 3
				},
				data: option.seasonalRangePrice
			}
		]
	};
};

/**
 * 处理大屏配置化图表
 */
const handlePreConfigChart = (option: any, downloadName?: string, chartType?: number) => {
	const yAxisTop = option.unitList[0] != '' && chartType != 3 ? 30 : 12;
	return {
		textStyle: {
			color: '#fff'
		},
		tooltip: {
			show: true,
			trigger: 'axis',
			appendToBody: true,
			formatter(params: any) {
				const [firstElement] = params;
				return `<div>	
					<p class="tooltip-title">${firstElement.axisValueLabel}</p>
					${tooltipItemsHtmlString(params, option.unitList)}
				</div>`;
			},
			className: 'echarts-tooltip-diy',
		},
		emphasis: {
			focus: 'series',
		},
		grid: {
			top: yAxisTop,
			left: 0,
			right: 10,
			bottom: 10,
			containLabel: true
		},
		dataZoom: [
			{
				type: 'inside',
				orient: option.yAxis.length && option.yAxis[0].type === 'category' ? 'vertical' : 'horizontal'
			},
		],
		xAxis: option.xAxis,
		yAxis: option.yAxis,
		series: option.series,
	};

};
/**
 * 转换timeBar
 */
const getTimeObject = (params: { deviceSn: string, timeUnit: number; timer: string }) => {
	const {deviceSn, timeUnit, timer} = params;
	let result: any = {};
	if (deviceSn) {
		result.deviceSn = deviceSn;
	}
	switch (timeUnit) {
		case 0:
			result.dayTime = timer;
			break;
		case 1:
			result.monthTime = timer;
			break;
		case 2:
			result.yearTime = timer;
			break;
		default:
			return null;
	}
	return result;
};

export {
	handleStackChart,
	handleChartData,
	handleSeasonalRangeChart,
	handleConfigChart,
	handlePreConfigChart,
	handleDataVChartData,
	getTimeObject,
	handleDataVChartCandlestickData,
	handleZhiChart
};
