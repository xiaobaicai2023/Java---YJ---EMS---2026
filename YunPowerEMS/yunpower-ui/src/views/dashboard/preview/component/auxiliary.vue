<template>
  <a-card title="辅助设置">
    <a-form ref="formRef" :model="form" auto-label-width>
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

      <!--图例位置-->
      <a-form-item field="legendPosition" label="图例位置">
        <a-radio-group v-model="form.legendPosition">
          <a-radio v-for="(item,index) in datav_legend_position" :key="index" :value="item.value">{{item.label}}</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {getCurrentInstance, ref} from "vue";
import {FormInstance} from "@arco-design/web-vue/es/form";

const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {datav_line_direction, datav_legend_position } = proxy?.useDict("datav_line_direction", "datav_legend_position");

// 辅助线
const formRef = ref<FormInstance>();

const form = ref({
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

defineExpose({
  form,
  formRef,
  setForm(val:any){
    // console.log(val, 'val')
    if(!val.lineChildren){
      val.lineChildren = [];
    }
    form.value = val;
  }
});
</script>

<style lang="less" scoped>
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