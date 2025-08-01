<template>
  <a-card :bordered="false">
    <a-spin style="width: 100%; height: 100%" :loading="loading">
      <a-row>
        <a-col :span="4">
          <upLoad :imgUrl="file.url"
                  @upload="imgUpload"
                  uploadUrl="/system/user/profile/avatar"
                  fileName="avatarfile"
                  show-remove-button
                  :delete="false"
                  class="upload-view">
          </upLoad>
        </a-col>
        <a-col :span="14">
          <a-descriptions
              :data="renderData"
              :column="2"
              align="left"
              layout="inline-horizontal"
              :label-style="{
          width: '68px',
          fontWeight: 'bold',
          color: 'rgb(var(--gray-8))',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
          whiteSpace: 'nowrap',
        }"
              :value-style="{
          width: '300px',
          paddingLeft: '8px',
          textAlign: 'left',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
          whiteSpace: 'nowrap',
        }"
          >
            <template #label="{ label }">{{ label }} :</template>
            <template #value="{ value, data }">
              <span>{{ value }}</span>
            </template>
          </a-descriptions>
        </a-col>
      </a-row>
    </a-spin>
  </a-card>
</template>

<script lang="ts" setup>
import {computed, nextTick, onMounted, ref, watchEffect} from 'vue';
import {useUserStore} from '@/store';
import upLoad from "@/components/upload/index.vue";
import {Notification} from "@arco-design/web-vue";
import {getUserProfile} from "@/api/user/user-center";
import useLoading from "@/hooks/loading";
import {notification} from "@/hooks/my-design";
const userStore: any = useUserStore();

//加载中
const {loading, setLoading} = useLoading(false);
const avatar = computed(() => {
  return userStore.user.headPic ? userStore.user.headPic :loadImage();
});

/**
 * 加载图片
 */
const loadImage = () => {
  const result = (userStore.user.id % 80) + 1;
  const resultFormat = result.toString().padStart(2, '0');
  const path = `/src/assets/heads/head_${resultFormat}.jpg`;
  const modules: Record<string, any> = import.meta.glob("@/assets/heads/*.{png,svg,jpg,jpeg}", {eager: true});
  if (modules[path]) {
    return modules[path].default;
  } else {
    // 地址错误
    console.error("头像地址错误");
  }
}

const props = defineProps({
  isUpdate: {
    type: Number,
    default: 0
  }
})

const file = ref({
  uid: '-2',
  name: 'avatar.png',
  url: avatar
});

//路径
const renderData = ref<any>([
  {
    label: '用户名称',
    value: '',
  },
  {
    label: '手机号码',
    value: '',
  },
  {
    label: '用户邮箱',
    value: '',
  },
  {
    label: '所属岗位',
    value: '',
  },
  {
    label: '所属角色',
    value: '',
  },
  {
    label: '创建日期',
    value: '',
  },
]);
/**
 * @param file 子组件上传的图片信息
 * @param key form接受的属性  background壁纸、coreBackground中央图片
 * @desc 壁纸、中央图片图片上传
 */
const imgUpload = async (data: any) => {
  Notification.info({
    title: '提示',
    content: '上传成功',
    duration: 2000,
  });

  await getUserProfileFetch();
  await userStore.info();
};

const getUserProfileFetch = async () => {
  try{
    setLoading(true);
    const res: any = await getUserProfile({});
    if(res.code == 200){
      renderData.value[0].value = res.data.nickName;
      renderData.value[1].value = res.data.mobile;
      renderData.value[2].value = res.data.email;
      renderData.value[3].value = res.postGroup;
      renderData.value[4].value = res.roleGroup;
      renderData.value[5].value = res.data.createTime;
    }else{
      notification(res);
    }
  }catch (e) {
    console.error(e);
  }finally {
    setLoading(false);
  }
}

//
onMounted(()=>{
  getUserProfileFetch();
})

watchEffect(async () => {
  if (props.isUpdate) {
    await userStore.info();
    getUserProfileFetch();
  }
})
</script>

<style scoped lang="less">
.arco-card {
  padding: 14px 0 4px 4px;
  border-radius: 4px;
}

:deep(.arco-avatar-trigger-icon-button) {
  width: 32px;
  height: 32px;
  line-height: 32px;
  background-color: #e8f3ff;

  .arco-icon-camera {
    margin-top: 8px;
    color: rgb(var(--arcoblue-6));
    font-size: 14px;
  }
}

.upload-view{
  width: 88px;
  height: 88px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 auto;
}

</style>
