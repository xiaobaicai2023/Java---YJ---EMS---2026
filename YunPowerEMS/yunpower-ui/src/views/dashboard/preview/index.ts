// <!--
//  * 功能：通用配置公共方法
//  * 作者：闫李壮
//  * 日期：2024-06-05
// -->
import useCompany from "@/store/modules/company";
import FixedValue from "@/components/public/fixed-value/index.vue";

/**
 * 1、折线
 * 2、柱状
 * 3、条形
 * 4、堆叠
 * 5、面积
 * 6、饼
 * 7、K线
 * 8、水滴
 * 9、仪表盘
 * 10、桑基
 * 11、分段
 * 12、排名
 * 13、状态
 * 14、单值
 * 15、报警列表
 * 16、工单列表
 * 17、报警警列表
 * 18、工单统计
 * 19、极坐标
 * 21、固定值
 */
const EnumChartType: any = {
    1: 'card-chart-combine',
    2: 'card-chart-combine',
    3: 'card-chart-combine',
    4: 'card-chart-stack',
    5: 'card-chart-combine',
    6: 'card-chart-pie',
    7: 'card-chart-k',
    8: 'card-chart-water',
    9: 'card-chart-gauge',
    10: 'card-chart-sankey',
    11: 'card-chart-section',
    12: 'card-chart-ranking',
    13: 'card-status',
    14: 'card-base',
    15: 'card-table',
    16: 'card-table',
    17: 'card-chart-pie-statistics',
    18: 'card-chart-pie-statistics',
    19: 'polar-coordinates',
    21: 'fixed-value',

    30: 'card-pv-station',
    31: 'card-weather',
};

const DefaultValueData: any = {
    1: {valueType: 2, apiType: 2},
    2: {valueType: 2, apiType: 2},
    3: {valueType: 2, apiType: 2},
    4: {valueType: 2, apiType: 2, dateDim: 60, storageType: 2},
    5: {valueType: 2, apiType: 2},
    6: {valueType: 1, apiType: 2, indicator: 1, dateDim: 70},
    7: {valueType: 2, apiType: 2, storageType: 1},
    8: {valueType: 1, apiType: 2, indicator: 1, dateDim: 70, percentage: 2},
    9: {valueType: 1, apiType: 2, indicator: 1, dateDim: 70},//只能指定变量,
    10: {valueType: 1, apiType: 1, indicator: 1,},
    11: {apiType: 2, dateDim: 60, isFullDate: 1},
    12: {valueType: 1, apiType: 2, indicator: 1, dateDim: 70},
    13: {valueType: 1, apiType: 1, indicator: 1, storageType: 1, changeType: 1}, //只能指定变量,
    14: {valueType: 1, apiType: 1},
    15: {tableType: 1, apiType: 2,},
    16: {tableType: 2, apiType: 2,},
    17: {tableType: 1, apiType: 2,},
    18: {tableType: 2, apiType: 2,},
    19: {valueType: 1, apiType: 2, },
    21: {tableType: 1, apiType: 1, indicator: 1,dateDim: 70},
};

/**
 * @desc 计算新的卡片位置
 * @param pageConfig 原数据
 * @param layout 新卡片的宽和高
 * @return w宽 h高 i下标
 */
const calculateNewPosition = (pageConfig:any[], layout:{w:number,h:number}) => {
    const { w, h } = layout;
    const PAGE_WIDTH = 12;

    // 获取最后一行的信息
    let lastRowY = 0;
    let lastRowMaxX = 0;
    pageConfig.forEach((item) => {
        if (item.y > lastRowY || (item.y === lastRowY && (item.x + item.w) > lastRowMaxX)) {
            lastRowY = item.y;
            lastRowMaxX = item.x + item.w;
        }
    });

    // 确定新卡片的起始位置
    let newX = lastRowMaxX;
    let newY = lastRowY;
    if (newX + w > PAGE_WIDTH) {
        newX = 0; // 新卡片无法加入当前行，应置于新行首
        newY = lastRowY + h; // 增加行高以适应新行
    }

    return {
        x: newX,
        y: newY,
        i: new Date().getTime() // 计算新的i值(获取当前时间戳)
    };
};

/**
 *    @desc 获取pageKey
 *    @return pageKey
 */
const getPageKey = (router: any) => {
    let currentPath = router.currentRoute.value.fullPath;
    if(currentPath.indexOf("overview")){
        currentPath = currentPath.replace("/overview","")
    }
    const stationId = useCompany().companyValue;

    return `${currentPath.slice(1).replace(/\//g, '-')}-${stationId}`
}
/**
 *    @desc 获取pageName
 *    @return pageName
 */
const getPageName = (route: any) => {
    const pageName = route.matched
        .filter((item: any) => item.meta.locale)
        .map((item: any) => item.meta.locale)
    return pageName[pageName.length - 1]
}

export {
    EnumChartType,
    DefaultValueData,
    calculateNewPosition,
    getPageKey,
    getPageName
}
