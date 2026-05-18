<template>
  <div class="collect-page">
    <header class="page-header">
      <h1 class="page-title">我的收藏</h1>
      <p class="page-subtitle" v-if="data.collectData.length">
        共 <span class="count">{{ data.collectData.length }}</span> 个心仪岗位
      </p>
    </header>

    <section v-if="data.collectData.length" class="collect-grid">
      <div
        v-for="it in data.collectData"
        :key="it.id"
        class="collect-item"
      >
        <JobCard :job="toJob(it)" />
        <button
          class="unfavorite-btn"
          title="取消收藏"
          @click.stop="unfavorite(it.id)"
        >
          <el-icon><Delete /></el-icon>
        </button>
      </div>
    </section>

    <EmptyState
      v-else
      icon="💔"
      title="还没有收藏任何岗位"
      description="去发现一些心动的工作机会吧"
      action-text="去逛逛"
      @action="goExplore"
    />
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import request from '@/utils/request.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import JobCard from '@/components/JobCard.vue'
import EmptyState from '@/components/EmptyState.vue'

const data = reactive({
  collectData: []
})

const loadCollect = () => {
  request.get('/collect/selectAll').then((res) => {
    if (res.code === '200') {
      data.collectData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// Map a collect record to the shape JobCard expects
const toJob = (it) => ({
  id: it.positionId,
  name: it.positionName,
  salary: it.positionSalary,
  tagList: it.tagList,
  employAvatar: it.employAvatar,
  employName: it.employName,
  employStage: it.employStage
})

const unfavorite = (collectId) => {
  ElMessageBox.confirm('确定取消收藏该岗位吗？', '提示', { type: 'warning' })
    .then(() => {
      request.delete('/collect/delete/' + collectId).then((res) => {
        if (res.code === '200') {
          ElMessage.success('取消收藏成功')
          loadCollect()
        } else {
          ElMessage.error(res.msg)
        }
      })
    })
    .catch(() => {})
}

const goExplore = () => {
  location.href = '/front/search'
}

loadCollect()
</script>

<style scoped>
.collect-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 48px 20px 80px;
  min-height: calc(100vh - 200px);
}

/* ===== Page header ===== */
.page-header {
  margin-bottom: 36px;
  text-align: left;
}

.page-title {
  font-size: 32px;
  font-weight: 800;
  color: var(--text-primary);
  margin: 0;
  position: relative;
  padding-left: 18px;
  letter-spacing: -0.5px;
}

.page-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 8px;
  bottom: 8px;
  width: 5px;
  border-radius: 3px;
  background: var(--gradient-button);
}

.page-subtitle {
  margin: 10px 0 0 18px;
  font-size: 14px;
  color: var(--text-muted);
}

.page-subtitle .count {
  font-weight: 700;
  background: var(--gradient-button);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* ===== Grid ===== */
.collect-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.collect-item {
  position: relative;
  min-width: 0;
}

.collect-item > :deep(.job-card) {
  width: 100%;
  min-width: 0;
}

/* ===== Unfavorite hover action ===== */
.unfavorite-btn {
  position: absolute;
  top: 14px;
  right: 14px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-white);
  color: var(--color-error);
  border: 1px solid var(--border-light);
  border-radius: 50%;
  cursor: pointer;
  opacity: 0;
  transform: translateY(-4px);
  box-shadow: var(--shadow-sm);
  transition: opacity 0.25s ease, transform 0.25s ease, background 0.25s ease, color 0.25s ease;
  z-index: 2;
}

.collect-item:hover .unfavorite-btn,
.unfavorite-btn:focus-visible {
  opacity: 1;
  transform: translateY(0);
}

.unfavorite-btn:hover {
  background: var(--color-error);
  color: #fff;
  border-color: var(--color-error);
}

.unfavorite-btn :deep(.el-icon) {
  font-size: 16px;
}

/* ===== Responsive ===== */
@media (max-width: 960px) {
  .collect-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .page-title {
    font-size: 26px;
  }
}

@media (max-width: 640px) {
  .collect-page {
    padding: 32px 16px 60px;
  }

  .collect-grid {
    grid-template-columns: 1fr;
  }

  /* Always-visible action on touch devices */
  .unfavorite-btn {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
