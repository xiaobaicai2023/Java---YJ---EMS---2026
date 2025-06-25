<template>
  <a-card title="数据选择" class="list-card">
    <!--单位-->
    <a-typography>
      <a-typography-title style="font-weight: bold" :heading="6">
        <icon-home/>
        单位
      </a-typography-title>
      <a-typography-paragraph style="padding-left: 18px;color: var(--color-neutral-7)">{{ companyData?.entName }}
      </a-typography-paragraph>
    </a-typography>
    <!-- 站点-->
    <a-typography>
      <a-typography-title style="font-weight: bold" :heading="6">
        <icon-wifi/>
        站点
      </a-typography-title>
      <a-typography-paragraph style="padding-left: 18px;color: var(--color-neutral-7)">{{
          companyData?.stationName
        }}
      </a-typography-paragraph>
    </a-typography>
    <a-divider dashed margin="10px"/>

    <template v-if="![13,15,16,17,18].includes(data.chartType)">
      <a-typography>
        <a-typography-title style="font-weight: bold" :heading="6">
          <icon-relation/>
          变量
          <a-tooltip v-if="data.chartType!=2" content="模拟量和状态量只能二选一" position="top" mini>
            <icon-exclamation-circle style="color:var(--color-neutral-8);cursor: pointer"/>
          </a-tooltip>
        </a-typography-title>
      </a-typography>
      <a-space direction="vertical" class="variable-wapper">
        <p>
          <i></i>模拟量<i @click="addVar(1)">
          <custom-icon :size="16" type="icon-ykite-jia"></custom-icon>
        </i>
        </p>
        <div>
          <p class="tag" v-for="(item, index) of form.varList" v-show="item.varName" :key="index">
            <span>{{ item.varName }}</span>
            <icon-close-circle class="tag-delete" @click="handleRemove(index)"/>
          </p>
        </div>
      </a-space>
      <!--状态量显示-->
      <a-space v-if="data.chartType==13" direction="vertical" class="variable-wapper">
        <p>
          <i></i>状态量<i @click="addVar(2)">
          <custom-icon :size="16" type="icon-ykite-jia"></custom-icon>
        </i>
        </p>
        <a-space direction="vertical" fill>
          <p class="tag" v-for="(item, index) of form.varList" v-show="item.varName" :key="index">
            <span>{{ item.varName }}</span>
            <icon-close-circle class="tag-delete" @click="handleRemove(index)"/>
          </p>
        </a-space>
      </a-space>
    </template>
    <!-- 变量-->

    <!--弹窗-->
    <select-var-modal :visible="selectVarVisible"
                      :var-type="varType"
                      :station-type="Number(companyData?.stationType)"
                      :deptId="Number(companyData?.deptId)"
                      :is-accumulation="data.chartType==4?1:0"
                      @cancel="handleModalClose" @add="handleDeviceVarSelect"/>
  </a-card>
</template>
<script lang="ts" setup>
import {onMounted, ref, watchEffect} from "vue";
import {useRoute, useRouter} from "vue-router";
import {getConfigInfo} from "@/api/dashboard/api";
import SelectVarModal from '@/views/manage/station/components/select-var-modal/index.vue';
import useLoading from "@/hooks/loading";

const props = defineProps({
  varData: {
    type: Array,
    default: []
  },
  data: {
    type: Object,
    default: () => {
      return {
        baseParams: {}
      }
    }
  },
  companyData: {
    type: Object,
    default: () => {
      return {}
    }
  }
});

const varType = ref<number>(1);
//路由
const router = useRouter();
const route = useRoute();
const emit = defineEmits(['handleVarChange', 'handleVarDelete']);

// 弹窗显示隐藏
const selectVarVisible = ref(false);
const {loading, setLoading} = useLoading(false);
// 默认
const form = ref<any>({
  deviceId: 12, //设备id
  varName: '', //设备名称
  varList: [], //变量配置
  children: []
});

// 添加变量
const addVar = (id: number) => {
  varType.value = id;
  selectVarVisible.value = true;
}

// 删除tag
const handleRemove = (index: number) => {
  form.value.varList.splice(index, 1)
  emit('handleVarDelete', form.value.varList);
};

// 确认添加
const handleDeviceVarSelect = (record: any) => {
  const {unit, varName, varSn} = record;
  const obj1 = {
    unit,
    varName,
    varSn,
  };
  if (props.data.chartType == 6) {
    form.value.varList.push({...obj1})
  } else {
    form.value.varList[0] = obj1
  }
  emit('handleVarChange', form.value.varList);
  handleModalClose();
};

// 关闭弹窗
const handleModalClose = () => {
  selectVarVisible.value = false;
};


watchEffect(() => {
  props.varData.map((val: any, ind: number) => {
    if (val.varSn == "") {
      props.varData.splice(ind, 1)
    }
  })
  form.value.varList = props.varData;
  // companyData.value = {};
});

onMounted(() => {

})
</script>
<style lang="less" scoped>

.list-wrapper {
  width: 100%;
  height: 100%;
  position: sticky;
  z-index: 111;
}

.list-card {
  width: 100%;
  height: 100%;
  position: sticky;
  box-shadow: 1px 1px 1px 0px rgba(0, 0, 0, 0.1);

  //:deep(.arco-card-header){
  //  background: var(--color-neutral-2);
  //}

  .variable-wapper {
    width: 100%;
    padding-left: 16px;
    box-sizing: border-box;

    p {
      padding: 0;
      margin: 0;
      display: flex;
      align-items: center;

      i {
        &:nth-child(1) {
          display: inline-block;
          width: 6px;
          height: 6px;
          background: #999999;
          border-radius: 50%;
          vertical-align: middle;
          margin-right: 8px;
        }

        &:nth-last-child(1) {
          margin-left: 8px;

          &:hover {
            cursor: pointer;
          }
        }
      }
    }

    .tag {
      margin-bottom: 4px;

      & > span {
        background: var(--color-bg-6);
        border-radius: 2px;
        padding: 0 10px;
      }

      .tag-delete {
        font-size: 18px;
        margin-left: 8px;
        color: #999;
      }
    }
  }
}

:deep(.arco-typography) {
  margin-top: 0;
  margin-bottom: 6px;
}

:deep(.arco-tag-size-medium) {
  margin-bottom: 6px;

  &:hover {
    background: var(--color-neutral-3);
  }
}


</style>
