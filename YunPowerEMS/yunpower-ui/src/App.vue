<template>
  <a-config-provider :locale="locale">
    <router-view/>
    <global-setting/>
  </a-config-provider>
</template>

<script lang="ts" setup>
import {computed, provide, ref} from 'vue';
import enUS from '@arco-design/web-vue/es/locale/lang/en-us';
import zhCN from '@arco-design/web-vue/es/locale/lang/zh-cn';
import {useDark, useToggle} from '@vueuse/core'
import GlobalSetting from '@/components/global-setting/index.vue';
import useLocale from '@/hooks/locale';
const {currentLocale} = useLocale();
import {DeviceVarAlarmMessage, SocketMessage, useSocket, WebSocketBaseMessage} from '@/api/websocketService'
import {useAppStore} from "@/store";

const locale = computed(() => {
  switch (currentLocale.value) {
    case 'zh-CN':
      return zhCN
    case 'en-US':
      return enUS
    default:
      return enUS
  }
})
const appStore = useAppStore();
const isDark = useDark({
  selector: 'body',
  attribute: 'arco-theme',
  valueDark: 'dark',
  valueLight: 'light',
  storageKey: 'arco-theme',
  onChanged(dark: boolean) {
    // overridden default behavior
    appStore.toggleTheme(dark)
  },
})
useToggle(isDark);

const webSocketData = ref<SocketMessage | DeviceVarAlarmMessage>({
  value: 0
})
const {on, send} = useSocket(import.meta.env.VITE_WEBSOCKET_URL);
/**
 * 收到服务端数据
 */
on('message', (message: WebSocketBaseMessage) => {
  webSocketData.value = message.bizData;
});
/**
 * 像socket 发送订阅消息
 * @param message
 */
const sendData = (message: { action: string, bizSn: any}) => {
  const params = {
    action: message.action,
    bizSn: message.bizSn
  }
  if(params.bizSn && params.bizSn.length > 0){
    send(JSON.stringify(params))
  }
}

provide('webSocketData', {
  data: webSocketData,
  sendData:sendData
});
</script>
