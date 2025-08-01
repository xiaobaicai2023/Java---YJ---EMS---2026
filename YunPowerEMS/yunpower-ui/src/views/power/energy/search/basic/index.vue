<!--
* 功能：基础报表
* 作者：曹晓桐
* 日期：2023-11-19
-->
<template>
  <div>
    <a-row :gutter="15">

      <!-- 左侧树 start -->
      <a-col :span="4" style="overflow: scroll;">
        <search-tree :title="$t('global.device')" :time="true" :multiple="true" @onChange="searchTreeChange" />
      </a-col>
      <!-- 左侧树 end -->

      <!-- 右侧内容 start -->
      <a-col :span="20">
        <a-card class="general-card" :title="$t('power.energy.search.queryData')">

          <!-- 标签 start -->
          <a-space wrap>
            <a-tag v-for="(item, index) in tagList" :key="index" @click="handleTagSelect(item.tagName)"
              :style="{ color: item.checkable ? 'rgb(var(--primary-6))' : '', cursor: 'pointer' }">
              <template #icon><icon-check-circle-fill v-if="item.checkable"
                  style="color:rgb(var(--primary-6))" /></template>
              {{ item.tagName }}
                <icon-close v-if="item.closable" @click="handleTagDel(index)"></icon-close>
            </a-tag>
            <a-tag :style="{
              width: '110px',
              backgroundColor: 'var(--color-fill-2)',
              border: '1px dashed var(--color-fill-3)',
              cursor: 'pointer',
            }" @click="handleTagAdd">
              <template #icon>
                <icon-plus />
              </template>
              保存当前模板
            </a-tag>
          </a-space>
          <!-- 标签 end -->

          <!-- 表格 start -->
          <a-table style="margin-top: 15px;" :data="renderData" :loading="loading"
            :bordered="{ wrapper: true, cell: true }" :pagination="false" :scroll="{ x: tableWidth, y: '100%' }">
            <template #columns>
              <a-table-column v-for="( item, index ) in  customColumnsValue " :key="index" :title="item.title"
                :data-index="item.dataIndex" :fixed="item.fixed" :width="item.width" :align="item.align"></a-table-column>
            </template>
          </a-table>
          <!-- 表格 end -->
        </a-card>
      </a-col>
      <!-- 右侧内容 end -->


      <!-- 操作弹框 start -->
      <a-modal v-model:visible="operateModalModel.visible" class="modal-box">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
            style="color: rgb(var(--red-6)); margin-right: 5px" />
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px" />
          确认提示
        </template>
        <div v-if="operateModalModel.type == 0">
          <a-input v-model.trim="operateModalModel.name" :max-length="20" placeholder="请输入模板标题" allow-clear />
        </div>
        <div v-else style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{ operateModalModel.name }}】的数据项？
        </div>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">取消</a-button>
              <a-button type="primary" @click="handleOperateModelOk">确认</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>

    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import useLoading from "@/hooks/loading";
import { basicDataList, ppfvList } from "@/api/system/home-power";
import dayjs from "dayjs";
import { Message } from "@arco-design/web-vue";

//加载中
const { loading, setLoading } = useLoading(false);
const tableWidth = ref<number>();
const customColumnsValue = ref<any>();
//数据
const renderData = ref<any>([]);
//自定义头
const searchParams = ref<any>({});
//默认标签
const defaultTag = ref<any>({
  tagName: "默认模板",
  closable: false,
  checkable: true,
})
//标签集合
const tagList = ref<any>([])
tagList.value.push(defaultTag.value);

//操作弹框
const generateOperateModalModel = () => {
  return {
    //0保存  1删除
    type: 0,
    visible: false,
    title: "",
    id: 0,
    value: 0,
    name: ""
  };
};
//弹框模型
const operateModalModel = ref(generateOperateModalModel());


/**
 * 搜索树Change
 */
const searchTreeChange = async (val: any) => {
  searchParams.value = val;
  await fetchData();
}


/**
 * 选中标签
 * @param val 索引
 */
const handleTagSelect = (val: string) => {
  if (!val || val.length <= 0) {
    val = defaultTag.value.name;
  }
  tagList.value.forEach((item: any, index: number) => {
    item.checkable = false;
    if (item.tagName == val) {
      item.checkable = true;
    }
  })
}

/**
 * 选中标签
 * @param val 索引
 */
const handleTagDel = (val:number) => {
  handleOperateModelCancle();
  operateModalModel.value.visible = true;
  operateModalModel.value.type = 1;
  operateModalModel.value.value = val;
  operateModalModel.value.title = "删除";
  operateModalModel.value.name = tagList.value[val].tagName;
}

/**
 * 添加标签
 */
const handleTagAdd = () => {
  handleOperateModelCancle();
  operateModalModel.value.visible = true;
  operateModalModel.value.type = 0;
}


/**
 * 操作弹框取消
 */
const handleOperateModelCancle = () => {
  operateModalModel.value = generateOperateModalModel();
}

/**
 * 操作弹框确认
 */
const handleOperateModelOk = async () => {
  if (operateModalModel.value.type == 0) {
    if (!operateModalModel.value.name || operateModalModel.value.name.length <= 0) {
      Message.error({
        content: "请输入模板名称",
        duration: 2 * 1000,
      })
      return false;
    }
    //调用添加接口
    tagList.value.push({
      tagName: operateModalModel.value.name,
      closable: true,
      checkable: true,
    })
    handleTagSelect(operateModalModel.value.name)
  } else {
    //调用删除接口
    tagList.value.splice(operateModalModel.value.value, 1);
    handleTagSelect(defaultTag.value.name);
  }
  handleOperateModelCancle();
}

/**
 * 获取图表类型
 * @param param 时间参数
 */
const fetchData = async () => {
  setLoading(true);
  try {
    let param = {
      ...searchParams.value
    }
    let res = await basicDataList(param);
    if (res.code == 200 && res.data && res.data.length > 0) {
      let data: any = [];
      let columns: any = [];
      let width = 0;
      res.data.forEach((item:any, index:any) => {
        let info: any = {};
        item.forEach((citem:any, cindex:any) => {
          info[citem.key] = citem.value;
          if (index == 0) {
            let temp: any = {
              title: citem.key,
              dataIndex: citem.key,
              width: 120,
              align: "center"
            };
            if (citem.key == '设备') {
              temp["fixed"] = "left";
              temp["align"] = "left";
              temp["width"] = 210;
            }
            if (citem.key == '合计') {
              temp["fixed"] = "right";
              temp["align"] = "center";
              temp["width"] = 150;
            }
            width += temp.width;
            columns.push(temp)
          }
        })
        data.push(info);
      })
      customColumnsValue.value = columns;
      renderData.value = data;
      tableWidth.value = width;
    }else{
      renderData.value = [];
    }
  } catch (ex) {
    renderData.value = [];
    console.error("峰谷数据出错", ex)
  } finally {
    setLoading(false);
  }
}

onMounted(async () => {
  const dateNow = dayjs().format("YYYY-MM-DD HH:mm");
})

</script>

<style scoped>
.title {
  font-size: 16px;
}
</style>

