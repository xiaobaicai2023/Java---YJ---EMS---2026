<template>
  <a-row class="statistics-card-main" :class="{ 'no-background': cardInfo.unBgColor }">
    <a-col class="statistics-card-title">
      <ul>
        <li v-if="cardInfo.iconColor" class="statistics-card-icon" :class="cardInfo.iconColor"></li>
        <li>{{ cardInfo.title }}</li>
        <li class="statistics-card-unit" v-if="cardInfo.unit">/{{ cardInfo.unit }}</li>
      </ul>
    </a-col>
    <a-col class="statistics-card-content">
      {{ cardInfo.content }}
    </a-col>
  </a-row>
</template>

<script lang="ts" setup>
defineProps({
  titleClass: {
    type: String,
    default: "",
  },
  contentClass: {
    type: String,
    default: "",
  },
  cardInfo: {
    type: Object,
    default() {
      return {
        title: "",
        content: 0,
        iconColor: "",
        unBgColor: false
      };
    },
  },
});
</script>

<style lang="less" scoped>
.statistics-card-main {
  height: 70px;
  background: #f1f1f1;
  padding: 10px;

  &.no-background {
    background: none;
  }

  .statistics-card-title {
    ul {
      list-style: none;
      display: flex;
      align-items: center;
      padding: 0;
      margin: 0;

      li {
        margin-right: 8px;

        &:last-child {
          margin-right: 0;
        }

        &.statistics-card-icon {
          width: 6px;
          height: 6px;
          border-radius: 50%;

          &.lightGreen {
            background: lightgreen;
          }

          &.green {
            background: green;
          }

          &.deepGreen {
            background: rgb(4, 76, 4);
          }

          &.orange {
            background: orange;
          }
        }

        &.statistics-card-unit {
          color: rgb(182, 182, 182);
        }
      }
    }
  }

  .statistics-card-content {
    font-size: 20px;
  }
}
</style>
