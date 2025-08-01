<template>
  <div class="container">
    <Toolbar class="toolbar-box" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="toolbarOption.mode" />
    <Editor class="editor-box" :style="{ height: height + 'px' }" v-model="valueHtml" :defaultConfig="editorConfig"
      :mode="toolbarOption.mode" @onCreated="handleCreated" @onChange="handleChange" />
  </div>
</template>

<script setup lang="ts">
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { nextTick, onBeforeUnmount, onMounted, ref, shallowRef } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { DomEditor } from '@wangeditor/editor'
import { IEditorConfig } from '@wangeditor/editor'
import { getToken } from '@/utils/auth'
import { renderSlot } from 'vue'
const { value, height } = defineProps({
  value: {
    type: String,
    default: ''
  },
  height: {
    type: Number,
    default: 500
  }
})
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()
const emit = defineEmits(['change'])
// 内容 HTML
const valueHtml = ref('')

const toolbarConfig = {
  excludeKeys: ['group-video']
}
type InsertFnType = (url: string, alt: string, href: string) => void
const token = getToken()
const baseUrl = import.meta.env.VITE_API_BASE_URL;
const editorConfig: Partial<IEditorConfig> = {  // TS 语法
  // const editorConfig = {                       // JS 语法
  MENU_CONF: {
    //https://www.wangeditor.com/v5/menu-config.html#%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87
    "uploadImage": {
      server: baseUrl + '/file/upload',
      // form-data fieldName ，默认值 'wangeditor-uploaded-image'
      fieldName: 'file',
      // 单个文件的最大体积限制，默认为 2M
      maxFileSize: 5 * 1024 * 1024, // 1M
      // 最多可上传几个文件，默认为 100
      maxNumberOfFiles: 1,
      headers: {
        "Authorization": `Bearer ${token}`
      },
      customInsert(res: any, insertFn: InsertFnType) {  // TS 语法
        // 从 res 中找到 url alt href ，然后插入图片
        if (res.code == 200) {
          insertFn(res.data.url, res.data.name, res.data.name);
        }
      }
    }
  },
  placeholder: '请输入内容...'
  // 其他属性...
}

//触发毁掉
const handleChange = (editor: any) => { emit('change', editor.getHtml()) }

// 菜单栏配置
const toolbarOption = {
  mode: 'default',// default || simple
  toolbarConfig: []
}

onMounted(() => {
  nextTick(() => {
    valueHtml.value = value as string
  })
})

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor: any) => {
  // 记录 editor 实例，重要！
  editorRef.value = editor
  nextTick(() => {
    const toolbar = DomEditor.getToolbar(editor)
    const curToolbarConfig = toolbar?.getConfig()
    // 当前菜单排序和分组
    // console.log(curToolbarConfig?.toolbarKeys)
  })

}
</script>

<style lang="less" scoped>
.toolbar-box {
  border-bottom: 1px solid #ccc;
}

.editor-box {
  overflow-y: hidden;
}

.w-e-full-screen-container {
  z-index: 998;
}
</style>
