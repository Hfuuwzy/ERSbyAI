/* biome-ignore-all assist/source/organizeImports */
<template>
  <span class="tag-skill" :class="tagClass">{{ label }}</span>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({ label: { type: String, required: true } });

const tagClass = computed(() => {
	const l = props.label.toLowerCase();
	if (
		l.includes("前端") ||
		l.includes("vue") ||
		l.includes("react") ||
		l.includes("js") ||
		l.includes("css") ||
		l.includes("web")
	)
		return "tag-blue";
	if (
		l.includes("java") ||
		l.includes("后端") ||
		l.includes("python") ||
		l.includes("go") ||
		l.includes("spring") ||
		l.includes("node")
	)
		return "tag-purple";
	if (
		l.includes("产品") ||
		l.includes("ui") ||
		l.includes("设计") ||
		l.includes("ux")
	)
		return "tag-pink";
	if (l.includes("运营") || l.includes("市场") || l.includes("销售"))
		return "tag-orange";
	return "tag-gray";
});

void tagClass;
</script>

<style scoped>
.tag-skill {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 9999px;
  font-size: 12px;
  font-weight: 500;
}
.tag-blue { background: rgba(59, 130, 246, 0.1); color: #3b82f6; }
.tag-purple { background: rgba(118, 75, 162, 0.1); color: #764ba2; }
.tag-pink { background: rgba(240, 147, 251, 0.1); color: #d946ef; }
.tag-orange { background: rgba(245, 158, 11, 0.1); color: #f59e0b; }
.tag-gray { background: rgba(148, 163, 184, 0.1); color: #64748b; }
</style>
