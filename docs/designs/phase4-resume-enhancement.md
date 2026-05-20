# Phase 4: 简历功能增强设计文档

**日期**: 2025-05-19  
**版本**: 1.0  
**状态**: 待实现  
**方案**: 方案A（前端PDF + localStorage自动保存）

---

## 1. 概述

### 1.1 目标
为简历系统添加两个增强功能：
1. **PDF导出** - 精美模板样式，支持自定义模板，导出基本信息
2. **自动保存** - 1分钟自动保存频率，localStorage存储，支持恢复草稿

### 1.2 范围
- **In Scope**: 前端Vue实现，localStorage存储，浏览器PDF生成
- **Out of Scope**: 后端API改动，数据库存储，多设备同步

---

## 2. PDF导出功能

### 2.1 技术选型
- **PDF库**: `html2pdf.js` (基于html2canvas + jsPDF)
- **模板系统**: Vue组件 + CSS样式（无需后端模板引擎）
- **安装**: `npm install html2pdf.js`

### 2.2 模板设计
支持3种内置模板：
1. **简约模板** - 白底黑字，清晰布局
2. **专业模板** - 带侧边栏，强调技能
3. **创意模板** - 现代设计，适合设计岗位

### 2.3 导出内容
```
基本信息：
- 姓名
- 性别
- 联系电话
- 联系邮箱
- 学历
- 工作年限
- 期望薪资
```

### 2.4 UI流程
1. 用户在简历编辑页点击"导出PDF"按钮
2. 弹出模板选择对话框
3. 选择模板后，实时预览PDF效果
4. 确认后生成并下载PDF文件

### 2.5 实现位置
- **文件**: `vue/src/components/ResumePdfTemplate.vue` - PDF模板组件
- **文件**: `vue/src/components/ResumePdfExport.vue` - 导出对话框组件
- **修改**: `vue/src/views/front/ResumeEdit.vue` - 添加导出按钮

---

## 3. 自动保存功能

### 3.1 技术方案
- **存储位置**: localStorage
- **存储Key**: `resume_draft_{userId}`
- **保存频率**: 1分钟（60000ms）
- **防抖**: 300ms（防止频繁触发）

### 3.2 自动保存逻辑
```javascript
// 伪代码
watch(resumeData, debounce(() => {
  // 数据变化时，标记为"需要保存"
  needsAutoSave = true
}, 300))

// 定时器每1分钟检查
setInterval(() => {
  if (needsAutoSave && hasUnsavedChanges) {
    saveToLocalStorage()
    needsAutoSave = false
    lastSaveTime = Date.now()
  }
}, 60000)
```

### 3.3 草稿恢复逻辑
**进入编辑页时：**
1. 检查URL是否有resumeId参数
2. 如果有ID，加载服务端数据
3. 同时检查localStorage是否有草稿
4. 比较草稿时间和服务端数据时间
5. 如果草稿较新，提示"是否恢复未保存的草稿？"

### 3.4 草稿清理
- **手动保存成功后**: 清除localStorage草稿
- **用户选择不恢复**: 清除localStorage草稿
- **新创建简历**: 检查并提示恢复之前的草稿

### 3.5 UI反馈
- **保存状态指示器**: 页面右上角显示"已保存"或"保存中..."
- **最后保存时间**: 显示"最后自动保存: 2分钟前"
- **恢复提示**: 使用ElMessageBox弹窗

---

## 4. 数据模型

### 4.1 草稿数据结构
```javascript
{
  "resumeId": 123,           // 可选，编辑现有简历时存在
  "data": {                  // 完整简历数据
    "name": "简历名称",
    "username": "姓名",
    "sex": "男",
    "salary": "10-20k",
    "education": "本科",
    "experience": "3到5年",
    "phone": "13800138000",
    "email": "example@email.com",
    "eduExps": [...],
    "workExps": [...],
    "proExps": [...]
  },
  "timestamp": 1716100000000, // 保存时间戳
  "template": "professional"  // 当前选中的PDF模板
}
```

### 4.2 localStorage存储
```javascript
// Key格式
const DRAFT_KEY = `resume_draft_${userId}`

// 存储内容
localStorage.setItem(DRAFT_KEY, JSON.stringify(draftData))

// 读取草稿
const draft = JSON.parse(localStorage.getItem(DRAFT_KEY))
```

---

## 5. 界面设计

### 5.1 简历编辑页修改
```
┌─────────────────────────────────────────────────────┐
│  [编辑简历]                    [导出PDF] [保存简历]  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  [自动保存状态: ✓ 已保存  2分钟前]                  │
│                                                     │
│  ┌───────────────────────────────────────────────┐  │
│  │  基本信息                                      │  │
│  │  [表单内容...]                                │  │
│  └───────────────────────────────────────────────┘  │
│                                                     │
└─────────────────────────────────────────────────────┘
```

### 5.2 PDF导出对话框
```
┌─────────────────────────────────┐
│  导出PDF                       │
├─────────────────────────────────┤
│  选择模板:                      │
│  [○ 简约] [○ 专业] [○ 创意]    │
│                                 │
│  [预览区域]                     │
│  ┌─────────────────────────┐   │
│  │                         │   │
│  │    PDF预览             │   │
│  │                         │   │
│  └─────────────────────────┘   │
│                                 │
│  [取消]              [下载PDF] │
└─────────────────────────────────┘
```

### 5.3 草稿恢复提示
```
┌─────────────────────────────────┐
│  恢复草稿?                      │
├─────────────────────────────────┤
│  发现未保存的草稿 (5分钟前)     │
│                                 │
│  是否恢复草稿继续编辑?          │
│                                 │
│  [放弃草稿]        [恢复草稿]   │
└─────────────────────────────────┘
```

---

## 6. 实现步骤

### 6.1 第一阶段: PDF导出
1. 安装html2pdf.js依赖
2. 创建ResumePdfTemplate.vue模板组件
3. 创建ResumePdfExport.vue导出对话框
4. 在ResumeEdit.vue添加导出按钮和调用逻辑
5. 测试PDF导出功能

### 6.2 第二阶段: 自动保存
1. 实现自动保存逻辑（watch + setInterval）
2. 添加保存状态UI指示器
3. 实现草稿恢复逻辑
4. 测试自动保存和恢复功能
5. 优化性能和用户体验

---

## 7. 技术细节

### 7.1 html2pdf.js使用示例
```javascript
import html2pdf from 'html2pdf.js'

const generatePDF = async () => {
  const element = document.getElementById('resume-template')
  const opt = {
    margin: 10,
    filename: '我的简历.pdf',
    image: { type: 'jpeg', quality: 0.98 },
    html2canvas: { scale: 2 },
    jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
  }
  await html2pdf().set(opt).from(element).save()
}
```

### 7.2 防抖实现
```javascript
const debounce = (fn, delay) => {
  let timer = null
  return function (...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => fn.apply(this, args), delay)
  }
}
```

### 7.3 localStorage限制处理
```javascript
// 检查存储空间（简单方案）
const checkStorage = () => {
  try {
    const test = 'x'.repeat(1024 * 1024) // 1MB
    localStorage.setItem('test', test)
    localStorage.removeItem('test')
    return true
  } catch (e) {
    return false // 存储已满
  }
}
```

---

## 8. 验收标准

见配套验收标准文档: `docs/superpowers/acceptance/2025-05-19-resume-enhancement.md`

---

## 9. 风险与注意事项

### 9.1 已知限制
- localStorage容量限制（约5-10MB）
- 不同浏览器PDF渲染可能有细微差异
- localStorage数据不会跨设备同步

### 9.2 缓解措施
- 草稿数据精简（不包含大字段）
- PDF模板使用标准CSS
- 明确提示用户这是本地草稿

---

## 10. 附录

### 10.1 依赖安装
```bash
cd vue
npm install html2pdf.js
```

### 10.2 参考资料
- html2pdf.js文档: https://ekoopmans.github.io/html2pdf.js/
- localStorage API: https://developer.mozilla.org/en-US/docs/Web/API/Window/localStorage
