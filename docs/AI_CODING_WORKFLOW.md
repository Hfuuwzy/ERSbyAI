# AI Coding 工作流规范

> 本文档定义了 85Boss 智能招聘平台使用 AI Coding 进行项目升级的完整工作流规范。  
> **适用范围**：所有 AI Coding 任务、代码提交、Git 操作、PR 流程。

**仓库地址**：[https://github.com/Hfuuwzy/ERSbyAI](https://github.com/Hfuuwzy/ERSbyAI)  
**最后更新**：2026-05-15

---

## 目录

- [1. 工作流总览](#1-工作流总览)
- [2. Git 分支策略](#2-git-分支策略)
- [3. 功能分支命名规范](#3-功能分支命名规范)
- [4. Git Worktree 使用](#4-git-worktree-使用)
- [5. 提交规范](#5-提交规范)
- [6. 代码实现流程](#6-代码实现流程)
- [7. 验证要求](#7-验证要求)
- [8. GitHub PR 流程](#8-github-pr-流程)
- [9. 敏感信息处理](#9-敏感信息处理)
- [10. 任务优先级](#10-任务优先级)

---

## 1. 工作流总览

```
┌─────────────────────────────────────────────────────────────────┐
│                        需求提出                                  │
│                     （用户描述功能需求）                          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        AI 代码分析                               │
│               （AI 分析代码库、识别影响范围）                      │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        输出 Plan                                 │
│           （AI 输出实现计划，包含：影响文件、验证方式、风险）         │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        用户确认                                  │
│            （用户确认 Plan，或提出修改意见）                       │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     创建功能分支 + Worktree                       │
│           （AI 创建 feature/xxx 分支和独立 worktree）             │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        AI 实现代码                               │
│                    （AI 在 worktree 中编码）                      │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        本地验证                                  │
│           （后端 mvn compile / 前端 npm run build）                │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        Git 提交                                  │
│                   （AI 执行 git commit）                          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Push 到 GitHub                              │
│                 （AI 执行 git push）                              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      创建 Pull Request                           │
│              （AI 使用 gh CLI 创建 PR）                          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        PR Review                                 │
│                （用户 Review，或要求修改）                        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        合并到 main                               │
│            （用户确认后合并，AI 不擅自合并）                       │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. Git 分支策略

### 2.1 主分支保护

| 规则 | 说明 |
|------|------|
| `main` 永远可运行 | 所有提交到 `main` 的代码必须能通过编译和构建 |
| 不在 `main` 直接开发 | 所有功能通过 feature 分支实现 |
| 功能分支完成后合并 | 通过 PR 合并到 `main` |
| 禁止强推 `main` | 不使用 `git push --force` 到 `main` |

### 2.2 分支结构

```
main
  ├── feature/phase1-security
  ├── feature/backend-ai-upgrade
  ├── feature/ui-modernization
  ├── feature/docker-ci
  ├── fix/api-key-config
  └── docs/ai-coding-workflow
```

---

## 3. 功能分支命名规范

### 3.1 命名格式

```
<type>/<description>
```

### 3.2 类型说明

| 类型 | 用途 | 示例 |
|------|------|------|
| `feature/` | 新功能开发 | `feature/phase1-security` |
| `fix/` | Bug 修复 | `fix/api-key-config` |
| `refactor/` | 代码重构 | `refactor/user-cf-algorithm` |
| `docs/` | 文档更新 | `docs/ai-coding-workflow` |
| `chore/` | 工程化配置 | `chore/ci-setup` |
| `test/` | 测试相关 | `test/user-service-unit` |
| `style/` | 代码样式 | `style/frontend-formatting` |

### 3.3 命名建议

- 使用小写字母
- 使用连字符 `-` 分隔单词
- 描述简洁清晰
- 避免过长的分支名

**✅ 好示例**：
- `feature/ai-resume-parser`
- `fix/cors-configuration`
- `docs/api-documentation`

**❌ 坏示例**：
- `feature/NewFunction`（大小写混合）
- `fix_bug`（使用下划线）
- `feature/phase1`（太模糊）

---

## 4. Git Worktree 使用

### 4.1 什么是 Worktree

Git worktree 允许在同一个仓库中同时检出多个分支，每个分支有独立的工作目录。

**优点**：
- 每个功能独立目录，不污染主工作区
- 可以并行开发多个功能
- 切换分支无需 `git stash`
- 出问题容易删除和重建

### 4.2 Worktree 目录结构

```
ErsbyAI/                          # 主工作区
  ├── springboot/
  ├── vue/
  ├── docs/
  └── ...
  
ErsbyAI/.worktrees/               # worktree 目录（已加入 .gitignore）
  ├── phase1-security/            # Phase 1 功能分支
  │   ├── springboot/
  │   ├── vue/
  │   └── ...
  ├── backend-ai-upgrade/         # AI 升级分支
  └── ui-modernization/           # UI 升级分支
```

### 4.3 Worktree 创建命令

```bash
# 创建 worktree 并同时创建新分支
git worktree add .worktrees/<branch-name> -b <branch-name>

# 示例
git worktree add .worktrees/feature-phase1 -b feature/phase1-security
```

### 4.4 Worktree 删除命令

```bash
# 删除 worktree
git worktree remove .worktrees/<branch-name>

# 如果 worktree 有未提交更改，强制删除
git worktree remove -f .worktrees/<branch-name>

# 删除后清理分支
git branch -D <branch-name>
```

---

## 5. 提交规范

### 5.1 Conventional Commits 格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### 5.2 Type（类型）

| 类型 | 说明 |
|------|------|
| `feat` | 新功能 |
| `fix` | 修复 Bug |
| `docs` | 文档更新 |
| `style` | 代码格式（不影响功能） |
| `refactor` | 重构（既不修复 bug 也不添加功能） |
| `perf` | 性能优化 |
| `test` | 添加测试 |
| `chore` | 构建过程或辅助工具的变动 |
| `ci` | CI 配置 |

### 5.3 Scope（范围）

可选，表示影响的模块：

| Scope | 说明 |
|-------|------|
| `security` | 安全配置 |
| `ai` | AI 相关 |
| `backend` | 后端通用 |
| `frontend` | 前端通用 |
| `api` | API 接口 |
| `db` | 数据库 |
| `config` | 配置 |
| `ci` | CI/CD |
| `docs` | 文档 |

### 5.4 Subject（主题）

- 使用祈使句，现在时
- 首字母小写
- 结尾不加句号
- 不超过 50 个字符

**✅ 好示例**：
```
feat(ai): add resume PDF parser
fix(security): move API key to environment config
refactor(backend): simplify token validation logic
docs(workflow): add AI coding workflow guide
chore(ci): add GitHub Actions pipeline
```

**❌ 坏示例**：
```
feat: Added new feature          # 过去时
fix: fixed bug                   # 小写开头
refactor: refactoring code       # 无意义描述
```

### 5.5 Body（正文）

可选，详细说明：
- 为什么要做这个更改
- 与之前行为的对比
- 不明显的副作用

### 5.6 Footer（脚注）

可选，用于：
- 关闭 Issue：`Closes #123`
- 破坏性变更说明：`BREAKING CHANGE: ...`

---

## 6. 代码实现流程

### 6.1 开始前的检查清单

每次开始功能前，AI 必须执行：

```bash
# 1. 检查 Git 状态
git status

# 2. 确认 remote
git remote -v
# 期望输出：origin  https://github.com/Hfuuwzy/ERSbyAI.git (fetch/push)

# 3. 获取最新代码
git fetch origin

# 4. 检查 main 分支状态
git log --oneline -5
```

### 6.2 创建功能分支

```bash
# 从 main 创建新分支
git checkout main
git pull origin main
git checkout -b feature/<name>
```

或使用 worktree：

```bash
git worktree add .worktrees/<name> -b feature/<name>
cd .worktrees/<name>
```

### 6.3 实现过程

1. **先分析，后编码**
   - 识别所有影响文件
   - 评估风险
   - 确定验证方式

2. **小步快跑**
   - 每个独立功能一个 commit
   - 避免一个 commit 做太多事

3. **及时验证**
   - 每完成一个功能单元就验证
   - 不等到最后再验证

### 6.4 提交代码

```bash
# 查看修改
git status
git diff

# 添加文件（逐个添加，看清每个文件）
git add <file1>
git add <file2>

# 提交
git commit -m "type(scope): description"
```

### 6.5 推送到 GitHub

```bash
# 首次推送
git push -u origin feature/<name>

# 后续推送
git push
```

---

## 7. 验证要求

### 7.1 后端验证

```bash
cd springboot

# 清理并编译
mvn clean compile

# 期望输出：
# [INFO] BUILD SUCCESS
# 无编译错误
# 无新增警告（或记录已有警告）
```

### 7.2 前端验证

```bash
cd vue

# 构建
npm run build

# 期望输出：
# dist/ 目录生成
# 无编译错误
# 无关键 warning
```

### 7.3 测试验证（如有测试）

```bash
# 后端测试
cd springboot
mvn test

# 前端测试（如已配置）
cd vue
npm run test
```

### 7.4 验证失败处理

如果验证失败：
1. 停止提交
2. 修复问题
3. 重新验证
4. 修复完成后一起提交

**禁止**：在验证失败的情况下说"完成"

---

## 8. GitHub PR 流程

### 8.1 创建 PR 的时机

功能完成并验证通过后，立即创建 PR。

### 8.2 PR 标题格式

```
<type>: <description>

# 示例
feat: implement AI resume PDF parser
fix: resolve CORS configuration issue
docs: add AI coding workflow documentation
```

### 8.3 PR 描述模板

```markdown
## 变更描述
<!-- 描述本次 PR 做了什么，为什么做 -->

## 影响范围
<!-- 列出修改的文件、影响的模块 -->

## 测试情况
- [ ] 本地编译通过（后端）
- [ ] 本地构建通过（前端）
- [ ] 手动测试步骤：
  1. ...
  2. ...

## 破坏性变更
- [ ] 无
- [ ] 有（请说明）

## 相关 Issue
Closes #123
```

### 8.4 PR 创建命令

```bash
# 使用 GitHub CLI 创建 PR
gh pr create --title "type: description" --body "PR body"

# 示例
gh pr create --title "feat: add AI resume PDF parser" --body "实现了PDF简历解析功能，集成Apache PDFBox。"
```

### 8.5 PR 合并规则

| 场景 | 操作 |
|------|------|
| 用户说"可以合并" | AI 执行合并 |
| 用户说"Review 一下" | 等待用户 Review |
| 用户未明确说 | AI 只创建 PR，不合并 |
| CI 检查失败 | 修复后再请求合并 |

### 8.6 PR 合并命令

```bash
# 合并 PR
gh pr merge <pr-number> --squash --delete-branch

# 或交互式
gh pr merge
```

---

## 9. 敏感信息处理

### 9.1 绝不提交的内容

```
❌ 真实 API Key
❌ 数据库密码
❌ GitHub Token / SSH 私钥
❌ 服务器地址和端口
❌ 用户隐私数据
❌ 日志文件
❌ 上传的用户文件
```

### 9.2 处理方式

| 场景 | 处理方式 |
|------|----------|
| API Key | 使用环境变量或配置文件，代码中使用占位符 |
| 数据库密码 | 使用 `application-local.yml`，模板放在 `.env.example` |
| 上传文件 | 放在 `files/` 目录，已加入 `.gitignore` |
| 日志文件 | 放在 `logs/` 目录，已加入 `.gitignore` |

### 9.3 环境变量示例

`.env.example`：
```bash
# AI 配置
AI_API_KEY=your_api_key_here

# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=xm-job
DB_USER=root
DB_PASSWORD=your_password_here
```

### 9.4 配置注入示例

`application.yml`：
```yaml
ai:
  api-key: ${AI_API_KEY:}
  model: deepseek-r1-250120

db:
  password: ${DB_PASSWORD:}
```

---

## 10. 任务优先级

### 10.1 优先级定义

| 优先级 | 说明 | 响应时间 |
|--------|------|----------|
| **Critical** | 阻塞性问题，系统无法运行 | 立即处理 |
| **High** | 重要功能，影响主要流程 | 本阶段优先 |
| **Medium** | 优化项，提升体验 | 有资源时处理 |
| **Low** | 锦上添花，可延后 | 最后处理 |

### 10.2 当前任务优先级

```
Phase 1: 修复环境与安全问题（API Key外移、配置优化）
├── 1.1 API Key 迁移到配置          [Critical]
├── 1.2 CORS 安全配置              [High]
├── 1.3 修复 Lombok 警告            [Low]
└── 1.4 多环境配置模板              [Medium]

Phase 2: 后端功能增强
├── 2.1 AI 能力升级                 [High]
├── 2.2 简历 PDF 解析               [High]
├── 2.3 推荐算法优化                 [Medium]
├── 2.4 全局异常处理                 [Medium]
├── 2.5 OpenAPI 文档                 [Medium]
└── 2.6 单元测试覆盖                 [High]

Phase 3: 前端 UI 升级
├── 3.1 响应式布局                   [High]
├── 3.2 路由重构                     [Medium]
├── 3.3 状态管理升级                 [Medium]
├── 3.4 加载体验优化                 [Low]
└── 3.5 深色模式                     [Low]

Phase 4: 新功能开发
├── 4.1 实时消息通知                 [High]
├── 4.2 数据可视化增强               [Medium]
├── 4.3 批量操作                     [Medium]
├── 4.4 搜索增强                     [Medium]
└── 4.5 操作日志审计                 [Low]

Phase 5: 工程化与 DevOps
├── 5.1 Docker 容器化               [High]
├── 5.2 GitHub Actions CI/CD        [High]
├── 5.3 代码质量门禁                 [Medium]
├── 5.4 自动化测试                   [Medium]
└── 5.5 部署文档                     [Low]
```

---

## 附录：常用命令速查

### Git 基础

```bash
# 查看状态
git status

# 查看修改
git diff
git diff --staged

# 添加文件
git add <file>
git add -p              # 交互式添加

# 提交
git commit -m "message"
git commit --amend      # 修改上次提交

# 查看历史
git log --oneline -20
git log --graph --oneline
```

### 分支操作

```bash
# 查看分支
git branch
git branch -r           # 远程分支
git branch -a           # 所有分支

# 切换分支
git checkout <branch>
git switch <branch>

# 创建分支
git checkout -b <branch>
git switch -c <branch>

# 删除分支
git branch -d <branch>  # 已合并
git branch -D <branch>  # 强制删除
```

### Worktree 操作

```bash
# 列出 worktree
git worktree list

# 创建 worktree
git worktree add <path> -b <branch>

# 删除 worktree
git worktree remove <path>

# 清理 worktree
git worktree prune
```

### 远程操作

```bash
# 查看 remote
git remote -v

# 获取更新
git fetch origin

# 拉取并合并
git pull origin main

# 拉取并变基（推荐）
git pull --rebase origin main

# 推送
git push origin <branch>
git push -u origin <branch>  # 首次推送
```

### GitHub CLI

```bash
# 查看 PR 列表
gh pr list

# 创建 PR
gh pr create --title "..." --body "..."

# 查看 PR
gh pr view
gh pr view <number>

# 合并 PR
gh pr merge <number> --squash

# 查看 Actions
gh run list
gh run view <run-id>
```

---

## 变更记录

| 日期 | 版本 | 变更内容 |
|------|------|----------|
| 2026-05-15 | v1.0 | 初始版本，定义完整工作流 |

---

**文档维护者**：AI Coding Assistant  
**Review 周期**：每个 Phase 完成后更新
