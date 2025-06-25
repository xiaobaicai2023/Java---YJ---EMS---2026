<template>
  <div>
    <a-spin :loading="loading">
      <a-upload
          :action="baseUrl+uploadUrl"
          :fileList="file ? [file] : []"
          :show-file-list="false"
          :headers="headers"
          :name="fileName"
          @before-upload="beforeUpload"
          @change="onChange"
          @progress="onProgress"
          @success="handleUploadSuccess"
          @error="setLoading(false)"
      >
        <template #upload-button>
          <div :class="uploadItemClass">
            <div v-if="imgUrl">
              <div class="arco-upload-list-picture custom-upload-avatar">
                <img :src="imgUrl" alt="加载失败" @error="setDefaultImage"/>
                <div class="arco-upload-list-picture-mask">
                  <a-tooltip :content="$t('global.edit')">
                    <IconEdit/>
                  </a-tooltip>
                  <a-tooltip :content="$t('global.delete')" v-if="delete">
                    <IconDelete class="mask-icon-delete" @click.stop="handleDelete"/>
                  </a-tooltip>
                </div>
              </div>
            </div>
            <div class="arco-upload-picture-card" v-else>
              <div class="arco-upload-picture-card-text">
                <IconPlus/>
                <div style="margin-top: 10px; font-weight: 600">上传</div>
              </div>
            </div>
          </div>
        </template>
      </a-upload>
    </a-spin>
    <div class="tooltip-view" v-if="toolTipTitle">
      <icon-exclamation-circle style="color: rgb(var(--orange-6))" />
      <span>{{ toolTipTitle }}{{imgType.join('、')}}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import {IconDelete, IconEdit, IconPlus} from '@arco-design/web-vue/es/icon';
import {FileItem} from '@arco-design/web-vue';
import {notification} from "@/hooks/my-design";
import {ref, computed} from 'vue';
import {getToken} from "@/utils/auth";
import useLoading from "@/hooks/loading";

// 接收父组件的参数
const props = defineProps({
  imgUrl: {
    type: String,
    default: '',
  },
  upLoadType: {
    type: Number,
    default: 0,
  },
  size: {
    type: Number,
    default: 2048, // 限制文件大小为2MB
  },
  imgType: {
    type: Array,
    default: () => ['jpg', 'png', 'jpeg'], // 限制文件类型
  },
  limitWH: {
    type: Boolean,
    default: false, // 是否限制图片宽高
  },
  maxWidth: {
    type: Number,
    default: 1024, // 最大宽度
  },
  maxHeight: {
    type: Number,
    default: 1024, // 最大高度
  },
  toolTipTitle:{
    type: String, // 提示语
    default: ""
  },
  uploadUrl: {
    type: String,
    default: "/file/upload"
  },
  fileName: {
    type: String,
    default: "file"
  },
  delete: {
    type: Boolean,
    default: true, //是否显示删除
  }
});

// 定义组件的事件
const emit = defineEmits(['upload', 'remove']);

// 加载状态和设置加载状态的函数
const {loading, setLoading} = useLoading(false);

// 文件上传的基础URL
const baseUrl = import.meta.env.VITE_API_BASE_URL;

// 上传路径
// const uploadUrl = `${baseUrl}/file/upload`;

// 请求头设置
const headers = {
  'Authorization': `Bearer ${getToken()}`,
};

// 存储文件的响应式变量
const file = ref<FileItem | null>(null);

// 动态计算上传项的类名
const uploadItemClass = computed(() => {
  return `arco-upload-list-item${file.value && file.value.status === 'error' ? ' arco-upload-list-item-error' : ''}`;
});

// 设置默认图片
const setDefaultImage = (e: Event) => {
  (e.target as HTMLImageElement).src = new URL(`@/assets/bi/icon-video.png`, import.meta.url).href;
};

// 文件状态变化时更新文件
const onChange = (_: any, currentFile: FileItem) => {
  file.value = {...currentFile};
};

// 上传进度变化时更新文件
const onProgress = (currentFile: FileItem) => {
  file.value = currentFile;
};

// 上传前的校验
const beforeUpload = (file: File) => {
  setLoading(true);

  // 文件类型校验
  const fileExtension = file.type.split('/').pop()?.toLowerCase();
  const isAllowedType = props.imgType.includes(fileExtension!);

  if (!isAllowedType) {
    notification({code: 400, msg: '图片格式错误'});
    setLoading(false);
    return false;
  }

  // 文件大小校验
  const fileSizeInMB = file.size / (1024 * 1024);
  if (fileSizeInMB > props.size) {
    notification({code: 400, msg: `文件大小不得超过${props.size}MB`});
    setLoading(false);
    return false;
  }
  return true;
};

// 上传成功处理
const handleUploadSuccess = (fileItem: FileItem) => {
  setLoading(false);
  emit('upload', fileItem.response);
};

// 删除文件处理
const handleDelete = () => {
  emit('remove');
};
</script>

<style lang="less" scoped>
.mask-icon-delete {
  margin-left: 6px;
}

.arco-upload-list-item {
  margin-top: 0;
}

.arco-upload-list-picture {
  width: 88px;
  height: 88px;
  margin: 0;
}

.tooltip-view{
  font-size: 12px;
  margin-top: 5px;
  span{
    margin-left: 3px;
  }
}
</style>
