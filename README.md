# 85Boss 智能招聘平台

[![GitHub](https://img.shields.io/github/v/release/Hfuuwzy/ERSbyAI)](https://github.com/Hfuuwzy/ERSbyAI)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.4-brightgreen)](https://vuejs.org/)

全栈智能招聘平台，集成 AI 简历筛选 + 协同过滤推荐算法。

## ✨ 功能特性

- 🤖 **AI 简历筛选** - 使用 kimi-k2.6 进行智能简历评分和匹配
- 🎯 **职位推荐** - 基于协同过滤算法的个性化职位推荐
- 📝 **富文本简历编辑器** - 支持 WangEditor 富文本编辑
- 📊 **数据可视化** - ECharts 实现的数据看板
- 🔐 **JWT 认证** - 安全的用户认证机制
- 🎨 **响应式 UI** - Element Plus 组件库

## 🛠️ 技术栈

**后端：**
- Spring Boot 3.3
- MyBatis + MySQL
- JWT 认证
- OpenAI 兼容 API (kimi-k2.6)

**前端：**
- Vue 3.4 + Vite
- Element Plus
- Axios
- WangEditor
- ECharts

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+

### 1. 克隆项目

```bash
git clone https://github.com/Hfuuwzy/ERSbyAI.git
cd ERSbyAI
```

### 2. 数据库初始化

```bash
# 在 MySQL 中执行
mysql -u root -p < xm-job.sql
```

### 3. 后端配置

```bash
cd springboot/src/main/resources

# 复制开发配置模板
cp application-dev.yml.example application-dev.yml

# 编辑配置（填入你的 API Key）
# - ai.api-key: 你的 kimi API Key
# - spring.datasource.password: 数据库密码
```

**AI 配置支持两种方式：**

**方式 A：配置文件（推荐开发）**
```yaml
# application-dev.yml
ai:
  base-url: https://kimi.a7m.com.cn/v1
  api-key: your_api_key_here
  model: kimi-k2.6
```

**方式 B：环境变量（推荐生产）**
```bash
export KIMI_API_KEY=your_api_key_here
export SPRING_PROFILES_ACTIVE=dev
```

### 4. 启动后端

**方式 1：IDE（推荐开发）**
- 直接用 IDEA / Cursor 打开项目
- 运行 `SpringbootApplication.java`
- 端口：9090

**方式 2：命令行**
```bash
cd springboot
mvn spring-boot:run
```

### 5. 前端配置

```bash
cd vue
npm install
npm run dev
```
- 端口：5173
- 自动打开浏览器：http://localhost:5173

### 6. 访问系统

- **前台（求职者）**：http://localhost:5173/
- **后台（企业/管理员）**：http://localhost:5173/manager
- **后端 API**：http://localhost:9090

**测试账号：**
- 管理员：admin / admin
- 其他：需自行注册

## 📁 项目结构

```
ERSbyAI/
├── springboot/              # 后端项目
│   ├── src/main/java/com/example/
│   │   ├── controller/     # REST API
│   │   ├── service/        # 业务逻辑
│   │   ├── mapper/         # MyBatis DAO
│   │   ├── entity/         # 实体类
│   │   └── utils/          # 工具类
│   └── src/main/resources/
│       ├── application.yml           # 主配置
│       ├── application-dev.yml       # 开发配置（不提交）
│       ├── application-dev.yml.example  # 开发配置模板
│       └── mapper/           # MyBatis XML
├── vue/                     # 前端项目
│   ├── src/
│   │   ├── views/           # 页面组件
│   │   │   ├── front/       # 前台页面
│   │   │   └── manager/     # 后台页面
│   │   ├── utils/           # 工具函数
│   │   └── router/          # 路由配置
│   └── public/
└── xm-job.sql               # 数据库初始化脚本
```

## 🔧 配置说明

### AI 服务配置

项目已从 DeepSeek 切换到 **kimi-k2.6**，需要：

1. **申请 API Key**：从第三方服务商购买 kimi API Key
2. **配置 base-url**：`https://kimi.a7m.com.cn/v1`
3. **选择模型**：`kimi-k2.6`

### 多环境配置

| 环境 | 配置文件 | 说明 |
|------|----------|------|
| 默认 | `application.yml` | 公共配置，所有环境共享 |
| 开发 | `application-dev.yml` | 开发环境，本地配置，**不提交 Git** |
| 生产 | `application-prod.yml` | 生产环境配置 |

**新建开发环境：**
```bash
cp springboot/src/main/resources/application-dev.yml.example \
   springboot/src/main/resources/application-dev.yml
# 然后编辑填入你的配置
```

## 📖 文档

- [AI Coding 工作流规范](./docs/AI_CODING_WORKFLOW.md)
- [Git 工作流程指南](./GIT_WORKFLOW.md)

## 🤝 贡献

项目的升级通过 AI Coding 方式进行，详细工作流程见 [AI_CODING_WORKFLOW.md](./docs/AI_CODING_WORKFLOW.md)。

## 📝 更新日志

### 2026-05-16
- ✅ 切换 AI 服务：DeepSeek → kimi-k2.6
- ✅ API Key 从硬编码迁移到配置文件
- ✅ 重构 AiUtil：volcengine SDK → okhttp + gson
- ✅ 添加多环境配置支持

## 📄 许可证

[MIT](LICENSE)

---

**⭐ 如果项目对你有帮助，请给个 Star！**
