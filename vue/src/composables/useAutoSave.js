import { computed, ref } from 'vue'

function getDraftKey(userId) {
  return `resume_draft_${userId}`
}

function isQuotaExceededError(error) {
  return error instanceof DOMException && (
    error.name === 'QuotaExceededError' ||
    error.name === 'NS_ERROR_DOM_QUOTA_REACHED'
  )
}

function cloneData(data) {
  if (data == null) return data
  try {
    return structuredClone(data)
  } catch {
    return JSON.parse(JSON.stringify(data))
  }
}

function formatMinutesAgo(timestamp) {
  const diff = Date.now() - timestamp
  const minutes = Math.max(0, Math.floor(diff / 60000))
  return `${minutes}分钟`
}

export function useAutoSave(userId) {
  const lastSaveTime = ref(null)
  const isSaving = ref(false)
  const needsSave = ref(false)
  const saveError = ref('')

  const draftKey = computed(() => getDraftKey(userId))

  const statusText = computed(() => {
    if (isSaving.value) {
      return '保存中...'
    }

    if (lastSaveTime.value) {
      return `最后自动保存: ${formatMinutesAgo(lastSaveTime.value)}`
    }

    return needsSave.value ? '有未保存更改' : ''
  })

  const readStorage = () => {
    if (typeof window === 'undefined') return null
    return window.localStorage
  }

  const saveDraft = (data, resumeId) => {
    const storage = readStorage()
    if (!storage) return false

    isSaving.value = true
    saveError.value = ''

    const payload = {
      resumeId: resumeId ?? null,
      data: cloneData(data),
      timestamp: Date.now(),
    }

    try {
      storage.setItem(draftKey.value, JSON.stringify(payload))
      lastSaveTime.value = payload.timestamp
      needsSave.value = false
      return true
    } catch (error) {
      if (isQuotaExceededError(error)) {
        saveError.value = '本地存储空间不足，草稿未能保存'
      } else {
        saveError.value = error?.message || '草稿保存失败'
      }
      needsSave.value = true
      return false
    } finally {
      isSaving.value = false
    }
  }

  const loadDraft = () => {
    const storage = readStorage()
    if (!storage) return null

    try {
      const raw = storage.getItem(draftKey.value)
      return raw ? JSON.parse(raw) : null
    } catch {
      return null
    }
  }

  const clearDraft = () => {
    const storage = readStorage()
    if (!storage) return

    storage.removeItem(draftKey.value)
    lastSaveTime.value = null
    needsSave.value = false
    saveError.value = ''
  }

  const hasDraft = () => {
    const storage = readStorage()
    if (!storage) return false

    return Boolean(storage.getItem(draftKey.value))
  }

  const markNeedsSave = () => {
    needsSave.value = true
  }

  return {
    lastSaveTime,
    isSaving,
    needsSave,
    saveError,
    statusText,
    saveDraft,
    loadDraft,
    clearDraft,
    hasDraft,
    markNeedsSave,
  }
}
