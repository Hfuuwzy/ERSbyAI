<template>
  <div class="job-card" @click="goDetail">
    <div v-if="matchScore !== null" :class="['match-badge', getMatchClass(matchScore)]">
      匹配度 {{ matchScore }}%
    </div>
    <div class="job-card-header">
      <div class="job-name">{{ job.name }}</div>
      <div class="job-salary">{{ job.salary }}</div>
    </div>

    <div class="job-tags" v-if="job.tagList && job.tagList.length">
      <span class="job-tag" v-for="tag in job.tagList" :key="tag">{{ tag }}</span>
    </div>

    <div class="job-card-footer">
      <img class="employ-avatar" :src="job.employAvatar" alt="" />
      <div class="employ-info">
        <div class="employ-name">{{ job.employName }}</div>
        <div class="employ-stage">{{ job.employStage }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  job: {
    type: Object,
    required: true
  }
})

const normalizeScore = (score) => Math.max(0, Math.min(100, Math.round(Number(score) || 0)))

const matchScore = computed(() => {
  const value = props.job.matchScore ?? props.job.matchPercent ?? props.job.aiScore ?? props.job.score
  if (value === null || value === undefined || value === '') return null
  return normalizeScore(value)
})

const getMatchClass = (score) => {
  if (score >= 80) return 'high'
  if (score >= 60) return 'mid'
  return 'low'
}

const goDetail = () => {
  // keep parity with project convention (non-SPA navigation used elsewhere)
  location.href = '/front/positionDetail?id=' + props.job.id
}
</script>

<style scoped>
.job-card {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-width: 300px;
  height: 190px;
  padding: 18px 20px;
  background: var(--gradient-card);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease, border-color 0.3s ease;
  overflow: hidden;
}

.match-badge {
  position: absolute;
  top: 12px;
  right: 14px;
  z-index: 2;
  padding: 4px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  line-height: 1.4;
  border: 1px solid currentColor;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 6px 18px rgba(15, 23, 42, 0.08);
}

.match-badge.high {
  color: #10b981;
}

.match-badge.mid {
  color: #f59e0b;
}

.match-badge.low {
  color: #ef4444;
}

.job-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: var(--gradient-button);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.job-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-hover);
  border-color: rgba(102, 126, 234, 0.3);
}

.job-card:hover::before {
  opacity: 1;
}

.job-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding-right: 82px;
}

.job-name {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
  text-align: left;
}

.job-salary {
  font-size: 15px;
  font-weight: 700;
  background: var(--gradient-button);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  white-space: nowrap;
}

.job-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin: 10px 0;
}

.job-tag {
  padding: 3px 10px;
  font-size: 12px;
  color: var(--color-primary);
  background: rgba(102, 126, 234, 0.08);
  border-radius: 999px;
  white-space: nowrap;
}

.job-card-footer {
  display: flex;
  align-items: center;
  margin-top: auto;
  gap: 10px;
}

.employ-avatar {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  object-fit: cover;
  border: 1px solid var(--border-light);
}

.employ-info {
  flex: 1;
  text-align: left;
  min-width: 0;
}

.employ-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.employ-stage {
  font-size: 12px;
  color: var(--text-muted);
}
</style>
