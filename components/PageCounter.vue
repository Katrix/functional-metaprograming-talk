<template>
  <span class="page-count">
    {{ sectionPageNo }} / {{ sectionSlidesCount }}
  </span>
</template>

<script setup lang="ts">
import { useNav, useSlideContext } from '@slidev/client'
import { computed } from 'vue'

const nav = useNav()

const { $page } = useSlideContext()

function findSectionSlide(start: number, cond: (i: number) => boolean, mod: number) {
  for (let i = start; cond(i); i += mod) {
    const fm = nav.slides.value[i]?.meta?.slide?.frontmatter ?? {}

    if (fm.section) {
      return nav.slides.value[i]
    }
  }

  return null
}

const startSectionSlide = computed(() => findSectionSlide($page.value - 1, (i) => i >= 0, -1))
const nextSectionSlide = computed(() => findSectionSlide($page.value, (i) => i < nav.slides.value.length, 1))

const sectionStartSlideNo = computed(() => startSectionSlide.value?.no ?? 1)
const sectionEndSlideNo = computed(() =>
  nextSectionSlide.value?.no ? nextSectionSlide.value?.no - 1 : nav.total.value,
)

const sectionSlidesCount = computed(() => sectionEndSlideNo.value - sectionStartSlideNo.value + 1)

const sectionPageNo = computed(() => $page.value - sectionStartSlideNo.value + 1)
</script>
