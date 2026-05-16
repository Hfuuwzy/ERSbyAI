<template>
  <button class="gradient-button" :class="{ 'secondary': variant === 'secondary' }" @click="$emit('click')">
    <slot></slot>
  </button>
</template>

<script setup>
defineProps({ variant: { type: String, default: 'primary' } })
defineEmits(['click'])
</script>

<style scoped>
.gradient-button {
  padding: 12px 24px;
  border: none;
  border-radius: var(--radius-sm);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  background: var(--gradient-button);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}
.gradient-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}
.gradient-button.secondary {
  background: white;
  color: var(--text-primary);
  border: 1px solid var(--border-light);
  box-shadow: var(--shadow-sm);
}
.gradient-button.secondary:hover {
  background: rgba(102, 126, 234, 0.05);
  border-color: var(--color-primary);
}
</style>
