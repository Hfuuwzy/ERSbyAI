# Phase 3: 前台 UI/UX 全面升级设计

**目标**: 前台求职端全面 UI/UX 升级，统一年轻活力风格，提升用户体验  
**日期**: 2025-05-16  
**状态**: 已确认，待实施  

---

## 1. 设计体系 (Design System)

### 1.1 色彩系统

```scss
// 主色 - 蓝紫渐变（年轻科技）
--color-primary: #667eea;
--color-secondary: #764ba2;
--color-accent: #f093fb;

// 背景梯度
--gradient-hero: linear-gradient(135deg, #667eea 0%, #764ba2 40%, #f093fb 100%);
--gradient-card: linear-gradient(145deg, #ffffff 0%, #f8fafc 100%);

// 功能色
--color-success: #10b981;
--color-warning: #f59e0b;
--color-error: #ef4444;
--color-info: #3b82f6;

// 中性色
--text-primary: #1e293b;
--text-secondary: #64748b;
--text-muted: #94a3b8;
--bg-primary: #f8fafc;
--bg-white: #ffffff;
--border-light: #e2e8f0;
```

### 1.2 字体系统

- **主字体**: 思源黑体 / Inter
- **标题**: font-weight: 700, color: --text-primary
- **正文**: font-weight: 400, line-height: 1.6, color: --text-secondary
- **辅助文字**: font-weight: 400, color: --text-muted

### 1.3 通用组件规格

| 组件 | 规格 |
|------|------|
| **卡片** | 圆角 16px (rounded-2xl), 白色背景, 阴影 `0 4px 20px rgba(0,0,0,0.08)`, hover 上浮 8px |
| **按钮-主色** | 渐变背景 (--gradient-hero), 圆角 8px, hover 发光效果 |
| **按钮-次色** | 白色背景, 边框 1px solid --border-light, hover 淡紫背景 |
| **标签** | 彩色圆角标签（技能-蓝/紫/粉）, 圆角 9999px |
| **输入框** | 圆角 8px, focus 时边框变为 --color-primary |
| **导航** | 毛玻璃效果 (backdrop-blur-md), 固定顶部, 滚动显示阴影 |

### 1.4 动效库

```css
/* 淡入上滑 */
@keyframes fadeUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-up { animation: fadeUp 0.6s ease-out; }

/* 悬浮上浮 */
.hover-lift {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.hover-lift:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.15);
}

/* 渐变流动 */
@keyframes gradientShift {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}
.animate-gradient {
  background-size: 200% 200%;
  animation: gradientShift 8s ease infinite;
}
```

---

## 2. 页面架构

### 2.1 Front.vue - 布局外壳

**Header 设计**:
- 固定顶部 (fixed top-0), z-index 50
- 背景: `bg-white/80 backdrop-blur-md`
- 滚动超过 50px 时添加阴影 `shadow-md`
- 布局: Logo(左) + 导航(中) + 用户区(右)
- 导航项: 首页 | 职位搜索 | 企业
- 移动端: 汉堡菜单

**Footer 设计**:
- 四栏布局: 关于我们 | 求职者 | 企业 | 联系方式
- 底部渐变色条
- 社交媒体图标行
- 备案信息

---

## 3. 各页面详细设计

### 3.1 Home.vue - 首页

#### Hero 区域
- 全宽渐变背景 (animate-gradient)
- 大标题: "发现你的下一份理想工作"
- 副标题: "AI 智能匹配 + 海量优质岗位"
- 居中搜索框 + 渐变搜索按钮
- 浮动装饰元素 (模糊圆形, 轻微动画)

#### 广告位 - Bento Grid
```
┌─────────┬─────────────────┬─────────┐
│  广告1   │                 │  广告2   │
│  竖版    │     广告3       │  竖版    │
│          │    横幅主广告    │          │
├─────────┼─────────────────┼─────────┤
│  广告4   │      广告5      │  广告6   │
│  横版    │      横版       │  横版    │
└─────────┴─────────────────┴─────────┘
```
- 不规则网格布局
- 圆角 16px, hover 放大 1.02 倍
- 渐变遮罩保证文字可读

#### 精选岗位
- 横向滚动容器
- JobCard 组件: 玻璃拟态风格
- 左右箭头控制
- 卡片内容: 职位名 + 薪资 + 技能标签 + 公司信息

#### 热招岗位
- Tab 切换带滑动指示器动画
- 3 列网格布局
- 热度标识 🔥
- 快速投递按钮

### 3.2 PositionDetail.vue - 职位详情

#### 顶部信息区
- 渐变背景 Header (弱化原深蓝灰)
- 职位名称 + 薪资 (高亮)
- 基本信息行: 📍城市 | 📅经验 | 🎓学历 | 💼类型
- 操作按钮: 💜 收藏 | 💼 投递简历 (渐变按钮)

#### 内容区 (左右分栏)
- **左侧 (70%)**:
  - 职位描述区块
  - 技能标签云 (彩色标签)
  - 富文本内容区
  - 相似职位推荐 (横向滚动)

- **右侧 (30%)**:
  - 公司信息卡片 (Sticky)
  - Logo + 名称 + 发展阶段 + 行业 + 规模
  - 在招职位列表 (前 5 条)

### 3.3 Search.vue - 搜索页

#### 搜索区
- 大型居中搜索框 (宽度 600px)
- 热门搜索词标签行
- 渐变搜索按钮

#### 筛选栏
- 城市 | 薪资 | 经验 | 学历
- 下拉选择器
- 清除筛选按钮

#### 结果区
- 3 列网格, JobCard 组件
- 加载更多按钮 / 分页器
- 无结果友好提示 + 推荐职位

### 3.4 Resume.vue - 我的简历

#### 页面头部
- 标题 "我的简历" + 新建简历按钮 (渐变)

#### 简历列表
- 卡片列表式布局
- 每条简历显示:
  - 简历名称
  - 更新日期
  - 期望薪资 + 工作经验
  - 操作按钮: 编辑 | 删除 | 投递
- 空状态: 引导创建第一条简历

### 3.5 ResumeEdit.vue - 简历编辑

#### 页面头部
- 标题 "编辑简历" + 保存 | 预览按钮

#### 分区块表单
- **基本信息**: 姓名、性别、期望薪资等 (2 列布局)
- **教育经历**: 可添加多条, 时间轴展示
- **工作经历**: 同上
- **项目经历**: 同上

#### 交互
- 新增经历用 Modal 弹窗
- 实时保存草稿提示
- 表单验证提示

### 3.6 Submit.vue - 我的投递

#### Tab 筛选
- 全部 | 待处理 | 面试中 | 已录用 | 已拒绝
- 滑动指示器动画

#### 投递记录列表
- 卡片式展示
- 显示: 职位名 + 公司 + 状态标签 + 薪资
- AI 评估分数 (如有)
- 时间轴视图切换选项

### 3.7 Collect.vue - 我的收藏

- 网格布局, JobCard 组件
- 收藏星标 hover 显示取消
- 空状态友好引导

### 3.8 Person.vue - 个人中心

#### 顶部个人卡片
- 大头像
- 姓名 + 职位标签 + 联系方式

#### 信息区块
- 基本信息
- 求职意向 (可编辑)

### 3.9 登录/注册页

- 全屏渐变背景
- 居中玻璃拟态卡片
- 表单输入框
- 社交登录按钮 (微信、GitHub)
- 切换登录/注册链接

---

## 4. 组件清单

### 4.1 新增组件

| 组件名 | 路径 | 功能 |
|--------|------|------|
| GlassCard | components/GlassCard.vue | 玻璃拟态卡片容器 |
| GradientButton | components/GradientButton.vue | 渐变按钮 |
| JobCard | components/JobCard.vue | 职位卡片 |
| TagSkill | components/TagSkill.vue | 技能标签 (彩色) |
| SearchBox | components/SearchBox.vue | 搜索框组件 |
| EmptyState | components/EmptyState.vue | 空状态提示 |
| LoadingState | components/LoadingState.vue | 加载状态 |
| FadeInView | components/FadeInView.vue | 淡入动画容器 |

### 4.2 样式文件

| 文件 | 内容 |
|------|------|
| assets/css/design-system.scss | CSS 变量、工具类 |
| assets/css/animations.scss | 通用动画关键帧 |

---

## 5. 响应式断点

| 断点 | 宽度 | 布局调整 |
|------|------|----------|
| Mobile | < 640px | 单列, 汉堡菜单 |
| Tablet | 640-1024px | 2 列网格 |
| Desktop | > 1024px | 3-4 列网格 |

---

## 6. 验收标准

### 6.1 视觉标准
- [ ] 所有页面使用统一色彩系统
- [ ] 所有卡片圆角 16px, 有悬浮效果
- [ ] 所有按钮有 hover 状态
- [ ] 渐变背景有流动动画
- [ ] 无风格割裂或突兀页面

### 6.2 交互标准
- [ ] 页面切换有淡入动画
- [ ] 卡片悬浮上浮效果流畅
- [ ] Tab 切换有滑动指示器
- [ ] 移动端菜单动画顺畅

### 6.3 功能标准
- [ ] 所有原功能正常运行
- [ ] 搜索、筛选、分页正常
- [ ] 表单提交、验证正常
- [ ] 图片加载有占位/错误处理

### 6.4 响应式标准
- [ ] 移动端无布局错乱
- [ ] 触摸目标最小 44px
- [ ] 字体大小适配可读

---

## 7. 文件修改清单

### 7.1 新增文件
```
vue/src/assets/css/design-system.scss
vue/src/assets/css/animations.scss
vue/src/components/GlassCard.vue
vue/src/components/GradientButton.vue
vue/src/components/JobCard.vue
vue/src/components/TagSkill.vue
vue/src/components/SearchBox.vue
vue/src/components/EmptyState.vue
vue/src/components/LoadingState.vue
vue/src/components/FadeInView.vue
```

### 7.2 重写文件
```
vue/src/views/Front.vue
vue/src/views/front/Home.vue
vue/src/views/front/PositionDetail.vue
vue/src/views/front/Search.vue
vue/src/views/front/Resume.vue
vue/src/views/front/ResumeEdit.vue
vue/src/views/front/Submit.vue
vue/src/views/front/Collect.vue
vue/src/views/front/Person.vue
vue/src/views/Login.vue
vue/src/views/Register.vue
vue/src/views/404.vue
```

---

**下一步**: 编写验收标准文档 (writing-acceptance-criteria) → 制定实施计划 (writing-plans) → 代码开发
