<template>
    <div class="resume-container">
        <div class="main-content">
            <div class="header-section">
                <div class="page-title">我的简历</div>
                <div class="resume-count">共 {{ data.resumeData.length }} 份简历</div>
            </div>

            <div class="resume-grid">
                <!-- 新建简历卡片 -->
                <div class="resume-card create-card" @click="navTo('/front/resumeEdit')">
                    <div class="create-icon">
                        <el-icon><Plus /></el-icon>
                    </div>
                    <div class="create-text">新建简历</div>
                </div>

                <!-- 简历列表卡片 -->
                <div class="resume-card" v-for="(item, index) in data.resumeData" :key="index">
                    <div class="card-cover" @click="navTo('/front/resumeEdit?id=' + item.id)">
                        <!-- 使用图片展示封面 -->
                        <img src="@/assets/imgs/img.png" alt="简历封面" class="resume-cover-img">
                        <div class="cover-hover-mask">
                            <el-button type="primary" round size="small">编辑简历</el-button>
                        </div>
                    </div>
                    
                    <div class="card-info">
                        <div class="info-main">
                            <div class="resume-name" :title="item.name">{{ item.name }}</div>
                            <div class="resume-time">最近更新: 刚刚</div>
                        </div>
                        <div class="info-actions">
                            <el-tooltip content="预览简历" placement="top">
                                <a :href="'/resumeView?id=' + item.id" target="_blank" class="action-btn preview-btn">
                                    <el-icon><View /></el-icon>
                                </a>
                            </el-tooltip>
                            <el-tooltip content="删除简历" placement="top">
                                <div class="action-btn delete-btn" @click.stop="delResume(item.id)">
                                    <el-icon><Delete /></el-icon>
                                </div>
                            </el-tooltip>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- 空状态 -->
            <div v-if="data.resumeData.length === 0" class="empty-state">
                <el-empty description="暂无简历，快去创建一份吧！"></el-empty>
            </div>
        </div>
    </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Plus, View} from "@element-plus/icons-vue";

const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    resumeData: [],
})

const navTo = (url) => {
    location.href = url
}
const delResume = (id) => {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
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
        params: {
            userId: data.user.id
        }
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
.resume-container {
    min-height: calc(100vh - 100px);
    background-color: #f5f7fa;
    padding: 40px 0;
}

.main-content {
    width: 1200px;
    margin: 0 auto;
    max-width: 95%;
}

.header-section {
    display: flex;
    align-items: baseline;
    margin-bottom: 30px;
}

.page-title {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin-right: 15px;
}

.resume-count {
    color: #999;
    font-size: 14px;
}

.resume-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
    gap: 25px;
}

/* 卡片通用样式 */
.resume-card {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: column;
    height: 280px; /* 调整总高度适应图片 */
    position: relative;
}

.resume-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* 新建卡片样式 */
.create-card {
    border: 2px dashed #e0e0e0;
    box-shadow: none;
    cursor: pointer;
    align-items: center;
    justify-content: center;
    background: transparent;
}

.create-card:hover {
    border-color: #409EFF;
    background: rgba(64, 158, 255, 0.02);
}

.create-icon {
    font-size: 40px;
    color: #ccc;
    margin-bottom: 15px;
    transition: color 0.3s;
}

.create-text {
    color: #666;
    font-size: 16px;
    font-weight: 500;
}

.create-card:hover .create-icon,
.create-card:hover .create-text {
    color: #409EFF;
}

/* 简历封面区域 */
.card-cover {
    height: 180px; /* 稍微调低高度，让图片比例更协调 */
    background: #f8f8f8;
    position: relative;
    cursor: pointer;
    overflow: hidden;
}

.resume-cover-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.5s ease;
}

.resume-card:hover .resume-cover-img {
    transform: scale(1.05); /* 悬停微放大图片 */
}

/* 移除之前的 CSS 模拟样式相关代码 */
.cover-hover-mask {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s;
}

.card-cover:hover .cover-hover-mask {
    opacity: 1;
}

/* 卡片信息区域 */
.card-info {
    padding: 15px 20px;
    background: #fff;
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-top: 1px solid #f0f0f0;
}

.info-main {
    flex: 1;
    overflow: hidden;
    margin-right: 10px;
}

.resume-name {
    font-size: 15px;
    font-weight: 600;
    color: #333;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin-bottom: 4px;
}

.resume-time {
    font-size: 12px;
    color: #999;
}

.info-actions {
    display: flex;
    gap: 10px;
}

.action-btn {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.2s;
    background: #f5f7fa;
    color: #606266;
}

.preview-btn:hover {
    background: #e6f7ff;
    color: #1890ff;
}

.delete-btn:hover {
    background: #fff1f0;
    color: #ff4d4f;
}

/* 响应式 */
@media (max-width: 768px) {
    .resume-grid {
        grid-template-columns: repeat(auto-fill, minmax(100%, 1fr));
    }
}
</style>