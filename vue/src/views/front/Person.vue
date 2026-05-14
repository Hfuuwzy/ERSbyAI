<template>
    <div class="person-container">
        <div class="card person-card">
            <div class="card-header">
                <div class="header-title">个人资料</div>
                <el-button type="primary" link @click="changePasswordInit">
                    <el-icon style="margin-right: 3px"><Lock /></el-icon>修改密码
                </el-button>
            </div>
            
            <div class="form-wrapper">
                <el-form ref="user" :model="data.user" label-width="70px" class="person-form">
                    <div class="avatar-section">
                        <el-upload
                            :action="baseUrl + '/files/upload'"
                            :on-success="handleFileUpload"
                            :show-file-list="false"
                            class="avatar-uploader"
                        >
                            <div class="avatar-box">
                                <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar"/>
                                <el-icon v-else class="avatar-uploader-icon"><Plus/></el-icon>
                                <div class="avatar-mask">
                                    <el-icon><Camera /></el-icon>
                                    <span class="mask-text">更换头像</span>
                                </div>
                            </div>
                        </el-upload>
                        <div class="username-text">{{ data.user.username }}</div>
                    </div>

                    <el-form-item prop="username" label="用户名">
                        <el-input disabled v-model="data.user.username" prefix-icon="User" placeholder="请输入用户名"></el-input>
                    </el-form-item>
                    <el-form-item prop="name" label="姓名">
                        <el-input v-model="data.user.name" prefix-icon="Postcard" placeholder="请输入姓名"></el-input>
                    </el-form-item>
                    <el-form-item prop="phone" label="电话">
                        <el-input v-model="data.user.phone" prefix-icon="Iphone" placeholder="请输入电话"></el-input>
                    </el-form-item>
                    <el-form-item prop="email" label="邮箱">
                        <el-input v-model="data.user.email" prefix-icon="Message" placeholder="请输入邮箱"></el-input>
                    </el-form-item>
                    
                    <div class="form-footer">
                        <el-button type="primary" class="save-btn" @click="update">保存修改</el-button>
                    </div>
                </el-form>
            </div>
        </div>

        <el-dialog title="修改密码" v-model="data.formVisible" width="400px" destroy-on-close center>
            <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="80px" style="padding: 20px 10px">
                <el-form-item label="原密码" prop="password">
                    <el-input v-model="data.user.password" placeholder="请输入原密码" show-password prefix-icon="Key"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                    <el-input v-model="data.user.newPassword" placeholder="请输入新密码" show-password prefix-icon="Lock"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input v-model="data.user.confirmPassword" placeholder="请确认新密码" show-password prefix-icon="Check"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="data.formVisible = false">取 消</el-button>
                    <el-button type="primary" @click="changePassword">确 定</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const formRef = ref()

const baseUrl = import.meta.env.VITE_BASE_URL
const validatePass = (rule, value, callback) => {
    if (!value) {
        callback(new Error('请确认密码'))
    } else {
        if (value !== data.user.newPassword) {
            callback(new Error("确认密码跟原密码不一致!"))
        }
        callback()
    }
}
const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    formVisible: false,
    rules: {
        password: [
            {required: true, message: '请输入原密码', trigger: 'blur'},
        ],
        newPassword: [
            {required: true, message: '请输入新密码', trigger: 'blur'},
        ],
        confirmPassword: [
            {validator: validatePass, trigger: 'blur'}
        ]
    }
})

const handleFileUpload = (res) => {
    data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])
const update = () => {
    if (data.user.role === 'USER') {
        request.put('/user/update', data.user).then(res => {
            if (res.code === '200') {
                ElMessage.success('保存成功')
                localStorage.setItem('xm-user', JSON.stringify(data.user))
                emit('updateUser')
            } else {
                ElMessage.error(res.msg)
            }
        })
    }
}
const changePasswordInit = () => {
    data.formVisible = true;
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
</script>

<style scoped>
.person-container {
    width: 100%;
    padding: 20px;
    display: flex;
    justify-content: center;
}

.person-card {
    width: 100%;
    max-width: 600px; /* 限制最大宽度 */
    padding: 0;
    overflow: hidden;
}

.card-header {
    padding: 15px 20px;
    border-bottom: 1px solid #f0f0f0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fafafa;
}

.header-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
}

.form-wrapper {
    padding: 30px 40px;
}

.avatar-section {
    text-align: center;
    margin-bottom: 30px;
}

.avatar-uploader {
    display: inline-block;
}

.avatar-box {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    border: 2px solid #e0e0e0;
    overflow: hidden;
    position: relative;
    cursor: pointer;
    background: #f8f8f8;
    transition: all 0.3s;
}

.avatar-box:hover {
    border-color: #409EFF;
}

.avatar {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}

/* 头像悬停遮罩 */
.avatar-mask {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    color: #fff;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    opacity: 0;
    transition: opacity 0.3s;
}

.avatar-box:hover .avatar-mask {
    opacity: 1;
}

.mask-text {
    font-size: 12px;
    margin-top: 5px;
}

.username-text {
    margin-top: 10px;
    font-size: 18px;
    font-weight: 500;
    color: #333;
}

.person-form .el-form-item {
    margin-bottom: 25px;
}

.form-footer {
    text-align: center;
    margin-top: 30px;
}

.save-btn {
    width: 100%;
    height: 40px;
    font-size: 16px;
    letter-spacing: 2px;
}
</style>