<template>
  <a-grid :cols="6" :colGap="12" :rowGap="12">
    <a-grid-item v-for="(item,index) in 20" :key="index">
      <a-card>
        <!--水表状态、表底数-->
        <div class="water-number">
          <span :class="index==2||index==8||index==7?'water-warn':''"></span>
          <a-statistic :value="44109" :precision="2"/>
        </div>
        <!--安装位置-->
        <p class="water-local">西1公寓西南＃东主干</p>
        <!--距上次上传时间、口径-->
        <p class="water-device">
          <span>28068393</span>
          <span>D200</span>
        </p>
        <a-progress color="rgb(var(--arcoblue-5))" :stroke-width="5" :percent="0.2" :show-text="false"/>
      </a-card>
    </a-grid-item>
  </a-grid>
</template>

<script setup lang="ts">

</script>

<style scoped lang="less">
.water-number {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;

  span {
    width: 26px;
    height: 26px;
    background-color: rgb(var(--green-5));
    border-radius: 50%;
    flex-shrink: 0;
  }

  .water-warn {
    background: rgb(var(--red-6));
  }
}

.water-local {
  width: 100%;
  text-align: right;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.water-device {
  width: 100%;
  margin-bottom: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  span {
    display: inline-block;
    width: 50%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;

    &:nth-child(2) {
      text-align: right;
    }
  }
}

:deep(.arco-statistic) {
  max-width: 80%;
}

:deep(.arco-statistic-content .arco-statistic-value) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>