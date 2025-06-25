<template>
  <div class="container">
    <a-space>
      <!-- 切换 日 月 年 start -->
      <a-radio-group v-model="extraActive" type="button" @change="radioChange">
        <a-radio v-for="(item,index) in timerData" :key="index" :value="item.value">
          {{ $t(item.label) }}
        </a-radio>
      </a-radio-group>
      <!-- 切换 日 月 年 end -->
      <div v-if="props.pickType === 0">
        <component
            :is="extraActive === 0 ?'a-date-picker' : extraActive === 1 ? 'a-month-picker' : 'a-year-picker'"
            v-model="pickerVal"
            :allow-clear="false"
            @change="pickerChange"
            :disabledDate="isCurrentDateDisabled ? (current:number) => dayjs(current).isAfter(dayjs()) : null">
        </component>
      </div>
      <div v-if="props.pickType === 1">
        <a-range-picker :allow-clear="false" v-model="rangePickerVal"
                        :mode="extraActive === 0 ? 'date' : extraActive === 1 ? 'month' : 'year'"
                        @change="onRangeChange"/>
      </div>
    </a-space>
  </div>
</template>
<script setup lang="ts">
import {computed, ref} from 'vue'
import dayjs from "dayjs";
import {onMounted} from 'vue';

const props = defineProps({
  // 0 为指定日期选择起 1 为范围选择器
  pickType: {
    type: Number,
    default: 0
  },
  // 是否需要禁用当前日期之后的时间
  isCurrentDateDisabled: {
    type: Boolean,
    default: false
  },
  // 日，月，年
  dateUnit: {
    type: Array,
    default: () => {
      return ['day', 'month', 'year']
    }
  },
  chartType: {
    type: Number,
    default: 0
  }
})
const timerData: any = computed(() => {
  return props.dateUnit.map((item: any): any => {
    switch (item) {
      case 'day':
        return {
          label: 'power.energy.preview.day',
          value: 0
        }
      case 'month':
        return {
          label: 'power.energy.preview.month',
          value: 1
        }
      case 'year':
        return {
          label: 'power.energy.preview.year',
          value: 2
        }
    }
  })
})
const emits = defineEmits(['change'])
// 选择的日期单位
const extraActive = ref<string | number | boolean>(props.chartType == 16 || props.chartType == 15? 2 : (props.dateUnit.length < 3 ? 1 : 0))
// const extraActive = ref<string | number | boolean>(1)
// 日期选择器 - 值
const pickerVal = ref<any>(extraActive.value == 0 ? dayjs().format("YYYY-MM-DD") : extraActive.value == 1 ? dayjs().format("YYYY-MM") : dayjs().format("YYYY"))
// 范围选择器 - 值
const rangePickerVal = ref<string[]>([dayjs().startOf('month').format("YYYY-MM-DD"), dayjs().format("YYYY-MM-DD")])
// 日-月-年 change
const radioChange = (index: string | number | boolean) => {
  extraActive.value = index
  if (props.pickType === 0) {
    pickerVal.value = dayjs()
    if(props.chartType == 16 || props.chartType == 15){
    }
    timerChange(index, extraActive.value == 0 ? dayjs().format("YYYY-MM-DD") : extraActive.value == 1 ? dayjs().format("YYYY-MM") : dayjs().format("YYYY"))
  } else {
    if (extraActive.value == 0) {
      rangePickerVal.value = [dayjs().startOf('month').format("YYYY-MM-DD"), dayjs().format("YYYY-MM-DD")]
    } else if (extraActive.value == 1) {
      rangePickerVal.value = [dayjs().startOf('year').format("YYYY-MM"), dayjs().format("YYYY-MM")]
    } else {
      rangePickerVal.value = [dayjs().format("YYYY"), dayjs().format("YYYY")]
    }
    timerChange(index, rangePickerVal.value)
  }
}
// 日期选择
const pickerChange = (val: string) => {
  timerChange(extraActive.value, val)
}
const timerChange = (val: string | number | boolean, timer: any) => {
  if (props.pickType === 1) {
    if (val == 0) {
      timer[0] = dayjs(timer[0]).format("YYYY-MM-DD")
      timer[1] = dayjs(timer[1]).format("YYYY-MM-DD")
    } else if (val == 1) {
      timer[0] = dayjs(timer[0]).format("YYYY-MM-DD")
      timer[1] = getLastDayOfMonth(timer[1])
    } else {
      timer[0] = dayjs(`${timer[0]}-01-01`).format("YYYY-MM-DD")
      timer[1] = getLastDayOfYear(timer[1])
    }
  }
  emits('change', {timeUnit: val, timer})
}


const getLastDayOfYear = (year: any) => {
  // 获取某年的第一天
  const firstDayOfYear = dayjs(`${year}-01-01`);

  // 将该日期设置为下一年的第一天
  const firstDayOfNextYear = firstDayOfYear.add(1, 'year');

  // 将该日期减去一天
  const lastDayOfYear = firstDayOfNextYear.subtract(1, 'day');

  return dayjs(lastDayOfYear).format("YYYY-MM-DD");
}

const getLastDayOfMonth = (dateString: any) => {
  // 使用 dayjs 函数创建一个日期对象，并将其设置为某月的第一天
  const firstDayOfMonth = dayjs(dateString);

  // 将该日期设置为下一个月的第一天
  const firstDayOfNextMonth = firstDayOfMonth.add(1, 'month');

  // 将该日期减去一天
  const lastDayOfMonth = firstDayOfNextMonth.subtract(1, 'day');

  return dayjs(lastDayOfMonth).format("YYYY-MM-DD");
}

// 范围选择器
const onRangeChange = (val: any) => {
  timerChange(extraActive.value, val)
}


onMounted(async () => {
  timerChange(extraActive.value, props.pickType == 0 ? pickerVal.value : rangePickerVal.value)
});

</script>
<style scoped lang="less">
.container {
  display: flex;
  align-items: center;
  justify-content: center;

  div {
    flex: 1;
  }
}
</style>
