<template>
  <router-view v-slot="{ Component, route }">
    <breadcrumb v-if="route.path.lastIndexOf('diagramPreview') == -1 " :items="routeList"/>
    <transition name="fade" mode="out-in" appear>
      <component
          :is="Component"
          v-if="route.meta.ignoreCache"
          :key="route.fullPath"
      />
      <keep-alive v-else :include="cacheList">
        <component :is="Component" :key="route.fullPath"/>
      </keep-alive>
    </transition>
  </router-view>
</template>

<script lang="ts" setup>
import {computed, watchEffect, onBeforeUnmount, ref, h, onMounted, onUnmounted, nextTick, inject, watch} from 'vue'
import {useRoute} from 'vue-router'
import {useTabBarStore} from '@/store'
import {Notification} from '@arco-design/web-vue'
import {useRouter} from 'vue-router';
import {SocketData} from "@/api/websocketService";

const tabBarStore = useTabBarStore()

const cacheList = computed(() => tabBarStore.getCacheList)


const route = useRoute()
let routeList = ref<any[]>([])
let timerId: number | undefined
// 监听路由变化
watchEffect(() => {
  routeList.value = route.matched
      .filter(item => item.meta.locale)
      .map(item => item.meta.locale)
})

// 监听路由变化
onBeforeUnmount(() => {
  console.log(routeList.value)
})




</script>

<style scoped lang="less">
.nav-title {
  margin-top: 0;
  color: var(--color-neutral-10);
}

.breadcrumb-item-text {
  font-size: 16px;
}

</style>
