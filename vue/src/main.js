import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css'
import './assets/css/design-system.scss'
import './assets/css/animations.scss'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

const app = createApp(App)

app.use(router)
app.use(ElementPlus, { locale: zhCn })

// 抑制非关键的 Vue emits 警告（开发模式）
if (import.meta.env.DEV) {
  const originalWarn = console.warn
  console.warn = (...args) => {
    // 忽略特定的 emits 警告
    if (args[0] && typeof args[0] === 'string' && args[0].includes('Extraneous non-emits event listeners')) {
      return
    }
    originalWarn.apply(console, args)
  }
}

app.mount('#app')

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
