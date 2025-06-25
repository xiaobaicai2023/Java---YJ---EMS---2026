import { ref, onMounted, onUnmounted } from 'vue';

export function useFullscreen() {
  // 当前全屏状态
  const isFullscreen = ref(Boolean(document.fullscreenElement));

  // 切换全屏状态
  const toggleFullscreen = () => {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen();
    } else {
      if (document.exitFullscreen) document.exitFullscreen();
    }
    isFullscreen.value = !isFullscreen.value;
  };

  // 监听全屏状态变化
  const onFullscreenChange = () => {
    isFullscreen.value = Boolean(document.fullscreenElement);
  };

  // 在组件挂载时添加监听器
  onMounted(() => {
    document.addEventListener('fullscreenchange', onFullscreenChange);
  });

  // 组件卸载时移除监听器
  onUnmounted(() => {
    document.removeEventListener('fullscreenchange', onFullscreenChange);
  });

  return { isFullscreen, toggleFullscreen };
}