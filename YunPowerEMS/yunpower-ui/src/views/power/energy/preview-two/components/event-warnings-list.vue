<template>
  <TransitionGroup tag="ul" name="fade" class="container">
    <li v-for="item in warningsList" class="warnings-list-row" :key="item">
      <div class="warnings-list-text">
        <p :title="item.title">{{item.title}}</p>
        <p>{{item.timestamp}}</p>
      </div>
      <div class="warnings-list-btn">
        <a-button :status="item.status==1? 'success': item.status==2? 'danger': 'warning'"
          @click="buttonEvent(item)">{{item.status==1?
          '已恢复': item.status==2? '不处理': '待处理'}}</a-button>
      </div>
    </li>
  </TransitionGroup>
</template>
<script setup>
  import { ref, onMounted, computed } from 'vue';
  import { Notification } from '@arco-design/web-vue';
  import dayjs from "dayjs";

  // 按钮操作
  const buttonEvent = (data) => {
    Notification.info({
      title: '提示',
      content: data.title,
    })
  }

  // 初始化公告消息列表
  const warningsList = ref([
    { id: 1, title: 'title 1', status: 1, timestamp: '2024-5-8 12:00:00' },
    { id: 2, title: '[华一华邦多功能导轨表] 设备离线设备离线设备离线', status: 3, timestamp: '2024-5-8 12:00:00' },
    { id: 3, title: 'title 2', status: 1, timestamp: '2024-5-8 12:00:00' }
  ]);

  onMounted(() => {
    setInterval(() => {
      const newNotice = {
        id: Date.now(),
        title: `这是新的警告消息 ${Math.floor(Math.random() * 100) + 1}`,
        timestamp: dayjs().format("YYYY-MM-DD HH:mm")
      };

      insertNotice(newNotice);
    }, 5000);
  });

  // 插入新消息
  const insertNotice = (newNotice) => {
    warningsList.value.unshift(newNotice);
  }

</script>
<style lang="less" scoped>
  /* 标题大小 */
  @titleSize: 16px;
  /* 文本大小 */
  @textSize: 12px;

  .container {
    position: relative;
    padding: 0;
    margin: 0;
    list-style-type: none;
  }

  p {
    width: 100%;
    list-style: none;
    padding: 0;
    margin: 0;
    overflow: hidden;
  }

  .warnings-list-row {
    padding: 7px 0px;
    margin-bottom: 4px;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: space-between;
    box-sizing: border-box;
    border-bottom: 1px solid var(--color-neutral-4);
    transition: all 0.5s ease-out;

    &:nth-last-child(1) {
      border-bottom: none;
    }
  }

  /* 过渡效果 */
  .fade-move,
  .fade-enter-active,
  .fade-leave-active {
    transition: all 0.5s cubic-bezier(0.55, 0, 0.1, 1);
  }

  /* 进入和离开的状态 */
  .fade-enter-from,
  .fade-leave-to {
    opacity: 0;
    transform: scaleY(0.01) translate(30px, 0);
  }

  /* 离开的项目被移除出了布局流 */
  .fade-leave-active {
    position: absolute;
  }

  .warnings-list-text {
    margin-right: 10px;
    cursor: pointer;
    flex: 1;
    overflow: hidden;
    box-sizing: border-box;

    p {
      overflow: hiddden;
      text-overflow: ellipsis;
      white-space: nowrap;

      &:nth-child(1) {
        font-size: @titleSize;
      }

      &:nth-child(2) {
        font-size: @textSize;
        color: var(--color-neutral-6);
        margin-top: 4px;
      }
    }
  }

  .warnings-list-btn {
    flex-shrink: 0;
  }

</style>