import { ElMessage } from 'element-plus'

const isQuotaExceededError = (error) => {
  return (
    error?.name === 'QuotaExceededError'
    || error?.name === 'NS_ERROR_DOM_QUOTA_REACHED'
    || error?.code === 22
    || error?.code === 1014
  )
}

export function useAutoSave(storageKey) {
  const resolveKey = () => (typeof storageKey === 'function' ? storageKey() : storageKey)

  const save = (payload) => {
    try {
      localStorage.setItem(resolveKey(), JSON.stringify(payload))
      return { success: true }
    } catch (error) {
      if (isQuotaExceededError(error)) {
        ElMessage.warning('存储空间不足')
        return { success: false, error: '存储空间不足' }
      }

      ElMessage.warning('自动保存失败')
      return { success: false, error: '自动保存失败' }
    }
  }

  const load = () => {
    try {
      const raw = localStorage.getItem(resolveKey())
      return raw ? JSON.parse(raw) : null
    } catch {
      return null
    }
  }

  const clear = () => {
    localStorage.removeItem(resolveKey())
  }

  return {
    save,
    load,
    clear
  }
}
