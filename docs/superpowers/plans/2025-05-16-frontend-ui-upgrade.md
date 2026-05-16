# Phase 3 Frontend UI/UX Upgrade Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `skill(name="executing-plans")` to implement this plan task-by-task. It will decide whether each batch should run in parallel or serial subagent mode and will pass only task-local context to each subagent. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 全面升级前台求职端 UI/UX，统一蓝紫粉渐变年轻活力风格，提升用户体验

**Architecture:** 
- 先建立设计系统（CSS 变量 + 动画 + 工具类）
- 再创建通用组件库（玻璃卡片、渐变按钮、职位卡片等）
- 最后重写所有页面，确保风格一致
- 响应式适配贯穿始终

**Tech Stack:** Vue 3 + Element Plus + Tailwind CSS + SCSS

---

## Prerequisite

Before starting, ensure:
- [ ] Vue dev server can start (`cd vue && npm run dev`)
- [ ] Backend is running for API testing

---

## Part 1: 设计系统 (Design System)

### Task 1.1: Create Design System CSS Variables

**Files:**
- Create: `vue/src/assets/css/design-system.scss`
- Modify: `vue/src/main.js` (import the scss)

**验收标准:** AC-001, AC-002

- [ ] **Step 1: Create design-system.scss with CSS variables**

```scss
// design-system.scss
:root {
  // 主色 - 蓝紫渐变
  --color-primary: #667eea;
  --color-secondary: #764ba2;
  --color-accent: #f093fb;

  // 背景梯度
  --gradient-hero: linear-gradient(135deg, #667eea 0%, #764ba2 40%, #f093fb 100%);
  --gradient-card: linear-gradient(145deg, #ffffff 0%, #f8fafc 100%);
  --gradient-button: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

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

  // 阴影
  --shadow-sm: 0 2px 8px rgba(0,0,0,0.08);
  --shadow-md: 0 4px 20px rgba(0,0,0,0.12);
  --shadow-lg: 0 8px 40px rgba(102, 126, 234, 0.2);
  --shadow-hover: 0 12px 30px rgba(102, 126, 234, 0.15);

  // 圆角
  --radius-sm: 8px;
  --radius-md: 16px;
  --radius-lg: 24px;
}
```

- [ ] **Step 2: Import in main.js**

```javascript
// Add to top of main.js
import './assets/css/design-system.scss'
```

- [ ] **Step 3: Verify variables work**

Create test in Home.vue temporarily:
```html
<div style="background: var(--gradient-hero); color: white; padding: 20px;">
  Test Gradient
</div>
```
Check browser shows gradient.

- [ ] **Step 4: Commit**

```bash
git add vue/src/assets/css/design-system.scss vue/src/main.js
git commit -m "feat: add design system CSS variables"
```

---

### Task 1.2: Create Animation Keyframes

**Files:**
- Create: `vue/src/assets/css/animations.scss`

**验收标准:** AC-002, AC-060

- [ ] **Step 1: Create animations.scss**

```scss
// animations.scss

// 淡入上滑
@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-up {
  animation: fadeUp 0.6s ease-out forwards;
}

// 渐变流动
@keyframes gradientShift {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

.animate-gradient {
  background-size: 200% 200%;
  animation: gradientShift 8s ease infinite;
}

// 悬浮动画
@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

.animate-float {
  animation: float 3s ease-in-out infinite;
}

// 悬浮效果类
.hover-lift {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  
  &:hover {
    transform: translateY(-8px);
    box-shadow: var(--shadow-hover);
  }
}

// 玻璃拟态
.glass {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

// 渐入延迟类
.stagger-1 { animation-delay: 0.1s; }
.stagger-2 { animation-delay: 0.2s; }
.stagger-3 { animation-delay: 0.3s; }
.stagger-4 { animation-delay: 0.4s; }
.stagger-5 { animation-delay: 0.5s; }
```

- [ ] **Step 2: Import in main.js**

```javascript
import './assets/css/animations.scss'
```

- [ ] **Step 3: Commit**

```bash
git add vue/src/assets/css/animations.scss vue/src/main.js
git commit -m "feat: add animation keyframes and utility classes"
```

---

## Part 2: 通用组件 (Reusable Components)

### Task 2.1: GlassCard Component

**Files:**
- Create: `vue/src/components/GlassCard.vue`

**验收标准:** AC-006

- [ ] **Step 1: Create GlassCard.vue**

```vue
<template>
  <div 
    class="glass-card"
    :class="{ 'hover-lift': liftable }"
  >
    <slot></slot>
  </div>
</template>

<script setup>
defineProps({
  liftable: {
    type: Boolean,
    default: true
  }
})
</script>

<style scoped>
.glass-card {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
  padding: 24px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.glass-card:hover {
  box-shadow: var(--shadow-lg);
}
</style>
```

- [ ] **Step 2: Test in Home.vue**

```vue
<GlassCard>
  <h3>Test Card</h3>
  <p>This is a glass card</p>
</GlassCard>
```

Verify: Card has glass effect, hover lifts up.

- [ ] **Step 3: Commit**

```bash
git add vue/src/components/GlassCard.vue
git commit -m "feat: add GlassCard component"
```

---

### Task 2.2: GradientButton Component

**Files:**
- Create: `vue/src/components/GradientButton.vue`

**验收标准:** AC-004, AC-007

- [ ] **Step 1: Create GradientButton.vue**

```vue
<template>
  <button 
    class="gradient-button"
    :class="{ 'secondary': variant === 'secondary' }"
    @click="$emit('click')"
  >
    <slot></slot>
  </button>
</template>

<script setup>
defineProps({
  variant: {
    type: String,
    default: 'primary' // 'primary' | 'secondary'
  }
})

defineEmits(['click'])
</script>

<style scoped>
.gradient-button {
  padding: 12px 24px;
  border: none;
  border-radius: var(--radius-sm);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  background: var(--gradient-button);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.gradient-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.gradient-button.secondary {
  background: white;
  color: var(--text-primary);
  border: 1px solid var(--border-light);
  box-shadow: var(--shadow-sm);
}

.gradient-button.secondary:hover {
  background: rgba(102, 126, 234, 0.05);
  border-color: var(--color-primary);
}
</style>
```

- [ ] **Step 2: Test both variants**

```vue
<GradientButton>Primary</GradientButton>
<GradientButton variant="secondary">Secondary</GradientButton>
```

- [ ] **Step 3: Commit**

```bash
git add vue/src/components/GradientButton.vue
git commit -m "feat: add GradientButton component"
```

---

### Task 2.3: JobCard Component

**Files:**
- Create: `vue/src/components/JobCard.vue`

**验收标准:** AC-003, AC-008

- [ ] **Step 1: Create JobCard.vue**

```vue
<template>
  <div class="job-card hover-lift" @click="navigate">
    <div class="job-header">
      <h3 class="job-title">{{ job.name }}</h3>
      <span class="job-salary">{{ job.salary }}</span>
    </div>
    
    <div class="job-tags">
      <TagSkill 
        v-for="tag in job.tagList" 
        :key="tag" 
        :label="tag"
      />
    </div>
    
    <div class="job-company">
      <img :src="job.employAvatar" class="company-logo" />
      <div class="company-info">
        <div class="company-name">{{ job.employName }}</div>
        <div class="company-stage">{{ job.employStage }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import TagSkill from './TagSkill.vue'

const props = defineProps({
  job: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const navigate = () => {
  router.push(`/front/positionDetail?id=${props.job.id}`)
}
</script>

<style scoped>
.job-card {
  background: white;
  border-radius: var(--radius-md);
  padding: 20px;
  box-shadow: var(--shadow-sm);
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.job-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-hover);
}

.job-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.job-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-primary);
  flex: 1;
  margin-right: 12px;
  line-height: 1.4;
}

.job-salary {
  color: #ef4444;
  font-weight: 700;
  font-size: 14px;
  white-space: nowrap;
}

.job-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 16px;
}

.job-company {
  display: flex;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid var(--border-light);
}

.company-logo {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  object-fit: cover;
  margin-right: 12px;
}

.company-info {
  flex: 1;
}

.company-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.company-stage {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 2px;
}
</style>
```

- [ ] **Step 2: Commit**

```bash
git add vue/src/components/JobCard.vue
git commit -m "feat: add JobCard component"
```

---

### Task 2.4: TagSkill Component

**Files:**
- Create: `vue/src/components/TagSkill.vue`

**验收标准:** AC-005

- [ ] **Step 1: Create TagSkill.vue**

```vue
<template>
  <span class="tag-skill" :class="tagClass">
    {{ label }}
  </span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  label: {
    type: String,
    required: true
  }
})

const tagClass = computed(() => {
  const label = props.label.toLowerCase()
  
  // Frontend related
  if (label.includes('前端') || label.includes('vue') || label.includes('react') || 
      label.includes('js') || label.includes('css') || label.includes('web')) {
    return 'tag-blue'
  }
  
  // Backend related
  if (label.includes('java') || label.includes('后端') || label.includes('python') || 
      label.includes('go') || label.includes('spring') || label.includes('node')) {
    return 'tag-purple'
  }
  
  // Product/UI
  if (label.includes('产品') || label.includes('ui') || label.includes('设计') || 
      label.includes('ux')) {
    return 'tag-pink'
  }
  
  // Operations/Marketing
  if (label.includes('运营') || label.includes('市场') || label.includes('销售')) {
    return 'tag-orange'
  }
  
  // Default
  return 'tag-gray'
})
</script>

<style scoped>
.tag-skill {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 9999px;
  font-size: 12px;
  font-weight: 500;
}

.tag-blue {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.tag-purple {
  background: rgba(118, 75, 162, 0.1);
  color: #764ba2;
}

.tag-pink {
  background: rgba(240, 147, 251, 0.1);
  color: #d946ef;
}

.tag-orange {
  background: rgba(245, 158, 11, 0.1);
  color: #f59e0b;
}

.tag-gray {
  background: rgba(148, 163, 184, 0.1);
  color: #64748b;
}
</style>
```

- [ ] **Step 2: Commit**

```bash
git add vue/src/components/TagSkill.vue
git commit -m "feat: add TagSkill component with color categories"
```

---

### Task 2.5: EmptyState Component

**Files:**
- Create: `vue/src/components/EmptyState.vue`

**验收标准:** AC-010

- [ ] **Step 1: Create EmptyState.vue**

```vue
<template>
  <div class="empty-state">
    <div class="empty-icon">{{ icon }}</div>
    <h3 class="empty-title">{{ title }}</h3>
    <p class="empty-description">{{ description }}</p>
    <GradientButton v-if="actionText" @click="$emit('action')">
      {{ actionText }}
    </GradientButton>
  </div>
</template>

<script setup>
import GradientButton from './GradientButton.vue'

defineProps({
  icon: {
    type: String,
    default: '📭'
  },
  title: {
    type: String,
    required: true
  },
  description: {
    type: String,
    default: ''
  },
  actionText: {
    type: String,
    default: ''
  }
})

defineEmits(['action'])
</script>

<style scoped>
.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.empty-description {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 24px;
}
</style>
```

- [ ] **Step 2: Commit**

```bash
git add vue/src/components/EmptyState.vue
git commit -m "feat: add EmptyState component"
```

---

## Part 3: 布局外壳 (Front.vue)

### Task 3.1: Rewrite Front.vue Header

**Files:**
- Modify: `vue/src/views/Front.vue`

**验收标准:** AC-011, AC-012, AC-013, AC-015

- [ ] **Step 1: Backup original Front.vue**

```bash
cp vue/src/views/Front.vue vue/src/views/Front.vue.backup
```

- [ ] **Step 2: Rewrite template section**

```vue
<template>
  <div class="front-layout">
    <!-- 公告条 -->
    <div v-if="data.top" class="notice-bar">
      <el-icon><Bell /></el-icon>
      <span>{{ data.top }}</span>
    </div>
    
    <!-- 新 Header -->
    <header class="main-header" :class="{ 'scrolled': isScrolled }">
      <div class="header-container">
        <!-- Logo -->
        <div class="logo" @click="router.push('/front/home')">
          <img src="@/assets/imgs/logo.png" alt="85Boss" />
          <span class="logo-text">85Boss</span>
        </div>
        
        <!-- Desktop Nav -->
        <nav class="desktop-nav">
          <router-link to="/front/home" :class="{ active: route.path === '/front/home' }">首页</router-link>
          <router-link to="/front/collect" :class="{ active: route.path === '/front/collect' }">我的收藏</router-link>
          <router-link to="/front/resume" :class="{ active: route.path === '/front/resume' }">我的简历</router-link>
          <router-link to="/front/submit" :class="{ active: route.path === '/front/submit' }">我的投递</router-link>
          <router-link to="/front/person" :class="{ active: route.path === '/front/person' }">个人中心</router-link>
        </nav>
        
        <!-- Mobile Menu Button -->
        <button class="mobile-menu-btn" @click="toggleMobileMenu">
          <el-icon><Menu /></el-icon>
        </button>
        
        <!-- User Section -->
        <div class="user-section">
          <template v-if="!data.user.id">
            <button class="btn-text" @click="router.push('/login')">登录</button>
            <GradientButton @click="router.push('/register')">注册</GradientButton>
          </template>
          <template v-else>
            <el-dropdown trigger="click">
              <div class="user-avatar">
                <img :src="data.user.avatar" />
                <span class="user-name">{{ data.user.name }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </div>
      </div>
    </header>
    
    <!-- Mobile Nav Drawer -->
    <div class="mobile-drawer" :class="{ 'open': mobileMenuOpen }">
      <nav class="mobile-nav">
        <router-link v-for="item in navItems" :key="item.path" :to="item.path" @click="mobileMenuOpen = false">
          {{ item.name }}
        </router-link>
      </nav>
    </div>
    
    <!-- Main Content -->
    <main class="main-content">
      <RouterView @updateUser="updateUser" />
    </main>
    
    <!-- Footer -->
    <footer class="main-footer">
      <!-- ... footer content ... -->
    </footer>
  </div>
</template>
```

[Additional steps would continue with script and style sections...]

---

## Part 4: 页面实现 (Pages)

**Note:** Due to length constraints, showing structure only. Each page follows similar pattern:

### Task 4.X: [PageName].vue
- Import components
- Rewrite template with new design
- Update styles
- Test functionality preserved
- Commit

**Page List:**
1. Home.vue - Hero + Bento Grid + Job Lists
2. PositionDetail.vue - Job info + Company sidebar
3. Search.vue - Search box + Filters + Results grid
4. Resume.vue - Resume cards list
5. ResumeEdit.vue - Form sections
6. Submit.vue - Tab filter + Records list
7. Collect.vue - Collection grid
8. Person.vue - Profile + Settings
9. Login.vue - Gradient bg + Glass card
10. Register.vue - Same as login

---

## Implementation Order

**Phase 3A (This Sprint):**
1. ✅ Design System (Task 1.1, 1.2) - 并行执行
2. ✅ Components (Task 2.1-2.5) - 依次执行  
3. 🔄 Front.vue Layout (Task 3.1)
4. 🔄 Home.vue (Task 4.1) - 最高优先级
5. 🔄 Search.vue (Task 4.3)
6. 🔄 PositionDetail.vue (Task 4.2)

**Phase 3B (Next Sprint):**
7. Resume.vue, ResumeEdit.vue
8. Submit.vue, Collect.vue
9. Person.vue
10. Login.vue, Register.vue
11. Responsive polish
12. Animation optimization

---

## Testing Checklist

- [ ] All 66 AC criteria pass
- [ ] Mobile responsive (320px, 768px, 1024px, 1440px)
- [ ] No console errors
- [ ] All original functionality works
- [ ] Lighthouse score > 90

---

**Ready for execution with `superpowers:executing-plans`**
