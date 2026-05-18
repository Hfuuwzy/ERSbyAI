<template>
  <div class="person-page">
    <div class="person-bg"></div>

    <div class="person-container">
      <!-- Profile Hero -->
      <GlassCard class="profile-card" :liftable="false">
        <div class="profile-header">
          <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleFileUpload"
            :show-file-list="false"
            class="avatar-uploader"
          >
            <div class="avatar-wrap">
              <img v-if="data.user.avatar" :src="data.user.avatar" class="profile-avatar" alt="avatar" />
              <div v-else class="profile-avatar avatar-placeholder">
                <el-icon><User /></el-icon>
              </div>
              <div class="avatar-overlay">
                <el-icon><Camera /></el-icon>
                <span>更换</span>
              </div>
            </div>
          </el-upload>

          <div class="profile-info">
            <h1 class="profile-name">{{ data.user.name || data.user.username || '未命名用户' }}</h1>
            <div class="profile-meta">
              <span class="role-badge" :class="roleBadgeClass">{{ roleLabel }}</span>
              <span class="profile-handle">@{{ data.user.username }}</span>
            </div>
            <div class="contact-row">
              <span v-if="data.user.phone" class="contact-item">
                <el-icon><Iphone /></el-icon>
                <span>{{ data.user.phone }}</span>
              </span>
              <span v-if="data.user.email" class="contact-item">
                <el-icon><Message /></el-icon>
                <span>{{ data.user.email }}</span>
              </span>
              <span v-if="!data.user.phone && !data.user.email" class="contact-empty">
                还未填写联系方式
              </span>
            </div>
          </div>

          <GradientButton class="edit-btn" @click="editProfileInit">
            <el-icon style="vertical-align: -3px; margin-right: 6px;"><Edit /></el-icon>
            编辑资料
          </GradientButton>
        </div>
      </GlassCard>

      <!-- Info grid -->
      <div class="info-grid">
        <!-- 基本信息 -->
        <GlassCard class="info-card">
          <div class="card-head">
            <div class="head-icon head-icon-info">
              <el-icon><User /></el-icon>
            </div>
            <h2 class="card-title">基本信息</h2>
          </div>
          <ul class="info-list">
            <li class="info-item">
              <span class="info-label">用户名</span>
              <span class="info-value">{{ data.user.username || '未填写' }}</span>
            </li>
            <li class="info-item">
              <span class="info-label">姓名</span>
              <span class="info-value">{{ data.user.name || '未填写' }}</span>
            </li>
            <li class="info-item">
              <span class="info-label">邮箱</span>
              <span class="info-value">{{ data.user.email || '未填写' }}</span>
            </li>
          </ul>
        </GlassCard>

        <!-- 求职意向 (USER) -->
        <GlassCard v-if="data.user.role === 'USER'" class="info-card">
          <div class="card-head">
            <div class="head-icon head-icon-job">
              <el-icon><Aim /></el-icon>
            </div>
            <h2 class="card-title">求职意向</h2>
          </div>
          <ul class="info-list">
            <li class="info-item">
              <span class="info-label">期望职位</span>
              <span class="info-value">{{ data.user.position || '请到简历中编辑' }}</span>
            </li>
            <li class="info-item">
              <span class="info-label">期望薪资</span>
              <span class="info-value">{{ data.user.salary || '请到简历中编辑' }}</span>
            </li>
            <li class="info-item">
              <span class="info-label">期望城市</span>
              <span class="info-value">{{ data.user.city || '请到简历中编辑' }}</span>
            </li>
          </ul>
          <button class="ghost-link" @click="goResume">
            <span>前往简历编辑</span>
            <el-icon><ArrowRight /></el-icon>
          </button>
        </GlassCard>

        <!-- 企业信息 (EMPLOY) -->
        <GlassCard v-else-if="data.user.role === 'EMPLOY'" class="info-card">
          <div class="card-head">
            <div class="head-icon head-icon-job">
              <el-icon><OfficeBuilding /></el-icon>
            </div>
            <h2 class="card-title">企业信息</h2>
          </div>
          <ul class="info-list">
            <li class="info-item">
              <span class="info-label">企业名称</span>
              <span class="info-value">{{ data.user.companyName || data.user.name || '未填写' }}</span>
            </li>
            <li class="info-item">
              <span class="info-label">所在地</span>
              <span class="info-value">{{ data.user.city || '未填写' }}</span>
            </li>
            <li class="info-item">
              <span class="info-label">行业</span>
              <span class="info-value">{{ data.user.industry || '未填写' }}</span>
            </li>
          </ul>
        </GlassCard>

        <!-- 账号安全 -->
        <GlassCard class="info-card">
          <div class="card-head">
            <div class="head-icon head-icon-security">
              <el-icon><Lock /></el-icon>
            </div>
            <h2 class="card-title">账号安全</h2>
          </div>
          <ul class="info-list">
            <li class="info-item action-item">
              <div class="action-meta">
                <div class="action-title">登录密码</div>
                <div class="action-hint">建议定期更换以保护账号安全</div>
              </div>
              <button class="ghost-btn" @click="changePasswordInit">修改</button>
            </li>
            <li class="info-item action-item">
              <div class="action-meta">
                <div class="action-title">绑定手机</div>
                <div class="action-hint">
                  {{ data.user.phone ? maskedPhone : '未绑定，请补充手机号' }}
                </div>
              </div>
              <button class="ghost-btn" @click="editProfileInit">
                {{ data.user.phone ? '更换' : '绑定' }}
              </button>
            </li>
          </ul>
        </GlassCard>
      </div>
    </div>

    <!-- Edit profile dialog -->
    <el-dialog title="编辑资料" v-model="data.editVisible" width="460px" destroy-on-close center>
      <el-form ref="editFormRef" :model="data.editForm" label-width="70px" style="padding: 12px 8px 0;">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="data.editForm.name" prefix-icon="Postcard" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="data.editForm.phone" prefix-icon="Iphone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="data.editForm.email" prefix-icon="Message" placeholder="请输入邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.editVisible = false">取 消</el-button>
          <GradientButton @click="saveProfile">保存修改</GradientButton>
        </span>
      </template>
    </el-dialog>

    <!-- Change password dialog -->
    <el-dialog title="修改密码" v-model="data.formVisible" width="420px" destroy-on-close center>
      <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="80px" style="padding: 12px 8px 0;">
        <el-form-item label="原密码" prop="password">
          <el-input v-model="data.user.password" placeholder="请输入原密码" show-password prefix-icon="Key" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="data.user.newPassword" placeholder="请输入新密码" show-password prefix-icon="Lock" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="data.user.confirmPassword" placeholder="请确认新密码" show-password prefix-icon="Check" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <GradientButton @click="changePassword">确 定</GradientButton>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from "vue"
import request from "@/utils/request.js"
import { ElMessage } from "element-plus"
import {
  User, Camera, Lock, Edit, Iphone, Message, Aim,
  OfficeBuilding, ArrowRight
} from "@element-plus/icons-vue"
import GlassCard from "@/components/GlassCard.vue"
import GradientButton from "@/components/GradientButton.vue"

const formRef = ref()
const editFormRef = ref()

const baseUrl = import.meta.env.VITE_BASE_URL

const validatePass = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('请确认密码'))
  }
  if (value !== data.form.newPassword) {
    return callback(new Error('确认密码与新密码不一致'))
  }
  callback()
}

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  editVisible: false,
  editForm: {},
  rules: {
    password: [
      { required: true, message: '请输入原密码', trigger: 'blur' },
    ],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
    ],
    confirmPassword: [
      { validator: validatePass, trigger: 'blur' }
    ]
  }
})

const roleLabel = computed(() => {
  const map = { USER: '求职者', EMPLOY: '企业', ADMIN: '管理员' }
  return map[data.user.role] || '访客'
})

const roleBadgeClass = computed(() => {
  const map = {
    USER: 'role-user',
    EMPLOY: 'role-employ',
    ADMIN: 'role-admin'
  }
  return map[data.user.role] || 'role-guest'
})

const maskedPhone = computed(() => {
  const p = data.user.phone || ''
  if (p.length < 7) return p
  return p.slice(0, 3) + '****' + p.slice(-4)
})

const handleFileUpload = (res) => {
  data.user.avatar = res.data
  persistUser()
}

const emit = defineEmits(['updateUser'])

const persistUser = () => {
  const endpoint = data.user.role === 'EMPLOY' ? '/employ/update' : '/user/update'
  request.put(endpoint, data.user).then(res => {
    if (res.code === '200') {
      localStorage.setItem('xm-user', JSON.stringify(data.user))
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const editProfileInit = () => {
  data.editForm = {
    name: data.user.name,
    phone: data.user.phone,
    email: data.user.email
  }
  data.editVisible = true
}

const saveProfile = () => {
  Object.assign(data.user, data.editForm)
  const endpoint = data.user.role === 'EMPLOY' ? '/employ/update' : '/user/update'
  request.put(endpoint, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('保存成功')
      localStorage.setItem('xm-user', JSON.stringify(data.user))
      data.editVisible = false
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const changePasswordInit = () => {
  data.formVisible = true
}

const changePassword = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.put('/updatePassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('保存成功')
          localStorage.removeItem('xm-user')
          location.href = '/login'
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const goResume = () => {
  location.href = '/front/resume'
}
</script>

<style scoped>
.person-page {
  position: relative;
  min-height: calc(100vh - 80px);
  padding: 48px 24px 64px;
  overflow: hidden;
}

.person-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 0% 0%, rgba(102, 126, 234, 0.18), transparent 45%),
    radial-gradient(circle at 100% 0%, rgba(240, 147, 251, 0.15), transparent 45%),
    var(--bg-primary);
  z-index: 0;
}

.person-container {
  position: relative;
  z-index: 1;
  max-width: 1100px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 28px;
}

/* Profile Hero */
.profile-card {
  padding: 36px;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 28px;
  flex-wrap: wrap;
}

.avatar-uploader :deep(.el-upload) {
  border-radius: 50%;
}

.avatar-wrap {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  cursor: pointer;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.28);
  transition: transform 0.3s ease;
}

.avatar-wrap:hover {
  transform: scale(1.04);
}

.profile-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.avatar-placeholder {
  background: var(--gradient-button);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 42px;
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  background: rgba(15, 23, 42, 0.55);
  color: #fff;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-overlay .el-icon {
  font-size: 22px;
}

.avatar-wrap:hover .avatar-overlay {
  opacity: 1;
}

.profile-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.profile-name {
  margin: 0;
  font-size: 30px;
  font-weight: 700;
  background: var(--gradient-hero);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 0.4px;
}

.profile-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.role-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.4px;
}

.role-user {
  background: rgba(102, 126, 234, 0.12);
  color: var(--color-primary);
  border: 1px solid rgba(102, 126, 234, 0.3);
}

.role-employ {
  background: rgba(240, 147, 251, 0.12);
  color: var(--color-accent);
  border: 1px solid rgba(240, 147, 251, 0.35);
}

.role-admin {
  background: rgba(245, 158, 11, 0.12);
  color: var(--color-warning);
  border: 1px solid rgba(245, 158, 11, 0.3);
}

.role-guest {
  background: rgba(148, 163, 184, 0.15);
  color: var(--text-muted);
  border: 1px solid var(--border-light);
}

.profile-handle {
  font-size: 13px;
  color: var(--text-muted);
}

.contact-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 13px;
  color: var(--text-secondary);
}

.contact-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.contact-item .el-icon {
  color: var(--color-primary);
  font-size: 15px;
}

.contact-empty {
  color: var(--text-muted);
  font-style: italic;
}

.edit-btn {
  flex-shrink: 0;
}

/* Info grid */
.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.info-card {
  display: flex;
  flex-direction: column;
  gap: 18px;
  min-height: 240px;
}

.card-head {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 14px;
  border-bottom: 1px solid var(--border-light);
}

.head-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 20px;
  box-shadow: var(--shadow-sm);
}

.head-icon-info {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.head-icon-job {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.head-icon-security {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-title {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
}

.info-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 14px;
  flex: 1;
}

.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  font-size: 14px;
}

.info-label {
  color: var(--text-secondary);
  flex-shrink: 0;
}

.info-value {
  color: var(--text-primary);
  font-weight: 500;
  text-align: right;
  word-break: break-all;
}

/* Action item (security card) */
.action-item {
  align-items: flex-start;
  padding: 12px;
  border-radius: var(--radius-sm);
  background: rgba(248, 250, 252, 0.6);
  border: 1px solid var(--border-light);
}

.action-meta {
  flex: 1;
  min-width: 0;
}

.action-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.action-hint {
  font-size: 12px;
  color: var(--text-muted);
}

.ghost-btn {
  flex-shrink: 0;
  padding: 6px 14px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-light);
  background: var(--bg-white);
  color: var(--color-primary);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s ease;
}

.ghost-btn:hover {
  border-color: var(--color-primary);
  background: rgba(102, 126, 234, 0.08);
  transform: translateY(-1px);
}

.ghost-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-top: auto;
  padding: 8px 0;
  background: none;
  border: none;
  color: var(--color-primary);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  align-self: flex-start;
  transition: gap 0.25s ease;
}

.ghost-link:hover {
  gap: 8px;
}

.dialog-footer {
  display: inline-flex;
  align-items: center;
  gap: 12px;
}

/* Responsive */
@media (max-width: 992px) {
  .info-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 720px) {
  .profile-header {
    flex-direction: column;
    align-items: flex-start;
    text-align: left;
  }
  .profile-name {
    font-size: 24px;
  }
  .edit-btn {
    width: 100%;
  }
}

@media (max-width: 600px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
  .person-page {
    padding: 32px 16px 48px;
  }
  .profile-card {
    padding: 24px;
  }
}
</style>
