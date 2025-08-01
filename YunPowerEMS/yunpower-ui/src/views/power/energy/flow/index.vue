<!--
* 功能：能源流向
* 作者：曹晓桐
* 日期：2023-11-18
-->
<template>
  <div>
    <a-card class="general-card" :title="$t('power.energy.flow.title')">
      <template #extra>
        <time-bar :pick-type="1" @change="timerChange" />
      </template>
      <a-spin :loading="loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
        <Chart v-if="!loading" :options="chartOption" autoResize height="600px" />
      </a-spin>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { PowerModuleEunm, getEnergySteamDate } from "@/api/system/home-power";
import useChartOption from "@/hooks/chart-option";
import useLoading from "@/hooks/loading";
import { ref } from "vue";

//加载中
const { loading, setLoading } = useLoading(false);

const chartData = ref<any>([]);
const chartLinks = ref<any>([]);
//图表数据
const { chartOption } = useChartOption((isDark) => {
  return {
    series: {
      type: 'sankey',
      data: chartData.value,
      links: chartLinks.value,
      emphasis: {
        focus: 'adjacency'
      },
      lineStyle: {
        color: 'gradient',
        curveness: 0.5
      }
    },
    tooltip: {
      trigger: "item",
      triggerOn: 'mousemove',
      axisPointer: { type: "shadow" },
      valueFormatter: (value) => {
        return value;
      }
    },
  };
});


/**
 * 时间改变
 */
const timerChange = async (options: any) => {
  await fetchData({ pageValue: PowerModuleEunm.energy_steam_flow, "beginTime": options.timer[0], "endTime": options.timer[1] });
}

/**
 * 获取日期数据
 * @param param 时间参数
 */
const fetchData = async (param: any) => {
  setLoading(true);
  try {
    let res = await getEnergySteamDate(param);
    if (res.code == 200) {
      const { data, links } = generateSankeyDataAndLinks(res.data);
      chartData.value = data;
      chartLinks.value = links
    }
  } catch (ex) {
    console.error("能源流向出错", ex)
  } finally {
    setLoading(false);
  }
}



function generateSankeyDataAndLinks(treeData: any) {
  const data: any = [];
  const links: any = [];

  function traverseTree(node: any, parentName: any) {
    if (parentName) {
      links.push({
        source: parentName,
        target: node.key,
        value: node.value,
      });
    }

    data.push({
      name: node.key,
    });

    if (node.children) {
      node.children.forEach((child: any) => {
        traverseTree(child, node.key);
      });
    }
  }

  treeData.forEach((node: any) => {
    traverseTree(node, null);
  });

  return { data, links };
}

</script>
