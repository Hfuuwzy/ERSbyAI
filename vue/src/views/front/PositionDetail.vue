<template>
  <div class="position-page">
    <!-- ====== Gradient Header ====== -->
    <header class="position-header">
      <div class="header-bg animate-gradient"></div>
      <div class="header-blob blob-1"></div>
      <div class="header-blob blob-2"></div>

      <div class="header-content">
        <div class="header-title-row">
          <h1 class="job-title">{{ data.positionData.name }}</h1>
          <div class="job-salary">{{ data.positionData.salary }}</div>
        </div>

        <div class="meta-row">
          <div class="meta-item" v-if="data.positionData.employCity">
            <el-icon><LocationInformation /></el-icon>
            <span>{{ data.positionData.employCity }}</span>
          </div>
          <div class="meta-item" v-if="data.positionData.experience">
            <el-icon><Calendar /></el-icon>
            <span>{{ data.positionData.experience }}</span>
          </div>
          <div class="meta-item" v-if="data.positionData.education">
            <el-icon><School /></el-icon>
            <span>{{ data.positionData.education }}</span>
          </div>
          <div class="meta-item" v-if="data.positionData.type">
            <el-icon><Monitor /></el-icon>
            <span>{{ data.positionData.type }}</span>
          </div>
        </div>

        <div class="header-actions">
          <GradientButton @click="submitInit">投递简历</GradientButton>
          <button class="outline-btn" @click="collect">
            <el-icon><Star /></el-icon>
            <span>收藏岗位</span>
          </button>
        </div>
      </div>
    </header>

    <!-- ====== Two Column Body ====== -->
    <div class="content-wrapper">
      <!-- Main column 70% -->
      <div class="main-content">
        <GlassCard>
          <h2 class="section-title">职位描述</h2>

          <div class="skill-tag-row" v-if="data.positionData.tagList && data.positionData.tagList.length">
            <TagSkill
              v-for="(tag, idx) in data.positionData.tagList"
              :key="idx"
              :label="tag"
            />
          </div>

          <div class="job-description" v-html="data.positionData.content"></div>
        </GlassCard>

        <!-- Similar jobs -->
        <section class="similar-jobs" v-if="data.recommendData && data.recommendData.length">
          <div class="section-header">
            <h2 class="section-title">你可能对这些职位感兴趣</h2>
            <span class="section-subtitle">基于相似度的推荐</span>
          </div>
          <div class="similar-grid">
            <JobCard
              v-for="job in data.recommendData"
              :key="job.id"
              :job="job"
            />
          </div>
        </section>
      </div>

      <!-- Sidebar 30% (sticky) -->
      <aside class="sidebar">
        <GlassCard :liftable="false">
          <div class="company-header">
            <img
              class="company-avatar"
              :src="data.positionData.employAvatar"
              alt=""
            />
            <div class="company-name">{{ data.positionData.employName }}</div>
          </div>

          <div class="company-meta">
            <div class="company-meta-item" v-if="data.positionData.employStage">
              <el-icon><Coordinate /></el-icon>
              <span>{{ data.positionData.employStage }}</span>
            </div>
            <div class="company-meta-item" v-if="data.positionData.employScale">
              <el-icon><User /></el-icon>
              <span>{{ data.positionData.employScale }}</span>
            </div>
            <div class="company-meta-item" v-if="data.positionData.employAddress">
              <el-icon><OfficeBuilding /></el-icon>
              <span>{{ data.positionData.employAddress }}</span>
            </div>
          </div>

          <GradientButton
            variant="secondary"
            @click="navTo('/front/employ?id=' + data.positionData.employId)"
          >
            查看全部职位
          </GradientButton>
        </GlassCard>
      </aside>
    </div>

    <!-- ====== Resume Dialog ====== -->
    <el-dialog title="选择简历" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="resumeId" label="选择简历">
          <el-select v-model="data.resumeId" placeholder="请选择您的简历">
            <el-option
              v-for="item in data.resumeData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="submit">投 递</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue"
import request from "@/utils/request.js"
import { ElMessage } from "element-plus"
import router from "@/router/index.js"
import {
  LocationInformation,
  Calendar,
  School,
  Monitor,
  Coordinate,
  User,
  OfficeBuilding,
  Star
} from "@element-plus/icons-vue"
import GlassCard from "@/components/GlassCard.vue"
import GradientButton from "@/components/GradientButton.vue"
import JobCard from "@/components/JobCard.vue"
import TagSkill from "@/components/TagSkill.vue"

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  positionId: router.currentRoute.value.query.id,
  positionData: {},
  recommendData: [],
  resumeId: null,
  resumeData: [],
  formVisible: false,
  form: {}
})

const loadPosition = () => {
  data.positionId = router.currentRoute.value.query.id
  request.get('/position/selectById/' + data.positionId).then(res => {
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
      data.recommendData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const collect = () => {
  if (data.user.role !== 'USER') {
    ElMessage.warning("您的角色不支持此操作")
    return
  }
  request.post('/collect/add', {
    studentId: data.user.id,
    positionId: data.positionId
  }).then((res) => {
    if (res.code === '200') {
      ElMessage.success("岗位收藏成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const checkExistingSubmit = async () => {
  const res = await request.get('/submit/selectAll', {
    params: {
      userId: data.user.id,
      positionId: data.positionId
    }
  })

  if (res.code !== '200') {
    ElMessage.error(res.msg)
    return null
  }

  return (res.data || [])[0] || null
}

const submitInit = async () => {
  if (data.user.role !== 'USER') {
    ElMessage.warning("您的角色不支持此操作")
    return
  }
  const existing = await checkExistingSubmit()
  if (existing) {
    ElMessage.warning(`当前岗位已投递，状态：${existing.status}`)
    return
  }
  request.get('resume/selectAll', {
    params: {
      userId: data.user.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.resumeData = res.data
      data.formVisible = true
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const submit = () => {
  let submitData = {
    employId: data.positionData.employId,
    positionId: router.currentRoute.value.query.id,
    userId: data.user.id,
    resumeId: data.resumeId
  }
  request.post('/submit/add', submitData).then((res) => {
    if (res.code === '200') {
      ElMessage.success('岗位投递成功,请在我的投递板块查看投递状态')
    } else {
      ElMessage.error(res.msg)
    }
    data.formVisible = false
  })
}

const navTo = (url) => {
  location.href = url
}

onMounted(() => {
  loadPosition()
  loadRecommend()
})
</script>

<style scoped>
.position-page {
  background: var(--bg-primary);
  min-height: 100vh;
  padding-bottom: 80px;
}

/* ===== Gradient Header ===== */
.position-header {
  position: relative;
  overflow: hidden;
  padding: 80px 24px 64px;
}

.header-bg {
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

.header-blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.4;
  z-index: 1;
  pointer-events: none;
}

.header-blob.blob-1 {
  width: 360px;
  height: 360px;
  background: rgba(255, 255, 255, 0.5);
  top: -120px;
  left: -80px;
  animation: blobFloat 18s ease-in-out infinite;
}

.header-blob.blob-2 {
  width: 280px;
  height: 280px;
  background: rgba(240, 147, 251, 0.55);
  bottom: -100px;
  right: -60px;
  animation: blobFloat 22s ease-in-out infinite reverse;
}

@keyframes blobFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(40px, -30px) scale(1.1); }
}

.header-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  color: #fff;
}

.header-title-row {
  display: flex;
  align-items: baseline;
  gap: 24px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.job-title {
  font-size: 40px;
  font-weight: 800;
  margin: 0;
  letter-spacing: -0.5px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.18);
  line-height: 1.15;
}

.job-salary {
  font-size: 28px;
  font-weight: 700;
  color: #fff;
  background: rgba(255, 255, 255, 0.18);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  padding: 4px 16px;
  border-radius: var(--radius-sm);
  border: 1px solid rgba(255, 255, 255, 0.35);
}

.meta-row {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  margin-bottom: 32px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.95);
  font-size: 15px;
  font-weight: 500;
}

.meta-item .el-icon {
  font-size: 18px;
}

.header-actions {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
}

.outline-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: var(--radius-sm);
  color: #fff;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  transition: all 0.3s ease;
}

.outline-btn:hover {
  background: rgba(255, 255, 255, 0.22);
  border-color: rgba(255, 255, 255, 0.8);
  transform: translateY(-2px);
}

/* ===== Content Wrapper (two columns) ===== */
.content-wrapper {
  position: relative;
  z-index: 3;
  max-width: 1200px;
  margin: -32px auto 0;
  padding: 0 24px;
  display: grid;
  grid-template-columns: minmax(0, 7fr) minmax(0, 3fr);
  gap: 24px;
  align-items: start;
}

/* ===== Main Content ===== */
.main-content {
  display: flex;
  flex-direction: column;
  gap: 32px;
  min-width: 0;
}

.section-title {
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 20px;
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

.skill-tag-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
}

.job-description {
  font-size: 15px;
  color: var(--text-secondary);
  line-height: 1.8;
}

.job-description :deep(p) {
  margin: 0 0 12px;
}

.job-description :deep(ul),
.job-description :deep(ol) {
  padding-left: 24px;
  margin: 8px 0 16px;
}

.job-description :deep(li) {
  margin: 4px 0;
}

.job-description :deep(strong),
.job-description :deep(b) {
  color: var(--text-primary);
}

/* ===== Similar Jobs ===== */
.similar-jobs {
  margin-top: 8px;
}

.section-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-bottom: 20px;
  gap: 12px;
}

.section-subtitle {
  font-size: 13px;
  color: var(--text-muted);
}

.similar-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.similar-grid > :deep(.job-card) {
  min-width: 0;
  width: 100%;
}

/* ===== Sidebar ===== */
.sidebar {
  position: sticky;
  top: 24px;
  min-width: 0;
}

.company-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 20px;
  padding-bottom: 18px;
  border-bottom: 1px solid var(--border-light);
}

.company-avatar {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-sm);
  object-fit: cover;
  border: 1px solid var(--border-light);
}

.company-name {
  font-size: 17px;
  font-weight: 700;
  color: var(--text-primary);
  flex: 1;
  min-width: 0;
  word-break: break-word;
}

.company-meta {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 22px;
}

.company-meta-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: var(--text-secondary);
}

.company-meta-item .el-icon {
  font-size: 16px;
  color: var(--color-primary);
  flex-shrink: 0;
}

.company-meta-item span {
  flex: 1;
  min-width: 0;
  word-break: break-word;
}

.sidebar :deep(.gradient-button) {
  width: 100%;
}

/* ===== Responsive ===== */
@media (max-width: 960px) {
  .position-header {
    padding: 60px 20px 48px;
  }
  .job-title {
    font-size: 30px;
  }
  .job-salary {
    font-size: 22px;
  }
  .content-wrapper {
    grid-template-columns: 1fr;
  }
  .sidebar {
    position: static;
  }
}

@media (max-width: 640px) {
  .position-header {
    padding: 48px 16px 40px;
  }
  .job-title {
    font-size: 24px;
  }
  .job-salary {
    font-size: 18px;
  }
  .meta-row {
    gap: 14px;
  }
  .meta-item {
    font-size: 13px;
  }
  .similar-grid {
    grid-template-columns: 1fr;
  }
  .content-wrapper {
    padding: 0 16px;
  }
}
</style>
