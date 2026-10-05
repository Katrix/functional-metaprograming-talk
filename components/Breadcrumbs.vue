<template>
  <div class="breadcrumbs">
    <div class="band-1">{{ $slidev.configs.title }}</div>
    <div class="band-2">
      <span v-if="section">└─ {{ section }}</span>
      <span v-else>&nbsp;</span>
    </div>
    <div class="band-3">
      <span v-if="subsection">└─ {{ subsection }}</span>
      <span v-else>&nbsp;</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useNav, useSlideContext } from '@slidev/client'
import { computed } from 'vue'

const nav = useNav()
const { $slidev } = useSlideContext()

function lookBack(key: 'section' | 'subsection') {
  for (let i = nav.currentSlideNo.value - 1; i >= 0; i--) {
    const fm = nav.slides.value[i]?.meta?.slide?.frontmatter ?? {}

    // We only look for subsections until we hit a section
    if (fm.section !== undefined && key === 'subsection' && !fm.subsection) return ''
    if (fm[key]) return fm[key]
  }
  return ''
}
const section = computed(() => lookBack('section'))
const subsection = computed(() => lookBack('subsection'))
</script>
