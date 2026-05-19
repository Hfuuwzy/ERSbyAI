<template>
  <div class="submit-page">
    <div class="page-header">
      <h1 class="page-title">我的投递</h1>
      <p class="page-subtitle">共投递 {{ data.total }} 份简历</p>
    </div>
    
    <!-- Tab Navigation -->
    <div class="tab-nav">
      <button 
        v-for="tab in tabItems" 
        :key="tab.value"
        :class="['tab-btn', { active: activeTab === tab.value }]"
        @click="activeTab = tab.value"
      >
        {{ tab.label }}
        <span v-if="tab.count" class="tab-count">{{ tab.count }}</span>
      </button>
    </div>
    
    <!-- Submissions List -->
    <div v-if="filteredData.length" class="submissions-list">
      <GlassCard v-for="item in filteredData" :key="item.id" class="submission-item">
        <div class="submission-header">
          <div class="position-info">
            <h3 class="position-name">
              <a :href="'/front/positionDetail?id=' + item.positionId" class="position-link">{{ item.positionName }}</a>
            </h3>
            <span class="company-name">{{ item.employName }}</span>
          </div>
          <TagSkill :label="getStatusLabel(item.status)" :variant="getStatusVariant(item.status)" />
        </div>
        
        <div class="submission-meta">
          <span class="salary">{{ item.salary || '薪资面议' }}</span>
          <span class="time">投递于 {{ item.time }}</span>
          <span v-if="hasAiScore(item)" :class="['ai-score', getScoreClass(item.aiScore)]">
            AI评分: {{ item.aiScore }}分 · {{ getScoreLevel(item.aiScore) }}
          </span>
        </div>

        <div v-if="hasAiScore(item) || item.aiReview" class="ai-panel">
          <div v-if="hasAiScore(item)" class="ai-score-block">
            <el-progress
              type="circle"
              :percentage="normalizeScore(item.aiScore)"
              :width="72"
              :stroke-width="8"
              :color="getScoreColor(item.aiScore)"
            />
            <div class="ai-score-text">
              <div class="ai-score-title">AI匹配评分</div>
              <el-tag :type="getScoreTagType(item.aiScore)" effect="light">
                {{ getScoreLevel(item.aiScore) }}
              </el-tag>
            </div>
          </div>

          <div v-if="item.aiReview" class="ai-review-card">
            <div class="ai-review-title">AI评语</div>
            <div class="ai-review-content">{{ item.aiReview }}</div>
          </div>

          <div v-if="hasAiScore(item) || item.aiReview" class="ai-suggestion-grid">
            <div class="suggestion-card advantage">
              <div class="suggestion-label">优势</div>
              <div>{{ getAiAdvice(item).advantage }}</div>
            </div>
            <div class="suggestion-card weakness">
              <div class="suggestion-label">不足</div>
              <div>{{ getAiAdvice(item).weakness }}</div>
            </div>
            <div class="suggestion-card advice">
              <div class="suggestion-label">建议</div>
              <div>{{ getAiAdvice(item).advice }}</div>
            </div>
          </div>
        </div>

        <div class="submission-steps">
          <el-steps :active="getActiveStep(item.status)" align-center finish-status="success">
            <el-step title="已投递" description="简历已提交"></el-step>
            <el-step title="面试中" description="企业正在推进面试"></el-step>
            <el-step
              :title="getFinalStepTitle(item.status)"
              :description="getFinalStepDescription(item.status)"
              :status="getFinalStepStatus(item.status)"
            ></el-step>
          </el-steps>
        </div>
        
        <div class="submission-actions">
          <a :href="'/resumeView?id=' + item.resumeId" target="_blank" class="action-link">查看简历</a>
          <el-button 
            v-if="normalizeStatus(item.status) === '已投递'" 
            type="danger" 
            text 
            size="small"
            @click="cancel(item.id)"
          >
            撤回投递
          </el-button>
        </div>
      </GlassCard>
    </div>
    
    <EmptyState 
      v-else 
      icon="📭" 
      title="暂无投递记录" 
      description="快去投递心仪的职位吧"
      action-text="去投递"
      @action="navTo('/front/home')"
    />
    
    <!-- Pagination -->
    <div v-if="data.total > data.pageSize" class="pagination-wrapper">
      <el-pagination 
        @current-change="loadSubmit" 
        background 
        layout="prev, pager, next" 
        :page-size="data.pageSize" 
        v-model:current-page="data.pageNum" 
        :total="data.total" 
      />
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from "vue"
import request from "@/utils/request.js"
import { ElMessage, ElMessageBox } from "element-plus"
import GlassCard from "@/components/GlassCard.vue"
import TagSkill from "@/components/TagSkill.vue"
import EmptyState from "@/components/EmptyState.vue"

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
})

const activeTab = ref('all')

const tabs = [
  { value: 'all', label: '全部' },
  { value: '已投递', label: '待处理' },
  { value: '已查看', label: '已查看' },
  { value: '面试中', label: '面试中' },
  { value: '通过', label: '已通过' },
  { value: '不适合', label: '已拒绝' },
  { value: '已撤回', label: '已撤回' },
]

const normalizeStatus = (status) => {
  if (status === '已通过') return '通过'
  if (status === '已拒绝') return '不适合'
  return status
}

const getStatusLabel = (status) => {
  const map = {
    '已投递': '已投递',
    '已查看': '已查看',
    '面试中': '面试中',
    '通过': '已通过',
    '不适合': '已拒绝',
    '已撤回': '已撤回'
  }
  return map[normalizeStatus(status)] || status || '-'
}

const getActiveStep = (status) => {
  const current = normalizeStatus(status)
  if (current === '面试中' || current === '已查看') return 1
  if (current === '通过' || current === '不适合') return 2
  return 0
}

const getFinalStepTitle = (status) => {
  const current = normalizeStatus(status)
  if (current === '通过') return '已通过'
  if (current === '不适合') return '已拒绝/不适合'
  if (current === '已撤回') return '已撤回'
  return '结果待定'
}

const getFinalStepDescription = (status) => {
  const current = normalizeStatus(status)
  if (current === '通过') return '恭喜你，已通过筛选'
  if (current === '不适合') return '本次投递未通过筛选'
  if (current === '已撤回') return '你已撤回该投递'
  return '等待企业进一步处理'
}

const getFinalStepStatus = (status) => {
  const current = normalizeStatus(status)
  if (current === '通过') return 'success'
  if (current === '不适合') return 'error'
  if (current === '已撤回') return 'process'
  return 'wait'
}

const tabItems = computed(() => {
  return tabs.map(tab => ({
    ...tab,
    count: tab.value === 'all'
      ? data.tableData.length
      : data.tableData.filter(item => normalizeStatus(item.status) === tab.value).length
  }))
})

const filteredData = computed(() => {
  if (activeTab.value === 'all') return data.tableData
  return data.tableData.filter(item => normalizeStatus(item.status) === activeTab.value)
})

const getStatusVariant = (status) => {
  const map = {
    '已投递': 'default',
    '已查看': 'blue',
    '面试中': 'blue',
    '通过': 'success',
    '不适合': 'error',
    '已撤回': 'info'
  }
  return map[normalizeStatus(status)] || 'default'
}

const hasAiScore = (item) => item.aiScore !== null && item.aiScore !== undefined && item.aiScore !== ''

const normalizeScore = (score) => Math.max(0, Math.min(100, Number(score) || 0))

const getScoreLevel = (score) => {
  const value = normalizeScore(score)
  if (value >= 80) return '高匹配'
  if (value >= 60) return '中匹配'
  return '低匹配'
}

const getScoreColor = (score) => {
  const value = normalizeScore(score)
  if (value >= 80) return '#10b981'
  if (value >= 60) return '#f59e0b'
  return '#ef4444'
}

const getScoreClass = (score) => {
  const value = normalizeScore(score)
  if (value >= 80) return 'score-high'
  if (value >= 60) return 'score-mid'
  return 'score-low'
}

const getScoreTagType = (score) => {
  const value = normalizeScore(score)
  if (value >= 80) return 'success'
  if (value >= 60) return 'warning'
  return 'danger'
}

const getAiAdvice = (item) => {
  const score = normalizeScore(item.aiScore)
  const passed = item.aiReview === '合格' || normalizeStatus(item.status) === '通过'
  if (score >= 80 || passed) {
    return {
      advantage: '简历与岗位要求整体匹配度较高，可重点展示相关项目和技能。',
      weakness: '仍需补充更量化的成果数据，提升企业筛选效率。',
      advice: '面试前围绕岗位职责准备2-3个代表性案例。'
    }
  }
  if (score >= 60) {
    return {
      advantage: '具备部分岗位相关经历，基础条件有一定契合度。',
      weakness: '关键技能或项目经历与岗位要求仍有差距。',
      advice: '优化简历关键词，补充与岗位职责直接相关的经历。'
    }
  }
  return {
    advantage: '已完成投递，可根据AI反馈继续迭代简历内容。',
    weakness: '当前简历与该岗位要求匹配度偏低。',
    advice: '优先投递技能、经验要求更贴近的岗位，或完善相关项目经历。'
  }
}

const loadSubmit = () => {
  request.get('/submit/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    }
  })
}

const navTo = (url) => {
  location.href = url
}

const cancel = (id) => {
  ElMessageBox.confirm('确定撤回该投递吗？撤回后记录仍会保留。', '提示', { type: 'warning' }).then(() => {
    request.delete('/submit/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("撤回成功")
        loadSubmit()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

loadSubmit()
</script>

<style scoped>
.submit-page {
  min-height: 100vh;
  background: var(--bg-primary);
  padding: 60px 24px;
}

.page-header {
  max-width: 1200px;
  margin: 0 auto 32px;
  text-align: center;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.page-subtitle {
  font-size: 16px;
  color: var(--text-secondary);
}

.tab-nav {
  max-width: 1200px;
  margin: 0 auto 32px;
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.tab-btn {
  padding: 10px 20px;
  background: transparent;
  border: none;
  border-radius: var(--radius-sm);
  font-size: 14px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
}

.tab-btn:hover {
  color: var(--color-primary);
  background: rgba(102, 126, 234, 0.08);
}

.tab-btn.active {
  color: var(--color-primary);
  font-weight: 600;
}

.tab-btn.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 20%;
  right: 20%;
  height: 2px;
  background: var(--gradient-button);
  border-radius: 2px;
}

.tab-count {
  margin-left: 4px;
  padding: 2px 8px;
  background: var(--bg-primary);
  border-radius: 999px;
  font-size: 12px;
}

.submissions-list {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.submission-item {
  padding: 24px;
}

.submission-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.position-info {
  flex: 1;
}

.position-name {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.position-link {
  color: var(--text-primary);
  text-decoration: none;
  transition: color 0.2s ease;
}

.position-link:hover {
  color: var(--color-primary);
}

.company-name {
  font-size: 14px;
  color: var(--text-secondary);
}

.submission-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  font-size: 14px;
  color: var(--text-secondary);
}

.salary {
  color: var(--color-primary);
  font-weight: 600;
}

.ai-score {
  font-weight: 500;
}

.score-high {
  color: #10b981;
}

.score-mid {
  color: #f59e0b;
}

.score-low {
  color: #ef4444;
}

.ai-panel {
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 16px;
  margin-bottom: 16px;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  background: rgba(102, 126, 234, 0.04);
}

.ai-score-block {
  display: flex;
  align-items: center;
  gap: 16px;
}

.ai-score-text {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ai-score-title,
.ai-review-title,
.suggestion-label {
  font-size: 13px;
  font-weight: 700;
  color: var(--text-primary);
}

.ai-review-card {
  padding: 12px;
  border-radius: var(--radius-sm);
  background: var(--bg-white);
  border: 1px solid var(--border-light);
}

.ai-review-content {
  margin-top: 6px;
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
}

.ai-suggestion-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px;
}

.suggestion-card {
  padding: 12px;
  border-radius: var(--radius-sm);
  font-size: 13px;
  line-height: 1.6;
  color: var(--text-secondary);
  background: var(--bg-white);
  border: 1px solid var(--border-light);
}

.suggestion-card.advantage {
  border-color: rgba(16, 185, 129, 0.28);
}

.suggestion-card.weakness {
  border-color: rgba(245, 158, 11, 0.28);
}

.suggestion-card.advice {
  border-color: rgba(102, 126, 234, 0.28);
}

.submission-steps {
  margin: 0 0 16px;
  padding: 12px 8px 0;
}

.submission-steps :deep(.el-step__title) {
  font-size: 14px;
}

.submission-steps :deep(.el-step__description) {
  font-size: 12px;
  line-height: 1.5;
}

.submission-actions {
  display: flex;
  gap: 16px;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid var(--border-light);
}

.action-link {
  color: var(--color-primary);
  text-decoration: none;
  font-size: 14px;
}

.action-link:hover {
  text-decoration: underline;
}

.pagination-wrapper {
  max-width: 800px;
  margin: 32px auto 0;
  display: flex;
  justify-content: center;
}

@media (max-width: 640px) {
  .submission-header {
    flex-direction: column;
    gap: 12px;
  }
  
  .submission-meta {
    flex-direction: column;
    gap: 8px;
  }

  .ai-score-block {
    align-items: flex-start;
  }

  .ai-suggestion-grid {
    grid-template-columns: 1fr;
  }
}
</style>
