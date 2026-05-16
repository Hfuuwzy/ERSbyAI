<template>
  <div class="glass-card" :class="{ 'liftable': liftable }">
    <slot></slot>
  </div>
</template>

<script setup>
defineProps({
  liftable: { type: Boolean, default: true }
})
</script>

<style scoped>
.glass-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  padding: 24px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.glass-card.liftable:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-hover);
}
</style>
