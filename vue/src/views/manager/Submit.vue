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
                <el-table-column prop="aiScore" label="AI评分" width="100">
                    <template v-slot="scope">
                        <el-tag v-if="hasAiScore(scope.row.aiScore)" :type="getAiScoreType(scope.row.aiScore)">
                            {{ scope.row.aiScore }}分
                        </el-tag>
                        <span v-else class="text-gray">-</span>
                    </template>
                </el-table-column>
                <el-table-column prop="aiReview" label="AI审核" min-width="200">
                    <template v-slot="scope">
                        <div v-if="scope.row.aiReview" class="ai-review-cell">
                            <el-tooltip :content="scope.row.aiReview" placement="top" :max-width="400">
                                <span class="ai-review-text">{{ scope.row.aiReview }}</span>
                            </el-tooltip>
                        </div>
                        <span v-else class="text-gray">待审核</span>
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
import {ElMessage} from "element-plus";


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

const getAiScoreType = (score) => {
    const num = Number(score)
    if (num >= 80) return 'success'
    if (num >= 60) return 'warning'
    return 'danger'
}

const hasAiScore = (score) => score !== null && score !== undefined && score !== ''

const reset = () => {
    data.positionName = null
    load()
}

load()
</script>

<style scoped>
.text-gray {
  color: #909399;
}

.ai-review-cell {
  width: 100%;
  overflow: hidden;
}

.ai-review-text {
  display: inline-block;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
}
</style>