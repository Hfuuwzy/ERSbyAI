<template>
    <div>
        <div class="card" style="margin-bottom: 5px">
            <el-input v-model="data.positionName" prefix-icon="Search" style="width: 240px; margin-right: 10px"
                      placeholder="请输入岗位名称查询"></el-input>
            <el-button type="info" plain @click="load">查询</el-button>
            <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
        </div>
        <div class="card" style="margin-bottom: 5px">
            <el-table stripe :data="data.tableData">
                <el-table-column prop="employName" label="企业名称"/>
                <el-table-column prop="positionName" label="岗位名称">
                </el-table-column>
                <el-table-column prop="userName" label="投递用户"/>
                <el-table-column prop="resumeId" label="投递简历">
                    <template v-slot="scope">
                        <a :href="'/resumeView?id=' + scope.row.resumeId" target="_blank">简历预览</a>
                    </template>
                </el-table-column>
                <el-table-column prop="time" label="投递时间"/>
                <el-table-column prop="status" label="投递状态">
                    <template v-slot="scope">
                        <el-tag v-if="scope.row.status === '不适合'" type="danger">{{ scope.row.status }}</el-tag>
                        <el-tag v-if="scope.row.status === '面试中'" type="primary">{{ scope.row.status }}</el-tag>
                        <el-tag v-if="scope.row.status === '通过'" type="success">{{ scope.row.status }}</el-tag>
                        <el-tag v-if="scope.row.status === '不通过'" type="warning">{{ scope.row.status }}</el-tag>
                        <el-tag v-if="scope.row.status === '已投递'" type="info">{{ scope.row.status }}</el-tag>
                    </template>
                </el-table-column>
              <el-table-column prop="aiScore" label="AI评分" min-width="170">
                <template v-slot="scope">
                  <div v-if="hasAiScore(scope.row)" class="ai-score-cell">
                    <el-progress
                      :percentage="normalizeScore(scope.row.aiScore)"
                      :stroke-width="8"
                      :color="getScoreColor(scope.row.aiScore)"
                    />
                    <el-tag :type="getScoreTagType(scope.row.aiScore)" effect="light" size="small">
                      {{ getScoreLevel(scope.row.aiScore) }}
                    </el-tag>
                  </div>
                  <span v-else class="empty-text">待评分</span>
                </template>
              </el-table-column>
              <el-table-column prop="aiReview" label="AI审核/建议" min-width="260" show-overflow-tooltip>
                <template v-slot="scope">
                  <div class="ai-review-cell">
                    <el-tag :type="getReviewTagType(scope.row.aiReview)" effect="light" size="small">
                      {{ scope.row.aiReview || '待审核' }}
                    </el-tag>
                    <div class="ai-advice-text">{{ getAiAdvice(scope.row).advice }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="400" fixed="right" v-if="data.user.role === 'EMPLOY'">
                <template v-slot="scope">
                  <el-button @click="updateStatus(scope.row, '不适合')" type="danger">不适合</el-button>
                  <el-button @click="updateStatus(scope.row, '面试中')" type="primary">面试中</el-button>
                  <el-button @click="updateStatus(scope.row, '通过')" type="success">通过</el-button>
                  <el-button @click="updateStatus(scope.row, '不通过')" type="warning">不通过</el-button>
                </template>
              </el-table-column>
            </el-table>
        </div>
        <div class="card" v-if="data.total">
            <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize"
                           v-model:current-page="data.pageNum" :total="data.total"/>
        </div>
    </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";


const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    tableData: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    positionName: null,
})

const load = () => {
    request.get('/submit/selectPage', {
        params: {
            pageNum: data.pageNum,
            pageSize: data.pageSize,
            positionName: data.positionName
        }
    }).then(res => {
        if (res.code === '200') {
            data.tableData = res.data?.list || []
            data.total = res.data?.total
        }
    })
}

const updateStatus = (row,status) => {
    let form = JSON.parse(JSON.stringify(row))
    form.status = status
    request.put('submit/update',form).then((res) => {
        if (res.code === '200') {
            ElMessage.success('操作成功')
            load()
        } else {
            ElMessage.error(res.msg)
        }
    })
}

const reset = () => {
    data.positionName = null
    load()
}

const hasAiScore = (row) => row.aiScore !== null && row.aiScore !== undefined && row.aiScore !== ''

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

const getScoreTagType = (score) => {
    const value = normalizeScore(score)
    if (value >= 80) return 'success'
    if (value >= 60) return 'warning'
    return 'danger'
}

const getReviewTagType = (review) => {
    if (review === '合格') return 'success'
    if (review === '不合格') return 'danger'
    return 'info'
}

const getAiAdvice = (row) => {
    const score = normalizeScore(row.aiScore)
    const passed = row.aiReview === '合格' || row.status === '通过'
    if (score >= 80 || passed) {
        return {
            advice: '优势明显，建议优先沟通并结合岗位职责安排面试。'
        }
    }
    if (score >= 60) {
        return {
            advice: '具备部分匹配点，建议人工复核关键技能和项目经历。'
        }
    }
    return {
        advice: '匹配度偏低，建议谨慎推进或补充筛选条件后复核。'
    }
}

load()
</script>

<style scoped>
.ai-score-cell {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.ai-score-cell :deep(.el-progress__text) {
    min-width: 34px;
}

.ai-review-cell {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.ai-advice-text {
    font-size: 12px;
    line-height: 1.5;
    color: #666;
}

.empty-text {
    color: #999;
    font-size: 13px;
}
</style>
