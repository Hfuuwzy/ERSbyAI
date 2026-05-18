<template>
  <div class="search-page">
    <!-- Search Header -->
    <div class="search-header">
      <h1 class="page-title">搜索职位</h1>
      <div class="search-box">
        <el-input 
          v-model="data.name" 
          placeholder="输入职位名称、公司名称" 
          size="large" 
          clearable 
          @clear="reset"
          class="search-input"
        />
        <GradientButton @click="loadPosition">搜索</GradientButton>
      </div>
    </div>
    
    <!-- Search Results -->
    <div class="search-results">
      <div v-if="data.positionData.length" class="results-grid">
        <JobCard v-for="job in data.positionData" :key="job.id" :job="job" />
      </div>
      <EmptyState 
        v-else 
        icon="🔍" 
        title="没有找到相关职位" 
        description="换个关键词试试看"
        action-text="查看全部职位"
        @action="reset"
      />
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue"
import { useRoute } from "vue-router"
import request from "@/utils/request.js"
import { ElMessage } from "element-plus"
import JobCard from "@/components/JobCard.vue"
import GradientButton from "@/components/GradientButton.vue"
import EmptyState from "@/components/EmptyState.vue"

const route = useRoute()

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  positionData: [],
  name: route.query.name || ''
})

const loadPosition = () => {
  request.get('/position/selectAll', {
    params: {
      name: data.name,
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

const reset = () => {
  data.name = ''
  loadPosition()
}

onMounted(() => {
  loadPosition()
})
</script>

<style scoped>
.search-page {
  min-height: 100vh;
  background: var(--bg-primary);
  padding: 60px 24px;
}

.search-header {
  text-align: center;
  max-width: 800px;
  margin: 0 auto 60px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 32px;
}

.search-box {
  display: flex;
  gap: 16px;
  justify-content: center;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  flex: 1;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: var(--radius-sm);
  box-shadow: var(--shadow-sm);
}

.search-results {
  max-width: 1200px;
  margin: 0 auto;
}

.results-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

@media (max-width: 1024px) {
  .results-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .results-grid {
    grid-template-columns: 1fr;
  }
  
  .search-box {
    flex-direction: column;
  }
  
  .page-title {
    font-size: 24px;
  }
}
</style>
