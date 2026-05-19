<template>
  <el-button type="primary" @click="handleExport">导出 PDF</el-button>
</template>

<script setup>
import { ElMessage } from 'element-plus'

const props = defineProps({
  resumeData: {
    type: Object,
    default: () => ({})
  }
})

const escapeHtml = (value) => {
  return String(value ?? '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;')
}

const buildSection = (title, items, renderItem, emptyText) => {
  if (!items?.length) {
    return `<div class="section"><h3>${title}</h3><p class="empty">${emptyText}</p></div>`
  }

  return `
    <div class="section">
      <h3>${title}</h3>
      ${items.map(renderItem).join('')}
    </div>
  `
}

const buildHtml = (resumeData) => {
  const name = escapeHtml(resumeData.username || resumeData.name || '未命名简历')
  const avatar = escapeHtml(resumeData.userAvatar || '')
  const baseInfo = [
    ['姓名', resumeData.username],
    ['联系电话', resumeData.phone],
    ['邮箱', resumeData.email],
    ['学历', resumeData.education],
    ['工作年限', resumeData.experience],
    ['期望薪资', resumeData.salary]
  ]
    .map(([label, value]) => `<div class="meta-item"><span>${label}</span><strong>${escapeHtml(value || '-')}</strong></div>`)
    .join('')

  const eduSection = buildSection(
    '教育经历',
    resumeData.eduExpList,
    (item) => `<div class="item"><strong>${escapeHtml(item.school || '-')}</strong><span>${escapeHtml(item.speciality || '-')}（${escapeHtml(item.education || '-') }）</span><p>${escapeHtml(item.course || '暂无主修课程')}</p></div>`,
    '暂无教育经历'
  )

  const workSection = buildSection(
    '工作经历',
    resumeData.workExpList,
    (item) => `<div class="item"><strong>${escapeHtml(item.employ || '-')}</strong><span>${escapeHtml(item.position || '-')}（${escapeHtml(item.type || '-') }）</span><p>${escapeHtml(item.project || '项目')}${item.content ? `：${escapeHtml(item.content)}` : ''}</p></div>`,
    '暂无工作经历'
  )

  const projectSection = buildSection(
    '项目经历',
    resumeData.proExpList,
    (item) => `<div class="item"><strong>${escapeHtml(item.name || '-')}</strong><span>${escapeHtml(item.start || '-')} ~ ${escapeHtml(item.end || '至今')}</span><p>${escapeHtml(item.content || '暂无项目介绍')}</p></div>`,
    '暂无项目经历'
  )

  return `
    <!doctype html>
    <html lang="zh-CN">
      <head>
        <meta charset="UTF-8" />
        <title>${name} - 简历导出</title>
        <style>
          body { font-family: Arial, sans-serif; margin: 0; padding: 32px; color: #1f2937; }
          .sheet { max-width: 860px; margin: 0 auto; }
          .header { display: flex; gap: 24px; align-items: center; margin-bottom: 28px; }
          .avatar { width: 96px; height: 96px; border-radius: 12px; object-fit: cover; background: #f3f4f6; }
          .title { margin: 0; font-size: 28px; }
          .grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 10px 18px; margin: 18px 0 24px; }
          .meta-item { display: flex; justify-content: space-between; gap: 12px; padding: 10px 12px; background: #f9fafb; border-radius: 10px; }
          .section { margin-top: 24px; }
          .section h3 { margin: 0 0 12px; padding-bottom: 8px; border-bottom: 2px solid #dbeafe; }
          .item { padding: 12px 14px; margin-bottom: 12px; border: 1px solid #e5e7eb; border-radius: 10px; }
          .item strong { display: block; margin-bottom: 4px; }
          .item span, .item p, .empty { color: #6b7280; }
          .item p { margin: 6px 0 0; white-space: pre-wrap; }
        </style>
      </head>
      <body>
        <div class="sheet">
          <div class="header">
            ${avatar ? `<img class="avatar" src="${avatar}" alt="avatar" />` : '<div class="avatar"></div>'}
            <div>
              <h1 class="title">${name}</h1>
              <div class="grid">${baseInfo}</div>
            </div>
          </div>
          ${eduSection}
          ${workSection}
          ${projectSection}
        </div>
      </body>
    </html>
  `
}

const handleExport = () => {
  const resumeData = props.resumeData || {}

  if (!resumeData.username && !resumeData.phone) {
    ElMessage.warning('简历信息为空，请至少填写姓名和联系方式')
    return
  }

  const printWindow = window.open('', '_blank', 'width=960,height=720')

  if (!printWindow) {
    ElMessage.warning('浏览器已拦截弹窗，请允许后重试')
    return
  }

  printWindow.document.open()
  printWindow.document.write(buildHtml(resumeData))
  printWindow.document.close()

  setTimeout(() => {
    printWindow.focus()
    printWindow.print()
    printWindow.close()
  }, 300)
}

defineExpose({ handleExport })
</script>
