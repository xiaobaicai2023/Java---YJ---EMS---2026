<!--
* 功能：峰谷数据
* 作者：曹晓桐
* 日期：2023-11-19
-->
<template>
  <div>
    <a-row :gutter="15">

      <!-- 左侧树 start -->
      <a-col :span="4">
        <search-tree :title="$t('global.device')" :time="true" :multiple="true" @onChange="searchTreeChange" />
      </a-col>
      <!-- 左侧树 end -->

      <!-- 右侧内容 start -->
      <a-col :span="20">
        <a-card class="general-card" :title="$t('power.energy.search.queryData')" v-if="renderData.length > 0">
          <!-- <template #extra>
            <a-button @click="downloadTableData" type="primary">
              <template #icon>
                <icon-download />
              </template>
              下载
            </a-button>
          </template> -->
          <a-table :data="renderData" :loading="loading" :bordered="{ wrapper: true, cell: true }" :pagination="false"
            :scroll="{ x: tableWidth, y: '100%' }">
            <template #columns>
              <a-table-column v-for="(item, index) in customColumnsValue" :key="index" :title="item.title"
                :data-index="item.dataIndex" :fixed="item.fixed" :width="item.width"
                :align="item.align"></a-table-column>
            </template>
          </a-table>
        </a-card>
        <custom-empty v-else />
      </a-col>
      <!-- 右侧内容 end -->
    </a-row>
  </div>
</template>

<script setup lang="ts">
  import { onMounted, reactive, ref } from "vue";
  import useLoading from "@/hooks/loading";
  import { ppfvList } from "@/api/system/home-power";
  import dayjs from "dayjs";
  import { TableColumnData } from "@arco-design/web-vue";
  import { download } from "@/api/download";

  //加载中
  const { loading, setLoading } = useLoading(false);
  const tableWidth = ref < number > ();
  const customColumnsValue = ref < any > ();
  //数据
  const renderData = ref < any > ([]);
  //自定义头
  const searchParams = ref < any > ({});

  /**
   * 搜索树Change
   */
  const searchTreeChange = async (val: any) => {
    searchParams.value = val;
    await fetchData();
  }


  /**
   * 获取图表类型
   * @param param 时间参数
   */
  const fetchData = async () => {
    setLoading(true);
    try {
      let param = {
        ...searchParams.value
      }
      let res = await ppfvList(param);
      if (res.code == 200 && res.data && res.data.length > 0) {
        let data: any = [];
        let columns: any = [];
        let width = 0;
        res.data.forEach((item: any, index: any) => {
          let info: any = {};
          item.forEach((citem: any, cindex: any) => {
            info[citem.key] = citem.value;
            if (index == 0) {
              let temp: any = {
                title: citem.key,
                dataIndex: citem.key,
                width: 120,
                align: "center"
              };
              if (citem.key == '设备') {
                temp["fixed"] = "left";
                temp["align"] = "left";
                temp["width"] = 220;
              }
              if (citem.key == '总') {
                temp["fixed"] = "right";
                temp["align"] = "center";
                temp["width"] = 150;
              }
              width += temp.width;
              columns.push(temp)
            }
          })
          data.push(info);
        })
        customColumnsValue.value = columns;
        renderData.value = data;
        tableWidth.value = width;
      } else {
        renderData.value = [];
      }
    } catch (ex) {
      renderData.value = [];
      console.error("峰谷数据出错", ex)
    } finally {
      setLoading(false);
    }
  }

  /**
   * 合计
   */
  const summary = () => {

  }

  /**
    * 下载表格数据
  */
  const downloadTableData = () => {
    // download(url, params, filename)
    // download("/api/system/home-power/ppfvList", searchParams.value, "峰谷数据").then(res => {
    //   console.log('res',res)
    // }).catch(error=>{
    //   console.log('error', error)
    // })
  }

  onMounted(async () => {
    const dateNow = dayjs().format("YYYY-MM-DD HH:mm");
  })

</script>

<style scoped>
  .title {
    font-size: 16px;
  }
</style>
