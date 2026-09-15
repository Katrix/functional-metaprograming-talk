<template>
  <div class="h-full flex flex-col">
    <Breadcrumbs />

    <div class="slidev-layout default flex-1 min-h-0">
      <slot />
    </div>

    <PageCounter />
  </div>
</template>

<script setup lang="ts">
import Breadcrumbs from '../components/Breadcrumbs.vue'
import PageCounter from '../components/PageCounter.vue'
</script>
