<template>
  <div class="slide-preview" :class="{ missing: !slide }" @click="go">
    <SlideContainer v-if="slide" :no="slide.no" :width="width" class="pointer-events-none">
      <SlideWrapper :clicks-context="clicksContext!" :route="slide" render-context="overview" />
    </SlideContainer>
    <div v-else class="placeholder" :style="{ width: `${width}px`, height: `${width / slideAspect}px` }">
      Not written yet
    </div>
    <div class="label">{{ label ?? slide?.meta?.slide?.frontmatter.section ?? to }}</div>
  </div>
</template>

<script setup lang="ts">
import { useNav } from '@slidev/client'
import { createFixedClicks } from '@slidev/client/composables/useClicks.ts'
import { slideAspect } from '@slidev/client/env.ts'
import SlideContainer from '@slidev/client/internals/SlideContainer.vue'
import SlideWrapper from '@slidev/client/internals/SlideWrapper.vue'
import { computed } from 'vue'

const props = withDefaults(
  defineProps<{
    to: string | number
    width?: number
    label?: string
  }>(),
  { width: 300 },
)

const nav = useNav()

const slide = computed(() =>
  nav.slides.value.find(s => s.no === Number(props.to) || s.meta.slide?.frontmatter.routeAlias === props.to),
)

// Show the slide as it looks when first arriving at it
const clicksContext = computed(() => (slide.value ? createFixedClicks(slide.value, 0) : undefined))

function go() {
  if (slide.value) {
    nav.go(slide.value.no)
  }
}
</script>

<style scoped>
.slide-preview {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.4rem;
  cursor: pointer;
}

.slide-preview > :first-child {
  border: 1px solid var(--slidev-theme-primary);
  border-radius: 4px;
  overflow: hidden;
  transition: transform 0.15s, box-shadow 0.15s;
}

.slide-preview:hover > :first-child {
  transform: scale(1.03);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.slide-preview.missing {
  cursor: default;
  opacity: 0.6;
}

.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  font-style: italic;
  border-style: dashed !important;
}

.label {
  font-size: 0.9rem;
}
</style>