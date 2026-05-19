<template>
  <div class="search-page">
    <!-- Hero / Search Header -->
    <section class="search-hero">
      <div class="hero-bg"></div>
      <div class="hero-blob blob-1"></div>
      <div class="hero-blob blob-2"></div>

      <div class="hero-content">
        <h1 class="hero-title">寻找你的下一份机会</h1>
        <p class="hero-subtitle">海量岗位 · 精准筛选 · AI 智能匹配</p>

        <div class="search-bar">
          <el-input
            v-model="data.name"
            size="large"
            clearable
            class="search-input"
            placeholder="请输入您感兴趣的职位、公司或技能"
            @clear="reset"
            @keyup.enter="handleSearch"
          />
          <GradientButton @click="handleSearch">搜索</GradientButton>
        </div>

        <div class="search-history-row" v-if="data.searchHistory.length">
          <span class="history-label">最近搜索</span>
          <TagSkill
            v-for="item in data.searchHistory"
            :key="item"
            :label="item"
            class="history-tag-clickable"
            @click="searchByHistory(item)"
          />
          <button class="history-clear" type="button" @click="clearHistory">清除</button>
        </div>

        <div class="hot-tags-row" v-if="hotTags.length">
          <span class="hot-label">热门搜索</span>
          <TagSkill
            v-for="tag in hotTags"
            :key="tag"
            :label="tag"
            class="hot-tag-clickable"
            @click="searchByTag(tag)"
          />
        </div>
      </div>
    </section>

    <!-- Filters -->
    <section class="filters-section">
      <GlassCard :liftable="false" class="filters-card">
        <div class="filter-row">
          <div class="filter-item">
            <label class="filter-label">城市</label>
            <el-select
              v-model="data.filters.city"
              placeholder="不限"
              clearable
              class="filter-select"
            >
              <el-option
                v-for="opt in cityOptions"
                :key="opt"
                :label="opt"
                :value="opt"
              />
            </el-select>
          </div>

          <div class="filter-item">
            <label class="filter-label">薪资</label>
            <el-select
              v-model="data.filters.salary"
              placeholder="不限"
              clearable
              class="filter-select"
            >
              <el-option
                v-for="opt in salaryOptions"
                :key="opt"
                :label="opt"
                :value="opt"
              />
            </el-select>
          </div>

          <div class="filter-item">
            <label class="filter-label">经验</label>
            <el-select
              v-model="data.filters.experience"
              placeholder="不限"
              clearable
              class="filter-select"
            >
              <el-option
                v-for="opt in experienceOptions"
                :key="opt"
                :label="opt"
                :value="opt"
              />
            </el-select>
          </div>

          <div class="filter-item">
            <label class="filter-label">学历</label>
            <el-select
              v-model="data.filters.education"
              placeholder="不限"
              clearable
              class="filter-select"
            >
              <el-option
                v-for="opt in educationOptions"
                :key="opt"
                :label="opt"
                :value="opt"
              />
            </el-select>
          </div>

          <div class="filter-actions">
            <GradientButton variant="secondary" @click="clearFilters">重置</GradientButton>
          </div>
        </div>
      </GlassCard>
    </section>

    <!-- Results -->
    <section class="results-section">
      <div class="results-header">
        <h2 class="results-title">搜索结果</h2>
        <span class="results-count">共 {{ data.total }} 个岗位<span v-if="data.total">，当前显示 {{ resultRange }}</span></span>
      </div>

      <template v-if="data.positionData.length">
        <div class="jobs-grid">
          <JobCard
            v-for="job in data.positionData"
            :key="job.id"
            :job="job"
          />
        </div>

        <div class="pagination-wrap">
          <el-pagination
            v-model:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            :page-sizes="pageSizes"
            :total="data.total"
            layout="total, sizes, prev, pager, next, jumper"
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </template>

      <EmptyState
        v-else
        icon="🔍"
        title="没有找到匹配的岗位"
        description="试着换个关键词或者重置筛选条件"
        action-text="重置筛选"
        @action="clearFilters"
      />
    </section>
  </div>
</template>

<script setup>
import { reactive, computed, nextTick, watch } from 'vue'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import router from '@/router/index.js'
import GlassCard from '@/components/GlassCard.vue'
import GradientButton from '@/components/GradientButton.vue'
import JobCard from '@/components/JobCard.vue'
import TagSkill from '@/components/TagSkill.vue'
import EmptyState from '@/components/EmptyState.vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  positionData: [],
  name: '',
  searchHistory: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  filters: {
    city: '',
    salary: '',
    experience: '',
    education: ''
  }
})

const HISTORY_KEY = 'xm-search-history'
const URL_KEYS = ['name', 'city', 'salary', 'experience', 'education', 'page', 'pageSize']

const hotTags = ['前端', 'Java', 'Python', 'Vue', 'React', 'UI', '产品', '运营']
const pageSizes = [10, 20, 50]

const salaryOptions = ['面议', '5K以下', '5K-10K', '10K-20K', '20K-30K', '30K以上']
const experienceOptions = ['不限', '应届生', '1年以下', '1-3年', '3-5年', '5-10年', '10年以上']
const educationOptions = ['不限', '高中', '大专', '本科', '硕士', '博士']
const cityOptions = ['北京市', '上海市', '合肥市', '广州市', '深圳市', '杭州市', '南京市', '成都市', '武汉市', '西安市']

const resultRange = computed(() => {
  if (!data.total) return '0-0'
  const start = (data.pageNum - 1) * data.pageSize + 1
  const end = Math.min(data.pageNum * data.pageSize, data.total)
  return `${start}-${end}`
})

const getQueryValue = (value) => {
  if (Array.isArray(value)) return value[0] || ''
  return value || ''
}

const getPositiveNumber = (value, fallback) => {
  const number = Number.parseInt(getQueryValue(value), 10)
  return Number.isFinite(number) && number > 0 ? number : fallback
}

const getFilterParam = (value) => {
  return value && value !== '不限' ? value : undefined
}

const buildQueryFromState = () => {
  const query = {}
  if (data.name) query.name = data.name
  Object.keys(data.filters).forEach(key => {
    const value = getFilterParam(data.filters[key])
    if (value) query[key] = value
  })
  if (data.pageNum > 1) query.page = String(data.pageNum)
  if (data.pageSize !== 10) query.pageSize = String(data.pageSize)
  return query
}

const isSameQuery = (currentQuery, nextQuery) => {
  return URL_KEYS.every(key => getQueryValue(currentQuery[key]) === (nextQuery[key] || ''))
}

const loadHistory = () => {
  try {
    const history = JSON.parse(localStorage.getItem(HISTORY_KEY) || '[]')
    data.searchHistory = Array.isArray(history) ? history.filter(Boolean).slice(0, 10) : []
  } catch (e) {
    data.searchHistory = []
  }
}

const saveHistory = () => {
  const keyword = (data.name || '').trim()
  if (!keyword) return

  data.searchHistory = [
    keyword,
    ...data.searchHistory.filter(item => item !== keyword)
  ].slice(0, 10)
  localStorage.setItem(HISTORY_KEY, JSON.stringify(data.searchHistory))
}

const clearHistory = () => {
  data.searchHistory = []
  localStorage.removeItem(HISTORY_KEY)
}

let syncingFromUrl = false
let syncingToUrl = false

const syncUrlToState = (query = router.currentRoute.value.query) => {
  syncingFromUrl = true
  data.name = getQueryValue(query.name)
  data.filters.city = getQueryValue(query.city)
  data.filters.salary = getQueryValue(query.salary)
  data.filters.experience = getQueryValue(query.experience)
  data.filters.education = getQueryValue(query.education)
  data.pageNum = getPositiveNumber(query.page, 1)
  const pageSize = getPositiveNumber(query.pageSize, 10)
  data.pageSize = pageSizes.includes(pageSize) ? pageSize : 10
  nextTick(() => {
    syncingFromUrl = false
  })
}

const syncStateToUrl = () => {
  const query = buildQueryFromState()
  if (isSameQuery(router.currentRoute.value.query, query)) return
  syncingToUrl = true
  router.replace({ query }).finally(() => {
    syncingToUrl = false
  })
}

const loadPosition = (options = {}) => {
  syncStateToUrl()
  if (options.saveHistory !== false) saveHistory()

  request.get('/position/selectPage', {
    params: {
      name: data.name,
      status: '审核通过',
      city: getFilterParam(data.filters.city),
      employCity: getFilterParam(data.filters.city),
      salary: getFilterParam(data.filters.salary),
      experience: getFilterParam(data.filters.experience),
      education: getFilterParam(data.filters.education),
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then(res => {
    if (res.code === '200') {
      data.positionData = res.data?.list || []
      data.total = res.data?.total || 0
      data.pageNum = res.data?.pageNum || data.pageNum
      data.pageSize = res.data?.pageSize || data.pageSize
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () => {
  data.name = ''
  data.pageNum = 1
  loadPosition({ saveHistory: false })
}

const clearFilters = () => {
  data.filters.city = ''
  data.filters.salary = ''
  data.filters.experience = ''
  data.filters.education = ''
  data.pageNum = 1
  loadPosition({ saveHistory: false })
}

const handleSearch = () => {
  data.pageNum = 1
  loadPosition()
}

const searchByTag = (tag) => {
  data.name = tag
  handleSearch()
}

const searchByHistory = (keyword) => {
  data.name = keyword
  handleSearch()
}

const handleCurrentChange = (pageNum) => {
  data.pageNum = pageNum
  loadPosition({ saveHistory: false })
}

const handleSizeChange = (pageSize) => {
  data.pageSize = pageSize
  data.pageNum = 1
  loadPosition({ saveHistory: false })
}

watch(
  () => router.currentRoute.value.query,
  (query) => {
    if (syncingToUrl) return
    syncUrlToState(query)
    loadPosition({ saveHistory: false })
  }
)

watch(
  () => data.filters,
  () => {
    if (syncingFromUrl) return
    data.pageNum = 1
    loadPosition({ saveHistory: false })
  },
  { deep: true }
)

loadHistory()
syncUrlToState()
loadPosition({ saveHistory: false })
</script>

<style scoped>
.search-page {
  background: var(--bg-primary);
  min-height: 100vh;
  padding-bottom: 80px;
}

/* ===== Hero ===== */
.search-hero {
  position: relative;
  min-height: 380px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 70px 20px 90px;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background: var(--gradient-hero);
  background-size: 200% 200%;
  animation: gradientFlow 15s ease infinite;
  z-index: 0;
}

@keyframes gradientFlow {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.hero-blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.4;
  z-index: 1;
  pointer-events: none;
}

.blob-1 {
  width: 380px;
  height: 380px;
  background: rgba(255, 255, 255, 0.5);
  top: -120px;
  left: -100px;
  animation: blobFloat 18s ease-in-out infinite;
}

.blob-2 {
  width: 320px;
  height: 320px;
  background: rgba(240, 147, 251, 0.55);
  bottom: -100px;
  right: -100px;
  animation: blobFloat 22s ease-in-out infinite reverse;
}

@keyframes blobFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(40px, -30px) scale(1.1); }
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  max-width: 820px;
  width: 100%;
  color: #fff;
}

.hero-title {
  font-size: 42px;
  font-weight: 800;
  margin: 0 0 12px;
  letter-spacing: -0.5px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.hero-subtitle {
  font-size: 16px;
  margin: 0 0 32px;
  opacity: 0.92;
  font-weight: 400;
}

/* ===== Search Bar ===== */
.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  max-width: 640px;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.95);
  padding: 8px;
  border-radius: var(--radius-md);
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(8px);
}

.search-bar :deep(.search-input .el-input__wrapper) {
  box-shadow: none !important;
  background: transparent;
  padding: 0 12px;
}

.search-bar :deep(.el-input__inner) {
  font-size: 16px;
  height: 44px;
}

/* ===== Hot tags / History ===== */
.hot-tags-row,
.search-history-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 22px;
}

.search-history-row {
  margin-top: 14px;
}

.hot-label,
.history-label {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.85);
  margin-right: 4px;
  font-weight: 500;
}

.hot-tag-clickable,
.history-tag-clickable {
  cursor: pointer;
  background: rgba(255, 255, 255, 0.18) !important;
  color: #fff !important;
  border: 1px solid rgba(255, 255, 255, 0.25);
  transition: all 0.25s ease;
}

.hot-tag-clickable:hover,
.history-tag-clickable:hover {
  background: rgba(255, 255, 255, 0.32) !important;
  transform: translateY(-2px);
}

.history-clear {
  border: none;
  background: transparent;
  color: rgba(255, 255, 255, 0.82);
  cursor: pointer;
  font-size: 13px;
  padding: 4px 6px;
}

.history-clear:hover {
  color: #fff;
  text-decoration: underline;
}

/* ===== Filters ===== */
.filters-section {
  max-width: 1200px;
  margin: -50px auto 0;
  padding: 0 20px;
  position: relative;
  z-index: 3;
}

.filters-card {
  padding: 20px 24px;
}

.filter-row {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 18px;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  flex: 1 1 160px;
  min-width: 140px;
}

.filter-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
  letter-spacing: 0.2px;
}

.filter-select {
  width: 100%;
}

.filter-select :deep(.el-select__wrapper) {
  border-radius: var(--radius-sm);
}

.filter-actions {
  display: flex;
  align-items: flex-end;
  margin-left: auto;
}

/* ===== Results ===== */
.results-section {
  max-width: 1200px;
  margin: 40px auto 0;
  padding: 0 20px;
}

.results-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-bottom: 24px;
  gap: 12px;
}

.results-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  position: relative;
  padding-left: 14px;
}

.results-title::before {
  content: "";
  position: absolute;
  left: 0;
  top: 6px;
  bottom: 6px;
  width: 4px;
  border-radius: 2px;
  background: var(--gradient-button);
}

.results-count {
  font-size: 14px;
  color: var(--text-muted);
}

.jobs-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.jobs-grid > :deep(.job-card) {
  min-width: 0;
  width: 100%;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

.pagination-wrap :deep(.el-pagination) {
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px;
}

/* ===== Responsive ===== */
@media (max-width: 960px) {
  .hero-title { font-size: 32px; }
  .hero-subtitle { font-size: 15px; }
  .search-bar { flex-direction: column; padding: 12px; }
  .search-bar :deep(.el-input) { width: 100%; }

  .filter-row { gap: 12px; }
  .filter-item { flex: 1 1 calc(50% - 12px); }
  .filter-actions { width: 100%; margin-left: 0; }

  .jobs-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 640px) {
  .search-hero { min-height: 340px; padding: 50px 16px 80px; }
  .hero-title { font-size: 26px; }
  .filter-item { flex: 1 1 100%; }
  .jobs-grid { grid-template-columns: 1fr; }
  .results-header { flex-direction: column; align-items: flex-start; }
  .pagination-wrap { justify-content: flex-start; }
}
</style>
