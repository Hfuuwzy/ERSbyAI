<template>
  <div class="ai-filter-container">
    <!-- Header Section with Stats -->
    <div class="stats-section">
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ data.total }}</div>
              <div class="stat-label">已筛选简历</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card">
            <div class="stat-icon score">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ data.avgScore }}</div>
              <div class="stat-label">AI平均分</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card">
            <div class="stat-icon excellent">
              <el-icon><Medal /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ data.excellentRate }}<span class="unit">%</span></div>
              <div class="stat-label">优秀率</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card">
            <div class="stat-icon suitable">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ data.suitableRate }}<span class="unit">%</span></div>
              <div class="stat-label">适合率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- Filter Section -->
    <div class="filter-section">
      <div class="filter-header">
        <div class="filter-title">
          <el-icon><Filter /></el-icon>
          <span>筛选条件</span>
        </div>
        <el-button type="primary" plain size="small" @click="reset">
          <el-icon><Refresh /></el-icon>
          重置筛选
        </el-button>
      </div>
      <div class="filter-content">
        <div class="filter-item">
          <span class="filter-label">岗位名称</span>
          <el-input 
            v-model="data.positionName" 
            placeholder="搜索岗位名称"
            clearable
            style="width: 200px"
          />
        </div>
        <div class="filter-item">
          <span class="filter-label">AI评分</span>
          <el-select 
            v-model="data.scoreFilter" 
            placeholder="选择评分范围"
            clearable
            style="width: 160px"
          >
            <el-option label="全部评分" value="" />
            <el-option label="优秀 (80-100分)" value="excellent">
              <span style="color: #67c23a;">●</span> 优秀 (80-100分)
            </el-option>
            <el-option label="良好 (60-79分)" value="good">
              <span style="color: #e6a23c;">●</span> 良好 (60-79分)
            </el-option>
            <el-option label="待提升 (0-59分)" value="needsImprovement">
              <span style="color: #f56c6c;">●</span> 待提升 (0-59分)
            </el-option>
            <el-option label="未评分" value="unscored">
              <span style="color: #909399;">●</span> 未评分
            </el-option>
          </el-select>
        </div>
        <div class="filter-item">
          <span class="filter-label">AI审核</span>
          <el-select 
            v-model="data.reviewFilter" 
            placeholder="选择审核结果"
            clearable
            style="width: 140px"
          >
            <el-option label="全部" value="" />
            <el-option label="适合" value="适合">
              <el-tag type="success" size="small">适合</el-tag>
            </el-option>
            <el-option label="不适合" value="不适合">
              <el-tag type="danger" size="small">不适合</el-tag>
            </el-option>
            <el-option label="一般" value="一般">
              <el-tag type="warning" size="small">一般</el-tag>
            </el-option>
            <el-option label="待审核" value="待审核">
              <el-tag type="info" size="small">待审核</el-tag>
            </el-option>
          </el-select>
        </div>
        <div class="filter-actions">
          <el-button type="primary" @click="load">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
        </div>
      </div>
    </div>

    <!-- Data Table -->
    <div class="table-section">
      <div class="table-header">
        <div class="table-title">
          <el-icon><List /></el-icon>
          <span>筛选结果</span>
          <el-tag type="info" effect="plain" size="small">{{ data.tableData.length }} 条记录</el-tag>
        </div>
      </div>
      <el-table 
        :data="data.tableData"
        :default-sort="{ prop: 'aiScore', order: 'descending' }"
        stripe
        highlight-current-row
        class="ai-table"
      >
        <el-table-column prop="employName" label="企业" min-width="100"/>
        <el-table-column prop="positionName" label="岗位" min-width="140" />
        <el-table-column prop="userName" label="候选人" min-width="90"/>
        <el-table-column prop="resumeId" label="简历" width="80" align="center">
          <template #default="scope">
            <el-button 
              link 
              type="primary" 
              @click="openResume(scope.row.resumeId)"
            >
              <el-icon><Document /></el-icon>
              查看
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="aiScore" label="AI评分" width="100" sortable align="center">
          <template #default="scope">
            <div v-if="hasAiScore(scope.row)" class="score-display">
              <div class="score-ring" :class="getScoreClass(scope.row.aiScore)">
                {{ scope.row.aiScore }}
              </div>
            </div>
            <span v-else class="no-score">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="aiReview" label="AI评价" width="100" align="center">
          <template #default="scope">
            <el-tag 
              :type="getReviewTagType(scope.row.aiReview)" 
              effect="dark"
              size="small"
              class="review-tag"
            >
              {{ getShortReview(scope.row.aiReview) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="aiReview" label="AI建议" min-width="200" show-overflow-tooltip>
          <template #default="scope">
            <span class="suggestion-text">{{ scope.row.aiReview || '暂无AI审核建议' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="scope">
            <el-tag 
              :type="getStatusTagType(scope.row.status)" 
              effect="light"
              size="small"
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="投递时间" width="150"/>
        <el-table-column label="操作" width="120" fixed="right" align="center" v-if="data.user.role === 'EMPLOY'">
          <template #default="scope">
            <el-dropdown trigger="click">
              <el-button type="primary" size="small">
                操作<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <!-- 根据当前状态显示可用操作 -->
                  <template v-if="scope.row.status === '已投递'">
                    <el-dropdown-item @click="updateStatus(scope.row, '面试中')">
                      <el-icon><Check /></el-icon>安排面试
                    </el-dropdown-item>
                    <el-dropdown-item @click="updateStatus(scope.row, '通过')">
                      <el-icon><CircleCheck /></el-icon>直接录用
                    </el-dropdown-item>
                    <el-dropdown-item @click="updateStatus(scope.row, '不适合')" divided>
                      <el-icon><CircleClose /></el-icon>标记不适合
                    </el-dropdown-item>
                  </template>
                  <template v-else-if="scope.row.status === '面试中'">
                    <el-dropdown-item @click="updateStatus(scope.row, '通过')">
                      <el-icon><CircleCheck /></el-icon>录用
                    </el-dropdown-item>
                    <el-dropdown-item @click="updateStatus(scope.row, '不通过')">
                      <el-icon><CircleClose /></el-icon>面试不通过
                    </el-dropdown-item>
                  </template>
                  <template v-else-if="scope.row.status === '不通过' || scope.row.status === '不适合'">
                    <el-dropdown-item disabled>
                      <el-icon><InfoFilled /></el-icon>已结束
                    </el-dropdown-item>
                  </template>
                  <template v-else-if="scope.row.status === '通过'">
                    <el-dropdown-item disabled>
                      <el-icon><SuccessFilled /></el-icon>已通过
                    </el-dropdown-item>
                  </template>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Pagination -->
    <div class="pagination-section" v-if="data.total">
      <el-pagination 
        @current-change="load" 
        background 
        layout="total, prev, pager, next" 
        :page-size="data.pageSize"
        v-model:current-page="data.pageNum" 
        :total="data.total"
      />
    </div>
  </div>
</template>

<script setup>
import { reactive, watch } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { 
  Document, 
  TrendCharts, 
  Medal, 
  CircleCheck, 
  Filter, 
  Refresh, 
  Search, 
  List,
  ArrowDown,
  Check,
  CircleClose,
  InfoFilled,
  SuccessFilled
} from "@element-plus/icons-vue";

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

const openResume = (resumeId) => {
  window.open(`/resumeView?id=${resumeId}`, '_blank');
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

const getScoreClass = (score) => {
  const num = Number(score);
  if (num >= 80) return 'excellent';
  if (num >= 60) return 'good';
  return 'needs-improvement';
};

const getReviewTagType = (review) => {
  if (!review) return 'info';
  if (review.includes('不合格') || review.includes('不适合')) return 'danger';
  if (review.includes('合格') || review.includes('适合')) return 'success';
  if (review.includes('一般') || review.includes('待提升')) return 'warning';
  return 'info';
};

const getShortReview = (review) => {
  if (!review) return '待审核';
  // 支持多种评价格式
  if (review.includes('不合格') || review.includes('不适合')) return '不适合';
  if (review.includes('合格') || review.includes('适合')) return '适合';
  if (review.includes('一般')) return '一般';
  if (review.includes('待提升')) return '待提升';
  // 返回原文前几个字
  return review.length > 4 ? review.substring(0, 4) + '...' : review;
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

// 热查询 - 监听筛选条件变化
watch(
  () => [data.positionName, data.scoreFilter, data.reviewFilter],
  ([newPositionName, newScoreFilter, newReviewFilter], [oldPositionName, oldScoreFilter, oldReviewFilter]) => {
    // 只有当筛选条件真正改变时才触发查询（避免初始化时重复加载）
    if (newPositionName !== oldPositionName || newScoreFilter !== oldScoreFilter || newReviewFilter !== oldReviewFilter) {
      data.pageNum = 1; // 重置到第一页
      load();
    }
  },
  { immediate: false }
);

load();
</script>

<style scoped>
.ai-filter-container {
  padding: 0;
}

/* Stats Section */
.stats-section {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  border: 1px solid #e4e7ed;
  transition: all 0.3s;
}

.stat-card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-icon.total {
  background: linear-gradient(135deg, #409eff 0%, #79bbff 100%);
  color: white;
}

.stat-icon.score {
  background: linear-gradient(135deg, #67c23a 0%, #95d475 100%);
  color: white;
}

.stat-icon.excellent {
  background: linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%);
  color: white;
}

.stat-icon.suitable {
  background: linear-gradient(135deg, #f56c6c 0%, #f89898 100%);
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1.2;
}

.stat-value .unit {
  font-size: 14px;
  font-weight: 400;
  color: #909399;
  margin-left: 2px;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

/* Filter Section */
.filter-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  border: 1px solid #e4e7ed;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.filter-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.filter-title .el-icon {
  font-size: 18px;
  color: #409eff;
}

.filter-content {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: flex-end;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-label {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
}

.filter-actions {
  margin-left: auto;
  display: flex;
  gap: 10px;
}

/* Table Section */
.table-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  border: 1px solid #e4e7ed;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.table-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.table-title .el-icon {
  font-size: 18px;
  color: #409eff;
}

/* AI Table Styles */
.ai-table {
  border-radius: 4px;
}

.ai-table :deep(.el-table__header) {
  background-color: #f5f7fa;
}

.ai-table :deep(.el-table__header th) {
  font-weight: 600;
  color: #606266;
  background-color: #f5f7fa;
}

.score-display {
  display: flex;
  align-items: center;
  justify-content: center;
}

.score-ring {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  border: 2px solid;
}

.score-ring.excellent {
  border-color: #67c23a;
  color: #67c23a;
  background: rgba(103, 194, 58, 0.1);
}

.score-ring.good {
  border-color: #e6a23c;
  color: #e6a23c;
  background: rgba(230, 162, 60, 0.1);
}

.score-ring.needs-improvement {
  border-color: #f56c6c;
  color: #f56c6c;
  background: rgba(245, 108, 108, 0.1);
}

.no-score {
  color: #909399;
  font-size: 14px;
}

.review-tag {
  min-width: 60px;
  text-align: center;
}

.suggestion-text {
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
}

/* Pagination Section */
.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* Responsive */
@media (max-width: 768px) {
  .filter-content {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-item {
    width: 100%;
  }
  
  .filter-item :deep(.el-input),
  .filter-item :deep(.el-select) {
    width: 100% !important;
  }
  
  .filter-actions {
    margin-left: 0;
    justify-content: flex-end;
  }
  
  .stat-card {
    margin-bottom: 10px;
  }
}
</style>
