# 85Boss智能招聘系统 - 升级总体规划

> **目标**：通过AI Coding进行系统性升级，引入专业Git工作流，将项目从原型阶段推进到生产就绪阶段。

**当前状态：** 基础功能完整，代码质量良好，可直接构建运行。
**项目规模：** 后端62个Java文件 + 前端24个Vue页面 + 13张数据库表

---

## 📊 问题诊断汇总

### ✅ 已验证正常
| 项目 | 状态 | 说明 |
|------|------|------|
| 后端编译 | ✅ | `mvn clean compile` SUCCESS |
| 前端构建 | ✅ | `npm run build` SUCCESS，无错误 |
| 数据库脚本 | ✅ | `xm-job.sql` 存在，表结构完整 |
| 依赖配置 | ✅ | pom.xml和package.json配置合理 |

### ⚠️ 待修复问题

#### Critical（必须修复）
1. **硬编码API Key** (`AiUtil.java:21`)
   - 当前：火山引擎API Key直接写在代码中
   - 风险：代码泄露 = API Key泄露
   - 修复：移至 `application.yml` + 环境变量

#### Warning（建议修复）
2. **Lombok警告** (`Employ.java`)
   - 警告：生成equals/hashCode未调用父类
   - 修复：添加 `@EqualsAndHashCode(callSuper=false)`

3. **前端路由方式** (`Home.vue`)
   - 当前：使用 `location.href` 跳转（非SPA方式）
   - 建议：使用 `router.push()` 保持SPA体验

4. **缺少安全配置**
   - 无CORS配置
   - 无请求限流
   - 无SQL注入防护（依赖MyBatis参数绑定）

### 💡 优化建议
- API响应统一包装（已完成✅）
- JWT过期处理
- 前端Pinia状态管理（当前用localStorage）
- 图片上传大小限制

---

## 🚀 升级路线图（5个Phase）

### Phase 1: 环境修复与安全加固 ⏱️ 1-2天
**目标**：解决Critical问题，建立安全的开发环境

| 任务 | 优先级 | 工作量 | 产出 |
|------|--------|--------|------|
| 1.1 迁移API Key到配置 | Critical | 2h | `application.yml` + 环境变量配置 |
| 1.2 添加CORS安全配置 | High | 1h | `CorsConfig.java` |
| 1.3 修复Lombok警告 | Low | 30m | 3个实体类注解修复 |
| 1.4 创建环境配置文件模板 | Medium | 1h | `application-dev.yml`, `application-prod.yml` |
| 1.5 添加.gitignore强化 | Low | 30m | 排除敏感文件 |

**验收标准**：
- API Key从代码中移除
- 后端可通过环境变量配置运行
- 无编译警告

---

### Phase 2: 后端功能增强 ⏱️ 3-5天
**目标**：AI能力升级、性能优化、代码质量提升

| 任务 | 优先级 | 工作量 | 技术方案 |
|------|--------|--------|----------|
| 2.1 AI能力升级 | High | 2d | 支持多轮对话、增加Prompt模板管理 |
| 2.2 简历PDF解析 | High | 2d | 集成Apache PDFBox或Tika |
| 2.3 推荐算法优化 | Medium | 1d | UserCF + 基于内容的混合推荐 |
| 2.4 添加全局异常处理 | Medium | 1d | `@ControllerAdvice` + 统一错误响应 |
| 2.5 接口文档（OpenAPI） | Medium | 1d | SpringDoc OpenAPI |
| 2.6 单元测试覆盖 | High | 2d | JUnit 5 + Mockito，核心服务覆盖率>60% |

**验收标准**：
- AI评分功能支持更多维度
- 新用户冷启动问题改善
- API文档自动生成
- 核心服务有单元测试

---

### Phase 3: 前端UI升级 ⏱️ 3-5天
**目标**：现代化设计、响应式适配、交互优化

| 任务 | 优先级 | 工作量 | 技术方案 |
|------|--------|--------|----------|
| 3.1 设计系统规范化 | High | 1d | 统一颜色、字体、间距变量 |
| 3.2 响应式布局优化 | High | 2d | 移动端适配、断点优化 |
| 3.3 路由重构 | Medium | 1d | `router.push` 替换 `location.href` |
| 3.4 状态管理升级 | Medium | 1d | Pinia 替换 localStorage |
| 3.5 加载体验优化 | Low | 1d | Skeleton屏、懒加载 |
| 3.6 深色模式支持 | Low | 1d | Element Plus 暗色主题 |

**验收标准**：
- 移动端可正常使用
- 所有路由使用Vue Router
- 状态管理集中化
- 加载等待有反馈

---

### Phase 4: 新功能开发 ⏱️ 5-7天
**目标**：增强产品价值，添加企业级功能

| 任务 | 优先级 | 工作量 | 描述 |
|------|--------|--------|------|
| 4.1 实时消息通知 | High | 2d | WebSocket + 站内信系统 |
| 4.2 数据可视化增强 | Medium | 2d | ECharts 高级图表、数据导出 |
| 4.3 批量操作功能 | Medium | 1d | 批量导入职位、批量导出简历 |
| 4.4 搜索功能增强 | Medium | 2d | Elasticsearch集成、全文搜索 |
| 4.5 操作日志审计 | Low | 1d | 关键操作记录、可追溯 |

**验收标准**：
- 投递状态变化有实时通知
- 管理员可查看操作日志
- 支持批量导入导出

---

### Phase 5: 工程化与DevOps ⏱️ 3-5天
**目标**：容器化、CI/CD、自动化测试

| 任务 | 优先级 | 工作量 | 产出 |
|------|--------|--------|------|
| 5.1 Docker容器化 | High | 1d | `Dockerfile` + `docker-compose.yml` |
| 5.2 GitHub Actions CI/CD | High | 2d | `.github/workflows/ci.yml` |
| 5.3 代码质量门禁 | Medium | 1d | Spotless + ESLint + Pre-commit hooks |
| 5.4 自动化测试流水线 | Medium | 1d | 集成测试、构建验证 |
| 5.5 部署文档 | Low | 1d | 生产部署指南 |

**验收标准**：
- `docker-compose up` 一键启动
- PR自动跑测试和构建
- 代码提交前自动格式化

---

## 📁 Git工作流设计

### 分支策略：GitHub Flow（推荐）

```
main (保护分支)
  ↑
feature/ai-upgrade    ← PR → review → merge
  ↑
feature/ui-modernize  ← PR → review → merge
  ↑
hotfix/api-key        ← PR → review → merge (urgent)
```

### 工作流规范

#### 1. 分支命名
```bash
feature/<描述>     # 新功能
fix/<描述>         # Bug修复
refactor/<描述>    # 重构
docs/<描述>        # 文档
chore/<描述>       # 杂项
```

#### 2. 提交规范（Conventional Commits）
```bash
# 格式
<type>(<scope>): <description>

# 示例
feat(ai): add resume PDF parser
fix(auth): resolve JWT expiration issue
refactor(service): extract common logic to base class
docs(readme): update setup guide
```

#### 3. PR模板
```markdown
## 变更描述
- 修复/添加了什么功能
- 为什么需要这个变更

## 测试情况
- [ ] 本地测试通过
- [ ] 单元测试通过
- [ ] 手动测试步骤：

## 破坏性变更
- [ ] 无
- [ ] 有（请说明）
```

#### 4. 保护规则（GitHub Branch Protection）
- main分支需要PR才能合并
- PR需要至少1个Review
- CI检查必须通过
- 禁止直接push到main

---

## 🛠️ AI Coding协作模式

### 工作模式

#### 模式A：任务驱动（推荐）
1. 你：提出功能需求或Bug修复
2. AI：分析问题 → 创建Plan → 在Worktree中实现
3. AI：提交PR → 代码Review → 合并

#### 模式B：探索驱动
1. AI：分析代码库 → 发现改进点
2. AI：提出建议 → 你确认优先级
3. AI：按计划执行

### 协作原则
1. **Plan优先**：复杂任务先写Plan，再执行
2. **Worktree隔离**：每个功能在独立worktree中开发
3. **Review把关**：AI生成代码后Review再合并
4. **小步快跑**：任务拆小，频繁提交

---

## 📋 下一步行动清单

### 立即执行（今天）
- [ ] 确认升级规划
- [ ] 选择Phase 1的任务开始

### 本周目标
- [ ] 完成Phase 1（环境修复）
- [ ] 启动Phase 2（后端增强）

### 月度目标
- [ ] 完成Phase 1-3（核心升级）
- [ ] 建立稳定的Git工作流

---

## 💡 技术决策记录

| 决策 | 选择 | 原因 |
|------|------|------|
| Git工作流 | GitHub Flow | 团队规模小，需要快速迭代 |
| AI升级方案 | Prompt模板 + 多轮对话 | 成本低，效果好 |
| 推荐算法 | 混合推荐 | 解决新用户冷启动 |
| 状态管理 | Pinia | Vue3官方推荐 |
| 容器化 | Docker Compose | 开发环境一致性 |

---

**规划日期**：2026-05-15  
**下次Review**：完成Phase 1后

---

*本文档由AI Coding生成，可随项目进展更新*
