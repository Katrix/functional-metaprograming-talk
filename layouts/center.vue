<template>
  <div class="h-full flex flex-col">
    <Breadcrumbs />
    <div class="slidev-layout center flex-1 min-h-0 grid place-content-center">
      <div class="my-auto">
        <slot />
      </div>
    </div>

    <PageCounter />
  </div>
</template>

<script setup lang="ts">
import Breadcrumbs from '../components/Breadcrumbs.vue'
import PageCounter from '../components/PageCounter.vue'
</script>
