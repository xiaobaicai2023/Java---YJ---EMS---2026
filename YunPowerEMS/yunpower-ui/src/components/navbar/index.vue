<template>
  <div class="navbar">
    <div class="left-side">
      <a-space>
        <template v-if="!topMenu && appStore.device === 'mobile'">
          <component :is="drawerVisible ? 'icon-menu-fold' : 'icon-menu-unfold'" class="menu-icon" size="20"
                     @click="toggleDrawerMenu"/>
        </template>
        <!-- <a-select v-model="companyValue" :trigger-props="{ autoFitPopupMinWidth: true }" size="large" placeholder="请选择站点"
          @change="companyChange">
          <a-option v-for="item in companyList" :key="item.id" :value="item.id" :label="item.deptName" />
        </a-select> -->
        <a-tree-select v-model="companyValue" :allow-clear="false" :data="companyList" :dropdown-style="{}"
                       :fieldNames="{
            key: 'id', title: 'deptName', children: 'children'
          }"
                       :filter-tree-node="filterTreeNode" allow-search placeholder="请选择站点"
                       @change="companyChange"></a-tree-select>
      </a-space>
    </div>
    <div class="center-side">
      <Menu v-if="topMenu"/>
    </div>
    <ul class="right-side">
      <!-- <li>
        <a-tooltip :content="$t('settings.search')">
          <a-button class="nav-btn" type="outline" :shape="'circle'">
            <template #icon>
              <icon-search />
            </template>
          </a-button>
        </a-tooltip>
      </li> -->
      <li>
        <a-tooltip :content="$t('settings.language')">
          <a-button
              :shape="'circle'"
              class="nav-btn"
              type="outline"
              @click="setDropDownVisible"
          >
            <template #icon>
              <icon-language/>
            </template>
          </a-button>
        </a-tooltip>
        <a-dropdown trigger="click" @select="changeLocale as any">
          <div ref="triggerBtn" class="trigger-btn"></div>
          <template #content>
            <a-doption
                v-for="item in locales"
                :key="item.value"
                :value="item.value"
            >
              <template #icon>
                <icon-check v-show="item.value === currentLocale"/>
              </template>
              {{ item.label }}
            </a-doption>
          </template>
        </a-dropdown>
      </li>
      <li>
        <a-tooltip :content="theme === 'light'
          ? $t('settings.navbar.theme.toDark')
          : $t('settings.navbar.theme.toLight')
          ">
          <a-button :shape="'circle'" class="nav-btn" type="outline" @click="handleToggleTheme">
            <template #icon>
              <icon-moon-fill v-if="theme === 'dark'"/>
              <icon-sun-fill v-else/>
            </template>
          </a-button>
        </a-tooltip>
      </li>
      <li>
        <a-tooltip :content="$t('settings.navbar.alerts')">
          <div class="message-box-trigger">
            <a-badge :count="warnCount" dot>
              <a-button
                  class="nav-btn"
                  type="outline"
                  :shape="'circle'"
                  @click="setPopoverVisible"
              >
                <icon-notification/>
              </a-button>
            </a-badge>
          </div>
        </a-tooltip>
        <!--        <a-popover-->
        <!--            trigger="click"-->
        <!--            :arrow-style="{ display: 'none' }"-->
        <!--            :content-style="{ padding: 0, minWidth: '400px' }"-->
        <!--            content-class="message-popover"-->
        <!--        >-->
        <!--          <div ref="refBtn" class="ref-btn"></div>-->
        <!--          <template #content>-->
        <!--            <message-box/>-->
        <!--          </template>-->
        <!--        </a-popover>-->
      </li>
      <!--      <li>-->
      <!--        <a-tooltip :content="$t('settings.navbar.toMotif')" position="bottom">-->
      <!--          <a-button :shape="'circle'" class="nav-btn" type="outline" @click="toggleMotif">-->
      <!--            <template #icon>-->
      <!--              <icon-skin/>-->
      <!--            </template>-->
      <!--          </a-button>-->
      <!--        </a-tooltip>-->
      <!--      </li>-->
      <li>
        <a-tooltip :content="isFullscreen
          ? $t('settings.navbar.screen.toExit')
          : $t('settings.navbar.screen.toFull')
          ">
          <a-button :shape="'circle'" class="nav-btn" type="outline" @click="toggleFullScreen">
            <template #icon>
              <icon-fullscreen-exit v-if="isFullscreen"/>
              <icon-fullscreen v-else/>
            </template>
          </a-button>
        </a-tooltip>
      </li>
      <li>
        <a-dropdown trigger="click">
          <div class="avatar-container">
            <a-avatar :size="32" :style="{ marginRight: '8px', cursor: 'pointer' }">
              <img :alt="userName" :src="avatar"/>
            </a-avatar>
            <p style="color:var(--color-neutral-9);">{{ userName }}</p>
          </div>
          <template #content>
            <a-doption>
              <div @click="handleMessage">
                <icon-user/>
                <span style="margin-left: 2px">个人信息</span>
              </div>
            </a-doption>
            <a-doption>
              <div @click="handleLogout">
                <icon-export/>
                <span style="margin-left: 2px"> {{ $t('messageBox.logout') }}</span>
              </div>
            </a-doption>
          </template>
        </a-dropdown>
      </li>
    </ul>
  </div>
  <reset-password/>
  <!-- 换肤抽屉-->
  <a-modal v-model:visible="motifVisible" :footer="false" width="900px">
    <template #title>
      <div class="theme-box-header">
        {{ $t('settings.navbar.theme.title') }}
        <a-input :style="{width:'220px'}" @input="searchMotif" allow-clear/>
      </div>
    </template>
    <a-spin :loading="modalLoading" class="lab-grid-view">
      <a-grid :colGap="12" :cols="{ xs: 1, sm: 2, md: 3 }" :rowGap="16" class="lab-grid-view">
        <a-grid-item v-for="(item,index) in motifData.list" :key="index">
          <a-card>
            <template #cover>
              <img :src="item.cover"
                   :style="{ width: '100%', height: '160px' }"
                   alt="dessert"
              />
            </template>
            <a-typography-paragraph ellipsis>
              {{ item.themeName }}
            </a-typography-paragraph>
            <a-tag color="arcoblue" bordered v-if="item.packageName === localMotif.packageName">
              {{ $t('settings.navbar.theme.currentThemes') }}
            </a-tag>
            <a-button size="mini" type="primary" @click="changeTheme(item)" v-else>
              {{ $t('settings.navbar.theme.install') }}
            </a-button>
          </a-card>
        </a-grid-item>
      </a-grid>
      <a-row>
        <a-col :span="12">
          <a-button size="small" type="primary" @click="resetTheme">{{
              $t('settings.navbar.theme.resetTheme')
            }}
          </a-button>
        </a-col>
        <a-col :span="12">
          <a-pagination :current="motifPage.currentPage"
                        :page-size="motifPage.pageSize"
                        :total="motifData.total"
                        style="justify-content: flex-end;"
                        @change="handlePageChange"/>
        </a-col>
      </a-row>
    </a-spin>
  </a-modal>
</template>

<script lang="ts" setup>
import Menu from '@/components/menu/index.vue'
import useLocale from '@/hooks/locale'
import useUser from '@/hooks/user'
import {LOCALE_OPTIONS} from '@/locale'
import {useAppStore, useCompanyStore, useThemeStore, useUserStore} from '@/store'
import {Message, Notification} from '@arco-design/web-vue'
import { useFullscreen } from '@vueuse/core'
import {computed, h, inject, nextTick, onMounted, onUnmounted, ref, watch} from 'vue'
import {storeToRefs} from 'pinia'
import {changeTheme, resetTheme, updateLinkTag} from '@/utils/changeTheme'
import {findById} from '@/utils/ruoyi'
import {getThemesList} from "@/api/user";
import useLoading from "@/hooks/loading";
import ResetPassword from "@/components/reset-password/index.vue";
import {DeviceVarAlarmMessage, SocketData} from "@/api/websocketService";
import {useRoute, useRouter} from "vue-router";
import {useDebounceFn} from '@vueuse/core'

defineProps({
  drawerVisible: {
    type: Boolean,
    default: false
  }
});
const router = useRouter();
const route = useRoute();
const appStore = useAppStore();
const userStore = useUserStore();
const companyStore = useCompanyStore();
const {companyValue, companyList, selectCompany} = storeToRefs(companyStore); // 选择的机构, 机构列表
const {setCompanyList, updateCompanyValue} = companyStore; // 设置机构
const {logout} = useUser();
const {changeLocale, currentLocale} = useLocale();
const {isFullscreen, toggle: toggleFullScreen} = useFullscreen();
const locales = [...LOCALE_OPTIONS];
const avatar = computed(() => {
  return userStore.user.headPic ? userStore.user.headPic : loadImage()
});
const userName = computed(() => {
  return userStore.user.nickName;
})
const refBtn = ref()
const triggerBtn = ref()
const theme = computed(() => {
  return appStore.theme
})
const topMenu = computed(() => appStore.topMenu && appStore.menu)
const renderFatherMenu = computed(() => appStore.appAsyncFatherMenu);

// 切换主题 弹框
const motifVisible = ref<boolean>(false);
// 主题列表
const motifData = ref<any>({
  total: 0,
  list: []
})
const motifPage = ref({
  currentPage: 1,
  pageSize: 6,
});
const {loading: modalLoading, setLoading} = useLoading(false);

const localMotif = computed<any>(() => {
  return useThemeStore().themePackage
})
const warnCount = ref(0);

if (localMotif.value._id) {
  updateLinkTag(localMotif.value)
}
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
/**
 * 左补零
 * @param val 值
 * @param targetLength 长度
 */
const padLeft = (val: number, targetLength: number) => {
  return val.toString().padStart(targetLength, '0');
}

const handleToggleTheme = () => {
  appStore.toggleTheme(appStore.theme !== 'dark');
}
const setVisible = () => {
  appStore.updateSettings({globalSettings: true})
}


/**
 * 站点搜索
 * @param searchValue
 * @param nodeData
 */
const filterTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.deptName.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
}

const reload = inject('reload') as () => void

// 机构下拉框change
const companyChange = async (val: any) => {
  const findData = findById(companyList.value, val);
  let res = await updateCompanyValue(findData)
  if (res.code == 200) {
    reload();
  }
}

const handleLogout = () => {
  logout();
}

const handleMessage = () => {
  router.push({
    path: '/user/profile/setting'
  })
}

// 打开主题弹窗
const toggleMotif = () => {
  motifVisible.value = true;
  setThemesList()
}
const searchMotif = useDebounceFn((keyword?: string) => {
  setThemesList(keyword)
}, 500)
// 获得主题包
const setThemesList = async (keyword?: string) => {
  try {
    setLoading(true)
    const params = {
      ...motifPage.value,
      depLibrary: '@arco-design/web-vue',
      keyword
    }
    motifData.value = await getThemesList(params);
  } catch (e) {

  } finally {
    setLoading(false)
  }
}
const handlePageChange = (val: number) => {
  motifPage.value.currentPage = val;
  setThemesList()
}

const setDropDownVisible = () => {
  const event = new MouseEvent('click', {
    view: window,
    bubbles: true,
    cancelable: true,
  })
  triggerBtn.value.dispatchEvent(event)
}
const switchRoles = async () => {
  const res = await userStore.switchRoles()
  Message.success(res as string)
}
const toggleDrawerMenu = inject('toggleDrawerMenu') as () => void

// 使用 h 函数构建 VNode
const renderContent = (data: DeviceVarAlarmMessage) => {
  return h(
      'div',
      null,
      [
        h('p', null, [h('strong', null, '站点：'), ' ', data.stationName]),
        h('p', null, [h('strong', null, '设备：'), ' ', `${data.deviceName} ${data.deviceVarName}`]),
        h(
            'p',
            null,
            [
              h('strong', null, '类型：'),
              ' ',
              h('span', {style: {color: 'rgb(var(--orange-6))'}}, data.categoryName)
            ]
        ),
        h('p', null, [h('strong', null, '信息：'), ' ', data.content]),
        h('p', null, [h('strong', null, '时间：'), ' ', data.happenTime]),
      ]
  );
}

const isChildOf = (child: any, parent: any) => {
  var el = child;
  while ((el = el.parentElement) && el !== parent) {
  }
  return el === parent;
}

const startNotificationTimer = (newVal: DeviceVarAlarmMessage) => {
  Notification.info({
    title: newVal.triggerLevelName,
    content: () => renderContent(newVal),
    closable: true,
    duration: 1000 * 60
  })

  warnCount.value = 1;
  // 通知在最上面
  nextTick(() => {
    const elementContainer: any = document.querySelector('.arco-notification-list');
    if (!elementContainer) return;
    elementContainer.style.top = "50px";
    const lastElement = elementContainer.lastElementChild;
    if (!lastElement) return;
    var closeElement = lastElement.querySelector('.arco-icon-close');
    var notificationLeftElement = lastElement.querySelector('.arco-notification-left');
    var titleElement = lastElement.querySelector('.arco-notification-title');
    var contentElement = lastElement.querySelector('.arco-notification-content');
    notificationLeftElement.style.display = "none";
    // 当前选中报警
    const lastStylePorps = {
      position: 'relative',
      paddingTop: '40px',
      marginBottom: '10px',
      background: '#fff',
    }
    // 当前content
    const contentStyleProps = {
      color: '#666',
      paddingBottom: '0',
    }
    // 关闭按钮
    const closeProps = {
      width: '20px',
      height: '20px',
    }
    // 设置title背景
    const styleProps = {
      position: 'absolute',
      left: '0',
      top: '0',
      width: '100%',
      height: '40px',
      lineHeight: '40px',
      paddingLeft: '20px',
      backgroundColor: newVal.triggerStatus == 0 && newVal.triggerLevel == 100 ? 'rgb(var(--red-6))' : newVal.triggerStatus == 0 && newVal.triggerLevel > 100 ? 'rgb(var(--orange-6))' : 'rgb(var(--arcoblue-6))',
      color: '#fff',
    };
    // 添加点击事件监听器
    lastElement.addEventListener('click', function (event: any) {
      const classes = event.target.classList;
      if (!event.target.matches('.arco-icon-close') && !classes.contains(closeElement) && !isChildOf(event.target, closeElement)) {
        const aaa: any = renderFatherMenu.value.find(item => item.props?.stationType == newVal.stationType);
        console.log(aaa.path)
        // 创建一个新的 MouseEvent 对象
        const clickEvent = new MouseEvent('click', {
          bubbles: true, // 这里可以保留，但我们将手动阻止冒泡
          cancelable: true,
          view: window
        });

        // 路由替换
        router.replace({
          path: `${aaa.path}/alarm/list`
        });

        // 触发 click 事件
        setTimeout(() => {
          closeElement.dispatchEvent(clickEvent);
          warnCount.value = 0;

        }, 1000 * 2)
      } else {
        return false;
      }
    });
    Object.assign(titleElement.style, styleProps);
    Object.assign(lastElement.style, lastStylePorps);
    Object.assign(contentElement.style, contentStyleProps);
    Object.assign(closeElement.style, closeProps);
    elementContainer.insertBefore(lastElement, elementContainer.firstChild);
  })
};

// WebSocket 数据
const {data, sendData}: SocketData = inject('webSocketData', {
  data: {value: 0},
  sendData: () => {
  }
});

// 订阅
const subscribe = () => {
  sendData({
    action: 'subscribe',
    bizSn: selectCompany.value.deptSn
  });
}

// 取消订阅
const onSubscribe = () => {
  sendData({
    action: 'unsubscribe',
    bizSn: selectCompany.value.deptSn
  });
}

const setPopoverVisible = () => {
  const aaa: any = renderFatherMenu.value.find(item => selectCompany.value.stationType && item.props?.stationType == selectCompany.value.stationType);
  router.replace({
    path: `${aaa.path}/alarm/list`
  });
  const elementContainer: any = document.querySelector('.arco-notification-list');
  if (elementContainer) elementContainer.style.display = "none";
  warnCount.value = 0;
}

watch(data, async (newVal: any) => {
  if (selectCompany.value.deptSn && newVal.stationSn == selectCompany.value.deptSn) {
    startNotificationTimer(newVal);
  }
});

onMounted(async () => {
  // 设置公司列表
  // startNotificationTimer({
  //   stationName: '红太阳广电',
  //   deviceName: 'D1',
  //   deviceVarName: '三相电压',
  //   categoryName: '电流过低',
  //   content: '【D1 D1.1b】205.849三相电压过低',
  //   happenTime: '2024-8-24 12：00',
  //   triggerStatusName: '已完成',
  //   triggerLevelName: '二级报警',
  //   triggerStatus: 0,
  //   triggerLevel: 101
  // });
  await setCompanyList();
  subscribe();
})

onUnmounted(() => {
  onSubscribe();
})
</script>

<style lang="less" scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  height: 100%;
  background-color: var(--color-bg-2);
  border-bottom: 1px solid var(--color-border);
}

.left-side {
  display: flex;
  align-items: center;
  padding-left: 10px;
}

.center-side {
  flex: 1;
}

.right-side {
  display: flex;
  padding-right: 20px;
  list-style: none;

  :deep(.locale-select) {
    border-radius: 20px;
  }

  li {
    display: flex;
    align-items: center;
    padding: 0 10px;
  }

  a {
    color: var(--color-text-1);
    text-decoration: none;
  }

  .nav-btn {
    color: rgb(var(--gray-8));
    font-size: 16px;
    border-color: rgb(var(--gray-3));
  }

  .trigger-btn,
  .ref-btn {
    position: absolute;
    bottom: 14px;
  }

  .trigger-btn {
    margin-left: 14px;
  }
}

.message-popover {
  .arco-popover-content {
    margin-top: 0;
  }
}

:deep(.arco-select-view-single) {
  border: none;
  background-color: transparent;
  border-radius: 0;

  .arco-select-view-suffix .arco-select-view-icon svg {
    font-size: 18px
  }
}

.lab-box {
  margin-top: 10px;
  display: flex;
  align-items: center;

  div {
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    margin-right: 10px;
    width: 20px;
    height: 20px;
    border-radius: 3px;
  }
}

.menu-icon {
  cursor: pointer;
  color: var(--color-text-1)
}

.lab-grid-view {
  height: 550px;
  width: 100%;
}

.custom-close-button {
  width: 26px !important;
  height: 26px !important;
  border-radius: 50% !important;
  background: red !important;
  text-align: center;
  line-height: 26px;
}

.theme-box-header {
  width: 100%;
  padding-right: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center
}

.avatar-container {
  width: 100%;
  display: flex;
  align-items: center;

  p {
    max-width: 80px;
    text-align: center;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

</style>
