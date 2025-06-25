<template>
  <a-row class="content">
    <a-col flex="50px">
      <a-space>
        <a-button type="primary" status="success" :disabled="status" @click="handleReception">接收</a-button>
        <a-button type="primary" status="warning" :disabled="!status" @click="handleStop">停止</a-button>
        <a-button type="primary" status="danger" @click="handleClear">清空</a-button>
      </a-space>
    </a-col>
    <a-col flex="auto" class="content-view">
      <TransitionGroup name="list" tag="ul">
        <li v-for="item in codeData" :key="item">
          <p>{{item.time}}</p>
          {{ item.value }}
        </li>
      </TransitionGroup>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import {onMounted, ref, inject, watch, onBeforeUnmount} from "vue";
import {useRouter} from 'vue-router';
import {SocketData} from "@/api/websocketService";

//路由
const router = useRouter();
const channelSn = ref<any>(router.currentRoute.value.query.channelSn);
const codeData = ref<any>([]);
const status = ref<any>(true);
let timer: ReturnType<typeof setTimeout> | null = null;

// WebSocket 数据
const {data, sendData}: SocketData = inject('webSocketData', {
  data: {value: 0},
  sendData: () => {
  }
});

// 订阅
const subscribe = () => {
  sendData({
    action: 'subscribe',
    bizSn: channelSn.value
  });
}

// 取消订阅
const onSubscribe = () => {
  sendData({
    action: 'unsubscribe',
    bizSn: channelSn.value
  });
}

//接收
const handleReception = () => {
  status.value = !status.value;
  subscribe();
  timerStart();
}

// 计时器开始
const timerStart = () => {
  // 设置定时器，在一分钟之后取消请求
  timer = setTimeout(() => {
    handleStop();
  }, 60 * 1000)
}

// 停止
const handleStop = () => {
  status.value = !status.value;
  clearTimeout(timer!);
  onSubscribe();
}

// 清空
const handleClear = () => {
  codeData.value = [];
}


function splitStringIntoPairs(str: any) {
  return str.match(/.{1,2}/g).join(' ');
}

watch(data, async (newVal: any) => {
  if (newVal.key == channelSn.value) {
    newVal.value = splitStringIntoPairs(newVal.value)
    codeData.value.unshift(newVal);
  }
});

onMounted(async () => {
  subscribe();
  timerStart();
})


onBeforeUnmount(() => {
  onSubscribe();
  clearTimeout(timer!);
});

</script>

<style lang="less" scoped>
.content {
  width: 100%;
  padding: 20px;
  display: flex;
  box-sizing: border-box;
  flex-direction: column;
  background: var(--color-bg-1);

  .content-view {
    width: 100%;
    height: 74vh;
    overflow-y: hidden;
    box-sizing: border-box;
    color: var(--color-neutral-8);
    outline: none;
    box-shadow: none; /* 移除可能的阴影效果 */
    border: none;
    padding: 10px;
    background: var(--color-bg-3);

    ul {
      margin: 0;
      padding: 0;
      width: 100%;
      height: 100%;
      list-style: none;
      overflow-y: scroll;

      &::-webkit-scrollbar {
        display: none;
      }

      li {
        line-height: 20px;
        margin-bottom: 20px;
        transition: all 0.1s ease-out;

        p{
          margin: 0;
          color: rgb(var(--red-7));
          //font-weight: bold;
          font-size: 14px;
          margin-left: 4px;
        }
      }
    }

    .textarea {
      position: absolute;
      width: 100%;
      outline: none;
      box-shadow: none; /* 移除可能的阴影效果 */
      border: none;
      padding: 10px;
      overflow-y: hidden;
      box-sizing: border-box;
      color: var(--color-neutral-8);
      background: var(--color-neutral-2);
    }
  }
}

.list-enter-active,
.list-leave-active {
  //transition: all 0.2s ease;
}
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

</style>