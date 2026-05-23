<template>
  <div>
    <!-- Search & Filter Card -->
    <div class="card" style="margin-bottom: 5px">
      <el-input 
        v-model="data.positionName" 
        prefix-icon="Search" 
        style="width: 200px; margin-right: 10px"
        placeholder="岗位名称"
      />
      <el-select 
        v-model="data.scoreFilter" 
        placeholder="AI评分筛选" 
        style="width: 140px; margin-right: 10px"
        clearable
      >
        <el-option label="全部" value="" />
        <el-option label="优秀 (80-100)" value="excellent" />
        <el-option label="良好 (60-79)" value="good" />
        <el-option label="待提升 (0-59)" value="needsImprovement" />
        <el-option label="未评分" value="unscored" />
      </el-select>
      <el-select 
        v-model="data.reviewFilter" 
        placeholder="AI审核结果" 
        style="width: 140px; margin-right: 10px"
        clearable
      >
        <el-option label="全部" value="" />
        <el-option label="适合" value="适合" />
        <el-option label="不适合" value="不适合" />
        <el-option label="一般" value="一般" />
        <el-option label="待审核" value="待审核" />
      </el-select>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <!-- Stats Card -->
    <div class="card" style="margin-bottom: 5px">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-label">已筛选简历</div>
            <div class="stat-value">{{ data.total }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-label">平均分</div>
            <div class="stat-value">{{ data.avgScore }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-label">优秀率</div>
            <div class="stat-value">{{ data.excellentRate }}%</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-label">适合率</div>
            <div class="stat-value">{{ data.suitableRate }}%</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- AI Filtered Table -->
    <div class="card" style="margin-bottom: 5px">
      <el-table 
        stripe 
        :data="data.tableData"
        :default-sort="{ prop: 'aiScore', order: 'descending' }"
      >
        <el-table-column prop="employName" label="企业名称" width="120"/>
        <el-table-column prop="positionName" label="岗位名称" width="150" />
        <el-table-column prop="userName" label="候选人" width="100"/>
        <el-table-column prop="resumeId" label="简历" width="90">
          <template #default="scope">
            <a :href="'/resumeView?id=' + scope.row.resumeId" target="_blank">查看简历</a>
          </template>
        </el-table-column>
        <el-table-column prop="aiScore" label="AI评分" width="120" sortable>
          <template #default="scope">
            <div v-if="hasAiScore(scope.row)" class="ai-score-cell">
              <span class="score-value">{{ scope.row.aiScore }}分</span>
              <el-tag :type="getScoreTagType(scope.row.aiScore)" effect="light" size="small">
                {{ getScoreLevel(scope.row.aiScore) }}
              </el-tag>
            </div>
            <span v-else class="empty-text">待评分</span>
          </template>
        </el-table-column>
        <el-table-column prop="aiReview" label="AI审核结论" width="120">
          <template #default="scope">
            <el-tag :type="getReviewTagType(scope.row.aiReview)" effect="light" size="small">
              {{ scope.row.aiReview || '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="aiReview" label="AI建议" min-width="250" show-overflow-tooltip>
          <template #default="scope">
            <span class="review-detail">{{ scope.row.aiReview || '暂无AI审核建议' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="当前状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)" effect="light">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="投递时间" width="160"/>
        <el-table-column label="操作" width="150" fixed="right" v-if="data.user.role === 'EMPLOY'">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === '已投递'" 
              @click="updateStatus(scope.row, '面试中')" 
              type="primary" 
              size="small"
            >安排面试</el-button>
            <el-button 
              v-if="scope.row.status === '面试中'" 
              @click="updateStatus(scope.row, '通过')" 
              type="success" 
              size="small"
            >录用</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Pagination -->
    <div class="card" v-if="data.total">
      <el-pagination 
        @current-change="load" 
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
import { reactive, computed } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  positionName: null,
  scoreFilter: '',
  reviewFilter: '',
  avgScore: '-',
  excellentRate: '-',
  suitableRate: '-',
});

const load = () => {
  request.get('/submit/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      positionName: data.positionName,
      // Note: These filters would need backend support
      // scoreFilter: data.scoreFilter,
      // reviewFilter: data.reviewFilter,
    }
  }).then(res => {
    if (res.code === '200') {
      let list = res.data?.list || [];
      
      // Client-side filtering for AI filters
      if (data.scoreFilter) {
        list = list.filter(item => {
          if (!hasAiScore(item)) return false;
          const score = Number(item.aiScore);
          switch (data.scoreFilter) {
            case 'excellent': return score >= 80;
            case 'good': return score >= 60 && score < 80;
            case 'needsImprovement': return score < 60;
            case 'unscored': return !hasAiScore(item);
            default: return true;
          }
        });
      }
      
      if (data.reviewFilter) {
        list = list.filter(item => {
          if (data.reviewFilter === '待审核') return !item.aiReview;
          return item.aiReview && item.aiReview.includes(data.reviewFilter);
        });
      }
      
      data.tableData = list;
      data.total = res.data?.total;
      
      // Calculate statistics
      calculateStats(list);
    }
  });
};

const calculateStats = (list) => {
  if (!list.length) {
    data.avgScore = '-';
    data.excellentRate = '-';
    data.suitableRate = '-';
    return;
  }
  
  const scored = list.filter(item => hasAiScore(item));
  if (scored.length) {
    const totalScore = scored.reduce((sum, item) => sum + Number(item.aiScore), 0);
    data.avgScore = (totalScore / scored.length).toFixed(1);
    const excellentCount = scored.filter(item => Number(item.aiScore) >= 80).length;
    data.excellentRate = Math.round((excellentCount / scored.length) * 100);
  } else {
    data.avgScore = '-';
    data.excellentRate = '0';
  }
  
  const reviewed = list.filter(item => item.aiReview);
  if (reviewed.length) {
    const suitableCount = reviewed.filter(item => item.aiReview.includes('适合')).length;
    data.suitableRate = Math.round((suitableCount / reviewed.length) * 100);
  } else {
    data.suitableRate = '0';
  }
};

const updateStatus = (row, status) => {
  let form = JSON.parse(JSON.stringify(row));
  form.status = status;
  request.put('/submit/update', form).then((res) => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const reset = () => {
  data.positionName = null;
  data.scoreFilter = '';
  data.reviewFilter = '';
  data.pageNum = 1;
  load();
};

// AI Score Helpers
const hasAiScore = (row) => {
  return row.aiScore !== null && row.aiScore !== undefined && row.aiScore !== '';
};

const getScoreTagType = (score) => {
  const num = Number(score);
  if (num >= 80) return 'success';
  if (num >= 60) return 'warning';
  return 'danger';
};

const getScoreLevel = (score) => {
  const num = Number(score);
  if (num >= 80) return '优秀';
  if (num >= 60) return '良好';
  return '待提升';
};

const getReviewTagType = (review) => {
  if (!review) return 'info';
  if (review.includes('不适合')) return 'danger';
  if (review.includes('适合')) return 'success';
  if (review.includes('一般')) return 'warning';
  return 'info';
};

const getStatusTagType = (status) => {
  switch (status) {
    case '通过': return 'success';
    case '面试中': return 'primary';
    case '不适合':
    case '不通过': return 'danger';
    default: return 'info';
  }
};

load();
</script>

<style scoped>
.ai-score-cell {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.score-value {
  font-weight: bold;
  font-size: 14px;
  color: #303133;
}

.empty-text {
  color: #909399;
  font-size: 12px;
}

.review-detail {
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
}

.stat-item {
  text-align: center;
  padding: 10px;
}

.stat-label {
  color: #909399;
  font-size: 12px;
  margin-bottom: 5px;
}

.stat-value {
  color: #303133;
  font-size: 24px;
  font-weight: bold;
}
</style>
