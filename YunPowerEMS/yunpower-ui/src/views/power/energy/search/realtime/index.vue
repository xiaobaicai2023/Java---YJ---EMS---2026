<!--
* 功能：实时数据
* 作者：曹晓桐
* 日期：2023-11-18
-->
<template>
  <div>
    <a-row :gutter="12">
      <!-- 左侧树 start -->
      <a-col :span="4">
        <search-tree :title="$t('global.device')" @onChange="searchTreeChange" :stationType="props.stationType"/>
      </a-col>
      <!-- 左侧树 end -->

      <!-- 右侧内容 start -->
      <a-col :span="20">
        <a-card :loading="loadingOne" class="first-card"
                v-if="configRunTimeListData && configRunTimeListData.length > 0">
          <a-grid class="row-1" :cols="{ xs: 1, sm: 2, md: 3, lg: 4, xl: 6, xxl: 8 }" :colGap="12" :rowGap="16">
            <a-card v-for="(item, index) in configRunTimeListData" :key="index" :bordered="false" hoverable
                    :body-style="{ textAlign: 'center', fontWeight: '700', color: item.name == '设备状态' ? (item.value == '在线' ? 'rgb(var(--green-6))' : 'rgb(var(--red-7))') : 'rgb(var(--color-neutral-10))' }"
                    :header-style="{ textAlign: 'center', fontWeight: '700' }">
              <template #title>{{ item.name }}
              </template>
              {{ item.value }} {{item.unit}}
            </a-card>
          </a-grid>
        </a-card>
        <a-card :loading="loadingTwo"
                :style="{ padding:'20px',marginTop: configRunTimeListData && configRunTimeListData.length > 0 ? '12px' : '0px' }"
                v-if="runTimeListData && runTimeListData.length > 0">
          <a-descriptions :align="{ label: 'right' }" :column="{ lg: 2 }">
            <a-descriptions-item v-for="item of runTimeListData" :key="item.label" :label="item.label" :span="item.span ?? 1">
              <a-tooltip :content="item.time" position="right">
                <a-tag>{{ item.value }} {{item.unit}}</a-tag>
              </a-tooltip>
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
        <custom-empty v-if="configRunTimeListData.length == 0 && runTimeListData.length == 0"/>
      </a-col>
      <!-- 右侧内容 end -->
    </a-row>
  </div>
</template>

<script setup lang="ts">
import {StationTypeEnum} from "@/api/system/device";
import {getConfigRunTimeList, getRunTimeList} from "@/api/system/device-var";
import useLoading from "@/hooks/loading";
import {ref, onMounted, inject, onBeforeUnmount, watch} from "vue";
import {SocketData} from "@/api/websocketService";
import {useIntervalFn} from "@vueuse/core";

/*************************** 变量区域 begin ***************************/

    //接受组件参数
const props = defineProps({
      stationType: {
        type: Number,
        default: StationTypeEnum.power,
      },
    })
//加载中1
const loadingOne = ref<boolean>(false);
//加载中2
const loadingTwo = ref<boolean>(false);
//第一排
const configRunTimeListData = ref<any>([]);
//第二排
const runTimeListData = ref<any>([]);
const searchParams = ref<any>();

/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

// WebSocket 数据
const {data, sendData}: SocketData = inject('webSocketData', {
  data: {value: 0},
  sendData: () => {
  }
});

// 监听 WebSocket 数据
watch(data, async (newVal) => {
  if (newVal.key && newVal.key.length > 0) {
    changeValue(newVal);
    changeValue2(newVal);
  }
});

/**
 * 搜索树Change
 */
const searchTreeChange = async (val: any) => {
  searchParams.value = val;
  await fetchData();
  await fetchData2();
}

const changeValue = (newVal: any) => {
  if (configRunTimeListData.value && configRunTimeListData.value.length > 0) {
    configRunTimeListData.value.forEach((item: any) => {
      if (item.key == newVal.key) {
        item.value = newVal.value;
        item.time = newVal.time;
        //状态量
        if(item.varType == "2"){
          item.value = parseInt(newVal.value) == 0 ? "投入" : "投出";
        }
      }
    })
  }
}

const changeValue2 = (newVal: any) => {
  if (runTimeListData.value && runTimeListData.value.length > 0) {
    runTimeListData.value.forEach((item: any) => {
      if (item.key == newVal.key) {
        item.time = newVal.time;
        item.value = newVal.value;
        //状态量
        if(item.varType == "2"){
          item.value = parseInt(newVal.value) == 0 ? "投入" : "投出";
        }
      }
    })
  }
}


const subscribe = () => {
  if (configRunTimeListData.value && configRunTimeListData.value.length > 0) {
    configRunTimeListData.value.forEach((item: any) => {
      sendData({
        action: 'subscribe',
        bizSn: item.key
      });
    })
  }
}

const onSubscribe = () => {
  if (configRunTimeListData.value && configRunTimeListData.value.length > 0) {
    configRunTimeListData.value.forEach((item: any) => {
      sendData({
        action: 'unsubscribe',
        bizSn: item.key
      });
    })
  }
}

/**
 * 实时数据
 * @param params 查询参数
 * @returns 结果
 */
const fetchData = async () => {
  loadingOne.value = true;
  configRunTimeListData.value = [];
  onSubscribe();
  try {
    const res: any = await getConfigRunTimeList(searchParams.value.deviceSn, props.stationType);
    if (res.code == 200) {
      configRunTimeListData.value = res.data || [];
      subscribe();
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    loadingOne.value = false;
  }
};


const subscribe2 = () => {
  if (runTimeListData.value && runTimeListData.value.length > 0) {
    runTimeListData.value.forEach((item: any) => {
      sendData({
        action: 'subscribe',
        bizSn: item.key
      });
    })
  }
}

const onSubscribe2 = () => {
  if (runTimeListData.value && runTimeListData.value.length > 0) {
    runTimeListData.value.forEach((item: any) => {
      sendData({
        action: 'unsubscribe',
        bizSn: item.key
      });
    })
  }
}

/**
 * 实时数据
 * @param params 查询参数
 * @returns 结果
 */
const fetchData2 = async () => {
  loadingTwo.value = true;
  runTimeListData.value = [];
  onSubscribe2();
  try {
    const res: any = await getRunTimeList(searchParams.value.deviceSn);
    if (res.code == 200 && res.data && res.data.length > 0) {
      let data: any = [];
      res.data.forEach((item: any) => {
        data.push({
          label: item.name,
          value: item.value,
          time: item.time,
          key: item.key,
          unit: item.unit,
          varType:item.varType
        })
      });
      runTimeListData.value = data;
      subscribe2();
    }
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
    loadingTwo.value = false;
  }
};

/*************************** 方法区域 end ***************************/
/**
 * 实例创建完成后立即执行
 */
onMounted(async () => {
  await fetchData();
  await fetchData2();
});

onBeforeUnmount(() => {
  onSubscribe();
  onSubscribe2();
});

</script>

<style lang="less" scoped>
.first-card {
  .row-1 .arco-card {
    background-color: var(--color-fill-2);
  }
}
</style>
