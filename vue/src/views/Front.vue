<template>
    <div class="front-layout">
        <div class="front-notice">
            <el-icon>
                <Bell/>
            </el-icon>
            公告：{{ data.top }}
        </div>
        <div class="front-header">
            <div class="front-header-left">
                <img src="@/assets/imgs/logo.png" alt="">
                <div class="title" @click="router.push('/front/home')">85Boss</div>
            </div>
            <div class="front-header-center">
                <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
                    <el-menu-item index="/front/home">首页</el-menu-item>
                    <el-menu-item index="/front/collect">我的收藏</el-menu-item>
                    <el-menu-item index="/front/resume">我的简历</el-menu-item>
                    <el-menu-item index="/front/submit">我的投递</el-menu-item>
                    <el-menu-item index="/front/person">个人中心</el-menu-item>
                </el-menu>
            </div>
            <div class="front-header-right">
                <div v-if="!data.user.id">
                    <el-button @click="router.push('/login')">登录</el-button>
                    <el-button @click="router.push('/register')">注册</el-button>
                </div>
                <div v-else>
                    <el-dropdown style="cursor: pointer; height: 50px">
                        <div style="display: flex; align-items: center">
                            <img style="width: 35px; height: 35px; border-radius: 50%;border: 2px solid #34daa9"
                                 :src="data.user.avatar" alt="">
                            <span style="margin-left: 5px;color: white">{{ data.user.name }}</span>
                            <el-icon>
                                <arrow-down/>
                            </el-icon>
                        </div>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </div>
        </div>

        <div class="main-body">
            <RouterView @updateUser="updateUser"/>
        </div>

        <div class="front-footer" style="background-color: #313438">
            <div
                style="width: 70%; margin: 0 auto; line-height: 50px; height: 50px; font-size: 14px; color: #93999F; text-align: center;">
                <span style="color: #FFFFFFCC">友情链接：</span>
                <span style="margin-left: 10px"><a style="margin-left: 10px; color: #93999F"
                                                   href="https://www.baidu.com/" target="_blank">百度一下</a></span>

                <span style="margin-left: 10px"><a style="margin-left: 10px; color: #93999F"
                                                   href="https://hfuuwzy.github.io/" target="_blank">我的博客</a></span>
            </div>
            <div style="text-align: center; line-height: 30px; font-size: 13px; margin-bottom: 10px; color: #93999F">
                Copyright ©2024 www.baidu.cn 版权所有 <a style="margin-left: 10px; color: #93999F"
                                                         href="https://beian.miit.gov.cn/" target="_blank">皖ICP备
                81242841号-1</a>
            </div>
        </div>

    </div>
</template>

<script setup>
import router from "@/router/index.js";
import {reactive} from "vue";
import request from "@/utils/request.js";
import {Bell} from "@element-plus/icons-vue";

const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    top: '',
    noticeData: []
})

const logout = () => {
    localStorage.removeItem('xm-user')
    router.push('/login')
}

const updateUser = () => {
    data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}

const loadNotice = () => {
    request.get('/notice/selectAll').then(res => {
        data.noticeData = res.data
        let i = 0
        if (data.noticeData && data.noticeData.length) {
            data.top = data.noticeData[0].content
            setInterval(() => {
                data.top = data.noticeData[i].content
                i++
                if (i === data.noticeData.length) {
                    i = 0
                }
            }, 2500)
        }
    })
}
loadNotice()
</script>

<style scoped>
@import "@/assets/css/front.css";
/*html, body {*/
/*    height: 100%;*/
/*    margin: 0;*/
/*}*/

.front-layout {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    background-color:#F6F6F8;
}

.main-body {
    flex: 1;
    padding: 20px 0; /* 让内容区撑满剩余高度，footer自然沉底 */

}

.front-footer {
    background: #313438;
    color: #93999F;
    padding: 20px 0;
}
</style>