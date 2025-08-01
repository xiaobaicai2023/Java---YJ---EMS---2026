<template>
  <div class="calendar-box">
    <div class="calendar">
      <a-card>
        <template #title>
          历史用电量详细
        </template>
        <!-- 切换日期 start -->
        <template #extra>
          <div class="calendar-operate">
            <icon-caret-left size="16" @click="changeMonth('prev')" />
            <div class="calendar-operate__title">{{ dateText }}</div>
            <icon-caret-right size="16" @click="changeMonth('next')" />
          </div>
        </template>
        <a-spin class="energy-box" :loading="loading" style="width: 100%" :tip="$t('global.loading')">
          <!-- 切换日期 end -->
          <!-- 日历 start -->
          <div class="calendar-header">
            <span v-for="(item, index) in weekMapZh" :key="index" class="calendar-header__item">{{ item }}</span>
          </div>
          <div class="calendar-content" :data-month="date.getMonth() + 1">
            <template v-for="(item, index) in calendarTable" :key="index">
              <div class="calendar-content__item"
                :style="{ backgroundColor: item.backGroundColor ? item.backGroundColor : '#CEF3DC' }"
                :class="[{ light: !item.isCurrentMonth }]" @click="lightClick(item.day, item.isCurrentMonth)">
                <div :class="[{ active: isActive(item) }]">
                  <span>{{ item.day }}日</span>
                </div>
                <div class="calendar-content__battery">
                  {{ item.battery }}
                </div>
              </div>
            </template>
          </div>
          <!-- 日历 end -->
          <div class="illustrate-box">
            <div class="title">
              用电登记（度）
            </div>
            <div class="process">
              <template v-for="(item, index) in ['#CEF3DC', '#E9F5F0', '#FBF1E6', '#F9DBC1', '#F7EBE9', '#F4C8CA']">
                <div :style="{ backgroundColor: item }">{{ index * 250 }}</div>
              </template>
            </div>
          </div>
        </a-spin>
      </a-card>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, onUnmounted, ref, watchEffect } from 'vue';
import { CalendarItem, generateCalendar, isAllTrue, weekMapZh } from './calendar';
import { PowerModuleEunm, getChart } from '@/api/system/home-power';
import useLoading from '@/hooks/loading';
import dayjs from 'dayjs';
import { useIntervalFn } from '@vueuse/core';

//加载中
const { loading, setLoading } = useLoading(false);
//图表数据
const chartData = ref<any>({});
// 当前时间
const date = ref<Date>(new Date());
//日历
const calendarTable = ref<CalendarItem[]>([]);
//生成日历
calendarTable.value = generateCalendar(date.value)

//计算当前月份
const dateText = computed(() => {
  return `${date.value.getFullYear()}年${date.value.getMonth() + 1}月`;
});
// 是否是今天
const isToday = computed(() => {
  const current = new Date();
  const validArr = [
    date.value.getFullYear() === current.getFullYear(),
    date.value.getMonth() === current.getMonth(),
    date.value.getDay() === current.getDay(),
  ];
  return isAllTrue(validArr);
});


/**
 * 当天日期高亮显示, 兼容切换日期：
 * 年月日都要对上才能高亮
 * ps: 日历可能会显示下月/上月的同样日期， 仅当月日期高亮
 */
const isActive = (item: CalendarItem) => {
  return isAllTrue([
    item.day === date.value.getDate(),
    item.isCurrentMonth,
    item.month === new Date().getMonth(),
    item.year === new Date().getFullYear(),
  ]);
};


// 切换到今天
const currentDate = () => {
  date.value = new Date();
};

/**
 * 切换月份, 上个月 or 下个月
 * @param type
 */
const changeMonth = async (type: 'prev' | 'next') => {
  let month: number;
  let year: number;
  if (type === 'prev') {
    month = date.value.getMonth() === 0 ? 11 : date.value.getMonth() - 1;
    year = month === 11 ? date.value.getFullYear() - 1 : date.value.getFullYear();
  } else {
    month = date.value.getMonth() === 11 ? 0 : date.value.getMonth() + 1;
    year = month === 0 ? date.value.getFullYear() + 1 : date.value.getFullYear();
  }
  let day = 0;
  if (month === new Date().getMonth()) {
    day = new Date().getDate()
  }
  date.value.setDate(day);
  date.value.setMonth(month);
  date.value.setFullYear(year);
  date.value = new Date(date.value);
  console.log("calendarTable", calendarTable.value);
  calendarTable.value = generateCalendar(date.value)
  await fetchData();
};

/**
 * 点击上个月或者下个月的 日期
 * @param day
 * @param isCurrentMonth
 */
const lightClick = (day: number, isCurrentMonth: boolean) => {
  if (isCurrentMonth) return
  if (day < 15) {
    changeMonth('next')
  }
  if (day > 15) {
    changeMonth('prev')
  }
}


/**
 * 左补0
 * @param number
 * @param targetLength
 */
const padLeftZero = (number: number, targetLength: number) => number.toString().padStart(targetLength, '0');


/**
 * 获取日期数据
 * @param param 时间参数
 */
const fetchData = async () => {
  setLoading(true);
  try {
    let res = await getChart({ pageValue: PowerModuleEunm.electric_use_detail, "monthTime": `${date.value.getFullYear()}-${padLeftZero(date.value.getMonth() + 1, 2)}` });
    if (res.code == 200) {
      if (res.data.xAxis && res.data.yAxis && res.data.yAxis.length == 1 && res.data.yAxis[0].dataList && res.data.xAxis.length == res.data.yAxis[0].dataList.length) {
        let map = new Map();
        let data = res.data.yAxis[0].dataList;
        res.data.xAxis.forEach((item: any, index: number) => {
          map.set(item, data[index]);
        })
        calendarTable.value.forEach((item: any) => {
          item.battery = null;
          if (item.isCurrentMonth) {
            item.battery = map.get(`${item.year}-${padLeftZero(item.month + 1, 2)}-${padLeftZero(item.day, 2)}`);
          }
          item.backGroundColor = item.battery >= 1250 ? '#F4C8CA'
            : item.battery >= 1000 ? '#F7EBE9'
              : item.battery >= 750 ? '#F9DBC1'
                : item.battery >= 500 ? '#FBF1E6'
                  : item.battery >= 250 ? '#E9F5F0'
                    : '#CEF3DC';
        })
        console.log("历史用电量详细", calendarTable.value)
      }
    }
  } catch (ex) {
    console.error("历史用电量详细出错", ex)
  } finally {
    setLoading(false);
  }
}


/**
 * 5分钟定时-历史用电量详细
 *
 */
const { resume: startInterval, pause: stopInterval } = useIntervalFn(async () => {
  console.log("5分钟定时-历史用电量详细")
  await fetchData();
}, 1000 * 60 * 5)

/**
 * 组件加载完成
 */
onMounted(async () => {
  await fetchData();
  startInterval();
})

/**
 * 组件卸载
 */
onUnmounted(() => {
  stopInterval()
})


</script>
<style lang="less" scoped>
@gap: 8px;
@sub-active-color: #dbf0ff;
@active-color: rgb(var(--primary-5));
@gray: #979797;

.button {
  height: 28px;
  font-size: 12px;
  background: #fff;
  margin: 0;
  padding: 0 16px;
  border: 1px solid rgba(@gray, 0.4);
  border-radius: 14px;
  cursor: pointer;

  &:hover {
    color: @active-color;
  }

  &:active {
    background-color: rgba(0, 0, 0, 6%);
  }

  &:disabled {
    color: @gray;
    background-color: rgba(0, 0, 0, 6.5%);
    cursor: not-allowed;
  }
}

.button-group {
  .button:first-child {
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
  }

  .button:last-child {
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
    border-left: 0;
  }

  .button:not(:first-child, :last-child) {
    border-radius: 0;
    border-left: 0;
  }
}

.calendar-operate {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 16px;
  position: relative;

  :hover {
    cursor: pointer;
  }

  &__title {
    display: flex;
    align-items: center;
    justify-content: center;
    flex: 1;
    margin: 0 8px;
  }
}

.calendar-header {
  margin-top: 6px;
  //padding: 8px 0;
  font-size: 14px;
  font-weight: bold;
  display: flex;

  &__item {
    flex: 1;
    text-align: center;
    border-radius: 1px;

    &.gray {
      color: @gray;
      font-weight: normal;
    }
  }
}

.calendar-content {
  display: flex;
  flex-wrap: wrap;
  margin: 10px 0;
  position: relative;
  color: #333;

  &__battery {
    text-align: left;
    color: red;
  }

  &__item {
    min-width: 60px;
    height: 45px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    flex: calc(14.2% - @gap);
    border-radius: 3px;
    box-sizing: border-box;
    text-align: right;
    padding: 4px;
    margin: 4px;
    user-select: none;
    transition: all 0.2s ease;

    &:nth-child(7n) {
      margin-right: 0;
    }

    .active {
      text-align: right;

      span {
        display: inline-block;
        background: white;
        padding: 0 5px;
        border-radius: 3px;
      }

      //border-bottom: 2px solid @active-color;
    }

    &.light {
      color: rgba(@gray);
      cursor: pointer;
    }

    &.light:hover {
      background-color: rgba(@sub-active-color, 0.4);
    }
  }
}

.arco-card {
  border-radius: 5px;
  height: 460px;
}

.illustrate-box {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  user-select: none;

  .title {
    width: 100px;
  }

  .process {
    color: #1d2129;
    width: calc(100% - 100px);
    display: flex;

    div {
      width: 25%;
      margin-right: 10px;
      padding: 2px 0;
    }
  }
}
</style>
