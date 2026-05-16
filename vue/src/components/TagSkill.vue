<template>
  <span class="tag-skill" :class="variant" @click="$emit('click', $event)">
    {{ label }}
  </span>
</template>

<script setup>
defineProps({
  label: { type: String, required: true },
  variant: { type: String, default: 'default' }
})
defineEmits(['click'])
</script>

<style scoped>
.tag-skill {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  margin: 2px 4px 2px 0;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 500;
  background: rgba(102, 126, 234, 0.08);
  color: var(--color-primary);
  border: 1px solid rgba(102, 126, 234, 0.18);
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
}
.tag-skill:hover {
  background: rgba(102, 126, 234, 0.16);
  border-color: var(--color-primary);
  transform: translateY(-1px);
}
.tag-skill.muted {
  background: rgba(100, 116, 139, 0.08);
  color: var(--text-secondary);
  border-color: var(--border-light);
}
.tag-skill.muted:hover {
  background: rgba(100, 116, 139, 0.14);
}
</style>
