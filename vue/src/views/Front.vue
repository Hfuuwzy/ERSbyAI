<template>
    <div class="front-layout">
        <!-- Notice Bar -->
        <div v-if="data.top" class="notice-bar">
            <el-icon>
                <Bell/>
            </el-icon>
            <span>{{ data.top }}</span>
        </div>

        <!-- Header -->
        <header class="main-header" :class="{ 'scrolled': isScrolled }">
            <div class="header-container">
                <div class="logo" @click="router.push('/front/home')">
                    <img src="@/assets/imgs/logo.png" alt="logo"/>
                    <span>85Boss</span>
                </div>

                <nav class="desktop-nav">
                    <router-link
                        v-for="item in navItems"
                        :key="item.path"
                        :to="item.path"
                        :class="{ active: route.path === item.path }"
                    >
                        {{ item.name }}
                    </router-link>
                </nav>

                <button class="mobile-menu-btn" @click="toggleMobileMenu" aria-label="菜单">
                    <el-icon>
                        <Menu/>
                    </el-icon>
                </button>

                <div class="user-section">
                    <template v-if="!data.user.id">
                        <button class="btn-text" @click="router.push('/login')">登录</button>
                        <button class="btn-primary" @click="router.push('/register')">注册</button>
                    </template>
                    <template v-else>
                        <el-dropdown>
                            <div class="user-avatar">
                                <img :src="data.user.avatar" alt="avatar"/>
                                <span>{{ data.user.name }}</span>
                                <el-icon>
                                    <ArrowDown/>
                                </el-icon>
                            </div>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </template>
                </div>
            </div>
        </header>

        <!-- Mobile Drawer -->
        <div class="mobile-drawer" :class="{ 'open': mobileMenuOpen }" @click.self="mobileMenuOpen = false">
            <nav class="mobile-nav">
                <router-link
                    v-for="item in navItems"
                    :key="item.path"
                    :to="item.path"
                    @click="mobileMenuOpen = false"
                >
                    {{ item.name }}
                </router-link>
            </nav>
        </div>

        <!-- Main Content -->
        <main class="main-content">
            <RouterView @updateUser="updateUser"/>
        </main>

        <!-- Footer -->
        <footer class="main-footer">
            <div class="footer-container">
                <div class="footer-grid">
                    <div class="footer-col">
                        <h4>关于我们</h4>
                        <a href="#">公司介绍</a>
                        <a href="#">联系我们</a>
                        <a href="#">加入我们</a>
                    </div>
                    <div class="footer-col">
                        <h4>求职者</h4>
                        <a href="#">搜索职位</a>
                        <a href="#">我的简历</a>
                        <a href="#">投递记录</a>
                    </div>
                    <div class="footer-col">
                        <h4>企业</h4>
                        <a href="#">发布职位</a>
                        <a href="#">人才搜索</a>
                        <a href="#">企业服务</a>
                    </div>
                    <div class="footer-col">
                        <h4>联系方式</h4>
                        <p>客服电话：400-xxx-xxxx</p>
                        <p>邮箱：support@85boss.com</p>
                        <div class="social-links">
                            <a href="#">微信</a>
                            <a href="#">微博</a>
                        </div>
                    </div>
                </div>
                <div class="footer-bottom">
                    <p>Copyright ©2024 85Boss 版权所有 | 皖ICP备81242841号-1</p>
                </div>
            </div>
        </footer>
    </div>
</template>

<script setup>
import router from "@/router/index.js";
import {reactive, ref, onMounted, onBeforeUnmount} from "vue";
import {useRoute} from "vue-router";
import request from "@/utils/request.js";
import {Bell, Menu, ArrowDown} from "@element-plus/icons-vue";

const route = useRoute()

const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    top: '',
    noticeData: []
})

const navItems = [
    {path: '/front/home', name: '首页'},
    {path: '/front/collect', name: '我的收藏'},
    {path: '/front/resume', name: '我的简历'},
    {path: '/front/submit', name: '我的投递'},
    {path: '/front/person', name: '个人中心'},
]

const mobileMenuOpen = ref(false)
const isScrolled = ref(false)

const toggleMobileMenu = () => {
    mobileMenuOpen.value = !mobileMenuOpen.value
}

const handleScroll = () => {
    isScrolled.value = window.scrollY > 10
}

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

onMounted(() => {
    window.addEventListener('scroll', handleScroll, {passive: true})
    handleScroll()
})

onBeforeUnmount(() => {
    window.removeEventListener('scroll', handleScroll)
})

loadNotice()
</script>

<style scoped>
.front-layout {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    background-color: var(--bg-primary);
}

/* Notice Bar */
.notice-bar {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 8px 16px;
    background: var(--gradient-hero);
    color: #fff;
    font-size: 13px;
    line-height: 1.5;
    text-align: center;
}

.notice-bar .el-icon {
    font-size: 16px;
}

.notice-bar span {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 80%;
}

/* Main Header */
.main-header {
    position: sticky;
    top: 0;
    z-index: 1000;
    background: rgba(255, 255, 255, 0.75);
    backdrop-filter: blur(12px);
    -webkit-backdrop-filter: blur(12px);
    border-bottom: 1px solid var(--border-light);
    transition: box-shadow 0.3s ease, background 0.3s ease;
}

.main-header.scrolled {
    box-shadow: var(--shadow-md);
    background: rgba(255, 255, 255, 0.92);
}

.header-container {
    max-width: 1280px;
    margin: 0 auto;
    height: 64px;
    padding: 0 24px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 24px;
}

/* Logo */
.logo {
    display: flex;
    align-items: center;
    gap: 10px;
    cursor: pointer;
    flex-shrink: 0;
}

.logo img {
    width: 36px;
    height: 36px;
    object-fit: contain;
}

.logo span {
    font-size: 20px;
    font-weight: 700;
    background: var(--gradient-hero);
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
    letter-spacing: 0.5px;
}

/* Desktop Nav */
.desktop-nav {
    display: flex;
    align-items: center;
    gap: 8px;
    flex: 1;
    justify-content: center;
}

.desktop-nav a {
    position: relative;
    padding: 8px 16px;
    font-size: 14px;
    font-weight: 500;
    color: var(--text-secondary);
    border-radius: var(--radius-sm);
    transition: color 0.2s ease, background 0.2s ease;
}

.desktop-nav a:hover {
    color: var(--color-primary);
    background: rgba(102, 126, 234, 0.08);
}

.desktop-nav a.active {
    color: var(--color-primary);
}

.desktop-nav a.active::after {
    content: '';
    position: absolute;
    left: 50%;
    bottom: -2px;
    transform: translateX(-50%);
    width: 24px;
    height: 3px;
    border-radius: 3px;
    background: var(--gradient-button);
}

/* Mobile Menu Button */
.mobile-menu-btn {
    display: none;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    border: none;
    background: transparent;
    border-radius: var(--radius-sm);
    cursor: pointer;
    color: var(--text-primary);
    transition: background 0.2s ease;
}

.mobile-menu-btn:hover {
    background: rgba(102, 126, 234, 0.08);
}

.mobile-menu-btn .el-icon {
    font-size: 22px;
}

/* User Section */
.user-section {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-shrink: 0;
}

.btn-text {
    padding: 8px 16px;
    font-size: 14px;
    font-weight: 500;
    color: var(--text-secondary);
    background: transparent;
    border: none;
    border-radius: var(--radius-sm);
    cursor: pointer;
    transition: color 0.2s ease, background 0.2s ease;
}

.btn-text:hover {
    color: var(--color-primary);
    background: rgba(102, 126, 234, 0.08);
}

.btn-primary {
    padding: 8px 18px;
    font-size: 14px;
    font-weight: 600;
    color: #fff;
    background: var(--gradient-button);
    border: none;
    border-radius: var(--radius-sm);
    cursor: pointer;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.btn-primary:hover {
    transform: translateY(-1px);
    box-shadow: var(--shadow-lg);
}

.user-avatar {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 4px 12px 4px 4px;
    border-radius: 999px;
    background: rgba(102, 126, 234, 0.06);
    cursor: pointer;
    transition: background 0.2s ease;
}

.user-avatar:hover {
    background: rgba(102, 126, 234, 0.12);
}

.user-avatar img {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    object-fit: cover;
    border: 2px solid #fff;
}

.user-avatar span {
    font-size: 14px;
    font-weight: 500;
    color: var(--text-primary);
    max-width: 96px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.user-avatar .el-icon {
    color: var(--text-muted);
    font-size: 14px;
}

/* Mobile Drawer */
.mobile-drawer {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 999;
    background: rgba(15, 23, 42, 0.4);
    backdrop-filter: blur(4px);
    opacity: 0;
    pointer-events: none;
    transition: opacity 0.25s ease;
}

.mobile-drawer.open {
    opacity: 1;
    pointer-events: auto;
}

.mobile-nav {
    position: absolute;
    top: 64px;
    left: 0;
    right: 0;
    background: #fff;
    box-shadow: var(--shadow-md);
    padding: 12px;
    display: flex;
    flex-direction: column;
    gap: 4px;
    transform: translateY(-12px);
    transition: transform 0.25s ease;
}

.mobile-drawer.open .mobile-nav {
    transform: translateY(0);
}

.mobile-nav a {
    padding: 12px 16px;
    font-size: 15px;
    font-weight: 500;
    color: var(--text-primary);
    border-radius: var(--radius-sm);
    transition: background 0.2s ease, color 0.2s ease;
}

.mobile-nav a:hover,
.mobile-nav a.router-link-active {
    color: var(--color-primary);
    background: rgba(102, 126, 234, 0.08);
}

/* Main Content */
.main-content {
    flex: 1;
    padding: 24px 0;
}

/* Footer */
.main-footer {
    position: relative;
    background: #1e293b;
    color: #cbd5e1;
    padding: 56px 24px 24px;
}

.main-footer::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: var(--gradient-hero);
}

.footer-container {
    max-width: 1280px;
    margin: 0 auto;
}

.footer-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 40px;
    padding-bottom: 32px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.footer-col h4 {
    margin: 0 0 16px;
    font-size: 15px;
    font-weight: 600;
    color: #fff;
    letter-spacing: 0.3px;
}

.footer-col a,
.footer-col p {
    display: block;
    margin: 0 0 10px;
    font-size: 13px;
    line-height: 1.6;
    color: #94a3b8;
    transition: color 0.2s ease;
}

.footer-col a:hover {
    color: var(--color-accent);
}

.social-links {
    display: flex;
    gap: 12px;
    margin-top: 12px;
}

.social-links a {
    margin: 0;
    padding: 6px 14px;
    border: 1px solid rgba(255, 255, 255, 0.12);
    border-radius: var(--radius-sm);
    font-size: 12px;
    transition: all 0.2s ease;
}

.social-links a:hover {
    border-color: var(--color-accent);
    color: var(--color-accent);
    background: rgba(240, 147, 251, 0.08);
}

.footer-bottom {
    padding-top: 20px;
    text-align: center;
}

.footer-bottom p {
    margin: 0;
    font-size: 12px;
    color: #64748b;
}

/* Mobile Responsive */
@media (max-width: 768px) {
    .header-container {
        height: 56px;
        padding: 0 16px;
        gap: 12px;
    }

    .desktop-nav {
        display: none;
    }

    .mobile-menu-btn {
        display: flex;
    }

    .mobile-nav {
        top: 56px;
    }

    .logo span {
        font-size: 18px;
    }

    .user-avatar span {
        display: none;
    }

    .btn-text {
        padding: 6px 10px;
    }

    .btn-primary {
        padding: 6px 12px;
    }

    .footer-grid {
        grid-template-columns: repeat(2, 1fr);
        gap: 28px;
    }

    .main-footer {
        padding: 40px 20px 20px;
    }
}

@media (max-width: 480px) {
    .footer-grid {
        grid-template-columns: 1fr;
    }
}
</style>
