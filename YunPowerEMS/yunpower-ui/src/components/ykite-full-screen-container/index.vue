<template>
  <div id="ykite-full-screen-container" :style="style">
    <slot />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

// 用于getScale()函数
const fixedWidth = ref(document.documentElement.clientWidth.toString());
const fixedHeight = ref(document.documentElement.clientHeight.toString());

// 默认行内样式
const style = ref({
  'width': `${fixedWidth.value}px`,
  'height':  `${fixedHeight.value}px`,
  'transform': 'scale(1) translate(-50% -50%)',
})

onMounted(()=>{
  setScale(); // 初始化时就执行一次setScale
  window.onresize = debounce(setScale, 300); // 监听onresize事件，执行setSacle
})

const debounce = (fn, delay)=>{
  let timer;
  console.log('执行了debounce'); // 只执行一次，下面紧邻函数内的代码会在每次触发resize事件时执行
  return function(){
    // const args = arguments; // fn函数（即setScale函数）不必使用args作为fn的入参，
    // apply()方法作用：https://blog.csdn.net/weixin_43877799/article/details/120282509
    if(timer){
      clearTimeout(timer);
    }
    const context = this;
    timer = setTimeout(()=>{
      timer = null;
      console.log('正在执行自适应');
      fn.apply(context); // 或者可以直接运行fn();
    }, delay);
  }
}

// 获取放大缩小比例
const getScale = ()=>{
  const w = window.innerWidth / fixedWidth.value;
  const h = window.innerHeight / fixedHeight.value;
  return w<h? w: h;
}

// 修改样式
const setScale = ()=>{
  style.value.transform =  `scale${getScale()})) translate(-50% -50%)`;
  // 宽高自适应，window.innerWidth见文章：https://juejin.cn/post/6844903598489337869
  style.value.width =  `${(window.innerWidth).toString()}px`; // 赋值时不要使用document.documentElement.clientWidth
  style.value.height =  `${(window.innerHeight).toString()}px`;// 赋值时不要使用document.documentElement.clientHeight
}
</script>

<style scoped lang="less">
#ykite-full-screen-container{
  transform-origin: 0 0;
  transition: 0.2s; // 增大该值可以比较明显地看到自适应过程
}
</style>


