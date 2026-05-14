# AGENTS.md - 85Boss智能招聘平台

## 项目概览

全栈智能招聘平台，集成AI简历筛选 + 协同过滤推荐算法。

**技术栈**：Spring Boot 3.3 + Vue 3.4 + MySQL + DeepSeek AI

---

## 快速开始

### 启动后端 (端口 9090)

```bash
cd springboot
# 方式1: IDE运行 SpringbootApplication.java
# 方式2: Maven
mvn spring-boot:run
```

**数据库配置** (`springboot/src/main/resources/application.yml`):
- 数据库: `xm-job` (MySQL)
- 用户名: `root`
- 密码: `123456`
- SQL初始化: `xm-job.sql` (根目录)

### 启动前端 (端口 5173)

```bash
cd vue
npm install
npm run dev
```

---

## 架构约定

### 后端架构 (`springboot/src/main/java/com/example/`)

```
controller/  → REST API (10个控制器，统一返回Result)
service/     → 业务逻辑
mapper/      → MyBatis数据访问 (XML在 resources/mapper/)
entity/      → 实体类 (13个)
utils/       → 工具类
  - AiUtil.java    → DeepSeek AI调用 (VolcEngine ARK SDK)
  - UserCF.java    → 协同过滤推荐算法
  - CoreMath.java  → 余弦相似度计算
  - TokenUtils.java → JWT工具
```

**API规范**：所有控制器遵循统一CRUD模式
- `POST /xxx/add` — 新增
- `PUT /xxx/update` — 修改
- `DELETE /xxx/delete/{id}` — 删除
- `GET /xxx/selectPage` — 分页查询

### 前端架构 (`vue/src/`)

```
views/
  ├── Login.vue / Register.vue    # 登录注册
  ├── Manager.vue                  # 后台布局组件
  ├── Front.vue                    # 前台布局组件
  ├── manager/                     # 后台页面 (13个)
  │   ├── Home.vue                # 数据看板 (Echarts)
  │   ├── AiFilter.vue            # AI简历筛选
  │   └── ...
  └── front/                       # 前台页面 (9个)
      ├── Home.vue                # 首页 (推荐+热招)
      ├── ResumeEdit.vue          # 简历编辑器 (WangEditor)
      └── ...
utils/request.js                  # Axios封装 (JWT自动注入)
router/index.js                   # 路由配置 (24个路由)
```

**重要**：前端使用 `localStorage.getItem('xm-user')` 存储用户信息，不是Pinia/Vuex。

---

## 角色权限

| 角色 | 标识 | 权限 |
|------|------|------|
| 管理员 | `ADMIN` | 全平台管理、数据统计看板 |
| 企业 | `EMPLOY` | 发布职位、AI简历筛选 |
| 求职者 | `USER` | 投递简历、编辑简历 |

---

## 特殊依赖说明

### AI能力 (DeepSeek R1)

**文件**: `springboot/src/main/java/com/example/utils/AiUtil.java`

- 使用字节跳动火山引擎 ARK SDK
- 模型: `deepseek-r1-250120`
- ⚠️ **硬编码API Key** (安全漏洞，需移到配置)

### 协同过滤推荐

**文件**: `springboot/src/main/java/com/example/utils/UserCF.java`

- 算法: User-Based Collaborative Filtering
- 相似度: 余弦相似度
- 用途: 首页"精选岗位"推荐

### 富文本编辑器

**前端**: `@wangeditor/editor-for-vue`
- 用于简历编辑 (`ResumeEdit.vue`)

### 数据可视化

**前端**: `echarts`
- 首页数据看板: 饼图、柱状图、折线图

---

## 开发注意事项

### 安全事项 🔒

1. **AI API Key硬编码** (`AiUtil.java` 第21行)
   ```java
   String apiKey = "eb9000bd-..."  // ❌ 硬编码
   ```
   应改为: `application.yml` 配置 + `@Value` 注入

2. **前端路由跳转使用 `location.href`** (非SPA方式)
   ```javascript
   // vue/src/views/front/Home.vue
   location.href = '/front/search?name=' + data.name  // ❌
   // 建议: router.push('/front/search?name=' + data.name)
   ```

### 状态管理

- ❌ 无Pinia/Vuex
- ✅ 使用 `localStorage` 存储用户信息
- ✅ 全局请求头自动注入JWT: `request.js` 第15行

### 样式规范

- Element Plus 按需自动导入 (vite.config.js)
- Sass主题定制: `@/assets/css/index.scss`
- 大量内联style存在于模板中 (待优化)

---

## 数据库表

核心表 (13个):
- `admin` / `employ` / `user` — 用户表
- `position` — 职位
- `resume` / `edu_exp` / `work_exp` / `pro_exp` — 简历及经历
- `submit` — 投递记录 (含 `ai_score`, `ai_review` AI评估字段)
- `collect` — 收藏
- `advertise` — 广告位 (6个位置)
- `industry` — 行业分类
- `notice` — 系统公告

---

## 构建部署

### 前端构建

```bash
cd vue
npm run build  # 输出到 dist/
```

### 后端构建

```bash
cd springboot
mvn clean package  # 输出到 target/*.jar
```

---

## 测试账号

导入 `xm-job.sql` 后:
- **管理员**: admin / admin
- **企业/求职者**: 需自行注册

---

## 常见任务

### 添加新API

1. Controller → 添加@RestController方法
2. Service → 实现业务逻辑
3. Mapper.java → 定义接口
4. Mapper.xml → 写SQL
5. `Result.success(data)` 返回

### 添加新页面

1. `views/manager/` 或 `views/front/` 新建 .vue
2. `router/index.js` 添加路由
3. 父级布局: Manager.vue (后台) / Front.vue (前台)
4. 使用 Element Plus 组件

---

*创建于: 2025年 | 项目: 85Boss智能招聘平台*
