<template>
  <div class="container">
    <div class="content">
      <a-result class="result" status="404" :subtitle="'你没有访问权限'"> </a-result>
      <div class="operation-row">
        <a-button key="back" type="primary" @click="back"> 返回 </a-button>
      </div>
    </div>
  </div>

</template>

<script lang="ts" setup>
  import { useRouter } from 'vue-router'

  const router = useRouter()
  const back = () => {
    // warning： Go to the node that has the permission
    router.go(-1);
  }
</script>

<style scoped lang="less">
.container{
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
  .content {
    // padding-top: 100px;
    //position: absolute;
    //top: 50%;
    //left: 50%;
    //margin-top: -121px;
    //margin-left: -95px;
    text-align: center;
  }
</style>
