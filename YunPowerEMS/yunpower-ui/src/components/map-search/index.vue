<template>
  <a-modal :width="1200" title="坐标拾取" v-model:visible="mapVisible" @ok="handleOk" @cancel="handleCancel">
    <a-space direction="vertical" :size="16" style="display: block;">
      <a-row justify="center">
        <a-col :span="12">
          <a-space>
            <a-input id="tipinput" v-model="searchName" placeholder="请输入关键词进行搜索" allow-clear />
            <a-button type="primary" @click="search">
              <template #icon>
                <icon-search />
              </template>
              搜索
            </a-button>
            <div>
              坐标获取结果：{{ location }}
            </div>
          </a-space>
        </a-col>
      </a-row>
      <a-row justify="center">
        <a-col>
          <div id="container">
          </div>
          <div id="divCoordinate" style="display: none;"></div>
        </a-col>
      </a-row>
    </a-space>
  </a-modal>
</template>

<script lang="ts" setup>
import { onMounted, onUnmounted, ref } from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import { Message } from "@arco-design/web-vue";

const emits = defineEmits(['success', 'cancle'])

window._AMapSecurityConfig = {
  securityJsCode: "0f2d48aa1d4c1c8379efa60e68e34cae",
};
const searchName = ref<any>("");
const location = ref<any>("");
let map: any = null;
let placeSearch: any = null
let geolocation: any = null
let citysearch: any = null
let cityCode: any = ref<any>('');
let cityName: any = ref<any>('');
const loading = ref<boolean>(true);
let divCoordinate: any = null;
let auto: any = null;
//组件参数
const props = defineProps({
  visible: Boolean,
})

//生成查询条件对象
const generateSearchModel = () => {
  return {
    location: ''
  };
};
//查询表单对象
const searchModel = ref(generateSearchModel());
//显示状态
const mapVisible = ref<boolean>(props.visible);
/**
 * 确定
 */
const handleOk = () => {
  emits('success', location.value)
}
/**
 * 取消
 */
const handleCancel = () => {
  emits('cancle', '')
}
onMounted(() => {
  AMapLoader.load({
    key: "6bcdaa6062b344a686e6cda8c7074b0e", // 申请好的Web端开发者Key，首次调用 load 时必填
    version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: ["AMap.PlaceSearch", "AMap.Geolocation", "AMap.CitySearch", 'AMap.AutoComplete'], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
  })
    .then((AMap) => {
      map = new AMap.Map("container", {
        // 设置地图容器id
        viewMode: "2D", // 是否为3D地图模式
        zoom: 11, // 初始化地图级别
        resizeEnable: true
      });
      map.setDefaultCursor("pointer");

      //定位
      geolocation = new AMap.Geolocation({
        enableHighAccuracy: true,//是否使用高精度定位，默认:true
        timeout: 10000,          //超过10秒后停止定位，默认：5s
        position: 'RB',    //定位按钮的停靠位置
        offset: [10, 20], //定位按钮与设置的停靠位置的偏移量，默认：[10, 20]
        zoomToAccuracy: true,   //定位成功后是否自动调整地图视野到定位点

      });
      map.addControl(geolocation);

      //城市
      citysearch = new AMap.CitySearch();
      geolocation.getCurrentPosition(function (status: any, result: any) {
        if (status == 'complete') {
          onComplete(result)
        } else {
          onError(result)
        }
      });

      citysearch.getLocalCity(function (status: any, result: any) {
        if (status === 'complete' && result.info === 'OK') {
          if (result && result.city && result.bounds) {
            var cityinfo = result.city;
            var citybounds = result.bounds;
            cityCode.value = result.infocode;
            cityName.value = cityinfo;
            // document.getElementById('info').innerHTML = '您当前所在城市：' + cityinfo;
            //地图显示当前城市
            map.setBounds(citybounds);
            loading.value = false
            placeSearch = new AMap.PlaceSearch({
              map: map,
              autoFitView: true // 是否自动调整地图视野使绘制的 Marker点都处于视口的可见范围
            });  //构造地点查询类

            divCoordinate = document.getElementById('divCoordinate');
            map.on('mousemove', function (e: any) {
              divCoordinate.style.display = 'block';
              divCoordinate.style.left = e.originEvent.pixel.x + 50 + 'px';
              divCoordinate.style.top = e.originEvent.pixel.y + 70 + 'px';
              let str = `${e.lnglat.lng}，${e.lnglat.lat}`;
              // location.value = str;
              divCoordinate.innerHTML = str;
            });
            map.on('mouseout', function (e: any) {
               divCoordinate.style.display  = 'none';
            });
            map.on('click', function (e: any) {
              let str = `${e.lnglat.lng}，${e.lnglat.lat}`;
               location.value = str;
            });
          }
        } else {
          // document.getElementById('info').innerHTML = result.info;
        }
      });



    })
    .catch((e) => {
      console.log(e);
    });
});

const onComplete = (result: any) => {
  console.log("onComplete", result);
}

const onError = (result: any) => {
  console.log("onError", result);
}

const search = () => {
  if (loading.value) {
    Message.success("定位中..." as string)
    return;
  }
  placeSearch.search(searchName.value);
}

onUnmounted(() => {
  map?.destroy();
});
</script>

<style scoped lang="less">
#container {
  width: 100%;
  height: 600px;
}

#panel {
  max-height: 90%;
  width: 100%;
}

#divCoordinate {
  display: none;
  position: absolute;
  height: 26px;
  line-height: 26px;
  padding: 0 10px;
  border: 1px solid #999;
  background: #fff;
  z-index: 99
}
</style>
