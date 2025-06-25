import { createApp } from 'vue';
import ArcoVue from '@arco-design/web-vue';
import ArcoVueIcon from '@arco-design/web-vue/es/icon';
import hljsVuePlugin from '@highlightjs/vue-plugin';

import router from './router';
import store from './store';
import i18n from './locale';
import directive from './directive';

import globalComponents from '@/components';
import { useDict } from '@/utils/dict';
import { addDateRange } from '@/utils/ruoyi';

import App from './App.vue';
import '@/api/interceptor';
import '@arco-themes/vue-ykite-theme/index.less';
import '@/assets/style/global.less';
import 'highlight.js/lib/common';
import 'highlight.js/styles/atom-one-dark.css';

const app = createApp(App);

app.use(ArcoVue, {});
app.use(ArcoVueIcon);
app.use(hljsVuePlugin);
app.use(router);
app.use(store);
app.use(i18n);
app.use(globalComponents);
app.use(directive);

// 全局$t
app.config.globalProperties.$t = i18n.global.t;
// 全局方法挂载
app.config.globalProperties.useDict = useDict;
app.config.globalProperties.addDateRange = addDateRange;

app.mount('#app');
