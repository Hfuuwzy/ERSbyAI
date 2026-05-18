<template>
  <div class="employ-page">
    <!-- Hero Header -->
    <header class="employ-header">
      <div class="header-bg"></div>
      <div class="header-content">
        <div class="employ-info">
          <img :src="data.employData.avatar" class="employ-avatar" />
          <div class="employ-meta">
            <h1 class="employ-name">{{ data.employData.name }}</h1>
            <div class="employ-tags">
              <span class="meta-item">
                <el-icon><Coordinate /></el-icon>
                {{ data.employData.stage }}
              </span>
              <span class="meta-item">
                <el-icon><User /></el-icon>
                {{ data.employData.scale }}
              </span>
              <span class="meta-item">
                <el-icon><CollectionTag /></el-icon>
                在招职位 {{ data.positionData.length }}
              </span>
            </div>
            <div class="employ-address">
              <el-icon><LocationInformation /></el-icon>
              <span>{{ data.employData.address }}</span>
            </div>
          </div>
        </div>
      </div>
    </header>
    
    <!-- Search Section -->
    <div class="search-section">
      <div class="search-box">
        <el-input 
          v-model="data.name" 
          placeholder="搜索该企业正在招聘的岗位" 
          size="large" 
          clearable 
          @clear="reset"
          class="search-input"
        />
        <GradientButton @click="loadPosition">搜索</GradientButton>
      </div>
    </div>
    
    <!-- Jobs Grid -->
    <div class="jobs-section">
      <div v-if="data.positionData.length" class="jobs-grid">
        <JobCard v-for="job in data.positionData" :key="job.id" :job="job" />
      </div>
      <EmptyState 
        v-else 
        icon="🔍" 
        title="暂无在招职位" 
        description="该企业暂时没有开放职位"
      />
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue"
import { useRoute } from "vue-router"
import request from "@/utils/request.js"
import { ElMessage } from "element-plus"
import { Coordinate, User, CollectionTag, LocationInformation } from "@element-plus/icons-vue"
import JobCard from "@/components/JobCard.vue"
import GradientButton from "@/components/GradientButton.vue"
import EmptyState from "@/components/EmptyState.vue"

const route = useRoute()

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  employId: route.query.id,
  employData: {},
  positionData: [],
  name: ''
})

const loadEmploy = () => {
  request.get('/employ/selectById/' + data.employId).then(res => {
    if (res.code === '200') {
      data.employData = res.data
    }
  })
}

const loadPosition = () => {
  request.get('/position/selectAll', {
    params: {
      employId: data.employId,
      name: data.name,
      status: "审核通过"
    }
  }).then((res) => {
    if (res.code === '200') {
      data.positionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () => {
  data.name = ''
  loadPosition()
}

onMounted(() => {
  loadEmploy()
  loadPosition()
})
</script>

<style scoped>
.employ-page {
  min-height: 100vh;
  background: var(--bg-primary);
}

.employ-header {
  position: relative;
  padding: 60px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.header-bg {
  position: absolute;
  inset: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="50" cy="50" r="40" fill="rgba(255,255,255,0.1)"/></svg>') center/200px repeat;
  opacity: 0.5;
}

.header-content {
  position: relative;
  max-width: 1200px;
  margin: 0 auto;
}

.employ-info {
  display: flex;
  gap: 24px;
  align-items: center;
}

.employ-avatar {
  width: 80px;
  height: 80px;
  border-radius: var(--radius-md);
  border: 3px solid rgba(255,255,255,0.3);
  box-shadow: var(--shadow-lg);
}

.employ-meta {
  flex: 1;
}

.employ-name {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 12px;
}

.employ-tags {
  display: flex;
  gap: 20px;
  margin-bottom: 8px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  opacity: 0.9;
}

.employ-address {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  opacity: 0.8;
}

.search-section {
  max-width: 800px;
  margin: -30px auto 40px;
  padding: 0 24px;
}

.search-box {
  display: flex;
  gap: 12px;
  background: white;
  padding: 20px;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
}

.search-input {
  flex: 1;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: var(--radius-sm);
}

.jobs-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px 60px;
}

.jobs-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

@media (max-width: 1024px) {
  .jobs-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .employ-header {
    padding: 40px 20px;
  }
  
  .employ-info {
    flex-direction: column;
    text-align: center;
  }
  
  .employ-tags {
    flex-direction: column;
    gap: 8px;
  }
  
  .search-box {
    flex-direction: column;
  }
  
  .jobs-grid {
    grid-template-columns: 1fr;
  }
}
</style>
