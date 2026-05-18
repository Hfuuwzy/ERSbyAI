<template>
  <div class="resume-edit-page">
    <div class="edit-bg"></div>

    <div class="edit-container">
      <!-- Page header -->
      <header class="page-header">
        <div class="title-block">
          <h1 class="page-title">{{ isEdit ? '编辑简历' : '创建简历' }}</h1>
          <p class="page-subtitle">完善你的简历，离心仪 offer 更近一步</p>
        </div>
        <div class="header-actions">
          <el-button class="preview-btn" @click="preview">
            <el-icon class="btn-icon"><View /></el-icon>
            预览
          </el-button>
          <GradientButton @click="saveResume">
            <el-icon class="btn-icon"><Check /></el-icon>
            保存简历
          </GradientButton>
        </div>
      </header>

      <!-- Basic info -->
      <GlassCard class="edit-card">
        <div class="section-head">
          <div class="section-bar"></div>
          <h2 class="section-title">基本信息</h2>
        </div>

        <el-form :model="data.resumeData" label-position="top" class="basic-form">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12">
              <el-form-item label="简历名称">
                <el-input v-model="data.resumeData.name" placeholder="请输入简历名称" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12">
              <el-form-item label="姓名">
                <el-input v-model="data.resumeData.username" placeholder="请输入姓名" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12">
              <el-form-item label="性别">
                <el-select v-model="data.resumeData.sex" placeholder="请选择性别" style="width: 100%">
                  <el-option label="男" value="男" />
                  <el-option label="女" value="女" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12">
              <el-form-item label="期望薪资">
                <el-select v-model="data.resumeData.salary" placeholder="请选择期望薪资" style="width: 100%">
                  <el-option label="3k以下" value="3k以下" />
                  <el-option label="3-5k" value="3-5k" />
                  <el-option label="5-10k" value="5-10k" />
                  <el-option label="10-20k" value="10-20k" />
                  <el-option label="20-50k" value="20-50k" />
                  <el-option label="50k以上" value="50k以上" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12">
              <el-form-item label="学历">
                <el-select v-model="data.resumeData.education" placeholder="请选择学历" style="width: 100%">
                  <el-option label="初中及以下" value="初中及以下" />
                  <el-option label="中专/中技" value="中专/中技" />
                  <el-option label="高中" value="高中" />
                  <el-option label="大专" value="大专" />
                  <el-option label="本科" value="本科" />
                  <el-option label="硕士" value="硕士" />
                  <el-option label="博士" value="博士" />
                  <el-option label="博士后" value="博士后" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12">
              <el-form-item label="工作年限">
                <el-select v-model="data.resumeData.experience" placeholder="请选择工作年限" style="width: 100%">
                  <el-option label="在校生" value="在校生" />
                  <el-option label="应届生" value="应届生" />
                  <el-option label="1年以内" value="1年以内" />
                  <el-option label="1到3年" value="1到3年" />
                  <el-option label="3到5年" value="3到5年" />
                  <el-option label="5到10年" value="5到10年" />
                  <el-option label="10年以上" value="10年以上" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12">
              <el-form-item label="联系电话">
                <el-input v-model="data.resumeData.phone" placeholder="请输入联系电话" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12">
              <el-form-item label="联系邮箱">
                <el-input v-model="data.resumeData.email" placeholder="请输入联系邮箱" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </GlassCard>

      <!-- Education -->
      <GlassCard class="edit-card">
        <div class="section-head">
          <div class="section-bar"></div>
          <h2 class="section-title">教育经历</h2>
          <span class="section-count">{{ data.resumeData.eduExpList.length }} 条</span>
          <button class="add-btn" @click="addEduExp">
            <el-icon><Plus /></el-icon>
            <span>添加</span>
          </button>
        </div>

        <div v-if="data.resumeData.eduExpList.length" class="exp-list">
          <div
            v-for="item in data.resumeData.eduExpList"
            :key="item.id"
            class="exp-item"
          >
            <div class="exp-time">
              <el-icon><Calendar /></el-icon>
              <span>{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</span>
            </div>
            <div class="exp-main">
              <div class="exp-title">{{ item.school }}</div>
              <div class="exp-sub">{{ item.speciality }}<span class="exp-tag">{{ item.education }}</span></div>
              <div v-if="item.course" class="exp-desc">主修课程：{{ item.course }}</div>
            </div>
            <div class="exp-actions">
              <button class="icon-btn edit" @click="editEduExp(item)" title="编辑">
                <el-icon><Edit /></el-icon>
              </button>
              <button class="icon-btn delete" @click="delEduExp(item.id)" title="删除">
                <el-icon><Delete /></el-icon>
              </button>
            </div>
          </div>
        </div>
        <div v-else class="exp-empty">
          <el-icon class="empty-icon"><School /></el-icon>
          <p>还没有教育经历，点击右上角"添加"</p>
        </div>
      </GlassCard>

      <!-- Work -->
      <GlassCard class="edit-card">
        <div class="section-head">
          <div class="section-bar"></div>
          <h2 class="section-title">工作经历</h2>
          <span class="section-count">{{ data.resumeData.workExpList.length }} 条</span>
          <button class="add-btn" @click="addWorkExp">
            <el-icon><Plus /></el-icon>
            <span>添加</span>
          </button>
        </div>

        <div v-if="data.resumeData.workExpList.length" class="exp-list">
          <div
            v-for="item in data.resumeData.workExpList"
            :key="item.id"
            class="exp-item"
          >
            <div class="exp-time">
              <el-icon><Calendar /></el-icon>
              <span>{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</span>
            </div>
            <div class="exp-main">
              <div class="exp-title">{{ item.employ }}</div>
              <div class="exp-sub">{{ item.position }}<span class="exp-tag">{{ item.type }}</span></div>
              <div v-if="item.project || item.content" class="exp-desc">
                <strong v-if="item.project">{{ item.project }}：</strong>{{ item.content }}
              </div>
            </div>
            <div class="exp-actions">
              <button class="icon-btn edit" @click="editWorkExp(item)" title="编辑">
                <el-icon><Edit /></el-icon>
              </button>
              <button class="icon-btn delete" @click="delWorkExp(item.id)" title="删除">
                <el-icon><Delete /></el-icon>
              </button>
            </div>
          </div>
        </div>
        <div v-else class="exp-empty">
          <el-icon class="empty-icon"><Briefcase /></el-icon>
          <p>还没有工作经历，点击右上角"添加"</p>
        </div>
      </GlassCard>

      <!-- Project -->
      <GlassCard class="edit-card">
        <div class="section-head">
          <div class="section-bar"></div>
          <h2 class="section-title">项目经历</h2>
          <span class="section-count">{{ data.resumeData.proExpList.length }} 条</span>
          <button class="add-btn" @click="addProExp">
            <el-icon><Plus /></el-icon>
            <span>添加</span>
          </button>
        </div>

        <div v-if="data.resumeData.proExpList.length" class="exp-list">
          <div
            v-for="item in data.resumeData.proExpList"
            :key="item.id"
            class="exp-item"
          >
            <div class="exp-time">
              <el-icon><Calendar /></el-icon>
              <span>{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</span>
            </div>
            <div class="exp-main">
              <div class="exp-title">{{ item.name }}</div>
              <div v-if="item.content" class="exp-desc">项目介绍：{{ item.content }}</div>
            </div>
            <div class="exp-actions">
              <button class="icon-btn edit" @click="editProExp(item)" title="编辑">
                <el-icon><Edit /></el-icon>
              </button>
              <button class="icon-btn delete" @click="delProExp(item.id)" title="删除">
                <el-icon><Delete /></el-icon>
              </button>
            </div>
          </div>
        </div>
        <div v-else class="exp-empty">
          <el-icon class="empty-icon"><Folder /></el-icon>
          <p>还没有项目经历，点击右上角"添加"</p>
        </div>
      </GlassCard>

      <!-- Bottom save bar -->
      <div class="bottom-actions">
        <el-button class="preview-btn" @click="preview">
          <el-icon class="btn-icon"><View /></el-icon>
          预览
        </el-button>
        <GradientButton @click="saveResume">
          <el-icon class="btn-icon"><Check /></el-icon>
          保存简历
        </GradientButton>
      </div>
    </div>

    <!-- Education dialog -->
    <el-dialog
      class="resume-dialog"
      :title="data.eduForm.school ? '编辑教育经历' : '添加教育经历'"
      v-model="data.eduFormVisible"
      width="520px"
      destroy-on-close
    >
      <el-form ref="form" :model="data.eduForm" label-position="top" class="dialog-form">
        <el-form-item prop="school" label="学校名称">
          <el-input v-model="data.eduForm.school" placeholder="请输入学校名称" />
        </el-form-item>
        <el-form-item prop="speciality" label="专业名称">
          <el-input v-model="data.eduForm.speciality" placeholder="请输入专业名称" />
        </el-form-item>
        <el-form-item prop="education" label="学历">
          <el-select v-model="data.eduForm.education" placeholder="请选择学历" style="width: 100%">
            <el-option label="初中及以下" value="初中及以下" />
            <el-option label="中专/中技" value="中专/中技" />
            <el-option label="高中" value="高中" />
            <el-option label="大专" value="大专" />
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
            <el-option label="博士" value="博士" />
            <el-option label="博士后" value="博士后" />
          </el-select>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item prop="start" label="入学时间">
              <el-date-picker
                style="width: 100%"
                v-model="data.eduForm.start"
                type="date"
                placeholder="请选择日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="end" label="毕业时间">
              <el-date-picker
                style="width: 100%"
                v-model="data.eduForm.end"
                type="date"
                placeholder="请选择日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="course" label="主修课程">
          <el-input type="textarea" :rows="3" v-model="data.eduForm.course" placeholder="请输入主修课程" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.eduFormVisible = false">取消</el-button>
        <GradientButton @click="saveEduExp">确定</GradientButton>
      </template>
    </el-dialog>

    <!-- Work dialog -->
    <el-dialog
      class="resume-dialog"
      :title="data.workForm.employ ? '编辑工作经历' : '添加工作经历'"
      v-model="data.workFormVisible"
      width="520px"
      destroy-on-close
    >
      <el-form ref="form" :model="data.workForm" label-position="top" class="dialog-form">
        <el-form-item prop="employ" label="公司名称">
          <el-input v-model="data.workForm.employ" placeholder="请输入公司名称" />
        </el-form-item>
        <el-form-item prop="project" label="项目名称">
          <el-input v-model="data.workForm.project" placeholder="请输入项目名称" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item prop="position" label="职位名称">
              <el-input v-model="data.workForm.position" placeholder="请输入职位名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="type" label="职位类型">
              <el-select v-model="data.workForm.type" placeholder="请选择职位类型" style="width: 100%">
                <el-option label="全职" value="全职" />
                <el-option label="实习" value="实习" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item prop="start" label="入职时间">
              <el-date-picker
                style="width: 100%"
                v-model="data.workForm.start"
                type="date"
                placeholder="请选择日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="end" label="离职时间">
              <el-date-picker
                style="width: 100%"
                v-model="data.workForm.end"
                type="date"
                placeholder="请选择日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="content" label="项目介绍">
          <el-input type="textarea" :rows="3" v-model="data.workForm.content" placeholder="请输入项目介绍" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.workFormVisible = false">取消</el-button>
        <GradientButton @click="saveWorkExp">确定</GradientButton>
      </template>
    </el-dialog>

    <!-- Project dialog -->
    <el-dialog
      class="resume-dialog"
      :title="data.proForm.name ? '编辑项目经历' : '添加项目经历'"
      v-model="data.proFormVisible"
      width="520px"
      destroy-on-close
    >
      <el-form ref="form" :model="data.proForm" label-position="top" class="dialog-form">
        <el-form-item prop="name" label="项目名称">
          <el-input v-model="data.proForm.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item prop="start" label="开始时间">
              <el-date-picker
                style="width: 100%"
                v-model="data.proForm.start"
                type="date"
                placeholder="请选择日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="end" label="结束时间">
              <el-date-picker
                style="width: 100%"
                v-model="data.proForm.end"
                type="date"
                placeholder="请选择日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="content" label="项目介绍">
          <el-input type="textarea" :rows="4" v-model="data.proForm.content" placeholder="请输入项目介绍" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.proFormVisible = false">取消</el-button>
        <GradientButton @click="saveProExp">确定</GradientButton>
      </template>
    </el-dialog>

    <!-- Resume preview dialog -->
    <el-dialog
      class="resume-preview-dialog"
      v-model="data.previewVisible"
      title="简历预览"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="resume-preview">
        <h2>{{ data.previewData.username || data.previewData.name || '未命名简历' }}</h2>
        <p><strong>简历名称:</strong> {{ data.previewData.name || '-' }}</p>
        <p><strong>期望薪资:</strong> {{ data.previewData.salary || '-' }}</p>
        <p><strong>学历:</strong> {{ data.previewData.education || '-' }}</p>
        <p><strong>工作年限:</strong> {{ data.previewData.experience || '-' }}</p>
        <p><strong>电话:</strong> {{ data.previewData.phone || '-' }}</p>
        <p><strong>邮箱:</strong> {{ data.previewData.email || '-' }}</p>

        <h3>教育经历</h3>
        <div v-if="(data.previewData.eduExpList || []).length">
          <div v-for="edu in data.previewData.eduExpList" :key="edu.id" class="preview-section">
            <p>{{ edu.school }} - {{ edu.speciality }} ({{ edu.education }})</p>
          </div>
        </div>
        <p v-else class="preview-empty">暂无教育经历</p>

        <h3>工作经历</h3>
        <div v-if="(data.previewData.workExpList || []).length">
          <div v-for="work in data.previewData.workExpList" :key="work.id" class="preview-section">
            <p>{{ work.employ }} - {{ work.position }}</p>
          </div>
        </div>
        <p v-else class="preview-empty">暂无工作经历</p>

        <h3>项目经历</h3>
        <div v-if="(data.previewData.proExpList || []).length">
          <div v-for="pro in data.previewData.proExpList" :key="pro.id" class="preview-section">
            <p>{{ pro.name }}</p>
          </div>
        </div>
        <p v-else class="preview-empty">暂无项目经历</p>
      </div>
      <template #footer>
        <el-button @click="data.previewVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, computed } from "vue"
import request from "@/utils/request.js"
import { ElMessage, ElMessageBox } from "element-plus"
import {
  Edit, Delete, Plus, Calendar, View, Check,
  School, Briefcase, Folder
} from "@element-plus/icons-vue"
import Router from "@/router/index.js"
import GlassCard from "@/components/GlassCard.vue"
import GradientButton from "@/components/GradientButton.vue"

const data = reactive({
  resumeId: Router.currentRoute.value.query.id,
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  resumeData: {
    eduExpList: [],
    workExpList: [],
    proExpList: []
  },
  eduForm: {},
  workForm: {},
  proForm: {},
    eduFormVisible: false,
    workFormVisible: false,
    proFormVisible: false,
    previewVisible: false,
    previewData: {
      eduExpList: [],
      workExpList: [],
      proExpList: []
    },
  })

const isEdit = computed(() => !!data.resumeId)

const loadResume = () => {
  data.resumeId = Router.currentRoute.value.query.id
  if (data.resumeId) {
    request.get('resume/selectById/' + data.resumeId).then((res) => {
      if (res.code === '200') {
        data.resumeData = res.data
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

const saveResume = () => {
  if (data.resumeData.id) {
    request.put('/resume/update', data.resumeData).then((res) => {
      if (res.code === '200') {
        ElMessage.success("保存成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    data.resumeData.userId = data.user.id
    request.post('/resume/add', data.resumeData).then(res => {
      if (res.code === '200') {
        ElMessage.success('保存成功')
        setTimeout(() => {
          location.href = '/front/resume'
        }, 500)
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

const preview = () => {
  if (!data.resumeData.username && !data.resumeData.name) {
    ElMessage.info('请先填写简历信息')
    return
  }
  data.previewData = JSON.parse(JSON.stringify(data.resumeData))
  data.previewVisible = true
}

const addEduExp = () => {
  data.eduForm = {
    id: new Date().getTime() + Math.random().toString(36).substr(2)
  }
  data.eduFormVisible = true
}
const addWorkExp = () => {
  data.workForm = {
    id: new Date().getTime() + Math.random().toString(36).substr(2)
  }
  data.workFormVisible = true
}
const addProExp = () => {
  data.proForm = {
    id: new Date().getTime() + Math.random().toString(36).substr(2)
  }
  data.proFormVisible = true
}

const saveEduExp = () => {
  let form = data.resumeData.eduExpList.filter(v => v.id === data.eduForm.id)
  if (form && form.length) {
    data.resumeData.eduExpList.forEach(item => {
      if (item.id === data.eduForm.id) {
        item.school = data.eduForm.school
        item.speciality = data.eduForm.speciality
        item.education = data.eduForm.education
        item.start = data.eduForm.start
        item.end = data.eduForm.end
        item.course = data.eduForm.course
      }
    })
  } else {
    data.resumeData.eduExpList.push(data.eduForm)
  }
  data.eduFormVisible = false
}

const saveWorkExp = () => {
  let form = data.resumeData.workExpList.filter(v => v.id === data.workForm.id)
  if (form && form.length) {
    data.resumeData.workExpList.forEach(item => {
      if (item.id === data.workForm.id) {
        item.employ = data.workForm.employ
        item.project = data.workForm.project
        item.position = data.workForm.position
        item.type = data.workForm.type
        item.start = data.workForm.start
        item.end = data.workForm.end
        item.content = data.workForm.content
      }
    })
  } else {
    data.resumeData.workExpList.push(data.workForm)
  }
  data.workFormVisible = false
}

const saveProExp = () => {
  let form = data.resumeData.proExpList.filter(v => v.id === data.proForm.id)
  if (form && form.length) {
    data.resumeData.proExpList.forEach(item => {
      if (item.id === data.proForm.id) {
        item.name = data.proForm.name
        item.start = data.proForm.start
        item.end = data.proForm.end
        item.content = data.proForm.content
      }
    })
  } else {
    data.resumeData.proExpList.push(data.proForm)
  }
  data.proFormVisible = false
}

const editEduExp = (item) => {
  data.eduForm = JSON.parse(JSON.stringify(item))
  data.eduFormVisible = true
}
const editWorkExp = (item) => {
  data.workForm = JSON.parse(JSON.stringify(item))
  data.workFormVisible = true
}
const editProExp = (item) => {
  data.proForm = JSON.parse(JSON.stringify(item))
  data.proFormVisible = true
}

const delEduExp = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(() => {
    data.resumeData.eduExpList = data.resumeData.eduExpList.filter(v => v.id !== id)
  }).catch(err => { console.error(err) })
}
const delWorkExp = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(() => {
    data.resumeData.workExpList = data.resumeData.workExpList.filter(v => v.id !== id)
  }).catch(err => { console.error(err) })
}
const delProExp = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(() => {
    data.resumeData.proExpList = data.resumeData.proExpList.filter(v => v.id !== id)
  }).catch(err => { console.error(err) })
}

loadResume()
</script>

<style scoped>
.resume-edit-page {
  position: relative;
  min-height: calc(100vh - 80px);
  padding: 48px 24px 64px;
  overflow: hidden;
}

.edit-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 0% 0%, rgba(102, 126, 234, 0.18), transparent 45%),
    radial-gradient(circle at 100% 0%, rgba(240, 147, 251, 0.15), transparent 45%),
    var(--bg-primary);
  z-index: 0;
}

.edit-container {
  position: relative;
  z-index: 1;
  max-width: 960px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* Page header */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 8px;
}

.title-block {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.page-title {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
  background: var(--gradient-hero);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 0.5px;
}

.page-subtitle {
  margin: 0;
  font-size: 14px;
  color: var(--text-secondary);
}

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.preview-btn {
  height: 44px;
  padding: 0 20px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-light);
  background: var(--bg-white);
  color: var(--text-primary);
  font-weight: 600;
  transition: all 0.3s ease;
}

.preview-btn:hover {
  border-color: var(--color-primary);
  color: var(--color-primary);
  background: rgba(102, 126, 234, 0.05);
}

.btn-icon {
  margin-right: 6px;
  vertical-align: -2px;
}

/* Section head inside GlassCard */
.section-head {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.section-bar {
  width: 4px;
  height: 22px;
  border-radius: 2px;
  background: var(--gradient-button);
}

.section-title {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
}

.section-count {
  padding: 2px 10px;
  font-size: 12px;
  font-weight: 600;
  color: var(--color-primary);
  background: rgba(102, 126, 234, 0.1);
  border-radius: 999px;
}

.add-btn {
  margin-left: auto;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 6px 14px;
  font-size: 13px;
  font-weight: 600;
  color: var(--color-primary);
  background: rgba(102, 126, 234, 0.08);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.25s ease;
}

.add-btn:hover {
  background: var(--gradient-button);
  color: #fff;
  border-color: transparent;
  box-shadow: 0 4px 14px rgba(102, 126, 234, 0.35);
}

/* Basic form */
.basic-form :deep(.el-form-item__label) {
  color: var(--text-secondary);
  font-weight: 600;
  padding-bottom: 6px;
}

.basic-form :deep(.el-input__wrapper),
.basic-form :deep(.el-select__wrapper) {
  border-radius: var(--radius-sm);
  box-shadow: 0 0 0 1px var(--border-light) inset;
  transition: box-shadow 0.25s ease;
}

.basic-form :deep(.el-input__wrapper:hover),
.basic-form :deep(.el-select__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--color-primary) inset;
}

.basic-form :deep(.el-input.is-focus .el-input__wrapper),
.basic-form :deep(.el-select__wrapper.is-focused) {
  box-shadow: 0 0 0 2px var(--color-primary) inset;
}

/* Experience list */
.exp-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.exp-item {
  display: flex;
  gap: 16px;
  padding: 16px 18px;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  transition: all 0.25s ease;
}

.exp-item:hover {
  border-color: rgba(102, 126, 234, 0.4);
  box-shadow: var(--shadow-sm);
  transform: translateX(2px);
}

.exp-time {
  flex-shrink: 0;
  width: 200px;
  display: flex;
  align-items: flex-start;
  gap: 6px;
  padding-top: 2px;
  font-size: 13px;
  font-weight: 600;
  color: var(--color-primary);
}

.exp-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.exp-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-primary);
}

.exp-sub {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}

.exp-tag {
  padding: 2px 8px;
  font-size: 12px;
  color: var(--color-secondary);
  background: rgba(118, 75, 162, 0.1);
  border-radius: 4px;
  font-weight: 600;
}

.exp-desc {
  margin-top: 4px;
  font-size: 13px;
  line-height: 1.6;
  color: var(--text-muted);
}

.exp-actions {
  flex-shrink: 0;
  display: flex;
  gap: 6px;
  align-items: flex-start;
}

.icon-btn {
  width: 32px;
  height: 32px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-sm);
  background: var(--bg-white);
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
}

.icon-btn.edit {
  color: var(--color-info);
}

.icon-btn.edit:hover {
  background: var(--color-info);
  color: #fff;
  border-color: var(--color-info);
}

.icon-btn.delete {
  color: var(--color-error);
}

.icon-btn.delete:hover {
  background: var(--color-error);
  color: #fff;
  border-color: var(--color-error);
}

/* Empty state inside section */
.exp-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 32px 16px;
  background: rgba(248, 250, 252, 0.6);
  border: 1px dashed var(--border-light);
  border-radius: var(--radius-md);
  color: var(--text-muted);
  font-size: 13px;
}

.exp-empty .empty-icon {
  font-size: 36px;
  color: var(--text-muted);
  opacity: 0.6;
}

.exp-empty p {
  margin: 0;
}

/* Bottom actions */
.bottom-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 0 8px;
}

/* Dialog form */
:global(.resume-dialog .el-dialog) {
  border-radius: var(--radius-md);
  overflow: hidden;
}

:global(.resume-dialog .el-dialog__header) {
  padding: 20px 24px;
  margin-right: 0;
  background: var(--gradient-card);
  border-bottom: 1px solid var(--border-light);
}

:global(.resume-dialog .el-dialog__title) {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
}

:global(.resume-dialog .el-dialog__body) {
  padding: 24px;
}

:global(.resume-dialog .el-dialog__footer) {
  padding: 16px 24px;
  border-top: 1px solid var(--border-light);
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:global(.resume-preview-dialog .el-dialog) {
  border-radius: var(--radius-md);
  overflow: hidden;
}

:global(.resume-preview-dialog .el-dialog__header) {
  padding: 20px 24px;
  margin-right: 0;
  background: var(--gradient-card);
  border-bottom: 1px solid var(--border-light);
}

:global(.resume-preview-dialog .el-dialog__title) {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
}

:global(.resume-preview-dialog .el-dialog__body) {
  padding: 0;
}

:global(.resume-preview-dialog .el-dialog__footer) {
  padding: 16px 24px;
  border-top: 1px solid var(--border-light);
  display: flex;
  justify-content: flex-end;
}

.resume-preview {
  padding: 20px 24px;
  max-height: 600px;
  overflow-y: auto;
}

.resume-preview h2 {
  margin: 0 0 20px;
  color: var(--text-primary);
}

.resume-preview h3 {
  margin: 20px 0 10px;
  color: var(--text-secondary);
  border-bottom: 1px solid #eee;
  padding-bottom: 5px;
}

.resume-preview p {
  margin: 5px 0;
  color: var(--text-secondary);
}

.preview-section {
  margin-bottom: 12px;
}

.preview-empty {
  color: #999;
  font-size: 14px;
  margin: 8px 0 0;
}

.dialog-form :deep(.el-form-item__label) {
  color: var(--text-secondary);
  font-weight: 600;
  padding-bottom: 4px;
}

/* Responsive */
@media (max-width: 720px) {
  .resume-edit-page {
    padding: 32px 16px 48px;
  }

  .page-title {
    font-size: 26px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .header-actions {
    width: 100%;
    justify-content: flex-end;
  }

  .exp-item {
    flex-direction: column;
    gap: 10px;
  }

  .exp-time {
    width: auto;
  }

  .exp-actions {
    align-self: flex-end;
  }
}
</style>
