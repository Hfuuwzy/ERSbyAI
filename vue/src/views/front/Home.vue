<template>
  <div class="home-page">
    <!-- Hero -->
    <section class="hero-section">
      <div class="hero-bg animate-gradient"></div>
      <div class="hero-blob blob-1"></div>
      <div class="hero-blob blob-2"></div>
      <div class="hero-content">
        <h1 class="hero-title">发现你的下一份理想工作</h1>
        <p class="hero-subtitle">AI 智能匹配 + 海量优质岗位</p>
        <div class="hero-search">
          <el-input
            v-model="data.name"
            placeholder="搜索职位、公司"
            size="large"
            class="hero-search-input"
            @keyup.enter="search"
          />
          <GradientButton @click="search">搜索</GradientButton>
        </div>
      </div>
    </section>

    <!-- Bento Ads -->
    <section class="bento-section">
      <div class="bento-grid">
        <div
          v-if="data.leftAd.img"
          class="bento-item large"
          @click="navTo('/front/positionDetail?id=' + data.leftAd.positionId)"
        >
          <img :src="data.leftAd.img" alt="left ad" />
        </div>
        <div
          v-if="data.centerAd.img"
          class="bento-item xlarge"
          @click="navTo('/front/positionDetail?id=' + data.centerAd.positionId)"
        >
          <img :src="data.centerAd.img" alt="center ad" />
        </div>
        <div
          v-if="data.rightAd.img"
          class="bento-item large"
          @click="navTo('/front/positionDetail?id=' + data.rightAd.positionId)"
        >
          <img :src="data.rightAd.img" alt="right ad" />
        </div>
        <div
          v-for="(ad, idx) in bottomAds"
          :key="idx"
          class="bento-item small"
          @click="navTo('/front/positionDetail?id=' + ad.positionId)"
        >
          <img :src="ad.img" :alt="'ad ' + idx" />
        </div>
      </div>
    </section>

    <!-- Featured Jobs -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">精选岗位</h2>
        <span class="section-subtitle">AI 为你推荐的优质机会</span>
      </div>
      <div class="horizontal-scroll">
        <JobCard
          v-for="job in data.recommendData"
          :key="job.id"
          :job="job"
        />
        <div v-if="!data.recommendData || data.recommendData.length === 0" class="empty-hint">
          暂无推荐岗位
        </div>
      </div>
    </section>

    <!-- Hot Jobs -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">热招岗位</h2>
        <span class="section-subtitle">来自各行业的热门职位</span>
      </div>

      <div class="tabs" ref="tabsRef">
        <button
          v-for="industry in data.industryData"
          :key="industry.id"
          :ref="el => setTabRef(el, industry.id)"
          :class="['tab-btn', { active: data.activeName === industry.id }]"
          @click="handleClick(industry.id)"
        >
          {{ industry.name }}
        </button>
        <div
          class="tab-indicator"
          :style="{ left: indicatorStyle.left + 'px', width: indicatorStyle.width + 'px' }"
        ></div>
      </div>

      <div class="jobs-grid">
        <JobCard
          v-for="job in data.positionData"
          :key="job.id"
          :job="job"
        />
        <div v-if="!data.positionData || data.positionData.length === 0" class="empty-hint">
          该行业暂无在招岗位
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import router from '@/router/index.js'
import GradientButton from '@/components/GradientButton.vue'
import JobCard from '@/components/JobCard.vue'

const data = reactive({
  name: null,
  advertiseData: [],
  centerAd: {},
  leftAd: {},
  rightAd: {},
  centerDownAd: {},
  leftDownAd: {},
  rightDownAd: {},
  industryData: [],
  activeName: null,
  positionData: [],
  recommendData: []
})

const tabRefs = ref({})
const indicatorStyle = reactive({ left: 0, width: 0 })

const setTabRef = (el, id) => {
  if (el) tabRefs.value[id] = el
}

const updateIndicator = () => {
  const el = tabRefs.value[data.activeName]
  if (!el) return
  indicatorStyle.left = el.offsetLeft
  indicatorStyle.width = el.offsetWidth
}

const bottomAds = computed(() =>
  [data.leftDownAd, data.centerDownAd, data.rightDownAd].filter(ad => ad && ad.img)
)

const loadAdvertise = () => {
  request.get('/advertise/selectAll').then(res => {
    if (res.code === '200') {
      data.advertiseData = res.data
      const pick = (loc) => {
        const arr = res.data.filter(v => v.location === loc)
        return arr && arr.length > 0 ? arr[0] : {}
      }
      data.centerAd = pick('中心大图')
      data.leftAd = pick('左侧大图')
      data.rightAd = pick('右侧大图')
      data.centerDownAd = pick('中心小图')
      data.leftDownAd = pick('左侧小图')
      data.rightDownAd = pick('右侧小图')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadIndustry = () => {
  request.get('/industry/selectAll').then(res => {
    if (res.code === '200') {
      data.industryData = res.data
      if (data.industryData.length > 0) {
        data.activeName = data.industryData[0].id
        handleClick(data.activeName)
        nextTick(updateIndicator)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleClick = (industryId) => {
  data.activeName = industryId
  nextTick(updateIndicator)
  request.get('/position/selectAll', {
    params: {
      industryId: industryId,
      status: '审核通过'
    }
  }).then(res => {
    if (res.code === '200') {
      data.positionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadRecommend = () => {
  request.get('position/recommend').then(res => {
    if (res.code === '200') {
      data.recommendData = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const search = () => {
  if (data.name?.trim()) {
    router.push({
      path: '/front/search',
      query: { name: data.name.trim() }
    })
  }
}

const navTo = (url) => {
  location.href = url
}

loadAdvertise()
loadIndustry()
onMounted(() => {
  loadRecommend()
  window.addEventListener('resize', updateIndicator)
})
</script>

<style scoped>
.home-page {
  background: var(--bg-primary);
  min-height: 100vh;
  padding-bottom: 60px;
}

/* ===== Hero ===== */
.hero-section {
  position: relative;
  min-height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 80px 20px;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background: var(--gradient-hero);
  background-size: 200% 200%;
  z-index: 0;
}

.animate-gradient {
  animation: gradientFlow 15s ease infinite;
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
  width: 400px;
  height: 400px;
  background: rgba(255, 255, 255, 0.5);
  top: -100px;
  left: -100px;
  animation: blobFloat 18s ease-in-out infinite;
}

.blob-2 {
  width: 300px;
  height: 300px;
  background: rgba(240, 147, 251, 0.6);
  bottom: -80px;
  right: -80px;
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
  max-width: 800px;
  width: 100%;
  color: #fff;
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  margin: 0 0 16px;
  letter-spacing: -0.5px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.hero-subtitle {
  font-size: 18px;
  margin: 0 0 40px;
  opacity: 0.9;
  font-weight: 400;
}

.hero-search {
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

.hero-search :deep(.hero-search-input .el-input__wrapper) {
  box-shadow: none !important;
  background: transparent;
  padding: 0 12px;
}

.hero-search :deep(.el-input__inner) {
  font-size: 16px;
  height: 44px;
}

/* ===== Bento Ads ===== */
.bento-section {
  max-width: 1200px;
  margin: -50px auto 0;
  padding: 0 20px;
  position: relative;
  z-index: 3;
}

.bento-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: 220px 140px;
  gap: 16px;
}

.bento-item {
  position: relative;
  cursor: pointer;
  border-radius: var(--radius-md);
  overflow: hidden;
  background: var(--bg-white);
  box-shadow: var(--shadow-sm);
  transition: transform 0.35s ease, box-shadow 0.35s ease;
}

.bento-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.5s ease;
}

.bento-item:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.bento-item:hover img {
  transform: scale(1.05);
}

.bento-item.large {
  grid-column: span 1;
  grid-row: span 1;
}

.bento-item.xlarge {
  grid-column: span 2;
  grid-row: span 1;
}

.bento-item.small {
  grid-column: span 1;
  grid-row: span 1;
}

/* ensure bottom row spans 4 columns evenly when 3 small items render */
.bento-item.small:nth-child(4) { grid-column: 1 / span 1; }
.bento-item.small:nth-child(5) { grid-column: 2 / span 2; }
.bento-item.small:nth-child(6) { grid-column: 4 / span 1; }

/* ===== Sections ===== */
.section {
  max-width: 1200px;
  margin: 60px auto 0;
  padding: 0 20px;
}

.section-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-bottom: 24px;
  gap: 12px;
}

.section-title {
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  position: relative;
  padding-left: 14px;
}

.section-title::before {
  content: "";
  position: absolute;
  left: 0;
  top: 6px;
  bottom: 6px;
  width: 4px;
  border-radius: 2px;
  background: var(--gradient-button);
}

.section-subtitle {
  font-size: 14px;
  color: var(--text-muted);
}

/* horizontal scroll */
.horizontal-scroll {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  padding: 8px 4px 16px;
  scroll-snap-type: x proximity;
  scrollbar-width: thin;
  scrollbar-color: rgba(102, 126, 234, 0.3) transparent;
}

.horizontal-scroll::-webkit-scrollbar {
  height: 8px;
}

.horizontal-scroll::-webkit-scrollbar-thumb {
  background: rgba(102, 126, 234, 0.3);
  border-radius: 4px;
}

.horizontal-scroll::-webkit-scrollbar-thumb:hover {
  background: rgba(102, 126, 234, 0.5);
}

.horizontal-scroll > :deep(.job-card) {
  scroll-snap-align: start;
  flex: 0 0 auto;
}

/* ===== Tabs ===== */
.tabs {
  position: relative;
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  padding: 6px;
  margin-bottom: 24px;
  background: var(--bg-white);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  width: fit-content;
}

.tab-btn {
  position: relative;
  z-index: 2;
  padding: 10px 20px;
  border: none;
  background: transparent;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: color 0.3s ease;
}

.tab-btn:hover {
  color: var(--color-primary);
}

.tab-btn.active {
  color: #fff;
}

.tab-indicator {
  position: absolute;
  top: 6px;
  bottom: 6px;
  background: var(--gradient-button);
  border-radius: var(--radius-sm);
  transition: left 0.35s cubic-bezier(0.4, 0, 0.2, 1), width 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1;
  pointer-events: none;
}

/* ===== Jobs grid ===== */
.jobs-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.jobs-grid > :deep(.job-card) {
  min-width: 0;
  width: 100%;
}

.empty-hint {
  grid-column: 1 / -1;
  text-align: center;
  padding: 40px 0;
  color: var(--text-muted);
  font-size: 14px;
}

/* ===== Responsive ===== */
@media (max-width: 960px) {
  .hero-title { font-size: 36px; }
  .hero-subtitle { font-size: 16px; }
  .hero-search { flex-direction: column; padding: 12px; }
  .hero-search :deep(.el-input) { width: 100%; }

  .bento-grid {
    grid-template-columns: repeat(2, 1fr);
    grid-template-rows: auto;
  }
  .bento-item.large,
  .bento-item.xlarge,
  .bento-item.small,
  .bento-item.small:nth-child(4),
  .bento-item.small:nth-child(5),
  .bento-item.small:nth-child(6) {
    grid-column: span 1;
    height: 160px;
  }
  .bento-item.xlarge {
    grid-column: span 2;
    height: 200px;
  }

  .jobs-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .hero-section { min-height: 420px; padding: 60px 16px; }
  .hero-title { font-size: 28px; }
  .bento-grid { grid-template-columns: 1fr; }
  .bento-item,
  .bento-item.large,
  .bento-item.xlarge,
  .bento-item.small,
  .bento-item.small:nth-child(4),
  .bento-item.small:nth-child(5),
  .bento-item.small:nth-child(6) {
    grid-column: span 1;
    height: 160px;
  }
  .jobs-grid { grid-template-columns: 1fr; }
  .section-header { flex-direction: column; align-items: flex-start; }
}
</style>
