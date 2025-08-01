import { App } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart, PieChart, RadarChart, GaugeChart, CustomChart, SankeyChart } from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  DataZoomComponent,
  GraphicComponent,
  TitleComponent,
  ToolboxComponent
} from 'echarts/components'
import UpLoad from '@/components/upload/index.vue'
import Chart from '@/components/chart/index.vue'
import CutomChart from '@/components/custom-chart/index.vue'
import CutomChartDataV from '@/components/custom-chart-datav/index.vue'

import Breadcrumb from '@/components/breadcrumb/index.vue'
import CustomIcon from '@/components/custom-icon/index.vue'
import ResultIcon from '@/components/result-icon/index.vue'
import TimeBar from '@/components/time-bar/index.vue'
import SearchTree from '@/components/search-tree/index.vue'
import hljs from '@/components/hljs/index.vue'
import dictTag from '@/components/dict-tag/index.vue'
import stopFlag from '@/components/stop-flag/index.vue'
import CustomChartGeneral from '@/components/custom-chart-general/index.vue'
import CustomEmpty from '@/components/custom-empty/index.vue'
import SystemGroup from '@/components/system-group/index.vue'
// Manually introduce ECharts modules to reduce packing size
import NodeSankey from "@/components/node-sankey/index.vue";


import YkiteFullScreenContainer from '@/components/ykite-full-screen-container/index.vue'
// 卡片-基础
import CardBase from '@/components/public/card-base/index.vue';
// 卡片-站点
import CardStatus from '@/components/public/card-status/index.vue';
// 卡片-列表
import CardTable from '@/components/public/card-table/index.vue';
// 图表-折线
import CardChartCombine from '@/components/public/card-chart-combine/index.vue';
// 图表-堆叠
import CardChartStack from '@/components/public/card-chart-stack/index.vue';
// 图表-饼图
import CardChartPie from '@/components/public/card-chart-pie/index.vue';
// 图表-分段
import CardChartSection from '@/components/public/card-chart-section/index.vue';
// 图表-水滴
import CardChartWater from '@/components/public/card-chart-water/index.vue';
// 图表-仪表盘
import CardChartGauge from '@/components/public/card-chart-gauge/index.vue';
// 图表-桑基
import CardChartSankey from '@/components/public/card-chart-sankey/index.vue';
// 图表-k
import CardChartK from '@/components/public/card-chart-k/index.vue';
// 图表-排名
import CardChartRanking from '@/components/public/card-chart-ranking/index.vue';
// 图表-极坐标
import PolarCoordinates from '@/components/public/polar-coordinates/index.vue';
// 图表-固定值
import FixedValue from '@/components/public/fixed-value/index.vue';
// 专用模板-报警统计-饼图
import CardChartPieStatistics from "@/components/public-large/card-chart-pie-statistics/index.vue";
// 大屏-状态图
import LargeScreenStatus from "@/components/public-large/large-screen-status/index.vue";
// 大屏-折线
import LargeCombine from "@/components/public-large/chart-combine/index.vue";
// 大屏-堆叠
import LargeStack from "@/components/public-large/chart-stack/index.vue";
// 图表-饼图
import LargePie from '@/components/public-large/chart-pie/index.vue';
// 大屏-分段
import LargeSection from "@/components/public-large/chart-section/index.vue";
// 大屏-水滴
import LargeWater from "@/components/public-large/chart-water/index.vue";
// 大屏-仪表盘
import LargeGauge from "@/components/public-large/chart-gauge/index.vue";
// 大屏-桑基
import LargeSankey from "@/components/public-large/chart-sankey/index.vue";
// 大屏-折线
import LargeK from "@/components/public-large/chart-k/index.vue";
// 大屏-表格
import LargeTable from "@/components/public-large/chart-table/index.vue";
// 大屏-排行榜
import LargeRanking from "@/components/public-large/chart-ranking/index.vue";


//光伏站点
import CardPvStation from "@/components/public/card-pv-station/index.vue";
//光伏站点
import CardWeather from "@/components/public/card-weather/index.vue";
use([
  CanvasRenderer,
  BarChart,
  LineChart,
  PieChart,
  RadarChart,
  GaugeChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  DataZoomComponent,
  GraphicComponent,
  TitleComponent,
  ToolboxComponent,
  CustomChart,
  SankeyChart
])

export default {
  install(Vue: App) {
    Vue.component('UpLoad', UpLoad)
    Vue.component('Chart', Chart)
    Vue.component('CutomChart', CutomChart)
    Vue.component('CutomChartDataV', CutomChartDataV)
    Vue.component('Breadcrumb', Breadcrumb)
    Vue.component('custom-icon', CustomIcon)
    Vue.component('result-icon', ResultIcon)
    Vue.component('time-bar', TimeBar)
    Vue.component('search-tree', SearchTree)
    Vue.component('hljs', hljs)
    Vue.component('dict-tag', dictTag)
    Vue.component('stop-flag', stopFlag)
    Vue.component("custom-empty", CustomEmpty)
    Vue.component('CustomChartGeneral', CustomChartGeneral)
    Vue.component("SystemGroup", SystemGroup)
    Vue.component("YkiteFullScreenContainer", YkiteFullScreenContainer)
    Vue.component('CardBase', CardBase)
    Vue.component('CardTable', CardTable)
    Vue.component('CardStatus', CardStatus)
    Vue.component('CardChartCombine', CardChartCombine)
    Vue.component('CardChartStack', CardChartStack)
    Vue.component('CardChartPie', CardChartPie)
    Vue.component('CardChartSection', CardChartSection)
    Vue.component('CardChartWater', CardChartWater)
    Vue.component('CardChartGauge', CardChartGauge)
    Vue.component('CardChartSankey', CardChartSankey)
    Vue.component('CardChartK', CardChartK)
    Vue.component('CardChartRanking', CardChartRanking)
    Vue.component('CardChartPieStatistics', CardChartPieStatistics)
    Vue.component('LargeScreenStatus', LargeScreenStatus)
    Vue.component('LargeCombine', LargeCombine)
    Vue.component('LargeStack', LargeStack)
    Vue.component('LargePie', LargePie)
    Vue.component('LargeSection', LargeSection)
    Vue.component('LargeWater', LargeWater)
    Vue.component('LargeGauge', LargeGauge)
    Vue.component('LargeSankey', LargeSankey)
    Vue.component('LargeK', LargeK)
    Vue.component('LargeTable', LargeTable)
    Vue.component('LargeRanking', LargeRanking)
    Vue.component('PolarCoordinates', PolarCoordinates)
    Vue.component('FixedValue', FixedValue)

    Vue.component('CardPvStation', CardPvStation)
    Vue.component('CardWeather', CardWeather)
    Vue.component('NodeSankey', NodeSankey)
    // Vue.component('chartEdit', ChartBarEdit)
  },
}
