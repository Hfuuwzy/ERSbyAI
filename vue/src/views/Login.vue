<template>
  <div class="auth-page">
    <div class="auth-bg animate-gradient"></div>
    <GlassCard class="auth-card" :liftable="false">
      <h1 class="auth-title">欢迎回来</h1>
      <p class="auth-subtitle">登录你的 85Boss 账号</p>

      <el-form ref="formRef" :model="data.form" :rules="data.rules" @keyup.enter="login">
        <el-form-item prop="username">
          <el-input
            v-model="data.form.username"
            :prefix-icon="User"
            placeholder="用户名"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="data.form.password"
            :prefix-icon="Lock"
            type="password"
            placeholder="密码"
            size="large"
            show-password
          />
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="data.form.role" placeholder="请选择角色" size="large" style="width: 100%">
            <el-option value="ADMIN" label="管理员" />
            <el-option value="EMPLOY" label="企业" />
            <el-option value="USER" label="用户" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <GradientButton class="submit-btn" @click="login">登 录</GradientButton>
        </el-form-item>
      </el-form>

      <div class="auth-footer">
        <span>还没有账号？</span>
        <router-link to="/register">立即注册</router-link>
      </div>
    </GlassCard>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue"
import { User, Lock } from "@element-plus/icons-vue"
import request from "@/utils/request.js"
import { ElMessage } from "element-plus"
import router from "@/router/index.js"
import GlassCard from "@/components/GlassCard.vue"
import GradientButton from "@/components/GradientButton.vue"

const data = reactive({
  form: {},
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ],
    role: [
      { required: true, message: '请选择角色', trigger: 'change' }
    ]
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('登录成功')
          localStorage.setItem('xm-user', JSON.stringify(res.data))
          if (res.data.role === 'EMPLOY') {
            router.push('/manager/position')
          } else if (res.data.role === 'ADMIN') {
            router.push('/manager/home')
          } else {
            router.push('/front/home')
          }
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.auth-page {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  overflow: hidden;
}

.auth-bg {
  position: absolute;
  inset: 0;
  background: var(--gradient-hero);
  z-index: 0;
}

.auth-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  padding: 40px;
}

.auth-title {
  margin: 0 0 8px;
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  text-align: center;
}

.auth-subtitle {
  margin: 0 0 32px;
  font-size: 14px;
  color: var(--text-secondary);
  text-align: center;
}

.submit-btn {
  width: 100%;
  padding: 14px 24px;
  font-size: 16px;
}

.auth-footer {
  margin-top: 8px;
  text-align: center;
  font-size: 14px;
  color: var(--text-secondary);
}

.auth-footer a {
  color: var(--color-primary);
  font-weight: 600;
  text-decoration: none;
  margin-left: 4px;
  transition: color 0.2s ease;
}

.auth-footer a:hover {
  color: var(--color-secondary);
}

:deep(.el-input__wrapper) {
  border-radius: var(--radius-sm);
  background: rgba(255, 255, 255, 0.7);
  box-shadow: 0 0 0 1px var(--border-light) inset;
  transition: box-shadow 0.2s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--color-primary) inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px var(--color-primary) inset !important;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: var(--radius-sm);
}
</style>
