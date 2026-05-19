<template>
  <el-dialog title="导出PDF" v-model="dialogVisible" width="900px" destroy-on-close>
    <div class="template-selector">
      <el-radio-group v-model="selectedTemplate">
        <el-radio-button label="simple">简约</el-radio-button>
        <el-radio-button label="professional">专业</el-radio-button>
        <el-radio-button label="creative">创意</el-radio-button>
      </el-radio-group>
    </div>

    <div class="preview-container">
      <div class="preview-header">
        <span>实时预览</span>
        <span class="preview-tag">{{ templateLabel }}</span>
      </div>

      <div class="preview-frame">
        <ResumePdfTemplate :data="resumeData" :templateType="selectedTemplate" />
      </div>
    </div>

    <template #footer>
      <el-button @click="closeDialog">取消</el-button>
      <el-button type="primary" :loading="exporting" @click="exportPDF">下载PDF</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { computed, ref } from 'vue'
import ResumePdfTemplate from './ResumePdfTemplate.vue'

const props = defineProps({
  resumeData: {
    type: Object,
    default: () => ({})
  },
  visible: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:visible'])

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const selectedTemplate = ref('simple')
const exporting = ref(false)

const templateLabel = computed(() => {
  const labels = {
    simple: '简约',
    professional: '专业',
    creative: '创意'
  }
  return labels[selectedTemplate.value] || '简约'
})

const pdfFileName = computed(() => {
  const name = String(props.resumeData?.username || '').trim()
  return name ? `${name}.pdf` : '我的简历.pdf'
})

const closeDialog = () => {
  dialogVisible.value = false
}

const exportPDF = async () => {
  try {
    const target = document.getElementById('resume-template')
    if (!target) {
      ElMessage.error('未找到可导出的简历内容')
      return
    }

    exporting.value = true
    const html2pdfModule = await import('html2pdf.js')
    const html2pdf = html2pdfModule.default || html2pdfModule

    await html2pdf()
      .set({
        margin: 0,
        filename: pdfFileName.value,
        image: { type: 'jpeg', quality: 0.98 },
        html2canvas: { scale: 2, useCORS: true },
        jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
      })
      .from(target)
      .save()
  } catch (error) {
    console.error('PDF导出失败:', error)
    ElMessage.error('PDF导出失败，请重试')
  } finally {
    exporting.value = false
  }
}

void [templateLabel, closeDialog, exportPDF, ResumePdfTemplate]
</script>

<style scoped>
.template-selector {
  margin-bottom: 16px;
}

.preview-container {
  padding: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  background: #f8fafc;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  color: #374151;
}

.preview-tag {
  padding: 4px 10px;
  border-radius: 999px;
  background: #e0e7ff;
  color: #4338ca;
  font-size: 12px;
}

.preview-frame {
  max-height: 70vh;
  overflow: auto;
  padding: 12px;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: inset 0 0 0 1px rgba(229, 231, 235, 0.9);
}
</style>
