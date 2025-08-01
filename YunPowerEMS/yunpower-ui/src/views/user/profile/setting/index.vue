<template>
  <div class="container">
    <a-row style="margin-bottom: 16px">
      <a-col :span="24">
        <UserPanel :isUpdate="isUpdate"/>
      </a-col>
    </a-row>
    <a-row class="wrapper">
      <a-col :span="24">
        <a-tabs default-active-key="1" type="rounded">
          <a-tab-pane key="1" title="基础信息">
            <BasicInformation @handleSave="handleSave"/>
          </a-tab-pane>
          <a-tab-pane key="2" title="修改密码">
            <SecuritySettings/>
          </a-tab-pane>
        </a-tabs>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import UserPanel from './components/user-panel.vue';
import BasicInformation from './components/basic-information.vue';
import SecuritySettings from './components/security-settings.vue';
import {ref} from "vue";

const isUpdate = ref<any>(0);

const handleSave = () => {
  isUpdate.value += 1;
}
</script>

<script lang="ts">
export default {
  name: 'Setting',
};

</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

.wrapper {
  padding: 20px 0 0 20px;
  min-height: 580px;
  background-color: var(--color-bg-3);
  border-radius: 4px;
}

:deep(.section-title) {
  margin-top: 0;
  margin-bottom: 16px;
  font-size: 14px;
}
</style>