<template>
  <div :class="['resume-template', `template-${templateType}`]" id="resume-template">
    <template v-if="templateType === 'professional'">
      <div class="professional-layout">
        <aside class="professional-sidebar">
          <div class="avatar-placeholder">
            <span>{{ (displayValue(data?.username, '').trim() || 'R').slice(0, 1).toUpperCase() }}</span>
          </div>

          <div class="sidebar-block">
            <h2 class="name-title">{{ displayValue(data?.username, '姓名') }}</h2>
            <p class="subtitle">{{ displayValue(data?.education, '学历信息') }}</p>
          </div>

          <div class="sidebar-block contact-stack">
            <div class="contact-item"><span>电话</span><strong>{{ displayValue(data?.phone) }}</strong></div>
            <div class="contact-item"><span>邮箱</span><strong>{{ displayValue(data?.email) }}</strong></div>
            <div class="contact-item"><span>性别</span><strong>{{ displayValue(data?.sex) }}</strong></div>
          </div>
        </aside>

        <main class="professional-content">
          <section class="section-card highlight-card">
            <div class="section-header">
              <h3>求职概览</h3>
              <span class="salary-pill">{{ displayValue(data?.salary, '薪资期望') }}</span>
            </div>
            <p class="overview-text">{{ displayValue(data?.experience, '暂无工作经验描述') }}</p>
          </section>

          <section class="section-card">
            <h3>基础信息</h3>
            <div class="detail-list">
              <div class="detail-row"><span>姓名</span><strong>{{ displayValue(data?.username) }}</strong></div>
              <div class="detail-row"><span>性别</span><strong>{{ displayValue(data?.sex) }}</strong></div>
              <div class="detail-row"><span>学历</span><strong>{{ displayValue(data?.education) }}</strong></div>
              <div class="detail-row"><span>薪资</span><strong>{{ displayValue(data?.salary) }}</strong></div>
            </div>
          </section>

          <section class="section-card">
            <h3>联系方式</h3>
            <div class="detail-list">
              <div class="detail-row"><span>电话</span><strong>{{ displayValue(data?.phone) }}</strong></div>
              <div class="detail-row"><span>邮箱</span><strong>{{ displayValue(data?.email) }}</strong></div>
            </div>
          </section>
        </main>
      </div>
    </template>

    <template v-else-if="templateType === 'creative'">
      <header class="creative-header">
        <div class="creative-header__content">
          <p class="eyebrow">Resume Template</p>
          <h1>{{ displayValue(data?.username, '个人简历') }}</h1>
          <p class="creative-summary">{{ displayValue(data?.experience, '用简洁有力的经历描述你的价值') }}</p>
        </div>
        <div class="creative-badge">
          <span>{{ (displayValue(data?.username, '').trim() || 'R').slice(0, 1).toUpperCase() }}</span>
        </div>
      </header>

      <section class="creative-grid">
        <article class="info-card accent-card">
          <span class="card-label">薪资期望</span>
          <strong>{{ displayValue(data?.salary) }}</strong>
        </article>
        <article class="info-card">
          <span class="card-label">学历</span>
          <strong>{{ displayValue(data?.education) }}</strong>
        </article>
        <article class="info-card">
          <span class="card-label">性别</span>
          <strong>{{ displayValue(data?.sex) }}</strong>
        </article>
        <article class="info-card wide-card">
          <span class="card-label">工作经历</span>
          <p>{{ displayValue(data?.experience, '暂无工作经历') }}</p>
        </article>
        <article class="info-card">
          <span class="card-label">电话</span>
          <strong>{{ displayValue(data?.phone) }}</strong>
        </article>
        <article class="info-card">
          <span class="card-label">邮箱</span>
          <strong>{{ displayValue(data?.email) }}</strong>
        </article>
      </section>
    </template>

    <template v-else>
      <header class="simple-header">
        <p class="simple-kicker">Resume</p>
        <h1>{{ displayValue(data?.username, '姓名') }}</h1>
        <p class="simple-meta">{{ displayValue(data?.education, '学历信息') }} · {{ displayValue(data?.sex, '性别') }}</p>
      </header>

      <section class="simple-contact">
        <span>{{ displayValue(data?.phone, '电话') }}</span>
        <span>{{ displayValue(data?.email, '邮箱') }}</span>
        <span>{{ displayValue(data?.salary, '薪资期望') }}</span>
      </section>

      <section class="simple-info-grid">
        <div class="simple-info-card">
          <h3>姓名</h3>
          <p>{{ displayValue(data?.username) }}</p>
        </div>
        <div class="simple-info-card">
          <h3>性别</h3>
          <p>{{ displayValue(data?.sex) }}</p>
        </div>
        <div class="simple-info-card">
          <h3>学历</h3>
          <p>{{ displayValue(data?.education) }}</p>
        </div>
        <div class="simple-info-card">
          <h3>薪资</h3>
          <p>{{ displayValue(data?.salary) }}</p>
        </div>
        <div class="simple-info-card span-2">
          <h3>工作经历</h3>
          <p>{{ displayValue(data?.experience, '暂无工作经历描述') }}</p>
        </div>
        <div class="simple-info-card">
          <h3>电话</h3>
          <p>{{ displayValue(data?.phone) }}</p>
        </div>
        <div class="simple-info-card">
          <h3>邮箱</h3>
          <p>{{ displayValue(data?.email) }}</p>
        </div>
      </section>
    </template>
  </div>
</template>

<script setup>

const props = defineProps({
  data: { type: Object, required: true },
  templateType: { type: String, default: 'simple' }
})

const displayValue = (value, fallback = '未填写') => {
  if (value === null || value === undefined || value === '') return fallback
  return String(value)
}

void displayValue(props.data?.username)

</script>

<style scoped>
.resume-template {
  width: 210mm;
  min-height: 297mm;
  margin: 0 auto;
  box-sizing: border-box;
  color: #1f2937;
  background: #ffffff;
  overflow: hidden;
  -webkit-print-color-adjust: exact;
  print-color-adjust: exact;
}

.resume-template * {
  box-sizing: border-box;
}

.simple-header,
.simple-contact,
.simple-info-grid,
.professional-layout,
.creative-header,
.creative-grid {
  width: 100%;
}

.template-simple {
  padding: 18mm 16mm 16mm;
}

.simple-header {
  text-align: center;
  padding-bottom: 10mm;
  border-bottom: 2px solid #e5e7eb;
}

.simple-kicker,
.eyebrow,
.card-label,
.simple-meta,
.subtitle {
  color: #6b7280;
}

.simple-kicker {
  margin: 0 0 4mm;
  font-size: 12px;
  letter-spacing: 0.18em;
  text-transform: uppercase;
}

.simple-header h1,
.creative-header h1,
.name-title {
  margin: 0;
  font-size: 30px;
  line-height: 1.15;
  font-weight: 800;
  color: #111827;
}

.simple-meta {
  margin: 4mm 0 0;
  font-size: 13px;
}

.simple-contact {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px 18px;
  padding: 8mm 0 10mm;
  font-size: 13px;
  color: #374151;
}

.simple-contact span {
  padding: 4px 10px;
  border-radius: 999px;
  background: #f3f4f6;
}

.simple-info-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.simple-info-card {
  min-height: 52mm;
  padding: 14px 16px;
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  background: linear-gradient(180deg, #ffffff 0%, #f9fafb 100%);
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.04);
}

.simple-info-card h3,
.section-card h3 {
  margin: 0 0 8px;
  font-size: 14px;
  line-height: 1.4;
  color: #111827;
}

.simple-info-card p,
.overview-text,
.info-card p,
.detail-row strong,
.contact-item strong {
  margin: 0;
  font-size: 13px;
  line-height: 1.65;
  color: #374151;
  word-break: break-word;
}

.span-2 {
  grid-column: span 2;
}

.template-professional {
  padding: 0;
}

.professional-layout {
  display: grid;
  grid-template-columns: 74mm 1fr;
  min-height: 297mm;
}

.professional-sidebar {
  padding: 16mm 10mm;
  color: #ffffff;
  background: linear-gradient(180deg, #0f172a 0%, #1d4ed8 100%);
}

.avatar-placeholder {
  display: grid;
  place-items: center;
  width: 42mm;
  height: 42mm;
  margin: 0 auto 10mm;
  border: 2px solid rgba(255, 255, 255, 0.7);
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.12);
  font-size: 28px;
  font-weight: 800;
}

.sidebar-block + .sidebar-block {
  margin-top: 10mm;
}

.subtitle {
  margin: 4px 0 0;
  color: rgba(255, 255, 255, 0.78);
  font-size: 13px;
}

.contact-stack {
  display: grid;
  gap: 10px;
}

.contact-item {
  padding: 10px 12px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.14);
}

.contact-item span,
.detail-row span {
  display: block;
  margin-bottom: 2px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.72);
}

.contact-item strong {
  color: #ffffff;
}

.professional-content {
  padding: 16mm 14mm;
  background: #f8fafc;
}

.section-card {
  padding: 16px;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05);
}

.section-card + .section-card {
  margin-top: 12px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
  margin-bottom: 10px;
}

.section-header h3 {
  margin: 0;
}

.salary-pill {
  flex-shrink: 0;
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  color: #1d4ed8;
  background: rgba(29, 78, 216, 0.1);
}

.detail-list {
  display: grid;
  gap: 10px;
}

.detail-row {
  padding: 10px 12px;
  border-radius: 12px;
  background: #f8fafc;
}

.detail-row span {
  color: #6b7280;
}

.highlight-card {
  border-left: 4px solid #2563eb;
}

.template-creative {
  padding: 16mm;
  background: linear-gradient(180deg, #ffffff 0%, #f8fafc 100%);
}

.creative-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  padding: 18px 20px;
  border-radius: 22px;
  color: #ffffff;
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 50%, #f59e0b 100%);
  box-shadow: 0 14px 32px rgba(139, 92, 246, 0.22);
}

.eyebrow {
  margin: 0 0 8px;
  font-size: 12px;
  letter-spacing: 0.16em;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.82);
}

.creative-summary {
  max-width: 118mm;
  margin: 10px 0 0;
  font-size: 13px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.9);
}

.creative-badge {
  display: grid;
  place-items: center;
  width: 36mm;
  height: 36mm;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.22);
  font-size: 28px;
  font-weight: 800;
}

.creative-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.info-card {
  padding: 16px;
  border-radius: 18px;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.05);
}

.accent-card {
  background: linear-gradient(180deg, rgba(139, 92, 246, 0.12), rgba(236, 72, 153, 0.08));
  border-color: rgba(139, 92, 246, 0.18);
}

.wide-card {
  grid-column: span 2;
}

.card-label {
  display: block;
  margin-bottom: 6px;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.info-card strong {
  font-size: 16px;
  color: #111827;
}

.resume-template p {
  white-space: pre-wrap;
}

@media print {
  .resume-template {
    margin: 0;
    border-radius: 0;
    box-shadow: none;
  }

  .template-simple,
  .template-creative {
    padding: 0;
  }

  .professional-layout,
  .professional-sidebar,
  .professional-content,
  .template-creative,
  .template-simple {
    break-inside: avoid;
    page-break-inside: avoid;
  }
}
</style>
