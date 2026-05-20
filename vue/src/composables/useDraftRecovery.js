import { ElMessageBox } from 'element-plus'
import { ref } from 'vue'
import { useAutoSave } from './useAutoSave'

export function useDraftRecovery(userId) {
  const showRecoveryDialog = ref(false)
  const draftData = ref(null)
  const autoSave = useAutoSave(userId)

  const checkAndPromptRecovery = async (currentResumeId, serverTimestamp = 0) => {
    const draft = autoSave.loadDraft()

    if (!draft) {
      draftData.value = null
      return {
        hasDraft: false,
        shouldRecover: false,
        data: null,
      }
    }

    const sameResume = currentResumeId == null || draft.resumeId == null || String(draft.resumeId) === String(currentResumeId)
    const isNewerThanServer = !serverTimestamp || (draft.timestamp || 0) > serverTimestamp

    draftData.value = draft.data ?? null

    if (!sameResume || !isNewerThanServer) {
      return {
        hasDraft: true,
        shouldRecover: false,
        data: draftData.value,
      }
    }

    showRecoveryDialog.value = true

    try {
      await ElMessageBox.confirm(
        '检测到本地草稿，是否恢复未保存内容？',
        '恢复草稿',
        {
          confirmButtonText: '恢复草稿',
          cancelButtonText: '放弃草稿',
          distinguishCancelAndClose: true,
          type: 'warning',
        },
      )

      return {
        hasDraft: true,
        shouldRecover: true,
        data: draftData.value,
      }
    } catch {
      return {
        hasDraft: true,
        shouldRecover: false,
        data: draftData.value,
      }
    } finally {
      showRecoveryDialog.value = false
    }
  }

  return {
    showRecoveryDialog,
    draftData,
    checkAndPromptRecovery,
  }
}
