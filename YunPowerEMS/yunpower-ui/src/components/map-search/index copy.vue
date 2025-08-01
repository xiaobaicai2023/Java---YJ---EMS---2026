<template>
  <a-modal :width="1200" title="定位" v-model:visible="mapVisible" @ok="handleOk" @cancel="handleCancel">
    <a-row :gutter="24">
      <!-- 左侧搜索 -->
      <a-col :span="8">
        <a-row>
          <a-col>
            <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
              label-align="left" auto-label-width>
              <a-row :gutter="16">
                <a-col :span="18">
                  <a-form-item field="stationName" label="站点名称">
                    <a-input v-model="searchModel.location" placeholder="请输入地址" allow-clear @press-enter="search" />
                  </a-form-item>
                </a-col>
                <a-col :span="6">
                  <a-space direction="vertical" :size="18">
                    <a-button type="primary" @click="search" :loading="loading">
                      <template #icon>
                        <icon-search />
                      </template>
                      搜索
                    </a-button>
                  </a-space>
                </a-col>
              </a-row>
            </a-form>
          </a-col>

        </a-row>
        <a-row>
          <a-col>
            <div id="panel">
            </div>
          </a-col>
        </a-row>
      </a-col>
      <a-col :span="16">
        <div id="container">
        </div>
        <div id="divCoordinate" style="display: none;"></div>
      </a-col>
    </a-row>
  </a-modal>
</template>

<script lang="ts" setup>
import { onMounted, onUnmounted, ref } from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import { Message } from "@arco-design/web-vue";
window._AMapSecurityConfig = {
  securityJsCode: "0f2d48aa1d4c1c8379efa60e68e34cae",
};

let map: any = null;
let placeSearch: any = null
let geolocation: any = null
let citysearch: any = null
let cityCode: any = ref<any>('');
let cityName: any = ref<any>('');
const loading = ref<boolean>(true);
let divCoordinate:any = null;
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

}

/**
 * 取消
 */
const handleCancel = () => {

}

onMounted(() => {

  AMapLoader.load({
    key: "6bcdaa6062b344a686e6cda8c7074b0e", // 申请好的Web端开发者Key，首次调用 load 时必填
    version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: ["AMap.PlaceSearch", "AMap.Geolocation", "AMap.CitySearch"], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
  })
    .then((AMap) => {
      map = new AMap.Map("container", {
        // 设置地图容器id
        viewMode: "2D", // 是否为3D地图模式
        zoom: 11, // 初始化地图级别
        resizeEnable: true
      });


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
              pageSize: 6, // 单页显示结果条数
              pageIndex: 1, // 页码
              city: cityCode.value || undefined, // 兴趣点城市
              citylimit: false,  //是否强制限制在设置的城市内搜索
              map: map, // 展现结果的地图实例
              panel: "panel", // 结果列表将在此容器中进行展示。
              autoFitView: true // 是否自动调整地图视野使绘制的 Marker点都处于视口的可见范围
            });
            divCoordinate = document.getElementById('divCoordinate');
            
            // 监听鼠标移动事件
            AMap.Event.addListener(map, 'mousemove', function (e: any) {
              // 更新 marker 的位置
              divCoordinate.style.display = "block";
              divCoordinate.style.top = e.originEvent.clientX+'px';
              divCoordinate.style.left = e.originEvent.clientY + 'px';
              divCoordinate.innerHTML = `${e.lnglat.lng}，${e.lnglat.lat}`;
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
  placeSearch.search(searchModel.value.location, function (status: any, result: any) {
    if (result.info == "OK" && result.poiList.pois) {
      result.poiList.pois.forEach((item: any) => {

      });
    }
  });
}

onUnmounted(() => {
  map?.destroy();
});
</script>

<style scoped lang="less">
#container {
  width: 100%;
  height: 500px;
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
