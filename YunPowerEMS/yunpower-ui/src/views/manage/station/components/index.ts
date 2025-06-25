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
 * 17、报警状态
 * 18、工单状态
 */

const EnumChartType: any = {
    1: 'large-combine',
    2: 'large-combine',
    3: 'large-combine',
    4: 'large-stack',
    5: 'large-combine',
    6: 'large-pie',
    7: 'large-k',
    8: 'large-water',
    9: 'large-gauge',
    10: 'large-sankey',
    11: 'large-section',
    12: 'large-ranking',
    13: 'large-screen-status',
    14: 'card-base',
    15: 'large-table',
    16: 'large-table',
    17: 'card-chart-pie-statistics',
    18: 'large-combine',
};


const DefaultValueData: any = {
    1: {valueType: 2, apiType: 2, indicator: 1, changeType: 1},
    2: {valueType: 2, apiType: 2, indicator: 1, changeType: 1},
    3: {valueType: 2, apiType: 2, indicator: 1, changeType: 1},
    4: {valueType: 2, apiType: 2, indicator: 1, storageType: 2, percentage: 0},
    5: {valueType: 2, apiType: 2, indicator: 1, changeType: 1},
    6: {valueType: 1, apiType: 1, indicator: 1},
    7: {},//K线图,
    8: {valueType: 1, apiType: 1, indicator: 1, percentage: 1},
    9: {valueType: 1, apiType: 2, indicator: 1,},//只能指定变量,
    12: {valueType: 1, apiType: 2, indicator: 1, storageType: 2},
    13: {apiType: 3, equipType: 1}, //只能指定变量,
    14: {valueType: 1, apiType: 1, indicator: 1},
    15: {tableType: 1, apiType: 3, dateDim: 70, cardName: '实时报警信息'},
    16: {tableType: 2, apiType: 3, dateDim: 70, cardName: '实时工单信息'},
    17: {tableType: 1, apiType: 3, dateDim: 70, cardName: '报警状态', staticType:1},
    18: {tableType: 2, apiType: 3, dateDim: 70, cardName: '工单状态', staticType:2},
};

const ChartLabel: any = {
    1: '折线图',
    2: '柱状图',
    3: '条形图',
    4: '堆叠图',
    5: '面积图',
    6: '饼图',
    7: 'K线图',
    8: '水滴图',
    9: '仪表盘',
    10: '桑基图',
    11: '分段图',
    12: '排行图',
    13: '状态图',
    14: '单值图',
    15: '报警列表',
    16: '工单列表',
    17: '报警统计',
    18: '工单统计',
}

export {
    DefaultValueData,
    ChartLabel,
    EnumChartType
}

