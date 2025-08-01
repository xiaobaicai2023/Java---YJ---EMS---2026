<template>
  <div :style="'height:' + height">
    <!-- <a-spin :loading="loading" :style="'height:' + height" style="display: block;width: 100%;" :tip="$t('global.loading')">
    </a-spin> -->
    <iframe :src="url" frameborder="no" style="width: 100%; height: 100%" scrolling="auto"></iframe>
  </div>
</template>

<script lang="ts" setup>
// import useLoading from "@/hooks/loading";
import { getToken } from "@/utils/auth";
// import { useTimeoutFn } from "@vueuse/core";
import { useRouter } from "vue-router";
/*************************** 变量区域 begin ***************************/
//路由信息
const router = useRouter()
const projectId = router.currentRoute.value.params.projectId;
console.log("projectId=>", projectId);
if (!projectId || projectId.length <= 0) {
  router.back();
}
const YKITE_DIAGRA = import.meta.env.VITE_YKITE_DIAGRA_URL;
const url = `${YKITE_DIAGRA}/diagram/design/${projectId}/${getToken()}`;
const height = window.innerHeight + "px;";
/*************************** 变量区域 end ***************************/



/*************************** 方法区域 begin ***************************/


/*************************** 方法区域 end ***************************/

</script>

<style lang="scss" scoped></style>
