# Git 工作流程指南

> 本项目已初始化Git仓库，并完成了首次提交。本指南帮助你熟悉Git工作流程。

---

## 📊 当前状态

```
仓库状态: ✅ 已初始化
当前分支: master
提交数量: 4
文件数量: 132
```

**提交历史：**
```
559351e chore: add database schema and seed data
df1771f feat: add Vue 3 frontend with Element Plus
920f5f9 feat: add Spring Boot backend with AI resume screening
7e573c9 chore: add gitignore for Java/Vue project
```

---

## 🚀 连接到GitHub（下一步操作）

### 1. 在GitHub创建仓库

访问 https://github.com/new 创建新仓库：
- **Repository name**: `85boss-job` (或其他你喜欢的名字)
- **Description**: 全栈智能招聘平台，集成AI简历筛选 + 协同过滤推荐
- **Public/Private**: 选择 Public（如果你想开源）或 Private
- **Initialize**: ❌ 不要勾选（我们已有本地仓库）

### 2. 连接本地仓库到GitHub

在本地执行以下命令：

```bash
# 添加GitHub远程仓库（替换YOUR_USERNAME为你的GitHub用户名）
git remote add origin https://github.com/YOUR_USERNAME/85boss-job.git

# 验证远程仓库
git remote -v

# 推送代码到GitHub
git branch -M main        # 将主分支重命名为main（GitHub推荐）
git push -u origin main    # 首次推送并建立追踪关系
```

### 3. 验证推送成功

访问 `https://github.com/YOUR_USERNAME/85boss-job` 确认代码已上传。

---

## 🌿 日常开发工作流程

### 场景1：开始新功能开发

```bash
# 1. 确保在最新代码基础上开发
git checkout main
git pull origin main

# 2. 创建功能分支（推荐命名：feature/xxx 或 你的姓名缩写-功能）
git checkout -b feature/ai-optimization
# 或
git checkout -b wzy/resume-parser

# 3. 开始编码...
```

### 场景2：开发中提交代码

```bash
# 1. 查看修改了哪些文件
git status

# 2. 添加要提交的文件（逐个添加，不要 git add .）
git add springboot/src/main/java/com/example/utils/AiUtil.java
git add springboot/src/main/java/com/example/service/ResumeService.java

# 3. 提交（遵循提交规范）
git commit -m "feat: add AI resume parser for PDF files"

# 4. 继续开发其他功能，再次提交
git add springboot/src/main/resources/mapper/ResumeMapper.xml
git commit -m "feat: add MyBatis XML for resume parser"
```

### 场景3：功能完成，推送到GitHub

```bash
# 1. 获取远程最新代码（防止冲突）
git checkout feature/ai-optimization
git fetch origin
git rebase origin/main

# 2. 如果有冲突，解决后继续
# git add <conflicted-files>
# git rebase --continue

# 3. 推送到GitHub
git push -u origin feature/ai-optimization

# 4. 在GitHub上创建Pull Request，请求合并到main
```

---

## 📝 提交规范（必看）

我们使用 **Conventional Commits** 规范，这会让你的提交历史非常清晰。

### 提交格式

```
<type>: <description>

[optional body]

[optional footer]
```

### 常用类型

| 类型 | 用途 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat: add user registration` |
| `fix` | 修复Bug | `fix: correct JWT token expiration` |
| `refactor` | 重构代码 | `refactor: simplify UserCF algorithm` |
| `docs` | 文档更新 | `docs: update README with setup guide` |
| `chore` | 杂项/配置 | `chore: add gitignore rules` |
| `style` | 代码样式 | `style: fix indentation in Login.vue` |
| `test` | 测试相关 | `test: add unit tests for UserService` |

### 示例提交

```bash
# 好示例 ✅
git commit -m "feat: add AI resume scoring API" -m "- Integrate DeepSeek R1 via VolcEngine" -m "- Add ai_score and ai_review fields"

git commit -m "fix: resolve CORS issue in production" -m "Allow requests from https://85boss.com"

git commit -m "refactor: extract JWT auth logic to interceptor"

# 坏示例 ❌
git commit -m "update"              # 太模糊
git commit -m "fix bug"              # 没说清楚什么bug
git commit -m "WIP"                  # 不要提交半成品
```

---

## 🔄 常见Git操作速查

### 基础操作

```bash
# 查看状态
git status

# 查看修改内容
git diff
git diff --staged    # 已暂存的修改

# 查看提交历史
git log --oneline -20
git log --graph --oneline

# 查看某文件的修改历史
git log -p -- springboot/src/main/java/com/example/utils/AiUtil.java
```

### 撤销操作

```bash
# 撤销工作区的修改（未add）
git restore vue/src/views/Login.vue

# 撤销暂存区的修改（已add未commit）
git restore --staged springboot/pom.xml

# 修改最后一次提交
git commit --amend

# 回退到某个版本（保留修改）
git reset --soft HEAD~1

# 回退到某个版本（丢弃修改）⚠️危险
git reset --hard abc1234
```

### 分支操作

```bash
# 查看分支
git branch              # 本地分支
git branch -r           # 远程分支
git branch -a           # 所有分支

# 切换分支
git checkout main
git switch feature/xxx

# 删除分支
git branch -d feature/xxx       # 已合并的分支
git branch -D feature/xxx       # 强制删除

# 合并分支
git checkout main
git merge feature/xxx

# 变基（保持线性历史）
git checkout feature/xxx
git rebase main
```

### 远程操作

```bash
# 获取远程更新（不合并）
git fetch origin

# 拉取并合并
git pull origin main

# 拉取并变基（推荐）
git pull --rebase origin main

# 推送
git push origin main

# 强制推送（⚠️谨慎使用）
git push --force-with-lease origin main
```

---

## 🛡️ 重要安全提示

### ⚠️ 永远不要提交敏感信息

`.gitignore` 已配置排除以下文件：
- 环境变量文件 (`.env`)
- 依赖目录 (`node_modules/`, `target/`)
- IDE配置 (`.idea/`, `.vscode/`)
- 上传文件 (`files/`)

### 🔐 当前需要手动处理的安全问题

**问题1**: `AiUtil.java` 中硬编码了API Key
```java
// springboot/src/main/java/com/example/utils/AiUtil.java
String apiKey = "eb9000bd-1357-4635-a826-f11920f596ef";  // ❌
```

**解决方案**：
1. 创建 `application-local.yml`（已加入.gitignore）
2. 将API Key移到环境变量或配置文件

```java
// 更好的做法
@Value("${ai.api-key}")
private String apiKey;
```

---

## 🎯 AI Coding练习建议

作为AI Coding学习项目，建议按以下顺序练习：

### Phase 1: 代码质量 (Week 1)
- [ ] 添加单元测试 (JUnit + Mockito)
- [ ] 配置代码检查工具 (ESLint, Spotless)
- [ ] 修复安全漏洞 (API Key外移)

### Phase 2: 功能增强 (Week 2)
- [ ] AI能力增强 (OCR简历解析)
- [ ] 推荐算法优化 (从UserCF到混合推荐)
- [ ] 添加消息通知 (WebSocket)

### Phase 3: 工程化 (Week 3)
- [ ] Docker容器化
- [ ] CI/CD流水线 (GitHub Actions)
- [ ] 自动化测试

---

## 📚 学习资源

### Git官方文档
- [Git Book 中文版](https://git-scm.com/book/zh/v2)
- [GitHub Git Cheat Sheet](https://education.github.com/git-cheat-sheet-education.pdf)

### 推荐练习
- [Learn Git Branching](https://learngitbranching.js.org/?locale=zh_CN) - 可视化学习
- [Oh Shit, Git!?!](https://ohshitgit.com/) - 常见错误修复

---

## 💡 常用命令速记

```bash
# 每日工作流
git checkout main && git pull origin main
git checkout -b feature/xxx
# ...编码...
git add <files>
git commit -m "feat: xxx"
git push -u origin feature/xxx
# GitHub上创建PR

# 紧急情况
git stash                    # 临时保存修改
git stash pop               # 恢复修改
git cherry-pick abc1234     # 挑拣某个提交
```

---

## 🎉 下一步行动清单

1. ✅ **初始化Git仓库** - 已完成
2. ⬜ **创建GitHub仓库** - 需要你手动操作
3. ⬜ **推送代码到GitHub** - 连接后执行
4. ⬜ **创建第一个分支** - 练习分支操作
5. ⬜ **提交第一个PR** - 体验完整流程

准备好后告诉我，我可以指导你完成GitHub连接！

---

*最后更新: 2025-05-14 | 项目: 85Boss智能招聘平台*
