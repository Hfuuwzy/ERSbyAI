# 85Boss 智能招聘平台

[![GitHub](https://img.shields.io/github/v/release/Hfuuwzy/ERSbyAI)](https://github.com/Hfuuwzy/ERSbyAI)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.4-brightgreen)](https://vuejs.org/)
[![AI Powered](https://img.shields.io/badge/AI-kimi--k2.6-blue)](https://kimi.moonshot.cn/)

> 🚀 **AI驱动开发** | 全栈智能招聘平台，集成 AI 简历筛选 + 协同过滤推荐算法
> 
> 本项目采用 **AI Coding** 方式开发，利用大模型辅助完成架构设计、代码实现、Bug修复和文档编写。

---

## 🤖 AI Coding 特点

本项目是 **AI Coding 实践案例**，展示了如何利用大语言模型进行全栈开发：

- **🧠 智能架构设计** - AI辅助设计系统架构、数据库模型、API接口
- **⚡ 快速迭代开发** - 通过自然语言描述需求，AI生成可运行代码
- **🔍 智能Bug修复** - 利用AI分析错误日志，定位并修复问题
- **📝 自动化文档** - AI根据代码自动生成技术文档和更新日志
- **🎨 UI/UX优化** - AI提供界面设计建议和实现方案

**开发工具链：**
- Cursor (VS Code + AI)
- kimi-k2.6 (代码生成与审查)
- GitHub (版本控制与协作)

---

## ✨ 核心功能特性

### 🤖 AI 智能模块
- **AI 简历筛选** - 使用 kimi-k2.6 进行智能简历评分（0-100分）和匹配度分析
- **AI 审核建议** - 自动生成"适合/不适合/一般"的审核结论及改进建议
- **AI 职位推荐** - 基于协同过滤算法的个性化职位推荐（UserCF + ItemCF + ContentBased混合策略）
- **AI 内容生成** - 智能生成职位描述，支持百度富文本格式

### 👤 三角色权限系统
| 角色 | 权限 | 功能 |
|------|------|------|
| 👨‍💼 管理员 | ADMIN | 全平台管理、数据统计看板、用户审核 |
| 🏢 企业 | EMPLOY | 发布职位、AI简历筛选、批量操作 |
| 👤 求职者 | USER | 投递简历、编辑简历、查看推荐 |

### 📝 简历系统
- **富文本编辑器** - WangEditor 支持图文混排、表格等复杂格式
- **PDF导出** - 3种精美模板，支持自定义导出
- **自动保存** - 60秒间隔自动保存，防止数据丢失
- **草稿恢复** - 智能提示恢复未保存的草稿
- **AI评分** - 实时AI评分和审核建议展示

### 🎯 推荐系统
- **混合推荐策略** - UserCF(50%) + ItemCF(30%) + ContentBased(20%)
- **冷启动处理** - 新用户基于内容推荐，解决冷启动问题
- **实时计算** - 基于用户行为的实时推荐更新

### 📊 数据可视化
- **企业数据看板** - 简历投递转化率、AI评分分布、热门职位TOP10
- **统计图表** - ECharts 实现的饼图、柱状图、折线图
- **AI筛选统计** - 平均分、优秀率、适合率实时统计

---

## 🛠️ 技术栈

### 后端技术
- **Spring Boot 3.3** - 企业级Java框架
- **MyBatis + MySQL** - ORM框架与关系型数据库
- **JWT 认证** - 安全的Token认证机制
- **kimi-k2.6 API** - OpenAI兼容格式的AI服务
- **Maven** - 项目构建工具

### 前端技术
- **Vue 3.4 + Vite** - 现代化前端框架与构建工具
- **Element Plus** - 企业级UI组件库
- **Axios** - HTTP客户端
- **WangEditor** - 富文本编辑器
- **ECharts** - 数据可视化图表库

### AI 技术
- **kimi-k2.6** - 大语言模型，用于简历评分和内容生成
- **协同过滤算法** - User-Based + Item-Based 混合推荐
- **内容推荐** - 基于简历和职位内容的相似度计算

---

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

---

## 📁 项目结构

```
ERSbyAI/
├── springboot/              # 后端项目
│   ├── src/main/java/com/example/
│   │   ├── controller/     # REST API (10个控制器)
│   │   ├── service/        # 业务逻辑
│   │   ├── mapper/         # MyBatis DAO
│   │   ├── entity/         # 实体类 (13个)
│   │   └── utils/          # 工具类
│   │       ├── AiUtil.java         # AI调用工具
│   │       ├── UserCF.java         # 用户协同过滤
│   │       ├── ItemCF.java         # 物品协同过滤
│   │       └── ContentBased.java   # 内容推荐
│   └── src/main/resources/
│       ├── application.yml           # 主配置
│       ├── application-dev.yml       # 开发配置
│       ├── application-dev.yml.example  # 配置模板
│       └── mapper/           # MyBatis XML
├── vue/                     # 前端项目
│   ├── src/
│   │   ├── views/           # 页面组件
│   │   │   ├── front/       # 前台页面 (9个)
│   │   │   └── manager/     # 后台页面 (13个)
│   │   ├── utils/           # 工具函数
│   │   └── router/          # 路由配置 (24个路由)
│   └── public/
├── files/                   # 上传文件存储目录
├── docs/                    # 项目文档
│   ├── designs/             # 设计文档
│   └── AI_CODING_WORKFLOW.md # AI Coding工作流
├── xm-job.sql               # 数据库初始化脚本
└── README.md                # 项目说明
```

---

## 🔧 配置说明

### AI 服务配置
项目使用 **kimi-k2.6** 作为AI引擎，需要：
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

---

## 📖 文档

- [AI Coding 工作流规范](./docs/AI_CODING_WORKFLOW.md)
- [Git 工作流程指南](./GIT_WORKFLOW.md)
- [Phase 5 企业端优化设计](./docs/designs/phase5-enterprise-optimization.md)

---

## 📝 更新日志

### 2026-05-23 (最新)
- ✅ **Phase 5：企业端优化与AI推荐增强**
  - 统一简历管理界面（SubmitManager + AiFilterManager分离）
  - 批量操作功能（批量状态更新）
  - AI筛选页面布局优化（统计卡片、评分圆环、热查询）
  - 混合推荐算法（UserCF + ItemCF + ContentBased）
  - 图片显示修复（Content-Type智能识别）
  - Token过期自动跳转登录页

### 2025-05-20
- ✅ Phase 4：简历功能增强
  - PDF导出功能（3种精美模板）
  - 自动保存（60秒间隔，localStorage存储）
  - 草稿恢复（重新进入编辑页时提示恢复）
  - 保存状态指示器

### 2026-05-16
- ✅ 切换 AI 服务：DeepSeek → kimi-k2.6
- ✅ API Key 从硬编码迁移到配置文件
- ✅ 重构 AiUtil：volcengine SDK → okhttp + gson
- ✅ 添加多环境配置支持

---

## 🤝 贡献

项目的升级通过 **AI Coding** 方式进行，详细工作流程见 [AI_CODING_WORKFLOW.md](./docs/AI_CODING_WORKFLOW.md)。

**AI Coding 最佳实践：**
1. 使用自然语言描述需求
2. AI生成代码并解释逻辑
3. 人工审查和测试
4. 迭代优化直至满足需求

---

## 📄 许可证

[MIT](LICENSE)

---

**⭐ 如果项目对你有帮助，请给个 Star！**

**🤖 本项目由 AI 辅助开发，展示了 AI Coding 在实战项目中的应用。**