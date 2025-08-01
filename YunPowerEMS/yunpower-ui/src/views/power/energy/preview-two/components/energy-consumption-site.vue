<template>
  <a-spin>
    <div class="energy-consumption-site">
      <!-- 图标 -->
      <div class="site-wrapper" :style="{ background: isDark ? 'rgb(var(--green-3))' : 'rgb(var(--green-6))' }">
        <custom-icon class="site-icon" type="icon-ykite-hot-spot-signal" :size="34"></custom-icon>
      </div>
      <!-- 站点通讯 -->
      <p :style="{ color: isDark? 'var(--color-neutral-8)' : 'rgb(var(--green-6))'}">{{$t('power.energy.preview.communication')}}</p>
    </div>
  </a-spin>
</template>
<script setup>
  import useThemes from '@/hooks/themes';
  const { isDark } = useThemes();
</script>
<style lang="less" scoped>
  @normalBgColor: rgb(var(--green-6));
  @wranBgColor: rgb(var(--red-6));

  .arco-spin {
    width: 100%;
  }

  /* 初始化p标签 */
  p {
    list-style: none;
    margin: 0;
    padding: 0;
  }

  /* 图标、文字居中 */
  .energy-consumption-site {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-left: 20px;
    color: @normalBgColor;

    .site-wrapper {
      padding: 10px;
      border-radius: 50%;

      .site-icon {
        color: #fff;
      }
    }

    p {
      margin-top: 12px;
    }
  }
</style>
