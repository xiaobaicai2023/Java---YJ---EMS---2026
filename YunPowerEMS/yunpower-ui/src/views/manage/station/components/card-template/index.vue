<template>
  <!--新建配置-->
  <a-modal width="80%" :modal-style="{height:85+'%'}" :body-style="{height:92+'%'}" v-model:visible="props.visible" :closable="false" @cancel="handleCancel" @ok="handleOk">
    <!--      <template #title></template>-->
    <div v-if="props.visible">
      <!--通用模板-->
      <a-card title="通用模板">
        <a-row :gutter="[16, 16]">
          <a-col :span="6" :class="['item-wrapper', ind==10?'disabled-item':'']" v-for="(val, ind) in globalTemplate" :key="ind"  @click="chooseTemplate(val,ind)">
            <dl>
              <dd>{{val.public.title}}</dd>
              <dt>
                <img :src="val.url" alt="找不到了">
              </dt>
            </dl>
            <a-button class="choose-button" :disabled="ind==10" size="mini">选择</a-button>
          </a-col>
        </a-row>
      </a-card>

      <!--专用模板-->
      <a-card title="专用模板" style="margin-top: 20px">
        <a-row :gutter="[16, 16]">
          <a-col  class="item-wrapper" v-for="(val,ind) in specializedTemplate" :key="ind" :span="val.layout_span" @click="chooseTemplate(val)">
            <dl>
              <dd>{{val.name}}</dd>
              <dt :style="{height:ind==0||ind==1?'26vh':''}">
                <img :src="val.url" alt="111">
              </dt>
            </dl>
            <a-button class="choose-button" size="mini">选择</a-button>
          </a-col>
        </a-row>
      </a-card>
    </div>
  </a-modal>
</template>
<script lang="ts" setup>
import bar2 from "@/assets/charts/bar2.png";
import stack from "@/assets/charts/stack.png";
import line from "@/assets/charts/line.png";
import bar from "@/assets/charts/bar.png";
import pie from "@/assets/charts/pie.png";
import waterDrop from "@/assets/charts/water-drop.png";
import single from "@/assets/charts/single.png";
import status from "@/assets/charts/status.png";
import area from "@/assets/charts/area.png";
import rank from "@/assets/charts/rank.png";
import kLine from "@/assets/charts/k-line.png";
import panel from "@/assets/charts/panel.png";

import warn from "@/assets/charts/warn.png";
import orderInfo from "@/assets/charts/order-info.png";
import alarmStatus from "@/assets/charts/alarm-status.png";
import orderStatus from "@/assets/charts/order-status.png";
import {nextTick, onMounted, ref} from "vue";


const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(['chooseTemplate']);
const scrollDiv = ref(null)

const globalTemplate = [{
  chartType: 2,
  url: bar2,
  name: '柱状图',
  type: 'chart-edit',
  layout: 'card-chart-combine',
  public: {
    title: '柱状图',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 3,
  url: bar,
  layout: 'card-chart-combine',
  public: {
    title: '条形图',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 1,
  url: line,
  layout: 'card-chart-combine',
  public: {
    title: '折线图',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 4,
  url: stack,
  layout: 'card-chart-combine',
  public: {
    title: '堆叠图',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 6,
  url: pie,
  layout: 'card-chart-combine',
  public: {
    title: '饼图',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 8,
  url: waterDrop,
  layout: 'card-chart-combine',
  public: {
    title: '水滴图',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 14,
  url: single,
  layout: 'card-chart-combine',
  public: {
    title: '单值图',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 13,
  url: status,
  layout: 'card-chart-combine',
  public: {
    title: '状态量',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 5,
  url: area,
  layout: 'card-chart-combine',
  public: {
    title: '面积图',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 12,
  url: rank,
  name: '排行榜',
  layout: 'card-chart-combine',
  public: {
    title: '排行榜',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 7,
  url: kLine,
  name: 'K线图',
  layout: 'card-chart-combine',
  public: {
    title: 'K线图',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 9,
  url: panel,
  name: '仪表盘',
  layout: 'card-chart-combine',
  public: {
    title: '仪表盘',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
}];

const specializedTemplate = [{
  chartType: 15,
  url: warn,
  name: '实时报警',
  layout_span: 16,
  layout: 'card-chart-combine',
  public: {
    title: '实时报警',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 16,
  url: orderInfo,
  name: '工单信息',
  layout_span: 8,
  layout: 'card-chart-combine',
  public: {
    title: '工单信息',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType:17,
  url: alarmStatus,
  name: '报警状态',
  layout_span: 6,
  staticType: 1,
  layout: 'card-chart-combine',
  public: {
    title: '报警状态',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
},{
  chartType: 18,
  url: orderStatus,
  name: '工单状态',
  layout_span: 6,
  layout: 'card-chart-combine',
  staticType: 2,
  public: {
    title: '工单状态',
    icon: {
      url: 'icon-ykite-gongshuai',
      color: 'rgb(var(--arcoblue-4))',
      status: 1
    }
  }
}];

const handleClick = () => {
  emit('chooseTemplate');
}

const myScrollingDiv = ref<HTMLElement | null>(null);

const scrollToTop = async () => {
  // 使用nextTick确保DOM更新后再执行滚动操作
  await nextTick();
  if (myScrollingDiv.value) {
    // 使用scrollTo方法滚动到顶部
    myScrollingDiv.value.scrollTo(0, 0);
  }
};

const handleOk = () => {
  console.log('[111')
  scrollToTop()
  emit('chooseTemplate');
}


const handleCancel = () => {
  emit('chooseTemplate');
}


const chooseTemplate = (val, ind) => {
  if(ind !=10){
    emit('chooseTemplate', val);
  }
}


onMounted(()=>{
  nextTick(()=>{

  })
})


</script>
<style lang="less" scoped>
.item-wrapper{
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;

  dl{
    padding: 0;
    margin: 0;
    width: 100%;
    flex: 1;
    box-sizing: border-box;
    border: 1px solid var(--color-neutral-4);
    border-radius: 4px;

    dd{
      padding: 0;
      margin: 0;
      line-height: 24px;
      text-align: center;
      border-top-left-radius: 4px;
      border-top-right-radius: 4px;
      color: var(--color-neutral-10);
      background-color: var(--color-neutral-4);
    }

    dt{
      padding: 0;
      margin: 0;
      overflow: hidden;
      display: flex;
      width: 100%;
      //height: 160px;
      justify-content: center;
      align-items: center;

      img{
        width: 100%;
        //height: 100%;
      }
    }
  }


  button{
    margin-top: 8px;
  }
}

.item-wrapper:hover{
  dl{
    border-color: var(--color-neutral-6);
  }
  .choose-button{
    background: var(--color-neutral-3);
  }
}

.disabled-item:hover{
  cursor: not-allowed;
  dl{
    border: 1px solid var(--color-neutral-4);
  }
  .choose-button{
    background: none;
  }
}
</style>
