<template>
    <iframe :src="weatherUrl" :style="iframeStyle"></iframe>
    <!--  <div id="tp-weather-widget" style="width: 100%;height: 400px"></div>-->
  </template>
  <script lang="ts" setup>
  const iframeStyle = {
    width: '100%',
    height: '430px',
    border: 'none',
    borderRadius: '5px'
  }
  const weatherUrl = `https://weather.seniverse.com/?token=${import.meta.env.VITE_WEATHER_API}`;

  </script>

  <style lang="less" scoped>

  </style>
