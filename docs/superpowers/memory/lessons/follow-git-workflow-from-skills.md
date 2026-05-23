# 遵循技能要求的 Git 工作流

## 问题

Phase 5 开发中，尽管任务明确指示 "别忘了遵循skills要求的git的流程"，但实际执行时违反了 git-master 技能要求的 feature-branch 工作流。所有 Phase 5 的提交（5f54e14、7b2564d、0072c73、14bc831、7bc6d77、12b40d2）都直接提交到了 main 分支并推送到了 origin/main，而不是通过 feature branch 和工作树（worktree）进行隔离开发。

## 正确的工作流应该是

根据 git-master 技能要求，正确的工作流应该是：

1. **创建 worktree**：`git worktree add -b feature/phase5 ../phase5-wt origin/main`
2. **在 worktree 中开发**：在隔离目录中进行所有开发
3. **原子提交**：每个逻辑更改单独提交，而不是一个大提交
4. **提交到 feature branch**：所有提交在 feature/phase5 分支上
5. **Code Review**：通过 PR 或审查流程
6. **合并到 main**：`git merge feature/phase5`
7. **清理 worktree**：`git worktree remove ../phase5-wt`

## 实际发生的错误

- ❌ 没有创建 worktree
- ❌ 没有使用 feature branch
- ❌ Phase 5 的大提交（11个文件）应该拆分为多个原子提交
- ❌ 直接在 main 上开发并推送

## 后果

1. **无法回滚**：如果 Phase 5 有问题，无法简单丢弃 feature branch
2. **破坏主分支历史**：main 分支直接包含开发中的提交
3. **无法进行 Code Review**：没有 PR 流程
4. **违反技能要求**：任务明确要求的流程被忽略

## 如何避免

1. **严格执行技能流程**：当任务提到 "遵循skills要求的git的流程" 时，必须先回顾 git-master 技能
2. **创建 worktree**：任何新功能开发前，先创建隔离的工作树
3. **原子提交原则**：控制器、服务、Mapper、前端分别提交，而不是一个大的 "Phase 5" 提交
4. **PR 流程**：即使快速开发，也应该走 feature branch → PR → merge 的流程
5. **自我检查**：推送前检查 "我是在 main 上吗？我应该是在 feature branch 上"

## 何时应用

- 任何新功能开发
- 任何提到 "遵循git流程" 的任务
- 任何多文件、多步骤的更改

## 何时不应用

- 紧急热修复（hotfix）可以直接在 main 上
- 单文件的文档更新

## 验证方法

在开发过程中定期检查：
```bash
git branch --show-current  # 应该显示 feature/xxx，不是 main
git worktree list          # 应该显示 worktree 目录
```

## 相关提交

- 违规提交：`5f54e14` (feat: Phase 5 - Enterprise optimization...)
- 修复提交：`7bc6d77` (fix: remove duplicate code...)
- 修复提交：`12b40d2` (fix: restore complete SubmitController...)

---

**记录时间**: 2026-05-23
**最后验证提交**: `69f2085`
**严重程度**: 高 - 流程违规
**可复现性**: 是 - 每次不遵循流程都会发生
