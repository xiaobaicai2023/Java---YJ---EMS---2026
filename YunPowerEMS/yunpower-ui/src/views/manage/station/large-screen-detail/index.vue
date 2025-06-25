<!--
 * 功能：大屏配置-新增、编辑
 * 作者：闫李壮
 * 日期：2024-6-15
-->
<template>
  <div>
    <a-spin :loading="loading" style="width: 100%">
      <a-form :model="form" ref="formRef" :rules="rules" auto-label-width>
        <!-- 模板列表 -->
        <a-card title="选择模板">
          <a-radio-group v-model="form.pageTemplate">
            <template v-for="item in previewList" :key="item.id">
              <a-radio :value="item.id">
                <template #radio="{ checked }">
                  <a-space
                      align="start"
                      class="custom-radio-card"
                      :class="{ 'custom-radio-card-checked': checked }"
                      :gutter="0"
                  >
                    <img :src="item.imgUrl" alt="">
                    <div class="custom-radio-card-wrapper">
                      <div class="custom-radio-card-mask">
                        <div class="custom-radio-card-mask-dot"/>
                      </div>
                      <span>{{ item.name }}</span>
                    </div>
                  </a-space>
                </template>
              </a-radio>
            </template>
          </a-radio-group>
        </a-card>
        <!-- 核心表单 -->
        <a-card>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item field="entId" label="单位" :disabled="!!route.query.id">
                <a-select v-model="form.entId" placeholder="请选择单位" :options="entOption" :field-names="{
                value: 'id', label: 'entName'
              }" @change="setSiteOption($event as string,'change')">
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item field="pageName" label="标题">
                <a-input v-model="form.pageName" placeholder="请输入字母或数字，最多18个字符"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item field="deptId" label="站点" :disabled="!!route.query.id">
                <a-tree-select
                    v-model="form.deptId"
                    :data="siteOption"
                    :allow-search="true"
                    :allow-clear="true"
                    placeholder="请选择站点"
                    @change="handleDeptId"
                    :fieldNames="{
                    key: 'id', title: 'deptName', children: 'children'
                  }"
                ></a-tree-select>
              </a-form-item>
            </a-col>
            <a-col :span="12" style="position:relative;">
              <a-form-item label="壁纸">
                <up-load class="upload-view"
                         :imgUrl="pageConfig.background.url"
                         :imgType="['jpg', 'png', 'jpeg','mp4']"
                         toolTipTitle="壁纸支持格式："
                         show-remove-button
                         @upload="imgUpload($event, 'background')"
                         @remove="imgRemove($event, 'background')">
                </up-load>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="内容">
                <a-checkbox-group v-model="defaultCheck">
                  <a-checkbox v-for="(item,index) in pageConfig.header"
                              :key="index"
                              :value="item.key">{{ item.label }}
                  </a-checkbox>
                </a-checkbox-group>
              </a-form-item>
              <a-form-item>
                <a-checkbox-group v-model="defaultCheck">
                  <a-checkbox v-for="item in pageConfig.content" :key="item.id" :value="item.key">{{
                      item.label
                    }}
                  </a-checkbox>
                </a-checkbox-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form-item label="核心数据">
                <a-radio-group v-model="defaultRadio">
                  <a-radio v-for="item in pageConfig.coreMainData" :key="item.key" :value="item.key">
                    {{ item.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="统计数据">
                <a-space class="statistics-view">
                  <a-select v-model="pageConfig.coreSubStatData.one" :options="coreSubStatOption"
                            :field-names="{value:'value',}" allow-clear allow-search/>
                  <a-select v-model="pageConfig.coreSubStatData.two" :options="coreSubStatOption"
                            :field-names="{value:'value'}" allow-clear allow-search/>
                  <a-select v-model="pageConfig.coreSubStatData.three" :options="coreSubStatOption"
                            :field-names="{value:'value'}" allow-clear allow-search/>
                </a-space>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="中央图片">
                <up-load :imgUrl="pageConfig.coreBackground.url"
                         @remove="imgRemove($event, 'coreBackground')"
                         @upload="imgUpload($event,'coreBackground')"></up-load>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <!-- 卡片列表 -->
        <a-card title="配置卡片">
          <!-- 左侧上、中、下  -->
          <div class="card-list-view">
            <a-space direction="vertical" fill class="space-view">
              <template v-for="item in pageConfig.cardList.leftTop" :key="item.id">
                <a-form-item :label="item.label">
                  <a-row style="width: 100%" :gutter="24">
                    <a-col :span="18">
                      <a-select v-model="item.key">
                        <a-option v-for="itemOpt of cardList" :key="itemOpt.id" :value="itemOpt.cardKey"
                                  :label="itemOpt.cardName"/>
                      </a-select>
                    </a-col>
                    <a-col :span="4">
                      <a-space>
                        <a-button @click="handleAddCard" v-if="!item.key">新建</a-button>
                        <a-button @click="handleEditCard(item)" v-if="item.key">编辑</a-button>
                        <a-button @click="handleItemPreview(item)">预览</a-button>
                      </a-space>
                    </a-col>
                  </a-row>
                </a-form-item>
              </template>
            </a-space>
            <!-- 右侧上、中、下  -->
            <a-space direction="vertical" fill class="space-view">
              <template v-for="item in pageConfig.cardList.rightTop" :key="item.id">
                <a-form-item :label="item.label">
                  <a-row style="width: 100%" :gutter="24">
                    <a-col :span="18">
                      <a-select v-model="item.key">
                        <a-option v-for="itemOpt of cardList" :key="itemOpt.id" :value="itemOpt.cardKey"
                                  :label="itemOpt.cardName"/>
                      </a-select>
                    </a-col>
                    <a-col :span="4">
                      <a-space>
                        <a-button @click="handleAddCard" v-if="!item.key">新建</a-button>
                        <a-button @click="handleEditCard(item)" v-if="item.key">编辑</a-button>
                        <a-button @click="handleItemPreview(item)">预览</a-button>
                      </a-space>
                    </a-col>
                  </a-row>
                </a-form-item>
              </template>
            </a-space>
          </div>
          <!-- 底部左、右  -->
          <a-row style="width: 100%">
            <a-col :span="12" v-for="item in pageConfig.cardList.bottomList">
              <a-form-item :label="item.label">
                <a-row style="width: 100%" :gutter="24">
                  <a-col :span="18">
                    <a-select v-model="item.key">
                      <a-option v-for="itemOpt of cardList" :key="itemOpt.id" :value="itemOpt.cardKey"
                                :label="itemOpt.cardName"/>
                    </a-select>
                  </a-col>
                  <a-col :span="4">
                    <a-space>
                      <a-button @click="handleAddCard" v-if="!item.key">新建</a-button>
                      <a-button @click="handleEditCard(item)" v-if="item.key">编辑</a-button>
                      <a-button @click="handleItemPreview(item)">预览</a-button>
                    </a-space>
                  </a-col>
                </a-row>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </a-form>
      <div class="actions">
        <a-space>
          <a-button type="primary" status="warning" @click="handleSubmit('preview')">预览</a-button>
          <a-button type="primary" @click="handleSubmit('save')">保存</a-button>
          <a-button @click="handleCancel">取消</a-button>
        </a-space>
      </div>
      <a-modal :width="800" v-model:visible="modelVisible" @ok="handleClose" hide-cancel>
        <template #title>
          预览
        </template>
        <div style="height: 500px">
          <component v-if="modelVisible" :is="EnumChartType[previewData.chartType]"
                     :public="previewData"
                     style="margin-top: 0">
          </component>
        </div>
      </a-modal>
      <card-template v-if="cardVisible" :visible="cardVisible" @chooseTemplate="handleCardTemplate"></card-template>
      <!--卡片编辑弹窗-->
      <card-edit v-if="cardEditVisible"
                 :visible="cardEditVisible"
                 :data="{...form,...cardEditData}"
                 @closeModal="closeModal"></card-edit>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, reactive, ref, watchEffect} from "vue";
import {useRoute, useRouter} from 'vue-router';
import {Message, SelectOptionData} from "@arco-design/web-vue";
import { previewList, reduceArr} from './index';
import CardTemplate from "@/views/manage/station/components/card-template/index.vue";
import CardEdit from "@/views/manage/station/components/card-edit/index.vue";
import useLoading from "@/hooks/loading";
import {notification} from "@/hooks/my-design";
import {getStationList} from "@/api/public";
import {getConfigInfo} from "@/api/dashboard/api";
import {listEnterpriseAll} from "@/api/system/enterprise";
import {editConfigList, getCardList} from "@/api/manage/station";
import {EnumChartType} from "@/views/manage/station/components/index";
import qs from "qs";

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {sys_stat_var_sn_second_code} = proxy?.useDict("sys_stat_var_sn_second_code");
//******* 这里编写动态获取下拉列表 end ******
// 路有信息
const router = useRouter();
const route = useRoute();
// loading
const {loading, setLoading} = useLoading(false);
// from or 实例
const cardListConfig = () => {
  return {
    leftTop: [
      {
        id: 1,
        label: "左侧上",
        key: ""
      },
      {
        id: 2,
        label: "左侧中",
        key: ""
      },
      {
        id: 3,
        label: "左侧下",
        key: ""
      }
    ],
    rightTop: [
      {
        id: 4,
        label: "右侧上",
        key: ""
      },
      {
        id: 5,
        label: "右侧中",
        key: ""
      },
      {
        id: 6,
        label: "右侧下",
        key: ""
      }
    ],
    bottomList: [
      {
        id: 7,
        label: "底部左",
        key: ""
      },
      {
        id: 8,
        label: "底部右",
        key: ""
      }
    ]
  }
}
const formRef = ref<any>(null);
const form = ref<any>({
  id: '',
  entId: '',
  deptId: '',
  pageName: "",
  pageType: 1,
  pageTemplate: '1',
});

const coreMainDataConfig = () => {
  const result: any = [];
  sys_stat_var_sn_second_code.value.filter((item: any) => {
    if (item.value.includes('total_')) {
      result.push({
        id: item.id,
        label: item.label,
        key: item.value,
        value: false
      })
    }
  })
  return result;
}

// 核心数据表单
const pageConfig = ref<any>({
  templateId: "20240529",
  background: {
    type: "image",
    url: ""
  },
  header: [
    {
      id: 1,
      label: "日期时间",
      key: "date",
      value: false,
    }, {
      id: 2,
      label: "天气状况",
      key: "weather",
      value: false,
    }, {
      id: 3,
      label: "网络通断",
      key: "network",
      value: false,
    }],
  content: [
    {
      id: 1,
      label: "站点数量",
      key: "stationNum",
      value: false,
    }, {
      id: 2,
      label: "设备数量",
      key: "deviceNum",
      value: false,
    }, {
      id: 3,
      label: "点位数量",
      key: "pointNum",
      value: false,
    }, {
      id: 4,
      label: "年减排量",
      key: "yearEmissionReduction",
      value: false,
    }
  ],
  coreMainData: [],
  coreSubStatData: {
    one: '',
    two: '',
    three: ''
  },
  coreBackground: {
    type: "image",
    url: ""
  },
  cardList: cardListConfig()
});

const coreSubStatOption = computed(() => {
  const result: any = [];
  sys_stat_var_sn_second_code.value.filter((item: any) => {
    if (!item.value.includes('total_')) {
      result.push(item)
    }
  })
  return result;
});

const rules = {
  entId: [
    {
      required: true,
      message: '请选择所属单位',
    },
  ],
  pageName: [
    {
      required: true,
      message: '标题不得为空'
    },
    {
      validator: (value: any, cb: any) => {
        const length = value.length
        if (length > 18) {
          cb('请输入字母和数字，最多为18个字符')
        } else {
          cb()
        }
      }
    }
  ],
  deptId: [
    {
      required: true,
      message: '请选择站点',
    },
  ]
};
// 公司options
const entOption = ref<any>([])
// 站点options
const siteOption = ref<Array<SelectOptionData>>([]);
// 内容checkbox-默认选中
const defaultCheck = ref<Array<string>>([]);
// 核心数据radio-默认选中
const defaultRadio = ref<string>('');

// 预览
const modelVisible = ref(false);
//选择卡片模板弹窗
const cardVisible = ref(false);
// 卡片编辑弹窗
const cardEditVisible = ref(false);
// 卡片编辑数据
const cardEditData = ref({})
// 卡片列表
const cardList = ref<any>([]);
// 预览数据
const previewData = ref<any>({});


// ************** methods ***************
/**
 * 编辑详情数据
 */
const getConfigDetail = async (id: string) => {
  try {
    setLoading(true);
    const res: any = await getConfigInfo(id);
    if (res.code === 200) {
      form.value = res.data;
      pageConfig.value= res.data.pageConfig === "{}" || ! res.data.pageConfig? JSON.parse(res.data.pageConfigPre) : JSON.parse(res.data.pageConfig);
      await getCardListAll();
      for (const [name, value] of Object.entries(pageConfig.value.cardList)) {
        value?.map((item: any) => {
          const foundUser = cardList.value.find((listItem: any) => listItem.cardKey === item.key);
          if (!foundUser) item.key = undefined;
        })
      }
      handleCheckbox(pageConfig.value.header, 2);
      handleCheckbox(pageConfig.value.content, 2);
      handleRadio(pageConfig.value.coreMainData, 2);
    }
  } catch (e) {

  } finally {
    setLoading(false);
  }
}

/**
 * 设置公司options
 */
const setEntOption = async () => {
  const res: any = await listEnterpriseAll();
  if (res.code !== 200) return;
  entOption.value = res.data;
  if (form.value.entId) {
    await setSiteOption(form.value.entId);
  }
};

/**
 * 设置站点options
 * @param entId 选中的公司id
 * @param type 是否 公司select触发
 */
const setSiteOption = async (entId: string, type: string = '') => {
  try {
    const {code, data} = await getStationList({});
    if (code !== 200) return;

    const site = data.find((item: any) => item.entId === entId)?.children;
    if (!site) return;

    siteOption.value = site.map((item: any) => ({...item, selectable: false}));

    if (type) {
      form.value.deptId = '';
    }
  } catch (error) {
    console.error('Error fetching station list:', error);
  }
};


/**
 * 设置选中的checkBox
 */
const setDefaultCheck = () => {
  const result: string[] = [];
  // 过滤默认选中
  const header = reduceArr(pageConfig.value.header);
  const content = reduceArr(pageConfig.value.content);
  result.push(...header, ...content);
  defaultCheck.value = result;
};

/**
 * 设置选中的radio
 */
const setDefaultRadio = () => {
  // 过滤默认选中
  const findData = pageConfig.value.coreMainData.find((item: any) => item.value);
  if (findData) {
    defaultRadio.value = findData?.key;
  }
};

/**
 * @param file 子组件上传的图片信息
 * @param key form接受的属性  background壁纸、coreBackground中央图片
 * @desc 壁纸、中央图片图片上传
 */
const imgUpload = (file: any, key: string) => {
  pageConfig.value[key].type = 'image';
  pageConfig.value[key].url = file.data.url;
};

/**
 * @param file 子组件上传的图片信息
 * @param key form接受的属性  background壁纸、coreBackground中央图片
 * @desc 壁纸、中央图片图片删除
 */
const imgRemove = (file: any, key: string) => {
  pageConfig.value[key].type = '';
  pageConfig.value[key].url = '';
}

/**
 * 确认提交
 * @param type 提交类型  保存or预览
 */
const handleSubmit = async (type?: string) => {
  formRef.value.validate(async (valid: boolean) => {
    if (valid) return;
    try {
      setLoading(true);
      // 处理复选框和单选框
      handleCheckbox(pageConfig.value.header, 1);
      handleCheckbox(pageConfig.value.content, 1);
      handleRadio(pageConfig.value.coreMainData, 1);

      const pageConfigStr = JSON.stringify(pageConfig.value);
      // 准备参数
      const params = {
        id: route.query.id,
        entId: form.value.entId,
        deptId: form.value.deptId,
        pageName: form.value.pageName,
        pageType: form.value.pageType,
        pageTemplate: form.value.pageTemplate,
        pageKey: `${form.value.deptId}-${form.value.pageTemplate}`,
        pageConfig: type === 'preview' ? null : pageConfigStr,
        pageConfigPre: pageConfigStr
      };

      const res: any = await editConfigList(params);
      if (res.code !== 200) return notification(res);

      // 新建后的逻辑
      if (!route.query.id && res.data && !type) {
        route.query.id = res.data;
        cardVisible.value = true;
        return;
      }

      notification(res);

      // 根据type参数进行相应操作
      if (type === 'preview') {
        const previewParams = {
          referer: window.location.href,
          stationId: form.value.deptId,
          isPre: 1
        };
        window.open(`${window.location.origin}/bi?${qs.stringify(previewParams)}`, "_blank");
      } else if (type === 'save') {
        router.push({path: '/setting/station/largeScreenConfig'});
      }

    } catch (error) {
      console.error('错误:', error);
    } finally {
      setLoading(false);
    }
  });
};


/**
 * 处理复选框
 * @param arr
 * @param type 1选中的在form中勾选，2form中的数据反显复选框
 */
const handleCheckbox = (arr: any[], type?: number) => {
  if (type === 1) {
    arr.forEach((item: any) => {
      item.value = defaultCheck.value.includes(item.key);
    });
  } else if (type === 2) {
    arr.forEach((item: any) => {
      if (item.value) {
        defaultCheck.value.push(item.key);
      }
    });
  }
};
/**
 * 处理单选框
 * @param arr
 * @param type 1选中的在form中勾选，2form中的数据反显复选框
 */
const handleRadio = (arr: any[], type?: number) => {
  if (type === 1) {
    arr.forEach((item: any) => {
      item.value = item.key === defaultRadio.value;
    });
  } else if (type === 2) {
    arr.forEach((item: any) => {
      if (item.value) {
        defaultRadio.value = item.key;
      }
    });
  }
};
/**
 * 取消按钮
 */
const handleCancel = () => {
  formRef.value?.resetFields();
  router.push({
    path: '/setting/station/largeScreenConfig'
  });
}

/**
 * 新建卡片
 */
const handleAddCard = () => {
  if (!route.query.id) {
    handleSubmit();
  }
  if (route.query.id) {
    cardVisible.value = true;
  }
}
/**
 * 编辑卡片
 */
const handleEditCard = (item: any) => {
  const record = cardList.value.find((val: any) => {
    return item.key == val.cardKey;
  })
  if (record) {
    const {dashboardConfigId, cardKey, chartType, cardName} = record;
    cardEditData.value = {
      dashboardConfigId,
      cardKey,
      chartType,
      staticType: record.staticType ? record.staticType : undefined,
      public: {
        dashboardConfigId,
        cardName,
        cardKey,
      },
      preview: 1
    };
  }
  cardEditVisible.value = true;
}
/**
 * 关闭选择卡片模板弹窗
 * @param val 选择的图表模板
 */
const handleCardTemplate = (val: any) => {
  cardVisible.value = false;
  if (val) {
    cardEditVisible.value = true;
    cardEditData.value = val;
  } else {
    cardEditVisible.value = false;
  }
}

/**
 * 关闭编辑弹窗
 */
const closeModal = async () => {
  cardEditVisible.value = false;
  await getCardListAll();
}

/**
 * 配置卡片预览
 */
const handleItemPreview = (item: any) => {
  const record = cardList.value.find((val: any) => {
    return item.key == val.cardKey;
  })
  if (record) {
    const {dashboardConfigId, cardKey, chartType, cardName} = record;
    previewData.value = {
      dashboardConfigId,
      cardKey,
      chartType,
      public: {
        dashboardConfigId,
        cardName,
        cardKey,
      },
      preview: 1
    };
    modelVisible.value = true
  } else {
    previewData.value = {};
    Message.warning({
      content: '请先选择卡片',
    });
  }
};

/**
 * 关闭预览弹窗
 */
const handleClose = () => {
  modelVisible.value = false
}

// 切换站点
const handleDeptId = async () => {
  pageConfig.value.cardList = {...cardListConfig()};
}

const getCardListAll = async () => {
  cardList.value = [];
  try {
    setLoading(true);
    const res = await getCardList({dashboardConfigId: route.query.id || 0});
    if (res?.code == 200) {
      cardList.value = res.data;
    }
  } catch (e) {
    console.error(e);
  } finally {
    setLoading(false);
  }
}


onMounted(async () => {
  if (route.query.id) {
    await getConfigDetail(route.query.id as string);
    await getCardListAll();
  }
  setEntOption();
  setDefaultCheck();
  setDefaultRadio();
})

watchEffect(()=>{
  if(sys_stat_var_sn_second_code.value && !route.query.id){
    pageConfig.value.coreMainData = coreMainDataConfig();
  }
})
</script>

<style lang="less" scoped>
.arco-form {
  margin-bottom: 60px;
}

.arco-card {
  margin-bottom: 15px;
}

.statistics-view {
  ::v-deep(.arco-select) {
    min-width: 220px;
  }
}

.upload-view {
  position: absolute;
  top: -10px;
}

.card-list-view {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .space-view {
    width: 50%;
  }
}

.preview-list {
  display: flex;
  flex-direction: column;
  align-items: center;

  img {
    margin-bottom: 10px;
  }
}


.custom-radio-card {
  padding: 10px 16px;
  border: 1px solid var(--color-border-2);
  padding-left: 24px;
  border-radius: 4px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.custom-radio-card-wrapper {
  display: flex;
  margin-top: 10px;
  align-items: center;
}

.custom-radio-card-wrapper span {
  margin-left: 10px;
}

.custom-radio-card-mask {
  height: 14px;
  width: 14px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 100%;
  border: 1px solid var(--color-border-2);
  box-sizing: border-box;
}

.custom-radio-card-mask-dot {
  width: 8px;
  height: 8px;
  border-radius: 100%;
}

.custom-radio-card-title {
  color: var(--color-text-1);
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 8px;
}

.custom-radio-card:hover,
.custom-radio-card-checked,
.custom-radio-card:hover .custom-radio-card-mask,
.custom-radio-card-checked .custom-radio-card-mask {
  border-color: rgb(var(--primary-6));
}

.custom-radio-card-checked {
  background-color: var(--color-primary-light-1);
}

.custom-radio-card:hover .custom-radio-card-title,
.custom-radio-card-checked .custom-radio-card-title {
  color: rgb(var(--primary-6));
}

.custom-radio-card-checked .custom-radio-card-mask-dot {
  background-color: rgb(var(--primary-6));
}

:deep(.arco-space-horizontal .arco-space-item) {
  margin-right: 8px !important;
}

.actions {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: 60px;
  padding: 14px 20px 14px 0;
  background: var(--color-bg-2);
  text-align: right;
  box-shadow: 0 10px 30px 0 rgb(0 0 0 / 40%);
}

.upload-view{
  margin-top: 10px!important;
}
</style>
