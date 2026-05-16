<template>
  <div class="glass-card" :class="{ 'hover-lift': liftable }">
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
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
  padding: 24px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.glass-card:hover { box-shadow: var(--shadow-lg); }
</style>
