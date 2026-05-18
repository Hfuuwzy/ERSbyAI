<template>
  <div class="resume-page">
    <div class="resume-bg"></div>
    <div class="resume-container">
      <!-- Page header -->
      <header class="page-header">
        <div class="title-block">
          <h1 class="page-title">我的简历</h1>
          <p class="page-subtitle">共 {{ data.resumeData.length }} 份简历</p>
        </div>
        <GradientButton @click="navTo('/front/resumeEdit')">
          <el-icon style="vertical-align: -3px; margin-right: 6px;"><Plus /></el-icon>
          新建简历
        </GradientButton>
      </header>

      <!-- Empty state -->
      <EmptyState
        v-if="data.resumeData.length === 0"
        icon="📄"
        title="还没有简历"
        description="创建你的第一份简历，开启求职之旅"
        actionText="立即创建"
        @action="navTo('/front/resumeEdit')"
      />

      <!-- Resume grid -->
      <div v-else class="resume-grid">
        <!-- Create new resume card -->
        <GlassCard class="create-card" @click="navTo('/front/resumeEdit')">
          <div class="create-icon-wrap">
            <el-icon class="create-icon"><Plus /></el-icon>
          </div>
          <div class="create-text">新建简历</div>
          <div class="create-hint">从空白开始打造一份新简历</div>
        </GlassCard>

        <!-- Resume cards -->
        <GlassCard
          v-for="(item, index) in data.resumeData"
          :key="index"
          class="resume-card"
        >
          <div class="card-head">
            <div class="resume-avatar">
              <el-icon><Document /></el-icon>
            </div>
            <div class="resume-meta">
              <h3 class="resume-name" :title="item.name">{{ item.name || '未命名简历' }}</h3>
              <div class="resume-time">
                <el-icon><Clock /></el-icon>
                <span>最近更新: 刚刚</span>
              </div>
            </div>
          </div>

          <div class="card-body">
            <div class="info-row">
              <span class="info-label">期望薪资</span>
              <span class="info-value salary">{{ item.salary || '面议' }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">工作经验</span>
              <span class="info-value">{{ item.experience || '不限' }}</span>
            </div>
          </div>

          <div class="card-actions">
            <button class="action-btn edit" @click="navTo('/front/resumeEdit?id=' + item.id)">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </button>
            <button class="action-btn delete" @click="delResume(item.id)">
              <el-icon><Delete /></el-icon>
              <span>删除</span>
            </button>
            <button class="action-btn submit" @click="goSubmit">
              <el-icon><Promotion /></el-icon>
              <span>投递</span>
            </button>
          </div>
        </GlassCard>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue"
import request from "@/utils/request.js"
import { ElMessage, ElMessageBox } from "element-plus"
import { Delete, Plus, Edit, Promotion, Document, Clock } from "@element-plus/icons-vue"
import GlassCard from "@/components/GlassCard.vue"
import GradientButton from "@/components/GradientButton.vue"
import EmptyState from "@/components/EmptyState.vue"

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  resumeData: [],
})

const navTo = (url) => {
  location.href = url
}

const goSubmit = () => {
  location.href = '/front/search'
}

const delResume = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/resume/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        loadResume()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const loadResume = () => {
  request.get('/resume/selectAll', {
    params: { userId: data.user.id }
  }).then((res) => {
    if (res.code === '200') {
      data.resumeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadResume()
</script>

<style scoped>
.resume-page {
  position: relative;
  min-height: calc(100vh - 80px);
  padding: 48px 24px 64px;
  overflow: hidden;
}

.resume-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 0% 0%, rgba(102, 126, 234, 0.18), transparent 45%),
    radial-gradient(circle at 100% 0%, rgba(240, 147, 251, 0.15), transparent 45%),
    var(--bg-primary);
  z-index: 0;
}

.resume-container {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
}

/* Header */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 32px;
}

.title-block {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.page-title {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
  background: var(--gradient-hero);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 0.5px;
}

.page-subtitle {
  margin: 0;
  font-size: 14px;
  color: var(--text-secondary);
}

/* Grid */
.resume-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

/* Create card */
.create-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  min-height: 260px;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.55);
  border: 2px dashed rgba(102, 126, 234, 0.4);
  text-align: center;
}

.create-card:hover {
  border-color: var(--color-primary);
  background: rgba(255, 255, 255, 0.75);
}

.create-icon-wrap {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-button);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.35);
}

.create-icon {
  font-size: 32px;
  color: #fff;
}

.create-text {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.create-hint {
  font-size: 13px;
  color: var(--text-secondary);
}

/* Resume card */
.resume-card {
  display: flex;
  flex-direction: column;
  gap: 20px;
  min-height: 260px;
}

.card-head {
  display: flex;
  align-items: center;
  gap: 14px;
}

.resume-avatar {
  flex-shrink: 0;
  width: 48px;
  height: 48px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-button);
  color: #fff;
  font-size: 22px;
  box-shadow: 0 4px 14px rgba(102, 126, 234, 0.3);
}

.resume-meta {
  flex: 1;
  min-width: 0;
}

.resume-name {
  margin: 0 0 4px;
  font-size: 17px;
  font-weight: 700;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.resume-time {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: var(--text-muted);
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 14px 0;
  border-top: 1px solid var(--border-light);
  border-bottom: 1px solid var(--border-light);
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 13px;
}

.info-label {
  color: var(--text-secondary);
}

.info-value {
  color: var(--text-primary);
  font-weight: 600;
}

.info-value.salary {
  color: var(--color-error);
}

.card-actions {
  display: flex;
  gap: 8px;
  margin-top: auto;
}

.action-btn {
  flex: 1;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  padding: 8px 0;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-sm);
  background: rgba(255, 255, 255, 0.6);
  color: var(--text-secondary);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.action-btn:hover {
  transform: translateY(-1px);
}

.action-btn.edit:hover {
  background: rgba(102, 126, 234, 0.1);
  color: var(--color-primary);
  border-color: var(--color-primary);
}

.action-btn.delete:hover {
  background: rgba(239, 68, 68, 0.08);
  color: var(--color-error);
  border-color: var(--color-error);
}

.action-btn.submit {
  background: var(--gradient-button);
  color: #fff;
  border-color: transparent;
  box-shadow: 0 3px 10px rgba(102, 126, 234, 0.25);
}

.action-btn.submit:hover {
  box-shadow: 0 6px 18px rgba(102, 126, 234, 0.4);
}

/* Responsive */
@media (max-width: 992px) {
  .resume-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .resume-grid {
    grid-template-columns: 1fr;
  }
  .page-title {
    font-size: 26px;
  }
}
</style>
