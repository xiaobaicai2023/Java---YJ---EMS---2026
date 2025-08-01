<template>
  <a-card title="辅助设置">
    <a-form ref="formRef" :rules="rules" :model="form" auto-label-width>
      <!--辅助线-->
<!--      <a-form-item field="lineDirection" label="辅助线">-->
<!--        <a-radio-group v-model="form.lineDirection">-->
<!--          <a-radio v-for="(item,index) in guideList" :key="index" :value="item.value">{{item.label}}</a-radio>-->
<!--        </a-radio-group>-->
<!--      </a-form-item>-->
      <!--阈值-->
<!--      <a-form-item field="lineChildren" label="阈值">-->
<!--        <a-grid :cols="2" :colGap="12" :rowGap="16">-->
<!--          <a-grid-item v-for="(item,index) in form.lineChildren" :key="index">-->
<!--            <a-space>-->
<!--              <a-checkbox v-model="item.status" @change="handleCheck(item, index)">-->
<!--                {{ item.label }}-->
<!--              </a-checkbox>-->
<!--              <a-input v-model="item.lineName" placeholder="名称" :disabled="!item.status" style="width: 150px" />-->
<!--              <a-input v-model="item.lineValue" placeholder="值" :disabled="!item.status" style="width: 150px" />-->
<!--            </a-space>-->
<!--          </a-grid-item>-->
<!--        </a-grid>-->
<!--      </a-form-item>-->

      <!--辅助线-->
      <a-form-item field="isShowLine" label="辅助线">
        <a-checkbox v-model="form.isShowLine" @change="handleIsShowLine">是</a-checkbox>
      </a-form-item>

      <template v-if="form.isShowLine">
        <!--添加辅助线-->
        <a-form-item>
          <a-button type="outline" @click="addGuide" :disabled="form.lineChildren.length>=4">添加辅助线</a-button>
          <a-tooltip content="最多可添加4条" position="top" mini>
            <icon-exclamation-circle class="guide-tip" />
          </a-tooltip>
        </a-form-item>

        <!--横线竖向-->
        <a-form-item v-if="form.lineChildren.length>0">
          <a-row :gutter="[0, 8]">
            <a-col v-for="(item,index) in form.lineChildren" :key="index" :span="24">
              <a-radio-group v-model="item.lineDirection">
                <a-radio v-for="(itemTwo, indexTwo) in datav_line_direction" :key="indexTwo" :value="itemTwo.value">{{itemTwo.label}}</a-radio>
              </a-radio-group>
              <a-input v-model="item.lineName" placeholder="辅助线名称" style="width: 160px;margin-right: 8px" />
              <a-input v-model="item.lineValue" placeholder="值" style="width: 150px" />
              <icon-close-circle-fill class="delete-icon" @click="handleDeleteLine(index)" />
            </a-col>
          </a-row>
        </a-form-item>
      </template>

      <!--4：堆叠图-->
      <template v-if="props.chartType!=4">
        <!--显示类型-->
        <a-form-item field="percentage" label="显示类型">
          <a-radio-group v-model="form.percentage">
            <a-radio v-for="(item,index) in datav_percentage" :value="Number(item.value)" :key="index">{{ item.label }}
            </a-radio>
          </a-radio-group>
        </a-form-item>

        <!-- 基准值-->
        <a-form-item field="baseValue" label="基准值" v-if="form.percentage==2||form.percentage==3">
          <a-input-number v-model="form.baseValue" hideButton placeholder="请输入" />
        </a-form-item>
      </template>

    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {getCurrentInstance, ref} from "vue";
import {FormInstance} from "@arco-design/web-vue/es/form";
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {datav_percentage, datav_line_direction, datav_legend_position} = proxy?.useDict("datav_percentage", "datav_line_direction", "datav_legend_position");

const props = defineProps({
  chartType: {
    type: Number,
    default: 5,
  }
});

const formRef = ref<FormInstance>();
const rules:any = [{}];
const form = ref<any>({
  percentage: 1,
  baseValue: 0,
  isShowLine: false, //辅助线
  legendPosition: 2, //图例位置（1上 2下 3左 4右）
  lineChildren: []
});

// 添加辅助线
const addGuide = () => {
  const newGuide = {
    lineName: '',
    lineValue: '',
    lineDirection: 0
  };
  form.value.lineChildren.push(newGuide);
};

// 删除辅助线
const handleDeleteLine = (index: number) => {
  form.value.lineChildren.splice(index,1);
};

// 是否显示辅助线
const handleIsShowLine = (val: any) => {
  if(!val){
    form.value.lineChildren = [];
  }
};

const resetForm = () => {
  form.value = {
    percentage: 1,
    baseValue: 0,
    isShowLine: false, //辅助线
    legendPosition: 2, //图例位置（1上 2下 3左 4右）
    lineChildren: []
  }
};

defineExpose({
  form,
  formRef,
  resetForm,
  setForm: (data: any) => {
    if(!data.lineChildren){
      data.lineChildren = [];
    }
    form.value = data;
  },
})
</script>

<style scoped lang="less">
.guide-tip{
  color:var(--color-neutral-8);
  cursor: pointer;
  font-size: 20px;
  margin-left: 10px;
}

.delete-icon{
  font-size: 20px;
  margin-left: 12px;
}
</style>
