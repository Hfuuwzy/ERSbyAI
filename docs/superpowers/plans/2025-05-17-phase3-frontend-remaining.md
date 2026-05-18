# Phase 3 Frontend UI/UX Upgrade - Remaining Pages Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:executing-plans` to implement this plan task-by-task.

**Goal:** Complete Phase 3 frontend upgrade by rewriting 7 remaining front pages with new design system

**Architecture:** Use established Vue 3 + SCSS design system with CSS variables and reusable components. Each page follows same pattern: import new components, replace inline styles with CSS classes, maintain all existing functionality.

**Tech Stack:** Vue 3, Element Plus, SCSS, CSS Variables, Vite

---

## Prerequisites

### Design System (Already Complete)
- **CSS Variables**: `vue/src/assets/css/design-system.scss`
  - Colors: --color-primary (#667eea), --text-primary, --bg-primary
  - Shadows: --shadow-md, --shadow-hover
  - Radii: --radius-md (16px), --radius-sm (8px)
  - Gradients: --gradient-hero, --gradient-button

- **Animations**: `vue/src/assets/css/animations.scss`
  - .animate-fade-up, .animate-gradient, .hover-lift

### Components (Already Complete)
- **GlassCard**: Glass morphism card with backdrop blur
- **GradientButton**: Gradient background button with hover glow
- **JobCard**: Job listing card with hover lift effect
- **TagSkill**: Colored skill tags (blue/purple/pink/orange/gray)
- **EmptyState**: Empty state placeholder with icon and action

### Reference Implementations
- **Home.vue**: Fully upgraded - use as template
- **Login.vue**: Form handling + GlassCard usage
- **Front.vue**: Layout patterns

---

## File Status

| File | Status | Priority |
|------|--------|----------|
| Search.vue | ❌ Old inline styles | P1 |
| PositionDetail.vue | ❌ Old inline styles | P1 |
| Resume.vue | ❌ Old inline styles | P2 |
| ResumeEdit.vue | ❌ Old inline styles | P2 |
| Submit.vue | ❌ Old inline styles | P2 |
| Collect.vue | ❌ Old inline styles | P2 |
| Person.vue | ❌ Old inline styles | P3 |

---

## Task Breakdown

### Task 1: Search.vue Upgrade

**Files:**
- Modify: `vue/src/views/front/Search.vue`

**Requirements:**
1. Centered search box with gradient search button
2. Hot search tags below search box
3. Job results in 3-column grid using JobCard component
4. Filter bar (city, salary, experience, education)
5. EmptyState when no results

**Implementation:**

```vue
<template>
  <div class="search-page">
    <div class="search-header">
      <h1 class="page-title">搜索职位</h1>
      <div class="search-box">
        <el-input v-model="data.name" placeholder="输入职位名称、公司名称" size="large" clearable @clear="reset" />
        <GradientButton @click="loadPosition">搜索</GradientButton>
      </div>
      <div class="hot-tags">
        <span>热门搜索：</span>
        <TagSkill v-for="tag in hotTags" :key="tag" :label="tag" @click="data.name = tag; loadPosition()" />
      </div>
    </div>
    
    <div class="search-filters">
      <el-select v-model="filters.city" placeholder="城市" clearable />
      <el-select v-model="filters.salary" placeholder="薪资" clearable />
      <el-select v-model="filters.experience" placeholder="经验" clearable />
      <el-select v-model="filters.education" placeholder="学历" clearable />
      <el-button v-if="hasFilters" @click="clearFilters" text>清除筛选</el-button>
    </div>
    
    <div class="search-results">
      <div v-if="data.positionData.length" class="results-grid">
        <JobCard v-for="job in data.positionData" :key="job.id" :job="job" />
      </div>
      <EmptyState v-else icon="🔍" title="没有找到相关职位" description="换个关键词试试看" action-text="查看全部职位" @action="reset" />
    </div>
  </div>
</template>
```

**Styles:**
- .search-page: min-height: 100vh, padding: 40px 24px
- .search-header: text-align: center, max-width: 800px, margin: 0 auto 40px
- .search-box: display: flex, gap: 12px, max-width: 600px
- .hot-tags: display: flex, gap: 8px, margin-top: 16px
- .search-filters: display: flex, gap: 12px, margin-bottom: 24px
- .results-grid: display: grid, grid-template-columns: repeat(3, 1fr), gap: 20px

**QA:**
- Search functionality works
- Results display in JobCard format
- Empty state shows when no results
- Filters can be cleared

---

### Task 2: PositionDetail.vue Upgrade

**Files:**
- Modify: `vue/src/views/front/PositionDetail.vue`

**Requirements:**
1. Gradient header with job title and salary
2. Meta info row (location, experience, education, type)
3. Action buttons (投递简历, 收藏)
4. Two-column layout: content left, sticky company card right
5. Job description with skill tags
6. Similar jobs at bottom

**Implementation:**

```vue
<template>
  <div class="position-page">
    <header class="position-header">
      <div class="header-content">
        <h1>{{ data.positionData.name }} <span class="salary">{{ data.positionData.salary }}</span></h1>
        <div class="meta-row">
          <span><el-icon><Location /></el-icon> {{ data.positionData.employCity }}</span>
          <span><el-icon><Calendar /></el-icon> {{ data.positionData.experience }}</span>
          <span><el-icon><School /></el-icon> {{ data.positionData.education }}</span>
          <span><el-icon><Monitor /></el-icon> {{ data.positionData.type }}</span>
        </div>
        <div class="actions">
          <GradientButton v-if="canSubmit" @click="submitInit">投递简历</GradientButton>
          <button class="btn-collect" @click="collect">收藏</button>
        </div>
      </div>
    </header>
    
    <div class="content-wrapper">
      <div class="main-content">
        <GlassCard class="section">
          <h2>职位描述</h2>
          <div class="tags">
            <TagSkill v-for="tag in data.positionData.tagList" :key="tag" :label="tag" />
          </div>
          <div class="description" v-html="data.positionData.content"></div>
        </GlassCard>
        
        <section class="similar-jobs">
          <h2>相似职位</h2>
          <div class="similar-grid">
            <JobCard v-for="job in similarJobs" :key="job.id" :job="job" />
          </div>
        </section>
      </div>
      
      <aside class="sidebar">
        <GlassCard :liftable="false" class="company-card">
          <h3>公司信息</h3>
          <div class="company-header">
            <img :src="data.positionData.employAvatar" />
            <div>
              <div class="company-name">{{ data.positionData.employName }}</div>
              <div class="company-stage">{{ data.positionData.employStage }}</div>
            </div>
          </div>
          <div class="company-info">
            <p><el-icon><OfficeBuilding /></el-icon> {{ data.positionData.industryName }}</p>
            <p><el-icon><User /></el-icon> {{ data.positionData.employScale }}</p>
            <p><el-icon><Location /></el-icon> {{ data.positionData.employAddress }}</p>
          </div>
        </GlassCard>
      </aside>
    </div>
  </div>
</template>
```

**Styles:**
- .position-header: gradient background, padding: 40px 0
- .header-content: max-width: 1200px, margin: 0 auto, color: white
- .content-wrapper: max-width: 1200px, margin: 40px auto, display: grid, grid-template-columns: 1fr 320px
- .sidebar: position: sticky, top: 20px

**QA:**
- Job details display correctly
- Company card sticky on scroll
- Submit and collect buttons work
- Similar jobs section populated

---

### Task 3: Resume.vue Upgrade

**Files:**
- Modify: `vue/src/views/front/Resume.vue`

**Requirements:**
1. Page header with "我的简历" title and create button
2. Resume cards grid (3 columns)
3. Each card: resume name, update time, salary, experience, actions
4. Create new resume card (special design)
5. EmptyState when no resumes

**Implementation:**
- Replace current card structure with GlassCard components
- Use Grid layout: 3 columns on desktop, 2 on tablet, 1 on mobile
- Actions: 编辑, 删除, 投递 buttons
- Keep all existing data fetching logic

**QA:**
- Resume list displays correctly
- Create new resume navigation works
- Edit/Delete/Submit actions work
- Empty state shows when no resumes

---

### Task 4: ResumeEdit.vue Upgrade

**Files:**
- Modify: `vue/src/views/front/ResumeEdit.vue`

**Requirements:**
1. Page header with "编辑简历" / "创建简历" title
2. Form sections in GlassCard containers:
   - 基本信息 (name, gender, expected salary, education, experience)
   - 教育经历 (list with add/remove)
   - 工作经历 (list with add/remove)
   - 项目经历 (list with add/remove)
3. Save and Preview buttons
4. Modal dialogs for adding/editing experiences

**Implementation:**
- Wrap each section in GlassCard
- Use consistent form styling (refer to Login.vue)
- Keep WangEditor for rich text if used
- Preserve all form validation

**QA:**
- Form loads with correct data
- Add/Edit/Remove experiences work
- Save functionality works
- Preview shows formatted resume

---

### Task 5: Submit.vue Upgrade

**Files:**
- Modify: `vue/src/views/front/Submit.vue`

**Requirements:**
1. Tab navigation: 全部, 待处理, 面试中, 已录用, 已拒绝
2. Application cards showing:
   - Position name and company
   - Application status (colored tag)
   - Salary
   - Application date
   - AI score if available
3. EmptyState when no applications

**Implementation:**
- Tab buttons with active state highlighting
- Each application in GlassCard
- Status colors mapping
- Grid or list layout

**QA:**
- Tab switching works
- Applications list correctly
- Status displayed accurately
- AI score shown when available

---

### Task 6: Collect.vue Upgrade

**Files:**
- Modify: `vue/src/views/front/Collect.vue`

**Requirements:**
1. Page title "我的收藏"
2. Grid of JobCard components
3. Unfavorite action on each card
4. EmptyState with "去逛逛" action

**Implementation:**
- Reuse JobCard component for each collected position
- 3-column grid layout
- Hover effect shows unfavorite button
- Simple upgrade - mostly styling changes

**QA:**
- Collection displays in JobCard format
- Unfavorite removes item
- Empty state works
- Navigation to detail works

---

### Task 7: Person.vue Upgrade

**Files:**
- Modify: `vue/src/views/front/Person.vue`

**Requirements:**
1. Profile header card with:
   - Large avatar
   - User name
   - Role badge
   - Contact info
2. Info sections in GlassCard:
   - 基本信息
   - 求职意向
   - 账号安全
3. Edit profile functionality

**Implementation:**
- Top hero section with user info
- Grid of info cards below
- Consistent with other upgraded pages

**QA:**
- User info displays correctly
- Edit functionality works
- All sections visible

---

## Common Patterns

### 1. Import Structure
```vue
<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import GlassCard from '@/components/GlassCard.vue'
import GradientButton from '@/components/GradientButton.vue'
import JobCard from '@/components/JobCard.vue'
import TagSkill from '@/components/TagSkill.vue'
import EmptyState from '@/components/EmptyState.vue'
</script>
```

### 2. Page Structure
```vue
<template>
  <div class="page-name">
    <!-- Header section -->
    <div class="page-header">
      <h1 class="page-title">Page Title</h1>
      <GradientButton @click="action">Action</GradientButton>
    </div>
    
    <!-- Content section -->
    <div class="content-area">
      <GlassCard v-for="item in data.items" :key="item.id">
        <!-- Card content -->
      </GlassCard>
    </div>
    
    <!-- Empty state -->
    <EmptyState v-if="!data.items.length" icon="📭" title="No Data" description="..." />
  </div>
</template>
```

### 3. Common Styles
```scss
.page-name {
  min-height: 100vh;
  background: var(--bg-primary);
  padding: 40px 24px;
}

.page-header {
  max-width: 1200px;
  margin: 0 auto 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
}

.content-area {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
```

---

## Dependencies & Parallelization

### Independent Tasks (Can Run in Parallel)
- Task 1: Search.vue
- Task 2: PositionDetail.vue
- Task 3: Resume.vue
- Task 6: Collect.vue

### Sequential Tasks
- Task 4: ResumeEdit.vue (after Resume.vue for consistency)
- Task 5: Submit.vue (simple, can be parallel)
- Task 7: Person.vue (lowest priority)

---

## Testing Criteria

### Manual QA Checklist Per Page
- [ ] Page loads without console errors
- [ ] All data displays correctly
- [ ] All buttons/actions work
- [ ] Navigation to/from page works
- [ ] Responsive on mobile/tablet/desktop
- [ ] Loading states handled
- [ ] Empty states show when appropriate
- [ ] No functionality broken

### Regressions to Check
- [ ] Login/Logout still works
- [ ] Home page still works
- [ ] Navigation between pages works
- [ ] Data persistence works
- [ ] API calls succeed

---

## Git Workflow

```bash
# Create branch
git checkout -b feature/phase3-frontend-complete

# After each page
git add vue/src/views/front/[Page].vue
git commit -m "feat: upgrade [Page] with new design system"

# Final push
git push origin feature/phase3-frontend-complete
# Then create PR
```

---

## Acceptance Criteria

- [ ] All 7 pages upgraded with new design system
- [ ] All pages use CSS variables instead of inline styles
- [ ] GlassCard, GradientButton components used where appropriate
- [ ] JobCard used for job listings
- [ ] EmptyState used for empty lists
- [ ] All functionality preserved (API calls, routing, actions)
- [ ] Consistent styling across all pages
- [ ] Mobile responsive layouts
- [ ] PR created and reviewed
- [ ] Phase 3 complete
